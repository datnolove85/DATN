<template>
  <!-- Giao diện tràn viền, nền trắng toàn màn hình -->
  <div class="min-h-screen bg-white flex flex-col">
    <!-- Thanh Navigation: Đã được giới hạn chiều rộng khớp với khối sản phẩm bên dưới -->
    <div class="w-full max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <ShopHeader />
    </div>

    <!-- Nội dung các trang thay đổi theo Router -->
    <main class="flex-1">
      <router-view />
    </main>

    <!-- Chân trang (Bọc tương tự để thẳng hàng với Navigation và nội dung) -->
    <div class="w-full max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <ShopFooter />
    </div>

    <!-- Chatbot hỗ trợ -->
    <ShopChatbot />
    <CustomerChatWidget />
  </div>
</template>

<script setup>
import ShopHeader from '../components/ShopHeader.vue'
import ShopFooter from '../components/ShopFooter.vue'
import ShopChatbot from '@/components/ShopChatbot.vue'
import CustomerChatWidget from '@/components/CustomerChatWidget.vue'
</script>
