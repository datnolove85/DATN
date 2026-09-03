<template>
  <div class="w-full bg-slate-100 font-sans py-4">
    <!-- Cập nhật px-4 md:px-8 để chuẩn hóa lề hai bên -->
    <section class="mx-auto max-w-[1480px] px-4 md:px-8">
      <div class="grid grid-cols-1 gap-4 lg:grid-cols-12">
        <!-- Banner 1: Left Slider (Khuyến mãi chính - Deal Sốc Thời Trang) -->
        <div
          class="relative overflow-hidden rounded-2xl border border-orange-300 shadow-lg lg:col-span-7 group"
        >
          <div
            class="flex transition-transform duration-500 ease-out h-full"
            :style="{ transform: `translateX(-${currentSlide * 100}%)` }"
          >
            <div
              v-for="(slide, index) in slides"
              :key="index"
              class="relative flex min-h-[260px] min-w-full flex-col justify-between p-5 md:min-h-[320px] md:p-7 bg-gradient-to-r from-red-600 via-orange-500 to-amber-400 text-white select-none"
            >
              <!-- Background Decorative Elements -->
              <div
                class="absolute -right-12 -top-12 h-48 w-48 rounded-full bg-yellow-300/20 blur-2xl pointer-events-none"
              ></div>
              <div
                class="absolute left-1/3 bottom-0 h-32 w-full bg-gradient-to-t from-black/20 to-transparent pointer-events-none"
              ></div>

              <!-- Top Row: Date Badge & Service Badges -->
              <div class="relative z-10 flex flex-wrap items-center gap-2">
                <!-- Date Badge -->
                <span
                  class="rounded-md bg-white/20 px-2.5 py-1 text-[11px] font-extrabold uppercase tracking-wide text-white backdrop-blur-md border border-white/30 shadow-sm"
                >
                  📅 {{ slide.dateRange || '12.09.2026 – 22.09.2026' }}
                </span>

                <!-- Feature Mini Cards -->
                <div class="hidden sm:flex items-center gap-1.5 text-[10px] font-bold">
                  <span class="rounded bg-white px-2 py-0.5 text-red-600 shadow-sm"
                    >⚡ SẮM OUTFIT GIÁ TỐT</span
                  >
                  <span class="rounded bg-white px-2 py-0.5 text-amber-700 shadow-sm"
                    >💳 ĐỔI TRẢ 30 NGÀY</span
                  >
                  <span class="rounded bg-white px-2 py-0.5 text-emerald-700 shadow-sm"
                    >🚚 FREESHIP ĐƠN 99K</span
                  >
                  <span class="rounded bg-white px-2 py-0.5 text-red-700 shadow-sm"
                    >🛡️ HÀNG CHÍNH HÃNG 100%</span
                  >
                </div>
              </div>

              <!-- Middle Row: Banner Titles & Green Sale Tag -->
              <div
                class="relative z-10 my-3 flex flex-col md:flex-row md:items-center justify-between gap-4"
              >
                <div class="max-w-xl">
                  <h2
                    class="text-3xl font-black uppercase italic tracking-tight text-white drop-shadow-[0_3px_5px_rgba(0,0,0,0.4)] md:text-5xl leading-none"
                  >
                    {{ slide.title }}
                    <span
                      class="block text-yellow-300 font-black mt-1 drop-shadow-[0_2px_4px_rgba(0,0,0,0.6)]"
                    >
                      {{ slide.highlight }}
                    </span>
                  </h2>
                  <p class="mt-2 text-xs md:text-sm font-bold text-amber-100 drop-shadow-sm">
                    {{ slide.subtitle }}
                  </p>
                </div>

                <!-- Green Chalkboard Sale Badge -->
                <div
                  class="self-start md:self-center shrink-0 transform rotate-3 transition hover:scale-105"
                >
                  <div
                    class="rounded-xl bg-emerald-800 border-2 border-emerald-400 p-2.5 shadow-xl text-center min-w-[120px]"
                  >
                    <span
                      class="block text-[10px] font-black uppercase text-emerald-200 tracking-wider"
                      >SALE UP TO</span
                    >
                    <span
                      class="text-2xl md:text-3xl font-black text-yellow-300 leading-none drop-shadow"
                      >50%</span
                    >
                    <button
                      type="button"
                      class="mt-1 block w-full rounded bg-amber-400 py-1 text-[10px] font-black uppercase text-slate-950 shadow hover:bg-amber-300"
                      @click="$emit('select-banner', slide)"
                    >
                      MUA NGAY!
                    </button>
                  </div>
                </div>
              </div>

              <!-- Bottom Showcase Area / Product Chips -->
              <div class="relative z-10 flex items-end justify-between pt-2">
                <div class="flex items-center gap-2 overflow-x-auto pb-1 max-w-[75%] no-scrollbar">
                  <div
                    v-for="(item, i) in slide.products || defaultProducts"
                    :key="i"
                    class="shrink-0 bg-white/10 backdrop-blur-md rounded-lg p-1.5 border border-white/20 flex items-center gap-2"
                  >
                    <div
                      class="h-8 w-8 rounded bg-white/20 flex items-center justify-center text-lg"
                    >
                      {{ item.icon }}
                    </div>
                    <div class="text-[10px] leading-tight">
                      <p class="font-bold truncate max-w-[95px]">{{ item.name }}</p>
                      <p class="text-yellow-300 font-extrabold">{{ item.price }}</p>
                    </div>
                  </div>
                </div>

                <!-- Navigation Dots -->
                <div
                  class="flex items-center gap-1.5 bg-black/30 px-3 py-1.5 rounded-full backdrop-blur-md"
                >
                  <button
                    v-for="(_, idx) in slides"
                    :key="idx"
                    type="button"
                    class="h-2 rounded-full transition-all"
                    :class="
                      currentSlide === idx ? 'w-6 bg-yellow-400' : 'w-2 bg-white/60 hover:bg-white'
                    "
                    @click="currentSlide = idx"
                  />
                </div>
              </div>
            </div>
          </div>

          <!-- Prev/Next Arrow Buttons -->
          <button
            type="button"
            class="absolute left-2 top-1/2 -translate-y-1/2 h-9 w-9 rounded-full bg-white/80 hover:bg-white text-slate-800 flex items-center justify-center shadow-md transition opacity-0 group-hover:opacity-100"
            @click="prevSlide"
          >
            ❮
          </button>
          <button
            type="button"
            class="absolute right-2 top-1/2 -translate-y-1/2 h-9 w-9 rounded-full bg-white/80 hover:bg-white text-slate-800 flex items-center justify-center shadow-md transition opacity-0 group-hover:opacity-100"
            @click="nextSlide"
          >
            ❯
          </button>
        </div>

        <!-- Banner 2: Right Static/Promo Banner (Back To School / Outfit Gen Z) -->
        <div
          class="relative flex min-h-[260px] flex-col justify-between overflow-hidden rounded-2xl border border-sky-300 bg-gradient-to-b from-sky-400 via-sky-300 to-amber-200 p-5 md:min-h-[320px] md:p-7 shadow-lg lg:col-span-5 select-none"
        >
          <!-- Background Grid / Notebook Line Effect -->
          <div
            class="absolute inset-0 bg-[linear-gradient(to_right,#0000000a_1px,transparent_1px),linear-gradient(to_bottom,#0000000a_1px,transparent_1px)] bg-[size:16px_16px] pointer-events-none"
          ></div>

          <!-- Pinned Note Cards -->
          <div class="relative z-10 flex items-start justify-between">
            <div
              class="relative -rotate-3 rounded-lg bg-white p-2.5 shadow-md border border-slate-200 max-w-[140px] transform hover:rotate-0 transition"
            >
              <span class="absolute -top-2 left-1/2 -translate-x-1/2 text-red-600 text-sm">📌</span>
              <p
                class="text-[10px] font-black text-slate-800 text-center uppercase leading-tight pt-1"
              >
                SẮM OUTFIT <br /><span class="text-blue-600 text-xs">GIÁ TỐT</span>
              </p>
            </div>

            <div
              class="relative rotate-2 rounded-lg bg-white p-2.5 shadow-md border border-slate-200 max-w-[140px] transform hover:rotate-0 transition"
            >
              <span class="absolute -top-2 left-1/2 -translate-x-1/2 text-red-600 text-sm">📌</span>
              <p
                class="text-[10px] font-black text-slate-800 text-center uppercase leading-tight pt-1"
              >
                MUA 2 TẶNG 1 <br /><span class="text-red-600 text-xs">CỰC HẤP DẪN</span>
              </p>
            </div>
          </div>

          <!-- Main Title & Green Blackboard Badge -->
          <div class="relative z-10 my-2 flex items-center justify-between gap-2">
            <div>
              <h2
                class="text-3xl font-black uppercase tracking-tight text-blue-950 italic md:text-4xl drop-shadow-[0_2px_0_#fff]"
              >
                BACK TO <br />
                <span class="text-sky-600 drop-shadow-[0_2px_0_#fff]">SCHOOL</span>
              </h2>
              <div
                class="mt-2 inline-block rounded-md bg-amber-400 px-2.5 py-1 text-xs font-black text-red-950 shadow-sm border border-amber-300"
              >
                👕 OUTFIT ĐẾN TRƯỜNG CỰC CHILL
              </div>
            </div>

            <!-- Green Sale Tag -->
            <div class="shrink-0 -rotate-6 transform hover:rotate-0 transition">
              <div
                class="rounded-xl bg-emerald-800 border-2 border-emerald-400 p-2.5 shadow-xl text-center"
              >
                <span class="block text-[9px] font-black uppercase text-emerald-200"
                  >SALE UP TO</span
                >
                <span class="text-2xl font-black text-yellow-300 leading-none">49%</span>
              </div>
            </div>
          </div>

          <!-- Bottom Action & Pinned Note + Arrow Navigation -->
          <div class="relative z-10 flex items-end justify-between">
            <div
              class="relative rotate-1 rounded-lg bg-white p-2 shadow-md border border-slate-200"
            >
              <span class="absolute -top-2 left-3 text-red-600 text-xs">📌</span>
              <p class="text-[10px] font-bold text-blue-900 pt-0.5">
                MIỄN PHÍ <span class="font-black text-red-600">VẬN CHUYỂN</span>
              </p>
            </div>

            <button
              type="button"
              class="flex h-11 w-11 items-center justify-center rounded-full bg-white text-blue-600 shadow-lg border border-blue-100 transition hover:bg-amber-300 hover:text-slate-900 active:scale-95"
              @click="$emit('open-flash-sale')"
            >
              <span class="text-xl font-black">➔</span>
            </button>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const props = defineProps({
  customSlides: {
    type: Array,
    default: null,
  },
  flashSaleTitle: {
    type: String,
    default: 'BACK TO SCHOOL - OUTFIT GIÁ CHILL',
  },
})

defineEmits(['select-banner', 'open-flash-sale'])

const defaultProducts = [
  { icon: '👕', name: 'Áo Thun Unisex', price: 'Chỉ từ 99k' },
  { icon: '👖', name: 'Quần Jeans Baggy', price: 'Giảm 40%' },
  { icon: '🧥', name: 'Áo Khoác Jacket', price: 'Tặng Voucher 50k' },
]

const defaultSlides = [
  {
    dateRange: '12.09.2026 – 22.09.2026',
    title: 'DEAL SỐC',
    highlight: 'GIỮA THÁNG',
    subtitle: 'Săn deal Áo thun, Quần Jeans & Jacket cực chất - Giảm kịch sàn!',
    products: defaultProducts,
    link: '/category/fashion-sale',
  },
  {
    dateRange: 'ƯU ĐÃI THÀNH VIÊN',
    title: 'BST STREETWEAR',
    highlight: 'XU HƯỚNG MỚI',
    subtitle: 'Tặng ngay Mũ Cùng Tone cho đơn hàng từ 499k',
    products: [
      { icon: '👗', name: 'Đầm Style GenZ', price: 'Đồng giá 199k' },
      { icon: '👟', name: 'Giày Sneaker', price: 'Giảm 35%' },
    ],
    link: '/category/streetwear',
  },
]

const slides = props.customSlides || defaultSlides
const currentSlide = ref(0)
let timer = null

const nextSlide = () => {
  currentSlide.value = (currentSlide.value + 1) % slides.length
}

const prevSlide = () => {
  currentSlide.value = (currentSlide.value - 1 + slides.length) % slides.length
}

onMounted(() => {
  timer = setInterval(nextSlide, 4500)
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>

<style scoped>
.no-scrollbar::-webkit-scrollbar {
  display: none;
}
.no-scrollbar {
  -ms-overflow-style: none;
  scrollbar-width: none;
}
</style>
