<template>
  <article
    class="group flex h-full min-w-0 cursor-pointer flex-col overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-sm transition duration-300 hover:-translate-y-1 hover:border-slate-300 hover:shadow-2xl"
    tabindex="0"
    @click="$emit('detail', product.id)"
    @keydown.enter="$emit('detail', product.id)"
  >
    <div class="relative aspect-[4/5] overflow-hidden bg-[#f4f1ec]">
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
          class="rounded-full bg-rose-600 px-2.5 py-1 text-[10px] font-black text-white"
          >-{{ product.maxDiscountPercent }}%</span
        >
        <span
          v-if="isNewProduct(product)"
          class="rounded-full bg-white px-2.5 py-1 text-[10px] font-black text-slate-950 shadow"
          >MỚI</span
        >
      </div>
      <button
        type="button"
        class="absolute right-3 top-3 grid h-9 w-9 place-items-center rounded-full bg-white/95 text-slate-700 shadow-md transition hover:scale-105 hover:text-rose-600"
        @click.stop="$emit('favorite', product.id)"
        :aria-label="isFavorite(product.id) ? 'Bỏ yêu thích' : 'Thêm yêu thích'"
      >
        <Heart
          :size="17"
          :fill="isFavorite(product.id) ? 'currentColor' : 'none'"
          :class="isFavorite(product.id) ? 'text-rose-600' : ''"
        />
      </button>
      <div class="absolute inset-x-3 bottom-3 flex justify-between gap-2">
        <span
          class="rounded-full bg-white/95 px-2.5 py-1 text-[10px] font-black text-slate-900 shadow"
          >{{ product.soLuong > 0 ? 'Còn hàng' : 'Hết hàng' }}</span
        >
        <button
          type="button"
          class="translate-y-2 rounded-full bg-slate-950 px-3 py-1.5 text-[10px] font-black text-white opacity-0 shadow-xl transition group-hover:translate-y-0 group-hover:opacity-100"
          @click.stop="$emit('detail', product.id)"
        >
          Xem nhanh
        </button>
      </div>
    </div>

    <div class="flex flex-1 flex-col p-3.5">
      <div
        class="flex items-center justify-between gap-2 text-[10px] font-black uppercase tracking-[0.12em] text-rose-600"
      >
        <span class="truncate">{{ product.idThuongHieu?.tenThuongHieu || 'HD Fashion' }}</span>
        <span class="shrink-0 font-semibold text-slate-400">{{ product.maSanPham }}</span>
      </div>
      <h3 class="mt-1.5 line-clamp-2 min-h-[42px] text-sm font-black leading-5 text-slate-900">
        {{ product.tenSanPham }}
      </h3>
      <p class="mt-1 line-clamp-2 min-h-[38px] text-[11px] leading-5 text-slate-500">
        {{
          product.moTa ||
          `${product.idDanhMuc?.tenDanhMuc || 'Thời trang'} · ${product.idChatLieu?.tenChatLieu || 'Chất liệu chọn lọc'}`
        }}
      </p>
      <div class="mt-2 flex min-h-5 flex-wrap gap-1">
        <span
          v-if="product.colorCount"
          class="rounded bg-slate-100 px-2 py-1 text-[9px] font-black text-slate-600"
          >{{ product.colorCount }} màu</span
        >
        <span
          v-if="product.sizeCount"
          class="rounded bg-slate-100 px-2 py-1 text-[9px] font-black text-slate-600"
          >{{ product.sizeCount }} size</span
        >
      </div>
      <div class="mt-auto border-t border-slate-100 pt-3">
        <div class="flex items-end justify-between gap-2">
          <div class="min-w-0">
            <div class="truncate text-base font-black text-rose-600">{{ priceLabel(product) }}</div>
            <div
              v-if="
                product.displayDiscountPercent > 0 && product.originalPrice > product.displayPrice
              "
              class="mt-0.5 flex items-center gap-2"
            >
              <span class="text-[11px] text-slate-400 line-through">{{
                formatPrice(product.originalPrice)
              }}</span>
              <span class="text-[10px] font-black text-emerald-600"
                >-{{ product.displayDiscountPercent }}%</span
              >
            </div>
          </div>
          <span class="text-[10px] font-bold text-slate-400">Yêu thích</span>
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
