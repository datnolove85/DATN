<template>
  <div
    class="space-y-6 max-w-full mx-auto p-4 animate-fade-in bg-slate-50 text-slate-800 rounded-2xl selection:bg-indigo-100 selection:text-indigo-900 overflow-hidden"
  >
    <!-- HEADER BLOCK: Nền tối sâu #0b0f19 tối giản sang trọng -->
    <div
      class="relative p-8 bg-[#0b0f19] rounded-3xl text-white flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 shadow-sm overflow-hidden"
    >
      <div>
        <span class="text-[10px] tracking-widest uppercase font-bold text-indigo-400"
          >Hệ thống quản trị thế hệ mới</span
        >
        <h1 class="text-2xl font-bold tracking-tight mt-1">Quản Lý Chiến Dịch Khuyến Mãi</h1>
        <p class="text-xs text-slate-400 mt-1 flex items-center gap-2">
          <span class="relative flex h-2 w-2">
            <span
              class="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75"
            ></span>
            <span class="relative inline-block rounded-full h-2 w-2 bg-emerald-500"></span>
          </span>
          Điều phối và cấu hình giảm giá thời gian thực
        </p>
      </div>

      <!-- BUTTON: Nút bấm màu Indigo đặc trưng -->
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
        TẠO ĐỢT GIẢM GIÁ MỚI
      </button>
    </div>

    <!-- FILTER & SEARCH BAR -->
    <div
      class="flex flex-col sm:flex-row items-stretch sm:items-center gap-3 bg-white p-3 rounded-2xl border border-slate-100 shadow-sm"
    >
      <div class="relative flex-grow">
        <input
          v-model="searchQuery"
          type="text"
          placeholder="Tìm kiếm theo mã hoặc tên chiến dịch..."
          class="w-full pl-4 pr-4 h-11 bg-slate-50/60 border border-slate-100 rounded-xl text-sm focus:bg-white focus:border-indigo-500 outline-none transition-all"
        />
      </div>
      <select
        v-model="statusFilter"
        class="px-4 h-11 bg-slate-50/60 border border-slate-100 rounded-xl text-xs font-bold text-slate-700 outline-none cursor-pointer tracking-wide focus:bg-white focus:border-indigo-500 transition-all min-w-[160px]"
      >
        <option value="all">TẤT CẢ TRẠNG THÁI</option>
        <option value="active">ACTIVE</option>
        <option value="inactive">MUTED</option>
      </select>
    </div>

    <!-- DATA TABLE LIST -->
    <div class="space-y-3">
      <!-- TABLE HEADER -->
      <div
        class="hidden md:grid grid-cols-12 gap-0 px-6 py-3 bg-white border border-slate-100 rounded-t-xl text-[11px] uppercase text-slate-400 font-bold tracking-wider select-none shadow-sm"
      >
        <div class="col-span-3 px-4 border-r border-slate-100">Chương trình</div>
        <div class="col-span-2 text-center border-r border-slate-100">Loại áp dụng</div>
        <div class="col-span-2 text-center border-r border-slate-100">Giá trị giảm</div>
        <div class="col-span-2 text-center border-r border-slate-100">Trạng thái</div>
        <div class="col-span-2 px-4 border-r border-slate-100">Thời hạn chiến dịch</div>
        <div class="col-span-1 text-center">Tùy chọn</div>
      </div>

      <div class="space-y-2">
        <!-- LIST ITEM -->
        <div
          v-for="(item, index) in filteredList"
          :key="item.id ?? index"
          class="grid grid-cols-1 md:grid-cols-12 gap-4 md:gap-0 px-6 py-4 md:py-3 items-center bg-white border border-slate-100 hover:border-indigo-500 hover:bg-slate-50/80 rounded-xl md:rounded-lg transition-all duration-300 ease-in-out shadow-sm group"
        >
          <div
            class="col-span-3 flex flex-col justify-center md:px-4 md:border-r md:border-slate-100 h-full group-hover:translate-x-1 transition-transform"
          >
            <div class="text-sm font-bold text-slate-800 truncate">{{ item.tenDotGiamGia }}</div>
            <div class="mt-1">
              <span
                class="inline-block px-2 py-0.5 bg-slate-50 text-indigo-600 font-bold rounded-xl border border-slate-100 text-[10px] font-mono tracking-wide uppercase group-hover:bg-white group-hover:border-indigo-400 transition-all"
              >
                {{ item.maDotGiamGia }}
              </span>
            </div>
          </div>

          <div
            class="col-span-2 flex md:justify-center items-center md:px-4 md:border-r md:border-slate-100 h-full text-xs font-bold text-slate-500"
          >
            <span class="md:hidden text-[10px] uppercase font-bold text-slate-400 mr-2">Loại:</span>
            {{ item.loaiGiamGia || 'Phần trăm (%)' }}
          </div>

          <div
            class="col-span-2 flex md:justify-center items-center md:px-4 md:border-r md:border-slate-100 h-full font-mono text-sm font-bold text-indigo-600"
          >
            <span class="md:hidden text-[10px] uppercase font-bold text-slate-400 mr-2"
              >Giảm giá:</span
            >
            {{ item.giaTriGiam }}%
          </div>

          <div
            class="col-span-2 flex md:justify-center items-center md:border-r md:border-slate-100 h-full"
          >
            <span
              :class="[
                'inline-flex items-center gap-1.5 px-3 py-1 text-[10px] font-bold uppercase tracking-wider rounded-xl border transition-all duration-300',
                item.trangThai
                  ? 'bg-emerald-50 text-emerald-600 border-emerald-100'
                  : 'bg-rose-50 text-rose-600 border-rose-100',
              ]"
            >
              <span
                :class="[
                  'w-1.5 h-1.5 rounded-full',
                  item.trangThai ? 'bg-emerald-500 animate-pulse' : 'bg-rose-500',
                ]"
              ></span>
              {{ item.trangThai ? 'Active' : 'Muted' }}
            </span>
          </div>

          <div
            class="col-span-2 text-[10px] text-slate-400 flex flex-col gap-0.5 md:px-4 md:border-r md:border-slate-100 h-full justify-center font-mono whitespace-nowrap"
          >
            <div>BD: {{ formatDate(item.ngayBatDau) }}</div>
            <div>KT: {{ formatDate(item.ngayKetThuc) }}</div>
          </div>

          <div class="col-span-1 flex justify-end md:justify-center items-center pt-2 md:pt-0">
            <!-- OPERATIONS BUTTONS -->
            <div class="flex gap-1">
              <button
                @click="openEditModal(item)"
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
                @click="prepareDelete(item.id)"
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

        <!-- EMPTY STATE -->
        <div
          v-if="filteredList.length === 0"
          class="text-center py-20 bg-white border border-slate-100 rounded-3xl text-slate-400 text-sm"
        >
          <p>Không tìm thấy chiến dịch khuyến mãi nào phù hợp.</p>
        </div>
      </div>
    </div>

    <!-- MODAL FORM -->
    <Teleport to="body">
      <div v-if="isModalOpen" class="fixed inset-0 z-[999] flex items-center justify-center p-4">
        <div
          class="absolute inset-0 bg-slate-900/30 backdrop-blur-sm"
          @click="isModalOpen = false"
        ></div>
        <div
          class="bg-white border border-slate-100 rounded-3xl shadow-xl w-full max-w-xl z-10 flex flex-col max-h-[90vh] overflow-hidden animate-scale-up"
        >
          <!-- MODAL HEADER -->
          <div
            class="px-6 py-5 border-b border-slate-100 bg-slate-50/50 flex justify-between items-center flex-shrink-0"
          >
            <div class="space-y-0.5">
              <h3 class="text-xs font-bold uppercase tracking-widest text-indigo-600">
                {{ isEdit ? '⚡ CẬP NHẬT CHIẾN DỊCH' : '✨ KHỞI TẠO CHIẾN DỊCH' }}
              </h3>
              <p class="text-[11px] text-slate-400">
                Vui lòng cấu hình các thông số chiến dịch giảm giá dưới đây
              </p>
            </div>
            <button
              type="button"
              @click="isModalOpen = false"
              class="p-1.5 text-slate-400 hover:text-slate-600 hover:bg-slate-100 rounded-xl transition-all"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke-width="2.5"
                stroke="currentColor"
                class="w-4 h-4"
              >
                <path stroke-linecap="round" stroke-linejoin="round" d="M6 18 18 6M6 6l12 12" />
              </svg>
            </button>
          </div>

          <!-- MODAL BODY FORM -->
          <form @submit.prevent="save" class="flex flex-col flex-grow overflow-hidden">
            <div class="p-6 space-y-5 overflow-y-auto flex-grow custom-scrollbar">
              <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div class="space-y-1">
                  <label class="text-[10px] font-bold text-slate-400 uppercase"
                    >Mã giảm giá *</label
                  >
                  <input
                    v-model="form.maDotGiamGia"
                    placeholder="VD: SUMMER2026"
                    class="w-full h-11 bg-slate-50 border border-slate-100 px-4 rounded-xl text-sm focus:bg-white focus:border-indigo-500 outline-none transition-all uppercase font-mono font-bold text-indigo-600"
                    required
                  />
                </div>

                <div class="space-y-1">
                  <label class="text-[10px] font-bold text-slate-400 uppercase"
                    >Giá trị giảm (%) *</label
                  >
                  <input
                    type="number"
                    v-model="form.giaTriGiam"
                    placeholder="Nhập từ 1 - 100"
                    class="w-full h-11 bg-slate-50 border border-slate-100 px-4 rounded-xl text-sm focus:bg-white focus:border-indigo-500 outline-none transition-all font-mono font-bold text-slate-700"
                    required
                  />
                </div>
              </div>

              <div class="space-y-1">
                <label class="text-[10px] font-bold text-slate-400 uppercase"
                  >Tên chương trình *</label
                >
                <input
                  v-model="form.tenDotGiamGia"
                  placeholder="Nhập tên đợt giảm giá (ví dụ: Ưu đãi mùa hè)..."
                  class="w-full h-11 bg-slate-50 border border-slate-100 px-4 rounded-xl text-sm focus:bg-white focus:border-indigo-500 outline-none transition-all font-semibold text-slate-800"
                  required
                />
              </div>

              <!-- TIMELINE BLOCK -->
              <div class="p-4 bg-slate-50/80 rounded-2xl border border-slate-100 space-y-3">
                <div class="text-[10px] font-bold text-slate-400 uppercase tracking-wider">
                  Thời hạn áp dụng chiến dịch
                </div>
                <div class="grid grid-cols-1 sm:grid-cols-2 gap-3">
                  <div class="space-y-1">
                    <label class="text-[11px] font-semibold text-slate-500">Ngày bắt đầu</label>
                    <input
                      type="datetime-local"
                      v-model="form.ngayBatDau"
                      class="w-full h-10 border border-slate-100 px-3 rounded-xl text-xs font-mono font-medium text-slate-700 outline-none focus:border-indigo-500 bg-white shadow-sm"
                      required
                    />
                  </div>
                  <div class="space-y-1">
                    <label class="text-[11px] font-semibold text-slate-500">Ngày kết thúc</label>
                    <input
                      type="datetime-local"
                      v-model="form.ngayKetThuc"
                      class="w-full h-10 border border-slate-100 px-3 rounded-xl text-xs font-mono font-medium text-slate-700 outline-none focus:border-indigo-500 bg-white shadow-sm"
                      required
                    />
                  </div>
                </div>
              </div>

              <!-- STATUS CHANGER -->
              <div class="space-y-1.5">
                <label class="text-[10px] font-bold text-slate-400 uppercase"
                  >Trạng thái hoạt động</label
                >
                <div
                  class="flex p-1 bg-slate-100 rounded-xl border border-slate-200/40 w-full sm:w-64"
                >
                  <button
                    type="button"
                    @click="form.trangThai = true"
                    :class="[
                      'flex-1 py-2 text-xs font-bold rounded-lg transition-all flex items-center justify-center gap-1.5',
                      form.trangThai
                        ? 'bg-white text-emerald-600 shadow-sm'
                        : 'text-slate-400 hover:text-slate-600',
                    ]"
                  >
                    <span
                      class="w-1.5 h-1.5 rounded-full bg-emerald-500"
                      v-if="form.trangThai"
                    ></span>
                    ACTIVE
                  </button>
                  <button
                    type="button"
                    @click="form.trangThai = false"
                    :class="[
                      'flex-1 py-2 text-xs font-bold rounded-lg transition-all flex items-center justify-center gap-1.5',
                      !form.trangThai
                        ? 'bg-white text-rose-600 shadow-sm'
                        : 'text-slate-400 hover:text-slate-600',
                    ]"
                  >
                    <span
                      class="w-1.5 h-1.5 rounded-full bg-rose-500"
                      v-if="!form.trangThai"
                    ></span>
                    MUTED
                  </button>
                </div>
              </div>
            </div>

            <!-- MODAL FOOTER ACTION -->
            <div
              class="flex justify-end gap-2 px-6 py-4 border-t border-slate-100 bg-slate-50 flex-shrink-0"
            >
              <button
                type="button"
                @click="isModalOpen = false"
                class="px-5 h-10 bg-slate-100 hover:bg-slate-200 text-slate-600 rounded-xl text-xs font-bold transition-all"
              >
                HỦY BỎ
              </button>
              <button
                type="submit"
                class="px-6 h-10 bg-indigo-600 text-white rounded-xl text-xs font-bold hover:bg-indigo-500 shadow-md shadow-indigo-600/10 transition-all uppercase"
              >
                XÁC NHẬN LƯU
              </button>
            </div>
          </form>
        </div>
      </div>
    </Teleport>

    <!-- DIALOG: CONFIRM DELETE -->
    <Teleport to="body">
      <div v-if="deleteId" class="fixed inset-0 z-[999] flex items-center justify-center p-4">
        <div
          class="absolute inset-0 bg-slate-900/30 backdrop-blur-sm"
          @click="deleteId = null"
        ></div>
        <div
          class="bg-white border border-slate-100 rounded-3xl shadow-xl w-full max-w-xs p-6 z-10 animate-scale-up text-center"
        >
          <div
            class="w-12 h-12 bg-rose-50 text-rose-600 rounded-full flex items-center justify-center mx-auto mb-3"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="w-5 h-5"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              stroke-width="2.5"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
              />
            </svg>
          </div>
          <h3 class="text-sm font-bold text-slate-800 mb-1">XÁC NHẬN XÓA</h3>
          <p class="text-[11px] text-slate-400 mb-6">Chiến dịch này sẽ bị gỡ bỏ vĩnh viễn.</p>
          <div class="flex gap-2">
            <button
              @click="deleteId = null"
              class="flex-1 py-2.5 bg-slate-100 hover:bg-slate-200 rounded-2xl text-xs font-bold transition-all"
            >
              HỦY
            </button>
            <button
              @click="executeDelete"
              class="flex-1 py-2.5 bg-rose-600 hover:bg-rose-700 text-white rounded-2xl text-xs font-bold shadow-md shadow-rose-600/10 transition-all"
            >
              XÓA NGAY
            </button>
          </div>
        </div>
      </div>
    </Teleport>

    <!-- TOAST NOTIFICATION CORNER: Bọc Teleport độc lập để giải quyết triệt để lỗi che khuất thông báo -->
    <Teleport to="body">
      <div
        class="fixed top-6 right-6 z-[9999] flex flex-col gap-3 pointer-events-none max-w-sm w-full sm:w-auto"
      >
        <transition-group name="toast">
          <div
            v-for="toast in toasts"
            :key="toast.id"
            :class="[
              'pointer-events-auto px-5 py-4 rounded-2xl text-white shadow-[0_10px_30px_rgba(0,0,0,0.2)] flex items-center gap-3 font-bold text-xs border backdrop-blur-md transition-all duration-300',
              toast.type === 'success'
                ? 'bg-emerald-600/95 border-emerald-500 shadow-emerald-600/10'
                : 'bg-rose-600/95 border-rose-500 shadow-rose-600/10',
            ]"
          >
            <div
              class="flex-shrink-0 w-4 h-4 rounded-full bg-white/20 flex items-center justify-center text-[10px]"
            >
              {{ toast.type === 'success' ? '✓' : '✕' }}
            </div>
            <p class="flex-grow tracking-wide text-white">{{ toast.message }}</p>
          </div>
        </transition-group>
      </div>
    </Teleport>
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
const form = ref({
  id: null,
  maDotGiamGia: '',
  tenDotGiamGia: '',
  giaTriGiam: null,
  trangThai: true,
  ngayBatDau: '',
  ngayKetThuc: '',
})
const currentId = ref(null)
const searchQuery = ref('')
const statusFilter = ref('all')
const toasts = ref([])
const deleteId = ref(null)

const showToast = (message, type = 'success') => {
  const id = Date.now()
  toasts.value.push({ id, message, type })
  setTimeout(() => {
    toasts.value = toasts.value.filter((t) => t.id !== id)
  }, 3000)
}

const filteredList = computed(() => {
  return list.value.filter((item) => {
    const ma = item.maDotGiamGia?.toLowerCase() || ''
    const ten = item.tenDotGiamGia?.toLowerCase() || ''
    const query = searchQuery.value.toLowerCase().trim()

    const matchesSearch = ma.includes(query) || ten.includes(query)
    const matchesStatus =
      statusFilter.value === 'all' ||
      (statusFilter.value === 'active' ? item.trangThai : !item.trangThai)

    return matchesSearch && matchesStatus
  })
})

const loadData = async () => {
  try {
    list.value = await getAllDotGiamGia()
  } catch (err) {
    console.error('Lỗi khi tải danh sách đợt giảm giá:', err)
  }
}

const formatDate = (date) => {
  if (!date) return 'N/A'
  const d = new Date(date)
  if (isNaN(d.getTime())) return 'N/A'
  return `${d.toLocaleDateString('vi-VN')} ${d.toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' })}`
}

const openAddModal = () => {
  isEdit.value = false
  currentId.value = null
  form.value = {
    id: null,
    maDotGiamGia: '',
    tenDotGiamGia: '',
    giaTriGiam: null,
    trangThai: true,
    ngayBatDau: '',
    ngayKetThuc: '',
  }
  isModalOpen.value = true
}

const openEditModal = (item) => {
  isEdit.value = true
  currentId.value = item.id
  form.value = {
    ...item,
    ngayBatDau: item.ngayBatDau?.slice(0, 16) || '',
    ngayKetThuc: item.ngayKetThuc?.slice(0, 16) || '',
  }
  isModalOpen.value = true
}

const save = async () => {
  const code = form.value.maDotGiamGia.trim().toLowerCase()
  const name = form.value.tenDotGiamGia.trim().toLowerCase()
  const valueDiscount = Number(form.value.giaTriGiam)

  // 1. Kiểm tra dải giá trị phần trăm (%)
  if (isNaN(valueDiscount) || valueDiscount <= 0 || valueDiscount > 100) {
    showToast('Giá trị giảm giá phải từ 1% đến 100%!', 'error')
    return
  }

  // 2. Kiểm tra logic mốc thời gian áp dụng
  if (form.value.ngayBatDau && form.value.ngayKetThuc) {
    const start = new Date(form.value.ngayBatDau).getTime()
    const end = new Date(form.value.ngayKetThuc).getTime()
    if (end <= start) {
      showToast('Ngày kết thúc phải diễn ra sau ngày bắt đầu!', 'error')
      return
    }
  }

  // 3. Kiểm tra trùng lặp mã chiến dịch
  const isDuplicateCode = list.value.some(
    (item) =>
      item.maDotGiamGia.trim().toLowerCase() === code &&
      (!isEdit.value || item.id !== currentId.value),
  )
  if (isDuplicateCode) {
    showToast('Mã đợt giảm giá này đã tồn tại trên hệ thống!', 'error')
    return
  }

  // 4. Kiểm tra trùng lặp tên chiến dịch
  const isDuplicateName = list.value.some(
    (item) =>
      item.tenDotGiamGia.trim().toLowerCase() === name &&
      (!isEdit.value || item.id !== currentId.value),
  )
  if (isDuplicateName) {
    showToast('Tên chương trình giảm giá này đã tồn tại!', 'error')
    return
  }

  try {
    isEdit.value
      ? await updateDotGiamGia(currentId.value, form.value)
      : await createDotGiamGia(form.value)
    showToast(isEdit.value ? 'Cập nhật chiến dịch thành công!' : 'Khởi tạo chiến dịch thành công!')
    isModalOpen.value = false
    await loadData()
  } catch (error) {
    showToast('Có lỗi xảy ra trong quá trình xử lý', 'error')
  }
}

const prepareDelete = (id) => {
  deleteId.value = id
}

const executeDelete = async () => {
  if (!deleteId.value) return
  try {
    await deleteDotGiamGia(deleteId.value)
    showToast('Đã xóa đợt giảm giá thành công!')
    await loadData()
  } catch (error) {
    showToast('Lỗi hệ thống khi thực hiện xóa', 'error')
  } finally {
    deleteId.value = null
  }
}

onMounted(loadData)
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
  animation: scaleUp 0.2s cubic-bezier(0.16, 1, 0.3, 1) forwards;
}

.custom-scrollbar::-webkit-scrollbar {
  width: 5px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background: #e2e8f0;
  border-radius: 9999px;
}
.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: #cbd5e1;
}
</style>
