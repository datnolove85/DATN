<template>
  <!-- Giao diện tràn viền, nền trắng toàn màn hình -->
  <div class="min-h-screen bg-white flex flex-col">
    <!-- Header: vẫn giới hạn chiều rộng -->
    <div class="w-full max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <ShopHeader />
    </div>

    <!-- Nội dung các trang: giữ nguyên -->
    <main class="flex-1">
      <router-view />
    </main>

    <!-- Footer: full width màn hình -->
    <ShopFooter />

    <!-- Chatbot hỗ trợ -->
    <ShopChatbot />
  </div>
</template>

<script setup>
import ShopHeader from '../components/ShopHeader.vue'
import ShopFooter from '../components/ShopFooter.vue'
import ShopChatbot from '@/components/ShopChatbot.vue'
</script>
