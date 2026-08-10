<template>
  <div class="grid grid-cols-2 sm:grid-cols-3 xl:grid-cols-4 2xl:grid-cols-5 gap-2.5">
    <div
      v-for="sp in sortedProducts"
      :key="sp.id"
      @click="
        (sp.soLuongKhaDung ?? sp.soLuongTon) > 0 && sp.trangThai ? $emit('add-to-cart', sp) : null
      "
      :class="[
        'bg-white rounded-xl border p-2 flex flex-col justify-between transition-all duration-200 group relative overflow-hidden select-none',
        (sp.soLuongKhaDung ?? sp.soLuongTon) > 0 && sp.trangThai
          ? 'border-slate-200/80 hover:border-indigo-500 hover:shadow-md cursor-pointer hover:-translate-y-0.5'
          : 'border-slate-200 bg-slate-50/80 opacity-60 cursor-not-allowed',
      ]"
    >
      <!-- Badge Giảm Giá -->
      <div v-if="sp.dangGiamGia" class="absolute top-2 right-2 z-20">
        <span
          class="bg-gradient-to-r from-amber-500 to-rose-500 text-white text-[9px] font-black px-1.5 py-0.5 rounded-full shadow-xs"
        >
          -{{ sp.phanTramGiam }}%
        </span>
      </div>

      <!-- Ảnh Sản Phẩm & Overlay Hết Hàng/Ngừng Bán -->
      <div
        class="aspect-square w-full rounded-lg bg-slate-100 overflow-hidden relative mb-1.5 border border-slate-100"
      >
        <img
          :src="getProductImage(sp)"
          :alt="sp.tenSanPham"
          class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
          @error="setDefaultImage"
        />
        <div
          v-if="!sp.trangThai || (sp.soLuongKhaDung ?? sp.soLuongTon) <= 0"
          class="absolute inset-0 bg-slate-900/40 backdrop-blur-[1px] flex items-center justify-center z-20"
        >
          <span
            class="bg-white/95 text-rose-600 text-[9px] font-black px-2 py-0.5 rounded shadow-sm"
          >
            {{ !sp.trangThai ? 'NGỪNG BÁN' : 'HẾT HÀNG' }}
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  sortedProducts: Array,
  getProductImage: Function,
  setDefaultImage: Function,
})

defineEmits(['add-to-cart'])
</script>
