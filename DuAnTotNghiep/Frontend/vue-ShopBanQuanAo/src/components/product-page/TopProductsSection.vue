<template>
  <div class="w-full bg-[#f5f5f5] font-sans">
    <section ref="sectionRef" class="mx-auto max-w-[1480px] px-4 py-3 md:px-8">
      <div class="w-full rounded-2xl border border-zinc-200/80 bg-white p-3 shadow-xs md:p-4">
        <!-- HEADER SECTION & TABS LỌC -->
        <div class="mb-3.5 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
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
            v-if="availableTabs.length > 1"
            class="inline-flex flex-wrap items-center gap-1 rounded-xl bg-zinc-100 p-1 text-xs font-medium text-zinc-600"
          >
            <button
              v-for="tab in availableTabs"
              :key="tab.id"
              type="button"
              class="rounded-lg px-3 py-1.5 transition-all duration-200 cursor-pointer"
              :class="
                activeTab === tab.id
                  ? 'bg-white font-semibold text-red-600 shadow-xs'
                  : 'text-zinc-600 hover:text-zinc-900'
              "
              @click="handleTabSelect(tab.id)"
            >
              {{ tab.name }}
            </button>
          </div>
        </div>

        <!-- GRID LAYOUT -->
        <div class="grid grid-cols-2 gap-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 md:gap-2.5">
          <!-- BANNER THỜI TRANG -->
          <div
            class="relative col-span-2 flex min-h-[200px] select-none flex-col justify-between overflow-hidden rounded-xl bg-gradient-to-br from-zinc-900 via-zinc-850 to-rose-950 p-3.5 text-white shadow-sm transition-all duration-300 hover:shadow-md md:p-4"
          >
            <div
              class="pointer-events-none absolute -right-10 -top-10 h-44 w-44 rounded-full bg-rose-500/20 blur-3xl"
            ></div>
            <div
              class="pointer-events-none absolute -bottom-10 -left-10 h-40 w-40 rounded-full bg-amber-500/15 blur-2xl"
            ></div>

            <div class="relative z-10 flex items-center justify-between">
              <span
                class="inline-flex items-center gap-1 rounded-full border border-rose-400/30 bg-rose-500/20 px-2 py-0.5 text-[10px] font-semibold tracking-wide text-rose-200 backdrop-blur-md"
              >
                <span class="h-1.5 w-1.5 animate-pulse rounded-full bg-rose-400"></span>
                NEW COLLECTION 2026
              </span>
              <span class="text-[11px] font-medium tracking-wider text-zinc-300 uppercase">
                TRENDING NOW
              </span>
            </div>

            <div class="relative z-10 my-auto py-1">
              <p class="text-[11px] font-semibold uppercase tracking-wider text-rose-300">
                Gợi ý outfit tuần này
              </p>
              <h3
                class="mt-0.5 text-xl font-bold uppercase tracking-tight text-white md:text-2xl lg:text-3xl"
              >
                GIẢM TỚI <span class="text-amber-300">50%</span>
              </h3>
              <p class="mt-0.5 text-xs font-normal text-zinc-200 line-clamp-1">
                Áp dụng cho toàn bộ Áo thun, Hoodie & Quần Jeans chọn lọc
              </p>
            </div>

            <div class="relative z-10 flex items-center justify-between pt-1">
              <div class="flex items-center gap-1 text-xs font-medium text-zinc-200">
                <span class="h-2 w-2 rounded-full bg-emerald-400"></span>
                <span>Freeship từ 299k</span>
              </div>

              <button
                type="button"
                class="group/btn flex items-center gap-1 rounded-lg bg-white px-3 py-1 text-xs font-semibold text-zinc-900 shadow-sm transition duration-200 hover:bg-red-600 hover:text-white active:scale-95 cursor-pointer"
              >
                <span>Khám phá ngay</span>
                <span class="transition-transform duration-200 group-hover/btn:translate-x-1"
                  >→</span
                >
              </button>
            </div>
          </div>

          <!-- CARD SẢN PHẨM (Tối ưu không gian & tự co giãn theo tiêu đề) -->
          <article
            v-for="(product, index) in displayProducts"
            :key="product.id || product.maSanPham || index"
            class="group relative flex flex-col justify-between overflow-hidden rounded-xl border border-zinc-200/80 bg-white p-2 md:p-2.5 shadow-2xs transition duration-300 hover:-translate-y-0.5 hover:border-red-500/80 hover:shadow-md"
          >
            <div class="cursor-pointer flex flex-col flex-1" @click="$emit('detail', product.id)">
              <!-- Khung ảnh sản phẩm -->
              <div class="relative aspect-square w-full overflow-hidden rounded-lg bg-zinc-50 p-1">
                <img
                  :src="imageUrl(product)"
                  :alt="product.tenSanPham"
                  class="h-full w-full object-contain transition duration-500 group-hover:scale-105"
                  @error="handleImageError"
                />

                <!-- BADGE TOP BÁN CHẠY -->
                <div
                  v-if="index < 3"
                  class="absolute top-1 left-1 flex items-center gap-0.5 rounded bg-zinc-900/85 px-1.5 py-0.5 text-[9px] font-bold text-amber-300 backdrop-blur-xs shadow-xs"
                >
                  <Flame :size="10" class="text-amber-400 fill-amber-400 animate-pulse" />
                  <span>TOP {{ index + 1 }}</span>
                </div>
              </div>

              <!-- Tên sản phẩm (Bỏ h-9 cố định: Tự giãn theo chiều dài tên, tối đa 2 dòng) -->
              <h3
                class="mt-1.5 line-clamp-2 text-xs font-medium leading-snug text-zinc-800 transition group-hover:text-red-600 md:text-sm"
                :title="product.tenSanPham"
              >
                {{ product.tenSanPham }}
              </h3>

              <!-- Cụm Giá + Badge % Giảm giá chuyển xuống đây -->
              <div class="mt-1 flex flex-wrap items-center gap-1.5">
                <span class="text-xs font-bold text-red-600 md:text-sm">
                  {{ priceLabel(product) }}
                </span>

                <span
                  v-if="getOriginalPrice(product)"
                  class="text-[10px] font-normal text-zinc-400 line-through md:text-[11px]"
                >
                  {{ getOriginalPrice(product) }}
                </span>

                <!-- BADGE PHẦN TRĂM GIẢM GIÁ HIỆN TRONG CỤM GIÁ -->
                <span
                  v-if="getDiscountPercent(product) > 0"
                  class="rounded bg-red-50 border border-red-200/60 px-1 py-0.2 text-[9px] font-extrabold text-red-600 md:text-[10px]"
                >
                  -{{ getDiscountPercent(product) }}%
                </span>
              </div>

              <!-- Số lượng đã bán -->
              <div class="mt-1 flex items-center justify-end text-[10px] text-zinc-500">
                <span class="rounded-full bg-zinc-100 px-1.5 py-0.5 font-medium text-zinc-600">
                  Đã bán {{ getSoldQuantity(product) }}
                </span>
              </div>
            </div>

            <!-- FOOTER: YÊU THÍCH & XEM NHANH -->
            <div class="mt-2 flex items-center gap-1.5 border-t border-zinc-100 pt-2">
              <button
                type="button"
                class="flex flex-1 items-center justify-center gap-1 rounded-lg border border-zinc-200 bg-zinc-50 py-1 text-[10px] font-semibold text-zinc-700 transition hover:border-rose-300 hover:bg-rose-50 hover:text-rose-600 active:scale-95 cursor-pointer md:text-[11px]"
                @click.stop="$emit('favorite', product.id)"
              >
                <Heart
                  :size="13"
                  :class="isFavorite(product.id) ? 'fill-rose-600 text-rose-600' : 'text-zinc-500'"
                />
                <span>{{ isFavorite(product.id) ? 'Đã thích' : 'Thích' }}</span>
              </button>

              <button
                type="button"
                class="flex flex-1 items-center justify-center gap-1 rounded-lg bg-[#df3440] py-1 text-[10px] font-semibold text-white shadow-sm transition hover:bg-[#ce2d38] active:scale-95 cursor-pointer md:text-[11px]"
                @click.stop="$emit('quick-view', product.id)"
              >
                <Eye :size="13" />
                <span>Xem nhanh</span>
              </button>
            </div>
          </article>
        </div>

        <!-- Trạng thái trống -->
        <div
          v-if="!displayProducts.length"
          class="py-8 text-center text-xs font-medium text-zinc-500 md:text-sm"
        >
          Chưa có sản phẩm thuộc danh mục này.
        </div>

        <!-- NÚT XEM THÊM & THU GỌN -->
        <div
          v-if="hasMoreProducts || isExpanded"
          class="mt-4 flex items-center justify-center gap-3"
        >
          <button
            v-if="hasMoreProducts"
            type="button"
            class="rounded-lg bg-red-600 px-6 py-1.5 text-xs font-semibold text-white shadow-xs transition hover:bg-red-700 active:scale-95 cursor-pointer md:text-sm"
            @click="handleLoadMore"
          >
            Xem thêm sản phẩm
          </button>

          <button
            v-if="isExpanded"
            type="button"
            class="flex items-center gap-1 rounded-lg border border-zinc-300 bg-white px-5 py-1.5 text-xs font-semibold text-zinc-700 shadow-xs transition hover:border-zinc-400 hover:bg-zinc-100 hover:text-zinc-900 active:scale-95 cursor-pointer md:text-sm"
            @click="handleCollapse"
          >
            <span>Thu gọn</span>
            <ChevronUp :size="16" />
          </button>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { Heart, Eye, Flame, ChevronUp } from 'lucide-vue-next'

const props = defineProps({
  products: { type: Array, default: () => [] },
  categories: { type: Array, default: () => [] },
  imageUrl: { type: Function, required: true },
  handleImageError: { type: Function, required: true },
  priceLabel: { type: Function, required: true },
  isFavorite: { type: Function, default: () => false },
})

const emit = defineEmits(['detail', 'favorite', 'quick-view', 'tab-change', 'load-more'])

const sectionRef = ref(null)
const activeTab = ref('all')
const displayLimit = ref(8)

const getNum = (val) => {
  if (val === null || val === undefined) return 0
  if (typeof val === 'number') return val
  const parsed = parseFloat(String(val).replace(/[^0-9.-]+/g, ''))
  return isNaN(parsed) ? 0 : parsed
}

// 1. Chuẩn hóa danh sách Tabs
const availableTabs = computed(() => {
  const tabs = [{ id: 'all', name: 'Tất cả' }]

  if (props.categories && props.categories.length > 0) {
    props.categories.forEach((cat, index) => {
      let id, name
      if (typeof cat === 'string' || typeof cat === 'number') {
        id = String(cat)
        name = String(cat)
      } else if (typeof cat === 'object' && cat !== null) {
        id =
          cat.id ??
          cat.value ??
          cat.danhMucId ??
          cat.maDanhMuc ??
          cat.name ??
          cat.label ??
          `cat-${index}`
        name = cat.name ?? cat.label ?? cat.tenDanhMuc ?? cat.title ?? String(id)
      }
      if (name) tabs.push({ id: String(id), name: String(name) })
    })
  } else if (props.products && props.products.length > 0) {
    const catMap = new Map()
    props.products.forEach((p) => {
      const catId = p.danhMucId ?? p.categoryId ?? p.category?.id ?? p.tenDanhMuc
      const catName = p.tenDanhMuc ?? p.categoryName ?? p.category?.name ?? p.category
      if (catId && catName) catMap.set(String(catId), String(catName))
    })
    catMap.forEach((name, id) => tabs.push({ id: String(id), name: String(name) }))
  }

  return tabs
})

// 2. Tính số lượng bán ra
const getSoldQuantity = (product) => {
  if (!product) return 0
  const variants =
    product.variants || product.sanPhamChiTiet || product.chiTietSanPhams || product.spct || []

  if (Array.isArray(variants) && variants.length > 0) {
    return variants.reduce((sum, v) => sum + getNum(v.soLuongDaBan ?? v.daBan ?? 0), 0)
  }
  return getNum(product.soLuongDaBan ?? product.daBan ?? product.totalSold ?? 0)
}

// 3. Logic bóc tách Giá & % giảm giá
const getItemPriceInfo = (item, parentProduct = {}) => {
  if (!item) return { giaBan: 0, giaGoc: 0, percent: 0 }

  let percent = getNum(
    item.phanTramGiam ??
      item.displayDiscountPercent ??
      item.maxDiscountPercent ??
      item.giamGia ??
      item.discount ??
      item.phanTramGiamGia ??
      item.dotGiamGia?.phanTramGiam ??
      item.khuyenMai?.phanTramGiam ??
      parentProduct.phanTramGiam ??
      parentProduct.displayDiscountPercent ??
      parentProduct.giamGia ??
      0,
  )

  let salePrice = getNum(
    item.giaSauGiam ??
      item.giaKhuyenMai ??
      item.minPrice ??
      item.displayPrice ??
      parentProduct.giaSauGiam ??
      parentProduct.giaKhuyenMai ??
      parentProduct.minPrice ??
      0,
  )

  let origPrice = getNum(
    item.giaGoc ??
      item.giaNiemYet ??
      item.originalPrice ??
      item.minOriginalPrice ??
      parentProduct.giaGoc ??
      parentProduct.giaNiemYet ??
      parentProduct.originalPrice ??
      0,
  )

  const rawGiaBan = getNum(
    item.giaBan ?? parentProduct.giaBan ?? item.donGia ?? parentProduct.donGia ?? 0,
  )

  if (salePrice > 0) {
    if (origPrice === 0 && rawGiaBan > salePrice) {
      origPrice = rawGiaBan
    }
  } else {
    salePrice = rawGiaBan
  }

  if (origPrice > salePrice && salePrice > 0) {
    if (percent === 0) {
      percent = Math.round(((origPrice - salePrice) / origPrice) * 100)
    }
  } else if (percent > 0 && percent < 100 && salePrice > 0) {
    origPrice = Math.round(salePrice / (1 - percent / 100))
  }

  return {
    giaBan: salePrice,
    giaGoc: origPrice > salePrice ? origPrice : 0,
    percent: percent,
  }
}

// 4. Tính % giảm giá cao nhất
const getDiscountPercent = (product) => {
  if (!product) return 0

  const variants =
    product.variants || product.sanPhamChiTiet || product.chiTietSanPhams || product.spct || []

  let maxPercent = 0

  if (Array.isArray(variants) && variants.length > 0) {
    variants.forEach((v) => {
      const info = getItemPriceInfo(v, product)
      if (info.percent > maxPercent) maxPercent = info.percent
    })
  }

  if (maxPercent === 0) {
    const mainInfo = getItemPriceInfo(product)
    maxPercent = mainInfo.percent
  }

  return maxPercent
}

// 5. Lấy giá gốc gạch ngang
const getOriginalPrice = (product) => {
  if (!product) return null

  const variants =
    product.variants || product.sanPhamChiTiet || product.chiTietSanPhams || product.spct || []

  let targetGiaGoc = 0
  let targetGiaBan = 0
  let maxPercent = 0

  if (Array.isArray(variants) && variants.length > 0) {
    variants.forEach((v) => {
      const info = getItemPriceInfo(v, product)
      if (info.percent > maxPercent) {
        maxPercent = info.percent
        targetGiaGoc = info.giaGoc
        targetGiaBan = info.giaBan
      }
    })
  }

  if (maxPercent === 0) {
    const mainInfo = getItemPriceInfo(product)
    if (mainInfo.percent > 0 || mainInfo.giaGoc > mainInfo.giaBan) {
      targetGiaGoc = mainInfo.giaGoc
      targetGiaBan = mainInfo.giaBan
    }
  }

  if (targetGiaGoc > targetGiaBan && targetGiaBan > 0) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(
      targetGiaGoc,
    )
  }

  return null
}

const handleTabSelect = (tabId) => {
  activeTab.value = tabId
  displayLimit.value = 8
  emit('tab-change', tabId)
}

const handleLoadMore = () => {
  displayLimit.value += 8
  emit('load-more', { tab: activeTab.value, limit: displayLimit.value })
}

const handleCollapse = () => {
  displayLimit.value = 8
  if (sectionRef.value) {
    sectionRef.value.scrollIntoView({ behavior: 'smooth', block: 'start' })
  }
}

// 6. Lọc sản phẩm
const isProductMatchingTab = (product, activeTabId) => {
  if (!activeTabId || activeTabId === 'all') return true

  const selectedTab = availableTabs.value.find((t) => String(t.id) === String(activeTabId))
  const tabIdStr = String(activeTabId).toLowerCase().trim()
  const tabNameStr = selectedTab ? selectedTab.name.toLowerCase().trim() : tabIdStr

  const catFields = [
    product.danhMucId,
    product.idDanhMuc,
    product.categoryId,
    product.category?.id,
    product.category,
    product.tenDanhMuc,
    product.categoryName,
    product.category?.tenDanhMuc,
    product.category?.name,
    product.maDanhMuc,
  ]
    .filter((v) => v !== null && v !== undefined)
    .map((v) => String(v).toLowerCase().trim())

  const hasDirectMatch = catFields.some(
    (val) =>
      val === tabIdStr ||
      val === tabNameStr ||
      val.includes(tabNameStr) ||
      tabNameStr.includes(val),
  )
  if (hasDirectMatch) return true

  if (product.tenSanPham && tabNameStr) {
    const productName = String(product.tenSanPham).toLowerCase()
    if (productName.includes(tabNameStr)) return true

    const keywords = tabNameStr.split(' ').filter((k) => k.length > 1)
    if (keywords.length > 0 && keywords.every((kw) => productName.includes(kw))) {
      return true
    }
  }

  return false
}

// 7. Sắp xếp theo lượt bán
const filteredProducts = computed(() => {
  if (!props.products || !props.products.length) return []

  const sortedBySold = [...props.products].sort((a, b) => getSoldQuantity(b) - getSoldQuantity(a))

  if (activeTab.value === 'all') {
    return sortedBySold
  }

  return sortedBySold.filter((product) => isProductMatchingTab(product, activeTab.value))
})

// 8. Hiển thị thực tế
const displayProducts = computed(() => {
  return filteredProducts.value.slice(0, displayLimit.value)
})

const hasMoreProducts = computed(() => {
  return filteredProducts.value.length > displayLimit.value
})

const isExpanded = computed(() => {
  return displayLimit.value > 8
})
</script>
