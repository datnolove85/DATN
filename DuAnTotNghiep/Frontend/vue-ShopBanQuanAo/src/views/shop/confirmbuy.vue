<template>
  <div
    class="max-w-[1400px] mx-auto px-6 lg:px-8 py-12 bg-gradient-to-b from-white to-slate-50 min-h-screen"
  >
    <div v-if="loading" class="flex justify-center items-center py-20 text-gray-400">
      <div class="animate-pulse">Đang tải sản phẩm...</div>
    </div>

    <div v-else class="grid grid-cols-1 xl:grid-cols-[620px_minmax(450px,1fr)] gap-16 items-start">
      <!-- CỘT TRÁI: HÌNH ẢNH -->
      <div class="relative group">
        <div class="absolute top-5 left-5 z-10 flex flex-col gap-2">
          <span
            v-if="selectedVariant?.soLuongTon > 0"
            class="bg-green-500 text-white px-3 py-1 rounded-full text-xs font-semibold"
            >Còn hàng</span
          >
          <span v-else class="bg-red-500 text-white px-3 py-1 rounded-full text-xs font-semibold"
            >Hết hàng</span
          >
          <span
            v-if="selectedVariant?.phanTramGiam"
            class="bg-red-600 text-white px-3 py-1 rounded-full text-xs font-bold"
            >-{{ selectedVariant.phanTramGiam }}%</span
          >
        </div>
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
        <div class="grid grid-cols-5 gap-3 mt-5">
          <button
            v-for="img in galleryImages"
            :key="img.image"
            @click="selectImage(img)"
            class="aspect-square rounded-2xl overflow-hidden border-2 transition hover:scale-105"
            :class="mainImage === img.url ? 'border-black' : 'border-white hover:border-gray-300'"
          >
            <img :src="img.url" class="w-full h-full object-cover" />
          </button>
        </div>
      </div>

      <!-- CỘT PHẢI: THÔNG TIN VÀ HÀNH ĐỘNG -->
      <div class="sticky top-28">
        <div class="rounded-3xl bg-white border border-gray-200 shadow-xl shadow-slate-100 p-10">
          <!-- Tên và Giá -->
          <h1 class="text-3xl font-bold text-gray-900 leading-tight">{{ product?.productName }}</h1>
          <div class="mt-4">
            <div class="flex items-center gap-3">
              <span class="text-5xl font-extrabold tracking-tight"
                >{{
                  (selectedVariant?.giaSauGiam || selectedVariant?.giaBan)?.toLocaleString('vi-VN')
                }}đ</span
              >
              <span v-if="selectedVariant?.giaSauGiam" class="line-through text-gray-400"
                >{{ selectedVariant?.giaBan?.toLocaleString('vi-VN') }}đ</span
              >
            </div>
            <p v-if="selectedVariant?.giaSauGiam" class="text-green-600 mt-2">
              Tiết kiệm
              {{ (selectedVariant.giaBan - selectedVariant.giaSauGiam).toLocaleString('vi-VN') }}đ
            </p>
          </div>

          <!-- Chọn Màu & Size -->
          <div class="space-y-6 mt-8">
            <div>
              <div class="flex items-center justify-between mb-3">
                <span class="text-sm font-bold text-gray-900 uppercase">Màu sắc</span>
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
                      ? 'border-blue-600 bg-blue-600 text-white shadow-lg shadow-blue-200'
                      : 'border-gray-200 bg-white text-gray-700 hover:border-blue-400 hover:bg-blue-50'
                  "
                >
                  {{ color.name }}
                </button>
              </div>
            </div>

            <div>
              <div class="flex items-center justify-between mb-3">
                <span class="text-sm font-bold text-gray-900 uppercase">Kích thước</span>
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
                    variant.soLuongTon === 0
                      ? 'bg-gray-100 text-gray-300 border-gray-200 cursor-not-allowed line-through'
                      : selectedVariant?.id === variant.id
                        ? 'border-blue-600 bg-blue-600 text-white shadow-lg shadow-blue-200'
                        : 'border-gray-200 bg-white text-gray-700 hover:border-blue-400 hover:bg-blue-50',
                  ]"
                >
                  {{ variant.tenKichThuoc }}
                </button>
              </div>
            </div>
          </div>

          <!-- Số lượng -->
          <div class="mt-8">
            <div class="text-sm font-bold uppercase mb-3">Số lượng</div>
            <div class="flex items-center w-fit border border-gray-200 rounded-xl overflow-hidden">
              <button
                @click="decreaseQty"
                :disabled="quantity <= 1"
                class="w-12 h-12 hover:bg-gray-100"
              >
                -
              </button>
              <div class="w-14 text-center font-semibold">{{ quantity }}</div>
              <button
                @click="increaseQty"
                :disabled="!selectedVariant || quantity >= selectedVariant.soLuongTon"
                class="w-12 h-12 hover:bg-gray-100"
              >
                +
              </button>
            </div>
          </div>

          <!-- Nút Hành Động -->
          <!-- Nút Hành Động đã đổi sang tone màu xanh dương -->
          <div class="grid grid-cols-2 gap-4 mt-8">
            <!-- Nút Thêm vào giỏ: Viền xanh, chữ xanh -->
            <button
              @click="addToCart"
              :disabled="!selectedVariant || selectedVariant.soLuongTon === 0"
              class="w-full py-4 rounded-2xl border-2 font-semibold transition-all border-blue-600 text-blue-600 hover:bg-blue-600 hover:text-white disabled:bg-gray-100 disabled:border-gray-200"
            >
              🛒 Thêm vào giỏ
            </button>

            <!-- Nút Mua ngay: Nền xanh đậm -->
            <button
              @click="buyNow"
              :disabled="!selectedVariant || selectedVariant.soLuongTon === 0"
              class="w-full py-4 rounded-2xl font-bold text-lg transition-all bg-blue-600 text-white hover:bg-blue-700 disabled:bg-gray-300 disabled:text-gray-500"
            >
              Mua ngay
            </button>
          </div>
        </div>

        <!-- Thông tin hỗ trợ -->
        <div class="mt-8 bg-green-50 border border-green-100 rounded-2xl p-5 space-y-3 text-sm">
          <div class="flex items-center gap-2 text-green-600">
            ✔ Còn {{ selectedVariant?.soLuongTon }} sản phẩm
          </div>
          <div class="flex items-center gap-2">🚚 Miễn phí giao hàng từ 500.000đ</div>
          <div class="flex items-center gap-2">🔄 Đổi trả trong 7 ngày</div>
        </div>
      </div>
    </div>

    <!-- CHI TIẾT SẢN PHẨM -->
    <div class="mt-16 bg-gray-50/50 p-6 rounded-2xl border border-gray-100">
      <h4 class="font-bold text-gray-900 mb-4">Chi tiết sản phẩm</h4>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-x-10 gap-y-4 text-sm">
        <div class="flex justify-between py-2 border-b border-gray-100">
          <span class="text-gray-500">Mã SP</span
          ><span class="font-medium">{{ selectedVariant?.maSanPhamChiTiet }}</span>
        </div>
        <div class="flex justify-between py-2 border-b border-gray-100">
          <span class="text-gray-500">Danh mục</span><span>{{ selectedVariant?.tenDanhMuc }}</span>
        </div>
        <div class="flex justify-between py-2 border-b border-gray-100">
          <span class="text-gray-500">Thương hiệu</span
          ><span>{{ selectedVariant?.tenThuongHieu }}</span>
        </div>
        <div class="flex justify-between py-2 border-b border-gray-100">
          <span class="text-gray-500">Chất liệu</span
          ><span>{{ selectedVariant?.tenChatLieu }}</span>
        </div>
      </div>
    </div>

    <!-- Sản phẩm khác của shop -->
    <div class="mt-16">
      <div class="flex justify-between items-center mb-5">
        <h2 class="text-xl font-bold text-slate-800">Sản phẩm khác của shop</h2>

        <RouterLink to="/san-pham" class="text-sm font-semibold text-blue-600 hover:underline">
          Xem tất cả →
        </RouterLink>
      </div>

      <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-5">
        <div
          v-for="item in shopProducts"
          :key="item.id"
          @click="$router.push({ name: 'confirmbuy', params: { id: item.idSanPham } })"
          class="group bg-white rounded-2xl overflow-hidden border border-slate-200 hover:border-blue-500 hover:shadow-2xl transition-all duration-300 cursor-pointer"
        >
          <!-- Ảnh -->
          <div class="relative overflow-hidden bg-slate-100">
            <img
              :src="item.image ? API_URL + item.image : placeholder"
              class="w-full h-64 object-cover transition duration-500 group-hover:scale-110"
            />

            <!-- Badge -->

            <div
              v-if="item.dangGiamGia"
              class="absolute top-3 left-3 bg-red-600 text-white px-3 py-1 rounded-full text-xs font-bold"
            >
              -{{ item.phanTramGiam }}%
            </div>

            <div
              class="absolute top-3 right-3 bg-blue-600 text-white px-3 py-1 rounded-full text-xs font-semibold"
            >
              {{ item.tenThuongHieu }}
            </div>

            <div
              class="absolute bottom-3 right-3 bg-white/90 backdrop-blur px-3 py-1 rounded-full text-xs shadow"
            >
              {{ item.tongSoLuong }} SP
            </div>
          </div>

          <!-- Nội dung -->
          <!-- Nội dung -->
          <div class="p-4 flex flex-col h-[360px]">
            <h3 class="font-semibold text-[15px] text-slate-800 line-clamp-2 h-11">
              {{ item.tenSanPham }}
            </h3>

            <div class="mt-3 h-20 flex flex-col justify-between">
              <!-- Giá -->
              <div class="flex justify-between items-center">
                <span class="text-2xl font-bold text-red-600">
                  {{ item.giaSauGiam.toLocaleString('vi-VN') }}đ
                </span>

                <span
                  v-if="item.dangGiamGia"
                  class="bg-red-500 text-white text-xs px-2 py-1 rounded-full"
                >
                  -{{ item.phanTramGiam }}%
                </span>
              </div>

              <!-- Giá gốc -->
              <div class="h-5">
                <span v-if="item.dangGiamGia" class="text-sm line-through text-gray-400">
                  {{ item.giaBan.toLocaleString('vi-VN') }}đ
                </span>
              </div>

              <!-- Tiết kiệm -->
              <div class="h-5">
                <span v-if="item.dangGiamGia" class="text-xs text-green-600">
                  Tiết kiệm
                  {{ (item.giaBan - item.giaSauGiam).toLocaleString('vi-VN') }}đ
                </span>
              </div>
            </div>

            <div class="mt-3 space-y-2">
              <div class="flex justify-between text-sm">
                <span class="text-gray-400">Danh mục</span>
                <span class="font-medium">
                  {{ item.tenDanhMuc }}
                </span>
              </div>

              <div class="flex justify-between text-sm">
                <span class="text-gray-400">Thương hiệu</span>
                <span class="font-medium">
                  {{ item.tenThuongHieu }}
                </span>
              </div>

              <div class="flex justify-between text-sm">
                <span class="text-gray-400">Chất liệu</span>
                <span class="font-medium">
                  {{ item.tenChatLieu }}
                </span>
              </div>
            </div>

            <div class="mt-auto pt-4 border-t border-slate-100 flex justify-between items-center">
              <div>
                <div class="text-xs text-gray-400">Tồn kho</div>

                <div class="font-bold text-green-600">{{ item.tongSoLuong }} sản phẩm</div>
              </div>

              <button
                class="px-4 py-2 rounded-xl bg-blue-600 text-white text-sm font-semibold transition hover:bg-blue-700"
              >
                Xem ngay
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'

import { useRoute } from 'vue-router'

import { getAllSanpham } from '@/service/SanphamService'

import { useRouter } from 'vue-router'

import { getShopVariantsByProductId } from '@/service/SanPhamChiTiet'

import { getAllSanPhamChiTiet } from '@/service/SanPhamChiTiet'

import axios from 'axios'

import stompClient from '@/socket'

import emitter from '@/utils/emitter'
const route = useRoute()

const router = useRouter()

const product = ref(null)

const selectedColor = ref(null)

const selectedVariant = ref(null)

const loading = ref(true)

const mainImage = ref('')

const quantity = ref(1)

const shopVariants = ref([])

const API_URL = 'http://localhost:8080'

const placeholder = 'https://via.placeholder.com/300'

const loadShopProducts = async () => {
  const data = await getAllSanPhamChiTiet()

  shopVariants.value = data.filter(
    (item) => item.trangThai && item.idSanPham !== Number(route.params.id),
  )
}
const shopProducts = computed(() => {
  const groups = {}

  shopVariants.value.forEach((item) => {
    if (!item.trangThai) return

    if (!groups[item.idSanPham]) {
      groups[item.idSanPham] = {
        idSanPham: item.idSanPham,

        tenSanPham: item.tenSanPham,
        tenDanhMuc: item.tenDanhMuc,
        tenThuongHieu: item.tenThuongHieu,
        tenChatLieu: item.tenChatLieu,

        giaBan: item.giaBan,
        giaSauGiam: item.giaSauGiam ?? item.giaBan,

        dangGiamGia: item.dangGiamGia,
        phanTramGiam: item.phanTramGiam ?? 0,

        tongSoLuong: 0,

        image: item.images?.[0] ?? '',

        colors: [],
      }
    }

    const p = groups[item.idSanPham]

    // Tổng tồn
    p.tongSoLuong += item.soLuongTon

    // Giá bán thấp nhất
    if (item.giaBan < p.giaBan) {
      p.giaBan = item.giaBan
    }

    // Giá sau giảm thấp nhất
    const currentPrice = item.giaSauGiam ?? item.giaBan

    if (currentPrice < p.giaSauGiam) {
      p.giaSauGiam = currentPrice
    }

    // Lấy mức giảm lớn nhất
    if (item.dangGiamGia && (item.phanTramGiam ?? 0) > p.phanTramGiam) {
      p.phanTramGiam = item.phanTramGiam
      p.dangGiamGia = true
    }

    // Màu
    if (!p.colors.includes(item.tenMauSac)) {
      p.colors.push(item.tenMauSac)
    }
  })

  return Object.values(groups).slice(0, 8)
})
const loadProduct = async () => {
  loading.value = true

  try {
    const data = await getShopVariantsByProductId(route.params.id)

    product.value = data

    if (data.colors.length) {
      selectedColor.value = data.colors[0]

      if (data.colors[0].variants.length) {
        selectedVariant.value = data.colors[0].variants[0]
      }
    }

    quantity.value = 1
  } catch (e) {
    console.log(e)
  } finally {
    loading.value = false
  }
}

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

    await loadShopProducts()
    await loadProduct()

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
  connectSocket()
})
function connectSocket() {
  if (stompClient.connected) {
    subscribeDetail()
  } else {
    stompClient.onConnect = () => {
      console.log('✅ Connected')

      subscribeDetail()
    }
  }
}

function subscribeDetail() {
  stompClient.subscribe('/topic/pos', async (msg) => {
    const event = JSON.parse(msg.body)

    switch (event.type) {
      case 'DISCOUNT_UPDATED':
        await loadShopProducts()
        await loadProduct()
        break

      case 'PRODUCT_UPDATED':
        await loadShopProducts()
        await loadProduct()
        break
    }
  })
}
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
  const token = sessionStorage.getItem('token')

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

watch(
  () => route.params.id,
  async () => {
    await loadProduct()
    await loadShopProducts()

    window.scrollTo({
      top: 0,
      behavior: 'smooth',
    })
  },
)
</script>
