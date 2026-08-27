<template>
  <header class="border-b border-slate-200 bg-white text-slate-900">
    <div class="bg-slate-950 text-white">
      <div
        class="mx-auto flex max-w-[1480px] items-center justify-between gap-3 px-4 py-2 text-[11px] font-semibold md:px-8"
      >
        <p class="truncate">Miễn phí giao hàng cho đơn từ 499.000đ · Đổi trả trong 7 ngày</p>
        <span class="hidden shrink-0 text-slate-300 md:inline">Hotline 1900 6750</span>
      </div>
    </div>

    <div
      class="mx-auto grid max-w-[1480px] items-center gap-4 px-4 py-4 md:grid-cols-[auto_minmax(0,1fr)_auto] md:px-8"
    >
      <div class="flex items-center gap-3">
        <div
          class="grid h-11 w-11 place-items-center rounded-xl bg-slate-950 text-sm font-black text-white"
        >
          HD
        </div>
        <div>
          <p class="text-[11px] font-black uppercase tracking-[0.2em] text-rose-600">
            Fashion Store
          </p>
          <p class="text-lg font-black tracking-tight">HD Fashion</p>
        </div>
      </div>

      <label
        class="group flex min-w-0 items-center rounded-full border border-slate-200 bg-slate-50 px-4 py-2.5 transition focus-within:border-rose-500 focus-within:bg-white focus-within:shadow-lg focus-within:shadow-rose-100"
      >
        <Search :size="19" class="mr-2 shrink-0 text-slate-400 group-focus-within:text-rose-500" />
        <input
          v-model="keyword"
          type="search"
          class="min-w-0 flex-1 bg-transparent text-sm font-semibold outline-none placeholder:text-slate-400"
          placeholder="Tìm kiếm sản phẩm, thương hiệu..."
          @keydown.enter="emitSearch"
        />
        <button
          v-if="keyword"
          type="button"
          class="rounded-full p-1.5 text-slate-400 hover:bg-slate-100"
          @click="keyword = ''"
        >
          <X :size="16" />
        </button>
      </label>

      <div class="flex items-center justify-end gap-2">
        <button
          type="button"
          class="hidden h-10 w-10 place-items-center rounded-full border border-slate-200 hover:border-slate-300 sm:grid"
          title="Tài khoản"
        >
          <UserRound :size="18" />
        </button>
        <button
          type="button"
          class="grid h-10 w-10 place-items-center rounded-full border border-slate-200 hover:border-slate-300"
          title="Yêu thích"
        >
          <Heart :size="18" />
        </button>
        <button
          type="button"
          class="relative grid h-10 w-10 place-items-center rounded-full bg-rose-600 text-white shadow-lg shadow-rose-200"
          title="Giỏ hàng"
        >
          <ShoppingBag :size="18" />
          <span
            class="absolute -right-1 -top-1 grid h-5 min-w-5 place-items-center rounded-full bg-slate-950 px-1 text-[10px] font-black text-white"
            >0</span
          >
        </button>
      </div>
    </div>

    <div class="border-t border-slate-100 bg-white">
      <div class="mx-auto flex max-w-[1480px] items-center gap-1 overflow-x-auto px-4 md:px-8">
        <button
          type="button"
          class="shrink-0 rounded-none border-b-2 border-rose-600 px-3 py-3 text-xs font-black text-rose-600"
        >
          Sản phẩm
        </button>
        <button
          v-for="item in menu"
          :key="item"
          type="button"
          class="shrink-0 rounded-none border-b-2 border-transparent px-3 py-3 text-xs font-bold text-slate-600 transition hover:border-slate-900 hover:text-slate-900"
        >
          {{ item }}
        </button>
        <span
          class="ml-auto hidden shrink-0 px-3 py-3 text-[11px] font-semibold text-slate-400 lg:inline"
          >Tìm đúng thứ bạn cần · chọn nhanh · mua dễ</span
        >
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, watch } from 'vue'
import { Heart, Search, ShoppingBag, UserRound, X } from 'lucide-vue-next'

const props = defineProps({
  modelValue: { type: String, default: '' },
})
const emit = defineEmits(['update:modelValue'])
const keyword = ref(props.modelValue)
const menu = ['Trang chủ', 'Nam', 'Nữ', 'Áo', 'Quần', 'Phụ kiện', 'Khuyến mãi']
watch(
  () => props.modelValue,
  (value) => {
    keyword.value = value
  },
)
watch(keyword, (value) => emit('update:modelValue', value))
const emitSearch = () => emit('update:modelValue', keyword.value.trim())
</script>
