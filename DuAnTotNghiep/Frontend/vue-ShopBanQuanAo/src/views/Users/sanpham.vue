<template>
  <header
    :class="[
      'fixed top-0 inset-x-0 z-50 transition-all duration-300',
      scrolled
        ? 'bg-white shadow-lg border-b border-neutral-200'
        : 'bg-white/95 backdrop-blur-md border-b border-neutral-100',
    ]"
  >
    <div class="max-w-7xl mx-auto px-5 lg:px-8 h-16 lg:h-20 flex items-center justify-between">
      <a href="#" class="text-2xl font-extrabold tracking-tight">
        Vel<span class="text-amber-500">o</span>ra
      </a>

      <nav class="hidden lg:flex items-center gap-9">
        <RouterLink
          v-for="item in nav"
          :key="item.name"
          :to="item.path"
          class="relative text-sm font-medium text-neutral-700 hover:text-neutral-900 transition after:content-[''] after:absolute after:-bottom-1 after:left-0 after:h-0.5 after:w-0 after:bg-amber-500 after:transition-all hover:after:w-full"
        >
          {{ item.name }}
        </RouterLink>
      </nav>

      <div class="flex items-center gap-2 sm:gap-3">
        <button class="p-2 rounded-full hover:bg-neutral-100 transition">
          <Search class="w-5 h-5" />
        </button>

        <button class="p-2 rounded-full hover:bg-neutral-100 transition relative">
          <ShoppingBagIcon class="w-5 h-5" />
          <span
            class="absolute -top-0.5 -right-0.5 bg-amber-500 text-white text-[10px] font-bold rounded-full w-4 h-4 flex items-center justify-center"
          >
            {{ cartCount }}
          </span>
        </button>

        <button class="p-2 rounded-full hover:bg-neutral-100 transition hidden sm:inline-flex">
          <User class="w-5 h-5" />
        </button>

        <button
          @click="mobileOpen = !mobileOpen"
          class="lg:hidden p-2 rounded-full hover:bg-neutral-100 transition"
        >
          <component :is="mobileOpen ? XIcon : MenuIcon" class="w-5 h-5" />
        </button>
      </div>
    </div>

    <transition name="fade">
      <div v-if="mobileOpen" class="lg:hidden bg-white border-t border-neutral-200">
        <div class="px-6 py-4 flex flex-col gap-3">
          <RouterLink
            v-for="item in nav"
            :key="item.name"
            :to="item.path"
            @click="mobileOpen = false"
            class="py-2 text-neutral-700 font-medium hover:text-amber-500 transition"
          >
            {{ item.name }}
          </RouterLink>
        </div>
      </div>
    </transition>
  </header>

  <div class="pt-24 min-h-screen bg-neutral-100">
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pb-24 bg-white rounded-3xl shadow-md">
      <div
        class="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-8 pb-6 border-b border-gray-200"
      >
        <div class="flex flex-wrap gap-2">
          <button
            @click="selectedCategory = 'all'"
            :class="[
              selectedCategory === 'all'
                ? 'bg-indigo-600 text-white shadow-md'
                : 'bg-white text-gray-600 hover:bg-gray-100',
              'px-4 py-2 rounded-xl text-sm font-medium transition',
            ]"
          >
            Tất Cả
          </button>
          <button
            v-for="cat in categories"
            :key="cat"
            @click="selectedCategory = cat"
            :class="[
              selectedCategory === cat
                ? 'bg-indigo-600 text-white shadow-md'
                : 'bg-white text-gray-600 hover:bg-gray-100',
              'px-4 py-2 rounded-xl text-sm font-medium capitalize transition',
            ]"
          >
            {{ cat }}
          </button>
        </div>

        <div class="text-sm text-gray-500 font-medium">
          Hiển thị {{ filteredProducts.length }} sản phẩm
        </div>
      </div>

      <!-- DANH SÁCH SẢN PHẨM (ĐÃ ĐƯỢC THIẾT KẾ LẠI CAO CẤP) -->

      <div
        class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4"
      >
        <div
          v-for="product in filteredProducts"
          :key="product.id"
          class="group bg-neutral-50/60 rounded-2xl p-2.5 border border-neutral-200/60 hover:border-indigo-200 hover:bg-white shadow-sm hover:shadow-[0_16px_32px_-12px_rgba(79,70,229,0.1)] transition-all duration-400 flex flex-col relative hover:-translate-y-1"
        >
          <div class="relative bg-neutral-200/40 rounded-xl pt-[100%] overflow-hidden block w-full">
            <RouterLink :to="`/product/${product.id}`" class="absolute inset-0 z-10">
              <img
                :src="getImageUrl(product.images?.[0] || product.link)"
                :alt="product.tenSanPham"
                class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500 ease-out"
              />
            </RouterLink>

            <button
              @click.stop="toggleWishlist(product.id)"
              class="absolute top-2 right-2 z-20 bg-white/90 backdrop-blur-md p-1.5 rounded-lg shadow-sm text-neutral-400 hover:text-red-500 transition-colors duration-300"
              :class="{ '!text-red-500 !bg-white': wishlist.includes(product.id) }"
            >
              <HeartIcon
                class="w-3.5 h-3.5"
                :fill="wishlist.includes(product.id) ? 'currentColor' : 'none'"
              />
            </button>

            <div
              class="absolute inset-x-0 bottom-0 p-2 translate-y-full group-hover:translate-y-0 transition-transform duration-300 ease-out z-20 hidden sm:block"
            >
              <button
                @click.stop="openQuickView(product)"
                class="w-full bg-neutral-900/90 backdrop-blur-sm text-white font-medium py-1 rounded-md shadow hover:bg-indigo-600 transition-colors duration-300 text-[11px] flex items-center justify-center gap-1"
              >
                <EyeIcon class="w-3 h-3" />
                Xem nhanh
              </button>
            </div>
          </div>

          <div class="pt-2.5 px-0.5 flex-1 flex flex-col justify-between">
            <div class="space-y-1">
              <div class="flex flex-wrap gap-1">
                <span
                  class="text-[8px] uppercase font-bold tracking-wider px-1.5 py-0.5 rounded bg-indigo-50 text-indigo-600"
                >
                  {{ product.tenMauSac }}
                </span>
                <span
                  class="text-[8px] uppercase font-bold tracking-wider px-1.5 py-0.5 rounded bg-neutral-200/60 text-neutral-600"
                >
                  {{ product.tenKichThuoc }}
                </span>
              </div>

              <RouterLink :to="`/product/${product.id}`" class="block group/title">
                <h3
                  class="font-bold text-neutral-800 text-xs md:text-sm line-clamp-2 group-hover/title:text-indigo-600 transition-colors duration-200 leading-tight min-h-[2rem]"
                >
                  {{ product.tenSanPham }}
                </h3>
              </RouterLink>

              <div class="text-sm md:text-base font-black text-neutral-900 tracking-tight">
                {{ formatPrice(product.giaBan) }}
              </div>
            </div>

            <div class="flex items-center justify-between mt-2.5 pt-2 border-t border-neutral-100">
              <span class="text-[10px] text-neutral-400">
                Kho: <span class="font-medium text-neutral-600">{{ product.soLuongTon }}</span>
              </span>

              <button
                @click="addToCart(product)"
                class="bg-neutral-100 text-neutral-700 hover:bg-indigo-600 hover:text-white p-1.5 rounded-lg transition-all duration-200 active:scale-95 flex items-center justify-center group/btn"
                title="Thêm vào giỏ hàng"
              >
                <ShoppingBagIcon
                  class="w-3.5 h-3.5 transition-transform group-hover/btn:scale-105"
                />
              </button>
            </div>
          </div>
        </div>
      </div>
    </main>

    <div
      v-if="selectedProduct"
      class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm transition-opacity"
      @click.self="closeQuickView"
    >
      <div
        class="bg-white rounded-3xl max-w-3xl w-full overflow-hidden shadow-2xl relative flex flex-col md:flex-row animate-fade-in"
      >
        <button
          @click="closeQuickView"
          class="absolute top-4 right-4 z-10 bg-white/80 hover:bg-white p-2 rounded-full shadow border border-gray-100 text-gray-500 hover:text-gray-800 transition"
        >
          <XIcon class="w-5 h-5" />
        </button>

        <div
          class="md:w-1/2 bg-gray-50 flex items-center justify-center p-6 relative min-h-[300px]"
        >
          <img
            :src="getImageUrl(selectedProduct.images?.[0] || selectedProduct.image)"
            :alt="selectedProduct.tenSanPham"
            class="absolute inset-0 w-full h-full object-cover"
          />
        </div>

        <div class="md:w-1/2 p-8 flex flex-col justify-between">
          <div>
            <span class="text-xs font-bold text-indigo-600 uppercase tracking-widest">
              {{ selectedProduct.tenMauSac }} - {{ selectedProduct.tenKichThuoc }}
            </span>
            <h2 class="text-2xl font-extrabold text-gray-900 mt-2 mb-3">
              {{ selectedProduct.tenSanPham }}
            </h2>

            <div class="mb-6 space-y-1.5">
              <div class="text-3xl font-bold text-red-500 mb-3">
                {{ formatPrice(selectedProduct.giaBan) }}
              </div>
              <div class="text-sm text-gray-600">
                Mã SPCT: {{ selectedProduct.maSanPhamChiTiet }}
              </div>
              <div class="text-sm text-gray-600">Màu sắc: {{ selectedProduct.tenMauSac }}</div>
              <div class="text-sm text-gray-600">
                Kích thước: {{ selectedProduct.tenKichThuoc }}
              </div>
              <div class="text-sm text-gray-600">Tồn kho: {{ selectedProduct.soLuongTon }}</div>
            </div>
          </div>

          <button
            @click="handleAddToCart"
            class="w-full bg-indigo-600 text-white font-semibold py-4 rounded-2xl shadow-lg hover:bg-indigo-700 transition duration-300 flex items-center justify-center gap-2"
          >
            <ShoppingBagIcon class="w-5 h-5" />
            Thêm Vào Giỏ Hàng
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { getAllSanPhamChiTiet } from '@/service/SanPhamChiTiet'
import { RouterLink, useRouter } from 'vue-router'
import {
  Search,
  User,
  Menu as MenuIcon,
  ShoppingBag as ShoppingBagIcon,
  Eye as EyeIcon,
  Heart as HeartIcon,
  X as XIcon,
} from 'lucide-vue-next'

const router = useRouter()

// Navbar & UI states
const scrolled = ref(false)
const mobileOpen = ref(false)
const cartCount = ref(0)

// NAV
const nav = [
  { name: 'Home', path: '/home' },
  { name: 'Shop', path: '/san-pham' },
  { name: 'New Arrivals', path: '/new-arrivals' },
  { name: 'Collections', path: '/collections' },
  { name: 'About', path: '/about' },
]

// DATA
const products = ref([])
const categories = ref([])
const selectedCategory = ref('all')
const wishlist = ref([])
const selectedProduct = ref(null)

// SCROLL
const handleScroll = () => {
  scrolled.value = window.scrollY > 20
}

// LOAD DATA
const loadProducts = async () => {
  try {
    const response = await getAllSanPhamChiTiet()
    products.value = response

    categories.value = [...new Set(response.map((item) => item.tenMauSac))]
  } catch (error) {
    console.error('Lỗi khi tải danh sách sản phẩm:', error)
  }
}

onMounted(() => {
  loadProducts()
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})

// FILTER
const normalize = (str) => {
  if (!str) return ''
  return str.trim().toLowerCase()
}

const filteredProducts = computed(() => {
  if (selectedCategory.value === 'all') return products.value

  return products.value.filter(
    (item) => normalize(item.tenMauSac) === normalize(selectedCategory.value),
  )
})

// PRICE
const formatPrice = (value) => {
  if (!value) return '0 đ'
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
  }).format(value)
}

// IMAGE
const getImageUrl = (path) => {
  if (!path) return ''
  if (path.startsWith('http')) return path
  return `http://localhost:8080${path}`
}

// WISHLIST
const toggleWishlist = (id) => {
  if (wishlist.value.includes(id)) {
    wishlist.value = wishlist.value.filter((x) => x !== id)
  } else {
    wishlist.value.push(id)
  }
}

// 👇 CHỈ SỬA Ở ĐÂY (QUAN TRỌNG NHẤT)
const openProduct = (id) => {
  router.push(`/product/${id}`)
}

// QUICK VIEW (GIỮ NGUYÊN)
const openQuickView = (product) => {
  selectedProduct.value = product
}

const closeQuickView = () => {
  selectedProduct.value = null
}

const addToCart = (product) => {
  cartCount.value++
  alert(`Đã thêm ${product.tenSanPham} vào giỏ hàng`)
}
</script>
<style scoped>
/* Thêm hiệu ứng fade mượt mà cho transition mobile menu */
.fade-enter-active,
.fade-leave-active {
  transition:
    opacity 0.2s ease,
    transform 0.2s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* Hiệu ứng zoom nhẹ cho modal quickview khi xuất hiện */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: scale(0.95);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}
.animate-fade-in {
  animation: fadeIn 0.3s ease-out forwards;
}
</style>
