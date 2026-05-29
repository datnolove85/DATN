<template>
  <div class="min-h-screen bg-gray-50 text-gray-800 font-sans">
    <div
      class="bg-gradient-to-r from-indigo-600 to-purple-600 text-white py-10 px-4 sm:px-6 lg:px-8 mb-6 rounded-b-2xl shadow"
    >
      <div class="max-w-7xl mx-auto flex items-center justify-between">
        <div class="space-y-2 max-w-lg">
          <span
            class="bg-white/15 text-white text-[10px] font-semibold px-2.5 py-1 rounded-full uppercase tracking-wider"
          >
            Bộ Sưu Tập 2026
          </span>

          <h1 class="text-2xl md:text-3xl font-bold tracking-tight leading-tight">
            Nâng Tầm Phong Cách
          </h1>

          <p class="text-indigo-100 text-sm leading-relaxed">
            Khám phá sản phẩm công nghệ và thời trang hiện đại với mức giá ưu đãi.
          </p>

          <button
            class="bg-white text-indigo-600 text-sm font-semibold px-4 py-2 rounded-lg shadow-sm hover:bg-indigo-50 transition duration-300"
          >
            Mua Ngay
          </button>
        </div>

        <div class="hidden md:block w-48 h-48 bg-white/10 rounded-full blur-3xl"></div>
      </div>
    </div>

    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pb-24">
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
              'px-4 py-2 rounded-xl text-sm font-medium text-capitalize transition',
            ]"
          >
            {{ cat }}
          </button>
        </div>

        <div class="text-sm text-gray-500 font-medium">
          Hiển thị {{ filteredProducts.length }} sản phẩm
        </div>
      </div>

      <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-8">
        <div
          v-for="product in filteredProducts"
          :key="product.id"
          class="group bg-white rounded-2xl overflow-hidden shadow-sm hover:shadow-xl transition-all duration-300 border border-gray-100 flex flex-col relative"
        >
          <span
            v-if="product.discount"
            class="absolute top-3 left-3 z-10 bg-red-500 text-white text-xs font-bold px-2.5 py-1 rounded-lg shadow-sm"
          >
            -{{ product.discount }}%
          </span>

          <div
            class="relative bg-gray-100 pt-[100%] overflow-hidden cursor-pointer"
            @click="openQuickView(product)"
          >
            <img
              :src="product.image"
              :alt="product.name"
              class="absolute inset-0 w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
            />
            <div
              class="absolute inset-0 bg-black/20 opacity-0 group-hover:opacity-100 transition-opacity duration-300 flex items-center justify-center gap-3"
            >
              <button
                @click.stop="openQuickView(product)"
                class="bg-white text-gray-900 p-3 rounded-xl shadow-md hover:bg-indigo-600 hover:text-white transition transform hover:scale-110"
                title="Xem nhanh"
              >
                <EyeIcon class="w-5 h-5" />
              </button>
              <button
                @click.stop="toggleWishlist(product.id)"
                class="bg-white p-3 rounded-xl shadow-md hover:bg-red-50 text-gray-900 transition transform hover:scale-110"
                :class="{ 'text-red-500': wishlist.includes(product.id) }"
              >
                <HeartIcon
                  class="w-5 h-5"
                  :fill="wishlist.includes(product.id) ? 'currentColor' : 'none'"
                />
              </button>
            </div>
          </div>

          <div class="p-5 flex-1 flex flex-col justify-between">
            <div>
              <p class="text-xs text-indigo-600 font-semibold tracking-wider uppercase mb-1">
                {{ product.category }}
              </p>
              <h3
                class="font-bold text-gray-900 text-base line-clamp-2 hover:text-indigo-600 transition cursor-pointer mb-2"
                @click="openQuickView(product)"
              >
                {{ product.name }}
              </h3>

              <div class="flex items-center gap-1 mb-3">
                <div class="flex text-amber-400">
                  <StarIcon
                    v-for="i in 5"
                    :key="i"
                    class="w-4 h-4"
                    :fill="i <= Math.floor(product.rating) ? 'currentColor' : 'none'"
                  />
                </div>
                <span class="text-xs text-gray-400 font-medium">({{ product.reviews }})</span>
              </div>
            </div>

            <div class="flex items-center justify-between mt-4 pt-4 border-t border-gray-50">
              <div class="flex flex-col">
                <span class="text-lg font-extrabold text-gray-900">{{
                  formatPrice(product.price)
                }}</span>
                <span
                  v-if="product.oldPrice"
                  class="text-xs text-gray-400 line-through font-medium"
                  >{{ formatPrice(product.oldPrice) }}</span
                >
              </div>
              <button
                @click="addToCart(product)"
                class="bg-gray-900 text-white hover:bg-indigo-600 p-3 rounded-xl transition duration-300 shadow-sm flex items-center justify-center"
              >
                <ShoppingBagIcon class="w-5 h-5" />
              </button>
            </div>
          </div>
        </div>
      </div>
    </main>

    <div
      v-if="selectedProduct"
      class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm transition-opacity"
    >
      <div
        class="bg-white rounded-3xl max-w-3xl w-full overflow-hidden shadow-2xl relative animate-fade-in flex flex-col md:flex-row"
      >
        <button
          @click="closeQuickView"
          class="absolute top-4 right-4 z-10 bg-white/80 hover:bg-white p-2 rounded-full shadow border border-gray-100 text-gray-500 hover:text-gray-800 transition"
        >
          <XIcon class="w-5 h-5" />
        </button>

        <div class="md:w-1/2 bg-gray-50 flex items-center justify-center p-6">
          <img
            :src="selectedProduct.image"
            :alt="selectedProduct.name"
            class="max-h-80 object-contain rounded-xl"
          />
        </div>

        <div class="md:w-1/2 p-8 flex flex-col justify-between">
          <div>
            <span class="text-xs font-bold text-indigo-600 uppercase tracking-widest">{{
              selectedProduct.category
            }}</span>
            <h2 class="text-2xl font-extrabold text-gray-900 mt-2 mb-3">
              {{ selectedProduct.name }}
            </h2>

            <div class="flex items-center gap-2 mb-4">
              <div class="flex text-amber-400">
                <StarIcon
                  v-for="i in 5"
                  :key="i"
                  class="w-4 h-4"
                  :fill="i <= Math.floor(selectedProduct.rating) ? 'currentColor' : 'none'"
                />
              </div>
              <span class="text-sm text-gray-500"
                >({{ selectedProduct.reviews }} đánh giá từ khách hàng)</span
              >
            </div>

            <div class="flex items-baseline gap-3 mb-6">
              <span class="text-3xl font-black text-gray-900">{{
                formatPrice(selectedProduct.price)
              }}</span>
              <span v-if="selectedProduct.oldPrice" class="text-base text-gray-400 line-through">{{
                formatPrice(selectedProduct.oldPrice)
              }}</span>
            </div>

            <p class="text-gray-600 text-sm leading-relaxed mb-6">
              Đây là mô tả ngắn gọn nhưng đầy đủ cho sản phẩm cao cấp này. Chất liệu bền bỉ, thiết
              kế công thái học và mang lại trải nghiệm tuyệt vời nhất cho người dùng.
            </p>
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
import { ref, computed } from 'vue'
// Import các Icon đẹp từ Lucide
import {
  Star as StarIcon,
  ShoppingBag as ShoppingBagIcon,
  Eye as EyeIcon,
  Heart as HeartIcon,
  X as XIcon,
} from 'lucide-vue-next'

const handleAddToCart = () => {
  addToCart(selectedProduct.value)
  closeQuickView()
}

// Mock Data danh sách sản phẩm mẫu cực xịn
const products = ref([
  {
    id: 1,
    name: 'Tai Nghe Không Dây Pro Max Ultra',
    category: 'Công nghệ',
    price: 3500000,
    oldPrice: 4200000,
    discount: 16,
    rating: 4.8,
    reviews: 128,
    image:
      'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500&auto=format&fit=crop&q=60',
  },
  {
    id: 2,
    name: 'Đồng Hồ Thông Minh Sport V2',
    category: 'Phụ kiện',
    price: 1990000,
    rating: 4.5,
    reviews: 84,
    image:
      'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500&auto=format&fit=crop&q=60',
  },
  {
    id: 3,
    name: 'Balo Thời Trang Chống Nước Urban',
    category: 'Thời trang',
    price: 850000,
    oldPrice: 1200000,
    discount: 29,
    rating: 4.7,
    reviews: 210,
    image:
      'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=500&auto=format&fit=crop&q=60',
  },
  {
    id: 4,
    name: 'Bàn Phím Cơ Custom RGB Không Dây',
    category: 'Công nghệ',
    price: 2450000,
    rating: 4.9,
    reviews: 65,
    image:
      'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=500&auto=format&fit=crop&q=60',
  },
])

// Bộ lọc
const categories = ['Công nghệ', 'Phụ kiện', 'Thời trang']
const selectedCategory = ref('all')
const wishlist = ref([])
const selectedProduct = ref(null)

// Computed filter sản phẩm theo danh mục
const filteredProducts = computed(() => {
  if (selectedCategory.value === 'all') return products.value
  return products.value.filter((p) => p.category === selectedCategory.value)
})

// Định dạng giá tiền VNĐ
const formatPrice = (value) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value)
}

// Xử lý yêu thích (Wishlist)
const toggleWishlist = (id) => {
  if (wishlist.value.includes(id)) {
    wishlist.value = wishlist.value.filter((item) => item !== id)
  } else {
    wishlist.value.push(id)
  }
}

// Xử lý Modal Quick View
const openQuickView = (product) => {
  selectedProduct.value = product
}
const closeQuickView = () => {
  selectedProduct.value = null
}

// Giả lập thêm vào giỏ hàng
const addToCart = (product) => {
  alert(`Đã thêm "${product.name}" vào giỏ hàng thành công!`)
}
</script>

<style scoped>
/* Hiệu ứng mượt mà cho Modal */
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
