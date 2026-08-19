<template>
  <div class="shop-home overflow-hidden">
    <HeroBanner />

    <main class="bg-[var(--lux-ivory)]">
      <!-- Category rail: inspired by Apple’s product-family navigation and Shopee’s fast discovery/filter patterns. -->
      <section class="app-container py-8 md:py-12">
        <div class="flex items-end justify-between gap-6 mb-6">
          <div>
            <p class="lux-eyebrow">Khám phá nhanh</p>
            <h2 class="mt-2 text-2xl md:text-3xl font-black tracking-[-0.035em]">
              Chọn theo phong cách
            </h2>
          </div>
          <RouterLink to="/san-pham" class="hidden sm:inline-flex btn-secondary"
            >Xem tất cả <ArrowUpRight :size="16"
          /></RouterLink>
        </div>

        <div v-if="catalogLoading" class="flex gap-3 overflow-hidden">
          <div v-for="n in 5" :key="n" class="lux-skeleton h-12 w-40 shrink-0 rounded-2xl"></div>
        </div>
        <div v-else class="flex gap-3 overflow-x-auto hide-scrollbar pb-2">
          <RouterLink
            v-for="(category, index) in categories"
            :key="category.id || category.name"
            :to="{ path: '/san-pham', query: { category: category.name } }"
            class="home-category-card"
          >
            <span class="home-category-number">0{{ index + 1 }}</span>
            <span>{{ category.name }}</span>
            <ArrowUpRight
              :size="16"
              class="ml-auto opacity-50 transition-transform group-hover:translate-x-0.5 group-hover:-translate-y-0.5"
            />
          </RouterLink>
        </div>
      </section>

      <!-- Product discovery -->
      <section class="app-container pb-12 md:pb-20">
        <div class="home-section-head">
          <div>
            <p class="lux-eyebrow">Sản phẩm mới</p>
            <h2 class="mt-2 text-3xl md:text-4xl font-black tracking-[-0.045em]">
              Những lựa chọn đáng chú ý
            </h2>
            <p class="mt-3 lux-subtitle">
              Khám phá những sản phẩm đang được cập nhật trong cửa hàng.
            </p>
          </div>
          <RouterLink to="/san-pham" class="btn-secondary shrink-0"
            >Khám phá sản phẩm <ArrowRight :size="16"
          /></RouterLink>
        </div>

        <div v-if="catalogLoading" class="home-product-grid">
          <div v-for="n in 8" :key="n" class="lux-product-skeleton">
            <div class="lux-skeleton aspect-[4/5] rounded-[22px]"></div>
            <div class="lux-skeleton mt-4 h-4 w-2/3 rounded"></div>
            <div class="lux-skeleton mt-2 h-5 w-1/2 rounded"></div>
          </div>
        </div>

        <div v-else-if="featuredProducts.length" class="home-product-grid">
          <article
            v-for="(product, index) in featuredProducts"
            :key="product.id"
            class="home-product-card motion-reveal"
            :style="{ '--reveal-delay': `${Math.min(index * 55, 350)}ms` }"
            @click="goToProduct(product.id)"
          >
            <div class="relative overflow-hidden rounded-[22px] bg-[#eee9e0] aspect-[4/5]">
              <img
                :src="imageUrl(product)"
                :alt="product.tenSanPham"
                loading="lazy"
                class="h-full w-full object-cover transition duration-700 group-hover:scale-[1.045]"
                @error="handleImageError"
              />
              <div class="absolute left-3 top-3 flex flex-wrap gap-2">
                <span v-if="product.dangGiamGia" class="lux-badge lux-badge-wine">Ưu đãi</span>
                <span v-if="isNewProduct(product)" class="lux-badge lux-badge-gold">Mới</span>
              </div>
              <button type="button" class="home-product-arrow" aria-label="Xem sản phẩm">
                <ArrowUpRight :size="18" />
              </button>
            </div>
            <div class="px-1 pt-4">
              <p
                class="text-[11px] font-black uppercase tracking-[0.15em] text-[var(--lux-wine)] truncate"
              >
                {{ product.idThuongHieu?.tenThuongHieu || 'K-ZONE' }}
              </p>
              <h3
                class="mt-1.5 line-clamp-2 min-h-[44px] text-[15px] font-black leading-5 text-[var(--lux-ink)]"
              >
                {{ product.tenSanPham }}
              </h3>
              <div class="mt-2 flex items-center justify-between gap-3">
                <span class="text-lg font-black text-[var(--lux-ink)]">{{
                  priceLabel(product)
                }}</span>
                <span
                  v-if="product.soLuong > 0"
                  class="text-[11px] font-bold text-[var(--lux-green)]"
                  >Còn hàng</span
                >
              </div>
            </div>
          </article>
        </div>

        <div v-else class="lux-surface p-10 text-center">
          <p class="font-bold text-[var(--lux-muted)]">Chưa có sản phẩm để hiển thị.</p>
          <RouterLink to="/san-pham" class="btn-primary mt-5">Xem cửa hàng</RouterLink>
        </div>
      </section>

      <!-- Editorial strip -->
      <section class="app-container pb-12 md:pb-20">
        <div class="home-editorial-grid">
          <RouterLink to="/san-pham" class="home-editorial-card home-editorial-dark group">
            <div class="relative z-10 max-w-xl">
              <p class="lux-eyebrow text-[var(--lux-gold-soft)]">Phong cách mỗi ngày</p>
              <h2
                class="mt-4 text-3xl md:text-5xl font-black leading-[1.02] tracking-[-0.045em] text-white"
              >
                Từ lựa chọn nhỏ đến phong cách riêng.
              </h2>
              <p class="mt-4 max-w-lg text-sm leading-7 text-white/65">
                Khám phá sản phẩm phù hợp với nhịp sống và cá tính của bạn.
              </p>
              <span class="mt-7 inline-flex items-center gap-2 font-black text-white"
                >Mua sắm ngay <ArrowRight :size="17"
              /></span>
            </div>
            <div class="home-editorial-orb"></div>
          </RouterLink>

          <RouterLink to="/san-pham" class="home-editorial-card home-editorial-paper group">
            <div>
              <p class="lux-eyebrow">Trải nghiệm mua sắm</p>
              <h2 class="mt-3 text-2xl md:text-3xl font-black leading-tight tracking-[-0.04em]">
                Tìm nhanh. Chọn đúng. Mua dễ.
              </h2>
              <p class="mt-3 text-sm leading-7 text-[var(--lux-muted)]">
                Tìm kiếm, lọc theo danh mục, thương hiệu, chất liệu và khoảng giá ngay trong cửa
                hàng.
              </p>
            </div>
            <span
              class="mt-8 inline-flex h-11 w-11 items-center justify-center rounded-full bg-[var(--lux-ink)] text-white transition group-hover:translate-x-1"
              ><ArrowUpRight :size="18"
            /></span>
          </RouterLink>
        </div>
      </section>

      <!-- Trust row -->
      <section class="border-y border-[var(--lux-border)] bg-[var(--lux-paper)]">
        <div class="app-container grid gap-px md:grid-cols-3">
          <div v-for="item in trustItems" :key="item.title" class="home-trust-item">
            <component :is="item.icon" :size="21" />
            <div>
              <h3>{{ item.title }}</h3>
              <p>{{ item.description }}</p>
            </div>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { RouterLink, useRouter } from 'vue-router'
import { ArrowRight, ArrowUpRight, BadgeCheck, PackageCheck, ShieldCheck } from 'lucide-vue-next'
import HeroBanner from './components/HeroBanner.vue'
import { useProductCatalog } from '@/composables/useProductCatalog'

const router = useRouter()
const {
  products,
  loading: catalogLoading,
  dynamicFilters,
  imageUrl,
  handleImageError,
  isNewProduct,
  priceLabel,
} = useProductCatalog({ pageSize: 8 })

const categories = computed(() => dynamicFilters.value?.[0]?.options?.slice(0, 8) || [])
const featuredProducts = computed(() => products.value.slice(0, 8))

const trustItems = [
  {
    title: 'Sản phẩm rõ ràng',
    description: 'Thông tin và biến thể được hiển thị trực tiếp.',
    icon: BadgeCheck,
  },
  {
    title: 'Đặt hàng thuận tiện',
    description: 'Từ chọn sản phẩm đến thanh toán trong một luồng.',
    icon: PackageCheck,
  },
  {
    title: 'Thanh toán an tâm',
    description: 'Hỗ trợ các phương thức thanh toán của cửa hàng.',
    icon: ShieldCheck,
  },
]

const goToProduct = (id) => router.push({ name: 'confirmbuy', params: { id } })
</script>
