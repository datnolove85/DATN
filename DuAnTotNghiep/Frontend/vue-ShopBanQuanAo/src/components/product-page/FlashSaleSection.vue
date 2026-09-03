<template>
  <div
    v-if="loading || (processedProducts && processedProducts.length > 0)"
    class="w-full bg-[#f5f5f5] py-4 font-sans antialiased"
  >
    <section class="mx-auto max-w-[1480px] px-4 md:px-8">
      <div class="relative w-full rounded-2xl bg-[#8B0000] p-3 shadow-2xl md:p-5">
        <!-- HEADER & BỘ ĐẾM NGƯỢC -->
        <div
          class="mb-4 flex flex-wrap items-center justify-between gap-3 border-b border-white/10 pb-3"
        >
          <div class="flex flex-wrap items-center gap-2 md:gap-4">
            <div class="flex items-center gap-1 font-mono text-xs font-black text-white md:text-sm">
              <span
                class="flex h-7 min-w-[28px] items-center justify-center rounded-md bg-white px-1.5 text-[#8B0000] shadow-sm md:h-8 md:min-w-[32px]"
              >
                {{ timer.days }}
              </span>
              <span class="font-bold text-white">:</span>
              <span
                class="flex h-7 min-w-[28px] items-center justify-center rounded-md bg-white px-1.5 text-[#8B0000] shadow-sm md:h-8 md:min-w-[32px]"
              >
                {{ timer.hours }}
              </span>
              <span class="font-bold text-white">:</span>
              <span
                class="flex h-7 min-w-[28px] items-center justify-center rounded-md bg-white px-1.5 text-[#8B0000] shadow-sm md:h-8 md:min-w-[32px]"
              >
                {{ timer.minutes }}
              </span>
              <span class="font-bold text-white">:</span>
              <span
                class="flex h-7 min-w-[28px] items-center justify-center rounded-md bg-white px-1.5 text-[#8B0000] shadow-sm md:h-8 md:min-w-[32px]"
              >
                {{ timer.seconds }}
              </span>
            </div>

            <div class="flex items-center gap-1.5">
              <Zap :size="24" class="animate-pulse fill-yellow-400 text-yellow-400" />
              <h2
                class="text-lg font-black italic tracking-wide text-yellow-400 uppercase md:text-2xl"
              >
                FLASH SALE MỖI NGÀY
              </h2>
            </div>
          </div>
        </div>

        <!-- Nút Cuộn Trái -->
        <button
          v-if="hasOverflow && !loading"
          type="button"
          class="absolute left-1 top-1/2 z-20 flex h-9 w-9 -translate-y-1/2 items-center justify-center rounded-full bg-white text-zinc-800 shadow-xl transition-all hover:bg-red-600 hover:text-white disabled:opacity-20 md:left-2 md:h-10 md:w-10"
          :disabled="isAtStart"
          @click="scroll('left')"
        >
          <ChevronLeft :size="20" />
        </button>

        <!-- SKELETON LOADING -->
        <div v-if="loading" class="flex gap-3 overflow-hidden py-1">
          <div
            v-for="i in 5"
            :key="i"
            class="h-72 w-[200px] shrink-0 animate-pulse rounded-xl bg-white/20 p-3 sm:w-[225px]"
          ></div>
        </div>

        <!-- DANH SÁCH SẢN PHẨM -->
        <div
          v-else
          ref="scrollContainer"
          class="flex gap-2.5 overflow-x-auto scroll-smooth py-1 [scrollbar-width:none] [-ms-overflow-style:none] [&::-webkit-scrollbar]:hidden md:gap-3.5"
          @scroll="updateScrollStatus"
        >
          <article
            v-for="item in processedProducts"
            :key="item.id"
            class="group flex w-[200px] shrink-0 flex-col justify-between overflow-hidden rounded-xl bg-white p-3 shadow-md transition-all duration-300 hover:-translate-y-1 hover:shadow-xl sm:w-[225px]"
          >
            <!-- Bấm khu vực thông tin để xem chi tiết -->
            <div class="cursor-pointer" @click="$emit('detail', item.id)">
              <!-- Ảnh sản phẩm -->
              <div class="relative aspect-square w-full overflow-hidden rounded-lg bg-zinc-50 p-1">
                <img
                  v-if="item.image"
                  :src="item.image"
                  :alt="item.tenSanPham"
                  class="h-full w-full object-contain transition duration-300 group-hover:scale-105"
                  @error="onImgError"
                />
                <div
                  v-else
                  class="flex h-full w-full items-center justify-center text-xs text-zinc-400"
                >
                  Chưa có ảnh
                </div>
              </div>

              <!-- Tên sản phẩm -->
              <h3
                class="mt-2 line-clamp-2 min-h-[38px] text-xs font-semibold leading-snug text-zinc-800 md:text-sm"
              >
                {{ item.tenSanPham }}
              </h3>

              <!-- Cụm Giá tiền -->
              <div class="mt-2 flex min-h-[44px] flex-col justify-end">
                <div
                  v-if="item.originalPrice > item.minPrice"
                  class="mb-0.5 text-[11px] font-normal text-zinc-400 line-through leading-none"
                >
                  {{ formatCurrency(item.originalPrice) }}
                </div>

                <div class="flex flex-wrap items-center gap-1.5 leading-none">
                  <span class="text-sm font-bold text-red-600 md:text-base">
                    {{ formatCurrency(item.minPrice) }}
                  </span>
                  <span
                    v-if="item.discountPercent > 0"
                    class="rounded bg-red-600 px-1.5 py-0.5 text-[10px] font-bold text-white"
                  >
                    -{{ item.discountPercent }}%
                  </span>
                </div>
              </div>

              <!-- Thanh tiến trình bán -->
              <div class="mt-3">
                <div class="flex items-center gap-1 text-[11px] font-semibold text-amber-600">
                  <Flame :size="13" class="fill-amber-500 text-amber-500" />
                  <span>{{ item.statusText }}</span>
                </div>
                <div class="mt-1.5 h-2.5 w-full overflow-hidden rounded-full bg-red-100">
                  <div
                    class="h-full rounded-full bg-gradient-to-r from-amber-400 to-red-600 transition-all duration-500"
                    :style="{ width: item.soldPercent + '%' }"
                  ></div>
                </div>
              </div>
            </div>

            <!-- FOOTER: NÚT TIM YÊU THÍCH & XEM NHANH -->
            <div class="mt-3 flex items-center gap-2 border-t border-zinc-100 pt-2.5">
              <button
                type="button"
                class="flex flex-1 items-center justify-center gap-1 rounded-lg border border-zinc-200 bg-zinc-50 py-1.5 text-[11px] font-semibold text-zinc-700 transition hover:border-rose-300 hover:bg-rose-50 hover:text-rose-600 active:scale-95"
                @click.stop="$emit('favorite', item.id)"
              >
                <Heart
                  :size="14"
                  :class="isFavorite(item.id) ? 'fill-rose-600 text-rose-600' : 'text-zinc-500'"
                />
                <span>{{ isFavorite(item.id) ? 'Đã thích' : 'Yêu thích' }}</span>
              </button>

              <button
                type="button"
                class="flex flex-1 items-center justify-center gap-1 rounded-lg bg-[#df3440] py-1.5 text-[11px] font-semibold text-white shadow-sm transition hover:bg-[#ce2d38] active:scale-95"
                @click.stop="$emit('quick-view', item.id)"
              >
                <Eye :size="14" />
                <span>Xem nhanh</span>
              </button>
            </div>
          </article>
        </div>

        <!-- Nút Cuộn Phải -->
        <button
          v-if="hasOverflow && !loading"
          type="button"
          class="absolute right-1 top-1/2 z-20 flex h-9 w-9 -translate-y-1/2 items-center justify-center rounded-full bg-white text-zinc-800 shadow-xl transition-all hover:bg-red-600 hover:text-white disabled:opacity-20 md:right-2 md:h-10 md:w-10"
          :disabled="isAtEnd"
          @click="scroll('right')"
        >
          <ChevronRight :size="20" />
        </button>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick, watch } from 'vue'
import { Zap, ChevronLeft, ChevronRight, Flame, Heart, Eye } from 'lucide-vue-next'

const props = defineProps({
  products: { type: Array, default: () => [] },
  loading: { type: Boolean, default: false },
  endTime: { type: [String, Number, Date, Array], default: null },
  baseUrl: { type: String, default: 'http://localhost:8080' },
  isFavorite: { type: Function, default: () => false },
})

defineEmits(['detail', 'favorite', 'quick-view', 'view-all'])

const getNum = (val) => {
  if (val === null || val === undefined) return 0
  if (typeof val === 'number') return val
  const parsed = parseFloat(String(val).replace(/[^0-9.-]+/g, ''))
  return isNaN(parsed) ? 0 : parsed
}

const parseDate = (dateVal) => {
  if (!dateVal) return null
  if (typeof dateVal === 'number') return dateVal < 10000000000 ? dateVal * 1000 : dateVal
  if (dateVal instanceof Date) return dateVal.getTime()
  if (Array.isArray(dateVal)) {
    const [y, m, d, h = 0, min = 0, s = 0] = dateVal
    return new Date(y, m - 1, d, h, min, s).getTime()
  }
  const timeMs = new Date(dateVal).getTime()
  return isNaN(timeMs) ? null : timeMs
}

const targetEndTime = computed(() => {
  if (props.endTime) {
    const propTime = parseDate(props.endTime)
    if (propTime && propTime > Date.now()) return propTime
  }
  let rawList = Array.isArray(props.products)
    ? props.products
    : props.products?.content || props.products?.data || []
  if (!rawList.length) return null
  let maxTime = 0
  rawList.forEach((item) => {
    const endStr =
      item?.ngayKetThuc ||
      item?.variants?.[0]?.ngayKetThuc ||
      item?.thoiGianKetThuc ||
      item?.endTime
    const timeMs = parseDate(endStr)
    if (timeMs && timeMs > maxTime) maxTime = timeMs
  })
  return maxTime > Date.now() ? maxTime : null
})

const timer = ref({ days: '00', hours: '00', minutes: '00', seconds: '00' })
let timerInterval = null

const updateCountdown = () => {
  if (!targetEndTime.value) {
    timer.value = { days: '00', hours: '00', minutes: '00', seconds: '00' }
    return
  }
  const diff = targetEndTime.value - Date.now()
  if (diff <= 0) {
    timer.value = { days: '00', hours: '00', minutes: '00', seconds: '00' }
    if (timerInterval) clearInterval(timerInterval)
    return
  }
  const d = Math.floor(diff / (1000 * 60 * 60 * 24))
  const h = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60))
  const m = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60))
  const s = Math.floor((diff % (1000 * 60)) / 1000)
  timer.value = {
    days: String(d).padStart(2, '0'),
    hours: String(h).padStart(2, '0'),
    minutes: String(m).padStart(2, '0'),
    seconds: String(s).padStart(2, '0'),
  }
}

const startTimer = () => {
  if (timerInterval) clearInterval(timerInterval)
  updateCountdown()
  if (targetEndTime.value) timerInterval = setInterval(updateCountdown, 1000)
}

watch(
  targetEndTime,
  (newVal) => {
    if (newVal) startTimer()
    else {
      if (timerInterval) clearInterval(timerInterval)
      timer.value = { days: '00', hours: '00', minutes: '00', seconds: '00' }
    }
  },
  { immediate: true },
)

const extractSoldAndStock = (item) => {
  let sold = 0,
    stock = 0
  if (Array.isArray(item.variants) && item.variants.length > 0) {
    item.variants.forEach((v) => {
      sold += getNum(v.soLuongDaBan ?? v.daBan ?? 0)
      stock += getNum(v.soLuongKhaDung ?? v.soLuongTon ?? v.soLuong ?? 0)
    })
  } else {
    sold = getNum(item.soLuongDaBan ?? item.daBan ?? 0)
    stock = getNum(item.soLuongKhaDung ?? item.soLuongTon ?? item.soLuong ?? 0)
  }
  return { sold, stock }
}

const getProductImage = (item) => {
  let imgPath = item?.image || item?.hinhAnh || item?.thumbnail || ''
  if (!imgPath && Array.isArray(item?.images) && item.images.length > 0) imgPath = item.images[0]
  if (!imgPath) return ''
  if (
    imgPath.startsWith('http://') ||
    imgPath.startsWith('https://') ||
    imgPath.startsWith('data:')
  )
    return imgPath
  const cleanBase = props.baseUrl ? props.baseUrl.replace(/\/$/, '') : ''
  const cleanPath = imgPath.startsWith('/') ? imgPath : `/${imgPath}`
  return cleanBase ? `${cleanBase}${cleanPath}` : cleanPath
}

const onImgError = (e) => {
  e.target.src = 'https://via.placeholder.com/300x300?text=No+Image'
}

const formatCurrency = (val) => {
  const num = getNum(val)
  return num <= 0 ? '0đ' : new Intl.NumberFormat('vi-VN').format(num) + 'đ'
}

const processedProducts = computed(() => {
  let rawList = Array.isArray(props.products)
    ? props.products
    : props.products?.content || props.products?.data || []
  if (!rawList.length) return []
  const groupMap = new Map()

  rawList.forEach((spct) => {
    const pct = getNum(spct.phanTramGiam ?? spct.displayDiscountPercent ?? spct.maxDiscountPercent)
    const isSale =
      spct.dangGiamGia === true ||
      String(spct.dangGiamGia) === 'true' ||
      spct.dangGiamGia === 1 ||
      pct > 0
    if (!isSale) return

    const productId = spct.idSanPham || spct.id
    if (!productId) return

    const salePrice = getNum(spct.giaSauGiam ?? spct.minPrice ?? spct.displayPrice ?? spct.giaBan)
    const origPrice = getNum(spct.giaBan ?? spct.minOriginalPrice ?? spct.originalPrice)
    const currentImg = getProductImage(spct)
    const { sold, stock } = extractSoldAndStock(spct)

    if (!groupMap.has(productId)) {
      groupMap.set(productId, {
        id: productId,
        tenSanPham: spct.tenSanPham || 'Sản phẩm',
        image: currentImg,
        minPrice: salePrice,
        originalPrice: origPrice > salePrice ? origPrice : 0,
        discountPercent: pct,
        totalSold: sold,
        totalStock: stock,
        ngayKetThuc: spct.ngayKetThuc || spct.variants?.[0]?.ngayKetThuc,
        rawItem: spct,
      })
    } else {
      const group = groupMap.get(productId)
      if (!group.image && currentImg) group.image = currentImg
      if (salePrice > 0 && salePrice < group.minPrice) {
        group.minPrice = salePrice
        group.originalPrice = origPrice > salePrice ? origPrice : 0
        group.discountPercent = pct
      }
      group.totalSold += sold
      group.totalStock += stock
    }
  })

  return Array.from(groupMap.values()).map((group) => {
    const total = group.totalSold + group.totalStock
    let soldPercent = total > 0 ? Math.min(Math.round((group.totalSold / total) * 100), 100) : 0
    let statusText = `Đã bán ${group.totalSold}`
    if (group.totalSold === 0) {
      if (group.totalStock > 0 && group.totalStock <= 10) {
        statusText = 'Sắp cháy hàng'
        soldPercent = 85
      } else {
        statusText = 'Vừa mở bán'
        soldPercent = 10
      }
    } else {
      soldPercent = Math.max(soldPercent, 15)
    }

    return {
      id: group.id,
      tenSanPham: group.tenSanPham,
      image: group.image,
      minPrice: group.minPrice,
      originalPrice: group.originalPrice,
      discountPercent: group.discountPercent,
      statusText: statusText,
      soldPercent: soldPercent,
      ngayKetThuc: group.ngayKetThuc,
      rawItem: group.rawItem,
    }
  })
})

const scrollContainer = ref(null)
const isAtStart = ref(true)
const isAtEnd = ref(false)
const hasOverflow = ref(false)

const scroll = (direction) => {
  if (!scrollContainer.value) return
  scrollContainer.value.scrollBy({ left: direction === 'left' ? -460 : 460, behavior: 'smooth' })
}

const updateScrollStatus = () => {
  if (!scrollContainer.value) return
  const { scrollLeft, scrollWidth, clientWidth } = scrollContainer.value
  hasOverflow.value = scrollWidth > clientWidth + 5
  isAtStart.value = scrollLeft <= 5
  isAtEnd.value = scrollLeft + clientWidth >= scrollWidth - 5
}

watch(processedProducts, () => nextTick(updateScrollStatus), { immediate: true })

onMounted(() => {
  nextTick(() => {
    updateScrollStatus()
    window.addEventListener('resize', updateScrollStatus)
  })
})

onUnmounted(() => {
  if (timerInterval) clearInterval(timerInterval)
  window.removeEventListener('resize', updateScrollStatus)
})
</script>
