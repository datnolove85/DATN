<script setup>
import { Check } from 'lucide-vue-next'
</script>

<template>
  <div
    class="mx-auto mt-5 grid max-w-3xl grid-cols-3 rounded-2xl border border-slate-200/80 bg-white/90 p-2 shadow-sm backdrop-blur"
  >
    <div
      class="flex items-center justify-center gap-2 rounded-xl px-2 py-3 text-xs font-bold text-emerald-700 sm:text-sm"
    >
      <span class="grid h-6 w-6 place-items-center rounded-full bg-emerald-100">
        <Check :size="14" />
      </span>
      <span class="hidden sm:inline">Sản phẩm</span>
    </div>

    <div
      class="flex items-center justify-center gap-2 rounded-xl bg-indigo-50 px-2 py-3 text-xs font-bold text-indigo-700 sm:text-sm"
    >
      <span class="grid h-6 w-6 place-items-center rounded-full bg-indigo-600 text-white">2</span>
      Xác nhận
    </div>

    <div
      class="flex items-center justify-center gap-2 rounded-xl px-2 py-3 text-xs font-bold text-slate-400 sm:text-sm"
    >
      <span class="grid h-6 w-6 place-items-center rounded-full bg-slate-100">3</span>
      <span class="hidden sm:inline">Thanh toán</span>
    </div>
  </div>
</template>
