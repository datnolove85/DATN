<template>
  <div
    class="space-y-6 max-w-full mx-auto p-4 animate-fade-in bg-slate-50 text-slate-800 rounded-2xl selection:bg-indigo-100 selection:text-indigo-900 overflow-hidden"
  >
    <div
      class="relative p-6 bg-white/80 backdrop-blur-md rounded-2xl border border-slate-300 shadow-[0_8px_30px_rgb(0,0,0,0.02)] flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 overflow-hidden group transition-all duration-300 hover:shadow-[0_8px_30px_rgba(99,102,241,0.05)]"
    >
      <div
        class="absolute inset-0 bg-[radial-gradient(circle_at_30%_30%,rgba(99,102,241,0.06),transparent_50%)] transition-opacity duration-500 group-hover:opacity-100"
      ></div>
      <div class="relative z-10">
        <h1
          class="text-2xl font-black tracking-wider bg-gradient-to-r from-slate-950 via-indigo-900 to-indigo-950 bg-clip-text text-transparent uppercase group-hover:tracking-widest transition-all duration-500 easy-in-out"
        >
          Quản lý chất liệu
        </h1>
        <p class="text-xs text-indigo-600/90 mt-1 flex items-center gap-2 font-semibold">
          <span class="relative flex h-2 w-2">
            <span
              class="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75"
            ></span>
            <span class="relative inline-block rounded-full h-2 w-2 bg-emerald-500"></span>
          </span>
          Hệ thống cơ sở dữ liệu thời gian thực
        </p>
      </div>

      <button
        @click="openAddModal"
        class="relative z-10 flex items-center gap-2 px-5 py-2.5 bg-gradient-to-r from-indigo-600 to-violet-600 hover:from-indigo-500 hover:to-violet-500 text-white rounded-xl text-xs font-bold shadow-[0_4px_12px_rgba(99,102,241,0.2)] hover:shadow-[0_6px_20px_rgba(99,102,241,0.4)] transition-all duration-300 active:scale-95 active:duration-75 group/btn overflow-hidden"
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
        THÊM CHẤT LIỆU MỚI
      </button>
    </div>

    <div class="space-y-3 overflow-hidden">
      <div
        class="hidden md:grid grid-cols-12 gap-0 px-6 py-3 bg-white border border-b-2 border-slate-300 rounded-t-xl text-[11px] uppercase text-indigo-950 font-black tracking-widest select-none shadow-sm"
      >
        <div class="col-span-1 text-center border-r border-slate-200">ID</div>
        <div class="col-span-2 px-4 border-r border-slate-200">Mã chất liệu</div>
        <div class="col-span-3 px-4 border-r border-slate-200">Tên chất liệu</div>
        <div class="col-span-2 text-center border-r border-slate-200">Trạng thái</div>
        <div class="col-span-3 px-4 border-r border-slate-200">Dấu mốc thời gian</div>
        <div class="col-span-1 text-center">Tùy chọn</div>
      </div>

      <div class="space-y-2">
        <div
          v-for="(material, index) in materials"
          :key="material.id ?? index"
          class="grid grid-cols-1 md:grid-cols-12 gap-4 md:gap-0 px-6 py-4 md:py-3 items-center bg-white border border-slate-300 hover:border-indigo-500 hover:bg-slate-50/80 rounded-xl md:rounded-lg transition-all duration-300 ease-in-out shadow-[0_2px_4px_rgba(0,0,0,0.01)] hover:shadow-[0_10px_25px_-5px_rgba(99,102,241,0.12)] group overflow-hidden"
        >
          <div
            class="col-span-1 flex md:justify-center items-center gap-2 md:gap-0 md:border-r md:border-slate-200 h-full transition-colors duration-300 group-hover:border-indigo-200"
          >
            <span class="md:hidden text-[10px] uppercase font-bold text-slate-400">ID:</span>
            <span
              class="px-2 py-0.5 bg-slate-100 border border-slate-300 text-indigo-700 font-mono text-xs rounded font-bold transition-all duration-300 group-hover:bg-indigo-600 group-hover:text-white group-hover:border-indigo-600"
            >
              #{{ material.id }}
            </span>
          </div>

          <div
            class="col-span-2 flex items-center gap-2 md:gap-0 md:px-4 md:border-r md:border-slate-200 h-full transition-colors duration-300 group-hover:border-indigo-200"
          >
            <span class="md:hidden text-[10px] uppercase font-bold text-slate-400">Mã:</span>
            <span
              class="inline-block px-2.5 py-1 bg-slate-50 text-indigo-800 font-black rounded border border-slate-300 text-xs font-mono tracking-wider transition-all duration-300 group-hover:bg-white group-hover:border-indigo-400 group-hover:text-indigo-600"
            >
              {{ material.maChatLieu }}
            </span>
          </div>

          <div
            class="col-span-3 text-sm font-bold text-slate-800 group-hover:text-indigo-600 transform transition-all duration-300 ease-out capitalize truncate md:px-4 md:border-r md:border-slate-200 h-full flex items-center group-hover:translate-x-1.5"
          >
            {{ material.tenChatLieu }}
          </div>

          <div
            class="col-span-2 flex md:justify-center items-center gap-2 md:gap-0 md:border-r md:border-slate-200 h-full transition-colors duration-300 group-hover:border-indigo-200"
          >
            <span class="md:hidden text-[10px] uppercase font-bold text-slate-400"
              >Trạng thái:</span
            >
            <span
              :class="[
                'inline-flex items-center gap-1.5 px-3 py-1 text-[11px] font-black uppercase tracking-wider rounded-full border transition-all duration-300 group-hover:scale-105',
                material.trangThai
                  ? 'bg-emerald-50 text-emerald-700 border-emerald-300 group-hover:bg-emerald-500 group-hover:text-white group-hover:border-emerald-500'
                  : 'bg-rose-50 text-rose-700 border-rose-300 group-hover:bg-rose-500 group-hover:text-white group-hover:border-rose-500',
              ]"
            >
              <span
                :class="[
                  'w-1.5 h-1.5 rounded-full transition-all duration-300',
                  material.trangThai
                    ? 'bg-emerald-500 group-hover:bg-white animate-pulse'
                    : 'bg-rose-500 group-hover:bg-white',
                ]"
              ></span>
              {{ material.trangThai ? 'Active' : 'Muted' }}
            </span>
          </div>

          <div
            class="col-span-3 text-xs text-slate-600 flex flex-col gap-1 border-l-2 border-slate-300 pl-3 md:pl-4 md:border-l-0 md:border-r md:border-slate-200 h-full justify-center transition-colors duration-300 group-hover:border-indigo-200"
          >
            <div class="flex items-center gap-1.5">
              <span
                class="text-[10px] text-slate-400 font-mono uppercase font-bold group-hover:text-indigo-400 transition-colors"
                >In:</span
              >
              <span
                class="font-medium text-slate-700 font-mono group-hover:text-slate-900 transition-colors"
                >{{ formatDate(material.ngayTao) }}</span
              >
            </div>
            <div class="flex items-center gap-1.5">
              <span
                class="text-[10px] text-slate-400 font-mono uppercase font-bold group-hover:text-indigo-400 transition-colors"
                >Up:</span
              >
              <span
                class="font-medium text-slate-600 font-mono italic group-hover:text-slate-900 transition-colors"
                >{{ formatDate(material.ngayCapNhat) }}</span
              >
            </div>
          </div>

          <div
            class="col-span-1 flex justify-end md:justify-center items-center border-t border-slate-200 md:border-none pt-3 md:pt-0 h-full"
          >
            <div
              class="inline-flex items-center bg-slate-50 border border-slate-300 rounded-lg p-0.5 shadow-inner group-hover:bg-white transition-colors duration-300"
            >
              <button
                @click="openEditModal(material)"
                class="p-1.5 text-slate-500 hover:text-indigo-600 hover:bg-slate-100 rounded-md transition duration-150 active:scale-90"
                title="Chỉnh sửa chất liệu"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke-width="2.5"
                  stroke="currentColor"
                  class="w-4 h-4"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10"
                  />
                </svg>
              </button>

              <div class="w-px h-3 bg-slate-300 mx-0.5"></div>

              <button
                @click="deleteMaterial(material.id)"
                class="p-1.5 text-slate-500 hover:text-rose-600 hover:bg-slate-100 rounded-md transition duration-150 active:scale-90"
                title="Xóa chất liệu"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke-width="2.5"
                  stroke="currentColor"
                  class="w-4 h-4"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    d="m14.74 9-.34 9m-4.72 0L9 9m4.72-4c.03-.66-.45-1.27-1.14-1.27H10.53c-.69 0-1.17.58-1.14 1.27l.38 3.36h4.72l.38-3.36ZM19.5 7.125A18.25 18.25 0 0 1 11.25 18H5.25A18.25 18.25 0 0 1 3 7.125h16.5Z"
                  />
                </svg>
              </button>
            </div>
          </div>
        </div>

        <div
          v-if="materials.length === 0"
          class="text-center py-16 border-2 border-dashed border-slate-300 rounded-2xl bg-white shadow-sm"
        >
          <div
            class="inline-flex p-3 bg-slate-50 rounded-full border border-slate-300 text-indigo-500/80 mb-3"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="1.5"
              stroke="currentColor"
              class="w-6 h-6"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="M20.25 7.5l-.625 10.632a2.25 2.25 0 01-2.247 2.118H6.622a2.25 2.25 0 01-2.247-2.118L3.75 7.5M10 11.25h4M3.375 7.5h17.25c.621 0 1.125-.504 1.125-1.125v-1.5c0-.621-.504-1.125-1.125-1.125H3.375c-.621 0-1.125.504-1.125 1.125v1.5c0 .621.504 1.125 1.125 1.125z"
              />
            </svg>
          </div>
          <p class="text-slate-700 font-bold text-sm tracking-wide">
            Kho lưu trữ chất liệu trống rỗng
          </p>
          <p class="text-xs text-slate-400 mt-0.5">
            Vui lòng khởi tạo thông tin chất liệu sản phẩm mới.
          </p>
        </div>
      </div>
    </div>

    <div v-if="isModalOpen" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div
        class="absolute inset-0 bg-slate-900/30 backdrop-blur-[4px] animate-fade-overlay"
        @click="closeModal"
      ></div>

      <div
        class="bg-white border border-slate-300 rounded-2xl shadow-[0_20px_60px_-15px_rgba(0,0,0,0.12)] w-full max-w-md z-10 overflow-hidden animate-scale-up"
      >
        <div
          class="px-6 py-4 bg-slate-50 border-b border-slate-200 text-slate-800 flex justify-between items-center"
        >
          <h3 class="text-xs font-black uppercase tracking-widest text-indigo-600">
            {{ isEditMode ? '⚡ CẬP NHẬT CHẤT LIỆU' : '✨ KHỞI TẠO CHẤT LIỆU' }}
          </h3>
          <button
            @click="closeModal"
            class="w-6 h-6 flex items-center justify-center rounded-full bg-white text-slate-400 hover:text-slate-700 hover:bg-slate-100 border border-slate-300 transition duration-150 text-xs active:scale-90"
          >
            ✕
          </button>
        </div>

        <form @submit.prevent="handleSubmit" class="p-6 space-y-5">
          <div class="space-y-1.5">
            <label class="text-[10px] font-black uppercase text-indigo-600/80 tracking-widest"
              >Mã chất liệu</label
            >
            <input
              v-model="form.maChatLieu"
              class="w-full bg-white border border-slate-300 focus:border-indigo-500 px-4 py-2.5 rounded-xl text-sm text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/10 font-mono uppercase tracking-wider transition-all duration-200 shadow-sm"
              placeholder="VD: CT, PL, KAK, SILK..."
              required
            />
          </div>

          <div class="space-y-1.5">
            <label class="text-[10px] font-black uppercase text-indigo-600/80 tracking-widest"
              >Tên chất liệu</label
            >
            <input
              v-model="form.tenChatLieu"
              class="w-full bg-white border border-slate-300 focus:border-indigo-500 px-4 py-2.5 rounded-xl text-sm text-slate-800 focus:outline-none focus:ring-2 focus:ring-indigo-500/10 transition-all duration-200 shadow-sm"
              placeholder="Nhập tên cụ thể (VD: Cotton, Polyester...)"
              required
            />
          </div>

          <div class="space-y-2">
            <label class="text-[10px] font-black uppercase text-indigo-600/80 tracking-widest"
              >Phân loại Trạng thái</label
            >
            <div class="grid grid-cols-2 gap-3">
              <label
                :class="[
                  'flex items-center justify-center gap-2 p-3 rounded-xl border text-xs font-bold cursor-pointer transition-all duration-200 select-none active:scale-95',
                  form.trangThai
                    ? 'bg-emerald-50 border-emerald-500 text-emerald-700 shadow-[0_4px_12px_rgba(16,185,129,0.12)] font-black'
                    : 'bg-white border-slate-300 text-slate-400 hover:border-slate-400',
                ]"
              >
                <input type="radio" :value="true" v-model="form.trangThai" class="hidden" />
                <span>HOẠT ĐỘNG</span>
              </label>

              <label
                :class="[
                  'flex items-center justify-center gap-2 p-3 rounded-xl border text-xs font-bold cursor-pointer transition-all duration-200 select-none active:scale-95',
                  !form.trangThai
                    ? 'bg-rose-50 border-rose-500 text-rose-700 shadow-[0_4px_12px_rgba(244,63,94,0.12)] font-black'
                    : 'bg-white border-slate-300 text-slate-400 hover:border-slate-400',
                ]"
              >
                <input type="radio" :value="false" v-model="form.trangThai" class="hidden" />
                <span>TẠM NGỪNG</span>
              </label>
            </div>
          </div>

          <div class="flex justify-end gap-2.5 pt-4 border-t border-slate-200">
            <button
              type="button"
              @click="closeModal"
              class="px-4 py-2 bg-slate-100 hover:bg-slate-200 text-slate-600 rounded-xl text-xs font-bold transition duration-150 active:scale-95"
            >
              HỦY BỎ
            </button>
            <button
              type="submit"
              class="px-5 py-2 bg-gradient-to-r from-indigo-600 to-indigo-700 hover:from-indigo-500 hover:to-indigo-600 text-white rounded-xl text-xs font-bold shadow-md hover:shadow-lg transition duration-150 active:scale-95"
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
  getAllChatLieu,
  createChatLieu,
  updateChatLieu,
  deleteChatLieu,
} from '@/service/ChatLieuService'

// ================= DATA =================
const materials = ref([])
const isModalOpen = ref(false)
const isEditMode = ref(false)

const form = ref({
  id: null,
  maChatLieu: '',
  tenChatLieu: '',
  trangThai: true,
})

// ================= LOAD DATA =================
const loadData = async () => {
  try {
    materials.value = await getAllChatLieu()
  } catch (err) {
    console.error('Lỗi khi tải danh sách chất liệu:', err)
  }
}

onMounted(loadData)

// ================= FORMAT DATE =================
const formatDate = (date) => {
  if (!date) return '---'
  const d = new Date(date)
  return `${d.toLocaleDateString('vi-VN')} - ${d.toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' })}`
}

// ================= MODAL =================
const openAddModal = () => {
  isEditMode.value = false
  isModalOpen.value = true
  form.value = {
    id: null,
    maChatLieu: '',
    tenChatLieu: '',
    trangThai: true,
  }
}

const openEditModal = (material) => {
  isEditMode.value = true
  isModalOpen.value = true
  form.value = { ...material }
}

const closeModal = () => {
  isModalOpen.value = false
}

// ================= CRUD =================
const handleSubmit = async () => {
  try {
    if (isEditMode.value) {
      await updateChatLieu(form.value.id, form.value)
    } else {
      await createChatLieu(form.value)
    }

    await loadData()
    closeModal()
  } catch (err) {
    console.error('Lỗi lưu chất liệu:', err)
  }
}

const deleteMaterial = async (id) => {
  if (confirm('Bạn có chắc chắn muốn xóa chất liệu này khỏi hệ thống?')) {
    try {
      await deleteChatLieu(id)
      await loadData()
    } catch (err) {
      console.error('Lỗi xóa:', err)
    }
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

@keyframes fadeInOverlay {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes scaleUp {
  from {
    opacity: 0;
    transform: scale(0.96) translateY(4px);
  }
  to {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
}

@keyframes shimmer {
  100% {
    transform: translateX(100%);
  }
}

.animate-fade-in {
  animation: fadeIn 0.35s cubic-bezier(0.16, 1, 0.3, 1) forwards;
}

.animate-fade-overlay {
  animation: fadeInOverlay 0.2s linear forwards;
}

.animate-scale-up {
  animation: scaleUp 0.3s cubic-bezier(0.34, 1.56, 0.64, 1) forwards;
}

.animate-shimmer {
  animation: shimmer 1.5s infinite;
}
</style>
