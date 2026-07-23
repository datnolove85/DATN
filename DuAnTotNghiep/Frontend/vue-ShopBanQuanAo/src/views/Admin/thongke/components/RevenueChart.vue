<template>
  <div class="bg-white rounded-3xl border border-slate-200 shadow-sm p-6">
    <div class="mb-5">
      <h2 class="text-xl font-black text-slate-800">Doanh thu</h2>

      <p class="text-sm text-slate-500">Biểu đồ doanh thu theo thời gian</p>
    </div>

    <VueApexCharts type="area" height="350" :options="options" :series="series" />
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

const series = computed(() => [
  {
    name: 'Doanh thu',

    data: props.data.map((item) => Number(item.revenue)),
  },
])
const options = computed(() => ({
  chart: {
    toolbar: {
      show: false,
    },
  },

  stroke: {
    curve: 'smooth',
    width: 4,
  },

  xaxis: {
    categories: props.data.map((item) => item.label),
  },

  tooltip: {
    y: {
      formatter(value) {
        return Number(value).toLocaleString('vi-VN') + ' ₫'
      },
    },
  },
}))
</script>
