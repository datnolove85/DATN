<template>
  <div class="max-w-full mx-auto p-6 bg-slate-50 min-h-screen text-slate-800 font-sans">
    <div class="grid grid-cols-1 xl:grid-cols-12 gap-6">
      <div class="xl:col-span-9 space-y-6">
        <div
          class="relative p-8 bg-[#0b0f19] rounded-3xl text-white flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 shadow-sm overflow-hidden"
        >
          <div>
            <span class="text-[10px] tracking-widest uppercase font-bold text-indigo-400"
              >Hệ thống quản trị thế hệ mới</span
            >
            <h1 class="text-2xl font-bold tracking-tight mt-1">Bảng Điều Phối Sản Phẩm Chi Tiết</h1>
            <p class="text-xs text-slate-400 mt-1">
              Sắp xếp, định danh và tối ưu hóa biến thể kho hàng thời gian thực.
            </p>
          </div>
          <button
            @click="openModal()"
            class="flex items-center gap-2 px-5 py-3 bg-indigo-650 hover:bg-indigo-600 bg-indigo-600 text-white rounded-2xl text-xs font-bold shadow-lg shadow-indigo-600/20 transition-all active:scale-95 whitespace-nowrap"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="2.5"
              stroke="currentColor"
              class="w-4 h-4"
            >
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
            </svg>
            + KHỞI TẠO BIẾN THỂ
          </button>
        </div>

        <div
          class="bg-white border border-slate-100 p-4 rounded-3xl shadow-sm grid grid-cols-1 md:grid-cols-3 gap-4 items-center"
        >
          <div class="md:col-span-2 relative">
            <span class="absolute inset-y-0 left-0 flex items-center pl-4 text-slate-400">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke-width="2"
                stroke="currentColor"
                class="w-4 h-4"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.604 10.604z"
                />
              </svg>
            </span>
            <input
              v-model="filters.searchQuery"
              type="text"
              placeholder="Tìm theo tên sản phẩm hoặc mã SKU..."
              class="w-full bg-slate-50 border border-slate-100 pl-10 pr-4 py-2.5 rounded-2xl text-xs outline-none focus:border-indigo-500 focus:bg-white transition-all"
            />
          </div>
          <div>
            <select
              v-model="filters.status"
              class="w-full bg-slate-50 border border-slate-100 px-4 py-2.5 rounded-2xl text-xs outline-none focus:border-indigo-500 focus:bg-white transition-all text-slate-600 font-medium"
            >
              <option value="all">Tất cả trạng thái</option>
              <option value="kinhDoanh">Đang Kinh Doanh</option>
              <option value="luuKho">Đang Lưu Kho</option>
            </select>
          </div>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          <div
            v-for="item in filteredProducts"
            :key="item.id"
            class="bg-white border border-slate-100 rounded-3xl p-5 flex flex-col justify-between shadow-sm hover:shadow-md transition-all relative group"
          >
            <div class="flex justify-between items-center mb-4">
              <span
                class="px-3 py-1 bg-indigo-50 text-indigo-600 font-bold font-mono text-[10px] rounded-xl border border-indigo-100"
              >
                #{{ item.maSanPhamChiTiet }}
              </span>
              <span class="flex h-2 w-2 relative">
                <span
                  :class="[
                    'animate-ping absolute inline-flex h-full w-full rounded-full opacity-75',
                    item.trangThai ? 'bg-emerald-400' : 'bg-rose-400',
                  ]"
                ></span>
                <span
                  :class="[
                    'relative inline-block rounded-full h-2 w-2',
                    item.trangThai ? 'bg-emerald-500' : 'bg-rose-500',
                  ]"
                ></span>
              </span>
            </div>

            <div class="flex items-center gap-4 mb-4">
              <div
                class="w-16 h-16 rounded-2xl bg-slate-900 flex items-center justify-center overflow-hidden shrink-0 border border-slate-800"
              >
                <img
                  v-if="item.images && item.images.length > 0"
                  :src="baseUrl + item.images[0]"
                  class="w-full h-full object-cover"
                  @error="handleImageError"
                />
                <span v-else class="text-xl">👕</span>
              </div>
              <div>
                <h3 class="font-bold text-slate-800 text-sm line-clamp-1">
                  {{ item.tenSanPhamChiTiet }}
                </h3>
                <p class="text-[11px] text-slate-400 mt-0.5">
                  Tồn kho:
                  <span class="font-bold font-mono text-slate-700">{{ item.soLuongTon }}</span>
                </p>
              </div>
            </div>

            <div class="grid grid-cols-2 gap-2 mb-4">
              <div class="bg-slate-50/80 border border-slate-100 p-2.5 rounded-xl text-left">
                <span class="block text-[9px] font-bold text-slate-400 uppercase tracking-wider"
                  >Giá Bán Lẻ</span
                >
                <span class="text-xs font-black text-slate-700 font-mono">{{
                  formatCurrency(item.giaBan)
                }}</span>
              </div>
              <div class="bg-slate-50/80 border border-slate-100 p-2.5 rounded-xl text-left">
                <span class="block text-[9px] font-bold text-slate-400 uppercase tracking-wider"
                  >Trạng Thái</span
                >
                <span
                  :class="[
                    'text-[10px] font-bold uppercase',
                    item.trangThai ? 'text-emerald-600' : 'text-rose-600',
                  ]"
                >
                  {{ item.trangThai ? 'Kinh Doanh' : 'Lưu Kho' }}
                </span>
              </div>
            </div>

            <div class="flex justify-between items-center pt-2 border-t border-slate-50">
              <span class="text-[10px] font-bold text-slate-400 uppercase tracking-wider"
                >Thao tác</span
              >
              <div class="flex gap-1">
                <button
                  @click="toggleDetails(item.id)"
                  class="p-2 text-slate-400 hover:text-indigo-600 hover:bg-indigo-50 rounded-xl transition-all"
                  title="Xem chi tiết biến thể"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    class="w-4 h-4"
                    :class="{ 'text-indigo-600': expandedIds.includes(item.id) }"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2.5"
                  >
                    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" />
                    <circle cx="12" cy="12" r="3" />
                  </svg>
                </button>
                <button
                  @click="openModal(item)"
                  class="p-2 text-slate-400 hover:text-blue-600 hover:bg-blue-50 rounded-xl transition-all"
                  title="Sửa"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    class="w-4 h-4"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2.5"
                  >
                    <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" />
                    <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" />
                  </svg>
                </button>
                <button
                  @click="deleteItem(item.id)"
                  class="p-2 text-slate-400 hover:text-rose-600 hover:bg-rose-50 rounded-xl transition-all"
                  title="Xóa"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    class="w-4 h-4"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2.5"
                  >
                    <polyline points="3 6 5 6 21 6" />
                    <path
                      d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"
                    />
                  </svg>
                </button>
              </div>
            </div>

            <div
              v-if="expandedIds.includes(item.id)"
              class="mt-3 pt-3 border-t border-dashed border-slate-200 text-[11px] space-y-3 text-slate-600 bg-slate-50 p-3 rounded-2xl animate-fade-in"
            >
              <div
                class="flex items-center justify-between p-2 bg-white rounded-xl border border-slate-100 shadow-sm"
              >
                <span class="font-bold text-slate-400 uppercase text-[9px] tracking-wider"
                  >Số lượng tồn</span
                >
                <div class="flex items-center gap-2">
                  <span
                    class="text-[10px] font-bold"
                    :class="item.soLuongTon > 10 ? 'text-emerald-600' : 'text-rose-600'"
                  >
                    {{ item.soLuongTon > 10 ? 'Còn hàng' : 'Sắp hết' }}
                  </span>
                  <span class="text-base font-black text-slate-800 font-mono">{{
                    item.soLuongTon
                  }}</span>
                </div>
              </div>

              <div>
                <p class="font-bold text-slate-400 uppercase text-[9px] tracking-wider mb-1">
                  Thông số sản phẩm
                </p>
                <p>
                  Danh mục:
                  <span class="text-slate-800 font-bold">{{
                    item.tenDanhMuc || 'Chưa phân loại'
                  }}</span>
                </p>
                <p>
                  Thương hiệu:
                  <span class="text-slate-800 font-bold">{{
                    item.tenThuongHieu || 'Không rõ'
                  }}</span>
                </p>
                <p>
                  Màu / Size:
                  <span class="text-slate-800 font-bold"
                    >{{ item.tenMauSac || '-' }} / {{ item.tenKichThuoc || '-' }}</span
                  >
                </p>
              </div>
              <div class="pt-1.5 border-t border-slate-200/60">
                <p class="font-bold text-slate-400 uppercase text-[9px] tracking-wider mb-1">
                  Dữ liệu tài chính
                </p>
                <p>
                  Giá nhập kho:
                  <span class="font-bold text-slate-700 font-mono">{{
                    formatCurrency(item.giaNhap)
                  }}</span>
                </p>
                <p>
                  Giá bán:
                  <span class="font-bold text-slate-700 font-mono">{{
                    formatCurrency(item.giaBan)
                  }}</span>
                </p>
              </div>
              <div
                v-if="item.images && item.images.length > 1"
                class="pt-1.5 border-t border-slate-200/60"
              >
                <p class="font-bold text-slate-400 uppercase text-[9px] tracking-wider mb-1">
                  Ảnh biến thể khác
                </p>
                <div class="flex gap-1 overflow-x-auto pt-0.5">
                  <img
                    v-for="(img, idx) in item.images.slice(1)"
                    :key="idx"
                    :src="baseUrl + img"
                    class="w-8 h-8 rounded-lg border border-slate-200 object-cover bg-white"
                    @error="handleImageError"
                  />
                </div>
              </div>
            </div>
          </div>
        </div>

        <div
          v-if="filteredProducts.length === 0"
          class="text-center py-20 bg-white border border-slate-100 rounded-3xl text-slate-400 text-sm"
        >
          Không có sản phẩm biến thể nào được tìm thấy.
        </div>
      </div>

      <div class="xl:col-span-3 space-y-4">
        <div class="bg-white border border-slate-100 p-6 rounded-3xl space-y-4 shadow-sm">
          <div class="flex justify-between items-center mb-2">
            <h2 class="text-xs font-bold text-slate-800 uppercase tracking-wider">
              Cấu Trúc Dữ Liệu
            </h2>
            <span class="w-2 h-2 rounded-full bg-emerald-500"></span>
          </div>

          <div
            class="flex items-center justify-between p-3.5 bg-slate-50 rounded-2xl border border-slate-100/60"
          >
            <div>
              <p class="text-[10px] font-bold text-slate-400 uppercase tracking-wide">
                Tổng hiển thị / gốc
              </p>
              <p class="text-xl font-black text-slate-800 mt-0.5 font-mono">
                {{ filteredProducts.length }} / {{ products.length }}
              </p>
            </div>
            <div class="p-2.5 bg-amber-50 text-amber-600 rounded-xl">📦</div>
          </div>

          <div
            class="flex items-center justify-between p-3.5 bg-slate-50 rounded-2xl border border-slate-100/60"
          >
            <div>
              <p class="text-[10px] font-bold text-slate-400 uppercase tracking-wide">
                Đang bán lẻ
              </p>
              <p class="text-xl font-black text-slate-800 mt-0.5 font-mono">
                {{ products.filter((p) => p.trangThai).length }}
              </p>
            </div>
            <div class="p-2.5 bg-emerald-50 text-emerald-600 rounded-xl">🏷️</div>
          </div>
        </div>

        <div class="bg-[#171933] text-indigo-100 p-6 rounded-3xl shadow-sm space-y-2">
          <div
            class="flex items-center gap-2 text-xs font-bold uppercase tracking-wider text-white"
          >
            <span class="text-indigo-400">✦</span> Lưu ý vận hành
          </div>
          <p class="text-[11px] text-slate-400 leading-relaxed">
            Mã định danh SKU của biến thể là duy nhất. Khi cập nhật hình ảnh thông qua URL, hãy đảm
            bảo giao thức bảo mật mã hóa HTTPS hoạt động bình thường để tránh lỗi hiển thị.
          </p>
        </div>
      </div>
    </div>

    <Transition name="slide-fade">
      <div
        v-if="toast.show"
        :class="toast.type === 'success' ? 'bg-emerald-600' : 'bg-rose-600'"
        class="fixed top-6 right-6 z-[100] px-5 py-3.5 rounded-2xl text-white shadow-xl flex items-center gap-3 font-bold text-xs"
      >
        <span>{{ toast.type === 'success' ? '✓' : '✕' }}</span>
        <p>{{ toast.message }}</p>
      </div>
    </Transition>

    <Transition name="fade">
      <div v-if="isModalOpen" class="fixed inset-0 z-50 flex items-center justify-center p-4">
        <div
          class="absolute inset-0 bg-slate-900/30 backdrop-blur-sm"
          @click="isModalOpen = false"
        ></div>
        <div
          class="bg-white rounded-3xl shadow-xl w-full max-w-xl z-10 p-6 border border-slate-100"
        >
          <h3 class="text-xs font-bold uppercase tracking-widest text-indigo-600 mb-6">
            {{ isEditMode ? '⚡ Cập nhật biến thể sản phẩm' : '✨ Khởi tạo biến thể mới' }}
          </h3>

          <form @submit.prevent="saveData" class="space-y-4">
            <div class="grid grid-cols-2 gap-4">
              <div class="col-span-2">
                <label class="block text-[10px] font-bold text-slate-400 uppercase mb-1"
                  >Tên sản phẩm chi tiết</label
                >
                <input
                  v-model="formData.tenSanPhamChiTiet"
                  class="w-full bg-slate-50 border border-slate-100 px-4 py-2.5 rounded-2xl text-sm outline-none focus:bg-white focus:border-indigo-500 transition-all"
                  required
                />
              </div>

              <div>
                <label class="block text-[10px] font-bold text-slate-400 uppercase mb-1"
                  >Mã SKU</label
                >
                <input
                  v-model="formData.maSanPhamChiTiet"
                  class="w-full bg-slate-50 border border-slate-100 px-4 py-2.5 rounded-2xl text-sm outline-none uppercase font-mono focus:bg-white focus:border-indigo-500 transition-all"
                  required
                />
              </div>

              <div>
                <label class="block text-[10px] font-bold text-slate-400 uppercase mb-1"
                  >Số lượng tồn</label
                >
                <input
                  v-model="formData.soLuongTon"
                  type="number"
                  class="w-full bg-slate-50 border border-slate-100 px-4 py-2.5 rounded-2xl text-sm outline-none font-mono focus:bg-white focus:border-indigo-500 transition-all"
                  required
                />
              </div>

              <div>
                <label class="block text-[10px] font-bold text-slate-400 uppercase mb-1"
                  >ID Màu sắc</label
                >
                <input
                  v-model="formData.idMauSac"
                  type="number"
                  class="w-full bg-slate-50 border border-slate-100 px-4 py-2.5 rounded-2xl text-sm outline-none font-mono focus:bg-white focus:border-indigo-500 transition-all"
                />
              </div>

              <div>
                <label class="block text-[10px] font-bold text-slate-400 uppercase mb-1"
                  >ID Kích thước</label
                >
                <input
                  v-model="formData.idKichThuoc"
                  type="number"
                  class="w-full bg-slate-50 border border-slate-100 px-4 py-2.5 rounded-2xl text-sm outline-none font-mono focus:bg-white focus:border-indigo-500 transition-all"
                />
              </div>

              <div>
                <label class="block text-[10px] font-bold text-slate-400 uppercase mb-1"
                  >Giá bán</label
                >
                <input
                  v-model="formData.giaBan"
                  type="number"
                  class="w-full bg-slate-50 border border-slate-100 px-4 py-2.5 rounded-2xl text-sm outline-none font-mono focus:bg-white focus:border-indigo-500 transition-all"
                  required
                />
              </div>

              <div>
                <label class="block text-[10px] font-bold text-slate-400 uppercase mb-1"
                  >Giá nhập</label
                >
                <input
                  v-model="formData.giaNhap"
                  type="number"
                  class="w-full bg-slate-50 border border-slate-100 px-4 py-2.5 rounded-2xl text-sm outline-none font-mono focus:bg-white focus:border-indigo-500 transition-all"
                  required
                />
              </div>

              <div class="col-span-2">
                <label class="block text-[10px] font-bold text-slate-400 uppercase mb-1"
                  >ID Sản phẩm gốc</label
                >
                <input
                  v-model="formData.idSanPham"
                  type="number"
                  class="w-full bg-slate-50 border border-slate-100 px-4 py-2.5 rounded-2xl text-sm outline-none font-mono focus:bg-white focus:border-indigo-500 transition-all"
                  required
                />
              </div>

              <div class="col-span-2">
                <label class="block text-[10px] font-bold text-slate-400 uppercase mb-1"
                  >Hình ảnh biến thể</label
                >
                <input
                  type="file"
                  multiple
                  accept="image/*"
                  @change="handleFileUpload"
                  class="w-full text-xs text-slate-500 file:mr-4 file:py-2 file:px-4 file:rounded-xl file:border-0 file:text-xs file:font-bold file:bg-indigo-50 file:text-indigo-700 hover:file:bg-indigo-100 transition-all border border-slate-100 rounded-2xl p-1"
                />
              </div>

              <div class="col-span-2 flex gap-3 pt-2">
                <button
                  type="button"
                  @click="formData.trangThai = true"
                  :class="[
                    'flex-1 py-3 text-xs font-bold rounded-2xl border transition-all',
                    formData.trangThai
                      ? 'bg-emerald-50 border-emerald-500 text-emerald-700'
                      : 'bg-slate-50 border-slate-100 text-slate-400',
                  ]"
                >
                  KINH DOANH
                </button>
                <button
                  type="button"
                  @click="formData.trangThai = false"
                  :class="[
                    'flex-1 py-3 text-xs font-bold rounded-2xl border transition-all',
                    !formData.trangThai
                      ? 'bg-rose-50 border-rose-500 text-rose-700'
                      : 'bg-slate-50 border-slate-100 text-slate-400',
                  ]"
                >
                  LƯU KHO
                </button>
              </div>
            </div>

            <div class="flex justify-end gap-2 pt-4">
              <button
                type="button"
                @click="isModalOpen = false"
                class="px-5 py-2.5 bg-slate-100 hover:bg-slate-200 rounded-2xl text-xs font-bold transition-all"
              >
                HỦY
              </button>
              <button
                type="submit"
                class="px-6 py-2.5 bg-indigo-600 text-white rounded-2xl text-xs font-bold shadow-md shadow-indigo-600/10 transition-all"
              >
                LƯU DỮ LIỆU
              </button>
            </div>
          </form>
        </div>
      </div>
    </Transition>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import {
  getSanPhamChiTietByProductId,
  getAllSanPhamChiTiet,
  createSanPhamChiTiet,
  updateSanPhamChiTiet,
  deleteSanPhamChiTiet,
} from '@/service/SanPhamChiTiet'

const route = useRoute()
const productId = route.params.id

// Các biến state
const selectedFiles = ref([]) // Dùng để gửi lên server
const previewImages = ref([]) // Dùng để hiển thị trong form
const products = ref([])
const expandedIds = ref([])
const isModalOpen = ref(false)
const isEditMode = ref(false)
const formData = ref({})
const toast = reactive({ show: false, message: '', type: 'success' })
const baseUrl = 'http://localhost:8080'

// Xử lý chọn file
const handleFileUpload = (event) => {
  const files = event.target.files
  if (!files) return

  // 1. Cập nhật biến để gửi lên server
  selectedFiles.value = Array.from(files)

  // 2. Cập nhật biến để preview ảnh
  previewImages.value = []
  Array.from(files).forEach((file) => {
    const reader = new FileReader()
    reader.onload = (e) => {
      previewImages.value.push(e.target.result)
    }
    reader.readAsDataURL(file)
  })
}

// Logic dữ liệu
onMounted(() => {
  if (route.params.id) {
    loadSPCT(route.params.id)
  } else {
    loadAll()
  }
})

const loadSPCT = async (id) => {
  try {
    const data = await getSanPhamChiTietByProductId(id)
    products.value = data
  } catch (error) {
    console.error(error)
  }
}

const loadAll = async () => {
  try {
    const data = await getAllSanPhamChiTiet()
    products.value = data
  } catch (error) {
    console.error(error)
  }
}

const reloadData = async () => {
  if (productId) await loadSPCT(productId)
  else await loadAll()
}

const openModal = (item = null) => {
  isEditMode.value = !!item
  formData.value = item ? { ...item } : { trangThai: true, giaNhap: 0, giaBan: 0, soLuongTon: 0 }
  previewImages.value = [] // Reset preview khi mở modal
  selectedFiles.value = [] // Reset files khi mở modal
  isModalOpen.value = true
}

const saveData = async () => {
  console.log('Payload chuẩn bị gửi:', formData.value)
  try {
    const payload = new FormData()

    // Gán các trường từ form vào payload
    payload.append('idSanPham', formData.value.idSanPham || '')
    payload.append('idMauSac', formData.value.idMauSac || 0)
    payload.append('idKichThuoc', formData.value.idKichThuoc || 0)
    payload.append('maSanPhamChiTiet', formData.value.maSanPhamChiTiet || '')
    payload.append('tenSanPhamChiTiet', formData.value.tenSanPhamChiTiet || '')
    payload.append('giaNhap', formData.value.giaNhap || 0)
    payload.append('giaBan', formData.value.giaBan || 0)
    payload.append('soLuongTon', formData.value.soLuongTon || 0)
    payload.append('trangThai', formData.value.trangThai ?? true)

    // Gán các file đã chọn vào payload
    selectedFiles.value.forEach((file) => {
      payload.append('files', file)
    })

    if (isEditMode.value) {
      // Đảm bảo formData.value.id có giá trị ở đây
      console.log('Đang gửi update cho ID:', formData.value.id)
      await updateSanPhamChiTiet(formData.value.id, payload)
      showToast('Cập nhật thành công!')
    } else {
      await createSanPhamChiTiet(payload)
      showToast('Thêm mới thành công!')
    }

    isModalOpen.value = false
    selectedFiles.value = []
    previewImages.value = []
    await reloadData()
  } catch (error) {
    showToast('Có lỗi xảy ra: ' + error.message, 'error')
  }
}

// Các hàm phụ trợ
const filters = reactive({ searchQuery: '', status: 'all' })
const filteredProducts = computed(() => {
  if (!Array.isArray(products.value)) return []
  return products.value.filter((item) => {
    const query = filters.searchQuery.trim().toLowerCase()
    const matchesSearch =
      !query ||
      item.tenSanPhamChiTiet?.toLowerCase().includes(query) ||
      item.maSanPhamChiTiet?.toLowerCase().includes(query)
    let matchesStatus = true
    if (filters.status === 'kinhDoanh') matchesStatus = item.trangThai === true
    else if (filters.status === 'luuKho') matchesStatus = item.trangThai === false
    return matchesSearch && matchesStatus
  })
})

const showToast = (message, type = 'success') => {
  toast.message = message
  toast.type = type
  toast.show = true
  setTimeout(() => {
    toast.show = false
  }, 3000)
}

const toggleDetails = (id) => {
  const index = expandedIds.value.indexOf(id)
  if (index > -1) expandedIds.value.splice(index, 1)
  else expandedIds.value.push(id)
}

const deleteItem = async (id) => {
  if (confirm('Xác nhận xóa sản phẩm #' + id + '?')) {
    try {
      await deleteSanPhamChiTiet(id)
      await reloadData()
      showToast('Đã xóa thành công!')
    } catch (error) {
      showToast('Không thể xóa sản phẩm này!', 'error')
    }
  }
}

const formatCurrency = (val) =>
  new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(val || 0)
const handleImageError = (e) => {
  e.target.src = 'https://via.placeholder.com/150'
}
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
.slide-fade-enter-active {
  transition: all 0.3s ease-out;
}
.slide-fade-leave-active {
  transition: all 0.3s cubic-bezier(1, 0.5, 0.8, 1);
}
.slide-fade-enter-from,
.slide-fade-leave-to {
  transform: translateX(20px);
  opacity: 0;
}
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-5px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
.animate-fade-in {
  animation: fadeIn 0.2s ease-out forwards;
}
</style>
