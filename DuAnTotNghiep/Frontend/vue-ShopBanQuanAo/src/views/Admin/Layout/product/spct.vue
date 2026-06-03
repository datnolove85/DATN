<template>
  <div
    class="space-y-6 max-w-full mx-auto p-4 animate-fade-in bg-slate-50 text-slate-800 rounded-2xl selection:bg-indigo-100 selection:text-indigo-900 overflow-hidden"
  >
    <!-- Header -->
    <div
      class="relative p-6 bg-gradient-to-r from-sky-600 via-blue-700 to-indigo-800 rounded-2xl border border-blue-500 shadow-[0_8px_30px_rgba(0,0,0,0.15)] flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 overflow-hidden group transition-all duration-300 hover:shadow-[0_8px_30px_rgba(59,130,246,0.25)]"
    >
      <div
        class="absolute inset-0 bg-[radial-gradient(circle_at_30%_30%,rgba(255,255,255,0.15),transparent_50%)] transition-opacity duration-500 group-hover:opacity-100"
      ></div>

      <div class="relative z-10">
        <h1
          class="text-2xl font-black tracking-wider text-white uppercase group-hover:tracking-widest transition-all duration-500 ease-in-out"
        >
          Quản lý sản phẩm
        </h1>

        <p class="text-xs text-blue-100 mt-1.5 flex items-center gap-2 font-semibold tracking-wide">
          <span class="relative flex h-2 w-2">
            <span
              class="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-300 opacity-75"
            ></span>
            <span class="relative inline-block rounded-full h-2 w-2 bg-emerald-400"></span>
          </span>
          Danh mục & Thông số kỹ thuật chi tiết
        </p>
      </div>

      <button
        @click="openModal()"
        class="relative z-10 flex items-center gap-2 px-5 py-2.5 bg-white/15 backdrop-blur-md hover:bg-white/25 text-white rounded-xl text-xs font-bold border border-white/20 shadow-[0_4px_12px_rgba(0,0,0,0.15)] hover:shadow-[0_6px_20px_rgba(255,255,255,0.15)] transition-all duration-300 active:scale-95 active:duration-75 group/btn overflow-hidden tracking-wider"
      >
        <span
          class="absolute inset-0 w-full h-full bg-gradient-to-r from-transparent via-white/20 to-transparent -translate-x-full group-hover/btn:animate-shimmer"
        ></span>

        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          stroke-width="3"
          stroke="currentColor"
          class="w-3.5 h-3.5 transition-transform group-hover/btn:rotate-90 duration-300"
        >
          <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
        </svg>

        TẠO SẢN PHẨM MỚI
      </button>
    </div>

    <!-- Table / List -->
    <div class="space-y-3 overflow-hidden">
      <!-- Table Header (Desktop) -->
      <div
        class="hidden md:grid grid-cols-12 gap-0 px-6 py-3.5 bg-white border border-b-2 border-slate-300 rounded-t-xl text-[11px] uppercase text-slate-500 font-bold tracking-widest select-none shadow-sm"
      >
        <div class="col-span-4 px-4 border-r border-slate-200">Sản phẩm / Mã định danh</div>
        <div class="col-span-2 px-4 border-r border-slate-200">Giá bán lẻ</div>
        <div class="col-span-2 text-center border-r border-slate-200">Tồn kho</div>
        <div class="col-span-2 text-center border-r border-slate-200">Trạng thái</div>
        <div class="col-span-2 text-center">Tùy chọn</div>
      </div>

      <!-- Table Body -->
      <div class="space-y-2.5">
        <div
          v-for="item in products"
          :key="item.id"
          :class="[
            'relative bg-white border rounded-xl md:rounded-lg transition-all duration-300 ease-in-out shadow-[0_2px_4px_rgba(0,0,0,0.01)] group overflow-hidden pl-0',
            expandedIds.includes(item.id)
              ? 'border-indigo-600 shadow-[0_12px_30px_-5px_rgba(99,102,241,0.2)]'
              : 'border-slate-300 hover:border-indigo-400 hover:shadow-[0_10px_25px_-5px_rgba(99,102,241,0.12)]',
          ]"
        >
          <!-- Thanh màu Indigo dọc ở cạnh trái -->
          <div
            :class="[
              'absolute left-0 top-0 bottom-0 w-1.5 bg-indigo-600 transition-all duration-300 transform origin-left',
              expandedIds.includes(item.id) ? 'scale-x-100' : 'scale-x-0',
            ]"
          ></div>

          <!-- Main Row -->
          <div
            :class="[
              'grid grid-cols-1 md:grid-cols-12 gap-4 md:gap-0 px-6 py-4.5 md:py-3.5 items-center transition-colors duration-300',
              expandedIds.includes(item.id) ? 'bg-indigo-50/50' : 'group-hover:bg-slate-50/80',
            ]"
          >
            <!-- Tên sản phẩm -->
            <div
              class="col-span-4 md:px-4 md:border-r md:border-slate-200 h-full flex flex-col justify-center group-hover:translate-x-1 transition-transform duration-300"
            >
              <div
                :class="[
                  'text-[15px] font-extrabold capitalize truncate tracking-wide transition-colors leading-snug',
                  expandedIds.includes(item.id) ? 'text-indigo-600 font-black' : 'text-slate-700',
                ]"
              >
                {{ item.tenSanPhamChiTiet }}
              </div>
              <div
                class="inline-block mt-1 text-xs font-mono font-bold text-indigo-600 bg-indigo-50 border border-indigo-100 px-1.5 py-0.5 rounded w-max"
              >
                {{ item.maSanPhamChiTiet }}
              </div>
            </div>

            <!-- Giá tiền -->
            <div
              class="col-span-2 md:px-4 md:border-r md:border-slate-200 h-full flex items-center font-mono text-[13px] font-bold text-slate-900 tracking-wide"
            >
              {{ formatCurrency(item.giaBan) }}
            </div>

            <!-- Tồn kho -->
            <div
              class="col-span-2 flex md:justify-center items-center gap-2 md:gap-0 md:border-r md:border-slate-200 h-full"
            >
              <span class="md:hidden text-xs font-bold text-slate-400 uppercase tracking-wider"
                >Tồn kho:</span
              >
              <span
                :class="[
                  'px-2.5 py-0.5 border text-xs rounded font-mono font-bold tracking-wide transition-all',
                  expandedIds.includes(item.id)
                    ? 'bg-indigo-600 text-white border-indigo-600 shadow-sm'
                    : 'bg-slate-100 border-slate-300 text-slate-800 group-hover:bg-indigo-600 group-hover:text-white group-hover:border-indigo-600',
                ]"
              >
                {{ item.soLuongTon }}
              </span>
            </div>

            <!-- Trạng thái -->
            <div
              class="col-span-2 flex md:justify-center items-center gap-2 md:gap-0 md:border-r md:border-slate-200 h-full"
            >
              <span class="md:hidden text-xs font-bold text-slate-400 uppercase tracking-wider"
                >Trạng thái:</span
              >
              <span
                :class="[
                  'inline-flex items-center gap-1.5 px-3 py-1 text-xs font-bold uppercase tracking-wider rounded-full border transition-all duration-300 group-hover:scale-105',
                  item.trangThai
                    ? 'bg-emerald-50 text-emerald-700 border-emerald-300 group-hover:bg-emerald-500 group-hover:text-white group-hover:border-emerald-500'
                    : 'bg-rose-50 text-rose-700 border-rose-300 group-hover:bg-rose-500 group-hover:text-white group-hover:border-rose-500',
                ]"
              >
                <span
                  :class="[
                    'w-1.5 h-1.5 rounded-full',
                    item.trangThai
                      ? 'bg-emerald-500 group-hover:bg-white animate-pulse'
                      : 'bg-rose-500 group-hover:bg-white',
                  ]"
                ></span>
                {{ item.trangThai ? 'Active' : 'Muted' }}
              </span>
            </div>

            <!-- Tùy chọn -->
            <div class="col-span-2 flex justify-end md:justify-center items-center pt-3 md:pt-0">
              <div
                class="inline-flex items-center bg-white border border-slate-300 rounded-xl p-1.5 shadow-sm hover:border-indigo-400 hover:shadow-md transition-all duration-300"
              >
                <button
                  @click="toggleDetails(item.id)"
                  :class="[
                    'p-1.5 rounded-lg transition-all duration-300',
                    expandedIds.includes(item.id)
                      ? 'bg-indigo-600 text-white scale-110 shadow-sm'
                      : 'text-indigo-600 hover:bg-indigo-100',
                  ]"
                  title="Chi tiết"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    class="w-5 h-5 transition-transform duration-300"
                    :class="{ 'rotate-180': expandedIds.includes(item.id) }"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2.5"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  >
                    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" />
                    <circle cx="12" cy="12" r="3" />
                  </svg>
                </button>

                <div class="w-px h-5 bg-slate-300 mx-1"></div>

                <button
                  @click="openModal(item)"
                  class="p-1.5 text-amber-600 hover:bg-amber-100 rounded-lg transition-all duration-200"
                  title="Chỉnh sửa"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    class="w-5 h-5"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2.5"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  >
                    <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" />
                    <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" />
                  </svg>
                </button>

                <div class="w-px h-5 bg-slate-300 mx-1"></div>

                <button
                  @click="deleteItem(item.id)"
                  class="p-1.5 text-rose-600 hover:bg-rose-100 rounded-lg transition-all duration-200"
                  title="Xóa"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    class="w-5 h-5"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2.5"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  >
                    <polyline points="3 6 5 6 21 6" />
                    <path
                      d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"
                    />
                  </svg>
                </button>
              </div>
            </div>
          </div>

          <!-- Expanded Details Section -->
          <div
            v-if="expandedIds.includes(item.id)"
            class="px-6 pb-6 pt-5 border-t border-indigo-100 bg-gradient-to-b from-indigo-50/30 to-white/90 animate-slide-down"
          >
            <div
              class="mb-5 pb-2.5 border-b border-dashed border-slate-200 flex items-center gap-2"
            >
              <span class="w-1.5 h-1.5 rounded-full bg-indigo-600 animate-pulse"></span>
              <span class="text-xs font-bold text-slate-500 uppercase tracking-wider"
                >Đang xem dữ liệu của:</span
              >
              <span
                class="text-xs font-bold bg-indigo-100 text-indigo-800 px-2.5 py-0.5 rounded-md capitalize tracking-wide shadow-sm"
              >
                {{ item.tenSanPhamChiTiet }}
              </span>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-3 gap-8 text-sm leading-relaxed">
              <div>
                <!-- ĐÃ SỬA LỖI GỐI CLASS TẠI ĐÂY: loại bỏ text-slate-900 dư thừa -->
                <p
                  class="font-extrabold uppercase text-[11px] mb-2.5 tracking-widest text-indigo-900"
                >
                  Thông số kỹ thuật
                </p>
                <div class="space-y-2 font-medium text-slate-600">
                  <p>
                    Danh mục:
                    <span class="font-bold text-slate-900">{{
                      item.tenDanhMuc || 'Chưa phân loại'
                    }}</span>
                  </p>
                  <p>
                    Thương hiệu:
                    <span class="font-bold text-slate-900">{{
                      item.tenThuongHieu || 'Không rõ'
                    }}</span>
                  </p>
                  <p>
                    Màu / Kích thước:
                    <span class="font-bold text-slate-900"
                      >{{ item.tenMauSac || '-' }} / {{ item.tenKichThuoc || '-' }}</span
                    >
                  </p>
                </div>
              </div>
              <div>
                <!-- ĐÃ SỬA LỖI GỐI CLASS TẠI ĐÂY: loại bỏ text-slate-900 dư thừa -->
                <p
                  class="font-extrabold uppercase text-[11px] mb-2.5 tracking-widest text-indigo-900"
                >
                  Thông tin tài chính
                </p>
                <div class="space-y-2 font-medium text-slate-600">
                  <p>
                    Giá nhập kho:
                    <span
                      class="font-bold font-mono text-slate-900 bg-slate-100 px-1 py-0.5 rounded"
                      >{{ formatCurrency(item.giaNhap) }}</span
                    >
                  </p>
                  <p>
                    Giá niêm yết gốc:
                    <span
                      class="font-bold font-mono text-slate-900 bg-slate-100 px-1 py-0.5 rounded"
                      >{{ formatCurrency(item.giaGoc) }}</span
                    >
                  </p>
                  <p>
                    Giá đang bán:
                    <span
                      class="font-extrabold font-mono text-indigo-700 bg-indigo-50 border border-indigo-100 px-1.5 py-0.5 rounded"
                      >{{ formatCurrency(item.giaBan) }}</span
                    >
                  </p>
                </div>
              </div>
              <div>
                <p
                  class="font-extrabold uppercase text-[11px] mb-2.5 tracking-widest text-indigo-900"
                >
                  Thư viện hình ảnh
                </p>
                <div class="flex gap-2.5 overflow-x-auto scrollbar-thin pb-2">
                  <img
                    v-for="(img, idx) in item.images"
                    :key="idx"
                    :src="baseUrl + img"
                    class="w-14 h-14 rounded-xl border border-slate-200 object-cover bg-white shadow-sm hover:scale-110 hover:border-indigo-400 transition-all duration-300"
                    @error="handleImageError"
                  />
                  <div
                    v-if="!item.images || item.images.length === 0"
                    class="text-slate-400 italic text-xs py-2"
                  >
                    Không có hình ảnh hiển thị
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Empty State -->
      <div
        v-if="products.length === 0"
        class="text-center py-16 border-2 border-dashed border-slate-300 rounded-2xl bg-white shadow-sm"
      >
        <p class="text-slate-700 font-bold text-sm">Kho lưu trữ sản phẩm trống rỗng</p>
      </div>
    </div>

    <!-- Modal Form -->
    <div v-if="isModalOpen" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div
        class="absolute inset-0 bg-slate-900/30 backdrop-blur-[4px]"
        @click="isModalOpen = false"
      ></div>
      <div
        class="bg-white border border-slate-300 rounded-2xl shadow-[0_20px_60px_-15px_rgba(0,0,0,0.12)] w-full max-w-2xl z-10 p-6 animate-scale-up"
      >
        <h3 class="text-xs font-black uppercase tracking-widest text-indigo-600 mb-6">
          {{ isEditMode ? '⚡ CẬP NHẬT SẢN PHẨM' : '✨ KHỞI TẠO SẢN PHẨM' }}
        </h3>

        <form @submit.prevent="saveData" class="space-y-4">
          <div class="grid grid-cols-2 gap-4">
            <div class="col-span-2">
              <label class="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-1"
                >Tên sản phẩm chi tiết</label
              >
              <input
                v-model="formData.tenSanPhamChiTiet"
                placeholder="Ví dụ: Áo Sơ Mi Nam Tay Dài Cao Cấp"
                class="w-full border border-slate-300 px-4 py-2.5 rounded-xl text-sm focus:ring-2 focus:ring-indigo-500/20 outline-none transition-all"
                required
              />
            </div>
            <div>
              <label class="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-1"
                >Mã định danh (SKU)</label
              >
              <input
                v-model="formData.maSanPhamChiTiet"
                placeholder="Mã sản phẩm"
                class="w-full border border-slate-300 px-4 py-2.5 rounded-xl text-sm focus:ring-2 focus:ring-indigo-500/20 outline-none transition-all uppercase font-mono"
                required
              />
            </div>
            <div>
              <label class="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-1"
                >Số lượng tồn kho</label
              >
              <input
                v-model="formData.soLuongTon"
                type="number"
                placeholder="Số lượng tồn"
                class="w-full border border-slate-300 px-4 py-2.5 rounded-xl text-sm focus:ring-2 focus:ring-indigo-500/20 outline-none transition-all font-mono"
                required
              />
            </div>
            <div>
              <label class="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-1"
                >Giá gốc</label
              >
              <input
                v-model="formData.giaGoc"
                type="number"
                placeholder="Giá gốc"
                class="w-full border border-slate-300 px-4 py-2.5 rounded-xl text-sm focus:ring-2 focus:ring-indigo-500/20 outline-none transition-all font-mono"
              />
            </div>
            <div>
              <label class="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-1"
                >Giá nhập</label
              >
              <input
                v-model="formData.giaNhap"
                type="number"
                placeholder="Giá nhập"
                class="w-full border border-slate-300 px-4 py-2.5 rounded-xl text-sm focus:ring-2 focus:ring-indigo-500/20 outline-none transition-all font-mono"
                required
              />
            </div>
            <div>
              <label class="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-1"
                >Giá bán niêm yết</label
              >
              <input
                v-model="formData.giaBan"
                type="number"
                placeholder="Giá bán"
                class="w-full border border-slate-300 px-4 py-2.5 rounded-xl text-sm focus:ring-2 focus:ring-indigo-500/20 outline-none transition-all font-mono"
                required
              />
            </div>
            <div>
              <label class="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-1"
                >ID Sản phẩm gốc</label
              >
              <input
                v-model="formData.idSanPham"
                type="number"
                placeholder="ID Sản phẩm gốc"
                class="w-full border border-slate-300 px-4 py-2.5 rounded-xl text-sm focus:ring-2 focus:ring-indigo-500/20 outline-none transition-all font-mono"
                required
              />
            </div>
            <div class="col-span-2 flex gap-3 pt-2">
              <button
                type="button"
                @click="formData.trangThai = true"
                :class="[
                  'flex-1 py-2.5 text-xs font-bold rounded-xl border transition-all tracking-wider',
                  formData.trangThai
                    ? 'bg-emerald-50 border-emerald-500 text-emerald-700'
                    : 'bg-slate-50 border-slate-200 text-slate-400',
                ]"
              >
                HOẠT ĐỘNG
              </button>
              <button
                type="button"
                @click="formData.trangThai = false"
                :class="[
                  'flex-1 py-2.5 text-xs font-bold rounded-xl border transition-all tracking-wider',
                  !formData.trangThai
                    ? 'bg-rose-50 border-rose-500 text-rose-700'
                    : 'bg-slate-50 border-slate-200 text-slate-400',
                ]"
              >
                TẠM NGỪNG
              </button>
            </div>
          </div>

          <div class="flex justify-end gap-2 pt-4">
            <button
              type="button"
              @click="isModalOpen = false"
              class="px-4 py-2 bg-slate-100 hover:bg-slate-200 rounded-xl text-xs font-bold transition-colors tracking-wide"
            >
              HỦY
            </button>
            <button
              type="submit"
              class="px-5 py-2 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl text-xs font-bold shadow-lg shadow-indigo-100 transition-all tracking-wide"
            >
              LƯU DỮ LIỆU
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import {
  getAllSanPhamChiTiet,
  createSanPhamChiTiet,
  updateSanPhamChiTiet,
  deleteSanPhamChiTiet,
} from '@/service/SanPhamChiTiet'

const baseUrl = 'http://localhost:8080'
const products = ref([])
const expandedIds = ref([])
const isModalOpen = ref(false)
const isEditMode = ref(false)
const formData = ref({})

const loadData = async () => {
  try {
    products.value = await getAllSanPhamChiTiet()
  } catch (error) {
    console.error('Lỗi khi load danh sách sản phẩm:', error)
  }
}

const toggleDetails = (id) => {
  const index = expandedIds.value.indexOf(id)
  if (index > -1) {
    expandedIds.value.splice(index, 1)
  } else {
    expandedIds.value.push(id)
  }
}

const openModal = (item = null) => {
  isEditMode.value = !!item
  formData.value = item
    ? { ...item }
    : { trangThai: true, giaNhap: 0, giaGoc: 0, giaBan: 0, soLuongTon: 0 }
  isModalOpen.value = true
}

const saveData = async () => {
  try {
    const payload = {
      idSanPham: Number(formData.value.idSanPham),
      idMauSac: Number(formData.value.idMauSac || 0),
      idKichThuoc: Number(formData.value.idKichThuoc || 0),
      maSanPhamChiTiet: formData.value.maSanPhamChiTiet,
      tenSanPhamChiTiet: formData.value.tenSanPhamChiTiet,
      giaNhap: formData.value.giaNhap,
      giaGoc: formData.value.giaGoc || 0,
      giaBan: formData.value.giaBan,
      soLuongTon: Number(formData.value.soLuongTon),
      trangThai: formData.value.trangThai,
    }

    if (isEditMode.value) {
      await updateSanPhamChiTiet(formData.value.id, payload)
    } else {
      await createSanPhamChiTiet(payload)
    }

    isModalOpen.value = false
    await loadData()
  } catch (error) {
    alert('Lỗi khi thực thi: ' + error.message)
  }
}

const deleteItem = async (id) => {
  if (confirm('Xác nhận xóa sản phẩm #' + id + '?')) {
    try {
      await deleteSanPhamChiTiet(id)
      await loadData()
    } catch (error) {
      alert('Không thể xóa sản phẩm này. Lỗi: ' + error.message)
    }
  }
}

const formatCurrency = (val) =>
  new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(val || 0)

const handleImageError = (e) => {
  e.target.src = 'https://via.placeholder.com/150'
}

onMounted(loadData)
</script>

<style scoped>
.scrollbar-thin::-webkit-scrollbar {
  height: 5px;
}
.scrollbar-thin::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 99px;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(8px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
@keyframes scaleUp {
  from {
    opacity: 0;
    transform: scale(0.96);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}
@keyframes shimmer {
  100% {
    transform: translateX(100%);
  }
}

.animate-fade-in {
  animation: fadeIn 0.35s ease-out forwards;
}
.animate-slide-down {
  animation: slideDown 0.3s cubic-bezier(0.16, 1, 0.3, 1) forwards;
}
.animate-scale-up {
  animation: scaleUp 0.3s cubic-bezier(0.34, 1.56, 0.64, 1) forwards;
}
.animate-shimmer {
  animation: shimmer 1.5s infinite;
}
</style>
