<template>
  <div
    v-if="show"
    class="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/60 backdrop-blur-xs p-4"
  >
    <div
      class="bg-white rounded-2xl shadow-2xl max-w-lg w-full overflow-hidden border border-slate-100"
    >
      <div
        class="px-5 py-3.5 bg-slate-50 border-b border-slate-200/80 flex items-center justify-between"
      >
        <div>
          <h3 class="font-extrabold text-sm text-slate-800">Chọn Phân Loại Sản Phẩm</h3>
          <p class="text-[11px] text-slate-500">{{ activeMasterProduct?.tenSanPham }}</p>
        </div>
        <button
          @click="$emit('update:show', false)"
          class="w-7 h-7 rounded-full bg-slate-200/60 hover:bg-rose-500 hover:text-white flex items-center justify-center text-xs font-bold text-slate-500"
        >
          ✕
        </button>
      </div>

      <div class="p-5 space-y-4 max-h-[70vh] overflow-y-auto custom-scrollbar">
        <!-- Preview -->
        <div
          class="flex items-center gap-3 p-3 bg-indigo-50/50 border border-indigo-100 rounded-xl"
        >
          <img
            :src="getProductImage(selectedVariant || activeMasterProduct?.represent)"
            class="w-14 h-14 rounded-lg object-cover border border-slate-200 flex-shrink-0"
          />
          <div class="min-w-0 flex-1">
            <p class="font-bold text-xs text-slate-800 truncate">
              {{ activeMasterProduct?.tenSanPham }}
            </p>
            <p class="text-sm font-extrabold text-indigo-600">
              {{ formatPrice(selectedVariant?.donGia || activeMasterProduct?.minPrice) }}
            </p>
            <p class="text-[11px] text-slate-500">
              Tồn kho:
              <span class="font-bold text-slate-700">{{
                selectedVariant ? selectedVariant.soLuongTon : activeMasterProduct?.totalStock
              }}</span>
            </p>
          </div>
        </div>

        <!-- Màu sắc -->
        <div>
          <label class="block text-xs font-bold text-slate-700 mb-1.5">Màu sắc</label>
          <div class="flex flex-wrap gap-2">
            <button
              v-for="color in availableColors"
              :key="color.id"
              @click="selectedColorId = color.id"
              :class="[
                'px-3 py-1.5 rounded-lg border text-xs font-bold transition-all',
                selectedColorId === color.id
                  ? 'bg-indigo-600 border-indigo-600 text-white shadow-xs'
                  : 'bg-white border-slate-200 text-slate-700 hover:border-slate-300',
              ]"
            >
              {{ color.tenMauSac }}
            </button>
          </div>
        </div>

        <!-- Size -->
        <div>
          <label class="block text-xs font-bold text-slate-700 mb-1.5">Kích thước</label>
          <div class="flex flex-wrap gap-2">
            <button
              v-for="size in availableSizes"
              :key="size.id"
              @click="selectedSizeId = size.id"
              :class="[
                'px-3 py-1.5 rounded-lg border text-xs font-bold transition-all',
                selectedSizeId === size.id
                  ? 'bg-indigo-600 border-indigo-600 text-white shadow-xs'
                  : 'bg-white border-slate-200 text-slate-700 hover:border-slate-300',
              ]"
            >
              {{ size.tenKichThuoc }}
            </button>
          </div>
        </div>

        <!-- Số lượng -->
        <div>
          <label class="block text-xs font-bold text-slate-700 mb-1.5">Số lượng mua</label>
          <div class="flex items-center border border-slate-200 rounded-lg p-0.5 bg-slate-50 w-fit">
            <button
              @click="quantity > 1 && quantity--"
              class="w-8 h-8 flex items-center justify-center font-bold text-slate-600 hover:bg-white rounded-md"
            >
              -
            </button>
            <input
              type="number"
              v-model.number="quantity"
              min="1"
              class="w-12 text-center bg-transparent text-xs font-bold focus:outline-none"
            />
            <button
              @click="quantity++"
              class="w-8 h-8 flex items-center justify-center font-bold text-slate-600 hover:bg-white rounded-md"
            >
              +
            </button>
          </div>
        </div>
      </div>

      <div class="px-5 py-3 bg-slate-50 border-t border-slate-200/80 flex justify-end gap-2">
        <button
          @click="$emit('update:show', false)"
          class="px-4 py-2 rounded-xl border border-slate-200 hover:bg-slate-100 text-slate-600 text-xs font-bold"
        >
          Hủy
        </button>
        <button
          @click="handleConfirm"
          :disabled="!selectedVariant || selectedVariant.soLuongTon <= 0"
          class="px-5 py-2 bg-indigo-600 hover:bg-indigo-700 disabled:bg-slate-300 text-white rounded-xl text-xs font-extrabold shadow-md"
        >
          Thêm vào giỏ
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'

const props = defineProps({
  show: Boolean,
  activeMasterProduct: Object,
  getProductImage: Function,
  formatPrice: Function,
})

const emit = defineEmits(['update:show', 'confirm'])

const selectedColorId = ref(null)
const selectedSizeId = ref(null)
const quantity = ref(1)

watch(
  () => props.activeMasterProduct,
  (newVal) => {
    if (newVal && newVal.variants && newVal.variants.length > 0) {
      selectedColorId.value = newVal.variants[0].mauSacId || null
      selectedSizeId.value = newVal.variants[0].kichThuocId || null
      quantity.value = 1
    }
  },
  { immediate: true },
)

const availableColors = computed(() => {
  if (!props.activeMasterProduct?.variants) return []
  const map = new Map()
  props.activeMasterProduct.variants.forEach((v) => {
    if (v.mauSacId && !map.has(v.mauSacId))
      map.set(v.mauSacId, { id: v.mauSacId, tenMauSac: v.tenMauSac })
  })
  return Array.from(map.values())
})

const availableSizes = computed(() => {
  if (!props.activeMasterProduct?.variants) return []
  const map = new Map()
  props.activeMasterProduct.variants.forEach((v) => {
    if (v.kichThuocId && !map.has(v.kichThuocId))
      map.set(v.kichThuocId, { id: v.kichThuocId, tenKichThuoc: v.tenKichThuoc })
  })
  return Array.from(map.values())
})

const selectedVariant = computed(() => {
  if (!props.activeMasterProduct?.variants) return null
  return props.activeMasterProduct.variants.find(
    (v) => v.mauSacId === selectedColorId.value && v.kichThuocId === selectedSizeId.value,
  )
})

const handleConfirm = () => {
  if (selectedVariant.value) {
    emit('confirm', { variant: selectedVariant.value, quantity: quantity.value })
    emit('update:show', false)
  }
}
</script>
