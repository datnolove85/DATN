<!-- ========================= -->
<!-- RevenueChart.vue (1/2) -->
<!-- ========================= -->

<template>
  <div class="rounded-[30px] border border-slate-200 bg-white shadow-sm overflow-hidden">
    <!-- Header -->
    <div
      class="px-8 py-7 border-b border-slate-100 flex flex-col xl:flex-row xl:items-center xl:justify-between gap-6"
    >
      <div>
        <div
          class="inline-flex items-center gap-2 rounded-full bg-indigo-50 text-indigo-600 px-4 py-2 font-semibold text-sm"
        >
          Revenue Analytics
        </div>

        <h2 class="mt-5 text-3xl font-black text-slate-800">Doanh thu bán hàng</h2>

        <p class="mt-2 text-slate-500">Theo dõi doanh thu theo từng khoảng thời gian.</p>
      </div>

      <div class="flex flex-wrap gap-3">
        <button
          v-for="item in ranges"
          :key="item"
          @click="active = item"
          class="rounded-xl px-5 py-3 font-semibold transition-all duration-300"
          :class="
            active === item
              ? 'bg-slate-900 text-white shadow-lg'
              : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
          "
        >
          {{ item }}
        </button>
      </div>
    </div>

    <!-- Body -->

    <div class="grid grid-cols-1 2xl:grid-cols-4">
      <!-- LEFT -->

      <div class="border-r border-slate-100 p-8 space-y-7">
        <div
          v-for="item in overview"
          :key="item.title"
          class="rounded-2xl border border-slate-100 p-5 hover:border-indigo-300 transition"
        >
          <div class="text-sm font-semibold text-slate-500">
            {{ item.title }}
          </div>

          <div class="mt-3 text-3xl font-black text-slate-800">
            {{ item.value }}
          </div>

          <div class="mt-2 flex items-center gap-2">
            <span class="rounded-full bg-emerald-100 px-2 py-1 text-xs font-bold text-emerald-700">
              {{ item.change }}
            </span>

            <span class="text-xs text-slate-400"> so với kỳ trước </span>
          </div>
        </div>
      </div>

      <!-- RIGHT -->

      <div class="2xl:col-span-3 p-8">
        <!-- Toolbar -->

        <div class="flex flex-wrap items-center justify-between gap-4 mb-8">
          <div class="flex gap-3">
            <button class="rounded-xl bg-indigo-600 text-white px-5 py-2.5 font-semibold">
              Doanh thu
            </button>

            <button
              class="rounded-xl bg-slate-100 px-5 py-2.5 font-semibold text-slate-600 hover:bg-slate-200"
            >
              Đơn hàng
            </button>

            <button
              class="rounded-xl bg-slate-100 px-5 py-2.5 font-semibold text-slate-600 hover:bg-slate-200"
            >
              Khách hàng
            </button>
          </div>

          <button
            class="rounded-xl border border-slate-200 px-5 py-2.5 font-semibold hover:bg-slate-50"
          >
            Tải báo cáo
          </button>
        </div>

        <!-- Fake Chart -->

        <div
          class="relative h-[430px] rounded-[28px] bg-gradient-to-br from-slate-50 to-white border border-slate-100 overflow-hidden"
        >
          <!-- Grid -->

          <div class="absolute inset-0">
            <div
              v-for="n in 8"
              :key="'h' + n"
              class="absolute left-0 right-0 border-t border-slate-100"
              :style="{ top: n * 12.5 + '%' }"
            ></div>

            <div
              v-for="n in 11"
              :key="'v' + n"
              class="absolute top-0 bottom-0 border-l border-slate-100"
              :style="{ left: n * 9 + '%' }"
            ></div>
          </div>

          <!-- Chart Area -->

          <div class="absolute inset-0 flex items-center justify-center">
            <div class="text-center">
              <div class="text-7xl">📈</div>

              <div class="mt-5 text-2xl font-black text-slate-700">
                ApexChart sẽ hiển thị tại đây
              </div>

              <div class="mt-2 text-slate-400">Sau khi cài apexcharts chỉ cần thay phần này.</div>
            </div>
          </div>
        </div>

        <!-- Bottom Stats -->

        <div class="grid grid-cols-2 xl:grid-cols-4 gap-5 mt-8">
          <div v-for="item in bottom" :key="item.title" class="rounded-2xl bg-slate-50 p-5">
            <div class="text-sm text-slate-500">
              {{ item.title }}
            </div>

            <div class="mt-3 text-2xl font-black text-slate-800">
              {{ item.value }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const ranges = ['Hôm nay', '7 ngày', '30 ngày', 'Tháng', 'Năm']

const active = ref('30 ngày')

const overview = [
  {
    title: 'Doanh thu hôm nay',
    value: '12.5M',
    change: '+18%',
  },
  {
    title: 'Doanh thu tháng',
    value: '235.8M',
    change: '+12%',
  },
  {
    title: 'Đơn hàng',
    value: '1,285',
    change: '+9%',
  },
]

const bottom = [
  {
    title: 'Giá trị TB',
    value: '862K',
  },
  {
    title: 'Khách mới',
    value: '58',
  },
  {
    title: 'Đơn thành công',
    value: '97%',
  },
  {
    title: 'Tỷ lệ hoàn',
    value: '1.6%',
  },
]
</script>
<!-- ========================= -->
<!-- RevenueChart.vue (2/2) -->
<!-- Thêm vào CUỐI FILE -->
<!-- ========================= -->

<style scoped>
.chart-enter-active,
.chart-leave-active {
  transition: all 0.35s ease;
}

.chart-enter-from,
.chart-leave-to {
  opacity: 0;
  transform: translateY(10px);
}

.chart-bg {
  background-image:
    radial-gradient(circle at top right, rgba(99, 102, 241, 0.12), transparent 30%),
    radial-gradient(circle at bottom left, rgba(16, 185, 129, 0.08), transparent 35%);
}

.fake-line {
  position: absolute;
  left: 4%;
  right: 4%;
  bottom: 70px;
  height: 260px;
}

.fake-line svg {
  width: 100%;
  height: 100%;
}

.fake-line path {
  fill: none;
  stroke: #4f46e5;
  stroke-width: 5;
  stroke-linecap: round;
  stroke-linejoin: round;
}

.fake-line circle {
  fill: #4f46e5;
  stroke: white;
  stroke-width: 4;
}

.months {
  position: absolute;
  left: 40px;
  right: 40px;
  bottom: 22px;
  display: flex;
  justify-content: space-between;
  font-size: 13px;
  color: rgb(100 116 139);
  font-weight: 600;
}

.y-label {
  position: absolute;
  left: 18px;
  top: 35px;
  bottom: 60px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  font-size: 12px;
  color: rgb(148 163 184);
  font-weight: 700;
}

.chart-card:hover {
  transform: translateY(-3px);
}

.chart-card {
  transition: 0.25s;
}
</style>
