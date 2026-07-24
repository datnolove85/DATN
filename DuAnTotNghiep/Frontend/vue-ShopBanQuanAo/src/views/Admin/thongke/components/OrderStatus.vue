<template>
  <div
    class="bg-white rounded-3xl border border-slate-200 shadow-sm overflow-hidden p-6 hover:shadow-xl transition-all duration-300"
  >
    <!-- HEADER & TỔNG QUAN -->
    <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 mb-6">
      <div>
        <h2 class="text-xl font-black text-slate-800 flex items-center gap-2.5">
          <span
            class="p-2 bg-amber-100 text-amber-600 rounded-2xl flex items-center justify-center text-base shadow-sm"
          >
            <i class="fa-solid fa-chart-pie"></i>
          </span>
          Trạng thái đơn hàng
        </h2>
        <p class="text-sm text-slate-500 mt-1">Tỷ lệ phân bổ đơn hàng theo tiến độ</p>
      </div>

      <!-- BADGE TỔNG SỐ ĐƠN -->
      <div
        v-if="totalOrders"
        class="bg-slate-50 border border-slate-100 px-4 py-2 rounded-2xl flex items-center gap-2.5 shadow-sm self-start sm:self-auto"
      >
        <div class="w-2.5 h-2.5 rounded-full bg-indigo-500 animate-pulse"></div>
        <span class="text-xs font-bold text-slate-500">Tổng cộng:</span>
        <span class="text-sm font-black text-slate-800"
          >{{ totalOrders.toLocaleString('vi-VN') }} đơn</span
        >
      </div>
    </div>

    <!-- BIỂU ĐỒ DONUT -->
    <div class="relative min-h-[320px] flex items-center justify-center">
      <VueApexCharts
        v-if="props.data && props.data.length"
        type="donut"
        height="330"
        class="w-full"
        :options="options"
        :series="series"
      />

      <!-- KHÔNG CÓ DỮ LIỆU -->
      <div
        v-else
        class="h-[320px] text-center text-slate-400 flex flex-col items-center justify-center gap-2"
      >
        <i class="fa-solid fa-boxes-packing text-4xl text-slate-300 animate-bounce"></i>
        <span class="text-sm font-medium">Chưa có dữ liệu trạng thái đơn hàng</span>
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

// Bảng cấu hình tên hiển thị & màu sắc tương ứng từng trạng thái
const statusConfigMap = {
  DA_XAC_NHAN: { label: 'Đã xác nhận', color: '#6366F1' }, // Indigo
  DANG_GIAO: { label: 'Đang giao', color: '#F59E0B' }, // Amber / Orange
  DA_GIAO: { label: 'Đã giao', color: '#06B6D4' }, // Cyan
  HOAN_THANH: { label: 'Hoàn thành', color: '#10B981' }, // Emerald / Green
  DA_HUY: { label: 'Đã hủy', color: '#EF4444' }, // Red
  CHO_XU_LY: { label: 'Chờ xử lý', color: '#8B5CF6' }, // Purple
}

// Hàm lấy thông tin cấu hình từ Key trạng thái
const getStatusConfig = (status) => {
  if (!status) return { label: 'Khác', color: '#94A3B8' }
  const key = String(status).toUpperCase()
  return statusConfigMap[key] || { label: status, color: '#94A3B8' }
}

// Tính tổng số lượng đơn hàng
const totalOrders = computed(() => {
  if (!props.data || !props.data.length) return 0
  return props.data.reduce((sum, item) => sum + Number(item.total || 0), 0)
})

// Chuỗi số lượng đơn
const series = computed(() => {
  return props.data.map((item) => Number(item.total || 0))
})

// Mảng màu sắc tương ứng với từng item trong data
const colors = computed(() => {
  return props.data.map((item) => getStatusConfig(item.status).color)
})

// Mảng nhãn hiển thị
const labels = computed(() => {
  return props.data.map((item) => getStatusConfig(item.status).label)
})

// Cấu hình ApexCharts
const options = computed(() => ({
  chart: {
    type: 'donut',
    fontFamily: 'Plus Jakarta Sans, Inter, sans-serif',
    animations: {
      enabled: true,
      speed: 800,
    },
  },

  labels: labels.value,
  colors: colors.value,

  // Viền trắng cách biệt giữa các mảng donut
  stroke: {
    width: 3,
    colors: ['#ffffff'],
  },

  // Cấu hình Chú thích (Legend)
  legend: {
    position: 'bottom',
    fontSize: '13px',
    fontWeight: 600,
    labels: {
      colors: '#475569',
    },
    markers: {
      width: 10,
      height: 10,
      radius: 12,
      offsetX: -4,
    },
    itemMargin: {
      horizontal: 12,
      vertical: 6,
    },
  },

  // Hiển thị % trực tiếp trên bánh
  dataLabels: {
    enabled: true,
    dropShadow: { enabled: false },
    style: {
      fontSize: '11px',
      fontWeight: '700',
    },
  },

  // Tùy chỉnh phần rỗng giữa lòng Donut
  plotOptions: {
    pie: {
      donut: {
        size: '72%',
        labels: {
          show: true,
          name: {
            show: true,
            fontSize: '12px',
            fontWeight: '700',
            color: '#94a3b8',
            offsetY: -4,
          },
          value: {
            show: true,
            fontSize: '22px',
            fontWeight: '900',
            color: '#1e293b',
            offsetY: 6,
            formatter(val) {
              return Number(val).toLocaleString('vi-VN')
            },
          },
          total: {
            show: true,
            label: 'TỔNG ĐƠN',
            fontSize: '11px',
            fontWeight: '800',
            color: '#94a3b8',
            formatter() {
              return totalOrders.value.toLocaleString('vi-VN')
            },
          },
        },
      },
    },
  },

  // Tooltip hover
  tooltip: {
    theme: 'light',
    y: {
      formatter(val) {
        return Number(val).toLocaleString('vi-VN') + ' đơn hàng'
      },
    },
  },

  // Phản hồi co giãn màn hình
  responsive: [
    {
      breakpoint: 480,
      options: {
        chart: {
          height: 300,
        },
        legend: {
          position: 'bottom',
        },
      },
    },
  ],
}))
</script>
