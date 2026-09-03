<template>
  <div class="min-h-screen bg-[#f6f1e6] text-zinc-900">
    <PublicSiteHeader />
    <section class="bg-[#800000] text-white">
      <div class="mx-auto max-w-[1480px] px-6 py-14 lg:px-10">
        <p class="text-xs font-black uppercase tracking-[0.28em] text-amber-300">K-ZONE SHOWROOM</p>
        <h1 class="mt-2 text-4xl font-black uppercase md:text-6xl">Hệ thống cửa hàng</h1>
        <p class="mt-4 max-w-2xl text-sm leading-7 text-red-100">
          Các showroom minh hoạ của K-ZONE để bạn có thể trình bày giao diện hệ thống cửa hàng trong
          đồ án.
        </p>
      </div>
    </section>

    <main class="mx-auto max-w-[1480px] px-6 py-10 lg:px-10">
      <section class="grid gap-5 lg:grid-cols-4">
        <div class="rounded-2xl bg-white p-5 shadow-sm ring-1 ring-black/5">
          <p class="text-xs text-zinc-500">Showroom</p>
          <p class="mt-2 text-3xl font-black text-red-700">04</p>
        </div>
        <div class="rounded-2xl bg-white p-5 shadow-sm ring-1 ring-black/5">
          <p class="text-xs text-zinc-500">Khu vực</p>
          <p class="mt-2 text-3xl font-black">03</p>
        </div>
        <div class="rounded-2xl bg-white p-5 shadow-sm ring-1 ring-black/5">
          <p class="text-xs text-zinc-500">Giờ mở cửa</p>
          <p class="mt-2 text-lg font-black">08:30 — 22:00</p>
        </div>
        <div class="rounded-2xl bg-white p-5 shadow-sm ring-1 ring-black/5">
          <p class="text-xs text-zinc-500">Hotline</p>
          <p class="mt-2 text-lg font-black">1900 6750</p>
        </div>
      </section>

      <section class="mt-8 grid gap-6 md:grid-cols-2 xl:grid-cols-4">
        <article
          v-for="store in stores"
          :key="store.id"
          class="group overflow-hidden rounded-3xl bg-white shadow-sm ring-1 ring-black/5"
        >
          <div class="relative aspect-[4/3] overflow-hidden">
            <img
              :src="store.image"
              :alt="store.name"
              class="h-full w-full object-cover transition duration-700 group-hover:scale-105"
            />
            <span
              class="absolute left-4 top-4 rounded-full bg-white/95 px-3 py-1 text-[10px] font-black uppercase text-red-700"
              >{{ store.region }}</span
            >
          </div>
          <div class="p-5">
            <h2 class="text-lg font-black">{{ store.name }}</h2>
            <p class="mt-2 text-sm font-semibold text-zinc-700">{{ store.city }}</p>
            <p class="mt-1 text-xs leading-5 text-zinc-500">{{ store.address }}</p>
            <div class="mt-4 flex gap-2">
              <button
                class="flex-1 rounded-xl bg-[#800000] px-3 py-2.5 text-xs font-bold text-white hover:bg-red-700"
                @click="selected = store"
              >
                Xem chi tiết
              </button>
              <button
                class="rounded-xl border border-zinc-200 px-3 py-2.5 text-xs font-bold text-zinc-700 hover:border-red-300 hover:text-red-600"
                @click="selected = store"
              >
                Chỉ đường
              </button>
            </div>
          </div>
        </article>
      </section>

      <section class="mt-10 grid gap-6 lg:grid-cols-[1.1fr_.9fr]">
        <div class="overflow-hidden rounded-3xl bg-zinc-950 p-8 text-white md:p-10">
          <p class="text-xs font-black uppercase tracking-[0.2em] text-amber-300">
            Trải nghiệm showroom
          </p>
          <h2 class="mt-3 text-3xl font-black">
            Không gian thời trang gọn, hiện đại và dễ khám phá.
          </h2>
          <p class="mt-4 text-sm leading-7 text-zinc-300">
            Khu thử đồ, khu sản phẩm mới, khu phụ kiện và quầy thanh toán được bố trí rõ ràng để tạo
            cảm giác showroom thật.
          </p>
        </div>
        <div class="relative min-h-[280px] overflow-hidden rounded-3xl bg-zinc-900">
          <img
            src="https://images.unsplash.com/photo-1441986300917-64674bd600d8?q=80&w=1400&auto=format&fit=crop"
            alt="Showroom"
            class="absolute inset-0 h-full w-full object-cover opacity-85"
          />
          <div class="absolute inset-0 bg-gradient-to-t from-black/70 to-transparent"></div>
          <div class="absolute bottom-6 left-6 text-white">
            <p class="text-xs font-black uppercase tracking-widest text-amber-300">K-ZONE</p>
            <p class="mt-1 text-2xl font-black">Shop in style.</p>
          </div>
        </div>
      </section>
    </main>

    <div
      v-if="selected"
      class="fixed inset-0 z-[100] grid place-items-center bg-black/65 p-4"
      @click.self="selected = null"
    >
      <div class="w-full max-w-xl overflow-hidden rounded-3xl bg-white">
        <img :src="selected.image" :alt="selected.name" class="h-56 w-full object-cover" />
        <div class="p-6">
          <p class="text-xs font-black uppercase tracking-wider text-red-600">
            {{ selected.region }}
          </p>
          <h2 class="mt-1 text-2xl font-black">{{ selected.name }}</h2>
          <p class="mt-3 text-sm font-semibold">{{ selected.city }}</p>
          <p class="mt-1 text-sm text-zinc-500">{{ selected.address }}</p>
          <p class="mt-4 text-sm text-zinc-600">Giờ mở cửa: 08:30 — 22:00 · Hotline: 1900 6750</p>
          <button
            class="mt-6 rounded-xl bg-zinc-900 px-5 py-3 text-sm font-bold text-white"
            @click="selected = null"
          >
            Đóng
          </button>
        </div>
      </div>
    </div>

    <ShopFooter />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import PublicSiteHeader from '@/components/public/PublicSiteHeader.vue'
import ShopFooter from './components/ShopFooter.vue'

const selected = ref(null)
const stores = [
  {
    id: 1,
    name: 'K-ZONE Hà Nội',
    city: 'Hà Nội',
    region: 'Miền Bắc',
    address: '123 Cầu Giấy, Quận Cầu Giấy',
    image:
      'https://images.unsplash.com/photo-1556740758-90de374c12ad?q=80&w=1000&auto=format&fit=crop',
  },
  {
    id: 2,
    name: 'K-ZONE Đà Nẵng',
    city: 'Đà Nẵng',
    region: 'Miền Trung',
    address: '68 Nguyễn Văn Linh, Hải Châu',
    image:
      'https://images.unsplash.com/photo-1604014237800-1c9102c219da?q=80&w=1000&auto=format&fit=crop',
  },
  {
    id: 3,
    name: 'K-ZONE Sài Gòn',
    city: 'TP. Hồ Chí Minh',
    region: 'Miền Nam',
    address: '86 Nguyễn Trãi, Quận 1',
    image:
      'https://images.unsplash.com/photo-1497366754035-f200968a6e72?q=80&w=1000&auto=format&fit=crop',
  },
  {
    id: 4,
    name: 'K-ZONE Thủ Đức',
    city: 'TP. Hồ Chí Minh',
    region: 'Miền Nam',
    address: '24 Võ Văn Ngân, TP. Thủ Đức',
    image:
      'https://images.unsplash.com/photo-1441986300917-64674bd600d8?q=80&w=1000&auto=format&fit=crop',
  },
]
</script>
