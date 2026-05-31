<template>
  <div>
    <!-- NAVBAR -->
    <header
      :class="[
        'fixed top-0 inset-x-0 z-50 transition-all duration-300',
        scrolled
          ? 'bg-neutral-50/95 backdrop-blur-md shadow-md border-b border-neutral-200'
          : 'bg-neutral-100/90 backdrop-blur-md',
      ]"
    >
      <div class="max-w-7xl mx-auto px-5 lg:px-8 h-16 lg:h-20 flex items-center justify-between">
        <a href="#" class="text-2xl font-extrabold tracking-tight text-neutral-900">
          Vel<span class="text-amber-500">o</span>ra
        </a>
      </div>
    </header>

    <!-- LOADING -->
    <div v-if="loading" class="pt-32 text-center text-gray-500">Đang tải sản phẩm...</div>

    <!-- NOT FOUND -->
    <div v-else-if="!product" class="pt-32 text-center text-red-500">Không tìm thấy sản phẩm</div>

    <!-- MAIN -->
    <div v-else class="min-h-screen bg-neutral-50 pt-28 pb-16">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <!-- PRODUCT GRID -->
        <div
          class="grid grid-cols-1 lg:grid-cols-12 gap-10 bg-white p-8 rounded-3xl shadow-sm border"
        >
          <!-- LEFT IMAGE -->
          <div class="lg:col-span-5 space-y-4">
            <div class="aspect-square bg-neutral-100 rounded-2xl overflow-hidden">
              <img
                :src="selectedImage || getImageUrl(product.images?.[0])"
                class="w-full h-full object-cover"
              />
            </div>
          </div>

          <!-- RIGHT INFO -->
          <div class="lg:col-span-7 space-y-6">
            <h1 class="text-3xl font-bold">
              {{ product.tenSanPham }}
            </h1>

            <div class="bg-gray-50 p-5 rounded-xl">
              <div class="text-3xl font-black text-red-600">
                {{ formatPrice(product.giaBan) }}
              </div>
            </div>

            <!-- COLOR -->
            <div class="flex gap-3 flex-wrap">
              <button
                v-for="c in product.danhSachMau || []"
                :key="c.ten"
                @click="selectedColor = c.ten"
                class="px-4 py-2 border rounded-xl"
                :class="selectedColor === c.ten ? 'bg-indigo-600 text-white' : 'bg-white'"
              >
                {{ c.ten }}
              </button>
            </div>

            <!-- SIZE -->
            <div class="flex gap-2 flex-wrap">
              <button
                v-for="s in product.danhSachSize || []"
                :key="s"
                @click="selectedSize = s"
                class="px-4 py-2 border rounded-xl"
                :class="selectedSize === s ? 'bg-black text-white' : 'bg-white'"
              >
                {{ s }}
              </button>
            </div>

            <!-- QUANTITY -->
            <div class="flex items-center gap-4">
              <button @click="giamSoLuong">-</button>
              <input v-model.number="soLuong" class="w-12 text-center border" />
              <button @click="tangSoLuong">+</button>
            </div>

            <!-- BUTTON -->
            <div class="flex gap-4">
              <button
                @click="handleThemVaoGio"
                class="bg-indigo-600 text-white px-6 py-3 rounded-xl"
              >
                Thêm vào giỏ
              </button>

              <button @click="handleMuaNgay" class="bg-black text-white px-6 py-3 rounded-xl">
                Mua ngay
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getSanPhamChiTietById } from '@/service/SanPhamChiTiet'

// ======================
const route = useRoute()

const product = ref(null)
const loading = ref(true)

const selectedImage = ref(null)
const selectedColor = ref(null)
const selectedSize = ref(null)
const soLuong = ref(1)

const scrolled = ref(true)

// ======================
const loadProduct = async (id) => {
  loading.value = true
  product.value = null

  try {
    const res = await getSanPhamChiTietById(id)

    if (!res) {
      product.value = null
      return
    }

    product.value = res
    selectedImage.value = getImageUrl(res.images?.[0])
    selectedImage.value = null
    selectedColor.value = null
    selectedSize.value = null
    soLuong.value = 1
  } catch (e) {
    console.error('API error:', e)
    product.value = null
  } finally {
    loading.value = false
  }
}

// ======================
onMounted(() => {
  loadProduct(route.params.id)
})

watch(
  () => route.params.id,
  (newId) => {
    if (newId) {
      loadProduct(newId)
    }
  },
)

// ======================
const tangSoLuong = () => {
  soLuong.value++
}

const giamSoLuong = () => {
  if (soLuong.value > 1) soLuong.value--
}

const formatPrice = (value) => {
  if (!value) return '0 đ'
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
  }).format(value)
}

const getImageUrl = (path) => {
  if (!path) return 'https://placehold.co/600x600'
  if (path.startsWith('http')) return path
  return `http://localhost:8080${path}`
}

const kiemTra = () => {
  if (!selectedColor.value) return alert('Chọn màu')
  if (!selectedSize.value) return alert('Chọn size')
  return true
}

const handleThemVaoGio = () => {
  if (!kiemTra()) return
  alert('Đã thêm giỏ hàng')
}

const handleMuaNgay = () => {
  if (!kiemTra()) return
  alert('Thanh toán')
}
</script>
