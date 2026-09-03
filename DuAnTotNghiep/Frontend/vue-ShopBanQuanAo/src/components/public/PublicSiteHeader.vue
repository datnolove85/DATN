<template>
  <header class="sticky top-0 z-[90] w-full bg-white/95 shadow-sm backdrop-blur">
    <div class="bg-[#d30f15] px-4 py-2 text-center text-xs font-bold text-white md:text-sm">
      MỪNG SIÊU LỄ · GIẢM ĐẾN 50% TOÀN BỘ SẢN PHẨM
    </div>
    <div class="border-b border-amber-100 bg-[#f4eee0] px-4 py-4 md:px-8">
      <div class="mx-auto flex max-w-[1480px] items-center gap-5">
        <button type="button" class="flex shrink-0 items-center gap-2" @click="go('/home')">
          <Shirt :size="30" /><span class="text-2xl font-black tracking-tight md:text-3xl"
            >K-ZONE</span
          >
        </button>
        <div class="hidden flex-1 md:block">
          <div class="relative mx-auto max-w-2xl">
            <input
              v-model="keyword"
              type="text"
              placeholder="Tìm kiếm sản phẩm..."
              class="w-full rounded-xl border border-zinc-300 bg-white py-2.5 pl-4 pr-11 text-sm outline-none focus:border-red-600 focus:ring-2 focus:ring-red-600/15"
              @keydown.enter="search"
            /><button
              type="button"
              class="absolute right-3 top-2.5 text-zinc-500 hover:text-red-600"
              @click="search"
            >
              <Search :size="19" />
            </button>
          </div>
        </div>
        <div class="ml-auto flex items-center gap-3 text-zinc-800">
          <button
            type="button"
            title="Hệ thống cửa hàng"
            class="hidden rounded-full p-2 hover:bg-black/5 hover:text-red-600 md:inline-flex"
            @click="go('/he-thong-cua-hang')"
          >
            <Store :size="21" /></button
          ><button
            type="button"
            title="Giỏ hàng"
            class="rounded-full p-2 hover:bg-black/5 hover:text-red-600"
            @click="go('/giohang')"
          >
            <ShoppingBag :size="21" /></button
          ><button
            type="button"
            title="Tài khoản"
            class="rounded-full p-2 hover:bg-black/5 hover:text-red-600"
            @click="go('/tai-khoan')"
          >
            <User :size="21" />
          </button>
        </div>
      </div>
    </div>
    <nav class="overflow-x-auto bg-[#800000] text-white">
      <div class="mx-auto flex min-w-max max-w-[1480px] items-center gap-1 px-4 md:px-8">
        <button type="button" class="nav-btn" @click="go('/home')">Trang chủ</button
        ><button type="button" class="nav-btn" @click="go('/home')">Giới thiệu</button
        ><button
          type="button"
          class="nav-btn font-extrabold text-yellow-300"
          @click="go('/flash-sale')"
        >
          ⚡ Flash Sale</button
        ><button type="button" class="nav-btn" @click="go('/tin-tuc')">Tin tức mới</button
        ><button type="button" class="nav-btn" @click="go('/san-pham')">Sản phẩm</button
        ><button type="button" class="nav-btn" @click="go('/he-thong-cua-hang')">
          Hệ thống cửa hàng</button
        ><button type="button" class="nav-btn" @click="go('/lien-he')">Liên hệ</button>
      </div>
    </nav>
  </header>
</template>
<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Shirt, Search, Store, ShoppingBag, User } from 'lucide-vue-next'
const router = useRouter()
const keyword = ref('')
const go = (path) => router.push(path).catch(() => {})
const search = () => {
  const q = keyword.value.trim()
  router.push({ path: '/san-pham', query: q ? { keyword: q } : {} }).catch(() => {})
}
</script>
<style scoped>
.nav-btn {
  white-space: nowrap;
  padding: 0.9rem 0.9rem;
  font-size: 0.85rem;
  font-weight: 700;
  transition:
    color 0.2s ease,
    background 0.2s ease;
}
.nav-btn:hover {
  color: #fde047;
  background: rgba(0, 0, 0, 0.08);
}
@media (min-width: 1024px) {
  .nav-btn {
    font-size: 0.95rem;
    padding-left: 1rem;
    padding-right: 1rem;
  }
}
</style>
