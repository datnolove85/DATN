<template>
  <div
    class="space-y-6 max-w-screen-2xl mx-auto px-6 lg:px-10 py-6 animate-fade-in bg-slate-50 min-h-screen"
  >
    <div class="relative p-8 bg-[#0b0f19] rounded-3xl text-white overflow-hidden">
      <div class="relative z-10">
        <span class="text-[10px] tracking-widest uppercase font-bold text-indigo-400"
          >Hệ thống vận hành</span
        >
        <h1 class="text-2xl font-bold tracking-tight mt-1">Quản lý đơn hàng Online</h1>
        <p class="text-xs text-slate-400 mt-1 flex items-center gap-2">
          <span class="relative flex h-2 w-2">
            <span
              class="animate-ping absolute inline-flex h-full w-full rounded-full bg-indigo-400 opacity-75"
            ></span>
            <span class="relative inline-block rounded-full h-2 w-2 bg-indigo-500"></span>
          </span>
          Theo dõi và xử lý đơn hàng của khách hàng thời gian thực.
        </p>
      </div>
    </div>

    <div class="bg-white p-6 rounded-2xl shadow-sm border border-slate-100">
      <div class="flex items-center justify-between mb-5">
        <h3
          class="text-xs font-extrabold uppercase tracking-widest text-indigo-600 flex items-center gap-2"
        >
          <span class="inline-block w-1.5 h-3.5 bg-indigo-600 rounded-sm"></span>
          Bộ lọc dữ liệu
        </h3>

        <button
          @click="resetFilters"
          class="text-xs px-3 py-1.5 rounded-xl border border-slate-200 bg-white hover:bg-slate-50 transition"
        >
          Reset bộ lọc
        </button>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div
          class="relative flex items-center bg-slate-50 border border-slate-200 focus-within:border-indigo-500 rounded-xl px-3 py-1 transition-all"
        >
          <input
            v-model="filters.keyword"
            type="text"
            placeholder="Mã đơn / Tên / SĐT..."
            class="bg-transparent text-slate-800 placeholder-slate-400 text-xs font-semibold outline-none w-full py-2"
          />
        </div>

        <select
          v-model="filters.trangThai"
          class="bg-slate-50 border border-slate-200 focus:border-indigo-500 rounded-xl px-4 py-2.5 text-xs font-bold text-slate-600 outline-none cursor-pointer"
        >
          <option value="">Tất cả trạng thái</option>
          <option value="cho_xac_nhan">Chờ xác nhận</option>
          <option value="da_xac_nhan">Đã xác nhận</option>
          <option value="dang_giao">Đang giao</option>
          <option value="hoan_thanh">Hoàn thành</option>
          <option value="da_huy">Đã hủy</option>
        </select>

        <select
          v-model="filters.thanhToan"
          class="bg-slate-50 border border-slate-200 focus:border-indigo-500 rounded-xl px-4 py-2.5 text-xs font-bold text-slate-600 outline-none cursor-pointer"
        >
          <option value="">Tất cả thanh toán</option>
          <option value="da_thanh_toan">Đã thanh toán</option>
          <option value="chua_thanh_toan">Chưa thanh toán</option>
        </select>

        <select
          v-model="filters.quickDate"
          class="bg-slate-50 border border-slate-200 focus:border-indigo-500 rounded-xl px-4 py-2.5 text-xs font-bold text-slate-600 outline-none cursor-pointer"
        >
          <option value="">Tất cả thời gian</option>
          <option value="today">Hôm nay</option>
          <option value="yesterday">Hôm qua</option>
          <option value="7days">7 ngày gần đây</option>
          <option value="30days">30 ngày gần đây</option>
        </select>
      </div>
    </div>

    <div class="bg-white rounded-2xl border border-slate-100 shadow-sm overflow-visible">
      <table class="w-full text-left border-collapse">
        <thead>
          <tr class="bg-slate-50 text-slate-400 uppercase text-[11px] font-bold tracking-wider">
            <th class="p-4">Mã Đơn</th>
            <th class="p-4">Khách Hàng</th>
            <th class="p-4">Địa Chỉ</th>
            <th class="p-4">Thành Tiền</th>
            <th class="p-4">Trạng Thái</th>
            <th class="p-4 text-center">Thao Tác</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-slate-100">
          <tr v-if="orders.length === 0">
            <td colspan="6" class="p-8 text-center text-slate-400 text-xs">
              Không có đơn hàng nào
            </td>
          </tr>
          <tr v-for="o in orders" :key="o.id" class="hover:bg-slate-50/60 transition group text-xs">
            <td class="p-4 font-bold text-indigo-600 font-mono">{{ o.maHoaDon }}</td>
            <td class="p-4">
              <div class="font-bold text-slate-800 text-sm">{{ o.tenNguoiNhan }}</div>
              <div class="text-[11px] text-slate-400">{{ o.soDienThoaiNguoiNhan }}</div>
            </td>
            <td class="p-4 text-slate-600 truncate max-w-xs">{{ o.diaChiGiaoHang }}</td>
            <td class="p-4 font-extrabold text-slate-900 font-mono">
              {{ o.tongThanhToan?.toLocaleString() }}đ
            </td>
            <td class="p-4">
              <div class="flex flex-col gap-1 items-start">
                <span
                  class="px-2 py-0.5 rounded-lg border font-bold text-[10px]"
                  :class="statusClass(o.trangThai)"
                  >{{ o.trangThai }}</span
                >
                <span
                  class="px-2 py-0.5 rounded-lg border font-bold text-[10px]"
                  :class="paymentClass(o.trangThaiThanhToan)"
                  >{{ o.trangThaiThanhToan }}</span
                >
              </div>
            </td>
            <td class="p-4">
              <div class="flex justify-center gap-2">
                <div class="relative inline-block text-left">
                  <button
                    @click="
                      () => {
                        o.open = !o.open
                      }
                    "
                    class="px-3 py-1.5 rounded-xl bg-slate-900 text-white text-xs font-bold"
                  >
                    Thao tác
                  </button>

                  <div
                    v-if="o.open"
                    class="absolute right-0 mt-2 w-44 bg-white border border-slate-200 rounded-xl shadow-lg overflow-hidden z-50"
                  >
                    <button
                      v-if="canChangeStatus(o.trangThai, 'da_xac_nhan')"
                      @click="handleAction(o, 'da_xac_nhan')"
                      class="w-full text-left px-4 py-2 text-xs hover:bg-indigo-50"
                    >
                      ✔ Xác nhận
                    </button>

                    <button
                      v-if="canChangeStatus(o.trangThai, 'dang_giao')"
                      @click="handleAction(o, 'dang_giao')"
                      class="w-full text-left px-4 py-2 text-xs hover:bg-blue-50"
                    >
                      🚚 Giao hàng
                    </button>

                    <button
                      v-if="canChangeStatus(o.trangThai, 'hoan_thanh')"
                      @click="handleAction(o, 'hoan_thanh')"
                      class="w-full text-left px-4 py-2 text-xs hover:bg-green-50"
                    >
                      ✔ Hoàn thành
                    </button>

                    <button
                      v-if="canChangeStatus(o.trangThai, 'da_huy')"
                      @click="handleAction(o, 'da_huy')"
                      class="w-full text-left px-4 py-2 text-xs hover:bg-red-50 text-red-600"
                    >
                      ✖ Hủy đơn
                    </button>
                  </div>
                </div>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="flex justify-end items-center gap-2" v-if="totalPages > 1">
      <button
        class="px-4 py-2 rounded-xl bg-white border border-slate-200 text-xs font-bold hover:bg-slate-50 disabled:opacity-50"
        :disabled="page === 0"
        @click="goPage(page - 1)"
      >
        Trước
      </button>
      <button
        v-for="p in totalPages"
        :key="p"
        @click="goPage(p - 1)"
        class="px-4 py-2 rounded-xl border text-xs font-bold transition-all"
        :class="
          page === p - 1
            ? 'bg-indigo-600 text-white border-indigo-600'
            : 'bg-white border-slate-200'
        "
      >
        {{ p }}
      </button>
      <button
        class="px-4 py-2 rounded-xl bg-white border border-slate-200 text-xs font-bold hover:bg-slate-50 disabled:opacity-50"
        :disabled="page >= totalPages - 1"
        @click="goPage(page + 1)"
      >
        Sau
      </button>
    </div>

    <OrderDetailDrawer
      v-if="showDrawer"
      :orderId="selectedOrderId"
      @close="showDrawer = false"
      @reload="fetchOrders"
    />
  </div>
</template>
<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import OrderDetailDrawer from './OrderDetailDrawer.vue'
import { searchHoadonOnline, hoaDonService } from '@/service/HoaDonService'
// =======================
// STATE
// =======================
const orders = ref([])
const showDrawer = ref(false)
const selectedOrderId = ref(null)
const page = ref(0)
const size = ref(10)
const totalPages = ref(0)
const goPage = (p) => {
  if (p < 0 || p >= totalPages.value) return
  page.value = p
  fetchOrders()
}
// FILTER
const filters = ref({
  keyword: '',
  trangThai: 'cho_xac_nhan',
  thanhToan: '',
  fromDate: '',
  toDate: '',
  quickDate: '',
})

const resetFilters = () => {
  filters.value = {
    keyword: '',
    trangThai: '',
    thanhToan: '',
    fromDate: '',
    toDate: '',
    quickDate: '',
  }

  page.value = 0
  fetchOrders()
}

// TABS
const tabs = ref([
  { key: '', label: 'Tất cả' },
  { key: 'cho_xac_nhan', label: 'Chờ xác nhận' },
  { key: 'da_xac_nhan', label: 'Đã xác nhận' },
  { key: 'dang_giao', label: 'Đang giao' },
  { key: 'hoan_thanh', label: 'Hoàn thành' },
  { key: 'da_huy', label: 'Đã hủy' },
])

// =======================
// LOAD DATA
const fetchOrders = async () => {
  try {
    const res = await searchHoadonOnline(
      {
        loaiHoaDon: 'online',
        keyword: filters.value.keyword,
        trangThai: filters.value.trangThai,
        trangThaiThanhToan: filters.value.thanhToan,
        fromDate: filters.value.fromDate,
        toDate: filters.value.toDate,
      },
      page.value,
      size.value,
    )

    const list = res?.content || []
    orders.value = list.map((item) => ({
      ...item,
      trangThai: item.trangThai?.toLowerCase(),
      trangThaiThanhToan: item.trangThaiThanhToan?.toLowerCase(),
      open: false,
    }))
    totalPages.value = res?.totalPages || 0
  } catch (err) {
    console.error('Lỗi load hóa đơn:', err)
    orders.value = []
  }
}

watch(
  () => [
    filters.value.keyword,
    filters.value.trangThai,
    filters.value.thanhToan,
    filters.value.quickDate,
  ],
  () => {
    page.value = 0
    fetchOrders()
  },
  { deep: true },
)
watch(
  () => filters.value.quickDate,
  (value) => {
    const today = new Date()
    const format = (d) => d.toISOString().split('T')[0]

    if (!value) {
      filters.value.fromDate = ''
      filters.value.toDate = ''
      return
    }

    switch (value) {
      case 'today':
        filters.value.fromDate = format(today)
        filters.value.toDate = format(today)
        break

      case 'yesterday': {
        const d = new Date(today)
        d.setDate(d.getDate() - 1)
        filters.value.fromDate = format(d)
        filters.value.toDate = format(d)
        break
      }

      case '7days': {
        const d = new Date(today)
        d.setDate(d.getDate() - 6)
        filters.value.fromDate = format(d)
        filters.value.toDate = format(today)
        break
      }

      case '30days': {
        const d = new Date(today)
        d.setDate(d.getDate() - 29)
        filters.value.fromDate = format(d)
        filters.value.toDate = format(today)
        break
      }
    }

    // ❌ KHÔNG fetch ở đây nữa
    page.value = 0
  },
)

// =======================
// ACTIONS
// =======================
const openDetail = (id) => {
  selectedOrderId.value = id
  showDrawer.value = true
}

const huyDon = (id) => {
  const o = orders.value.find((x) => x.id === id)
  if (o) o.trangThai = 'da_huy'
}

// =======================
// BADGE STYLE
// =======================
const statusClass = (status) => {
  switch (status) {
    case 'cho_xac_nhan':
      return 'bg-yellow-100 text-yellow-700'
    case 'da_xac_nhan':
      return 'bg-blue-100 text-blue-700'
    case 'dang_giao':
      return 'bg-purple-100 text-purple-700'
    case 'hoan_thanh':
      return 'bg-green-100 text-green-700'
    case 'da_huy':
      return 'bg-red-100 text-red-700'
    default:
      return 'bg-gray-100 text-gray-700'
  }
}

const paymentClass = (status) => {
  if (!status) return 'bg-gray-100 text-gray-600'

  switch (status) {
    case 'da_thanh_toan':
      return 'bg-green-100 text-green-700'
    case 'chua_thanh_toan':
      return 'bg-orange-100 text-orange-700'
    default:
      return 'bg-gray-100 text-gray-700'
  }
}

// =======================
// INIT
// =======================
onMounted(() => {
  fetchOrders()
})

const canChangeStatus = (current, target) => {
  current = current?.toLowerCase()

  const map = {
    cho_xac_nhan: ['da_xac_nhan', 'da_huy'],
    da_xac_nhan: ['dang_giao', 'da_huy'],
    dang_giao: ['hoan_thanh'],
    hoan_thanh: [],
    da_huy: [],
  }

  return map[current]?.includes(target)
}
const statusMap = {
  cho_xac_nhan: 'CHO_XAC_NHAN',
  da_xac_nhan: 'DA_XAC_NHAN',
  dang_giao: 'DANG_GIAO',
  hoan_thanh: 'HOAN_THANH',
  da_huy: 'DA_HUY',
}
const updateStatus = async (id, newStatus) => {
  const payload = statusMap[newStatus]

  console.log('SEND TO BACKEND:', newStatus, '→', payload)

  try {
    await hoaDonService.updateTrangThai(id, payload)
    await fetchOrders()
  } catch (err) {
    console.error('UPDATE ERROR:', err)
  }
}
const handleAction = async (o, status) => {
  await updateStatus(o.id, status)
  o.open = false
}
</script>
