<template>
  <aside class="hidden lg:block">
    <div
      class="sticky top-24 overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-sm"
    >
      <div class="border-b border-slate-100 px-5 py-4">
        <p class="text-[11px] font-black uppercase tracking-[0.15em] text-rose-600">
          Bộ lọc tìm kiếm
        </p>
        <div class="mt-1 flex items-center justify-between gap-3">
          <h2 class="text-base font-black">Chọn theo nhu cầu</h2>
          <button
            v-if="hasActiveFilters"
            type="button"
            class="text-[11px] font-black text-rose-600 hover:underline"
            @click="$emit('clear')"
          >
            Xóa
          </button>
        </div>
      </div>
      <div class="divide-y divide-slate-100">
        <section class="p-4">
          <h3 class="text-[11px] font-black uppercase tracking-[0.14em] text-slate-500">Mức giá</h3>
          <div class="mt-3 space-y-1.5">
            <label
              v-for="price in priceRanges"
              :key="price.value"
              class="flex cursor-pointer items-center gap-3 rounded-lg px-2.5 py-2 text-xs font-semibold transition hover:bg-slate-50"
              :class="
                filters.priceRange === price.value ? 'bg-rose-50 text-rose-700' : 'text-slate-600'
              "
            >
              <input
                v-model="filters.priceRange"
                type="radio"
                :value="price.value"
                class="accent-rose-600"
              />
              {{ price.label }}
            </label>
          </div>
        </section>
        <details v-for="group in dynamicFilters" :key="group.key" class="group" open>
          <summary
            class="flex cursor-pointer list-none items-center justify-between px-5 py-4 text-[11px] font-black uppercase tracking-[0.14em] text-slate-500"
          >
            {{ group.label }}
            <ChevronDown :size="16" class="transition group-open:rotate-180" />
          </summary>
          <div class="max-h-56 space-y-1 overflow-y-auto px-3 pb-4">
            <label
              v-for="option in group.options"
              :key="option.id"
              class="flex cursor-pointer items-center gap-3 rounded-lg px-2.5 py-2 text-xs font-semibold text-slate-600 hover:bg-slate-50"
            >
              <input
                v-model="filters[group.key]"
                type="checkbox"
                :value="option.name"
                class="h-4 w-4 rounded accent-rose-600"
              />
              <span class="truncate">{{ option.name }}</span>
            </label>
          </div>
        </details>
      </div>
    </div>
  </aside>
</template>

<script setup>
import { ChevronDown } from 'lucide-vue-next'

defineProps({
  filters: { type: Object, required: true },
  priceRanges: { type: Array, default: () => [] },
  dynamicFilters: { type: Array, default: () => [] },
  hasActiveFilters: { type: Boolean, default: false },
})
defineEmits(['clear'])
</script>
