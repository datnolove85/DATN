<template>
  <section class="border-b border-slate-200 bg-white">
    <div class="mx-auto max-w-[1480px] px-4 py-6 md:px-8">
      <div class="flex items-end justify-between gap-4">
        <div>
          <p class="text-[11px] font-black uppercase tracking-[0.18em] text-rose-600">Khám phá</p>
          <h2 class="mt-1 text-xl font-black tracking-tight md:text-2xl">Mua theo danh mục</h2>
        </div>
        <button
          type="button"
          class="hidden text-xs font-black text-slate-500 hover:text-slate-950 md:block"
        >
          Xem tất cả
        </button>
      </div>

      <div class="mt-5 grid grid-cols-3 gap-3 sm:grid-cols-5 lg:grid-cols-8">
        <button
          type="button"
          class="group rounded-2xl border px-3 py-4 text-left transition hover:-translate-y-1 hover:border-rose-200 hover:shadow-xl hover:shadow-rose-100"
          :class="
            !activeCategories.length ? 'border-rose-500 bg-rose-50' : 'border-slate-200 bg-white'
          "
          @click="$emit('clear')"
        >
          <div
            class="mb-3 grid h-12 w-12 place-items-center rounded-full bg-slate-950 text-white transition group-hover:scale-105"
          >
            <Grid2X2 :size="19" />
          </div>
          <span class="block truncate text-xs font-black">Tất cả</span>
          <span class="mt-1 block text-[10px] font-semibold text-slate-400"
            >{{ totalCount }} sản phẩm</span
          >
        </button>
        <button
          v-for="(category, index) in categories.slice(0, 15)"
          :key="category.id"
          type="button"
          class="group rounded-2xl border border-slate-200 bg-white px-3 py-4 text-left transition hover:-translate-y-1 hover:border-rose-200 hover:shadow-xl hover:shadow-rose-100"
          :class="activeCategories.includes(category.name) ? 'border-rose-500 bg-rose-50' : ''"
          @click="$emit('toggle', category.name)"
        >
          <div
            class="mb-3 grid h-12 w-12 place-items-center rounded-full text-white transition group-hover:scale-105"
            :class="palette[index % palette.length]"
          >
            <Shirt :size="19" />
          </div>
          <span class="block truncate text-xs font-black" :title="category.name">{{
            category.name
          }}</span>
          <span class="mt-1 block text-[10px] font-semibold text-slate-400">Khám phá ngay</span>
        </button>
      </div>
    </div>
  </section>
</template>

<script setup>
import { Grid2X2, Shirt } from 'lucide-vue-next'

defineProps({
  categories: { type: Array, default: () => [] },
  activeCategories: { type: Array, default: () => [] },
  totalCount: { type: Number, default: 0 },
})
defineEmits(['toggle', 'clear'])
const palette = [
  'bg-slate-950',
  'bg-rose-600',
  'bg-amber-500',
  'bg-emerald-600',
  'bg-indigo-600',
  'bg-sky-600',
]
</script>
