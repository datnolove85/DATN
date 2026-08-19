<template>
  <aside
    class="w-64 bg-slate-900 text-white flex-col hidden md:flex shrink-0 shadow-xl border-r border-slate-800 h-screen overflow-y-auto custom-scrollbar"
  >
    <div class="flex-1">
      <!-- ✨ LOGO THIẾT KẾ MỚI XỊN XÒ HƠN -->
      <div
        class="h-20 flex items-center px-6 border-b border-slate-800/80 bg-slate-950/40 shrink-0"
      >
        <router-link to="/admin/dashboard" class="flex items-center gap-3.5 group w-full">
          <!-- Biểu tượng Logo dạng Badge Gradient -->
          <div
            class="relative flex items-center justify-center w-10 h-10 rounded-xl bg-gradient-to-tr from-indigo-600 via-purple-600 to-amber-500 shadow-lg shadow-indigo-600/30 group-hover:scale-105 transition-all duration-300 ring-1 ring-white/20"
          >
            <span class="text-white text-lg font-black tracking-tighter">K</span>
            <div
              class="absolute -bottom-1 -right-1 w-4 h-4 bg-amber-500 rounded-full border-2 border-slate-900 flex items-center justify-center text-[9px] font-bold text-slate-950 shadow"
            >
              Z
            </div>
          </div>

          <!-- Tên thương hiệu & Subtitle -->
          <div class="flex flex-col">
            <span
              class="text-xl font-black tracking-wider bg-gradient-to-r from-white via-slate-100 to-indigo-300 bg-clip-text text-transparent"
            >
              K-<span class="text-amber-400">ZONE</span>
            </span>
            <span class="text-[10px] font-bold tracking-widest text-indigo-400 uppercase -mt-0.5">
              Admin Portal
            </span>
          </div>
        </router-link>
      </div>

      <nav class="mt-6 px-3 space-y-1 pb-6">
        <p class="px-4 text-xs font-semibold text-slate-500 uppercase tracking-wider mb-2">
          Hệ thống
        </p>

        <!-- Thống kê (Cả Admin & Staff đều thấy) -->
        <router-link
          to="/admin/thongke"
          class="flex items-center px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
          active-class="bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20"
        >
          <span class="mr-3 text-base w-5 text-center">📈</span>
          Thống kê
        </router-link>

        <!-- 🔒 Quản lý Nhân viên (Chỉ ADMIN) -->
        <router-link
          v-if="isAdmin"
          to="/admin/nhan-vien"
          class="flex items-center px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
          active-class="bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20"
        >
          <span class="mr-3 text-base w-5 text-center">👥</span>
          Quản lý Nhân viên
        </router-link>

        <!-- Quản lý Khách hàng (Cả Admin & Staff đều thấy) -->
        <router-link
          to="/admin/khachhang"
          class="flex items-center px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
          active-class="bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20"
        >
          <span class="mr-3 text-base w-5 text-center">👤</span>
          Quản lý Khách hàng
        </router-link>

        <!-- 🔒 Cấu hình Hệ thống (Chỉ ADMIN) -->
        <router-link
          v-if="isAdmin"
          to="/admin/cauhinh"
          class="flex items-center px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
          active-class="bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20"
        >
          <span class="mr-3 text-base w-5 text-center">⚙️</span>
          Cấu hình Hệ thống
        </router-link>

        <p class="px-4 text-xs font-semibold text-slate-500 uppercase tracking-wider pt-4 mb-2">
          Kinh doanh
        </p>

        <!-- Bán hàng (Cả Admin & Staff) -->
        <router-link
          to="/admin/banhang"
          class="flex items-center px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
          active-class="bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20"
        >
          <span class="mr-3 text-base w-5 text-center">🛒</span>
          Bán hàng
        </router-link>

        <!-- Đơn hàng Online (Cả Admin & Staff) -->
        <router-link
          to="/admin/hoadononline"
          class="flex items-center px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
          active-class="bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20"
        >
          <span class="mr-3 text-base w-5 text-center">📦</span>
          Đơn hàng Online
        </router-link>

        <!-- Hóa đơn (Cả Admin & Staff) -->
        <router-link
          to="/admin/hoadon"
          class="flex items-center px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
          active-class="bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20"
        >
          <span class="mr-3 text-base w-5 text-center">🧾</span>
          Hóa đơn
        </router-link>

        <!-- 🔒 Quản lý Minigame (Chỉ ADMIN) -->
        <div v-if="isAdmin" class="space-y-1">
          <button
            @click="isOpenGamification = !isOpenGamification"
            class="w-full flex items-center justify-between px-4 py-2.5 text-slate-300 hover:bg-slate-800 hover:text-white rounded-lg transition-all text-sm"
            :class="[
              isOpenGamification ? 'bg-slate-800/50 text-white' : '',
              activeGamification
                ? 'bg-indigo-600 text-white font-medium shadow-md shadow-indigo-600/20'
                : '',
            ]"
          >
            <div class="flex items-center">
              <span class="mr-3 text-base w-5 text-center">🎡</span>
              <span>Quản lý Minigame</span>
            </div>

            <span
              class="text-[10px] transition-transform duration-200"
              :class="{ 'rotate-180': isOpenGamification }"
            >
              ▼
            </span>
          </button>
          <div v-show="isOpenGamification" class="pl-4 space-y-1">
            <router-link
              to="/admin/gamification"
              class="flex items-center px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/40 rounded-md text-sm transition-colors"
              active-class="text-indigo-400 font-medium bg-slate-800/60"
            >
              <span class="mr-3 text-xs w-5 text-center">⚙️</span>
              Cấu hình Minigame
            </router-link>

            <router-link
              to="/admin/kho-voucher"
              class="flex items-center px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/40 rounded-md text-sm transition-colors"
              active-class="text-indigo-400 font-medium bg-slate-800/60"
            >
              <span class="mr-3 text-xs w-5 text-center">🗄️</span>
              Kho Voucher KH
            </router-link>
          </div>
        </div>

        <!-- 🔒 Sales (Chỉ ADMIN) -->
        <div v-if="isAdmin" class="space-y-1">
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
              Quản lý Voucher
            </router-link>
          </div>
        </div>

        <!-- 🔒 Sản phẩm & Kho (Chỉ ADMIN thấy toàn bộ nhóm này) -->
        <template v-if="isAdmin">
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
        </template>
      </nav>
    </div>
  </aside>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

// 1. Đọc thông tin user từ sessionStorage để kiểm tra phân quyền hiển thị menu
const userStr = sessionStorage.getItem('user')
const currentUser = userStr ? JSON.parse(userStr) : {}

// Kiểm tra xem tài khoản hiện tại có phải ADMIN không
const isAdmin = computed(() => currentUser.vaiTro === 'ADMIN')

const activeDiscount = computed(() => ['/admin/sales', '/admin/voucher'].includes(route.path))

const activeGamification = computed(() =>
  ['/admin/gamification', '/admin/kho-voucher'].includes(route.path),
)

const activeCategory = computed(() => ['/admin/categories'].includes(route.path))

const activeProduct = computed(() => ['/admin/sanpham', '/admin/spct'].includes(route.path))

const activeAttribute = computed(() =>
  ['/admin/mausac', '/admin/sizes', '/admin/materials', '/admin/brands'].includes(route.path),
)

const isOpenDiscount = ref(activeDiscount.value)
const isOpenGamification = ref(activeGamification.value)
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
