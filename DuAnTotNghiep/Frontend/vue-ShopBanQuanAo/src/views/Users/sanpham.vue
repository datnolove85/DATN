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
          class="relative text-sm font-medium text-neutral-700 hover:text-neutral-900 transition"
        >
          {{ item.name }}
        </RouterLink>
      </nav>

      <div class="flex items-center gap-3">
        <button class="p-2 rounded-full hover:bg-neutral-100">
          <Search class="w-5 h-5" />
        </button>

        <button @click="cartOpen = true" class="p-2 rounded-full hover:bg-neutral-100 relative">
          <ShoppingBagIcon class="w-5 h-5" />
          <span
            v-if="cartCount > 0"
            class="absolute -top-1 -right-1 bg-amber-500 text-white text-xs w-4 h-4 rounded-full flex items-center justify-center"
          >
            {{ cartCount }}
          </span>
        </button>

        <button class="p-2 rounded-full hover:bg-neutral-100 hidden sm:inline-flex">
          <User class="w-5 h-5" />
        </button>

        <button @click="mobileOpen = !mobileOpen" class="lg:hidden p-2">
          <component :is="mobileOpen ? XIcon : MenuIcon" class="w-5 h-5" />
        </button>
      </div>
    </div>

    <transition name="fade">
      <div v-if="mobileOpen" class="lg:hidden bg-white border-t">
        <div class="px-6 py-4 flex flex-col gap-3">
          <RouterLink
            v-for="item in nav"
            :key="item.name"
            :to="item.path"
            @click="mobileOpen = false"
            class="text-neutral-700"
          >
            {{ item.name }}
          </RouterLink>
        </div>
      </div>
    </transition>
  </header>

  <div class="pt-24 min-h-screen bg-neutral-100">
    <main class="max-w-7xl mx-auto px-4 pb-24 bg-white rounded-3xl shadow-md">
      <div class="flex flex-wrap justify-between items-center gap-4 mb-6 border-b pb-6">
        <div class="flex flex-wrap gap-2">
          <button
            v-for="cat in categories"
            :key="cat.id"
            @click="selectedCategory = cat.id"
            :class="[
              selectedCategory === cat.id
                ? 'bg-indigo-600 text-white shadow-md'
                : 'bg-white text-gray-600 hover:bg-gray-100',
              'px-4 py-2 rounded-xl text-sm font-medium transition',
            ]"
          >
            {{ cat.tenDanhMuc }}
          </button>
        </div>

        <div class="text-sm text-gray-500">Hiển thị {{ filteredProducts.length }} sản phẩm</div>
      </div>

      <div
        class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4"
      >
        <div
          v-for="product in filteredProducts"
          :key="product.id"
          class="group bg-neutral-50 rounded-2xl border p-2 hover:shadow-lg transition"
        >
          <div class="relative pt-[100%] overflow-hidden rounded-xl">
            <RouterLink :to="`/product/${product.id}`" class="absolute inset-0">
              <div class="relative pt-[100%] overflow-hidden rounded-xl bg-neutral-200">
                <img
                  v-if="getImageUrl(product.images)"
                  :src="getImageUrl(product.images)"
                  class="absolute inset-0 w-full h-full object-cover group-hover:scale-105 transition"
                />

                <div
                  v-else
                  class="absolute inset-0 flex flex-col items-center justify-center text-neutral-400"
                >
                  <ShoppingBagIcon class="w-10 h-10 mb-2 opacity-50" />
                  <span class="text-xs font-medium">Chưa có ảnh</span>
                </div>
              </div>
            </RouterLink>

            <button
              @click.stop="toggleWishlist(product.id)"
              class="absolute top-2 right-2 bg-white p-1.5 rounded-lg shadow-sm hover:bg-neutral-50 transition z-10"
            >
              <HeartIcon class="w-4 h-4" :fill="wishlist.includes(product.id) ? 'red' : 'none'" />
            </button>

            <div
              class="absolute bottom-0 inset-x-0 flex translate-y-full group-hover:translate-y-0 transition duration-300 z-10"
            >
              <button
                @click.stop="openQuickView(product)"
                class="flex-1 bg-black/70 hover:bg-black/90 text-white text-xs py-2 font-medium border-r border-white/10 transition"
              >
                Xem nhanh
              </button>

              <button
                @click.stop="addToCart(product)"
                class="bg-indigo-600 hover:bg-indigo-700 text-white px-3 py-2 flex items-center justify-center transition"
                title="Thêm nhanh vào giỏ hàng"
              >
                <ShoppingBagIcon class="w-4 h-4" />
              </button>
            </div>
          </div>

          <div class="p-2 text-center">
            <RouterLink :to="`/product/${product.id}`">
              <h3
                class="font-semibold text-sm line-clamp-2 text-neutral-800 hover:text-indigo-600 transition mx-auto"
              >
                {{ product.tenSanPham }}
              </h3>
            </RouterLink>

            <div class="font-bold text-indigo-600 mt-0.5">
              {{ formatPrice(product.giaBan) }}
            </div>
          </div>
        </div>
      </div>
    </main>

    <div
      v-if="selectedProduct"
      class="fixed inset-0 bg-black/60 flex items-center justify-center z-50 p-4"
      @click.self="closeQuickView"
    >
      <div
        class="bg-white rounded-3xl p-6 w-[700px] max-w-full shadow-2xl relative animate-fade-in text-left flex flex-col md:flex-row gap-6"
      >
        <button
          @click="closeQuickView"
          class="absolute top-4 right-4 text-gray-400 hover:text-gray-600 transition z-10"
        >
          <XIcon class="w-6 h-6" />
        </button>

        <div class="w-full md:w-1/2 flex items-center justify-center">
          <img
            :src="getImageUrl(selectedProduct.images)"
            class="w-full h-64 md:h-80 object-cover rounded-2xl shadow-inner"
          />
        </div>

        <div class="w-full md:w-1/2 flex flex-col justify-between">
          <div>
            <h2 class="text-xl font-bold text-neutral-800 pr-6 line-clamp-2">
              {{ selectedProduct.tenSanPham }}
            </h2>

            <div class="text-indigo-600 font-extrabold text-2xl mt-2">
              {{ formatPrice(selectedProduct.giaBan) }}
            </div>

            <hr class="my-4 border-neutral-100" />

            <div class="space-y-2.5 text-sm">
              <div class="flex items-center gap-2">
                <span class="text-neutral-400 min-w-[90px]">Thương hiệu:</span>
                <span class="font-medium text-neutral-800 bg-neutral-100 px-2.5 py-1 rounded-md">
                  {{ selectedProduct.tenThuongHieu || 'Đang cập nhật' }}
                </span>
              </div>

              <div class="flex items-center gap-2">
                <span class="text-neutral-400 min-w-[90px]">Chất liệu:</span>
                <span class="font-medium text-neutral-800 bg-neutral-100 px-2.5 py-1 rounded-md">
                  {{ selectedProduct.tenChatLieu || 'Đang cập nhật' }}
                </span>
              </div>
            </div>
          </div>

          <button
            @click="addToCart(selectedProduct)"
            class="w-full mt-6 bg-indigo-600 hover:bg-indigo-700 text-white py-3 rounded-xl font-medium transition shadow-md hover:shadow-lg flex items-center justify-center gap-2"
          >
            <ShoppingBagIcon class="w-5 h-5" />
            Thêm vào giỏ hàng
          </button>
        </div>
      </div>
    </div>

    <div v-if="cartOpen" class="fixed inset-0 z-50 overflow-hidden">
      <div class="absolute inset-0 bg-black/50 transition-opacity" @click="cartOpen = false"></div>

      <div class="absolute inset-y-0 right-0 pl-10 max-w-full flex">
        <div class="w-screen max-w-md bg-white shadow-xl flex flex-col">
          <div class="p-6 border-b flex items-center justify-between">
            <h2 class="text-lg font-bold text-neutral-900 flex items-center gap-2">
              <ShoppingBagIcon class="w-5 h-5 text-indigo-600" />
              Giỏ hàng ({{ cartCount }})
            </h2>
            <button @click="cartOpen = false" class="text-neutral-400 hover:text-neutral-500">
              <XIcon class="w-6 h-6" />
            </button>
          </div>

          <div
            v-if="cartItems.length > 0"
            class="px-6 py-2 bg-neutral-50 border-b flex items-center justify-between text-sm"
          >
            <label
              class="flex items-center gap-2 cursor-pointer select-none font-medium text-neutral-600"
            >
              <input
                type="checkbox"
                :checked="isAllSelected"
                @change="toggleSelectAll"
                class="w-4 h-4 rounded text-indigo-600 focus:ring-indigo-500 border-neutral-300 transition"
              />
              Chọn tất cả
            </label>

            <button
              v-if="selectedCartIds.length > 0"
              @click="removeSelectedItems"
              class="text-red-500 hover:text-red-700 font-semibold transition flex items-center gap-1"
            >
              Xóa đã chọn ({{ selectedCartIds.length }})
            </button>
          </div>

          <div class="flex-1 overflow-y-auto p-6 space-y-4">
            <div v-if="cartItems.length === 0" class="text-center py-12 text-neutral-400">
              <ShoppingBagIcon class="w-12 h-12 mx-auto mb-3 opacity-20" />
              <p>Giỏ hàng của bạn đang trống.</p>
            </div>

            <div
              v-for="item in cartItems"
              :key="item.id"
              class="flex items-center gap-3 bg-neutral-50 p-3 rounded-xl border border-neutral-100"
            >
              <input
                type="checkbox"
                :value="item.id"
                v-model="selectedCartIds"
                class="w-4 h-4 rounded text-indigo-600 focus:ring-indigo-500 border-neutral-300 cursor-pointer transition flex-shrink-0"
              />

              <img
                :src="getImageUrl(item.images)"
                class="w-16 h-16 object-cover rounded-lg border bg-white flex-shrink-0"
              />
              <div class="flex-1 min-w-0">
                <h4 class="text-sm font-semibold text-neutral-800 truncate">
                  {{ item.tenSanPham }}
                </h4>
                <div class="text-xs text-neutral-400 mt-0.5">
                  SL: {{ item.quantity }} x
                  <span class="text-indigo-600 font-medium">{{ formatPrice(item.giaBan) }}</span>
                </div>
              </div>
            </div>
          </div>

          <div v-if="cartItems.length > 0" class="border-t p-6 bg-neutral-50 space-y-4">
            <div class="flex justify-between text-base font-bold text-neutral-900">
              <span>Tổng tiền:</span>
              <span class="text-indigo-600 text-lg">{{ formatPrice(cartTotal) }}</span>
            </div>
            <div class="grid grid-cols-2 gap-3">
              <button
                @click="cartOpen = false"
                class="w-full bg-white border border-neutral-200 text-neutral-700 py-3 rounded-xl font-medium text-sm hover:bg-neutral-100 transition"
              >
                Mua tiếp
              </button>
              <button
                @click="goToCheckout"
                class="w-full bg-indigo-600 text-white py-3 rounded-xl font-medium text-sm hover:bg-indigo-700 transition shadow-md"
              >
                Thanh toán
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { getAllSanPhamChiTiet } from '@/service/SanPhamChiTiet'
import { getAllDanhMuc } from '@/service/DanhMucService'
import { RouterLink, useRouter } from 'vue-router'
import {
  Search,
  User,
  Menu as MenuIcon,
  ShoppingBag as ShoppingBagIcon,
  Heart as HeartIcon,
  X as XIcon,
} from 'lucide-vue-next'

const router = useRouter()

const searchKeyword = ref('')

const nav = ref([
  { name: 'Trang chủ', path: '/' },
  { name: 'Sản phẩm', path: '/' },
])

// ===================== UI STATE =====================
const scrolled = ref(false)
const mobileOpen = ref(false)
const cartOpen = ref(false)

// ===================== DATA =====================
const products = ref([])
const categories = ref([])
const selectedCategory = ref('all')
const wishlist = ref([])
const selectedProduct = ref(null)

// Mảng chứa sản phẩm giỏ hàng và mảng chứa ID các sản phẩm được tick chọn
const cartItems = ref([])
const selectedCartIds = ref([])

// ===================== SCROLL =====================
const handleScroll = () => {
  scrolled.value = window.scrollY > 20
}

// ===================== LOAD DATA =====================
const loadProducts = async () => {
  try {
    const response = await getAllSanPhamChiTiet()
    products.value = response
  } catch (error) {
    console.error('Lỗi load sản phẩm:', error)
  }
}

const loadCategories = async () => {
  try {
    const res = await getAllDanhMuc() // Giả sử API trả về { id, tenDanhMuc }
    categories.value = [{ id: 'all', tenDanhMuc: 'Tất cả' }, ...res]
  } catch (error) {
    console.error('Lỗi load danh mục:', error)
  }
}

onMounted(() => {
  loadProducts()
  loadCategories()
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})

// ===================== FILTER & FORMAT =====================
const filteredProducts = computed(() => {
  if (selectedCategory.value === 'all') return products.value
  // So sánh dựa trên tên danh mục (vì API không trả về idDanhMuc)
  return products.value.filter((item) => item.tenDanhMuc === selectedCategory.value)
})

const formatPrice = (value) => {
  if (!value) return '0 đ'
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
  }).format(value)
}

const getImageUrl = (images) => {
  if (!images || images.length === 0) return null // Trả về null nếu không có ảnh
  const path = images[0]
  return path.startsWith('http') ? path : `http://localhost:8080${path}`
}
// ===================== WISHLIST =====================
const toggleWishlist = (id) => {
  if (wishlist.value.includes(id)) {
    wishlist.value = wishlist.value.filter((x) => x !== id)
  } else {
    wishlist.value.push(id)
  }
}

// ===================== QUICK VIEW =====================
const openQuickView = (product) => {
  selectedProduct.value = product
}

const closeQuickView = () => {
  selectedProduct.value = null
}

// ===================== CART LOGIC =====================
const cartCount = computed(() => {
  return cartItems.value.reduce((total, item) => total + item.quantity, 0)
})

const cartTotal = computed(() => {
  return cartItems.value.reduce((total, item) => total + item.giaBan * item.quantity, 0)
})

// Kiểm tra xem tất cả các mặt hàng đã được tick chọn chưa
const isAllSelected = computed(() => {
  return cartItems.value.length > 0 && selectedCartIds.value.length === cartItems.value.length
})

// Hàm thêm sản phẩm vào giỏ (Không tự động mở Sidebar)
const addToCart = (product) => {
  const existingItem = cartItems.value.find((item) => item.id === product.id)

  if (existingItem) {
    existingItem.quantity++
  } else {
    cartItems.value.push({
      ...product,
      quantity: 1,
    })
  }
}

// Xử lý sự kiện "Chọn tất cả" hoặc "Bỏ chọn tất cả"
const toggleSelectAll = (e) => {
  if (e.target.checked) {
    selectedCartIds.value = cartItems.value.map((item) => item.id)
  } else {
    selectedCartIds.value = []
  }
}

// Xóa toàn bộ các sản phẩm đã được tick chọn
const removeSelectedItems = () => {
  if (confirm(`Bạn có chắc chắn muốn xóa ${selectedCartIds.value.length} sản phẩm đã chọn?`)) {
    // Lọc lại giỏ hàng, giữ lại các sản phẩm KHÔNG nằm trong mảng ID được chọn
    cartItems.value = cartItems.value.filter((item) => !selectedCartIds.value.includes(item.id))
    // Xóa xong thì reset lại mảng ID được chọn về rỗng
    selectedCartIds.value = []
  }
}

const goToCheckout = () => {
  cartOpen.value = false
  router.push('/checkout')
}
</script>

<style scoped>
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
