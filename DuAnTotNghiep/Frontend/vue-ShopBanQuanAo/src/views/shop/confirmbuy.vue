<template>
  <div
    class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 bg-gradient-to-b from-white to-slate-50 min-h-screen"
  >
    <div v-if="loading" class="flex justify-center items-center py-20 text-gray-400">
      <div class="animate-pulse">Đang tải sản phẩm...</div>
    </div>

    <!-- BỐ CỤC 2 CỘT: Cột trái sticky ảnh, cột phải cuộn tự nhiên mượt mà -->
    <div v-else class="grid grid-cols-1 lg:grid-cols-[480px_1fr] gap-8 items-start">
      <!-- CỘT TRÁI (ẢNH): Giữ sticky để cố định ảnh khi người dùng đọc thông tin -->
      <div class="sticky top-28 bg-white border border-gray-200 rounded-3xl p-6 shadow-sm">
        <div
          class="relative aspect-[4/5] bg-gray-50 rounded-2xl overflow-hidden border border-gray-100"
        >
          <div class="absolute top-4 left-4 z-10 flex flex-col gap-2">
            <span
              v-if="selectedVariant?.dangGiamGia && selectedVariant?.phanTramGiam > 0"
              class="bg-red-600 text-white px-3 py-1 rounded-full text-xs font-bold shadow-sm"
              >-{{ selectedVariant.phanTramGiam }}%</span
            >
          </div>
          <img
            v-if="mainImage"
            :src="mainImage"
            class="w-full h-full object-cover transition-opacity duration-700"
          />
          <div v-else class="w-full h-full flex items-center justify-center text-gray-400">
            Không có ảnh
          </div>
        </div>

        <div class="grid grid-cols-5 gap-3 mt-4">
          <button
            v-for="img in galleryImages"
            :key="img.image"
            @click="selectImage(img)"
            class="aspect-square rounded-xl overflow-hidden border-2 transition hover:scale-105"
            :class="
              mainImage === img.url ? 'border-gray-900' : 'border-transparent hover:border-gray-300'
            "
          >
            <img :src="img.url" class="w-full h-full object-cover" />
          </button>
        </div>
      </div>

      <!-- CỘT PHẢI: THÔNG TIN VÀ HÀNH ĐỘNG (Đã bỏ sticky để cuộn tự nhiên) -->
      <div>
        <div class="rounded-3xl bg-white border border-gray-200 shadow-sm p-6 lg:p-8">
          <!-- Tên sản phẩm -->
          <h1 class="text-2xl lg:text-3xl font-bold text-gray-900 leading-tight">
            {{ product?.productName }}
          </h1>

          <!-- Thông tin mã, thương hiệu, tình trạng -->
          <div class="mt-3 space-y-1.5 text-sm text-gray-700">
            <div>
              <span class="text-gray-500">Mã: </span>
              <span class="italic text-gray-800">{{
                selectedVariant?.maSanPhamChiTiet || 'Đang cập nhật'
              }}</span>
            </div>
            <div>
              <span class="text-gray-500">Thương hiệu: </span>
              <span class="text-teal-600 font-medium">{{
                selectedVariant?.tenThuongHieu || 'Wolf Calie'
              }}</span>
            </div>
            <div class="flex items-center gap-2 pt-0.5">
              <span class="text-gray-500">Tình trạng: </span>
              <span class="bg-emerald-600 text-white px-3 py-0.5 rounded-full text-xs font-semibold"
                >Còn hàng</span
              >
            </div>
          </div>

          <!-- Giá -->
          <div class="mt-4">
            <div class="flex items-baseline gap-3">
              <span class="text-4xl font-extrabold text-red-600 tracking-tight"
                >{{
                  (selectedVariant?.giaSauGiam || selectedVariant?.giaBan)?.toLocaleString('vi-VN')
                }}đ</span
              >
              <span v-if="selectedVariant?.dangGiamGia" class="line-through text-gray-400 text-xl"
                >{{ selectedVariant?.giaBan?.toLocaleString('vi-VN') }}đ</span
              >
            </div>
          </div>

          <!-- Khuyến mãi đặc biệt box -->
          <div class="mt-6 border border-rose-200 rounded-2xl p-4 relative bg-rose-50/25">
            <div
              class="absolute -top-3.5 left-4 bg-rose-500 text-white px-3.5 py-1 rounded-full text-xs font-bold flex items-center gap-1.5 shadow-sm"
            >
              <span>🎁</span> Khuyến mãi đặc biệt
            </div>
            <ul class="space-y-2 text-xs text-gray-700 mt-2">
              <li class="flex items-start gap-2">
                <span class="text-red-500 font-bold">✓</span> Giảm 10% cho đơn hàng từ 3 sản phẩm
                trở lên.
              </li>
              <li class="flex items-start gap-2">
                <span class="text-red-500 font-bold">✓</span> Miễn phí giao hàng toàn quốc cho đơn
                hàng trên 500.000 VNĐ.
              </li>
              <li class="flex items-start gap-2">
                <span class="text-red-500 font-bold">✓</span> Tặng ngay voucher 50.000 VNĐ cho khách
                hàng mới.
              </li>
            </ul>
          </div>

          <!-- Chọn Màu -->
          <div class="mt-6">
            <div class="text-sm font-bold text-gray-900 mb-2">
              Màu sắc: <span class="font-normal text-gray-700">{{ selectedColor?.name }}</span>
            </div>
            <div class="flex flex-wrap gap-2.5">
              <button
                v-for="color in product?.colors"
                :key="color.id"
                @click="selectColor(color)"
                class="w-11 h-11 rounded-xl border transition-all flex items-center justify-center shadow-sm"
                :class="
                  selectedColor?.id === color.id
                    ? 'border-gray-900 ring-2 ring-gray-900/20'
                    : 'border-gray-200 hover:border-gray-400'
                "
                :style="{ backgroundColor: getColorStyle(color.name) }"
                :title="color.name"
              ></button>
            </div>
          </div>

          <!-- Chọn Size -->
          <div class="mt-6">
            <div class="flex items-center justify-between mb-2">
              <span class="text-sm font-bold text-gray-900">
                Size:
                <span class="font-normal text-gray-700">{{ selectedVariant?.tenKichThuoc }}</span>
              </span>
              <button
                class="text-xs text-teal-600 font-medium hover:underline flex items-center gap-1"
              >
                📏 Gợi ý tìm size
              </button>
            </div>
            <div class="flex flex-wrap gap-2.5">
              <button
                v-for="variant in selectedColor?.variants"
                :key="variant.id"
                @click="getVariantStock(variant) > 0 && selectVariant(variant)"
                :disabled="getVariantStock(variant) === 0"
                class="w-12 h-12 rounded-xl text-sm font-semibold border flex items-center justify-center transition-all"
                :class="[
                  getVariantStock(variant) === 0
                    ? 'bg-gray-100 text-gray-300 border-gray-200 cursor-not-allowed line-through'
                    : selectedVariant?.id === variant.id
                      ? 'border-gray-900 bg-gray-900 text-white shadow-sm'
                      : 'border-gray-200 bg-white text-gray-800 hover:border-gray-300',
                ]"
              >
                {{ variant.tenKichThuoc }}
              </button>
            </div>
          </div>

          <!-- Số lượng và Khả dụng kho -->
          <div class="mt-6 space-y-2">
            <div class="flex items-center gap-4">
              <span class="text-sm font-bold text-gray-900">Số lượng:</span>
              <div
                class="flex items-center w-fit border border-gray-200 rounded-xl overflow-hidden bg-white shadow-sm"
              >
                <button
                  @click="decreaseQty"
                  :disabled="quantity <= 1"
                  class="w-10 h-10 bg-sky-50 hover:bg-sky-100 flex items-center justify-center font-bold text-sky-600 disabled:text-gray-300 disabled:bg-gray-50 transition"
                >
                  -
                </button>
                <div class="w-12 text-center font-semibold text-gray-800">{{ quantity }}</div>
                <button
                  @click="increaseQty"
                  :disabled="!selectedVariant || quantity >= availableStock"
                  class="w-10 h-10 bg-sky-50 hover:bg-sky-100 flex items-center justify-center font-bold text-sky-600 disabled:text-gray-300 disabled:bg-gray-50 transition"
                >
                  +
                </button>
              </div>
            </div>
            <div class="text-xs text-emerald-700 font-medium flex items-center gap-1.5 pl-1">
              ✔ Khả dụng: {{ availableStock }} sản phẩm trong kho
            </div>
          </div>

          <!-- Nút Hành Động -->
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-3 mt-6">
            <button
              @click="buyNow"
              :disabled="!selectedVariant || availableStock === 0"
              class="w-full py-3.5 px-4 rounded-xl font-bold text-sm sm:text-base transition-all bg-[#00A884] text-white hover:bg-[#009272] shadow-sm disabled:bg-gray-300 disabled:text-gray-500 text-center flex flex-col justify-center items-center leading-tight"
            >
              <span>MUA NGAY</span>
              <span class="text-[11px] font-normal opacity-90"
                >(Giao tận nơi hoặc nhận tại cửa hàng)</span
              >
            </button>

            <button
              @click="addToCart"
              :disabled="!selectedVariant || availableStock === 0"
              class="w-full py-3.5 px-4 rounded-xl font-bold text-sm sm:text-base transition-all bg-[#E03C31] text-white hover:bg-[#C9352B] shadow-sm disabled:bg-gray-300 disabled:text-gray-500 flex items-center justify-center gap-2"
            >
              <span>🛒 Thêm vào giỏ</span>
            </button>
          </div>

          <button
            type="button"
            :disabled="!selectedVariant"
            class="mt-3 w-full py-3.5 rounded-xl font-bold text-sm transition-all bg-purple-600 text-white hover:bg-purple-700 disabled:bg-gray-300 disabled:text-gray-500 flex items-center justify-center gap-2 shadow-sm"
            @click="showTryOn = true"
          >
            ✨ Thử đồ bằng AI
          </button>

          <!-- Tiện ích phụ -->
          <div class="flex items-center justify-between mt-4 text-sm">
            <button class="flex items-center gap-1.5 text-teal-600 font-medium hover:underline">
              📍 Tìm cửa hàng gần nhất
            </button>
            <button class="flex items-center gap-1.5 text-blue-600 hover:underline">
              ⚖️ So sánh
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- BANNER CAM KẾT -->
    <div class="mt-16">
      <h3 class="text-sm font-bold text-gray-800 uppercase tracking-wider mb-4">
        WOLF CALIE cam kết
      </h3>
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <div
          class="bg-white border border-gray-200 rounded-2xl p-4 flex items-center gap-3 shadow-sm"
        >
          <div
            class="w-12 h-12 rounded-xl bg-gray-50 flex items-center justify-center flex-shrink-0 text-xl"
          >
            🛡️
          </div>
          <div class="text-xs text-gray-700 leading-snug">
            Cam kết sản phẩm đúng mô tả, chất liệu cao cấp.
          </div>
        </div>
        <div
          class="bg-white border border-gray-200 rounded-2xl p-4 flex items-center gap-3 shadow-sm"
        >
          <div
            class="w-12 h-12 rounded-xl bg-gray-50 flex items-center justify-center flex-shrink-0 text-xl"
          >
            🚚
          </div>
          <div class="text-xs text-gray-700 leading-snug">
            Giao trong 3-5 ngày và freeship đơn từ 498k
          </div>
        </div>
        <div
          class="bg-white border border-gray-200 rounded-2xl p-4 flex items-center gap-3 shadow-sm"
        >
          <div
            class="w-12 h-12 rounded-xl bg-gray-50 flex items-center justify-center flex-shrink-0 text-xl"
          >
            🔄
          </div>
          <div class="text-xs text-gray-700 leading-snug">
            Hỗ trợ đổi trả trong 7 ngày nếu sản phẩm lỗi.
          </div>
        </div>
        <div
          class="bg-white border border-gray-200 rounded-2xl p-4 flex items-center gap-3 shadow-sm"
        >
          <div
            class="w-12 h-12 rounded-xl bg-gray-50 flex items-center justify-center flex-shrink-0 text-xl"
          >
            ❓
          </div>
          <div class="text-xs text-gray-700 leading-snug">
            Đội ngũ tư vấn tận tâm, giải đáp nhanh chóng
          </div>
        </div>
      </div>
    </div>

    <!-- HỆ THỐNG TABS THÔNG TIN SẢN PHẨM -->
    <div class="mt-8 bg-white border border-gray-200 rounded-3xl p-8 shadow-sm">
      <div class="flex border-b border-gray-200 gap-8 justify-center mb-6 overflow-x-auto">
        <button
          v-for="tab in ['THÔNG TIN SẢN PHẨM', 'BẢO QUẢN', 'GIAO HÀNG & ĐỔI TRẢ']"
          :key="tab"
          @click="activeTab = tab"
          class="pb-3 text-sm font-bold tracking-wide transition-all border-b-2 whitespace-nowrap"
          :class="
            activeTab === tab
              ? 'border-teal-600 text-teal-600'
              : 'border-transparent text-gray-400 hover:text-gray-600'
          "
        >
          {{ tab }}
        </button>
      </div>

      <div
        v-if="activeTab === 'THÔNG TIN SẢN PHẨM'"
        class="text-sm text-gray-600 leading-relaxed space-y-4"
      >
        <p>
          Áo thun tôn dáng dành riêng cho nàng. Thiết kế cổ tròn, dáng ôm tôn lên đường cong cơ thể
          giúp nàng trông thon gọn và năng động hơn. Sản phẩm cho cảm giác mặc siêu mềm mại, siêu co
          giãn, xứng đáng là một item không thể thiếu trong tủ đồ hàng ngày cho các chị em.
        </p>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-x-10 gap-y-3 pt-4 border-t border-gray-100">
          <div class="flex justify-between py-1.5 border-b border-gray-100">
            <span class="text-gray-400">Mã SP</span>
            <span class="font-medium text-gray-800">{{
              selectedVariant?.maSanPhamChiTiet || 'Đang cập nhật'
            }}</span>
          </div>
          <div class="flex justify-between py-1.5 border-b border-gray-100">
            <span class="text-gray-400">Danh mục</span>
            <span class="font-medium text-gray-800">{{
              selectedVariant?.tenDanhMuc || 'Đang cập nhật'
            }}</span>
          </div>
          <div class="flex justify-between py-1.5 border-b border-gray-100">
            <span class="text-gray-400">Thương hiệu</span>
            <span class="font-medium text-gray-800">{{
              selectedVariant?.tenThuongHieu || 'Wolf Calie'
            }}</span>
          </div>
          <div class="flex justify-between py-1.5 border-b border-gray-100">
            <span class="text-gray-400">Chất liệu</span>
            <span class="font-medium text-gray-800">{{
              selectedVariant?.tenChatLieu || 'Đang cập nhật'
            }}</span>
          </div>
        </div>
      </div>

      <div
        v-else-if="activeTab === 'BẢO QUẢN'"
        class="text-sm text-gray-600 leading-relaxed space-y-2"
      >
        <p>- Giặt tay hoặc giặt máy ở chế độ nhẹ với nước lạnh.</p>
        <p>- Không sử dụng chất tẩy mạnh.</p>
        <p>- Phơi ở nơi bóng râm, tránh ánh nắng trực tiếp.</p>
        <p>- Ủi ở nhiệt độ thấp nếu cần thiết.</p>
      </div>

      <div
        v-else-if="activeTab === 'GIAO HÀNG & ĐỔI TRẢ'"
        class="text-sm text-gray-600 leading-relaxed space-y-2"
      >
        <p>- Giao hàng toàn quốc từ 3 - 5 ngày làm việc.</p>
        <p>- Miễn phí vận chuyển cho đơn hàng từ 500.000 VNĐ.</p>
        <p>
          - Hỗ trợ đổi trả trong vòng 7 ngày đối với sản phẩm có lỗi từ nhà sản xuất hoặc chưa qua
          sử dụng.
        </p>
      </div>
    </div>

    <!-- CÙNG DANH MỤC -->
    <div class="mt-16" v-if="sameCategoryProducts.length > 0">
      <h2 class="text-xl font-bold text-slate-800 mb-5">CÙNG DANH MỤC</h2>
      <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-5">
        <div
          v-for="item in sameCategoryProducts"
          :key="item.idSanPham"
          @click="$router.push({ name: 'confirmbuy', params: { id: item.idSanPham } })"
          class="group bg-white rounded-2xl overflow-hidden border border-slate-200 hover:border-teal-500 hover:shadow-xl transition-all duration-300 cursor-pointer flex flex-col justify-between"
        >
          <div class="relative overflow-hidden bg-slate-100 aspect-[4/5]">
            <img
              :src="item.image ? API_URL + item.image : placeholder"
              class="w-full h-full object-cover transition duration-500 group-hover:scale-105"
            />
            <div
              v-if="item.dangGiamGia"
              class="absolute top-3 left-3 bg-red-600 text-white px-3 py-1 rounded-full text-xs font-bold shadow-sm"
            >
              -{{ item.phanTramGiam }}%
            </div>
          </div>
          <div class="p-4 flex flex-col justify-between flex-1">
            <h3 class="font-semibold text-[14px] text-slate-800 line-clamp-2 mb-2">
              {{ item.tenSanPham }}
            </h3>
            <div class="flex items-center justify-between">
              <span class="text-base font-bold text-red-600"
                >{{ item.giaSauGiam.toLocaleString('vi-VN') }}đ</span
              >
              <span v-if="item.dangGiamGia" class="text-xs line-through text-gray-400"
                >{{ item.giaBan.toLocaleString('vi-VN') }}đ</span
              >
            </div>
          </div>
        </div>
      </div>
      <div class="flex justify-center mt-8">
        <button
          @click="$router.push('/san-pham')"
          class="px-8 py-3 rounded-xl bg-[#A91D22] text-white text-sm font-semibold hover:bg-[#8F161A] transition shadow-sm"
        >
          Xem thêm sản phẩm
        </button>
      </div>
    </div>

    <!-- BẠN ĐÃ XEM -->
    <div class="mt-16" v-if="recentlyViewedProducts.length > 0">
      <h2 class="text-xl font-bold text-slate-800 mb-5">BẠN ĐÃ XEM</h2>
      <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-5">
        <div
          v-for="item in recentlyViewedProducts"
          :key="item.idSanPham"
          @click="$router.push({ name: 'confirmbuy', params: { id: item.idSanPham } })"
          class="group bg-white rounded-2xl overflow-hidden border border-slate-200 hover:border-teal-500 hover:shadow-xl transition-all duration-300 cursor-pointer flex flex-col justify-between"
        >
          <div class="relative overflow-hidden bg-slate-100 aspect-[4/5]">
            <img
              :src="
                item.image
                  ? item.image.startsWith('http')
                    ? item.image
                    : API_URL + item.image
                  : placeholder
              "
              class="w-full h-full object-cover transition duration-500 group-hover:scale-105"
            />
            <div
              v-if="item.dangGiamGia"
              class="absolute top-3 left-3 bg-red-600 text-white px-3 py-1 rounded-full text-xs font-bold shadow-sm"
            >
              -{{ item.phanTramGiam }}%
            </div>
          </div>
          <div class="p-4 flex flex-col justify-between flex-1">
            <h3 class="font-semibold text-[14px] text-slate-800 line-clamp-2 mb-2">
              {{ item.tenSanPham }}
            </h3>
            <div class="flex items-center justify-between">
              <span class="text-base font-bold text-red-600"
                >{{ item.giaSauGiam.toLocaleString('vi-VN') }}đ</span
              >
              <span v-if="item.dangGiamGia" class="text-xs line-through text-gray-400"
                >{{ item.giaBan.toLocaleString('vi-VN') }}đ</span
              >
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <VirtualTryOn
    v-if="showTryOn && selectedVariant"
    is-modal
    :spct-id="selectedVariant.id"
    :default-garment-url="mainImage"
    :default-category="tryOnCategory"
    @close="showTryOn = false"
  />
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getShopVariantsByProductId, getAllSanPhamChiTiet } from '@/service/SanPhamChiTiet'
import VirtualTryOn from '@/components/VirtualTryOn.vue'
import axios from 'axios'
import stompClient from '@/socket'
import emitter from '@/utils/emitter'

const route = useRoute()
const router = useRouter()
const showTryOn = ref(false)
const activeTab = ref('THÔNG TIN SẢN PHẨM')

const product = ref(null)
const selectedColor = ref(null)
const selectedVariant = ref(null)
const loading = ref(true)
const mainImage = ref('')
const quantity = ref(1)
const shopVariants = ref([])
const recentlyViewedProducts = ref([])

const API_URL = 'http://localhost:8080'
const placeholder = 'https://via.placeholder.com/300'

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
const tryOnCategory = computed(() => {
  const text = `${product.value?.productName || ''} ` + `${selectedVariant.value?.tenDanhMuc || ''}`
  const normalized = text.toLowerCase()

  if (
    normalized.includes('đầm') ||
    normalized.includes('váy liền') ||
    normalized.includes('dress')
  ) {
    return 'dresses'
  }
  if (
    normalized.includes('quần') ||
    normalized.includes('chân váy') ||
    normalized.includes('pants') ||
    normalized.includes('trouser')
  ) {
    return 'lower_body'
  }
  return 'upper_body'
})

const loadShopProducts = async () => {
  try {
    const data = await getAllSanPhamChiTiet()
    shopVariants.value = data.filter(
      (item) => item.trangThai && item.idSanPham !== Number(route.params.id),
    )
  } catch (e) {
    console.error('Lỗi tải danh sách sản phẩm khác:', e)
  }
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
    p.tongSoLuong += getVariantStock(item)
    if (item.giaBan < p.giaBan) p.giaBan = item.giaBan
    const currentPrice = item.giaSauGiam ?? item.giaBan
    if (currentPrice < p.giaSauGiam) p.giaSauGiam = currentPrice
    if (item.dangGiamGia && (item.phanTramGiam ?? 0) > p.phanTramGiam) {
      p.phanTramGiam = item.phanTramGiam
      p.dangGiamGia = true
    }
    if (!p.colors.includes(item.tenMauSac)) {
      p.colors.push(item.tenMauSac)
    }
  })
  return Object.values(groups)
})

const sameCategoryProducts = computed(() => {
  const currentCategory = selectedVariant.value?.tenDanhMuc
  if (!currentCategory) return []
  return shopProducts.value
    .filter(
      (item) => item.tenDanhMuc === currentCategory && item.idSanPham !== Number(route.params.id),
    )
    .slice(0, 5)
})

const saveToRecentlyViewed = (currentProd) => {
  if (!currentProd) return
  let viewed = JSON.parse(localStorage.getItem('recentlyViewed') || '[]')
  viewed = viewed.filter((item) => item.idSanPham !== currentProd.idSanPham)
  viewed.unshift(currentProd)
  if (viewed.length > 10) viewed = viewed.slice(0, 10)
  localStorage.setItem('recentlyViewed', JSON.stringify(viewed))
  recentlyViewedProducts.value = viewed.filter((item) => item.idSanPham !== Number(route.params.id))
}

const loadRecentlyViewed = () => {
  const viewed = JSON.parse(localStorage.getItem('recentlyViewed') || '[]')
  recentlyViewedProducts.value = viewed.filter((item) => item.idSanPham !== Number(route.params.id))
}

const loadProduct = async () => {
  loading.value = true
  try {
    const data = await getShopVariantsByProductId(route.params.id)
    product.value = data
    if (data.colors?.length) {
      selectedColor.value = data.colors[0]
      if (data.colors[0].variants?.length) {
        selectedVariant.value = data.colors[0].variants[0]
      }
    }
    const firstImg = data.gallery?.[0]?.image || ''
    const currentGrouped = {
      idSanPham: Number(route.params.id),
      tenSanPham: data.productName,
      tenDanhMuc: selectedVariant.value?.tenDanhMuc || '',
      giaBan: selectedVariant.value?.giaBan || 0,
      giaSauGiam: selectedVariant.value?.giaSauGiam || selectedVariant.value?.giaBan || 0,
      dangGiamGia: selectedVariant.value?.dangGiamGia || false,
      phanTramGiam: selectedVariant.value?.phanTramGiam || 0,
      image: firstImg,
    }
    saveToRecentlyViewed(currentGrouped)
  } catch (e) {
    console.error('Lỗi tải sản phẩm chi tiết:', e)
  } finally {
    loading.value = false
  }
}

const increaseQty = () => {
  if (!selectedVariant.value) return
  if (quantity.value < availableStock.value) {
    quantity.value++
  }
}

const decreaseQty = () => {
  if (quantity.value > 1) {
    quantity.value--
  }
}

const galleryImages = computed(() => {
  if (!product.value?.gallery) return []
  return product.value.gallery.map((item) => ({
    ...item,
    url: item.image.startsWith('http') ? item.image : `${API_URL}${item.image}`,
  }))
})

watch(
  selectedVariant,
  (variant) => {
    if (!variant || !product.value) return
    const firstImage = product.value.gallery?.find((g) => g.spctId === variant.id)
    if (firstImage) {
      mainImage.value = firstImage.image.startsWith('http')
        ? firstImage.image
        : `${API_URL}${firstImage.image}`
    } else {
      mainImage.value = ''
    }
    const stock = getVariantStock(variant)
    quantity.value = stock > 0 ? 1 : 0
  },
  { immediate: true },
)

const selectImage = (img) => {
  mainImage.value = img.url
  for (const color of product.value?.colors || []) {
    const variant = color.variants?.find((v) => v.id === img.spctId)
    if (variant) {
      selectedColor.value = color
      selectedVariant.value = variant
      break
    }
  }
}

const selectColor = (color) => {
  selectedColor.value = color
  selectedVariant.value = color.variants?.[0] ?? null
}

const selectVariant = (variant) => {
  selectedVariant.value = variant
}

onMounted(async () => {
  await loadProduct()
  await loadShopProducts()
  loadRecentlyViewed()
  connectSocket()
})

function connectSocket() {
  if (stompClient.connected) {
    subscribeDetail()
  } else {
    stompClient.onConnect = () => {
      subscribeDetail()
    }
  }
}

function subscribeDetail() {
  stompClient.subscribe('/topic/pos', async (msg) => {
    const event = JSON.parse(msg.body)
    if (['DISCOUNT_UPDATED', 'PRODUCT_UPDATED'].includes(event.type)) {
      await loadShopProducts()
      await loadProduct()
    }
  })
}

const addToCart = async () => {
  const token = sessionStorage.getItem('token')
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
    await axios.post(
      `${API_URL}/giohang/them`,
      {
        idSanPhamChiTiet: selectedVariant.value.id,
        soLuong: quantity.value,
      },
      {
        headers: { Authorization: `Bearer ${token}` },
      },
    )
    emitter.emit('cart-updated')
    alert('Đã thêm sản phẩm vào giỏ hàng!')
  } catch (err) {
    console.error(err)
    alert(err?.response?.data || 'Lỗi thêm giỏ hàng')
  }
}

const buyNow = () => {
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
    loadRecentlyViewed()
    window.scrollTo({ top: 0, behavior: 'smooth' })
  },
)
</script>
