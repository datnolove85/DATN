<template>
  <div class="w-full bg-[#f5f5f5] font-sans antialiased">
    <section class="mx-auto max-w-[1480px] px-4 py-2 md:px-8">
      <div
        class="group/section relative w-full rounded-2xl border border-amber-200/60 bg-white p-3.5 shadow-sm transition-all duration-300 hover:shadow-md md:p-4.5"
      >
        <!-- Tiêu đề (giảm mb-6 pb-2 -> mb-3 pb-1) -->
        <div class="relative mb-3 pb-1">
          <h2 class="text-lg font-bold uppercase tracking-wide text-zinc-900 md:text-xl">
            {{ title }}
          </h2>
          <div
            class="absolute -bottom-0.5 left-0 h-1 w-16 rounded-full bg-gradient-to-r from-red-600 via-rose-500 to-transparent"
          ></div>
        </div>

        <button
          v-if="hasOverflow"
          type="button"
          aria-label="Cuộn sang trái"
          class="absolute left-2 top-1/2 z-20 flex h-8 w-8 -translate-y-1/2 items-center justify-center rounded-full border border-amber-200 bg-white/95 text-zinc-800 shadow-md backdrop-blur-sm transition-all hover:scale-105 hover:bg-red-600 hover:text-white disabled:pointer-events-none disabled:opacity-30 md:left-3 md:h-9 md:w-9"
          :disabled="isAtStart"
          @click="scroll('left')"
        >
          <component :is="LucideIcons.ChevronLeft" :size="18" stroke-width="2" />
        </button>

        <!-- Danh sách card (giảm gap, py-2 -> py-1) -->
        <div
          ref="scrollContainer"
          class="flex cursor-grab select-none gap-2.5 overflow-x-auto scroll-smooth py-1 active:cursor-grabbing [scrollbar-width:none] [-ms-overflow-style:none] [&::-webkit-scrollbar]:hidden md:gap-3"
          @scroll="updateScrollProgress"
          @mousedown="startDrag"
          @mouseleave="stopDrag"
          @mouseup="stopDrag"
          @mousemove="onDrag"
        >
          <!-- Card sản phẩm (giảm padding p-3 -> p-2, kích thước thẻ thu gọn bớt) -->
          <router-link
            v-for="item in categories"
            :key="item.id || item.slug || item.name"
            :to="getCategoryPath(item)"
            class="group flex aspect-square w-[115px] sm:w-[130px] flex-none cursor-pointer flex-col items-center justify-center rounded-xl bg-white p-2 text-center border border-amber-200/50 shadow-sm transition-all duration-300 hover:-translate-y-0.5 hover:border-red-500 hover:shadow-md active:scale-95"
            @click="handleCardClick($event)"
          >
            <!-- Khung Icon (giảm mb-3 -> mb-1.5, kích thước h-14 -> h-11) -->
            <div
              class="mb-1.5 flex h-11 w-11 items-center justify-center rounded-xl bg-[#f5f5f5] p-2 transition-transform duration-300 group-hover:scale-105 group-hover:bg-red-50"
            >
              <img
                :src="getCategoryIcon(item)"
                :alt="item.name"
                class="h-8 w-8 object-contain drop-shadow-sm"
              />
            </div>

            <span
              class="text-xs font-semibold text-zinc-800 leading-tight transition-colors group-hover:text-red-600 line-clamp-2"
            >
              {{ item.name }}
            </span>
          </router-link>
        </div>

        <button
          v-if="hasOverflow"
          type="button"
          aria-label="Cuộn sang phải"
          class="absolute right-2 top-1/2 z-20 flex h-8 w-8 -translate-y-1/2 items-center justify-center rounded-full border border-amber-200 bg-white/95 text-zinc-800 shadow-md backdrop-blur-sm transition-all hover:scale-105 hover:bg-red-600 hover:text-white disabled:pointer-events-none disabled:opacity-30 md:right-3 md:h-9 md:w-9"
          :disabled="isAtEnd"
          @click="scroll('right')"
        >
          <component :is="LucideIcons.ChevronRight" :size="18" stroke-width="2" />
        </button>

        <!-- Thanh progress (giảm mt-6 -> mt-3) -->
        <div v-if="hasOverflow" class="mt-3 flex justify-center">
          <div class="relative h-1 w-24 overflow-hidden rounded-full bg-amber-200/60">
            <div
              class="absolute top-0 h-1 w-7 rounded-full bg-red-600 transition-transform duration-150 ease-out"
              :style="{ transform: `translateX(${progressOffset}px)` }"
            ></div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick, watch } from 'vue'
import * as LucideIcons from 'lucide-vue-next'

const props = defineProps({
  title: {
    type: String,
    default: 'MUA THEO DANH MỤC',
  },
  categories: {
    type: Array,
    default: () => [
      { id: 1, name: 'Áo Thun & Polo' },
      { id: 2, name: 'Áo Sơ Mi' },
      { id: 3, name: 'Quần Jeans' },
      { id: 4, name: 'Quần Tây & Âu' },
      { id: 5, name: 'Áo Khoác & Hoodie' },
      { id: 6, name: 'Quần Shorts' },
      { id: 7, name: 'Váy & Đầm' },
      { id: 8, name: 'Áo Vest & Suit' },
      { id: 9, name: 'Giày Dép' },
      { id: 10, name: 'Túi & Balo' },
      { id: 11, name: 'Mũ & Nón' },
      { id: 12, name: 'Phụ Kiện' },
    ],
  },
})

// --- BỘ ICON VECTOR MULTI-COLOR ---
const SVG_ICONS = {
  TSHIRT: `data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64"><path fill="%232563EB" d="M18 10L6 22l8 8 6-4v28h24V26l6 4 8-8-12-12-10 4a12 12 0 0 1-16 0z"/><path fill="%231D4ED8" d="M18 10l-4 4 6 12 4-2V12z"/><path fill="%2360A5FA" d="M32 18a12 12 0 0 1-8-3l-2 2a12 12 0 0 0 20 0l-2-2a12 12 0 0 1-8 3z"/></svg>`,
  SHIRT: `data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64"><path fill="%230284C7" d="M16 12L6 22l8 6 6-4v30h24V24l6 4 8-6-10-10-10 6-6-6-6 6z"/><path fill="%2338BDF8" d="M22 8l10 10 10-10-6-2-4 4-4-4z"/><circle cx="32" cy="26" r="2.5" fill="%23FFFFFF"/><circle cx="32" cy="36" r="2.5" fill="%23FFFFFF"/><circle cx="32" cy="46" r="2.5" fill="%23FFFFFF"/></svg>`,
  JEANS: `data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64"><path fill="%231E3A8A" d="M14 10h36l-2 46h-13l-3-24-3 24H16z"/><path fill="%232563EB" d="M14 10h36v7H14z"/><path fill="%23F59E0B" d="M28 13h8v3h-8z"/><path fill="%2393C5FD" d="M16 52h13v4H16zm19 0h13v4H35z"/></svg>`,
  TROUSERS: `data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64"><path fill="%23D97706" d="M15 10h34l-2 46h-13l-2-22-2 22H17z"/><path fill="%23B45309" d="M15 10h34v6H15z"/><path fill="%2378350F" d="M28 12h8v2h-8z"/></svg>`,
  SHORTS: `data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64"><path fill="%23EF4444" d="M14 12h36l-3 28h-13l-2-12-2 12H17z"/><path fill="%23DC2626" d="M14 12h36v6H14z"/><path fill="%23FDE047" d="M28 14h8v3h-8z"/></svg>`,
  JACKET: `data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64"><path fill="%230D9488" d="M16 10L6 22l8 6 4-3v29h28V25l4 3 8-6-10-12-10 4a12 12 0 0 1-16 0z"/><path fill="%230f766e" d="M30 18h4v36h-4z"/><path fill="%23F59E0B" d="M30 22h4v4h-4z"/></svg>`,
  SUIT: `data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64"><path fill="%231E293B" d="M16 10L6 22l8 6 4-3v29h28V25l4 3 8-6-10-12z"/><path fill="%23FFFFFF" d="M26 10l6 14 6-14z"/><path fill="%23DC2626" d="M30 15l2 12 2-12-2-3z"/></svg>`,
  DRESS: `data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64"><path fill="%23EC4899" d="M24 8l-4 12h24L40 8l-8 4z"/><path fill="%23F43F5E" d="M20 20l-10 36h44L44 20z"/><circle cx="32" cy="20" r="3.5" fill="%23FDE047"/></svg>`,
  SKIRT: `data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64"><path fill="%238B5CF6" d="M20 16h24l6 36H14z"/><path fill="%237C3AED" d="M20 16h24v6H20z"/></svg>`,
  SHOES: `data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64"><path fill="%23F97316" d="M10 38l12-12 16 4 16 8v10H10z"/><path fill="%23F3F4F6" d="M8 48h48v6H8z"/><path fill="%23FFFFFF" d="M24 30l4-2m-2 6l4-2m-2 6l4-2"/></svg>`,
  BAG: `data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64"><path fill="%23D97706" d="M12 24h40l-4 30H16z"/><path fill="none" stroke="%23B45309" stroke-width="4" d="M22 24V14a10 10 0 0 1 20 0v10"/><circle cx="32" cy="34" r="4" fill="%23FDE047"/></svg>`,
  HAT: `data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64"><path fill="%2318181B" d="M16 32a16 16 0 0 1 32 0v6H16z"/><path fill="%23EAB308" d="M12 38h40l8 4H4z"/><circle cx="32" cy="16" r="3" fill="%23EAB308"/></svg>`,
  ACCESSORIES: `data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64"><path fill="%2318181B" d="M8 24h22v16H8zm26 0h22v16H34z"/><path fill="none" stroke="%23EAB308" stroke-width="4" d="M30 28h4M8 26L2 20m54 6l6-6"/></svg>`,
  UNDERWEAR: `data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64"><path fill="%2314B8A6" d="M12 20h40l-4 24a16 16 0 0 1-32 0z"/><path fill="%230D9488" d="M12 20h40v5H12z"/></svg>`,
  SOCKS: `data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64"><path fill="%23EC4899" d="M20 10h16v26l8 8v10H20z"/><path fill="%233B82F6" d="M20 18h16v6H20zm4 26h12v6H24z"/></svg>`,
  DEFAULT: `data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64"><path fill="none" stroke="%23DC2626" stroke-width="4" stroke-linecap="round" d="M32 20a6 6 0 1 0-6-6m-18 30l24-14 24 14H8z"/><circle cx="32" cy="40" r="3" fill="%23DC2626"/></svg>`,
}

const iconMap = {
  'ao-thun': SVG_ICONS.TSHIRT,
  'ao-phong': SVG_ICONS.TSHIRT,
  polo: SVG_ICONS.TSHIRT,
  'ao-polo': SVG_ICONS.TSHIRT,
  't-shirt': SVG_ICONS.TSHIRT,
  tshirt: SVG_ICONS.TSHIRT,
  'ao-thun-nam': SVG_ICONS.TSHIRT,
  'ao-thun-nu': SVG_ICONS.TSHIRT,
  'ao-thun-tay-ngan': SVG_ICONS.TSHIRT,
  'ao-thun-tay-dai': SVG_ICONS.TSHIRT,
  'ao-ba-lo': SVG_ICONS.TSHIRT,
  'ao-ba-ba': SVG_ICONS.TSHIRT,
  tanktop: SVG_ICONS.TSHIRT,
  'tank-top': SVG_ICONS.TSHIRT,
  'crop-top': SVG_ICONS.TSHIRT,
  croptop: SVG_ICONS.TSHIRT,
  'ao-so-mi': SVG_ICONS.SHIRT,
  'so-mi': SVG_ICONS.SHIRT,
  shirt: SVG_ICONS.SHIRT,
  'so-mi-nam': SVG_ICONS.SHIRT,
  'so-mi-nu': SVG_ICONS.SHIRT,
  'so-mi-tay-ngan': SVG_ICONS.SHIRT,
  'so-mi-tay-dai': SVG_ICONS.SHIRT,
  'so-mi-co-tau': SVG_ICONS.SHIRT,
  'so-mi-caro': SVG_ICONS.SHIRT,
  'quan-jeans': SVG_ICONS.JEANS,
  'quan-jean': SVG_ICONS.JEANS,
  'quan-bo': SVG_ICONS.JEANS,
  jeans: SVG_ICONS.JEANS,
  denim: SVG_ICONS.JEANS,
  'jeans-nam': SVG_ICONS.JEANS,
  'jeans-nu': SVG_ICONS.JEANS,
  'quan-jeans-ong-suong': SVG_ICONS.JEANS,
  baggy: SVG_ICONS.JEANS,
  'quan-tay': SVG_ICONS.TROUSERS,
  'quan-au': SVG_ICONS.TROUSERS,
  'quan-kaki': SVG_ICONS.TROUSERS,
  'quan-dai': SVG_ICONS.TROUSERS,
  'quan-jogger': SVG_ICONS.TROUSERS,
  jogger: SVG_ICONS.TROUSERS,
  pants: SVG_ICONS.TROUSERS,
  trousers: SVG_ICONS.TROUSERS,
  chinos: SVG_ICONS.TROUSERS,
  'quan-shorts': SVG_ICONS.SHORTS,
  'quan-short': SVG_ICONS.SHORTS,
  'quan-dui': SVG_ICONS.SHORTS,
  shorts: SVG_ICONS.SHORTS,
  'quan-sot': SVG_ICONS.SHORTS,
  'quan-ngan': SVG_ICONS.SHORTS,
  'ao-khoac': SVG_ICONS.JACKET,
  jacket: SVG_ICONS.JACKET,
  hoodie: SVG_ICONS.JACKET,
  'ao-hoodie': SVG_ICONS.JACKET,
  sweater: SVG_ICONS.JACKET,
  'ao-len': SVG_ICONS.JACKET,
  cardigan: SVG_ICONS.JACKET,
  'ao-gio': SVG_ICONS.JACKET,
  bomber: SVG_ICONS.JACKET,
  'ao-phao': SVG_ICONS.JACKET,
  vest: SVG_ICONS.SUIT,
  blazer: SVG_ICONS.SUIT,
  suit: SVG_ICONS.SUIT,
  'ao-vest': SVG_ICONS.SUIT,
  'do-vest': SVG_ICONS.SUIT,
  vay: SVG_ICONS.DRESS,
  dam: SVG_ICONS.DRESS,
  'vay-dam': SVG_ICONS.DRESS,
  dress: SVG_ICONS.DRESS,
  'chan-vay': SVG_ICONS.SKIRT,
  skirt: SVG_ICONS.SKIRT,
  yem: SVG_ICONS.DRESS,
  'ao-dai': SVG_ICONS.DRESS,
  'giay-dep': SVG_ICONS.SHOES,
  giay: SVG_ICONS.SHOES,
  shoes: SVG_ICONS.SHOES,
  sneaker: SVG_ICONS.SHOES,
  sneakers: SVG_ICONS.SHOES,
  sandal: SVG_ICONS.SHOES,
  dep: SVG_ICONS.SHOES,
  boots: SVG_ICONS.SHOES,
  'giay-cao-got': SVG_ICONS.SHOES,
  'giay-tay': SVG_ICONS.SHOES,
  'tui-xach': SVG_ICONS.BAG,
  tui: SVG_ICONS.BAG,
  balo: SVG_ICONS.BAG,
  backpack: SVG_ICONS.BAG,
  bag: SVG_ICONS.BAG,
  vi: SVG_ICONS.BAG,
  'tui-deo-cheo': SVG_ICONS.BAG,
  mu: SVG_ICONS.HAT,
  non: SVG_ICONS.HAT,
  hat: SVG_ICONS.HAT,
  cap: SVG_ICONS.HAT,
  'mu-luoi-trai': SVG_ICONS.HAT,
  'mu-bucket': SVG_ICONS.HAT,
  'phu-kien': SVG_ICONS.ACCESSORIES,
  accessories: SVG_ICONS.ACCESSORIES,
  'that-lung': SVG_ICONS.ACCESSORIES,
  belt: SVG_ICONS.ACCESSORIES,
  kinh: SVG_ICONS.ACCESSORIES,
  'mat-kinh': SVG_ICONS.ACCESSORIES,
  'dong-ho': SVG_ICONS.ACCESSORIES,
  'trang-suc': SVG_ICONS.ACCESSORIES,
  'do-lot': SVG_ICONS.UNDERWEAR,
  underwear: SVG_ICONS.UNDERWEAR,
  'do-ngu': SVG_ICONS.UNDERWEAR,
  tat: SVG_ICONS.SOCKS,
  vo: SVG_ICONS.SOCKS,
  socks: SVG_ICONS.SOCKS,
}

const slugify = (str) => {
  if (!str) return ''
  return str
    .toLowerCase()
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .replace(/[đĐ]/g, 'd')
    .replace(/[^a-z0-9 -]/g, '')
    .trim()
    .replace(/\s+/g, '-')
    .replace(/-+/g, '-')
}

const getCategoryIcon = (category) => {
  if (
    category.icon &&
    typeof category.icon === 'string' &&
    (category.icon.startsWith('http') || category.icon.startsWith('data:image'))
  ) {
    return category.icon
  }

  if (category.slug && iconMap[category.slug]) {
    return iconMap[category.slug]
  }

  const autoSlug = slugify(category.name || '')
  if (iconMap[autoSlug]) {
    return iconMap[autoSlug]
  }

  const nameClean = autoSlug.replace(/-/g, ' ')

  if (nameClean.includes('so mi') || nameClean.includes('shirt')) return SVG_ICONS.SHIRT
  if (
    nameClean.includes('thun') ||
    nameClean.includes('phong') ||
    nameClean.includes('polo') ||
    nameClean.includes('tshirt') ||
    nameClean.includes('ba lo') ||
    nameClean.includes('tank')
  )
    return SVG_ICONS.TSHIRT
  if (
    nameClean.includes('jean') ||
    nameClean.includes('bo') ||
    nameClean.includes('denim') ||
    nameClean.includes('baggy')
  )
    return SVG_ICONS.JEANS
  if (nameClean.includes('vest') || nameClean.includes('blazer') || nameClean.includes('suit'))
    return SVG_ICONS.SUIT
  if (
    nameClean.includes('khoac') ||
    nameClean.includes('hoodie') ||
    nameClean.includes('jacket') ||
    nameClean.includes('len') ||
    nameClean.includes('cardigan') ||
    nameClean.includes('sweater') ||
    nameClean.includes('gio') ||
    nameClean.includes('phao')
  )
    return SVG_ICONS.JACKET
  if (nameClean.includes('short') || nameClean.includes('dui') || nameClean.includes('sot'))
    return SVG_ICONS.SHORTS
  if (nameClean.includes('chan vay') || nameClean.includes('skirt')) return SVG_ICONS.SKIRT
  if (
    nameClean.includes('vay') ||
    nameClean.includes('dam') ||
    nameClean.includes('dress') ||
    nameClean.includes('yem') ||
    nameClean.includes('ao dai')
  )
    return SVG_ICONS.DRESS
  if (
    nameClean.includes('tay') ||
    nameClean.includes('au') ||
    nameClean.includes('kaki') ||
    nameClean.includes('jogger') ||
    nameClean.includes('pants') ||
    nameClean.includes('quan dai')
  )
    return SVG_ICONS.TROUSERS
  if (
    nameClean.includes('giay') ||
    nameClean.includes('dep') ||
    nameClean.includes('sneaker') ||
    nameClean.includes('sandal') ||
    nameClean.includes('boot')
  )
    return SVG_ICONS.SHOES
  if (
    nameClean.includes('tui') ||
    nameClean.includes('balo') ||
    nameClean.includes('backpack') ||
    nameClean.includes('bag') ||
    nameClean.includes('vi')
  )
    return SVG_ICONS.BAG
  if (
    nameClean.includes('mu') ||
    nameClean.includes('non') ||
    nameClean.includes('hat') ||
    nameClean.includes('cap')
  )
    return SVG_ICONS.HAT
  if (
    nameClean.includes('phu kien') ||
    nameClean.includes('kinh') ||
    nameClean.includes('that lung') ||
    nameClean.includes('belt') ||
    nameClean.includes('dong ho') ||
    nameClean.includes('trang suc')
  )
    return SVG_ICONS.ACCESSORIES
  if (nameClean.includes('lot') || nameClean.includes('ngu') || nameClean.includes('underwear'))
    return SVG_ICONS.UNDERWEAR
  if (nameClean.includes('tat') || nameClean.includes('vo') || nameClean.includes('socks'))
    return SVG_ICONS.SOCKS

  return SVG_ICONS.DEFAULT
}

const getCategoryPath = (item) => {
  const category = item?.id ?? item?.maDanhMuc ?? item?.slug ?? item?.name
  return {
    path: '/san-pham',
    query: category ? { category: String(category) } : {},
  }
}

const scrollContainer = ref(null)
const isAtStart = ref(true)
const isAtEnd = ref(false)
const hasOverflow = ref(false)
const progressOffset = ref(0)

let isMouseDown = false
let isDragging = false
let startX = 0
let scrollLeftStart = 0
let resizeObserver = null

const scroll = (direction) => {
  if (!scrollContainer.value) return
  const scrollAmount = 300
  scrollContainer.value.scrollBy({
    left: direction === 'left' ? -scrollAmount : scrollAmount,
    behavior: 'smooth',
  })
}

const updateScrollProgress = () => {
  if (!scrollContainer.value) return
  const { scrollLeft, scrollWidth, clientWidth } = scrollContainer.value

  hasOverflow.value = scrollWidth > clientWidth + 5

  isAtStart.value = scrollLeft <= 5
  isAtEnd.value = scrollLeft + clientWidth >= scrollWidth - 5

  const maxScroll = scrollWidth - clientWidth
  if (maxScroll > 0) {
    const ratio = scrollLeft / maxScroll
    progressOffset.value = ratio * 68
  } else {
    progressOffset.value = 0
  }
}

const startDrag = (e) => {
  if (!hasOverflow.value) return
  isMouseDown = true
  isDragging = false
  startX = e.pageX - scrollContainer.value.offsetLeft
  scrollLeftStart = scrollContainer.value.scrollLeft
}

const stopDrag = () => {
  isMouseDown = false
  setTimeout(() => {
    isDragging = false
  }, 50)
}

const onDrag = (e) => {
  if (!isMouseDown || !hasOverflow.value) return
  e.preventDefault()
  const x = e.pageX - scrollContainer.value.offsetLeft
  const walk = (x - startX) * 1.4
  if (Math.abs(x - startX) > 5) {
    isDragging = true
  }
  scrollContainer.value.scrollLeft = scrollLeftStart - walk
}

const handleCardClick = (e) => {
  if (isDragging) {
    e.preventDefault()
  }
}

watch(
  () => props.categories,
  () => {
    nextTick(updateScrollProgress)
  },
  { deep: true },
)

onMounted(() => {
  nextTick(() => {
    updateScrollProgress()
    if (window.ResizeObserver && scrollContainer.value) {
      resizeObserver = new ResizeObserver(() => updateScrollProgress())
      resizeObserver.observe(scrollContainer.value)
    } else {
      window.addEventListener('resize', updateScrollProgress)
    }
  })
})

onUnmounted(() => {
  if (resizeObserver) resizeObserver.disconnect()
  else window.removeEventListener('resize', updateScrollProgress)
})
</script>
