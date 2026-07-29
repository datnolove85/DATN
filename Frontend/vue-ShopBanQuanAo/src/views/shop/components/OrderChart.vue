<template>
  <div class="bg-white rounded-3xl border border-slate-200 shadow-sm overflow-hidden h-full">
    <!-- Header -->
    <div class="px-6 py-5 flex items-center justify-between border-b border-slate-100">
      <div>
        <h3 class="text-lg font-bold text-slate-800">Đơn hàng</h3>

        <p class="text-sm text-slate-500 mt-1">Số lượng đơn theo từng tháng</p>
      </div>

      <div class="w-11 h-11 rounded-2xl bg-indigo-50 flex items-center justify-center">
        <i class="fa-solid fa-cart-shopping text-indigo-600 text-lg"></i>
      </div>
    </div>

    <!-- Loading -->
    <div v-if="loading" class="h-[380px] flex items-center justify-center">
      <div class="animate-pulse space-y-4 w-full px-6">
        <div class="h-5 bg-slate-200 rounded w-1/3"></div>

        <div class="space-y-3">
          <div class="h-4 rounded bg-slate-200"></div>
          <div class="h-4 rounded bg-slate-200"></div>
          <div class="h-4 rounded bg-slate-200"></div>
          <div class="h-4 rounded bg-slate-200"></div>
          <div class="h-4 rounded bg-slate-200"></div>
          <div class="h-4 rounded bg-slate-200"></div>
        </div>
      </div>
    </div>

    <!-- Chart -->
    <div v-else class="p-4">
      <VueApexCharts height="380" type="bar" :options="chartOptions" :series="series" />
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import VueApexCharts from 'vue3-apexcharts'

const props = defineProps({
  loading: {
    type: Boolean,
    default: false,
  },

  data: {
    type: Array,
    default: () => [],
  },
})

const months = computed(() => props.data.map((item) => item.month))

const orders = computed(() => props.data.map((item) => item.orders))

const series = computed(() => [
  {
    name: 'Đơn hàng',
    data: orders.value,
  },
])

const chartOptions = computed(() => ({
  chart: {
    toolbar: {
      show: false,
    },
    zoom: {
      enabled: false,
    },
    fontFamily: 'inherit',
  },

  colors: ['#6366F1'],

  plotOptions: {
    bar: {
      borderRadius: 8,
      columnWidth: '50%',
    },
  },

  dataLabels: {
    enabled: false,
  },

  stroke: {
    show: false,
  },

  xaxis: {
    categories: months.value,

    axisBorder: {
      show: false,
    },

    axisTicks: {
      show: false,
    },

    labels: {
      style: {
        colors: '#64748b',
        fontSize: '12px',
      },
    },
  },

  yaxis: {
    labels: {
      style: {
        colors: '#64748b',
      },
    },
  },

  grid: {
    borderColor: '#E2E8F0',
    strokeDashArray: 4,
  },

  tooltip: {
    theme: 'light',

    y: {
      formatter(value) {
        return value + ' đơn'
      },
    },
  },

  legend: {
    show: false,
  },
}))
</script>
