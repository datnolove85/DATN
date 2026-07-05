<template>
  <div class="max-w-[1000px] mx-auto px-4 py-16 bg-white min-h-screen">
    <div v-if="loading" class="flex justify-center items-center py-20 text-gray-400">
      <div class="animate-pulse">Đang tải sản phẩm...</div>
    </div>

    <div v-else class="grid grid-cols-1 md:grid-cols-[1fr,minmax(360px,auto)] gap-16">
      <div class="relative group">
        <div
          class="aspect-[4/5] bg-gray-50 rounded-3xl overflow-hidden border border-gray-100 shadow-sm transition-transform duration-500 group-hover:scale-[1.01]"
        >
          <img
            v-if="mainImage"
            :src="mainImage"
            class="w-full h-full object-cover transition-opacity duration-700"
          />

          <div v-else class="w-full h-full flex items-center justify-center text-gray-400">
            Không có ảnh
          </div>
        </div>

        <div
          class="absolute top-1/2 -left-10 -translate-y-1/2 flex flex-col gap-2.5 p-2 bg-white/70 backdrop-blur-sm rounded-full shadow-lg border border-white/50"
        >
          <button
            v-for="img in galleryImages"
            :key="img.image"
            @click="selectImage(img)"
            class="w-11 h-11 rounded-full overflow-hidden border-2 transition-all"
            :class="mainImage === img.url ? 'border-black' : 'border-white hover:border-gray-300'"
          >
            <img :src="img.url" class="w-full h-full object-cover" />
          </button>
        </div>
      </div>

      <div class="flex flex-col justify-start">
        <div class="mb-10">
          <h1 class="text-3xl font-bold text-gray-900 leading-tight">
            {{ product?.productName }}
          </h1>

          <div
            class="mt-2 text-xl font-bold text-gray-900 bg-gray-100 inline-block px-3 py-1 rounded-full"
          >
            {{ selectedVariant?.giaBan?.toLocaleString('vi-VN') }}đ
          </div>
        </div>

        <div class="space-y-10 pb-10 border-b border-gray-100">
          <div>
            <div class="flex items-center justify-between mb-4">
              <span class="text-sm font-bold text-gray-900 uppercase tracking-wide">Màu sắc</span>

              <span class="text-sm text-gray-500 bg-gray-50 px-2.5 py-0.5 rounded-full">{{
                selectedColor?.name
              }}</span>
            </div>

            <div class="flex flex-wrap gap-2.5">
              <button
                v-for="color in product?.colors"
                :key="color.id"
                @click="selectColor(color)"
                class="px-5 py-2 rounded-xl text-sm font-medium border-2 transition-all"
                :class="
                  selectedColor?.id === color.id
                    ? 'border-black bg-black text-white'
                    : 'border-gray-200 hover:border-gray-300'
                "
              >
                {{ color.name }}
              </button>
            </div>
          </div>

          <div>
            <div class="flex items-center justify-between mb-4">
              <span class="text-sm font-bold text-gray-900 uppercase tracking-wide"
                >Kích thước</span
              >

              <span class="text-sm text-gray-500 bg-gray-50 px-2.5 py-0.5 rounded-full">{{
                selectedVariant?.tenKichThuoc
              }}</span>
            </div>

            <div class="flex flex-wrap gap-2.5">
              <button
                v-for="variant in selectedColor?.variants"
                :key="variant.id"
                @click="variant.soLuongTon > 0 && selectVariant(variant)"
                :disabled="variant.soLuongTon === 0"
                class="w-12 h-12 rounded-xl text-sm font-medium border-2 flex items-center justify-center transition-all"
                :class="[
                  selectedVariant?.id === variant.id ? 'border-black bg-black text-white' : '',

                  variant.soLuongTon === 0
                    ? 'bg-gray-100 text-gray-300 border-gray-200 cursor-not-allowed line-through'
                    : 'border-gray-200 hover:border-gray-300',
                ]"
              >
                {{ variant.tenKichThuoc }}
              </button>
            </div>
          </div>
        </div>

        <div class="mt-8">
          <div class="text-sm font-bold uppercase tracking-wide mb-3">Số lượng</div>

          <div class="flex items-center w-fit border border-gray-200 rounded-xl overflow-hidden">
            <button
              @click="decreaseQty"
              :disabled="quantity <= 1"
              class="w-12 h-12 transition"
              :class="
                quantity <= 1 ? 'bg-gray-100 text-gray-300 cursor-not-allowed' : 'hover:bg-gray-100'
              "
            >
              -
            </button>

            <div class="w-14 text-center font-semibold">
              {{ quantity }}
            </div>

            <button
              @click="increaseQty"
              :disabled="
                !selectedVariant ||
                quantity >= selectedVariant.soLuongTon ||
                selectedVariant.soLuongTon === 0
              "
              class="w-12 h-12 transition"
              :class="
                !selectedVariant ||
                quantity >= selectedVariant.soLuongTon ||
                selectedVariant.soLuongTon === 0
                  ? 'bg-gray-100 text-gray-300 cursor-not-allowed'
                  : 'hover:bg-gray-100'
              "
            >
              +
            </button>
          </div>

          <p class="text-xs text-gray-500 mt-2">
            Tối đa {{ selectedVariant?.soLuongTon || 0 }} sản phẩm
          </p>
        </div>

        <div class="mt-10 flex gap-3">
          <button
            :disabled="!selectedVariant || selectedVariant.soLuongTon === 0"
            class="flex-1 py-4 rounded-2xl border-2 font-semibold transition-all"
            :class="
              !selectedVariant || selectedVariant.soLuongTon === 0
                ? 'border-gray-200 text-gray-400 bg-gray-100 cursor-not-allowed'
                : 'border-black text-black hover:bg-black hover:text-white'
            "
            @click="addToCart"
          >
            🛒 Thêm vào giỏ
          </button>

          <button
            :disabled="!selectedVariant || selectedVariant.soLuongTon === 0"
            class="flex-[1.3] py-4 rounded-2xl font-bold text-lg transition-all"
            :class="
              !selectedVariant || selectedVariant.soLuongTon === 0
                ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                : 'bg-black text-white hover:bg-gray-800'
            "
            @click="buyNow"
          >
            Mua ngay
          </button>
        </div>

        <div class="mt-10 space-y-4 text-sm bg-gray-50/50 p-6 rounded-2xl border border-gray-100">
          <h4 class="font-bold text-gray-900 mb-2">Chi tiết sản phẩm</h4>

          <div class="flex justify-between items-center py-2 border-b border-gray-100">
            <p class="text-gray-500">Mã sản phẩm</p>

            <p class="font-medium text-gray-900">{{ selectedVariant?.maSanPhamChiTiet }}</p>
          </div>

          <div class="flex justify-between items-center py-2 border-b border-gray-100">
            <p class="text-gray-500">Tình trạng</p>

            <p
              class="font-medium"
              :class="selectedVariant?.soLuongTon > 0 ? 'text-green-600' : 'text-red-500'"
            >
              {{
                selectedVariant?.soLuongTon > 0
                  ? 'Còn hàng (' + selectedVariant?.soLuongTon + ')'
                  : 'Hết hàng'
              }}
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'

import { useRoute } from 'vue-router'

import { useRouter } from 'vue-router'

import { getShopVariantsByProductId } from '@/service/SanPhamChiTiet'

import axios from 'axios'

import emitter from '@/utils/emitter'
const route = useRoute()

const router = useRouter()

const product = ref(null)

const selectedColor = ref(null)

const selectedVariant = ref(null)

const loading = ref(true)

const mainImage = ref('')

const quantity = ref(1)

const increaseQty = () => {
  if (!selectedVariant.value) return

  if (quantity.value < selectedVariant.value.soLuongTon) {
    quantity.value++
  }
}

const decreaseQty = () => {
  if (quantity.value > 1) {
    quantity.value--
  }
}

const galleryImages = computed(() => {
  if (!product.value) return []

  return product.value.gallery.map((item) => ({
    ...item,
    url: `http://localhost:8080${item.image}`,
  }))
})

watch(selectedVariant, (variant) => {
  if (!variant || !product.value) return

  const firstImage = product.value.gallery.find((g) => g.spctId === variant.id)

  mainImage.value = firstImage ? `http://localhost:8080${firstImage.image}` : ''
})
const selectImage = (img) => {
  mainImage.value = img.url

  for (const color of product.value.colors) {
    const variant = color.variants.find((v) => v.id === img.spctId)

    if (variant) {
      selectedColor.value = color
      selectedVariant.value = variant
      quantity.value = variant.soLuongTon > 0 ? 1 : 0
      break
    }
  }
}
watch(selectedVariant, (variant) => {
  if (!variant) return

  if (variant.soLuongTon === 0) {
    quantity.value = 0
  } else {
    quantity.value = 1
  }
})

const selectColor = (color) => {
  selectedColor.value = color

  selectedVariant.value = color.variants?.[0] ?? null

  quantity.value = 1
}

const selectVariant = (variant) => {
  selectedVariant.value = variant

  quantity.value = 1
}

onMounted(async () => {
  try {
    const data = await getShopVariantsByProductId(route.params.id)

    product.value = data

    if (data.colors.length) {
      selectedColor.value = data.colors[0]

      if (data.colors[0].variants.length) {
        selectedVariant.value = data.colors[0].variants[0]
      }
    }
  } catch (e) {
    console.log(e)
  } finally {
    loading.value = false
  }
})
const addToCart = async () => {
  const token = localStorage.getItem('token')

  if (!token) {
    alert('Bạn cần đăng nhập')
    router.push('/login')
    return
  }

  if (!selectedVariant.value) {
    alert('Vui lòng chọn sản phẩm')
    return
  }

  try {
    const res = await axios.post(
      'http://localhost:8080/giohang/them',
      {
        idSanPhamChiTiet: selectedVariant.value.id,
        soLuong: quantity.value,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      },
    )

    console.log('Emit cart-updated')
    // Báo Header tải lại giỏ hàng
    emitter.emit('cart-updated')

    alert('Thêm vào giỏ thành công 🎉')
  } catch (err) {
    console.log(err)
    alert(err?.response?.data || 'Lỗi thêm giỏ hàng')
  }
}

const buyNow = () => {
  const token = localStorage.getItem('token')

  if (!token) {
    router.push('/login')
    return
  }

  if (!selectedVariant.value) return

  router.push({
    path: '/xacnhan',
    query: {
      spct: selectedVariant.value.id,
      qty: quantity.value,
    },
  })
}
</script>
