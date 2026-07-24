<template>
  <div
    class="bg-white rounded-3xl border border-slate-200 shadow-sm p-6 hover:shadow-xl transition-all duration-300"
  >
    <!-- HEADER & BỘ LỌC -->
    <div
      class="flex flex-col lg:flex-row lg:items-center justify-between gap-4 mb-6 pb-5 border-b border-slate-100"
    >
      <div>
        <h2 class="text-xl font-black text-slate-800 flex items-center gap-2">
          <span>🔥</span> Top sản phẩm bán chạy
        </h2>
        <p class="text-sm text-slate-500 mt-0.5">
          Sản phẩm dẫn đầu theo
          {{ filterType === 'doanhThu' ? 'doanh thu đóng góp' : 'số lượng bán ra' }}
        </p>
      </div>

      <!-- BỘ LỌC NHANH -->
      <div class="flex flex-wrap items-center gap-3">
        <!-- Tab Xếp theo (Thay thế cho Dropdown cũ) -->
        <div class="flex items-center bg-slate-100 p-1 rounded-2xl">
          <button
            @click="filterType = 'doanhThu'"
            class="px-3 py-1.5 text-xs font-bold rounded-xl transition-all flex items-center gap-1.5"
            :class="
              filterType === 'doanhThu'
                ? 'bg-white text-indigo-600 shadow-sm'
                : 'text-slate-500 hover:text-slate-800'
            "
          >
            <i class="fa-solid fa-sack-dollar text-[11px]"></i> Doanh thu
          </button>
          <button
            @click="filterType = 'soLuong'"
            class="px-3 py-1.5 text-xs font-bold rounded-xl transition-all flex items-center gap-1.5"
            :class="
              filterType === 'soLuong'
                ? 'bg-white text-indigo-600 shadow-sm'
                : 'text-slate-500 hover:text-slate-800'
            "
          >
            <i class="fa-solid fa-boxes-packing text-[11px]"></i> Số lượng
          </button>
        </div>

        <!-- Limit dropdown được thiết kế lại cute hơn -->
        <div
          class="relative flex items-center bg-slate-100 px-3 py-1.5 rounded-2xl border border-transparent hover:border-slate-200 transition"
        >
          <i class="fa-solid fa-list-ol text-xs text-slate-400 mr-2"></i>
          <select
            v-model="limit"
            class="bg-transparent text-slate-700 text-xs font-bold outline-none cursor-pointer pr-1"
          >
            <option :value="5">Top 5</option>
            <option :value="10">Top 10</option>
            <option :value="20">Top 20</option>
            <option :value="50">Top 50</option>
          </select>
        </div>
      </div>
    </div>

    <!-- DANH SÁCH SẢN PHẨM -->
    <div v-if="limitedData.length" class="space-y-3">
      <div
        v-for="(item, index) in limitedData"
        :key="item.productId || index"
        class="group flex flex-col sm:flex-row sm:items-center justify-between p-4 rounded-2xl bg-slate-50/50 hover:bg-gradient-to-r hover:from-indigo-50/40 hover:to-purple-50/40 border border-transparent hover:border-indigo-100 transition-all duration-300 gap-4"
      >
        <!-- THÔNG TIN TRÁI: RANK, ẢNH & CHI TIẾT -->
        <div class="flex items-center gap-3.5 flex-1 min-w-0">
          <!-- Ảnh + Huy hiệu Rank Top 1, 2, 3 -->
          <div class="relative shrink-0">
            <img
              :src="imageUrl(item.anh)"
              class="w-14 h-14 rounded-2xl object-cover border border-slate-200/80 shadow-sm transition-transform duration-300 group-hover:scale-105"
              @error="imageError"
            />

            <span
              class="absolute -top-1.5 -left-1.5 w-6 h-6 rounded-xl text-xs flex items-center justify-center font-black text-white shadow-md border-2 border-white"
              :class="getRankBadgeClass(index)"
            >
              {{ index + 1 }}
            </span>
          </div>

          <!-- Chi tiết tên & tags -->
          <div class="flex-1 min-w-0">
            <div
              class="font-bold text-slate-800 text-sm md:text-base truncate group-hover:text-indigo-600 transition"
            >
              {{ item.tenSanPham }}
            </div>

            <!-- Tags thông tin sản phẩm -->
            <div class="flex flex-wrap items-center gap-1.5 mt-1 text-[11px]">
              <span
                v-if="item.maSanPham"
                class="bg-slate-200/60 text-slate-600 px-2 py-0.5 rounded-md font-mono font-medium"
              >
                #{{ item.maSanPham }}
              </span>
              <span
                v-if="item.thuongHieu"
                class="bg-indigo-50 text-indigo-600 px-2 py-0.5 rounded-md font-semibold flex items-center gap-1"
              >
                <i class="fa-solid fa-copyright text-[9px]"></i> {{ item.thuongHieu }}
              </span>
              <span
                v-if="item.chatLieu"
                class="bg-emerald-50 text-emerald-600 px-2 py-0.5 rounded-md font-semibold flex items-center gap-1"
              >
                <i class="fa-solid fa-shirt text-[9px]"></i> {{ item.chatLieu }}
              </span>
              <span
                v-if="item.soBienThe"
                class="bg-purple-50 text-purple-600 px-2 py-0.5 rounded-md font-semibold flex items-center gap-1"
              >
                <i class="fa-solid fa-layer-group text-[9px]"></i> {{ item.soBienThe }} biến thể
              </span>
            </div>

            <!-- Thanh tiến trình Rực rỡ -->
            <div class="mt-2.5 max-w-md">
              <div class="flex justify-between text-[11px] font-medium mb-1">
                <span class="text-slate-400">
                  {{ filterType === 'doanhThu' ? 'Tỷ trọng doanh thu' : 'Số lượng bán ra' }}
                </span>
                <span class="font-bold text-indigo-600">
                  {{
                    filterType === 'doanhThu'
                      ? revenuePercent(item.doanhThu) + '%'
                      : (item.soLuongBan || 0) + ' SP'
                  }}
                </span>
              </div>
              <div class="h-2 w-full bg-slate-100 rounded-full overflow-hidden p-0.5">
                <div
                  class="h-full bg-gradient-to-r from-indigo-500 to-purple-500 rounded-full transition-all duration-500"
                  :style="{
                    width:
                      filterType === 'doanhThu'
                        ? revenuePercent(item.doanhThu) + '%'
                        : percent(item.soLuongBan) + '%',
                  }"
                ></div>
              </div>
            </div>
          </div>
        </div>

        <!-- THÔNG TIN PHẢI: DOANH THU, LỢI NHUẬN & TỒN KHO -->
        <div
          class="flex sm:flex-col items-center sm:items-end justify-between border-t sm:border-t-0 border-slate-100 pt-2 sm:pt-0 shrink-0"
        >
          <div class="text-right">
            <div
              class="font-black text-indigo-600 text-sm md:text-base flex items-center justify-end gap-1"
            >
              <i class="fa-solid fa-money-bill-wave text-xs text-indigo-400"></i>
              {{ money(item.doanhThu) }}
            </div>
            <div
              class="text-xs text-emerald-600 font-bold mt-0.5 flex items-center justify-end gap-1"
            >
              <i class="fa-solid fa-arrow-trend-up text-[10px]"></i>
              Lãi {{ money(item.loiNhuan) }}
            </div>
          </div>

          <div class="mt-1 sm:mt-2">
            <span
              class="inline-flex items-center gap-1 text-[11px] font-bold text-slate-500 bg-slate-100 px-2.5 py-0.5 rounded-lg"
            >
              <i class="fa-solid fa-boxes-stacked text-[10px] text-slate-400"></i>
              Tồn: {{ item.tongTonKho || 0 }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- TRƯỜNG HỢP TRỐNG -->
    <div
      v-else
      class="text-center text-slate-400 py-10 flex flex-col items-center justify-center gap-2"
    >
      <i class="fa-solid fa-box-open text-3xl text-slate-300 animate-bounce"></i>
      <span class="text-sm font-medium">Chưa có dữ liệu sản phẩm bán chạy</span>
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

const filterType = ref('doanhThu')
const limit = ref(5)

const sortedData = computed(() => {
  return [...props.data].sort((a, b) => {
    if (filterType.value === 'doanhThu') {
      return Number(b.doanhThu || 0) - Number(a.doanhThu || 0)
    } else {
      return Number(b.soLuongBan || 0) - Number(a.soLuongBan || 0)
    }
  })
})

const limitedData = computed(() => {
  return sortedData.value.slice(0, limit.value)
})

// Huy hiệu xếp hạng đồng bộ màu với Top 3 bên Khách hàng VIP
const getRankBadgeClass = (index) => {
  if (index === 0) return 'bg-gradient-to-br from-amber-400 to-orange-500 shadow-orange-200' // Vàng
  if (index === 1) return 'bg-gradient-to-br from-slate-300 to-slate-500 shadow-slate-200' // Bạc
  if (index === 2) return 'bg-gradient-to-br from-amber-600 to-amber-800 shadow-amber-200' // Đồng
  return 'bg-slate-400'
}

const imageUrl = (path) => {
  if (!path) {
    return 'https://placehold.co/100x100?text=No+Image'
  }
  return 'http://localhost:8080' + path
}

const imageError = (event) => {
  event.target.src = 'https://placehold.co/100x100?text=No+Image'
}

const money = (value) => {
  return Number(value || 0).toLocaleString('vi-VN') + ' ₫'
}

const maxSold = () => {
  return Math.max(...props.data.map((x) => Number(x.soLuongBan || 0)), 1)
}

const percent = (value) => {
  return Math.round((Number(value || 0) / maxSold()) * 100)
}

const totalRevenue = () => {
  return props.data.reduce((sum, item) => sum + Number(item.doanhThu || 0), 0)
}

const revenuePercent = (revenue) => {
  const total = totalRevenue()
  if (!total) return 0
  return ((Number(revenue || 0) / total) * 100).toFixed(1)
}
</script>
