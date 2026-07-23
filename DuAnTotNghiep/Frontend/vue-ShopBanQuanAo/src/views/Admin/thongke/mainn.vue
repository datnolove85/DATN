<template>
  <div class="min-h-screen bg-slate-50 p-6">
    <!-- HEADER & ADVANCED FILTER -->
    <div class="flex flex-col xl:flex-row xl:items-center xl:justify-between gap-4 mb-8">
      <div
        class="rounded-3xl bg-gradient-to-r from-slate-900 via-indigo-900 to-slate-900 p-8 text-white flex-1"
      >
        <h1 class="text-3xl xl:text-4xl font-black">Dashboard thống kê</h1>
        <p class="text-slate-300 mt-2">Theo dõi tình hình kinh doanh realtime</p>
      </div>

      <!-- Thanh bộ lọc xịn xò -->
      <div
        class="flex flex-wrap items-center gap-2 bg-white p-2.5 rounded-3xl border border-slate-200 shadow-sm self-start xl:self-auto"
      >
        <!-- Bộ lọc thời gian -->
        <div class="flex items-center bg-slate-100 p-1 rounded-2xl">
          <button
            v-for="item in filterOptions"
            :key="item.value"
            @click="handleFilterChange(item.value)"
            class="px-3.5 py-1.5 text-xs font-bold rounded-xl transition-all"
            :class="
              currentFilter === item.value
                ? 'bg-white text-slate-900 shadow-xs'
                : 'text-slate-500 hover:text-slate-900'
            "
          >
            {{ item.label }}
          </button>
        </div>

        <!-- Bộ lọc kênh bán hàng (Online / POS) -->
        <div class="flex items-center bg-slate-100 p-1 rounded-2xl border-l border-slate-200">
          <button
            v-for="kenh in kenhOptions"
            :key="kenh.value"
            @click="handleKenhChange(kenh.value)"
            class="px-3.5 py-1.5 text-xs font-bold rounded-xl transition-all"
            :class="
              currentKenh === kenh.value
                ? 'bg-white text-slate-900 shadow-xs'
                : 'text-slate-500 hover:text-slate-900'
            "
          >
            {{ kenh.label }}
          </button>
        </div>

        <!-- Ô chọn ngày tùy chỉnh -->
        <div
          v-if="currentFilter === 'custom'"
          class="flex items-center gap-1.5 px-2 border-l border-slate-200"
        >
          <input
            type="date"
            v-model="startDate"
            class="bg-slate-50 border border-slate-200 text-slate-700 text-xs rounded-xl px-2 py-1.5 focus:outline-none focus:ring-2 focus:ring-indigo-500/20"
          />
          <span class="text-slate-400 text-xs">đến</span>
          <input
            type="date"
            v-model="endDate"
            @change="handleCustomDateChange"
            class="bg-slate-50 border border-slate-200 text-slate-700 text-xs rounded-xl px-2 py-1.5 focus:outline-none focus:ring-2 focus:ring-indigo-500/20"
          />
        </div>

        <!-- Nút Refresh -->
        <button
          @click="handleRefresh"
          class="flex items-center gap-1.5 bg-indigo-50 hover:bg-indigo-100 text-indigo-600 px-3.5 py-2 rounded-2xl text-xs font-bold transition active:scale-95 ml-auto"
        >
          <i :class="['fa-solid fa-rotate', { 'animate-spin': loading }]"></i>
          <span>Làm mới</span>
        </button>
      </div>
    </div>

    <!-- CARDS STATS (Đã đổi thành grid-cols-5 cho cân đối 5 thẻ) -->
    <div class="grid xl:grid-cols-5 md:grid-cols-3 gap-6">
      <StatCard
        title="Doanh thu"
        :value="formatMoney(dashboard?.tongDoanhThu)"
        :cash="dashboard?.doanhThuTienMat"
        :transfer="dashboard?.doanhThuChuyenKhoan"
        icon="fa-solid fa-money-bill-trend-up"
        bg="bg-emerald-100 text-emerald-600"
      />
      <!-- Thẻ Lợi nhuận gộp -->
      <StatCard
        title="Lợi nhuận"
        :value="formatMoney(dashboard?.loiNhuanGop)"
        icon="fa-solid fa-chart-line"
        bg="bg-amber-100 text-amber-600"
      />

      <StatCard
        title="Đơn hàng"
        :value="dashboard?.tongDonHang ?? 0"
        icon="fa-solid fa-cart-shopping"
        bg="bg-indigo-100 text-indigo-600"
      />

      <StatCard
        title="Khách hàng"
        :value="dashboard?.tongKhachHang ?? 0"
        icon="fa-solid fa-users"
        bg="bg-sky-100 text-sky-600"
      />

      <StatCard
        title="Sản phẩm"
        :value="dashboard?.tongSanPham ?? 0"
        icon="fa-solid fa-shirt"
        bg="bg-rose-100 text-rose-600"
      />
    </div>

    <!-- CHARTS -->
    <div class="grid xl:grid-cols-3 gap-6 mt-8">
      <div class="xl:col-span-2">
        <RevenueChart :data="revenue" />
      </div>
      <OrderStatus :data="orderStatus" />
    </div>

    <!-- TABLES -->
    <div class="grid xl:grid-cols-2 gap-6 mt-8">
      <TopProducts :data="topProducts" />
      <TopCustomers :data="topCustomers" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

import StatCard from './components/StatCard.vue'
import RevenueChart from './components/RevenueChart.vue'
import OrderStatus from './components/OrderStatus.vue'
import TopProducts from './components/TopProducts.vue'
import TopCustomers from './components/TopCustomers.vue'

import { useThongKe } from './composables/useThongKe.js'

const { dashboard, revenue, orderStatus, topProducts, topCustomers, loading, loadAll } =
  useThongKe()

const currentFilter = ref('month')
const currentKenh = ref('') // Mặc định tất cả kênh
const startDate = ref('')
const endDate = ref('')

const filterOptions = [
  { label: 'Hôm nay', value: 'today' },
  { label: 'Tháng này', value: 'month' },
  { label: 'Năm nay', value: 'year' },
  { label: 'Tùy chỉnh', value: 'custom' },
]

const kenhOptions = [
  { label: 'Tất cả kênh', value: '' },
  { label: '🌐 Online', value: 'online' },
  { label: '🏪 POS', value: 'tai_quay' },
]

const handleFilterChange = (value) => {
  currentFilter.value = value
  if (value !== 'custom') {
    // Đã bổ sung truyền thêm currentKenh.value vào đây
    loadAll(value, startDate.value, endDate.value, currentKenh.value)
  }
}

const handleKenhChange = (value) => {
  currentKenh.value = value
  loadAll(currentFilter.value, startDate.value, endDate.value, currentKenh.value)
}

const handleCustomDateChange = () => {
  if (startDate.value && endDate.value) {
    loadAll('custom', startDate.value, endDate.value, currentKenh.value)
  }
}

const handleRefresh = () => {
  loadAll(currentFilter.value, startDate.value, endDate.value, currentKenh.value)
}

onMounted(() => {
  loadAll('month', '', '', '')
})

const formatMoney = (value) => {
  if (!value) return '0 ₫'
  return Number(value).toLocaleString('vi-VN') + ' ₫'
}
</script>
