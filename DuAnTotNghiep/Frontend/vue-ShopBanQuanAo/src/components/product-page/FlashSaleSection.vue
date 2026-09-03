<template>
  <div class="w-full bg-[#f5f5f5] py-4 font-sans antialiased">
    <section class="mx-auto max-w-[1480px] px-4 md:px-8">
      <!-- Container Flash Sale đỏ đô -->
      <div class="relative w-full rounded-2xl bg-[#8B0000] p-3 shadow-2xl md:p-5">
        <!-- HEADER SECTION -->
        <div
          class="mb-4 flex flex-wrap items-center justify-between gap-3 border-b border-white/10 pb-3"
        >
          <div class="flex flex-wrap items-center gap-2 md:gap-4">
            <!-- Đếm thời gian 4 ô -->
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

            <!-- Tiêu đề -->
            <div class="flex items-center gap-1.5">
              <Zap :size="24" class="animate-pulse fill-yellow-400 text-yellow-400" />
              <h2
                class="text-lg font-black italic tracking-wide text-yellow-400 uppercase md:text-2xl"
              >
                FLASH SALE MỖI NGÀY
              </h2>
            </div>
          </div>

          <button
            type="button"
            class="flex items-center gap-1 rounded-full bg-black/40 px-3.5 py-1 text-xs font-semibold text-white transition hover:bg-black/60 md:px-4 md:py-1.5 md:text-sm"
            @click="$emit('view-all')"
          >
            <span>Xem tất cả</span>
            <ChevronRight :size="15" />
          </button>
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

        <!-- KHỐI DỮ LIỆU ĐANG TẢI (SKELETON) -->
        <div v-if="loading" class="flex gap-3 overflow-hidden py-1">
          <div
            v-for="i in 5"
            :key="i"
            class="h-72 w-[200px] shrink-0 animate-pulse rounded-xl bg-white/20 p-3 sm:w-[225px]"
          ></div>
        </div>

        <!-- THÔNG BÁO KHI KHÔNG CÓ SP GIẢM GIÁ -->
        <div
          v-else-if="!processedProducts || processedProducts.length === 0"
          class="flex h-36 items-center justify-center text-sm font-medium text-white/80"
        >
          Chưa có sản phẩm nào đang giảm giá trong đợt Flash Sale này.
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

              <!-- Cụm hiển thị Giá tiền chuẩn (Hiển thị Giá Min & % Giảm đồng bộ) -->
              <div class="mt-2 flex min-h-[44px] flex-col justify-end">
                <div
                  v-if="item.originalPrice"
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
                <div class="flex items-center gap-1 text-[11px] font-medium text-amber-600">
                  <Flame :size="13" class="fill-amber-500 text-amber-500" />
                  <span>{{ item.statusText }}</span>
                </div>
                <div class="mt-1 h-2 w-full overflow-hidden rounded-full bg-red-100">
                  <div
                    class="h-full rounded-full bg-gradient-to-r from-amber-400 to-red-600 transition-all duration-500"
                    :style="{ width: item.soldPercent + '%' }"
                  ></div>
                </div>
              </div>
            </div>

            <!-- Footer Đánh giá & Yêu thích -->
            <div
              class="mt-3 flex items-center justify-between border-t border-zinc-100 pt-2 text-[11px] text-zinc-500"
            >
              <div class="flex items-center gap-1">
                <span class="font-bold text-amber-500">{{ item.danhGia.toFixed(1) }}</span>
                <Star :size="12" class="fill-amber-400 text-amber-400" />
                <span class="text-zinc-400">({{ item.luotDanhGia }})</span>
              </div>

              <button
                type="button"
                class="flex items-center gap-1 text-zinc-600 transition hover:text-red-600"
                @click.stop="$emit('favorite', item.id)"
              >
                <Heart :size="14" />
                <span>Yêu thích</span>
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
import { Zap, ChevronLeft, ChevronRight, Flame, Star, Heart } from 'lucide-vue-next'

const props = defineProps({
  products: { type: Array, default: () => [] },
  loading: { type: Boolean, default: false },
  endTime: { type: [String, Number, Date], default: null },
  baseUrl: { type: String, default: 'http://localhost:8080' },
})

defineEmits(['detail', 'favorite', 'view-all'])

// Ép kiểu số an toàn
const getNum = (val) => {
  if (val === null || val === undefined) return 0
  if (typeof val === 'number') return val
  const parsed = parseFloat(String(val).replace(/[^0-9.-]+/g, ''))
  return isNaN(parsed) ? 0 : parsed
}

// Xử lý lấy ảnh từ SPCT
const getProductImage = (item) => {
  let imgPath = item?.image || item?.hinhAnh || item?.thumbnail || item?.urlAnh || ''

  if (!imgPath && Array.isArray(item?.images) && item.images.length > 0) {
    imgPath = item.images[0]
  } else if (!imgPath && Array.isArray(item?.lstHinhAnh) && item.lstHinhAnh.length > 0) {
    imgPath = item.lstHinhAnh[0]?.duongDan || item.lstHinhAnh[0]?.url || item.lstHinhAnh[0]
  }

  if (!imgPath) return ''
  if (
    imgPath.startsWith('http://') ||
    imgPath.startsWith('https://') ||
    imgPath.startsWith('data:')
  ) {
    return imgPath
  }

  const cleanBase = props.baseUrl ? props.baseUrl.replace(/\/$/, '') : ''
  const cleanPath = imgPath.startsWith('/') ? imgPath : `/${imgPath}`
  return cleanBase ? `${cleanBase}${cleanPath}` : cleanPath
}

// Khi đường dẫn ảnh bị lỗi (404), hiển thị ảnh mặc định thay vì làm ô trống
const onImgError = (e) => {
  e.target.src = 'https://via.placeholder.com/300x300?text=No+Image'
}

const formatCurrency = (val) => {
  const num = getNum(val)
  if (num <= 0) return 'Liên hệ'
  return new Intl.NumberFormat('vi-VN').format(num) + 'đ'
}
// BÓC TÁCH DỮ LIỆU CHUẨN THEO API THỰC TẾ
const processedProducts = computed(() => {
  let rawList = []
  if (Array.isArray(props.products)) {
    rawList = props.products
  } else if (props.products?.content) {
    rawList = props.products.content
  } else if (props.products?.data) {
    rawList = props.products.data
  }

  if (!rawList.length) return []

  return rawList
    .filter((item) => {
      // Lọc các sản phẩm đang giảm giá
      const pct = getNum(item.displayDiscountPercent || item.maxDiscountPercent)
      return (
        item.dangGiamGia === true ||
        String(item.dangGiamGia) === 'true' ||
        item.dangGiamGia === 1 ||
        pct > 0
      )
    })
    .map((item) => {
      // Lấy giá bán thực tế và giá gốc từ API
      const minPrice = getNum(item.minPrice || item.displayPrice)
      const originalPrice = getNum(item.minOriginalPrice || item.originalPrice)
      const discountPercent = getNum(item.displayDiscountPercent || item.maxDiscountPercent)

      const totalSold = getNum(item.daBan || item.soLuongDaBan || 0)
      const totalStock = getNum(item.soLuong || 0)

      let statusText = 'Đang giảm giá'
      let soldPercent = 25

      if (totalSold > 0) {
        statusText = `Đã bán: ${totalSold}`
        const total = totalSold + totalStock
        soldPercent = total > 0 ? Math.min(Math.round((totalSold / total) * 100), 100) : 50
      } else if (totalStock > 0 && totalStock <= 10) {
        statusText = 'Sắp cháy hàng'
        soldPercent = 85
      }

      return {
        id: item.id,
        tenSanPham: item.tenSanPham || 'Sản phẩm',
        image: getProductImage(item),
        minPrice: minPrice,
        originalPrice: originalPrice > minPrice ? originalPrice : 0,
        discountPercent: discountPercent,
        statusText: statusText,
        soldPercent: soldPercent,
        danhGia: getNum(item.danhGia || 5),
        luotDanhGia: getNum(item.luotDanhGia || 0),
      }
    })
})

// Đếm ngược Flash Sale
const timer = ref({ days: '00', hours: '08', minutes: '22', seconds: '25' })
let timerInterval = null

const updateCountdown = () => {
  if (!props.endTime) return
  const diff = new Date(props.endTime).getTime() - new Date().getTime()
  if (diff <= 0 || isNaN(diff)) {
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

// Cuộn danh sách
const scrollContainer = ref(null)
const isAtStart = ref(true)
const isAtEnd = ref(false)
const hasOverflow = ref(false)

const scroll = (direction) => {
  if (!scrollContainer.value) return
  scrollContainer.value.scrollBy({
    left: direction === 'left' ? -460 : 460,
    behavior: 'smooth',
  })
}

const updateScrollStatus = () => {
  if (!scrollContainer.value) return
  const { scrollLeft, scrollWidth, clientWidth } = scrollContainer.value
  hasOverflow.value = scrollWidth > clientWidth + 5
  isAtStart.value = scrollLeft <= 5
  isAtEnd.value = scrollLeft + clientWidth >= scrollWidth - 5
}

watch(
  () => props.products,
  () => nextTick(updateScrollStatus),
  { deep: true },
)

onMounted(() => {
  if (props.endTime) {
    updateCountdown()
    timerInterval = setInterval(updateCountdown, 1000)
  }
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
