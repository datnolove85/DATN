import { ref, computed, onMounted, nextTick, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import { getAllVoucher } from '@/service/VoucherService'
import { getSanPhamChiTietById } from '@/service/SanPhamChiTiet'
import { taoHoaDonOnline } from '@/service/HoaDonService'
import stompClient from '@/socket'
import { getProvinces, getDistricts, getWards, getShippingFee } from '@/service/GHNService'
import {
  getAllDiaChi,
  themDiaChi,
  capNhatDiaChi,
  xoaDiaChi,
  doiMacDinh,
} from '@/service/DiaChiKhachHangService'
import L from 'leaflet'
import gamificationApi from '@/service/clientGamificationService'
import 'leaflet/dist/leaflet.css'
import icon from 'leaflet/dist/images/marker-icon.png'
import shadow from 'leaflet/dist/images/marker-shadow.png'
import { detailKhachHangService } from '@/service/KhachHangService'
import { cauHinhService } from '@/service/cauHinhService'

export function useCheckout() {
  const toast = useToast()
  const route = useRoute()
  const router = useRouter()

  const authToken = sessionStorage.getItem('token')
  const isLoggedIn = Boolean(authToken)

  // State hệ thống & khách hàng
  const systemConfig = ref({})
  const customerInfo = ref(null)
  const loading = ref(false)
  const isPlacingOrder = ref(false)
  const shippingLoading = ref(false)
  const shippingFee = ref(0)
  const note = ref('')

  // State Xu & Giảm giá
  const soXuCanDung = ref(0)
  const soXuSuDung = ref(0)
  const tienGiamDoXu = ref(0)

  // State sản phẩm & giỏ hàng
  const product = ref(null)
  const checkoutItems = ref([])
  const isCartCheckout = ref(false)
  const quantity = ref(Number(route.query.qty) || 1)

  const spctId = computed(() => {
    return route.query.spct ? Number(route.query.spct) : null
  })

  // State Địa chỉ
  const addresses = ref([])
  const selectedAddressId = ref(null)
  const showAllAddresses = ref(false)
  const showAddressModal = ref(false)
  const editingAddress = ref(null)

  // GHN Location state
  const provinces = ref([])
  const districts = ref([])
  const wards = ref([])
  const wardCache = ref([])
  const selectedProvince = ref(null)
  const selectedDistrict = ref(null)
  const selectedWard = ref(null)

  // Voucher state
  const vouchers = ref([])
  const selectedVoucherId = ref(null)
  const showVoucherModal = ref(false)

  // Leaflet Map instances
  let map = null
  let marker = null

  // --- CONFIG & LOAD DATA ---
  const loadSystemConfig = async () => {
    try {
      const data = await cauHinhService.getCauHinhHeThong()
      systemConfig.value = data.reduce((acc, item) => {
        acc[item.maCauHinh] = item.giaTriSo
        return acc
      }, {})
    } catch (error) {
      console.error('Lỗi load config:', error)
    }
  }

  // --- XU LOGIC ---
  const apDungSoXu = () => {
    const tyLeQuyDoi = Number(systemConfig.value['TY_LE_QUY_DOI_XU'] || 1000)
    const tyLeGiamToiDa = Number(systemConfig.value['TY_LE_GIAM_TOI_DA_XU'] || 50)
    const soDuHienTai = customerInfo.value?.soDuXu || 0

    let soXuMuonDung = Number(soXuCanDung.value)

    if (!soXuMuonDung || soXuMuonDung === 0) {
      huyBoSoXu()
      return
    }

    if (soXuMuonDung < 0) {
      toast.warning('Số xu sử dụng không hợp lệ!')
      soXuCanDung.value = 0
      return
    }

    if (soDuHienTai < soXuMuonDung) {
      toast.warning(`Số dư xu của quý khách không đủ (Hiện có: ${soDuHienTai} xu)`)
      soXuCanDung.value = soDuHienTai
      soXuMuonDung = soDuHienTai
    }

    const tienGiamToiDaTheoPhanTram = (subtotal.value * tyLeGiamToiDa) / 100
    const maxXuTheoPhanTram = Math.floor(tienGiamToiDaTheoPhanTram / tyLeQuyDoi)
    const maxXuTheoTongTien = Math.floor(subtotal.value / tyLeQuyDoi)

    if (soXuMuonDung > 0 && maxXuTheoPhanTram === 0) {
      toast.error('Đơn hàng quá nhỏ, không đủ điều kiện để sử dụng xu!')
      soXuCanDung.value = 0
      soXuSuDung.value = 0
      tienGiamDoXu.value = 0
      return
    }

    let xuThucTeSuDung = Math.min(soXuMuonDung, soDuHienTai)
    xuThucTeSuDung = Math.min(xuThucTeSuDung, maxXuTheoPhanTram)
    xuThucTeSuDung = Math.min(xuThucTeSuDung, maxXuTheoTongTien)

    soXuCanDung.value = xuThucTeSuDung
    soXuSuDung.value = xuThucTeSuDung
    tienGiamDoXu.value = xuThucTeSuDung * tyLeQuyDoi

    toast.success(`Đã áp dụng ${soXuSuDung.value} xu (Giảm ${formatMoney(tienGiamDoXu.value)})`)
  }

  const huyBoSoXu = () => {
    soXuCanDung.value = 0
    soXuSuDung.value = 0
    tienGiamDoXu.value = 0
    toast.info('Đã hủy sử dụng xu.')
  }

  // --- QUANTITY & CART LOGIC ---
  const maxAvailable = computed(() => {
    if (!product.value) return 0
    return product.value.soLuongKhaDung ?? product.value.soLuongTon ?? 0
  })

  const increaseCartQty = (item) => {
    const max = item.soLuongKhaDung ?? item.soLuongTon ?? 0
    if (max === 0) return
    if (item.quantity < max) {
      item.quantity++
    } else {
      toast.warning(`Sản phẩm này chỉ còn ${max} sản phẩm khả dụng`)
    }
  }

  const decreaseCartQty = (item) => {
    if (item.quantity > 1) {
      item.quantity--
    }
  }

  // --- VOUCHER LOGIC ---
  const fetchVouchers = async () => {
    const voucherHeThong = (await getAllVoucher())
      .filter((v) => v.trangThai === 1)
      .map((v) => ({
        ...v,
        uid: `sys_${v.id}`,
        loaiVoucher: 'HE_THONG',
      }))

    let voucherCaNhan = []
    const currentUser = JSON.parse(sessionStorage.getItem('user'))

    if (currentUser?.idKhachHang) {
      try {
        const res = await gamificationApi.getVoucherCuaToi(currentUser.idKhachHang)
        voucherCaNhan = (res.data || [])
          .filter((v) => v.trangThai === 'CHUA_DUNG')
          .map((v) => ({
            id: v.idKhoVoucher,
            idVoucherKhachHang: v.idVoucherKhachHang,
            tenVoucher: v.tenVoucher,
            maVoucher: v.maCode,
            giaTriDonHangToiThieu: v.dieuKienToiThieu,
            giaTriGiam: v.giaTriGiam,
            giaTriGiamToiDa: v.giaTriGiamToiDa,
            loaiGiamGia: v.loaiGiamGia,
            ngayBatDau: v.ngayBatDau,
            ngayKetThuc: v.ngayHetHan,
            moTa: v.moTa,
            trangThai: 1,
            loaiVoucher: 'CA_NHAN',
            uid: `per_${v.idVoucherKhachHang || v.idKhoVoucher}`,
          }))
      } catch (e) {
        console.error('Lỗi tải voucher cá nhân:', e)
      }
    }

    vouchers.value = [...voucherHeThong, ...voucherCaNhan]
  }

  const selectedVoucher = computed(() => {
    return vouchers.value.find((v) => v.uid === selectedVoucherId.value) || null
  })

  const getVoucherDiscount = (voucher) => {
    if (!voucher || subtotal.value < voucher.giaTriDonHangToiThieu) return 0
    if (voucher.loaiGiamGia === 'tien_mat') {
      return Number(voucher.giaTriGiam)
    }
    let discount = (subtotal.value * Number(voucher.giaTriGiam)) / 100
    if (voucher.giaTriGiamToiDa) {
      discount = Math.min(discount, Number(voucher.giaTriGiamToiDa))
    }
    return discount
  }

  // --- COMPUTED TOTALS ---
  const subtotal = computed(() => {
    if (isCartCheckout.value) {
      return checkoutItems.value.reduce((sum, item) => sum + item.giaBan * item.quantity, 0)
    }
    return (product.value?.giaSauGiam || product.value?.giaBan || 0) * quantity.value
  })

  const voucherDiscount = computed(() => {
    const v = selectedVoucher.value
    if (!v || subtotal.value < v.giaTriDonHangToiThieu) return 0
    return v.loaiGiamGia === 'phan_tram'
      ? Math.min(
          (subtotal.value * v.giaTriGiam) / 100,
          v.giaTriGiamToiDa ?? Number.POSITIVE_INFINITY,
        )
      : v.giaTriGiam
  })

  const total = computed(() =>
    Math.max(subtotal.value + shippingFee.value - voucherDiscount.value - tienGiamDoXu.value, 0),
  )

  const formatMoney = (value) => Number(value || 0).toLocaleString('vi-VN') + ' đ'
  const formatCurrency = (value) => Number(value || 0).toLocaleString('vi-VN') + 'đ'

  // --- ADDRESS & GHN LOGIC ---
  const addressForm = ref({
    tenNguoiNhan: '',
    soDienThoai: '',
    thanhPho: '',
    quan: '',
    phuong: '',
    diaChiCuThe: '',
    districtId: null,
    wardCode: '',
    latitude: null,
    longitude: null,
    macDinh: false,
  })

  const calculateShipping = async (address) => {
    if (!address.thanhPho || !address.quan || !address.phuong) {
      shippingFee.value = 0
      return
    }
    try {
      shippingLoading.value = true
      const fee = await getShippingFee({
        thanhPho: address.thanhPho,
        quan: address.quan,
        phuong: address.phuong,
      })
      shippingFee.value = fee
    } catch (error) {
      console.error('Lỗi tính ship', error)
      toast.error('Không thể tính phí vận chuyển cho địa chỉ này')
      shippingFee.value = 0
    } finally {
      shippingLoading.value = false
    }
  }

  const selectAddress = (id) => {
    selectedAddressId.value = id
    showAllAddresses.value = false
  }

  const loadAddresses = async () => {
    if (!isLoggedIn) return
    try {
      addresses.value = await getAllDiaChi()
      const selected = addresses.value.find((item) => item.macDinh) || addresses.value[0]
      selectedAddressId.value = selected?.id ?? null
    } catch (e) {
      console.error(e)
      toast.error('Không tải được địa chỉ')
    }
  }

  const openAddAddress = () => {
    resetAddressForm()
    if (addresses.value.length >= 3) {
      toast.warning('Bạn chỉ được lưu tối đa 3 địa chỉ')
      return
    }
    editingAddress.value = null
    showAddressModal.value = true
  }

  const openEditAddress = (item) => {
    editingAddress.value = item
    addressForm.value = { ...item }
    showAddressModal.value = true
  }

  const resetAddressForm = () => {
    addressForm.value = {
      tenNguoiNhan: '',
      soDienThoai: '',
      thanhPho: '',
      quan: '',
      phuong: '',
      diaChiCuThe: '',
      districtId: null,
      wardCode: '',
      latitude: null,
      longitude: null,
      macDinh: false,
    }
    editingAddress.value = null
  }

  const deleteAddress = async (id) => {
    if (!confirm('Xóa địa chỉ này?')) return
    try {
      await xoaDiaChi(id)
      toast.success('Đã xóa')
      await loadAddresses()
    } catch (e) {
      toast.error(e.message)
    }
  }

  const setDefault = async (id) => {
    try {
      await doiMacDinh(id)
      toast.success('Đã đổi địa chỉ mặc định')
      await loadAddresses()
    } catch (e) {
      toast.error(e.message)
    }
  }

  const onProvinceChangeId = async (provinceId) => {
    selectedProvince.value = provinces.value.find((p) => p.ProvinceID == provinceId) || null
    await onProvinceChange()
  }

  const onDistrictChangeId = async (districtId) => {
    selectedDistrict.value = districts.value.find((d) => d.DistrictID == districtId) || null
    await onDistrictChange()
  }

  const onWardChangeId = async (wardCode) => {
    selectedWard.value = wards.value.find((w) => w.WardCode == wardCode) || null
    await onWardChange()
  }

  const onProvinceChange = async () => {
    selectedDistrict.value = null
    selectedWard.value = null
    districts.value = []
    wards.value = []
    shippingFee.value = 0
    addressForm.value.quan = ''
    addressForm.value.phuong = ''
    addressForm.value.districtId = null
    addressForm.value.wardCode = ''

    if (!selectedProvince.value) {
      addressForm.value.thanhPho = ''
      return
    }
    addressForm.value.thanhPho = selectedProvince.value.ProvinceName
    districts.value = await getDistricts(selectedProvince.value.ProvinceID)
  }

  const onDistrictChange = async () => {
    selectedWard.value = null
    wards.value = []
    shippingFee.value = 0
    addressForm.value.phuong = ''
    addressForm.value.wardCode = ''

    if (!selectedDistrict.value) {
      addressForm.value.quan = ''
      addressForm.value.districtId = null
      return
    }
    addressForm.value.quan = selectedDistrict.value.DistrictName
    addressForm.value.districtId = selectedDistrict.value.DistrictID
    wards.value = await getWards(selectedDistrict.value.DistrictID)
  }

  const onWardChange = async () => {
    shippingFee.value = 0
    if (!selectedWard.value) {
      addressForm.value.phuong = ''
      addressForm.value.wardCode = ''
      return
    }
    addressForm.value.phuong = selectedWard.value.WardName
    addressForm.value.wardCode = selectedWard.value.WardCode
    if (!isLoggedIn) {
      await calculateShipping(addressForm.value)
    }
  }

  // --- MAP & GEOLOCATION ---
  const initMap = () => {
    if (map) {
      map.remove()
      map = null
    }
    map = L.map('map').setView([21.0285, 105.8542], 13)
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap',
    }).addTo(map)

    if (editingAddress.value && editingAddress.value.latitude) {
      addMarker(editingAddress.value.latitude, editingAddress.value.longitude)
    }
  }

  const addMarker = (lat, lng) => {
    if (marker) map.removeLayer(marker)
    marker = L.marker([lat, lng], {
      icon: L.icon({
        iconUrl: icon,
        shadowUrl: shadow,
        iconSize: [25, 41],
        iconAnchor: [12, 41],
      }),
    }).addTo(map)
    map.setView([lat, lng], 16)
  }

  const getWardsCached = async (districtId) => {
    if (wardCache[districtId]) return wardCache[districtId]
    const data = await getWards(districtId)
    wardCache[districtId] = data
    return data
  }

  const getCurrentLocation = () => {
    loading.value = true
    navigator.geolocation.getCurrentPosition(
      async (pos) => {
        try {
          const { latitude, longitude } = pos.coords
          addressForm.value.latitude = latitude
          addressForm.value.longitude = longitude
          map.setView([latitude, longitude], 16)
          if (marker) map.removeLayer(marker)
          marker = L.marker([latitude, longitude], {
            icon: L.icon({
              iconUrl: icon,
              shadowUrl: shadow,
              iconSize: [25, 41],
              iconAnchor: [12, 41],
            }),
          }).addTo(map)

          const response = await fetch(
            `https://nominatim.openstreetmap.org/reverse?format=json&addressdetails=1&zoom=18&lat=${latitude}&lon=${longitude}`,
          )
          const data = await response.json()
          if (!data.address) return
          const a = data.address

          addressForm.value.diaChiCuThe =
            [a.house_number, a.road, a.neighbourhood, a.residential, a.quarter]
              .filter(Boolean)
              .join(', ') ||
            a.road ||
            ''

          const cleanText = (str) =>
            str
              .replace(/^(thành phố|tỉnh|quận|huyện|thị xã|phường|xã|thị trấn)\s+/i, '')
              .trim()
              .toLowerCase()

          const targetProvince = cleanText(a.state || a.province || a.region || '')
          const targetDistrict = cleanText(
            a.county || a.city_district || a.district || a.municipality || a.city || '',
          )
          const targetWard = cleanText(
            a.suburb || a.ward || a.town || a.village || a.neighbourhood || '',
          )

          const foundProvince = provinces.value.find((p) =>
            cleanText(p.ProvinceName).includes(targetProvince),
          )
          if (!foundProvince) {
            toast.warning('Không tự động xác định được Tỉnh/Thành phố.')
            return
          }

          selectedProvince.value = foundProvince
          addressForm.value.thanhPho = foundProvince.ProvinceName
          districts.value = await getDistricts(foundProvince.ProvinceID)

          const foundDistrict = districts.value.find((d) =>
            cleanText(d.DistrictName).includes(targetDistrict),
          )
          if (foundDistrict) {
            selectedDistrict.value = foundDistrict
            addressForm.value.quan = foundDistrict.DistrictName
            addressForm.value.districtId = foundDistrict.DistrictID
            wards.value = await getWardsCached(foundDistrict.DistrictID)

            const foundWard = wards.value.find((w) => cleanText(w.WardName).includes(targetWard))
            if (foundWard) {
              selectedWard.value = foundWard
              addressForm.value.phuong = foundWard.WardName
              addressForm.value.wardCode = foundWard.WardCode
            }
          }
          toast.success('Đã định vị thành công!')
        } catch (error) {
          console.error(error)
          toast.error('Không thể phân giải vị trí.')
        } finally {
          loading.value = false
        }
      },
      () => {
        toast.error('Vui lòng cấp quyền vị trí.')
        loading.value = false
      },
      { enableHighAccuracy: true },
    )
  }

  // --- PLACE ORDER LOGIC ---
  const validateGuestCheckout = () => {
    if (!addressForm.value.tenNguoiNhan.trim()) {
      toast.warning('Vui lòng nhập họ tên người nhận')
      return false
    }
    if (!/^[0-9]{9,11}$/.test(addressForm.value.soDienThoai.trim())) {
      toast.warning('Số điện thoại phải gồm 9 đến 11 chữ số')
      return false
    }
    if (
      !addressForm.value.thanhPho ||
      !addressForm.value.quan ||
      !addressForm.value.phuong ||
      !addressForm.value.diaChiCuThe.trim()
    ) {
      toast.warning('Vui lòng nhập đầy đủ địa chỉ giao hàng')
      return false
    }
    return true
  }

  const placeOrder = async () => {
    if (isPlacingOrder.value) return
    if (isLoggedIn && !selectedAddressId.value) {
      toast.warning('Vui lòng chọn địa chỉ giao hàng')
      return
    }
    if (!isLoggedIn && !validateGuestCheckout()) return

    const selectedVoucherObj = selectedVoucher.value
    const body = {
      addressId: isLoggedIn ? selectedAddressId.value : null,
      tenNguoiNhan: isLoggedIn ? null : addressForm.value.tenNguoiNhan,
      soDienThoaiNguoiNhan: isLoggedIn ? null : addressForm.value.soDienThoai,
      thanhPho: isLoggedIn ? null : addressForm.value.thanhPho,
      quan: isLoggedIn ? null : addressForm.value.quan,
      phuong: isLoggedIn ? null : addressForm.value.phuong,
      diaChiCuThe: isLoggedIn ? null : addressForm.value.diaChiCuThe,
      districtId: isLoggedIn ? null : addressForm.value.districtId,
      wardCode: isLoggedIn ? null : addressForm.value.wardCode,
      shippingFee: shippingFee.value,
      voucherId:
        selectedVoucherObj?.loaiVoucher === 'CA_NHAN' ? null : (selectedVoucherObj?.id ?? null),
      voucherKhachHangId:
        selectedVoucherObj?.loaiVoucher === 'CA_NHAN'
          ? selectedVoucherObj.idVoucherKhachHang
          : null,
      soXuSuDung: soXuSuDung.value,
      tienGiamDoXu: tienGiamDoXu.value,
      note: note.value,
      items: isCartCheckout.value
        ? checkoutItems.value.map((item) => ({
            productDetailId: item.productDetailId,
            quantity: item.quantity,
          }))
        : [
            {
              productDetailId: spctId.value,
              quantity: quantity.value,
              price: product.value?.giaBan,
            },
          ],
    }

    try {
      isPlacingOrder.value = true
      const res = await taoHoaDonOnline(body, authToken)
      toast.success(`Đặt hàng thành công. Mã đơn: ${res.maHoaDon}`)
      if (isCartCheckout.value) {
        sessionStorage.removeItem('checkoutData')
      }
      sessionStorage.setItem('orderProduct', JSON.stringify(product.value))
      sessionStorage.setItem('lastGuestOrderCode', res.maHoaDon)
      router.push({
        path: '/payment',
        query: { id: res.id, maHoaDon: res.maHoaDon, qrUrl: res.qrUrl },
      })
    } catch (error) {
      toast.error(error.message || 'Đặt hàng thất bại ❌')
    } finally {
      isPlacingOrder.value = false
    }
  }

  // --- LIFECYCLE & SOCKET ---
  const loadData = async () => {
    product.value = await getSanPhamChiTietById(spctId.value)
    const max = product.value.soLuongKhaDung ?? product.value.soLuongTon ?? 0
    if (max === 0) {
      quantity.value = 0
    } else if (quantity.value > max) {
      quantity.value = max
    }
    await fetchVouchers()
    if (isLoggedIn) {
      await loadAddresses()
    }
    provinces.value = await getProvinces()
  }

  const connectSocket = () => {
    if (stompClient.connected) {
      subscribeOrder()
    } else {
      stompClient.onConnect = () => {
        subscribeOrder()
      }
    }
  }

  const subscribeOrder = () => {
    stompClient.subscribe('/topic/pos', async (msg) => {
      const event = JSON.parse(msg.body)
      if (event.type === 'VOUCHER_UPDATED' || event.type === 'KHO_VOUCHER_UPDATED') {
        await fetchVouchers()
      }
    })
  }

  onMounted(async () => {
    loadSystemConfig()
    const userSession = sessionStorage.getItem('user')
    if (userSession) {
      try {
        const user = JSON.parse(userSession)
        customerInfo.value = await detailKhachHangService(user.idKhachHang)
      } catch (error) {
        console.error(error)
      }
    }

    if (spctId.value) {
      isCartCheckout.value = false
      await loadData()
      connectSocket()
      return
    }

    const checkout = sessionStorage.getItem('checkoutData')
    if (checkout) {
      isCartCheckout.value = true
      checkoutItems.value = JSON.parse(checkout).items.map((item) => ({
        ...item,
        quantity: (item.soLuongKhaDung ?? item.soLuongTon ?? 0) === 0 ? 0 : item.quantity || 1,
      }))
      await fetchVouchers()
      if (isLoggedIn) await loadAddresses()
      provinces.value = await getProvinces()
      connectSocket()
      return
    }
    router.push('/')
  })

  watch(showAddressModal, (val) => {
    if (val) {
      nextTick(() => {
        initMap()
        map?.invalidateSize()
      })
    } else {
      if (map) {
        map.remove()
        map = null
      }
    }
  })

  watch(selectedAddressId, async (id) => {
    if (!id) return
    const address = addresses.value.find((x) => x.id === id)
    if (!address) return
    await calculateShipping(address)
  })

  return {
    router,
    isLoggedIn,
    total,
    addresses,
    selectedAddressId,
    addressForm,
    provinces,
    districts,
    wards,
    selectedProvince,
    selectedDistrict,
    selectedWard,
    loading,
    shippingFee,
    checkoutItems,
    showVoucherModal,
    showAddressModal,
    note,
    isPlacingOrder,
    selectAddress,
    openAddAddress,
    openEditAddress,
    deleteAddress,
    setDefault,
    onProvinceChangeId,
    onDistrictChangeId,
    onWardChangeId,
    increaseCartQty,
    decreaseCartQty,
    apDungSoXu,
    placeOrder,
  }
}
