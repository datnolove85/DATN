<!-- components/VoucherModal.vue -->
<script setup>
const props = defineProps({
  isOpen: Boolean,
  sortedVouchers: {
    type: Array,
    default: () => [],
  },
  bestVoucher: Object,
  selectedVoucherId: [String, Number, null],
  subtotal: {
    type: Number,
    default: 0,
  },
})

const emit = defineEmits(['close', 'select', 'clear'])

const formatCurrency = (val) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(val || 0)
}

const formatShortCurrency = (val) => {
  if (!val) return '0đ'
  if (val >= 1000000) return val / 1000000 + 'Tr'
  if (val >= 1000) return val / 1000 + 'k'
  return val
}

const getVoucherDiscount = (v) => {
  if (v.loaiGiamGia === 'tien_mat') {
    return v.giaTriGiam
  } else {
    const discount = (props.subtotal * v.giaTriGiam) / 100
    return v.giaTriGiamToiDa ? Math.min(discount, v.giaTriGiamToiDa) : discount
  }
}
</script>

<template>
  <Teleport to="body">
    <div
      v-if="isOpen"
      class="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4 backdrop-blur-sm"
      @click.self="emit('close')"
    >
      <div
        class="w-full max-w-lg rounded-2xl bg-slate-50 overflow-hidden shadow-2xl flex flex-col max-h-[85vh]"
      >
        <div class="flex items-center justify-between bg-white px-6 py-4 border-b border-slate-100">
          <h2 class="text-lg font-bold text-slate-800 flex items-center gap-2">
            <span>🎁 Chọn Voucher Giảm Giá</span>
          </h2>
          <button
            @click="emit('close')"
            class="text-slate-400 hover:text-slate-600 text-xl font-light w-8 h-8 flex items-center justify-center rounded-full hover:bg-slate-100 transition-colors"
          >
            ✕
          </button>
        </div>

        <div
          class="bg-indigo-50/70 px-6 py-2.5 text-xs text-indigo-700 border-b border-indigo-100 flex items-center gap-1.5 font-medium shrink-0"
        >
          <span>💡 Nhấn vào voucher để chọn và áp dụng ngay cho đơn hàng</span>
        </div>

        <div
          class="p-4 space-y-3 overflow-y-auto flex-1 max-h-[420px] overscroll-contain custom-scrollbar"
        >
          <div
            @click="emit('clear')"
            :class="[
              'flex items-center justify-between rounded-xl border p-3.5 cursor-pointer transition-all bg-white',
              selectedVoucherId === null
                ? 'border-emerald-500 ring-2 ring-emerald-100'
                : 'border-slate-200 hover:border-indigo-500 hover:shadow-md',
            ]"
          >
            <div>
              <div class="font-semibold text-slate-800 flex items-center gap-2">
                Không sử dụng voucher
                <span
                  v-if="selectedVoucherId === null"
                  class="px-2 py-0.5 rounded-full bg-emerald-100 text-emerald-700 text-[10px] font-bold"
                >
                  ✓ Đang áp dụng
                </span>
              </div>
              <p class="text-xs text-slate-500 mt-1">Thanh toán theo giá gốc của đơn hàng</p>
            </div>
            <input
              type="radio"
              name="voucher-selection"
              :checked="selectedVoucherId === null"
              class="w-4 h-4 accent-indigo-600"
              @click.stop="emit('clear')"
            />
          </div>

          <div
            v-for="v in sortedVouchers"
            :key="v.uid"
            @click="subtotal >= v.giaTriDonHangToiThieu && emit('select', v.uid)"
            :class="[
              'relative flex items-center bg-white rounded-xl border transition-all overflow-hidden p-3.5 gap-4',
              selectedVoucherId === v.uid
                ? 'border-emerald-500 ring-2 ring-emerald-100'
                : bestVoucher && bestVoucher.uid === v.uid
                  ? 'border-amber-400 ring-2 ring-amber-100'
                  : subtotal >= v.giaTriDonHangToiThieu
                    ? 'border-slate-200 hover:border-indigo-500 hover:shadow-md'
                    : 'opacity-55 cursor-not-allowed border-slate-200 bg-slate-100/60',
            ]"
          >
            <div
              :class="[
                'w-28 py-3 rounded-xl text-white flex flex-col items-center justify-center text-center shrink-0 shadow-md',
                subtotal >= v.giaTriDonHangToiThieu
                  ? 'bg-gradient-to-tr from-blue-600 via-indigo-600 to-violet-600'
                  : 'bg-gradient-to-tr from-slate-400 to-slate-500',
              ]"
            >
              <span class="text-[9px] uppercase tracking-wider opacity-90 font-medium">
                {{ v.loaiVoucher === 'CA_NHAN' ? 'Voucher của bạn' : 'Voucher hệ thống' }}
              </span>
              <span class="text-base font-black my-0.5 tracking-tight">
                {{
                  v.loaiGiamGia === 'tien_mat'
                    ? formatShortCurrency(v.giaTriGiam)
                    : v.giaTriGiam + '%'
                }}
              </span>
            </div>

            <div class="flex-1 flex flex-col justify-between">
              <div class="flex items-start justify-between gap-2">
                <div>
                  <div class="flex items-center gap-2 flex-wrap">
                    <h3 class="font-bold text-slate-800 text-sm line-clamp-1">
                      {{ v.tenVoucher }}
                    </h3>
                    <span
                      v-if="v.loaiVoucher === 'CA_NHAN'"
                      class="px-2 py-0.5 rounded-full bg-amber-100 text-amber-700 text-[10px] font-bold"
                    >
                      🎁 Voucher của bạn
                    </span>
                    <span
                      v-else
                      class="px-2 py-0.5 rounded-full bg-blue-100 text-blue-700 text-[10px] font-bold"
                    >
                      🏷️ Hệ thống
                    </span>
                    <span
                      v-if="selectedVoucherId === v.uid"
                      class="px-2 py-0.5 rounded-full bg-emerald-100 text-emerald-700 text-[10px] font-bold"
                    >
                      ✓ Đang áp dụng
                    </span>
                    <span
                      v-else-if="bestVoucher && bestVoucher.uid === v.uid"
                      class="px-2 py-0.5 rounded-full bg-amber-100 text-amber-700 text-[10px] font-bold"
                    >
                      ⭐ Đề xuất
                    </span>
                  </div>

                  <div class="flex items-center gap-2 mt-0.5">
                    <p class="text-xs text-slate-400 font-mono">
                      Mã: <span class="text-slate-600 font-semibold">{{ v.maVoucher }}</span>
                    </p>
                    <p class="text-xs text-emerald-600 font-semibold mt-1">
                      Tiết kiệm {{ formatCurrency(getVoucherDiscount(v)) }}
                    </p>
                    <span
                      v-if="v.loaiGiamGia === 'phan_tram' && v.giaTriGiamToiDa"
                      class="text-[10px] bg-indigo-50 text-indigo-600 px-1.5 py-0.5 rounded font-medium"
                    >
                      Tối đa {{ formatShortCurrency(v.giaTriGiamToiDa) }}
                    </span>
                  </div>
                </div>

                <div class="shrink-0 pt-0.5">
                  <input
                    type="radio"
                    :name="'voucher-selection'"
                    :checked="selectedVoucherId === v.uid"
                    :disabled="subtotal < v.giaTriDonHangToiThieu"
                    class="w-4 h-4 text-indigo-600 accent-indigo-600 cursor-pointer"
                    @click.stop="subtotal >= v.giaTriDonHangToiThieu && emit('select', v.uid)"
                  />
                </div>
              </div>

              <div
                class="mt-3 flex items-center justify-between text-xs pt-2 border-t border-dashed border-slate-100"
              >
                <span class="text-slate-500">
                  Đơn tối thiểu:
                  <strong class="text-slate-700">{{
                    formatCurrency(v.giaTriDonHangToiThieu)
                  }}</strong>
                </span>

                <span v-if="subtotal < v.giaTriDonHangToiThieu" class="text-rose-500 font-semibold">
                  Thiếu {{ formatCurrency(v.giaTriDonHangToiThieu - subtotal) }}
                </span>
                <span v-else class="text-emerald-600 font-semibold"> ✓ Đạt điều kiện </span>
              </div>
            </div>
          </div>
        </div>

        <div
          class="bg-white px-6 py-3 border-t border-slate-100 flex items-center justify-end shrink-0"
        >
          <button
            @click="emit('close')"
            class="px-5 py-2 rounded-lg bg-slate-200 text-slate-700 text-sm font-medium hover:bg-slate-300 transition-colors"
          >
            Đóng
          </button>
        </div>
      </div>
    </div>
  </Teleport>
</template>
