<template>
  <div class="w-full bg-[#f5f5f5] font-sans">
    <!-- HEADER -->
    <header class="w-full sticky top-0 z-50 transition-all duration-300">
      <!-- 1. Banner khuyến mãi (Đỏ tươi) -->
      <div class="bg-[#d30f15] px-4 py-2.5 text-white shadow-sm">
        <div
          class="mx-auto flex max-w-[1480px] items-center justify-center gap-4 text-sm font-bold md:text-base"
        >
          <span
            class="rounded-full bg-yellow-400 px-3 py-1 text-xs font-black uppercase text-red-800 shadow-sm md:text-sm"
          >
            MỪNG SIÊU LỄ
          </span>
          <span class="tracking-wide">
            GIẢM LỚN ĐẾN
            <strong class="text-lg font-black text-yellow-300 md:text-xl">50%</strong> ALL ITEMS
          </span>
          <button
            type="button"
            class="hidden rounded-full bg-yellow-400 px-4 py-1 text-xs font-black uppercase text-red-900 transition hover:bg-yellow-300 sm:inline-block md:text-sm"
            @click="goToProductPage()"
          >
            XEM NGAY
          </button>
        </div>
      </div>

      <!-- 2. Thanh Header chính -->
      <div class="border-b border-amber-200/50 bg-[#f4eee0] px-4 py-4 md:px-8 shadow-sm">
        <div class="mx-auto flex max-w-[1480px] items-center justify-between gap-6 md:gap-10">
          <!-- Logo thương hiệu -->
          <div class="flex shrink-0 cursor-pointer items-center gap-3" @click="goToHome">
            <Shirt :size="36" class="text-zinc-900" />
            <span class="text-3xl font-black uppercase tracking-tighter text-zinc-900 md:text-4xl">
              K-ZONE
            </span>
          </div>

          <!-- Ô tìm kiếm -->
          <div class="max-w-2xl flex-1">
            <div class="relative flex items-center">
              <input
                v-model="keyword"
                type="text"
                class="w-full rounded-lg border border-zinc-300 bg-white py-2.5 pl-4 pr-11 text-sm text-zinc-800 shadow-sm outline-none placeholder:text-zinc-400 focus:border-red-600 focus:ring-2 focus:ring-red-600/20 md:text-base"
                placeholder="Tìm kiếm trong K-Zone (Áo thun, Quần Jean, Áo khoác...)"
                @keydown.enter="emitSearch"
              />
              <button
                type="button"
                class="absolute right-3.5 text-zinc-500 transition hover:text-red-600"
                @click="emitSearch"
              >
                <Search :size="20" />
              </button>
            </div>
          </div>

          <!-- Cụm tiện ích & liên hệ -->
          <div class="hidden shrink-0 items-center gap-6 text-sm font-medium text-zinc-800 lg:flex">
            <!-- Hotline -->
            <div class="flex cursor-pointer items-center gap-2.5 transition hover:text-red-600">
              <Headphones :size="24" class="text-zinc-800" />
              <div class="leading-tight">
                <p class="text-xs font-medium text-zinc-500">Hotline</p>
                <p class="text-sm font-bold text-zinc-900">1900 6750</p>
              </div>
            </div>

            <!-- Showroom -->
            <div class="flex cursor-pointer items-center gap-2.5 transition hover:text-red-600">
              <Store :size="24" class="text-zinc-800" />
              <div class="leading-tight">
                <p class="text-xs font-medium text-zinc-500">Hệ thống</p>
                <p class="text-sm font-bold text-zinc-900">Showroom</p>
              </div>
            </div>

            <!-- TÀI KHOẢN (USER DROPDOWN) -->
            <div
              class="relative user-dropdown"
              @mouseenter="openUser = true"
              @mouseleave="openUser = false"
            >
              <button
                type="button"
                class="p-2 text-zinc-800 transition hover:text-red-600 rounded-full hover:bg-black/5"
                title="Tài khoản"
              >
                <User :size="24" />
              </button>

              <transition
                enter-active-class="transition duration-200 ease-out"
                enter-from-class="opacity-0 scale-95 translate-y-1"
                enter-to-class="opacity-100 scale-100 translate-y-0"
                leave-active-class="transition duration-150 ease-in"
                leave-from-class="opacity-100 scale-100 translate-y-0"
                leave-to-class="opacity-0 scale-95 translate-y-1"
              >
                <div
                  v-if="openUser"
                  class="absolute right-0 mt-2 w-72 bg-white rounded-2xl shadow-xl border border-zinc-200 overflow-hidden py-1.5 z-50 text-left"
                >
                  <div
                    class="px-4 py-3 mx-2 my-1 bg-gradient-to-r from-red-600 to-amber-600 text-white rounded-xl shadow-xs"
                  >
                    <div class="flex items-center gap-3">
                      <div
                        class="w-10 h-10 rounded-full bg-white/20 flex items-center justify-center font-bold text-sm flex-shrink-0"
                      >
                        {{ avatar }}
                      </div>
                      <div class="min-w-0">
                        <p class="font-bold text-xs truncate">{{ user?.hoTen || 'Khách hàng' }}</p>
                        <p class="text-[11px] opacity-80 truncate mt-0.5">
                          {{ user?.email || 'Chưa cập nhật email' }}
                        </p>
                      </div>
                    </div>
                  </div>

                  <div class="h-px bg-zinc-100 my-1"></div>

                  <div class="px-1.5 space-y-0.5">
                    <RouterLink
                      v-for="item in userMenus"
                      :key="item.name"
                      :to="item.path"
                      class="flex items-center gap-3 px-3 py-2.5 rounded-xl text-xs font-medium text-zinc-700 hover:text-red-600 hover:bg-zinc-100 transition"
                    >
                      <component :is="item.icon" class="w-4 h-4 text-red-600" />
                      <span>{{ item.name }}</span>
                    </RouterLink>
                    <RouterLink
                      to="/minigame"
                      class="flex items-center gap-3 px-3 py-2.5 rounded-xl text-xs font-medium text-zinc-700 hover:text-red-600 hover:bg-zinc-100 transition"
                    >
                      <Sparkles class="w-4 h-4 text-amber-500" />
                      <span>Minigame & Ví Xu</span>
                    </RouterLink>
                  </div>

                  <div class="h-px bg-zinc-100 my-1"></div>

                  <div class="px-1.5">
                    <button
                      @click="logout"
                      class="w-full flex items-center gap-3 px-3 py-2.5 rounded-xl text-xs font-medium text-rose-600 hover:bg-rose-50 transition text-left"
                    >
                      <LogOut class="w-4 h-4 text-rose-500" />
                      <span>Đăng xuất</span>
                    </button>
                  </div>
                </div>
              </transition>
            </div>

            <!-- YÊU THÍCH (WISHLIST) -->
            <RouterLink
              to="/yeuthich"
              class="relative p-2 text-zinc-800 transition hover:text-red-600 rounded-full hover:bg-black/5"
              title="Yêu thích"
            >
              <Heart :size="24" />
              <span
                v-if="wishlistCount > 0"
                class="absolute top-0 right-0 min-w-[18px] h-4 px-1 rounded-full bg-emerald-600 text-white text-[10px] font-bold flex items-center justify-center ring-2 ring-white shadow"
              >
                {{ wishlistCount }}
              </span>
            </RouterLink>

            <!-- GIỎ HÀNG (CART DROPDOWN + TÍNH TOÁN VỊ TRÍ) -->
            <div class="relative" @mouseenter="openCart" @mouseleave="closeCart">
              <button
                ref="cartBtnRef"
                type="button"
                class="flex items-center gap-2 p-2 rounded-xl text-zinc-800 transition hover:text-red-600 hover:bg-black/5"
                title="Giỏ hàng"
                @click.stop="toggleCart"
              >
                <div class="relative">
                  <ShoppingBag :size="24" />
                  <span
                    v-if="cartCount > 0"
                    class="absolute -right-2 -top-1.5 min-w-[18px] h-4 px-1 rounded-full bg-red-600 text-white text-[10px] font-bold flex items-center justify-center ring-2 ring-white shadow"
                  >
                    {{ cartCount }}
                  </span>
                </div>
                <span class="ml-1 text-sm font-bold text-zinc-900">Giỏ hàng</span>
              </button>

              <!-- CART DROPDOWN -->
              <transition
                enter-active-class="transition duration-200 ease-out"
                enter-from-class="opacity-0 translate-y-2 scale-95"
                enter-to-class="opacity-100 translate-y-0 scale-100"
                leave-active-class="transition duration-150 ease-in"
                leave-from-class="opacity-100 translate-y-0 scale-100"
                leave-to-class="opacity-0 translate-y-2 scale-95"
              >
                <div
                  v-if="showCart"
                  class="absolute right-0 mt-3 w-[380px] bg-white rounded-2xl shadow-xl border border-zinc-200 overflow-hidden z-50 text-left"
                >
                  <div
                    class="px-5 py-4 border-b border-zinc-100 flex justify-between items-center bg-zinc-50"
                  >
                    <h3 class="font-bold text-zinc-800 text-base flex items-center gap-2">
                      <ShoppingBag :size="18" class="text-red-600" />
                      <span>Giỏ hàng</span>
                    </h3>
                    <span class="text-xs font-medium text-zinc-500">{{ cartCount }} sản phẩm</span>
                  </div>

                  <div v-if="cart.length === 0" class="py-12 text-center text-zinc-400">
                    <ShoppingBag class="mx-auto w-10 h-10 mb-3 opacity-40" />
                    <p class="text-sm">Giỏ hàng đang trống</p>
                  </div>

                  <div v-else class="max-h-[360px] overflow-y-auto divide-y divide-zinc-100">
                    <div
                      v-for="item in cart"
                      :key="item.id"
                      class="flex gap-3 px-5 py-3.5 hover:bg-zinc-50 transition"
                    >
                      <div
                        class="w-14 h-14 rounded-xl overflow-hidden bg-zinc-100 flex-shrink-0 border border-zinc-200"
                      >
                        <img
                          v-if="item.anh"
                          :src="'http://localhost:8080' + item.anh"
                          class="w-full h-full object-cover"
                          alt="Product"
                        />
                        <div v-else class="w-full h-full flex items-center justify-center text-lg">
                          📦
                        </div>
                      </div>

                      <div class="flex-1 min-w-0 flex flex-col justify-center">
                        <p class="font-semibold text-xs text-zinc-800 truncate">
                          {{ item.tenSanPham }}
                        </p>
                        <p class="text-[11px] text-zinc-500 mt-0.5">
                          {{ item.mauSac }} • {{ item.kichCo }}
                        </p>

                        <div class="flex justify-between items-center mt-1.5">
                          <span class="text-[11px] text-zinc-500">
                            {{ item.soLuong }} × {{ Number(item.giaBan).toLocaleString('vi-VN') }}đ
                          </span>
                          <span class="font-bold text-red-600 text-xs">
                            {{ Number(item.thanhTien).toLocaleString('vi-VN') }}đ
                          </span>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div v-if="cart.length" class="p-4 bg-zinc-50 border-t border-zinc-100">
                    <div class="flex justify-between items-center mb-3">
                      <span class="text-xs font-medium text-zinc-600">Tổng tiền thanh toán</span>
                      <span class="font-bold text-red-600 text-base">
                        {{ totalPrice.toLocaleString('vi-VN') }}đ
                      </span>
                    </div>
                    <RouterLink
                      to="/giohang"
                      @click="closeCartImmediately"
                      class="block text-center py-2.5 rounded-xl bg-red-600 text-white text-xs font-bold hover:bg-red-700 transition shadow-sm uppercase tracking-wider"
                    >
                      Xem giỏ hàng chi tiết
                    </RouterLink>
                  </div>
                </div>
              </transition>
            </div>
          </div>
        </div>
      </div>

      <!-- 3. Thanh Navigation -->
      <div class="bg-[#800000] text-white">
        <div class="mx-auto flex max-w-[1480px] items-center px-4 md:px-8">
          <div
            class="flex w-[260px] shrink-0 cursor-pointer items-center justify-between bg-[#6b0000] px-4 py-3.5 text-sm font-bold text-white transition hover:bg-[#580000] md:text-base xl:w-[280px]"
          >
            <div class="flex items-center gap-2.5">
              <LayoutGrid :size="18" />
              <span>Danh mục sản phẩm</span>
            </div>
            <ChevronDown :size="16" />
          </div>

          <nav
            class="flex flex-1 items-center justify-start gap-2 px-4 text-xs font-bold md:text-sm lg:text-base"
          >
            <RouterLink
              to="/home"
              class="whitespace-nowrap px-3.5 py-3.5 text-white transition hover:text-yellow-300"
            >
              Trang chủ
            </RouterLink>
            <a
              href="#"
              class="whitespace-nowrap px-3.5 py-3.5 text-white transition hover:text-yellow-300"
              >Giới thiệu</a
            >
            <a
              href="#"
              class="flex items-center gap-1.5 whitespace-nowrap px-3.5 py-3.5 font-extrabold text-yellow-300 transition hover:text-yellow-200"
            >
              <Zap :size="16" class="fill-yellow-300 text-yellow-300" /> Flash Sale
            </a>
            <a
              href="#"
              class="whitespace-nowrap px-3.5 py-3.5 text-white transition hover:text-yellow-300"
              >Tin tức mới</a
            >
            <a
              href="#"
              class="whitespace-nowrap px-3.5 py-3.5 text-white transition hover:text-yellow-300"
              @click.prevent="goToProductPage()"
              >Sản phẩm</a
            >
            <a
              href="#"
              class="whitespace-nowrap px-3.5 py-3.5 text-white transition hover:text-yellow-300"
              >Hệ thống cửa hàng</a
            >
            <a
              href="#"
              class="whitespace-nowrap px-3.5 py-3.5 text-white transition hover:text-yellow-300"
              >Liên hệ</a
            >
          </nav>
        </div>
      </div>
    </header>

    <!-- KHU VỰC HERO BANNER & MENU DỌC -->
    <main class="relative mx-auto flex max-w-[1480px] items-stretch px-4 py-4 md:px-8">
      <!-- Cột trái: Menu dọc danh mục từ API DB -->
      <aside
        class="relative z-10 hidden w-[260px] shrink-0 flex-col rounded-b-xl border border-t-0 border-zinc-200 bg-white shadow-sm lg:flex xl:w-[280px]"
      >
        <ul class="flex flex-col py-0.5">
          <li
            v-for="item in categoriesList"
            :key="item.id || item.maDanhMuc || item.name"
            class="group flex cursor-pointer items-center justify-between border-b border-zinc-100 px-4 py-3 text-sm font-semibold text-zinc-700 transition hover:bg-zinc-50 hover:text-red-600 last:border-0"
            @click="navigateToCategory(item)"
          >
            <div class="flex items-center gap-3">
              <component
                :is="getCategoryIcon(item)"
                :size="20"
                class="text-zinc-500 transition group-hover:text-red-600"
              />
              <span>{{ item.tenDanhMuc || item.name }}</span>
            </div>
            <ChevronRight :size="16" class="text-zinc-400 transition group-hover:text-red-600" />
          </li>
        </ul>

        <div
          class="mt-auto flex items-center justify-center gap-2 border-t border-zinc-100 bg-zinc-50 px-4 py-2.5 text-[11px] font-medium text-zinc-500"
        >
          <Mouse :size="14" />
          <span>Khám phá thêm</span>
        </div>
      </aside>

      <!-- Cột phải: HERO BANNER -->
      <div class="ml-0 flex flex-1 flex-col lg:ml-4">
        <div class="grid h-full w-full grid-cols-1 gap-4 lg:grid-cols-3">
          <!-- Main Hero Slider -->
          <div
            class="relative flex min-h-[380px] flex-col justify-between overflow-hidden rounded-2xl bg-zinc-950 p-5 text-white shadow-sm md:p-6 lg:col-span-2"
            @mouseenter="stopAutoplay"
            @mouseleave="startAutoplay"
          >
            <div
              class="absolute inset-0 z-10 bg-gradient-to-r from-black/85 via-black/40 to-transparent"
            ></div>

            <img
              :src="slides[activeSlide].image"
              :alt="slides[activeSlide].title"
              class="absolute inset-0 h-full w-full object-cover object-center transition-all duration-700 ease-in-out"
            />

            <div class="relative z-20 max-w-md">
              <span
                class="inline-block rounded-full bg-red-600 px-3 py-0.5 text-xs font-black uppercase tracking-wider text-white shadow-sm"
              >
                {{ slides[activeSlide].tag }}
              </span>
              <h1
                class="mt-2 text-2xl font-black uppercase tracking-tight text-white leading-tight md:text-3xl lg:text-4xl drop-shadow-md"
              >
                {{ slides[activeSlide].title }}
              </h1>
              <p class="mt-2 text-xs text-zinc-200 line-clamp-2 md:text-sm">
                {{ slides[activeSlide].desc }}
              </p>
              <div class="mt-4 md:mt-5">
                <button
                  type="button"
                  class="rounded-xl bg-white px-5 py-2 text-xs font-bold text-zinc-950 shadow-md transition hover:bg-yellow-400 md:text-sm"
                  @click="goToProductPage()"
                >
                  Khám phá ngay
                </button>
              </div>
            </div>

            <div class="relative z-20 mt-4 flex items-center justify-between">
              <div class="flex items-center gap-2">
                <span
                  v-for="(slide, index) in slides"
                  :key="index"
                  class="h-2 cursor-pointer rounded-full transition-all duration-300"
                  :class="
                    activeSlide === index ? 'w-8 bg-red-600' : 'w-2 bg-white/50 hover:bg-white'
                  "
                  @click="activeSlide = index"
                ></span>
              </div>

              <div class="flex items-center gap-2">
                <button
                  type="button"
                  class="flex h-8 w-8 items-center justify-center rounded-full bg-black/40 text-white backdrop-blur-md transition hover:bg-red-600 md:h-9 md:w-9"
                  @click="prevSlide"
                >
                  <ChevronLeft :size="18" />
                </button>
                <button
                  type="button"
                  class="flex h-8 w-8 items-center justify-center rounded-full bg-black/40 text-white backdrop-blur-md transition hover:bg-red-600 md:h-9 md:w-9"
                  @click="nextSlide"
                >
                  <ChevronRight :size="18" />
                </button>
              </div>
            </div>
          </div>

          <!-- Sub-Banners -->
          <div class="flex h-full flex-col gap-4 lg:col-span-1">
            <div
              class="group relative flex flex-1 cursor-pointer flex-col justify-end overflow-hidden rounded-2xl bg-zinc-900 p-4 shadow-sm md:p-5"
              @click="goToProductPage()"
            >
              <img
                src="https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?q=80&w=600&auto=format&fit=crop"
                alt="Urban Outfit"
                class="absolute inset-0 h-full w-full object-cover opacity-90 transition-transform duration-700 ease-out group-hover:scale-110"
              />
              <div
                class="absolute inset-0 bg-gradient-to-t from-black/90 via-black/30 to-transparent"
              ></div>

              <div class="relative z-10">
                <span
                  class="inline-block rounded-md bg-yellow-400 px-2 py-0.5 text-[10px] font-black uppercase text-zinc-950 shadow-sm"
                >
                  Trending Now
                </span>
                <h3
                  class="mt-1 text-base font-black uppercase tracking-tight text-white transition group-hover:text-yellow-300 md:text-lg"
                >
                  Urban Streetwear
                </h3>
                <p class="text-xs text-zinc-300">Outfit cá tính, chuẩn chất Gen Z</p>
              </div>
            </div>

            <div
              class="group relative flex flex-1 cursor-pointer flex-col justify-end overflow-hidden rounded-2xl bg-zinc-900 p-4 shadow-sm md:p-5"
              @click="goToProductPage()"
            >
              <img
                src="https://images.unsplash.com/photo-1552374196-1ab2a1c593e8?q=80&w=600&auto=format&fit=crop"
                alt="Accessories & Sneaker"
                class="absolute inset-0 h-full w-full object-cover opacity-90 transition-transform duration-700 ease-out group-hover:scale-110"
              />
              <div
                class="absolute inset-0 bg-gradient-to-t from-black/90 via-black/30 to-transparent"
              ></div>

              <div class="relative z-10">
                <span
                  class="inline-block rounded-md bg-red-600 px-2 py-0.5 text-[10px] font-black uppercase text-white shadow-sm"
                >
                  Must-have Items
                </span>
                <h3
                  class="mt-1 text-base font-black uppercase tracking-tight text-white transition group-hover:text-red-400 md:text-lg"
                >
                  Sneaker & Cap
                </h3>
                <p class="text-xs text-zinc-300">Nâng tầm phong cách mỗi ngày</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted, onUnmounted } from 'vue'
import { useRouter, RouterLink } from 'vue-router'
import {
  Search,
  Shirt,
  Headphones,
  Store,
  User,
  Heart,
  ShoppingBag,
  LayoutGrid,
  ChevronDown,
  ChevronRight,
  Zap,
  Mouse,
  Layers,
  Scissors,
  Sparkles,
  Gem,
  Glasses,
  Watch,
  ChevronLeft,
  Package,
  LogOut,
} from 'lucide-vue-next'
import axios from 'axios'
import emitter from '@/utils/emitter'
import yeuThichService from '@/service/yeuThichService'

const props = defineProps({
  modelValue: { type: String, default: '' },
  categories: { type: Array, default: () => [] },
})

const emit = defineEmits(['update:modelValue', 'select-category'])

const router = useRouter()
const keyword = ref(props.modelValue)
const categoriesList = ref([])

// State của ShopHeader
const openUser = ref(false)
const showCart = ref(false)
const cart = ref([])
const wishlist = ref([])
const cartBtnRef = ref(null)
let cartTimeout = null

// URL API Backend
const API_CATEGORIES_URL = 'http://localhost:8080/danhmuc'

// --- USER & AUTHENTICATION ---
const user = computed(() => {
  const data = sessionStorage.getItem('user')
  return data ? JSON.parse(data) : null
})

const getCustomerId = () => {
  const userStr = sessionStorage.getItem('user')
  if (!userStr) return null
  const userObj = JSON.parse(userStr)
  return (
    userObj?.idKhachHang || userObj?.khachHangId || userObj?.khachHang?.id || userObj?.id || null
  )
}

const userMenus = computed(() => [
  {
    name: 'Thông tin tài khoản',
    path: `/admin/khach-hang/edit/${user.value?.id || user.value?.idKhachHang || 1}`,
    icon: User,
  },
  {
    name: 'Đơn mua của tôi',
    path: '/donhang',
    icon: Package,
  },
])

const avatar = computed(() => {
  if (!user.value || !user.value.hoTen) return '?'
  return user.value.hoTen.substring(0, 2).toUpperCase()
})

const logout = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')
  router.push('/login')
}

// --- CART & WISHLIST LOGIC ---
const cartCount = computed(() => cart.value.length)
const wishlistCount = computed(() => wishlist.value.length)

const totalPrice = computed(() => {
  return cart.value.reduce((sum, item) => sum + Number(item.thanhTien), 0)
})

const loadCart = async () => {
  try {
    const token = sessionStorage.getItem('token')
    if (!token) {
      cart.value = []
      return
    }

    const res = await axios.get('http://localhost:8080/giohang', {
      headers: { Authorization: `Bearer ${token}` },
    })

    cart.value = res.data || []
  } catch (err) {
    console.warn('Lỗi khi tải giỏ hàng:', err)
    cart.value = []
  }
}

const loadWishlist = async () => {
  try {
    const idKhachHang = getCustomerId()
    if (!idKhachHang) {
      wishlist.value = []
      return
    }

    const data = await yeuThichService.getDanhSachYeuThich(idKhachHang)
    wishlist.value = data || []
  } catch (err) {
    console.warn('Lỗi khi tải wishlist:', err)
    wishlist.value = []
  }
}

// Cập nhật vị trí nút giỏ hàng hỗ trợ hiệu ứng thêm vào giỏ bay
const updateCartPosition = () => {
  if (cartBtnRef.value) {
    const rect = cartBtnRef.value.getBoundingClientRect()
    const position = {
      x: rect.left + rect.width / 2,
      y: rect.top + rect.height / 2,
    }
    sessionStorage.setItem('cart_icon_pos', JSON.stringify(position))
  }
}

const openCart = () => {
  if (cartTimeout) clearTimeout(cartTimeout)
  showCart.value = true
}

const closeCart = () => {
  cartTimeout = setTimeout(() => {
    showCart.value = false
  }, 250)
}

const toggleCart = () => {
  showCart.value = !showCart.value
}

const closeCartImmediately = () => {
  showCart.value = false
}

// --- DANH MỤC & ĐIỀU HƯỚNG ---
const fetchCategoriesFromDb = async () => {
  try {
    const res = await fetch(API_CATEGORIES_URL)
    if (res.ok) {
      const data = await res.json()
      categoriesList.value = data
      return
    }
  } catch (err) {
    console.warn('Chưa kết nối API danh mục, dùng danh mục mặc định:', err)
  }

  categoriesList.value = [
    { id: 1, maDanhMuc: 'DM01', tenDanhMuc: 'Áo Thun & Áo Polo' },
    { id: 2, maDanhMuc: 'DM02', tenDanhMuc: 'Áo Sơ Mi Nam Nữ' },
    { id: 3, maDanhMuc: 'DM03', tenDanhMuc: 'Quần Jean & Kaki' },
    { id: 4, maDanhMuc: 'DM04', tenDanhMuc: 'Áo Khoác & Hoodie' },
    { id: 5, maDanhMuc: 'DM05', tenDanhMuc: 'Đầm & Chân Váy' },
    { id: 6, maDanhMuc: 'DM06', tenDanhMuc: 'Phụ kiện Thời Trang' },
    { id: 7, maDanhMuc: 'DM07', tenDanhMuc: 'Túi xách & Balo' },
    { id: 8, maDanhMuc: 'DM08', tenDanhMuc: 'Đồng Hồ & Trang Sức' },
  ]
}

const navigateToCategory = (categoryItem) => {
  const categoryParam =
    categoryItem.id || categoryItem.maDanhMuc || categoryItem.tenDanhMuc || categoryItem.name

  if (router) {
    router
      .push({
        path: '/san-pham',
        query: { category: categoryParam },
      })
      .catch(() => {})
  }
  emit('select-category', categoryItem)
}

const goToProductPage = (queryParam = {}) => {
  if (router) {
    router.push({ path: '/san-pham', query: queryParam }).catch(() => {})
  }
}

const goToHome = () => {
  if (router) router.push('/home')
}

const getCategoryIcon = (item) => {
  const name = (item.tenDanhMuc || item.name || '').toLowerCase()
  if (name.includes('sơ mi') || name.includes('áo thun') || name.includes('polo')) return Shirt
  if (name.includes('quần')) return Scissors
  if (name.includes('khoác') || name.includes('hoodie')) return Sparkles
  if (name.includes('đầm') || name.includes('váy')) return Gem
  if (name.includes('túi') || name.includes('balo')) return ShoppingBag
  if (name.includes('kính') || name.includes('phụ kiện')) return Glasses
  if (name.includes('đồng hồ') || name.includes('trang sức')) return Watch
  return Layers
}

watch(
  () => props.modelValue,
  (value) => {
    keyword.value = value
  },
)
watch(keyword, (value) => emit('update:modelValue', value))

const emitSearch = () => {
  const queryVal = keyword.value.trim()
  if (router) {
    router.push({ path: '/san-pham', query: { keyword: queryVal } }).catch(() => {})
  }
  emit('update:modelValue', queryVal)
}

// --- SLIDE HERO BANNER ---
const activeSlide = ref(0)
const slides = [
  {
    tag: 'Bộ Sưu Tập Mới 2026',
    title: 'Streetwear Minimalist',
    desc: 'Phong cách tối giản, chất liệu cotton cao cấp 100%. Định hình phong cách thời trang trẻ trung & hiện đại.',
    image:
      'https://images.unsplash.com/photo-1441986300917-64674bd600d8?q=80&w=1200&auto=format&fit=crop',
  },
  {
    tag: 'Bán Chạy Nhất',
    title: 'Oversized Hoodie',
    desc: 'Thiết kế unisex chuẩn form street style, chất vải nỉ chân cua dày dặn giữ ấm tối ưu.',
    image:
      'https://images.unsplash.com/photo-1556905055-8f358a7a47b2?q=80&w=1200&auto=format&fit=crop',
  },
  {
    tag: 'Flash Sale Lễ',
    title: 'Basic Tee & Denim Pants',
    desc: 'Combo hoàn hảo cho outfit hằng ngày. Giảm sâu đến 50% toàn bộ sản phẩm trong tuần này.',
    image:
      'https://images.unsplash.com/photo-1489987707025-afc232f7ea0f?q=80&w=1200&auto=format&fit=crop',
  },
]

let timer = null
const nextSlide = () => {
  activeSlide.value = (activeSlide.value + 1) % slides.length
}
const prevSlide = () => {
  activeSlide.value = (activeSlide.value - 1 + slides.length) % slides.length
}
const startAutoplay = () => {
  stopAutoplay()
  timer = setInterval(nextSlide, 4000)
}
const stopAutoplay = () => {
  if (timer) clearInterval(timer)
}

// --- LIFECYCLE HOOKS ---
onMounted(() => {
  window.addEventListener('resize', updateCartPosition)

  emitter.on('cart-updated', loadCart)
  emitter.on('wishlist-updated', loadWishlist)

  loadCart()
  loadWishlist()

  if (props.categories && props.categories.length > 0) {
    categoriesList.value = props.categories
  } else {
    fetchCategoriesFromDb()
  }
  startAutoplay()

  setTimeout(updateCartPosition, 100)
})

onUnmounted(() => {
  window.removeEventListener('resize', updateCartPosition)

  emitter.off('cart-updated', loadCart)
  emitter.off('wishlist-updated', loadWishlist)

  stopAutoplay()
})
</script>
