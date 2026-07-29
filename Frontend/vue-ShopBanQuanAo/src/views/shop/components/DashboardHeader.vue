<template>
  <div
    class="relative overflow-hidden rounded-[32px] bg-gradient-to-br from-slate-900 via-slate-800 to-indigo-900 text-white shadow-[0_25px_60px_rgba(15,23,42,.35)]"
  >
    <!-- Background -->
    <div class="absolute -top-24 -right-24 w-72 h-72 rounded-full bg-indigo-500/20 blur-3xl"></div>

    <div class="absolute -bottom-20 -left-20 w-72 h-72 rounded-full bg-cyan-400/10 blur-3xl"></div>

    <div class="relative z-10 p-8 lg:p-10">
      <div class="flex flex-col xl:flex-row xl:items-center xl:justify-between gap-8">
        <!-- LEFT -->
        <div>
          <div
            class="inline-flex items-center gap-2 rounded-full bg-white/10 px-4 py-2 backdrop-blur"
          >
            <div class="w-2 h-2 rounded-full bg-emerald-400 animate-pulse"></div>

            <span class="text-sm font-semibold tracking-wide"> Dashboard Analytics </span>
          </div>

          <h1 class="mt-6 text-4xl md:text-5xl font-black tracking-tight">Thống kê bán hàng</h1>

          <p class="mt-4 max-w-3xl text-slate-300 leading-8">
            Theo dõi doanh thu, đơn hàng, khách hàng, sản phẩm bán chạy, hiệu quả kinh doanh và toàn
            bộ hoạt động của cửa hàng theo thời gian thực.
          </p>

          <div class="mt-8 flex flex-wrap gap-4">
            <button
              class="rounded-xl bg-white text-slate-900 font-bold px-6 py-3 hover:scale-105 duration-300 shadow-lg"
            >
              Xuất báo cáo
            </button>

            <button
              class="rounded-xl border border-white/20 bg-white/10 px-6 py-3 backdrop-blur hover:bg-white/20 duration-300"
            >
              Làm mới dữ liệu
            </button>
          </div>
        </div>

        <!-- RIGHT -->
        <div class="grid grid-cols-2 gap-5 min-w-[320px]">
          <div class="rounded-3xl bg-white/10 backdrop-blur-xl border border-white/10 p-5">
            <div class="text-sm text-slate-300">Hôm nay</div>

            <div class="mt-3 text-3xl font-black">18</div>

            <div class="mt-1 text-sm text-emerald-300">Đơn hàng mới</div>
          </div>

          <div class="rounded-3xl bg-white/10 backdrop-blur-xl border border-white/10 p-5">
            <div class="text-sm text-slate-300">Doanh thu</div>

            <div class="mt-3 text-3xl font-black">12.5M</div>

            <div class="mt-1 text-sm text-cyan-300">Trong hôm nay</div>
          </div>

          <div class="rounded-3xl bg-white/10 backdrop-blur-xl border border-white/10 p-5">
            <div class="text-sm text-slate-300">Khách hàng</div>

            <div class="mt-3 text-3xl font-black">+32</div>

            <div class="mt-1 text-sm text-pink-300">Khách mới</div>
          </div>

          <div class="rounded-3xl bg-white/10 backdrop-blur-xl border border-white/10 p-5">
            <div class="text-sm text-slate-300">Tăng trưởng</div>

            <div class="mt-3 text-3xl font-black">+18%</div>

            <div class="mt-1 text-sm text-yellow-300">So với hôm qua</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// Sau này sẽ nhận dữ liệu từ API
</script>
