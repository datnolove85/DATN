<template>
  <div
    class="bg-white rounded-3xl border border-slate-200 shadow-sm overflow-hidden p-6 hover:shadow-xl transition-all duration-300"
  >
    <!-- HEADER & CHỈ SỐ TỔNG QUAN -->
    <div class="flex flex-col md:flex-row md:items-center md:justify-between gap-4 mb-6">
      <div>
        <h2 class="text-xl font-black text-slate-800 flex items-center gap-2.5">
          <span
            class="p-2 bg-indigo-100 text-indigo-600 rounded-2xl flex items-center justify-center text-base shadow-sm"
          >
            <i class="fa-solid fa-chart-line"></i>
          </span>
          Doanh thu
        </h2>
        <p class="text-sm text-slate-500 mt-1">Biểu đồ biến động doanh thu theo thời gian</p>
      </div>

      <!-- BADGES HIỂN THỊ CHỈ SỐ LỚN -->
      <div v-if="props.data && props.data.length" class="flex items-center gap-3 flex-wrap">
        <!-- Tổng doanh thu kỳ này -->
        <div
          class="bg-slate-50/80 border border-slate-100 px-4 py-2 rounded-2xl flex items-center gap-3 shadow-sm"
        >
          <div
            class="w-8 h-8 rounded-xl bg-emerald-100 text-emerald-600 flex items-center justify-center text-xs font-bold"
          >
            <i class="fa-solid fa-coins"></i>
          </div>
          <div>
            <div class="text-[10px] uppercase font-bold text-slate-400">Tổng doanh thu</div>
            <div class="text-xs font-black text-slate-800">{{ formatMoney(totalRevenue) }}</div>
          </div>
        </div>

        <!-- Đỉnh doanh thu -->
        <div
          class="bg-slate-50/80 border border-slate-100 px-4 py-2 rounded-2xl flex items-center gap-3 shadow-sm"
        >
          <div
            class="w-8 h-8 rounded-xl bg-indigo-100 text-indigo-600 flex items-center justify-center text-xs font-bold"
          >
            <i class="fa-solid fa-arrow-trend-up"></i>
          </div>
          <div>
            <div class="text-[10px] uppercase font-bold text-slate-400">Đạt đỉnh</div>
            <div class="text-xs font-black text-indigo-600">{{ formatMoney(maxRevenue) }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- KHU VỰC HIỂN THỊ BIỂU ĐỒ -->
    <div class="relative min-h-[350px]">
      <VueApexCharts
        v-if="props.data && props.data.length"
        type="area"
        height="350"
        :options="options"
        :series="series"
      />

      <!-- KHÔNG CÓ DỮ LIỆU -->
      <div
        v-else
        class="h-[350px] text-center text-slate-400 flex flex-col items-center justify-center gap-2"
      >
        <i class="fa-solid fa-chart-area text-4xl text-slate-300 animate-pulse"></i>
        <span class="text-sm font-medium">Chưa có dữ liệu doanh thu thời gian này</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import VueApexCharts from 'vue3-apexcharts'

const props = defineProps({
  data: {
    type: Array,
    default: () => [],
  },
})

// Tính tổng doanh thu
const totalRevenue = computed(() => {
  return props.data.reduce((sum, item) => sum + Number(item.revenue || 0), 0)
})

// Tính doanh thu cao nhất
const maxRevenue = computed(() => {
  if (!props.data.length) return 0
  return Math.max(...props.data.map((item) => Number(item.revenue || 0)))
})

// Cấu hình Chuỗi dữ liệu (Series)
const series = computed(() => [
  {
    name: 'Doanh thu',
    data: props.data.map((item) => Number(item.revenue || 0)),
  },
])

// Cấu hình Biểu đồ ApexCharts rực rỡ
const options = computed(() => ({
  chart: {
    type: 'area',
    toolbar: { show: false },
    sparkline: { enabled: false },
    animations: {
      enabled: true,
      easing: 'easeinout',
      speed: 800,
    },
    fontFamily: 'Plus Jakarta Sans, Inter, sans-serif',
  },

  // Màu đường kẻ dải Indigo chủ đạo
  colors: ['#6366F1'],

  // Đổ màu Gradient mượt bên dưới đường cong
  fill: {
    type: 'gradient',
    gradient: {
      shadeIntensity: 1,
      opacityFrom: 0.45,
      opacityTo: 0.03,
      stops: [0, 90, 100],
    },
  },

  // Độ cong và độ dày nét vẽ
  stroke: {
    curve: 'smooth',
    width: 3.5,
  },

  // Điểm dữ liệu (Points)
  markers: {
    size: 4,
    colors: ['#6366F1'],
    strokeColors: '#ffffff',
    strokeWidth: 2,
    hover: {
      size: 7,
    },
  },

  // Lưới đường kẻ mờ dịu mắt
  grid: {
    borderColor: '#f1f5f9',
    strokeDashArray: 4,
    padding: {
      top: 10,
      right: 10,
      bottom: 0,
      left: 10,
    },
  },

  // Trục X (Thời gian/Ngày)
  xaxis: {
    categories: props.data.map((item) => item.label),
    labels: {
      style: {
        colors: '#94a3b8',
        fontSize: '12px',
        fontWeight: 600,
      },
    },
    axisBorder: { show: false },
    axisTicks: { show: false },
  },

  // Trục Y (Số tiền rút gọn)
  yaxis: {
    labels: {
      style: {
        colors: '#94a3b8',
        fontSize: '11px',
        fontWeight: 600,
      },
      formatter(value) {
        if (value >= 1000000000) return (value / 1000000000).toFixed(1) + ' tỷ'
        if (value >= 1000000) return (value / 1000000).toFixed(0) + ' tr'
        if (value >= 1000) return (value / 1000).toFixed(0) + ' k'
        return value
      },
    },
  },

  // Tooltip hiển thị khi rê chuột vào
  tooltip: {
    theme: 'light',
    x: { show: true },
    y: {
      formatter(value) {
        return Number(value).toLocaleString('vi-VN') + ' ₫'
      },
      title: {
        formatter: () => 'Doanh thu: ',
      },
    },
    style: {
      fontSize: '12px',
    },
  },
}))

const formatMoney = (value) => {
  return Number(value || 0).toLocaleString('vi-VN') + ' ₫'
}
</script>
