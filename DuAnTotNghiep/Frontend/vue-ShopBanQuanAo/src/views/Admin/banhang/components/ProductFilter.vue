<template>
  <div
    class="bg-white p-2.5 rounded-xl border border-slate-200/80 shadow-xs flex flex-wrap gap-2 items-center"
  >
    <!-- Lọc Danh mục -->
    <select
      :value="filterCategory"
      @input="$emit('update:filterCategory', $event.target.value)"
      class="text-xs font-semibold bg-slate-50 border border-slate-200/80 rounded-lg px-2.5 py-1.5 text-slate-600 focus:outline-none focus:ring-2 focus:ring-indigo-500/20"
    >
      <option value="">Tất cả Danh mục</option>
      <option v-for="dm in danhMucList" :key="dm.id" :value="dm.id">
        {{ dm.tenDanhMuc }}
      </option>
    </select>

    <!-- Lọc Thương hiệu -->
    <select
      :value="filterBrand"
      @input="$emit('update:filterBrand', $event.target.value)"
      class="text-xs font-semibold bg-slate-50 border border-slate-200/80 rounded-lg px-2.5 py-1.5 text-slate-600 focus:outline-none focus:ring-2 focus:ring-indigo-500/20"
    >
      <option value="">Tất cả Thương hiệu</option>
      <option v-for="th in thuongHieuList" :key="th.id" :value="th.id">
        {{ th.tenThuongHieu }}
      </option>
    </select>

    <!-- Lọc Màu sắc -->
    <select
      :value="filterColor"
      @input="$emit('update:filterColor', $event.target.value)"
      class="text-xs font-semibold bg-slate-50 border border-slate-200/80 rounded-lg px-2.5 py-1.5 text-slate-600 focus:outline-none focus:ring-2 focus:ring-indigo-500/20"
    >
      <option value="">Màu sắc</option>
      <option v-for="ms in mauSacList" :key="ms.id" :value="ms.id">{{ ms.tenMauSac }}</option>
    </select>

    <!-- Lọc Kích thước -->
    <select
      :value="filterSize"
      @input="$emit('update:filterSize', $event.target.value)"
      class="text-xs font-semibold bg-slate-50 border border-slate-200/80 rounded-lg px-2.5 py-1.5 text-slate-600 focus:outline-none focus:ring-2 focus:ring-indigo-500/20"
    >
      <option value="">Kích thước</option>
      <option v-for="kt in kichThuocList" :key="kt.id" :value="kt.id">
        {{ kt.tenKichThuoc }}
      </option>
    </select>

    <!-- Nút Xóa bộ lọc -->
    <button
      @click="$emit('reset-filters')"
      class="text-xs font-bold text-rose-600 hover:text-rose-700 bg-rose-50 hover:bg-rose-100 px-2.5 py-1.5 rounded-lg ml-auto transition-colors"
    >
      Xóa bộ lọc
    </button>
  </div>
</template>

<script setup>
defineProps({
  filterCategory: String,
  filterBrand: String,
  filterColor: String,
  filterSize: String,
  danhMucList: Array,
  thuongHieuList: Array,
  mauSacList: Array,
  kichThuocList: Array,
})

defineEmits([
  'update:filterCategory',
  'update:filterBrand',
  'update:filterColor',
  'update:filterSize',
  'reset-filters',
])
</script>
