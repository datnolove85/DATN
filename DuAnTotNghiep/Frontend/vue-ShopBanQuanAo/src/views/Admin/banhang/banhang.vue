<template>
  <div
    class="pos-wrapper min-h-screen bg-slate-100/90 text-slate-800 font-sans antialiased selection:bg-indigo-500 selection:text-white"
  >
    <!-- ================= HEADER POS ================= -->
    <PosHeader
      ref="headerRef"
      :all-orders="allOrders"
      :current-order-index="currentOrderIndex"
      v-model:search-query="searchQuery"
      :is-dropdown-visible="isDropdownVisible"
      :filtered-products="filteredProducts"
      :get-product-image="getProductImage"
      :format-price="formatPrice"
      :get-variant-discount-percent="getVariantDiscountPercent"
      @switch-order="switchOrder"
      @remove-order="removeOrder"
      @create-new-order="createNewOrder"
      @open-dropdown="openDropdown"
      @close-dropdown="closeDropdown"
      @select-product-from-search="openVariantModalFromSearch"
    />

    <!-- Loading Overlay -->
    <div
      v-if="isLoading"
      class="flex flex-col justify-center items-center py-28 text-slate-500 font-medium text-sm gap-3"
    >
      <div
        class="w-10 h-10 border-4 border-indigo-200 border-t-indigo-600 rounded-full animate-spin"
      ></div>
      <span class="animate-pulse text-xs font-semibold">Đang tải dữ liệu hệ thống POS...</span>
    </div>

    <!-- MAIN CONTENT -->
    <main
      v-else
      class="max-w-[1800px] mx-auto px-4 py-3 grid grid-cols-1 lg:grid-cols-12 gap-4 items-start"
    >
      <!-- ================= BÊN TRÁI: DANH SÁCH SẢN PHẨM & BỘ LỌC ================= -->
      <section class="lg:col-span-7 xl:col-span-8 flex flex-col space-y-3">
        <!-- Thanh Bộ Lọc -->
        <!-- Thanh Bộ Lọc -->
        <PosFilter
          v-model:search-query="searchQuery"
          v-model:filter-category="filterCategory"
          v-model:filter-brand="filterBrand"
          v-model:filter-color="filterColor"
          v-model:filter-size="filterSize"
          v-model:only-sale="onlySale"
          v-model:sort-by="sortBy"
          :danh-muc-list="danhMucList"
          :thuong-hieu-list="thuongHieuList"
          :mau-sac-list="mauSacList"
          :kich-thuoc-list="kichThuocList"
          @reset-filters="resetFilters"
        />
        <!-- Grid Sản Phẩm (Gom nhóm theo Sản phẩm gốc) -->
        <!-- Grid Sản Phẩm (Gom nhóm theo Sản phẩm gốc) -->
        <PosProductGrid
          :products="groupedMasterProducts"
          :get-product-image="getProductImage"
          :format-price="formatPrice"
          :get-variant-discount-percent="getVariantDiscountPercent"
          @select-product="openVariantModal"
        />
      </section>

      <!-- ================= BÊN PHẢI: GIỎ HÀNG & THANH TOÁN ================= -->
      <PosCartSidebar
        :current-order="currentOrder"
        :selected-customer="selectedCustomer"
        :ty-le-quy-doi-xu="tyLeQuyDoiXu"
        :filtered-vouchers="filteredVouchers"
        :best-voucher="bestVoucher"
        :pttt-list="ptttList"
        :coins-used="coinsUsed"
        :coin-discount="coinDiscount"
        :voucher-discount="voucherDiscount"
        :total-cart-price="totalCartPrice"
        :total-need-pay="totalNeedPay"
        :applied-voucher="appliedVoucher"
        :selected-voucher="selectedVoucher"
        :loai-hoa-don="loaiHoaDon"
        v-model:phuong-thuc-thanh-toan="phuongThucThanhToan"
        :is-multi-payment="isMultiPayment"
        :is-cash-payment="isCashPayment"
        :display-cash="displayCash"
        :display-bank="displayBank"
        :display-tien-khach-dua="displayTienKhachDua"
        :tien-thoi-lai="tienThoiLai"
        :get-product-image="getProductImage"
        :set-default-image="setDefaultImage"
        :format-price="formatPrice"
        :get-variant-discount-percent="getVariantDiscountPercent"
        :is-voucher-valid="isVoucherValid"
        :get-voucher-error="getVoucherError"
        @open-customer-modal="openCustomerModal"
        @remove-customer="handleRemoveCustomer"
        @decrease-qty="decreaseQty"
        @increase-qty="increaseQty"
        @remove-from-cart="removeFromCart"
        @change-qty="debounceChangeQty"
        @remove-voucher="handleRemoveVoucher"
        @select-voucher="selectVoucher"
        @coins-input="onCoinsInput"
        @use-max-coins="useMaxCoins"
        @remove-coins="removeCoins"
        @change-payment-method="handlePaymentMethod"
        @cash-input="onCashInput"
        @bank-input="onBankInput"
        @tien-khach-dua-input="onTienKhachDuaInput"
        @submit-checkout="submitCheckout"
      />
    </main>

    <!-- ================= MODAL CHỌN PHÂN LOẠI ================= -->
    <div
      v-if="showVariantModal"
      class="fixed inset-0 bg-slate-900/50 backdrop-blur-xs flex items-center justify-center p-4 z-50 animate-in fade-in duration-200"
    >
      <div
        class="bg-white rounded-2xl max-w-lg w-full p-6 shadow-2xl border border-slate-100 flex flex-col max-h-[90vh]"
      >
        <div class="flex justify-between items-start mb-4 pb-3 border-b border-slate-100">
          <div>
            <span
              class="text-[10px] font-bold text-indigo-600 bg-indigo-50 px-2 py-0.5 rounded uppercase"
            >
              {{ activeMasterProduct?.representativeProduct?.tenThuongHieu }}
            </span>
            <h3 class="text-base font-black text-slate-900 mt-1">
              {{ activeMasterProduct?.tenSanPham }}
            </h3>
          </div>
          <button
            @click="showVariantModal = false"
            class="text-slate-400 hover:text-slate-600 font-bold text-lg p-1 rounded-full hover:bg-slate-100 transition-colors"
          >
            ✕
          </button>
        </div>

        <div class="space-y-4 overflow-y-auto pr-1 custom-scrollbar flex-1">
          <!-- Chọn Màu Sắc -->
          <div>
            <label class="text-xs font-bold text-slate-700 block mb-2">1. Chọn Màu Sắc</label>
            <div class="flex flex-wrap gap-2">
              <button
                v-for="color in availableColorsForActiveMaster"
                :key="color.idMauSac"
                @click="selectedColorId = color.idMauSac"
                :class="[
                  'px-3.5 py-2 rounded-xl text-xs font-bold border transition-all',
                  selectedColorId === color.idMauSac
                    ? 'bg-indigo-600 text-white border-indigo-600 shadow-sm'
                    : 'bg-white text-slate-700 border-slate-200 hover:border-indigo-300 hover:bg-slate-50',
                ]"
              >
                {{ color.tenMauSac }}
              </button>
            </div>
          </div>

          <!-- Chọn Kích Thước -->
          <div>
            <label class="text-xs font-bold text-slate-700 block mb-2">2. Chọn Kích Thước</label>
            <div class="flex flex-wrap gap-2">
              <button
                v-for="size in availableSizesForSelectedColor"
                :key="size.idKichThuoc"
                @click="selectedSizeId = size.idKichThuoc"
                :class="[
                  'px-3.5 py-2 rounded-xl text-xs font-bold border transition-all',
                  selectedSizeId === size.idKichThuoc
                    ? 'bg-indigo-600 text-white border-indigo-600 shadow-sm'
                    : size.soKhaDung <= 0
                      ? 'bg-slate-100 text-slate-400 border-slate-200 opacity-60 cursor-not-allowed'
                      : 'bg-white text-slate-700 border-slate-200 hover:border-indigo-300 hover:bg-slate-50',
                ]"
                :disabled="size.soKhaDung <= 0"
              >
                {{ size.tenKichThuoc }}
                <span class="text-[10px] ml-1 opacity-80">({{ size.soKhaDung }})</span>
              </button>
            </div>
          </div>

          <!-- Thông tin biến thể đang chọn -->
          <div
            v-if="selectedVariant"
            class="bg-indigo-50/70 p-4 rounded-xl border border-indigo-100 flex items-center justify-between mt-2"
          >
            <div>
              <p class="text-[11px] text-slate-500 font-medium">
                Mã SPCT:
                <span class="font-mono font-bold text-slate-700">{{
                  selectedVariant.maSanPhamChiTiet
                }}</span>
              </p>
              <div class="mt-1 flex items-center gap-2">
                <span class="text-xs font-bold text-indigo-700"
                  >🎨 {{ selectedVariant.tenMauSac }}</span
                >
                <span class="text-xs font-bold text-indigo-700"
                  >📏 {{ selectedVariant.tenKichThuoc }}</span
                >
              </div>
            </div>
            <div class="text-right">
              <template v-if="selectedVariant.dangGiamGia">
                <p class="text-sm font-black text-rose-600">
                  {{ formatPrice(selectedVariant.giaSauGiam) }}
                </p>
                <div class="flex items-center justify-end gap-1.5 mt-0.5">
                  <span
                    v-if="getVariantDiscountPercent(selectedVariant) > 0"
                    class="bg-rose-100 text-rose-700 text-[10px] font-bold px-1.5 py-0.2 rounded"
                  >
                    -{{ getVariantDiscountPercent(selectedVariant) }}%
                  </span>
                  <span class="text-xs text-slate-400 line-through font-medium">
                    {{ formatPrice(selectedVariant.giaBan) }}
                  </span>
                </div>
              </template>
              <template v-else>
                <p class="text-sm font-black text-indigo-600">
                  {{ formatPrice(selectedVariant.giaBan) }}
                </p>
              </template>
              <span class="text-[10px] font-bold text-emerald-600 block mt-1"
                >Kho: {{ selectedVariant.soLuongKhaDung ?? selectedVariant.soLuongTon }}</span
              >
            </div>
          </div>
        </div>

        <div class="pt-4 border-t border-slate-100 mt-4 flex gap-3">
          <button
            @click="showVariantModal = false"
            class="flex-1 bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold py-2.5 rounded-xl text-xs transition-colors"
          >
            Hủy bỏ
          </button>
          <button
            @click="confirmAddVariantToCart"
            :disabled="
              !selectedVariant ||
              (selectedVariant.soLuongKhaDung ?? selectedVariant.soLuongTon) <= 0
            "
            class="flex-1 bg-indigo-600 hover:bg-indigo-700 disabled:opacity-50 disabled:cursor-not-allowed text-white font-bold py-2.5 rounded-xl text-xs shadow-md shadow-indigo-200 transition-all flex items-center justify-center gap-1.5"
          >
            <span>Thêm vào giỏ hàng</span>
          </button>
        </div>
      </div>
    </div>

    <!-- ================= MODAL CHỌN KHÁCH HÀNG ================= -->
    <div
      v-if="showCustomerModal"
      class="fixed inset-0 bg-slate-900/40 backdrop-blur-xs flex items-center justify-center p-4 z-50"
    >
      <div
        class="bg-white rounded-2xl max-w-md w-full p-5 shadow-2xl border border-slate-100 animate-in fade-in zoom-in-95 duration-150"
      >
        <div class="flex justify-between items-center mb-3">
          <h3 class="text-sm font-bold text-slate-900">Chọn Khách Hàng Hệ Thống</h3>
          <button
            @click="showCustomerModal = false"
            class="text-slate-400 hover:text-slate-600 font-bold text-sm"
          >
            ✕
          </button>
        </div>

        <input
          type="text"
          v-model="searchCustomerQuery"
          placeholder="Tìm theo tên hoặc SĐT..."
          class="w-full bg-slate-50 border border-slate-200 rounded-lg px-3 py-1.5 text-xs mb-2.5 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 font-medium"
        />

        <div class="max-h-60 overflow-y-auto space-y-2 mb-1 custom-scrollbar">
          <div class="bg-indigo-50/50 p-2.5 rounded-xl border border-indigo-100 mb-2">
            <p class="text-[9px] font-bold text-indigo-700 uppercase mb-1.5">Thêm khách hàng mới</p>
            <input
              v-model="newCust.hoTen"
              placeholder="Họ tên khách hàng"
              class="w-full p-1.5 mb-1.5 rounded-lg border border-slate-200 bg-white text-xs font-medium"
            />
            <input
              v-model="newCust.sdt"
              placeholder="Số điện thoại"
              class="w-full p-1.5 rounded-lg border border-slate-200 bg-white text-xs font-medium"
            />
            <button
              @click="saveNewCustomer"
              class="w-full mt-2 bg-indigo-600 hover:bg-indigo-700 text-white py-1.5 rounded-lg text-xs font-bold transition-colors"
            >
              Lưu & Chọn khách hàng
            </button>
          </div>

          <div
            v-for="kh in filteredCustomers"
            :key="kh.id"
            @click="selectCustomer(kh)"
            class="p-2.5 border border-slate-100 rounded-lg hover:bg-indigo-50/60 cursor-pointer flex justify-between items-center transition-colors"
          >
            <div>
              <p class="text-xs font-bold text-slate-800">{{ kh.hoTen }}</p>
              <p class="text-[10px] text-slate-500 font-medium">{{ kh.soDienThoai }}</p>
              <p class="text-[10px] text-amber-600 font-bold mt-0.5 flex items-center gap-1">
                <span>🪙</span> {{ kh.soDuXu ?? 0 }} xu
              </p>
            </div>
            <span
              class="text-[9px] font-bold px-1.5 py-0.5 rounded bg-amber-100 text-amber-800 self-start"
            >
              {{ kh.hangThanhVien || 'Thành viên' }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- Modals Phụ Khác -->
    <InvoiceModal v-if="showInvoiceModal" :hoaDon="hoaDonPrint" @close="handleCloseInvoice" />
    <PaymentQrDialog
      v-model:visible="showQrDialog"
      :qr-url="qrData.qrUrl"
      :ma-hoa-don="qrData.maHoaDon"
      :tong-tien="qrData.tongTien"
      @paid="xacNhanDaThanhToan"
    />
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted, onBeforeUnmount } from 'vue'
import { useToast } from 'vue-toastification'
import Swal from 'sweetalert2'
import { debounce } from 'lodash-es'

import PosCartSidebar from '@/views/Admin/banhang/components/PosCartSidebar.vue'
import PosHeader from '@/views/Admin/banhang/components/PosHeader.vue'
import PosFilter from '@/views/Admin/banhang/components/PosProductFilter.vue'
import PosProductGrid from '@/views/Admin/banhang/components/PosProductGrid.vue'

const headerRef = ref(null) // Dùng để gọi hàm focus ô search nếu cần

import stompClient from '@/socket'
import InvoiceModal from './InvoiceModal.vue'
import PaymentQrDialog from '@/views/shop/components/PaymentQrDialog.vue'

// --- SERVICES ---
import { getAllDanhMuc } from '@/service/DanhMucService'
import { getAllKichThuoc } from '@/service/KichThuocService'
import { getAllMauSac } from '@/service/MauSacService'
import { getAllThuongHieu } from '@/service/ThuongHieuService'
import { getAllSanPhamChiTiet } from '@/service/SanPhamChiTiet'
import { getAllPTTT } from '@/service/PhuongThucThanhToanService'
import { getALLKhachHang, addKhachHangQuickService } from '@/service/KhachHangService'
import { getAllVoucher } from '@/service/VoucherService'
import {
  getHoadonById,
  getHoaDonCho,
  taoHoaDonCho,
  themSanPhamVaoHoaDon,
  giamSoLuongSanPham,
  tangSoLuongSanPham,
  ganKhachHang,
  thanhToanHoaDon,
  huyHoaDon,
  apVoucher,
  boVoucher,
  taoQr,
  capNhatSoLuong,
  xoaSanPhamKhoiHoaDon,
  goKhachHang,
  apDungXu,
} from '@/service/HoaDonService'

import { cauHinhService } from '@/service/cauHinhService.js'

// Thêm vào phần script setup của banhang.vue
const tyLeQuyDoiXu = ref(1000) // Giá trị mặc định phòng hờ
const tyLeGiamToiDaXu = ref(50) // Giá trị mặc định phòng hờ

const loadCauHinhHeThong = async () => {
  try {
    // Gọi thông qua cauHinhService đã định nghĩa thay vì axios thuần sai đường dẫn
    const configs = await cauHinhService.getCauHinhHeThong()

    // Tìm cấu hình quy đổi xu
    const quyDoiConfig = configs.find((c) => c.maCauHinh === 'TY_LE_QUY_DOI_XU')
    if (quyDoiConfig && quyDoiConfig.giaTriSo != null) {
      tyLeQuyDoiXu.value = Number(quyDoiConfig.giaTriSo)
    }

    // Tìm cấu hình giảm tối đa xu
    const giamToiDaConfig = configs.find((c) => c.maCauHinh === 'TY_LE_GIAM_TOI_DA_XU')
    if (giamToiDaConfig && giamToiDaConfig.giaTriSo != null) {
      tyLeGiamToiDaXu.value = Number(giamToiDaConfig.giaTriSo)
    }
  } catch (error) {
    console.error('Không tải được cấu hình hệ thống:', error)
  }
}

const toast = useToast()
const DEFAULT_PRODUCT_IMAGE = 'https://images.unsplash.com/photo-1523381210434-271e8be1f52b?w=400'
const max_oder_waiting = 6
const user = JSON.parse(sessionStorage.getItem('user')) || {}

const danhMucList = ref([])
const thuongHieuList = ref([])
const mauSacList = ref([])
const kichThuocList = ref([])
const products = ref([])
const customers = ref([])
const vouchers = ref([])
const ptttList = ref([])
const isLoading = ref(true)
const defaultPTTTId = ref(null)

// --- STATE MODAL CHỌN PHÂN LOẠI ---
const showVariantModal = ref(false)
const activeMasterProduct = ref(null)
const selectedColorId = ref(null)
const selectedSizeId = ref(null)

const isMultiPayment = ref(false)
const paymentCash = ref(0)
const paymentBank = ref(0)
const pendingCheckoutPayload = ref(null)

const tienKhachDua = ref(0)
const displayTienKhachDua = ref('')

const getVariantDiscountPercent = (v) => {
  if (!v.dangGiamGia) return 0
  if (v.phanTramGiam && v.phanTramGiam > 0) return v.phanTramGiam
  if (v.giaBan > 0 && v.giaSauGiam < v.giaBan) {
    return Math.round(((v.giaBan - v.giaSauGiam) / v.giaBan) * 100)
  }
  return 0
}

const handleBankInput = () => {
  const bank = Number(paymentBank.value) || 0
  const needed = totalNeedPay.value
  const cash = needed - bank
  paymentCash.value = cash > 0 ? cash : 0
  displayCash.value = formatCurrencyInput(paymentCash.value)
}

const resetPaymentForm = () => {
  isMultiPayment.value = false
  paymentCash.value = 0
  paymentBank.value = 0
  tienKhachDua.value = 0
  displayTienKhachDua.value = ''
  displayCash.value = ''
  displayBank.value = ''

  const cashMethod = ptttList.value.find(
    (p) =>
      ['CASH', 'TIEN_MAT'].includes(p.maPhuongThuc?.toUpperCase()) ||
      p.tenPhuongThuc?.toLowerCase().includes('tiền mặt'),
  )
  const cashId = cashMethod ? cashMethod.id : defaultPTTTId.value

  if (currentOrder.value) {
    currentOrder.value.phuongThucThanhToan = cashId
  }
}

const isCashPayment = computed(() => {
  const selectedPttt = ptttList.value.find((p) => p.id === Number(phuongThucThanhToan.value))
  if (!selectedPttt) return true
  const code = selectedPttt.maPhuongThuc?.toUpperCase()
  const name = selectedPttt.tenPhuongThuc?.toLowerCase() || ''
  return ['CASH', 'TIEN_MAT'].includes(code) || name.includes('tiền mặt')
})

const tienThoiLai = computed(() => {
  const khachDua = Number(tienKhachDua.value) || 0
  const canTra = totalNeedPay.value
  return khachDua > canTra ? khachDua - canTra : 0
})

const onTienKhachDuaInput = (e) => {
  const rawValue = e.target.value.replace(/\D/g, '')
  const numVal = rawValue ? Number(rawValue) : 0
  tienKhachDua.value = numVal
  displayTienKhachDua.value = formatCurrencyInput(numVal)
}

const allOrders = ref([
  {
    cart: [],
    selectedCustomer: null,
    appliedVoucher: null,
    voucherQuery: '',
    coinsUsed: 0, // Lưu số xu sử dụng cho từng hóa đơn chờ
    loaiHoaDon: 'tai_quay',
    phuongThucThanhToan: '',
  },
])
const currentOrderIndex = ref(0)
const selectedVoucher = ref(null)
const editingQty = reactive({})

const searchQuery = ref('')
const filterCategory = ref('')
const filterBrand = ref('')
const filterColor = ref('')
const filterSize = ref('')

const onlySale = ref(false) // Lọc sản phẩm đang giảm giá
const sortBy = ref('newest') // Thuật toán sắp xếp (newest, price-asc, price-desc)
const voucherCode = ref('')
const searchCustomerQuery = ref('')
const newCust = ref({ hoTen: '', sdt: '' })

const hoaDonPrint = ref(null)
const showInvoiceModal = ref(false)
const showCustomerModal = ref(false)
const showVoucherDropdown = ref(false)
const isDropdownVisible = ref(false)

const searchInput = ref(null)
const voucherRef = ref(null)

const pendingPaidOrderId = ref(null)

const showQrDialog = ref(false)
const qrData = ref({
  qrUrl: '',
  maHoaDon: '',
  tongTien: 0,
})

// --- COMPUTED LIÊN QUAN ĐẾN XU ĐỔI GIẢM GIÁ ---
// 1. Số lượng xu đang sử dụng lấy từ đơn hàng hiện tại (đồng bộ với DB)
const coinsUsed = computed({
  get: () => currentOrder.value?.soXuSuDung || currentOrder.value?.coinsUsed || 0,
  set: (v) => {
    if (currentOrder.value) currentOrder.value.coinsUsed = v
  },
})

// 2. Tổng tiền hàng lấy trực tiếp từ Backend
const totalCartPrice = computed(() => currentOrder.value?.tongTienHang || 0)

// 3. Tiền giảm do Xu = số xu sử dụng * tỷ giá quy đổi
const coinDiscount = computed(() => {
  const soXu = currentOrder.value?.soXuSuDung ?? coinsUsed.value ?? 0
  return soXu * tyLeQuyDoiXu.value
})

// 4. Vì Backend trả về `tongGiamGia` là tổng gộp (Voucher + Xu),
// nên giảm giá Voucher = tổng giảm giá - tiền giảm từ xu
const voucherDiscount = computed(() => {
  const tongGiamGiaBackend = currentOrder.value?.tongGiamGia || 0
  const giamDoXu = coinDiscount.value
  return Math.max(0, tongGiamGiaBackend - giamDoXu)
})

// 5. Tổng thanh toán LẤY TRỰC TIẾP TỪ BACKEND (đảm bảo chuẩn khớp 100% với DB)
const totalNeedPay = computed(() => {
  return currentOrder.value?.tongThanhToan || 0
})

const maxCoinsAllowed = computed(() => {
  if (!selectedCustomer.value) return 0

  const tongTien = Number(totalCartPrice.value || 0)
  const soDuXu = Number(selectedCustomer.value.soDuXu || 0)

  // Tiền giảm tối đa theo %
  const tienGiamToiDaTheoPhanTram = tongTien * (tyLeGiamToiDaXu.value / 100)

  // Quy đổi ra số xu (làm tròn xuống giống BE)
  const maxXuTheoPhanTram = Math.floor(tienGiamToiDaTheoPhanTram / tyLeQuyDoiXu.value)

  // Không vượt quá tổng tiền
  const maxXuTheoTongTien = Math.floor(tongTien / tyLeQuyDoiXu.value)

  return Math.min(soDuXu, maxXuTheoPhanTram, maxXuTheoTongTien)
})

// --- XỬ LÝ GỌI API ÁP DỤNG XU ---
const applyCoinsToApi = async (soXu) => {
  if (!currentOrder.value?.id) return
  try {
    // Gọi API apDungXu (truyền id hóa đơn và số xu sử dụng)
    await apDungXu(currentOrder.value.id, soXu)
    // Load lại chi tiết hóa đơn để server tính toán lại tổng tiền hàng, giảm giá và tổng thanh toán
    await loadChiTietHoaDon(currentOrder.value.id)
  } catch (error) {
    const errorMsg = error?.response?.data?.message || 'Không thể áp dụng xu!'
    toast.error(errorMsg)
  }
}

// Sử dụng debounce để tránh gọi API quá nhiều lần khi người dùng đang gõ phím liên tục
const debounceApplyCoins = debounce(async (soXu) => {
  await applyCoinsToApi(soXu)
}, 600)

// --- CẬP NHẬT CÁC HÀM XỬ LÝ SỰ KIỆN XU ---
const onCoinsInput = (e) => {
  if (!selectedCustomer.value) {
    toast.warning('Vui lòng chọn khách hàng trước khi dùng xu!')
    coinsUsed.value = 0
    return
  }
  const raw = e.target.value.replace(/\D/g, '')
  let val = raw ? Number(raw) : 0
  const max = maxCoinsAllowed.value
  if (val > max) {
    val = max
    toast.warning(`Chỉ được dùng tối đa ${max} xu cho đơn hàng này!`)
  }
  coinsUsed.value = val

  // Tự động gọi API có debounce khi người dùng đang nhập
  debounceApplyCoins(val)
}

const useMaxCoins = async () => {
  if (!selectedCustomer.value || (selectedCustomer.value.soDuXu ?? 0) <= 0) {
    toast.warning('Khách hàng không có xu tích lũy!')
    return
  }
  coinsUsed.value = maxCoinsAllowed.value

  // Gọi API ngay lập tức khi bấm nút "Dùng hết"
  await applyCoinsToApi(coinsUsed.value)
}

const removeCoins = async () => {
  coinsUsed.value = 0

  // Gọi API với số xu = 0 để gỡ bỏ xu trên server
  await applyCoinsToApi(0)
}

const mapKhoVoucherToStandardVoucher = (khoVc) => {
  return {
    id: khoVc.id,
    idVoucher: khoVc.idVoucher || khoVc.voucherId || khoVc.idVoucherGoc,
    idVoucherKhachHang: khoVc.idVoucherKhachHang || khoVc.id,
    maCode: khoVc.maCode,
    maVoucher: khoVc.maCode,
    tenVoucher: khoVc.tenVoucher,
    loaiGiamGia: khoVc.loaiGiamGia,
    giaTriGiam: khoVc.giaTriGiam,
    giaTriDonHangToiThieu: khoVc.dieuKienToiThieu,
    giaTriGiamToiDa: khoVc.giaTriGiamToiDa,
    soLuong: khoVc.soLuongConLai,
    soLuongDaDung: 0,
    ngayBatDau: khoVc.ngayBatDau,
    ngayKetThuc: khoVc.ngayHetHan,
    moTa: khoVc.moTa || `Đổi bằng ${khoVc.soXuDoi} xu`,
    trangThai: khoVc.trangThai ? 1 : 0,
    isCustomerVoucher: true,
  }
}

const currentOrder = computed(() => {
  if (allOrders.value.length === 0 || currentOrderIndex.value < 0) {
    return { cart: [] }
  }
  return allOrders.value[currentOrderIndex.value] || { cart: [] }
})

const selectedCustomer = computed({
  get: () => currentOrder.value?.selectedCustomer || null,
  set: (v) => {
    if (currentOrder.value) currentOrder.value.selectedCustomer = v
  },
})

const appliedVoucher = computed({
  get: () => currentOrder.value?.appliedVoucher || null,
  set: (v) => {
    if (currentOrder.value) currentOrder.value.appliedVoucher = v
  },
})

const voucherQuery = computed({
  get: () => currentOrder.value?.voucherQuery || '',
  set: (v) => {
    if (currentOrder.value) currentOrder.value.voucherQuery = v
  },
})

const loaiHoaDon = computed({
  get: () => currentOrder.value?.loaiHoaDon || 'tai_quay',
  set: (v) => {
    if (currentOrder.value) currentOrder.value.loaiHoaDon = v
  },
})

const phuongThucThanhToan = computed({
  get: () => currentOrder.value?.phuongThucThanhToan || '',
  set: (v) => {
    if (currentOrder.value) currentOrder.value.phuongThucThanhToan = v
  },
})

const hasCurrentOrder = computed(() => !!currentOrder.value?.id)
const filteredProducts = computed(() => {
  // 1. Lọc sản phẩm
  let result = products.value.filter((sp) => {
    const nameStr = (sp.tenSanPham || '').toLowerCase()
    const codeStr = (sp.maSanPhamChiTiet || '').toLowerCase()
    const searchStr = searchQuery.value.toLowerCase().trim()

    const matchSearch = !searchStr || nameStr.includes(searchStr) || codeStr.includes(searchStr)

    const selectedDM = danhMucList.value.find((dm) => dm.id == filterCategory.value)
    const selectedTH = thuongHieuList.value.find((th) => th.id == filterBrand.value)

    const matchCategory = !filterCategory.value || sp.tenDanhMuc === selectedDM?.tenDanhMuc
    const matchBrand = !filterBrand.value || sp.tenThuongHieu === selectedTH?.tenThuongHieu
    const matchColor = !filterColor.value || Number(sp.idMauSac) === Number(filterColor.value)
    const matchSize = !filterSize.value || Number(sp.idKichThuoc) === Number(filterSize.value)

    // Lọc theo trạng thái giảm giá
    const matchSale = !onlySale.value || sp.dangGiamGia === true

    return matchSearch && matchCategory && matchBrand && matchColor && matchSize && matchSale
  })

  // 2. Sắp xếp danh sách sau khi lọc
  if (sortBy.value === 'price-asc') {
    result.sort(
      (a, b) =>
        (a.dangGiamGia ? a.giaSauGiam : a.giaBan) - (b.dangGiamGia ? b.giaSauGiam : b.giaBan),
    )
  } else if (sortBy.value === 'price-desc') {
    result.sort(
      (a, b) =>
        (b.dangGiamGia ? b.giaSauGiam : b.giaBan) - (a.dangGiamGia ? a.giaSauGiam : a.giaBan),
    )
  } else if (sortBy.value === 'newest') {
    result.sort((a, b) => new Date(b.ngayTao || 0) - new Date(a.ngayTao || 0))
  }

  return result
})

// Cập nhật hàm Reset bộ lọc
const resetFilters = () => {
  filterCategory.value = ''
  filterBrand.value = ''
  filterColor.value = ''
  filterSize.value = ''
  searchQuery.value = ''
  onlySale.value = false
  sortBy.value = 'newest'
}
const groupedMasterProducts = computed(() => {
  const map = new Map()

  filteredProducts.value.forEach((sp) => {
    const key = sp.tenSanPham || 'Khác'
    if (!map.has(key)) {
      map.set(key, {
        tenSanPham: key,
        representativeProduct: sp,
        variants: [],
        totalStock: 0,
        minPrice: Infinity,
        maxPrice: 0,
        hasDiscount: false,
        maxDiscountPercent: 0,
      })
    }

    const group = map.get(key)
    group.variants.push(sp)

    const stock = sp.soLuongKhaDung ?? sp.soLuongTon ?? 0
    group.totalStock += stock

    const price = sp.dangGiamGia ? sp.giaSauGiam : sp.giaBan
    if (price < group.minPrice) group.minPrice = price
    if (price > group.maxPrice) group.maxPrice = price

    if (sp.dangGiamGia) {
      group.hasDiscount = true
      const percent = getVariantDiscountPercent(sp)
      if (percent > group.maxDiscountPercent) {
        group.maxDiscountPercent = percent
      }
    }
  })

  return Array.from(map.values()).map((group) => {
    const rep = group.representativeProduct || {}
    const minP = group.minPrice === Infinity ? 0 : group.minPrice

    let priceFormatted = ''
    if (group.minPrice === group.maxPrice || group.minPrice === Infinity) {
      priceFormatted = formatPrice(minP)
    } else {
      priceFormatted = `${formatPrice(group.minPrice)} - ${formatPrice(group.maxPrice)}`
    }

    return {
      ...rep, // Lấy các thuộc tính mặc định của sản phẩm đại diện
      ...group,
      // Map đúng tên biến mà PosProductGrid.vue sử dụng
      giaBan: minP,
      giaSauGiam: minP,
      soLuongKhaDung: group.totalStock,
      soLuongTon: group.totalStock,
      image: rep.image,
      images: rep.images,
      dangGiamGia: group.hasDiscount,
      phanTramGiam: group.maxDiscountPercent,
      priceFormatted,
    }
  })
})

const openVariantModal = (master) => {
  activeMasterProduct.value = master
  const firstColor = availableColorsForActiveMaster.value[0]
  selectedColorId.value = firstColor ? firstColor.idMauSac : null

  const firstValidSize =
    availableSizesForSelectedColor.value.find((s) => s.soKhaDung > 0) ||
    availableSizesForSelectedColor.value[0]
  selectedSizeId.value = firstValidSize ? firstValidSize.idKichThuoc : null

  showVariantModal.value = true
}

const openVariantModalFromSearch = (sp) => {
  const master = groupedMasterProducts.value.find((m) => m.tenSanPham === sp.tenSanPham)
  if (master) {
    openVariantModal(master)
    selectedColorId.value = sp.idMauSac
    selectedSizeId.value = sp.idKichThuoc
  }
}

const availableColorsForActiveMaster = computed(() => {
  if (!activeMasterProduct.value) return []
  const colorMap = new Map()
  activeMasterProduct.value.variants.forEach((v) => {
    if (!colorMap.has(v.idMauSac)) {
      colorMap.set(v.idMauSac, {
        idMauSac: v.idMauSac,
        tenMauSac: v.tenMauSac,
      })
    }
  })
  return Array.from(colorMap.values())
})

const availableSizesForSelectedColor = computed(() => {
  if (!activeMasterProduct.value || !selectedColorId.value) return []
  return activeMasterProduct.value.variants
    .filter((v) => Number(v.idMauSac) === Number(selectedColorId.value))
    .map((v) => ({
      idKichThuoc: v.idKichThuoc,
      tenKichThuoc: v.tenKichThuoc,
      soKhaDung: v.soLuongKhaDung ?? v.soLuongTon ?? 0,
    }))
})

watch(selectedColorId, () => {
  const firstValidSize = availableSizesForSelectedColor.value.find((s) => s.soKhaDung > 0)
  if (firstValidSize) {
    selectedSizeId.value = firstValidSize.idKichThuoc
  } else if (availableSizesForSelectedColor.value.length > 0) {
    selectedSizeId.value = availableSizesForSelectedColor.value[0].idKichThuoc
  }
})

const selectedVariant = computed(() => {
  if (!activeMasterProduct.value || !selectedColorId.value || !selectedSizeId.value) return null
  return (
    activeMasterProduct.value.variants.find(
      (v) =>
        Number(v.idMauSac) === Number(selectedColorId.value) &&
        Number(v.idKichThuoc) === Number(selectedSizeId.value),
    ) || null
  )
})

const confirmAddVariantToCart = async () => {
  if (!selectedVariant.value) return
  showVariantModal.value = false
  await addToCart(selectedVariant.value)
}

const filteredCustomers = computed(() => {
  return customers.value.filter(
    (kh) =>
      kh.hoTen.toLowerCase().includes(searchCustomerQuery.value.toLowerCase()) ||
      kh.soDienThoai.includes(searchCustomerQuery.value),
  )
})
const customerVouchers = ref([])

const filteredVouchers = computed(() => {
  const combinedMap = new Map()
  vouchers.value.forEach((vc) => combinedMap.set(`sys_${vc.id}`, vc))
  customerVouchers.value.forEach((vc) => combinedMap.set(`cust_${vc.id}`, vc))

  const list = Array.from(combinedMap.values())
  list.sort((a, b) => {
    const validA = isVoucherValid(a)
    const validB = isVoucherValid(b)
    if (validA !== validB) return Number(validB) - Number(validA)
    return tinhTienGiam(b) - tinhTienGiam(a)
  })
  return list
})

const bestVoucher = computed(() => filteredVouchers.value.find(isVoucherValid) ?? null)

const loadAllDataFromAPI = async () => {
  try {
    isLoading.value = true
    const [resDM, resKT, resMS, resTH, resSPCT, resKH, resVoucher] = await Promise.all([
      getAllDanhMuc(),
      getAllKichThuoc(),
      getAllMauSac(),
      getAllThuongHieu(),
      getAllSanPhamChiTiet(),
      getALLKhachHang(),
      getAllVoucher(),
    ])
    danhMucList.value = resDM
    kichThuocList.value = resKT
    mauSacList.value = resMS
    thuongHieuList.value = resTH
    products.value = resSPCT
    customers.value = resKH
    vouchers.value = resVoucher
  } catch (error) {
    console.error(error)
    toast.error('Không thể tải dữ liệu. Hãy kiểm tra lại Server!')
  } finally {
    isLoading.value = false
  }
}

const loadPTTT = async () => {
  const data = await getAllPTTT()
  ptttList.value = data.filter(
    (pt) =>
      pt.trangThai &&
      ['CASH', 'BANK', 'MIXED', 'TIEN_MAT', 'CHUYEN_KHOAN', 'KET_HOP'].includes(
        pt.maPhuongThuc?.toUpperCase(),
      ),
  )

  const defaultId =
    ptttList.value.find((pt) => ['CASH', 'TIEN_MAT'].includes(pt.maPhuongThuc?.toUpperCase()))
      ?.id ?? ptttList.value[0]?.id
  defaultPTTTId.value = defaultId

  if (currentOrder.value && !currentOrder.value.phuongThucThanhToan) {
    currentOrder.value.phuongThucThanhToan = defaultId
  }
}

const loadChiTietHoaDon = async (idHoaDon) => {
  try {
    const data = await getHoadonById(idHoaDon)
    const order = allOrders.value.find((o) => o.id === idHoaDon)
    if (!order) return

    order.cart = data.sanPhams.map((item) => {
      const foundSp = products.value.find(
        (p) =>
          Number(p.id) === Number(item.idSanPhamChiTiet) ||
          Number(p.idSanPhamChiTiet) === Number(item.idSanPhamChiTiet),
      )

      return {
        id: item.id,
        product: {
          idSanPhamChiTiet: item.idSanPhamChiTiet,
          maSPCT: item.maSanPhamChiTiet,
          id: item.idSanPhamChiTiet,
          tenSanPhamChiTiet: item.tenSanPham,
          giaBan: foundSp ? foundSp.giaBan : item.donGia,
          giaSauGiam: foundSp ? foundSp.giaSauGiam : item.donGia,
          dangGiamGia: foundSp ? foundSp.dangGiamGia : false,
          phanTramGiam: foundSp ? foundSp.phanTramGiam : 0,
          tenMauSac: item.tenMauSac,
          tenKichThuoc: item.tenKichThuoc,
          image: item.anh,
        },
        soLuong: item.soLuong,
        thanhTien: item.thanhTien,
      }
    })

    order.cart.forEach((item) => {
      editingQty[item.id] = item.soLuong
    })

    order.appliedVoucher = data.voucher || null
    order.voucherQuery = data.voucher?.maVoucher || ''
    selectedVoucher.value = data.voucher || null
    appliedVoucher.value = data.voucher || null
    voucherQuery.value = data.voucher?.maVoucher || data.voucher?.maCode || ''
    voucherCode.value = data.voucher?.maVoucher || data.voucher?.maCode || ''

    order.tongTienHang = data.tongTienHang
    order.tongGiamGia = data.tongGiamGia
    order.tongThanhToan = data.tongThanhToan
    order.coinsUsed = data.soXuSuDung || 0
    coinsUsed.value = data.soXuSuDung || 0

    if (data.idKhachHang) {
      const foundCust = customers.value.find((c) => Number(c.id) === Number(data.idKhachHang))
      order.selectedCustomer = foundCust || {
        id: data.idKhachHang,
        hoTen: data.tenKhachHang,
        soDienThoai: data.soDienThoaiKhachHang,
        soDuXu: 0,
      }
    } else {
      order.selectedCustomer = null
    }

    if (data.idKhachHang) {
      try {
        const res = await ganKhachHang(idHoaDon, data.idKhachHang)
        let rawData = res.data !== undefined ? res.data : res
        if (typeof rawData === 'string') {
          try {
            rawData = JSON.parse(rawData)
          } catch (e) {
            rawData = []
          }
        }
        if (Array.isArray(rawData) && rawData.length > 0) {
          customerVouchers.value = rawData.map(mapKhoVoucherToStandardVoucher)
        } else {
          customerVouchers.value = []
        }
      } catch (err) {
        customerVouchers.value = []
      }
    } else {
      customerVouchers.value = []
    }

    allOrders.value = [...allOrders.value]
  } catch (error) {
    console.error(error)
  }
}

const loadProducts = async () => {
  products.value = await getAllSanPhamChiTiet()
}

let socketSubscription = null

const connectSocket = () => {
  if (stompClient.connected) {
    subscribePos()
  } else {
    stompClient.onConnect = () => {
      subscribePos()
    }
  }
}
// Khai báo biến timeout ở bên ngoài hàm subscribePos để lưu trạng thái giữa các message
let voucherTimeout = null

const subscribePos = () => {
  if (socketSubscription) {
    socketSubscription.unsubscribe()
  }

  socketSubscription = stompClient.subscribe('/topic/pos', async (msg) => {
    try {
      await loadProducts()
      if (currentOrder.value?.id) {
        await loadChiTietHoaDon(currentOrder.value.id)
      }

      if (msg && msg.body) {
        let data = {}
        let isJson = false

        try {
          data = JSON.parse(msg.body)
          isJson = true
        } catch (e) {}

        const eventType = isJson ? data.type || data.eventType || data.action : ''

        if (eventType === 'QUANTITY_UPDATED') return

        // Xử lý chung cho các sự kiện liên quan đến voucher để gom nhóm tránh lặp toast
        if (
          eventType === 'VOUCHER_UPDATED' ||
          eventType === 'VOUCHER_REMOVED' ||
          eventType === 'KHO_VOUCHER_UPDATED'
        ) {
          if (voucherTimeout) clearTimeout(voucherTimeout)

          voucherTimeout = setTimeout(async () => {
            vouchers.value = await getAllVoucher()
            if (currentOrder.value?.id) {
              await loadChiTietHoaDon(currentOrder.value.id)
            }
            toast.warning('Danh sách Voucher đã được thay đổi. Vui lòng kiểm tra lại!')
          }, 300) // Đợi 300ms, nếu có sự kiện tiếp theo cùng nhóm thì reset lại bộ đếm và chỉ chạy 1 lần cuối

          return
        }

        if (eventType === 'DISCOUNT_UPDATED') {
          await loadProducts()
          if (currentOrder.value?.id) {
            await loadChiTietHoaDon(currentOrder.value.id)
          }
          toast.info('Đợt giảm giá vừa được cập nhật!')
          return
        }

        if (eventType === 'PRODUCT_UPDATED') {
          toast.warning('Thông tin sản phẩm đã thay đổi. Vui lòng kiểm tra lại!')
          return
        }
        if (eventType === 'INVOICE_PAID') {
          toast.success('Thanh toán thành công!')
          return
        }
        if (eventType === 'ORDER_CANCELLED') {
          toast.warning(data.message)
          const hoaDonData = await getHoaDonCho(user.id)
          allOrders.value = hoaDonData.map((hd) => ({
            id: hd.id,
            maHoaDon: hd.maHoaDon,
            cart: [],
            selectedCustomer: null,
            appliedVoucher: hd.voucher || null,
            voucherQuery: hd.voucher?.maVoucher || '',
            coinsUsed: hd.soXuSuDung,
            loaiHoaDon: 'tai_quay',
            phuongThucThanhToan: defaultPTTTId.value,
          }))

          if (allOrders.value.length > 0) {
            currentOrderIndex.value = 0
            await loadChiTietHoaDon(allOrders.value[0].id)
          }
          return
        }

        const noiDungThongBao = isJson
          ? data.message || data.noiDung || data.content || msg.body
          : msg.body

        if (noiDungThongBao) toast.info(noiDungThongBao)
      }
    } catch (error) {
      console.error('Lỗi socket:', error)
    }
  })
}

const displayCash = ref('')
const displayBank = ref('')

const formatCurrencyInput = (value) => {
  if (!value && value !== 0) return ''
  const numericValue = String(value).replace(/\D/g, '')
  if (!numericValue) return ''
  return new Intl.NumberFormat('vi-VN').format(Number(numericValue))
}

const onCashInput = (e) => {
  const rawValue = e.target.value.replace(/\D/g, '')
  let numVal = rawValue ? Number(rawValue) : 0
  const needed = totalNeedPay.value

  if (numVal > needed) {
    numVal = needed
    toast.warning('Số tiền nhập không được vượt quá tổng hóa đơn!')
  }

  paymentCash.value = numVal
  displayCash.value = formatCurrencyInput(numVal)

  const bank = needed - numVal
  paymentBank.value = bank > 0 ? bank : 0
  displayBank.value = formatCurrencyInput(bank)
}

const onBankInput = (e) => {
  const rawValue = e.target.value.replace(/\D/g, '')
  let numVal = rawValue ? Number(rawValue) : 0
  const needed = totalNeedPay.value

  if (numVal > needed) {
    numVal = needed
    toast.warning('Số tiền nhập không được vượt quá tổng hóa đơn!')
  }

  paymentBank.value = numVal
  displayBank.value = formatCurrencyInput(numVal)

  const cash = needed - numVal
  paymentCash.value = cash > 0 ? cash : 0
  displayCash.value = formatCurrencyInput(cash)
}

const handlePaymentMethod = async () => {
  const pttt = ptttList.value.find((p) => p.id === Number(phuongThucThanhToan.value))
  if (!pttt) return

  const code = pttt.maPhuongThuc?.toUpperCase()

  if (code === 'MIXED' || code === 'KET_HOP') {
    isMultiPayment.value = true
    const totalNeedPayVal = totalNeedPay.value
    paymentCash.value = totalNeedPayVal
    paymentBank.value = 0

    displayCash.value = formatCurrencyInput(totalNeedPayVal)
    displayBank.value = '0'
  } else if (code === 'BANK' || code === 'CHUYEN_KHOAN') {
    isMultiPayment.value = false
    paymentCash.value = 0
    paymentBank.value = 0
    displayCash.value = ''
    displayBank.value = ''
    try {
      const transferAmount = totalNeedPay.value
      pendingCheckoutPayload.value = {
        idHoaDon: currentOrder.value.id,
        idVoucher: appliedVoucher.value
          ? appliedVoucher.value.isCustomerVoucher
            ? appliedVoucher.value.idVoucher
            : appliedVoucher.value.id
          : null,
        idVoucherKhachHang: appliedVoucher.value?.isCustomerVoucher
          ? appliedVoucher.value.idVoucherKhachHang
          : null,
        soXuSuDung: coinsUsed.value,
        tienGiamDoXu: coinDiscount.value,
        danhSachThanhToan: [
          {
            idPhuongThucThanhToan: pttt.id,
            soTien: transferAmount,
            maGiaoDich: 'QR-' + Date.now(),
          },
        ],
      }

      const data = await taoQr(currentOrder.value.id, transferAmount)
      qrData.value = { ...data, tongTien: transferAmount }
      showQrDialog.value = true
    } catch (e) {
      toast.error('Không thể tạo mã QR thanh toán!')
    }
  } else {
    isMultiPayment.value = false
    paymentCash.value = 0
    paymentBank.value = 0
    displayCash.value = ''
    displayBank.value = ''
  }
}

const submitCheckout = async () => {
  if (!currentOrder.value?.id) return toast.error('Hóa đơn không hợp lệ!')

  const tongTienCanThanhToan = totalNeedPay.value
  let danhSachThanhToanPayload = []

  const cashMethod = ptttList.value.find(
    (p) =>
      ['CASH', 'TIEN_MAT'].includes(p.maPhuongThuc?.toUpperCase()) ||
      p.tenPhuongThuc?.toLowerCase().includes('tiền mặt'),
  )
  const bankMethod = ptttList.value.find(
    (p) =>
      ['BANK', 'CHUYEN_KHOAN'].includes(p.maPhuongThuc?.toUpperCase()) ||
      p.tenPhuongThuc?.toLowerCase().includes('chuyển khoản'),
  )
  const selectedPttt = ptttList.value.find((p) => p.id === Number(phuongThucThanhToan.value))
  const selectedCode = selectedPttt?.maPhuongThuc?.toUpperCase()

  if (!isMultiPayment.value && isCashPayment.value) {
    const khachDua = Number(tienKhachDua.value) || 0
    if (khachDua < tongTienCanThanhToan) {
      return toast.error(
        `Số tiền khách đưa (${formatPrice(khachDua)}) còn thiếu so với tổng cần trả (${formatPrice(tongTienCanThanhToan)})!`,
      )
    }
  }

  if (isMultiPayment.value) {
    const tienMat = Number(paymentCash.value) || 0
    const chuyenKhoan = Number(paymentBank.value) || 0
    const tongKhachDua = tienMat + chuyenKhoan

    if (tongKhachDua < tongTienCanThanhToan) {
      return toast.error(
        `Số tiền thanh toán (${formatPrice(tongKhachDua)}) chưa đủ so với tổng hóa đơn (${formatPrice(tongTienCanThanhToan)})!`,
      )
    }

    if (tienMat > 0) {
      if (!cashMethod) return toast.error('Không tìm thấy phương thức Tiền mặt!')
      danhSachThanhToanPayload.push({
        idPhuongThucThanhToan: cashMethod.id,
        soTien: tienMat,
        maGiaoDich: null,
      })
    }

    if (chuyenKhoan > 0) {
      if (!bankMethod) return toast.error('Không tìm thấy phương thức Chuyển khoản!')
      danhSachThanhToanPayload.push({
        idPhuongThucThanhToan: bankMethod.id,
        soTien: chuyenKhoan,
        maGiaoDich: 'CK-' + Date.now(),
      })
    }
  } else if (selectedCode === 'BANK' || selectedCode === 'CHUYEN_KHOAN') {
    if (!bankMethod) return toast.error('Không tìm thấy phương thức Chuyển khoản!')
    danhSachThanhToanPayload.push({
      idPhuongThucThanhToan: bankMethod.id,
      soTien: tongTienCanThanhToan,
      maGiaoDich: 'CK-' + Date.now(),
    })
  } else {
    if (!phuongThucThanhToan.value) return toast.error('Vui lòng chọn phương thức thanh toán!')
    if (!cashMethod) return toast.error('Không tìm thấy phương thức Tiền mặt!')
    danhSachThanhToanPayload.push({
      idPhuongThucThanhToan: parseInt(phuongThucThanhToan.value),
      soTien: tongTienCanThanhToan > 0 ? tongTienCanThanhToan : 0,
      maGiaoDich: null,
    })
  }

  const payload = {
    idHoaDon: currentOrder.value.id,
    idVoucher: appliedVoucher.value
      ? appliedVoucher.value.isCustomerVoucher
        ? appliedVoucher.value.idVoucher
        : appliedVoucher.value.id
      : null,
    idVoucherKhachHang: appliedVoucher.value?.isCustomerVoucher
      ? appliedVoucher.value.idVoucherKhachHang
      : null,
    soXuSuDung: coinsUsed.value,
    tienGiamDoXu: coinDiscount.value,
    danhSachThanhToan: danhSachThanhToanPayload,
    tienKhachDua: isCashPayment.value ? Number(tienKhachDua.value) : tongTienCanThanhToan,
    tienThoi: isCashPayment.value ? Number(tienThoiLai.value) : 0,
  }

  const hasBankPayment = danhSachThanhToanPayload.some(
    (item) => item.idPhuongThucThanhToan === bankMethod?.id,
  )

  if (hasBankPayment) {
    pendingCheckoutPayload.value = payload
    try {
      const transferAmount = isMultiPayment.value ? Number(paymentBank.value) : tongTienCanThanhToan
      const data = await taoQr(currentOrder.value.id, transferAmount)
      qrData.value = { ...data, tongTien: transferAmount }
      showQrDialog.value = true
    } catch (e) {
      toast.error('Không thể tạo mã QR thanh toán!')
    }
    return
  }

  await executeFinalCheckout(payload)
}

const executeFinalCheckout = async (payload) => {
  try {
    const result = await thanhToanHoaDon(payload)
    hoaDonPrint.value = result
    pendingPaidOrderId.value = currentOrder.value?.id
    showInvoiceModal.value = true

    allOrders.value = allOrders.value.filter((o) => o.id !== currentOrder.value.id)

    selectedVoucher.value = null
    appliedVoucher.value = null
    voucherQuery.value = ''
    voucherCode.value = ''
    selectedCustomer.value = null
    customerVouchers.value = []
    coinsUsed.value = 0
    vouchers.value = await getAllVoucher()
    isMultiPayment.value = false
    paymentCash.value = 0
    paymentBank.value = 0
    pendingCheckoutPayload.value = null
    tienKhachDua.value = 0
    displayTienKhachDua.value = ''

    if (allOrders.value.length > 0) {
      currentOrderIndex.value = 0
      await loadChiTietHoaDon(allOrders.value[0].id)
      resetPaymentForm()
      const order = allOrders.value[0]
      selectedCustomer.value = order.selectedCustomer
      appliedVoucher.value = order.appliedVoucher
      selectedVoucher.value = order.appliedVoucher
      voucherQuery.value = order.voucherQuery || ''
      coinsUsed.value = order.coinsUsed || 0
    } else {
      currentOrderIndex.value = -1
      toast.info('Đã hết hóa đơn chờ')
    }
  } catch (error) {
    const errorMessage = error.response?.data?.message || error.message || 'Thanh toán thất bại!'
    toast.error(errorMessage)
    try {
      await loadProducts()
      if (currentOrder.value?.id) await loadChiTietHoaDon(currentOrder.value.id)
      vouchers.value = await getAllVoucher()
    } catch (syncErr) {}
  }
}

const handleCloseInvoice = () => {
  showInvoiceModal.value = false
}

const xacNhanDaThanhToan = async () => {
  showQrDialog.value = false
  if (pendingCheckoutPayload.value) {
    await executeFinalCheckout(pendingCheckoutPayload.value)
  } else {
    toast.error('Không tìm thấy thông tin hóa đơn thanh toán!')
  }
}

const addToCart = async (product) => {
  try {
    if (!currentOrder.value?.id) {
      return toast.error('Chưa có hóa đơn')
    }

    const payload = {
      idHoaDon: currentOrder.value.id,
      idSanPhamChiTiet: product.idSanPhamChiTiet || product.id,
      soLuong: 1,
    }

    await themSanPhamVaoHoaDon(payload)
    await loadChiTietHoaDon(currentOrder.value.id)
    toast.success('Đã thêm sản phẩm vào giỏ')
  } catch (error) {
    const errorMessage = error?.response?.data?.message || error?.message || ''
    if (errorMessage.includes('không đủ số lượng') || errorMessage.includes('số lượng khả dụng')) {
      await loadProducts()
      toast.warning('Sản phẩm không đủ số lượng khả dụng. Danh sách sản phẩm đã được cập nhật!')
      return
    }
    toast.error(errorMessage || 'Không thể thêm sản phẩm')
  }
}

const increaseQty = async (item) => {
  try {
    await tangSoLuongSanPham(item.id)
    await loadChiTietHoaDon(currentOrder.value.id)
    await loadProducts()
  } catch (e) {
    toast.error(e.message)
  }
}

const decreaseQty = async (item) => {
  try {
    if (item.soLuong <= 1) {
      await removeFromCart(currentOrder.value.cart.indexOf(item))
      return
    }
    await giamSoLuongSanPham(item.id)
    await loadChiTietHoaDon(currentOrder.value.id)
    await loadProducts()
  } catch (e) {
    toast.error(e.message)
  }
}

const removeFromCart = async (index) => {
  const item = currentOrder.value?.cart[index]
  if (!item) return
  try {
    await xoaSanPhamKhoiHoaDon(item.id)
    await loadChiTietHoaDon(currentOrder.value.id)
    await loadProducts()
    toast.success('Đã xóa sản phẩm khỏi giỏ')
  } catch (error) {
    toast.error('Không thể xóa sản phẩm')
  }
}
const debounceChangeQty = debounce(async (item) => {
  let newQty = Number(item.soLuong)
  if (!newQty || newQty <= 0 || !Number.isInteger(newQty)) {
    toast.warning('Số lượng phải là số nguyên lớn hơn 0')
    await loadChiTietHoaDon(currentOrder.value.id)
    return
  }

  try {
    await capNhatSoLuong(item.id, newQty)
    await Promise.all([loadChiTietHoaDon(currentOrder.value.id), loadProducts()])
  } catch (error) {
    // Lấy message từ Backend trả về (error.message) để hiển thị thông báo chi tiết
    const errorMessage = error.message || 'Không thể cập nhật số lượng'
    toast.error(errorMessage)

    // Load lại chi tiết hóa đơn để input trả về đúng số lượng cũ trước khi nhập
    await loadChiTietHoaDon(currentOrder.value.id)
  }
}, 600)
const createNewOrder = async () => {
  if (allOrders.value.length >= max_oder_waiting) {
    toast.warning(`Chỉ được tạo tối đa ${max_oder_waiting} hóa đơn chờ`)
    return
  }
  try {
    const hoaDon = await taoHoaDonCho(user.id)
    allOrders.value.push({
      id: hoaDon.id,
      maHoaDon: hoaDon.maHoaDon,
      cart: [],
      selectedCustomer: null,
      appliedVoucher: null,
      voucherQuery: '',
      coinsUsed: 0,
      loaiHoaDon: 'tai_quay',
      phuongThucThanhToan: defaultPTTTId.value,
    })
    currentOrderIndex.value = allOrders.value.length - 1
    toast.success('Tạo hóa đơn thành công')
  } catch (error) {
    toast.error('Không thể tạo hóa đơn')
  }
}

const switchOrder = async (index) => {
  const order = allOrders.value[index]
  if (!order?.id) return
  currentOrderIndex.value = index
  await loadChiTietHoaDon(order.id)
  selectedCustomer.value = order.selectedCustomer
  appliedVoucher.value = order.appliedVoucher
  voucherQuery.value = order.voucherQuery
}

const removeOrder = async (index) => {
  const order = allOrders.value[index]
  if (!order?.id) return
  try {
    await huyHoaDon(order.id)
    allOrders.value.splice(index, 1)
    if (allOrders.value.length > 0) {
      currentOrderIndex.value = Math.min(index, allOrders.value.length - 1)
      await loadChiTietHoaDon(allOrders.value[currentOrderIndex.value].id)
    } else {
      allOrders.value = []
      currentOrderIndex.value = -1
    }
    toast.success('Đã hủy hóa đơn')
  } catch (e) {
    toast.error('Hủy hóa đơn thất bại')
  }
}

const tinhTienGiam = (vc) => {
  if (!isVoucherValid(vc)) return 0
  // Nếu Backend đã trả về sẵn soTienGiam chính xác thì dùng luôn
  if (vc.soTienGiam !== undefined && vc.soTienGiam !== null) {
    return vc.soTienGiam
  }
  // Fallback phòng hờ
  if (vc.loaiGiamGia === 'tien_mat') return vc.giaTriGiam
  let giam = (totalCartPrice.value * vc.giaTriGiam) / 100
  if (vc.giaTriGiamToiDa) giam = Math.min(giam, vc.giaTriGiamToiDa)
  return giam
}
const selectVoucher = async (voucher) => {
  if (!isVoucherValid(voucher)) return
  try {
    let payloadApVoucher = {}
    if (voucher.isCustomerVoucher) {
      payloadApVoucher = {
        idHoaDon: currentOrder.value.id,
        idVoucher: null,
        idVoucherKhachHang: voucher.idVoucherKhachHang,
      }
    } else {
      payloadApVoucher = {
        idHoaDon: currentOrder.value.id,
        idVoucher: voucher.id ? Number(voucher.id) : null,
        idVoucherKhachHang: null,
      }
    }

    await apVoucher(
      payloadApVoucher.idHoaDon,
      payloadApVoucher.idVoucher,
      payloadApVoucher.idVoucherKhachHang,
    )
    await loadChiTietHoaDon(currentOrder.value.id)

    selectedVoucher.value = voucher
    appliedVoucher.value = voucher
    showVoucherDropdown.value = false
    toast.success('Đã áp dụng voucher')
  } catch (error) {
    toast.error('Không thể áp dụng voucher')
  }
}

const isVoucherValid = (vc) => {
  const now = new Date()
  if (vc.trangThai !== 1) return false
  if (vc.ngayBatDau && new Date(vc.ngayBatDau) > now) return false
  if (vc.ngayKetThuc && new Date(vc.ngayKetThuc) < now) return false
  if (vc.soLuongDaDung >= vc.soLuong) return false
  if (totalCartPrice.value < vc.giaTriDonHangToiThieu) return false
  return true
}

const getVoucherError = (vc) => {
  if (vc.trangThai !== 1) return 'Voucher đang bị khóa'
  if (totalCartPrice.value < vc.giaTriDonHangToiThieu)
    return `Đơn tối thiểu ${formatPrice(vc.giaTriDonHangToiThieu)}`
  return ''
}

const saveNewCustomer = async () => {
  if (!newCust.value.hoTen || !newCust.value.sdt)
    return toast.warning('Vui lòng nhập đầy đủ Tên và SĐT!')
  try {
    const data = await addKhachHangQuickService({
      hoTen: newCust.value.hoTen,
      soDienThoai: newCust.value.sdt,
    })
    customers.value.push(data)
    selectedCustomer.value = data
    newCust.value = { hoTen: '', sdt: '' }
    showCustomerModal.value = false
    toast.success('Thêm khách hàng thành công!')
  } catch (e) {
    toast.error('Lỗi khi lưu khách hàng!')
  }
}

const selectCustomer = async (kh) => {
  if (!hasCurrentOrder.value) return toast.error('Vui lòng chọn hoặc tạo hóa đơn trước!')
  try {
    const res = await ganKhachHang(currentOrder.value.id, kh.id)
    selectedCustomer.value = kh
    coinsUsed.value = 0 // Reset xu khi đổi khách hàng mới
    let rawData = res.data !== undefined ? res.data : res
    if (typeof rawData === 'string') {
      try {
        rawData = JSON.parse(rawData)
      } catch (e) {
        rawData = []
      }
    }
    if (Array.isArray(rawData) && rawData.length > 0) {
      customerVouchers.value = rawData.map(mapKhoVoucherToStandardVoucher)
    } else {
      customerVouchers.value = []
    }
    showCustomerModal.value = false
    toast.success('Đã chọn khách hàng thành công!')
  } catch (e) {
    toast.error('Không thể gán khách hàng')
  }
}

const getProductImage = (product) => {
  if (product?.image) return `http://localhost:8080${product.image}`
  if (product?.images?.length > 0) return `http://localhost:8080${product.images[0]}`
  return DEFAULT_PRODUCT_IMAGE
}

const setDefaultImage = (event) => {
  event.target.src = DEFAULT_PRODUCT_IMAGE
}

const formatPrice = (value) =>
  new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value || 0)

const openDropdown = () => (isDropdownVisible.value = true)
const closeDropdown = () => setTimeout(() => (isDropdownVisible.value = false), 200)
const openCustomerModal = () => (showCustomerModal.value = true)

const handleRemoveCustomer = async () => {
  if (!currentOrder.value?.id) return
  try {
    await goKhachHang(currentOrder.value.id)
    await boVoucher(currentOrder.value.id)
    selectedCustomer.value = null
    customerVouchers.value = []
    selectedVoucher.value = null
    appliedVoucher.value = null
    coinsUsed.value = 0
    toast.success('Đã gỡ khách hàng')
  } catch (e) {
    toast.error('Gỡ khách hàng thất bại')
  }
}

const handleRemoveVoucher = async () => {
  if (!currentOrder.value?.id) return
  try {
    await boVoucher(currentOrder.value.id)
    await loadChiTietHoaDon(currentOrder.value.id)
    selectedVoucher.value = null
    appliedVoucher.value = null
    toast.success('Đã gỡ voucher')
  } catch (e) {
    toast.error('Gỡ voucher thất bại')
  }
}

const handleKeyDown = (e) => {
  if (e.key === 'F1') {
    e.preventDefault()
    createNewOrder()
  }

  if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'f') {
    e.preventDefault()
    if (headerRef.value) {
      headerRef.value.focusSearch()
      openDropdown()
    }
  }

  if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'd') {
    e.preventDefault()
    if (allOrders.value.length > 0 && currentOrderIndex.value >= 0) {
      removeOrder(currentOrderIndex.value)
    }
  }
}

const handleClickOutside = (event) => {
  if (voucherRef.value && !voucherRef.value.contains(event.target)) {
    showVoucherDropdown.value = false
  }
}

onMounted(async () => {
  await loadAllDataFromAPI()
  await loadCauHinhHeThong()
  const [voucherData, hoaDonData] = await Promise.all([getAllVoucher(), getHoaDonCho(user.id)])
  await loadPTTT()

  vouchers.value = voucherData
  allOrders.value = hoaDonData.map((hd) => ({
    id: hd.id,
    maHoaDon: hd.maHoaDon,
    cart: [],
    selectedCustomer: null,
    appliedVoucher: hd.voucher || null,
    voucherQuery: hd.voucher?.maVoucher || '',
    coinsUsed: 0,
    loaiHoaDon: 'tai_quay',
    phuongThucThanhToan: defaultPTTTId.value,
  }))

  if (allOrders.value.length > 0) {
    currentOrderIndex.value = 0
    await loadChiTietHoaDon(allOrders.value[0].id)
  }

  connectSocket()
  window.addEventListener('keydown', handleKeyDown)
  document.addEventListener('click', handleClickOutside)
})

onBeforeUnmount(() => {
  if (socketSubscription) socketSubscription.unsubscribe()
  window.removeEventListener('keydown', handleKeyDown)
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style scoped>
.pos-wrapper {
  zoom: 0.82;
}
@supports not (zoom: 0.82) {
  .pos-wrapper {
    transform: scale(0.82);
    transform-origin: top left;
    width: 121.95%;
  }
}
.custom-scrollbar::-webkit-scrollbar {
  width: 4px;
  height: 4px;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 9999px;
}
.no-scrollbar::-webkit-scrollbar {
  display: none;
}
</style>
