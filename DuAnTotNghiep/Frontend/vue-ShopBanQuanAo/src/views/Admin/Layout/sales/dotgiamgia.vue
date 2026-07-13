<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import ThemSanPhamGiamGiaModal from '@/views/Admin/Layout/sales/ThemSanPhamGiamGiaModal.vue'
import { useRouter } from 'vue-router'
import { getAllDotGiamGia, getSanPhamChuaApDung } from '@/service/DotGiamGiaService'
import { createDotGiamGia } from '@/service/DotGiamGiaService'
import ThemDotGiamGiaModal from '@/views/Admin/Layout/sales/ThemDotGiamGiaModal.vue'
import { updateDotGiamGia } from '@/service/DotGiamGiaService'
import { deleteDotGiamGia } from '@/service/DotGiamGiaService'
import { doiTrangThaiDotGiamGia } from '@/service/DotGiamGiaService'
import QuanLySanPhamGiamGia from '@/views/Admin/Layout/sales/QuanLySanPhamTrongDot.vue'
const showModal = ref(false)
const firstLoading = ref(true)
const fromDate = ref('')
const toDate = ref('')
const filteredData = computed(() => {
  return danhSachDot.value.filter((dot) => {
    // tìm kiếm
    const keyword = search.value.trim().toLowerCase()

    const matchKeyword =
      !keyword ||
      dot.tenDotGiamGia?.toLowerCase().includes(keyword) ||
      dot.maDotGiamGia?.toLowerCase().includes(keyword)

    // trạng thái
    const matchStatus = filterStatus.value === 'all' || dot.trangThai === filterStatus.value

    // ngày bắt đầu
    let matchDate = true

    if (fromDate.value && toDate.value) {
      const filterStart = new Date(fromDate.value)
      const filterEnd = new Date(toDate.value)
      filterEnd.setHours(23, 59, 59, 999)

      const dotStart = new Date(dot.ngayBatDau)
      const dotEnd = new Date(dot.ngayKetThuc)

      // Hai khoảng thời gian có giao nhau
      matchDate = dotStart <= filterEnd && dotEnd >= filterStart
    }

    return matchKeyword && matchStatus && matchDate
  })
})

// Cập nhật hàm refresh
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

    showEditModal.value = false

    await loadData()
  } catch (e) {
    console.log(e)
  }
}

const xoaDot = async (dot) => {
  const ok = confirm(`Bạn có chắc muốn xóa ${dot.tenDotGiamGia}?`)

  if (!ok) return

  try {
    await deleteDotGiamGia(dot.id)

    await loadData()
  } catch (e) {
    console.log(e)
  }
}

// =======================
// DATA
// =======================

const loading = ref(false)

const danhSachDot = ref([])

const danhSachSanPham = ref([])

const search = ref('')

const filterStatus = ref('all')

const showProductModal = ref(false)

const dotSelected = ref(null)

// =======================
// PANEL QUẢN LÝ SẢN PHẨM
// =======================

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
    console.log(payload)
    await createDotGiamGia(payload)

    showAddModal.value = false

    await loadData()
  } catch (e) {
    console.log(e)
  }
}
// =======================
// LOAD DATA
// =======================

const loadData = async () => {
  try {
    if (firstLoading.value) {
      loading.value = true
    }

    danhSachDot.value = await getAllDotGiamGia()
  } finally {
    loading.value = false
    firstLoading.value = false
  }
}
const doiTrangThai = async (dot) => {
  try {
    await doiTrangThaiDotGiamGia(dot.id)

    await loadData()
  } catch (e) {
    console.log(e)

    alert(e.message || 'Không thể đổi trạng thái')
  }
}

onMounted(() => {
  loadData()
})

// =======================
// FORMAT
// =======================

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

// =======================
// STATUS
// =======================

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

// =======================
// THỐNG KÊ
// =======================

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

// =======================
// FILTER
// =======================

// =======================
// ACTION
// =======================

const openProduct = async (dot) => {
  try {
    loading.value = true

    dotSelected.value = dot.id

    danhSachSanPham.value = await getSanPhamChuaApDung(dot.id)

    showProductModal.value = true
  } catch (e) {
    console.log(e)
  } finally {
    loading.value = false
  }
}
</script>
<template>
  <div class="min-h-screen bg-gradient-to-br from-slate-100 via-slate-50 to-blue-50 p-8">
    <!-- Background decoration -->
    <div class="fixed inset-0 pointer-events-none overflow-hidden">
      <div
        class="absolute -top-40 -left-40 w-96 h-96 bg-indigo-300/20 rounded-full blur-3xl animate-pulse"
      ></div>

      <div
        class="absolute bottom-0 right-0 w-[500px] h-[500px] bg-cyan-300/20 rounded-full blur-3xl animate-pulse"
      ></div>
    </div>

    <div class="relative z-10">
      <!-- HEADER -->

      <div
        class="overflow-hidden rounded-3xl bg-gradient-to-r from-indigo-700 via-indigo-600 to-blue-600 shadow-2xl"
      >
        <div class="flex justify-between items-center px-10 py-8">
          <div>
            <div class="flex items-center gap-4">
              <div
                class="w-16 h-16 rounded-2xl bg-white/20 backdrop-blur-lg flex items-center justify-center text-3xl shadow-lg"
              >
                🎉
              </div>

              <div>
                <h1 class="text-4xl font-extrabold text-white tracking-wide">
                  Quản lý đợt giảm giá
                </h1>

                <p class="text-indigo-100 mt-2 text-lg">
                  Quản lý toàn bộ chương trình khuyến mãi của cửa hàng.
                </p>
              </div>
            </div>
          </div>

          <button
            @click="showAddModal = true"
            class="group flex items-center gap-3 rounded-2xl bg-white px-6 py-4 font-semibold text-indigo-700 shadow-xl transition-all duration-300 hover:-translate-y-1 hover:scale-105 hover:shadow-2xl"
          >
            <span
              class="flex h-10 w-10 items-center justify-center rounded-xl bg-indigo-600 text-white transition group-hover:rotate-90"
            >
              +
            </span>

            <span> Thêm đợt giảm giá </span>
          </button>
        </div>
      </div>

      <!-- Statistic -->

      <div class="grid grid-cols-5 gap-6 mt-8">
        <!-- Card 1 -->

        <div
          class="group rounded-3xl bg-white/90 backdrop-blur-xl border border-white shadow-xl p-6 transition-all duration-300 hover:-translate-y-2 hover:shadow-2xl"
        >
          <div class="flex justify-between items-center">
            <div>
              <p class="text-slate-500 font-medium">Tổng đợt</p>

              <h2 class="mt-3 text-4xl font-black text-slate-800">
                {{ danhSachDot.length }}
              </h2>
            </div>

            <div
              class="w-16 h-16 rounded-2xl bg-indigo-100 flex items-center justify-center text-3xl group-hover:rotate-12 transition"
            >
              📦
            </div>
          </div>
        </div>

        <!-- Card 2 -->

        <div
          class="group rounded-3xl bg-white border shadow-xl p-6 transition-all duration-300 hover:-translate-y-2 hover:shadow-2xl"
        >
          <div class="flex justify-between">
            <div>
              <p class="text-slate-500">Đang diễn ra</p>

              <h2 class="mt-3 text-4xl font-black text-green-600">
                {{ tongDangDienRa }}
              </h2>
            </div>

            <div
              class="w-16 h-16 rounded-2xl bg-green-100 flex items-center justify-center text-3xl"
            >
              🟢
            </div>
          </div>
        </div>

        <!-- Card 3 -->

        <div
          class="group rounded-3xl bg-white border shadow-xl p-6 transition-all duration-300 hover:-translate-y-2 hover:shadow-2xl"
        >
          <div class="flex justify-between">
            <div>
              <p class="text-slate-500">Sắp diễn ra</p>

              <h2 class="mt-3 text-4xl font-black text-amber-500">
                {{ tongSapDienRa }}
              </h2>
            </div>

            <div
              class="w-16 h-16 rounded-2xl bg-yellow-100 flex items-center justify-center text-3xl"
            >
              🕒
            </div>
          </div>
        </div>

        <!-- Card 4 -->

        <div
          class="group rounded-3xl bg-white border shadow-xl p-6 transition-all duration-300 hover:-translate-y-2 hover:shadow-2xl"
        >
          <div class="flex justify-between">
            <div>
              <p class="text-slate-500">Tạm dừng</p>

              <h2 class="mt-3 text-4xl font-black text-red-500">
                {{ tongTamDung }}
              </h2>
            </div>

            <div class="w-16 h-16 rounded-2xl bg-red-100 flex items-center justify-center text-3xl">
              ⏸
            </div>
          </div>
        </div>

        <!-- Card 5 -->

        <div
          class="group rounded-3xl bg-white border shadow-xl p-6 transition-all duration-300 hover:-translate-y-2 hover:shadow-2xl"
        >
          <div class="flex justify-between">
            <div>
              <p class="text-slate-500">Đã kết thúc</p>

              <h2 class="mt-3 text-4xl font-black text-slate-600">
                {{ tongKetThuc }}
              </h2>
            </div>

            <div
              class="w-16 h-16 rounded-2xl bg-slate-200 flex items-center justify-center text-3xl"
            >
              ✅
            </div>
          </div>
        </div>
      </div>
      <!-- HEADER -->

      <!-- FILTER -->

      <!-- ================= FILTER ================= -->

      <div
        class="mt-8 rounded-3xl border border-white/60 bg-white/80 backdrop-blur-xl shadow-xl p-6"
      >
        <div class="flex items-center justify-between mb-6">
          <div>
            <h2 class="text-xl font-bold text-slate-800">Bộ lọc dữ liệu</h2>

            <p class="text-slate-500 mt-1">Tìm kiếm và lọc nhanh các đợt giảm giá.</p>
          </div>

          <div class="hidden lg:flex items-center gap-2 text-sm text-slate-500">
            <span class="w-2 h-2 rounded-full bg-green-500 animate-pulse"></span>

            {{ filteredData.length }} kết quả
          </div>
        </div>

        <div class="grid grid-cols-12 gap-4 items-end">
          <!-- SEARCH -->

          <div class="col-span-12 lg:col-span-5">
            <div class="relative group">
              <svg
                class="absolute left-5 top-1/2 -translate-y-1/2 w-5 h-5 text-slate-400 group-focus-within:text-indigo-600 transition"
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
                class="w-full rounded-2xl border border-slate-200 bg-slate-50 pl-14 pr-5 py-4 text-slate-700 shadow-sm transition-all duration-300 outline-none focus:bg-white focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100 hover:border-indigo-300"
              />
            </div>
          </div>

          <!-- Từ ngày -->

          <div class="col-span-6 lg:col-span-2">
            <label class="text-sm text-slate-500 mb-2 block"> Từ ngày </label>

            <input
              v-model="fromDate"
              type="date"
              class="w-full rounded-2xl border border-slate-200 bg-slate-50 px-4 py-4 focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
            />
          </div>

          <!-- Đến ngày -->

          <div class="col-span-6 lg:col-span-2">
            <label class="text-sm text-slate-500 mb-2 block"> Đến ngày </label>

            <input
              v-model="toDate"
              type="date"
              class="w-full rounded-2xl border border-slate-200 bg-slate-50 px-4 py-4 focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
            />
          </div>
          <!-- STATUS -->

          <!-- BUTTON -->

          <div class="col-span-12 lg:col-span-3 flex gap-3">
            <button
              @click="resetFilter"
              class="group w-full rounded-2xl bg-gradient-to-r from-slate-800 to-slate-700 px-6 py-4 font-semibold text-white shadow-lg transition-all duration-300 hover:-translate-y-1 hover:shadow-xl active:scale-95"
            >
              <span class="flex items-center justify-center gap-2">
                <svg
                  class="w-5 h-5 transition-transform duration-500 group-hover:rotate-180"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2"
                  viewBox="0 0 24 24"
                >
                  <path stroke-linecap="round" stroke-linejoin="round" d="M4 4v6h6M20 20v-6h-6" />

                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    d="M20 9A8 8 0 005.3 5.3L4 6m16 12l-1.3-1.3A8 8 0 018 20"
                  />
                </svg>

                Làm mới
              </span>
            </button>
          </div>
        </div>

        <!-- Quick Filter -->

        <div class="flex flex-wrap gap-3 mt-6">
          <button
            @click="filterStatus = 'all'"
            class="px-4 py-2 rounded-full text-sm font-medium transition-all duration-300"
            :class="
              filterStatus == 'all'
                ? 'bg-indigo-600 text-white shadow-lg'
                : 'bg-slate-100 hover:bg-slate-200'
            "
          >
            Tất cả
          </button>

          <button
            @click="filterStatus = 'dang_dien_ra'"
            class="px-4 py-2 rounded-full text-sm font-medium transition-all duration-300"
            :class="
              filterStatus == 'dang_dien_ra'
                ? 'bg-green-600 text-white shadow-lg'
                : 'bg-green-100 text-green-700 hover:bg-green-200'
            "
          >
            Đang diễn ra
          </button>

          <button
            @click="filterStatus = 'sap_dien_ra'"
            class="px-4 py-2 rounded-full text-sm font-medium transition-all duration-300"
            :class="
              filterStatus == 'sap_dien_ra'
                ? 'bg-yellow-500 text-white shadow-lg'
                : 'bg-yellow-100 text-yellow-700 hover:bg-yellow-200'
            "
          >
            Sắp diễn ra
          </button>

          <button
            @click="filterStatus = 'tam_dung'"
            class="px-4 py-2 rounded-full text-sm font-medium transition-all duration-300"
            :class="
              filterStatus == 'tam_dung'
                ? 'bg-red-600 text-white shadow-lg'
                : 'bg-red-100 text-red-700 hover:bg-red-200'
            "
          >
            Tạm dừng
          </button>

          <button
            @click="filterStatus = 'da_ket_thuc'"
            class="px-4 py-2 rounded-full text-sm font-medium transition-all duration-300"
            :class="
              filterStatus == 'da_ket_thuc'
                ? 'bg-slate-700 text-white shadow-lg'
                : 'bg-slate-100 hover:bg-slate-200'
            "
          >
            Đã kết thúc
          </button>
        </div>
      </div>

      <!-- TABLE -->

      <!-- ========================= TABLE ========================= -->

      <div
        class="mt-8 overflow-hidden rounded-3xl border border-white/70 bg-white/90 backdrop-blur-xl shadow-2xl"
      >
        <!-- Header -->

        <div
          class="flex items-center justify-between border-b border-slate-100 px-8 py-6 bg-gradient-to-r from-white to-slate-50"
        >
          <div>
            <h2 class="text-2xl font-bold text-slate-800">Danh sách đợt giảm giá</h2>

            <p class="mt-1 text-sm text-slate-500">
              Theo dõi và quản lý toàn bộ chương trình khuyến mãi.
            </p>
          </div>

          <div class="flex items-center gap-3">
            <div class="rounded-2xl bg-indigo-50 px-4 py-2 text-sm font-semibold text-indigo-700">
              {{ filteredData.length }}

              đợt
            </div>
          </div>
        </div>

        <!-- Table -->

        <div class="overflow-x-auto">
          <table class="min-w-full border-separate border-spacing-0">
            <thead>
              <tr class="sticky top-0 z-10 bg-slate-50">
                <th
                  class="px-8 py-5 text-left text-xs uppercase tracking-wider font-bold text-slate-500"
                >
                  Đợt giảm giá
                </th>

                <th class="text-center text-xs uppercase tracking-wider font-bold text-slate-500">
                  Loại
                </th>

                <th class="text-center text-xs uppercase tracking-wider font-bold text-slate-500">
                  Giá trị
                </th>

                <th class="text-center text-xs uppercase tracking-wider font-bold text-slate-500">
                  Thời gian
                </th>

                <th class="text-center text-xs uppercase tracking-wider font-bold text-slate-500">
                  Trạng thái
                </th>

                <th class="text-center text-xs uppercase tracking-wider font-bold text-slate-500">
                  Thao tác
                </th>
              </tr>
            </thead>

            <tbody class="divide-y divide-slate-100">
              <!-- DATA -->

              <tr
                v-for="dot in filteredData"
                :key="dot.id"
                class="group transition-all duration-300 hover:bg-indigo-50/40"
              >
                <!-- Đợt giảm giá -->

                <td class="px-8 py-6">
                  <div class="flex items-center gap-4">
                    <div
                      class="flex h-14 w-14 items-center justify-center rounded-2xl bg-gradient-to-br from-indigo-500 to-blue-600 text-white shadow-lg"
                    >
                      🎁
                    </div>

                    <div>
                      <h3 class="font-bold text-slate-800 transition group-hover:text-indigo-600">
                        {{ dot.tenDotGiamGia }}
                      </h3>

                      <p class="mt-1 text-xs text-slate-500">
                        {{ dot.maDotGiamGia }}
                      </p>
                    </div>
                  </div>
                </td>

                <!-- Loại -->

                <td class="text-center">
                  <span
                    v-if="dot.loaiGiamGia === 'phan_tram'"
                    class="rounded-full bg-indigo-100 px-4 py-2 text-xs font-semibold text-indigo-700"
                  >
                    % Phần trăm
                  </span>

                  <span
                    v-else
                    class="rounded-full bg-emerald-100 px-4 py-2 text-xs font-semibold text-emerald-700"
                  >
                    💰 Tiền mặt
                  </span>
                </td>

                <!-- Giá trị -->

                <td class="text-center">
                  <div class="font-bold text-lg text-indigo-600">
                    {{ formatDiscount(dot) }}
                  </div>

                  <div v-if="dot.giaTriGiamToiDa" class="mt-1 text-xs text-slate-400">
                    Tối đa {{ money(dot.giaTriGiamToiDa) }} đ
                  </div>
                </td>

                <!-- Thời gian -->

                <td class="text-center">
                  <div class="inline-flex flex-col rounded-xl bg-slate-50 px-4 py-3">
                    <span class="text-xs text-slate-400">Bắt đầu</span>

                    <span class="font-semibold">
                      {{ formatDate(dot.ngayBatDau) }}
                    </span>

                    <div class="my-2 h-px bg-slate-200"></div>

                    <span class="text-xs text-slate-400">Kết thúc</span>

                    <span class="font-semibold">
                      {{ formatDate(dot.ngayKetThuc) }}
                    </span>
                  </div>
                </td>

                <!-- Trạng thái -->

                <td class="text-center">
                  <span
                    class="inline-flex items-center gap-2 rounded-full px-4 py-2 text-sm font-semibold"
                    :class="statusStyle(dot.trangThai)"
                  >
                    <span class="h-2.5 w-2.5 rounded-full bg-current animate-pulse"></span>

                    {{ formatStatus(dot.trangThai) }}
                  </span>
                </td>

                <!-- Action -->

                <td class="py-6">
                  <div class="flex flex-wrap justify-center gap-2">
                    <button
                      @click="quanLySanPham(dot)"
                      class="rounded-xl bg-slate-700 px-3 py-2 text-sm font-semibold text-white shadow transition hover:-translate-y-1 hover:bg-slate-800"
                    >
                      Quản lý
                    </button>

                    <button
                      v-if="
                        dot.trangThai === 'sap_dien_ra' ||
                        (dot.trangThai === 'dang_dien_ra' && dot.tongSanPham === 0) ||
                        dot.trangThai === 'tam_dung'
                      "
                      @click="suaDotGiamGia(dot)"
                      class="rounded-xl bg-amber-500 px-3 py-2 text-sm font-semibold text-white shadow transition hover:-translate-y-1 hover:bg-amber-600"
                    >
                      ✏
                    </button>

                    <button
                      v-if="dot.trangThai === 'sap_dien_ra' && dot.tongSanPham === 0"
                      @click="xoaDot(dot)"
                      class="rounded-xl bg-red-600 px-3 py-2 text-sm font-semibold text-white shadow transition hover:-translate-y-1 hover:bg-red-700"
                    >
                      🗑
                    </button>

                    <button
                      v-if="dot.trangThai === 'dang_dien_ra'"
                      @click="doiTrangThai(dot)"
                      class="rounded-xl bg-orange-500 px-3 py-2 text-sm font-semibold text-white shadow transition hover:-translate-y-1 hover:bg-orange-600"
                    >
                      Tạm dừng
                    </button>

                    <button
                      v-if="dot.trangThai === 'tam_dung'"
                      @click="doiTrangThai(dot)"
                      class="rounded-xl bg-green-600 px-3 py-2 text-sm font-semibold text-white shadow transition hover:-translate-y-1 hover:bg-green-700"
                    >
                      Tiếp tục
                    </button>
                  </div>
                </td>
              </tr>

              <!-- Empty -->

              <tr v-if="filteredData.length === 0">
                <td colspan="6" class="py-24">
                  <div class="flex flex-col items-center">
                    <div class="mb-4 text-6xl">📭</div>

                    <h3 class="text-xl font-bold text-slate-700">Không tìm thấy dữ liệu</h3>

                    <p class="mt-2 text-slate-400">Hãy thử thay đổi từ khóa hoặc bộ lọc.</p>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <Teleport to="body">
        <Transition
          enter-active-class="transition duration-300 ease-out"
          enter-from-class="opacity-0"
          enter-to-class="opacity-100"
          leave-active-class="transition duration-200 ease-in"
          leave-from-class="opacity-100"
          leave-to-class="opacity-0"
        >
          <div v-if="showProductManager" class="fixed inset-0 z-[100]">
            <!-- Overlay -->
            <div
              class="absolute inset-0 bg-black/40 backdrop-blur-sm"
              @click="showProductManager = false"
            />

            <!-- Drawer -->
            <Transition
              enter-active-class="transition duration-300 ease-out"
              enter-from-class="translate-x-full"
              enter-to-class="translate-x-0"
              leave-active-class="transition duration-200 ease-in"
              leave-from-class="translate-x-0"
              leave-to-class="translate-x-full"
            >
              <div
                class="absolute right-0 top-0 h-full w-full md:w-[90vw] lg:w-[80vw] xl:w-[72vw] 2xl:w-[70vw] bg-slate-100 rounded-l-3xl border-l border-slate-200 shadow-2xl flex flex-col"
              >
                <!-- Header -->
                <div
                  class="flex items-center justify-between px-8 py-6 border-b bg-gradient-to-r from-indigo-700 via-indigo-600 to-blue-600"
                >
                  <div>
                    <h2 class="text-3xl font-bold text-white">
                      {{ selectedDot?.tenDotGiamGia }}
                    </h2>

                    <p class="text-indigo-100 mt-1">Quản lý sản phẩm trong đợt giảm giá</p>
                  </div>

                  <button
                    @click="showProductManager = false"
                    class="h-12 w-12 rounded-xl bg-white/20 hover:bg-white/30 text-white text-xl"
                  >
                    ✕
                  </button>
                </div>

                <!-- Content -->
                <div class="flex-1 overflow-auto">
                  <QuanLySanPhamGiamGia :dot="selectedDot" @close="showProductManager = false" />
                </div>
              </div>
            </Transition>
          </div>
        </Transition>
      </Teleport>

      <ThemSanPhamGiamGiaModal
        :show="showProductModal"
        :idDot="dotSelected"
        :danhSachSanPham="danhSachSanPham"
        @close="showProductModal = false"
        @success="loadData"
      />

      <!-- Modal thêm đợt giảm giá -->
      <ThemDotGiamGiaModal
        :show="showAddModal"
        :isEdit="false"
        @close="showAddModal = false"
        @success="themDotGiamGia"
      />

      <!-- Modal sửa đợt giảm giá -->
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
