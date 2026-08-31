<template>
  <section class="lg:col-span-5 xl:col-span-4 h-[calc(100vh-1rem)] sticky top-2 flex flex-col">
    <div
      class="bg-white rounded-2xl border border-slate-200/90 shadow-xl flex flex-col h-full overflow-hidden"
    >
      <!-- 1. HEADER KHÁCH HÀNG (CỐ ĐỊNH) -->
      <div class="p-3 border-b border-slate-100 bg-slate-50/60 flex-shrink-0 space-y-2">
        <div class="flex items-center justify-between">
          <div class="flex items-center gap-1.5">
            <label class="text-[11px] font-extrabold text-slate-500 uppercase tracking-wider">
              Giỏ Hàng & Khách Hàng
            </label>
            <span
              v-if="currentOrder?.cart?.length"
              class="bg-indigo-100 text-indigo-700 text-[10px] font-extrabold px-2 py-0.5 rounded-full"
            >
              {{ currentOrder.cart.reduce((sum, item) => sum + item.soLuong, 0) }} món
            </span>
          </div>
          <button
            @click="emit('openCustomerModal')"
            class="text-xs font-bold text-indigo-600 hover:text-indigo-700 flex items-center gap-1 transition-colors"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="h-3.5 w-3.5"
              viewBox="0 0 20 20"
              fill="currentColor"
            >
              <path
                d="M8 9a3 3 0 100-6 3 3 0 000 6zM8 11a6 6 0 016 6H2a6 6 0 016-6zM16 7a1 1 0 10-2 0v1h-1a1 1 0 100 2h1v1a1 1 0 102 0v-1h1a1 1 0 100-2h-1V7z"
              />
            </svg>
            {{ selectedCustomer ? 'Đổi' : 'Chọn' }}
          </button>
        </div>

        <!-- Thông tin khách hàng -->
        <div
          class="bg-white border border-slate-200/80 rounded-xl px-3 py-1.5 flex items-center justify-between text-xs"
        >
          <template v-if="selectedCustomer">
            <div class="flex items-center gap-2 truncate">
              <span class="font-bold text-slate-800 truncate">{{ selectedCustomer.hoTen }}</span>
              <span class="text-[11px] text-slate-400 font-medium truncate">
                {{ selectedCustomer.soDienThoai }}
              </span>
            </div>
            <button
              @click="emit('removeCustomer')"
              class="text-slate-400 hover:text-rose-500 p-0.5 transition-colors flex-shrink-0"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="h-3.5 w-3.5"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M6 18L18 6M6 6l12 12"
                />
              </svg>
            </button>
          </template>
          <template v-else>
            <span class="font-bold text-slate-400">Khách lẻ</span>
          </template>
        </div>
      </div>

      <!-- 2. DANH SÁCH SẢN PHẨM -->
      <div class="flex-1 overflow-y-auto min-h-0 p-3 space-y-2 custom-scrollbar">
        <div
          v-if="currentOrder?.cart?.length === 0"
          class="h-full flex flex-col items-center justify-center text-slate-400 py-8"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-10 w-10 text-slate-300 mb-2"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="1.5"
              d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 0a2 2 0 11-4 0 2 2 0 014 0z"
            />
          </svg>
          <p class="text-xs font-semibold">Giỏ hàng trống. Chọn sản phẩm để bắt đầu.</p>
        </div>

        <div
          v-for="(item, index) in currentOrder?.cart || []"
          :key="item.id"
          class="flex items-center gap-2.5 bg-white p-2.5 rounded-xl border border-slate-200/70 hover:border-indigo-200 transition-all shadow-2xs"
        >
          <img
            :src="getProductImage(item.product)"
            class="w-11 h-11 rounded-lg object-cover bg-slate-50 flex-shrink-0 border border-slate-100"
            @error="setDefaultImage"
          />

          <div class="flex-1 min-w-0">
            <h4 class="text-xs font-bold text-slate-800 truncate leading-snug">
              {{ item.product.tenSanPhamChiTiet }}
            </h4>
            <div class="flex items-center gap-1 mt-0.5">
              <span
                class="text-[9px] font-extrabold text-indigo-700 bg-indigo-50 px-1 py-0.2 rounded truncate max-w-[65px]"
                :title="item.product.maSanPhamChiTiet"
              >
                {{ item.product.maSPCT }}
              </span>
              <span class="text-[10px] text-slate-400 truncate">
                {{ item.product.tenMauSac }} / {{ item.product.tenKichThuoc }}
              </span>
            </div>
            <div class="mt-0.5">
              <template v-if="item.product.dangGiamGia">
                <p class="text-xs font-black text-rose-600 flex items-center gap-1 flex-wrap">
                  <span>{{ formatPrice(item.product.giaSauGiam) }}</span>
                  <span
                    v-if="getVariantDiscountPercent(item.product) > 0"
                    class="text-[9px] bg-rose-100 text-rose-700 font-bold px-1 rounded"
                  >
                    -{{ getVariantDiscountPercent(item.product) }}%
                  </span>
                </p>
              </template>
              <template v-else>
                <p class="text-xs font-black text-indigo-600">
                  {{ formatPrice(item.product.giaBan) }}
                </p>
              </template>
            </div>
          </div>

          <!-- Tăng / Giảm Số Lượng -->
          <div
            class="flex items-center bg-slate-100/80 rounded-lg p-0.5 border border-slate-200/60 flex-shrink-0"
          >
            <button
              @click="emit('decreaseQty', item)"
              :disabled="item.soLuong <= 1"
              class="w-4 h-4 flex items-center justify-center text-slate-600 hover:bg-white rounded transition-colors text-xs font-bold disabled:opacity-40 disabled:cursor-not-allowed select-none"
            >
              -
            </button>
            <input
              type="number"
              min="1"
              v-model.number="item.soLuong"
              @input="emit('changeQty', item)"
              class="w-7 h-4 text-center text-xs font-extrabold text-slate-800 bg-transparent outline-none [appearance:textfield] [&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none"
            />
            <button
              @click="emit('increaseQty', item)"
              class="w-4 h-4 flex items-center justify-center text-slate-600 hover:bg-white rounded transition-colors text-xs font-bold select-none"
            >
              +
            </button>
          </div>

          <button
            @click="emit('removeFromCart', index)"
            class="text-slate-300 hover:text-rose-500 transition-colors p-0.5 flex-shrink-0"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="h-4 w-4"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
              />
            </svg>
          </button>
        </div>
      </div>

      <!-- 3. KHỐI THANH TOÁN LIỀN MẠCH Ở ĐÁY -->
      <div class="flex-shrink-0 border-t border-slate-200/90 bg-slate-50/70 p-3.5 space-y-3 z-10">
        <!-- KHUYẾN MÃI & TÍCH XU -->
        <div class="space-y-2">
          <!-- Voucher Dropdown (ĐÃ BỔ SUNG HIỂN THỊ ĐƠN TỐI THIỂU) -->
          <div class="relative" ref="voucherRef">
            <div
              class="w-full bg-white border border-slate-200 hover:border-indigo-300 rounded-xl px-3 py-2 text-xs font-bold text-slate-700 flex justify-between items-center transition-all shadow-2xs cursor-pointer"
              @click="showVoucherDropdown = !showVoucherDropdown"
            >
              <div class="flex-1 flex items-center pr-1 truncate">
                <template v-if="selectedVoucher || appliedVoucher">
                  <div class="flex items-center gap-1.5 truncate">
                    <span class="font-bold text-indigo-600 text-xs truncate flex-shrink-0">
                      🎟️ {{ (selectedVoucher || appliedVoucher).tenVoucher || 'Mã giảm giá' }}
                    </span>
                    <span class="text-[10px] text-slate-400 font-normal truncate">
                      (Đơn từ
                      {{
                        formatPrice(
                          (selectedVoucher || appliedVoucher).dieuKienToiThieu ||
                            (selectedVoucher || appliedVoucher).giaTriDonHangToiThieu ||
                            0,
                        )
                      }}
                      <template v-if="(selectedVoucher || appliedVoucher).giaTriGiamToiDa">
                        • Tối đa
                        {{
                          formatPrice((selectedVoucher || appliedVoucher).giaTriGiamToiDa)
                        }} </template
                      >)
                    </span>
                  </div>
                </template>
                <span v-else class="text-slate-400 font-normal text-xs">Chọn mã khuyến mãi...</span>
              </div>

              <div class="flex items-center gap-1.5 pl-2 border-l border-slate-100 flex-shrink-0">
                <button
                  v-if="selectedVoucher || appliedVoucher"
                  @click.stop="emit('removeVoucher')"
                  class="text-slate-400 hover:text-rose-600 transition-colors px-0.5"
                  title="Xóa voucher"
                >
                  ✕
                </button>
                <span class="text-slate-400 text-[10px]">
                  {{ showVoucherDropdown ? '▲' : '▼' }}
                </span>
              </div>
            </div>

            <!-- List Vouchers Dropdown -->
            <div
              v-if="showVoucherDropdown && filteredVouchers.length > 0"
              class="absolute bottom-full left-0 right-0 mb-1 bg-white border border-slate-200 rounded-xl shadow-2xl z-[100] max-h-52 overflow-y-auto custom-scrollbar p-2 space-y-1.5"
            >
              <div
                v-for="vc in filteredVouchers"
                :key="vc.id"
                @mousedown.prevent="isVoucherValid(vc) ? onSelectVoucher(vc) : null"
                :class="[
                  'p-2 rounded-lg border transition-all flex flex-col gap-1 text-xs',
                  isVoucherValid(vc)
                    ? 'cursor-pointer bg-white hover:border-indigo-300 hover:bg-indigo-50/40'
                    : 'opacity-60 bg-slate-50 border-slate-200 cursor-not-allowed',
                  appliedVoucher?.id === vc.id || selectedVoucher?.id === vc.id
                    ? 'border-indigo-500 bg-indigo-50/60 shadow-xs'
                    : 'border-slate-100',
                ]"
              >
                <div class="flex items-center justify-between">
                  <span class="font-bold text-slate-800">{{ vc.tenVoucher }}</span>
                  <span class="font-bold text-rose-600">
                    {{
                      vc.loaiGiamGia === 'tien_mat'
                        ? formatPrice(vc.giaTriGiam)
                        : vc.giaTriGiam + '%'
                    }}
                  </span>
                </div>

                <div class="flex justify-between items-center text-[11px] text-slate-500">
                  <span
                    >Đơn từ:
                    <b>{{ formatPrice(vc.dieuKienToiThieu || vc.giaTriDonHangToiThieu) }}</b></span
                  >
                  <span v-if="vc.giaTriGiamToiDa"
                    >Tối đa: <b>{{ formatPrice(vc.giaTriGiamToiDa) }}</b></span
                  >
                </div>
                <div
                  v-if="!isVoucherValid(vc)"
                  class="text-rose-600 font-semibold text-[10px] text-right"
                >
                  🔒 {{ getVoucherError(vc) }}
                </div>
              </div>
            </div>
          </div>

          <!-- Đổi Xu -->
          <div
            v-if="selectedCustomer && (selectedCustomer.soDuXu ?? 0) > 0"
            class="bg-amber-50/90 border border-amber-200/80 rounded-xl p-2 flex items-center justify-between gap-2"
          >
            <div class="flex items-center gap-1 text-xs min-w-0">
              <span class="font-bold text-amber-800 whitespace-nowrap"
                >🪙 {{ remainingCoins }} xu</span
              >
            </div>

            <div class="flex items-center gap-1 flex-shrink-0">
              <input
                type="text"
                :value="coinsUsed > 0 ? coinsUsed : ''"
                @input="emit('coinsInput', $event)"
                placeholder="Nhập xu"
                class="w-16 bg-white border border-amber-300 rounded-lg px-2 py-0.5 text-xs font-bold text-slate-800 outline-none focus:border-amber-500 text-center"
              />
              <button
                @click="emit('useMaxCoins')"
                class="bg-amber-600 hover:bg-amber-700 text-white px-2 py-0.5 rounded-lg text-xs font-bold transition-colors whitespace-nowrap"
              >
                Max
              </button>
              <button
                v-if="coinsUsed > 0"
                @click="emit('removeCoins')"
                class="text-slate-400 hover:text-rose-600 px-0.5 font-bold text-xs"
                title="Bỏ dùng xu"
              >
                ✕
              </button>
            </div>
          </div>
        </div>

        <!-- CHI TIẾT TÍNH TIỀN -->
        <div class="space-y-1.5 text-xs font-medium pt-1 border-t border-slate-200/60">
          <div class="flex justify-between text-slate-500">
            <span>Tổng tiền hàng</span>
            <span class="text-slate-800 font-bold">{{ formatPrice(totalCartPrice) }}</span>
          </div>

          <div v-if="appliedVoucher || selectedVoucher" class="flex justify-between text-rose-600">
            <span>Voucher giảm</span>
            <span class="font-bold">-{{ formatPrice(voucherDiscount) }}</span>
          </div>

          <div v-if="coinsUsed > 0" class="flex justify-between text-rose-600">
            <span>Đổi từ Xu ({{ coinsUsed }} xu)</span>
            <span class="font-bold">-{{ formatPrice(coinDiscount) }}</span>
          </div>

          <div class="flex justify-between items-center pt-2 border-t border-slate-200/80">
            <span class="font-black text-slate-800 text-xs uppercase">CẦN THANH TOÁN</span>
            <span class="text-xl font-black text-indigo-600">
              {{ formatPrice(totalNeedPay) }}
            </span>
          </div>
        </div>

        <!-- HÌNH THỨC THANH TOÁN & KHÁCH ĐƯA -->
        <div class="space-y-2 pt-1">
          <div>
            <label
              class="text-[10px] font-extrabold text-slate-400 uppercase tracking-wider block mb-1"
              >HÌNH THỨC THANH TOÁN</label
            >
            <select
              :value="phuongThucThanhToan"
              @change="onPtttChange"
              class="w-full bg-white border border-slate-200 rounded-xl px-3 py-1.5 text-xs font-bold text-slate-800 focus:ring-2 focus:ring-indigo-500/20 outline-none h-[36px]"
            >
              <option v-for="pt in ptttList" :key="pt.id" :value="pt.id">
                {{ pt.tenPhuongThuc }}
              </option>
            </select>
          </div>

          <div
            v-if="isMultiPayment"
            class="bg-indigo-50/60 p-2.5 rounded-xl border border-indigo-100 space-y-1.5"
          >
            <div class="grid grid-cols-2 gap-2">
              <div>
                <label class="text-[10px] font-bold text-slate-600 block mb-0.5">Tiền mặt</label>
                <input
                  type="text"
                  :value="displayCash"
                  @input="emit('cashInput', $event)"
                  placeholder="0"
                  class="w-full bg-white border border-slate-200 rounded-lg px-2.5 py-1 text-xs font-bold text-slate-800 outline-none focus:border-indigo-500"
                />
              </div>
              <div>
                <label class="text-[10px] font-bold text-slate-600 block mb-0.5"
                  >Chuyển khoản</label
                >
                <input
                  type="text"
                  :value="displayBank"
                  @input="emit('bankInput', $event)"
                  placeholder="0"
                  class="w-full bg-white border border-slate-200 rounded-lg px-2.5 py-1 text-xs font-bold text-slate-800 outline-none focus:border-indigo-500"
                />
              </div>
            </div>
          </div>

          <div
            v-if="!isMultiPayment && isCashPayment"
            class="bg-white p-2.5 rounded-xl border border-slate-200/80 space-y-2"
          >
            <div class="flex items-center justify-between gap-3">
              <label class="text-xs font-bold text-slate-700 whitespace-nowrap">Khách đưa:</label>
              <input
                type="text"
                :value="displayTienKhachDua"
                @input="emit('tienKhachDuaInput', $event)"
                placeholder="0"
                class="w-36 bg-slate-50 border border-slate-200 focus:bg-white rounded-lg px-2.5 py-1 text-right text-xs font-black text-slate-900 outline-none focus:border-indigo-600"
              />
            </div>
            <div class="flex items-center justify-between pt-1 border-t border-slate-100">
              <span class="text-xs font-bold text-slate-500">Thối lại:</span>
              <span class="font-black text-emerald-600 text-sm">{{
                formatPrice(tienThoiLai)
              }}</span>
            </div>
          </div>
        </div>

        <button
          @click="emit('submitCheckout')"
          class="w-full bg-indigo-600 hover:bg-indigo-700 active:scale-[0.99] text-white font-extrabold text-xs sm:text-sm py-3 px-4 rounded-xl shadow-md shadow-indigo-200 hover:shadow-lg transition-all flex items-center justify-center gap-2"
        >
          <span>XUẤT HÓA ĐƠN & THANH TOÁN</span>
        </button>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'

const props = defineProps({
  currentOrder: Object,
  selectedCustomer: Object,
  tyLeQuyDoiXu: { type: Number, default: 1000 },
  filteredVouchers: { type: Array, default: () => [] },
  bestVoucher: Object,
  ptttList: { type: Array, default: () => [] },
  coinsUsed: Number,
  coinDiscount: Number,
  voucherDiscount: Number,
  totalCartPrice: Number,
  totalNeedPay: Number,
  appliedVoucher: Object,
  selectedVoucher: Object,
  loaiHoaDon: String,
  phuongThucThanhToan: [String, Number],
  isMultiPayment: Boolean,
  isCashPayment: Boolean,
  displayCash: String,
  displayBank: String,
  displayTienKhachDua: String,
  tienThoiLai: Number,
  getProductImage: Function,
  setDefaultImage: Function,
  formatPrice: Function,
  getVariantDiscountPercent: Function,
  isVoucherValid: Function,
  getVoucherError: Function,
})

const emit = defineEmits([
  'openCustomerModal',
  'removeCustomer',
  'decreaseQty',
  'increaseQty',
  'removeFromCart',
  'changeQty',
  'removeVoucher',
  'selectVoucher',
  'coinsInput',
  'useMaxCoins',
  'removeCoins',
  'update:phuongThucThanhToan',
  'changePaymentMethod',
  'cashInput',
  'bankInput',
  'tienKhachDuaInput',
  'submitCheckout',
])

const remainingCoins = computed(() => {
  const total = props.selectedCustomer?.soDuXu ?? 0
  const used = props.coinsUsed ?? 0
  return Math.max(0, total - used)
})

const showVoucherDropdown = ref(false)
const voucherRef = ref(null)

const onSelectVoucher = (vc) => {
  emit('selectVoucher', vc)
  showVoucherDropdown.value = false
}

const onPtttChange = (e) => {
  emit('update:phuongThucThanhToan', e.target.value)
  emit('changePaymentMethod')
}

const handleClickOutside = (event) => {
  if (voucherRef.value && !voucherRef.value.contains(event.target)) {
    showVoucherDropdown.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>
