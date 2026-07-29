<template>
  <div class="flex h-screen w-screen bg-slate-50 font-sans antialiased overflow-hidden">
    <Sidebar />

    <div class="flex-1 flex flex-col overflow-hidden">
      <Navbar />

      <main class="flex-1 overflow-x-hidden overflow-y-auto p-6 bg-slate-50">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </main>
    </div>
  </div>
</template>

<script setup>
import Sidebar from '../components/Sidebar.vue' // Hãy chắc chắn đường dẫn này đúng với dự án của bạn
import Navbar from '../components/Navbar.vue' // Hãy chắc chắn đường dẫn này đúng với dự án của bạn
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.15s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
