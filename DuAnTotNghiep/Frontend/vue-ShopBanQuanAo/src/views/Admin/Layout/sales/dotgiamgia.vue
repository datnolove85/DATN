<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import ThemSanPhamGiamGiaModal from '@/views/Admin/Layout/sales/ThemSanPhamGiamGiaModal.vue'
import ThemDotGiamGiaModal from '@/views/Admin/Layout/sales/ThemDotGiamGiaModal.vue'
import QuanLySanPhamGiamGia from '@/views/Admin/Layout/sales/QuanLySanPhamTrongDot.vue'
import {
  getAllDotGiamGia,
  getSanPhamChuaApDung,
  createDotGiamGia,
  updateDotGiamGia,
  deleteDotGiamGia,
  doiTrangThaiDotGiamGia,
} from '@/service/DotGiamGiaService'

const toast = useToast()
const router = useRouter()

const showModal = ref(false)
const firstLoading = ref(true)
const fromDate = ref('')
const toDate = ref('')

const filteredData = computed(() => {
  return danhSachDot.value.filter((dot) => {
    const keyword = search.value.trim().toLowerCase()

    const matchKeyword =
      !keyword ||
      dot.tenDotGiamGia?.toLowerCase().includes(keyword) ||
      dot.maDotGiamGia?.toLowerCase().includes(keyword)

    const matchStatus = filterStatus.value === 'all' || dot.trangThai === filterStatus.value

    let matchDate = true

    if (fromDate.value && toDate.value) {
      const filterStart = new Date(fromDate.value)
      const filterEnd = new Date(toDate.value)
      filterEnd.setHours(23, 59, 59, 999)

      const dotStart = new Date(dot.ngayBatDau)
      const dotEnd = new Date(dot.ngayKetThuc)

      matchDate = dotStart <= filterEnd && dotEnd >= filterStart
    }

    return matchKeyword && matchStatus && matchDate
  })
})

const resetFilter = () => {
  search.value = ''
  filterStatus.value = 'all'
  fromDate.value = ''
  toDate.value = ''
}

const showEditModal = ref(false)
const dotEdit = ref(null)
const isEdit = ref(false)
const form = ref({})
const showAddModal = ref(false)

// Confirm Modal State
const confirmModal = ref({
  show: false,
  icon: '✕',
  label: 'Xác nhận xóa',
  title: 'Xóa đợt giảm giá?',
  message: '',
  code: '',
  confirmText: 'Xóa đợt giảm',
  payload: null,
})

const openCreate = () => {
  isEdit.value = false
  form.value = {
    tenDotGiamGia: '',
    loaiGiamGia: 'phan_tram',
    giaTriGiam: 0,
    giaTriGiamToiDa: null,
    ngayBatDau: '',
    ngayKetThuc: '',
  }
  showModal.value = true
}

const suaDotGiamGia = (dot) => {
  dotEdit.value = dot
  showEditModal.value = true
}

const capNhatDotGiamGia = async (payload) => {
  try {
    await updateDotGiamGia(dotEdit.value.id, payload)
    toast.success('Cập nhật đợt giảm giá thành công!')
    showEditModal.value = false
    await loadData()
  } catch (e) {
    toast.error(e.message || 'Cập nhật đợt giảm giá thất bại')
  }
}

const openDeleteConfirm = (dot) => {
  confirmModal.value = {
    show: true,
    icon: '✕',
    label: 'Xác nhận xóa',
    title: 'Xóa đợt giảm giá?',
    message: `Bạn có chắc chắn muốn xóa đợt giảm giá "${dot.tenDotGiamGia}" không?`,
    code: dot.maDotGiamGia || '',
    confirmText: 'Xóa đợt giảm',
    payload: dot,
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
    const res = await deleteDotGiamGia(payload.id)

    toast.success(res.message || 'Xóa đợt giảm giá thành công!')
    await loadData()
  } catch (e) {
    toast.error(e.message || 'Xóa đợt giảm giá thất bại')
  }
}

// DATA
const loading = ref(false)
const danhSachDot = ref([])
const danhSachSanPham = ref([])
const search = ref('')
const filterStatus = ref('all')
const showProductModal = ref(false)
const dotSelected = ref(null)

// PANEL QUẢN LÝ SẢN PHẨM
const selectedDot = ref(null)
const showProductManager = ref(false)

const quanLySanPham = (dot) => {
  if (selectedDot.value?.id === dot.id && showProductManager.value) {
    showProductManager.value = false
    selectedDot.value = null
    return
  }

  selectedDot.value = dot
  showProductManager.value = true

  nextTick(() => {
    document.getElementById('product-manager')?.scrollIntoView({
      behavior: 'smooth',
      block: 'start',
    })
  })
}

const themDotGiamGia = async (payload) => {
  try {
    await createDotGiamGia(payload)
    toast.success('Thêm đợt giảm giá thành công!')
    showAddModal.value = false
    await loadData()
  } catch (e) {
    toast.error(e.message || 'Thêm đợt giảm giá thất bại')
  }
}

// LOAD DATA
const loadData = async () => {
  try {
    if (firstLoading.value) {
      loading.value = true
    }
    danhSachDot.value = await getAllDotGiamGia()
  } catch (e) {
    toast.error('Không thể tải danh sách đợt giảm giá')
  } finally {
    loading.value = false
    firstLoading.value = false
  }
}

const doiTrangThai = async (dot) => {
  try {
    await doiTrangThaiDotGiamGia(dot.id)
    toast.success('Thay đổi trạng thái đợt giảm giá thành công!')
    await loadData()
  } catch (e) {
    toast.error(e.message || 'Không thể đổi trạng thái')
  }
}

onMounted(() => {
  loadData()
})

// FORMAT
const formatDate = (date) => {
  if (!date) return '--'
  return new Date(date).toLocaleDateString('vi-VN')
}

const money = (value) => {
  return Number(value || 0).toLocaleString('vi-VN')
}

const formatDiscount = (dot) => {
  if (dot.loaiGiamGia === 'phan_tram') {
    return `${dot.giaTriGiam}%`
  }
  if (dot.loaiGiamGia === 'tien_mat') {
    return `${money(dot.giaTriGiam)} đ`
  }
  return ''
}

// STATUS
const formatStatus = (status) => {
  const map = {
    dang_dien_ra: 'Đang diễn ra',
    sap_dien_ra: 'Sắp diễn ra',
    tam_dung: 'Tạm dừng',
    da_ket_thuc: 'Đã kết thúc',
  }
  return map[status] || status
}

const statusStyle = (status) => {
  const map = {
    dang_dien_ra: 'bg-green-100 text-green-700 border border-green-200',
    sap_dien_ra: 'bg-amber-100 text-amber-700 border border-amber-200',
    tam_dung: 'bg-red-100 text-red-700 border border-red-200',
    da_ket_thuc: 'bg-slate-100 text-slate-600 border border-slate-200',
  }
  return map[status]
}

// THỐNG KÊ
const tongDangDienRa = computed(
  () => danhSachDot.value.filter((x) => x.trangThai === 'dang_dien_ra').length,
)
const tongSapDienRa = computed(
  () => danhSachDot.value.filter((x) => x.trangThai === 'sap_dien_ra').length,
)
const tongTamDung = computed(
  () => danhSachDot.value.filter((x) => x.trangThai === 'tam_dung').length,
)
const tongKetThuc = computed(
  () => danhSachDot.value.filter((x) => x.trangThai === 'da_ket_thuc').length,
)

// ACTION
const openProduct = async (dot) => {
  try {
    loading.value = true
    dotSelected.value = dot.id
    danhSachSanPham.value = await getSanPhamChuaApDung(dot.id)
    showProductModal.value = true
  } catch (e) {
    toast.error(e.message || 'Không thể tải danh sách sản phẩm')
  } finally {
    loading.value = false
  }
}
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
            📦
          </div>
          <div>
            <h1 class="text-xl font-bold text-slate-800">Quản lý đợt giảm giá</h1>
            <p class="text-slate-500 text-xs">
              Quản lý toàn bộ chương trình khuyến mãi của cửa hàng.
            </p>
          </div>
        </div>

        <button
          @click="showAddModal = true"
          class="flex items-center gap-2 rounded-xl bg-indigo-600 px-4 py-2.5 font-medium text-white text-sm shadow-sm hover:bg-indigo-700 transition"
        >
          <span class="text-lg leading-none">+</span>
          <span>Thêm đợt giảm giá</span>
        </button>
      </div>

      <!-- STATISTICS -->
      <div class="grid grid-cols-2 md:grid-cols-5 gap-4">
        <div class="rounded-2xl bg-white border border-slate-200 p-4 shadow-sm">
          <div class="flex justify-between items-center">
            <div>
              <p class="text-slate-500 text-xs font-medium">Tổng đợt</p>
              <h2 class="mt-1 text-2xl font-bold text-slate-800">{{ danhSachDot.length }}</h2>
            </div>
            <div class="w-10 h-10 rounded-xl bg-indigo-50 flex items-center justify-center text-lg">
              📦
            </div>
          </div>
        </div>

        <div class="rounded-2xl bg-white border border-slate-200 p-4 shadow-sm">
          <div class="flex justify-between items-center">
            <div>
              <p class="text-slate-500 text-xs font-medium">Đang diễn ra</p>
              <h2 class="mt-1 text-2xl font-bold text-green-600">{{ tongDangDienRa }}</h2>
            </div>
            <div class="w-10 h-10 rounded-xl bg-green-50 flex items-center justify-center text-lg">
              🟢
            </div>
          </div>
        </div>

        <div class="rounded-2xl bg-white border border-slate-200 p-4 shadow-sm">
          <div class="flex justify-between items-center">
            <div>
              <p class="text-slate-500 text-xs font-medium">Sắp diễn ra</p>
              <h2 class="mt-1 text-2xl font-bold text-amber-500">{{ tongSapDienRa }}</h2>
            </div>
            <div class="w-10 h-10 rounded-xl bg-amber-50 flex items-center justify-center text-lg">
              🕒
            </div>
          </div>
        </div>

        <div class="rounded-2xl bg-white border border-slate-200 p-4 shadow-sm">
          <div class="flex justify-between items-center">
            <div>
              <p class="text-slate-500 text-xs font-medium">Tạm dừng</p>
              <h2 class="mt-1 text-2xl font-bold text-red-500">{{ tongTamDung }}</h2>
            </div>
            <div class="w-10 h-10 rounded-xl bg-red-50 flex items-center justify-center text-lg">
              ⏸
            </div>
          </div>
        </div>

        <div
          class="rounded-2xl bg-white border border-slate-200 p-4 shadow-sm col-span-2 md:col-span-1"
        >
          <div class="flex justify-between items-center">
            <div>
              <p class="text-slate-500 text-xs font-medium">Đã kết thúc</p>
              <h2 class="mt-1 text-2xl font-bold text-slate-600">{{ tongKetThuc }}</h2>
            </div>
            <div class="w-10 h-10 rounded-xl bg-slate-100 flex items-center justify-center text-lg">
              ✅
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
              placeholder="Tìm kiếm theo tên hoặc mã đợt giảm giá..."
              class="w-full rounded-xl border border-slate-200 bg-slate-50/50 pl-10 pr-4 py-2.5 text-sm text-slate-700 outline-none focus:bg-white focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500"
            />
          </div>

          <div class="flex flex-wrap items-center gap-3 w-full lg:w-auto">
            <div class="flex items-center gap-2">
              <span class="text-xs text-slate-500">Từ:</span>
              <input
                v-model="fromDate"
                type="date"
                class="rounded-xl border border-slate-200 bg-slate-50/50 px-3 py-2 text-sm text-slate-700 outline-none focus:border-indigo-500"
              />
            </div>
            <div class="flex items-center gap-2">
              <span class="text-xs text-slate-500">Đến:</span>
              <input
                v-model="toDate"
                type="date"
                class="rounded-xl border border-slate-200 bg-slate-50/50 px-3 py-2 text-sm text-slate-700 outline-none focus:border-indigo-500"
              />
            </div>
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
              filterStatus == 'all'
                ? 'bg-indigo-600 text-white'
                : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
            "
          >
            Tất cả
          </button>
          <button
            @click="filterStatus = 'dang_dien_ra'"
            class="px-3 py-1.5 rounded-xl text-xs font-medium transition"
            :class="
              filterStatus == 'dang_dien_ra'
                ? 'bg-green-600 text-white'
                : 'bg-green-50 text-green-700 hover:bg-green-100'
            "
          >
            Đang diễn ra
          </button>
          <button
            @click="filterStatus = 'sap_dien_ra'"
            class="px-3 py-1.5 rounded-xl text-xs font-medium transition"
            :class="
              filterStatus == 'sap_dien_ra'
                ? 'bg-amber-500 text-white'
                : 'bg-amber-50 text-amber-700 hover:bg-amber-100'
            "
          >
            Sắp diễn ra
          </button>
          <button
            @click="filterStatus = 'tam_dung'"
            class="px-3 py-1.5 rounded-xl text-xs font-medium transition"
            :class="
              filterStatus == 'tam_dung'
                ? 'bg-red-600 text-white'
                : 'bg-red-50 text-red-700 hover:bg-red-100'
            "
          >
            Tạm dừng
          </button>
          <button
            @click="filterStatus = 'da_ket_thuc'"
            class="px-3 py-1.5 rounded-xl text-xs font-medium transition"
            :class="
              filterStatus == 'da_ket_thuc'
                ? 'bg-slate-700 text-white'
                : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
            "
          >
            Đã kết thúc
          </button>
        </div>
      </div>

      <!-- TABLE -->
      <div class="rounded-2xl border border-slate-200 bg-white shadow-sm overflow-hidden">
        <div
          class="flex items-center justify-between border-b border-slate-200 px-6 py-4 bg-slate-50/50"
        >
          <h2 class="text-base font-bold text-slate-800">Danh sách đợt giảm giá</h2>
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
                <th class="px-4 py-3">Mã & Tên đợt giảm giá</th>
                <th class="px-4 py-3 text-center">Loại giảm</th>
                <th class="px-4 py-3 text-center">Mức giảm</th>
                <th class="px-4 py-3 text-center">Giảm tối đa</th>
                <th class="px-4 py-3 text-center">Ngày bắt đầu</th>
                <th class="px-4 py-3 text-center">Ngày kết thúc</th>
                <th class="px-4 py-3 text-center">Trạng thái</th>
                <th class="px-4 py-3 text-center">Hành động</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-slate-100 text-xs">
              <tr
                v-for="(dot, index) in filteredData"
                :key="dot.id"
                class="hover:bg-slate-50 transition-colors"
              >
                <td class="px-4 py-3.5 text-center font-mono text-slate-400">
                  {{ index + 1 }}
                </td>

                <td class="px-4 py-3.5">
                  <div class="space-y-0.5">
                    <span
                      class="font-mono text-[11px] font-semibold text-indigo-600 bg-indigo-50 px-1.5 py-0.5 rounded border border-indigo-100"
                    >
                      {{ dot.maDotGiamGia }}
                    </span>
                    <div class="font-medium text-slate-800 text-sm mt-1">
                      {{ dot.tenDotGiamGia }}
                    </div>
                  </div>
                </td>

                <td class="px-4 py-3.5 text-center">
                  <span
                    v-if="dot.loaiGiamGia === 'phan_tram'"
                    class="inline-flex items-center px-2 py-0.5 rounded text-[11px] font-medium bg-blue-50 text-blue-700 border border-blue-100"
                  >
                    Phần trăm (%)
                  </span>
                  <span
                    v-else
                    class="inline-flex items-center px-2 py-0.5 rounded text-[11px] font-medium bg-emerald-50 text-emerald-700 border border-emerald-100"
                  >
                    Tiền mặt (VNĐ)
                  </span>
                </td>

                <td class="px-4 py-3.5 text-center">
                  <span class="font-bold text-slate-900 text-sm">
                    {{ formatDiscount(dot) }}
                  </span>
                </td>

                <!-- Giảm tối đa định dạng abc.def (ví dụ: 100.000 đ) -->
                <td class="px-4 py-3.5 text-center">
                  <span v-if="dot.giaTriGiamToiDa" class="font-medium text-slate-700">
                    {{ money(dot.giaTriGiamToiDa) }} đ
                  </span>
                  <span v-else class="text-slate-400 italic">Không giới hạn</span>
                </td>

                <td class="px-4 py-3.5 text-center font-medium text-slate-700 whitespace-nowrap">
                  {{ formatDate(dot.ngayBatDau) }}
                </td>

                <td class="px-4 py-3.5 text-center font-medium text-slate-700 whitespace-nowrap">
                  {{ formatDate(dot.ngayKetThuc) }}
                </td>

                <td class="px-4 py-3.5 text-center">
                  <span
                    class="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-[11px] font-medium border"
                    :class="statusStyle(dot.trangThai)"
                  >
                    <span class="h-1.5 w-1.5 rounded-full bg-current"></span>
                    {{ formatStatus(dot.trangThai) }}
                  </span>
                </td>

                <td class="px-4 py-3.5 text-center">
                  <div class="flex items-center justify-center gap-1.5 flex-wrap">
                    <!-- Quản lý sản phẩm -->
                    <button
                      @click="quanLySanPham(dot)"
                      class="px-2.5 py-1 rounded-lg bg-slate-100 hover:bg-slate-800 hover:text-white text-slate-700 font-medium transition text-[11px]"
                    >
                      Sản phẩm
                    </button>

                    <!-- Sửa -->
                    <button
                      v-if="
                        dot.trangThai === 'sap_dien_ra' ||
                        (dot.trangThai === 'dang_dien_ra' && dot.tongSanPham === 0) ||
                        dot.trangThai === 'tam_dung'
                      "
                      @click="suaDotGiamGia(dot)"
                      class="px-2.5 py-1 rounded-lg bg-amber-50 hover:bg-amber-500 hover:text-white text-amber-700 font-medium transition border border-amber-200 text-[11px]"
                    >
                      Sửa
                    </button>

                    <!-- Xóa -->
                    <button
                      v-if="dot.trangThai === 'sap_dien_ra' && dot.tongSanPham === 0"
                      @click="openDeleteConfirm(dot)"
                      class="px-2.5 py-1 rounded-lg bg-red-50 hover:bg-red-600 hover:text-white text-red-700 font-medium transition border border-red-200 text-[11px]"
                    >
                      Xóa
                    </button>

                    <!-- Tạm dừng -->
                    <button
                      v-if="dot.trangThai === 'dang_dien_ra'"
                      @click="doiTrangThai(dot)"
                      class="px-2.5 py-1 rounded-lg bg-orange-50 hover:bg-orange-500 hover:text-white text-orange-700 font-medium transition border border-orange-200 text-[11px]"
                    >
                      Tạm dừng
                    </button>

                    <!-- Tiếp tục -->
                    <button
                      v-if="dot.trangThai === 'tam_dung'"
                      @click="doiTrangThai(dot)"
                      class="px-2.5 py-1 rounded-lg bg-emerald-50 hover:bg-emerald-600 hover:text-white text-emerald-700 font-medium transition border border-emerald-200 text-[11px]"
                    >
                      Tiếp tục
                    </button>
                  </div>
                </td>
              </tr>

              <tr v-if="filteredData.length === 0">
                <td colspan="9" class="py-12 text-center text-slate-400">
                  <div class="text-3xl mb-1">📭</div>
                  <p class="text-xs">Không tìm thấy dữ liệu phù hợp trong hệ thống</p>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- MODALS & OVERLAYS -->
      <Teleport to="body">
        <Transition
          enter-active-class="transition duration-200 ease-out"
          enter-from-class="opacity-0"
          enter-to-class="opacity-100"
          leave-active-class="transition duration-150 ease-in"
          leave-from-class="opacity-100"
          leave-to-class="opacity-0"
        >
          <div
            v-if="showProductManager"
            class="fixed inset-0 z-[100] flex justify-end bg-black/30 backdrop-blur-xs"
          >
            <div
              class="h-full w-full md:w-[70vw] lg:w-[60vw] bg-white shadow-2xl flex flex-col border-l border-slate-200"
            >
              <div class="flex items-center justify-between px-6 py-4 border-b bg-slate-50">
                <div>
                  <h2 class="text-lg font-bold text-slate-800">{{ selectedDot?.tenDotGiamGia }}</h2>
                  <p class="text-xs text-slate-500">Quản lý sản phẩm trong đợt giảm giá</p>
                </div>
                <button
                  @click="showProductManager = false"
                  class="h-8 w-8 rounded-lg bg-slate-200 text-slate-600 hover:bg-slate-300 font-bold flex items-center justify-center"
                >
                  ✕
                </button>
              </div>
              <div class="flex-1 overflow-auto p-4">
                <QuanLySanPhamGiamGia :dot="selectedDot" @close="showProductManager = false" />
              </div>
            </div>
          </div>
        </Transition>
      </Teleport>

      <!-- CONFIRM MODAL -->
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

      <ThemSanPhamGiamGiaModal
        :show="showProductModal"
        :idDot="dotSelected"
        :danhSachSanPham="danhSachSanPham"
        @close="showProductModal = false"
        @success="loadData"
      />

      <ThemDotGiamGiaModal
        :show="showAddModal"
        :isEdit="false"
        @close="showAddModal = false"
        @success="themDotGiamGia"
      />

      <ThemDotGiamGiaModal
        :show="showEditModal"
        :isEdit="true"
        :dataEdit="dotEdit"
        @close="showEditModal = false"
        @success="capNhatDotGiamGia"
      />
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
