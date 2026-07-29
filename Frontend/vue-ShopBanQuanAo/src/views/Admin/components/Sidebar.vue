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
          <span class="mr-3 text-base w-5 text-center">📊</span>
          Bảng điều khiển
        </router-link>
        <router-link
          to="/admin/thongke"
          class="flex items-center px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
          active-class="bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20"
        >
          <span class="mr-3 text-base w-5 text-center">📈</span>
          Thống kê
        </router-link>

        <router-link
          to="/admin/nhan-vien"
          class="flex items-center px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
          active-class="bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20"
        >
          <span class="mr-3 text-base w-5 text-center">👥</span>
          Quản lý Nhân viên
        </router-link>

        <p class="px-4 text-xs font-semibold text-slate-500 uppercase tracking-wider pt-4 mb-2">
          Kinh doanh
        </p>

        <router-link
          to="/admin/banhang"
          class="flex items-center px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
          active-class="bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20"
        >
          <span class="mr-3 text-base w-5 text-center">🛒</span>
          Bán hàng
        </router-link>
        <router-link
          to="/admin/hoadononline"
          class="flex items-center px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
          active-class="bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20"
        >
          <span class="mr-3 text-base w-5 text-center">📦</span>
          Đơn hàng Online
        </router-link>

        <div class="space-y-1">
          <button
            @click="isOpenHoaDon = !isOpenHoaDon"
            class="w-full flex items-center justify-between px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
            :class="[
              isOpenHoaDon ? 'bg-slate-800/50 text-white' : '',
              activeHoaDon
                ? 'bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20'
                : '',
            ]"
          >
            <div class="flex items-center">
              <span class="mr-3 text-base w-5 text-center">🧾</span>
              <span>Quản lý Hóa đơn</span>
            </div>

            <span
              class="text-[10px] transition-transform duration-200"
              :class="{ 'rotate-180': isOpenHoaDon }"
            >
              ▼
            </span>
          </button>

          <div v-show="isOpenHoaDon" class="pl-4 space-y-1">
            <router-link
              to="/admin/hoadon"
              class="flex items-center px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/40 rounded-md text-sm transition-colors"
              active-class="text-indigo-400 font-medium bg-slate-800/60"
            >
              <span class="mr-3 text-xs w-5 text-center">📄</span>
              Hóa đơn
            </router-link>

            <router-link
              to="/admin/hdct"
              class="flex items-center px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/40 rounded-md text-sm transition-colors"
              active-class="text-indigo-400 font-medium bg-slate-800/60"
            >
              <span class="mr-3 text-xs w-5 text-center">📑</span>
              Chi tiết hóa đơn
            </router-link>
          </div>
        </div>

        <router-link
          to="/admin/khachhang"
          class="flex items-center px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
          active-class="bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20"
        >
          <span class="mr-3 text-base w-5 text-center">👤</span>
          Quản lý Khách hàng
        </router-link>

        <div class="space-y-1">
          <button
            @click="isOpenDiscount = !isOpenDiscount"
            class="w-full flex items-center justify-between px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
            :class="[
              isOpenDiscount ? 'bg-slate-800/50 text-white' : '',
              activeDiscount
                ? 'bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20'
                : '',
            ]"
          >
            <div class="flex items-center">
              <span class="mr-3 text-base w-5 text-center">🏷️</span>
              <span>Sales</span>
            </div>

            <span
              class="text-[10px] transition-transform duration-200"
              :class="{ 'rotate-180': isOpenDiscount }"
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
              <span class="mr-3 text-xs w-5 text-center">🎟️</span>
              Đợt giảm giá
            </router-link>

            <router-link
              to="/admin/voucher"
              class="flex items-center px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/40 rounded-md text-sm transition-colors"
              active-class="text-indigo-400 font-medium bg-slate-800/60"
            >
              <span class="mr-3 text-xs w-5 text-center">🎟️</span>
              Voucher
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
            :class="[
              isOpenCategory ? 'bg-slate-800/50 text-white' : '',
              activeCategory
                ? 'bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20'
                : '',
            ]"
          >
            <div class="flex items-center">
              <span class="mr-3 text-base w-5 text-center">📂</span>
              <span>Quản lý danh mục</span>
            </div>

            <span
              class="text-[10px] transition-transform duration-200"
              :class="{ 'rotate-180': isOpenCategory }"
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
              <span class="mr-3 text-xs w-5 text-center">📋</span>
              Danh mục
            </router-link>
          </div>
        </div>

        <div class="space-y-1">
          <button
            @click="isOpenProduct = !isOpenProduct"
            class="w-full flex items-center justify-between px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
            :class="[
              isOpenProduct ? 'bg-slate-800/50 text-white' : '',
              activeProduct
                ? 'bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20'
                : '',
            ]"
          >
            <div class="flex items-center">
              <span class="mr-3 text-base w-5 text-center">🛍️</span>
              <span>Quản lý Sản phẩm</span>
            </div>

            <span
              class="text-[10px] transition-transform duration-200"
              :class="{ 'rotate-180': isOpenProduct }"
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
              <span class="mr-3 text-xs w-5 text-center">📄</span>
              Danh sách sản phẩm
            </router-link>

            <!-- <router-link
              to="/admin/spct"
              class="flex items-center px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/40 rounded-md text-sm transition-colors"
              active-class="text-indigo-400 font-medium bg-slate-800/60"
            >
              <span class="mr-3 text-xs w-5 text-center">📑</span>
              Biến thể sản phẩm
            </router-link> -->
          </div>
        </div>
        <div class="space-y-1">
          <button
            @click="isOpenAttribute = !isOpenAttribute"
            class="w-full flex items-center justify-between px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
            :class="[
              isOpenAttribute ? 'bg-slate-800/50 text-white' : '',
              activeAttribute
                ? 'bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20'
                : '',
            ]"
          >
            <div class="flex items-center">
              <span class="mr-3 text-base w-5 text-center">🎨</span>
              <span>Quản lý thuộc tính</span>
            </div>

            <span
              class="text-[10px] transition-transform duration-200"
              :class="{ 'rotate-180': isOpenAttribute }"
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
              <span class="mr-3 text-xs w-5 text-center">🔴</span>
              Màu sắc
            </router-link>

            <router-link
              to="/admin/sizes"
              class="flex items-center px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/40 rounded-md text-sm transition-colors"
              active-class="text-indigo-400 font-medium bg-slate-800/60"
            >
              <span class="mr-3 text-xs w-5 text-center">📏</span>
              Kích cỡ
            </router-link>

            <router-link
              to="/admin/materials"
              class="flex items-center px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/40 rounded-md text-sm transition-colors"
              active-class="text-indigo-400 font-medium bg-slate-800/60"
            >
              <span class="mr-3 text-xs w-5 text-center">🧶</span>
              Chất liệu
            </router-link>

            <router-link
              to="/admin/brands"
              class="flex items-center px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/40 rounded-md text-sm transition-colors"
              active-class="text-indigo-400 font-medium bg-slate-800/60"
            >
              <span class="mr-3 text-xs w-5 text-center">🏬</span>
              Thương hiệu
            </router-link>
          </div>
        </div>
      </nav>
    </div>
  </aside>
</template>
<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const activeHoaDon = computed(() => ['/admin/hoadon', '/admin/hdct'].includes(route.path))

const activeDiscount = computed(() => ['/admin/sales', '/admin/voucher'].includes(route.path))

const activeCategory = computed(() => ['/admin/categories'].includes(route.path))

const activeProduct = computed(() => ['/admin/sanpham', '/admin/spct'].includes(route.path))

const activeAttribute = computed(() =>
  ['/admin/mausac', '/admin/sizes', '/admin/materials', '/admin/brands'].includes(route.path),
)

const isOpenHoaDon = ref(activeHoaDon.value)
const isOpenDiscount = ref(activeDiscount.value)
const isOpenCategory = ref(activeCategory.value)
const isOpenProduct = ref(activeProduct.value)
const isOpenAttribute = ref(activeAttribute.value)
</script>
<style scoped>
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
