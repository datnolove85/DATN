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
          Quản lý Danh mục
        </h1>

        <p class="text-xs text-blue-100 mt-1 flex items-center gap-2 font-semibold">
          <span class="relative flex h-2 w-2">
            <span
              class="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-300 opacity-75"
            ></span>
            <span class="relative inline-block rounded-full h-2 w-2 bg-emerald-400"></span>
          </span>
          Hệ thống cấu trúc dữ liệu phân tầng
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

        TẠO DANH MỤC MỚI
      </button>
    </div>

    <div class="space-y-3 overflow-hidden">
      <div
        class="hidden md:grid grid-cols-12 gap-0 px-6 py-3 bg-white border border-b-2 border-slate-300 rounded-t-xl text-[11px] uppercase text-indigo-950 font-black tracking-widest select-none shadow-sm"
      >
        <div class="col-span-1 text-center border-r border-slate-200">ID</div>
        <div class="col-span-4 px-4 border-r border-slate-200">Tên danh mục</div>
        <div class="col-span-3 px-4 border-r border-slate-200">Mã danh mục</div>
        <div class="col-span-2 text-center border-r border-slate-200">Trạng thái</div>
        <div class="col-span-2 text-center">Tùy chọn</div>
      </div>

      <div class="space-y-2">
        <div
          v-for="cat in categories"
          :key="cat.id"
          class="grid grid-cols-1 md:grid-cols-12 gap-4 md:gap-0 px-6 py-4 md:py-3 items-center bg-white border border-slate-300 hover:border-indigo-500 hover:bg-slate-50/80 rounded-xl md:rounded-lg transition-all duration-300 ease-in-out shadow-[0_2px_4px_rgba(0,0,0,0.01)] hover:shadow-[0_10px_25px_-5px_rgba(99,102,241,0.12)] group"
        >
          <div
            class="col-span-1 flex md:justify-center items-center gap-2 md:gap-0 md:border-r md:border-slate-200 h-full"
          >
            <span class="md:hidden text-[10px] uppercase font-bold text-slate-400">ID:</span>
            <span
              class="px-2 py-0.5 bg-slate-100 border border-slate-300 text-indigo-700 font-mono text-xs rounded font-bold group-hover:bg-indigo-600 group-hover:text-white group-hover:border-indigo-600 transition-all"
            >
              #{{ cat.id }}
            </span>
          </div>

          <div
            class="col-span-4 text-sm font-bold text-slate-800 capitalize truncate md:px-4 md:border-r md:border-slate-200 h-full flex items-center group-hover:translate-x-1.5 transition-transform"
          >
            {{ cat.tenDanhMuc }}
          </div>

          <div class="col-span-3 md:px-4 md:border-r md:border-slate-200 h-full flex items-center">
            <span
              class="inline-block px-2.5 py-1 bg-slate-50 text-indigo-800 font-black rounded border border-slate-300 text-xs font-mono tracking-wider group-hover:bg-white group-hover:border-indigo-400 transition-all"
            >
              {{ cat.maDanhMuc }}
            </span>
          </div>

          <div
            class="col-span-2 flex md:justify-center items-center gap-2 md:gap-0 md:border-r md:border-slate-200 h-full"
          >
            <span
              :class="[
                'inline-flex items-center gap-1.5 px-3 py-1 text-[11px] font-black uppercase tracking-wider rounded-full border transition-all duration-300 group-hover:scale-105',
                cat.trangThai
                  ? 'bg-emerald-50 text-emerald-700 border-emerald-300 group-hover:bg-emerald-500 group-hover:text-white'
                  : 'bg-rose-50 text-rose-700 border-rose-300 group-hover:bg-rose-500 group-hover:text-white',
              ]"
            >
              <span
                :class="[
                  'w-1.5 h-1.5 rounded-full',
                  cat.trangThai
                    ? 'bg-emerald-500 group-hover:bg-white animate-pulse'
                    : 'bg-rose-500 group-hover:bg-white',
                ]"
              ></span>
              {{ cat.trangThai ? 'Hoạt động' : 'Ngừng' }}
            </span>
          </div>

          <div class="col-span-2 flex justify-end md:justify-center items-center pt-3 md:pt-0">
            <div
              class="inline-flex items-center bg-white border border-slate-300 rounded-xl p-1.5 shadow-sm hover:border-indigo-400 hover:shadow-md transition-all duration-300"
            >
              <button
                @click="editCategory(cat)"
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
                @click="removeCategory(cat.id)"
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
          v-if="categories.length === 0"
          class="text-center py-16 border-2 border-dashed border-slate-300 rounded-2xl bg-white shadow-sm"
        >
          <p class="text-slate-700 font-bold text-sm">Chưa có danh mục nào</p>
        </div>
      </div>
    </div>

    <div v-if="isModalOpen" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-slate-900/30 backdrop-blur-[4px]" @click="closeModal"></div>
      <div
        class="bg-white border border-slate-300 rounded-2xl shadow-[0_20px_60px_-15px_rgba(0,0,0,0.12)] w-full max-w-md z-10 p-6 animate-scale-up"
      >
        <h3 class="text-xs font-black uppercase tracking-widest text-indigo-600 mb-6">
          {{ isEditMode ? '⚡ CẬP NHẬT DANH MỤC' : '✨ KHỞI TẠO DANH MỤC' }}
        </h3>
        <form @submit.prevent="handleSubmit" class="space-y-4">
          <input
            v-model="form.maDanhMuc"
            placeholder="Mã danh mục (VD: CAT01)"
            class="w-full border border-slate-300 px-4 py-2.5 rounded-xl text-sm focus:ring-2 focus:ring-indigo-500/20 outline-none transition-all"
            required
          />
          <input
            v-model="form.tenDanhMuc"
            placeholder="Tên danh mục"
            class="w-full border border-slate-300 px-4 py-2.5 rounded-xl text-sm focus:ring-2 focus:ring-indigo-500/20 outline-none transition-all"
            required
          />
          <div class="flex gap-3">
            <button
              type="button"
              @click="form.trangThai = true"
              :class="[
                'flex-1 py-2 text-xs font-bold rounded-xl border',
                form.trangThai
                  ? 'bg-emerald-50 border-emerald-500 text-emerald-700'
                  : 'bg-slate-50 border-slate-200 text-slate-400',
              ]"
            >
              HOẠT ĐỘNG
            </button>
            <button
              type="button"
              @click="form.trangThai = false"
              :class="[
                'flex-1 py-2 text-xs font-bold rounded-xl border',
                !form.trangThai
                  ? 'bg-rose-50 border-rose-500 text-rose-700'
                  : 'bg-slate-50 border-slate-200 text-slate-400',
              ]"
            >
              TẠM NGỪNG
            </button>
          </div>
          <div class="flex justify-end gap-2 pt-4">
            <button
              type="button"
              @click="closeModal"
              class="px-4 py-2 bg-slate-100 rounded-xl text-xs font-bold text-slate-600"
            >
              HỦY
            </button>
            <button
              type="submit"
              class="px-5 py-2 bg-indigo-600 text-white rounded-xl text-xs font-bold"
            >
              LƯU DỮ LIỆU
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import {
  getAllDanhMuc,
  createDanhMuc,
  updateDanhMuc,
  deleteDanhMuc,
} from '@/service/DanhMucService'

const categories = ref<any[]>([])
const isModalOpen = ref(false)
const isEditMode = ref(false)
const form = ref({ id: null, maDanhMuc: '', tenDanhMuc: '', trangThai: true })

const loadData = async () => {
  categories.value = await getAllDanhMuc()
}

const openAddModal = () => {
  isEditMode.value = false
  form.value = { id: null, maDanhMuc: '', tenDanhMuc: '', trangThai: true }
  isModalOpen.value = true
}

const editCategory = (cat: any) => {
  isEditMode.value = true
  form.value = { ...cat }
  isModalOpen.value = true
}

const closeModal = () => (isModalOpen.value = false)

const handleSubmit = async () => {
  try {
    if (isEditMode.value) await updateDanhMuc(form.value.id, form.value)
    else await createDanhMuc(form.value)
    await loadData()
    closeModal()
  } catch (err) {
    console.error(err)
  }
}

const removeCategory = async (id: number) => {
  if (!confirm('Xóa danh mục này?')) return
  await deleteDanhMuc(id)
  await loadData()
}

onMounted(loadData)
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
</style>
