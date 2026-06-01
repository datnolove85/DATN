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
      <RouterLink to="/" class="text-2xl font-extrabold tracking-tight text-neutral-950">
        Vel<span class="text-amber-500">o</span>ra
      </RouterLink>

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

        <button
          :class="[
            'p-2 rounded-full hover:bg-neutral-100 relative transition-transform duration-300',
            isCartBouncing ? 'scale-110 text-amber-500 animate-bounce-short' : '',
          ]"
        >
          <ShoppingBagIcon class="w-5 h-5" />
          <span
            v-if="cartCount > 0"
            :class="[
              'absolute -top-1 -right-1 bg-amber-500 text-white text-xs w-4 h-4 rounded-full flex items-center justify-center font-bold transition-all duration-300',
              isCartBouncing ? 'scale-125 bg-red-500' : '',
            ]"
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

  <div
    class="min-h-screen bg-neutral-50 text-neutral-800 antialiased pt-24 pb-12 font-sans selection:bg-[#fbebed]"
  >
    <div class="max-w-6xl mx-auto px-4">
      <div class="flex items-center gap-2 text-xs text-neutral-500 mb-3 px-1">
        <RouterLink to="/" class="hover:text-[#ee4d2d] transition"> Velora </RouterLink>
        <span class="text-neutral-300">/</span>

        <button
          @click="quayLaiTrangTruoc"
          class="hover:text-[#ee4d2d] transition cursor-pointer outline-none font-normal"
        >
          Thời Trang Nam/Nữ
        </button>

        <span class="text-neutral-300">/</span>
        <span class="text-neutral-400 font-normal">Chi tiết sản phẩm</span>
      </div>

      <div
        v-if="loading"
        class="bg-white rounded border border-neutral-100 p-12 flex items-center justify-center min-h-[350px]"
      >
        <div class="text-center space-y-2.5">
          <div
            class="w-8 h-8 border-2 border-[#ee4d2d] border-t-transparent rounded-full animate-spin mx-auto"
          ></div>
          <p class="text-xs text-neutral-400">Đang tải sản phẩm từ hệ thống...</p>
        </div>
      </div>

      <div
        v-if="!loading && !product"
        class="bg-white rounded border border-neutral-100 p-12 flex items-center justify-center min-h-[350px]"
      >
        <div class="text-center max-w-xs">
          <div class="text-3xl mb-2">🔍</div>
          <h3 class="text-sm font-semibold text-neutral-800 mb-1">Sản phẩm không tồn tại</h3>
          <p class="text-xs text-neutral-400 mb-4 leading-normal">
            Có vẻ sản phẩm đã bị xóa hoặc đường dẫn bị lỗi.
          </p>
          <RouterLink
            to="/"
            class="inline-block bg-[#ee4d2d] text-white text-xs font-medium px-5 py-2 rounded hover:bg-[#d73211] transition shadow-sm"
          >
            Quay lại Cửa Hàng
          </RouterLink>
        </div>
      </div>

      <div
        v-if="!loading && product"
        class="bg-white rounded border border-neutral-100 grid grid-cols-1 md:grid-cols-12 gap-6 p-4 md:p-5"
      >
        <div class="md:col-span-5 space-y-2.5">
          <div
            class="aspect-square bg-neutral-50 rounded overflow-hidden relative group border border-neutral-100"
          >
            <img
              :src="selectedImage"
              class="w-full h-full object-cover transition duration-300 group-hover:scale-105"
              alt="Product Main"
            />
            <span
              class="absolute top-2 left-0 bg-[#ee4d2d] text-white text-[10px] font-bold px-2 py-0.5 rounded-r shadow-sm"
            >
              Yêu Thích+
            </span>
          </div>

          <div
            v-if="product.images && product.images.length > 0"
            class="flex gap-2 overflow-x-auto pb-1 scrollbar-thin"
          >
            <div
              v-for="(img, idx) in product.images"
              :key="idx"
              @mouseenter="selectedImage = getImageUrl(img)"
              :class="[
                'w-16 h-16 rounded overflow-hidden border-2 flex-shrink-0 cursor-pointer transition-all',
                selectedImage === getImageUrl(img)
                  ? 'border-[#ee4d2d]'
                  : 'border-neutral-100 hover:border-neutral-300',
              ]"
            >
              <img :src="getImageUrl(img)" class="w-full h-full object-cover" />
            </div>
          </div>

          <div
            class="flex items-center justify-between pt-2.5 text-xs text-neutral-400 border-t border-neutral-50 px-1"
          >
            <div class="flex items-center gap-2">
              <span>Chia sẻ:</span>
              <button
                class="w-5 h-5 rounded-full bg-[#3b5998] text-white flex items-center justify-center font-bold hover:opacity-90"
              >
                f
              </button>
              <button
                class="w-5 h-5 rounded-full bg-[#00f2fe] text-white flex items-center justify-center font-bold hover:opacity-90"
              >
                t
              </button>
            </div>
            <button
              @click="isLiked = !isLiked"
              class="flex items-center gap-1 hover:text-neutral-600 transition"
            >
              <span>{{ isLiked ? '❤️' : '🤍' }}</span>
              <span :class="isLiked ? 'text-[#ee4d2d] font-medium' : ''"
                >Đã thích ({{ isLiked ? 129 : 128 }})</span
              >
            </button>
          </div>
        </div>

        <div class="md:col-span-7 flex flex-col justify-between">
          <div class="space-y-4">
            <h1 class="text-lg font-semibold text-neutral-900 leading-snug break-words">
              {{ product.tenSanPham }}
            </h1>

            <div
              class="flex items-center gap-3 text-xs text-neutral-400 bg-neutral-50/70 py-1.5 px-2.5 rounded w-fit"
            >
              <div class="flex items-center gap-1">
                <span class="text-[#ee4d2d] font-bold">{{ product.rating || '4.9' }}</span>
                <span class="text-[#ee4d2d] text-[10px]">★★★★★</span>
              </div>
              <div class="w-[1px] h-2.5 bg-neutral-200"></div>
              <div><span class="text-neutral-700 font-medium">3,1k</span> Đánh Giá</div>
              <div class="w-[1px] h-2.5 bg-neutral-200"></div>
              <div><span class="text-neutral-700 font-medium">9,4k</span> Đã Bán</div>
            </div>

            <div class="bg-neutral-50/70 p-3.5 rounded space-y-1.5">
              <div class="flex items-center gap-2.5 flex-wrap">
                <span class="text-neutral-400 line-through text-xs">
                  {{ formatPrice(product.giaBan * 1.3) }}
                </span>
                <span class="text-2xl font-bold text-[#ee4d2d]">
                  {{ formatPrice(product.giaBan) }}
                </span>
                <span
                  class="bg-[#fbebed] text-[#ee4d2d] text-[10px] font-bold px-1.5 py-0.5 rounded uppercase tracking-wider"
                >
                  -30%
                </span>
              </div>
              <div class="flex items-center gap-1.5 text-[11px] text-[#ee4d2d]">
                <span class="bg-[#ee4d2d]/10 text-[#ee4d2d] px-1 rounded text-[10px] font-medium"
                  >Gì cũng rẻ</span
                >
                <span class="text-neutral-400"
                  >Giá tốt nhất so với các sản phẩm tương tự trên thị trường!</span
                >
              </div>
            </div>

            <div class="space-y-3.5 text-xs">
              <div class="grid grid-cols-12 gap-2 items-center"></div>

              <div class="grid grid-cols-12 gap-2 items-start py-1">
                <div class="col-span-3 text-neutral-400 font-medium pt-0.5">Vận Chuyển</div>
                <div class="col-span-9 space-y-0.5">
                  <div class="flex items-center gap-1 text-emerald-600 font-medium">
                    <span class="text-sm">🚚</span> Miễn phí vận chuyển
                  </div>
                  <div class="text-neutral-400 text-[11px] pl-4">
                    Miễn phí vận chuyển cho đơn hàng từ ₫0 (Ưu đãi giới hạn)
                  </div>
                </div>
              </div>

              <div class="grid grid-cols-12 gap-2 items-center">
                <div class="col-span-3 text-neutral-400 font-medium">Màu Sắc</div>
                <div class="col-span-9 flex gap-2.5 flex-wrap">
                  <button
                    v-for="color in colors"
                    :key="color"
                    @click="selectedColor = color"
                    :class="[
                      'relative px-4 py-1.5 text-[11px] border rounded transition-all duration-200 min-w-[75px] font-medium overflow-hidden select-none btn-variant',
                      selectedColor === color
                        ? 'border-[#ee4d2d] text-[#ee4d2d] bg-[#fbebed]/30 font-semibold shadow-sm'
                        : 'border-neutral-200 text-neutral-600 bg-white hover:border-neutral-400 hover:bg-neutral-50',
                    ]"
                  >
                    {{ color }}
                    <div
                      v-if="selectedColor === color"
                      class="absolute right-0 bottom-0 w-3 h-3 bg-[#ee4d2d] flex items-center justify-center text-white text-[7px] clip-triangle"
                    >
                      <span class="absolute right-[1px] bottom-0.5 scale-75">✓</span>
                    </div>
                  </button>
                </div>
              </div>

              <div class="grid grid-cols-12 gap-2 items-center">
                <div class="col-span-3 text-neutral-400 font-medium">Kích Thước</div>
                <div class="col-span-9 flex gap-2.5 flex-wrap">
                  <button
                    v-for="size in sizes"
                    :key="size"
                    @click="selectedSize = size"
                    :class="[
                      'relative px-4 py-1.5 text-[11px] border rounded transition-all duration-200 min-w-[65px] font-medium overflow-hidden select-none btn-variant',
                      selectedSize === size
                        ? 'border-[#ee4d2d] text-[#ee4d2d] bg-[#fbebed]/30 font-semibold shadow-sm'
                        : 'border-neutral-200 text-neutral-600 bg-white hover:border-neutral-400 hover:bg-neutral-50',
                    ]"
                  >
                    {{ size }}
                    <div
                      v-if="selectedSize === size"
                      class="absolute right-0 bottom-0 w-3 h-3 bg-[#ee4d2d] flex items-center justify-center text-white text-[7px] clip-triangle"
                    >
                      <span class="absolute right-[1px] bottom-0.5 scale-75">✓</span>
                    </div>
                  </button>
                </div>
              </div>

              <div class="grid grid-cols-12 gap-2 items-center">
                <div class="col-span-3 text-neutral-400 font-medium">Số Lượng</div>
                <div class="col-span-9 flex items-center gap-3">
                  <div
                    class="inline-flex items-center border border-neutral-200 rounded overflow-hidden h-7 bg-white"
                  >
                    <button
                      @click="giamSoLuong"
                      :disabled="soLuong <= 1"
                      class="w-7 h-full flex items-center justify-center font-normal text-neutral-500 hover:bg-neutral-50 transition disabled:opacity-30"
                    >
                      −
                    </button>
                    <input
                      v-model.number="soLuong"
                      type="text"
                      @change="validateSoLuong"
                      class="w-10 text-center text-xs text-neutral-800 outline-none border-x border-neutral-100 h-full font-medium"
                    />
                    <button
                      @click="tangSoLuong"
                      :disabled="
                        soLuong >= (selectedVariant?.soLuongTon ?? product.soLuongTon ?? 1)
                      "
                      class="w-7 h-full flex items-center justify-center font-normal text-neutral-500 hover:bg-neutral-50 transition disabled:opacity-30"
                    >
                      ＋
                    </button>
                  </div>
                  <span class="text-neutral-400 text-[11px]">
                    {{ selectedVariant?.soLuongTon ?? product.soLuongTon ?? 0 }} sản phẩm có sẵn
                  </span>
                </div>
              </div>
            </div>
          </div>

          <div class="space-y-3 pt-5">
            <div class="flex flex-col sm:flex-row gap-2">
              <button
                @click="handleThemVaoGio"
                class="flex items-center justify-center gap-2 bg-[#fbebed] border border-[#ee4d2d] text-[#ee4d2d] px-5 py-3 rounded font-medium text-xs hover:bg-[#fbe4df] transition w-full sm:w-auto sm:min-w-[180px]"
              >
                <span>🛒</span> Thêm Vào Giỏ Hàng
              </button>

              <button
                @click="handleMuaNgay"
                class="bg-[#ee4d2d] text-white px-6 py-3 rounded font-medium text-xs hover:bg-[#d73211] transition w-full sm:w-auto sm:min-w-[130px] shadow-sm shadow-orange-500/10"
              >
                Mua Ngay
              </button>
            </div>

            <div class="flex items-center gap-1 text-[11px] text-neutral-400 pt-1">
              <span class="text-[#ee4d2d] text-xs">🛡️</span>
              <span class="text-neutral-600 font-medium mr-1">Velora Đảm Bảo</span>
              <span>Ba ngày trả hàng / Hoàn tiền tận nơi nhanh chóng</span>
            </div>
          </div>
        </div>
      </div>

      <div
        v-if="product"
        class="bg-white rounded border border-neutral-100 mt-4 p-4 md:p-5 space-y-5"
      >
        <div>
          <h2
            class="text-neutral-800 font-bold text-xs uppercase tracking-wider pb-2 border-b border-neutral-100"
          >
            Chi tiết sản phẩm
          </h2>
          <div class="pt-3 text-xs text-neutral-600 max-w-xl">
            <div class="grid grid-cols-12 gap-y-2.5">
              <div class="col-span-4 text-neutral-400">Danh Mục</div>
              <div class="col-span-8 text-[#ee4d2d] font-medium">
                Velora Shop &gt; Thiết kế độc quyền
              </div>

              <div class="col-span-4 text-neutral-400">Kho hàng</div>
              <div class="col-span-8 text-neutral-700 font-medium">{{ product.soLuongTon }}</div>

              <div class="col-span-4 text-neutral-400">Gửi từ</div>
              <div class="col-span-8 text-neutral-700">Quận Hai Bà Trưng, Hà Nội</div>
            </div>
          </div>
        </div>

        <div>
          <h2
            class="text-neutral-800 font-bold text-xs uppercase tracking-wider pb-2 border-b border-neutral-100"
          >
            Mô tả sản phẩm
          </h2>
          <div
            class="pt-3 text-xs text-neutral-600 leading-relaxed whitespace-pre-line font-normal"
          >
            {{
              product.moTa ||
              'Không có mô tả chi tiết cho sản phẩm này. Vui lòng liên hệ bộ phận CSKH để được tư vấn chính xác nhất.'
            }}
          </div>
        </div>
      </div>
    </div>
  </div>

  <transition name="toast-fade">
    <div
      v-if="toast.show"
      :class="[
        'fixed top-20 right-4 z-[999] flex items-center gap-2.5 px-4 py-3 rounded shadow-lg text-xs font-medium text-white transition-all',
        toast.type === 'success' ? 'bg-neutral-900 border border-neutral-800' : 'bg-amber-600',
      ]"
    >
      <span>{{ toast.type === 'success' ? '✨' : '⚠️' }}</span>
      <span>{{ toast.message }}</span>
    </div>
  </transition>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, watch, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getSanPhamChiTietById } from '@/service/SanPhamChiTiet'

// Import Icons từ lucide-vue-next
import {
  Search,
  User,
  Menu as MenuIcon,
  ShoppingBag as ShoppingBagIcon,
  X as XIcon,
} from 'lucide-vue-next'

const route = useRoute()
const router = useRouter()

// ================= HEADER =================
const scrolled = ref(false)
const mobileOpen = ref(false)

const nav = ref([
  { name: 'Trang chủ', path: '/' },
  { name: 'Sản phẩm', path: '/' },
])

const handleScroll = () => {
  scrolled.value = window.scrollY > 20
}

// ================= CART =================
const cartCount = ref(0)
const isCartBouncing = ref(false)

// Cập nhật lại số lượng badge giỏ hàng từ localStorage lúc vào trang
const updateCartCount = () => {
  const cart = JSON.parse(localStorage.getItem('cart')) || []
  cartCount.value = cart.reduce((acc, item) => acc + item.soLuong, 0)
}

// ================= DATA =================
const product = ref(null)
const loading = ref(true)

const selectedImage = ref(null)
const soLuong = ref(1)

const selectedColor = ref('')
const selectedSize = ref('')

// ================= TOAST =================
const toast = ref({ show: false, message: '', type: 'success' })
const showToast = (msg, type = 'success') => {
  toast.value = { show: true, message: msg, type }
  setTimeout(() => (toast.value.show = false), 2500)
}

// ================= VARIANTS =================
const colors = computed(() => {
  if (!product.value?.variants) return []
  return [...new Set(product.value.variants.map((v) => v.tenMauSac))]
})

const sizes = computed(() => {
  if (!product.value?.variants || !selectedColor.value) return []

  return [
    ...new Set(
      product.value.variants
        .filter((v) => v.tenMauSac === selectedColor.value)
        .map((v) => v.tenKichThuoc),
    ),
  ]
})

const selectedVariant = computed(() => {
  if (!product.value?.variants) return null

  return product.value.variants.find(
    (v) => v.tenMauSac === selectedColor.value && v.tenKichThuoc === selectedSize.value,
  )
})

// ================= LOAD PRODUCT =================
const loadProduct = async (id) => {
  loading.value = true
  product.value = null

  // reset
  selectedColor.value = ''
  selectedSize.value = ''
  selectedImage.value = null
  soLuong.value = 1

  try {
    const res = await getSanPhamChiTietById(id)
    if (!res) return

    product.value = {
      ...res,
      images: res.images?.length ? res.images : [],
    }

    // SET DEFAULT VARIANT
    const firstVariant = res.variants?.[0]

    if (firstVariant) {
      selectedColor.value = firstVariant.tenMauSac

      await nextTick()

      const firstSize = product.value.variants.find((v) => v.tenMauSac === selectedColor.value)

      selectedSize.value = firstSize?.tenKichThuoc || ''
    }

    selectedImage.value = getImageUrl(product.value.images?.[0])
  } catch (e) {
    console.log(e)
    product.value = null
  }

  loading.value = false
}

// ================= FIX COLOR CHANGE =================
watch(selectedColor, async (color) => {
  if (!product.value?.variants) return

  await nextTick()

  const first = product.value.variants.find((v) => v.tenMauSac === color)

  selectedSize.value = first?.tenKichThuoc || ''
  validateSoLuong()
})

watch(selectedSize, () => {
  validateSoLuong()
})

// ================= QUANTITY =================
const validateSoLuong = () => {
  const max = selectedVariant.value?.soLuongTon ?? product.value?.soLuongTon ?? 1

  if (soLuong.value < 1) soLuong.value = 1
  if (soLuong.value > max) soLuong.value = max
}

const tangSoLuong = () => {
  const max = selectedVariant.value?.soLuongTon ?? product.value?.soLuongTon ?? 1
  if (soLuong.value < max) soLuong.value++
}

const giamSoLuong = () => {
  if (soLuong.value > 1) soLuong.value--
}

// ================= CART LOGIC & ANIMATION =================
const handleThemVaoGio = () => {
  if (!selectedVariant.value) {
    showToast('Chọn màu + size trước', 'warning')
    return
  }

  const item = {
    id: selectedVariant.value.id,
    tenSanPham: product.value.tenSanPham,
    giaBan: product.value.giaBan,
    image: selectedImage.value,
    color: selectedColor.value,
    size: selectedSize.value,
    soLuong: soLuong.value,
  }

  const cart = JSON.parse(localStorage.getItem('cart')) || []
  const index = cart.findIndex((i) => i.id === item.id)

  if (index > -1) {
    cart[index].soLuong += item.soLuong
  } else {
    cart.push(item)
  }

  localStorage.setItem('cart', JSON.stringify(cart))

  // Kích hoạt hiệu ứng Bounce giỏ hàng giống như file 1
  isCartBouncing.value = true
  updateCartCount()

  window.dispatchEvent(new Event('cart-updated'))

  setTimeout(() => {
    isCartBouncing.value = false
  }, 600)

  showToast('Đã thêm vào giỏ hàng')
}

// ================= UTIL =================
const formatPrice = (v) =>
  new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
  }).format(v || 0)

const getImageUrl = (p) => {
  if (!p) return ''
  if (p.startsWith('http')) return p
  return `http://localhost:8080${p}`
}

// ================= LIFECYCLE =================
onMounted(() => {
  loadProduct(route.params.id)
  updateCartCount()

  window.addEventListener('scroll', handleScroll)
  window.addEventListener('cart-updated', updateCartCount)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
  window.removeEventListener('cart-updated', updateCartCount)
})

watch(
  () => route.params.id,
  (id) => id && loadProduct(id),
)

// ================= NAV =================
const quayLaiTrangTruoc = () => router.back()

const handleMuaNgay = () => {
  if (!selectedVariant.value) {
    showToast('Chọn màu + size trước', 'warning')
    return
  }

  router.push({
    name: 'buy',
    params: { id: route.params.id },
  })
}
</script>

<style scoped>
/* Đồng bộ hiệu ứng fade menu mobile từ file 1 */
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

.scrollbar-thin::-webkit-scrollbar {
  height: 3px;
}
.scrollbar-thin::-webkit-scrollbar-thumb {
  background: #e5e5e5;
  border-radius: 99px;
}
.scrollbar-thin::-webkit-scrollbar-thumb:hover {
  background: #ccc;
}

/* === TOAST ANIMATION === */
.toast-fade-enter-active {
  animation: slide-in 0.3s ease-out;
}
.toast-fade-leave-active {
  animation: slide-in 0.2s ease-in reverse;
}

@keyframes slide-in {
  0% {
    transform: translateX(100%) scale(0.9);
    opacity: 0;
  }
  70% {
    transform: translateX(-10px) scale(1.02);
  }
  100% {
    transform: translateX(0) scale(1);
    opacity: 1;
  }
}

/* === BOUNCE/SHAKE ANIMATION GIỎ HÀNG GIỐNG FILE 1 === */
.animate-bounce-short {
  animation: bounce-short 0.5s ease-in-out;
}

@keyframes bounce-short {
  0%,
  100% {
    transform: scale(1) translateY(0);
  }
  30% {
    transform: scale(1.15) translateY(-8px);
  }
  50% {
    transform: scale(0.95) translateY(2px);
  }
  75% {
    transform: scale(1.05) translateY(-2px);
  }
}

/* === NEW VARIANT EFFECT CSS === */
.clip-triangle {
  clip-path: polygon(100% 0, 0 100%, 100% 100%);
}

.btn-variant:active {
  transform: scale(0.96);
}
</style>
