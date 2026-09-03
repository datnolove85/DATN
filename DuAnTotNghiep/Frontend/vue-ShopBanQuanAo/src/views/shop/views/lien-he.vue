<template>
  <div class="min-h-screen bg-[#f6f1e6] text-zinc-900">
    <PublicSiteHeader />
    <section class="bg-[#800000] text-white">
      <div class="mx-auto max-w-[1480px] px-6 py-14 lg:px-10">
        <p class="text-xs font-black uppercase tracking-[0.28em] text-amber-300">K-ZONE CONTACT</p>
        <h1 class="mt-2 text-4xl font-black uppercase md:text-6xl">Liên hệ với K-ZONE</h1>
        <p class="mt-4 max-w-2xl text-sm leading-7 text-red-100 md:text-base">
          Mọi thắc mắc về sản phẩm, đơn hàng, đổi trả và trải nghiệm mua sắm đều có thể gửi tới đội
          ngũ K-ZONE.
        </p>
      </div>
    </section>
    <main class="mx-auto max-w-[1480px] px-6 py-10 lg:px-10">
      <section class="grid gap-6 md:grid-cols-2 xl:grid-cols-4">
        <article
          v-for="item in contactCards"
          :key="item.title"
          class="rounded-3xl bg-white p-6 shadow-sm ring-1 ring-black/5"
        >
          <div class="grid h-12 w-12 place-items-center rounded-2xl bg-red-50 text-red-700">
            <component :is="item.icon" :size="24" />
          </div>
          <p class="mt-5 text-xs font-black uppercase tracking-wider text-zinc-400">
            {{ item.title }}
          </p>
          <p class="mt-2 text-lg font-black">{{ item.value }}</p>
          <p class="mt-2 text-sm leading-6 text-zinc-500">{{ item.note }}</p>
        </article>
      </section>
      <section class="mt-8 grid gap-7 lg:grid-cols-[1.15fr_.85fr]">
        <div class="rounded-3xl bg-white p-7 shadow-sm ring-1 ring-black/5 md:p-9">
          <p class="text-xs font-black uppercase tracking-[0.2em] text-red-600">Gửi yêu cầu</p>
          <h2 class="mt-2 text-3xl font-black">K-ZONE luôn sẵn sàng hỗ trợ</h2>
          <div class="mt-7 grid gap-4 sm:grid-cols-2">
            <input
              v-model="form.name"
              class="rounded-xl border border-zinc-200 px-4 py-3 outline-none focus:border-red-500"
              placeholder="Họ và tên"
            /><input
              v-model="form.email"
              class="rounded-xl border border-zinc-200 px-4 py-3 outline-none focus:border-red-500"
              placeholder="Email"
            />
          </div>
          <input
            v-model="form.subject"
            class="mt-4 w-full rounded-xl border border-zinc-200 px-4 py-3 outline-none focus:border-red-500"
            placeholder="Chủ đề liên hệ"
          /><textarea
            v-model="form.message"
            rows="6"
            class="mt-4 w-full rounded-xl border border-zinc-200 px-4 py-3 outline-none focus:border-red-500"
            placeholder="Nội dung bạn muốn gửi đến K-ZONE..."
          ></textarea
          ><button
            type="button"
            class="mt-5 rounded-xl bg-[#800000] px-6 py-3 text-sm font-black text-white hover:bg-red-700"
            @click="submit"
          >
            Gửi liên hệ
          </button>
          <p v-if="sent" class="mt-3 text-sm font-semibold text-emerald-700">
            Đã ghi nhận thông tin liên hệ. K-ZONE sẽ phản hồi sớm.
          </p>
        </div>
        <div class="overflow-hidden rounded-3xl bg-zinc-950 p-7 text-white md:p-9">
          <p class="text-xs font-black uppercase tracking-[0.2em] text-amber-300">
            Trụ sở & showroom
          </p>
          <h2 class="mt-3 text-3xl font-black">123 Nguyễn Trãi, Thanh Xuân, Hà Nội</h2>
          <p class="mt-4 text-sm leading-7 text-zinc-300">
            Địa chỉ minh hoạ sử dụng cho giao diện đồ án. Khu vực tiếp khách, tư vấn sản phẩm và
            trải nghiệm showroom được bố trí theo phong cách K-ZONE.
          </p>
          <div class="mt-7 space-y-4 text-sm">
            <div class="flex items-center gap-3">
              <Clock3 :size="19" class="text-amber-300" /> 08:30 — 22:00 mỗi ngày
            </div>
            <div class="flex items-center gap-3">
              <Phone :size="19" class="text-amber-300" /> 1900 6750
            </div>
            <div class="flex items-center gap-3">
              <Mail :size="19" class="text-amber-300" /> support@kzone.vn
            </div>
          </div>
          <button
            type="button"
            class="mt-7 rounded-xl border border-white/20 px-5 py-3 text-sm font-bold hover:bg-white/10"
            @click="router.push('/he-thong-cua-hang')"
          >
            Xem hệ thống cửa hàng
          </button>
        </div>
      </section>
    </main>
    <ShopFooter />
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Clock3, Mail, MapPin, MessageCircle, Phone } from 'lucide-vue-next'
import PublicSiteHeader from '@/components/public/PublicSiteHeader.vue'
import ShopFooter from './components/ShopFooter.vue'
const router = useRouter()
const sent = ref(false)
const form = ref({ name: '', email: '', subject: '', message: '' })
const contactCards = [
  { title: 'Hotline', value: '1900 6750', note: 'Hỗ trợ đơn hàng và sản phẩm.', icon: Phone },
  { title: 'Email', value: 'support@kzone.vn', note: 'Tiếp nhận yêu cầu và phản hồi.', icon: Mail },
  {
    title: 'Địa chỉ',
    value: '58 Nguyễn Khánh Toàn, Cầu Giấy, Hà Nội',
    note: 'Cầu Giấy, Hà Nội · thông tin minh hoạ.',
    icon: MapPin,
  },
  {
    title: 'Mạng xã hội',
    value: 'K-ZONE Official',
    note: 'Cập nhật bộ sưu tập và tin mới.',
    icon: MessageCircle,
  },
]
const submit = () => {
  sent.value = true
}
</script>
