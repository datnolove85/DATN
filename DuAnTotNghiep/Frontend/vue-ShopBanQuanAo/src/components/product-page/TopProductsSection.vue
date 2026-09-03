<template>
  <div class="w-full bg-[#f5f5f5] font-sans">
    <section class="mx-auto max-w-[1480px] px-4 py-3 md:px-8">
      <div class="w-full rounded-2xl border border-zinc-200/80 bg-white p-3.5 shadow-xs md:p-5">
        <!-- HEADER SECTION & TABS LỌC -->
        <div class="mb-4 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
          <!-- Tiêu đề chính -->
          <div class="relative pb-1.5">
            <h2 class="text-lg font-bold uppercase tracking-tight text-zinc-900 md:text-xl">
              TOP SẢN PHẨM BÁN CHẠY
            </h2>
            <div
              class="absolute -bottom-1 left-0 h-1 w-14 rounded-full bg-gradient-to-r from-red-600 via-red-500 to-transparent"
            ></div>
          </div>

          <!-- Tabs lọc danh mục -->
          <div
            v-if="availableTabs.length > 0"
            class="inline-flex flex-wrap items-center gap-1 rounded-xl bg-zinc-100 p-1 text-xs font-medium text-zinc-600"
          >
            <button
              v-for="tab in availableTabs"
              :key="getTabId(tab)"
              type="button"
              class="rounded-lg px-3 py-1.5 transition-all duration-200"
              :class="
                activeTab === getTabId(tab)
                  ? 'bg-white font-semibold text-red-600 shadow-xs'
                  : 'text-zinc-600 hover:text-zinc-900'
              "
              @click="handleTabSelect(getTabId(tab))"
            >
              {{ getTabName(tab) }}
            </button>
          </div>
        </div>

        <!-- GRID LAYOUT -->
        <div class="grid grid-cols-2 gap-2.5 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 md:gap-3">
          <!-- BANNER THỜI TRANG TRẺ TRUNG & DỄ ĐỌC -->
          <div
            class="relative col-span-2 flex min-h-[210px] select-none flex-col justify-between overflow-hidden rounded-xl bg-gradient-to-br from-zinc-900 via-zinc-850 to-rose-950 p-4 text-white shadow-sm transition-all duration-300 hover:shadow-md md:min-h-[230px] md:p-5"
          >
            <!-- Nền ánh sáng nhẹ -->
            <div
              class="pointer-events-none absolute -right-10 -top-10 h-44 w-44 rounded-full bg-rose-500/20 blur-3xl"
            ></div>
            <div
              class="pointer-events-none absolute -bottom-10 -left-10 h-40 w-40 rounded-full bg-amber-500/15 blur-2xl"
            ></div>

            <!-- Tag tin tức / loại sản phẩm -->
            <div class="relative z-10 flex items-center justify-between">
              <span
                class="inline-flex items-center gap-1.5 rounded-full border border-rose-400/30 bg-rose-500/20 px-2.5 py-1 text-[11px] font-semibold tracking-wide text-rose-200 backdrop-blur-md"
              >
                <span class="h-1.5 w-1.5 animate-pulse rounded-full bg-rose-400"></span>
                NEW COLLECTION 2026
              </span>
              <span class="text-xs font-medium tracking-wider text-zinc-300 uppercase">
                TRENDING NOW
              </span>
            </div>

            <!-- Tiêu đề Banner -->
            <div class="relative z-10 my-auto py-1">
              <p class="text-xs font-semibold uppercase tracking-wider text-rose-300">
                Gợi ý outfit tuần này
              </p>
              <h3
                class="mt-1 text-2xl font-bold uppercase tracking-tight text-white md:text-3xl lg:text-4xl"
              >
                GIẢM TỚI <span class="text-amber-300">50%</span>
              </h3>
              <p class="mt-1 text-xs md:text-sm font-normal text-zinc-200 line-clamp-1">
                Áp dụng cho toàn bộ Áo thun, Hoodie & Quần Jeans chọn lọc
              </p>
            </div>

            <!-- Chân Banner & Nút Mua Hàng -->
            <div class="relative z-10 flex items-center justify-between pt-1">
              <div class="flex items-center gap-1.5 text-xs font-medium text-zinc-200">
                <span class="h-2 w-2 rounded-full bg-emerald-400"></span>
                <span>Freeship từ 299k</span>
              </div>

              <button
                type="button"
                class="group/btn flex items-center gap-1.5 rounded-lg bg-white px-3.5 py-1.5 text-xs font-semibold text-zinc-900 shadow-sm transition duration-200 hover:bg-red-600 hover:text-white active:scale-95"
              >
                <span>Khám phá ngay</span>
                <span class="transition-transform duration-200 group-hover/btn:translate-x-1"
                  >→</span
                >
              </button>
            </div>
          </div>

          <!-- CARD SẢN PHẨM -->
          <article
            v-for="product in displayProducts"
            :key="product.id || product.maSanPham"
            class="group flex flex-col justify-between overflow-hidden rounded-xl border border-zinc-200/80 bg-white p-3 shadow-2xs transition duration-200 hover:-translate-y-0.5 hover:border-red-500 hover:shadow-md"
          >
            <div class="cursor-pointer" @click="$emit('detail', product.id)">
              <div class="relative aspect-square w-full overflow-hidden rounded-lg bg-zinc-50 p-1">
                <img
                  :src="imageUrl(product)"
                  :alt="product.tenSanPham"
                  class="h-full w-full object-contain transition duration-300 group-hover:scale-105"
                  @error="handleImageError"
                />
              </div>

              <!-- Tên sản phẩm: Chữ rõ nét, kích thước chuẩn, không quá đậm -->
              <h3
                class="mt-2 line-clamp-2 h-9 text-xs md:text-sm font-medium leading-snug text-zinc-800 transition group-hover:text-red-600"
                :title="product.tenSanPham"
              >
                {{ product.tenSanPham }}
              </h3>

              <!-- Giá tiền: Nổi bật vừa phải -->
              <div class="mt-1.5 text-sm md:text-base font-bold text-red-600">
                {{ priceLabel(product) }}
              </div>
            </div>

            <!-- Chân card: Đánh giá & Yêu thích -->
            <div
              class="mt-2 flex items-center justify-between border-t border-zinc-100 pt-2 text-xs text-zinc-500"
            >
              <div class="flex items-center gap-1">
                <span class="font-semibold text-amber-600">{{ product.rating || '0.0' }}</span>
                <Star :size="12" class="fill-amber-400 text-amber-400" />
                <span class="text-zinc-400">({{ product.danhGiaCount || 0 }})</span>
              </div>

              <button
                type="button"
                class="flex items-center gap-1 font-medium text-zinc-500 transition hover:text-red-600"
                @click.stop="$emit('favorite', product)"
              >
                <Heart :size="13" />
                <span>Lưu</span>
              </button>
            </div>
          </article>
        </div>

        <!-- Trạng thái trống -->
        <div
          v-if="!displayProducts.length"
          class="py-8 text-center text-xs md:text-sm font-medium text-zinc-500"
        >
          Chưa có sản phẩm thuộc danh mục này.
        </div>

        <!-- NÚT XEM THÊM -->
        <div class="mt-4 flex justify-center">
          <button
            type="button"
            class="rounded-lg bg-red-600 px-7 py-2 text-xs md:text-sm font-semibold text-white shadow-xs transition hover:bg-red-700 active:scale-95"
            @click="$emit('load-more', activeTab)"
          >
            Xem thêm sản phẩm
          </button>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { Star, Heart } from 'lucide-vue-next'

const props = defineProps({
  products: { type: Array, default: () => [] },
  categories: { type: Array, default: () => [] },
  imageUrl: { type: Function, required: true },
  handleImageError: { type: Function, required: true },
  priceLabel: { type: Function, required: true },
})

const emit = defineEmits(['detail', 'favorite', 'add-to-cart', 'tab-change', 'load-more'])

const activeTab = ref('all')

const availableTabs = computed(() => {
  if (props.categories && props.categories.length > 0) {
    return [{ id: 'all', name: 'Tất cả' }, ...props.categories]
  }
  return [{ id: 'all', name: 'Tất cả' }]
})

const getTabId = (tab) => {
  if (typeof tab === 'string') return tab
  return tab.id ?? tab.danhMucId ?? tab.maDanhMuc ?? tab.code ?? tab.name
}

const getTabName = (tab) => {
  if (typeof tab === 'string') return tab
  return tab.name ?? tab.tenDanhMuc ?? tab.label ?? ''
}

const handleTabSelect = (tabId) => {
  activeTab.value = tabId
  emit('tab-change', tabId)
}

const displayProducts = computed(() => {
  if (!props.products || !props.products.length) return []

  if (activeTab.value === 'all') {
    return props.products.slice(0, 8)
  }

  return props.products
    .filter((product) => {
      const catId =
        product.danhMucId ??
        product.idDanhMuc ??
        product.categoryId ??
        product.category?.id ??
        product.category
      return String(catId) === String(activeTab.value)
    })
    .slice(0, 8)
})
</script>
