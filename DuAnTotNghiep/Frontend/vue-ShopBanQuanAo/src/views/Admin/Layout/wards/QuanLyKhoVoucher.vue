<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useToast } from 'vue-toastification'
import axios from 'axios'

const toast = useToast()

// DATA & STATES[cite: 1, 3]
const loading = ref(false)
const firstLoading = ref(true)
const dsKhoVoucher = ref([])
const search = ref('')
const filterStatus = ref('all')

const showModal = ref(false)
const isEdit = ref(false)
const currentId = ref(null)

// PHÂN TRANG
const currentPage = ref(1)
const pageSize = ref(10) // Số bản ghi trên 1 trang

// Tự động quay về trang 1 khi người dùng gõ tìm kiếm hoặc đổi tab lọc
watch([search, filterStatus], () => {
  currentPage.value = 1
})

const totalPages = computed(() => {
  return Math.ceil(filteredData.value.length / pageSize.value) || 1
})

const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredData.value.slice(start, end)
})

const changePage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
  }
}

const form = ref({
  tenVoucher: '',
  loaiGiamGia: 'tien_mat',
  giaTriGiam: 0,
  giaTriGiamToiDa: null,
  dieuKienToiThieu: 0,
  soXuDoi: 0,
  ngayBatDau: '',
  ngayHetHan: '',
  moTa: '',
  trangThai: true,
})

// Confirm Modal State[cite: 1, 3]
const confirmModal = ref({
  show: false,
  icon: '✕',
  label: 'Xác nhận xóa',
  title: 'Xóa kho voucher?',
  message: '',
  code: '',
  confirmText: 'Xóa voucher',
  payload: null,
})

const apiUrl = 'http://localhost:8080/api/admin/gamification/kho-voucher'

// FILTERED DATA[cite: 1, 3]
const filteredData = computed(() => {
  return dsKhoVoucher.value.filter((item) => {
    const keyword = search.value.trim().toLowerCase()
    const matchKeyword =
      !keyword ||
      item.tenVoucher?.toLowerCase().includes(keyword) ||
      item.maCode?.toLowerCase().includes(keyword)

    let matchStatus = true
    if (filterStatus.value === 'active') matchStatus = item.trangThai === true
    if (filterStatus.value === 'inactive') matchStatus = item.trangThai === false

    return matchKeyword && matchStatus
  })
})

const resetFilter = () => {
  search.value = ''
  filterStatus.value = 'all'
}

// LOAD DATA[cite: 1, 3]
const loadData = async () => {
  try {
    if (firstLoading.value) {
      loading.value = true
    }
    const response = await axios.get(apiUrl)
    dsKhoVoucher.value = response.data
  } catch (e) {
    toast.error('Không thể tải danh sách kho voucher')
  } finally {
    loading.value = false
    firstLoading.value = false
  }
}

// OPEN MODAL[cite: 1, 3]
const openCreate = () => {
  isEdit.value = false
  currentId.value = null
  form.value = {
    tenVoucher: '',
    loaiGiamGia: 'tien_mat',
    giaTriGiam: 0,
    giaTriGiamToiDa: null,
    dieuKienToiThieu: 0,
    soXuDoi: 0,
    ngayBatDau: '',
    ngayHetHan: '',
    moTa: '',
    trangThai: true,
  }
  showModal.value = true
}

const openEdit = (item) => {
  isEdit.value = true
  currentId.value = item.id
  form.value = {
    tenVoucher: item.tenVoucher,
    loaiGiamGia: item.loaiGiamGia || 'tien_mat',
    giaTriGiam: item.giaTriGiam,
    giaTriGiamToiDa: item.giaTriGiamToiDa || null,
    dieuKienToiThieu: item.dieuKienToiThieu || 0,
    soXuDoi: item.soXuDoi,
    ngayBatDau: item.ngayBatDau ? item.ngayBatDau.substring(0, 16) : '',
    ngayHetHan: item.ngayHetHan ? item.ngayHetHan.substring(0, 16) : '',
    moTa: item.moTa || '',
    trangThai: item.trangThai,
  }
  showModal.value = true
}
// SAVE (CREATE / UPDATE) WITH VALIDATION
const saveVoucher = async () => {
  // 0. Kiểm tra tên voucher không được để trống và độ dài
  if (!form.value.tenVoucher || !form.value.tenVoucher.trim()) {
    toast.error('Tên voucher không được để trống!')
    return
  }
  if (form.value.tenVoucher.trim().length < 3) {
    toast.error('Tên voucher phải có ít nhất 3 ký tự!')
    return
  }

  // 0.1. Kiểm tra trùng tên voucher trong danh sách
  const trimmedName = form.value.tenVoucher.trim().toLowerCase()
  const isDuplicate = dsKhoVoucher.value.some((item) => {
    // Nếu đang ở chế độ sửa, bỏ qua chính item đang được sửa
    if (isEdit.value && item.id === currentId.value) {
      return false
    }
    return item.tenVoucher?.trim().toLowerCase() === trimmedName
  })

  if (isDuplicate) {
    toast.error('Tên voucher này đã tồn tại trong hệ thống, vui lòng chọn tên khác!')
    return
  }

  // 1. Kiểm tra các giá trị không được âm[cite: 3]
  if (form.value.giaTriGiam < 0) {
    toast.error('Giá trị giảm không được là số âm!')
    return
  }
  if (form.value.dieuKienToiThieu < 0) {
    toast.error('Giá trị đơn tối thiểu không được là số âm!')
    return
  }
  if (form.value.soXuDoi < 0) {
    toast.error('Số xu đổi không được là số âm!')
    return
  }
  if (form.value.giaTriGiamToiDa !== null && form.value.giaTriGiamToiDa < 0) {
    toast.error('Giá trị giảm tối đa không được là số âm!')
    return
  }

  // 2. Kiểm tra phần trăm không quá 100[cite: 3]
  if (form.value.loaiGiamGia === 'phan_tram' && form.value.giaTriGiam > 100) {
    toast.error('Giá trị giảm theo phần trăm (%) không được lớn hơn 100!')
    return
  }

  // 3. Kiểm tra giá trị giảm so với đơn tối thiểu[cite: 3]
  if (
    form.value.loaiGiamGia === 'tien_mat' &&
    form.value.dieuKienToiThieu > 0 &&
    form.value.giaTriGiam > form.value.dieuKienToiThieu
  ) {
    toast.error('Giá trị giảm tiền mặt không được lớn hơn giá trị đơn tối thiểu!')
    return
  }

  if (
    form.value.loaiGiamGia === 'phan_tram' &&
    form.value.giaTriGiamToiDa !== null &&
    form.value.giaTriGiamToiDa !== '' &&
    form.value.dieuKienToiThieu > 0 &&
    Number(form.value.giaTriGiamToiDa) > Number(form.value.dieuKienToiThieu)
  ) {
    toast.error('Giá trị giảm tối đa không được lớn hơn giá trị đơn tối thiểu!')
    return
  }

  // 4. Kiểm tra ngày tháng[cite: 3]
  if (form.value.ngayBatDau && form.value.ngayHetHan) {
    if (new Date(form.value.ngayBatDau) > new Date(form.value.ngayHetHan)) {
      toast.error('Ngày bắt đầu không được lớn hơn ngày hết hạn!')
      return
    }
  }

  try {
    let payload = {
      ...form.value,
      tenVoucher: form.value.tenVoucher.trim(),
      ngayBatDau: form.value.ngayBatDau ? form.value.ngayBatDau + ':00' : null,
      ngayHetHan: form.value.ngayHetHan ? form.value.ngayHetHan + ':00' : null,
      giaTriGiamToiDa: form.value.loaiGiamGia === 'phan_tram' ? form.value.giaTriGiamToiDa : null,
    }

    if (isEdit.value) {
      await axios.put(`${apiUrl}/${currentId.value}`, payload)
      toast.success('Cập nhật kho voucher thành công!')
    } else {
      await axios.post(apiUrl, payload)
      toast.success('Thêm mới kho voucher thành công!')
    }

    showModal.value = false
    await loadData()
  } catch (e) {
    toast.error('Lỗi: ' + (e.response?.data || e.message))
  }
}
// DELETE CONFIRM[cite: 1, 3]
const openDeleteConfirm = (item) => {
  confirmModal.value = {
    show: true,
    icon: '✕',
    label: 'Xác nhận xóa',
    title: 'Xóa kho voucher?',
    message: `Bạn có chắc chắn muốn xóa kho voucher "${item.tenVoucher}" không?`,
    code: item.maCode || '',
    confirmText: 'Xóa voucher',
    payload: item,
  }
}

const closeConfirmModal = () => {
  confirmModal.value.show = false
}

const handleConfirmAction = async () => {
  const { payload } = confirmModal.value
  closeConfirmModal()
  if (!payload) return

  try {
    await axios.delete(`${apiUrl}/${payload.id}`)
    toast.success('Xóa kho voucher thành công!')
    await loadData()
  } catch (e) {
    toast.error('Lỗi khi xóa: ' + (e.response?.data || e.message))
  }
}

onMounted(() => {
  loadData()
})

// FORMATTERS[cite: 1, 3]
const formatCurrency = (value) => {
  return Number(value || 0).toLocaleString('vi-VN') + ' đ'
}

const formatDate = (dateStr) => {
  if (!dateStr) return '--'
  return dateStr.replace('T', ' ')
}

const formatDiscount = (item) => {
  if (item.loaiGiamGia === 'phan_tram') {
    return `${item.giaTriGiam}%`
  }
  return formatCurrency(item.giaTriGiam)
}

// STATS[cite: 1, 3]
const tongVoucher = computed(() => dsKhoVoucher.value.length)
const tongHoatDong = computed(() => dsKhoVoucher.value.filter((x) => x.trangThai).length)
const tongTamNgung = computed(() => dsKhoVoucher.value.filter((x) => !x.trangThai).length)
</script>

<template>
  <div class="min-h-screen bg-slate-100 p-4 md:p-6 text-slate-800">
    <div class="max-w-[1700px] mx-auto space-y-6">
      <!-- HEADER -->
      <div
        class="flex flex-col sm:flex-row justify-between items-start sm:items-center bg-white rounded-2xl shadow-sm border border-slate-200 px-6 py-4 gap-4"
      >
        <div class="flex items-center gap-3">
          <div
            class="w-10 h-10 rounded-xl bg-indigo-50 flex items-center justify-center text-xl text-indigo-600 font-bold"
          >
            🎟️
          </div>
          <div>
            <h1 class="text-xl font-bold text-slate-800">Quản lý kho voucher đổi xu</h1>
            <p class="text-slate-500 text-xs">
              Quản lý toàn bộ kho voucher đổi thưởng bằng xu của hệ thống.
            </p>
          </div>
        </div>

        <button
          @click="openCreate"
          class="flex items-center gap-2 rounded-xl bg-indigo-600 px-4 py-2.5 font-medium text-white text-sm shadow-sm hover:bg-indigo-700 transition"
        >
          <span class="text-lg leading-none">+</span>
          <span>Thêm kho voucher mới</span>
        </button>
      </div>

      <!-- STATISTICS -->
      <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <div class="rounded-2xl bg-white border border-slate-200 p-4 shadow-sm">
          <div class="flex justify-between items-center">
            <div>
              <p class="text-slate-500 text-xs font-medium">Tổng kho voucher</p>
              <h2 class="mt-1 text-2xl font-bold text-slate-800">{{ tongVoucher }}</h2>
            </div>
            <div class="w-10 h-10 rounded-xl bg-indigo-50 flex items-center justify-center text-lg">
              📦
            </div>
          </div>
        </div>

        <div class="rounded-2xl bg-white border border-slate-200 p-4 shadow-sm">
          <div class="flex justify-between items-center">
            <div>
              <p class="text-slate-500 text-xs font-medium">Đang hoạt động</p>
              <h2 class="mt-1 text-2xl font-bold text-green-600">{{ tongHoatDong }}</h2>
            </div>
            <div class="w-10 h-10 rounded-xl bg-green-50 flex items-center justify-center text-lg">
              🟢
            </div>
          </div>
        </div>

        <div class="rounded-2xl bg-white border border-slate-200 p-4 shadow-sm">
          <div class="flex justify-between items-center">
            <div>
              <p class="text-slate-500 text-xs font-medium">Tạm ngưng</p>
              <h2 class="mt-1 text-2xl font-bold text-red-500">{{ tongTamNgung }}</h2>
            </div>
            <div class="w-10 h-10 rounded-xl bg-red-50 flex items-center justify-center text-lg">
              ⏸
            </div>
          </div>
        </div>
      </div>

      <!-- FILTER -->
      <div class="rounded-2xl border border-slate-200 bg-white shadow-sm p-4 md:p-5 space-y-4">
        <div class="flex flex-col lg:flex-row gap-4 justify-between items-start lg:items-center">
          <div class="w-full lg:w-1/3 relative">
            <svg
              class="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="M21 21l-5.2-5.2M10.5 18a7.5 7.5 0 100-15 7.5 7.5 0 000 15z"
              />
            </svg>
            <input
              v-model="search"
              placeholder="Tìm kiếm theo tên hoặc mã code voucher..."
              class="w-full rounded-xl border border-slate-200 bg-slate-50/50 pl-10 pr-4 py-2.5 text-sm text-slate-700 outline-none focus:bg-white focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500"
            />
          </div>

          <div class="flex items-center gap-3 w-full lg:w-auto">
            <button
              @click="resetFilter"
              class="rounded-xl bg-slate-100 hover:bg-slate-200 px-4 py-2 text-sm font-medium text-slate-600 transition flex items-center gap-1.5"
            >
              🔄 Làm mới
            </button>
          </div>
        </div>

        <!-- Quick Filter Tabs -->
        <div class="flex flex-wrap gap-2 pt-2 border-t border-slate-100">
          <button
            @click="filterStatus = 'all'"
            class="px-3 py-1.5 rounded-xl text-xs font-medium transition"
            :class="
              filterStatus === 'all'
                ? 'bg-indigo-600 text-white'
                : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
            "
          >
            Tất cả
          </button>
          <button
            @click="filterStatus = 'active'"
            class="px-3 py-1.5 rounded-xl text-xs font-medium transition"
            :class="
              filterStatus === 'active'
                ? 'bg-green-600 text-white'
                : 'bg-green-50 text-green-700 hover:bg-green-100'
            "
          >
            Đang hoạt động
          </button>
          <button
            @click="filterStatus = 'inactive'"
            class="px-3 py-1.5 rounded-xl text-xs font-medium transition"
            :class="
              filterStatus === 'inactive'
                ? 'bg-red-600 text-white'
                : 'bg-red-50 text-red-700 hover:bg-red-100'
            "
          >
            Ngừng hoạt động
          </button>
        </div>
      </div>

      <!-- TABLE -->
      <!-- TABLE -->
      <div class="rounded-2xl border border-slate-200 bg-white shadow-sm overflow-hidden">
        <div
          class="flex items-center justify-between border-b border-slate-200 px-6 py-4 bg-slate-50/50"
        >
          <h2 class="text-base font-bold text-slate-800">Danh sách kho voucher</h2>
          <span class="text-xs font-semibold text-slate-500 bg-slate-200/60 px-2.5 py-1 rounded-lg">
            Hiển thị {{ filteredData.length }} bản ghi
          </span>
        </div>

        <div class="overflow-x-auto">
          <table class="min-w-full text-left text-sm text-slate-600 divide-y divide-slate-200">
            <thead
              class="bg-slate-50 text-[11px] uppercase tracking-wider text-slate-500 font-semibold"
            >
              <tr>
                <th class="px-4 py-3 w-12 text-center">STT</th>
                <th class="px-4 py-3">Mã & Tên Voucher</th>
                <th class="px-4 py-3 text-center">Loại giảm</th>
                <th class="px-4 py-3 text-center">Mức giảm</th>
                <th class="px-4 py-3 text-center">Đơn tối thiểu</th>
                <th class="px-4 py-3 text-center">Số xu đổi</th>
                <th class="px-4 py-3 text-center">Thời gian</th>
                <th class="px-4 py-3 text-center">Trạng thái</th>
                <th class="px-4 py-3 text-center">Hành động</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-slate-100 text-xs">
              <!-- Đổi từ filteredData thành paginatedData -->
              <tr
                v-for="(item, index) in paginatedData"
                :key="item.id"
                class="hover:bg-slate-50 transition-colors"
              >
                <!-- Tính lại STT chuẩn theo trang hiện tại -->
                <td class="px-4 py-3.5 text-center font-mono text-slate-400">
                  {{ (currentPage - 1) * pageSize + index + 1 }}
                </td>

                <td class="px-4 py-3.5">
                  <div class="space-y-0.5">
                    <span
                      class="font-mono text-[11px] font-semibold text-indigo-600 bg-indigo-50 px-1.5 py-0.5 rounded border border-indigo-100"
                    >
                      {{ item.maCode }}
                    </span>
                    <div class="font-medium text-slate-800 text-sm mt-1">
                      {{ item.tenVoucher }}
                    </div>
                  </div>
                </td>

                <td class="px-4 py-3.5 text-center">
                  <span
                    class="inline-flex items-center px-2 py-0.5 rounded text-[11px] font-medium border"
                    :class="
                      item.loaiGiamGia === 'phan_tram'
                        ? 'bg-blue-50 text-blue-700 border-blue-100'
                        : 'bg-emerald-50 text-emerald-700 border-emerald-100'
                    "
                  >
                    {{ item.loaiGiamGia === 'phan_tram' ? 'Phần trăm (%)' : 'Tiền mặt (VNĐ)' }}
                  </span>
                </td>

                <td class="px-4 py-3.5 text-center">
                  <span class="font-bold text-slate-900 text-sm">
                    {{ formatDiscount(item) }}
                  </span>
                  <div
                    v-if="item.loaiGiamGia === 'phan_tram' && item.giaTriGiamToiDa"
                    class="text-[10px] text-slate-400"
                  >
                    Tối đa: {{ formatCurrency(item.giaTriGiamToiDa) }}
                  </div>
                </td>

                <td class="px-4 py-3.5 text-center font-medium text-slate-700">
                  {{ formatCurrency(item.dieuKienToiThieu) }}
                </td>

                <td class="px-4 py-3.5 text-center">
                  <span
                    class="inline-flex items-center gap-1 px-2.5 py-1 rounded-lg bg-amber-50 text-amber-700 border border-amber-200 font-bold"
                  >
                    🪙 {{ item.soXuDoi }} xu
                  </span>
                </td>

                <td class="px-4 py-3.5 text-center whitespace-nowrap text-[11px]">
                  <div class="text-slate-600">Từ: {{ formatDate(item.ngayBatDau) }}</div>
                  <div class="text-slate-400">Đến: {{ formatDate(item.ngayHetHan) }}</div>
                </td>

                <td class="px-4 py-3.5 text-center">
                  <span
                    class="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-[11px] font-medium border"
                    :class="
                      item.trangThai
                        ? 'bg-green-100 text-green-700 border-green-200'
                        : 'bg-red-100 text-red-700 border-red-200'
                    "
                  >
                    <span class="h-1.5 w-1.5 rounded-full bg-current"></span>
                    {{ item.trangThai ? 'Hoạt động' : 'Ngừng hoạt động' }}
                  </span>
                </td>

                <td class="px-4 py-3.5 text-center">
                  <div class="flex items-center justify-center gap-1.5">
                    <button
                      @click="openEdit(item)"
                      class="px-2.5 py-1 rounded-lg bg-amber-50 hover:bg-amber-500 hover:text-white text-amber-700 font-medium transition border border-amber-200 text-[11px]"
                    >
                      Sửa
                    </button>
                  </div>
                </td>
              </tr>

              <tr v-if="filteredData.length === 0">
                <td colspan="9" class="py-12 text-center text-slate-400">
                  <div class="text-3xl mb-1">📭</div>
                  <p class="text-xs">Chưa có kho voucher nào trong hệ thống</p>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- THANH PHÂN TRANG (PAGINATION FOOTER) -->
        <div
          class="flex flex-col sm:flex-row items-center justify-between border-t border-slate-200 px-6 py-4 bg-slate-50/50 gap-4"
        >
          <div class="text-xs text-slate-500">
            Hiển thị
            <span class="font-bold text-slate-700">{{
              filteredData.length ? (currentPage - 1) * pageSize + 1 : 0
            }}</span>
            đến
            <span class="font-bold text-slate-700">{{
              Math.min(currentPage * pageSize, filteredData.length)
            }}</span>
            trong tổng số
            <span class="font-bold text-slate-700">{{ filteredData.length }}</span> bản ghi
          </div>

          <div class="flex items-center gap-2">
            <button
              @click="changePage(currentPage - 1)"
              :disabled="currentPage === 1"
              class="px-3.5 py-1.5 rounded-xl border border-slate-200 bg-white text-xs font-medium text-slate-600 hover:bg-slate-50 disabled:opacity-40 disabled:cursor-not-allowed transition"
            >
              Trang trước
            </button>

            <div class="flex items-center gap-1 px-2">
              <span class="text-xs font-semibold text-slate-700"
                >Trang {{ currentPage }} / {{ totalPages || 1 }}</span
              >
            </div>

            <button
              @click="changePage(currentPage + 1)"
              :disabled="currentPage >= totalPages"
              class="px-3.5 py-1.5 rounded-xl border border-slate-200 bg-white text-xs font-medium text-slate-600 hover:bg-slate-50 disabled:opacity-40 disabled:cursor-not-allowed transition"
            >
              Trang sau
            </button>
          </div>
        </div>
      </div>

      <!-- MODAL THÊM / SỬA VOUCHER -->
      <Teleport to="body">
        <div v-if="showModal" class="fixed inset-0 z-[100] flex items-center justify-center p-4">
          <div
            class="absolute inset-0 bg-black/30 backdrop-blur-xs"
            @click="showModal = false"
          ></div>
          <div
            class="bg-white border border-slate-200 rounded-3xl shadow-2xl w-full max-w-2xl p-6 z-10 relative max-h-[90vh] overflow-y-auto"
          >
            <div class="flex items-center justify-between pb-4 border-b mb-4">
              <h3 class="text-base font-bold text-slate-800">
                {{ isEdit ? 'Cập Nhật Kho Voucher' : 'Thêm Mới Kho Voucher' }}
              </h3>
              <button
                @click="showModal = false"
                class="w-8 h-8 rounded-lg bg-slate-100 text-slate-600 hover:bg-slate-200 font-bold flex items-center justify-center"
              >
                ✕
              </button>
            </div>

            <form @submit.prevent="saveVoucher" class="space-y-4">
              <div>
                <label class="block text-xs font-semibold text-slate-600 mb-1">Tên Voucher</label>
                <input
                  type="text"
                  class="w-full rounded-xl border border-slate-200 px-3.5 py-2 text-sm outline-none focus:border-indigo-500"
                  v-model="form.tenVoucher"
                  required
                  placeholder="VD: Giảm 50K đơn 200K"
                />
              </div>

              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label class="block text-xs font-semibold text-slate-600 mb-1"
                    >Loại Giảm Giá</label
                  >
                  <select
                    class="w-full rounded-xl border border-slate-200 px-3.5 py-2 text-sm outline-none focus:border-indigo-500 bg-white"
                    v-model="form.loaiGiamGia"
                  >
                    <option value="tien_mat">Giảm trực tiếp (VNĐ)</option>
                    <option value="phan_tram">Giảm theo phần trăm (%)</option>
                  </select>
                </div>
                <div>
                  <label class="block text-xs font-semibold text-slate-600 mb-1">
                    Giá Trị Giảm {{ form.loaiGiamGia === 'phan_tram' ? '(%)' : '(VNĐ)' }}
                  </label>
                  <input
                    type="number"
                    step="0.01"
                    min="0"
                    :max="form.loaiGiamGia === 'phan_tram' ? 100 : undefined"
                    class="w-full rounded-xl border border-slate-200 px-3.5 py-2 text-sm outline-none focus:border-indigo-500"
                    v-model="form.giaTriGiam"
                    required
                    placeholder="VD: 50000 hoặc 10"
                  />
                </div>
              </div>

              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div v-if="form.loaiGiamGia === 'phan_tram'">
                  <label class="block text-xs font-semibold text-slate-600 mb-1"
                    >Giá Trị Giảm Tối Đa (VNĐ)</label
                  >
                  <input
                    type="number"
                    step="0.01"
                    min="0"
                    class="w-full rounded-xl border border-slate-200 px-3.5 py-2 text-sm outline-none focus:border-indigo-500"
                    v-model="form.giaTriGiamToiDa"
                    placeholder="VD: 100000"
                  />
                </div>
                <div :class="form.loaiGiamGia === 'phan_tram' ? '' : 'md:col-span-2'">
                  <label class="block text-xs font-semibold text-slate-600 mb-1"
                    >Giá Trị Đơn Tối Thiểu (VNĐ)</label
                  >
                  <input
                    type="number"
                    step="0.01"
                    min="0"
                    class="w-full rounded-xl border border-slate-200 px-3.5 py-2 text-sm outline-none focus:border-indigo-500"
                    v-model="form.dieuKienToiThieu"
                    placeholder="VD: 200000"
                  />
                </div>
              </div>

              <div>
                <label class="block text-xs font-semibold text-slate-600 mb-1">Số Xu Cần Đổi</label>
                <input
                  type="number"
                  min="0"
                  class="w-full rounded-xl border border-slate-200 px-3.5 py-2 text-sm outline-none focus:border-indigo-500"
                  v-model="form.soXuDoi"
                  required
                  placeholder="VD: 150"
                />
              </div>

              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label class="block text-xs font-semibold text-slate-600 mb-1"
                    >Ngày Bắt Đầu</label
                  >
                  <input
                    type="datetime-local"
                    class="w-full rounded-xl border border-slate-200 px-3.5 py-2 text-sm outline-none focus:border-indigo-500"
                    v-model="form.ngayBatDau"
                  />
                </div>
                <div>
                  <label class="block text-xs font-semibold text-slate-600 mb-1"
                    >Ngày Hết Hạn</label
                  >
                  <input
                    type="datetime-local"
                    class="w-full rounded-xl border border-slate-200 px-3.5 py-2 text-sm outline-none focus:border-indigo-500"
                    v-model="form.ngayHetHan"
                    required
                  />
                </div>
              </div>

              <div>
                <label class="block text-xs font-semibold text-slate-600 mb-1"
                  >Mô Tả / Thể Lệ</label
                >
                <textarea
                  class="w-full rounded-xl border border-slate-200 px-3.5 py-2 text-sm outline-none focus:border-indigo-500"
                  rows="2"
                  v-model="form.moTa"
                  placeholder="Nhập mô tả chi tiết điều kiện hoặc thể lệ đổi voucher..."
                ></textarea>
              </div>

              <div>
                <label class="block text-xs font-semibold text-slate-600 mb-1">Trạng Thái</label>
                <select
                  class="w-full rounded-xl border border-slate-200 px-3.5 py-2 text-sm outline-none focus:border-indigo-500 bg-white"
                  v-model="form.trangThai"
                >
                  <option :value="true">Đang Hoạt Động</option>
                  <option :value="false">Ngừng hoạt động</option>
                </select>
              </div>

              <div class="flex justify-end gap-2 pt-4 border-t">
                <button
                  type="button"
                  @click="showModal = false"
                  class="px-4 py-2.5 bg-slate-100 hover:bg-slate-200 text-slate-600 rounded-xl text-xs font-bold transition"
                >
                  HỦY BỎ
                </button>
                <button
                  type="submit"
                  class="px-5 py-2.5 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl text-xs font-bold shadow-md shadow-indigo-600/10 transition"
                >
                  LƯU LẠI
                </button>
              </div>
            </form>
          </div>
        </div>
      </Teleport>

      <!-- CONFIRM DELETE MODAL -->
      <Teleport to="body">
        <div
          v-if="confirmModal.show"
          class="fixed inset-0 z-[999] flex items-center justify-center p-4"
        >
          <div
            class="absolute inset-0 bg-black/30 backdrop-blur-xs"
            @click="closeConfirmModal"
          ></div>
          <div
            class="bg-white border border-slate-200 rounded-3xl shadow-2xl w-full max-w-sm p-6 z-10 animate-scale-up text-center relative overflow-hidden"
          >
            <div
              class="w-12 h-12 rounded-full flex items-center justify-center mx-auto mb-4 text-white font-black text-lg shadow-sm bg-rose-500 shadow-rose-500/10"
            >
              {{ confirmModal.icon }}
            </div>
            <span
              class="inline-block px-2 py-0.5 bg-slate-50 text-slate-400 border border-slate-200 rounded-lg text-[10px] uppercase font-bold tracking-wider mb-2"
            >
              {{ confirmModal.label }}
            </span>
            <h3 class="text-sm font-bold text-slate-800 mb-1.5 uppercase tracking-tight">
              {{ confirmModal.title }}
            </h3>
            <p class="text-[11px] text-slate-500 px-2 mb-4">
              {{ confirmModal.message }}
            </p>

            <div
              v-if="confirmModal.code"
              class="inline-block px-3 py-1 bg-slate-50 border border-slate-200 rounded-xl font-mono text-xs font-bold text-slate-700 uppercase tracking-wide mb-6"
            >
              {{ confirmModal.code }}
            </div>

            <div class="flex gap-2">
              <button
                @click="closeConfirmModal"
                class="flex-1 py-2.5 bg-slate-100 hover:bg-slate-200 text-slate-600 rounded-2xl text-xs font-bold transition-all"
              >
                HỦY BỎ
              </button>
              <button
                @click="handleConfirmAction"
                class="flex-1 py-2.5 text-white rounded-2xl text-xs font-bold transition-all shadow-md bg-rose-600 hover:bg-rose-700 shadow-rose-600/10"
              >
                {{ confirmModal.confirmText }}
              </button>
            </div>
          </div>
        </div>
      </Teleport>
    </div>
  </div>
</template>

<style scoped>
@keyframes scaleUp {
  from {
    opacity: 0;
    transform: scale(0.97);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}
.animate-scale-up {
  animation: scaleUp 0.22s cubic-bezier(0.16, 1, 0.3, 1) forwards;
}
</style>
