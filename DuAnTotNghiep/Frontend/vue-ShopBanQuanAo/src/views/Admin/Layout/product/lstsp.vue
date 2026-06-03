<template>
  <div
    class="space-y-6 max-w-full mx-auto p-4 animate-fade-in bg-slate-50 text-slate-800 rounded-2xl selection:bg-indigo-100 selection:text-indigo-900 overflow-hidden"
  >
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

        <p class="text-xs text-blue-100 mt-1 flex items-center gap-2 font-semibold">
          <span class="relative flex h-2 w-2">
            <span
              class="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-300 opacity-75"
            ></span>
            <span class="relative inline-block rounded-full h-2 w-2 bg-emerald-400"></span>
          </span>
          Hệ thống cơ sở dữ liệu thời gian thực
        </p>
      </div>

      <button
        @click="openAddModal"
        class="relative z-10 flex items-center gap-2 px-5 py-2.5 bg-white/15 backdrop-blur-md hover:bg-white/25 text-white rounded-xl text-xs font-bold border border-white/20 shadow-[0_4px_12px_rgba(0,0,0,0.15)] hover:shadow-[0_6px_20px_rgba(255,255,255,0.15)] transition-all duration-300 active:scale-95 active:duration-75 group/btn overflow-hidden"
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

        THÊM MỚI SẢN PHẨM
      </button>
    </div>

    <Transition name="slide-fade">
      <div
        v-if="toast.show"
        :class="toast.type === 'success' ? 'bg-emerald-500' : 'bg-rose-500'"
        class="fixed top-6 right-6 z-[100] px-6 py-4 rounded-xl text-white shadow-2xl flex items-center gap-3 min-w-[300px]"
      >
        <span>{{ toast.type === 'success' ? '✅' : '❌' }}</span>
        <p class="font-bold text-xs">{{ toast.message }}</p>
      </div>
    </Transition>

    <div class="space-y-3 overflow-hidden">
      <div
        class="hidden md:grid grid-cols-12 gap-0 px-6 py-3 bg-white border border-b-2 border-slate-300 rounded-t-xl text-[11px] uppercase text-indigo-950 font-black tracking-widest select-none shadow-sm"
      >
        <div class="col-span-4 px-4 border-r border-slate-200">Sản phẩm</div>
        <div class="col-span-4 px-4 border-r border-slate-200">Thông tin (DM / TH)</div>
        <div class="col-span-2 text-center border-r border-slate-200">Trạng thái</div>
        <div class="col-span-2 text-center">Tùy chọn</div>
      </div>

      <div class="space-y-2">
        <div
          v-for="product in products"
          :key="product.id"
          class="grid grid-cols-1 md:grid-cols-12 gap-4 md:gap-0 px-6 py-4 md:py-3 items-center bg-white border border-slate-300 hover:border-indigo-500 hover:bg-slate-50/80 rounded-xl md:rounded-lg transition-all duration-300 ease-in-out shadow-[0_2px_4px_rgba(0,0,0,0.01)] hover:shadow-[0_10px_25px_-5px_rgba(99,102,241,0.12)] group"
        >
          <div
            class="col-span-4 flex items-center gap-4 md:px-4 md:border-r md:border-slate-200 h-full"
          >
            <div
              class="w-10 h-10 rounded-lg bg-slate-100 flex items-center justify-center overflow-hidden shrink-0 border border-slate-200 group-hover:border-indigo-300 transition-all"
            >
              <img
                v-if="product.image"
                :src="getImageUrl(product.image)"
                class="w-full h-full object-cover"
              />
              <span v-else class="text-xs">👕</span>
            </div>
            <div class="group-hover:translate-x-1.5 transition-transform duration-300">
              <div class="font-bold text-slate-800 text-sm capitalize">
                {{ product.tenSanPham }}
              </div>
              <div
                class="inline-block px-2 py-0.5 mt-0.5 bg-slate-50 text-indigo-800 font-black rounded border border-slate-300 text-[10px] font-mono tracking-wider group-hover:bg-indigo-600 group-hover:text-white group-hover:border-indigo-600 transition-all"
              >
                #{{ product.maSanPham }}
              </div>
            </div>
          </div>

          <div
            class="col-span-4 text-xs text-slate-500 flex flex-col gap-0.5 md:px-4 md:border-r md:border-slate-200 h-full justify-center"
          >
            <div>
              DM:
              <span class="font-bold text-slate-700">{{
                product.idDanhMuc?.tenDanhMuc || 'N/A'
              }}</span>
            </div>
            <div>
              TH:
              <span class="font-bold text-slate-700">{{
                product.idThuongHieu?.tenThuongHieu || 'N/A'
              }}</span>
            </div>
          </div>

          <div
            class="col-span-2 flex md:justify-center items-center gap-2 md:gap-0 md:border-r md:border-slate-200 h-full"
          >
            <span
              :class="[
                'inline-flex items-center gap-1.5 px-3 py-1 text-[11px] font-black uppercase tracking-wider rounded-full border transition-all duration-300 group-hover:scale-105',
                product.trangThai
                  ? 'bg-emerald-50 text-emerald-700 border-emerald-300 group-hover:bg-emerald-500 group-hover:text-white'
                  : 'bg-rose-50 text-rose-700 border-rose-300 group-hover:bg-rose-500 group-hover:text-white',
              ]"
            >
              <span
                :class="[
                  'w-1.5 h-1.5 rounded-full',
                  product.trangThai
                    ? 'bg-emerald-500 group-hover:bg-white animate-pulse'
                    : 'bg-rose-500 group-hover:bg-white',
                ]"
              ></span>
              {{ product.trangThai ? 'Đang bán' : 'Ngừng bán' }}
            </span>
          </div>

          <div class="col-span-2 flex justify-end md:justify-center items-center pt-3 md:pt-0">
            <div
              class="inline-flex items-center bg-white border border-slate-300 rounded-xl p-1.5 shadow-sm hover:border-indigo-400 hover:shadow-md transition-all duration-300"
            >
              <button
                @click="openEditModal(product)"
                class="p-1.5 text-indigo-600 hover:bg-indigo-100 rounded-lg transition-all duration-200"
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
                @click="deleteProduct(product.id)"
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

        <div
          v-if="products.length === 0"
          class="text-center py-16 border-2 border-dashed border-slate-300 rounded-2xl bg-white shadow-sm"
        >
          <p class="text-slate-700 font-bold text-sm">Kho lưu trữ sản phẩm trống rỗng</p>
        </div>
      </div>
    </div>

    <Transition name="fade">
      <div v-if="isModalOpen" class="fixed inset-0 z-50 flex items-center justify-center p-4">
        <div class="absolute inset-0 bg-slate-900/30 backdrop-blur-[4px]" @click="closeModal"></div>
        <div
          class="bg-white border border-slate-300 rounded-2xl shadow-[0_20px_60px_-15px_rgba(0,0,0,0.12)] w-full max-w-md z-10 p-6 animate-scale-up"
        >
          <h3 class="text-xs font-black uppercase tracking-widest text-indigo-600 mb-6">
            {{ isEditMode ? '⚡ CẬP NHẬT SẢN PHẨM' : '✨ KHỞI TẠO SẢN PHẨM' }}
          </h3>

          <form @submit.prevent="handleSubmit" class="space-y-4">
            <div class="space-y-4">
              <div>
                <input
                  v-model="form.tenSanPham"
                  placeholder="Tên sản phẩm"
                  class="w-full border border-slate-300 px-4 py-2.5 rounded-xl text-sm focus:ring-2 focus:ring-indigo-500/20 outline-none transition-all"
                  required
                />
              </div>

              <div>
                <input
                  v-model="form.maSanPham"
                  placeholder="Mã sản phẩm (VD: SP01)"
                  class="w-full border border-slate-300 px-4 py-2.5 rounded-xl text-sm focus:ring-2 focus:ring-indigo-500/20 outline-none transition-all uppercase font-mono"
                  required
                />
              </div>

              <div class="grid grid-cols-2 gap-3">
                <select
                  v-model.number="form.idDanhMuc"
                  class="w-full border border-slate-300 px-4 py-2.5 rounded-xl text-sm focus:ring-2 focus:ring-indigo-500/20 outline-none transition-all text-slate-700 bg-white"
                >
                  <option :value="null">Chọn danh mục</option>
                  <option v-for="item in danhMucs" :key="item.id" :value="item.id">
                    {{ item.tenDanhMuc }}
                  </option>
                </select>

                <select
                  v-model.number="form.idThuongHieu"
                  class="w-full border border-slate-300 px-4 py-2.5 rounded-xl text-sm focus:ring-2 focus:ring-indigo-500/20 outline-none transition-all text-slate-700 bg-white"
                >
                  <option :value="null">Chọn thương hiệu</option>
                  <option v-for="item in thuongHieus" :key="item.id" :value="item.id">
                    {{ item.tenThuongHieu }}
                  </option>
                </select>
              </div>

              <div class="flex gap-3">
                <button
                  type="button"
                  @click="form.trangThai = true"
                  :class="[
                    'flex-1 py-2 text-xs font-bold rounded-xl border transition-all',
                    form.trangThai
                      ? 'bg-emerald-50 border-emerald-500 text-emerald-700'
                      : 'bg-slate-50 border-slate-200 text-slate-500',
                  ]"
                >
                  ĐANG BÁN
                </button>
                <button
                  type="button"
                  @click="form.trangThai = false"
                  :class="[
                    'flex-1 py-2 text-xs font-bold rounded-xl border transition-all',
                    !form.trangThai
                      ? 'bg-rose-50 border-rose-500 text-rose-700'
                      : 'bg-slate-50 border-slate-200 text-slate-500',
                  ]"
                >
                  NGỪNG BÁN
                </button>
              </div>
            </div>

            <div class="flex justify-end gap-2 pt-4">
              <button
                type="button"
                @click="closeModal"
                class="px-4 py-2 bg-slate-100 hover:bg-slate-200 rounded-xl text-xs font-bold transition-colors"
              >
                HỦY
              </button>
              <button
                type="submit"
                class="px-5 py-2 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl text-xs font-bold shadow-lg shadow-indigo-100 transition-all"
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
import { ref, reactive, onMounted } from 'vue'
import { getAllSanpham } from '@/service/SanphamService'
import { getAllDanhMuc } from '@/service/DanhMucService'
import { getAllThuongHieu } from '@/service/ThuongHieuService'
import { getAllChatLieu } from '@/service/ChatLieuService'

const products = ref([]),
  danhMucs = ref([]),
  thuongHieus = ref([]),
  chatLieus = ref([])
const isModalOpen = ref(false),
  isEditMode = ref(false)
const toast = reactive({ show: false, message: '', type: 'success' })
const form = ref({
  id: null,
  idDanhMuc: null,
  idThuongHieu: null,
  idChatLieu: null,
  maSanPham: '',
  tenSanPham: '',
  trangThai: true,
  image: '',
})

const showToast = (message, type = 'success') => {
  toast.message = message
  toast.type = type
  toast.show = true
  setTimeout(() => {
    toast.show = false
  }, 3000)
}

const resetForm = () => {
  form.value = {
    id: null,
    idDanhMuc: null,
    idThuongHieu: null,
    idChatLieu: null,
    maSanPham: '',
    tenSanPham: '',
    trangThai: true,
    image: '',
  }
}

const loadData = async () => {
  try {
    ;[products.value, danhMucs.value, thuongHieus.value, chatLieus.value] = await Promise.all([
      getAllSanpham(),
      getAllDanhMuc(),
      getAllThuongHieu(),
      getAllChatLieu(),
    ])
  } catch (err) {
    console.error(err)
  }
}

onMounted(loadData)

const getImageUrl = (path) => (path?.startsWith('http') ? path : `http://localhost:8080${path}`)
const openAddModal = () => {
  resetForm()
  isEditMode.value = false
  isModalOpen.value = true
}
const openEditModal = async (product) => {
  const res = await fetch(`http://localhost:8080/sanpham/detail/${product.id}`)
  const detail = await res.json()
  form.value = {
    ...detail,
    idDanhMuc: detail.idDanhMuc?.id,
    idThuongHieu: detail.idThuongHieu?.id,
    idChatLieu: detail.idChatLieu?.id,
  }
  isEditMode.value = true
  isModalOpen.value = true
}

const deleteProduct = async (id) => {
  if (!confirm('Xóa sản phẩm?')) return
  await fetch(`http://localhost:8080/sanpham/delete/${id}`, { method: 'DELETE' })
  await loadData()
  showToast('Đã xóa thành công!')
}

const closeModal = () => {
  isModalOpen.value = false
  resetForm()
}

const handleSubmit = async () => {
  if (!form.value.tenSanPham?.trim() || !form.value.maSanPham?.trim())
    return showToast('Vui lòng điền tên & mã SP', 'error')
  try {
    const url = isEditMode.value
      ? `http://localhost:8080/sanpham/update/${form.value.id}`
      : `http://localhost:8080/sanpham`
    const res = await fetch(url, {
      method: isEditMode.value ? 'PUT' : 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form.value),
    })
    if (!res.ok) throw new Error()
    await loadData()
    closeModal()
    showToast(isEditMode.value ? 'Cập nhật thành công!' : 'Thêm mới thành công!')
  } catch {
    showToast('Có lỗi xảy ra!', 'error')
  }
}
</script>

<style scoped>
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
.animate-scale-up {
  animation: scaleUp 0.3s cubic-bezier(0.34, 1.56, 0.64, 1) forwards;
}
.animate-shimmer {
  animation: shimmer 1.5s infinite;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
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
</style>
