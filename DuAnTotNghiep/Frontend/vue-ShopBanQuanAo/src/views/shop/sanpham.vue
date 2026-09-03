<template>
  <div class="min-h-screen bg-white text-slate-950">
    <!-- Header -->
    <Header v-model="filters.keyword" />

    <!-- Danh mục sản phẩm -->
    <CategorySection
      :categories="dynamicFilters[0]?.options || []"
      :active-categories="filters.categories"
      :total-count="stats.products"
      @toggle="toggleQuickCategory"
      @clear="filters.categories.splice(0)"
    />

    <!-- FLASH SALE SECTION -->
    <FlashSaleSection
      :products="visibleProducts"
      :loading="loading"
      :image-url="imageUrl"
      :handle-image-error="handleImageError"
      :price-label="priceLabel"
      @detail="goToDetail"
      @favorite="toggleFavorite"
      @view-all="handleViewAll"
    />

    <!-- HERO BANNER -->
    <HeroBanner :stats="stats" />

    <!-- THƯƠNG HIỆU -->
    <!-- THƯƠNG HIỆU -->
    <BrandSection
      :brands="brandOptions"
      :selected-brand="filters.brands"
      :loading="loading"
      @select-brand="handleSelectBrand"
    />

    <!-- COMPONENT TOP SẢN PHẨM BÁN CHẠY + BANNER SALE SỐC -->
    <TopProductsSection
      :products="visibleProducts"
      :image-url="imageUrl"
      :handle-image-error="handleImageError"
      :price-label="priceLabel"
      @detail="goToDetail"
      @favorite="toggleFavorite"
    />

    <!-- THỜI TRANG HOT TREND -->
    <FashionSection
      title="THỜI TRANG HOT TREND"
      :products="visibleProducts"
      :loading="loading"
      :image-url="imageUrl"
      :handle-image-error="handleImageError"
      :price-label="priceLabel"
      :format-price="formatPrice"
      @change-category="fetchProductsByApiCategory"
      @detail="goToDetail"
      @favorite="toggleFavorite"
    />

    <Footer />
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { SlidersHorizontal, X } from 'lucide-vue-next'
import Header from '@/components/product-page/Header.vue'
import HeroBanner from '@/components/product-page/HeroBanner.vue'
import CategorySection from '@/components/product-page/CategorySection.vue'
import FlashSaleSection from '@/components/product-page/FlashSaleSection.vue'
import TopProductsSection from '@/components/product-page/TopProductsSection.vue'
import FilterSidebar from '@/components/product-page/FilterSidebar.vue'
import FashionSection from '@/components/product-page/FashionSection.vue'
import BrandSection from '@/components/product-page/BrandSection.vue'
import { useProductCatalog } from '@/composables/useProductCatalog'
import Footer from '@/components/product-page/Footer.vue'

const router = useRouter()

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
  clearFilters,
  removeFilter,
  goToDetail,
  imageUrl,
  handleImageError,
  isNewProduct,
  formatPrice,
  priceLabel,
  reload,
} = useProductCatalog({ pageSize: 100 })

const priceRanges = [
  { value: 'all', label: 'Tất cả mức giá', shortLabel: 'Tất cả' },
  { value: 'under300', label: 'Dưới 300.000đ', shortLabel: '< 300K' },
  { value: 'from300to500', label: '300.000đ – 500.000đ', shortLabel: '300K – 500K' },
  { value: 'from500to1000', label: '500.000đ – 1.000.000đ', shortLabel: '500K – 1 triệu' },
  { value: 'over1000', label: 'Trên 1.000.000đ', shortLabel: '> 1 triệu' },
]

// Hàm xử lý sự kiện click "Xem tất cả" Flash Sale
const handleViewAll = () => {
  console.log('Xem tất cả sản phẩm Flash Sale')
  // Ví dụ chuyển hướng trang nếu có:
  // router.push('/flash-sale')
}

watch(
  visibleProducts,
  (val) => {
    console.log('Dữ liệu visibleProducts truyền vào FlashSale:', val)
  },
  { immediate: true },
)
const fetchProductsByApiCategory = async (categoryId) => {
  console.log('Gọi API lấy sản phẩm thời trang theo mã danh mục:', categoryId)
}

const brandOptions = computed(() => {
  const brandFilter = dynamicFilters.value?.find(
    (f) =>
      f.key === 'brands' || f.key === 'brand' || f.label?.toLowerCase().includes('thương hiệu'),
  )
  return brandFilter?.options || dynamicFilters.value?.[1]?.options || []
})

// Xử lý chọn / bỏ chọn thương hiệu linh hoạt
const handleSelectBrand = (brand) => {
  const brandName =
    typeof brand === 'object' ? brand.name || brand.tenThuongHieu || brand.label : brand

  if (Array.isArray(filters.brands)) {
    const index = filters.brands.indexOf(brandName)
    if (index >= 0) {
      filters.brands.splice(index, 1)
    } else {
      filters.brands.push(brandName)
    }
  } else {
    filters.brands = [brandName]
  }
}

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
