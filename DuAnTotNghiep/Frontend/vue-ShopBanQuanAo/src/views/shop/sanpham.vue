<template>
  <div class="min-h-screen bg-slate-50 text-slate-950 font-sans">
    <!-- 1. HEADER -->
    <Header v-model="filters.keyword" />

    <!-- CONTAINER CHÍNH CHỨA CÁC SECTION (Khoảng cách đồng bộ, hiện đại) -->
    <main class="space-y-4 md:space-y-6 pb-12">
      <!-- 2. HERO BANNER (Đặt lên đầu trang chuẩn UX) -->
      <HeroBanner :stats="stats" />

      <!-- 3. DANH MỤC SẢN PHẨM NỔI BẬT -->
      <CategorySection
        :categories="dynamicFilters[0]?.options || []"
        :active-categories="filters.categories"
        :total-count="stats.products"
        @toggle="toggleQuickCategory"
        @clear="filters.categories.splice(0)"
      />

      <!-- 4. FLASH SALE SECTION -->
      <FlashSaleSection
        :products="visibleProducts"
        :end-time="visibleProducts[0]?.ngayKetThuc"
        :loading="loading"
        :image-url="imageUrl"
        :handle-image-error="handleImageError"
        :price-label="priceLabel"
        :is-favorite="(id) => favoriteIds.includes(Number(id))"
        @detail="goToDetail"
        @favorite="toggleFavorite"
        @view-all="handleViewAll"
        @quick-view="handleQuickView"
      />

      <!-- 5. TOP SẢN PHẨM BÁN CHẠY (Đã bổ sung đầy đủ props Yêu thích & Xem nhanh) -->
      <TopProductsSection
        :products="visibleProducts"
        :categories="dynamicFilters[0]?.options || []"
        :image-url="imageUrl"
        :handle-image-error="handleImageError"
        :price-label="priceLabel"
        :is-favorite="(id) => favoriteIds.includes(Number(id))"
        @detail="goToDetail"
        @favorite="toggleFavorite"
        @quick-view="handleQuickView"
      />

      <!-- 6. THƯƠNG HIỆU ĐỒNG HÀNH -->
      <BrandSection
        :brands="brandOptions"
        :selected-brand="filters.brands"
        :loading="loading"
        @select-brand="handleSelectBrand"
      />

      <!-- 7. THỜI TRANG HOT TREND -->
      <FashionSection
        title="THỜI TRANG HOT TREND"
        :products="visibleProducts"
        :loading="loading"
        :image-url="imageUrl"
        :handle-image-error="handleImageError"
        :price-label="priceLabel"
        :format-price="formatPrice"
        :is-favorite="(id) => favoriteIds.includes(Number(id))"
        @change-category="fetchProductsByApiCategory"
        @detail="goToDetail"
        @favorite="toggleFavorite"
        @quick-view="handleQuickView"
      />
    </main>

    <!-- MODAL XEM NHANH -->
    <QuickViewModal
      v-if="isQuickViewOpen"
      :product-id="selectedProductId"
      @close="isQuickViewOpen = false"
    />

    <!-- FOOTER -->
    <Footer />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import Header from '@/components/product-page/Header.vue'
import HeroBanner from '@/components/product-page/HeroBanner.vue'
import CategorySection from '@/components/product-page/CategorySection.vue'
import FlashSaleSection from '@/components/product-page/FlashSaleSection.vue'
import TopProductsSection from '@/components/product-page/TopProductsSection.vue'
import FashionSection from '@/components/product-page/FashionSection.vue'
import BrandSection from '@/components/product-page/BrandSection.vue'
import Footer from '@/components/product-page/Footer.vue'
import QuickViewModal from './views/componnents/QuickViewModal.vue'

import { useProductCatalog } from '@/composables/useProductCatalog'

const {
  filters,
  dynamicFilters,
  visibleProducts,
  loading,
  stats,
  goToDetail,
  imageUrl,
  handleImageError,
  formatPrice,
  priceLabel,
} = useProductCatalog({ pageSize: 100 })

// 1. Quản lý trạng thái Xem Nhanh (QuickView)
const isQuickViewOpen = ref(false)
const selectedProductId = ref(null)

const handleQuickView = (productId) => {
  selectedProductId.value = productId
  isQuickViewOpen.value = true
}

// 2. Xử lý nút Xem tất cả Flash Sale
const handleViewAll = () => {
  console.log('Xem tất cả sản phẩm Flash Sale')
}

// 3. Lọc thương hiệu
const brandOptions = computed(() => {
  const brandFilter = dynamicFilters.value?.find(
    (f) =>
      f.key === 'brands' || f.key === 'brand' || f.label?.toLowerCase().includes('thương hiệu'),
  )
  return brandFilter?.options || dynamicFilters.value?.[1]?.options || []
})

const handleSelectBrand = (brand) => {
  const brandName =
    typeof brand === 'object' ? brand.name || brand.tenThuongHieu || brand.label : brand
  if (Array.isArray(filters.brands)) {
    const index = filters.brands.indexOf(brandName)
    if (index >= 0) filters.brands.splice(index, 1)
    else filters.brands.push(brandName)
  } else {
    filters.brands = [brandName]
  }
}

const toggleQuickCategory = (name) => {
  const index = filters.categories.indexOf(name)
  if (index >= 0) filters.categories.splice(index, 1)
  else filters.categories.push(name)
}

const fetchProductsByApiCategory = async (categoryId) => {
  console.log('Gọi API lấy sản phẩm thời trang theo mã danh mục:', categoryId)
}

// 4. Quản lý danh sách Yêu thích (Favorite)
const storedFavorites = (() => {
  try {
    const parsed = JSON.parse(localStorage.getItem('favorite-product-ids') || '[]')
    return Array.isArray(parsed) ? parsed.map(Number).filter(Number.isFinite) : []
  } catch {
    return []
  }
})()

const favoriteIds = ref(storedFavorites)

const toggleFavorite = (id) => {
  const normalizedId = Number(id)
  const index = favoriteIds.value.indexOf(normalizedId)
  if (index >= 0) {
    favoriteIds.value.splice(index, 1)
  } else {
    favoriteIds.value.push(normalizedId)
  }
  localStorage.setItem('favorite-product-ids', JSON.stringify(favoriteIds.value))
}
</script>
