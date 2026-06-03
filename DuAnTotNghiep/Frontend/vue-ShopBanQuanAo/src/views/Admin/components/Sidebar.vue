<template>
  <aside
    class="w-64 bg-slate-900 text-white flex-col hidden md:flex shrink-0 shadow-xl border-r border-slate-800 h-screen overflow-y-auto custom-scrollbar"
  >
    <div class="flex-1">
      <div
        class="h-16 flex items-center justify-center border-b border-slate-800 text-lg font-bold tracking-wider text-indigo-400 gap-2 shrink-0"
      >
        <a href="#" class="text-2xl font-extrabold tracking-tight">
          Vel<span class="text-amber-500">o</span>ra
        </a>
      </div>

      <nav class="mt-6 px-3 space-y-1 pb-6">
        <p class="px-4 text-xs font-semibold text-slate-500 uppercase tracking-wider mb-2">
          Hệ thống
        </p>

        <router-link
          to="/admin/dashboard"
          class="flex items-center px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
          active-class="bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20"
        >
          <span class="mr-3 text-base w-5 text-center">📊</span> Bảng điều khiển
        </router-link>

        <router-link
          to="/admin/nhan-vien"
          class="flex items-center px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
          active-class="bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20"
        >
          <span class="mr-3 text-base w-5 text-center">👥</span> Quản lý Nhân viên
        </router-link>

        <p class="px-4 text-xs font-semibold text-slate-500 uppercase tracking-wider pt-4 mb-2">
          Kinh doanh
        </p>

        <router-link
          to="/admin/banhang"
          class="flex items-center px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
          active-class="bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20"
        >
          <span class="mr-3 text-base w-5 text-center">🛒</span> Bán hàng
        </router-link>

        <router-link
          to="/admin/invoices"
          class="flex items-center px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
          active-class="bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20"
        >
          <span class="mr-3 text-base w-5 text-center">🧾</span> Quản lý Hóa đơn
        </router-link>

        <router-link
          to="/admin/customers"
          class="flex items-center px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
          active-class="bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20"
        >
          <span class="mr-3 text-base w-5 text-center">👤</span> Quản lý Khách hàng
        </router-link>

        <div class="space-y-1">
          <button
            @click="isOpenDiscount = !isOpenDiscount"
            class="w-full flex items-center justify-between px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
            :class="{ 'bg-slate-800/50 text-white': isOpenDiscount }"
          >
            <div class="flex items-center">
              <span class="mr-3 text-base w-5 text-center">🏷️</span>
              <span>Sales</span>
            </div>
            <span
              class="text-[10px] transition-transform duration-200 text-slate-500"
              :class="{ 'rotate-180 text-slate-300': isOpenDiscount }"
            >
              ▼
            </span>
          </button>

          <div v-show="isOpenDiscount" class="pl-4 space-y-1">
            <router-link
              to="/admin/sales"
              class="flex items-center px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/40 rounded-md text-sm transition-colors"
              active-class="text-indigo-400 font-medium bg-slate-800/60"
            >
              <span class="mr-3 text-xs w-5 text-center">🎟️</span> Đợt giảm giá
            </router-link>
          </div>
        </div>

        <p class="px-4 text-xs font-semibold text-slate-500 uppercase tracking-wider pt-4 mb-2">
          Sản phẩm & Kho
        </p>

        <div class="space-y-1">
          <button
            @click="isOpenCategory = !isOpenCategory"
            class="w-full flex items-center justify-between px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
            :class="{ 'bg-slate-800/50 text-white': isOpenCategory }"
          >
            <div class="flex items-center">
              <span class="mr-3 text-base w-5 text-center">📂</span>
              <span>Quản lý danh mục</span>
            </div>
            <span
              class="text-[10px] transition-transform duration-200 text-slate-500"
              :class="{ 'rotate-180 text-slate-300': isOpenCategory }"
            >
              ▼
            </span>
          </button>
          <div v-show="isOpenCategory" class="pl-4 space-y-1">
            <router-link
              to="/admin/categories"
              class="flex items-center px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/40 rounded-md text-sm transition-colors"
              active-class="text-indigo-400 font-medium bg-slate-800/60"
            >
              <span class="mr-3 text-xs w-5 text-center">📋</span> Danh mục
            </router-link>
          </div>
        </div>

        <div class="space-y-1">
          <button
            @click="isOpenProduct = !isOpenProduct"
            class="w-full flex items-center justify-between px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
            :class="{ 'bg-slate-800/50 text-white': isOpenProduct }"
          >
            <div class="flex items-center">
              <span class="mr-3 text-base w-5 text-center">🛍️</span>
              <span>Quản lý Sản phẩm</span>
            </div>
            <span
              class="text-[10px] transition-transform duration-200 text-slate-500"
              :class="{ 'rotate-180 text-slate-300': isOpenProduct }"
            >
              ▼
            </span>
          </button>
          <div v-show="isOpenProduct" class="pl-4 space-y-1">
            <router-link
              to="/admin/sanpham"
              class="flex items-center px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/40 rounded-md text-sm transition-colors"
              active-class="text-indigo-400 font-medium bg-slate-800/60"
            >
              <span class="mr-3 text-xs w-5 text-center">📄</span> Danh sách sản phẩm
            </router-link>
            <router-link
              to="/admin/spct"
              class="flex items-center px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/40 rounded-md text-sm transition-colors"
              active-class="text-indigo-400 font-medium bg-slate-800/60"
            >
              <span class="mr-3 text-xs w-5 text-center">📑</span> Biến thể sản phẩm
            </router-link>
          </div>
        </div>

        <div class="space-y-1">
          <button
            @click="isOpenAttribute = !isOpenAttribute"
            class="w-full flex items-center justify-between px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
            :class="{ 'bg-slate-800/50 text-white': isOpenAttribute }"
          >
            <div class="flex items-center">
              <span class="mr-3 text-base w-5 text-center">🎨</span>
              <span>Quản lý thuộc tính</span>
            </div>
            <span
              class="text-[10px] transition-transform duration-200 text-slate-500"
              :class="{ 'rotate-180 text-slate-300': isOpenAttribute }"
            >
              ▼
            </span>
          </button>
          <div v-show="isOpenAttribute" class="pl-4 space-y-1">
            <router-link
              to="/admin/mausac"
              class="flex items-center px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/40 rounded-md text-sm transition-colors"
              active-class="text-indigo-400 font-medium bg-slate-800/60"
            >
              <span class="mr-3 text-xs w-5 text-center">🔴</span> Màu sắc
            </router-link>
            <router-link
              to="/admin/sizes"
              class="flex items-center px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/40 rounded-md text-sm transition-colors"
              active-class="text-indigo-400 font-medium bg-slate-800/60"
            >
              <span class="mr-3 text-xs w-5 text-center">📏</span> Kích cỡ
            </router-link>
            <router-link
              to="/admin/materials"
              class="flex items-center px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/40 rounded-md text-sm transition-colors"
              active-class="text-indigo-400 font-medium bg-slate-800/60"
            >
              <span class="mr-3 text-xs w-5 text-center">🧶</span> Chất liệu
            </router-link>
            <router-link
              to="/admin/brands"
              class="flex items-center px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/40 rounded-md text-sm transition-colors"
              active-class="text-indigo-400 font-medium bg-slate-800/60"
            >
              <span class="mr-3 text-xs w-5 text-center">🏬</span> Thương hiệu
            </router-link>
          </div>
        </div>
      </nav>
    </div>
  </aside>
</template>

<style scoped>
/* Tùy chỉnh thanh cuộn tinh tế */
.custom-scrollbar::-webkit-scrollbar {
  width: 5px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: #0f172a;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: #334155;
  border-radius: 10px;
}
.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background-color: #475569;
}
</style>

<script setup>
import { ref } from 'vue'

const isOpenCategory = ref(false)
const isOpenProduct = ref(false)
const isOpenAttribute = ref(false)
const isOpenDiscount = ref(false)
</script>
