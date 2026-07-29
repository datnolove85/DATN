<template>
  <div
    class="min-h-screen bg-neutral-50 text-neutral-800 antialiased pb-20 font-sans text-xs sm:text-sm"
  >
    <!-- ==================== HEADER ĐỒNG BỘ CHUẨN FILE 1 ==================== -->
    <header class="bg-white border-b border-neutral-200 sticky top-0 z-50 shadow-xs">
      <div class="max-w-5xl mx-auto px-4 h-16 flex items-center justify-between gap-4">
        <!-- Khối Logo và Nhãn trang chuẩn File 1 -->
        <div class="flex items-center gap-3">
          <div class="flex items-center gap-1.5 cursor-pointer">
            <div
              class="w-6 h-6 bg-[#ee4d2d] rounded-full flex items-center justify-center text-white font-black text-sm relative -top-[1px]"
            >
              <span class="transform -translate-y-[1px]">v</span>
            </div>
            <span class="text-xl font-extrabold text-[#ee4d2d] tracking-tighter lowercase"
              >velora</span
            >
          </div>
          <div class="h-4 w-[1px] bg-neutral-200 hidden sm:block"></div>
          <span class="text-sm font-medium text-neutral-600 sm:text-neutral-700">Thanh Toán</span>
        </div>

        <!-- Thanh tìm kiếm rút gọn tinh tế -->
        <div class="hidden md:flex flex-1 max-w-sm relative">
          <input
            type="text"
            placeholder="Tìm kiếm sản phẩm, phụ kiện..."
            class="w-full bg-neutral-50 border border-neutral-200 focus:border-neutral-300 rounded pl-3 pr-8 py-1.5 outline-none text-xs transition"
          />
          <Search
            class="w-4 h-4 text-neutral-400 absolute right-2.5 top-1/2 -translate-y-1/2 pointer-events-none"
          />
        </div>

        <!-- Thông tin User góc phải (Đã bỏ giỏ hàng) -->
        <div class="flex items-center gap-2 cursor-pointer group">
          <div
            class="w-7 h-7 rounded-full bg-neutral-200 flex items-center justify-center text-neutral-600 font-semibold text-xs border border-neutral-300 overflow-hidden"
          >
            <span class="group-hover:scale-105 transition duration-200">Đ</span>
          </div>
          <span
            class="text-xs font-medium text-neutral-600 group-hover:text-neutral-900 transition hidden sm:block"
          >
            {{ checkoutData.user.name }}
          </span>
        </div>
      </div>
    </header>

    <!-- ==================== NỘI DUNG CHÍNH TRANG ==================== -->
    <div class="max-w-5xl mx-auto px-4 space-y-4 mt-5">
      <!-- SECTION 1: ĐỊA CHỈ NHẬN HÀNG -->
      <div
        class="bg-white rounded border border-neutral-200/80 p-5 shadow-xs relative overflow-hidden"
      >
        <div
          class="absolute top-0 left-0 right-0 h-1 bg-linear-to-r from-[#ee4d2d] via-orange-400 to-[#ee4d2d]"
        ></div>
        <div
          class="flex items-center gap-2 text-[#ee4d2d] text-sm font-semibold uppercase tracking-wider mb-3"
        >
          <MapPin class="w-4 h-4" />
          Địa Chỉ Nhận Hàng
        </div>
        <div
          class="flex flex-col sm:flex-row sm:items-center justify-between gap-3 bg-neutral-50 p-3 rounded border border-neutral-100"
        >
          <div class="flex flex-col sm:flex-row sm:items-center gap-2 sm:gap-4 flex-wrap">
            <span class="font-bold text-neutral-900">
              {{ checkoutData.user.name }} {{ checkoutData.user.phone }}
            </span>
            <span class="text-neutral-600 font-normal">
              {{ checkoutData.user.address }}
            </span>
            <span
              class="text-[10px] text-[#ee4d2d] bg-orange-50 border border-orange-200 px-1.5 py-0.5 rounded-xs font-semibold uppercase tracking-tight w-fit"
            >
              Mặc Định
            </span>
          </div>
          <button
            class="text-blue-600 hover:text-blue-700 font-medium text-xs flex-shrink-0 self-start sm:self-auto transition"
          >
            Thay Đổi
          </button>
        </div>
      </div>

      <!-- SECTION 2: CHI TIẾT SẢN PHẨM -->
      <div class="bg-white rounded border border-neutral-200/80 shadow-xs overflow-hidden">
        <!-- Header bảng -->
        <div
          class="hidden md:grid grid-cols-12 gap-4 px-5 py-3 text-neutral-400 font-medium border-b border-neutral-100 bg-neutral-50/50 text-xs uppercase tracking-wider"
        >
          <div class="col-span-6 text-neutral-700 font-semibold">Sản phẩm</div>
          <div class="col-span-2 text-center">Đơn giá</div>
          <div class="col-span-2 text-center">Số lượng</div>
          <div class="col-span-2 text-right">Thành tiền</div>
        </div>

        <div class="px-5 py-3 border-b border-neutral-100 flex items-center gap-2 bg-white">
          <span
            class="bg-[#ee4d2d] text-white text-[10px] font-bold px-1.5 py-0.5 rounded-xs uppercase tracking-wider"
          >
            Velora Xử Lý
          </span>
          <span class="font-semibold text-neutral-800 text-xs sm:text-sm">Đơn hàng số #VL101</span>
        </div>

        <!-- Thông tin sản phẩm chi tiết -->
        <div
          class="p-5 grid grid-cols-1 md:grid-cols-12 gap-4 items-center border-b border-neutral-100 bg-white"
        >
          <div class="col-span-1 md:col-span-6 flex gap-4">
            <img
              :src="checkoutData.product.image"
              class="w-20 h-20 object-cover border border-neutral-200 rounded p-0.5 bg-white flex-shrink-0"
            />
            <div class="space-y-1.5">
              <h3 class="font-medium text-neutral-900 leading-snug line-clamp-2">
                {{ checkoutData.product.name }}
              </h3>
              <div
                class="inline-flex items-center gap-1.5 bg-neutral-100 text-neutral-600 text-xs px-2 py-0.5 rounded-sm"
              >
                <span class="text-neutral-400">Phân loại:</span>
                <span class="font-medium"
                  >{{ checkoutData.product.color }} / {{ checkoutData.product.size }}</span
                >
              </div>
            </div>
          </div>

          <div
            class="col-span-1 md:col-span-2 flex justify-between md:justify-center items-center font-medium text-neutral-700"
          >
            <span class="md:hidden text-neutral-400 font-normal">Đơn giá:</span>
            {{ formatPrice(checkoutData.product.price) }}
          </div>

          <div
            class="col-span-1 md:col-span-2 flex justify-between md:justify-center items-center text-neutral-600"
          >
            <span class="md:hidden text-neutral-400 font-normal">Số lượng:</span>
            <span class="font-medium">x{{ checkoutData.product.quantity }}</span>
          </div>

          <div
            class="col-span-1 md:col-span-2 flex justify-between md:text-right items-center font-semibold text-neutral-900"
          >
            <span class="md:hidden text-neutral-400 font-normal">Thành tiền:</span>
            {{ formatPrice(checkoutData.product.price * checkoutData.product.quantity) }}
          </div>
        </div>

        <!-- Lời nhắn và Vận chuyển -->
        <div
          class="bg-neutral-50/50 p-4 border-b border-neutral-100 border-dashed grid grid-cols-1 md:grid-cols-12 gap-4"
        >
          <div
            class="md:col-span-5 border-r-0 md:border-r border-neutral-200 border-dashed pr-0 md:pr-5 flex items-center gap-3"
          >
            <span class="text-neutral-500 font-medium text-xs whitespace-nowrap">Lời nhắn:</span>
            <input
              type="text"
              placeholder="Lưu ý cho Người bán (màu sắc, thời gian giao hàng...)"
              class="w-full bg-white border border-neutral-200 rounded px-3 py-1.5 outline-none text-xs focus:border-neutral-400 transition placeholder:text-neutral-300"
            />
          </div>
          <div
            class="md:col-span-7 flex flex-col sm:flex-row justify-between items-start sm:items-center gap-3 pl-0 md:pl-5"
          >
            <div class="space-y-0.5">
              <div class="flex items-center gap-1.5 text-emerald-600 font-medium text-xs">
                <Truck class="w-4 h-4" />
                Phương thức vận chuyển: Nhanh Hỏa Tốc
              </div>
              <div class="text-xs text-neutral-400 pl-5">Dự kiến nhận hàng vào 3 - 5 ngày tới</div>
            </div>
            <div class="flex items-center gap-4 self-end sm:self-auto">
              <button class="text-blue-600 text-xs font-medium hover:underline transition">
                Thay Đổi
              </button>
              <span class="font-semibold text-neutral-800">{{
                formatPrice(checkoutData.shippingFee)
              }}</span>
            </div>
          </div>
        </div>

        <!-- Voucher Shop -->
        <div
          class="px-5 py-3.5 flex justify-between items-center bg-white border-b border-neutral-100"
        >
          <div class="flex items-center gap-2 text-neutral-700 font-medium">
            <TicketIcon class="w-4 h-4 text-orange-500" />
            Voucher giảm giá của Shop
          </div>
          <button class="text-blue-600 font-medium text-xs hover:underline transition">
            Chọn Voucher
          </button>
        </div>

        <!-- Tổng tiền hàng -->
        <div class="px-5 py-4 bg-neutral-50/40 text-right text-xs text-neutral-500">
          Tổng số tiền ({{ checkoutData.product.quantity }} sản phẩm):
          <span class="text-lg font-bold text-[#ee4d2d] ml-2">
            {{ formatPrice(totalProductCost) }}
          </span>
        </div>
      </div>

      <!-- SECTION 3: VOUCHER HỆ THỐNG & XU -->
      <div
        class="bg-white rounded border border-neutral-200/80 shadow-xs divide-y divide-neutral-100 overflow-hidden"
      >
        <div class="p-4 flex justify-between items-center">
          <div class="flex items-center gap-2 text-neutral-700 font-medium">
            <Ticket class="w-4 h-4 text-[#ee4d2d]" />
            Velora Voucher Toàn Sàn
          </div>
          <button class="text-blue-600 font-medium text-xs hover:underline transition">
            Chọn Mã Giảm Giá
          </button>
        </div>
        <div class="p-4 flex justify-between items-center">
          <div class="flex items-center gap-2 text-neutral-600 font-medium">
            <Coins class="w-4 h-4 text-amber-500" />
            Dùng 100 Xu tích lũy cá nhân
          </div>
          <input
            type="checkbox"
            v-model="useCoin"
            class="w-4 h-4 accent-[#ee4d2d] cursor-pointer rounded border-neutral-300 focus:ring-0"
          />
        </div>
      </div>

      <!-- SECTION 4: PHƯƠNG THỨC THANH TOÁN & CHI PHÍ TỔNG -->
      <div class="bg-white rounded border border-neutral-200/80 shadow-xs overflow-hidden">
        <div
          class="p-5 border-b border-neutral-100 flex flex-col sm:flex-row sm:items-center justify-between gap-3 bg-white"
        >
          <div class="font-semibold text-sm sm:text-base text-neutral-900">
            Phương thức thanh toán
          </div>
          <div class="flex items-center gap-4">
            <span
              class="text-neutral-700 font-medium bg-neutral-50 px-3 py-1 rounded border border-neutral-100"
            >
              Thanh toán khi nhận hàng (COD)
            </span>
            <button class="text-blue-600 hover:underline font-medium text-xs transition">
              THAY ĐỔI
            </button>
          </div>
        </div>

        <div
          class="p-6 bg-amber-50/10 flex flex-col items-end border-b border-neutral-100 border-dashed space-y-3"
        >
          <div
            class="w-full max-w-sm grid grid-cols-2 text-xs text-neutral-500 text-right gap-y-2.5"
          >
            <div>Tổng tiền hàng gốc</div>
            <div class="text-neutral-800 font-semibold">{{ formatPrice(totalProductCost) }}</div>

            <div>Phí vận chuyển gốc</div>
            <div class="text-neutral-800 font-semibold">
              {{ formatPrice(checkoutData.shippingFee) }}
            </div>

            <div v-if="useCoin">Ưu đãi giảm giá từ xu</div>
            <div v-if="useCoin" class="text-[#ee4d2d] font-semibold">-{{ formatPrice(1000) }}</div>

            <div class="text-neutral-800 text-sm font-semibold mt-2 flex items-center justify-end">
              Tổng thanh toán cuối cùng
            </div>
            <div class="text-2xl sm:text-3xl font-extrabold text-[#ee4d2d] mt-2 tracking-tight">
              {{ formatPrice(finalTotal) }}
            </div>
          </div>
        </div>

        <div
          class="p-5 sm:p-6 flex flex-col sm:flex-row sm:items-center justify-between gap-4 bg-neutral-50/50"
        >
          <p class="text-xs text-neutral-400 leading-normal max-w-md">
            Bằng việc nhấn nút "Đặt hàng", bạn xác nhận đồng ý tuân thủ theo các quy định và
            <a href="#" class="text-blue-600 hover:underline font-medium"
              >Điều khoản sử dụng Velora</a
            >.
          </p>
          <button
            @click="processOrder"
            class="bg-[#ee4d2d] text-white px-12 py-3 rounded font-semibold text-sm sm:text-base hover:bg-[#d73211] active:scale-[0.99] transition-all duration-150 w-full sm:w-auto shadow-sm shadow-orange-500/10 tracking-wider uppercase"
          >
            Đặt hàng ngay
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { MapPin, Truck, Ticket, TicketIcon, Coins, Search } from 'lucide-vue-next'

const checkoutData = ref({
  user: {
    name: 'Nguyễn Quang Đạt',
    phone: '(+84) 377 280 805',
    address: 'Cổng Làng Dinh Chu, Xóm 5, Xã Đại Đồng, Tỉnh Nghệ An',
  },
  product: {
    id: 101,
    name: 'Điện thoại Apple iPhone 13 128GB chính hãng bản VN/A',
    image:
      'https://images.unsplash.com/photo-1639491957454-61b402941544?q=80&w=300&auto=format&fit=crop',
    color: 'Midnight',
    size: '128GB',
    price: 16990000,
    quantity: 1,
  },
  shippingFee: 42500,
})

const useCoin = ref(false)

const totalProductCost = computed(() => {
  return checkoutData.value.product.price * checkoutData.value.product.quantity
})

const finalTotal = computed(() => {
  let total = totalProductCost.value + checkoutData.value.shippingFee
  if (useCoin.value) {
    total -= 1000
  }
  return total
})

const formatPrice = (value) => {
  if (!value && value !== 0) return '0 ₫'
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
  }).format(value)
}

const processOrder = () => {
  alert('🎉 Hệ thống đang xử lý đơn hàng của bạn! Xin cảm ơn.')
}
</script>

<style scoped>
:deep(.rounded) {
  border-radius: 4px !important;
}
:deep(.rounded-sm) {
  border-radius: 2px !important;
}
:deep(.rounded-xs) {
  border-radius: 1px !important;
}
</style>
