<template>
  <div
    class="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4 backdrop-blur-sm animate-fadeIn"
    @click="$emit('close')"
  >
    <div
      class="bg-white rounded-lg max-w-3xl w-full p-6 relative shadow-2xl transition-all max-h-[90vh] overflow-y-auto"
      @click.stop
    >
      <!-- Nút đóng -->
      <button
        type="button"
        @click="$emit('close')"
        class="absolute top-4 right-4 text-gray-400 hover:text-gray-700 text-xl font-bold w-8 h-8 rounded-full bg-gray-100 flex items-center justify-center transition-all z-10"
      >
        ✕
      </button>

      <!-- Loading State -->
      <div v-if="loading" class="py-20 flex items-center justify-center">
        <div class="flex flex-col items-center gap-3 text-gray-500">
          <div
            class="w-7 h-7 border-2 border-gray-200 border-t-[#222] rounded-full animate-spin"
          ></div>
          <span class="text-sm font-medium">Đang tải thông tin sản phẩm...</span>
        </div>
      </div>

      <!-- Main Content -->
      <div v-else class="grid grid-cols-1 md:grid-cols-[1.1fr_1fr] gap-6 items-start">
        <!-- LEFT: GALLERY -->
        <div class="flex flex-col gap-3">
          <div
            class="relative bg-[#f6f6f6] rounded-[8px] overflow-hidden aspect-[4/5] flex items-center justify-center"
          >
            <span
              v-if="selectedVariant?.dangGiamGia && selectedVariant?.phanTramGiam > 0"
              class="absolute top-3 right-3 z-10 bg-[#df2633] text-white px-2 py-0.5 text-[11px] font-semibold rounded-[3px]"
            >
              -{{ selectedVariant.phanTramGiam }}%
            </span>
            <img
              v-if="mainImage"
              :src="mainImage"
              :alt="product?.productName || 'Sản phẩm'"
              class="w-full h-full object-cover"
            />
          </div>

          <!-- Thumbnails -->
          <div v-if="galleryImages.length > 0" class="flex gap-2 overflow-x-auto pb-1">
            <button
              v-for="img in galleryImages"
              :key="img.image"
              type="button"
              @click="mainImage = img.url"
              class="relative shrink-0 w-[65px] aspect-square overflow-hidden bg-[#f6f6f6] border rounded-[4px] transition-all"
              :class="
                mainImage === img.url
                  ? 'border-[#222] ring-1 ring-[#222]'
                  : 'border-gray-200 hover:border-gray-400'
              "
            >
              <img :src="img.url" alt="" class="w-full h-full object-cover" />
            </button>
          </div>
        </div>

        <!-- RIGHT: PRODUCT INFO -->
        <div class="flex flex-col justify-between h-full">
          <div>
            <h2 class="text-[22px] font-bold text-gray-900 leading-snug">
              {{ product?.productName }}
            </h2>

            <div class="mt-2 space-y-1.5 text-[13px] text-gray-600">
              <p>
                Thương hiệu:
                <span class="font-medium text-[#00a884]">{{
                  selectedVariant?.tenThuongHieu || 'Wolf Calie'
                }}</span>
              </p>
              <p>
                Mã sản phẩm:
                <span class="italic text-gray-800">{{
                  selectedVariant?.maSanPhamChiTiet || 'Đang cập nhật'
                }}</span>
              </p>
            </div>

            <!-- PRICE -->
            <div class="mt-3 pb-3 border-b border-gray-200">
              <div class="flex items-baseline gap-2.5">
                <span class="text-[26px] font-bold text-[#bd2228]">
                  {{
                    (selectedVariant?.giaSauGiam || selectedVariant?.giaBan || 0).toLocaleString(
                      'vi-VN',
                    )
                  }}đ
                </span>
                <span
                  v-if="selectedVariant?.dangGiamGia"
                  class="text-[14px] text-gray-400 line-through"
                >
                  {{ (selectedVariant?.giaBan || 0).toLocaleString('vi-VN') }}đ
                </span>
              </div>
            </div>

            <!-- COLOR -->
            <div class="mt-4">
              <div class="text-[13px] font-semibold text-gray-900 mb-2">
                Màu sắc: <span class="font-normal text-gray-700">{{ selectedColor?.name }}</span>
              </div>
              <div class="flex flex-wrap gap-2">
                <button
                  v-for="color in product?.colors"
                  :key="color.id"
                  type="button"
                  @click="selectColor(color)"
                  class="relative w-[36px] h-[36px] border rounded-[3px] transition-all"
                  :class="
                    selectedColor?.id === color.id
                      ? 'border-[#222] ring-2 ring-[#222]/10'
                      : 'border-gray-300 hover:border-gray-600'
                  "
                  :style="{ backgroundColor: getColorStyle(color.name) }"
                  :title="color.name"
                >
                  <span
                    v-if="selectedColor?.id === color.id"
                    class="absolute inset-0 flex items-center justify-center"
                  >
                    <span
                      class="w-[14px] h-[14px] rounded-full bg-white/90 flex items-center justify-center shadow text-[9px] font-bold text-gray-900"
                      >✓</span
                    >
                  </span>
                </button>
              </div>
            </div>

            <!-- SIZE -->
            <div class="mt-4">
              <div class="text-[13px] font-semibold text-gray-900 mb-2">
                Size:
                <span class="font-normal text-gray-700">{{ selectedVariant?.tenKichThuoc }}</span>
              </div>
              <div class="flex flex-wrap gap-2">
                <button
                  v-for="variant in selectedColor?.variants"
                  :key="variant.id"
                  type="button"
                  :disabled="getVariantStock(variant) === 0"
                  @click="getVariantStock(variant) > 0 && (selectedVariant = variant)"
                  class="min-w-[40px] h-[38px] px-3 border rounded-[3px] text-[13px] font-medium transition-all"
                  :class="[
                    getVariantStock(variant) === 0
                      ? 'bg-gray-100 text-gray-400 border-gray-200 line-through cursor-not-allowed'
                      : selectedVariant?.id === variant.id
                        ? 'bg-[#222] text-white border-[#222]'
                        : 'bg-white text-gray-800 border-gray-300 hover:border-[#222]',
                  ]"
                >
                  {{ variant.tenKichThuoc }}
                </button>
              </div>
            </div>

            <!-- QUANTITY -->
            <div class="mt-4 flex items-center gap-4">
              <span class="text-[13px] font-semibold text-gray-900">Số lượng:</span>
              <div
                class="flex items-center h-[36px] border border-gray-300 rounded-[3px] overflow-hidden"
              >
                <button
                  type="button"
                  @click="quantity > 1 && quantity--"
                  class="w-9 h-full text-gray-600 hover:bg-gray-50"
                >
                  −
                </button>
                <input
                  type="number"
                  v-model.number="quantity"
                  class="w-12 h-full text-center border-x border-gray-300 text-[13px] bg-transparent focus:outline-none"
                />
                <button
                  type="button"
                  @click="quantity < availableStock && quantity++"
                  class="w-9 h-full text-gray-600 hover:bg-gray-50"
                >
                  +
                </button>
              </div>
            </div>
          </div>

          <!-- ACTIONS -->
          <div class="mt-6 pt-3 border-t border-gray-100 flex gap-2.5">
            <!-- TRONG PHẦN TEMPLATE -->
            <!-- Cập nhật sự kiện click nút Thêm vào giỏ hàng -->
            <button
              type="button"
              @click="handleAddToCart($event)"
              :disabled="!selectedVariant || availableStock === 0"
              class="flex-1 py-3 bg-[#df3440] hover:bg-[#ce2d38] text-white font-semibold text-[13px] rounded-[4px] transition-all disabled:bg-gray-300 flex items-center justify-center gap-1.5 shadow-sm"
            >
              <span>🛒</span>
              <span>Thêm vào giỏ hàng</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getShopVariantsByProductId } from '@/service/SanPhamChiTiet'
import axios from 'axios'
import emitter from '@/utils/emitter'
import { flyToCart } from '@/utils/cartAnimation'

const props = defineProps({
  productId: {
    type: Number,
    required: true,
  },
})

const emit = defineEmits(['close'])
const router = useRouter()

const loading = ref(true)
const product = ref(null)
const selectedColor = ref(null)
const selectedVariant = ref(null)
const mainImage = ref('')
const quantity = ref(1)

const API_URL = 'http://localhost:8080'

const getColorStyle = (name) => {
  const lower = (name || '').toLowerCase()
  if (lower.includes('trắng') || lower.includes('white')) return '#ffffff'
  if (lower.includes('đỏ') || lower.includes('red')) return '#ef4444'
  if (lower.includes('vàng') || lower.includes('yellow')) return '#facc15'
  if (lower.includes('đen') || lower.includes('black')) return '#111827'
  if (lower.includes('xám') || lower.includes('gray')) return '#9ca3af'
  if (lower.includes('xanh') || lower.includes('blue')) return '#3b82f6'
  return '#e5e7eb'
}

const getVariantStock = (variant) => {
  if (!variant) return 0
  return variant.soLuongKhaDung !== undefined ? variant.soLuongKhaDung : (variant.soLuongTon ?? 0)
}

const availableStock = computed(() => getVariantStock(selectedVariant.value))

const galleryImages = computed(() => {
  if (!product.value?.gallery) return []
  return product.value.gallery.map((item) => ({
    ...item,
    url: item.image.startsWith('http') ? item.image : `${API_URL}${item.image}`,
  }))
})

onMounted(async () => {
  try {
    const data = await getShopVariantsByProductId(props.productId)
    product.value = data
    if (data.colors?.length) {
      selectedColor.value = data.colors[0]
      if (data.colors[0].variants?.length) {
        selectedVariant.value = data.colors[0].variants[0]
      }
    }
    const firstImg = data.gallery?.[0]?.image || ''
    mainImage.value = firstImg.startsWith('http') ? firstImg : `${API_URL}${firstImg}`
  } catch (e) {
    console.error('Lỗi tải sản phẩm xem nhanh:', e)
  } finally {
    loading.value = false
  }
})

const selectColor = (color) => {
  selectedColor.value = color
  selectedVariant.value = color.variants?.[0] ?? null
}
const handleAddToCart = async (event) => {
  // Thêm event vào đây[cite: 1]
  const token = sessionStorage.getItem('token')
  if (!token) {
    alert('Bạn cần đăng nhập để thêm vào giỏ hàng!')
    router.push('/login')
    return
  }

  // Kích hoạt hiệu ứng bay vào giỏ hàng ngay khi nhấn nút
  flyToCart(event, mainImage.value) // Gọi animation[cite: 2]

  try {
    await axios.post(
      `${API_URL}/giohang/them`,
      {
        idSanPhamChiTiet: selectedVariant.value.id,
        soLuong: quantity.value,
      },
      { headers: { Authorization: `Bearer ${token}` } },
    )
    emitter.emit('cart-updated')
    // Có thể bỏ alert nếu hiệu ứng animation đã đủ trực quan
    // alert('Đã thêm sản phẩm vào giỏ hàng thành công!')
    emit('close')
  } catch (err) {
    alert(err?.response?.data || 'Lỗi thêm vào giỏ hàng!')
  }
}
const goToDetail = () => {
  emit('close')
  router.push({ name: 'confirmbuy', params: { id: props.productId } })
}
</script>
