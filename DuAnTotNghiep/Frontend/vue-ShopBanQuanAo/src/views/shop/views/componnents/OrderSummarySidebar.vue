<script setup>
import { ReceiptText, LoaderCircle, CreditCard, ShieldCheck } from 'lucide-vue-next'

defineProps({
  quantity: Number,
  subtotal: Number,
  shippingFee: Number,
  voucherDiscount: Number,
  tienGiamDoXu: Number,
  total: Number,
  isPlacingOrder: Boolean,
  shippingLoading: Boolean,
  isCartCheckout: Boolean,
  checkoutItems: Array,
  maxAvailable: Number,
})

defineEmits(['place-order'])

const formatMoney = (value) => Number(value || 0).toLocaleString('vi-VN') + ' đ'
</script>

<template>
  <aside class="space-y-5 lg:sticky lg:top-6">
    <section
      class="overflow-hidden rounded-[28px] border border-slate-200/80 bg-white shadow-2xl shadow-slate-200/60"
    >
      <div class="border-b border-slate-100 px-6 py-5">
        <div class="flex items-center gap-3">
          <span class="grid h-11 w-11 place-items-center rounded-2xl bg-slate-900 text-white">
            <ReceiptText :size="22" />
          </span>
          <div>
            <h2 class="text-xl font-black">Tóm tắt đơn hàng</h2>
            <p class="text-xs text-slate-400">{{ quantity }} sản phẩm trong đơn</p>
          </div>
        </div>
      </div>

      <div class="space-y-4 px-6 py-5 text-sm">
        <div class="flex items-center justify-between gap-4">
          <span class="text-slate-500">Tiền hàng</span>
          <span class="font-bold text-slate-800">{{ formatMoney(subtotal) }}</span>
        </div>
        <div class="flex items-center justify-between gap-4">
          <span class="text-slate-500">Phí vận chuyển</span>
          <span class="font-bold text-slate-800">{{ formatMoney(shippingFee) }}</span>
        </div>
        <div class="flex items-center justify-between gap-4">
          <span class="text-slate-500">Voucher giảm</span>
          <span class="font-bold text-emerald-600">-{{ formatMoney(voucherDiscount) }}</span>
        </div>
        <div v-if="tienGiamDoXu > 0" class="flex items-center justify-between gap-4">
          <span class="text-slate-500">Xu giảm giá</span>
          <span class="font-bold text-amber-600">-{{ formatMoney(tienGiamDoXu) }}</span>
        </div>

        <div class="border-t border-dashed border-slate-200 pt-4">
          <div class="flex items-end justify-between gap-4">
            <span class="font-black text-slate-900">Tổng thanh toán</span>
            <span class="text-right text-2xl font-black text-indigo-700">
              {{ formatMoney(total) }}
            </span>
          </div>
        </div>
      </div>

      <div class="border-t border-slate-100 bg-slate-50/80 p-5">
        <button
          type="button"
          :disabled="
            isPlacingOrder ||
            shippingLoading ||
            (isCartCheckout ? checkoutItems.length === 0 : maxAvailable === 0)
          "
          class="flex w-full items-center justify-center gap-2 rounded-2xl bg-indigo-600 px-5 py-4 text-base font-black text-white shadow-lg shadow-indigo-200 transition hover:-translate-y-0.5 hover:bg-indigo-700 disabled:cursor-not-allowed disabled:opacity-60 disabled:hover:translate-y-0"
          @click="$emit('place-order')"
        >
          <LoaderCircle v-if="isPlacingOrder" :size="20" class="animate-spin" />
          <CreditCard v-else :size="20" />
          {{ isPlacingOrder ? 'Đang tạo đơn...' : 'Đặt hàng và thanh toán' }}
        </button>

        <p class="mt-3 text-center text-xs leading-5 text-slate-400">
          Nhấn đặt hàng đồng nghĩa bạn xác nhận thông tin phía trên là chính xác.
        </p>
      </div>
    </section>

    <section class="rounded-2xl border border-emerald-100 bg-emerald-50/80 p-5">
      <div class="flex gap-3">
        <span
          class="grid h-10 w-10 shrink-0 place-items-center rounded-xl bg-white text-emerald-600 shadow-sm"
        >
          <ShieldCheck :size="21" />
        </span>
        <div>
          <h3 class="font-black text-emerald-900">Thông tin đơn hàng được bảo vệ</h3>
          <p class="mt-1 text-sm leading-5 text-emerald-800/75">
            Thông tin người nhận chỉ được dùng để xử lý và giao đơn hàng.
          </p>
        </div>
      </div>
    </section>
  </aside>
</template>
