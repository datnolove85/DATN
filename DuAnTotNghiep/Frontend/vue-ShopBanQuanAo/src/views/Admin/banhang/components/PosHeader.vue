<template>
  <header
    class="sticky top-0 z-40 bg-white/95 backdrop-blur-md border-b border-slate-200/80 shadow-xs"
  >
    <div class="max-w-[1800px] mx-auto px-4 h-14 flex items-center justify-between gap-3">
      <!-- Tab Hóa Đơn Chờ -->
      <div class="flex items-center gap-1.5 overflow-x-auto no-scrollbar py-1">
        <template v-if="allOrders.length > 0">
          <div
            v-for="(order, index) in allOrders.filter((o) => o.id)"
            :key="order.id"
            @click="$emit('switchOrder', index)"
            :class="[
              'group relative flex items-center gap-2 px-3 py-1 rounded-lg border transition-all duration-200 cursor-pointer text-xs font-bold select-none',
              currentOrderIndex === index
                ? 'bg-indigo-50 border-indigo-500 text-indigo-700 shadow-xs ring-1 ring-indigo-500/20'
                : 'bg-slate-50 border-slate-200/80 text-slate-600 hover:bg-slate-100 hover:border-slate-300',
            ]"
          >
            <span class="flex items-center gap-1.5">
              <span
                :class="[
                  'w-2 h-2 rounded-full',
                  currentOrderIndex === index ? 'bg-indigo-600 animate-pulse' : 'bg-slate-400',
                ]"
              ></span>
              HĐ #{{ order.maHoaDon }}
            </span>
            <button
              @click.stop="$emit('removeOrder', index)"
              class="ml-1 flex items-center justify-center w-4 h-4 rounded-full bg-slate-200/70 text-slate-500 hover:bg-rose-500 hover:text-white transition-all text-[11px] leading-none"
              title="Hủy hóa đơn (Ctrl+D)"
            >
              ×
            </button>
          </div>
        </template>

        <button
          @click="$emit('createNewOrder')"
          class="flex items-center gap-1 px-2.5 py-1 rounded-lg bg-indigo-600 hover:bg-indigo-700 text-white transition-all text-xs font-bold shadow-xs active:scale-95"
          title="Tạo hóa đơn mới (F1)"
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
              stroke-width="2.5"
              d="M12 4v16m8-8H4"
            />
          </svg>
          <span>Tạo mới (F1)</span>
        </button>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref } from 'vue'

defineProps({
  allOrders: { type: Array, required: true },
  currentOrderIndex: { type: Number, required: true },
  searchQuery: { type: String, default: '' },
  isDropdownVisible: { type: Boolean, default: false },
  filteredProducts: { type: Array, default: () => [] },
  getProductImage: { type: Function, required: true },
  formatPrice: { type: Function, required: true },
  getVariantDiscountPercent: { type: Function, required: true },
})

defineEmits([
  'update:searchQuery',
  'switchOrder',
  'removeOrder',
  'createNewOrder',
  'openDropdown',
  'closeDropdown',
  'selectProductFromSearch',
])

const searchInputRef = ref(null)

defineExpose({
  focusSearch: () => searchInputRef.value?.focus(),
})
</script>
