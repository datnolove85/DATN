<template>
  <div>
    <div v-if="loading" class="grid grid-cols-2 gap-3 md:grid-cols-3 xl:grid-cols-4">
      <div
        v-for="n in 8"
        :key="n"
        class="overflow-hidden rounded-2xl border border-slate-200 bg-white p-2.5"
      >
        <div class="aspect-[4/5] animate-pulse rounded-xl bg-slate-200" />
        <div class="mt-3 h-3 w-2/5 animate-pulse rounded bg-slate-200" />
        <div class="mt-2 h-5 w-4/5 animate-pulse rounded bg-slate-200" />
        <div class="mt-3 h-6 w-3/5 animate-pulse rounded bg-slate-200" />
      </div>
    </div>

    <div
      v-else-if="errorMessage"
      class="rounded-2xl border border-rose-200 bg-white px-6 py-16 text-center"
    >
      <CircleAlert class="mx-auto text-rose-500" :size="42" />
      <h2 class="mt-4 text-xl font-black">Không tải được sản phẩm</h2>
      <p class="mx-auto mt-2 max-w-lg text-sm text-slate-500">{{ errorMessage }}</p>
      <button
        type="button"
        class="mt-5 rounded-full bg-slate-950 px-5 py-2.5 text-sm font-black text-white"
        @click="$emit('reload')"
      >
        Thử lại
      </button>
    </div>

    <div
      v-else-if="!products.length"
      class="rounded-2xl border border-dashed border-slate-300 bg-white px-6 py-20 text-center"
    >
      <SearchX class="mx-auto text-slate-300" :size="48" />
      <h2 class="mt-4 text-xl font-black">Chưa tìm thấy sản phẩm phù hợp</h2>
      <p class="mt-2 text-sm text-slate-500">Hãy thử đổi từ khóa hoặc bớt bộ lọc.</p>
      <button
        type="button"
        class="mt-5 rounded-full bg-slate-950 px-5 py-2.5 text-sm font-black text-white"
        @click="$emit('clear')"
      >
        Xem toàn bộ sản phẩm
      </button>
    </div>

    <div v-else class="grid grid-cols-2 gap-3 md:grid-cols-3 md:gap-4 xl:grid-cols-4">
      <ProductCard
        v-for="product in products"
        :key="product.id"
        :product="product"
        :image-url="imageUrl"
        :handle-image-error="handleImageError"
        :is-new-product="isNewProduct"
        :is-favorite="isFavorite"
        :format-price="formatPrice"
        :price-label="priceLabel"
        @detail="$emit('detail', $event)"
        @favorite="$emit('favorite', $event)"
      />
    </div>

    <nav
      v-if="!loading && !errorMessage && filteredCount > 0 && totalPages > 1"
      class="mt-8 flex flex-wrap items-center justify-center gap-2"
    >
      <button
        type="button"
        class="grid h-10 w-10 place-items-center rounded-full border border-slate-200 bg-white text-slate-500 disabled:opacity-40"
        :disabled="currentPage === 1"
        @click="$emit('prev')"
      >
        <ChevronLeft :size="17" />
      </button>
      <button
        v-for="page in pageNumbers"
        :key="page"
        type="button"
        class="grid h-10 min-w-10 place-items-center rounded-full px-3 text-xs font-black"
        :class="
          currentPage === page
            ? 'bg-slate-950 text-white'
            : 'border border-slate-200 bg-white text-slate-600 hover:border-rose-300 hover:text-rose-600'
        "
        @click="$emit('page', page)"
      >
        {{ page }}
      </button>
      <button
        type="button"
        class="grid h-10 w-10 place-items-center rounded-full border border-slate-200 bg-white text-slate-500 disabled:opacity-40"
        :disabled="currentPage === totalPages"
        @click="$emit('next')"
      >
        <ChevronRight :size="17" />
      </button>
    </nav>
  </div>
</template>

<script setup>
import { CircleAlert, ChevronLeft, ChevronRight, SearchX } from 'lucide-vue-next'
import ProductCard from './ProductCard.vue'

defineProps({
  products: { type: Array, default: () => [] },
  filteredCount: { type: Number, default: 0 },
  loading: { type: Boolean, default: false },
  errorMessage: { type: String, default: '' },
  totalPages: { type: Number, default: 1 },
  currentPage: { type: Number, default: 1 },
  pageNumbers: { type: Array, default: () => [] },
  imageUrl: { type: Function, required: true },
  handleImageError: { type: Function, required: true },
  isNewProduct: { type: Function, required: true },
  isFavorite: { type: Function, required: true },
  formatPrice: { type: Function, required: true },
  priceLabel: { type: Function, required: true },
})
defineEmits(['detail', 'favorite', 'reload', 'clear', 'prev', 'next', 'page'])
</script>
