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
            @click="$emit('switch-order', index)"
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
              @click.stop="$emit('remove-order', index)"
              class="ml-1 flex items-center justify-center w-4 h-4 rounded-full bg-slate-200/70 text-slate-500 hover:bg-rose-500 hover:text-white transition-all text-[11px] leading-none"
              title="Hủy hóa đơn (Ctrl+D)"
            >
              ×
            </button>
          </div>
        </template>

        <button
          @click="$emit('create-order')"
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

      <!-- Thanh Tìm Kiếm Sản Phẩm Nhanh -->
      <div class="flex-1 max-w-md hidden md:block">
        <div class="relative">
          <input
            ref="searchInput"
            :value="searchQuery"
            @input="$emit('update:searchQuery', $event.target.value)"
            @focus="$emit('focus')"
            @blur="$emit('blur')"
            placeholder="Tìm theo tên hoặc mã SP... (Ctrl + F)"
            class="w-full pl-9 pr-12 py-1.5 bg-slate-50 hover:bg-white focus:bg-white border border-slate-200 focus:border-indigo-500 rounded-lg text-xs font-medium focus:outline-none focus:ring-2 focus:ring-indigo-500/20 transition-all"
          />
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-4 w-4 absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
            />
          </svg>
          <kbd
            class="absolute right-2.5 top-1/2 -translate-y-1/2 hidden sm:inline-block px-1.5 py-0.5 text-[9px] font-semibold text-slate-400 bg-slate-100 border border-slate-200 rounded-md"
            >Ctrl+F</kbd
          >

          <!-- Instant Search Dropdown -->
          <div
            v-if="isDropdownVisible && filteredProducts.length > 0"
            class="absolute top-full left-0 right-0 mt-1.5 bg-white border border-slate-200 shadow-2xl rounded-xl z-[60] max-h-72 overflow-y-auto custom-scrollbar p-1"
          >
            <div
              v-for="sp in filteredProducts.slice(0, 7)"
              :key="sp.id"
              @click="$emit('add-to-cart', sp)"
              class="flex items-center gap-2.5 p-1.5 hover:bg-indigo-50/70 rounded-lg cursor-pointer transition-colors border-b border-slate-100 last:border-0"
            >
              <img
                :src="getProductImage(sp)"
                class="w-9 h-9 rounded-md object-cover border border-slate-100 flex-shrink-0"
              />
              <div class="flex-1 min-w-0">
                <p class="font-bold text-xs text-slate-800 truncate">{{ sp.tenSanPham }}</p>
                <p class="text-[10px] text-slate-400 font-mono">#{{ sp.maSanPhamChiTiet }}</p>
              </div>
              <div class="text-right">
                <p class="text-xs font-black text-indigo-600">
                  {{ formatPrice(sp.dangGiamGia ? sp.giaSauGiam : sp.giaBan) }}
                </p>
                <span class="text-[9px] text-emerald-600 font-medium"
                  >Kho: {{ sp.soLuongKhaDung ?? sp.soLuongTon }}</span
                >
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
defineProps({
  allOrders: Array,
  currentOrderIndex: Number,
  searchQuery: String,
  isDropdownVisible: Boolean,
  filteredProducts: Array,
  getProductImage: Function,
  formatPrice: Function,
})

defineEmits([
  'switch-order',
  'remove-order',
  'create-order',
  'update:searchQuery',
  'focus',
  'blur',
  'add-to-cart',
])
</script>
