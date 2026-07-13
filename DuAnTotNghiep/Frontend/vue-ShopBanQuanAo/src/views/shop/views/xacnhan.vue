<template>
  <div class="min-h-screen bg-slate-50 py-10">
    <div class="max-w-6xl mx-auto px-6">
      <!-- HEADER -->
      <div class="mb-10 flex items-center justify-between">
        <div class="flex items-center gap-4">
          <div
            class="w-14 h-14 rounded-3xl bg-indigo-600 text-white flex items-center justify-center text-2xl shadow-lg"
          >
            🛒
          </div>
          <div>
            <h1 class="text-3xl font-black text-slate-900">Xác nhận đơn hàng</h1>
            <p class="text-slate-500">Kiểm tra thông tin và hoàn tất giao dịch</p>
          </div>
        </div>
        <!-- Progress Steps -->
        <div class="flex items-center gap-3 text-sm font-bold">
          <div class="text-indigo-600 flex items-center gap-2">
            <span
              class="w-8 h-8 rounded-full bg-indigo-600 text-white flex items-center justify-center"
              >1</span
            >
            Mua hàng
          </div>
          <div class="w-10 h-[2px] bg-indigo-200"></div>
          <div class="text-indigo-600 flex items-center gap-2">
            <span
              class="w-8 h-8 rounded-full bg-indigo-600 text-white flex items-center justify-center"
              >2</span
            >
            Xác nhận
          </div>
          <div class="w-10 h-[2px] bg-slate-200"></div>
          <div class="text-slate-400 flex items-center gap-2">
            <span class="w-8 h-8 rounded-full bg-slate-200 flex items-center justify-center"
              >3</span
            >
            Thanh toán
          </div>
        </div>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <!-- LEFT COLUMN -->
        <div class="lg:col-span-2 space-y-6">
          <!-- ĐỊA CHỈ GIAO HÀNG -->
          <div class="bg-white rounded-2xl shadow-sm border p-6">
            <div class="flex justify-between items-center mb-5">
              <h2 class="font-bold text-lg">Địa chỉ giao hàng</h2>
              <button
                v-if="addresses.length < 3"
                @click="openAddAddress"
                class="bg-indigo-600 hover:bg-indigo-700 text-white px-5 py-2 rounded-xl transition font-medium"
              >
                + Thêm địa chỉ
              </button>
              <div v-else class="text-sm text-slate-500 bg-slate-100 px-4 py-2 rounded-xl">
                Đã đạt tối đa 3 địa chỉ
              </div>
            </div>

            <div
              v-for="item in showAllAddresses ? addresses : addresses.filter((a) => a.macDinh)"
              :key="item.id"
              @click="selectAddress(item.id)"
              class="border rounded-2xl p-5 mb-4 cursor-pointer hover:border-indigo-400 transition-all"
              :class="
                selectedAddressId === item.id
                  ? 'border-indigo-600 bg-indigo-50/50'
                  : 'border-slate-200'
              "
            >
              <label class="flex cursor-pointer">
                <input
                  type="radio"
                  :checked="selectedAddressId === item.id"
                  @change="selectAddress(item.id)"
                  class="mt-1 accent-indigo-600"
                />
                <div class="ml-3 flex-1">
                  <div class="font-bold">
                    {{ item.tenNguoiNhan }}
                    <span
                      v-if="item.macDinh"
                      class="ml-2 text-[10px] bg-green-100 text-green-700 px-2 py-0.5 rounded"
                      >Mặc định</span
                    >
                  </div>
                  <div class="text-sm">{{ item.soDienThoai }}</div>
                  <div class="text-sm text-slate-600">
                    {{ item.diaChiCuThe }}, {{ item.phuong }}, {{ item.quan }}, {{ item.thanhPho }}
                  </div>
                  <div class="mt-2 space-x-3 text-sm">
                    <button @click.stop="openEditAddress(item)" class="text-blue-600 font-medium">
                      Sửa
                    </button>
                    <button @click.stop="deleteAddress(item.id)" class="text-red-600 font-medium">
                      Xóa
                    </button>
                    <button
                      v-if="!item.macDinh"
                      @click.stop="setDefault(item.id)"
                      class="text-green-600 font-medium"
                    >
                      Đặt mặc định
                    </button>
                  </div>
                </div>
              </label>
            </div>
            <button
              v-if="addresses.length > 1"
              @click="showAllAddresses = !showAllAddresses"
              class="w-full mt-2 py-2 text-indigo-600 font-semibold bg-indigo-50 rounded-xl hover:bg-indigo-100"
            >
              {{
                showAllAddresses
                  ? 'Thu gọn địa chỉ'
                  : 'Xem thêm địa chỉ khác (' + (addresses.length - 1) + ')'
              }}
            </button>
          </div>

          <!-- SẢN PHẨM -->
          <div class="bg-white rounded-2xl shadow-sm border p-6">
            <h2 class="font-bold text-lg mb-5">Sản phẩm</h2>
            <div class="flex gap-6 items-start bg-slate-50 rounded-2xl p-4">
              <img
                :src="product?.images?.length ? 'http://localhost:8080' + product.images[0] : ''"
                class="w-28 h-28 rounded-xl object-cover border"
              />
              <div class="flex-1">
                <h3 class="font-bold text-lg">{{ product?.tenSanPham }}</h3>
                <div class="flex gap-4 mt-2 text-sm text-slate-500">
                  <p>
                    Màu: <span class="font-semibold text-slate-800">{{ product?.tenMauSac }}</span>
                  </p>
                  <p>
                    Size:
                    <span class="font-semibold text-slate-800">{{ product?.tenKichThuoc }}</span>
                  </p>
                </div>
                <div class="mt-4 flex justify-between items-center">
                  <div v-if="product?.dangGiamGia" class="flex items-center gap-2">
                    <!-- Giá sau giảm -->
                    <span class="text-red-600 font-bold text-2xl">
                      {{ (product?.giaSauGiam || 0).toLocaleString() }}₫
                    </span>

                    <!-- Giá gốc -->
                    <span class="text-gray-400 line-through">
                      {{ (product?.giaBan || 0).toLocaleString() }}₫
                    </span>
                  </div>

                  <div v-else>
                    <span class="text-2xl font-bold">
                      {{ (product?.giaBan || 0).toLocaleString() }}₫
                    </span>
                  </div>
                  <div class="flex items-center border rounded-lg bg-white">
                    <button class="px-3 py-1 hover:bg-slate-100" @click="decreaseQty">-</button>
                    <span class="px-3 font-bold">{{ quantity }}</span>
                    <button class="px-3 py-1 hover:bg-slate-100" @click="increaseQty">+</button>
                  </div>
                </div>
                <p class="text-[10px] text-slate-400 mt-1">Kho: {{ stock }} sản phẩm sẵn có</p>
              </div>
            </div>
          </div>

          <!-- VẬN CHUYỂN & VOUCHER -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div class="bg-white rounded-2xl shadow-sm border p-6">
              <h2 class="font-bold mb-4">Phương thức vận chuyển</h2>
              <div
                class="border border-indigo-200 bg-indigo-50/50 rounded-xl p-4 flex justify-between"
              >
                <div>
                  <div class="font-semibold">Tiêu chuẩn</div>
                  <div class="text-xs text-slate-500">2-4 ngày làm việc</div>
                </div>
                <div class="font-bold text-indigo-600">{{ shippingFee.toLocaleString() }}đ</div>
              </div>
            </div>
            <div class="bg-white rounded-2xl shadow-sm border p-6">
              <h2 class="font-bold mb-4">Mã ưu đãi (Voucher)</h2>
              <select
                v-model="selectedVoucherId"
                class="w-full bg-slate-50 border rounded-xl p-3 outline-indigo-500"
              >
                <option :value="null">Không áp dụng</option>
                <option
                  v-for="v in vouchers"
                  :key="v.id"
                  :value="v.id"
                  :disabled="subtotal < v.giaTriDonHangToiThieu"
                >
                  {{ v.tenVoucher }} - {{ v.maVoucher }}
                </option>
              </select>
            </div>
          </div>

          <!-- GHI CHÚ -->
          <div class="bg-white rounded-2xl shadow-sm border p-6">
            <h2 class="font-bold mb-2">Ghi chú cho đơn hàng</h2>
            <textarea
              v-model="note"
              class="w-full bg-slate-50 border rounded-xl p-4 resize-none outline-indigo-500"
              placeholder="Ví dụ: Giao hàng giờ hành chính..."
            ></textarea>
          </div>
        </div>

        <!-- RIGHT COLUMN: SUMMARY -->
        <div class="lg:col-span-1">
          <div class="bg-white rounded-2xl shadow-sm border p-6 sticky top-8">
            <h2 class="font-bold text-lg mb-6">Tóm tắt đơn hàng</h2>
            <div class="space-y-4">
              <div class="flex justify-between">
                <span>Tiền hàng</span>
                <span class="font-bold">{{ subtotal.toLocaleString() }}đ</span>
              </div>
              <div class="flex justify-between">
                <span>Phí ship</span>
                <span class="font-bold">{{ shippingFee.toLocaleString() }}đ</span>
              </div>
              <div class="flex justify-between text-emerald-600">
                <span>Voucher giảm</span>
                <span class="font-bold">-{{ voucherDiscount.toLocaleString() }}đ</span>
              </div>
              <div class="border-t pt-4 flex justify-between items-center">
                <span class="font-bold">Tổng thanh toán</span>
                <span class="text-2xl font-black text-indigo-600"
                  >{{ total.toLocaleString() }}đ</span
                >
              </div>
              <button
                class="w-full bg-indigo-600 hover:bg-indigo-700 text-white py-4 rounded-xl font-bold text-lg shadow-lg"
                @click="placeOrder"
              >
                ĐẶT HÀNG NGAY
              </button>
            </div>
          </div>
        </div>
      </div>

      <Teleport to="body">
        <div
          v-if="showAddressModal"
          class="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center z-50 p-4"
        >
          <div
            class="bg-white w-full max-w-[900px] rounded-[32px] p-8 shadow-2xl flex flex-col md:flex-row gap-8"
          >
            <!-- Form Section -->
            <div class="flex-1">
              <h2 class="text-2xl font-bold text-gray-800 mb-6">
                {{ editingAddress ? 'Cập nhật địa chỉ' : 'Thêm địa chỉ mới' }}
              </h2>

              <div class="space-y-4">
                <div class="grid md:grid-cols-2 gap-5">
                  <input
                    v-model="addressForm.tenNguoiNhan"
                    placeholder="Họ và tên"
                    class="w-full border border-gray-300 rounded-xl px-4 py-3 focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition"
                  />
                  <input
                    v-model="addressForm.soDienThoai"
                    placeholder="Số điện thoại"
                    class="w-full border border-gray-200 p-3 rounded-xl focus:ring-2 focus:ring-indigo-500 outline-none transition"
                  />
                </div>
                <div class="space-y-4">
                  <!-- Tỉnh -->

                  <!-- Quận -->

                  <!-- Phường -->
                  <!-- Tỉnh / Thành phố -->
                  <div>
                    <label class="text-sm font-semibold text-gray-700 mb-2 block">
                      Tỉnh / Thành phố
                    </label>

                    <select
                      v-model="selectedProvince"
                      @change="onProvinceChange"
                      class="w-full border rounded-xl p-3"
                    >
                      <option :value="null">Chọn tỉnh/thành phố</option>

                      <option v-for="p in provinces" :key="p.ProvinceID" :value="p">
                        {{ p.ProvinceName }}
                      </option>
                    </select>
                  </div>

                  <!-- Quận / Huyện -->
                  <div>
                    <label class="text-sm font-semibold text-gray-700 mb-2 block">
                      Quận / Huyện
                    </label>

                    <select
                      v-model="selectedDistrict"
                      @change="onDistrictChange"
                      class="w-full border rounded-xl p-3"
                    >
                      <option :value="null">Chọn quận/huyện</option>

                      <option v-for="d in districts" :key="d.DistrictID" :value="d">
                        {{ d.DistrictName }}
                      </option>
                    </select>
                  </div>

                  <!-- Phường / Xã -->
                  <div>
                    <label class="text-sm font-semibold text-gray-700 mb-2 block">
                      Phường / Xã
                    </label>

                    <select
                      v-model="selectedWard"
                      @change="onWardChange"
                      class="w-full border rounded-xl p-3"
                    >
                      <option :value="null">Chọn phường/xã</option>

                      <option v-for="w in wards" :key="w.WardCode" :value="w">
                        {{ w.WardName }}
                      </option>
                    </select>
                  </div>
                </div>

                <div>
                  <label class="block text-sm font-semibold mb-2"> Địa chỉ cụ thể </label>

                  <textarea
                    rows="3"
                    v-model="addressForm.diaChiCuThe"
                    placeholder="Số nhà, tên đường..."
                    class="w-full border rounded-xl p-3 resize-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>

                <label class="flex items-center gap-3 cursor-pointer py-2">
                  <input
                    type="checkbox"
                    v-model="addressForm.macDinh"
                    class="w-5 h-5 accent-indigo-600"
                  />
                  <span class="text-gray-600">Đặt làm địa chỉ mặc định</span>
                </label>
              </div>
            </div>

            <!-- Map Section -->
            <div class="w-full md:w-[350px] flex flex-col gap-4">
              <div
                id="map"
                class="flex-1 rounded-2xl overflow-hidden border-2 border-gray-200 h-[300px]"
              ></div>

              <button
                type="button"
                @click="getCurrentLocation"
                class="w-full py-3 bg-indigo-600 hover:bg-indigo-700 text-white font-semibold rounded-xl transition flex items-center justify-center gap-2"
              >
                <span v-if="loading">Đang tải...</span>
                <span v-else>📍 Xác định vị trí hiện tại</span>
              </button>

              <div class="flex justify-end gap-3">
                <button
                  class="px-6 py-3 text-gray-500 hover:text-gray-800 transition"
                  @click="showAddressModal = false"
                >
                  Hủy
                </button>
                <button
                  class="bg-indigo-600 hover:bg-indigo-700 text-white px-8 py-3 rounded-xl shadow-lg shadow-indigo-200 transition font-medium"
                  @click="saveAddress"
                >
                  Lưu địa chỉ
                </button>
              </div>
            </div>
          </div>
        </div>
      </Teleport>
    </div>
  </div>
</template>

<script setup>
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
  getDiaChiMacDinh,
  themDiaChi,
  capNhatDiaChi,
  xoaDiaChi,
  doiMacDinh,
} from '@/service/DiaChiKhachHangService'
// import { getProvinces, getDistricts, getWards } from '@/service/ProvinceService'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'

// Fix lỗi icon Leaflet
import icon from 'leaflet/dist/images/marker-icon.png'
import shadow from 'leaflet/dist/images/marker-shadow.png'

const selectAddress = (id) => {
  selectedAddressId.value = id
  showAllAddresses.value = false
}
const showAllAddresses = ref(false)
const provinces = ref([])
const districts = ref([])
const wards = ref([])
// Cache toàn bộ phường theo tỉnh
const wardCache = ref([])

const getWardsCached = async (districtId) => {
  if (wardCache[districtId]) {
    return wardCache[districtId]
  }

  const data = await getWards(districtId)

  wardCache[districtId] = data

  return data
}

const selectedProvince = ref(null)
const selectedDistrict = ref(null)
const selectedWard = ref(null)

const provinceCode = ref(null)
const districtCode = ref(null)
const showAddressModal = ref(false)
const editingAddress = ref(null)

const calculateShipping = async (address) => {
  if (!address.thanhPho || !address.quan || !address.phuong) {
    console.warn('Thiếu thông tin địa chỉ để tính phí ship')
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
    console.log('Phí ship cập nhật:', fee)
  } catch (error) {
    console.error('Lỗi tính ship', error)
    toast.error('Không thể tính phí vận chuyển cho địa chỉ này')
    shippingFee.value = 0
  } finally {
    shippingLoading.value = false
  }
}

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

const center = ref({ lat: 21.0285, lng: 105.8542 }) // Mặc định Hà Nội
const markerPosition = ref(null)
const loading = ref(false)
let map = null
let marker = null

// Theo dõi biến showAddressModal để khởi tạo map khi modal hiện lên
watch(showAddressModal, (val) => {
  if (val) {
    nextTick(() => {
      initMap()
      map.invalidateSize() // Quan trọng: fix lỗi render map trong modal
    })
  }
})
const getCurrentLocation = () => {
  loading.value = true

  navigator.geolocation.getCurrentPosition(
    async (pos) => {
      try {
        const { latitude, longitude } = pos.coords

        // =========================
        // FILL GPS
        // =========================

        addressForm.value.latitude = latitude
        addressForm.value.longitude = longitude

        // =========================
        // MAP
        // =========================

        map.setView([latitude, longitude], 16)

        if (marker) {
          map.removeLayer(marker)
        }

        marker = L.marker([latitude, longitude], {
          icon: L.icon({
            iconUrl: icon,
            shadowUrl: shadow,
            iconSize: [25, 41],
            iconAnchor: [12, 41],
          }),
        }).addTo(map)

        // =========================
        // REVERSE GPS
        // =========================

        const response = await fetch(
          `https://nominatim.openstreetmap.org/reverse?format=json&addressdetails=1&zoom=18&lat=${latitude}&lon=${longitude}`,
        )

        const data = await response.json()

        console.log('Nominatim:', data.address)

        if (!data.address) return

        const a = data.address

        // =========================
        // ĐỊA CHỈ CHI TIẾT
        // =========================

        addressForm.value.diaChiCuThe = [
          a.house_number,
          a.road,
          a.neighbourhood,
          a.quarter,
          a.residential,
        ]
          .filter(Boolean)
          .join(', ')

        // =========================
        // FILL TỈNH QUẬN PHƯỜNG TỪ GPS
        // =========================

        const city = a.city || a.state || a.province || ''

        addressForm.value.thanhPho = city

        addressForm.value.quan = a.city_district || a.district || a.county || ''

        addressForm.value.phuong = a.suburb || a.town || a.village || a.neighbourhood || ''

        // =========================
        // FIND PROVINCE GHN
        // =========================

        const cleanCity = city.replace('Thành phố ', '').replace('Tỉnh ', '').trim().toLowerCase()

        selectedProvince.value = provinces.value.find((p) => {
          const name = p.ProvinceName.replace('Thành phố ', '')
            .replace('Tỉnh ', '')
            .trim()
            .toLowerCase()

          return name.includes(cleanCity) || cleanCity.includes(name)
        })

        if (!selectedProvince.value) {
          console.log('Không tìm thấy tỉnh GHN')

          return
        }

        // =========================
        // LOAD QUẬN
        // =========================

        districts.value = await getDistricts(selectedProvince.value.ProvinceID)

        const cleanWard = addressForm.value.phuong
          .replace('Phường ', '')
          .replace('Xã ', '')
          .replace('Thị trấn ', '')
          .trim()
          .toLowerCase()

        console.log('Tìm phường:', cleanWard)

        // =========================
        // LOAD PHƯỜNG SONG SONG
        // =========================

        const wardResults = await Promise.all(
          districts.value.map(async (district) => {
            const list = await getWardsCached(district.DistrictID)

            return {
              district,
              list,
            }
          }),
        )

        let foundDistrict = null
        let foundWard = null

        for (const item of wardResults) {
          const ward = item.list.find((w) => {
            const name = w.WardName.replace('Phường ', '')
              .replace('Xã ', '')
              .replace('Thị trấn ', '')
              .trim()
              .toLowerCase()

            return name === cleanWard || name.includes(cleanWard) || cleanWard.includes(name)
          })

          if (ward) {
            foundDistrict = item.district

            foundWard = ward

            wards.value = item.list

            break
          }
        }

        console.log('Quận GHN:', foundDistrict)

        console.log('Phường GHN:', foundWard)

        // =========================
        // UPDATE FORM
        // =========================

        if (foundDistrict && foundWard) {
          selectedDistrict.value = foundDistrict

          selectedWard.value = foundWard

          addressForm.value.thanhPho = selectedProvince.value.ProvinceName

          addressForm.value.quan = foundDistrict.DistrictName

          addressForm.value.phuong = foundWard.WardName

          addressForm.value.districtId = foundDistrict.DistrictID

          addressForm.value.wardCode = foundWard.WardCode
        }

        console.log('FORM CUOI:', JSON.parse(JSON.stringify(addressForm.value)))
      } catch (error) {
        console.error('Lỗi lấy vị trí:', error)
      } finally {
        loading.value = false
      }
    },

    () => {
      alert('Không thể lấy vị trí')

      loading.value = false
    },
  )
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

const addresses = ref([])
const selectedAddressId = ref(null)
const toast = useToast()
const route = useRoute()
const router = useRouter()

// Data
const spctId = Number(route.query.spct)
const quantity = ref(Number(route.query.qty) || 1)
const product = ref(null)
const vouchers = ref([])

const loadAddresses = async () => {
  try {
    addresses.value = await getAllDiaChi()

    const defaultAddr = addresses.value.find((item) => item.macDinh)

    if (defaultAddr) {
      selectedAddressId.value = defaultAddr.id
    }
  } catch (e) {
    console.error(e)
    toast.error('Không tải được địa chỉ')
  }
}

async function loadData() {
  product.value = await getSanPhamChiTietById(spctId)

  if (quantity.value > product.value.soLuong) {
    quantity.value = product.value.soLuong

    toast.warning('Số lượng sản phẩm vừa được cập nhật.')
  }
  vouchers.value = await getAllVoucher()

  provinces.value = await getProvinces()

  await loadAddresses()
}

onMounted(async () => {
  if (!spctId) {
    router.push('/')
    return
  }

  await loadData()

  connectSocket()
})

function connectSocket() {
  if (stompClient.connected) {
    subscribeOrder()
  } else {
    stompClient.onConnect = () => {
      console.log('✅ Connected')

      subscribeOrder()
    }
  }
}
function subscribeOrder() {
  stompClient.subscribe('/topic/pos', async (msg) => {
    const event = JSON.parse(msg.body)

    console.log(event)

    switch (event.type) {
      case 'DISCOUNT_UPDATED':
        product.value = await getSanPhamChiTietById(spctId)
        break

      case 'PRODUCT_UPDATED':
        product.value = await getSanPhamChiTietById(spctId)
        break

      case 'VOUCHER_UPDATED':
        vouchers.value = await getAllVoucher()
        break
    }
  })
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
  addressForm.value = {
    tenNguoiNhan: item.tenNguoiNhan,

    soDienThoai: item.soDienThoai,

    thanhPho: item.thanhPho,

    quan: item.quan,

    phuong: item.phuong,

    districtId: item.districtId,

    wardCode: item.wardCode,

    latitude: item.latitude,

    longitude: item.longitude,

    diaChiCuThe: item.diaChiCuThe,

    macDinh: item.macDinh,
  }

  showAddressModal.value = true
}
const saveAddress = async () => {
  try {
    if (editingAddress.value) {
      await capNhatDiaChi(editingAddress.value.id, addressForm.value)

      toast.success('Cập nhật thành công')
    } else {
      console.log('DATA SAVE', JSON.stringify(addressForm.value, null, 2))

      await themDiaChi(addressForm.value)

      toast.success('Thêm địa chỉ thành công')
    }

    // đóng modal
    showAddressModal.value = false

    // load lại danh sách địa chỉ
    await loadAddresses()

    // reset form sau khi lưu
    resetAddressForm()

    // reset combobox GHN
    selectedProvince.value = null
    selectedDistrict.value = null
    selectedWard.value = null

    districts.value = []
    wards.value = []
  } catch (e) {
    console.error(e)
    toast.error(e.message)
  }
  if (!editingAddress.value && addresses.value.length >= 3) {
    toast.warning('Chỉ được lưu tối đa 3 địa chỉ')

    return
  }
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

const onProvinceChange = async () => {
  if (!selectedProvince.value) return

  addressForm.value.thanhPho = selectedProvince.value.ProvinceName

  districts.value = await getDistricts(selectedProvince.value.ProvinceID)

  addressForm.value.quan = ''
  addressForm.value.phuong = ''

  addressForm.value.districtId = null
  addressForm.value.wardCode = ''
}
const onDistrictChange = async () => {
  if (!selectedDistrict.value) return

  addressForm.value.quan = selectedDistrict.value.DistrictName

  addressForm.value.districtId = selectedDistrict.value.DistrictID

  wards.value = await getWards(selectedDistrict.value.DistrictID)

  addressForm.value.phuong = ''
  addressForm.value.wardCode = ''
}
const onWardChange = () => {
  if (!selectedWard.value) return

  addressForm.value.phuong = selectedWard.value.WardName

  addressForm.value.wardCode = selectedWard.value.WardCode
}

// Logic Đơn hàng
const shippingFee = ref(0)
const shippingLoading = ref(false)
const selectedVoucherId = ref(null)
const note = ref('')

const stock = computed(() => product.value?.soLuongTon || 0)
const subtotal = computed(() => {
  return (product.value?.giaSauGiam || product.value?.giaBan || 0) * quantity.value
})
const voucherDiscount = computed(() => {
  const v = vouchers.value.find((v) => v.id === selectedVoucherId.value)
  if (!v || subtotal.value < v.giaTriDonHangToiThieu) return 0
  return v.loaiGiamGia === 'phan_tram'
    ? Math.min((subtotal.value * v.giaTriGiam) / 100, v.giaTriGiamToiDa)
    : v.giaTriGiam
})

const total = computed(() =>
  Math.max(subtotal.value + shippingFee.value - voucherDiscount.value, 0),
)

const increaseQty = () => {
  if (quantity.value < stock.value) quantity.value++
}
const decreaseQty = () => {
  if (quantity.value > 1) quantity.value--
}

const placeOrder = async () => {
  if (!selectedAddressId.value) {
    toast.warning('Vui lòng chọn địa chỉ giao hàng')
    return
  }
  const body = {
    addressId: selectedAddressId.value, // Thêm trường này vào body gửi lên
    shippingFee: shippingFee.value,
    voucherId: selectedVoucherId.value,
    note: note.value,
    items: [{ productDetailId: spctId, quantity: quantity.value }],
  }
  try {
    const res = await taoHoaDonOnline(body, sessionStorage.getItem('token'))
    toast.success('Đặt hàng thành công 🎉')
    console.log('SAVE =', JSON.stringify(product.value))
    sessionStorage.setItem('orderProduct', JSON.stringify(product.value))
    console.log(product.value)
    console.log(JSON.stringify(product.value))
    router.push({
      path: '/payment',
      query: { id: res.id, maHoaDon: res.maHoaDon, qrUrl: res.qrUrl },
    })
  } catch {
    toast.error('Đặt hàng thất bại ❌')
  }
}
watch(selectedAddressId, async (id) => {
  if (!id) return

  const address = addresses.value.find((a) => a.id === id)

  if (!address) return

  console.log('Địa chỉ tính ship:', address)

  if (!address.thanhPho || !address.quan || !address.phuong) {
    console.warn('Thiếu dữ liệu địa chỉ GHN', address)
    shippingFee.value = 0
    return
  }

  try {
    shippingLoading.value = true

    shippingFee.value = await getShippingFee({
      thanhPho: address.thanhPho,
      quan: address.quan,
      phuong: address.phuong,
    })
  } catch (e) {
    console.error('Lỗi tính ship:', e)
    shippingFee.value = 0
  } finally {
    shippingLoading.value = false
  }
})
// ... các khai báo hiện tại của bạn ...

// Hàm khởi tạo map
const initMap = () => {
  // Nếu đã tồn tại map, xóa nó trước khi tạo mới để tránh lỗi
  if (map) {
    map.remove()
    map = null
  }

  map = L.map('map').setView([21.0285, 105.8542], 13)
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap',
  }).addTo(map)

  // Nếu đang sửa địa chỉ, thêm lại marker cũ vào map mới
  if (editingAddress.value && editingAddress.value.latitude) {
    addMarker(editingAddress.value.latitude, editingAddress.value.longitude)
  }
}

// Hàm hỗ trợ vẽ marker
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

// Watch để khởi tạo map
watch(showAddressModal, (val) => {
  if (val) {
    nextTick(() => {
      initMap()
    })
  } else {
    // Dọn dẹp khi đóng modal
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
</script>
