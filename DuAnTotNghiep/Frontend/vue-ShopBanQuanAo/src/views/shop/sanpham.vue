<template>
  <div class="min-h-screen bg-white text-slate-950">
    <ProductThemeHeader v-model="filters.keyword" />

    <section class="border-b border-slate-200 bg-[#fbfaf8]">
      <div class="mx-auto max-w-[1480px] px-4 py-7 md:px-8 md:py-9">
        <div class="flex flex-col gap-5 lg:flex-row lg:items-end lg:justify-between">
          <div>
            <div class="flex flex-wrap items-center gap-2 text-[11px] font-semibold text-slate-400">
              <span>Trang chủ</span><span>/</span><span class="text-slate-900">Sản phẩm</span>
            </div>
            <p class="mt-5 text-[11px] font-black uppercase tracking-[0.2em] text-rose-600">
              Cửa hàng thời trang
            </p>
            <h1 class="mt-1 text-3xl font-black tracking-tight md:text-5xl">Tất cả sản phẩm</h1>
            <p class="mt-3 max-w-2xl text-sm leading-6 text-slate-500">
              Chọn theo danh mục, thương hiệu, chất liệu hoặc mức giá. Tất cả thông tin dưới đây lấy
              từ dữ liệu sản phẩm hiện có của hệ thống.
            </p>
          </div>
          <div class="grid grid-cols-2 gap-2 sm:grid-cols-4">
            <div class="rounded-2xl border border-slate-200 bg-white px-4 py-3">
              <span class="block text-[10px] font-bold text-slate-400">Sản phẩm</span
              ><strong class="mt-1 block text-xl font-black">{{ stats.products }}</strong>
            </div>
            <div class="rounded-2xl border border-slate-200 bg-white px-4 py-3">
              <span class="block text-[10px] font-bold text-slate-400">Thương hiệu</span
              ><strong class="mt-1 block text-xl font-black">{{ stats.brands }}</strong>
            </div>
            <div class="rounded-2xl border border-rose-100 bg-rose-50 px-4 py-3">
              <span class="block text-[10px] font-bold text-rose-500">Đang ưu đãi</span
              ><strong class="mt-1 block text-xl font-black text-rose-600">{{
                stats.onSale
              }}</strong>
            </div>
            <div class="rounded-2xl border border-emerald-100 bg-emerald-50 px-4 py-3">
              <span class="block text-[10px] font-bold text-emerald-600">Còn hàng</span
              ><strong class="mt-1 block text-xl font-black text-emerald-700">{{
                stats.inStock
              }}</strong>
            </div>
          </div>
        </div>
      </div>
    </section>

    <ProductCategoryShowcase
      :categories="dynamicFilters[0]?.options || []"
      :active-categories="filters.categories"
      :total-count="stats.products"
      @toggle="toggleQuickCategory"
      @clear="filters.categories.splice(0)"
    />

    <ProductFlashSale
      :products="visibleProducts.filter((p) => p.dangGiamGia).slice(0, 6)"
      :image-url="imageUrl"
      :handle-image-error="handleImageError"
      :price-label="priceLabel"
      @detail="goToDetail"
    />

    <main class="mx-auto max-w-[1480px] px-4 py-8 md:px-8 md:py-10">
      <section
        class="mb-5 flex flex-col gap-4 rounded-2xl border border-slate-200 bg-white p-4 shadow-sm md:flex-row md:items-center md:justify-between md:p-5"
      >
        <div class="flex items-center gap-3">
          <button
            type="button"
            class="inline-flex items-center gap-2 rounded-full border border-slate-200 px-4 py-2 text-xs font-black text-slate-700 hover:border-rose-300 hover:text-rose-600 lg:hidden"
            @click="mobileFiltersOpen = true"
          >
            <SlidersHorizontal :size="16" /> Bộ lọc
            <span
              v-if="activeFilterChips.length"
              class="grid h-5 min-w-5 place-items-center rounded-full bg-rose-600 px-1 text-[10px] text-white"
              >{{ activeFilterChips.length }}</span
            >
          </button>
          <div>
            <p class="text-[10px] font-black uppercase tracking-[0.14em] text-slate-400">
              Danh sách
            </p>
            <p class="mt-1 text-sm font-black">{{ filteredProducts.length }} sản phẩm phù hợp</p>
          </div>
        </div>
        <div class="flex flex-wrap items-center gap-2">
          <label
            class="inline-flex cursor-pointer items-center gap-2 rounded-full bg-emerald-50 px-3 py-2 text-[11px] font-black text-emerald-700"
            ><input
              v-model="filters.inStockOnly"
              type="checkbox"
              class="h-4 w-4 accent-emerald-600"
            />
            Còn hàng</label
          >
          <label
            class="inline-flex cursor-pointer items-center gap-2 rounded-full bg-rose-50 px-3 py-2 text-[11px] font-black text-rose-700"
            ><input v-model="filters.onSaleOnly" type="checkbox" class="h-4 w-4 accent-rose-600" />
            Giảm giá</label
          >
          <select
            v-model="sortBy"
            class="min-w-[165px] rounded-full border border-slate-200 bg-white px-4 py-2.5 text-xs font-black outline-none focus:border-rose-500"
          >
            <option value="newest">Hàng mới</option>
            <option value="name-asc">Tên A-Z</option>
            <option value="name-desc">Tên Z-A</option>
            <option value="price-asc">Giá tăng dần</option>
            <option value="price-desc">Giá giảm dần</option>
            <option value="discount-desc">Giảm giá nhiều nhất</option>
          </select>
        </div>
      </section>

      <div v-if="activeFilterChips.length" class="mb-5 flex flex-wrap items-center gap-2">
        <span class="mr-1 text-[10px] font-black uppercase tracking-[0.14em] text-slate-400"
          >Bộ lọc đang dùng</span
        >
        <button
          v-for="chip in activeFilterChips"
          :key="`${chip.key}-${chip.value}`"
          type="button"
          class="inline-flex items-center gap-1.5 rounded-full border border-rose-100 bg-rose-50 px-3 py-1.5 text-[10px] font-black text-rose-700"
          @click="removeFilter(chip)"
        >
          {{ chip.label }} <X :size="12" />
        </button>
        <button
          type="button"
          class="text-[10px] font-black text-slate-500 hover:text-rose-600"
          @click="clearFilters"
        >
          Xóa tất cả
        </button>
      </div>

      <div class="grid gap-6 lg:grid-cols-[250px_minmax(0,1fr)]">
        <ProductFilterSidebar
          :filters="filters"
          :price-ranges="priceRanges"
          :dynamic-filters="dynamicFilters"
          :has-active-filters="hasActiveFilters"
          @clear="clearFilters"
        />

        <ProductGrid
          :products="visibleProducts"
          :filtered-count="filteredProducts.length"
          :loading="loading"
          :error-message="errorMessage"
          :total-pages="totalPages"
          :current-page="currentPage"
          :page-numbers="pageNumbers"
          :image-url="imageUrl"
          :handle-image-error="handleImageError"
          :is-new-product="isNewProduct"
          :is-favorite="isFavorite"
          :format-price="formatPrice"
          :price-label="priceLabel"
          @detail="goToDetail"
          @favorite="toggleFavorite"
          @reload="reload"
          @clear="clearFilters"
          @prev="currentPage--"
          @next="currentPage++"
          @page="currentPage = $event"
        />
      </div>
    </main>

    <ProductEditorialCard />

    <Teleport to="body">
      <Transition name="drawer">
        <div
          v-if="mobileFiltersOpen"
          class="fixed inset-0 z-[100] bg-slate-950/55 backdrop-blur-sm"
          @click.self="mobileFiltersOpen = false"
        >
          <aside class="ml-auto flex h-full w-[min(92vw,380px)] flex-col bg-white shadow-2xl">
            <div class="flex items-center justify-between border-b border-slate-100 px-5 py-4">
              <div>
                <p class="text-[10px] font-black uppercase tracking-[0.16em] text-rose-600">
                  Bộ lọc
                </p>
                <h2 class="mt-1 text-lg font-black">Lọc sản phẩm</h2>
              </div>
              <button
                type="button"
                class="rounded-full bg-slate-100 p-2.5"
                @click="mobileFiltersOpen = false"
              >
                <X :size="18" />
              </button>
            </div>
            <div class="flex-1 overflow-y-auto p-5">
              <div>
                <p class="mb-3 text-[10px] font-black uppercase tracking-[0.14em] text-slate-400">
                  Mức giá
                </p>
                <div class="grid grid-cols-2 gap-2">
                  <label
                    v-for="price in priceRanges"
                    :key="price.value"
                    class="rounded-xl border px-3 py-2 text-[11px] font-bold"
                    :class="
                      filters.priceRange === price.value
                        ? 'border-rose-500 bg-rose-50 text-rose-700'
                        : 'border-slate-200 text-slate-600'
                    "
                    ><input
                      v-model="filters.priceRange"
                      type="radio"
                      :value="price.value"
                      class="mr-2 accent-rose-600"
                    />{{ price.shortLabel || price.label }}</label
                  >
                </div>
              </div>
              <div
                v-for="group in dynamicFilters"
                :key="group.key"
                class="mt-6 border-t border-slate-100 pt-5"
              >
                <p class="mb-3 text-[10px] font-black uppercase tracking-[0.14em] text-slate-400">
                  {{ group.label }}
                </p>
                <div class="grid grid-cols-2 gap-2">
                  <label
                    v-for="option in group.options"
                    :key="option.id"
                    class="flex items-center gap-2 rounded-xl border border-slate-200 px-3 py-2 text-[11px] font-bold text-slate-600"
                    ><input
                      v-model="filters[group.key]"
                      type="checkbox"
                      :value="option.name"
                      class="accent-rose-600"
                    /><span class="truncate">{{ option.name }}</span></label
                  >
                </div>
              </div>
            </div>
            <div class="grid grid-cols-2 gap-3 border-t border-slate-100 p-5">
              <button
                type="button"
                class="rounded-full border border-slate-200 px-4 py-3 text-xs font-black"
                @click="clearFilters"
              >
                Đặt lại
              </button>
              <button
                type="button"
                class="rounded-full bg-slate-950 px-4 py-3 text-xs font-black text-white"
                @click="mobileFiltersOpen = false"
              >
                Xem kết quả
              </button>
            </div>
          </aside>
        </div>
      </Transition>
    </Teleport>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import ProductThemeHeader from '@/components/product-page/ProductThemeHeader.vue'
import ProductCategoryShowcase from '@/components/product-page/ProductCategoryShowcase.vue'
import ProductFlashSale from '@/components/product-page/ProductFlashSale.vue'
import ProductFilterSidebar from '@/components/product-page/ProductFilterSidebar.vue'
import ProductGrid from '@/components/product-page/ProductGrid.vue'
import ProductEditorialCard from '@/components/product-page/ProductEditorialCard.vue'
import { useProductCatalog } from '@/composables/useProductCatalog'

const {
  filters,
  dynamicFilters,
  visibleProducts,
  filteredProducts,
  loading,
  errorMessage,
  hasActiveFilters,
  activeFilterChips,
  sortBy,
  currentPage,
  totalPages,
  pageNumbers,
  stats,
  mobileFiltersOpen,
  gridColumns,
  clearFilters,
  removeFilter,
  goToDetail,
  imageUrl,
  handleImageError,
  isNewProduct,
  formatPrice,
  priceLabel,
  reload,
} = useProductCatalog({ pageSize: 12 })

const priceRanges = [
  { value: 'all', label: 'Tất cả mức giá', shortLabel: 'Tất cả' },
  { value: 'under300', label: 'Dưới 300.000đ', shortLabel: '< 300K' },
  { value: 'from300to500', label: '300.000đ – 500.000đ', shortLabel: '300K – 500K' },
  { value: 'from500to1000', label: '500.000đ – 1.000.000đ', shortLabel: '500K – 1 triệu' },
  { value: 'over1000', label: 'Trên 1.000.000đ', shortLabel: '> 1 triệu' },
]

const quickCategoryActiveClasses = [
  'border-indigo-500 bg-indigo-600 text-white shadow-lg shadow-indigo-200',
  'border-fuchsia-500 bg-fuchsia-600 text-white shadow-lg shadow-fuchsia-200',
  'border-cyan-500 bg-cyan-600 text-white shadow-lg shadow-cyan-200',
  'border-rose-500 bg-rose-600 text-white shadow-lg shadow-rose-200',
  'border-amber-500 bg-amber-500 text-slate-950 shadow-lg shadow-amber-200',
]
const quickCategoryDots = [
  'bg-indigo-400',
  'bg-fuchsia-400',
  'bg-cyan-400',
  'bg-rose-400',
  'bg-amber-400',
]

const toggleQuickCategory = (name) => {
  const index = filters.categories.indexOf(name)
  if (index >= 0) filters.categories.splice(index, 1)
  else filters.categories.push(name)
}

const storedFavorites = (() => {
  try {
    const parsed = JSON.parse(localStorage.getItem('favorite-product-ids') || '[]')
    return Array.isArray(parsed) ? parsed.map(Number).filter(Number.isFinite) : []
  } catch {
    return []
  }
})()

const favoriteIds = ref(storedFavorites)
const isFavorite = (id) => favoriteIds.value.includes(Number(id))
const toggleFavorite = (id) => {
  const normalizedId = Number(id)
  const index = favoriteIds.value.indexOf(normalizedId)
  if (index >= 0) favoriteIds.value.splice(index, 1)
  else favoriteIds.value.push(normalizedId)
  localStorage.setItem('favorite-product-ids', JSON.stringify(favoriteIds.value))
}
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  overflow: hidden;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
}
.drawer-enter-active,
.drawer-leave-active {
  transition: opacity 0.2s ease;
}
.drawer-enter-active aside,
.drawer-leave-active aside {
  transition: transform 0.2s ease;
}
.drawer-enter-from,
.drawer-leave-to {
  opacity: 0;
}
.drawer-enter-from aside,
.drawer-leave-to aside {
  transform: translateX(100%);
}
</style>
