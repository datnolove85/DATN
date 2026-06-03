<template>
  <div class="min-h-screen bg-slate-50 text-slate-800 font-sans antialiased">
    <header
      class="sticky top-0 z-40 bg-white border-b border-slate-200 shadow-sm backdrop-blur-md bg-opacity-95"
    >
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
        <div class="flex-1 max-w-md mx-8 hidden md:block">
          <div class="relative">
            <span class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <svg
                class="h-5 w-5 text-slate-400"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
                />
              </svg>
            </span>
            <input
              type="text"
              v-model="searchQuery"
              placeholder="Tìm theo mã SPCT, tên quần, áo..."
              class="w-full pl-10 pr-4 py-2 border border-slate-200 rounded-xl bg-slate-50 focus:bg-white focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all text-sm"
            />
          </div>
        </div>
      </div>
    </header>

    <div
      v-if="isLoading"
      class="flex justify-center items-center py-20 text-slate-500 font-medium text-sm"
    >
      <span class="animate-pulse">🔄 Đang tải dữ liệu sản phẩm từ hệ thống...</span>
    </div>

    <main
      v-else
      class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6 grid grid-cols-1 lg:grid-cols-12 gap-6"
    >
      <section class="lg:col-span-7 xl:col-span-8 flex flex-col space-y-6">
        <div
          class="bg-white p-4 rounded-2xl border border-slate-200 shadow-sm flex flex-wrap gap-3 items-center mb-4 flex-shrink-0"
        >
          <select
            v-model="filterCategory"
            class="text-xs font-medium bg-slate-50 border border-slate-200 rounded-lg px-3 py-2 text-slate-600 focus:outline-none focus:ring-2 focus:ring-indigo-500"
          >
            <option value="">Tất cả Danh mục</option>
            <option v-for="dm in danhMucList" :key="dm.id" :value="dm.id">
              {{ dm.tenDanhMuc }}
            </option>
          </select>

          <select
            v-model="filterBrand"
            class="text-xs font-medium bg-slate-50 border border-slate-200 rounded-lg px-3 py-2 text-slate-600 focus:outline-none focus:ring-2 focus:ring-indigo-500"
          >
            <option value="">Tất cả Thương hiệu</option>
            <option v-for="th in thuongHieuList" :key="th.id" :value="th.id">
              {{ th.tenThuongHieu }}
            </option>
          </select>

          <select
            v-model="filterColor"
            class="text-xs font-medium bg-slate-50 border border-slate-200 rounded-lg px-3 py-2 text-slate-600 focus:outline-none focus:ring-2 focus:ring-indigo-500"
          >
            <option value="">Màu sắc</option>
            <option v-for="ms in mauSacList" :key="ms.id" :value="ms.id">{{ ms.tenMauSac }}</option>
          </select>

          <select
            v-model="filterSize"
            class="text-xs font-medium bg-slate-50 border border-slate-200 rounded-lg px-3 py-2 text-slate-600 focus:outline-none focus:ring-2 focus:ring-indigo-500"
          >
            <option value="">Kích thước</option>
            <option v-for="kt in kichThuocList" :key="kt.id" :value="kt.id">
              {{ kt.tenKichThuoc }}
            </option>
          </select>

          <button
            @click="resetFilters"
            class="text-xs font-semibold text-rose-600 hover:text-rose-700 ml-auto transition-colors"
          >
            Xóa bộ lọc
          </button>
        </div>
        <div class="grid grid-cols-2 sm:grid-cols-3 xl:grid-cols-4 gap-4">
          <div
            v-for="sp in filteredProducts"
            :key="sp.id"
            @click="sp.soLuongTon > 0 ? addToCart(sp) : null"
            :class="[
              'bg-white rounded-2xl border p-3 flex flex-col justify-between transition-all group relative overflow-hidden select-none',
              sp.soLuongTon > 0
                ? 'border-slate-200 hover:border-indigo-500 hover:shadow-lg cursor-pointer'
                : 'border-slate-200 bg-slate-50 opacity-60 cursor-not-allowed',
            ]"
          >
            <span
              v-if="sp.giaGoc > sp.giaBan"
              class="absolute top-2 left-2 z-10 bg-rose-500 text-white text-[10px] font-black px-2 py-0.5 rounded-lg shadow-sm"
            >
              GIẢM {{ Math.round(((sp.giaGoc - sp.giaBan) / sp.giaGoc) * 100) }}%
            </span>

            <div class="aspect-square w-full rounded-xl bg-slate-100 overflow-hidden relative mb-3">
              <img
                :src="getProductImage(sp)"
                alt="Product image"
                class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300 relative z-10"
                @error="setDefaultImage"
              />

              <div
                v-if="sp.soLuongTon > 0"
                class="absolute inset-0 bg-black bg-opacity-0 group-hover:bg-opacity-10 flex items-center justify-center transition-all"
              >
                <span
                  class="bg-indigo-600 text-white p-2 rounded-full opacity-0 group-hover:opacity-100 shadow-md transform translate-y-2 group-hover:translate-y-0 transition-all"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    class="h-4 w-4"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2.5"
                      d="M12 4v16m8-8H4"
                    />
                  </svg>
                </span>
              </div>

              <div
                v-else
                class="absolute inset-0 bg-slate-900 bg-opacity-40 flex items-center justify-center"
              >
                <span
                  class="bg-white text-slate-800 text-xs font-black px-2.5 py-1 rounded-lg shadow"
                >
                  HẾT HÀNG
                </span>
              </div>
            </div>

            <div class="flex-1 flex flex-col justify-between">
              <div>
                <div class="flex items-center justify-between gap-1">
                  <span
                    class="text-[9px] font-bold text-slate-400 uppercase tracking-wider truncate"
                  >
                    {{ sp.maSanPhamChiTiet }}
                  </span>
                  <span
                    class="text-[9px] font-bold bg-indigo-50 text-indigo-600 px-1.5 py-0.2 rounded"
                  >
                    {{ sp.tenThuongHieu }}
                  </span>
                </div>

                <h3
                  class="text-xs font-bold text-slate-800 line-clamp-2 mt-1 group-hover:text-indigo-600 transition-colors"
                  :title="sp.tenSanPhamChiTiet || sp.tenSanPham"
                >
                  {{ sp.tenSanPhamChiTiet || sp.tenSanPham }}
                </h3>

                <div class="flex gap-1 mt-2 flex-wrap">
                  <span
                    class="text-[9px] font-medium bg-slate-100 text-slate-600 px-1.5 py-0.5 rounded"
                  >
                    🎨 {{ sp.tenMauSac }}
                  </span>
                  <span
                    class="text-[9px] font-medium bg-slate-100 text-slate-600 px-1.5 py-0.5 rounded"
                  >
                    📏 Size {{ sp.tenKichThuoc }}
                  </span>
                  <span
                    v-if="sp.tenChatLieu"
                    class="text-[9px] font-medium bg-slate-100 text-slate-600 px-1.5 py-0.5 rounded"
                  >
                    🧵 {{ sp.tenChatLieu }}
                  </span>
                </div>
              </div>

              <div class="mt-3 pt-2 border-t border-slate-100 flex items-end justify-between">
                <div>
                  <p
                    class="text-[10px] line-through text-slate-400 font-medium leading-none mb-0.5"
                    v-if="sp.giaGoc > sp.giaBan"
                  >
                    {{ formatPrice(sp.giaGoc) }}
                  </p>
                  <p class="text-sm font-black text-indigo-600 leading-none">
                    {{ formatPrice(sp.giaBan) }}
                  </p>
                </div>

                <span
                  :class="[
                    'text-[10px] px-1.5 py-0.5 rounded font-bold',
                    sp.soLuongTon <= 0
                      ? 'bg-rose-50 text-rose-600'
                      : sp.soLuongTon <= 10
                        ? 'bg-amber-50 text-amber-700'
                        : 'bg-slate-100 text-slate-600',
                  ]"
                >
                  Kho: {{ sp.soLuongTon }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section class="lg:col-span-5 xl:col-span-4 flex flex-col space-y-6">
        <div
          class="bg-white rounded-3xl border border-slate-200 shadow-xl flex flex-col h-[calc(100vh-120px)] sticky top-24 overflow-hidden"
        >
          <div class="p-4 border-b border-slate-100 bg-slate-50 bg-opacity-70">
            <div class="flex items-center justify-between mb-2">
              <label class="text-xs font-bold text-slate-500 uppercase tracking-wider"
                >Khách hàng</label
              >
              <button
                @click="openCustomerModal"
                class="text-xs font-bold text-indigo-600 hover:underline flex items-center gap-1"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  class="h-3 w-3"
                  viewBox="0 0 20 20"
                  fill="currentColor"
                >
                  <path
                    d="M8 9a3 3 0 100-6 3 3 0 000 6zM8 11a6 6 0 016 6H2a6 6 0 016-6zM16 7a1 1 0 10-2 0v1h-1a1 1 0 100 2h1v1a1 1 0 102 0v-1h1a1 1 0 100-2h-1V7z"
                  />
                </svg>
                Chọn / Thêm mới
              </button>
            </div>

            <div
              class="bg-white border border-slate-200 rounded-xl p-3 flex items-center justify-between shadow-sm"
            >
              <div v-if="selectedCustomer">
                <p class="text-sm font-bold text-slate-800">{{ selectedCustomer.hoTen }}</p>
                <p class="text-xs text-slate-500 font-medium">
                  {{ selectedCustomer.soDienThoai }} •
                  <span class="text-indigo-600 font-semibold">{{
                    selectedCustomer.hangThanhVien || 'Thành viên lẻ'
                  }}</span>
                </p>
              </div>
              <div v-else>
                <p class="text-sm font-semibold text-slate-400">Khách vãng lai lẻ tại quầy</p>
              </div>
              <button
                v-if="selectedCustomer"
                @click="selectedCustomer = null"
                class="text-slate-400 hover:text-rose-500"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  class="h-4 w-4"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M6 18L18 6M6 6l12 12"
                  />
                </svg>
              </button>
            </div>
          </div>

          <div class="flex-1 overflow-y-auto p-4 space-y-3 custom-scrollbar">
            <div
              v-if="cart.length === 0"
              class="h-full flex flex-col items-center justify-center text-slate-400 py-12"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="h-12 w-12 text-slate-300 mb-2"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 0a2 2 0 11-4 0 2 2 0 014 0z"
                />
              </svg>
              <p class="text-xs font-semibold">Giỏ hàng trống. Click sản phẩm để thêm.</p>
            </div>

            <div
              v-for="(item, index) in cart"
              :key="index"
              class="flex items-center gap-3 bg-white p-2.5 rounded-xl border border-slate-100 hover:shadow-sm transition-shadow"
            >
              <img
                :src="getProductImage(item.product)"
                class="w-12 h-12 rounded-lg object-cover bg-slate-50 flex-shrink-0"
                @error="setDefaultImage"
              />
              <div class="flex-1 min-w-0">
                <h4 class="text-xs font-bold text-slate-800 truncate">
                  {{ item.product.tenSanPhamChiTiet || item.product.tenSanPham }}
                </h4>
                <p class="text-[10px] text-slate-500 font-medium">
                  {{ item.product.tenMauSac }} / {{ item.product.tenKichThuoc }}
                </p>
                <p class="text-xs font-black text-indigo-600 mt-0.5">
                  {{ formatPrice(item.product.giaBan) }}
                </p>
              </div>

              <div class="flex items-center bg-slate-100 rounded-lg p-1">
                <button
                  @click="decreaseQty(index)"
                  class="w-5 h-5 flex items-center justify-center text-slate-600 hover:bg-white rounded transition-colors text-xs font-bold"
                >
                  -
                </button>
                <span class="w-6 text-center text-xs font-bold text-slate-800">{{
                  item.soLuong
                }}</span>
                <button
                  @click="increaseQty(index)"
                  class="w-5 h-5 flex items-center justify-center text-slate-600 hover:bg-white rounded transition-colors text-xs font-bold"
                >
                  +
                </button>
              </div>

              <button
                @click="removeFromCart(index)"
                class="text-slate-300 hover:text-rose-500 transition-colors"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  class="h-4 w-4"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
                  />
                </svg>
              </button>
            </div>
          </div>

          <div class="border-t border-slate-100 p-4 bg-slate-50 space-y-4">
            <div class="mt-4 flex gap-2 items-end">
              <div class="relative flex-1">
                <label
                  class="text-[10px] font-bold text-slate-400 uppercase tracking-wider block mb-1"
                >
                  Voucher Khuyến Mãi
                </label>

                <div class="relative">
                  <input
                    type="text"
                    v-model="voucherQuery"
                    @focus="showVoucherDropdown = true"
                    @blur="setTimeout(() => (showVoucherDropdown = false), 200)"
                    placeholder="Tìm hoặc nhập mã..."
                    class="w-full bg-slate-50 border border-slate-200 rounded-xl px-4 py-2.5 text-xs font-medium focus:outline-none focus:ring-2 focus:ring-indigo-500 transition-all"
                  />
                  <span class="absolute right-3 top-2.5 text-slate-400">🎟️</span>
                </div>

                <div
                  v-if="showVoucherDropdown && filteredVouchers.length > 0"
                  class="absolute z-[100] w-full bg-white border border-slate-200 rounded-xl shadow-xl mt-1 max-h-48 overflow-y-auto custom-scrollbar"
                >
                  <div
                    v-for="vc in filteredVouchers"
                    :key="vc.id"
                    @mousedown.prevent="selectVoucher(vc)"
                    class="px-4 py-3 hover:bg-indigo-50 cursor-pointer border-b border-slate-50"
                  >
                    <div class="flex justify-between items-center">
                      <span class="text-xs font-bold text-slate-800">{{ vc.maVoucher }}</span>
                      <span
                        class="text-[10px] font-semibold text-rose-600 bg-rose-50 px-2 rounded-full"
                      >
                        -{{ vc.phanTramGiam }}%
                      </span>
                    </div>
                  </div>
                </div>
              </div>

              <button
                @click="applyVoucher"
                class="bg-slate-800 text-white text-xs font-bold px-4 py-2.5 rounded-xl hover:bg-slate-900 transition-colors"
              >
                Áp dụng
              </button>
            </div>

            <div
              v-if="appliedVoucher"
              class="bg-indigo-50 text-indigo-700 text-[11px] font-bold p-2 rounded-xl flex justify-between items-center"
            >
              <span>🎟️ Đã áp dụng: {{ appliedVoucher.tenVoucher }}</span>
              <button @click="removeVoucher" class="text-rose-500 hover:underline font-black">
                Xóa
              </button>
            </div>

            <div class="space-y-1.5 text-xs font-medium text-slate-600">
              <div class="flex justify-between">
                <span>Tổng tiền hàng</span>
                <span class="text-slate-800 font-semibold">{{ formatPrice(totalCartPrice) }}</span>
              </div>
              <div class="flex justify-between text-rose-600">
                <span>Giảm giá Voucher</span>
                <span>- {{ formatPrice(voucherDiscount) }}</span>
              </div>
              <div class="flex justify-between items-center pt-2 border-t border-slate-200">
                <span class="text-sm font-bold text-slate-800">Tổng thanh toán</span>
                <span class="text-lg font-black text-indigo-600">{{
                  formatPrice(finalPaymentPrice)
                }}</span>
              </div>
            </div>

            <div class="grid grid-cols-2 gap-2 pt-1">
              <div>
                <label class="text-[10px] font-bold text-slate-400 block mb-1">LOẠI HÓA ĐƠN</label>
                <select
                  v-model="loaiHoaDon"
                  class="w-full bg-white border border-slate-200 rounded-xl p-2 text-xs font-bold text-slate-700 focus:outline-none"
                >
                  <option value="tai_quay">Tại quầy (POS)</option>
                  <option value="online">Giao hàng (Online)</option>
                </select>
              </div>
              <div>
                <label class="text-[10px] font-bold text-slate-400 block mb-1"
                  >PHƯƠNG THỨC TT</label
                >
                <select
                  v-model="phuongThucThanhToan"
                  class="w-full bg-white border border-slate-200 rounded-xl p-2 text-xs font-bold text-slate-700 focus:outline-none"
                >
                  <option value="" disabled>Chọn phương thức</option>

                  <option v-for="pt in ptttList" :key="pt.id" :value="pt.id">
                    {{ pt.tenPhuongThuc }}
                  </option>
                </select>
              </div>
            </div>

            <button
              @click="submitCheckout"
              class="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-bold text-sm py-3 px-4 rounded-xl shadow-lg shadow-indigo-100 hover:shadow-xl active:scale-[0.99] transition-all flex items-center justify-center gap-2"
            >
              XUẤT HÓA ĐƠN & THANH TOÁN
            </button>
          </div>
        </div>
      </section>
    </main>

    <div
      v-if="showCustomerModal"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50"
    >
      <div class="bg-white rounded-3xl max-w-md w-full p-6 shadow-2xl">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-base font-bold text-slate-900">Chọn Khách Hàng Từ Hệ Thống</h3>
          <button @click="showCustomerModal = false" class="text-slate-400 hover:text-slate-600">
            ✕
          </button>
        </div>
        <input
          type="text"
          v-model="searchCustomerQuery"
          placeholder="Tìm tên hoặc số điện thoại..."
          class="w-full bg-slate-50 border border-slate-200 rounded-xl px-3 py-2 text-sm mb-3 focus:outline-none focus:ring-2 focus:ring-indigo-500"
        />
        <div class="max-h-60 overflow-y-auto space-y-2 mb-4">
          <div
            v-for="kh in filteredCustomers"
            :key="kh.id"
            @click="selectCustomer(kh)"
            class="p-3 border border-slate-100 rounded-xl hover:bg-indigo-50 cursor-pointer flex justify-between items-center transition-colors"
          >
            <div>
              <p class="text-xs font-bold text-slate-800">{{ kh.hoTen }}</p>
              <p class="text-[11px] text-slate-500 font-medium">{{ kh.soDienThoai }}</p>
            </div>
            <span class="text-[10px] font-bold px-2 py-0.5 rounded bg-amber-100 text-amber-800">
              {{ kh.hangThanhVien || 'Thành viên' }}
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

// --- 1. IMPORT CÁC SERVICE API ---
import { getAllDanhMuc } from '@/service/DanhMucService'
import { getAllKichThuoc } from '@/service/KichThuocService'
import { getAllMauSac } from '@/service/MauSacService'
import { getAllThuongHieu } from '@/service/ThuongHieuService'
import { getAllSanPhamChiTiet } from '@/service/SanPhamChiTiet'
import { getAllPTTT } from '@/service/PhuongThucThanhToanService' // Đường dẫn tới file chứa api của bạn

const voucherQuery = ref('')
const showVoucherDropdown = ref(false)
const allVouchers = ref([]) // Danh sách lấy từ API của bạn

// Bộ lọc khi gõ
const filteredVouchers = computed(() => {
  return allVouchers.value.filter((v) =>
    v.maVoucher.toLowerCase().includes(voucherQuery.value.toLowerCase()),
  )
})

// Hàm chọn voucher
const selectVoucher = (vc) => {
  voucherQuery.value = vc.maVoucher
  appliedVoucher.value = vc // Lưu vào biến bạn đang có sẵn
  showVoucherDropdown.value = false
}

// Đóng dropdown khi click ra ngoài (Tùy chọn)
// Bạn có thể dùng directive v-click-outside để chuyên nghiệp hơn
// Biến lưu danh sách PTTT
const ptttList = ref([])

// Hàm lấy dữ liệu
const fetchPTTT = async () => {
  try {
    const data = await getAllPTTT()
    ptttList.value = data
  } catch (error) {
    console.error('Lỗi khi tải PTTT:', error)
  }
}

onMounted(() => {
  fetchPTTT()
})

// --- BASE API URL HỆ THỐNG ---
const API_KHACH_HANG = 'http://localhost:8080/khachhang'
const API_HOA_DON = 'http://localhost:8080/hoadon'
const DEFAULT_PRODUCT_IMAGE = 'https://images.unsplash.com/photo-1523381210434-271e8be1f52b?w=400'

// --- TRẠNG THÁI DANH SÁCH (DỮ LIỆU ĐỔ TỪ API) ---
const danhMucList = ref([])
const thuongHieuList = ref([])
const mauSacList = ref([])
const kichThuocList = ref([])
const products = ref([])
const customers = ref([])

const isLoading = ref(true)

// --- TRẠNG THÁI FORM CHỨC NĂNG & GIỎ HÀNG (STATE) ---
const searchQuery = ref('')
const filterCategory = ref('')
const filterBrand = ref('')
const filterColor = ref('')
const filterSize = ref('')

const cart = ref([])
const voucherCode = ref('')
const appliedVoucher = ref(null)
const loaiHoaDon = ref('tai_quay')
const phuongThucThanhToan = ref('tien_mat')

const showCustomerModal = ref(false)
const searchCustomerQuery = ref('')
const selectedCustomer = ref(null)

// --- 2. HÀM TẢI DỮ LIỆU ĐỒNG THỜI TỪ BACKEND ---
const loadAllDataFromAPI = async () => {
  try {
    isLoading.value = true

    const [resDM, resKT, resMS, resTH, resSPCT] = await Promise.all([
      getAllDanhMuc(),
      getAllKichThuoc(),
      getAllMauSac(),
      getAllThuongHieu(),
      getAllSanPhamChiTiet(),
    ])

    danhMucList.value = resDM
    kichThuocList.value = resKT
    mauSacList.value = resMS
    thuongHieuList.value = resTH
    products.value = resSPCT

    customers.value = [
      { id: 1, hoTen: 'Nguyễn Văn A', soDienThoai: '0987654321', hangThanhVien: 'Vàng (Gold)' },
      { id: 2, hoTen: 'Trần Thị B', soDienThoai: '0123456789', hangThanhVien: 'Bạc (Silver)' },
      { id: 3, hoTen: 'Lê Hoàng C', soDienThoai: '0909090909', hangThanhVien: 'Kim Cương' },
    ]
  } catch (error) {
    console.error('Lỗi khi kết nối hệ thống API Server:', error)
    alert(
      'Không thể tải dữ liệu. Hãy chắc chắn Server Spring Boot Localhost 8080 đang chạy ổn định!',
    )
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  loadAllDataFromAPI()
})

// --- HÀM XỬ LÝ ẢNH SẢN PHẨM CHUYÊN NGHIỆP ---
// Thay thế hàm cũ bằng hàm này
const getProductImage = (sp) => {
  // 2. Ép kiểu về mảng nếu cần
  const images = sp.images

  if (images && images.length > 0) {
    const rawPath = images[0]

    // Nếu đường dẫn đã bắt đầu bằng http thì trả về luôn
    if (rawPath.startsWith('http')) return rawPath

    // Nếu đường dẫn có dấu / ở đầu (như /sanpham/anh1.jpg)
    // thì chỉ cần thêm domain, không cần thêm / ở giữa
    const cleanPath = rawPath.startsWith('/') ? rawPath.substring(1) : rawPath

    const finalUrl = `http://localhost:8080/${cleanPath}`

    return finalUrl
  }

  return 'https://via.placeholder.com/150'
}

const setDefaultImage = (event) => {
  event.target.src = DEFAULT_PRODUCT_IMAGE
}

// --- 3. BỘ LỌC TÌM KIẾM THÔNG MINH (COMPUTED) ---
const filteredProducts = computed(() => {
  return products.value.filter((sp) => {
    // 1. Logic tìm kiếm (Tên hoặc Mã)
    const nameStr = (sp.tenSanPhamChiTiet || sp.tenSanPham || '').toLowerCase()
    const codeStr = (sp.maSanPhamChiTiet || '').toLowerCase()
    const searchStr = searchQuery.value.toLowerCase()
    const matchSearch = nameStr.includes(searchStr) || codeStr.includes(searchStr)

    // 2. Logic bộ lọc (Sử dụng ép kiểu Number để so sánh chính xác id)
    // Nếu filter rỗng hoặc giá trị bằng nhau thì coi như khớp (true)
    const selectedDM = danhMucList.value.find((dm) => dm.id == filterCategory.value)

    const selectedTH = thuongHieuList.value.find((th) => th.id == filterBrand.value)

    const matchCategory = !filterCategory.value || sp.tenDanhMuc === selectedDM?.tenDanhMuc

    const matchBrand = !filterBrand.value || sp.tenThuongHieu === selectedTH?.tenThuongHieu
    const matchColor = !filterColor.value || Number(sp.idMauSac) === Number(filterColor.value)
    const matchSize = !filterSize.value || Number(sp.idKichThuoc) === Number(filterSize.value)

    return matchSearch && matchCategory && matchBrand && matchColor && matchSize
  })
})

const resetFilters = () => {
  filterCategory.value = ''
  filterBrand.value = ''
  filterColor.value = ''
  filterSize.value = ''
  searchQuery.value = ''
}

const filteredCustomers = computed(() => {
  return customers.value.filter(
    (kh) =>
      kh.hoTen.toLowerCase().includes(searchCustomerQuery.value.toLowerCase()) ||
      kh.soDienThoai.includes(searchCustomerQuery.value),
  )
})

const totalCartPrice = computed(() => {
  return cart.value.reduce((sum, item) => {
    return sum + item.product.giaBan * item.soLuong
  }, 0)
})

const voucherDiscount = computed(() => {
  if (!appliedVoucher.value) return 0
  if (appliedVoucher.value.loai === 'tien_mat') return appliedVoucher.value.giatri
  if (appliedVoucher.value.loai === 'phan_tram') {
    return (totalCartPrice.value * appliedVoucher.value.giatri) / 100
  }
  return 0
})

const finalPaymentPrice = computed(() => {
  const result = totalCartPrice.value - voucherDiscount.value
  return result > 0 ? result : 0
})

// --- 4. HÀM XỬ LÝ HÀNH VI CỦA HỆ THỐNG TRÊN GIỎ HÀNG ---
const addToCart = (product) => {
  if (product.soLuongTon <= 0) {
    alert('Sản phẩm đã hết hàng, không thể thêm vào đơn!')
    return
  }

  const existingItem = cart.value.find((item) => item.product.id === product.id)
  if (existingItem) {
    if (existingItem.soLuong < product.soLuongTon) {
      existingItem.soLuong++
    } else {
      alert('Số lượng chọn mua đã đạt mức giới hạn tồn kho của mặt hàng này!')
    }
  } else {
    cart.value.push({ product, soLuong: 1 })
  }
}

const increaseQty = (index) => {
  if (cart.value[index].soLuong < cart.value[index].product.soLuongTon) {
    cart.value[index].soLuong++
  } else {
    alert('Không thể tăng thêm, kho đã đạt số lượng tối đa hiện có!')
  }
}

const decreaseQty = (index) => {
  if (cart.value[index].soLuong > 1) {
    cart.value[index].soLuong--
  } else {
    removeFromCart(index)
  }
}

const removeFromCart = (index) => {
  cart.value.splice(index, 1)
}

const applyVoucher = () => {
  const code = voucherCode.value.toUpperCase().trim()
  if (code === 'VIP10') {
    appliedVoucher.value = { tenVoucher: 'Mã VIP10 (Giảm 10%)', loai: 'phan_tram', giatri: 10 }
  } else if (code === 'VIP50K') {
    appliedVoucher.value = { tenVoucher: 'Mã VIP50K (Giảm 50k)', loai: 'tien_mat', giatri: 50000 }
  } else {
    alert('Mã giảm giá vừa nhập không tồn tại hoặc không đáp ứng điều kiện!')
  }
}

const removeVoucher = () => {
  appliedVoucher.value = null
  voucherCode.value = ''
}

const openCustomerModal = () => {
  showCustomerModal.value = true
}
const selectCustomer = (kh) => {
  selectedCustomer.value = kh
  showCustomerModal.value = false
}

// --- 5. ĐẨY DỮ LIỆU THANH TOÁN LÊN DATABASE BACKEND ---
const submitCheckout = async () => {
  if (cart.value.length === 0) {
    alert('Vui lòng chọn ít nhất một sản phẩm vào đơn hàng trước khi thực hiện thanh toán!')
    return
  }

  const payloadHoaDon = {
    idKhachHang: selectedCustomer.value ? selectedCustomer.value.id : null,
    tongTienHang: totalCartPrice.value,
    tongGiamGia: voucherDiscount.value,
    tongThanhToan: finalPaymentPrice.value,
    loaiHoaDon: loaiHoaDon.value,
    phuongThucTt: phuongThucThanhToan.value,
    chiTietMua: cart.value.map((item) => ({
      idSanPhamChiTiet: item.product.id,
      soLuong: item.soLuong,
      donGia: item.product.giaBan,
    })),
  }

  try {
    const res = await fetch(API_HOA_DON, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payloadHoaDon),
    })

    if (!res.ok) throw new Error('Có lỗi xảy ra từ phía dịch vụ máy chủ hệ thống.')

    alert(
      `🎉 Tạo hóa đơn và thanh toán thành công!\nSố tiền nhận: ${formatPrice(finalPaymentPrice.value)}`,
    )

    cart.value = []
    removeVoucher()
    selectedCustomer.value = null

    await loadAllDataFromAPI()
  } catch (error) {
    console.error('Lỗi submitCheckout:', error)
    alert('Quá trình lưu hóa đơn thất bại. Vui lòng kiểm tra lại log kết nối máy chủ!')
  }
}

const formatPrice = (value) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value)
}
</script>

<style scoped>
.custom-scrollbar::-webkit-scrollbar {
  width: 5px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 10px;
}
.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}
</style>
