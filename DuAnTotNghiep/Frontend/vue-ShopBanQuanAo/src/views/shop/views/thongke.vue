<template>
  <div class="min-h-screen bg-slate-50 p-6">
    <!-- ================= HEADER ================= -->

    <div
      class="rounded-3xl overflow-hidden bg-gradient-to-r from-slate-900 via-indigo-900 to-slate-900 text-white shadow-2xl mb-8"
    >
      <div class="p-8 flex flex-col lg:flex-row lg:items-center lg:justify-between">
        <div>
          <div class="text-sm uppercase tracking-[6px] text-indigo-300 font-semibold">
            Dashboard
          </div>

          <h1 class="text-4xl font-black mt-3">Statistics Overview</h1>

          <p class="text-slate-300 mt-3 max-w-2xl">
            Theo dõi doanh thu, đơn hàng, khách hàng, sản phẩm bán chạy và hiệu suất kinh doanh theo
            thời gian.
          </p>
        </div>

        <div class="flex gap-3 mt-8 lg:mt-0">
          <button
            class="px-5 py-3 rounded-2xl bg-white/10 backdrop-blur border border-white/20 hover:bg-white/20 duration-300"
          >
            <i class="fa-solid fa-download mr-2"></i>

            Excel
          </button>

          <button class="px-5 py-3 rounded-2xl bg-indigo-500 hover:bg-indigo-600 duration-300">
            <i class="fa-solid fa-file-pdf mr-2"></i>

            PDF
          </button>
        </div>
      </div>
    </div>

    <!-- ================= FILTER ================= -->

    <div class="bg-white rounded-3xl border border-slate-200 shadow-sm p-6 mb-8">
      <div class="grid lg:grid-cols-5 gap-4">
        <select v-model="filter.type" class="rounded-2xl border border-slate-200 px-4 py-3">
          <option value="day">Theo ngày</option>
          <option value="month">Theo tháng</option>
          <option value="year">Theo năm</option>
        </select>

        <input
          type="date"
          v-model="filter.from"
          class="rounded-2xl border border-slate-200 px-4 py-3"
        />

        <input
          type="date"
          v-model="filter.to"
          class="rounded-2xl border border-slate-200 px-4 py-3"
        />

        <button class="rounded-2xl bg-indigo-600 text-white font-semibold">Áp dụng</button>

        <button class="rounded-2xl border border-slate-200">Đặt lại</button>
      </div>
    </div>

    <!-- ================= OVERVIEW ================= -->

    <div class="grid xl:grid-cols-4 md:grid-cols-2 gap-6 mb-8">
      <div
        v-for="item in overviewCards"
        :key="item.title"
        class="bg-white rounded-3xl p-6 shadow-sm border border-slate-200 hover:-translate-y-1 duration-300"
      >
        <div class="flex justify-between">
          <div>
            <div class="text-sm text-slate-500">
              {{ item.title }}
            </div>

            <div class="text-4xl font-black mt-3">
              {{ item.value }}
            </div>

            <div
              class="mt-4 inline-flex items-center px-3 py-1 rounded-full text-sm"
              :class="item.growth > 0 ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'"
            >
              {{ item.growth }}%
            </div>
          </div>

          <div class="w-16 h-16 rounded-2xl flex items-center justify-center" :class="item.bg">
            <i :class="item.icon" class="text-2xl"></i>
          </div>
        </div>
      </div>
    </div>

    <!-- ================= CHART ================= -->

    <div class="grid xl:grid-cols-3 gap-6 mb-8">
      <div class="xl:col-span-2 bg-white rounded-3xl border border-slate-200 shadow-sm p-6">
        <div class="flex justify-between items-center mb-6">
          <div>
            <h2 class="text-xl font-bold">Doanh thu</h2>

            <p class="text-slate-500 text-sm">12 tháng gần nhất</p>
          </div>
        </div>

        <VueApexCharts type="area" height="360" :options="revenueOption" :series="revenueSeries" />
      </div>

      <div class="bg-white rounded-3xl border border-slate-200 shadow-sm p-6">
        <h2 class="text-xl font-bold mb-6">Thanh toán</h2>

        <VueApexCharts type="donut" height="360" :series="paymentSeries" :options="paymentOption" />
      </div>
    </div>

    <!-- ORDER + CATEGORY -->

    <div class="grid xl:grid-cols-2 gap-6 mb-8">
      <div class="bg-white rounded-3xl p-6 border border-slate-200">
        <div class="flex justify-between mb-6">
          <h2 class="text-xl font-bold">Đơn hàng</h2>
        </div>

        <VueApexCharts height="330" type="bar" :series="orderSeries" :options="orderOption" />
      </div>

      <div class="bg-white rounded-3xl p-6 border border-slate-200">
        <div class="flex justify-between mb-6">
          <h2 class="text-xl font-bold">Danh mục</h2>
        </div>
        <VueApexCharts type="pie" height="330" :series="categorySeries" :options="categoryOption" />
      </div>
    </div>

    <!-- ================= PRODUCT + CUSTOMER ================= -->

    <div class="grid xl:grid-cols-2 gap-6 mb-8">
      <!-- TOP PRODUCT -->

      <div class="bg-white rounded-3xl border border-slate-200 shadow-sm">
        <div class="px-6 py-5 border-b">
          <h2 class="text-xl font-bold">Top sản phẩm bán chạy</h2>

          <p class="text-sm text-slate-500 mt-1">10 sản phẩm có doanh số cao nhất</p>
        </div>

        <div class="divide-y">
          <div
            v-for="item in topProducts"
            :key="item.id"
            class="p-5 flex items-center hover:bg-slate-50 transition"
          >
            <img :src="item.image" class="w-16 h-16 rounded-2xl object-cover border" />

            <div class="ml-4 flex-1">
              <div class="font-bold text-slate-800">
                {{ item.name }}
              </div>

              <div class="text-sm text-slate-500 mt-1">Đã bán {{ item.sold }} sản phẩm</div>

              <div class="mt-3 h-2 rounded-full bg-slate-100 overflow-hidden">
                <div
                  class="bg-indigo-600 h-full rounded-full"
                  :style="{ width: item.percent + '%' }"
                ></div>
              </div>
            </div>

            <div class="ml-4 text-right">
              <div class="font-black text-lg">
                {{ item.revenue }}
              </div>

              <div class="text-xs text-slate-400">{{ item.percent }}%</div>
            </div>
          </div>
        </div>
      </div>

      <!-- TOP CUSTOMER -->

      <div class="bg-white rounded-3xl border border-slate-200 shadow-sm">
        <div class="px-6 py-5 border-b">
          <h2 class="text-xl font-bold">Khách hàng VIP</h2>

          <p class="text-sm text-slate-500 mt-1">Những khách hàng chi tiêu nhiều nhất</p>
        </div>

        <div class="divide-y">
          <div
            v-for="customer in topCustomers"
            :key="customer.id"
            class="p-5 flex items-center hover:bg-slate-50"
          >
            <img :src="customer.avatar" class="w-14 h-14 rounded-full object-cover" />

            <div class="ml-4 flex-1">
              <div class="font-bold">
                {{ customer.name }}
              </div>

              <div class="text-sm text-slate-500 mt-1">{{ customer.orders }} đơn hàng</div>
            </div>

            <div class="text-right">
              <div class="font-black text-xl text-indigo-600">
                {{ customer.total }}
              </div>

              <div class="text-xs text-slate-400">Tổng chi tiêu</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- ================= RECENT ORDER ================= -->

    <div class="bg-white rounded-3xl border border-slate-200 shadow-sm overflow-hidden">
      <div class="px-6 py-5 border-b flex items-center justify-between">
        <div>
          <h2 class="text-xl font-bold">Đơn hàng gần đây</h2>

          <p class="text-sm text-slate-500">Danh sách đơn mới nhất</p>
        </div>

        <button class="px-5 py-2 rounded-xl bg-indigo-600 text-white">Xem tất cả</button>
      </div>

      <div class="overflow-x-auto">
        <table class="min-w-full">
          <thead class="bg-slate-50">
            <tr>
              <th class="px-6 py-4 text-left">Mã</th>

              <th class="px-6 py-4 text-left">Khách hàng</th>

              <th class="px-6 py-4 text-left">Ngày</th>

              <th class="px-6 py-4 text-left">Thanh toán</th>

              <th class="px-6 py-4 text-left">Trạng thái</th>

              <th class="px-6 py-4 text-right">Tổng tiền</th>
            </tr>
          </thead>

          <tbody>
            <tr v-for="order in recentOrders" :key="order.id" class="border-t hover:bg-slate-50">
              <td class="px-6 py-5 font-bold text-indigo-600">#{{ order.id }}</td>

              <td class="px-6 py-5">
                <div class="flex items-center gap-3">
                  <img :src="order.avatar" class="w-11 h-11 rounded-full object-cover" />

                  <div>
                    <div class="font-semibold">
                      {{ order.customer }}
                    </div>

                    <div class="text-xs text-slate-500">
                      {{ order.email }}
                    </div>
                  </div>
                </div>
              </td>

              <td class="px-6 py-5">
                {{ order.date }}
              </td>

              <td class="px-6 py-5">
                {{ order.payment }}
              </td>

              <td class="px-6 py-5">
                <span
                  class="px-3 py-1 rounded-full text-xs font-semibold"
                  :class="{
                    'bg-green-100 text-green-700': order.status === 'Hoàn thành',
                    'bg-yellow-100 text-yellow-700': order.status === 'Đang giao',
                    'bg-red-100 text-red-700': order.status === 'Đã hủy',
                  }"
                >
                  {{ order.status }}
                </span>
              </td>

              <td class="px-6 py-5 text-right font-black text-lg">
                {{ order.total }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import VueApexCharts from 'vue3-apexcharts'

const filter = ref({
  type: 'month',
  from: '',
  to: '',
})

const overviewCards = ref([
  {
    title: 'Doanh thu',
    value: '2.86 tỷ',
    growth: 18,
    icon: 'fa-solid fa-sack-dollar text-emerald-600',
    bg: 'bg-emerald-100',
  },

  {
    title: 'Đơn hàng',
    value: '12,486',
    growth: 13,
    icon: 'fa-solid fa-cart-shopping text-indigo-600',
    bg: 'bg-indigo-100',
  },

  {
    title: 'Khách hàng',
    value: '4,281',
    growth: 9,
    icon: 'fa-solid fa-users text-sky-600',
    bg: 'bg-sky-100',
  },

  {
    title: 'Sản phẩm',
    value: '986',
    growth: -3,
    icon: 'fa-solid fa-shirt text-rose-600',
    bg: 'bg-rose-100',
  },
])

const revenueSeries = ref([
  {
    name: 'Doanh thu',
    data: [120, 150, 180, 210, 260, 310, 340, 380, 420, 470, 520, 590],
  },
])

const revenueOption = ref({
  chart: {
    toolbar: {
      show: false,
    },

    zoom: {
      enabled: false,
    },
  },

  stroke: {
    curve: 'smooth',
    width: 4,
  },

  colors: ['#4F46E5'],

  fill: {
    type: 'gradient',
    gradient: {
      shadeIntensity: 1,
      opacityFrom: 0.5,
      opacityTo: 0.05,
    },
  },

  xaxis: {
    categories: ['T1', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'T8', 'T9', 'T10', 'T11', 'T12'],
  },
})

const orderSeries = ref([
  {
    name: 'Đơn hàng',
    data: [80, 120, 140, 180, 190, 210, 250, 270, 295, 310, 340, 390],
  },
])

const orderOption = ref({
  chart: {
    toolbar: {
      show: false,
    },
  },

  colors: ['#6366F1'],

  plotOptions: {
    bar: {
      borderRadius: 8,
      columnWidth: '55%',
    },
  },

  dataLabels: {
    enabled: false,
  },

  xaxis: {
    categories: ['T1', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'T8', 'T9', 'T10', 'T11', 'T12'],
  },

  grid: {
    borderColor: '#E2E8F0',
    strokeDashArray: 5,
  },
})

const paymentSeries = ref([48, 27, 15, 10])

const paymentOption = ref({
  labels: ['VNPay', 'COD', 'Momo', 'ZaloPay'],

  legend: {
    position: 'bottom',
  },

  colors: ['#4F46E5', '#10B981', '#F59E0B', '#EF4444'],

  dataLabels: {
    enabled: true,
  },
})

const categorySeries = ref([35, 28, 18, 12, 7])

const categoryOption = ref({
  labels: ['Áo', 'Quần', 'Giày', 'Túi', 'Phụ kiện'],

  legend: {
    position: 'bottom',
  },

  colors: ['#4F46E5', '#0EA5E9', '#10B981', '#F97316', '#EC4899'],
})

const topProducts = ref([
  {
    id: 1,
    image: 'https://picsum.photos/100?1',
    name: 'Nike Air Force 1',
    sold: 862,
    revenue: '468.000.000đ',
    percent: 95,
  },

  {
    id: 2,
    image: 'https://picsum.photos/100?2',
    name: 'Adidas Superstar',
    sold: 751,
    revenue: '401.000.000đ',
    percent: 88,
  },

  {
    id: 3,
    image: 'https://picsum.photos/100?3',
    name: 'Jordan Low',
    sold: 632,
    revenue: '359.000.000đ',
    percent: 80,
  },

  {
    id: 4,
    image: 'https://picsum.photos/100?4',
    name: 'Converse Classic',
    sold: 528,
    revenue: '290.000.000đ',
    percent: 71,
  },

  {
    id: 5,
    image: 'https://picsum.photos/100?5',
    name: 'Puma RS-X',
    sold: 482,
    revenue: '258.000.000đ',
    percent: 64,
  },
])

const topCustomers = ref([
  {
    id: 1,
    avatar: 'https://i.pravatar.cc/150?img=11',
    name: 'Nguyễn Văn A',
    orders: 45,
    total: '189.500.000đ',
  },

  {
    id: 2,
    avatar: 'https://i.pravatar.cc/150?img=12',
    name: 'Trần Thị B',
    orders: 39,
    total: '162.000.000đ',
  },

  {
    id: 3,
    avatar: 'https://i.pravatar.cc/150?img=13',
    name: 'Lê Văn C',
    orders: 35,
    total: '145.800.000đ',
  },

  {
    id: 4,
    avatar: 'https://i.pravatar.cc/150?img=14',
    name: 'Phạm Minh D',
    orders: 28,
    total: '118.900.000đ',
  },
])

const recentOrders = ref([
  {
    id: 1001,
    customer: 'Nguyễn Văn A',
    email: 'vana@gmail.com',
    avatar: 'https://i.pravatar.cc/100?img=21',
    payment: 'VNPay',
    status: 'Hoàn thành',
    total: '2.650.000đ',
    date: '05/07/2026',
  },

  {
    id: 1002,
    customer: 'Trần Thị B',
    email: 'thib@gmail.com',
    avatar: 'https://i.pravatar.cc/100?img=22',
    payment: 'COD',
    status: 'Đang giao',
    total: '1.920.000đ',
    date: '05/07/2026',
  },

  {
    id: 1003,
    customer: 'Lê Văn C',
    email: 'levanc@gmail.com',
    avatar: 'https://i.pravatar.cc/100?img=23',
    payment: 'Momo',
    status: 'Hoàn thành',
    total: '3.480.000đ',
    date: '04/07/2026',
  },

  {
    id: 1004,
    customer: 'Phạm Minh D',
    email: 'phamd@gmail.com',
    avatar: 'https://i.pravatar.cc/100?img=24',
    payment: 'ZaloPay',
    status: 'Đã hủy',
    total: '850.000đ',
    date: '04/07/2026',
  },
  {
    id: 1005,
    customer: 'Hoàng Văn E',
    email: 'hoange@gmail.com',
    avatar: 'https://i.pravatar.cc/100?img=25',
    payment: 'VNPay',
    status: 'Hoàn thành',
    total: '5.120.000đ',
    date: '03/07/2026',
  },

  {
    id: 1006,
    customer: 'Đỗ Thị F',
    email: 'dothif@gmail.com',
    avatar: 'https://i.pravatar.cc/100?img=26',
    payment: 'COD',
    status: 'Đang giao',
    total: '2.180.000đ',
    date: '03/07/2026',
  },

  {
    id: 1007,
    customer: 'Bùi Văn G',
    email: 'buivang@gmail.com',
    avatar: 'https://i.pravatar.cc/100?img=27',
    payment: 'Momo',
    status: 'Hoàn thành',
    total: '3.760.000đ',
    date: '02/07/2026',
  },
])

const formatCurrency = (value) => {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
    maximumFractionDigits: 0,
  }).format(value)
}

const exportExcel = () => {
  console.log('Export Excel...')
}

const exportPDF = () => {
  console.log('Export PDF...')
}
</script>

<style scoped>
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 999px;
}

::-webkit-scrollbar-track {
  background: #f8fafc;
}

table th {
  font-size: 13px;
  font-weight: 700;
  color: #64748b;
}

table td {
  font-size: 14px;
}

button {
  transition: 0.25s;
}

button:hover {
  transform: translateY(-2px);
}

img {
  transition: 0.25s;
}

img:hover {
  transform: scale(1.06);
}
</style>
