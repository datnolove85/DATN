<template>
  <div class="min-h-screen bg-white flex flex-col relative">
    <header class="w-full max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <ShopHeader />
    </header>

    <main class="flex-1">
      <router-view />
    </main>

    <!-- Đặt trực tiếp ShopFooter ở đây, không bọc trong container giới hạn chiều rộng -->
    <ShopFooter />

    <div class="fixed bottom-6 right-6 z-50">
      <ShopSupportWidget />
    </div>
  </div>
</template>

<script setup>
import ShopHeader from '../components/ShopHeader.vue'
import ShopFooter from '../components/ShopFooter.vue'
import ShopSupportWidget from '@/components/ShopSupportWidget.vue'
</script>
