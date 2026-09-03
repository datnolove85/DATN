<template>
  <div class="min-h-screen bg-[#f5f5f5] text-zinc-900">
    <PublicSiteHeader />

    <main class="mx-auto max-w-[1480px] px-4 py-6 md:px-8 md:py-8">
      <section class="overflow-hidden rounded-3xl bg-[#800000] text-white shadow-2xl">
        <div class="border-b border-white/10 px-5 py-6 md:px-8 md:py-7">
          <div class="flex flex-col gap-5 lg:flex-row lg:items-end lg:justify-between">
            <div>
              <div
                class="inline-flex items-center gap-2 rounded-full bg-yellow-400 px-3 py-1 text-xs font-black uppercase text-red-950"
              >
                <Zap :size="15" class="fill-red-900" />
                {{ isUpcoming ? 'Sắp diễn ra' : 'Đang diễn ra' }}
              </div>
              <h1 class="mt-3 text-3xl font-black uppercase tracking-tight md:text-5xl">
                Flash Sale K-ZONE
              </h1>
              <p class="mt-2 max-w-2xl text-sm leading-6 text-red-100 md:text-base">
                {{ currentCampaign?.tenDotGiamGia || 'Ưu đãi nổi bật hôm nay' }}
                <span v-if="currentCampaign?.moTa"> · {{ currentCampaign.moTa }}</span>
              </p>
            </div>

            <div class="rounded-2xl bg-black/20 px-4 py-3 backdrop-blur">
              <p class="text-[11px] font-bold uppercase tracking-wider text-red-100">
                {{ isUpcoming ? 'Bắt đầu sau' : 'Kết thúc sau' }}
              </p>
              <div class="mt-2 flex items-center gap-1.5 font-mono text-xl font-black md:text-2xl">
                <span
                  v-for="part in countdownParts"
                  :key="part.label"
                  class="rounded-lg bg-white px-2.5 py-1 text-[#800000]"
                >
                  {{ part.value }}
                </span>
                <span class="text-white">:</span>
                <span class="rounded-lg bg-white px-2.5 py-1 text-[#800000]">{{
                  countdown.minutes
                }}</span>
                <span class="text-white">:</span>
                <span class="rounded-lg bg-white px-2.5 py-1 text-[#800000]">{{
                  countdown.seconds
                }}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="px-5 py-6 md:px-8 md:py-8">
          <div v-if="loading" class="grid grid-cols-2 gap-4 md:grid-cols-3 xl:grid-cols-5">
            <div
              v-for="i in 10"
              :key="i"
              class="h-[340px] animate-pulse rounded-2xl bg-white/15"
            ></div>
          </div>

          <div
            v-else-if="error"
            class="rounded-2xl border border-white/10 bg-white/10 p-8 text-center text-sm text-white/90"
          >
            {{ error }}
            <button
              type="button"
              class="mt-4 rounded-xl bg-white px-4 py-2 font-bold text-[#800000]"
              @click="reload"
            >
              Tải lại
            </button>
          </div>

          <div
            v-else-if="!products.length"
            class="rounded-2xl border border-white/10 bg-white/10 p-10 text-center"
          >
            <div class="text-5xl">⚡</div>
            <h2 class="mt-4 text-2xl font-black">Chưa có sản phẩm trong đợt Flash Sale</h2>
            <p class="mt-2 text-sm text-red-100">
              Hãy quay lại sau hoặc xem toàn bộ sản phẩm của K-ZONE.
            </p>
            <RouterLink
              to="/san-pham"
              class="mt-5 inline-flex rounded-xl bg-white px-5 py-3 text-sm font-black text-[#800000]"
            >
              Xem sản phẩm
            </RouterLink>
          </div>

          <div v-else class="grid grid-cols-2 gap-4 md:grid-cols-3 xl:grid-cols-5">
            <article
              v-for="item in products"
              :key="item.id"
              class="group overflow-hidden rounded-2xl bg-white text-zinc-900 shadow-lg transition duration-300 hover:-translate-y-1 hover:shadow-2xl"
            >
              <button
                type="button"
                class="block w-full text-left"
                @click="goToDetail(item.productId || item.id)"
              >
                <div class="relative aspect-square overflow-hidden bg-zinc-50 p-4">
                  <div
                    class="absolute left-3 top-3 z-10 rounded-md bg-red-600 px-2 py-1 text-[10px] font-black text-white"
                  >
                    FLASH SALE
                  </div>
                  <div
                    v-if="item.discountPercent"
                    class="absolute right-3 top-3 z-10 rounded-md bg-orange-500 px-2 py-1 text-[10px] font-black text-white"
                  >
                    -{{ item.discountPercent }}%
                  </div>
                  <img
                    v-if="item.image"
                    :src="item.image"
                    :alt="item.tenSanPham"
                    class="h-full w-full object-contain transition duration-500 group-hover:scale-105"
                    @error="onImageError"
                  />
                  <div v-else class="flex h-full items-center justify-center text-sm text-zinc-400">
                    Chưa có ảnh
                  </div>
                </div>
                <div class="p-4">
                  <h3 class="line-clamp-2 min-h-[42px] text-sm font-bold leading-6">
                    {{ item.tenSanPham }}
                  </h3>
                  <div class="mt-3 flex flex-wrap items-end gap-2">
                    <span class="text-xl font-black text-red-600">{{ money(item.minPrice) }}</span>
                    <span v-if="item.originalPrice" class="text-xs text-zinc-400 line-through">{{
                      money(item.originalPrice)
                    }}</span>
                  </div>
                  <div class="mt-3 text-[11px] font-semibold text-amber-600">
                    🔥 {{ item.statusText }}
                  </div>
                  <div class="mt-1 h-2 overflow-hidden rounded-full bg-red-100">
                    <div
                      class="h-full rounded-full bg-gradient-to-r from-orange-400 to-red-600"
                      :style="{ width: item.soldPercent + '%' }"
                    ></div>
                  </div>
                </div>
              </button>
            </article>
          </div>
        </div>
      </section>

      <section class="mt-6 grid gap-4 md:grid-cols-3">
        <div class="rounded-2xl bg-white p-5 shadow-sm ring-1 ring-zinc-100">
          <p class="text-xs font-black uppercase tracking-widest text-red-600">Ưu đãi thật</p>
          <h2 class="mt-2 text-lg font-black">Giá lấy trực tiếp từ đợt giảm giá</h2>
          <p class="mt-2 text-sm leading-6 text-zinc-500">
            Trang Flash Sale và khối Flash Sale ở trang Sản phẩm dùng chung dữ liệu từ API đợt giảm
            giá.
          </p>
        </div>
        <div class="rounded-2xl bg-white p-5 shadow-sm ring-1 ring-zinc-100">
          <p class="text-xs font-black uppercase tracking-widest text-red-600">Cập nhật</p>
          <h2 class="mt-2 text-lg font-black">Tự làm mới dữ liệu</h2>
          <p class="mt-2 text-sm leading-6 text-zinc-500">
            Giao diện tự kiểm tra lại đợt Flash Sale mỗi phút để cập nhật trạng thái.
          </p>
        </div>
        <RouterLink
          to="/san-pham"
          class="rounded-2xl bg-[#f4eee0] p-5 shadow-sm ring-1 ring-amber-100 transition hover:-translate-y-0.5"
        >
          <p class="text-xs font-black uppercase tracking-widest text-red-600">Khám phá</p>
          <h2 class="mt-2 text-lg font-black text-zinc-900">Xem toàn bộ sản phẩm →</h2>
          <p class="mt-2 text-sm leading-6 text-zinc-500">
            Quay lại trang sản phẩm để tiếp tục lọc, tìm kiếm và mua sắm.
          </p>
        </RouterLink>
      </section>
    </main>
  </div>
</template>

<script setup>
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { Zap } from 'lucide-vue-next'
import PublicSiteHeader from '@/components/public/PublicSiteHeader.vue'
import { useFlashSale } from '@/composables/useFlashSale'

const router = useRouter()
const { products, currentCampaign, loading, error, endTime, isUpcoming, reload } = useFlashSale()

const countdown = ref({ days: '00', hours: '00', minutes: '00', seconds: '00' })
let interval = null

const countdownParts = computed(() => [
  { label: 'days', value: countdown.value.days },
  { label: 'hours', value: countdown.value.hours },
])

const tick = () => {
  if (!endTime.value) {
    countdown.value = { days: '00', hours: '00', minutes: '00', seconds: '00' }
    return
  }
  const diff = new Date(endTime.value).getTime() - Date.now()
  if (diff <= 0) {
    countdown.value = { days: '00', hours: '00', minutes: '00', seconds: '00' }
    return
  }
  const days = Math.floor(diff / 86400000)
  const hours = Math.floor((diff % 86400000) / 3600000)
  const minutes = Math.floor((diff % 3600000) / 60000)
  const seconds = Math.floor((diff % 60000) / 1000)
  countdown.value = {
    days: String(days).padStart(2, '0'),
    hours: String(hours).padStart(2, '0'),
    minutes: String(minutes).padStart(2, '0'),
    seconds: String(seconds).padStart(2, '0'),
  }
}

const money = (value) => `${Number(value || 0).toLocaleString('vi-VN')}đ`
const onImageError = (event) => {
  event.target.src = 'https://via.placeholder.com/500x500?text=K-ZONE'
}
const goToDetail = (id) => router.push(`/spct/${id}`)

onMounted(() => {
  tick()
  interval = window.setInterval(tick, 1000)
})

onBeforeUnmount(() => {
  if (interval) window.clearInterval(interval)
})
</script>
