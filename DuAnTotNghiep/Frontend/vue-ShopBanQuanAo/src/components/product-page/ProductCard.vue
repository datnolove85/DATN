<template>
  <article
    class="group flex h-full min-w-0 cursor-pointer flex-col overflow-hidden rounded-2xl border border-zinc-800 bg-zinc-900 text-zinc-100 shadow-lg transition duration-300 hover:-translate-y-1 hover:border-zinc-700 hover:shadow-2xl hover:shadow-red-950/30"
    tabindex="0"
    @click="$emit('detail', product.id)"
    @keydown.enter="$emit('detail', product.id)"
  >
    <div class="relative aspect-square overflow-hidden bg-zinc-950">
      <img
        :src="imageUrl(product)"
        :alt="product.tenSanPham"
        loading="lazy"
        class="h-full w-full object-cover transition duration-700 group-hover:scale-105"
        @error="handleImageError"
      />
      <div class="absolute left-3 top-3 flex flex-col gap-1.5">
        <span
          v-if="product.dangGiamGia && product.maxDiscountPercent"
          class="rounded-full bg-red-600 px-2.5 py-1 text-[10px] font-black text-white shadow-md"
          >-{{ product.maxDiscountPercent }}%</span
        >
        <span
          v-if="isNewProduct(product)"
          class="rounded-full bg-amber-500 px-2.5 py-1 text-[10px] font-black text-zinc-950 shadow-md"
          >NEW</span
        >
      </div>
      <button
        type="button"
        class="absolute right-3 top-3 grid h-9 w-9 place-items-center rounded-full bg-zinc-900/90 text-zinc-300 shadow-md transition hover:scale-105 hover:bg-red-600 hover:text-white"
        @click.stop="$emit('favorite', product.id)"
        :aria-label="isFavorite(product.id) ? 'Bỏ yêu thích' : 'Thêm yêu thích'"
      >
        <Heart
          :size="17"
          :fill="isFavorite(product.id) ? 'currentColor' : 'none'"
          :class="isFavorite(product.id) ? 'text-red-500 hover:text-white' : ''"
        />
      </button>
      <div class="absolute inset-x-3 bottom-3 flex justify-between gap-2">
        <span
          class="rounded-full bg-zinc-900/90 px-2.5 py-1 text-[10px] font-black text-zinc-300 shadow backdrop-blur-sm"
          >{{ product.soLuong > 0 ? 'Sẵn hàng' : 'Hết hàng' }}</span
        >
        <button
          type="button"
          class="translate-y-2 rounded-full bg-red-600 px-3 py-1.5 text-[10px] font-black text-white opacity-0 shadow-xl transition group-hover:translate-y-0 group-hover:opacity-100 hover:bg-red-700"
          @click.stop="$emit('detail', product.id)"
        >
          Xem nhanh
        </button>
      </div>
    </div>

    <div class="flex flex-1 flex-col p-4">
      <div
        class="flex items-center justify-between gap-2 text-[10px] font-black uppercase tracking-[0.12em] text-amber-500"
      >
        <span class="truncate">{{ product.idThuongHieu?.tenThuongHieu || 'WOLF MUSIC' }}</span>
        <span class="shrink-0 font-semibold text-zinc-500">{{ product.maSanPham }}</span>
      </div>
      <h3
        class="mt-1.5 line-clamp-2 min-h-[42px] text-sm font-black leading-5 text-white group-hover:text-amber-400"
      >
        {{ product.tenSanPham }}
      </h3>
      <p class="mt-1 line-clamp-2 min-h-[38px] text-[11px] leading-5 text-zinc-400">
        {{
          product.moTa ||
          `${product.idDanhMuc?.tenDanhMuc || 'Nhạc cụ'} · ${product.idChatLieu?.tenChatLieu || 'Chính hãng 100%'}`
        }}
      </p>
      <div class="mt-2 flex min-h-5 flex-wrap gap-1">
        <span
          v-if="product.colorCount"
          class="rounded bg-zinc-800 px-2 py-0.5 text-[9px] font-bold text-zinc-300"
          >{{ product.colorCount }} phiên bản</span
        >
        <span
          class="rounded bg-red-950/60 border border-red-900/50 px-2 py-0.5 text-[9px] font-bold text-red-400"
          >Bảo hành 12T</span
        >
      </div>
      <div class="mt-auto border-t border-zinc-800/80 pt-3">
        <div class="flex items-end justify-between gap-2">
          <div class="min-w-0">
            <div class="truncate text-base font-black text-red-500">{{ priceLabel(product) }}</div>
            <div
              v-if="
                product.displayDiscountPercent > 0 && product.originalPrice > product.displayPrice
              "
              class="mt-0.5 flex items-center gap-2"
            >
              <span class="text-[11px] text-zinc-500 line-through">{{
                formatPrice(product.originalPrice)
              }}</span>
              <span class="text-[10px] font-black text-amber-500"
                >-{{ product.displayDiscountPercent }}%</span
              >
            </div>
          </div>
          <span class="text-[10px] font-bold text-zinc-500 group-hover:text-zinc-300"
            >Chi tiết</span
          >
        </div>
      </div>
    </div>
  </article>
</template>

<script setup>
import { Heart } from 'lucide-vue-next'

defineProps({
  product: { type: Object, required: true },
  imageUrl: { type: Function, required: true },
  handleImageError: { type: Function, required: true },
  isNewProduct: { type: Function, required: true },
  isFavorite: { type: Function, required: true },
  formatPrice: { type: Function, required: true },
  priceLabel: { type: Function, required: true },
})
defineEmits(['detail', 'favorite'])
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  overflow: hidden;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
}
</style>
