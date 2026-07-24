<template>
  <div
    class="bg-white rounded-3xl border border-slate-200 shadow-sm overflow-hidden hover:shadow-xl transition-all duration-300"
  >
    <!-- HEADER & BỘ LỌC TIÊU CHÍ -->
    <div
      class="p-6 border-b border-slate-100 flex flex-col md:flex-row md:items-center md:justify-between gap-4 bg-gradient-to-r from-slate-50/50 to-white"
    >
      <div>
        <h2 class="text-xl font-black text-slate-800 flex items-center gap-2">
          <span
            class="p-1.5 bg-amber-100 text-amber-600 rounded-xl flex items-center justify-center text-base"
          >
            👑
          </span>
          Khách hàng VIP
        </h2>
        <p class="text-sm text-slate-500 mt-1">
          Xếp hạng khách hàng thân thiết theo
          {{ currentSort === 'revenue' ? 'tổng chi tiêu' : 'số lượng đơn' }}
        </p>
      </div>

      <!-- FILTERS -->
      <div class="flex items-center gap-3 flex-wrap">
        <!-- TAB CHỌN TIÊU CHÍ -->
        <div class="flex items-center bg-slate-100/80 p-1 rounded-2xl border border-slate-200/60">
          <button
            v-for="tab in filterTabs"
            :key="tab.value"
            @click="currentSort = tab.value"
            class="px-3 py-1.5 text-xs font-bold rounded-xl transition-all"
            :class="
              currentSort === tab.value
                ? 'bg-white text-indigo-600 shadow-sm'
                : 'text-slate-500 hover:text-slate-800'
            "
          >
            {{ tab.label }}
          </button>
        </div>

        <!-- LIMIT DROPDOWN -->
        <div
          class="flex items-center gap-1.5 bg-slate-100/80 p-1 rounded-2xl border border-slate-200/60"
        >
          <span class="text-xs font-bold text-slate-500 pl-2 flex items-center gap-1">
            <i class="fa-solid fa-list-ol text-[10px]"></i> Top:
          </span>
          <select
            v-model="limit"
            class="bg-white text-slate-800 text-xs font-bold rounded-xl px-2.5 py-1.5 outline-none shadow-sm cursor-pointer border border-slate-200/50 focus:border-indigo-500 transition"
          >
            <option :value="5">5</option>
            <option :value="10">10</option>
            <option :value="20">20</option>
            <option :value="50">50</option>
          </select>
        </div>
      </div>
    </div>

    <!-- DANH SÁCH KHÁCH HÀNG -->
    <div v-if="limitedData.length" class="divide-y divide-slate-100">
      <div
        v-for="(item, index) in limitedData"
        :key="item.customerId || index"
        class="group flex items-center justify-between p-4 sm:p-5 hover:bg-gradient-to-r hover:from-indigo-50/40 hover:to-purple-50/40 transition-all duration-300"
      >
        <!-- THÔNG TIN TRÁI: AVATAR & TÊN -->
        <div class="flex items-center gap-3.5">
          <!-- Avatar Cute & Huy hiệu Top 3 (GÓC TRÊN BÊN TRÁI) -->
          <div class="relative shrink-0">
            <!-- Huy chương số 1, 2, 3 chuyển lên góc trên bên trái -->
            <span
              v-if="index < 3"
              class="absolute -top-1.5 -left-1.5 w-5 h-5 rounded-full text-[10px] flex items-center justify-center font-black text-white shadow-md border-2 border-white z-10"
              :class="getBadgeColor(index)"
            >
              {{ index + 1 }}
            </span>

            <div
              class="w-12 h-12 rounded-2xl flex items-center justify-center font-black text-base shadow-sm transition-transform group-hover:scale-105"
              :class="getAvatarStyle(index)"
            >
              {{ getInitials(item.customerName) }}
            </div>
          </div>

          <div>
            <div
              class="font-bold text-slate-800 text-sm sm:text-base group-hover:text-indigo-600 transition"
            >
              {{ item.customerName || 'Khách lẻ' }}
            </div>
            <div class="flex items-center gap-2 mt-1 text-xs text-slate-400">
              <span
                class="bg-indigo-50 text-indigo-600 px-2.5 py-0.5 rounded-lg font-semibold text-[11px] flex items-center gap-1"
              >
                <i class="fa-solid fa-bag-shopping text-[10px]"></i> {{ item.totalOrders || 0 }} đơn
                hàng
              </span>
            </div>
          </div>
        </div>

        <!-- THÔNG TIN PHẢI: HIỂN THỊ CHỈ SỐ THEO TAB -->
        <div class="text-right">
          <div
            class="font-black text-slate-800 text-sm md:text-base group-hover:text-indigo-600 transition"
          >
            {{ formatValue(getMainMetricValue(item)) }}
          </div>
          <div class="text-[11px] text-slate-400 font-medium mt-0.5">
            {{ getMetricLabel() }}
          </div>
        </div>
      </div>
    </div>

    <!-- TRƯỜNG HỢP TRỐNG -->
    <div
      v-else
      class="text-center text-slate-400 py-12 flex flex-col items-center justify-center gap-2"
    >
      <i class="fa-solid fa-users-slash text-3xl text-slate-300 animate-bounce"></i>
      <span class="text-sm font-medium">Chưa có dữ liệu khách hàng</span>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  data: {
    type: Array,
    default: () => [],
  },
})

// Trạng thái bộ lọc sắp xếp ('revenue' | 'orders')
const currentSort = ref('revenue')
const limit = ref(5)

const filterTabs = [
  { label: '💰 Doanh thu', value: 'revenue' },
  { label: '📦 Số đơn hàng', value: 'orders' },
]

// Sắp xếp dữ liệu theo tab chọn
const sortedData = computed(() => {
  if (!props.data || !props.data.length) return []

  return [...props.data].sort((a, b) => {
    if (currentSort.value === 'revenue') {
      return Number(b.totalSpent || 0) - Number(a.totalSpent || 0)
    } else {
      return Number(b.totalOrders || 0) - Number(a.totalOrders || 0)
    }
  })
})

// Giới hạn số lượng hiển thị (Top 5, 10, 20...)
const limitedData = computed(() => {
  return sortedData.value.slice(0, limit.value)
})

// Giá trị hiển thị bên phải
const getMainMetricValue = (item) => {
  if (currentSort.value === 'revenue') return item.totalSpent || 0
  return item.totalOrders || 0
}

// Nhãn dưới giá trị bên phải
const getMetricLabel = () => {
  if (currentSort.value === 'revenue') return 'Tổng chi tiêu'
  return 'Số đơn đã mua'
}

// Lấy 2 chữ cái đầu của tên làm Avatar
const getInitials = (name) => {
  if (!name) return 'VIP'
  const words = name.trim().split(' ')
  if (words.length >= 2) {
    return (words[0][0] + words[words.length - 1][0]).toUpperCase()
  }
  return name.substring(0, 2).toUpperCase()
}

// Màu nền avatar ngẫu nhiên theo vị trí
const getAvatarStyle = (index) => {
  const styles = [
    'bg-gradient-to-br from-amber-400 to-orange-500 text-white shadow-amber-200',
    'bg-gradient-to-br from-indigo-400 to-purple-500 text-white shadow-indigo-200',
    'bg-gradient-to-br from-pink-400 to-rose-500 text-white shadow-pink-200',
    'bg-gradient-to-br from-emerald-400 to-teal-500 text-white shadow-emerald-200',
    'bg-gradient-to-br from-sky-400 to-blue-500 text-white shadow-sky-200',
  ]
  return styles[index % styles.length]
}

// Màu huy hiệu top 1, 2, 3
const getBadgeColor = (index) => {
  if (index === 0) return 'bg-amber-500' // Vàng
  if (index === 1) return 'bg-slate-400' // Bạc
  return 'bg-amber-700' // Đồng
}

const formatValue = (value) => {
  if (currentSort.value === 'orders') return Number(value || 0).toLocaleString('vi-VN') + ' đơn'
  return Number(value || 0).toLocaleString('vi-VN') + ' ₫'
}
</script>
