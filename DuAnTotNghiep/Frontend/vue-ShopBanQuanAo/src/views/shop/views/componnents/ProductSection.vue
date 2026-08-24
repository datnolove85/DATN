<script setup>
import { Package } from 'lucide-vue-next'

defineProps({
  isCartCheckout: Boolean,
  checkoutItems: Array,
  product: Object,
  quantity: Number,
  maxAvailable: Number,
  formatMoney: Function,
})

defineEmits([
  'decreaseCartQty',
  'increaseCartQty',
  'validateCartQty',
  'onCartQtyBlur',
  'decreaseQty',
  'increaseQty',
  'update:quantity',
  'onQtyBlur',
])

// Hàm chặn các ký tự không phải số (e, E, +, -, .)
const restrictNumberKeys = (event) => {
  if (['e', 'E', '+', '-', '.'].includes(event.key)) {
    event.preventDefault()
  }
}
</script>

<template>
  <!-- MỚI (liền mạch, không viền ngoài) -->
  <section class="bg-transparent">
    <!-- Header đồng bộ -->
    <div class="flex items-center gap-2.5 mb-3.5">
      <span
        class="grid h-9 w-9 shrink-0 place-items-center rounded-xl bg-indigo-50 text-indigo-600"
      >
        <Package :size="18" />
      </span>
      <div>
        <h2 class="text-sm font-bold tracking-tight text-slate-900 sm:text-base">
          Sản phẩm đã chọn
        </h2>
      </div>
    </div>

    <div class="space-y-3">
      <!-- ==================== CHECKOUT TỪ GIỎ HÀNG ==================== -->
      <template v-if="isCartCheckout">
        <article
          v-for="item in checkoutItems"
          :key="item.productDetailId"
          class="group flex flex-col sm:flex-row gap-3.5 rounded-xl border border-slate-200/80 bg-white p-3.5 shadow-2xs transition hover:border-indigo-200 hover:bg-slate-50/60"
        >
          <!-- Ảnh sản phẩm -->
          <div
            class="h-20 w-20 sm:h-24 sm:w-24 shrink-0 overflow-hidden rounded-lg border border-slate-200 bg-slate-100"
          >
            <img
              :src="
                item.anh
                  ? item.anh.startsWith('http')
                    ? item.anh
                    : 'http://localhost:8080' + item.anh
                  : '/no-image.png'
              "
              :alt="item.tenSanPham"
              class="h-full w-full object-cover"
            />
          </div>

          <!-- Thông tin chi tiết -->
          <div class="flex min-w-0 flex-1 flex-col justify-between">
            <div>
              <div class="flex items-start justify-between gap-2">
                <h3 class="text-xs font-bold text-slate-900 sm:text-sm line-clamp-2">
                  {{ item.tenSanPham }}
                </h3>
                <span
                  class="inline-flex shrink-0 items-center rounded bg-slate-100 px-1.5 py-0.5 text-[10px] font-semibold text-slate-500"
                >
                  #{{ item.maSanPhamChiTiet }}
                </span>
              </div>

              <!-- Thẻ thuộc tính & Tồn kho -->
              <div class="mt-1.5 flex flex-wrap items-center gap-1.5 text-[11px]">
                <span
                  v-if="item.mauSac"
                  class="inline-flex items-center gap-1 rounded bg-slate-100 px-2 py-0.5 font-medium text-slate-700"
                >
                  🎨 {{ item.mauSac }}
                </span>
                <span
                  v-if="item.kichCo"
                  class="inline-flex items-center gap-1 rounded bg-slate-100 px-2 py-0.5 font-medium text-slate-700"
                >
                  📏 {{ item.kichCo }}
                </span>
                <span
                  class="inline-flex items-center gap-1 rounded bg-emerald-50 px-2 py-0.5 font-semibold text-emerald-700 border border-emerald-200/60"
                >
                  📦 Khả dụng: {{ item.soLuongKhaDung ?? 0 }}
                </span>
              </div>
            </div>

            <!-- Đơn giá, Số lượng & Thành tiền -->
            <div
              class="mt-3 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 pt-2.5 border-t border-slate-100"
            >
              <!-- Đơn giá -->
              <div class="flex items-baseline gap-1.5">
                <span class="text-xs text-slate-400 uppercase tracking-wider font-medium"
                  >Đơn giá:</span
                >
                <span class="text-xs sm:text-sm font-bold text-slate-900">
                  {{ formatMoney(item.giaSauGiam || item.giaBan) }}
                </span>
                <span
                  v-if="item.giaSauGiam && item.giaSauGiam < item.giaBan"
                  class="text-[11px] text-slate-400 line-through"
                >
                  {{ formatMoney(item.giaBan) }}
                </span>
              </div>

              <!-- Bộ tăng giảm số lượng & Thành tiền + Cảnh báo -->
              <div class="flex flex-col items-end gap-1">
                <div class="flex items-center justify-between sm:justify-end gap-4">
                  <div
                    class="flex items-center overflow-hidden rounded-lg border border-slate-200 bg-white"
                  >
                    <button
                      type="button"
                      class="flex h-7 w-7 items-center justify-center text-slate-600 transition-colors hover:bg-slate-100 disabled:opacity-30 text-xs"
                      :disabled="
                        item.quantity <= 1 || (item.soLuongKhaDung ?? item.soLuongTon ?? 0) === 0
                      "
                      @click="$emit('decreaseCartQty', item)"
                    >
                      -
                    </button>

                    <input
                      type="number"
                      :value="item.quantity"
                      @keydown="restrictNumberKeys"
                      @input="$emit('validateCartQty', item, $event)"
                      @blur="$emit('onCartQtyBlur', item)"
                      min="0"
                      :max="item.soLuongKhaDung ?? item.soLuongTon ?? 0"
                      class="h-7 w-10 border-x border-slate-200 bg-white text-center text-xs font-bold text-slate-800 outline-none [appearance:textfield] [&::-webkit-inner-spin-button]:appearance-none [&::-webkit-outer-spin-button]:appearance-none"
                    />

                    <button
                      type="button"
                      class="flex h-7 w-7 items-center justify-center text-slate-600 transition-colors hover:bg-slate-100 disabled:opacity-30 text-xs"
                      :disabled="item.quantity >= (item.soLuongKhaDung ?? item.soLuongTon ?? 0)"
                      @click="$emit('increaseCartQty', item)"
                    >
                      +
                    </button>
                  </div>

                  <div class="text-right">
                    <span class="text-xs sm:text-sm font-extrabold text-rose-600">
                      {{ formatMoney((item.giaSauGiam || item.giaBan) * (item.quantity || 0)) }}
                    </span>
                  </div>
                </div>

                <!-- Text cảnh báo giỏ hàng -->
                <span
                  v-if="item.quantity > (item.soLuongKhaDung ?? item.soLuongTon ?? 0)"
                  class="text-[11px] text-rose-500 font-medium"
                >
                  Vượt quá số lượng khả dụng!
                </span>
              </div>
            </div>
          </div>
        </article>
      </template>

      <!-- ==================== MUA NGAY ==================== -->
      <template v-else>
        <article
          class="group flex flex-col sm:flex-row gap-3.5 rounded-xl border border-slate-200/80 bg-white p-3.5 shadow-2xs transition hover:border-indigo-200 hover:bg-slate-50/60"
        >
          <!-- Ảnh sản phẩm -->
          <div
            class="h-20 w-20 sm:h-24 sm:w-24 shrink-0 overflow-hidden rounded-lg border border-slate-200 bg-slate-100"
          >
            <img
              :src="
                product?.images?.length
                  ? product.images[0].startsWith('http')
                    ? product.images[0]
                    : 'http://localhost:8080' + product.images[0]
                  : '/no-image.png'
              "
              :alt="product?.tenSanPham"
              class="h-full w-full object-cover"
            />
          </div>

          <!-- Thông tin chi tiết -->
          <div class="flex min-w-0 flex-1 flex-col justify-between">
            <div>
              <div class="flex items-start justify-between gap-2">
                <h3 class="text-xs font-bold text-slate-900 sm:text-sm line-clamp-2">
                  {{ product?.tenSanPham }}
                </h3>
                <span
                  class="inline-flex shrink-0 items-center rounded bg-slate-100 px-1.5 py-0.5 text-[10px] font-semibold text-slate-500"
                >
                  #{{ product?.maSanPhamChiTiet }}
                </span>
              </div>

              <!-- Thẻ thuộc tính & Tồn kho -->
              <div class="mt-1.5 flex flex-wrap items-center gap-1.5 text-[11px]">
                <span
                  v-if="product?.tenMauSac"
                  class="inline-flex items-center gap-1 rounded bg-slate-100 px-2 py-0.5 font-medium text-slate-700"
                >
                  🎨 {{ product?.tenMauSac }}
                </span>
                <span
                  v-if="product?.tenKichThuoc"
                  class="inline-flex items-center gap-1 rounded bg-slate-100 px-2 py-0.5 font-medium text-slate-700"
                >
                  📏 {{ product?.tenKichThuoc }}
                </span>
                <span
                  class="inline-flex items-center gap-1 rounded bg-emerald-50 px-2 py-0.5 font-semibold text-emerald-700 border border-emerald-200/60"
                >
                  📦 Khả dụng: {{ maxAvailable }}
                </span>
              </div>
            </div>

            <!-- Đơn giá, Số lượng & Thành tiền -->
            <div
              class="mt-3 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 pt-2.5 border-t border-slate-100"
            >
              <!-- Đơn giá -->
              <div class="flex items-baseline gap-1.5">
                <span class="text-xs text-slate-400 uppercase tracking-wider font-medium"
                  >Đơn giá:</span
                >
                <span class="text-xs sm:text-sm font-bold text-slate-900">
                  {{ formatMoney(product?.giaSauGiam || product?.giaBan) }}
                </span>
                <span
                  v-if="
                    product?.dangGiamGia ||
                    (product?.giaSauGiam && product?.giaSauGiam < product?.giaBan)
                  "
                  class="text-[11px] text-slate-400 line-through"
                >
                  {{ formatMoney(product?.giaBan) }}
                </span>
              </div>

              <!-- Bộ tăng giảm số lượng & Thành tiền + Cảnh báo -->
              <div class="flex flex-col items-end gap-1">
                <div class="flex items-center justify-between sm:justify-end gap-4">
                  <div
                    class="flex items-center overflow-hidden rounded-lg border border-slate-200 bg-white"
                  >
                    <button
                      type="button"
                      class="flex h-7 w-7 items-center justify-center text-slate-600 transition-colors hover:bg-slate-100 disabled:opacity-30 text-xs"
                      :disabled="quantity <= 1 || maxAvailable === 0"
                      @click="$emit('decreaseQty')"
                    >
                      -
                    </button>

                    <input
                      type="number"
                      :value="quantity"
                      @keydown="restrictNumberKeys"
                      @input="
                        $emit('update:quantity', Number($event.target.value.replace(/\D/g, '')))
                      "
                      @blur="$emit('onQtyBlur')"
                      min="0"
                      :max="maxAvailable"
                      class="h-7 w-10 border-x border-slate-200 bg-white text-center text-xs font-bold text-slate-800 outline-none [appearance:textfield] [&::-webkit-inner-spin-button]:appearance-none [&::-webkit-outer-spin-button]:appearance-none"
                    />

                    <button
                      type="button"
                      class="flex h-7 w-7 items-center justify-center text-slate-600 transition-colors hover:bg-slate-100 disabled:opacity-30 text-xs"
                      :disabled="quantity >= maxAvailable || maxAvailable === 0"
                      @click="$emit('increaseQty')"
                    >
                      +
                    </button>
                  </div>

                  <div class="text-right">
                    <span class="text-xs sm:text-sm font-extrabold text-rose-600">
                      {{
                        formatMoney((product?.giaSauGiam || product?.giaBan || 0) * (quantity || 0))
                      }}
                    </span>
                  </div>
                </div>

                <!-- Text cảnh báo mua ngay -->
                <span
                  v-if="quantity > maxAvailable && maxAvailable > 0"
                  class="text-[11px] text-rose-500 font-medium"
                >
                  Vượt quá số lượng khả dụng!
                </span>
              </div>
            </div>
          </div>
        </article>
      </template>
    </div>
  </section>
</template>
