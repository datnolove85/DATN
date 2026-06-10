<template>
  <div
    class="space-y-6 max-w-full mx-auto p-4 animate-fade-in bg-slate-50 text-slate-800 rounded-2xl selection:bg-indigo-100 selection:text-indigo-900 overflow-hidden"
  >
    <div
      class="relative p-8 bg-[#0b0f19] rounded-3xl text-white flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 shadow-sm overflow-hidden"
    >
      <div>
        <span class="text-[10px] tracking-widest uppercase font-bold text-indigo-400"
          >Hệ thống quản trị thế hệ mới</span
        >
        <h1 class="text-2xl font-bold tracking-tight mt-1">Quản Lý Chất Liệu Chi Tiết</h1>
        <p class="text-xs text-slate-400 mt-1 flex items-center gap-2">
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
        class="flex items-center gap-2 px-5 py-3 bg-indigo-600 hover:bg-indigo-500 text-white rounded-2xl text-xs font-bold shadow-lg shadow-indigo-600/20 transition-all active:scale-95 whitespace-nowrap"
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
        + THÊM CHẤT LIỆU MỚI
      </button>
    </div>

    <div class="space-y-3 overflow-hidden">
      <div
        class="hidden md:grid grid-cols-12 gap-0 px-6 py-3 bg-white border border-slate-100 rounded-t-xl text-[11px] uppercase text-slate-400 font-bold tracking-wider select-none shadow-sm"
      >
        <div class="col-span-1 text-center border-r border-slate-100">ID</div>
        <div class="col-span-2 px-4 border-r border-slate-100">Mã chất liệu</div>
        <div class="col-span-3 px-4 border-r border-slate-100">Tên chất liệu</div>
        <div class="col-span-2 text-center border-r border-slate-100">Trạng thái</div>
        <div class="col-span-3 px-4 border-r border-slate-100">Thời gian</div>
        <div class="col-span-1 text-center">Tùy chọn</div>
      </div>

      <div class="space-y-2">
        <div
          v-for="(material, index) in materials"
          :key="material.id ?? index"
          class="grid grid-cols-1 md:grid-cols-12 gap-4 md:gap-0 px-6 py-4 md:py-3 items-center bg-white border border-slate-100 hover:border-indigo-500 hover:bg-slate-50/80 rounded-xl md:rounded-lg transition-all duration-300 ease-in-out shadow-sm group"
        >
          <div
            class="col-span-1 flex md:justify-center items-center gap-2 md:gap-0 md:border-r md:border-slate-100 h-full"
          >
            <span class="md:hidden text-[10px] uppercase font-bold text-slate-400">ID:</span>
            <span
              class="px-2 py-0.5 bg-indigo-50 border border-indigo-100 text-indigo-600 font-mono text-[10px] rounded-xl font-bold transition-all"
            >
              #{{ material.id }}
            </span>
          </div>

          <div class="col-span-2 md:px-4 md:border-r md:border-slate-100 h-full flex items-center">
            <span
              class="inline-block px-2.5 py-1 bg-slate-50/80 text-slate-700 font-bold rounded-xl border border-slate-100 text-[11px] font-mono tracking-wider group-hover:bg-white group-hover:border-indigo-400 transition-all"
            >
              {{ material.maChatLieu }}
            </span>
          </div>

          <div
            class="col-span-3 text-sm font-bold text-slate-800 capitalize truncate md:px-4 md:border-r md:border-slate-100 h-full flex items-center group-hover:translate-x-1.5 transition-transform"
          >
            {{ material.tenChatLieu }}
          </div>

          <div
            class="col-span-2 flex md:justify-center items-center gap-2 md:gap-0 md:border-r md:border-slate-100 h-full"
          >
            <span
              :class="[
                'inline-flex items-center gap-1.5 px-3 py-1 text-[10px] font-bold uppercase tracking-wider rounded-xl border transition-all duration-300',
                material.trangThai
                  ? 'bg-emerald-50 text-emerald-600 border-emerald-100'
                  : 'bg-rose-50 text-rose-600 border-rose-100',
              ]"
            >
              <span
                :class="[
                  'w-1.5 h-1.5 rounded-full',
                  material.trangThai ? 'bg-emerald-500 animate-pulse' : 'bg-rose-500',
                ]"
              ></span>
              {{ material.trangThai ? 'Active' : 'Muted' }}
            </span>
          </div>

          <div
            class="col-span-3 text-[10px] text-slate-400 flex flex-col gap-0.5 md:px-4 md:border-r md:border-slate-100 h-full justify-center font-mono"
          >
            <div>Tạo: {{ formatDate(material.ngayTao) }}</div>
            <div>Sửa: {{ formatDate(material.ngayCapNhat) }}</div>
          </div>

          <div class="col-span-1 flex justify-end md:justify-center items-center pt-3 md:pt-0">
            <div class="flex gap-1">
              <button
                @click="openEditModal(material)"
                class="p-2 text-slate-400 hover:text-blue-600 hover:bg-blue-50 rounded-xl transition-all"
                title="Chỉnh sửa"
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
                @click="promptDelete(material.id)"
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
        </div>

        <div
          v-if="materials.length === 0"
          class="text-center py-20 bg-white border border-slate-100 rounded-3xl text-slate-400 text-sm"
        >
          <p>Kho lưu trữ chất liệu trống rỗng.</p>
        </div>
      </div>
    </div>

    <div v-if="isModalOpen" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-slate-900/30 backdrop-blur-sm" @click="closeModal"></div>
      <div
        class="bg-white border border-slate-100 rounded-3xl shadow-xl w-full max-w-md z-10 p-6 animate-scale-up"
      >
        <h3 class="text-xs font-bold uppercase tracking-widest text-indigo-600 mb-6">
          {{ isEditMode ? '⚡ CẬP NHẬT CHẤT LIỆU' : '✨ KHỞI TẠO CHẤT LIỆU' }}
        </h3>
        <form @submit.prevent="handleSubmit" class="space-y-4">
          <div>
            <label class="block text-[10px] font-bold text-slate-400 uppercase mb-1"
              >Mã định danh</label
            >
            <input
              v-model="form.maChatLieu"
              placeholder="VD: CT"
              class="w-full bg-slate-50 border border-slate-100 px-4 py-2.5 rounded-2xl text-sm outline-none uppercase font-mono focus:bg-white focus:border-indigo-500 transition-all"
              required
            />
          </div>
          <div>
            <label class="block text-[10px] font-bold text-slate-400 uppercase mb-1"
              >Tên chất liệu</label
            >
            <input
              v-model="form.tenChatLieu"
              placeholder="Nhập tên chất liệu..."
              class="w-full bg-slate-50 border border-slate-100 px-4 py-2.5 rounded-2xl text-sm outline-none focus:bg-white focus:border-indigo-500 transition-all"
              required
            />
          </div>
          <div class="flex gap-3 pt-2">
            <button
              type="button"
              @click="form.trangThai = true"
              :class="[
                'flex-1 py-3 text-xs font-bold rounded-2xl border transition-all',
                form.trangThai
                  ? 'bg-emerald-50 border-emerald-500 text-emerald-700'
                  : 'bg-slate-50 border-slate-100 text-slate-400',
              ]"
            >
              HOẠT ĐỘNG
            </button>
            <button
              type="button"
              @click="form.trangThai = false"
              :class="[
                'flex-1 py-3 text-xs font-bold rounded-2xl border transition-all',
                !form.trangThai
                  ? 'bg-rose-50 border-rose-500 text-rose-700'
                  : 'bg-slate-50 border-slate-100 text-slate-400',
              ]"
            >
              TẠM NGỪNG
            </button>
          </div>
          <div class="flex justify-end gap-2 pt-4">
            <button
              type="button"
              @click="closeModal"
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

    <div v-if="deleteId" class="fixed inset-0 z-[60] flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-slate-900/30 backdrop-blur-sm" @click="deleteId = null"></div>
      <div
        class="bg-white border border-slate-100 rounded-3xl shadow-xl w-full max-w-xs p-6 z-10 animate-scale-up text-center"
      >
        <h3 class="text-sm font-bold text-slate-800 mb-2">Xác nhận xóa</h3>
        <p class="text-[11px] text-slate-400 mb-6">Bạn có chắc muốn xóa chất liệu này?</p>
        <div class="flex gap-2">
          <button
            @click="deleteId = null"
            class="flex-1 py-2.5 bg-slate-100 hover:bg-slate-200 rounded-2xl text-xs font-bold transition-all"
          >
            HỦY
          </button>
          <button
            @click="confirmDelete"
            class="flex-1 py-2.5 bg-rose-600 hover:bg-rose-700 text-white rounded-2xl text-xs font-bold shadow-md shadow-rose-600/10 transition-all"
          >
            XÓA NGAY
          </button>
        </div>
      </div>
    </div>

    <div class="fixed bottom-6 right-6 z-[100] flex flex-col gap-2">
      <transition-group name="toast">
        <div
          v-for="toast in toasts"
          :key="toast.id"
          :class="[
            'px-5 py-3.5 rounded-2xl text-white shadow-xl flex items-center gap-3 font-bold text-xs',
            toast.type === 'success' ? 'bg-emerald-600' : 'bg-rose-600',
          ]"
        >
          <span>{{ toast.type === 'success' ? '✓' : '✕' }}</span>
          <p>{{ toast.message }}</p>
        </div>
      </transition-group>
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

const materials = ref([]),
  toasts = ref([]),
  isModalOpen = ref(false),
  isEditMode = ref(false),
  deleteId = ref(null)
const form = ref({ id: null, maChatLieu: '', tenChatLieu: '', trangThai: true })

const showToast = (message, type = 'success') => {
  const id = Date.now()
  toasts.value.push({ id, message, type })
  setTimeout(() => {
    toasts.value = toasts.value.filter((t) => t.id !== id)
  }, 3000)
}

const loadData = async () => {
  try {
    materials.value = await getAllChatLieu()
  } catch (err) {
    console.error('Lỗi khi tải dữ liệu:', err)
  }
}

onMounted(loadData)

const formatDate = (date) => {
  if (!date) return 'N/A'
  const d = new Date(date)
  if (isNaN(d.getTime())) return 'N/A'
  return `${d.toLocaleDateString('vi-VN')} ${d.toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' })}`
}

const openAddModal = () => {
  isEditMode.value = false
  isModalOpen.value = true
  form.value = { id: null, maChatLieu: '', tenChatLieu: '', trangThai: true }
}

const openEditModal = (material) => {
  isEditMode.value = true
  isModalOpen.value = true
  form.value = { ...material }
}

const closeModal = () => {
  isModalOpen.value = false
}

const handleSubmit = async () => {
  const isDuplicateMa = materials.value.some(
    (item) =>
      item.maChatLieu.trim().toLowerCase() === form.value.maChatLieu.trim().toLowerCase() &&
      (!isEditMode.value || item.id !== form.value.id),
  )

  const isDuplicateTen = materials.value.some(
    (item) =>
      item.tenChatLieu.trim().toLowerCase() === form.value.tenChatLieu.trim().toLowerCase() &&
      (!isEditMode.value || item.id !== form.value.id),
  )

  if (isDuplicateMa) {
    showToast('Mã chất liệu này đã tồn tại!', 'error')
    return
  }

  if (isDuplicateTen) {
    showToast('Tên chất liệu này đã tồn tại!', 'error')
    return
  }

  try {
    isEditMode.value
      ? await updateChatLieu(form.value.id, form.value)
      : await createChatLieu(form.value)
    showToast(isEditMode.value ? 'Cập nhật thành công!' : 'Thêm mới thành công!')
    await loadData()
    closeModal()
  } catch (err) {
    showToast('Có lỗi xảy ra', 'error')
  }
}

const promptDelete = (id) => {
  deleteId.value = id
}

const confirmDelete = async () => {
  try {
    await deleteChatLieu(deleteId.value)
    await loadData()
    showToast('Đã xóa thành công!')
  } catch (err) {
    showToast('Lỗi khi xóa', 'error')
  } finally {
    deleteId.value = null
  }
}
</script>

<style scoped>
.toast-enter-active,
.toast-leave-active {
  transition: all 0.3s ease;
}
.toast-enter-from,
.toast-leave-to {
  opacity: 0;
  transform: translateX(30px);
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
.animate-fade-in {
  animation: fadeIn 0.35s ease-out forwards;
}
.animate-scale-up {
  animation: scaleUp 0.2s ease-out forwards;
}
</style>
