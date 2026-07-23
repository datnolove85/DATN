<template>
  <div
    class="bg-white rounded-3xl border border-slate-200 shadow-sm p-6 hover:shadow-lg transition flex flex-col justify-between"
  >
    <div class="flex justify-between items-start">
      <div>
        <p class="text-sm text-slate-500 font-medium">
          {{ title }}
        </p>

        <h2 class="text-3xl font-black mt-3 text-slate-800">
          {{ value }}
        </h2>
      </div>

      <div class="w-14 h-14 rounded-2xl flex items-center justify-center shrink-0" :class="bg">
        <i :class="icon" class="text-2xl"></i>
      </div>
    </div>

    <!-- Phần hiển thị chi tiết Tiền mặt & Chuyển khoản -->
    <div
      v-if="cash !== null && transfer !== null"
      class="mt-6 border-t border-slate-100 pt-4 space-y-4"
    >
      <!-- Tiền mặt -->
      <div>
        <div class="flex justify-between text-sm font-medium">
          <span class="flex items-center gap-2 text-slate-600">💵 Tiền mặt</span>
          <span class="font-bold text-slate-800">{{ money(cash) }}</span>
        </div>

        <div class="mt-2 h-2 rounded-full bg-slate-100 overflow-hidden">
          <div
            class="h-full rounded-full bg-emerald-500 transition-all duration-500"
            :style="{ width: cashPercent + '%' }"
          />
        </div>

        <div class="text-right text-xs text-slate-400 mt-1 font-medium">{{ cashPercent }}%</div>
      </div>

      <!-- Chuyển khoản -->
      <div>
        <div class="flex justify-between text-sm font-medium">
          <span class="flex items-center gap-2 text-slate-600">🏦 Chuyển khoản</span>
          <span class="font-bold text-slate-800">{{ money(transfer) }}</span>
        </div>

        <div class="mt-2 h-2 rounded-full bg-slate-100 overflow-hidden">
          <div
            class="h-full rounded-full bg-indigo-600 transition-all duration-500"
            :style="{ width: transferPercent + '%' }"
          />
        </div>

        <div class="text-right text-xs text-slate-400 mt-1 font-medium">{{ transferPercent }}%</div>
      </div>
    </div>

    <!-- Phần tăng trưởng so với kỳ trước (nếu cần hiển thị) -->
    <div v-if="growth !== 0" class="mt-4">
      <div
        class="inline-flex items-center gap-1 px-3 py-1 rounded-full text-xs font-bold"
        :class="growth >= 0 ? 'bg-emerald-100 text-emerald-700' : 'bg-rose-100 text-rose-700'"
      >
        <span>{{ growth >= 0 ? '+' : '' }}{{ growth }}%</span>
        <span class="font-medium">so với kỳ trước</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

// 1. Khai báo props trước
const props = defineProps({
  title: {
    type: String,
    default: '',
  },
  value: {
    type: [String, Number],
    default: 0,
  },
  growth: {
    type: Number,
    default: 0,
  },
  icon: {
    type: String,
    default: '',
  },
  bg: {
    type: String,
    default: 'bg-indigo-100 text-indigo-600',
  },
  cash: {
    type: [String, Number],
    default: null,
  },
  transfer: {
    type: [String, Number],
    default: null,
  },
})

// 2. Tính toán tổng và tỉ trọng sau khi đã có props
const total = computed(() => Number(props.cash || 0) + Number(props.transfer || 0))

const cashPercent = computed(() => {
  if (!total.value || total.value === 0) return 0
  return ((Number(props.cash || 0) / total.value) * 100).toFixed(1)
})

const transferPercent = computed(() => {
  if (!total.value || total.value === 0) return 0
  return ((Number(props.transfer || 0) / total.value) * 100).toFixed(1)
})

const money = (v) => Number(v || 0).toLocaleString('vi-VN') + ' ₫'
</script>
