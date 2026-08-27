<template>
  <section class="mx-auto max-w-[1480px] px-4 py-8 md:px-8 md:py-12">
    <div class="grid overflow-hidden rounded-[28px] bg-slate-950 md:grid-cols-[1.15fr_0.85fr]">
      <div
        class="relative min-h-[260px] overflow-hidden bg-[radial-gradient(circle_at_18%_20%,rgba(244,63,94,.35),transparent_32%),radial-gradient(circle_at_88%_12%,rgba(245,158,11,.18),transparent_30%),#111827] p-7 text-white md:p-10"
      >
        <div
          class="absolute inset-0 opacity-30 [background-image:linear-gradient(rgba(255,255,255,.06)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,.06)_1px,transparent_1px)] [background-size:42px_42px]"
        />
        <div class="relative max-w-xl">
          <p class="text-[11px] font-black uppercase tracking-[0.2em] text-amber-300">
            Phong cách của bạn
          </p>
          <h2 class="mt-3 text-3xl font-black leading-tight md:text-4xl">
            Chọn đúng món đồ, phần còn lại để phong cách lên tiếng.
          </h2>
          <p class="mt-4 max-w-lg text-sm leading-6 text-slate-300">
            Mỗi sản phẩm đều được tổ chức để bạn tìm, so sánh và mở chi tiết thật nhanh.
          </p>
          <button
            type="button"
            class="mt-6 rounded-full bg-white px-5 py-3 text-xs font-black text-slate-950"
          >
            Khám phá bộ sưu tập
          </button>
        </div>
      </div>
      <div class="grid grid-cols-2 gap-3 bg-[#f5efe4] p-5 md:p-7">
        <div class="rounded-2xl border border-white/70 bg-white/70 p-4">
          <Truck :size="20" class="text-rose-600" />
          <h3 class="mt-5 text-sm font-black">Giao hàng tận nơi</h3>
          <p class="mt-1 text-[11px] leading-5 text-slate-500">Theo dõi đơn hàng thuận tiện.</p>
        </div>
        <div class="rounded-2xl border border-white/70 bg-white/70 p-4">
          <ShieldCheck :size="20" class="text-emerald-600" />
          <h3 class="mt-5 text-sm font-black">Thanh toán an tâm</h3>
          <p class="mt-1 text-[11px] leading-5 text-slate-500">Nhiều phương thức thanh toán.</p>
        </div>
        <div class="rounded-2xl border border-white/70 bg-white/70 p-4">
          <RefreshCcw :size="20" class="text-indigo-600" />
          <h3 class="mt-5 text-sm font-black">Đổi trả linh hoạt</h3>
          <p class="mt-1 text-[11px] leading-5 text-slate-500">
            Hỗ trợ khi sản phẩm không phù hợp.
          </p>
        </div>
        <div class="rounded-2xl border border-white/70 bg-white/70 p-4">
          <Headphones :size="20" class="text-amber-600" />
          <h3 class="mt-5 text-sm font-black">Hỗ trợ nhanh</h3>
          <p class="mt-1 text-[11px] leading-5 text-slate-500">Chat với shop ngay trên website.</p>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { Headphones, RefreshCcw, ShieldCheck, Truck } from 'lucide-vue-next'
</script>
