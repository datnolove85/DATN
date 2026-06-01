<template>
  <aside
    class="w-64 bg-slate-900 text-white flex-col justify-between hidden md:flex shrink-0 shadow-xl border-r border-slate-800"
  >
    <div>
      <!-- LOGO -->
      <div
        class="h-16 flex items-center justify-center border-b border-slate-800 text-lg font-bold tracking-wider text-indigo-400 gap-2"
      >
        <a href="#" class="text-2xl font-extrabold tracking-tight">
          Vel<span class="text-amber-500">o</span>ra
        </a>
      </div>

      <nav class="mt-6 px-3 space-y-1">
        <!-- ================= HỆ THỐNG ================= -->
        <p class="px-4 text-xs font-semibold text-slate-500 uppercase tracking-wider mb-2">
          Hệ thống
        </p>

        <!-- Bảng điều khiển -->
        <router-link
          to="/admin/dashboard"
          class="flex items-center px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
          active-class="bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20"
        >
          <span class="mr-3 text-base">📊</span> Bảng điều khiển
        </router-link>

        <!-- [MỚI] Quản lý Nhân viên -->
        <router-link
          to="/admin/nhan-vien"
          class="flex items-center px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
          active-class="bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20"
        >
          <span class="mr-3 text-base">👥</span> Quản lý Nhân viên
        </router-link>

        <!-- ================= KINH DOANH ================= -->
        <p class="px-4 text-xs font-semibold text-slate-500 uppercase tracking-wider pt-4 mb-2">
          Kinh doanh
        </p>

        <!-- Đơn hàng & Bán hàng -->
        <router-link
          to="/admin/orders"
          class="flex items-center px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
          active-class="bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20"
        >
          <span class="mr-3 text-base">🛒</span> Đơn hàng & Bán hàng
        </router-link>

        <!-- [MỚI] Quản lý Khách hàng -->
        <router-link
          to="/admin/customers"
          class="flex items-center px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
          active-class="bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20"
        >
          <span class="mr-3 text-base">👤</span> Quản lý Khách hàng
        </router-link>

        <!-- [MỚI] Quản lý Giảm giá -->
        <router-link
          to="/admin/discounts"
          class="flex items-center px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
          active-class="bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20"
        >
          <span class="mr-3 text-base">🏷️</span> Chương trình Giảm giá
        </router-link>

        <!-- ================= SẢN PHẨM & KHO ================= -->
        <p class="px-4 text-xs font-semibold text-slate-500 uppercase tracking-wider pt-4 mb-2">
          Sản phẩm & Kho
        </p>

        <!-- Dropdown Quản lý Sản phẩm -->
        <div class="space-y-1">
          <button
            @click="isOpenProduct = !isOpenProduct"
            class="w-full flex items-center justify-between px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
            :class="{ 'bg-slate-800/50 text-white': isOpenProduct }"
          >
            <div class="flex items-center">
              <span class="mr-3 text-base">🛍️</span>
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
              <span class="mr-3 text-xs">📄</span> Danh sách sản phẩm
            </router-link>
            <router-link
              to="/admin/product-variants"
              class="flex items-center px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/40 rounded-md text-sm transition-colors"
              active-class="text-indigo-400 font-medium bg-slate-800/60"
            >
              <span class="mr-3 text-xs">📑</span> Biến thể sản phẩm
            </router-link>
          </div>
        </div>

        <!-- Dropdown Quản lý Thuộc tính -->
        <div class="space-y-1">
          <button
            @click="isOpenAttribute = !isOpenAttribute"
            class="w-full flex items-center justify-between px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
            :class="{ 'bg-slate-800/50 text-white': isOpenAttribute }"
          >
            <div class="flex items-center">
              <span class="mr-3 text-base">🎨</span>
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
              <span class="mr-3 text-xs">🔴</span> Màu sắc
            </router-link>
            <router-link
              to="/admin/sizes"
              class="flex items-center px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/40 rounded-md text-sm transition-colors"
              active-class="text-indigo-400 font-medium bg-slate-800/60"
            >
              <span class="mr-3 text-xs">📏</span> Kích cỡ
            </router-link>
            <router-link
              to="/admin/materials"
              class="flex items-center px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/40 rounded-md text-sm transition-colors"
              active-class="text-indigo-400 font-medium bg-slate-800/60"
            >
              <span class="mr-3 text-xs">🧶</span> Chất liệu
            </router-link>
            <router-link
              to="/admin/brands"
              class="flex items-center px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/40 rounded-md text-sm transition-colors"
              active-class="text-indigo-400 font-medium bg-slate-800/60"
            >
              <span class="mr-3 text-xs">🏬</span> Thương hiệu
            </router-link>
          </div>
        </div>
      </nav>
    </div>

    <!-- USER PROFILE FOOTER -->
    <div class="p-4 border-t border-slate-800 flex items-center justify-between bg-slate-950/40">
      <div class="flex items-center space-x-3">
        <div
          class="w-9 h-9 rounded-full bg-gradient-to-tr from-indigo-500 to-purple-500 flex items-center justify-center font-semibold text-sm shadow"
        >
          NV
        </div>
        <div class="truncate w-32">
          <p class="text-sm font-medium text-slate-200 truncate">Nguyễn Nhân Viên</p>
          <p class="text-xs text-slate-400 truncate">Mã NV: NV001</p>
        </div>
      </div>
      <button
        class="text-slate-400 hover:text-rose-400 p-1.5 hover:bg-slate-800 rounded-lg transition-colors"
        title="Đăng xuất"
      >
        🔌
      </button>
    </div>
  </aside>
</template>

<script setup>
import { ref } from 'vue'

const isOpenProduct = ref(false)
const isOpenAttribute = ref(false)
</script>
