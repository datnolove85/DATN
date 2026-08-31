<template>
  <div class="space-y-2">
    <!-- Thanh Bộ Lọc Chính -->
    <div
      class="bg-white/90 backdrop-blur-md p-2.5 rounded-2xl border border-slate-200/80 shadow-xs flex flex-wrap items-center gap-2"
    >
      <!-- 1. Tìm kiếm từ khóa -->
      <div class="relative flex-1 min-w-[180px]">
        <div
          class="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-2.5 text-slate-400"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="w-3.5 h-3.5"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
          >
            <circle cx="11" cy="11" r="8" />
            <path d="m21 21-4.3-4.3" />
          </svg>
        </div>
        <input
          :value="searchQuery"
          @input="$emit('update:searchQuery', $event.target.value)"
          type="text"
          placeholder="Tìm tên, mã SP..."
          class="w-full rounded-xl border border-slate-200/90 bg-slate-50/70 pl-8 pr-3 py-1.5 text-xs font-medium text-slate-700 placeholder-slate-400 focus:bg-white focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500/80 transition-all"
        />
      </div>

      <!-- 2. Lọc Danh mục -->
      <select
        :value="filterCategory"
        @change="$emit('update:filterCategory', $event.target.value)"
        :class="[
          'rounded-xl border px-2.5 py-1.5 text-xs font-semibold cursor-pointer focus:outline-none focus:ring-2 transition-all',
          filterCategory
            ? 'border-indigo-500 bg-indigo-50/60 text-indigo-700'
            : 'border-slate-200/90 bg-slate-50/70 text-slate-600 hover:bg-slate-100',
        ]"
      >
        <option value="">Tất cả Danh mục</option>
        <option v-for="dm in danhMucList" :key="dm.id" :value="dm.id">{{ dm.tenDanhMuc }}</option>
      </select>

      <!-- 3. Lọc Thương hiệu -->
      <select
        :value="filterBrand"
        @change="$emit('update:filterBrand', $event.target.value)"
        :class="[
          'rounded-xl border px-2.5 py-1.5 text-xs font-semibold cursor-pointer focus:outline-none focus:ring-2 transition-all',
          filterBrand
            ? 'border-indigo-500 bg-indigo-50/60 text-indigo-700'
            : 'border-slate-200/90 bg-slate-50/70 text-slate-600 hover:bg-slate-100',
        ]"
      >
        <option value="">Tất cả Thương hiệu</option>
        <option v-for="th in thuongHieuList" :key="th.id" :value="th.id">
          {{ th.tenThuongHieu }}
        </option>
      </select>

      <!-- 4. Lọc Màu sắc -->
      <select
        :value="filterColor"
        @change="$emit('update:filterColor', $event.target.value)"
        :class="[
          'rounded-xl border px-2.5 py-1.5 text-xs font-semibold cursor-pointer focus:outline-none focus:ring-2 transition-all',
          filterColor
            ? 'border-indigo-500 bg-indigo-50/60 text-indigo-700'
            : 'border-slate-200/90 bg-slate-50/70 text-slate-600 hover:bg-slate-100',
        ]"
      >
        <option value="">Màu sắc</option>
        <option v-for="ms in mauSacList" :key="ms.id" :value="ms.id">{{ ms.tenMauSac }}</option>
      </select>

      <!-- 5. Lọc Kích thước -->
      <select
        :value="filterSize"
        @change="$emit('update:filterSize', $event.target.value)"
        :class="[
          'rounded-xl border px-2.5 py-1.5 text-xs font-semibold cursor-pointer focus:outline-none focus:ring-2 transition-all',
          filterSize
            ? 'border-indigo-500 bg-indigo-50/60 text-indigo-700'
            : 'border-slate-200/90 bg-slate-50/70 text-slate-600 hover:bg-slate-100',
        ]"
      >
        <option value="">Kích thước</option>
        <option v-for="kt in kichThuocList" :key="kt.id" :value="kt.id">
          {{ kt.tenKichThuoc }}
        </option>
      </select>

      <!-- 6. Lọc Chất liệu -->
      <select
        v-if="chatLieuList && chatLieuList.length > 0"
        :value="filterMaterial"
        @change="$emit('update:filterMaterial', $event.target.value)"
        :class="[
          'rounded-xl border px-2.5 py-1.5 text-xs font-semibold cursor-pointer focus:outline-none focus:ring-2 transition-all',
          filterMaterial
            ? 'border-indigo-500 bg-indigo-50/60 text-indigo-700'
            : 'border-slate-200/90 bg-slate-50/70 text-slate-600 hover:bg-slate-100',
        ]"
      >
        <option value="">Chất liệu</option>
        <option v-for="cl in chatLieuList" :key="cl.id" :value="cl.id">{{ cl.tenChatLieu }}</option>
      </select>

      <!-- 7. Toggle Đang giảm giá -->
      <button
        @click="$emit('update:onlySale', !onlySale)"
        :class="[
          'inline-flex items-center gap-1 px-2.5 py-1.5 rounded-xl text-xs font-semibold border transition-all cursor-pointer',
          onlySale
            ? 'bg-amber-500 text-white border-amber-500 shadow-2xs'
            : 'bg-slate-50/70 border-slate-200/90 text-slate-600 hover:bg-slate-100',
        ]"
      >
        <span>🔥 Giảm giá</span>
      </button>

      <!-- 8. Sắp xếp -->
      <select
        :value="sortBy"
        @change="$emit('update:sortBy', $event.target.value)"
        class="rounded-xl border border-slate-200/90 bg-slate-50/70 px-2.5 py-1.5 text-xs font-semibold text-slate-600 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 cursor-pointer ml-auto"
      >
        <option value="newest">Mới nhất</option>
        <option value="price-asc">Giá: Thấp → Cao</option>
        <option value="price-desc">Giá: Cao → Thấp</option>
      </select>

      <!-- Nút xóa bộ lọc -->
      <button
        v-if="hasActiveFilter"
        @click="$emit('resetFilters')"
        class="text-xs font-bold text-rose-600 hover:text-rose-700 bg-rose-50 hover:bg-rose-100/80 px-2.5 py-1.5 rounded-xl transition-all cursor-pointer"
      >
        Xóa bộ lọc
      </button>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  searchQuery: { type: String, default: '' },
  filterCategory: { type: [String, Number], default: '' },
  filterBrand: { type: [String, Number], default: '' },
  filterColor: { type: [String, Number], default: '' },
  filterSize: { type: [String, Number], default: '' },
  filterMaterial: { type: [String, Number], default: '' },
  onlySale: { type: Boolean, default: false },
  sortBy: { type: String, default: 'newest' },
  danhMucList: { type: Array, default: () => [] },
  thuongHieuList: { type: Array, default: () => [] },
  mauSacList: { type: Array, default: () => [] },
  kichThuocList: { type: Array, default: () => [] },
  chatLieuList: { type: Array, default: () => [] },
})

defineEmits([
  'update:searchQuery',
  'update:filterCategory',
  'update:filterBrand',
  'update:filterColor',
  'update:filterSize',
  'update:filterMaterial',
  'update:onlySale',
  'update:sortBy',
  'resetFilters',
])

const hasActiveFilter = computed(() => {
  return !!(
    props.searchQuery ||
    props.filterCategory ||
    props.filterBrand ||
    props.filterColor ||
    props.filterSize ||
    props.filterMaterial ||
    props.onlySale
  )
})
</script>
