<script setup>
import { ArrowLeft, UserRound, ShieldCheck, ClipboardCheck } from 'lucide-vue-next'

defineProps({
  isLoggedIn: Boolean,
  total: Number,
})

defineEmits(['back'])

const formatMoney = (value) => Number(value || 0).toLocaleString('vi-VN') + ' đ'
</script>

<template>
  <div>
    <!-- TOP BAR -->
    <div class="mb-6 flex flex-wrap items-center justify-between gap-4">
      <button
        type="button"
        class="group inline-flex items-center gap-2 text-sm font-semibold text-slate-600 transition hover:text-indigo-700"
        @click="$emit('back')"
      >
        <span
          class="grid h-9 w-9 place-items-center rounded-full border border-slate-200 bg-white shadow-sm transition group-hover:border-indigo-200 group-hover:bg-indigo-50"
        >
          <ArrowLeft :size="17" />
        </span>
        Quay lại sản phẩm
      </button>

      <div
        class="inline-flex items-center gap-2 rounded-full border border-slate-200 bg-white px-4 py-2 text-sm font-semibold text-slate-700 shadow-sm"
      >
        <UserRound :size="16" class="text-indigo-600" />
        {{ isLoggedIn ? 'Khách hàng đã đăng nhập' : 'Đặt hàng không cần tài khoản' }}
      </div>
    </div>

    <!-- HERO -->
    <section
      class="relative overflow-hidden rounded-[28px] bg-gradient-to-r from-blue-600 via-indigo-600 to-violet-600 px-6 py-7 text-white shadow-xl shadow-indigo-500/20 sm:px-8 lg:px-10 lg:py-9"
    >
      <div class="absolute -right-20 -top-24 h-72 w-72 rounded-full bg-white/10 blur-2xl"></div>
      <div
        class="absolute -bottom-28 left-1/3 h-64 w-64 rounded-full bg-cyan-400/20 blur-2xl"
      ></div>

      <div class="relative grid gap-7 lg:grid-cols-[1fr_auto] lg:items-center">
        <div>
          <div
            class="mb-4 inline-flex items-center gap-2 rounded-full border border-white/20 bg-white/15 px-3 py-1.5 text-xs font-bold uppercase tracking-[0.18em] text-white backdrop-blur-md"
          >
            <ClipboardCheck :size="14" />
            Xác nhận đơn hàng
          </div>

          <h1
            class="max-w-2xl text-3xl font-black tracking-tight sm:text-4xl lg:text-5xl text-white"
          >
            Kiểm tra lần cuối trước khi đặt hàng
          </h1>

          <p class="mt-3 max-w-2xl text-sm leading-6 text-indigo-100 sm:text-base">
            Hoàn thiện thông tin nhận hàng, vận chuyển và ưu đãi. Bạn sẽ chọn phương thức thanh toán
            ở bước tiếp theo.
          </p>
        </div>

        <div
          class="min-w-[250px] rounded-2xl border border-white/20 bg-white/10 p-4 shadow-lg backdrop-blur-md"
        >
          <p class="text-xs font-bold uppercase tracking-[0.16em] text-indigo-100">
            Tổng thanh toán dự kiến
          </p>
          <p class="mt-2 text-3xl font-black text-white">{{ formatMoney(total) }}</p>
          <div class="mt-3 flex items-center gap-2 text-xs text-emerald-300 font-medium">
            <ShieldCheck :size="15" />
            Giá được kiểm tra lại ở máy chủ
          </div>
        </div>
      </div>
    </section>
  </div>
</template>
