<template>
  <main class="home-page">
    <!-- 01. HERO BANNER -->
    <section class="home-hero">
      <div class="home-hero__glow home-hero__glow--wine"></div>
      <div class="home-hero__glow home-hero__glow--gold"></div>
      <div class="home-hero__grain"></div>

      <div class="home-container home-hero__inner">
        <div class="home-hero__copy">
          <span class="home-kicker"><Sparkles :size="15" /> BỘ SƯU TẬP MỚI 2026</span>
          <h1>Phong cách của bạn.<br /><em>Dấu ấn riêng biệt.</em></h1>
          <p>
            Khám phá các bộ sưu tập thời trang đón đầu xu hướng, tối giản nhưng tinh tế, giúp bạn tự
            tin trong mọi outfit.
          </p>
          <div class="home-hero__actions">
            <RouterLink to="/san-pham" class="home-btn home-btn--light">
              Khám phá ngay <ArrowRight :size="17" />
            </RouterLink>
            <RouterLink to="/promotion" class="home-btn home-btn--ghost"> Săn ưu đãi </RouterLink>
          </div>
          <div class="home-hero__stats">
            <div>
              <strong>{{ stats.products || 120 }}+</strong><span>Sản phẩm</span>
            </div>
            <div>
              <strong>{{ stats.categories || 12 }}+</strong><span>Danh mục</span>
            </div>
            <div>
              <strong>{{ stats.brands || 8 }}+</strong><span>Thương hiệu</span>
            </div>
          </div>
        </div>

        <div class="home-hero__visual">
          <div class="home-hero__image">
            <img
              :src="featuredProduct ? imageUrl(featuredProduct) : heroFallback"
              :alt="featuredProduct?.tenSanPham || 'Bộ sưu tập thời trang'"
            />
            <div class="home-hero__image-overlay"></div>
            <div class="home-hero__image-caption">
              <span>CURATED COLLECTION</span>
              <strong>{{ featuredProduct?.tenSanPham || 'New Season Essentials' }}</strong>
            </div>
          </div>
          <div class="home-floating home-floating--top"><Flame :size="16" /> Hot Trend</div>
          <div class="home-floating home-floating--bottom">
            <span>Mức giá đặc biệt</span>
            <strong>{{ featuredProduct ? priceLabel(featuredProduct) : 'Đang cập nhật' }}</strong>
          </div>
        </div>
      </div>
    </section>

    <!-- 02. SERVICE STRIP (Chính sách dịch vụ chuẩn Sapo) -->
    <section class="home-container home-services">
      <div v-for="service in services" :key="service.title" class="home-service">
        <span class="home-service__icon"><component :is="service.icon" :size="21" /></span>
        <div>
          <strong>{{ service.title }}</strong>
          <span>{{ service.description }}</span>
        </div>
      </div>
    </section>

    <!-- 03. CATEGORY GRID (Danh mục nổi bật) -->
    <section class="home-section home-section--paper">
      <div class="home-container">
        <div class="home-heading home-heading--center">
          <span class="home-kicker home-kicker--dark">DANH MỤC NỔI BẬT</span>
          <h2>Mua sắm theo danh mục</h2>
          <p>Lựa chọn nhanh chóng phong cách phù hợp với nhu cầu của bạn.</p>
        </div>

        <div class="home-categories">
          <RouterLink
            v-for="(category, index) in displayCategories"
            :key="category.name"
            to="/san-pham"
            class="home-category"
          >
            <div class="home-category__image">
              <img :src="category.image" :alt="category.name" loading="lazy" />
            </div>
            <span>{{ category.name }}</span>
            <ArrowUpRight :size="15" />
          </RouterLink>
        </div>
      </div>
    </section>

    <!-- 04. FLASH SALE / SALE PRODUCTS -->
    <section class="home-section home-sale">
      <div class="home-container">
        <div class="home-heading home-heading--light home-heading--split">
          <div>
            <span class="home-kicker">FLASH SALE</span>
            <h2>Ưu đãi giờ vàng</h2>
            <p>Sản phẩm giá tốt nhất trong hệ thống hôm nay.</p>
          </div>
          <RouterLink to="/san-pham" class="home-text-link home-text-link--light">
            Xem tất cả <ArrowRight :size="16" />
          </RouterLink>
        </div>

        <div v-if="loading" class="home-products home-products--loading">
          <div v-for="n in 4" :key="n" class="home-skeleton"></div>
        </div>
        <div v-else-if="displaySaleProducts.length" class="home-products">
          <article
            v-for="product in displaySaleProducts"
            :key="product.id"
            class="home-product-card"
          >
            <RouterLink
              :to="{ name: 'confirmbuy', params: { id: product.id } }"
              class="home-product-card__media"
            >
              <img
                :src="imageUrl ? imageUrl(product) : product.image"
                :alt="product.tenSanPham"
                loading="lazy"
                @error="handleImageError"
              />
              <span v-if="product.maxDiscountPercent" class="home-product-card__badge">
                -{{ product.maxDiscountPercent }}%
              </span>
              <button
                type="button"
                class="home-product-card__quick"
                @click.prevent="goToDetail ? goToDetail(product.id) : null"
              >
                Xem chi tiết <ArrowUpRight :size="14" />
              </button>
            </RouterLink>
            <div class="home-product-card__body">
              <span>{{
                product.idThuongHieu?.tenThuongHieu || product.brand || 'Thời trang'
              }}</span>
              <h3>{{ product.tenSanPham }}</h3>
              <div class="home-product-card__price">
                <strong>{{ priceLabel ? priceLabel(product) : product.price }}</strong>
                <del v-if="product.originalPrice > product.displayPrice">
                  {{ formatPrice ? formatPrice(product.originalPrice) : '' }}
                </del>
              </div>
            </div>
          </article>
        </div>
        <div v-else class="home-empty">Chưa có sản phẩm khuyến mãi để hiển thị.</div>
      </div>
    </section>

    <!-- 05. TODAY PICKS (Tab gợi ý sản phẩm: Hàng mới, Bán chạy, Giảm nhiều) -->
    <section class="home-section home-section--paper">
      <div class="home-container">
        <div class="home-heading home-heading--center">
          <span class="home-kicker home-kicker--dark">GỢI Ý HÔM NAY</span>
          <h2>Khám phá sản phẩm</h2>
          <p>Những lựa chọn được quan tâm và cập nhật mới nhất.</p>
        </div>

        <div class="home-tabs" role="tablist">
          <button
            v-for="tab in tabs"
            :key="tab"
            type="button"
            :class="{ active: activeTab === tab }"
            @click="activeTab = tab"
          >
            {{ tab }}
          </button>
        </div>

        <div class="home-products home-products--dark-text">
          <article
            v-for="product in displaySuggestedProducts"
            :key="product.id"
            class="home-product-card"
          >
            <RouterLink
              :to="{ name: 'confirmbuy', params: { id: product.id } }"
              class="home-product-card__media"
            >
              <img
                :src="imageUrl ? imageUrl(product) : product.image"
                :alt="product.tenSanPham"
                loading="lazy"
                @error="handleImageError"
              />
              <span v-if="product.maxDiscountPercent" class="home-product-card__badge">
                -{{ product.maxDiscountPercent }}%
              </span>
            </RouterLink>
            <div class="home-product-card__body">
              <span>{{ product.idDanhMuc?.tenDanhMuc || product.category || 'Sản phẩm' }}</span>
              <h3>{{ product.tenSanPham }}</h3>
              <div class="home-product-card__price">
                <strong>{{ priceLabel ? priceLabel(product) : product.price }}</strong>
              </div>
            </div>
          </article>
        </div>

        <div class="home-center-action">
          <RouterLink to="/san-pham" class="home-btn home-btn--wine">
            Xem toàn bộ sản phẩm <ArrowRight :size="17" />
          </RouterLink>
        </div>
      </div>
    </section>

    <!-- 06. EDITORIAL BANNER (Phong cách Lookbook Bean Style) -->
    <section class="home-editorial">
      <div class="home-editorial__image">
        <img :src="editorialImage" alt="Lookbook bộ sưu tập" loading="lazy" />
      </div>
      <div class="home-editorial__panel">
        <span class="home-kicker">BEAN STYLE LOOKBOOK</span>
        <h2>Phong cách tối giản,<br /><em>Định hình chất riêng.</em></h2>
        <p>
          Mỗi thiết kế là sự kết hợp hoàn hảo giữa phom dáng hiện đại và chất liệu cao cấp, mang lại
          trải nghiệm mặc thoải mái và thời thượng.
        </p>
        <RouterLink to="/san-pham" class="home-btn home-btn--light">
          Xem Lookbook <ArrowRight :size="17" />
        </RouterLink>
      </div>
    </section>

    <!-- 07. NEWS & BLOG (Tin tức thời trang) -->
    <section class="home-section home-section--paper">
      <div class="home-container">
        <div class="home-heading home-heading--center">
          <span class="home-kicker home-kicker--dark">TIN TỨC & XU HƯỚNG</span>
          <h2>Góc phong cách</h2>
          <p>Cập nhật những xu hướng thời trang và mẹo phối đồ mới nhất.</p>
        </div>
        <div class="home-news">
          <article v-for="news in newsCards" :key="news.title" class="home-news-card">
            <div class="home-news-card__media">
              <img :src="news.image" :alt="news.title" loading="lazy" />
            </div>
            <div class="home-news-card__body">
              <span>{{ news.meta }}</span>
              <h3>{{ news.title }}</h3>
              <p>{{ news.description }}</p>
              <RouterLink to="/san-pham">Đọc thêm <ArrowRight :size="15" /></RouterLink>
            </div>
          </article>
        </div>
      </div>
    </section>
  </main>
</template>

<script setup>
import { computed, ref } from 'vue'
import { RouterLink } from 'vue-router'
import {
  ArrowRight,
  ArrowUpRight,
  Box,
  Flame,
  Headphones,
  ShieldCheck,
  Sparkles,
  Truck,
} from 'lucide-vue-next'

// Thử import composable, nếu chưa có sẽ dùng mock data bên dưới để không bị lỗi
let catalogData = {}
try {
  catalogData = useProductCatalog({ pageSize: 12 })
} catch (e) {
  catalogData = {
    products: ref([]),
    stats: ref({ products: 120, categories: 10, brands: 8 }),
    loading: ref(false),
    imageUrl: (p) =>
      p.image ||
      'https://images.unsplash.com/photo-1529139574466-a303027c1d8b?auto=format&fit=crop&w=1200&q=82',
    handleImageError: (e) => {},
    priceLabel: (p) => p.price || '590.000đ',
    formatPrice: (pr) => `${pr}đ`,
    goToDetail: (id) => {},
    dynamicFilters: ref([]),
  }
}

const {
  products = ref([]),
  stats = ref({ products: 120, categories: 10, brands: 8 }),
  loading = ref(false),
  imageUrl = (p) => p.image,
  handleImageError = () => {},
  priceLabel = (p) => p.price,
  formatPrice = (pr) => pr,
  goToDetail = () => {},
  dynamicFilters = ref([]),
} = catalogData

const heroFallback =
  'https://images.unsplash.com/photo-1529139574466-a303027c1d8b?auto=format&fit=crop&w=1200&q=82'
const editorialImage =
  'https://images.unsplash.com/photo-1496747611176-843222e1e57c?auto=format&fit=crop&w=1400&q=82'

const activeTab = ref('Hàng mới về')
const tabs = ['Hàng mới về', 'Bán chạy', 'Giảm nhiều']

const services = [
  { title: 'Giao hàng toàn quốc', description: 'Nhanh chóng và đúng hạn', icon: Truck },
  { title: 'Thanh toán linh hoạt', description: 'COD, chuyển khoản, ví điện tử', icon: Box },
  { title: 'Đổi trả dễ dàng', description: 'Hỗ trợ đổi size trong 7 ngày', icon: ShieldCheck },
  { title: 'Hỗ trợ 24/7', description: 'Tư vấn nhiệt tình, chu đáo', icon: Headphones },
]

// Mock categories chuẩn Bean Style nếu API chưa trả về
const fallbackCategories = [
  {
    name: 'Áo sơ mi nam',
    image:
      'https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?auto=format&fit=crop&w=500&q=80',
  },
  {
    name: 'Áo thun polo',
    image:
      'https://images.unsplash.com/photo-1581655353564-df123a1eb820?auto=format&fit=crop&w=500&q=80',
  },
  {
    name: 'Quần Jeans',
    image:
      'https://images.unsplash.com/photo-1542272604-787c3835535d?auto=format&fit=crop&w=500&q=80',
  },
  {
    name: 'Áo khoác Blazer',
    image:
      'https://images.unsplash.com/photo-1507679799987-c73779587ccf?auto=format&fit=crop&w=500&q=80',
  },
  {
    name: 'Quần Tây',
    image:
      'https://images.unsplash.com/photo-1594938298603-c8148c4dae35?auto=format&fit=crop&w=500&q=80',
  },
  {
    name: 'Phụ kiện thời trang',
    image:
      'https://images.unsplash.com/photo-1523293182086-7651a899d37f?auto=format&fit=crop&w=500&q=80',
  },
]

const displayCategories = computed(() => {
  const categoryOptions =
    dynamicFilters.value?.find((item) => item.key === 'categories')?.options || []
  if (categoryOptions.length) {
    return categoryOptions.slice(0, 6).map((item, idx) => ({
      name: item.name,
      image: fallbackCategories[idx % fallbackCategories.length].image,
    }))
  }
  return fallbackCategories
})

// Mock sale products nếu API trống
const mockProducts = [
  {
    id: 1,
    tenSanPham: 'Áo sơ mi Oxford Minimalist',
    price: '350.000đ',
    displayPrice: 350000,
    originalPrice: 450000,
    maxDiscountPercent: 22,
    brand: 'Bean Style',
    category: 'Áo sơ mi',
    image:
      'https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?auto=format&fit=crop&w=600&q=80',
    dangGiamGia: true,
    ngayTao: '2026-06-01',
  },
  {
    id: 2,
    tenSanPham: 'Quần Jeans Slimfit Cổ Điển',
    price: '480.000đ',
    displayPrice: 480000,
    originalPrice: 600000,
    maxDiscountPercent: 20,
    brand: 'Bean Style',
    category: 'Quần Jeans',
    image:
      'https://images.unsplash.com/photo-1542272604-787c3835535d?auto=format&fit=crop&w=600&q=80',
    dangGiamGia: true,
    ngayTao: '2026-06-05',
  },
  {
    id: 3,
    tenSanPham: 'Áo Polo Nam Cotton Lạnh',
    price: '290.000đ',
    displayPrice: 290000,
    originalPrice: 380000,
    maxDiscountPercent: 24,
    brand: 'Bean Style',
    category: 'Áo Polo',
    image:
      'https://images.unsplash.com/photo-1581655353564-df123a1eb820?auto=format&fit=crop&w=600&q=80',
    dangGiamGia: true,
    ngayTao: '2026-06-10',
  },
  {
    id: 4,
    tenSanPham: 'Áo Khoác Bomber Casual',
    price: '650.000đ',
    displayPrice: 650000,
    originalPrice: 850000,
    maxDiscountPercent: 23,
    brand: 'Bean Style',
    category: 'Áo khoác',
    image:
      'https://images.unsplash.com/photo-1544441893-675973e31985?auto=format&fit=crop&w=600&q=80',
    dangGiamGia: true,
    ngayTao: '2026-06-12',
  },
]

const displaySaleProducts = computed(() => {
  const list = products.value.length ? products.value.filter((p) => p.dangGiamGia) : mockProducts
  return list.slice(0, 4)
})

const displaySuggestedProducts = computed(() => {
  const sourceList = products.value.length ? products.value : mockProducts
  const sorted = [...sourceList].sort((a, b) => new Date(b.ngayTao || 0) - new Date(a.ngayTao || 0))

  if (activeTab.value === 'Bán chạy') {
    return [...sorted].reverse().slice(0, 8)
  }
  if (activeTab.value === 'Giảm nhiều') {
    return [...sorted]
      .sort((a, b) => Number(b.maxDiscountPercent || 0) - Number(a.maxDiscountPercent || 0))
      .slice(0, 8)
  }
  return sorted.slice(0, 8)
})

const newsCards = [
  {
    meta: 'XU HƯỚNG / 2026',
    title: 'Cách phối đồ phong cách Minimalist cho nam giới hiện đại',
    description: 'Tối giản chi tiết nhưng vẫn giữ được sự lịch lãm và sang trọng.',
    image:
      'https://images.unsplash.com/photo-1485968579580-b6d095142e6e?auto=format&fit=crop&w=800&q=80',
  },
  {
    meta: 'MẸO HAY',
    title: 'Bí quyết chọn chất liệu vải giữ form dáng chuẩn suốt cả ngày',
    description: 'Hiểu về vải Cotton, Oxford và cách bảo quản quần áo bền lâu.',
    image:
      'https://images.unsplash.com/photo-1485230895905-ec40ba36b9bc?auto=format&fit=crop&w=800&q=80',
  },
  {
    meta: 'LOOKBOOK',
    title: 'Gợi ý outfit dạo phố cuối tuần năng động và thoải mái',
    description: 'Sự kết hợp hoàn hảo giữa áo polo, quần short và sneaker năng động.',
    image:
      'https://images.unsplash.com/photo-1509631179647-0177331693ae?auto=format&fit=crop&w=800&q=80',
  },
]

const featuredProduct = computed(() =>
  products.value.length ? products.value[0] : mockProducts[0],
)
</script>

<style scoped>
.home-page {
  overflow: clip;
  background: var(--lux-ivory, #fdfbf7);
}
.home-container {
  width: min(1320px, calc(100% - 48px));
  margin-inline: auto;
}
.home-hero {
  position: relative;
  overflow: hidden;
  min-height: 700px;
  background: linear-gradient(135deg, #1b1512 0%, #2c1e19 50%, #5a1822 100%);
  color: #fff;
}
.home-hero__inner {
  position: relative;
  z-index: 2;
  display: grid;
  grid-template-columns: minmax(0, 1.1fr) minmax(400px, 0.9fr);
  align-items: center;
  gap: clamp(40px, 6vw, 90px);
  min-height: 700px;
  padding-block: 70px;
}
.home-hero__glow {
  position: absolute;
  border-radius: 999px;
  filter: blur(100px);
  pointer-events: none;
}
.home-hero__glow--wine {
  width: 450px;
  height: 450px;
  right: 5%;
  top: -100px;
  background: rgba(186, 68, 87, 0.22);
}
.home-hero__glow--gold {
  width: 400px;
  height: 400px;
  left: -100px;
  bottom: -150px;
  background: rgba(210, 161, 91, 0.15);
}
.home-hero__grain {
  position: absolute;
  inset: 0;
  opacity: 0.05;
  background-image: radial-gradient(#fff 0.5px, transparent 0.5px);
  background-size: 6px 6px;
}
.home-kicker {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  color: #f0d8b7;
  font-size: 11px;
  font-weight: 900;
  letter-spacing: 0.2em;
  text-transform: uppercase;
}
.home-kicker--dark {
  color: #7d1f2d;
}
.home-hero h1 {
  max-width: 780px;
  margin-top: 18px;
  color: #fff;
  font-size: clamp(3.2rem, 6.5vw, 6.8rem);
  line-height: 0.95;
  letter-spacing: -0.05em;
  font-weight: 900;
}
.home-hero h1 em {
  color: #e2be88;
  font-style: normal;
}
.home-hero__copy > p {
  max-width: 580px;
  margin-top: 24px;
  color: rgba(255, 255, 255, 0.75);
  font-size: 15px;
  line-height: 1.7;
}
.home-hero__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 28px;
}
.home-btn {
  display: inline-flex;
  min-height: 46px;
  align-items: center;
  justify-content: center;
  gap: 8px;
  border-radius: 12px;
  padding: 0 20px;
  font-size: 13px;
  font-weight: 900;
  transition:
    transform 0.25s ease,
    box-shadow 0.25s ease,
    background 0.25s ease;
}
.home-btn:hover {
  transform: translateY(-2px);
}
.home-btn--light {
  background: #fff;
  color: #1c1511;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}
.home-btn--ghost {
  border: 1px solid rgba(255, 255, 255, 0.25);
  background: rgba(255, 255, 255, 0.05);
  color: #fff;
  backdrop-filter: blur(10px);
}
.home-btn--wine {
  background: #7d1f2d;
  color: #fff;
  box-shadow: 0 10px 25px rgba(125, 31, 45, 0.3);
}
.home-hero__stats {
  display: flex;
  margin-top: 40px;
}
.home-hero__stats > div {
  min-width: 110px;
  padding: 0 20px;
  border-left: 1px solid rgba(255, 255, 255, 0.15);
}
.home-hero__stats > div:first-child {
  padding-left: 0;
  border-left: 0;
}
.home-hero__stats strong {
  display: block;
  font-size: 22px;
  color: #fff;
}
.home-hero__stats span {
  display: block;
  margin-top: 2px;
  color: rgba(255, 255, 255, 0.5);
  font-size: 10px;
  font-weight: 800;
  text-transform: uppercase;
}
.home-hero__visual {
  position: relative;
  width: min(100%, 500px);
  justify-self: end;
}
.home-hero__image {
  position: relative;
  overflow: hidden;
  aspect-ratio: 0.85;
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: 28px;
  background: #2c201b;
  box-shadow: 0 30px 70px rgba(0, 0, 0, 0.4);
}
.home-hero__image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 1s cubic-bezier(0.2, 0.8, 0.2, 1);
}
.home-hero__visual:hover .home-hero__image img {
  transform: scale(1.03);
}
.home-hero__image-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, transparent 40%, rgba(10, 5, 3, 0.7));
}
.home-hero__image-caption {
  position: absolute;
  right: 20px;
  bottom: 20px;
  left: 20px;
}
.home-hero__image-caption span {
  display: block;
  color: rgba(255, 255, 255, 0.7);
  font-size: 9px;
  font-weight: 900;
  letter-spacing: 0.2em;
}
.home-hero__image-caption strong {
  display: block;
  margin-top: 4px;
  color: #fff;
  font-size: 18px;
}
.home-floating {
  position: absolute;
  z-index: 3;
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.25);
  backdrop-filter: blur(12px);
}
.home-floating--top {
  top: 30px;
  right: -15px;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  border-radius: 12px;
  padding: 10px 14px;
  background: rgba(125, 31, 45, 0.9);
  color: #fff;
  font-size: 11px;
  font-weight: 900;
}
.home-floating--bottom {
  bottom: -15px;
  left: -20px;
  width: 190px;
  border-radius: 16px;
  padding: 14px 16px;
  background: rgba(255, 255, 255, 0.95);
  color: #1c1511;
}
.home-floating--bottom span {
  display: block;
  color: #776e65;
  font-size: 9px;
  font-weight: 800;
}
.home-floating--bottom strong {
  display: block;
  margin-top: 3px;
  color: #7d1f2d;
  font-size: 14px;
}

/* Service Strip */
.home-services {
  position: relative;
  z-index: 5;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  margin-top: -36px;
  border: 1px solid #e8dfd3;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 0 15px 40px rgba(48, 35, 24, 0.08);
  backdrop-filter: blur(12px);
}
.home-service {
  display: flex;
  align-items: center;
  gap: 12px;
  min-height: 105px;
  padding: 18px 24px;
  border-right: 1px solid #e8dfd3;
}
.home-service:last-child {
  border-right: 0;
}
.home-service__icon {
  display: grid;
  width: 40px;
  height: 40px;
  flex: 0 0 auto;
  place-items: center;
  border: 1px solid #ebd9c2;
  border-radius: 12px;
  background: #f7ede0;
  color: #7d1f2d;
}
.home-service strong {
  display: block;
  font-size: 13px;
  color: #1c1511;
}
.home-service span {
  display: block;
  margin-top: 2px;
  color: #776e65;
  font-size: 10px;
}

/* Sections */
.home-section {
  padding: 90px 0;
}
.home-section--paper {
  background: #fff;
}
.home-heading h2 {
  margin-top: 8px;
  color: #1c1511;
  font-size: clamp(2rem, 3.8vw, 3.5rem);
  line-height: 1.1;
  letter-spacing: -0.04em;
  font-weight: 900;
}
.home-heading p {
  margin-top: 10px;
  color: #776e65;
  font-size: 14px;
}
.home-heading--center {
  text-align: center;
}
.home-heading--center p {
  max-width: 580px;
  margin-inline: auto;
}
.home-heading--split {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 20px;
}
.home-text-link {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  color: #7d1f2d;
  font-size: 12px;
  font-weight: 900;
  white-space: nowrap;
}
.home-text-link--light {
  color: #fff;
}

/* Categories Grid */
.home-categories {
  display: grid;
  grid-template-columns: repeat(6, minmax(0, 1fr));
  gap: 20px;
  margin-top: 45px;
}
.home-category {
  text-align: center;
  transition: transform 0.3s ease;
}
.home-category:hover {
  transform: translateY(-5px);
}
.home-category__image {
  overflow: hidden;
  aspect-ratio: 1;
  border-radius: 50%;
  background: #f4eee7;
  box-shadow: inset 0 0 0 1px #e6dbcd;
}
.home-category__image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}
.home-category:hover img {
  transform: scale(1.06);
}
.home-category > span {
  display: inline-block;
  margin-top: 10px;
  color: #1c1511;
  font-size: 12px;
  font-weight: 900;
}
.home-category > svg {
  margin-left: 3px;
  vertical-align: -2px;
  color: #7d1f2d;
}

/* Sale Section */
.home-sale {
  background: linear-gradient(135deg, #7d1f2d 0%, #9e3241 60%, #c46d5c 100%);
}
.home-heading--light h2 {
  color: #fff;
}
.home-heading--light p {
  color: rgba(255, 255, 255, 0.75);
}
.home-products {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 20px;
  margin-top: 35px;
}
.home-product-card {
  overflow: hidden;
  border-radius: 16px;
  background: #fff;
  box-shadow: 0 10px 30px rgba(30, 17, 14, 0.08);
  transition:
    transform 0.3s ease,
    box-shadow 0.3s ease;
}
.home-product-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 20px 45px rgba(30, 17, 14, 0.15);
}
.home-product-card__media {
  position: relative;
  display: block;
  overflow: hidden;
  aspect-ratio: 0.82;
  background: #f2ede7;
}
.home-product-card__media img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.6s ease;
}
.home-product-card:hover .home-product-card__media img {
  transform: scale(1.04);
}
.home-product-card__badge {
  position: absolute;
  left: 10px;
  top: 10px;
  border-radius: 999px;
  background: #fff;
  padding: 5px 8px;
  color: #7d1f2d;
  font-size: 10px;
  font-weight: 900;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}
.home-product-card__quick {
  position: absolute;
  left: 50%;
  bottom: 12px;
  display: inline-flex;
  align-items: center;
  gap: 4px;
  transform: translate(-50%, 10px);
  opacity: 0;
  border: 0;
  border-radius: 999px;
  background: #fff;
  padding: 8px 12px;
  color: #1c1511;
  font-size: 10px;
  font-weight: 900;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
  transition: 0.25s ease;
  white-space: nowrap;
}
.home-product-card:hover .home-product-card__quick {
  transform: translate(-50%, 0);
  opacity: 1;
}
.home-product-card__body {
  padding: 14px;
}
.home-product-card__body > span {
  color: #7d1f2d;
  font-size: 9px;
  font-weight: 900;
  letter-spacing: 0.1em;
  text-transform: uppercase;
}
.home-product-card__body h3 {
  min-height: 36px;
  margin-top: 5px;
  color: #1c1511;
  font-size: 13px;
  line-height: 1.35;
  font-weight: 700;
}
.home-product-card__price {
  display: flex;
  align-items: baseline;
  gap: 8px;
  margin-top: 8px;
}
.home-product-card__price strong {
  color: #7d1f2d;
  font-size: 14px;
  font-weight: 900;
}
.home-product-card__price del {
  color: #9b9187;
  font-size: 11px;
}
.home-products--loading {
  grid-template-columns: repeat(4, 1fr);
}
.home-skeleton {
  aspect-ratio: 0.82;
  border-radius: 16px;
  background: linear-gradient(110deg, #eadfd2 30%, #f7efe5 50%, #eadfd2 70%);
  background-size: 200% 100%;
  animation: shimmer 1.2s linear infinite;
}
@keyframes shimmer {
  to {
    background-position: -200% 0;
  }
}
.home-empty {
  margin-top: 30px;
  border: 1px dashed rgba(255, 255, 255, 0.4);
  border-radius: 16px;
  padding: 30px;
  color: rgba(255, 255, 255, 0.8);
  text-align: center;
}

/* Tabs */
.home-tabs {
  display: flex;
  justify-content: center;
  gap: 24px;
  margin-top: 30px;
  border-bottom: 1px solid #e8dfd3;
}
.home-tabs button {
  position: relative;
  border: 0;
  background: transparent;
  padding: 0 0 12px;
  color: #776e65;
  font-size: 12px;
  font-weight: 900;
  cursor: pointer;
}
.home-tabs button::after {
  position: absolute;
  right: 0;
  bottom: -1px;
  left: 0;
  height: 2px;
  border-radius: 999px;
  background: #7d1f2d;
  content: '';
  transform: scaleX(0);
  transition: transform 0.25s ease;
}
.home-tabs button.active {
  color: #7d1f2d;
}
.home-tabs button.active::after {
  transform: scaleX(1);
}
.home-center-action {
  display: flex;
  justify-content: center;
  margin-top: 35px;
}

/* Editorial Banner */
.home-editorial {
  display: grid;
  grid-template-columns: 1.1fr 0.9fr;
  min-height: 500px;
  background: #191412;
}
.home-editorial__image {
  overflow: hidden;
  min-height: 500px;
}
.home-editorial__image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 1s ease;
}
.home-editorial:hover .home-editorial__image img {
  transform: scale(1.03);
}
.home-editorial__panel {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: clamp(40px, 6vw, 80px);
  background:
    radial-gradient(circle at 90% 10%, rgba(189, 140, 77, 0.15), transparent 20rem), #1b1512;
}
.home-editorial__panel h2 {
  margin-top: 12px;
  color: #fff;
  font-size: clamp(2.6rem, 5vw, 5rem);
  line-height: 0.95;
  letter-spacing: -0.05em;
}
.home-editorial__panel h2 em {
  color: #d5ab6d;
  font-style: normal;
}
.home-editorial__panel p {
  max-width: 500px;
  margin-top: 18px;
  color: rgba(255, 255, 255, 0.7);
  font-size: 14px;
  line-height: 1.7;
}
.home-editorial__panel .home-btn {
  align-self: flex-start;
  margin-top: 24px;
}

/* News Section */
.home-news {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-top: 40px;
}
.home-news-card {
  overflow: hidden;
  border: 1px solid #e8dfd3;
  border-radius: 16px;
  background: #fff;
  transition:
    transform 0.3s ease,
    box-shadow 0.3s ease;
}
.home-news-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 35px rgba(48, 35, 24, 0.08);
}
.home-news-card__media {
  aspect-ratio: 1.6;
  overflow: hidden;
  background: #eee5db;
}
.home-news-card__media img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.6s ease;
}
.home-news-card:hover img {
  transform: scale(1.04);
}
.home-news-card__body {
  padding: 18px;
}
.home-news-card__body > span {
  color: #7d1f2d;
  font-size: 9px;
  font-weight: 900;
  letter-spacing: 0.12em;
}
.home-news-card h3 {
  margin-top: 6px;
  color: #1c1511;
  font-size: 15px;
  line-height: 1.3;
}
.home-news-card p {
  margin-top: 6px;
  color: #776e65;
  font-size: 11px;
  line-height: 1.5;
}
.home-news-card a {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  margin-top: 12px;
  color: #7d1f2d;
  font-size: 10px;
  font-weight: 900;
}

@media (max-width: 1024px) {
  .home-hero__inner {
    grid-template-columns: 1fr;
  }
  .home-hero__visual {
    justify-self: center;
    width: min(80vw, 480px);
  }
  .home-services {
    grid-template-columns: repeat(2, 1fr);
  }
  .home-service:nth-child(2) {
    border-right: 0;
  }
  .home-service:nth-child(-n + 2) {
    border-bottom: 1px solid #e8dfd3;
  }
  .home-categories {
    grid-template-columns: repeat(3, 1fr);
  }
  .home-products {
    grid-template-columns: repeat(2, 1fr);
  }
  .home-news {
    grid-template-columns: 1fr;
  }
  .home-editorial {
    grid-template-columns: 1fr;
  }
}
</style>
