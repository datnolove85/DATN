<template>
  <div class="min-h-screen bg-gray-50">
    <ShopHeader />

    <router-view />

    <ShopFooter />
    <ShopChatbot />
  </div>
</template>

<script setup>
import ShopHeader from '../components/ShopHeader.vue'
import ShopFooter from '../components/ShopFooter.vue'
import ShopChatbot from '@/components/ShopChatbot.vue'
</script>
