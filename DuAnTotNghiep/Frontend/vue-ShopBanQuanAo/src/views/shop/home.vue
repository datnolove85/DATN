<template>
  <main class="home-page">
    <!-- 01. HERO -->
    <section class="home-hero" data-lux-reveal>
      <div class="home-hero__glow home-hero__glow--wine"></div>
      <div class="home-hero__glow home-hero__glow--gold"></div>
      <div class="home-hero__grain"></div>

      <div class="home-container home-hero__inner">
        <div class="home-hero__copy">
          <span class="home-kicker"><Sparkles :size="15" /> BỘ SƯU TẬP MỚI</span>
          <h1>Phong cách của bạn.<br /><em>Khác biệt của bạn.</em></h1>
          <p>
            Khám phá những sản phẩm đang được yêu thích, chọn đúng phong cách và hoàn thiện outfit
            theo cách của riêng bạn.
          </p>
          <div class="home-hero__actions">
            <RouterLink to="/san-pham" class="home-btn home-btn--light">
              Khám phá sản phẩm <ArrowRight :size="17" />
            </RouterLink>
            <RouterLink to="/promotion" class="home-btn home-btn--ghost"> Xem ưu đãi </RouterLink>
          </div>
          <div class="home-hero__stats">
            <div>
              <strong>{{ stats.products }}+</strong><span>Sản phẩm</span>
            </div>
            <div>
              <strong>{{ stats.categories }}+</strong><span>Danh mục</span>
            </div>
            <div>
              <strong>{{ stats.brands }}+</strong><span>Thương hiệu</span>
            </div>
          </div>
        </div>

        <div class="home-hero__visual" data-lux-tilt>
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
          <div class="home-floating home-floating--top"><Flame :size="16" /> Được yêu thích</div>
          <div class="home-floating home-floating--bottom">
            <span>Giá đang áp dụng</span>
            <strong>{{ featuredProduct ? priceLabel(featuredProduct) : 'Khám phá ngay' }}</strong>
          </div>
        </div>
      </div>
    </section>

    <!-- 02. SERVICE STRIP -->
    <section class="home-container home-services" data-lux-reveal data-motion="rise">
      <div v-for="service in services" :key="service.title" class="home-service">
        <span class="home-service__icon"><component :is="service.icon" :size="21" /></span>
        <div>
          <strong>{{ service.title }}</strong
          ><span>{{ service.description }}</span>
        </div>
      </div>
    </section>

    <!-- 03. CATEGORY -->
    <section class="home-section home-section--paper" data-lux-reveal data-motion="rise">
      <div class="home-container">
        <div class="home-heading home-heading--center">
          <span class="home-kicker home-kicker--dark">DANH MỤC NỔI BẬT</span>
          <h2>Chọn theo phong cách</h2>
          <p>Đi thẳng đến nhóm sản phẩm bạn đang tìm kiếm.</p>
        </div>

        <div class="home-categories">
          <RouterLink
            v-for="(category, index) in categories"
            :key="category.name"
            to="/san-pham"
            class="home-category"
            :style="{ '--category-delay': `${index * 55}ms` }"
          >
            <div class="home-category__image">
              <img :src="category.image || heroFallback" :alt="category.name" loading="lazy" />
            </div>
            <span>{{ category.name }}</span>
            <ArrowUpRight :size="15" />
          </RouterLink>
        </div>
      </div>
    </section>

    <!-- 04. VOUCHER / OFFERS -->
    <section class="home-section home-section--ivory" data-lux-reveal data-motion="rise">
      <div class="home-container">
        <div class="home-heading home-heading--split">
          <div>
            <span class="home-kicker home-kicker--dark">ƯU ĐÃI HÔM NAY</span>
            <h2>Ưu đãi dành cho bạn</h2>
          </div>
          <RouterLink to="/promotion" class="home-text-link"
            >Xem tất cả <ArrowRight :size="16"
          /></RouterLink>
        </div>

        <div class="home-offers">
          <RouterLink v-for="offer in offers" :key="offer.title" to="/promotion" class="home-offer">
            <span class="home-offer__code">{{ offer.code }}</span>
            <strong>{{ offer.title }}</strong>
            <small>{{ offer.description }}</small>
            <span class="home-offer__cta">Xem ngay <ArrowRight :size="14" /></span>
          </RouterLink>
        </div>
      </div>
    </section>

    <!-- 05. SALE PRODUCTS -->
    <section class="home-section home-sale" data-lux-reveal data-motion="rise">
      <div class="home-container">
        <div class="home-heading home-heading--light home-heading--split">
          <div>
            <span class="home-kicker">SPECIAL EDIT</span>
            <h2>Ưu đãi đặc biệt</h2>
            <p>Những sản phẩm đang có mức giá tốt trong hệ thống.</p>
          </div>
          <RouterLink to="/san-pham" class="home-text-link home-text-link--light"
            >Xem tất cả <ArrowRight :size="16"
          /></RouterLink>
        </div>

        <div v-if="loading" class="home-products home-products--loading">
          <div v-for="n in 4" :key="n" class="home-skeleton"></div>
        </div>
        <div v-else-if="saleProducts.length" class="home-products">
          <article v-for="product in saleProducts" :key="product.id" class="home-product-card">
            <RouterLink
              :to="{ name: 'confirmbuy', params: { id: product.id } }"
              class="home-product-card__media"
            >
              <img
                :src="imageUrl(product)"
                :alt="product.tenSanPham"
                loading="lazy"
                @error="handleImageError"
              />
              <span v-if="product.maxDiscountPercent" class="home-product-card__badge"
                >-{{ product.maxDiscountPercent }}%</span
              >
              <button
                type="button"
                class="home-product-card__quick"
                @click.prevent="goToDetail(product.id)"
              >
                Xem chi tiết <ArrowUpRight :size="14" />
              </button>
            </RouterLink>
            <div class="home-product-card__body">
              <span>{{ product.idThuongHieu?.tenThuongHieu || 'Thời trang' }}</span>
              <h3>{{ product.tenSanPham }}</h3>
              <div class="home-product-card__price">
                <strong>{{ priceLabel(product) }}</strong>
                <del v-if="product.originalPrice > product.displayPrice">{{
                  formatPrice(product.originalPrice)
                }}</del>
              </div>
            </div>
          </article>
        </div>
        <div v-else class="home-empty">Chưa có sản phẩm khuyến mãi để hiển thị.</div>
      </div>
    </section>

    <!-- 06. TODAY PICKS -->
    <section class="home-section home-section--paper" data-lux-reveal data-motion="rise">
      <div class="home-container">
        <div class="home-heading home-heading--center">
          <span class="home-kicker home-kicker--dark">GỢI Ý HÔM NAY</span>
          <h2>Những lựa chọn đáng xem</h2>
          <p>Sản phẩm mới, dễ phối và đang được quan tâm.</p>
        </div>

        <div class="home-tabs" role="tablist" aria-label="Bộ lọc gợi ý">
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
          <article v-for="product in suggestedProducts" :key="product.id" class="home-product-card">
            <RouterLink
              :to="{ name: 'confirmbuy', params: { id: product.id } }"
              class="home-product-card__media"
            >
              <img
                :src="imageUrl(product)"
                :alt="product.tenSanPham"
                loading="lazy"
                @error="handleImageError"
              />
              <span v-if="product.maxDiscountPercent" class="home-product-card__badge"
                >-{{ product.maxDiscountPercent }}%</span
              >
            </RouterLink>
            <div class="home-product-card__body">
              <span>{{ product.idDanhMuc?.tenDanhMuc || 'Sản phẩm' }}</span>
              <h3>{{ product.tenSanPham }}</h3>
              <div class="home-product-card__price">
                <strong>{{ priceLabel(product) }}</strong>
              </div>
            </div>
          </article>
        </div>

        <div class="home-center-action">
          <RouterLink to="/san-pham" class="home-btn home-btn--wine"
            >Xem toàn bộ sản phẩm <ArrowRight :size="17"
          /></RouterLink>
        </div>
      </div>
    </section>

    <!-- 07. EDITORIAL -->
    <section class="home-editorial" data-lux-reveal data-motion="scale">
      <div class="home-editorial__image">
        <img :src="editorialImage" alt="Bộ sưu tập thời trang" loading="lazy" />
      </div>
      <div class="home-editorial__panel">
        <span class="home-kicker">CURATED WARDROBE</span>
        <h2>Ít hơn nhưng<br /><em>đúng hơn.</em></h2>
        <p>
          Một giao diện bán hàng tốt phải giúp khách nhìn thấy sản phẩm, hiểu thông tin và đi đến
          quyết định mua mà không bị phân tâm bởi những chi tiết thừa.
        </p>
        <RouterLink to="/san-pham" class="home-btn home-btn--light"
          >Khám phá bộ sưu tập <ArrowRight :size="17"
        /></RouterLink>
      </div>
    </section>

    <!-- 08. CAMPAIGN CARDS -->
    <section class="home-section home-section--ivory" data-lux-reveal data-motion="rise">
      <div class="home-container">
        <div class="home-heading home-heading--split">
          <div>
            <span class="home-kicker home-kicker--dark">VELORA EXPERIENCE</span>
            <h2>Mua sắm theo cảm hứng</h2>
          </div>
          <RouterLink to="/promotion" class="home-text-link"
            >Khám phá <ArrowRight :size="16"
          /></RouterLink>
        </div>
        <div class="home-campaigns">
          <RouterLink
            v-for="campaign in campaigns"
            :key="campaign.title"
            to="/san-pham"
            class="home-campaign"
            :style="{
              backgroundImage: `linear-gradient(90deg, rgba(16,12,10,.72), rgba(16,12,10,.18)), url('${campaign.image}')`,
            }"
          >
            <div>
              <span>{{ campaign.kicker }}</span
              ><strong>{{ campaign.title }}</strong
              ><small>{{ campaign.description }}</small>
            </div>
            <ArrowUpRight :size="22" />
          </RouterLink>
        </div>
      </div>
    </section>

    <!-- 09. NEWS / INFORMATION -->
    <section class="home-section home-section--paper" data-lux-reveal data-motion="rise">
      <div class="home-container">
        <div class="home-heading home-heading--center">
          <span class="home-kicker home-kicker--dark">TIN TỨC & GỢI Ý</span>
          <h2>Thêm cảm hứng cho phong cách</h2>
          <p>Những nội dung ngắn gọn để khách dễ khám phá trước khi mua.</p>
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
              <RouterLink to="/san-pham">Khám phá <ArrowRight :size="15" /></RouterLink>
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
import { useProductCatalog } from '@/composables/useProductCatalog'

const heroFallback =
  'https://images.unsplash.com/photo-1529139574466-a303027c1d8b?auto=format&fit=crop&w=1200&q=82'
const editorialImage =
  'https://images.unsplash.com/photo-1496747611176-843222e1e57c?auto=format&fit=crop&w=1400&q=82'

const {
  products,
  stats,
  loading,
  imageUrl,
  handleImageError,
  priceLabel,
  formatPrice,
  goToDetail,
  dynamicFilters,
} = useProductCatalog({ pageSize: 12 })

const activeTab = ref('Hàng mới về')
const tabs = ['Hàng mới về', 'Giá thấp', 'Giảm nhiều']

const services = [
  { title: 'Giao hàng toàn quốc', description: 'Theo chính sách cửa hàng', icon: Truck },
  { title: 'Thanh toán linh hoạt', description: 'Nhiều phương thức thuận tiện', icon: Box },
  { title: 'Đổi trả minh bạch', description: 'Thông tin rõ ràng trước khi mua', icon: ShieldCheck },
  { title: 'Hỗ trợ khi cần', description: 'Tư vấn nhanh và trực quan', icon: Headphones },
]

const offers = [
  { code: 'KHÁM PHÁ', title: 'Sản phẩm nổi bật', description: 'Đi thẳng đến danh sách sản phẩm' },
  {
    code: 'ĐANG GIẢM',
    title: 'Sản phẩm đang giảm giá',
    description: 'Xem các mức giá đang được áp dụng',
  },
  {
    code: 'MỚI CẬP NHẬT',
    title: 'Sản phẩm mới',
    description: 'Xem những sản phẩm được cập nhật gần đây',
  },
  {
    code: 'DỄ CHỌN',
    title: 'Lọc theo nhu cầu',
    description: 'Tìm sản phẩm theo danh mục và thông tin',
  },
]

const categories = computed(() => {
  const categoryOptions =
    dynamicFilters.value.find((item) => item.key === 'categories')?.options || []
  return categoryOptions.slice(0, 8).map((item, index) => ({
    name: item.name,
    image:
      products.value.find((product) => product.idDanhMuc?.tenDanhMuc === item.name)?.image ||
      [
        'https://images.unsplash.com/photo-1525507119028-ed4c629a60a3?auto=format&fit=crop&w=500&q=78',
        'https://images.unsplash.com/photo-1551488831-00ddcb6c6bd3?auto=format&fit=crop&w=500&q=78',
        'https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?auto=format&fit=crop&w=500&q=78',
        'https://images.unsplash.com/photo-1551028719-00167b16eac5?auto=format&fit=crop&w=500&q=78',
      ][index % 4],
  }))
})

const saleProducts = computed(() =>
  products.value.filter((product) => product.dangGiamGia).slice(0, 4),
)
const suggestedProducts = computed(() => {
  const sorted = [...products.value].sort(
    (a, b) => new Date(b.ngayTao || 0) - new Date(a.ngayTao || 0),
  )
  if (activeTab.value === 'Giá thấp')
    return [...sorted]
      .sort((a, b) => Number(a.displayPrice || 0) - Number(b.displayPrice || 0))
      .slice(0, 8)
  if (activeTab.value === 'Giảm nhiều')
    return [...sorted]
      .sort((a, b) => Number(b.maxDiscountPercent || 0) - Number(a.maxDiscountPercent || 0))
      .slice(0, 8)
  return sorted.slice(0, 8)
})

const campaigns = [
  {
    kicker: 'COLLECTION',
    title: 'Chọn đúng sản phẩm, đúng phong cách',
    description: 'Đi thẳng đến danh mục và khám phá sản phẩm.',
    image:
      'https://images.unsplash.com/photo-1445205170230-053b83016050?auto=format&fit=crop&w=1000&q=82',
  },
  {
    kicker: 'SALE EDIT',
    title: 'Tìm món đồ đáng tiền',
    description: 'Lọc nhanh sản phẩm đang có ưu đãi.',
    image:
      'https://images.unsplash.com/photo-1490481651871-ab68de25d43d?auto=format&fit=crop&w=1000&q=82',
  },
  {
    kicker: 'STYLE GUIDE',
    title: 'Xây outfit theo cách của bạn',
    description: 'Xem sản phẩm và thông tin trong một luồng.',
    image:
      'https://images.unsplash.com/photo-1483985988355-763728e1935b?auto=format&fit=crop&w=1000&q=82',
  },
  {
    kicker: 'DISCOVER',
    title: 'Khám phá điều mới',
    description: 'Xem những sản phẩm mới được cập nhật.',
    image:
      'https://images.unsplash.com/photo-1496747611176-843222e1e57c?auto=format&fit=crop&w=1000&q=82',
  },
]

const newsCards = [
  {
    meta: 'STYLE / 01',
    title: 'Chọn sản phẩm dễ hơn khi thông tin được đặt đúng chỗ',
    description: 'Tập trung vào hình ảnh, giá, lựa chọn và trạng thái sản phẩm.',
    image:
      'https://images.unsplash.com/photo-1485968579580-b6d095142e6e?auto=format&fit=crop&w=1000&q=82',
  },
  {
    meta: 'STYLE / 02',
    title: 'Một giao diện tốt không cần quá nhiều chi tiết',
    description: 'Khoảng trắng, typography và chuyển động vừa đủ để dẫn mắt.',
    image:
      'https://images.unsplash.com/photo-1485230895905-ec40ba36b9bc?auto=format&fit=crop&w=1000&q=82',
  },
  {
    meta: 'STYLE / 03',
    title: 'Khám phá bộ sưu tập theo cách trực quan',
    description: 'Danh mục, ưu đãi và sản phẩm được tổ chức thành từng nhịp rõ ràng.',
    image:
      'https://images.unsplash.com/photo-1509631179647-0177331693ae?auto=format&fit=crop&w=1000&q=82',
  },
]

const featuredProduct = computed(() => products.value[0] || null)
</script>

<style scoped>
.home-page {
  overflow: clip;
  background: var(--lux-ivory);
}
.home-container {
  width: min(1440px, calc(100% - 48px));
  margin-inline: auto;
}
.home-hero {
  position: relative;
  overflow: hidden;
  min-height: 720px;
  background: linear-gradient(115deg, #191412 0%, #34221c 46%, #6f1e2a 100%);
  color: #fff;
}
.home-hero__inner {
  position: relative;
  z-index: 2;
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(440px, 0.78fr);
  align-items: center;
  gap: clamp(40px, 7vw, 110px);
  min-height: 720px;
  padding-block: 78px;
}
.home-hero__glow {
  position: absolute;
  border-radius: 999px;
  filter: blur(90px);
  pointer-events: none;
}
.home-hero__glow--wine {
  width: 500px;
  height: 500px;
  right: 8%;
  top: -180px;
  background: rgba(186, 68, 87, 0.26);
}
.home-hero__glow--gold {
  width: 430px;
  height: 430px;
  left: -130px;
  bottom: -220px;
  background: rgba(210, 161, 91, 0.16);
}
.home-hero__grain {
  position: absolute;
  inset: 0;
  opacity: 0.08;
  background-image: radial-gradient(rgba(255, 255, 255, 0.75) 0.5px, transparent 0.5px);
  background-size: 5px 5px;
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
  color: var(--lux-wine);
}
.home-hero h1 {
  max-width: 820px;
  margin-top: 22px;
  color: #fff;
  font-size: clamp(3.6rem, 7.2vw, 7.8rem);
  line-height: 0.9;
  letter-spacing: -0.07em;
  font-weight: 950;
}
.home-hero h1 em {
  color: #d8ad70;
  font-style: normal;
}
.home-hero__copy > p {
  max-width: 620px;
  margin-top: 28px;
  color: rgba(255, 255, 255, 0.7);
  font-size: 16px;
  line-height: 1.8;
}
.home-hero__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 32px;
}
.home-btn {
  display: inline-flex;
  min-height: 48px;
  align-items: center;
  justify-content: center;
  gap: 8px;
  border-radius: 14px;
  padding: 0 18px;
  font-size: 13px;
  font-weight: 900;
  transition:
    transform 0.28s ease,
    box-shadow 0.28s ease,
    background 0.28s ease;
}
.home-btn:hover {
  transform: translateY(-2px);
}
.home-btn--light {
  background: #fffaf3;
  color: #1c1511;
  box-shadow: 0 18px 40px rgba(0, 0, 0, 0.18);
}
.home-btn--ghost {
  border: 1px solid rgba(255, 255, 255, 0.22);
  background: rgba(255, 255, 255, 0.06);
  color: #fff;
  backdrop-filter: blur(14px);
}
.home-btn--wine {
  background: var(--lux-wine);
  color: #fff;
  box-shadow: 0 16px 34px rgba(125, 31, 45, 0.2);
}
.home-hero__stats {
  display: flex;
  margin-top: 46px;
}
.home-hero__stats > div {
  min-width: 120px;
  padding: 0 22px;
  border-left: 1px solid rgba(255, 255, 255, 0.15);
}
.home-hero__stats > div:first-child {
  padding-left: 0;
  border-left: 0;
}
.home-hero__stats strong,
.home-hero__stats span {
  display: block;
}
.home-hero__stats strong {
  font-size: 23px;
  color: #fff;
}
.home-hero__stats span {
  margin-top: 4px;
  color: rgba(255, 255, 255, 0.5);
  font-size: 10px;
  font-weight: 800;
}
.home-hero__visual {
  position: relative;
  width: min(100%, 560px);
  justify-self: end;
  transform-style: preserve-3d;
}
.home-hero__image {
  position: relative;
  overflow: hidden;
  aspect-ratio: 0.82;
  border: 1px solid rgba(255, 255, 255, 0.16);
  border-radius: 34px;
  background: #2c201b;
  box-shadow: 0 35px 90px rgba(0, 0, 0, 0.35);
}
.home-hero__image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 1.1s cubic-bezier(0.2, 0.75, 0.2, 1);
}
.home-hero__visual:hover .home-hero__image img {
  transform: scale(1.035);
}
.home-hero__image-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, rgba(0, 0, 0, 0.04), transparent 40%, rgba(10, 5, 3, 0.62));
}
.home-hero__image-caption {
  position: absolute;
  right: 24px;
  bottom: 24px;
  left: 24px;
}
.home-hero__image-caption span,
.home-hero__image-caption strong {
  display: block;
}
.home-hero__image-caption span {
  color: rgba(255, 255, 255, 0.65);
  font-size: 9px;
  font-weight: 900;
  letter-spacing: 0.2em;
}
.home-hero__image-caption strong {
  margin-top: 5px;
  color: #fff;
  font-size: 19px;
}
.home-floating {
  position: absolute;
  z-index: 3;
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 20px 55px rgba(0, 0, 0, 0.26);
  backdrop-filter: blur(18px);
  animation: homeFloat 5.5s ease-in-out infinite;
}
.home-floating--top {
  top: 42px;
  right: -18px;
  display: inline-flex;
  align-items: center;
  gap: 7px;
  border-radius: 15px;
  padding: 11px 14px;
  background: rgba(125, 31, 45, 0.92);
  color: #fff;
  font-size: 11px;
  font-weight: 900;
}
.home-floating--bottom {
  bottom: -12px;
  left: -28px;
  width: 210px;
  border-radius: 19px;
  padding: 15px 17px;
  background: rgba(255, 253, 249, 0.94);
  color: var(--lux-ink);
  animation-delay: 0.7s;
}
.home-floating--bottom span,
.home-floating--bottom strong {
  display: block;
}
.home-floating--bottom span {
  color: var(--lux-muted);
  font-size: 9px;
  font-weight: 800;
}
.home-floating--bottom strong {
  margin-top: 4px;
  color: var(--lux-wine);
  font-size: 14px;
}
@keyframes homeFloat {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-9px);
  }
}

.home-services {
  position: relative;
  z-index: 5;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  margin-top: -42px;
  border: 1px solid var(--lux-border);
  border-radius: 24px;
  background: rgba(255, 253, 249, 0.96);
  box-shadow: 0 20px 55px rgba(48, 35, 24, 0.12);
  backdrop-filter: blur(18px);
}
.home-service {
  display: flex;
  align-items: center;
  gap: 13px;
  min-height: 118px;
  padding: 22px 28px;
  border-right: 1px solid var(--lux-border);
}
.home-service:last-child {
  border-right: 0;
}
.home-service__icon {
  display: grid;
  width: 44px;
  height: 44px;
  flex: 0 0 auto;
  place-items: center;
  border: 1px solid #ead8bc;
  border-radius: 14px;
  background: #f8ead8;
  color: var(--lux-wine);
}
.home-service strong,
.home-service span {
  display: block;
}
.home-service strong {
  font-size: 13px;
  color: var(--lux-ink);
}
.home-service span {
  margin-top: 4px;
  color: var(--lux-muted);
  font-size: 10px;
  line-height: 1.5;
}

.home-section {
  padding: 108px 0;
}
.home-section--paper {
  background: #fffdf9;
}
.home-section--ivory {
  background: var(--lux-ivory);
}
.home-heading h2 {
  margin-top: 11px;
  color: var(--lux-ink);
  font-size: clamp(2.1rem, 4.2vw, 4rem);
  line-height: 1;
  letter-spacing: -0.055em;
  font-weight: 950;
}
.home-heading p {
  margin-top: 13px;
  color: var(--lux-muted);
  line-height: 1.7;
}
.home-heading--center {
  text-align: center;
}
.home-heading--center p {
  max-width: 640px;
  margin-inline: auto;
}
.home-heading--split {
  display: flex;
  align-items: end;
  justify-content: space-between;
  gap: 24px;
}
.home-text-link {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  color: var(--lux-wine);
  font-size: 12px;
  font-weight: 900;
  white-space: nowrap;
}
.home-text-link--light {
  color: #fff;
}

.home-categories {
  display: grid;
  grid-template-columns: repeat(8, minmax(0, 1fr));
  gap: 18px;
  margin-top: 58px;
}
.home-category {
  text-align: center;
  transition: transform 0.3s ease;
}
.home-category:hover {
  transform: translateY(-7px);
}
.home-category__image {
  overflow: hidden;
  aspect-ratio: 1;
  border-radius: 999px;
  background: #f5eee7;
  box-shadow: inset 0 0 0 1px #eee3d7;
}
.home-category__image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  mix-blend-mode: multiply;
  transition: transform 0.6s cubic-bezier(0.2, 0.75, 0.2, 1);
}
.home-category:hover img {
  transform: scale(1.06);
}
.home-category > span {
  display: inline-block;
  margin-top: 12px;
  color: var(--lux-ink);
  font-size: 11px;
  font-weight: 900;
}
.home-category > svg {
  margin-left: 4px;
  vertical-align: -2px;
  color: var(--lux-wine);
}

.home-offers {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-top: 46px;
}
.home-offer {
  position: relative;
  overflow: hidden;
  min-height: 170px;
  border: 1px solid #e6d7c5;
  border-radius: 20px;
  padding: 24px;
  background: linear-gradient(145deg, #fffdf9, #f6e9d8);
  transition:
    transform 0.3s ease,
    box-shadow 0.3s ease;
}
.home-offer::after {
  position: absolute;
  width: 120px;
  height: 120px;
  right: -40px;
  top: -50px;
  border-radius: 999px;
  background: rgba(125, 31, 45, 0.08);
  content: '';
}
.home-offer:hover {
  transform: translateY(-6px);
  box-shadow: 0 22px 45px rgba(48, 35, 24, 0.1);
}
.home-offer__code {
  color: var(--lux-wine);
  font-size: 9px;
  font-weight: 950;
  letter-spacing: 0.18em;
}
.home-offer strong {
  display: block;
  max-width: 210px;
  margin-top: 15px;
  color: var(--lux-ink);
  font-size: 18px;
  line-height: 1.15;
}
.home-offer small {
  display: block;
  max-width: 220px;
  margin-top: 7px;
  color: var(--lux-muted);
  font-size: 11px;
  line-height: 1.5;
}
.home-offer__cta {
  position: absolute;
  left: 24px;
  bottom: 20px;
  display: inline-flex;
  align-items: center;
  gap: 5px;
  color: var(--lux-wine);
  font-size: 10px;
  font-weight: 900;
}

.home-sale {
  background: linear-gradient(120deg, #6f1f2b 0%, #9a3342 55%, #d07f6b 100%);
}
.home-heading--light h2 {
  color: #fff;
}
.home-heading--light p {
  color: rgba(255, 255, 255, 0.68);
}
.home-products {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 20px;
  margin-top: 44px;
}
.home-product-card {
  overflow: hidden;
  border-radius: 20px;
  background: #fffdf9;
  box-shadow: 0 15px 40px rgba(30, 17, 14, 0.13);
  transition:
    transform 0.35s cubic-bezier(0.2, 0.75, 0.2, 1),
    box-shadow 0.35s ease;
}
.home-product-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 26px 60px rgba(30, 17, 14, 0.2);
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
  transition: transform 0.75s cubic-bezier(0.2, 0.75, 0.2, 1);
}
.home-product-card:hover .home-product-card__media img {
  transform: scale(1.045);
}
.home-product-card__badge {
  position: absolute;
  left: 12px;
  top: 12px;
  border-radius: 999px;
  background: #fff;
  padding: 7px 9px;
  color: var(--lux-wine);
  font-size: 10px;
  font-weight: 950;
  box-shadow: 0 7px 18px rgba(0, 0, 0, 0.12);
}
.home-product-card__quick {
  position: absolute;
  left: 50%;
  bottom: 14px;
  display: inline-flex;
  align-items: center;
  gap: 5px;
  transform: translate(-50%, 15px);
  opacity: 0;
  border: 0;
  border-radius: 999px;
  background: #fff;
  padding: 10px 13px;
  color: var(--lux-ink);
  font-size: 10px;
  font-weight: 900;
  box-shadow: 0 10px 24px rgba(0, 0, 0, 0.18);
  transition: 0.3s ease;
  white-space: nowrap;
}
.home-product-card:hover .home-product-card__quick {
  transform: translate(-50%, 0);
  opacity: 1;
}
.home-product-card__body {
  padding: 15px 16px 18px;
}
.home-product-card__body > span {
  color: var(--lux-wine);
  font-size: 9px;
  font-weight: 950;
  letter-spacing: 0.12em;
  text-transform: uppercase;
}
.home-product-card__body h3 {
  min-height: 38px;
  margin-top: 7px;
  color: var(--lux-ink);
  font-size: 14px;
  line-height: 1.35;
}
.home-product-card__price {
  display: flex;
  align-items: baseline;
  gap: 8px;
  margin-top: 10px;
}
.home-product-card__price strong {
  color: var(--lux-wine);
  font-size: 15px;
}
.home-product-card__price del {
  color: #9b9187;
  font-size: 11px;
}
.home-products--dark-text .home-product-card {
  box-shadow: 0 10px 28px rgba(48, 35, 24, 0.07);
}
.home-products--loading {
  grid-template-columns: repeat(4, 1fr);
}
.home-skeleton {
  aspect-ratio: 0.82;
  border-radius: 20px;
  background: linear-gradient(110deg, #eadfd2 30%, #f7efe5 50%, #eadfd2 70%);
  background-size: 220% 100%;
  animation: homeShimmer 1.4s linear infinite;
}
@keyframes homeShimmer {
  to {
    background-position: -220% 0;
  }
}
.home-empty {
  margin-top: 40px;
  border: 1px dashed rgba(255, 255, 255, 0.35);
  border-radius: 18px;
  padding: 35px;
  color: rgba(255, 255, 255, 0.75);
  text-align: center;
}

.home-tabs {
  display: flex;
  justify-content: center;
  gap: 28px;
  margin-top: 36px;
  border-bottom: 1px solid var(--lux-border);
}
.home-tabs button {
  position: relative;
  border: 0;
  background: transparent;
  padding: 0 0 13px;
  color: #8c8278;
  font-size: 12px;
  font-weight: 900;
}
.home-tabs button::after {
  position: absolute;
  right: 0;
  bottom: -1px;
  left: 0;
  height: 2px;
  border-radius: 999px;
  background: var(--lux-wine);
  content: '';
  transform: scaleX(0);
  transition: transform 0.25s ease;
}
.home-tabs button.active {
  color: var(--lux-wine);
}
.home-tabs button.active::after {
  transform: scaleX(1);
}
.home-center-action {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}

.home-editorial {
  display: grid;
  grid-template-columns: 1.15fr 0.85fr;
  min-height: 540px;
  background: #191412;
}
.home-editorial__image {
  overflow: hidden;
  min-height: 540px;
}
.home-editorial__image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 1.1s ease;
}
.home-editorial:hover .home-editorial__image img {
  transform: scale(1.035);
}
.home-editorial__panel {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: clamp(44px, 7vw, 100px);
  background:
    radial-gradient(circle at 90% 10%, rgba(189, 140, 77, 0.18), transparent 24rem), #1b1512;
}
.home-editorial__panel h2 {
  margin-top: 14px;
  color: #fff;
  font-size: clamp(3rem, 5.5vw, 6rem);
  line-height: 0.9;
  letter-spacing: -0.065em;
}
.home-editorial__panel h2 em {
  color: #d5ab6d;
  font-style: normal;
}
.home-editorial__panel p {
  max-width: 540px;
  margin-top: 22px;
  color: rgba(255, 255, 255, 0.64);
  font-size: 14px;
  line-height: 1.8;
}
.home-editorial__panel .home-btn {
  align-self: flex-start;
  margin-top: 28px;
}

.home-campaigns {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 18px;
  margin-top: 44px;
}
.home-campaign {
  position: relative;
  display: flex;
  min-height: 220px;
  align-items: flex-end;
  justify-content: space-between;
  overflow: hidden;
  border-radius: 22px;
  background-position: center;
  background-size: cover;
  padding: 28px;
  color: #fff;
  transition:
    transform 0.35s ease,
    box-shadow 0.35s ease;
}
.home-campaign:hover {
  transform: translateY(-6px);
  box-shadow: 0 24px 50px rgba(48, 35, 24, 0.16);
}
.home-campaign span,
.home-campaign strong,
.home-campaign small {
  display: block;
}
.home-campaign span {
  font-size: 9px;
  font-weight: 900;
  letter-spacing: 0.18em;
  color: #f1d7b4;
}
.home-campaign strong {
  max-width: 380px;
  margin-top: 8px;
  font-size: 24px;
  line-height: 1.05;
}
.home-campaign small {
  max-width: 400px;
  margin-top: 7px;
  color: rgba(255, 255, 255, 0.72);
  font-size: 11px;
  line-height: 1.5;
}
.home-campaign > svg {
  flex: 0 0 auto;
  margin-left: 18px;
}

.home-news {
  display: grid;
  grid-template-columns: 1.2fr 1fr 1fr;
  gap: 20px;
  margin-top: 48px;
}
.home-news-card {
  overflow: hidden;
  border: 1px solid var(--lux-border);
  border-radius: 20px;
  background: #fff;
  transition:
    transform 0.3s ease,
    box-shadow 0.3s ease;
}
.home-news-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 20px 45px rgba(48, 35, 24, 0.1);
}
.home-news-card__media {
  aspect-ratio: 1.55;
  overflow: hidden;
  background: #eee5db;
}
.home-news-card__media img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.7s ease;
}
.home-news-card:hover img {
  transform: scale(1.045);
}
.home-news-card__body {
  padding: 20px;
}
.home-news-card__body > span {
  color: var(--lux-wine);
  font-size: 9px;
  font-weight: 950;
  letter-spacing: 0.14em;
}
.home-news-card h3 {
  margin-top: 8px;
  color: var(--lux-ink);
  font-size: 17px;
  line-height: 1.2;
}
.home-news-card p {
  margin-top: 8px;
  font-size: 11px;
  line-height: 1.6;
}
.home-news-card a {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  margin-top: 15px;
  color: var(--lux-wine);
  font-size: 10px;
  font-weight: 900;
}

@media (max-width: 1100px) {
  .home-hero__inner {
    grid-template-columns: 1fr;
    min-height: auto;
  }
  .home-hero__visual {
    justify-self: center;
    width: min(82vw, 560px);
  }
  .home-services {
    grid-template-columns: repeat(2, 1fr);
  }
  .home-service:nth-child(2) {
    border-right: 0;
  }
  .home-service:nth-child(-n + 2) {
    border-bottom: 1px solid var(--lux-border);
  }
  .home-categories {
    grid-template-columns: repeat(4, 1fr);
  }
  .home-offers {
    grid-template-columns: repeat(2, 1fr);
  }
  .home-products,
  .home-products--loading {
    grid-template-columns: repeat(2, 1fr);
  }
  .home-news {
    grid-template-columns: 1fr 1fr;
  }
  .home-news-card:first-child {
    grid-column: 1 / -1;
  }
}

@media (max-width: 760px) {
  .home-container {
    width: min(100% - 28px, 1440px);
  }
  .home-hero__inner {
    padding-block: 55px 75px;
  }
  .home-hero h1 {
    font-size: clamp(3.1rem, 14vw, 5rem);
  }
  .home-hero__stats > div {
    min-width: 0;
    flex: 1;
    padding-inline: 12px;
  }
  .home-hero__visual {
    width: calc(100% - 12px);
  }
  .home-floating--bottom {
    left: -7px;
  }
  .home-floating--top {
    right: -7px;
  }
  .home-services {
    grid-template-columns: 1fr;
    margin-top: -28px;
  }
  .home-service,
  .home-service:nth-child(2) {
    border-right: 0;
    border-bottom: 1px solid var(--lux-border);
  }
  .home-service:last-child {
    border-bottom: 0;
  }
  .home-section {
    padding: 76px 0;
  }
  .home-heading--split {
    align-items: flex-start;
    flex-direction: column;
  }
  .home-categories {
    grid-template-columns: repeat(2, 1fr);
    gap: 24px 18px;
  }
  .home-offers {
    grid-template-columns: 1fr;
  }
  .home-products,
  .home-products--loading {
    grid-template-columns: 1fr 1fr;
    gap: 12px;
  }
  .home-product-card__body {
    padding: 12px;
  }
  .home-product-card__body h3 {
    font-size: 12px;
  }
  .home-editorial {
    grid-template-columns: 1fr;
  }
  .home-editorial__image {
    min-height: 390px;
  }
  .home-editorial__panel {
    min-height: 430px;
    padding: 42px 28px;
  }
  .home-campaigns,
  .home-news {
    grid-template-columns: 1fr;
  }
  .home-news-card:first-child {
    grid-column: auto;
  }
  .home-tabs {
    gap: 17px;
    overflow-x: auto;
    justify-content: flex-start;
  }
  .home-tabs button {
    flex: 0 0 auto;
  }
}

@media (prefers-reduced-motion: reduce) {
  .home-floating,
  .home-product-card,
  .home-category,
  .home-campaign,
  .home-news-card {
    animation: none !important;
    transition: none !important;
  }
}
</style>
