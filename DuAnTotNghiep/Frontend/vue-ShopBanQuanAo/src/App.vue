<template>
  <div>
    <router-view />
    <ChatWidget />
  </div>
</template>

<script setup>
import HeaderLayout from './layouts/HeaderLayout.vue'
import NavLayout from './layouts/NavLayout.vue'
import ChatWidget from './components/ChatWidget.vue'
</script>

<style scoped></style>
