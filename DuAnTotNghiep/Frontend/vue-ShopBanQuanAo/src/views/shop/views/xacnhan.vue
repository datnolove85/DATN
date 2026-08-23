<template>
  <div class="checkout-page min-h-screen bg-slate-50 text-slate-900 pb-12">
    <div class="pointer-events-none fixed inset-0 overflow-hidden">
      <div class="absolute -left-24 top-20 h-80 w-80 rounded-full bg-indigo-200/30 blur-3xl"></div>
      <div class="absolute -right-24 top-0 h-96 w-96 rounded-full bg-sky-200/30 blur-3xl"></div>
    </div>

    <div class="relative mx-auto px-4 py-6" style="max-width: 760px !important">
      <!-- KHUNG CHỨA DUY NHẤT -->
      <div class="rounded-3xl border border-slate-200/80 bg-white p-5 sm:p-6 shadow-sm space-y-4">
        <!-- 1. HERO HEADER -->
        <div class="flex items-center justify-between pb-4 border-b border-slate-100">
          <div class="flex items-center gap-3">
            <span
              class="grid h-9 w-9 shrink-0 place-items-center rounded-xl bg-indigo-50 text-indigo-600"
            >
              <ClipboardCheck :size="18" />
            </span>
            <div>
              <h1 class="text-sm font-bold text-slate-900">Xác nhận đơn hàng</h1>
              <p class="text-xs text-slate-500">Kiểm tra thông tin và hoàn tất thanh toán</p>
            </div>
          </div>
        </div>

        <!-- 3. ĐỊA CHỈ NHẬN HÀNG -->
        <div class="pb-4 border-b border-slate-100">
          <AddressSection
            :is-logged-in="isLoggedIn"
            :addresses="addresses"
            :selected-address-id="selectedAddressId"
            v-model:show-all-addresses="showAllAddresses"
            v-model:address-form="addressForm"
            :provinces="provinces"
            :districts="districts"
            :wards="wards"
            v-model:selected-province="selectedProvince"
            v-model:selected-district="selectedDistrict"
            v-model:selected-ward="selectedWard"
            @open-add-address="openAddAddress"
            @open-edit-address="openEditAddress"
            @delete-address="deleteAddress"
            @set-default="setDefault"
            @select-address="selectAddress"
            @province-change="onProvinceChange"
            @district-change="onDistrictChange"
            @ward-change="onWardChange"
          />
        </div>

        <!-- 4. SẢN PHẨM ĐÃ CHỌN -->
        <div class="pb-4 border-b border-slate-100">
          <ProductSection
            :is-cart-checkout="isCartCheckout"
            :checkout-items="checkoutItems"
            :product="product"
            v-model:quantity="quantity"
            :max-available="maxAvailable"
            :format-money="formatMoney"
            @decrease-cart-qty="decreaseCartQty"
            @increase-cart-qty="increaseCartQty"
            @validate-cart-qty="validateCartQty"
            @on-cart-qty-blur="onCartQtyBlur"
            @decrease-qty="decreaseQty"
            @increase-qty="increaseQty"
            @on-qty-blur="onQtyBlur"
          />
        </div>

        <!-- 4.5. PHƯƠNG THỨC THANH TOÁN -->
        <div class="pb-4 border-b border-slate-100">
          <div class="flex items-center justify-between mb-3">
            <div class="flex items-center gap-3">
              <span class="grid h-8 w-8 place-items-center rounded-xl bg-indigo-50 text-indigo-600">
                <CreditCard :size="16" />
              </span>
              <div>
                <h2 class="text-sm font-bold text-slate-900">Phương thức thanh toán</h2>
                <p class="text-xs text-slate-500">Lựa chọn hình thức thanh toán thuận tiện</p>
              </div>
            </div>
          </div>

          <div class="grid gap-3 sm:grid-cols-2">
            <!-- COD Card -->
            <div
              class="group relative cursor-pointer overflow-hidden rounded-2xl border-2 p-3.5 transition-all duration-300"
              :class="
                paymentMethod === 'COD'
                  ? 'border-indigo-600 bg-indigo-50/40 text-slate-900 shadow-sm ring-1 ring-indigo-600/20'
                  : 'border-slate-200/80 bg-white text-slate-900 hover:border-slate-300 hover:bg-slate-50/50'
              "
              @click="paymentMethod = 'COD'"
            >
              <div class="flex items-start gap-3">
                <div
                  class="grid h-9 w-9 shrink-0 place-items-center rounded-xl transition-colors"
                  :class="
                    paymentMethod === 'COD'
                      ? 'bg-indigo-600 text-white shadow-sm'
                      : 'bg-slate-100 text-slate-600'
                  "
                >
                  <Truck :size="18" />
                </div>
                <div class="flex-1 min-w-0">
                  <div class="flex items-center justify-between">
                    <span class="font-bold text-xs sm:text-sm">Thanh toán khi nhận (COD)</span>
                    <span
                      class="h-3.5 w-3.5 rounded-full border-2 flex items-center justify-center shrink-0"
                      :class="
                        paymentMethod === 'COD'
                          ? 'border-indigo-600 bg-indigo-600'
                          : 'border-slate-300'
                      "
                    >
                      <span
                        v-if="paymentMethod === 'COD'"
                        class="h-1.5 w-1.5 rounded-full bg-white"
                      ></span>
                    </span>
                  </div>
                  <p class="mt-0.5 text-[11px] leading-relaxed text-slate-600 truncate">
                    Thanh toán bằng tiền mặt khi giao hàng.
                  </p>
                </div>
              </div>
            </div>

            <!-- VNPAY Card -->
            <div
              class="group relative cursor-pointer overflow-hidden rounded-2xl border-2 p-3.5 transition-all duration-300"
              :class="
                paymentMethod === 'VNPAY'
                  ? 'border-indigo-600 bg-indigo-50/40 text-slate-900 shadow-sm ring-1 ring-indigo-600/20'
                  : 'border-slate-200/80 bg-white text-slate-900 hover:border-slate-300 hover:bg-slate-50/50'
              "
              @click="paymentMethod = 'VNPAY'"
            >
              <div class="flex items-start gap-3">
                <div
                  class="grid h-9 w-9 shrink-0 place-items-center rounded-xl transition-colors"
                  :class="
                    paymentMethod === 'VNPAY'
                      ? 'bg-indigo-600 text-white shadow-sm'
                      : 'bg-indigo-50 text-indigo-600'
                  "
                >
                  <CreditCard :size="18" />
                </div>
                <div class="flex-1 min-w-0">
                  <div class="flex items-center justify-between">
                    <span class="font-bold text-xs sm:text-sm">Cổng VNPAY QR</span>
                    <span
                      class="h-3.5 w-3.5 rounded-full border-2 flex items-center justify-center shrink-0"
                      :class="
                        paymentMethod === 'VNPAY'
                          ? 'border-indigo-600 bg-indigo-600'
                          : 'border-slate-300'
                      "
                    >
                      <span
                        v-if="paymentMethod === 'VNPAY'"
                        class="h-1.5 w-1.5 rounded-full bg-white"
                      ></span>
                    </span>
                  </div>
                  <p class="mt-0.5 text-[11px] leading-relaxed text-slate-600 truncate">
                    Quét mã QR qua ứng dụng ngân hàng.
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 5. TÓM TẮT ĐƠN HÀNG & DÙNG XU -->
        <div class="pb-4 border-b border-slate-100 space-y-3">
          <div class="flex items-center gap-3">
            <span class="grid h-8 w-8 place-items-center rounded-xl bg-slate-900 text-white">
              <ReceiptText :size="16" />
            </span>
            <div>
              <h2 class="text-sm font-bold text-slate-900">Tóm tắt đơn hàng</h2>
              <p class="text-xs text-slate-500">{{ quantity }} sản phẩm trong đơn</p>
            </div>
          </div>

          <div class="space-y-2 text-xs">
            <div class="flex items-center justify-between text-sm">
              <span class="text-slate-500">Tiền hàng</span>
              <span class="font-bold text-slate-800">{{ formatMoney(subtotal) }}</span>
            </div>

            <div class="flex items-center justify-between text-sm">
              <div class="flex items-center gap-1.5 text-slate-500">
                <span>Phí vận chuyển</span>
                <span class="text-xs text-slate-400"></span>
              </div>
              <div class="flex items-center gap-1">
                <LoaderCircle v-if="shippingLoading" :size="14" class="animate-spin text-sky-600" />
                <span v-else class="font-bold text-slate-800">{{ formatMoney(shippingFee) }}</span>
              </div>
            </div>

            <div class="flex items-center justify-between text-sm">
              <div class="flex items-center gap-1.5">
                <span class="text-slate-500">Voucher giảm</span>
                <button
                  type="button"
                  @click="showVoucherModal = true"
                  class="text-xs font-bold text-indigo-600 hover:underline"
                >
                  {{ selectedVoucher ? `(${selectedVoucher.maVoucher})` : '[Chọn]' }}
                </button>
              </div>
              <span class="font-bold text-emerald-600">-{{ formatMoney(voucherDiscount) }}</span>
            </div>

            <!-- DÙNG XU TÙY CHỈNH -->
            <!-- DÙNG XU TÙY CHỈNH -->
            <div
              v-if="isLoggedIn && customerInfo && customerInfo.soDuXu > 0"
              class="border-t border-slate-100 pt-3 space-y-2"
            >
              <div class="flex items-center justify-between text-xs">
                <span class="font-bold text-amber-600 flex items-center gap-1.5">
                  <span>🪙</span> Dùng xu (Khả dụng: {{ customerInfo.soDuXu }})
                </span>
                <span class="text-xs text-slate-400">1 xu = {{ formatMoney(tyLeQuyDoi) }}</span>
              </div>

              <!-- Đã bỏ nút bấm, input tự động cập nhật real-time -->
              <div>
                <input
                  type="number"
                  min="0"
                  :max="maxAllowedCoins"
                  v-model.number="soXuCanDung"
                  @input="onCoinsChange"
                  placeholder="Nhập số xu cần dùng..."
                  class="w-full rounded-xl border border-slate-200 bg-slate-50 px-3.5 py-1.5 text-xs font-medium focus:border-amber-500 focus:outline-none"
                />
              </div>

              <div
                v-if="tienGiamDoXu > 0"
                class="flex items-center justify-between text-xs text-amber-600 font-semibold"
              >
                <span>Giảm từ xu:</span>
                <span>-{{ formatMoney(tienGiamDoXu) }}</span>
              </div>
            </div>

            <div class="border-t border-dashed border-slate-200 pt-3">
              <div class="flex items-end justify-between">
                <span class="text-sm font-bold text-slate-900">Tổng thanh toán</span>
                <span class="text-xl font-black text-indigo-600">{{ formatMoney(total) }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 6. NÚT ĐẶT HÀNG & BẢO MẬT -->
        <div class="space-y-2.5 pt-1">
          <button
            type="button"
            :disabled="
              isPlacingOrder ||
              shippingLoading ||
              (isCartCheckout ? checkoutItems.length === 0 : maxAvailable === 0)
            "
            class="w-full rounded-2xl bg-indigo-600 py-3 text-xs sm:text-sm font-bold text-white shadow-md shadow-indigo-200/50 transition-all hover:bg-indigo-700 disabled:cursor-not-allowed disabled:opacity-60 flex items-center justify-center gap-2"
            @click="placeOrder"
          >
            <LoaderCircle v-if="isPlacingOrder" :size="16" class="animate-spin" />
            {{ isPlacingOrder ? 'Đang tạo đơn...' : 'Đặt hàng và thanh toán' }}
          </button>

          <p class="text-center text-[11px] text-slate-400">
            Nhấn đặt hàng đồng nghĩa bạn xác nhận thông tin phía trên là chính xác.
          </p>

          <!-- Khối bảo mật tinh gọn -->
          <div
            class="flex items-center gap-2.5 rounded-xl border border-emerald-100 bg-emerald-50/60 px-3 py-2"
          >
            <span
              class="grid h-6 w-6 shrink-0 place-items-center rounded-md bg-white text-emerald-600 shadow-sm"
            >
              <ShieldCheck :size="14" />
            </span>
            <p class="text-[11px] font-medium text-emerald-900 leading-tight">
              Thông tin cá nhân được bảo mật an toàn và chỉ dùng để xử lý đơn hàng.
            </p>
          </div>
        </div>
      </div>

      <!-- MODALS -->
      <div>
        <AddressModal
          :is-open="showAddressModal"
          :is-logged-in="isLoggedIn"
          :editing-address="editingAddress"
          :address-form="addressForm"
          :provinces="provinces"
          :districts="districts"
          :wards="wards"
          :selected-province="selectedProvince"
          :selected-district="selectedDistrict"
          :selected-ward="selectedWard"
          :loading="loading"
          @close="showAddressModal = false"
          @save="saveAddress"
          @get-location="getCurrentLocation"
          @update:selected-province="(val) => (selectedProvince = val)"
          @update:selected-district="(val) => (selectedDistrict = val)"
          @update:selected-ward="(val) => (selectedWard = val)"
          @province-change="onProvinceChange"
          @district-change="onDistrictChange"
        />

        <VoucherModal
          :is-open="showVoucherModal"
          :sorted-vouchers="sortedVouchers"
          :best-voucher="bestVoucher"
          :selected-voucher-id="selectedVoucherId"
          :subtotal="subtotal"
          @close="showVoucherModal = false"
          @select="
            (id) => {
              selectedVoucherId = id
              showVoucherModal = false
            }
          "
          @clear="clearVoucher"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  Check,
  ClipboardCheck,
  LoaderCircle,
  ReceiptText,
  ShieldCheck,
  CreditCard,
  Truck,
} from 'lucide-vue-next'
import { useToast } from 'vue-toastification'
import { getAllVoucher } from '@/service/VoucherService'
import { getSanPhamChiTietById } from '@/service/SanPhamChiTiet'
import { taoHoaDonOnline } from '@/service/HoaDonService'
import { thanhToan } from '@/service/PaymentService'
import stompClient from '@/socket'
import { getProvinces, getDistricts, getWards, getShippingFee } from '@/service/GHNService'
import {
  getAllDiaChi,
  themDiaChi,
  capNhatDiaChi,
  xoaDiaChi,
  doiMacDinh,
} from '@/service/DiaChiKhachHangService'
import gamificationApi from '@/service/clientGamificationService'
import { detailKhachHangService } from '@/service/KhachHangService'
import { cauHinhService } from '@/service/cauHinhService'

import AddressModal from '@/views/shop/views/componnents/AddressModal.vue'
import VoucherModal from '@/views/shop/views/componnents/VoucherModal.vue'
import AddressSection from '@/views/shop/views/componnents/AddressSection.vue'
import ProductSection from '@/views/shop/views/componnents/ProductSection.vue'
import emitter from '@/utils/emitter'

const systemConfig = ref({})

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

const customerInfo = ref(null)
const soXuCanDung = ref(0)
const soXuSuDung = ref(0)
const tienGiamDoXu = ref(0)
const paymentMethod = ref('COD') // Trạng thái phương thức thanh toán mặc định

const tyLeQuyDoi = computed(() => Number(systemConfig.value['TY_LE_QUY_DOI_XU'] || 1000))

const maxAllowedCoins = computed(() => {
  if (!customerInfo.value) return 0
  const tyLeGiamToiDa = Number(systemConfig.value['TY_LE_GIAM_TOI_DA_XU'] || 50)
  const tienGiamToiDaTheoPhanTram = (subtotal.value * tyLeGiamToiDa) / 100
  const maxXuTheoPhanTram = Math.floor(tienGiamToiDaTheoPhanTram / tyLeQuyDoi.value)
  const maxXuTheoTongTien = Math.floor(subtotal.value / tyLeQuyDoi.value)
  return Math.min(customerInfo.value.soDuXu || 0, maxXuTheoPhanTram, maxXuTheoTongTien)
})

const onCoinsChange = () => {
  if (soXuCanDung.value < 0 || isNaN(soXuCanDung.value)) {
    soXuCanDung.value = 0
  }
  if (soXuCanDung.value > maxAllowedCoins.value) {
    soXuCanDung.value = maxAllowedCoins.value
  }

  // Tự động áp dụng ngay lập tức khi giá trị thay đổi
  soXuSuDung.value = Number(soXuCanDung.value) || 0
  tienGiamDoXu.value = soXuSuDung.value * tyLeQuyDoi.value
}

const apDungXuTuyChinh = () => {
  onCoinsChange()
  soXuSuDung.value = Number(soXuCanDung.value) || 0
  tienGiamDoXu.value = soXuSuDung.value * tyLeQuyDoi.value
  if (soXuSuDung.value > 0) {
    toast.success(`Đã áp dụng ${soXuSuDung.value} xu (Giảm ${formatMoney(tienGiamDoXu.value)})`)
  } else {
    toast.info('Đã hủy dùng xu.')
  }
}

const maxAvailable = computed(() => {
  if (!product.value) return 0
  return product.value.soLuongKhaDung ?? product.value.soLuongTon ?? 0
})

const increaseQty = () => {
  if (maxAvailable.value === 0) return
  if (quantity.value < maxAvailable.value) {
    quantity.value++
  } else {
    toast.warning(`Số lượng tối đa có thể mua là ${maxAvailable.value}`)
  }
}

const decreaseQty = () => {
  if (quantity.value > 1) quantity.value--
}

const validateQty = async () => {
  if (maxAvailable.value === 0) {
    quantity.value = 0
    return
  }
  if (quantity.value > maxAvailable.value) {
    try {
      product.value = await getSanPhamChiTietById(spctId.value)
      const max = product.value.soLuongKhaDung ?? product.value.soLuongTon ?? 0
      quantity.value = max > 0 ? max : 0
    } catch (error) {
      console.error('Lỗi load lại sản phẩm:', error)
    }
  }
}

const onQtyBlur = () => {
  if (maxAvailable.value === 0) {
    quantity.value = 0
    return
  }
  if (!quantity.value || quantity.value < 1) {
    quantity.value = 1
  }
}

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

const validateCartQty = (item) => {
  const max = item.soLuongKhaDung ?? item.soLuongTon ?? 0
  if (max === 0) {
    item.quantity = 0
    return
  }
  if (item.quantity > max) {
    item.quantity = max
    toast.warning(`Số lượng vượt quá số lượng khả dụng (${max})`)
  }
}

const onCartQtyBlur = (item) => {
  const max = item.soLuongKhaDung ?? item.soLuongTon ?? 0
  if (max === 0) {
    item.quantity = 0
    return
  }
  if (!item.quantity || item.quantity < 1) {
    item.quantity = 1
  }
}

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

const selectedVoucherId = ref(null)

const selectedVoucher = computed(() => {
  return vouchers.value.find((v) => v.uid === selectedVoucherId.value) || null
})

const sortedVouchers = computed(() => {
  return [...vouchers.value].sort((a, b) => {
    if (a.uid === selectedVoucherId.value) return -1
    if (b.uid === selectedVoucherId.value) return 1
    const aValid = subtotal.value >= a.giaTriDonHangToiThieu
    const bValid = subtotal.value >= b.giaTriDonHangToiThieu
    if (aValid !== bValid) return bValid - aValid
    return getVoucherDiscount(b) - getVoucherDiscount(a)
  })
})

const clearVoucher = () => {
  selectedVoucherId.value = null
  showVoucherModal.value = false
}

const getVoucherDiscount = (voucher) => {
  if (!voucher || subtotal.value < voucher.giaTriDonHangToiThieu) return 0
  if (voucher.loaiGiamGia === 'tien_mat') return Number(voucher.giaTriGiam)
  let discount = (subtotal.value * Number(voucher.giaTriGiam)) / 100
  if (voucher.giaTriGiamToiDa) discount = Math.min(discount, Number(voucher.giaTriGiamToiDa))
  return discount
}

const bestVoucher = computed(() => {
  return vouchers.value
    .filter((v) => subtotal.value >= v.giaTriDonHangToiThieu)
    .sort((a, b) => getVoucherDiscount(b) - getVoucherDiscount(a))[0]
})

const showVoucherModal = ref(false)
const selectAddress = (id) => {
  selectedAddressId.value = id
  showAllAddresses.value = false
}

const showAllAddresses = ref(false)
const provinces = ref([])
const districts = ref([])
const wards = ref([])

const selectedProvince = ref(null)
const selectedDistrict = ref(null)
const selectedWard = ref(null)

const showAddressModal = ref(false)
const editingAddress = ref(null)

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
    shippingFee.value = 0
  } finally {
    shippingLoading.value = false
  }
}

const loading = ref(false)
const addresses = ref([])
const selectedAddressId = ref(null)
const toast = useToast()
const route = useRoute()
const router = useRouter()
const authToken = sessionStorage.getItem('token')
const isLoggedIn = Boolean(authToken)

const spctId = computed(() => (route.query.spct ? Number(route.query.spct) : null))
const quantity = ref(Number(route.query.qty) || 1)
const product = ref(null)
const checkoutItems = ref([])
const isCartCheckout = ref(false)
const vouchers = ref([])

async function loadData() {
  product.value = await getSanPhamChiTietById(spctId.value)
  const max = product.value.soLuongKhaDung ?? product.value.soLuongTon ?? 0
  if (max === 0) {
    quantity.value = 0
  } else if (quantity.value > max) {
    quantity.value = max
  }
  await fetchVouchers()
  if (isLoggedIn) await loadAddresses()
  provinces.value = await getProvinces()
}

onMounted(async () => {
  loadSystemConfig()
  const userSession = sessionStorage.getItem('user')
  if (userSession) {
    try {
      const user = JSON.parse(userSession)
      customerInfo.value = await detailKhachHangService(user.idKhachHang)
    } catch (error) {
      console.error('Lỗi tải thông tin khách hàng:', error)
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
    checkoutItems.value = JSON.parse(checkout).items.map((item) => {
      const max = item.soLuongKhaDung ?? item.soLuongTon ?? 0
      return { ...item, quantity: max === 0 ? 0 : item.quantity || 1 }
    })
    await fetchVouchers()
    if (isLoggedIn) await loadAddresses()
    provinces.value = await getProvinces()
    connectSocket()
    return
  }
  router.push('/')
})

function connectSocket() {
  if (!stompClient) return

  // Nếu đã kết nối rồi thì tiến hành subscribe luôn
  if (stompClient.connected) {
    subscribeOrder()
    return
  }

  // Kích hoạt kết nối nếu client chưa chạy (đối với @stomp/stompjs)
  if (typeof stompClient.activate === 'function' && !stompClient.active) {
    stompClient.activate()
  }

  // Lưu lại hàm onConnect cũ (nếu có trong file @/socket.js) để không bị ghi đè mất logic gốc
  const existingOnConnect = stompClient.onConnect

  stompClient.onConnect = (frame) => {
    if (typeof existingOnConnect === 'function') {
      existingOnConnect(frame)
    }
    subscribeOrder()
  }

  // Thêm xử lý khi mất kết nối hoặc lỗi để dễ debug
  stompClient.onStompError = (frame) => {
    console.error('Lỗi STOMP:', frame.headers['message'])
  }
}

function subscribeOrder() {
  stompClient.subscribe('/topic/pos', async (msg) => {
    const event = JSON.parse(msg.body)

    switch (event.type) {
      case 'DISCOUNT_UPDATED':
        product.value = await getSanPhamChiTietById(spctId.value)
        const max = product.value.soLuongKhaDung ?? product.value.soLuongTon ?? 0
        if (max === 0) {
          quantity.value = 0
          toast.warning('Sản phẩm đã hết hàng!')
        }
        break

      case 'VOUCHER_UPDATED':
        await fetchVouchers()

        if (selectedVoucherId.value) {
          const latest = vouchers.value.find((v) => v.uid === selectedVoucherId.value)

          if (!latest || latest.trangThai !== 1) {
            selectedVoucherId.value = null
            toast.warning('Voucher không còn khả dụng.')
            break
          }

          if (subtotal.value < latest.giaTriDonHangToiThieu) {
            selectedVoucherId.value = null
            toast.warning('Voucher không còn đủ điều kiện áp dụng.')
          }
        }
        break
      case 'KHO_VOUCHER_UPDATED':
        await fetchVouchers()

        if (selectedVoucherId.value) {
          const latest = vouchers.value.find((v) => v.id === selectedVoucherId.value)
          const now = new Date()

          if (
            !latest ||
            latest.trangThai === false ||
            (latest.soLuongConLai !== null && latest.soLuongConLai <= 0)
          ) {
            selectedVoucherId.value = null
            toast.warning(
              'Voucher bạn đang chọn đã bị Admin ngưng hoạt động hoặc đã hết lượt sử dụng!',
            )
            break
          }

          if (latest.ngayHetHan && new Date(latest.ngayHetHan) < now) {
            selectedVoucherId.value = null
            toast.warning('Voucher bạn đang chọn đã hết hạn sử dụng.')
            break
          }

          const dieuKienToiThieu = latest.dieuKienToiThieu ? Number(latest.dieuKienToiThieu) : 0
          if (subtotal.value < dieuKienToiThieu) {
            selectedVoucherId.value = null
            toast.warning(
              'Đơn hàng không còn đủ điều kiện tối thiểu theo mức quy định mới của voucher.',
            )
            break
          }

          toast.info('Thông tin hoặc mức giảm của voucher vừa được Admin cập nhật lại.')
        }
        break
    }
  })
}

const shippingFee = ref(0)
const shippingLoading = ref(false)

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
    ? Math.min((subtotal.value * v.giaTriGiam) / 100, v.giaTriGiamToiDa ?? Number.POSITIVE_INFINITY)
    : v.giaTriGiam
})

const total = computed(() =>
  Math.max(subtotal.value + shippingFee.value - voucherDiscount.value - tienGiamDoXu.value, 0),
)

const formatMoney = (value) => Number(value || 0).toLocaleString('vi-VN') + ' đ'
const isPlacingOrder = ref(false)
const placeOrder = async () => {
  if (isPlacingOrder.value) return
  if (isLoggedIn && !selectedAddressId.value) {
    toast.warning('Vui lòng chọn địa chỉ giao hàng')
    return
  }

  const selectedVoucherObj = selectedVoucher.value
  const body = {
    addressId: isLoggedIn ? selectedAddressId.value : null,
    shippingFee: shippingFee.value,
    voucherId:
      selectedVoucherObj?.loaiVoucher === 'CA_NHAN' ? null : (selectedVoucherObj?.id ?? null),
    voucherKhachHangId:
      selectedVoucherObj?.loaiVoucher === 'CA_NHAN' ? selectedVoucherObj.idVoucherKhachHang : null,
    soXuSuDung: soXuSuDung.value,
    tienGiamDoXu: tienGiamDoXu.value,
    paymentMethod: paymentMethod.value, // Gửi phương thức COD hoặc VNPAY lên để Backend biết
    note: '',
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

    // 1. Tạo hóa đơn chung trước
    const res = await taoHoaDonOnline(body, authToken)

    emitter.emit('cart-updated')

    // 2. Phân nhánh theo phương thức thanh toán
    if (paymentMethod.value === 'VNPAY') {
      // Nếu là VNPAY: Gọi tiếp API lấy link thanh toán dựa trên ID hóa đơn vừa tạo
      const paymentBody = {
        idHoaDon: Number(res.id),
        method: 'VNPAY',
        orderInfo: `Thanh toan don hang ${res.maHoaDon}`,
      }

      const paymentRes = await thanhToan(paymentBody)
      if (paymentRes && paymentRes.paymentUrl) {
        if (isCartCheckout.value) sessionStorage.removeItem('checkoutData')
        toast.success('Đang chuyển hướng đến cổng thanh toán VNPay...')
        window.location.href = paymentRes.paymentUrl
        return
      }
    } else {
      // Nếu là COD: Đặt hàng thành công hoàn tất, chuyển về trang quản lý đơn hàng
      toast.success(`Đặt hàng thành công! Mã đơn: ${res.maHoaDon}`)
      if (isCartCheckout.value) sessionStorage.removeItem('checkoutData')

      router.push('/donhang') // Thay bằng đường dẫn trang lịch sử đơn hàng của bạn
    }
  } catch (error) {
    toast.error(error.message || 'Đặt hàng thất bại ❌')
  } finally {
    isPlacingOrder.value = false
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

const getCurrentLocation = () => {
  loading.value = true
  navigator.geolocation.getCurrentPosition(
    async (pos) => {
      const { latitude, longitude } = pos.coords
      addressForm.value.latitude = latitude
      addressForm.value.longitude = longitude

      try {
        const response = await fetch(
          `https://nominatim.openstreetmap.org/reverse?format=json&lat=${latitude}&lon=${longitude}&accept-language=vi`,
        )
        const data = await response.json()

        if (data && data.address) {
          const addr = data.address

          const street = addr.road || addr.house_number || ''
          const suburb = addr.suburb || addr.quarter || addr.neighbourhood || ''
          addressForm.value.diaChiCuThe =
            [street, suburb].filter(Boolean).join(', ') || data.display_name

          const cityName = addr.city || addr.state || addr.province || ''
          if (cityName && provinces.value.length > 0) {
            const foundProv = provinces.value.find(
              (p) =>
                cityName.toLowerCase().includes(p.ProvinceName.toLowerCase()) ||
                p.ProvinceName.toLowerCase().includes(cityName.toLowerCase()),
            )

            if (foundProv) {
              selectedProvince.value = foundProv
              addressForm.value.thanhPho = foundProv.ProvinceName

              const distList = await getDistricts(foundProv.ProvinceID)
              districts.value = distList

              const rawWardName =
                addr.ward || addr.village || addr.suburb || addr.town || addr.hamlet || ''

              const districtName = addr.county || addr.district || ''
              let foundDist = null
              if (districtName) {
                foundDist = distList.find(
                  (d) =>
                    districtName.toLowerCase().includes(d.DistrictName.toLowerCase()) ||
                    d.DistrictName.toLowerCase().includes(districtName.toLowerCase()),
                )
              }

              if (foundDist) {
                selectedDistrict.value = foundDist
                addressForm.value.quan = foundDist.DistrictName
                addressForm.value.districtId = foundDist.DistrictID

                const wardList = await getWards(foundDist.DistrictID)
                wards.value = wardList

                if (rawWardName && wardList.length > 0) {
                  const foundWard = wardList.find(
                    (w) =>
                      rawWardName.toLowerCase().includes(w.WardName.toLowerCase()) ||
                      w.WardName.toLowerCase().includes(rawWardName.toLowerCase()),
                  )
                  if (foundWard) {
                    selectedWard.value = foundWard
                    addressForm.value.phuong = foundWard.WardName
                    addressForm.value.wardCode = foundWard.WardCode
                  }
                }
              } else if (rawWardName) {
                let matchedDist = null
                let matchedWard = null
                let matchedWardList = []

                for (const d of distList) {
                  try {
                    const wardList = await getWards(d.DistrictID)
                    const foundW = wardList.find(
                      (w) =>
                        rawWardName.toLowerCase().includes(w.WardName.toLowerCase()) ||
                        w.WardName.toLowerCase().includes(rawWardName.toLowerCase()),
                    )
                    if (foundW) {
                      matchedDist = d
                      matchedWard = foundW
                      matchedWardList = wardList
                      break
                    }
                  } catch (err) {
                    console.error('Lỗi duyệt danh sách xã:', err)
                  }
                }

                if (matchedDist && matchedWard) {
                  selectedDistrict.value = matchedDist
                  addressForm.value.quan = matchedDist.DistrictName
                  addressForm.value.districtId = matchedDist.DistrictID

                  wards.value = matchedWardList

                  selectedWard.value = matchedWard
                  addressForm.value.phuong = matchedWard.WardName
                  addressForm.value.wardCode = matchedWard.WardCode
                }
              }
            }
          }
          toast.success('Đã lấy và tự động điền thông tin vị trí thành công!')
        }
      } catch (e) {
        console.error('Lỗi giải mã tọa độ:', e)
        toast.warning('Đã định vị nhưng không thể tự động điền đầy đủ cấp hành chính.')
      } finally {
        loading.value = false
      }
    },
    () => {
      toast.error('Vui lòng cấp quyền truy cập vị trí trên trình duyệt.')
      loading.value = false
    },
    { enableHighAccuracy: true },
  )
}

const loadAddresses = async () => {
  if (!isLoggedIn) return
  try {
    addresses.value = await getAllDiaChi()
    const selected = addresses.value.find((item) => item.macDinh) || addresses.value[0]
    selectedAddressId.value = selected?.id ?? null
  } catch (e) {
    toast.error('Không tải được địa chỉ')
  }
}

const openAddAddress = () => {
  editingAddress.value = null
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
  selectedProvince.value = null
  selectedDistrict.value = null
  selectedWard.value = null
  districts.value = []
  wards.value = []

  showAddressModal.value = true
}

const openEditAddress = async (item) => {
  editingAddress.value = item
  addressForm.value = { ...item }
  showAddressModal.value = true
}

const saveAddress = async () => {
  try {
    if (editingAddress.value) {
      await capNhatDiaChi(editingAddress.value.id, addressForm.value)
      toast.success('Cập nhật thành công')
    } else {
      await themDiaChi(addressForm.value)
      toast.success('Thêm địa chỉ thành công')
    }
    showAddressModal.value = false
    await loadAddresses()
  } catch (e) {
    toast.error(e.message)
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
  selectedDistrict.value = null
  selectedWard.value = null
  districts.value = []
  wards.value = []
  shippingFee.value = 0
  if (selectedProvince.value) {
    addressForm.value.thanhPho = selectedProvince.value.ProvinceName
    districts.value = await getDistricts(selectedProvince.value.ProvinceID)
  }
}

const onDistrictChange = async () => {
  selectedWard.value = null
  wards.value = []
  shippingFee.value = 0
  if (selectedDistrict.value) {
    addressForm.value.quan = selectedDistrict.value.DistrictName
    addressForm.value.districtId = selectedDistrict.value.DistrictID
    wards.value = await getWards(selectedDistrict.value.DistrictID)
  }
}

const onWardChange = async () => {
  shippingFee.value = 0
  if (selectedWard.value) {
    addressForm.value.phuong = selectedWard.value.WardName
    addressForm.value.wardCode = selectedWard.value.WardCode
  }
}

watch(selectedAddressId, async (id) => {
  if (!id) return
  const address = addresses.value.find((x) => x.id === id)
  if (address) await calculateShipping(address)
})
</script>

<style scoped>
.checkout-page {
  background-image:
    linear-gradient(rgba(255, 255, 255, 0.72), rgba(248, 250, 252, 0.9)),
    radial-gradient(circle at 1px 1px, rgba(99, 102, 241, 0.12) 1px, transparent 0);
  background-size:
    auto,
    24px 24px;
}
</style>
