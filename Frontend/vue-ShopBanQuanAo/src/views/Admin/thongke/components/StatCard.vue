<template>
  <div
    class="bg-white rounded-3xl border border-slate-200 shadow-sm p-6 hover:shadow-lg transition flex flex-col justify-between"
  >
    <!-- Tiêu đề và giá trị chính -->
    <div class="flex justify-between items-start">
      <div>
        <p class="text-sm text-slate-500 font-medium">{{ title }}</p>
        <h2 class="text-2xl xl:text-3xl font-black mt-2 text-slate-800">{{ value }}</h2>
        <span
          v-if="subValue"
          class="inline-block text-xs font-bold text-amber-600 bg-amber-50 px-2 py-0.5 rounded-lg mt-1"
        >
          {{ subValue }}
        </span>
      </div>

      <div class="w-12 h-12 rounded-2xl flex items-center justify-center shrink-0" :class="bg">
        <i :class="icon" class="text-xl"></i>
      </div>
    </div>

    <!-- ================= CASE 1: DOANH THU ================= -->
    <div v-if="cash !== null && transfer !== null" class="mt-5 border-t border-slate-100 pt-3">
      <!-- Legend & Info -->
      <div class="flex justify-between items-end mb-2">
        <div class="text-xs">
          <span class="flex items-center gap-1.5 text-slate-500 font-medium">
            <span
              class="w-5 h-5 rounded-lg bg-emerald-50 text-emerald-600 flex items-center justify-center text-[10px]"
            >
              <i class="fa-solid fa-money-bill"></i>
            </span>
            Tiền mặt
          </span>
          <span class="font-bold text-slate-800 ml-6">{{ money(cash) }}</span>
        </div>
        <div class="text-xs text-right">
          <span class="flex items-center justify-end gap-1.5 text-slate-500 font-medium">
            Chuyển khoản
            <span
              class="w-5 h-5 rounded-lg bg-indigo-50 text-indigo-600 flex items-center justify-center text-[10px]"
            >
              <i class="fa-solid fa-building-columns"></i>
            </span>
          </span>
          <span class="font-bold text-slate-800 mr-6">{{ money(transfer) }}</span>
        </div>
      </div>
      <!-- Biểu đồ tỉ lệ -->
      <div class="h-2 w-full flex rounded-full overflow-hidden bg-slate-100 gap-0.5">
        <div
          class="bg-emerald-500 transition-all duration-500"
          :style="{ width: cashPercent + '%' }"
          title="Tiền mặt"
        ></div>
        <div
          class="bg-indigo-600 transition-all duration-500"
          :style="{ width: transferPercent + '%' }"
          title="Chuyển khoản"
        ></div>
      </div>
      <div class="flex justify-between mt-1.5 text-[10px] text-slate-400 font-bold">
        <span>{{ cashPercent }}%</span>
        <span>{{ transferPercent }}%</span>
      </div>
    </div>

    <!-- ================= CASE 2: LỢI NHUẬN ================= -->
    <div v-if="cost" class="mt-5 border-t border-slate-100 pt-3">
      <div class="bg-slate-50 rounded-2xl p-3 border border-slate-100 flex items-center gap-3">
        <div
          class="w-8 h-8 rounded-xl bg-amber-50 text-amber-600 flex items-center justify-center shrink-0"
        >
          <i class="fa-solid fa-box-archive text-xs"></i>
        </div>
        <div class="flex justify-between items-center flex-1 text-xs">
          <span class="text-slate-500 font-medium">Tổng giá vốn</span>
          <span class="font-bold text-slate-700">{{ cost }}</span>
        </div>
      </div>
    </div>

    <!-- ================= CASE 3: ĐƠN HÀNG ================= -->
    <div v-if="online !== null && pos !== null" class="mt-5 border-t border-slate-100 pt-3">
      <div class="flex justify-between items-end mb-2">
        <div class="text-xs">
          <span class="flex items-center gap-1.5 text-slate-500 font-medium">
            <span
              class="w-5 h-5 rounded-lg bg-blue-50 text-blue-600 flex items-center justify-center text-[10px]"
            >
              <i class="fa-solid fa-globe"></i>
            </span>
            Online
          </span>
          <span class="font-bold text-slate-800 ml-6">{{ online }} đơn</span>
        </div>
        <div class="text-xs text-right">
          <span class="flex items-center justify-end gap-1.5 text-slate-500 font-medium">
            Tại quầy
            <span
              class="w-5 h-5 rounded-lg bg-orange-50 text-orange-600 flex items-center justify-center text-[10px]"
            >
              <i class="fa-solid fa-store"></i>
            </span>
          </span>
          <span class="font-bold text-slate-800 mr-6">{{ pos }} đơn</span>
        </div>
      </div>
      <div class="h-2 w-full flex rounded-full overflow-hidden bg-slate-100 gap-0.5">
        <div
          class="bg-blue-500 transition-all duration-500"
          :style="{ width: onlinePercent + '%' }"
        ></div>
        <div
          class="bg-orange-400 transition-all duration-500"
          :style="{ width: posPercent + '%' }"
        ></div>
      </div>
      <div class="flex justify-between mt-1.5 text-[10px] text-slate-400 font-bold">
        <span>{{ onlinePercent }}%</span>
        <span>{{ posPercent }}%</span>
      </div>
    </div>

    <!-- ================= CASE 4: KHÁCH HÀNG ================= -->
    <div v-if="member !== null && guest !== null" class="mt-5 border-t border-slate-100 pt-3">
      <div class="flex justify-between items-end mb-2">
        <div class="text-xs">
          <span class="flex items-center gap-1.5 text-slate-500 font-medium">
            <span
              class="w-5 h-5 rounded-lg bg-purple-50 text-purple-600 flex items-center justify-center text-[10px]"
            >
              <i class="fa-solid fa-id-card"></i>
            </span>
            Thành viên
          </span>
          <span class="font-bold text-slate-800 ml-6">{{ member }} khách</span>
        </div>
        <div class="text-xs text-right">
          <span class="flex items-center justify-end gap-1.5 text-slate-500 font-medium">
            Khách lẻ
            <span
              class="w-5 h-5 rounded-lg bg-pink-50 text-pink-600 flex items-center justify-center text-[10px]"
            >
              <i class="fa-solid fa-user"></i>
            </span>
          </span>
          <span class="font-bold text-slate-800 mr-6">{{ guest }} khách</span>
        </div>
      </div>
      <div class="h-2 w-full flex rounded-full overflow-hidden bg-slate-100 gap-0.5">
        <div
          class="bg-purple-500 transition-all duration-500"
          :style="{ width: memberPercent + '%' }"
        ></div>
        <div
          class="bg-pink-400 transition-all duration-500"
          :style="{ width: guestPercent + '%' }"
        ></div>
      </div>
      <div class="flex justify-between mt-1.5 text-[10px] text-slate-400 font-bold">
        <span>{{ memberPercent }}%</span>
        <span>{{ guestPercent }}%</span>
      </div>
    </div>

    <!-- ================= CASE 5: SẢN PHẨM (ĐÃ CHUẨN HÓA CHÚ THÍCH) ================= -->
    <div
      v-if="sku !== null && avg !== null"
      class="mt-5 border-t border-slate-100 pt-3 grid grid-cols-2 gap-2"
    >
      <!-- Ô Biến thể SKU -->
      <div
        class="bg-slate-50 p-2.5 rounded-2xl border border-slate-100 flex flex-col justify-center"
        title="Số lượng mẫu/mã biến thể (SKU) khác nhau đã phát sinh đơn hàng"
      >
        <span class="text-[10px] text-slate-400 font-semibold uppercase flex items-center gap-1">
          <i class="fa-solid fa-shapes text-rose-500"></i> Biến thể (SKU)
        </span>
        <span class="text-sm font-black text-slate-800 mt-1 flex items-baseline gap-1">
          {{ sku }} <span class="text-[10px] font-bold text-slate-400">mã</span>
        </span>
      </div>

      <!-- Ô Trung bình SP trên mỗi đơn -->
      <div
        class="bg-slate-50 p-2.5 rounded-2xl border border-slate-100 flex flex-col justify-center"
        title="Số lượng sản phẩm trung bình bán ra trên mỗi đơn hàng"
      >
        <span class="text-[10px] text-slate-400 font-semibold uppercase flex items-center gap-1">
          <i class="fa-solid fa-boxes-stacked text-indigo-500"></i> TB SP / Đơn
        </span>
        <span class="text-sm font-black text-slate-800 mt-1 flex items-baseline gap-1">
          {{ avg }} <span class="text-[10px] font-bold text-slate-400">sp/đơn</span>
        </span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  title: { type: String, default: '' },
  value: { type: [String, Number], default: 0 },
  subValue: { type: String, default: '' },
  icon: { type: String, default: '' },
  bg: { type: String, default: 'bg-indigo-100 text-indigo-600' },

  cash: { type: [String, Number], default: null },
  transfer: { type: [String, Number], default: null },
  cost: { type: [String, Number], default: null },
  online: { type: [String, Number], default: null },
  pos: { type: [String, Number], default: null },
  member: { type: [String, Number], default: null },
  guest: { type: [String, Number], default: null },
  sku: { type: [String, Number], default: null },
  avg: { type: [String, Number], default: null },
})

// Tính toán phần trăm Doanh thu
const totalMoney = computed(() => Number(props.cash || 0) + Number(props.transfer || 0))
const cashPercent = computed(() =>
  totalMoney.value ? ((Number(props.cash || 0) / totalMoney.value) * 100).toFixed(1) : 0,
)
const transferPercent = computed(() =>
  totalMoney.value ? ((Number(props.transfer || 0) / totalMoney.value) * 100).toFixed(1) : 0,
)

// Tính toán phần trăm Đơn hàng
const totalOrders = computed(() => Number(props.online || 0) + Number(props.pos || 0))
const onlinePercent = computed(() =>
  totalOrders.value ? ((Number(props.online || 0) / totalOrders.value) * 100).toFixed(1) : 0,
)
const posPercent = computed(() =>
  totalOrders.value ? ((Number(props.pos || 0) / totalOrders.value) * 100).toFixed(1) : 0,
)

// Tính toán phần trăm Khách hàng
const totalCustomers = computed(() => Number(props.member || 0) + Number(props.guest || 0))
const memberPercent = computed(() =>
  totalCustomers.value ? ((Number(props.member || 0) / totalCustomers.value) * 100).toFixed(1) : 0,
)
const guestPercent = computed(() =>
  totalCustomers.value ? ((Number(props.guest || 0) / totalCustomers.value) * 100).toFixed(1) : 0,
)

const money = (v) => Number(v || 0).toLocaleString('vi-VN') + ' ₫'
</script>
