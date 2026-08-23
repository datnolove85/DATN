<script setup>
defineProps({
  customerInfo: Object,
  soXuCanDung: Number,
  soXuSuDung: Number,
  tienGiamDoXu: Number,
})

defineEmits(['update:soXuCanDung', 'apply-xu', 'cancel-xu'])

const formatMoney = (value) => Number(value || 0).toLocaleString('vi-VN') + ' đ'
</script>

<template>
  <section
    class="rounded-[28px] border border-slate-200/80 bg-white p-5 shadow-xl shadow-slate-200/40 sm:p-7"
  >
    <div class="flex items-center justify-between mb-4">
      <div class="flex items-center gap-3">
        <span class="grid h-11 w-11 place-items-center rounded-2xl bg-amber-50 text-amber-600">
          <span class="text-xl">🪙</span>
        </span>
        <div>
          <p class="text-xs font-black uppercase tracking-[0.15em] text-amber-600">Tích điểm</p>
          <h2 class="font-black text-slate-900">Sử dụng Xu giảm giá</h2>
        </div>
      </div>
      <span class="text-sm font-medium text-slate-600">
        Số dư khả dụng:
        <strong class="text-amber-600">{{ customerInfo?.soDuXu || 0 }} xu</strong>
      </span>
    </div>

    <div v-if="customerInfo && customerInfo.soDuXu > 0" class="space-y-3">
      <div class="flex items-center gap-3">
        <input
          type="number"
          :value="soXuCanDung"
          @input="$emit('update:soXuCanDung', Number($event.target.value))"
          min="0"
          :max="customerInfo.soDuXu"
          placeholder="Nhập số xu muốn dùng..."
          class="w-full rounded-xl border border-slate-300 bg-slate-50 px-4 py-3 text-sm outline-none transition focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
        />
        <button
          type="button"
          @click="$emit('apply-xu')"
          class="shrink-0 rounded-xl bg-amber-500 px-5 py-3 text-sm font-bold text-white shadow-md shadow-amber-100 transition hover:bg-amber-600"
        >
          Áp dụng xu
        </button>
        <button
          v-if="soXuSuDung > 0"
          type="button"
          @click="$emit('cancel-xu')"
          class="shrink-0 rounded-xl bg-slate-100 px-4 py-3 text-sm font-bold text-slate-600 transition hover:bg-slate-200"
        >
          Bỏ chọn
        </button>
      </div>

      <div
        v-if="tienGiamDoXu > 0"
        class="text-xs text-emerald-600 font-medium flex items-center gap-1"
      >
        <span
          >✓ Đã dùng <strong>{{ soXuSuDung }} xu</strong> để giảm trừ:</span
        >
        <span class="font-bold">-{{ formatMoney(tienGiamDoXu) }}</span>
      </div>
    </div>
    <div v-else class="text-sm text-slate-400 italic">
      Khách hàng hiện không có xu tích lũy để sử dụng.
    </div>
  </section>
</template>
