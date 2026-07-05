<template>
  <div class="bg-white rounded-[28px] border border-slate-200 shadow-sm p-6 space-y-6">
    <!-- Top -->
    <div class="flex flex-col xl:flex-row xl:items-center xl:justify-between gap-5">
      <div>
        <h2 class="text-xl font-black text-slate-800">Bộ lọc thống kê</h2>

        <p class="text-slate-500 mt-1">
          Lọc dữ liệu theo thời gian, danh mục và trạng thái đơn hàng.
        </p>
      </div>

      <div class="flex flex-wrap gap-3">
        <button
          v-for="item in quickFilters"
          :key="item"
          @click="activeQuick = item"
          class="px-5 py-2.5 rounded-xl font-semibold transition-all duration-300"
          :class="
            activeQuick === item
              ? 'bg-slate-900 text-white shadow-lg'
              : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
          "
        >
          {{ item }}
        </button>
      </div>
    </div>

    <!-- Filter -->
    <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-5">
      <!-- From -->
      <div>
        <label class="block text-sm font-bold text-slate-600 mb-2"> Từ ngày </label>

        <input
          type="date"
          v-model="filters.fromDate"
          class="w-full rounded-2xl border border-slate-200 px-4 py-3 focus:outline-none focus:ring-2 focus:ring-indigo-500"
        />
      </div>

      <!-- To -->
      <div>
        <label class="block text-sm font-bold text-slate-600 mb-2"> Đến ngày </label>

        <input
          type="date"
          v-model="filters.toDate"
          class="w-full rounded-2xl border border-slate-200 px-4 py-3 focus:outline-none focus:ring-2 focus:ring-indigo-500"
        />
      </div>

      <!-- Category -->
      <div>
        <label class="block text-sm font-bold text-slate-600 mb-2"> Danh mục </label>

        <select
          v-model="filters.category"
          class="w-full rounded-2xl border border-slate-200 px-4 py-3 focus:outline-none focus:ring-2 focus:ring-indigo-500"
        >
          <option>Tất cả</option>
          <option>Áo</option>
          <option>Quần</option>
          <option>Giày</option>
          <option>Phụ kiện</option>
        </select>
      </div>

      <!-- Brand -->
      <div>
        <label class="block text-sm font-bold text-slate-600 mb-2"> Thương hiệu </label>

        <select
          v-model="filters.brand"
          class="w-full rounded-2xl border border-slate-200 px-4 py-3 focus:outline-none focus:ring-2 focus:ring-indigo-500"
        >
          <option>Tất cả</option>
          <option>Nike</option>
          <option>Adidas</option>
          <option>Puma</option>
          <option>MLB</option>
        </select>
      </div>

      <!-- Status -->
      <div>
        <label class="block text-sm font-bold text-slate-600 mb-2"> Trạng thái </label>

        <select
          v-model="filters.status"
          class="w-full rounded-2xl border border-slate-200 px-4 py-3 focus:outline-none focus:ring-2 focus:ring-indigo-500"
        >
          <option>Tất cả</option>
          <option>Đã giao</option>
          <option>Đang giao</option>
          <option>Chờ xác nhận</option>
          <option>Đã huỷ</option>
        </select>
      </div>

      <!-- Payment -->
      <div>
        <label class="block text-sm font-bold text-slate-600 mb-2"> Thanh toán </label>

        <select
          v-model="filters.payment"
          class="w-full rounded-2xl border border-slate-200 px-4 py-3 focus:outline-none focus:ring-2 focus:ring-indigo-500"
        >
          <option>Tất cả</option>
          <option>COD</option>
          <option>VNPay</option>
          <option>Momo</option>
        </select>
      </div>

      <!-- Search -->
      <div class="md:col-span-2">
        <label class="block text-sm font-bold text-slate-600 mb-2"> Tìm kiếm </label>

        <input
          v-model="filters.keyword"
          type="text"
          placeholder="Tên sản phẩm, mã đơn..."
          class="w-full rounded-2xl border border-slate-200 px-4 py-3 focus:outline-none focus:ring-2 focus:ring-indigo-500"
        />
      </div>
    </div>

    <!-- Bottom -->
    <div class="flex flex-col md:flex-row md:justify-between md:items-center gap-4 pt-2">
      <div class="text-sm text-slate-500">Hiển thị thống kê theo bộ lọc đã chọn.</div>

      <div class="flex flex-wrap gap-3">
        <button
          @click="resetFilter"
          class="px-5 py-3 rounded-xl border border-slate-200 font-semibold hover:bg-slate-100 transition"
        >
          Đặt lại
        </button>

        <button
          class="px-5 py-3 rounded-xl bg-emerald-600 text-white font-semibold hover:bg-emerald-700 transition"
        >
          Xuất Excel
        </button>

        <button
          class="px-5 py-3 rounded-xl bg-red-500 text-white font-semibold hover:bg-red-600 transition"
        >
          Xuất PDF
        </button>

        <button
          class="px-6 py-3 rounded-xl bg-slate-900 text-white font-bold hover:scale-105 transition"
        >
          Áp dụng
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'

const quickFilters = ['Hôm nay', '7 ngày', '30 ngày', 'Tháng này', 'Năm nay']

const activeQuick = ref('30 ngày')

const filters = reactive({
  fromDate: '',
  toDate: '',
  category: 'Tất cả',
  brand: 'Tất cả',
  status: 'Tất cả',
  payment: 'Tất cả',
  keyword: '',
})

const resetFilter = () => {
  activeQuick.value = '30 ngày'

  filters.fromDate = ''
  filters.toDate = ''
  filters.category = 'Tất cả'
  filters.brand = 'Tất cả'
  filters.status = 'Tất cả'
  filters.payment = 'Tất cả'
  filters.keyword = ''
}
</script>
