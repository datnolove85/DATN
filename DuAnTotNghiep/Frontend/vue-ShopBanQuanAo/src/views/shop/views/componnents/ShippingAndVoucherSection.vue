<script setup>
import { Truck, TicketPercent, LoaderCircle } from 'lucide-vue-next'

defineProps({
  shippingLoading: Boolean,
  shippingFee: Number,
  selectedVoucher: Object,
})

defineEmits(['open-voucher-modal'])

const formatMoney = (value) => Number(value || 0).toLocaleString('vi-VN') + ' đ'
</script>

<template>
  <div class="grid gap-6 md:grid-cols-2">
    <section
      class="rounded-[28px] border border-slate-200/80 bg-white p-5 shadow-xl shadow-slate-200/40 sm:p-6"
    >
      <div class="flex items-center gap-3">
        <span class="grid h-11 w-11 place-items-center rounded-2xl bg-sky-50 text-sky-600">
          <Truck :size="22" />
        </span>
        <div>
          <p class="text-xs font-black uppercase tracking-[0.15em] text-sky-600">Vận chuyển</p>
          <h2 class="font-black text-slate-900">Giao hàng tiêu chuẩn</h2>
        </div>
      </div>

      <div class="mt-5 rounded-2xl border border-sky-100 bg-sky-50/70 p-4">
        <div class="flex items-center justify-between gap-4">
          <div>
            <p class="font-black text-slate-900">Tiêu chuẩn</p>
            <p class="mt-1 text-xs text-slate-500">Dự kiến 2–4 ngày làm việc</p>
          </div>
          <LoaderCircle v-if="shippingLoading" :size="20" class="animate-spin text-sky-600" />
          <p v-else class="font-black text-sky-700">{{ formatMoney(shippingFee) }}</p>
        </div>
      </div>
    </section>

    <section
      class="rounded-[28px] border border-slate-200/80 bg-white p-5 shadow-xl shadow-slate-200/40 sm:p-6"
    >
      <div class="flex items-center gap-3">
        <span class="grid h-11 w-11 place-items-center rounded-2xl bg-emerald-50 text-emerald-600">
          <TicketPercent :size="22" />
        </span>
        <div>
          <p class="text-xs font-black uppercase tracking-[0.15em] text-emerald-600">Ưu đãi</p>
          <h2 class="font-black text-slate-900">Voucher đơn hàng</h2>
        </div>
      </div>

      <div class="mt-5">
        <button
          @click="$emit('open-voucher-modal')"
          class="flex w-full items-center justify-between rounded-xl border border-slate-300 bg-white p-4 hover:border-indigo-500 transition"
        >
          <div class="text-left">
            <p class="font-semibold">
              {{ selectedVoucher ? selectedVoucher.tenVoucher : 'Chọn voucher' }}
            </p>
            <p class="text-sm text-gray-500">
              {{ selectedVoucher ? selectedVoucher.maVoucher : 'Nhấn để chọn voucher' }}
            </p>
          </div>
        </button>
      </div>
    </section>
  </div>
</template>
