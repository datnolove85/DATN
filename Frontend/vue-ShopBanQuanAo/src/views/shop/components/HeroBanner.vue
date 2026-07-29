<template>
  <section
    class="relative overflow-hidden bg-gradient-to-r from-slate-900 via-slate-800 to-indigo-900"
  >
    <!-- Background -->
    <div class="absolute inset-0 opacity-10">
      <div
        class="absolute -top-40 -left-20 w-[500px] h-[500px] rounded-full bg-white blur-3xl"
      ></div>
      <div
        class="absolute bottom-0 right-0 w-[400px] h-[400px] rounded-full bg-indigo-500 blur-3xl"
      ></div>
    </div>

    <div
      class="relative max-w-7xl mx-auto px-6 py-24 lg:py-32 flex flex-col lg:flex-row items-center gap-12"
    >
      <!-- Left -->
      <div class="flex-1 text-white">
        <span
          class="inline-flex items-center px-4 py-1 rounded-full bg-white/10 backdrop-blur text-sm tracking-wide"
        >
          ✨ New Collection 2026
        </span>

        <h1 class="mt-6 text-5xl lg:text-7xl font-black leading-tight tracking-tight">
          Elevate
          <span class="text-indigo-400">Your Style</span>
        </h1>

        <p class="mt-6 text-slate-300 text-lg max-w-xl leading-8">
          Khám phá bộ sưu tập thời trang mới với phong cách tối giản, chất liệu cao cấp và thiết kế
          dành cho mọi khoảnh khắc.
        </p>

        <div class="mt-10 flex flex-wrap gap-4">
          <RouterLink
            to="/san-pham"
            class="px-7 py-4 rounded-xl bg-indigo-600 hover:bg-indigo-700 transition font-semibold shadow-lg"
          >
            Mua ngay
          </RouterLink>

          <RouterLink
            to="/san-pham"
            class="px-7 py-4 rounded-xl border border-white/20 hover:bg-white/10 transition font-semibold"
          >
            Khám phá
          </RouterLink>
        </div>

        <div class="mt-12 flex gap-10">
          <div>
            <div class="text-3xl font-bold">10K+</div>
            <div class="text-slate-400 text-sm">Khách hàng</div>
          </div>

          <div>
            <div class="text-3xl font-bold">500+</div>
            <div class="text-slate-400 text-sm">Sản phẩm</div>
          </div>

          <div>
            <div class="text-3xl font-bold">4.9★</div>
            <div class="text-slate-400 text-sm">Đánh giá</div>
          </div>
        </div>
      </div>

      <!-- Right -->
      <div class="flex-1 flex justify-center">
        <div class="relative">
          <img
            src="https://images.unsplash.com/photo-1523398002811-999ca8dec234?w=700"
            class="w-[520px] rounded-3xl shadow-2xl object-cover"
          />

          <div class="absolute -bottom-6 -left-6 bg-white rounded-2xl p-5 shadow-xl">
            <div class="text-xs text-gray-400">Bộ sưu tập mới</div>

            <div class="font-bold text-lg mt-1">Summer Collection</div>

            <div class="text-indigo-600 font-bold mt-2">Giảm đến 50%</div>
          </div>

          <div
            class="absolute top-6 -right-6 bg-indigo-600 text-white px-5 py-3 rounded-2xl shadow-xl"
          >
            🔥 Bestseller
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { RouterLink } from 'vue-router'
</script>
