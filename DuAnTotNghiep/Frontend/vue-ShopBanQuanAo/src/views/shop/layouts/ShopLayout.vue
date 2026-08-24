<template>
  <!-- Giao diện tràn viền, nền trắng toàn màn hình -->
  <div class="min-h-screen bg-white flex flex-col relative">
    <!-- Thanh Navigation -->
    <div class="w-full max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <ShopHeader />
    </div>

    <!-- Nội dung các trang thay đổi theo Router -->
    <main class="flex-1">
      <router-view />
    </main>

    <!-- Chân trang chuẩn thương mại điện tử -->
    <footer class="w-full bg-gray-900 text-gray-300 mt-12">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <ShopFooter />
      </div>
    </footer>

    <!-- CHỈ DÙNG 1 NÚT HỖ TRỢ DUY NHẤT Ở GÓC PHẢI -->
    <div class="fixed bottom-6 right-6 z-50">
      <ShopSupportWidget />
    </div>
  </div>
</template>

<script setup>
import ShopHeader from '../components/ShopHeader.vue'
import ShopFooter from '../components/ShopFooter.vue'
import ShopSupportWidget from '@/components/ShopSupportWidget.vue' // Component gộp chung mới tạo
</script>
