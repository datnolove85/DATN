<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { RouterLink } from 'vue-router'
import {
  Search,
  ShoppingBag,
  User,
  Menu,
  X,
  Star,
  Truck,
  Shield,
  CreditCard,
  Headphones,
  ArrowRight,
  Heart,
  Plus,
  Facebook,
  Instagram,
  Twitter,
  Youtube,
  Mail,
  MapPin,
  Phone,
} from 'lucide-vue-next'

const mobileOpen = ref(false)
const cartCount = ref(3)
const email = ref('')
const scrolled = ref(false)

const onScroll = () => {
  scrolled.value = window.scrollY > 20
}
onMounted(() => {
  window.addEventListener('scroll', onScroll)
  const els = document.querySelectorAll('.reveal')
  const io = new IntersectionObserver(
    (entries) => {
      entries.forEach((e) => {
        if (e.isIntersecting) e.target.classList.add('is-visible')
      })
    },
    { threshold: 0.12 },
  )
  els.forEach((el) => io.observe(el))
})
onUnmounted(() => window.removeEventListener('scroll', onScroll))
const nav = [
  { name: 'Home', path: '/home' },
  { name: 'Shop', path: '/shop' },
  { name: 'New Arrivals', path: '/new-arrivals' },
  { name: 'Collections', path: '/collections' },
  { name: 'About', path: '/about' },
]
const categories = [
  {
    name: 'T-Shirts',
    img: 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=800&q=80',
  },
  {
    name: 'Jackets',
    img: 'https://images.unsplash.com/photo-1551028719-00167b16eac5?w=800&q=80',
  },
  {
    name: 'Jeans',
    img: 'https://images.unsplash.com/photo-1542272604-787c3835535d?w=800&q=80',
  },
  {
    name: 'Sneakers',
    img: 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=800&q=80',
  },
]

const products = [
  {
    name: 'Oversized Cotton Tee',
    category: 'T-Shirts',
    price: 39,
    rating: 4.8,
    img: 'https://images.unsplash.com/photo-1576566588028-4147f3842f27?w=700&q=80',
  },
  {
    name: 'Wool Bomber Jacket',
    category: 'Jackets',
    price: 189,
    rating: 4.9,
    img: 'https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=700&q=80',
  },
  {
    name: 'Slim Tapered Jeans',
    category: 'Jeans',
    price: 89,
    rating: 4.7,
    img: 'https://images.unsplash.com/photo-1602293589930-45aad59ba3ab?w=700&q=80',
  },
  {
    name: 'Court Low Sneakers',
    category: 'Sneakers',
    price: 129,
    rating: 4.8,
    img: 'https://images.unsplash.com/photo-1600185365483-26d7a4cc7519?w=700&q=80',
  },
  {
    name: 'Linen Shirt',
    category: 'Shirts',
    price: 69,
    rating: 4.6,
    img: 'https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=700&q=80',
  },
  {
    name: 'Leather Biker Jacket',
    category: 'Jackets',
    price: 299,
    rating: 4.9,
    img: 'https://images.unsplash.com/photo-1520975954732-35dd22299614?w=700&q=80',
  },
  {
    name: 'Knit Crew Sweater',
    category: 'Knits',
    price: 99,
    rating: 4.7,
    img: 'https://images.unsplash.com/photo-1620799140408-edc6dcb6d633?w=700&q=80',
  },
  {
    name: 'Runner Mesh Sneakers',
    category: 'Sneakers',
    price: 149,
    rating: 4.8,
    img: 'https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=700&q=80',
  },
]

const features = [
  {
    icon: Truck,
    title: 'Fast Shipping',
    desc: 'Free worldwide delivery on orders over $99.',
  },
  {
    icon: Shield,
    title: 'Premium Quality',
    desc: 'Crafted from carefully sourced materials.',
  },
  {
    icon: CreditCard,
    title: 'Secure Payment',
    desc: 'Encrypted checkout with trusted providers.',
  },
  {
    icon: Headphones,
    title: '24/7 Support',
    desc: 'A real human, any time you need help.',
  },
]

const testimonials = [
  {
    name: 'James Carter',
    role: 'Verified Buyer',
    text: 'Velora has completely upgraded my wardrobe. The fit, the fabric, the finish — all flawless.',
    img: 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200&q=80',
  },
  {
    name: 'Daniel Cole',
    role: 'Verified Buyer',
    text: 'Shipping was lightning fast and every piece feels truly premium. Easily my favorite store.',
    img: 'https://images.unsplash.com/photo-1531427186611-ecfd6d936c79?w=200&q=80',
  },
  {
    name: 'Marcus Lee',
    role: 'Verified Buyer',
    text: 'Minimal, modern, confident. Velora nails the vibe I’ve been chasing for years.',
    img: 'https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=200&q=80',
  },
]

const addToCart = () => {
  cartCount.value++
}
const subscribe = () => {
  if (email.value) {
    email.value = ''
    alert('Subscribed!')
  }
}
</script>

<template>
  <div
    class="min-h-screen bg-white text-neutral-900 font-sans antialiased selection:bg-amber-200 selection:text-neutral-900"
  >
    <!-- NAVBAR -->
    <header
      :class="[
        'fixed top-0 inset-x-0 z-50 transition-all duration-300',
        scrolled
          ? 'bg-white/80 backdrop-blur-md shadow-sm border-b border-neutral-200/60'
          : 'bg-transparent',
      ]"
    >
      <div class="max-w-7xl mx-auto px-5 lg:px-8 h-16 lg:h-20 flex items-center justify-between">
        <a href="#" class="text-2xl font-extrabold tracking-tight">
          Vel<span class="text-amber-500">o</span>ra
        </a>
        <nav class="hidden lg:flex items-center gap-9">
          <!-- DESKTOP MENU -->
          <RouterLink
            v-for="item in nav"
            :key="item.name"
            :to="item.path"
            class="relative text-sm font-medium text-neutral-700 hover:text-neutral-900 transition after:content-[''] after:absolute after:-bottom-1 after:left-0 after:h-0.5 after:w-0 after:bg-amber-500 after:transition-all hover:after:w-full"
          >
            {{ item.name }}
          </RouterLink>
        </nav>
        <div class="flex items-center gap-2 sm:gap-3">
          <button class="p-2 rounded-full hover:bg-neutral-100 transition">
            <Search class="w-5 h-5" />
          </button>
          <button class="p-2 rounded-full hover:bg-neutral-100 transition relative">
            <ShoppingBag class="w-5 h-5" />
            <span
              class="absolute -top-0.5 -right-0.5 bg-amber-500 text-white text-[10px] font-bold rounded-full w-4 h-4 flex items-center justify-center"
              >{{ cartCount }}</span
            >
          </button>
          <button class="p-2 rounded-full hover:bg-neutral-100 transition hidden sm:inline-flex">
            <User class="w-5 h-5" />
          </button>
          <button
            @click="mobileOpen = !mobileOpen"
            class="lg:hidden p-2 rounded-full hover:bg-neutral-100 transition"
          >
            <component :is="mobileOpen ? X : Menu" class="w-5 h-5" />
          </button>
        </div>
      </div>
      <transition name="fade">
        <div v-if="mobileOpen" class="lg:hidden bg-white border-t border-neutral-200">
          <div class="px-6 py-4 flex flex-col gap-3">
            <!-- MOBILE MENU -->
            <RouterLink
              v-for="item in nav"
              :key="item.name"
              :to="item.path"
              @click="mobileOpen = false"
              class="py-2 text-neutral-700 font-medium hover:text-amber-500 transition"
            >
              {{ item.name }}
            </RouterLink>
          </div>
        </div>
      </transition>
    </header>

    <!-- HERO -->
    <section
      class="relative pt-28 lg:pt-32 overflow-hidden bg-gradient-to-br from-neutral-50 via-white to-amber-50/40"
    >
      <div
        class="absolute -top-24 -right-24 w-[500px] h-[500px] bg-amber-200/30 rounded-full blur-3xl"
      ></div>
      <div
        class="absolute -bottom-32 -left-24 w-[400px] h-[400px] bg-neutral-200/40 rounded-full blur-3xl"
      ></div>

      <div
        class="relative max-w-7xl mx-auto px-5 lg:px-8 grid lg:grid-cols-2 gap-16 xl:gap-24 items-center py-16 lg:py-24"
      >
        <div class="reveal">
          <span
            class="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-white border border-neutral-200 text-xs font-medium text-neutral-700 shadow-sm"
          >
            <span class="w-1.5 h-1.5 rounded-full bg-amber-500"></span> New Season · Fall 2026
          </span>
          <h1
            class="mt-6 text-5xl sm:text-6xl lg:text-7xl font-extrabold leading-[1.05] tracking-tight"
          >
            Elevate Your
            <span class="italic font-serif text-amber-500">Style</span>
          </h1>
          <p class="mt-6 text-lg text-neutral-600 max-w-md">
            Modern fashion for confident men. Timeless silhouettes, premium fabrics, made to move
            with you.
          </p>
          <div class="mt-8 flex flex-wrap gap-3">
            <button
              class="group inline-flex items-center gap-2 px-6 py-3.5 rounded-full bg-neutral-900 text-white font-medium shadow-lg shadow-neutral-900/20 hover:bg-neutral-800 hover:shadow-xl transition-all"
            >
              Shop Now
              <ArrowRight class="w-4 h-4 group-hover:translate-x-1 transition-transform" />
            </button>
            <button
              class="inline-flex items-center gap-2 px-6 py-3.5 rounded-full bg-white border border-neutral-200 font-medium hover:border-neutral-900 transition"
            >
              Explore Collection
            </button>
          </div>
          <div class="mt-10 flex items-center gap-8">
            <div>
              <div class="text-2xl font-bold">500+</div>
              <div class="text-xs text-neutral-500">Products</div>
            </div>
            <div class="h-8 w-px bg-neutral-200"></div>
            <div>
              <div class="text-2xl font-bold">50k+</div>
              <div class="text-xs text-neutral-500">Customers</div>
            </div>
            <div class="h-8 w-px bg-neutral-200"></div>
            <div>
              <div class="text-2xl font-bold flex items-center gap-1">
                4.9<Star class="w-4 h-4 fill-amber-500 text-amber-500" />
              </div>
              <div class="text-xs text-neutral-500">Rating</div>
            </div>
          </div>
        </div>

        <div class="reveal relative">
          <div
            class="absolute inset-0 -m-6 bg-gradient-to-tr from-amber-200/40 to-transparent rounded-[2.5rem] blur-2xl"
          ></div>
          <div
            class="relative aspect-[4/5] rounded-[2rem] overflow-hidden shadow-2xl shadow-neutral-900/20"
          >
            <img
              src="https://images.unsplash.com/photo-1617137968427-85924c800a22?w=900&q=80"
              alt="Velora Model"
              class="w-full h-full object-cover hover:scale-105 transition-transform duration-[1.2s]"
            />
          </div>
          <div
            class="absolute -bottom-6 -left-6 bg-white rounded-2xl shadow-xl p-4 flex items-center gap-3 border border-neutral-100"
          >
            <div class="w-10 h-10 rounded-full bg-amber-100 flex items-center justify-center">
              <Heart class="w-5 h-5 text-amber-500" />
            </div>
            <div>
              <div class="text-xs text-neutral-500">Trending now</div>
              <div class="text-sm font-semibold">Wool Bomber</div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- CATEGORIES -->
    <section class="py-20 lg:py-28">
      <div class="max-w-7xl mx-auto px-5 lg:px-8">
        <div class="flex items-end justify-between mb-10 reveal">
          <div>
            <p class="text-amber-500 font-medium text-sm tracking-widest uppercase">Shop by</p>
            <h2 class="mt-2 text-3xl lg:text-4xl font-bold tracking-tight">Categories</h2>
          </div>
          <a
            href="#"
            class="hidden sm:inline-flex items-center gap-2 text-sm font-medium hover:text-amber-500 transition"
            >View all <ArrowRight class="w-4 h-4"
          /></a>
        </div>
        <div class="grid grid-cols-2 lg:grid-cols-4 gap-5">
          <a
            v-for="(c, i) in categories"
            :key="c.name"
            href="#"
            class="reveal group relative aspect-[4/5] rounded-3xl overflow-hidden shadow-md hover:shadow-2xl transition-all"
            :style="{ transitionDelay: i * 60 + 'ms' }"
          >
            <img
              :src="c.img"
              :alt="c.name"
              class="absolute inset-0 w-full h-full object-cover group-hover:scale-110 transition-transform duration-700"
            />
            <div
              class="absolute inset-0 bg-gradient-to-t from-neutral-900/70 via-neutral-900/10 to-transparent"
            ></div>
            <div class="absolute bottom-5 left-5 right-5 flex items-end justify-between">
              <h3 class="text-white text-xl font-bold">{{ c.name }}</h3>
              <span
                class="w-10 h-10 rounded-full bg-white text-neutral-900 flex items-center justify-center translate-y-2 opacity-0 group-hover:translate-y-0 group-hover:opacity-100 transition-all"
              >
                <ArrowRight class="w-4 h-4" />
              </span>
            </div>
          </a>
        </div>
      </div>
    </section>

    <!-- FEATURED PRODUCTS -->
    <section class="py-20 lg:py-28 bg-neutral-50">
      <div class="max-w-7xl mx-auto px-5 lg:px-8">
        <div class="flex items-end justify-between mb-12 reveal">
          <div>
            <p class="text-amber-500 font-medium text-sm tracking-widest uppercase">Curated</p>
            <h2 class="mt-2 text-3xl lg:text-4xl font-bold tracking-tight">Featured Products</h2>
          </div>
        </div>
        <div class="grid grid-cols-2 lg:grid-cols-4 gap-5 lg:gap-7">
          <div
            v-for="(p, i) in products"
            :key="p.name"
            class="reveal group bg-white rounded-3xl overflow-hidden shadow-sm hover:shadow-2xl transition-all duration-500"
            :style="{ transitionDelay: i * 50 + 'ms' }"
          >
            <div class="relative aspect-square overflow-hidden bg-neutral-100">
              <img
                :src="p.img"
                :alt="p.name"
                class="w-full h-full object-cover group-hover:scale-110 transition-transform duration-700"
              />
              <button
                class="absolute top-3 right-3 w-9 h-9 rounded-full bg-white/90 backdrop-blur flex items-center justify-center shadow hover:bg-white hover:scale-110 transition"
              >
                <Heart class="w-4 h-4" />
              </button>
              <button
                @click="addToCart"
                class="absolute bottom-3 inset-x-3 py-2.5 rounded-full bg-neutral-900 text-white text-sm font-medium flex items-center justify-center gap-2 translate-y-12 opacity-0 group-hover:translate-y-0 group-hover:opacity-100 transition-all hover:bg-amber-500"
              >
                <Plus class="w-4 h-4" /> Add to Cart
              </button>
            </div>
            <div class="p-4">
              <div class="text-xs text-neutral-500">{{ p.category }}</div>
              <h3 class="mt-1 font-semibold text-neutral-900 truncate">
                {{ p.name }}
              </h3>
              <div class="mt-2 flex items-center justify-between">
                <div class="font-bold">${{ p.price }}</div>
                <div class="flex items-center gap-1 text-xs text-neutral-600">
                  <Star class="w-3.5 h-3.5 fill-amber-500 text-amber-500" />
                  {{ p.rating }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- PROMO BANNER -->
    <section class="py-20 lg:py-28">
      <div class="max-w-7xl mx-auto px-5 lg:px-8">
        <div class="reveal relative overflow-hidden rounded-[2.5rem] bg-neutral-900 text-white">
          <img
            src="https://images.unsplash.com/photo-1490481651871-ab68de25d43d?w=1600&q=80"
            alt="Summer Collection"
            class="absolute inset-0 w-full h-full object-cover opacity-40"
          />
          <div
            class="absolute inset-0 bg-gradient-to-r from-neutral-900 via-neutral-900/70 to-transparent"
          ></div>
          <div class="relative px-8 sm:px-14 py-20 lg:py-28 max-w-2xl">
            <p class="text-amber-400 tracking-widest text-sm font-medium uppercase">Limited Time</p>
            <h2 class="mt-3 text-4xl sm:text-5xl lg:text-6xl font-extrabold leading-tight">
              Summer Collection <span class="text-amber-400">2026</span>
            </h2>
            <p class="mt-4 text-xl text-neutral-200">
              Up to <span class="text-amber-400 font-bold">40% Off</span> on selected pieces.
            </p>
            <button
              class="mt-8 inline-flex items-center gap-2 px-7 py-4 rounded-full bg-amber-500 text-neutral-900 font-semibold shadow-xl shadow-amber-500/30 hover:bg-amber-400 hover:scale-105 transition-all"
            >
              Shop the Sale <ArrowRight class="w-4 h-4" />
            </button>
          </div>
        </div>
      </div>
    </section>

    <!-- WHY CHOOSE US -->
    <section class="py-20 lg:py-28 bg-neutral-50">
      <div class="max-w-7xl mx-auto px-5 lg:px-8">
        <div class="text-center mb-14 reveal">
          <p class="text-amber-500 font-medium text-sm tracking-widest uppercase">Our Promise</p>
          <h2 class="mt-2 text-3xl lg:text-4xl font-bold tracking-tight">Why Choose Velora</h2>
        </div>
        <div class="grid sm:grid-cols-2 lg:grid-cols-4 gap-5">
          <div
            v-for="(f, i) in features"
            :key="f.title"
            class="reveal group p-7 bg-white rounded-3xl border border-neutral-100 hover:border-amber-200 hover:-translate-y-1 transition-all shadow-sm hover:shadow-xl"
            :style="{ transitionDelay: i * 60 + 'ms' }"
          >
            <div
              class="w-12 h-12 rounded-2xl bg-amber-50 text-amber-500 flex items-center justify-center group-hover:bg-amber-500 group-hover:text-white transition-colors"
            >
              <component :is="f.icon" class="w-6 h-6" />
            </div>
            <h3 class="mt-5 font-semibold text-lg">{{ f.title }}</h3>
            <p class="mt-2 text-sm text-neutral-600 leading-relaxed">
              {{ f.desc }}
            </p>
          </div>
        </div>
      </div>
    </section>

    <!-- TESTIMONIALS -->
    <section class="py-20 lg:py-28">
      <div class="max-w-7xl mx-auto px-5 lg:px-8">
        <div class="text-center mb-14 reveal">
          <p class="text-amber-500 font-medium text-sm tracking-widest uppercase">Loved by</p>
          <h2 class="mt-2 text-3xl lg:text-4xl font-bold tracking-tight">Our Customers</h2>
        </div>
        <div class="grid md:grid-cols-3 gap-6">
          <div
            v-for="(t, i) in testimonials"
            :key="t.name"
            class="reveal p-7 rounded-3xl bg-white border border-neutral-100 shadow-sm hover:shadow-xl hover:-translate-y-1 transition-all"
            :style="{ transitionDelay: i * 80 + 'ms' }"
          >
            <div class="flex gap-1 text-amber-500">
              <Star v-for="n in 5" :key="n" class="w-4 h-4 fill-amber-500" />
            </div>
            <p class="mt-4 text-neutral-700 leading-relaxed">"{{ t.text }}"</p>
            <div class="mt-6 flex items-center gap-3">
              <img :src="t.img" :alt="t.name" class="w-11 h-11 rounded-full object-cover" />
              <div>
                <div class="font-semibold text-sm">{{ t.name }}</div>
                <div class="text-xs text-neutral-500">{{ t.role }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- NEWSLETTER -->
    <section class="py-16 lg:py-20">
      <div class="max-w-5xl mx-auto px-5 lg:px-8">
        <div
          class="reveal relative overflow-hidden rounded-[2.5rem] bg-gradient-to-br from-neutral-900 to-neutral-800 text-white px-8 sm:px-14 py-14 lg:py-20 text-center"
        >
          <div
            class="absolute -top-20 -right-20 w-72 h-72 bg-amber-500/20 rounded-full blur-3xl"
          ></div>
          <h2 class="relative text-3xl lg:text-4xl font-bold tracking-tight">
            Join the Velora Club
          </h2>
          <p class="relative mt-3 text-neutral-300 max-w-md mx-auto">
            Early access to drops, members-only offers, and styling inspiration.
          </p>
          <form
            @submit.prevent="subscribe"
            class="relative mt-8 flex flex-col sm:flex-row gap-3 max-w-md mx-auto"
          >
            <input
              v-model="email"
              type="email"
              required
              placeholder="Enter your email"
              class="flex-1 px-5 py-3.5 rounded-full bg-white/10 border border-white/20 placeholder:text-neutral-400 focus:outline-none focus:border-amber-500 focus:bg-white/15 transition"
            />
            <button
              class="px-6 py-3.5 rounded-full bg-amber-500 text-neutral-900 font-semibold hover:bg-amber-400 hover:scale-105 transition-all"
            >
              Subscribe
            </button>
          </form>
        </div>
      </div>
    </section>

    <!-- FOOTER -->
    <footer class="bg-neutral-950 text-neutral-400 pt-20 pb-8">
      <div class="max-w-7xl mx-auto px-5 lg:px-8 grid md:grid-cols-2 lg:grid-cols-4 gap-10">
        <div>
          <!-- LOGO -->
          <RouterLink to="/home" class="text-2xl font-extrabold tracking-tight">
            Vel<span class="text-amber-500">o</span>ra
          </RouterLink>
          <p class="mt-4 text-sm leading-relaxed">
            Premium men's fashion designed for the modern, confident gentleman.
          </p>
          <div class="mt-6 flex gap-3">
            <a
              v-for="I in [Instagram, Facebook, Twitter, Youtube]"
              :key="I.name"
              href="#"
              class="w-9 h-9 rounded-full bg-white/5 hover:bg-amber-500 hover:text-neutral-900 flex items-center justify-center transition"
            >
              <component :is="I" class="w-4 h-4" />
            </a>
          </div>
        </div>
        <div>
          <h4 class="text-white font-semibold mb-4">Shop</h4>
          <ul class="space-y-2 text-sm">
            <li
              v-for="l in ['New Arrivals', 'Best Sellers', 'T-Shirts', 'Jackets', 'Sneakers']"
              :key="l"
            >
              <a href="#" class="hover:text-amber-500 transition">{{ l }}</a>
            </li>
          </ul>
        </div>
        <div>
          <h4 class="text-white font-semibold mb-4">Help</h4>
          <ul class="space-y-2 text-sm">
            <li v-for="l in ['Contact Us', 'Shipping', 'Returns', 'Size Guide', 'FAQ']" :key="l">
              <a href="#" class="hover:text-amber-500 transition">{{ l }}</a>
            </li>
          </ul>
        </div>
        <div>
          <h4 class="text-white font-semibold mb-4">Contact</h4>
          <ul class="space-y-3 text-sm">
            <li class="flex items-center gap-2">
              <MapPin class="w-4 h-4 text-amber-500" /> Hà Nội,Việt Nam
            </li>
            <li class="flex items-center gap-2">
              <Phone class="w-4 h-4 text-amber-500" /> +84 0377280805
            </li>
            <li class="flex items-center gap-2">
              <Mail class="w-4 h-4 text-amber-500" /> hello@velora.com
            </li>
          </ul>
        </div>
      </div>
      <div
        class="max-w-7xl mx-auto px-5 lg:px-8 mt-14 pt-6 border-t border-white/5 flex flex-col sm:flex-row items-center justify-between gap-3 text-xs"
      >
        <div>© 2026 Velora. All rights reserved.</div>
        <div class="flex gap-5">
          <a href="#" class="hover:text-amber-500">Privacy</a
          ><a href="#" class="hover:text-amber-500">Terms</a
          ><a href="#" class="hover:text-amber-500">Cookies</a>
        </div>
      </div>
    </footer>
  </div>
</template>
<style scoped>
/* ===== GLOBAL FIX ===== */
* {
  box-sizing: border-box;
}

html,
body,
#app {
  width: 100%;
  overflow-x: hidden;
}

/* ===== CONTAINER AUTO BALANCE ===== */
section,
header,
footer {
  width: 100%;
}

.max-w-7xl {
  width: 100%;
  max-width: 1400px !important;
  margin-left: auto;
  margin-right: auto;
}

/* ===== HERO FIX ===== */
.grid {
  align-items: center;
}

/* ===== IMAGE BALANCE ===== */
img {
  display: block;
  max-width: 100%;
}

/* ===== CARD BALANCE ===== */
.reveal {
  opacity: 0;
  transform: translateY(24px);
  transition:
    opacity 0.8s ease,
    transform 0.8s ease;
}

.reveal.is-visible {
  opacity: 1;
  transform: translateY(0);
}

/* ===== MOBILE MENU ===== */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.25s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* ===== RESPONSIVE FIX ===== */
@media (max-width: 1280px) {
  .max-w-7xl {
    max-width: 1200px !important;
  }
}

@media (max-width: 1024px) {
  .max-w-7xl {
    max-width: 100% !important;
    padding-left: 24px !important;
    padding-right: 24px !important;
  }
}

@media (max-width: 640px) {
  .max-w-7xl {
    padding-left: 18px !important;
    padding-right: 18px !important;
  }

  h1 {
    line-height: 1.1 !important;
  }
}
</style>
