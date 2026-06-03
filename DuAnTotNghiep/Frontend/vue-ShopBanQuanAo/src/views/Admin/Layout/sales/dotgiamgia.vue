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
          Đợt giảm giá
        </h1>

        <p class="text-xs text-blue-100 mt-1 flex items-center gap-2 font-semibold">
          <span class="relative flex h-2 w-2">
            <span
              class="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-300 opacity-75"
            ></span>
            <span class="relative inline-block rounded-full h-2 w-2 bg-emerald-400"></span>
          </span>
          Quản lý chiến dịch khuyến mãi thời gian thực
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

        TẠO ĐỢT GIẢM GIÁ
      </button>
    </div>

    <div
      class="flex flex-wrap items-center gap-4 bg-white p-3 rounded-2xl border border-slate-300 shadow-sm"
    >
      <input
        v-model="searchQuery"
        type="text"
        placeholder="Tìm kiếm chiến dịch..."
        class="flex-grow px-4 py-2.5 bg-slate-50 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-indigo-500/20 outline-none transition-all"
      />
      <select
        v-model="statusFilter"
        class="px-4 py-2.5 bg-slate-50 border border-slate-300 rounded-xl text-xs font-bold text-indigo-950 outline-none cursor-pointer tracking-wider"
      >
        <option value="all">TẤT CẢ TRẠNG THÁI</option>
        <option value="active">ACTIVE</option>
        <option value="inactive">OFF</option>
      </select>
    </div>

    <div class="space-y-3 overflow-hidden">
      <div
        class="hidden md:grid grid-cols-12 gap-0 px-6 py-3 bg-white border border-b-2 border-slate-300 rounded-t-xl text-[11px] uppercase text-indigo-950 font-black tracking-widest select-none shadow-sm"
      >
        <div class="col-span-3 px-4 border-r border-slate-200">Chương trình</div>
        <div class="col-span-2 text-center border-r border-slate-200">Loại</div>
        <div class="col-span-2 text-center border-r border-slate-200">Giá trị giảm</div>
        <div class="col-span-2 text-center border-r border-slate-200">Trạng thái</div>
        <div class="col-span-2 px-4 border-r border-slate-200">Thời gian chiến dịch</div>
        <div class="col-span-1 text-center">Tùy chọn</div>
      </div>

      <div class="space-y-2">
        <div
          v-for="item in filteredList"
          :key="item.id"
          class="grid grid-cols-1 md:grid-cols-12 gap-4 md:gap-0 px-6 py-4 md:py-3 items-center bg-white border border-slate-300 hover:border-indigo-500 hover:bg-slate-50/80 rounded-xl md:rounded-lg transition-all duration-300 ease-in-out shadow-[0_2px_4px_rgba(0,0,0,0.01)] hover:shadow-[0_10px_25px_-5px_rgba(99,102,241,0.12)] group"
        >
          <div
            class="col-span-3 flex flex-col justify-center md:px-4 md:border-r md:border-slate-200 h-full group-hover:translate-x-1.5 transition-transform"
          >
            <div class="text-sm font-bold text-slate-800 capitalize truncate">
              {{ item.tenDotGiamGia }}
            </div>
            <div class="mt-0.5">
              <span
                class="inline-block px-2.5 py-1 bg-slate-50 text-indigo-800 font-black rounded border border-slate-300 text-xs font-mono tracking-wider group-hover:bg-white group-hover:border-indigo-400 transition-all"
              >
                {{ item.maDotGiamGia }}
              </span>
            </div>
          </div>

          <div
            class="col-span-2 flex md:justify-center items-center md:px-4 md:border-r md:border-slate-200 h-full text-sm font-bold text-slate-600"
          >
            <span class="md:hidden text-[10px] uppercase font-bold text-slate-400 mr-2">Loại:</span>
            {{ item.loaiGiamGia }}
          </div>

          <div
            class="col-span-2 flex md:justify-center items-center md:px-4 md:border-r md:border-slate-200 h-full font-mono text-xs font-bold text-indigo-700"
          >
            <span class="md:hidden text-[10px] uppercase font-bold text-slate-400 mr-2"
              >Giá trị:</span
            >
            {{ formatCurrency(item.giaTriGiam) }}
          </div>

          <div
            class="col-span-2 flex md:justify-center items-center gap-2 md:gap-0 md:border-r md:border-slate-200 h-full"
          >
            <span
              :class="[
                'inline-flex items-center gap-1.5 px-3 py-1 text-[11px] font-black uppercase tracking-wider rounded-full border transition-all duration-300 group-hover:scale-105',
                item.trangThai
                  ? 'bg-emerald-50 text-emerald-700 border-emerald-300 group-hover:bg-emerald-500 group-hover:text-white'
                  : 'bg-rose-50 text-rose-700 border-rose-300 group-hover:bg-rose-500 group-hover:text-white',
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
              {{ item.trangThai ? 'Active' : 'Off' }}
            </span>
          </div>

          <div
            class="col-span-2 text-[10px] text-slate-500 flex flex-col gap-0.5 md:px-4 md:border-r md:border-slate-200 h-full justify-center font-mono"
          >
            <div>BD: {{ formatDate(item.ngayBatDau) }}</div>
            <div>KT: {{ formatDate(item.ngayKetThuc) }}</div>
          </div>

          <div class="col-span-1 flex justify-end md:justify-center items-center pt-3 md:pt-0">
            <div
              class="inline-flex items-center bg-white border border-slate-300 rounded-xl p-1.5 shadow-sm hover:border-indigo-400 hover:shadow-md transition-all duration-300"
            >
              <button
                @click="openEditModal(item)"
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

        <div
          v-if="filteredList.length === 0"
          class="text-center py-16 border-2 border-dashed border-slate-300 rounded-2xl bg-white shadow-sm"
        >
          <p class="text-slate-700 font-bold text-sm">Kho lưu trữ chiến dịch trống rỗng</p>
        </div>
      </div>
    </div>

    <div v-if="isModalOpen" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div
        class="absolute inset-0 bg-slate-900/30 backdrop-blur-[4px]"
        @click="isModalOpen = false"
      ></div>
      <div
        class="bg-white border border-slate-300 rounded-2xl shadow-[0_20px_60px_-15px_rgba(0,0,0,0.12)] w-full max-w-md z-10 p-6 animate-scale-up"
      >
        <h3 class="text-xs font-black uppercase tracking-widest text-indigo-600 mb-6">
          {{ isEdit ? '⚡ CẬP NHẬT ĐỢT GIẢM GIÁ' : '✨ KHỞI TẠO ĐỢT GIẢM GIÁ' }}
        </h3>
        <form @submit.prevent="save" class="space-y-4">
          <input
            v-model="form.maDotGiamGia"
            placeholder="Mã giảm giá (VD: SUMMER2026)"
            class="w-full border border-slate-300 px-4 py-2.5 rounded-xl text-sm focus:ring-2 focus:ring-indigo-500/20 outline-none transition-all uppercase font-mono font-bold"
            required
          />
          <input
            v-model="form.tenDotGiamGia"
            placeholder="Tên chương trình"
            class="w-full border border-slate-300 px-4 py-2.5 rounded-xl text-sm focus:ring-2 focus:ring-indigo-500/20 outline-none transition-all font-semibold"
            required
          />
          <div class="grid grid-cols-2 gap-4">
            <input
              type="number"
              v-model="form.giaTriGiam"
              placeholder="Giá trị giảm"
              class="w-full border border-slate-300 px-4 py-2.5 rounded-xl text-sm focus:ring-2 focus:ring-indigo-500/20 outline-none transition-all font-mono"
            />
            <input
              type="datetime-local"
              v-model="form.ngayBatDau"
              class="w-full border border-slate-300 px-4 py-2.5 rounded-xl text-sm focus:ring-2 focus:ring-indigo-500/20 outline-none transition-all text-slate-600 bg-white"
            />
          </div>

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
              KÍCH HOẠT (ACTIVE)
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
              TẠM NGỪNG (OFF)
            </button>
          </div>

          <div class="flex justify-end gap-2 pt-4">
            <button
              type="button"
              @click="isModalOpen = false"
              class="px-4 py-2 bg-slate-100 rounded-xl text-xs font-bold text-slate-600"
            >
              HỦY KHÔNG LƯU
            </button>
            <button
              type="submit"
              class="px-5 py-2 bg-indigo-600 text-white rounded-xl text-xs font-bold"
            >
              XÁC NHẬN LƯU
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import {
  getAllDotGiamGia,
  createDotGiamGia,
  updateDotGiamGia,
  deleteDotGiamGia,
} from '@/service/DotGiamGiaService'

const list = ref([])
const isModalOpen = ref(false)
const isEdit = ref(false)
const form = ref({})
const currentId = ref(null)
const searchQuery = ref('')
const statusFilter = ref('all')

const filteredList = computed(() => {
  return list.value.filter((item) => {
    const matchesSearch =
      item.tenDotGiamGia.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      item.maDotGiamGia.toLowerCase().includes(searchQuery.value.toLowerCase())
    const matchesStatus =
      statusFilter.value === 'all' ||
      (statusFilter.value === 'active' ? item.trangThai : !item.trangThai)
    return matchesSearch && matchesStatus
  })
})

const loadData = async () => {
  list.value = await getAllDotGiamGia()
}
const formatCurrency = (val) =>
  new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(val)
const formatDate = (date) =>
  new Date(date).toLocaleString('vi-VN', { dateStyle: 'short', timeStyle: 'short' })

const openAddModal = () => {
  isEdit.value = false
  form.value = { maDotGiamGia: '', tenDotGiamGia: '', trangThai: true }
  isModalOpen.value = true
}

const openEditModal = (item) => {
  isEdit.value = true
  currentId.value = item.id
  form.value = {
    ...item,
    ngayBatDau: item.ngayBatDau?.slice(0, 16),
    ngayKetThuc: item.ngayKetThuc?.slice(0, 16),
  }
  isModalOpen.value = true
}

const save = async () => {
  isEdit.value
    ? await updateDotGiamGia(currentId.value, form.value)
    : await createDotGiamGia(form.value)
  isModalOpen.value = false
  await loadData()
}

const deleteItem = async (id) => {
  if (confirm('Xác nhận xóa?')) {
    await deleteDotGiamGia(id)
    await loadData()
  }
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
