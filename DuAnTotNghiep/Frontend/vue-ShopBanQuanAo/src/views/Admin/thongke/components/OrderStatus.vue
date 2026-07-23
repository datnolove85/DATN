<template>
  <div class="bg-white rounded-3xl p-6 shadow-sm border border-slate-200">
    <h2 class="text-xl font-black mb-5">Trạng thái đơn hàng</h2>

    <VueApexCharts type="donut" height="320" :options="options" :series="series" />
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

const labelMap = {
  DA_XAC_NHAN: 'Đã xác nhận',

  DANG_GIAO: 'Đang giao',

  da_giao: 'Đã giao',

  hoan_thanh: 'Hoàn thành',

  DA_HUY: 'Đã hủy',
}

const series = computed(() => {
  return props.data.map((item) => item.total)
})

const options = computed(() => ({
  labels: props.data.map((item) => {
    return labelMap[item.status] || item.status
  }),

  legend: {
    position: 'bottom',
  },

  dataLabels: {
    enabled: true,
  },

  plotOptions: {
    pie: {
      donut: {
        size: '65%',
      },
    },
  },
}))
</script>
