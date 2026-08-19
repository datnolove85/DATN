<template>
  <section class="home-hero relative overflow-hidden">
    <div class="home-hero-grain"></div>
    <div class="home-hero-glow home-hero-glow-one"></div>
    <div class="home-hero-glow home-hero-glow-two"></div>

    <div
      class="app-container relative grid min-h-[680px] items-center gap-12 py-16 lg:grid-cols-[1.02fr_.98fr] lg:py-20"
    >
      <div class="relative z-10 motion-reveal">
        <div
          class="inline-flex items-center gap-2 rounded-full border border-white/15 bg-white/8 px-4 py-2 text-[11px] font-black uppercase tracking-[0.18em] text-white/80 backdrop-blur-xl"
        >
          <span
            class="h-1.5 w-1.5 rounded-full bg-[var(--lux-gold)] shadow-[0_0_14px_rgba(189,140,77,.8)]"
          ></span>
          New Collection 2026
        </div>

        <h1
          class="mt-7 max-w-3xl text-5xl font-black leading-[.95] tracking-[-0.06em] text-white sm:text-6xl lg:text-[clamp(4.5rem,7vw,7rem)]"
        >
          Elevate
          <span class="block text-[var(--lux-gold-soft)]">Your Style</span>
        </h1>

        <p class="mt-7 max-w-xl text-base leading-8 text-white/65 md:text-lg">
          Khám phá bộ sưu tập thời trang mới với phong cách tối giản, chất liệu cao cấp và thiết kế
          dành cho mọi khoảnh khắc.
        </p>

        <div class="mt-9 flex flex-wrap gap-3">
          <RouterLink to="/san-pham" class="hero-btn hero-btn-primary"
            >Mua ngay <ArrowRight :size="17"
          /></RouterLink>
          <RouterLink to="/san-pham" class="hero-btn hero-btn-secondary"
            >Khám phá <ArrowUpRight :size="17"
          /></RouterLink>
        </div>

        <div class="mt-12 grid max-w-xl grid-cols-3 border-t border-white/12 pt-6">
          <div>
            <div class="text-2xl font-black text-white md:text-3xl">10K+</div>
            <div class="mt-1 text-xs font-semibold text-white/45">Khách hàng</div>
          </div>
          <div class="border-l border-white/12 pl-5">
            <div class="text-2xl font-black text-white md:text-3xl">500+</div>
            <div class="mt-1 text-xs font-semibold text-white/45">Sản phẩm</div>
          </div>
          <div class="border-l border-white/12 pl-5">
            <div class="text-2xl font-black text-white md:text-3xl">4.9★</div>
            <div class="mt-1 text-xs font-semibold text-white/45">Đánh giá</div>
          </div>
        </div>
      </div>

      <div
        class="relative z-10 flex justify-center lg:justify-end motion-reveal"
        style="--reveal-delay: 120ms"
      >
        <div class="home-hero-media-wrap">
          <div class="home-hero-media">
            <img
              src="https://images.unsplash.com/photo-1523398002811-999ca8dec234?w=1000&q=85"
              alt="Bộ sưu tập thời trang mới"
              class="h-full w-full object-cover"
            />
            <div
              class="absolute inset-0 bg-gradient-to-t from-black/35 via-transparent to-white/5"
            ></div>
          </div>
          <div class="home-hero-floating home-hero-floating-left">
            <span>Bộ sưu tập mới</span>
            <strong>Summer Collection</strong>
            <b>Giảm đến 50%</b>
          </div>
          <div class="home-hero-floating home-hero-floating-right">
            <Sparkles :size="15" /> Bestseller
          </div>
        </div>
      </div>
    </div>

    <div
      class="absolute bottom-6 left-1/2 hidden -translate-x-1/2 items-center gap-3 text-[10px] font-black uppercase tracking-[0.22em] text-white/35 lg:flex"
    >
      <span class="h-px w-10 bg-white/20"></span> Scroll to explore
      <span class="h-px w-10 bg-white/20"></span>
    </div>
  </section>
</template>

<script setup>
import { ArrowRight, ArrowUpRight, Sparkles } from 'lucide-vue-next'
import { RouterLink } from 'vue-router'
</script>
