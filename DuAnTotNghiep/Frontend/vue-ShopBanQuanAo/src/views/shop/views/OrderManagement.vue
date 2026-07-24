<template>
  <div
    class="space-y-6 max-w-screen-2xl mx-auto px-4 sm:px-6 lg:px-10 py-6 bg-slate-50/50 min-h-screen text-slate-800 antialiased"
  >
    <!-- HEADER BANNER -->
    <div
      class="relative p-6 sm:p-8 bg-gradient-to-r from-slate-900 via-indigo-950 to-slate-900 rounded-3xl text-white shadow-xl shadow-slate-900/10 overflow-hidden border border-slate-800"
    >
      <div
        class="absolute -right-10 -bottom-10 w-60 h-60 bg-indigo-500/10 rounded-full blur-3xl pointer-events-none"
      ></div>
      <div class="relative z-10 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <div
            class="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-indigo-500/10 border border-indigo-400/20 text-indigo-300 text-[11px] font-semibold tracking-wide uppercase mb-2"
          >
            <span class="relative flex h-2 w-2">
              <span
                class="animate-ping absolute inline-flex h-full w-full rounded-full bg-indigo-400 opacity-75"
              ></span>
              <span class="relative inline-block rounded-full h-2 w-2 bg-indigo-400"></span>
            </span>
            Hệ thống vận hành
          </div>
          <h1 class="text-2xl sm:text-3xl font-extrabold tracking-tight text-white">
            Quản lý đơn hàng Online
          </h1>
          <p class="text-xs sm:text-sm text-slate-400 mt-1">
            Theo dõi, kiểm soát và xử lý đơn hàng thời gian thực.
          </p>
        </div>
      </div>
    </div>

    <!-- MAIN CARD CONTAINER -->
    <div class="bg-white rounded-3xl border border-slate-200/80 shadow-sm overflow-hidden">
      <!-- TOP STATUS TABS -->
      <div class="px-6 pt-5 pb-3 border-b border-slate-100 bg-slate-50/30">
        <div class="flex items-center gap-2 overflow-x-auto pb-2 scrollbar-none">
          <button
            v-for="tab in tabs"
            :key="tab.key"
            @click="filters.trangThai = tab.key"
            :class="[
              'px-4 py-2.5 text-xs font-bold rounded-xl whitespace-nowrap transition-all duration-200 flex items-center gap-2 cursor-pointer',
              filters.trangThai === tab.key
                ? 'bg-indigo-600 text-white shadow-md shadow-indigo-200'
                : 'bg-white text-slate-600 hover:bg-slate-100 hover:text-slate-900 border border-slate-200/80',
            ]"
          >
            <span>{{ tab.label }}</span>
          </button>
        </div>
      </div>

      <!-- FILTER BAR -->
      <div class="p-5 border-b border-slate-100 bg-white">
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-3">
          <!-- Search Input -->
          <div
            class="relative flex items-center bg-slate-50 border border-slate-200 focus-within:border-indigo-500 focus-within:ring-2 focus-within:ring-indigo-500/10 rounded-xl px-3 py-2 transition-all"
          >
            <svg
              class="w-4 h-4 text-slate-400 mr-2 shrink-0"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M21 21l-6-6m2-5a7 7 0 10-14 0 7 7 0 0014 0z"
              />
            </svg>
            <input
              v-model="filters.keyword"
              type="text"
              placeholder="Mã đơn / Tên / SĐT..."
              class="bg-transparent text-slate-800 placeholder-slate-400 text-xs font-medium outline-none w-full"
            />
          </div>

          <!-- Thanh toán Dropdown -->
          <div class="relative">
            <select
              v-model="filters.thanhToan"
              class="w-full bg-slate-50 border border-slate-200 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-500/10 rounded-xl px-3.5 py-2.5 text-xs font-semibold text-slate-700 outline-none cursor-pointer appearance-none"
            >
              <option value="">Tất cả thanh toán</option>
              <option value="da_thanh_toan">Đã thanh toán</option>
              <option value="chua_thanh_toan">Chưa thanh toán</option>
            </select>
            <div
              class="absolute right-3 top-1/2 -translate-y-1/2 pointer-events-none text-slate-400"
            >
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M19 9l-7 7-7-7"
                />
              </svg>
            </div>
          </div>

          <!-- Thời gian Dropdown -->
          <div class="relative">
            <select
              v-model="filters.quickDate"
              class="w-full bg-slate-50 border border-slate-200 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-500/10 rounded-xl px-3.5 py-2.5 text-xs font-semibold text-slate-700 outline-none cursor-pointer appearance-none"
            >
              <option value="">Tất cả thời gian</option>
              <option value="today">Hôm nay</option>
              <option value="yesterday">Hôm qua</option>
              <option value="7days">7 ngày gần đây</option>
              <option value="30days">30 ngày gần đây</option>
            </select>
            <div
              class="absolute right-3 top-1/2 -translate-y-1/2 pointer-events-none text-slate-400"
            >
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M19 9l-7 7-7-7"
                />
              </svg>
            </div>
          </div>

          <!-- Reset Filter Button -->
          <button
            @click="resetFilters"
            class="inline-flex items-center justify-center gap-2 text-xs font-bold px-4 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-700 hover:bg-slate-50 active:bg-slate-100 transition shadow-sm cursor-pointer"
          >
            <svg
              class="w-3.5 h-3.5 text-slate-500"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"
              />
            </svg>
            Reset bộ lọc
          </button>
        </div>
      </div>

      <!-- TABLE AREA -->
      <div class="overflow-x-auto min-h-[400px]">
        <table class="w-full text-left border-collapse">
          <thead>
            <tr
              class="bg-slate-50/80 border-b border-slate-100 text-slate-400 uppercase text-[10px] font-extrabold tracking-wider"
            >
              <th class="py-3.5 px-6">Mã Đơn</th>
              <th class="py-3.5 px-6">Khách Hàng</th>
              <th class="py-3.5 px-6">Địa Chỉ Giao Hàng</th>
              <th class="py-3.5 px-6">Thành Tiền</th>
              <th class="py-3.5 px-6">Trạng Thái</th>
              <th class="py-3.5 px-6 text-center">Thao Tác</th>
            </tr>
          </thead>

          <tbody class="divide-y divide-slate-100 text-xs">
            <!-- Empty State -->
            <tr v-if="orders.length === 0">
              <td colspan="6" class="py-16 text-center">
                <div class="flex flex-col items-center justify-center gap-2">
                  <div
                    class="w-12 h-12 rounded-full bg-slate-100 flex items-center justify-center text-slate-400 mb-1"
                  >
                    <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path
                        stroke-linecap="round"
                        stroke-linejoin="round"
                        stroke-width="1.5"
                        d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4"
                      />
                    </svg>
                  </div>
                  <p class="text-slate-500 font-semibold text-sm">Không tìm thấy đơn hàng nào</p>
                  <p class="text-slate-400 text-xs">Thử thay đổi từ khóa hoặc bộ lọc tìm kiếm.</p>
                </div>
              </td>
            </tr>

            <!-- Table Rows -->
            <tr
              v-for="o in orders"
              :key="o.id"
              class="hover:bg-slate-50/80 transition-colors group"
            >
              <!-- Mã đơn -->
              <td class="py-4 px-6 font-bold text-indigo-600 font-mono">
                <span
                  class="px-2.5 py-1 rounded-lg bg-indigo-50 border border-indigo-100/80 text-indigo-700"
                >
                  #{{ o.maHoaDon }}
                </span>
              </td>

              <!-- Khách hàng -->
              <td class="py-4 px-6">
                <div class="flex items-center gap-3">
                  <div
                    class="w-8 h-8 rounded-full bg-slate-100 text-slate-600 font-bold flex items-center justify-center text-xs shrink-0 border border-slate-200"
                  >
                    {{ getInitials(o.tenNguoiNhan) }}
                  </div>
                  <div>
                    <div class="font-bold text-slate-800 text-xs">
                      {{ o.tenNguoiNhan || 'Khách vãng lai' }}
                    </div>
                    <div class="text-[11px] font-mono text-slate-400 mt-0.5">
                      {{ o.soDienThoaiNguoiNhan || 'N/A' }}
                    </div>
                  </div>
                </div>
              </td>

              <!-- Địa chỉ -->
              <td class="py-4 px-6 text-slate-600 max-w-xs">
                <p class="truncate text-xs" :title="o.diaChiGiaoHang">
                  {{ o.diaChiGiaoHang || '---' }}
                </p>
              </td>

              <!-- Thành tiền -->
              <td class="py-4 px-6 font-black text-slate-900 font-mono text-sm">
                {{ o.tongThanhToan?.toLocaleString('vi-VN') }}
                <span class="text-xs font-normal text-slate-500 ml-0.5">đ</span>
              </td>

              <!-- Trạng thái -->
              <td class="py-4 px-6">
                <div class="flex flex-col gap-1.5 items-start">
                  <!-- Trạng thái đơn -->
                  <span
                    class="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-[10px] font-bold border"
                    :class="statusClass(o.trangThai)"
                  >
                    <span class="w-1.5 h-1.5 rounded-full bg-current"></span>
                    {{ getStatusLabel(o.trangThai) }}
                  </span>

                  <!-- Trạng thái thanh toán -->
                  <span
                    class="inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-md text-[10px] font-semibold border"
                    :class="paymentClass(o.trangThaiThanhToan)"
                  >
                    {{ getPaymentLabel(o.trangThaiThanhToan) }}
                  </span>
                </div>
              </td>

              <!-- Thao tác -->
              <td class="py-4 px-6 text-center action-menu-container">
                <div class="relative inline-block text-left">
                  <button
                    @click.stop="toggleMenu(o)"
                    class="px-3.5 py-1.5 rounded-xl bg-slate-900 hover:bg-indigo-600 text-white text-xs font-bold transition-all shadow-xs inline-flex items-center gap-1 cursor-pointer"
                  >
                    Thao tác
                    <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path
                        stroke-linecap="round"
                        stroke-linejoin="round"
                        stroke-width="2"
                        d="M19 9l-7 7-7-7"
                      />
                    </svg>
                  </button>

                  <!-- Popover Menu -->
                  <div
                    v-if="o.open"
                    class="absolute right-0 mt-2 w-52 bg-white border border-slate-200/90 rounded-2xl shadow-xl shadow-slate-200/50 overflow-hidden z-50 py-1.5 divide-y divide-slate-100 animate-in fade-in zoom-in-95 duration-100"
                  >
                    <div class="py-1">
                      <button
                        @click="openDetail(o.id)"
                        class="w-full text-left px-4 py-2 text-xs font-semibold text-slate-700 hover:bg-indigo-50 hover:text-indigo-600 flex items-center gap-2.5 transition"
                      >
                        <svg
                          class="w-4 h-4 text-slate-400"
                          fill="none"
                          stroke="currentColor"
                          viewBox="0 0 24 24"
                        >
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
                          />
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"
                          />
                        </svg>
                        Xem chi tiết
                      </button>
                    </div>

                    <div class="py-1">
                      <!-- 1. Xác nhận -->
                      <button
                        v-if="canChangeStatus(o.trangThai, 'da_xac_nhan')"
                        @click="handleAction(o, 'da_xac_nhan')"
                        class="w-full text-left px-4 py-2 text-xs font-semibold text-indigo-600 hover:bg-indigo-50 flex items-center gap-2.5 transition"
                      >
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M5 13l4 4L19 7"
                          />
                        </svg>
                        Xác nhận đơn
                      </button>

                      <!-- 2. Chuẩn bị hàng -->
                      <button
                        v-if="canChangeStatus(o.trangThai, 'cho_van_chuyen')"
                        @click="handleAction(o, 'cho_van_chuyen')"
                        class="w-full text-left px-4 py-2 text-xs font-semibold text-cyan-600 hover:bg-cyan-50 flex items-center gap-2.5 transition"
                      >
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"
                          />
                        </svg>
                        Chuẩn bị hàng
                      </button>

                      <!-- 3. Giao hàng -->
                      <button
                        v-if="canChangeStatus(o.trangThai, 'dang_giao')"
                        @click="handleAction(o, 'dang_giao')"
                        class="w-full text-left px-4 py-2 text-xs font-semibold text-blue-600 hover:bg-blue-50 flex items-center gap-2.5 transition"
                      >
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
                          />
                        </svg>
                        Giao hàng
                      </button>

                      <!-- 4. Giao thành công -->
                      <button
                        v-if="canChangeStatus(o.trangThai, 'giao_thanh_cong')"
                        @click="handleAction(o, 'giao_thanh_cong')"
                        class="w-full text-left px-4 py-2 text-xs font-semibold text-emerald-600 hover:bg-emerald-50 flex items-center gap-2.5 transition"
                      >
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
                          />
                        </svg>
                        Giao thành công
                      </button>

                      <!-- 5. Giao thất bại -->
                      <button
                        v-if="canChangeStatus(o.trangThai, 'giao_that_bai')"
                        @click="handleAction(o, 'giao_that_bai')"
                        class="w-full text-left px-4 py-2 text-xs font-semibold text-amber-600 hover:bg-amber-50 flex items-center gap-2.5 transition"
                      >
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"
                          />
                        </svg>
                        Giao thất bại
                      </button>

                      <!-- 6. Hoàn thành -->
                      <button
                        v-if="canChangeStatus(o.trangThai, 'hoan_thanh')"
                        @click="handleAction(o, 'hoan_thanh')"
                        class="w-full text-left px-4 py-2 text-xs font-semibold text-green-600 hover:bg-green-50 flex items-center gap-2.5 transition"
                      >
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M5 13l4 4L19 7"
                          />
                        </svg>
                        Hoàn thành
                      </button>
                    </div>

                    <!-- Hủy đơn -->
                    <div v-if="canChangeStatus(o.trangThai, 'da_huy')" class="py-1">
                      <button
                        @click="handleAction(o, 'da_huy')"
                        class="w-full text-left px-4 py-2 text-xs font-semibold text-rose-600 hover:bg-rose-50 flex items-center gap-2.5 transition"
                      >
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M6 18L18 6M6 6l12 12"
                          />
                        </svg>
                        Hủy đơn
                      </button>
                    </div>
                  </div>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- FOOTER PAGINATION -->
      <div
        class="px-6 py-4 bg-slate-50/50 border-t border-slate-100 flex flex-col sm:flex-row items-center justify-between gap-3"
        v-if="totalPages > 0"
      >
        <p class="text-xs text-slate-500 font-medium">
          Trang <span class="font-bold text-slate-800">{{ page + 1 }}</span> /
          <span class="font-bold text-slate-800">{{ totalPages }}</span>
        </p>

        <div class="flex items-center gap-1.5" v-if="totalPages > 1">
          <button
            class="px-3 py-1.5 rounded-xl bg-white border border-slate-200 text-xs font-bold text-slate-700 hover:bg-slate-50 active:bg-slate-100 disabled:opacity-40 disabled:hover:bg-white transition shadow-xs cursor-pointer"
            :disabled="page === 0"
            @click="goPage(page - 1)"
          >
            Trước
          </button>

          <button
            v-for="p in totalPages"
            :key="p"
            @click="goPage(p - 1)"
            class="w-8 h-8 rounded-xl border text-xs font-bold transition-all cursor-pointer flex items-center justify-center"
            :class="
              page === p - 1
                ? 'bg-indigo-600 text-white border-indigo-600 shadow-xs'
                : 'bg-white border-slate-200/80 text-slate-600 hover:bg-slate-50'
            "
          >
            {{ p }}
          </button>

          <button
            class="px-3 py-1.5 rounded-xl bg-white border border-slate-200 text-xs font-bold text-slate-700 hover:bg-slate-50 active:bg-slate-100 disabled:opacity-40 disabled:hover:bg-white transition shadow-xs cursor-pointer"
            :disabled="page >= totalPages - 1"
            @click="goPage(page + 1)"
          >
            Sau
          </button>
        </div>
      </div>
    </div>

    <!-- DRAWER DETAIL -->
    <OrderDetailDrawer v-if="showDrawer" :idHoaDon="selectedOrderId" @close="showDrawer = false" />
  </div>
</template>

<script setup>
import { ref, watch, onMounted, onUnmounted } from 'vue'
import OrderDetailDrawer from './OrderDetailDrawer.vue'
import { searchHoadonOnline, hoaDonService } from '@/service/HoaDonService'

// =======================
// STATE
// =======================
const orders = ref([])
const showDrawer = ref(false)
const selectedOrderId = ref(null)
const page = ref(0)
const size = ref(10)
const totalPages = ref(0)

const goPage = (p) => {
  if (p < 0 || p >= totalPages.value) return
  page.value = p
  fetchOrders()
}

// FILTER
const filters = ref({
  keyword: '',
  trangThai: '',
  thanhToan: '',
  fromDate: '',
  toDate: '',
  quickDate: '',
})

const resetFilters = () => {
  filters.value = {
    keyword: '',
    trangThai: '',
    thanhToan: '',
    fromDate: '',
    toDate: '',
    quickDate: '',
  }
  page.value = 0
  fetchOrders()
}

// TABS
const tabs = ref([
  { key: '', label: 'Tất cả' },
  { key: 'cho_xac_nhan', label: 'Chờ xác nhận' },
  { key: 'da_xac_nhan', label: 'Đã xác nhận' },
  { key: 'cho_van_chuyen', label: 'Chờ vận chuyển' },
  { key: 'dang_giao', label: 'Đang giao' },
  { key: 'giao_thanh_cong', label: 'Giao thành công' },
  { key: 'giao_that_bai', label: 'Giao thất bại' },
  { key: 'hoan_thanh', label: 'Hoàn thành' },
  { key: 'da_huy', label: 'Đã hủy' },
])

// =======================
// MAP DICTIONARIES
// =======================
const statusMap = {
  cho_xac_nhan: 'Chờ xác nhận',
  da_xac_nhan: 'Đã xác nhận',
  cho_van_chuyen: 'Chờ vận chuyển',
  dang_giao: 'Đang giao',
  giao_thanh_cong: 'Giao thành công',
  giao_that_bai: 'Giao thất bại',
  hoan_thanh: 'Hoàn thành',
  da_huy: 'Đã hủy',
}

const paymentMap = {
  da_thanh_toan: 'Đã thanh toán',
  chua_thanh_toan: 'Chưa thanh toán',
}

const getStatusLabel = (st) => statusMap[st] || st || '---'
const getPaymentLabel = (pm) => paymentMap[pm] || pm || 'Chưa thanh toán'

const getInitials = (name) => {
  if (!name) return 'K'
  const parts = name.trim().split(' ')
  return parts[parts.length - 1].charAt(0).toUpperCase()
}

// =======================
// LOAD DATA
// =======================
const fetchOrders = async () => {
  try {
    const res = await searchHoadonOnline(
      {
        loaiHoaDon: 'online',
        keyword: filters.value.keyword,
        trangThai: filters.value.trangThai,
        trangThaiThanhToan: filters.value.thanhToan,
        fromDate: filters.value.fromDate,
        toDate: filters.value.toDate,
      },
      page.value,
      size.value,
    )

    const list = res?.content || []
    orders.value = list.map((item) => ({
      ...item,
      trangThai: item.trangThai?.toLowerCase(),
      trangThaiThanhToan: item.trangThaiThanhToan?.toLowerCase(),
      open: false,
    }))
    totalPages.value = res?.totalPages || 0
  } catch (err) {
    console.error('Lỗi load hóa đơn:', err)
    orders.value = []
  }
}

// Chuyển đổi chọn nhanh thời gian thành từ ngày / đến ngày
watch(
  () => filters.value.quickDate,
  (value) => {
    const today = new Date()
    const format = (d) => d.toISOString().split('T')[0]

    if (!value) {
      filters.value.fromDate = ''
      filters.value.toDate = ''
      return
    }

    switch (value) {
      case 'today':
        filters.value.fromDate = format(today)
        filters.value.toDate = format(today)
        break

      case 'yesterday': {
        const d = new Date(today)
        d.setDate(d.getDate() - 1)
        filters.value.fromDate = format(d)
        filters.value.toDate = format(d)
        break
      }

      case '7days': {
        const d = new Date(today)
        d.setDate(d.getDate() - 6)
        filters.value.fromDate = format(d)
        filters.value.toDate = format(today)
        break
      }

      case '30days': {
        const d = new Date(today)
        d.setDate(d.getDate() - 29)
        filters.value.fromDate = format(d)
        filters.value.toDate = format(today)
        break
      }
    }
  },
)

// Gọi API tìm kiếm mỗi khi thay đổi bộ lọc thực tế (bao gồm cả fromDate và toDate)
watch(
  () => [
    filters.value.keyword,
    filters.value.trangThai,
    filters.value.thanhToan,
    filters.value.fromDate,
    filters.value.toDate,
  ],
  () => {
    page.value = 0
    fetchOrders()
  },
  { deep: true },
)

// =======================
// ACTIONS & TOGGLE
// =======================
const toggleMenu = (targetOrder) => {
  orders.value.forEach((item) => {
    if (item.id === targetOrder.id) {
      item.open = !item.open
    } else {
      item.open = false
    }
  })
}

// Bấm ra ngoài menu sẽ tự động đóng popover
const handleClickOutside = (event) => {
  if (!event.target.closest('.action-menu-container')) {
    orders.value.forEach((o) => (o.open = false))
  }
}

const openDetail = (id) => {
  selectedOrderId.value = id
  showDrawer.value = true
  orders.value.forEach((o) => (o.open = false))
}

// BADGE STYLES
const statusClass = (status) => {
  switch (status) {
    case 'cho_xac_nhan':
      return 'bg-amber-50 text-amber-700 border-amber-200/80'
    case 'da_xac_nhan':
      return 'bg-indigo-50 text-indigo-700 border-indigo-200/80'
    case 'cho_van_chuyen':
      return 'bg-cyan-50 text-cyan-700 border-cyan-200/80'
    case 'dang_giao':
      return 'bg-blue-50 text-blue-700 border-blue-200/80'
    case 'giao_thanh_cong':
      return 'bg-emerald-50 text-emerald-700 border-emerald-200/80'
    case 'giao_that_bai':
      return 'bg-rose-50 text-rose-700 border-rose-200/80'
    case 'hoan_thanh':
      return 'bg-emerald-50 text-emerald-800 border-emerald-300'
    case 'da_huy':
      return 'bg-slate-100 text-slate-600 border-slate-200'
    default:
      return 'bg-slate-50 text-slate-600 border-slate-200'
  }
}

const paymentClass = (status) => {
  switch (status) {
    case 'da_thanh_toan':
      return 'bg-emerald-50 text-emerald-700 border-emerald-200/60'
    case 'chua_thanh_toan':
      return 'bg-amber-50 text-amber-700 border-amber-200/60'
    default:
      return 'bg-slate-50 text-slate-600 border-slate-200/60'
  }
}

const canChangeStatus = (current, target) => {
  current = current?.toLowerCase()
  const map = {
    cho_xac_nhan: ['da_xac_nhan', 'da_huy'],
    da_xac_nhan: ['cho_van_chuyen', 'da_huy'],
    cho_van_chuyen: ['dang_giao', 'da_huy'],
    dang_giao: ['giao_thanh_cong', 'giao_that_bai'],
    giao_that_bai: ['dang_giao', 'da_huy'],
    giao_thanh_cong: ['hoan_thanh'],
    hoan_thanh: [],
    da_huy: [],
  }
  return map[current]?.includes(target)
}

const updateStatus = async (id, newStatus) => {
  try {
    await hoaDonService.updateTrangThai(id, newStatus)
    await fetchOrders()
  } catch (err) {
    console.error('Lỗi cập nhật trạng thái:', err)
  }
}

const handleAction = async (o, status) => {
  o.open = false
  await updateStatus(o.id, status)
}

onMounted(() => {
  fetchOrders()
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>
