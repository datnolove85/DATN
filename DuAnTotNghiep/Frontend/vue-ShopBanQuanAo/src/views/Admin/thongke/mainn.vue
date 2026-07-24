<template>
  <div class="min-h-screen bg-slate-50 p-6">
    <!-- HEADER & ADVANCED FILTER -->
    <div
      class="flex flex-col xl:flex-row xl:items-center xl:justify-between gap-5 mb-8 p-1 rounded-3xl bg-slate-950"
    >
      <!-- BANNER KHÔNG GIAN PHÁT SÁNG (CYBER GLOW) -->
      <div
        class="relative overflow-hidden rounded-[22px] bg-slate-900/80 backdrop-blur-xl p-6 md:p-8 text-white flex-1 border border-white/10 shadow-[0_0_50px_-12px_rgba(99,102,241,0.25)]"
      >
        <!-- Hiệu ứng dải Neon Glow ngầm -->
        <div
          class="absolute -top-20 -left-20 w-60 h-60 bg-cyan-500/30 rounded-full blur-[80px] pointer-events-none"
        ></div>
        <div
          class="absolute -bottom-20 -right-20 w-60 h-60 bg-indigo-500/30 rounded-full blur-[80px] pointer-events-none"
        ></div>

        <!-- Họa tiết lưới tọa độ HUD -->
        <div
          class="absolute inset-0 bg-[linear-gradient(to_right,#ffffff05_1px,transparent_1px),linear-gradient(to_bottom,#ffffff05_1px,transparent_1px)] bg-[size:24px_24px] pointer-events-none"
        ></div>

        <div
          class="relative z-10 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4"
        >
          <div>
            <!-- Badge Neon Pulse -->
            <div
              class="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-cyan-500/10 border border-cyan-400/30 text-cyan-400 text-xs font-mono mb-3"
            >
              <span
                class="w-2 h-2 rounded-full bg-cyan-400 shadow-[0_0_10px_#22d3ee] animate-pulse"
              ></span>
              SYSTEM ONLINE // LIVE METRICS
            </div>

            <h1
              class="text-2xl sm:text-3xl xl:text-4xl font-black tracking-tight text-transparent bg-clip-text bg-gradient-to-r from-white via-slate-200 to-cyan-400"
            >
              Dashboard Thống Kê
            </h1>
            <p class="text-slate-400 text-xs sm:text-sm mt-1.5 font-medium">
              Giám sát thời gian thực với hệ thống phân tích dữ liệu tự động
            </p>
          </div>
        </div>
      </div>

      <!-- THANH BỘ LỌC KÍNH MỜ (GLASS FILTER BAR) -->
      <div
        class="flex flex-wrap items-center gap-2.5 bg-slate-900/60 backdrop-blur-xl p-2.5 rounded-[22px] border border-white/10 shadow-2xl self-start xl:self-auto"
      >
        <!-- LỌC THỜI GIAN -->
        <div class="flex items-center bg-slate-950/80 p-1 rounded-2xl border border-white/5">
          <button
            v-for="item in filterOptions"
            :key="item.value"
            @click="handleFilterChange(item.value)"
            class="px-3.5 py-1.5 text-xs font-bold rounded-xl transition-all duration-300"
            :class="
              currentFilter === item.value
                ? 'bg-gradient-to-r from-cyan-500 to-indigo-600 text-white shadow-[0_0_15px_rgba(6,182,212,0.4)]'
                : 'text-slate-400 hover:text-slate-200'
            "
          >
            {{ item.label }}
          </button>
        </div>

        <!-- LỌC KÊNH BÁN -->
        <div class="flex items-center bg-slate-950/80 p-1 rounded-2xl border border-white/5">
          <button
            v-for="kenh in kenhOptions"
            :key="kenh.value"
            @click="handleKenhChange(kenh.value)"
            class="px-3.5 py-1.5 text-xs font-bold rounded-xl transition-all duration-300"
            :class="
              currentKenh === kenh.value
                ? 'bg-gradient-to-r from-cyan-500 to-indigo-600 text-white shadow-[0_0_15px_rgba(6,182,212,0.4)]'
                : 'text-slate-400 hover:text-slate-200'
            "
          >
            {{ kenh.label }}
          </button>
        </div>

        <!-- Ô CHỌN NGÀY TÙY CHỈNH (CYBER STYLE) -->
        <div
          v-if="currentFilter === 'custom'"
          class="flex items-center gap-1.5 px-3 py-1 bg-slate-950/80 rounded-2xl border border-white/10"
        >
          <i class="fa-regular fa-calendar text-cyan-400 text-xs"></i>
          <input
            type="date"
            v-model="startDate"
            class="bg-transparent text-slate-200 text-xs font-semibold px-1 py-1 focus:outline-none cursor-pointer [color-scheme:dark]"
          />
          <span class="text-slate-500 text-xs font-medium">→</span>
          <input
            type="date"
            v-model="endDate"
            @change="handleCustomDateChange"
            class="bg-transparent text-slate-200 text-xs font-semibold px-1 py-1 focus:outline-none cursor-pointer [color-scheme:dark]"
          />
        </div>

        <!-- NÚT REFRESH NEON (RESET VỀ HÔM NAY & TẤT CẢ KÊNH) -->
        <button
          @click="handleRefresh"
          :disabled="loading"
          class="flex items-center gap-2 bg-cyan-500/10 hover:bg-cyan-500/20 text-cyan-400 border border-cyan-500/30 px-4 py-2 rounded-2xl text-xs font-bold transition-all shadow-[0_0_15px_rgba(6,182,212,0.15)] active:scale-95 disabled:opacity-50 ml-auto xl:ml-0"
        >
          <i :class="['fa-solid fa-rotate', { 'animate-spin': loading }]"></i>
          <span>Làm mới</span>
        </button>
      </div>
    </div>

    <!-- CARDS STATS -->
    <div class="grid xl:grid-cols-5 md:grid-cols-3 gap-6">
      <!-- 1. Doanh thu -->
      <StatCard
        title="Doanh thu"
        :value="formatMoney(dashboard?.tongDoanhThu)"
        :cash="dashboard?.doanhThuTienMat ?? 0"
        :transfer="dashboard?.doanhThuChuyenKhoan ?? 0"
        icon="fa-solid fa-money-bill-trend-up"
        bg="bg-emerald-100 text-emerald-600"
      />

      <!-- 2. Lợi nhuận -->
      <StatCard
        title="Lợi nhuận"
        :value="formatMoney(dashboard?.loiNhuanGop)"
        :subValue="`Biên LN: ${dashboard?.bienLoiNhuan ?? 0}%`"
        :cost="formatMoney(dashboard?.giaVon)"
        icon="fa-solid fa-chart-line"
        bg="bg-amber-100 text-amber-600"
      />

      <!-- 3. Đơn hàng -->
      <StatCard
        title="Đơn hàng"
        :value="dashboard?.tongDonHang ?? 0"
        :online="dashboard?.donHangOnline ?? 0"
        :pos="dashboard?.donHangTaiQuay ?? 0"
        icon="fa-solid fa-cart-shopping"
        bg="bg-indigo-100 text-indigo-600"
      />

      <!-- 4. Khách hàng -->
      <StatCard
        title="Khách hàng"
        :value="(dashboard?.khachThanhVien ?? 0) + (dashboard?.khachLe ?? 0)"
        subValue="Khách mua trong kỳ"
        :member="dashboard?.khachThanhVien ?? 0"
        :guest="dashboard?.khachLe ?? 0"
        icon="fa-solid fa-users"
        bg="bg-sky-100 text-sky-600"
      />

      <!-- 5. Sản phẩm -->
      <StatCard
        title="Sản phẩm"
        :value="dashboard?.tongSanPham ?? 0"
        :sku="dashboard?.skuDaBan ?? 0"
        :avg="dashboard?.trungBinhSpMoiDon ?? 0"
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
const currentKenh = ref('') // Mặc định tất cả kênh ('')
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

// XỬ LÝ LÀM MỚI: RESET VỀ HÔM NAY VÀ TẤT CẢ KÊNH
const handleRefresh = () => {
  currentFilter.value = 'today'
  currentKenh.value = ''
  startDate.value = ''
  endDate.value = ''

  loadAll('today', '', '', '')
}

onMounted(() => {
  loadAll('month', '', '', '')
})

const formatMoney = (value) => {
  if (!value) return '0 ₫'
  return Number(value).toLocaleString('vi-VN') + ' ₫'
}
</script>
