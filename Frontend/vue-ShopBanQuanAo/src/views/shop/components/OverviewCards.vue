<template>
  <section class="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-4 gap-6">
    <div
      v-for="card in cards"
      :key="card.title"
      class="group relative overflow-hidden rounded-[28px] border border-slate-200 bg-white p-6 shadow-sm transition-all duration-300 hover:-translate-y-1 hover:shadow-2xl"
    >
      <!-- Background -->
      <div
        class="absolute -right-10 -top-10 h-32 w-32 rounded-full opacity-10 transition duration-500 group-hover:scale-150"
        :class="card.bg"
      ></div>

      <div class="relative flex items-start justify-between">
        <div>
          <p class="text-xs uppercase tracking-[0.25em] text-slate-400 font-bold">
            {{ card.title }}
          </p>

          <h2 class="mt-4 text-3xl font-black text-slate-800">
            {{ card.value }}
          </h2>

          <div class="mt-3 flex items-center gap-2">
            <span
              class="rounded-full px-2 py-1 text-xs font-bold"
              :class="card.positive ? 'bg-emerald-100 text-emerald-700' : 'bg-red-100 text-red-600'"
            >
              {{ card.change }}
            </span>

            <span class="text-sm text-slate-500"> so với tháng trước </span>
          </div>
        </div>

        <div
          class="flex h-16 w-16 items-center justify-center rounded-2xl text-3xl shadow-lg"
          :class="card.iconBg"
        >
          {{ card.icon }}
        </div>
      </div>

      <!-- Progress -->

      <div class="mt-8">
        <div class="flex justify-between text-xs text-slate-500 mb-2">
          <span>Mức hoàn thành</span>

          <span>{{ card.progress }}%</span>
        </div>

        <div class="h-2 overflow-hidden rounded-full bg-slate-100">
          <div
            class="h-full rounded-full transition-all duration-700"
            :class="card.bar"
            :style="{ width: card.progress + '%' }"
          ></div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
const cards = [
  {
    title: 'Doanh thu',

    value: '₫235.8M',

    change: '+18.4%',

    positive: true,

    progress: 82,

    icon: '💰',

    bg: 'bg-emerald-500',

    bar: 'bg-emerald-500',

    iconBg: 'bg-emerald-100',
  },

  {
    title: 'Đơn hàng',

    value: '1,285',

    change: '+12.1%',

    positive: true,

    progress: 75,

    icon: '📦',

    bg: 'bg-blue-500',

    bar: 'bg-blue-500',

    iconBg: 'bg-blue-100',
  },

  {
    title: 'Khách hàng',

    value: '642',

    change: '+8.7%',

    positive: true,

    progress: 68,

    icon: '👥',

    bg: 'bg-violet-500',

    bar: 'bg-violet-500',

    iconBg: 'bg-violet-100',
  },

  {
    title: 'Sản phẩm bán',

    value: '3,462',

    change: '+21.8%',

    positive: true,

    progress: 91,

    icon: '👕',

    bg: 'bg-orange-500',

    bar: 'bg-orange-500',

    iconBg: 'bg-orange-100',
  },

  {
    title: 'Đơn hoàn thành',

    value: '1,140',

    change: '+15.2%',

    positive: true,

    progress: 88,

    icon: '✅',

    bg: 'bg-teal-500',

    bar: 'bg-teal-500',

    iconBg: 'bg-teal-100',
  },

  {
    title: 'Đơn hủy',

    value: '17',

    change: '-2.3%',

    positive: false,

    progress: 20,

    icon: '❌',

    bg: 'bg-red-500',

    bar: 'bg-red-500',

    iconBg: 'bg-red-100',
  },

  {
    title: 'Giá trị TB',

    value: '₫862K',

    change: '+5.6%',

    positive: true,

    progress: 61,

    icon: '🧾',

    bg: 'bg-indigo-500',

    bar: 'bg-indigo-500',

    iconBg: 'bg-indigo-100',
  },

  {
    title: 'Khách mới',

    value: '58',

    change: '+10.4%',

    positive: true,

    progress: 70,

    icon: '⭐',

    bg: 'bg-pink-500',

    bar: 'bg-pink-500',

    iconBg: 'bg-pink-100',
  },
]
</script>
