<template>
  <div class="min-h-screen bg-[#f6f1e6] text-zinc-900">
    <PublicSiteHeader />
    <section class="bg-[#800000] text-white">
      <div class="mx-auto max-w-[1480px] px-6 py-16 lg:px-10">
        <div class="max-w-3xl">
          <p class="mb-3 text-xs font-black uppercase tracking-[0.28em] text-amber-300">
            K-ZONE NEWS
          </p>
          <h1 class="text-4xl font-black uppercase tracking-tight md:text-6xl">Tin tức mới</h1>
          <p class="mt-5 max-w-2xl text-sm leading-7 text-red-100 md:text-base">
            Cập nhật xu hướng, thiết kế mới, bộ sưu tập và những câu chuyện thời trang nổi bật của
            K-ZONE.
          </p>
        </div>
      </div>
    </section>

    <main class="mx-auto max-w-[1480px] px-6 py-10 lg:px-10">
      <section class="grid gap-6 lg:grid-cols-[1.3fr_.7fr]">
        <article class="group overflow-hidden rounded-3xl bg-white shadow-sm ring-1 ring-black/5">
          <div class="relative aspect-[16/9] overflow-hidden">
            <img
              :src="featured.image"
              :alt="featured.title"
              class="h-full w-full object-cover transition duration-700 group-hover:scale-105"
            />
            <div
              class="absolute inset-0 bg-gradient-to-t from-black/70 via-black/10 to-transparent"
            ></div>
            <div class="absolute bottom-0 left-0 right-0 p-6 text-white md:p-8">
              <span class="rounded-full bg-red-600 px-3 py-1 text-[11px] font-black uppercase"
                >Tin hot</span
              >
              <h2 class="mt-3 text-2xl font-black md:text-4xl">{{ featured.title }}</h2>
              <p class="mt-2 max-w-2xl text-sm text-white/85">{{ featured.excerpt }}</p>
            </div>
          </div>
        </article>

        <div class="grid gap-6 sm:grid-cols-2 lg:grid-cols-1">
          <article
            v-for="item in sideNews"
            :key="item.id"
            class="grid grid-cols-[110px_1fr] gap-4 overflow-hidden rounded-2xl bg-white p-3 shadow-sm ring-1 ring-black/5"
          >
            <img
              :src="item.image"
              :alt="item.title"
              class="h-full min-h-[110px] w-full rounded-xl object-cover"
            />
            <div class="flex flex-col justify-center">
              <span class="text-[10px] font-black uppercase tracking-wider text-red-600">{{
                item.category
              }}</span>
              <h3 class="mt-1 text-sm font-extrabold leading-5">{{ item.title }}</h3>
              <p class="mt-2 text-xs leading-5 text-zinc-500">{{ item.excerpt }}</p>
            </div>
          </article>
        </div>
      </section>

      <section class="mt-14">
        <div class="flex items-end justify-between gap-4">
          <div>
            <p class="text-xs font-black uppercase tracking-[0.2em] text-red-600">New Design Lab</p>
            <h2 class="mt-1 text-3xl font-black">Thiết kế quần áo mới</h2>
          </div>
          <span class="hidden text-xs text-zinc-500 md:block"
            >Cảm hứng mới · Form mới · Chất liệu mới</span
          >
        </div>

        <div class="mt-6 grid gap-6 sm:grid-cols-2 lg:grid-cols-4">
          <article
            v-for="item in designNews"
            :key="item.id"
            class="group overflow-hidden rounded-2xl bg-white shadow-sm ring-1 ring-black/5"
          >
            <div class="aspect-[4/5] overflow-hidden">
              <img
                :src="item.image"
                :alt="item.title"
                class="h-full w-full object-cover transition duration-700 group-hover:scale-105"
              />
            </div>
            <div class="p-5">
              <p class="text-[10px] font-black uppercase tracking-wider text-zinc-400">
                {{ item.date }}
              </p>
              <h3 class="mt-2 text-base font-black leading-6">{{ item.title }}</h3>
              <p class="mt-2 text-xs leading-5 text-zinc-500">{{ item.excerpt }}</p>
              <button
                class="mt-4 text-xs font-black uppercase text-red-600"
                @click="openArticle(item)"
              >
                Xem bài viết →
              </button>
            </div>
          </article>
        </div>
      </section>

      <section class="mt-14 overflow-hidden rounded-3xl bg-zinc-950 text-white">
        <div class="grid lg:grid-cols-[1fr_1.1fr]">
          <div class="p-8 md:p-10">
            <p class="text-xs font-black uppercase tracking-[0.22em] text-amber-300">
              Góc phong cách
            </p>
            <h2 class="mt-3 text-3xl font-black md:text-4xl">Mặc đẹp không cần quá nhiều.</h2>
            <p class="mt-4 text-sm leading-7 text-zinc-300">
              Mix basic cùng một điểm nhấn đúng lúc để tạo outfit gọn gàng nhưng vẫn nổi bật.
            </p>
          </div>
          <img
            src="https://images.unsplash.com/photo-1529139574466-a303027c1d8b?q=80&w=1200&auto=format&fit=crop"
            alt="Style"
            class="h-full min-h-[320px] w-full object-cover"
          />
        </div>
      </section>

      <section
        v-if="isAdmin"
        class="mt-14 rounded-3xl border border-dashed border-red-300 bg-white p-6 shadow-sm md:p-8"
      >
        <div class="flex flex-col gap-6 lg:flex-row lg:items-start lg:justify-between">
          <div>
            <p class="text-xs font-black uppercase tracking-[0.2em] text-red-600">
              Khu vực quản trị
            </p>
            <h2 class="mt-2 text-2xl font-black">Thêm bài viết / hình ảnh</h2>
            <p class="mt-2 text-sm text-zinc-500">Chỉ tài khoản ADMIN mới nhìn thấy khu vực này.</p>
          </div>
          <div class="w-full max-w-xl rounded-2xl bg-[#f8f4eb] p-5">
            <input
              v-model="newArticle.title"
              class="w-full rounded-xl border border-zinc-200 bg-white px-4 py-3 text-sm outline-none focus:border-red-500"
              placeholder="Tiêu đề bài viết"
            />
            <textarea
              v-model="newArticle.excerpt"
              rows="3"
              class="mt-3 w-full rounded-xl border border-zinc-200 bg-white px-4 py-3 text-sm outline-none focus:border-red-500"
              placeholder="Mô tả ngắn"
            ></textarea>
            <label
              class="mt-3 flex cursor-pointer items-center justify-between rounded-xl border border-dashed border-red-300 bg-white px-4 py-3 text-sm font-bold text-red-700"
            >
              <span>{{ newArticle.image ? 'Đã chọn ảnh' : 'Chọn ảnh bài viết' }}</span>
              <input type="file" accept="image/*" class="hidden" @change="handleAdminImage" />
              <span class="rounded-lg bg-red-50 px-3 py-2">Chọn ảnh</span>
            </label>
            <div v-if="newArticle.image" class="mt-3 overflow-hidden rounded-xl">
              <img
                :src="newArticle.image"
                alt="Ảnh bài viết mới"
                class="h-44 w-full object-cover"
              />
            </div>
            <button
              type="button"
              class="mt-4 w-full rounded-xl bg-[#800000] px-5 py-3 text-sm font-black text-white hover:bg-red-700"
              @click="addArticle"
            >
              Đăng bài viết
            </button>
          </div>
        </div>
      </section>

      <section
        v-else
        class="mt-14 rounded-3xl bg-white px-6 py-7 shadow-sm ring-1 ring-black/5 md:px-8"
      >
        <div class="flex items-center gap-3">
          <span class="grid h-10 w-10 place-items-center rounded-full bg-red-50 text-red-700"
            >K</span
          >
          <div>
            <p class="text-xs font-black uppercase tracking-wider text-zinc-400">K-ZONE NEWS</p>
            <p class="text-sm font-semibold text-zinc-700">
              Theo dõi tin mới và thiết kế mới nhất của K-ZONE.
            </p>
          </div>
        </div>
      </section>

      <section v-if="addedArticles.length" class="mt-10">
        <div class="mb-5">
          <p class="text-xs font-black uppercase tracking-[0.2em] text-red-600">
            Bài viết vừa đăng
          </p>
          <h2 class="mt-1 text-3xl font-black">Nội dung mới từ K-ZONE</h2>
        </div>
        <div class="grid gap-6 md:grid-cols-2 xl:grid-cols-3">
          <article
            v-for="item in addedArticles"
            :key="item.id"
            class="overflow-hidden rounded-2xl bg-white shadow-sm ring-1 ring-black/5"
          >
            <img :src="item.image" :alt="item.title" class="h-56 w-full object-cover" />
            <div class="p-5">
              <p class="text-[10px] font-black uppercase tracking-wider text-red-600">
                ADMIN · {{ item.date }}
              </p>
              <h3 class="mt-2 text-lg font-black">{{ item.title }}</h3>
              <p class="mt-2 text-sm leading-6 text-zinc-500">{{ item.excerpt }}</p>
            </div>
          </article>
        </div>
      </section>
    </main>

    <div
      v-if="selectedArticle"
      class="fixed inset-0 z-[100] grid place-items-center bg-black/65 p-4"
      @click.self="selectedArticle = null"
    >
      <div class="max-h-[90vh] w-full max-w-3xl overflow-y-auto rounded-3xl bg-white">
        <img
          :src="selectedArticle.image"
          :alt="selectedArticle.title"
          class="h-64 w-full object-cover md:h-80"
        />
        <div class="p-6 md:p-8">
          <p class="text-xs font-black uppercase tracking-wider text-red-600">
            {{ selectedArticle.category || 'K-ZONE NEWS' }}
          </p>
          <h2 class="mt-2 text-2xl font-black md:text-3xl">{{ selectedArticle.title }}</h2>
          <p class="mt-4 text-sm leading-7 text-zinc-600">{{ selectedArticle.excerpt }}</p>
          <button
            class="mt-6 rounded-xl bg-zinc-900 px-5 py-3 text-sm font-bold text-white"
            @click="selectedArticle = null"
          >
            Đóng
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import PublicSiteHeader from '@/components/public/PublicSiteHeader.vue'

const featured = {
  image:
    'https://images.unsplash.com/photo-1523398002811-999ca8dec234?q=80&w=1400&auto=format&fit=crop',
  title: 'K-ZONE ra mắt loạt thiết kế mới cho mùa mới',
  excerpt:
    'Những phom dáng basic được làm mới với đường cắt gọn hơn, màu sắc dễ phối và tinh thần streetwear hiện đại.',
}

const sideNews = [
  {
    id: 1,
    category: 'Trend',
    title: '5 cách phối áo thun cho outfit đi học và đi chơi',
    excerpt: 'Gọn, dễ mặc và không tốn quá nhiều thời gian.',
    image:
      'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?q=80&w=700&auto=format&fit=crop',
  },
  {
    id: 2,
    category: 'Hot',
    title: 'Màu trung tính đang trở lại mạnh mẽ',
    excerpt: 'Đen, kem, nâu và xám tiếp tục dẫn đầu phong cách dễ phối.',
    image:
      'https://images.unsplash.com/photo-1485230895905-ec40ba36b9bc?q=80&w=700&auto=format&fit=crop',
  },
  {
    id: 3,
    category: 'Guide',
    title: 'Chọn form áo theo dáng người',
    excerpt: 'Một vài nguyên tắc đơn giản để mặc vừa mắt hơn.',
    image:
      'https://images.unsplash.com/photo-1551488831-00ddcb6c6bd3?q=80&w=700&auto=format&fit=crop',
  },
]

const designNews = [
  {
    id: 11,
    date: '03.09.2026',
    title: 'Oversized Core — Form rộng mới',
    excerpt: 'Tập trung vào vai, tay áo và độ rơi tự nhiên.',
    image:
      'https://images.unsplash.com/photo-1551488831-00ddcb6c6bd3?q=80&w=900&auto=format&fit=crop',
  },
  {
    id: 12,
    date: '01.09.2026',
    title: 'Daily Shirt — Sơ mi tối giản',
    excerpt: 'Một chiếc sơ mi sạch, nhẹ và dễ layer.',
    image:
      'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?q=80&w=900&auto=format&fit=crop',
  },
  {
    id: 13,
    date: '28.08.2026',
    title: 'Urban Cargo — Quần túi hộp',
    excerpt: 'Form mạnh hơn nhưng vẫn ưu tiên chuyển động.',
    image:
      'https://images.unsplash.com/photo-1624378439575-d8705ad7ae80?q=80&w=900&auto=format&fit=crop',
  },
  {
    id: 14,
    date: '26.08.2026',
    title: 'Clean Layer — Layer nhẹ',
    excerpt: 'Phối lớp đơn giản cho những ngày thời tiết thất thường.',
    image:
      'https://images.unsplash.com/photo-1506629905607-d9c297d1e1d9?q=80&w=900&auto=format&fit=crop',
  },
]

const isAdmin = computed(() => {
  try {
    return JSON.parse(sessionStorage.getItem('user') || 'null')?.vaiTro === 'ADMIN'
  } catch {
    return false
  }
})
const addedArticles = ref([])
const newArticle = ref({ title: '', excerpt: '', image: '' })
const handleAdminImage = (event) => {
  const file = event.target.files?.[0]
  if (!file) return
  newArticle.value.image = URL.createObjectURL(file)
}
const addArticle = () => {
  if (!newArticle.value.title.trim() || !newArticle.value.image) return
  addedArticles.value.unshift({
    id: Date.now(),
    title: newArticle.value.title.trim(),
    excerpt: newArticle.value.excerpt.trim() || 'Bài viết mới từ K-ZONE.',
    image: newArticle.value.image,
    date: new Date().toLocaleDateString('vi-VN'),
  })
  newArticle.value = { title: '', excerpt: '', image: '' }
}
const selectedArticle = ref(null)
const previewImage = ref('')
const openArticle = (item) => (selectedArticle.value = { ...item, category: 'Thiết kế mới' })
const handleImage = (event) => {
  const file = event.target.files?.[0]
  if (!file) return
  previewImage.value = URL.createObjectURL(file)
}
</script>
