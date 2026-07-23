<template>
  <div class="bg-white rounded-3xl border border-slate-200 shadow-sm p-6">
    <div class="mb-6">
      <h2 class="text-xl font-black text-slate-800">Khách hàng VIP</h2>

      <p class="text-sm text-slate-500 mt-1">Khách hàng có tổng chi tiêu cao nhất</p>
    </div>

    <div v-if="data.length" class="space-y-4">
      <div
        v-for="(item, index) in data"
        :key="item.customerId"
        class="flex items-center justify-between p-4 rounded-2xl hover:bg-slate-50 transition"
      >
        <div class="flex items-center gap-4">
          <div
            class="w-12 h-12 rounded-full bg-indigo-100 text-indigo-600 flex items-center justify-center font-black"
          >
            {{ index + 1 }}
          </div>

          <div>
            <div class="font-bold text-slate-800">
              {{ item.customerName }}
            </div>

            <div class="text-sm text-slate-500">{{ item.totalOrders }} đơn hàng</div>
          </div>
        </div>

        <div class="text-right">
          <div class="font-black text-indigo-600">
            {{ formatMoney(item.totalSpent) }}
          </div>

          <div class="text-xs text-slate-400">Tổng chi tiêu</div>
        </div>
      </div>
    </div>

    <div v-else class="text-center text-slate-400 py-10">Chưa có dữ liệu khách hàng</div>
  </div>
</template>

<script setup>
defineProps({
  data: {
    type: Array,
    default: () => [],
  },
})

const formatMoney = (value) => {
  return Number(value || 0).toLocaleString('vi-VN') + ' ₫'
}
</script>
