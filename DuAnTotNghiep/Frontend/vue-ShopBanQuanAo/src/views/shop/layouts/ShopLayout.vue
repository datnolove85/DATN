<template>
  <div class="min-h-screen bg-gray-50">
    <ShopHeader />

    <router-view />

    <ShopFooter />
  </div>
</template>

<script setup>
import ShopHeader from '../components/ShopHeader.vue'
import ShopFooter from '../components/ShopFooter.vue'
</script>
