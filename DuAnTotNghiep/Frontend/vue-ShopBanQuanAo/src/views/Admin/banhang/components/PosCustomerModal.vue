<template>
  <div
    v-if="show"
    class="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/60 backdrop-blur-xs p-4"
  >
    <div
      class="bg-white rounded-2xl shadow-2xl max-w-lg w-full overflow-hidden border border-slate-100"
    >
      <!-- Header Tabs -->
      <div
        class="px-5 py-3 bg-slate-50 border-b border-slate-200/80 flex items-center justify-between"
      >
        <div class="flex gap-2">
          <button
            @click="activeTab = 'search'"
            :class="[
              'px-3 py-1.5 rounded-lg text-xs font-bold transition-all',
              activeTab === 'search'
                ? 'bg-indigo-600 text-white'
                : 'text-slate-600 hover:bg-slate-100',
            ]"
          >
            Tìm khách hàng
          </button>
          <button
            @click="activeTab = 'create'"
            :class="[
              'px-3 py-1.5 rounded-lg text-xs font-bold transition-all',
              activeTab === 'create'
                ? 'bg-indigo-600 text-white'
                : 'text-slate-600 hover:bg-slate-100',
            ]"
          >
            Thêm mới
          </button>
        </div>
        <button
          @click="$emit('update:show', false)"
          class="w-7 h-7 rounded-full bg-slate-200/60 hover:bg-rose-500 hover:text-white flex items-center justify-center text-xs font-bold text-slate-500"
        >
          ✕
        </button>
      </div>

      <!-- Tab Tìm kiếm -->
      <div v-if="activeTab === 'search'" class="p-5 space-y-3">
        <input
          v-model="searchQuery"
          placeholder="Nhập tên hoặc số điện thoại..."
          class="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-xs font-medium focus:outline-none focus:border-indigo-500"
        />
        <div class="max-h-60 overflow-y-auto custom-scrollbar space-y-1.5">
          <div
            v-for="c in filteredList"
            :key="c.id"
            @click="$emit('select', c)"
            class="p-2.5 hover:bg-indigo-50/80 rounded-xl border border-slate-100 hover:border-indigo-200 cursor-pointer transition-all flex items-center justify-between"
          >
            <div>
              <p class="font-bold text-xs text-slate-800">{{ c.hoTen }}</p>
              <p class="text-[11px] text-slate-500 font-mono">{{ c.soDienThoai }}</p>
            </div>
            <div class="text-right">
              <span
                class="px-2 py-0.5 rounded-full bg-amber-100 text-amber-800 text-[10px] font-bold"
              >
                {{ c.diemTichLuy || 0 }} xu
              </span>
            </div>
          </div>
        </div>
      </div>

      <!-- Tab Thêm mới -->
      <form v-else @submit.prevent="handleSave" class="p-5 space-y-3 text-xs">
        <div>
          <label class="block font-bold text-slate-700 mb-1">Họ và tên *</label>
          <input
            v-model="form.hoTen"
            required
            placeholder="Nguyễn Văn A"
            class="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl font-medium focus:outline-none focus:border-indigo-500"
          />
        </div>
        <div>
          <label class="block font-bold text-slate-700 mb-1">Số điện thoại *</label>
          <input
            v-model="form.soDienThoai"
            required
            placeholder="0912345678"
            class="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl font-medium focus:outline-none focus:border-indigo-500"
          />
        </div>
        <div>
          <label class="block font-bold text-slate-700 mb-1">Email</label>
          <input
            v-model="form.email"
            type="email"
            placeholder="khachhang@gmail.com"
            class="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl font-medium focus:outline-none focus:border-indigo-500"
          />
        </div>
        <div class="pt-2 flex justify-end gap-2">
          <button
            type="button"
            @click="activeTab = 'search'"
            class="px-4 py-2 border border-slate-200 rounded-xl font-bold text-slate-600 hover:bg-slate-100"
          >
            Quay lại
          </button>
          <button
            type="submit"
            class="px-5 py-2 bg-indigo-600 text-white rounded-xl font-extrabold shadow-md"
          >
            Lưu & Chọn
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  show: Boolean,
  customers: { type: Array, default: () => [] },
})

const emit = defineEmits(['update:show', 'select', 'save-new'])

const activeTab = ref('search')
const searchQuery = ref('')
const form = ref({ hoTen: '', soDienThoai: '', email: '' })

const filteredList = computed(() => {
  if (!searchQuery.value) return props.customers
  const q = searchQuery.value.toLowerCase()
  return props.customers.filter(
    (c) => c.hoTen?.toLowerCase().includes(q) || c.soDienThoai?.includes(q),
  )
})

const handleSave = () => {
  emit('save-new', { ...form.value })
  form.value = { hoTen: '', soDienThoai: '', email: '' }
  activeTab.value = 'search'
}
</script>
