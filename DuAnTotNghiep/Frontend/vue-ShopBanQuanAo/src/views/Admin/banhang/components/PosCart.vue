<template>
  <div class="flex-1 overflow-y-auto custom-scrollbar p-3 space-y-2.5">
    <div
      v-if="!currentOrder?.chiTietList || currentOrder.chiTietList.length === 0"
      class="h-full flex flex-col items-center justify-center text-slate-400 py-12"
    >
      <svg
        xmlns="http://www.w3.org/2000/svg"
        class="h-14 w-14 stroke-1 mb-2 text-slate-300"
        fill="none"
        viewBox="0 0 24 24"
        stroke="currentColor"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          stroke-width="1.5"
          d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z"
        />
      </svg>
      <p class="text-xs font-semibold text-slate-500">Chưa có sản phẩm nào trong giỏ</p>
      <p class="text-[11px] text-slate-400 mt-0.5">
        Quét mã vạch hoặc chọn sản phẩm từ danh sách bên trái
      </p>
    </div>

    <template v-else>
      <div
        v-for="(item, index) in currentOrder.chiTietList"
        :key="item.id || index"
        class="bg-slate-50/70 hover:bg-slate-50 border border-slate-200/80 rounded-xl p-2.5 transition-all flex flex-col gap-2 group"
      >
        <div class="flex items-start justify-between gap-2">
          <div class="flex-1 min-w-0">
            <h4 class="font-bold text-xs text-slate-800 truncate">{{ item.tenSanPham }}</h4>
            <div class="flex items-center gap-2 mt-0.5">
              <span
                class="text-[10px] bg-indigo-50 text-indigo-700 font-semibold px-1.5 py-0.5 rounded border border-indigo-100"
              >
                {{ item.tenKichThuoc }} / {{ item.tenMauSac }}
              </span>
              <span class="text-[10px] text-slate-400 font-mono">#{{ item.maSanPhamChiTiet }}</span>
            </div>
          </div>
          <button
            @click="$emit('remove-item', index)"
            class="text-slate-400 hover:text-rose-600 p-1 rounded-lg hover:bg-rose-50 transition-colors"
            title="Xóa sản phẩm"
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
                d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
              />
            </svg>
          </button>
        </div>

        <div class="flex items-center justify-between pt-1 border-t border-slate-200/60">
          <!-- Điều chỉnh số lượng -->
          <div
            class="flex items-center bg-white border border-slate-200 rounded-lg overflow-hidden shadow-2xs"
          >
            <button
              @click="$emit('update-quantity', index, item.soLuong - 1)"
              class="w-6 h-6 flex items-center justify-center text-slate-600 hover:bg-slate-100 font-bold text-xs transition-colors"
            >
              -
            </button>
            <input
              type="number"
              :value="item.soLuong"
              @change="$emit('update-quantity', index, parseInt($event.target.value) || 1)"
              class="w-9 text-center text-xs font-bold text-slate-800 focus:outline-none bg-transparent"
            />
            <button
              @click="$emit('update-quantity', index, item.soLuong + 1)"
              class="w-6 h-6 flex items-center justify-center text-slate-600 hover:bg-slate-100 font-bold text-xs transition-colors"
            >
              +
            </button>
          </div>

          <!-- Thành tiền -->
          <div class="text-right">
            <p class="text-xs font-black text-indigo-600">
              {{ formatPrice(item.donGia * item.soLuong) }}
            </p>
            <span v-if="item.soLuong > 1" class="text-[10px] text-slate-400">
              ({{ formatPrice(item.donGia) }}/sp)
            </span>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup>
defineProps({
  currentOrder: Object,
  formatPrice: Function,
})

defineEmits(['remove-item', 'update-quantity'])
</script>
