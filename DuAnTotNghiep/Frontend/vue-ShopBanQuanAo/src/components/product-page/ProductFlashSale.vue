<template>
  <section class="bg-[#f7f4ee]">
    <div class="mx-auto max-w-[1480px] px-4 py-7 md:px-8 md:py-10">
      <div class="flex items-end justify-between gap-4">
        <div>
          <div class="flex items-center gap-2 text-rose-600">
            <Zap :size="17" fill="currentColor" /><span
              class="text-[11px] font-black uppercase tracking-[0.18em]"
              >Flash Sale</span
            >
          </div>
          <h2 class="mt-1 text-2xl font-black tracking-tight md:text-3xl">Ưu đãi nổi bật</h2>
        </div>
        <button
          type="button"
          class="rounded-full bg-slate-950 px-4 py-2 text-[11px] font-black text-white"
        >
          Xem tất cả
        </button>
      </div>

      <div
        v-if="products.length"
        class="mt-5 grid gap-3 sm:grid-cols-2 md:grid-cols-3 xl:grid-cols-6"
      >
        <article
          v-for="product in products"
          :key="product.id"
          class="group overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-sm transition hover:-translate-y-1 hover:shadow-xl"
        >
          <button type="button" class="block w-full text-left" @click="$emit('detail', product.id)">
            <div class="relative aspect-square overflow-hidden bg-slate-100">
              <img
                :src="imageUrl(product)"
                :alt="product.tenSanPham"
                class="h-full w-full object-cover transition duration-500 group-hover:scale-105"
                @error="handleImageError"
              />
              <span
                v-if="product.maxDiscountPercent"
                class="absolute left-2 top-2 rounded-full bg-rose-600 px-2 py-1 text-[10px] font-black text-white"
                >-{{ product.maxDiscountPercent }}%</span
              >
            </div>
            <div class="p-3">
              <h3 class="line-clamp-2 min-h-[36px] text-xs font-black leading-5">
                {{ product.tenSanPham }}
              </h3>
              <div class="mt-2 flex items-end justify-between gap-2">
                <strong class="text-sm font-black text-rose-600">{{ priceLabel(product) }}</strong>
                <span class="text-[10px] font-bold text-slate-400">{{
                  product.soLuong > 0 ? 'Còn hàng' : 'Hết hàng'
                }}</span>
              </div>
            </div>
          </button>
        </article>
      </div>
      <div
        v-else
        class="mt-5 rounded-2xl border border-dashed border-slate-300 bg-white p-8 text-center text-sm text-slate-500"
      >
        Chưa có sản phẩm ưu đãi để hiển thị.
      </div>
    </div>
  </section>
</template>

<script setup>
import { Zap } from 'lucide-vue-next'

defineProps({
  products: { type: Array, default: () => [] },
  imageUrl: { type: Function, required: true },
  handleImageError: { type: Function, required: true },
  priceLabel: { type: Function, required: true },
})
defineEmits(['detail'])
</script>
