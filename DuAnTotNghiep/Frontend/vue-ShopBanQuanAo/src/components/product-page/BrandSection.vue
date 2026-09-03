<template>
  <div class="w-full bg-[#f5f5f5] font-sans">
    <section class="mx-auto max-w-[1480px] px-4 py-3 md:px-8">
      <div class="w-full rounded-2xl border border-zinc-200/80 bg-white p-5 shadow-sm md:p-6">
        <!-- Header Section -->
        <div class="mb-5 flex items-center justify-between">
          <div class="relative pb-2">
            <h2 class="text-lg font-black uppercase tracking-tight text-zinc-900 md:text-xl">
              TÌM THEO THƯƠNG HIỆU
            </h2>
            <div
              class="absolute -bottom-1 left-0 h-1 w-16 rounded-full bg-gradient-to-r from-red-600 via-red-500 to-transparent"
            ></div>
          </div>

          <!-- Nút Cuộn Trái / Phải -->
          <div v-if="brands && brands.length > 5" class="hidden gap-1.5 sm:flex">
            <button
              class="flex h-8 w-8 items-center justify-center rounded-lg border border-zinc-200 bg-white text-zinc-600 transition hover:border-red-600 hover:bg-red-600 hover:text-white"
              title="Cuộn sang trái"
              @click="scroll('left')"
            >
              ‹
            </button>
            <button
              class="flex h-8 w-8 items-center justify-center rounded-lg border border-zinc-200 bg-white text-zinc-600 transition hover:border-red-600 hover:bg-red-600 hover:text-white"
              title="Cuộn sang phải"
              @click="scroll('right')"
            >
              ›
            </button>
          </div>
        </div>

        <!-- Skeleton Loading -->
        <div v-if="loading" class="flex gap-3 overflow-hidden py-2">
          <div
            v-for="i in 6"
            :key="i"
            class="h-32 w-40 flex-shrink-0 animate-pulse rounded-xl border border-zinc-200 bg-zinc-100"
          ></div>
        </div>

        <!-- Danh sách Thương Hiệu Cuộn Ngang -->
        <div
          v-else-if="brands && brands.length > 0"
          ref="scrollContainer"
          class="no-scrollbar flex gap-3 overflow-x-auto scroll-smooth py-1 px-0.5 snap-x snap-mandatory md:gap-4"
        >
          <div
            v-for="brand in brands"
            :key="getBrandKey(brand)"
            class="group relative flex h-[135px] w-[145px] flex-shrink-0 cursor-pointer snap-start flex-col items-center justify-between rounded-xl border border-zinc-200 bg-white p-3 text-center transition-all duration-300 hover:-translate-y-1 hover:border-red-600 hover:shadow-lg md:w-[170px]"
            :class="{
              'border-red-600 ring-2 ring-red-600/20 bg-red-50/20': isSelected(brand),
            }"
            @click="handleSelectBrand(brand)"
          >
            <!-- CASE 1: Có ảnh Logo chuẩn -->
            <template v-if="resolveBrandLogo(brand)">
              <div class="flex h-20 w-full items-center justify-center rounded-lg bg-white p-1">
                <img
                  :src="resolveBrandLogo(brand)"
                  :alt="getBrandName(brand)"
                  class="max-h-full max-w-[85%] object-contain filter transition-transform duration-200 group-hover:scale-105"
                  @error="handleImageError"
                />
              </div>
              <span
                class="mb-1 text-[11px] font-extrabold uppercase tracking-widest text-zinc-600 transition-colors group-hover:text-red-600 md:text-xs"
                :class="{ 'text-red-600 font-black': isSelected(brand) }"
              >
                {{ getBrandName(brand) }}
              </span>
            </template>

            <!-- CASE 2: Không có ảnh -> TỰ ĐỘNG VẼ LOGO VECTOR SVG -->
            <template v-else>
              <div class="flex h-full w-full items-center justify-center py-1">
                <!-- VARIANT 0: Luxury Crown & Crest -->
                <svg
                  v-if="getLogoVariant(brand) === 0"
                  class="h-full w-full transition-transform duration-300 group-hover:scale-105"
                  viewBox="0 0 160 110"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <defs>
                    <linearGradient
                      :id="'gold-' + getBrandKey(brand)"
                      x1="0%"
                      y1="0%"
                      x2="100%"
                      y2="100%"
                    >
                      <stop offset="0%" stop-color="#D4AF37" />
                      <stop offset="50%" stop-color="#FFDF00" />
                      <stop offset="100%" stop-color="#AA771C" />
                    </linearGradient>
                  </defs>
                  <path
                    d="M65 22 L70 30 L80 18 L90 30 L95 22 L92 35 L68 35 Z"
                    :fill="'url(#gold-' + getBrandKey(brand) + ')'"
                  />
                  <path
                    d="M45 35 Q80 25 115 35 C115 75 80 90 80 90 C80 90 45 75 45 35 Z"
                    stroke-width="2.5"
                    fill="none"
                    :stroke="'url(#gold-' + getBrandKey(brand) + ')'"
                  />
                  <path
                    d="M50 39 Q80 30 110 39 C110 72 80 84 80 84 C80 84 50 72 50 39 Z"
                    stroke="#18181b"
                    stroke-width="1"
                    stroke-dasharray="2 2"
                    fill="none"
                    opacity="0.4"
                  />
                  <text
                    x="80"
                    y="62"
                    font-family="serif"
                    font-size="22"
                    font-weight="900"
                    text-anchor="middle"
                    fill="#18181b"
                    letter-spacing="1"
                  >
                    {{ getInitials(getBrandName(brand)) }}
                  </text>
                  <text
                    x="80"
                    y="102"
                    font-family="sans-serif"
                    font-size="10"
                    font-weight="800"
                    text-anchor="middle"
                    fill="#18181b"
                    letter-spacing="2"
                  >
                    {{ truncateName(getBrandName(brand), 12) }}
                  </text>
                </svg>

                <!-- VARIANT 1: Vintage Heritage Stamp -->
                <svg
                  v-else-if="getLogoVariant(brand) === 1"
                  class="h-full w-full transition-transform duration-300 group-hover:scale-105"
                  viewBox="0 0 160 110"
                  fill="none"
                >
                  <circle cx="80" cy="48" r="38" stroke="#18181b" stroke-width="2" fill="none" />
                  <circle
                    cx="80"
                    cy="48"
                    r="33"
                    stroke="#dc2626"
                    stroke-width="1.5"
                    stroke-dasharray="4 2"
                    fill="none"
                  />
                  <path d="M52 48 L54 44 L58 44 L55 47 L56 51 L52 48 Z" fill="#dc2626" />
                  <path d="M108 48 L106 44 L102 44 L105 47 L104 51 L108 48 Z" fill="#dc2626" />
                  <text
                    x="80"
                    y="55"
                    font-family="serif"
                    font-size="20"
                    font-weight="900"
                    text-anchor="middle"
                    fill="#18181b"
                  >
                    {{ getInitials(getBrandName(brand)) }}
                  </text>
                  <path d="M35 85 L50 78 L110 78 L125 85 L115 98 L80 93 L45 98 Z" fill="#18181b" />
                  <text
                    x="80"
                    y="90"
                    font-family="sans-serif"
                    font-size="9"
                    font-weight="900"
                    text-anchor="middle"
                    fill="#ffffff"
                    letter-spacing="1.5"
                  >
                    {{ truncateName(getBrandName(brand), 14) }}
                  </text>
                </svg>

                <!-- VARIANT 2: Cyber Geometric Tech -->
                <svg
                  v-else-if="getLogoVariant(brand) === 2"
                  class="h-full w-full transition-transform duration-300 group-hover:scale-105"
                  viewBox="0 0 160 110"
                  fill="none"
                >
                  <defs>
                    <linearGradient
                      :id="'neon-' + getBrandKey(brand)"
                      x1="0%"
                      y1="0%"
                      x2="100%"
                      y2="0%"
                    >
                      <stop offset="0%" stop-color="#dc2626" />
                      <stop offset="100%" stop-color="#f97316" />
                    </linearGradient>
                  </defs>
                  <polygon
                    points="80,10 120,45 80,80 40,45"
                    stroke-width="2.5"
                    fill="none"
                    :stroke="'url(#neon-' + getBrandKey(brand) + ')'"
                  />
                  <polygon
                    points="80,16 112,45 80,74 48,45"
                    stroke="#18181b"
                    stroke-width="1"
                    fill="none"
                    opacity="0.3"
                  />
                  <text
                    x="80"
                    y="52"
                    font-family="sans-serif"
                    font-size="20"
                    font-weight="900"
                    font-style="italic"
                    text-anchor="middle"
                    fill="#18181b"
                  >
                    {{ getInitials(getBrandName(brand)) }}
                  </text>
                  <line x1="55" y1="92" x2="105" y2="92" stroke="#dc2626" stroke-width="2" />
                  <text
                    x="80"
                    y="104"
                    font-family="sans-serif"
                    font-size="10"
                    font-weight="900"
                    text-anchor="middle"
                    fill="#18181b"
                    letter-spacing="2"
                  >
                    {{ truncateName(getBrandName(brand), 12) }}
                  </text>
                </svg>

                <!-- VARIANT 3: High-Fashion Monogram -->
                <svg
                  v-else-if="getLogoVariant(brand) === 3"
                  class="h-full w-full transition-transform duration-300 group-hover:scale-105"
                  viewBox="0 0 160 110"
                  fill="none"
                >
                  <path
                    d="M40 30 C50 15, 65 15, 80 20 C95 15, 110 15, 120 30"
                    stroke="#18181b"
                    stroke-width="1.5"
                    fill="none"
                  />
                  <path
                    d="M40 70 C50 85, 65 85, 80 80 C95 85, 110 85, 120 70"
                    stroke="#18181b"
                    stroke-width="1.5"
                    fill="none"
                  />
                  <text
                    x="80"
                    y="58"
                    font-family="serif"
                    font-size="28"
                    font-weight="bold"
                    font-style="italic"
                    text-anchor="middle"
                    fill="#18181b"
                  >
                    {{ getInitials(getBrandName(brand)) }}
                  </text>
                  <text
                    x="80"
                    y="100"
                    font-family="serif"
                    font-size="10"
                    font-weight="900"
                    text-anchor="middle"
                    fill="#dc2626"
                    letter-spacing="3"
                  >
                    {{ truncateName(getBrandName(brand), 12) }}
                  </text>
                </svg>

                <!-- VARIANT 4: Dynamic Sport Badge -->
                <svg
                  v-else
                  class="h-full w-full transition-transform duration-300 group-hover:scale-105"
                  viewBox="0 0 160 110"
                  fill="none"
                >
                  <path d="M45 20 L115 20 L125 60 L80 90 L35 60 Z" fill="#18181b" />
                  <path d="M50 25 L110 25 L118 58 L80 83 L42 58 Z" fill="#ffffff" />
                  <path d="M55 32 L105 32 L98 42 L48 42 Z" fill="#dc2626" />
                  <text
                    x="80"
                    y="65"
                    font-family="sans-serif"
                    font-size="22"
                    font-weight="900"
                    font-style="italic"
                    text-anchor="middle"
                    fill="#18181b"
                  >
                    {{ getInitials(getBrandName(brand)) }}
                  </text>
                  <text
                    x="80"
                    y="104"
                    font-family="sans-serif"
                    font-size="10"
                    font-weight="900"
                    text-anchor="middle"
                    fill="#18181b"
                    letter-spacing="1"
                  >
                    {{ truncateName(getBrandName(brand), 12) }}
                  </text>
                </svg>
              </div>
            </template>

            <!-- Tích đỏ khi chọn -->
            <div
              v-if="isSelected(brand)"
              class="absolute top-2 right-2 z-10 flex h-4 w-4 items-center justify-center rounded-full bg-red-600 text-[10px] font-bold text-white shadow-sm"
            >
              ✓
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  brands: {
    type: Array,
    default: () => [],
  },
  loading: {
    type: Boolean,
    default: false,
  },
  selectedBrand: {
    type: [Object, String, Number, Array],
    default: null,
  },
})

const emit = defineEmits(['select-brand'])
const scrollContainer = ref(null)

const LOGO_PRESETS = {
  nike: 'https://upload.wikimedia.org/wikipedia/commons/a/a6/Logo_NIKE.svg',
  adidas: 'https://upload.wikimedia.org/wikipedia/commons/2/20/Adidas_Logo.svg',
  puma: 'https://upload.wikimedia.org/wikipedia/en/3/37/Puma_complete_logo.svg',
  converse: 'https://upload.wikimedia.org/wikipedia/commons/3/30/Converse_logo.svg',
  vans: 'https://upload.wikimedia.org/wikipedia/commons/9/92/Vans-logo.svg',
  newbalance: 'https://upload.wikimedia.org/wikipedia/commons/e/ea/New_Balance_logo.svg',
  reebok: 'https://upload.wikimedia.org/wikipedia/commons/d/d4/Reebok_2019_logo.svg',
  zara: 'https://upload.wikimedia.org/wikipedia/commons/f/fd/Zara_Logo.svg',
  uniqlo: 'https://upload.wikimedia.org/wikipedia/commons/9/92/UNIQLO_logo.svg',
  hm: 'https://upload.wikimedia.org/wikipedia/commons/5/53/H%26M-Logo.svg',
  gucci: 'https://upload.wikimedia.org/wikipedia/commons/7/79/1990s_Gucci_logo.svg',
  chanel: 'https://upload.wikimedia.org/wikipedia/en/9/92/Chanel_logo_interlocking_cs.svg',
  dior: 'https://upload.wikimedia.org/wikipedia/commons/a/a8/Dior_Logo.svg',
  apple: 'https://upload.wikimedia.org/wikipedia/commons/f/fa/Apple_logo_black.svg',
  samsung: 'https://upload.wikimedia.org/wikipedia/commons/2/24/Samsung_Logo.svg',
}

const getBrandName = (brand) => {
  if (typeof brand === 'string') return brand
  return brand?.tenThuongHieu || brand?.name || brand?.ten || brand?.label || ''
}

const getCleanSlug = (brand) => {
  let raw = ''
  if (typeof brand === 'object') {
    raw = brand?.slug || brand?.code || getBrandName(brand)
  } else {
    raw = String(brand)
  }
  return raw.toLowerCase().replace(/[^a-z0-9]/g, '')
}

const getBrandKey = (brand) => {
  if (typeof brand === 'object') return brand.id || brand.maThuongHieu || getBrandName(brand)
  return brand
}

const resolveBrandLogo = (brand) => {
  if (typeof brand === 'object' && (brand.logo || brand.hinhAnh || brand.imageUrl)) {
    return brand.logo || brand.hinhAnh || brand.imageUrl
  }
  const cleanSlug = getCleanSlug(brand)
  return LOGO_PRESETS[cleanSlug] || null
}

const getInitials = (name) => {
  if (!name) return 'B'
  const words = name.trim().split(/\s+/)
  if (words.length >= 2) {
    return (words[0][0] + words[1][0]).toUpperCase()
  }
  return name.slice(0, 2).toUpperCase()
}

const truncateName = (str, maxLen = 12) => {
  if (!str) return ''
  return str.length > maxLen ? str.slice(0, maxLen) + '..' : str
}

const getLogoVariant = (brand) => {
  const name = getBrandName(brand)
  let hash = 0
  for (let i = 0; i < name.length; i++) {
    hash = name.charCodeAt(i) + ((hash << 5) - hash)
  }
  return Math.abs(hash) % 5
}

const handleImageError = (e) => {
  e.target.style.display = 'none'
}

const scroll = (direction) => {
  if (!scrollContainer.value) return
  const scrollAmount = direction === 'left' ? -320 : 320
  scrollContainer.value.scrollBy({ left: scrollAmount, behavior: 'smooth' })
}

const isSelected = (brand) => {
  if (!props.selectedBrand) return false
  const name = getBrandName(brand)
  const cleanSlug = getCleanSlug(brand)

  if (Array.isArray(props.selectedBrand)) {
    return (
      props.selectedBrand.includes(name) ||
      props.selectedBrand.includes(brand.id) ||
      props.selectedBrand.includes(cleanSlug)
    )
  }
  return (
    props.selectedBrand === name ||
    props.selectedBrand === brand.id ||
    props.selectedBrand === cleanSlug
  )
}

const handleSelectBrand = (brand) => {
  emit('select-brand', brand)
}
</script>

<style scoped>
.no-scrollbar::-webkit-scrollbar {
  display: none;
}
.no-scrollbar {
  -ms-overflow-style: none;
  scrollbar-width: none;
}
</style>
