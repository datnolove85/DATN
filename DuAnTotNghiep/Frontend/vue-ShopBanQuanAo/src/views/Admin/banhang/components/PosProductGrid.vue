<template>
  <div class="space-y-3">
    <!-- Grid Danh sách Sản phẩm còn hàng -->
    <div
      v-if="availableProducts && availableProducts.length > 0"
      class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-3 lg:grid-cols-3 xl:grid-cols-4 gap-3"
    >
      <div
        v-for="sp in availableProducts"
        :key="sp.id"
        @click="$emit('selectProduct', sp)"
        class="group bg-white rounded-xl border border-slate-200/80 shadow-xs hover:shadow-md hover:border-indigo-300 transition-all duration-200 cursor-pointer flex flex-col justify-between overflow-hidden relative"
      >
        <!-- Badge Giảm giá & Số lượng biến thể -->
        <div class="absolute top-2 left-2 z-10 flex flex-col gap-1">
          <span
            v-if="sp.dangGiamGia || sp.coGiamGia"
            class="px-1.5 py-0.5 bg-rose-500 text-white font-bold text-[10px] rounded-md shadow-xs"
          >
            -{{ getVariantDiscountPercent ? getVariantDiscountPercent(sp) : sp.phanTramGiam || 0 }}%
          </span>
          <span
            v-if="sp.soLuongBienThe && sp.soLuongBienThe > 1"
            class="px-1.5 py-0.5 bg-slate-900/70 backdrop-blur-xs text-white font-medium text-[10px] rounded-md"
          >
            {{ sp.soLuongBienThe }} biến thể
          </span>
        </div>

        <!-- Tồn kho Badge -->
        <div class="absolute top-2 right-2 z-10">
          <span
            class="px-1.5 py-0.5 font-bold text-[10px] rounded-md shadow-xs bg-emerald-50 text-emerald-700 border border-emerald-200"
          >
            Kho: {{ sp.tongSoLuongTon ?? sp.soLuongTon }}
          </span>
        </div>

        <!-- Ảnh sản phẩm -->
        <div class="w-full aspect-square bg-slate-50 overflow-hidden relative">
          <img
            :src="getProductImage(sp)"
            :alt="sp.tenSanPham"
            class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
            loading="lazy"
          />
        </div>

        <!-- Thông tin sản phẩm -->
        <div class="p-2.5 flex flex-col justify-between flex-1 gap-1.5">
          <div>
            <!-- Dòng mã sản phẩm + Tag chất liệu -->
            <div class="flex items-center justify-between gap-1 mb-1">
              <p class="text-[11px] font-mono text-slate-400 truncate">{{ sp.maSanPham }}</p>

              <!-- TAG CHẤT LIỆU MỚI -->
              <span
                v-if="sp.tenChatLieu || sp.chatLieu"
                class="px-1.5 py-0.5 bg-slate-100 text-slate-600 text-[9px] font-semibold rounded truncate max-w-[60%]"
                :title="sp.tenChatLieu || sp.chatLieu"
              >
                🧵 {{ sp.tenChatLieu || sp.chatLieu }}
              </span>
            </div>

            <h3
              class="font-bold text-xs text-slate-800 line-clamp-2 leading-snug group-hover:text-indigo-600 transition-colors"
            >
              {{ sp.tenSanPham }}
            </h3>
          </div>

          <!-- Giá tiền & Nút thao tác -->
          <div
            class="pt-1 border-t border-slate-100 flex items-center justify-between gap-1 mt-auto"
          >
            <div>
              <template v-if="sp.dangGiamGia || sp.coGiamGia">
                <p class="text-xs font-black text-rose-600">
                  {{ formatPrice(sp.giaSauGiam ?? sp.giaBan) }}
                </p>
                <p v-if="sp.giaBan" class="text-[10px] text-slate-400 line-through">
                  {{ formatPrice(sp.giaBan) }}
                </p>
              </template>
              <template v-else>
                <p class="text-xs font-black text-indigo-600">
                  {{ formatPrice(sp.giaBan) }}
                </p>
              </template>
            </div>

            <button
              type="button"
              class="p-1.5 rounded-lg bg-indigo-50 text-indigo-600 group-hover:bg-indigo-600 group-hover:text-white transition-all text-xs font-bold active:scale-95"
              title="Thêm vào đơn"
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
                  d="M12 4v16m8-8H4"
                />
              </svg>
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Trạng thái Trống (Không tìm thấy sản phẩm) -->
    <div
      v-else
      class="bg-white rounded-xl border border-slate-200/80 p-12 text-center flex flex-col items-center justify-center min-h-[350px]"
    >
      <div
        class="w-16 h-16 rounded-full bg-slate-100 flex items-center justify-center mb-3 text-slate-400"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          class="h-8 w-8"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="1.5"
            d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4"
          />
        </svg>
      </div>
      <p class="font-bold text-slate-600 text-sm">Không tìm thấy sản phẩm nào</p>
      <p class="text-xs text-slate-400 mt-1">Vui lòng kiểm tra lại từ khóa hoặc bộ lọc của bạn.</p>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  products: { type: Array, default: () => [] },
  getProductImage: { type: Function, required: true },
  formatPrice: { type: Function, required: true },
  getVariantDiscountPercent: { type: Function, default: null },
})

defineEmits(['selectProduct'])

// Lọc các sản phẩm có số lượng tồn > 0
const availableProducts = computed(() => {
  return props.products.filter((sp) => (sp.tongSoLuongTon ?? sp.soLuongTon ?? 0) > 0)
})
</script>
