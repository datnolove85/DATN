<template>
  <div class="w-full bg-slate-50 py-6">
    <section class="mx-auto max-w-[1480px] px-4 md:px-8">
      <div class="w-full rounded-2xl border border-slate-200 bg-white p-5 shadow-xs md:p-6">
        <!-- HEADER & TABS DANH MỤC -->
        <div class="mb-6 flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
          <div class="relative pb-1">
            <h2 class="text-xl font-black uppercase tracking-tight text-slate-900 md:text-2xl">
              {{ title }}
            </h2>
            <div class="absolute bottom-0 left-0 h-1 w-12 rounded-full bg-rose-600"></div>
          </div>

          <!-- Tab danh mục động -->
          <div v-if="categories && categories.length" class="flex flex-wrap items-center gap-2">
            <button
              v-for="cat in categories"
              :key="getCatId(cat)"
              type="button"
              class="rounded-lg px-4 py-2 text-xs font-bold transition md:text-sm"
              :class="
                selectedCategory === getCatId(cat)
                  ? 'bg-rose-600 text-white shadow-xs'
                  : 'bg-slate-100 text-slate-700 hover:bg-slate-200'
              "
              @click="handleCategoryChange(getCatId(cat))"
            >
              {{ getCatName(cat) }}
            </button>
          </div>
        </div>

        <!-- SKELETON LOADING -->
        <div
          v-if="loading"
          class="grid grid-cols-2 gap-3 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 md:gap-4"
        >
          <div
            v-for="n in skeletonCount"
            :key="n"
            class="animate-pulse rounded-xl border border-slate-200 bg-white p-3"
          >
            <div class="aspect-[3/4] w-full rounded-lg bg-slate-200"></div>
            <div class="mt-3 h-4 w-3/4 rounded bg-slate-200"></div>
            <div class="mt-2 h-4 w-1/2 rounded bg-slate-200"></div>
          </div>
        </div>

        <!-- GRID SẢN PHẨM -->
        <div
          v-else-if="displayProducts.length"
          class="grid grid-cols-2 gap-3 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 md:gap-4"
        >
          <article
            v-for="product in displayProducts"
            :key="getProductId(product)"
            class="relative flex flex-col justify-between overflow-hidden rounded-2xl border border-slate-200/80 bg-white p-3 transition-all duration-300 hover:-translate-y-1 hover:border-rose-300 hover:shadow-xl hover:shadow-rose-500/5"
            @mouseenter="hoveredProductId = getProductId(product)"
            @mouseleave="hoveredProductId = null"
          >
            <div>
              <!-- KHU VỰC ẢNH SẢN PHẨM -->
              <div
                class="relative aspect-[3/4] w-full overflow-hidden rounded-xl bg-slate-100 cursor-pointer"
                @click="$emit('detail', getProductId(product))"
              >
                <img
                  :src="getImgUrl(product)"
                  :alt="getProductName(product)"
                  class="h-full w-full object-cover transition-transform duration-500"
                  :class="{ 'scale-105': hoveredProductId === getProductId(product) }"
                  @error="onImgError"
                />

                <!-- BADGE GIẢM GIÁ -->
                <div
                  v-if="getDiscountPercent(product)"
                  class="absolute left-2 top-2 z-10 flex items-center gap-0.5 rounded-full bg-gradient-to-r from-rose-600 to-amber-500 px-2 py-0.5 text-[10px] font-black text-white shadow-md"
                >
                  <Flame :size="11" class="animate-pulse fill-amber-200 text-amber-200" />
                  <span>-{{ getDiscountPercent(product) }}%</span>
                </div>

                <!-- NÚT XEM NHANH & YÊU THÍCH -->
                <div
                  class="absolute right-2 top-2 z-30 flex flex-col gap-2 transition-all duration-300"
                  :class="
                    hoveredProductId === getProductId(product)
                      ? 'opacity-100 translate-x-0 pointer-events-auto'
                      : 'opacity-0 translate-x-2 pointer-events-none'
                  "
                >
                  <!-- Nút Xem nhanh -->
                  <button
                    type="button"
                    class="flex h-8 w-8 items-center justify-center rounded-full bg-white text-slate-700 shadow-md transition-all hover:bg-rose-600 hover:text-white active:scale-95"
                    title="Xem nhanh"
                    @click.stop="handleQuickView(product)"
                  >
                    <Eye :size="16" />
                  </button>

                  <!-- Nút Yêu thích -->
                  <button
                    type="button"
                    class="flex h-8 w-8 items-center justify-center rounded-full bg-white text-slate-700 shadow-md transition-all hover:bg-rose-600 hover:text-white active:scale-95"
                    title="Yêu thích"
                    @click.stop="toggleFavorite(product)"
                  >
                    <Heart
                      :size="16"
                      :class="isFavorited(product) ? 'fill-rose-600 text-rose-600' : ''"
                    />
                  </button>
                </div>

                <!-- NÚT THÊM GIỎ HÀNG -->
                <div
                  class="absolute inset-x-2 bottom-2 z-30 transition-all duration-300"
                  :class="
                    hoveredProductId === getProductId(product)
                      ? 'opacity-100 translate-y-0 pointer-events-auto'
                      : 'opacity-0 translate-y-2 pointer-events-none'
                  "
                >
                  <button
                    type="button"
                    class="flex w-full items-center justify-center gap-1.5 rounded-lg bg-slate-900 py-2 text-xs font-bold text-white shadow-lg transition hover:bg-rose-600 active:scale-95"
                    @click.stop="handleAddToCart(product)"
                  >
                    <ShoppingCart :size="14" />
                    <span>Thêm vào giỏ</span>
                  </button>
                </div>
              </div>

              <!-- THÔNG TIN SẢN PHẨM -->
              <div class="mt-3 cursor-pointer" @click="$emit('detail', getProductId(product))">
                <h3
                  class="line-clamp-2 min-h-[36px] text-xs font-bold leading-relaxed transition-colors"
                  :class="
                    hoveredProductId === getProductId(product) ? 'text-rose-600' : 'text-slate-800'
                  "
                  :title="getProductName(product)"
                >
                  {{ getProductName(product) }}
                </h3>

                <!-- KHỐI GIÁ -->
                <div class="mt-2 flex flex-wrap items-baseline gap-1.5">
                  <span class="text-sm font-black text-rose-600 md:text-base">
                    {{ displayFormattedPrice(product) }}
                  </span>

                  <span
                    v-if="getOldPrice(product)"
                    class="text-[11px] font-medium text-slate-400 line-through"
                  >
                    {{ displayFormattedOldPrice(product) }}
                  </span>
                </div>
              </div>
            </div>

            <!-- FOOTER: ĐÁNH GIÁ & LƯỢT BÁN -->
            <div
              v-if="getProductRating(product) || getSoldCount(product)"
              class="mt-3 flex items-center justify-between border-t border-slate-100 pt-2 text-[11px]"
            >
              <div v-if="getProductRating(product)" class="flex items-center gap-1">
                <Star :size="12" class="fill-amber-400 text-amber-400" />
                <span class="font-bold text-slate-700">{{ getProductRating(product) }}</span>
              </div>

              <span v-if="getSoldCount(product)" class="font-medium text-slate-400">
                Đã bán {{ getSoldCount(product) }}
              </span>
            </div>
          </article>
        </div>

        <!-- TRẠNG THÁI TRỐNG -->
        <div v-else class="py-12 text-center text-sm text-slate-400">
          {{ emptyText }}
        </div>

        <!-- NÚT XEM THÊM -->
        <div v-if="showLoadMore" class="mt-8 flex justify-center">
          <button
            type="button"
            class="rounded-xl bg-slate-900 px-8 py-3 text-xs font-bold text-white shadow-md transition-all hover:bg-rose-600 hover:shadow-lg hover:shadow-rose-600/20 md:text-sm"
            @click="$emit('load-more')"
          >
            {{ loadMoreText }}
          </button>
        </div>
      </div>
    </section>

    <!-- QUICK VIEW MODAL -->
    <QuickViewModal
      v-if="showQuickViewModal && quickViewProductId"
      :product-id="quickViewProductId"
      @close="showQuickViewModal = false"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Star, Heart, Flame, ShoppingCart, Eye } from 'lucide-vue-next'
import axios from 'axios'
import emitter from '@/utils/emitter'
import yeuThichService from '@/service/yeuThichService'
import QuickViewModal from '@/views/shop/views/componnents/QuickViewModal.vue'

const router = useRouter()
const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

const props = defineProps({
  title: { type: String, default: 'SẢN PHẨM NỔI BẬT' },
  products: { type: Array, default: () => [] },
  categories: { type: Array, default: () => [] },
  loading: { type: Boolean, default: false },
  limit: { type: Number, default: 0 },
  skeletonCount: { type: Number, default: 10 },
  showLoadMore: { type: Boolean, default: true },
  emptyText: { type: String, default: 'Chưa có sản phẩm nào trong danh mục này.' },
  loadMoreText: { type: String, default: 'Xem thêm sản phẩm' },

  imageUrl: { type: Function, default: null },
  handleImageError: { type: Function, default: null },
  priceLabel: { type: Function, default: null },
  formatPrice: { type: Function, default: null },
})

const emit = defineEmits(['detail', 'favorite', 'add-to-cart', 'load-more', 'change-category'])

const selectedCategory = ref('all')
const showQuickViewModal = ref(false)
const quickViewProductId = ref(null)
const hoveredProductId = ref(null)
const wishlistIds = ref([])

// Lấy ID khách hàng từ Session Storage
const getCustomerId = () => {
  const userStr = sessionStorage.getItem('user')
  if (!userStr) return null
  try {
    const userObj = JSON.parse(userStr)
    return (
      userObj?.idKhachHang || userObj?.khachHangId || userObj?.khachHang?.id || userObj?.id || null
    )
  } catch {
    return null
  }
}

// 1. TẢI DANH SÁCH YÊU THÍCH
const loadWishlist = async () => {
  const idKhachHang = getCustomerId()
  if (!idKhachHang) return

  try {
    const data = await yeuThichService.getDanhSach(idKhachHang)
    wishlistIds.value = (data || []).map(
      (item) => item.idSanPham || item.sanPham?.idSanPham || item.id,
    )
  } catch (err) {
    console.error('Lỗi tải danh sách yêu thích:', err)
  }
}

// KIỂM TRA SẢN PHẨM CÓ TRONG DANH SÁCH YÊU THÍCH KHÔNG
const isFavorited = (p) => {
  const prodId = getProductId(p)
  return (
    wishlistIds.value.includes(prodId) ||
    Boolean(p?.isFavorite || p?.isLiked || p?.yeuThich || p?.liked)
  )
}

// 2. TOGGLE YÊU THÍCH (THÊM / XÓA TIM)
const toggleFavorite = async (product) => {
  const idKhachHang = getCustomerId()
  if (!idKhachHang) {
    alert('Bạn cần đăng nhập để sử dụng tính năng yêu thích!')
    router.push('/login')
    return
  }

  const prodId = getProductId(product)
  try {
    await yeuThichService.toggleYeuThich(idKhachHang, prodId)

    if (wishlistIds.value.includes(prodId)) {
      wishlistIds.value = wishlistIds.value.filter((id) => id !== prodId)
    } else {
      wishlistIds.value.push(prodId)
    }
    emit('favorite', prodId)
  } catch (err) {
    console.error('Lỗi khi thao tác yêu thích:', err)
    alert('Không thể cập nhật trạng thái yêu thích, vui lòng thử lại!')
  }
}

// 3. THÊM VÀO GIỎ HÀNG (CALL API & EVENT EMITTER)
const handleAddToCart = async (product) => {
  const token = sessionStorage.getItem('token')
  if (!token) {
    alert('Bạn cần đăng nhập để thêm vào giỏ hàng!')
    router.push('/login')
    return
  }

  // Ưu tiên idSanPhamChiTiet / spctId, fallback về ID sản phẩm
  const spctId = product?.idSanPhamChiTiet ?? product?.spctId ?? getProductId(product)

  try {
    await axios.post(
      `${API_URL}/giohang/them`,
      {
        idSanPhamChiTiet: spctId,
        soLuong: 1,
      },
      {
        headers: { Authorization: `Bearer ${token}` },
      },
    )

    // Bắn event cập nhật badge giỏ hàng trên Header
    emitter.emit('cart-updated')
    emit('add-to-cart', product)
  } catch (err) {
    console.error('Lỗi thêm sản phẩm vào giỏ hàng:', err)
    alert(err?.response?.data || 'Lỗi thêm sản phẩm vào giỏ hàng!')
  }
}

// Mở Quick View Modal
const handleQuickView = (product) => {
  quickViewProductId.value = getProductId(product)
  showQuickViewModal.value = true
}

const getCatId = (cat) => cat?.id ?? cat?.maDanhMuc ?? cat?.value ?? cat
const getCatName = (cat) => cat?.name ?? cat?.tenDanhMuc ?? cat?.label ?? cat

const handleCategoryChange = (catId) => {
  selectedCategory.value = catId
  emit('change-category', catId)
}

// Helper bóc tách dữ liệu
const getProductId = (p) => p?.idSanPham ?? p?.id ?? p?._id ?? p?.maSanPham ?? ''
const getProductName = (p) => p?.tenSanPham ?? p?.name ?? p?.title ?? ''
const getProductRating = (p) => p?.rating ?? p?.danhGia ?? p?.soSao ?? null
const getSoldCount = (p) => p?.daBan ?? p?.luotMua ?? p?.sold ?? null

const getImgUrl = (p) => {
  if (props.imageUrl) return props.imageUrl(p)
  let img = ''
  if (typeof p?.hinhAnh === 'string') img = p.hinhAnh
  else if (Array.isArray(p?.hinhAnh) && p.hinhAnh.length) img = p.hinhAnh[0]
  else if (p?.hinhAnhDaiDien) img = p.hinhAnhDaiDien
  else if (p?.image) img = p.image
  else if (p?.imageUrl) img = p.imageUrl
  else if (Array.isArray(p?.images) && p.images.length) {
    img = typeof p.images[0] === 'string' ? p.images[0] : p.images[0]?.url
  }

  if (!img) return ''
  return img.startsWith('http') ? img : `${API_URL}${img.startsWith('/') ? '' : '/'}${img}`
}

const onImgError = (e) => {
  if (props.handleImageError) return props.handleImageError(e)
  e.target.src = 'https://via.placeholder.com/400x533?text=No+Image'
}

const getProductPrice = (p) => {
  return Number(
    p?.giaSauGiam ?? p?.giaBan ?? p?.giaKhuyenMai ?? p?.price ?? p?.gia ?? p?.salePrice ?? 0,
  )
}

const getOldPrice = (p) => {
  const oldP = Number(
    p?.giaBan ?? p?.giaGoc ?? p?.oldPrice ?? p?.originalPrice ?? p?.giaNiemYet ?? 0,
  )
  const curP = getProductPrice(p)
  return oldP > curP ? oldP : null
}

const getDiscountPercent = (p) => {
  if (p?.phanTramGiam || p?.phantramGiam || p?.discountPercent) {
    const rawPercent = Number(p.phanTramGiam || p.phantramGiam || p.discountPercent)
    if (rawPercent > 0) return rawPercent
  }
  const oldP = getOldPrice(p)
  const curP = getProductPrice(p)
  if (oldP && curP && oldP > curP) {
    return Math.round(((oldP - curP) / oldP) * 100)
  }
  return null
}

const displayFormattedPrice = (p) => {
  if (props.priceLabel) {
    const label = props.priceLabel(p)
    if (label) return label
  }
  const val = getProductPrice(p)
  if (props.formatPrice) return `${props.formatPrice(val)}đ`
  return new Intl.NumberFormat('vi-VN').format(val) + 'đ'
}

const displayFormattedOldPrice = (p) => {
  const val = getOldPrice(p)
  if (!val) return ''
  if (props.formatPrice) return `${props.formatPrice(val)}đ`
  return new Intl.NumberFormat('vi-VN').format(val) + 'đ'
}

const displayProducts = computed(() => {
  if (props.limit && props.limit > 0) {
    return props.products.slice(0, props.limit)
  }
  return props.products
})

onMounted(() => {
  loadWishlist()
})
</script>
