<template>
  <div class="p-3.5 bg-white border-t border-slate-200/80 space-y-3 rounded-b-2xl shadow-sm">
    <!-- Chọn Khách Hàng -->
    <div
      class="flex items-center justify-between bg-slate-50 border border-slate-200/80 rounded-xl p-2.5"
    >
      <div class="flex items-center gap-2">
        <div
          class="w-8 h-8 rounded-lg bg-indigo-100 text-indigo-700 flex items-center justify-center font-bold text-xs"
        >
          KH
        </div>
        <div>
          <p class="text-xs font-bold text-slate-800">
            {{ currentOrder?.khachHang ? currentOrder.khachHang.hoTen : 'Khách lẻ (Mặc định)' }}
          </p>
          <p class="text-[10px] text-slate-400">
            {{ currentOrder?.khachHang?.soDienThoai || 'Chưa cập nhật SĐT' }}
          </p>
        </div>
      </div>
      <button
        @click="$emit('open-customer-modal')"
        class="text-xs font-bold text-indigo-600 hover:text-indigo-700 bg-indigo-50 hover:bg-indigo-100 px-2.5 py-1.5 rounded-lg transition-colors"
      >
        {{ currentOrder?.khachHang ? 'Đổi khách' : 'Chọn khách' }}
      </button>
    </div>

    <!-- Bảng tổng tiền -->
    <div class="space-y-1.5 text-xs text-slate-600 pt-1">
      <div class="flex justify-between">
        <span>Tổng tiền hàng:</span>
        <span class="font-semibold text-slate-800">{{ formatPrice(calculateSubtotal()) }}</span>
      </div>
      <div class="flex justify-between">
        <span>Giảm giá (Voucher):</span>
        <span class="font-semibold text-rose-600"
          >-{{ formatPrice(currentOrder?.tienGiamGia || 0) }}</span
        >
      </div>
      <div
        class="flex justify-between text-sm font-black text-slate-900 pt-2 border-t border-dashed border-slate-200"
      >
        <span>Khách cần trả:</span>
        <span class="text-indigo-600 text-base">{{ formatPrice(calculateFinalTotal()) }}</span>
      </div>
    </div>

    <!-- Nút Hành Động (Thanh toán / Đặt hàng) -->
    <div class="grid grid-cols-2 gap-2 pt-1">
      <button
        @click="$emit('hold-order')"
        class="w-full py-2.5 px-3 bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-xl font-bold text-xs transition-all active:scale-95 border border-slate-200"
      >
        Lưu tạm (F4)
      </button>
      <button
        @click="$emit('checkout')"
        :disabled="!currentOrder?.chiTietList || currentOrder.chiTietList.length === 0"
        class="w-full py-2.5 px-3 bg-indigo-600 hover:bg-indigo-700 disabled:bg-slate-200 disabled:text-slate-400 text-white rounded-xl font-black text-xs transition-all shadow-md shadow-indigo-600/20 active:scale-95 flex items-center justify-center gap-1.5"
      >
        <span>Thanh toán (F9)</span>
      </button>
    </div>
  </div>
</template>

<script setup>
defineProps({
  currentOrder: Object,
  formatPrice: Function,
  calculateSubtotal: Function,
  calculateFinalTotal: Function,
})

defineEmits(['open-customer-modal', 'hold-order', 'checkout'])
</script>
