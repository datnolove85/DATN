<template>
  <div
    class="space-y-6 max-w-full mx-auto p-4 animate-fade-in bg-slate-50 text-slate-800 rounded-2xl min-h-screen selection:bg-indigo-100 selection:text-indigo-900 overflow-hidden"
  >
    <div
      class="relative p-8 bg-[#0b0f19] rounded-3xl text-white flex flex-col md:flex-row justify-between items-start md:items-center gap-6 shadow-sm overflow-hidden"
    >
      <div class="relative z-10">
        <span class="text-[10px] tracking-widest uppercase font-bold text-indigo-400"
          >Hệ thống vận hành giao dịch</span
        >
        <h1 class="text-2xl font-bold tracking-tight mt-1 text-white">Quản Lý Hóa Đơn</h1>
        <p class="text-xs text-slate-400 mt-1 flex items-center gap-2">
          <span class="relative flex h-2 w-2">
            <span
              class="animate-ping absolute inline-flex h-full w-full rounded-full bg-indigo-400 opacity-75"
            ></span>
            <span class="relative inline-block rounded-full h-2 w-2 bg-indigo-500"></span>
          </span>
          Truy vết luồng tiền, trạng thái đơn hàng và kênh phân phối thời gian thực.
        </p>
      </div>

      <button
        class="relative z-10 flex items-center justify-center gap-2 px-5 py-3 bg-white/5 border border-white/10 hover:bg-white/10 text-white rounded-2xl text-xs font-bold transition-all active:scale-95 whitespace-nowrap shadow-sm"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          stroke-width="2.5"
          stroke="currentColor"
          class="w-4 h-4 text-indigo-400"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            d="M19.5 14.25v-2.625a3.375 3.375 0 0 0-3.375-3.375h-1.5A1.125 1.125 0 0 1 13.5 7.125v-1.5a3.375 3.375 0 0 0-3.375-3.375H8.25m6.75 12-3-3m0 0-3 3m3-3v6m-1.5-15H5.625c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125V11.25a9 9 0 0 0-9-9Z"
          />
        </svg>
        XUẤT BÁO CÁO EXCEL
      </button>
    </div>

    <div class="bg-white p-6 rounded-2xl shadow-sm border border-slate-100">
      <div class="flex items-center justify-between mb-5 select-none">
        <h3
          class="text-xs font-extrabold uppercase tracking-widest text-indigo-600 flex items-center gap-2"
        >
          <span class="inline-block w-1.5 h-3.5 bg-indigo-600 rounded-sm"></span>
          Bộ lọc dữ liệu tối ưu
        </h3>
        <span
          class="text-[11px] font-bold text-slate-400 bg-slate-50 px-2.5 py-1 rounded-lg border border-slate-100"
        >
          Bộ lọc: Tự động áp dụng
        </span>
      </div>

      <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4">
        <div class="sm:col-span-2 flex gap-2">
          <div
            class="relative flex items-center bg-slate-50 border border-slate-200 focus-within:border-indigo-500 focus-within:bg-white rounded-xl px-3 py-1 w-full transition-all group"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="2.5"
              stroke="currentColor"
              class="w-4 h-4 text-slate-400 mr-2 group-focus-within:text-indigo-500"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.602 10.602Z"
              />
            </svg>
            <input
              v-model="filters.keyword"
              type="text"
              placeholder="Tìm mã đơn, khách hàng, số điện thoại..."
              class="bg-transparent text-slate-800 placeholder-slate-400 text-xs font-semibold outline-none w-full"
            />
          </div>

          <button
            @click="resetFilter"
            class="px-3.5 py-2.5 bg-slate-50 hover:bg-rose-50 border border-slate-200 hover:border-rose-200 text-slate-500 hover:text-rose-600 rounded-xl text-xs font-bold transition-all active:scale-95 flex items-center justify-center"
            title="Đặt lại bộ lọc"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="2.5"
              stroke="currentColor"
              class="w-4 h-4"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="M16.023 9.348h4.992v-.001M2.985 19.644v-4.992m0 0h4.992m-4.993 0 3.181 3.183a8.25 8.25 0 0 0 13.803-3.7M4.031 9.865a8.25 8.25 0 0 1 13.803-3.7l3.181 3.182m0-4.991v4.99"
              />
            </svg>
          </button>
        </div>

        <div class="flex flex-col gap-1">
          <input
            v-model="filters.fromDate"
            type="date"
            class="w-full bg-slate-50 border border-slate-200 focus:border-indigo-500 focus:bg-white rounded-xl px-4 py-2.5 text-xs font-semibold text-slate-700 outline-none transition-all"
          />
        </div>

        <div class="flex flex-col gap-1">
          <input
            v-model="filters.toDate"
            type="date"
            class="w-full bg-slate-50 border border-slate-200 focus:border-indigo-500 focus:bg-white rounded-xl px-4 py-2.5 text-xs font-semibold text-slate-700 outline-none transition-all"
          />
        </div>

        <select
          v-model="filters.trangThai"
          class="w-full bg-slate-50 border border-slate-200 focus:border-indigo-500 focus:bg-white rounded-xl px-4 py-2.5 text-xs font-bold text-slate-600 outline-none transition-all cursor-pointer appearance-none"
        >
          <option value="">Tất cả trạng thái</option>
          <option value="cho_xac_nhan">Chờ xác nhận</option>
          <option value="dang_giao">Đang giao</option>
          <option value="da_giao">Đã giao</option>
          <option value="da_huy">Đã hủy</option>
        </select>

        <select
          v-model="filters.loaiHoaDon"
          class="w-full bg-slate-50 border border-slate-200 focus:border-indigo-500 focus:bg-white rounded-xl px-4 py-2.5 text-xs font-bold text-slate-600 outline-none transition-all cursor-pointer appearance-none"
        >
          <option value="">Phân loại đơn</option>
          <option value="online">Hóa đơn Online</option>
          <option value="tai_quay">Mua tại quầy</option>
        </select>

        <div class="flex gap-2 sm:col-span-2">
          <input
            v-model="filters.minPrice"
            type="number"
            placeholder="Khoảng giá từ (đ)"
            class="w-1/2 bg-slate-50 border border-slate-200 focus:border-indigo-500 focus:bg-white rounded-xl px-4 py-2.5 text-xs font-semibold text-slate-700 outline-none transition-all"
          />
          <input
            v-model="filters.maxPrice"
            type="number"
            placeholder="đến giá (đ)"
            class="w-1/2 bg-slate-50 border border-slate-200 focus:border-indigo-500 focus:bg-white rounded-xl px-4 py-2.5 text-xs font-semibold text-slate-700 outline-none transition-all"
          />
        </div>
      </div>
    </div>

    <div class="bg-white rounded-2xl border border-slate-100 shadow-sm overflow-hidden">
      <div
        class="hidden md:grid grid-cols-12 gap-0 px-6 py-4 bg-slate-50 border-b border-slate-100 text-[11px] uppercase text-slate-400 font-bold tracking-wider select-none"
      >
        <div class="col-span-2">Mã hóa đơn</div>
        <div class="col-span-3">Khách hàng hồ sơ</div>
        <div class="col-span-2">Thời gian tạo</div>
        <div class="col-span-2">Tổng thành tiền</div>
        <div class="col-span-2 text-center">Trạng thái xử lý</div>
        <div class="col-span-1 text-center">Hành động</div>
      </div>

      <div class="divide-y divide-slate-100 text-xs">
        <div
          v-for="invoice in invoices"
          :key="invoice.id"
          @dblclick="goToDetail(invoice.id)"
          :class="[
            'relative transition-all duration-300 group',
            expandedIds.includes(invoice.id) ? 'bg-indigo-50/40' : 'hover:bg-slate-50/60',
          ]"
        >
          <div
            :class="[
              'absolute left-0 top-0 bottom-0 w-1.5 bg-indigo-600 transition-all duration-300',
              expandedIds.includes(invoice.id) ? 'opacity-100' : 'opacity-0',
            ]"
          ></div>

          <div class="grid grid-cols-1 md:grid-cols-12 gap-4 px-6 py-4 items-center">
            <div
              class="col-span-2 font-bold text-indigo-600 font-mono text-sm tracking-wide group-hover:translate-x-1 transition-transform duration-300"
            >
              {{ invoice.code }}
            </div>

            <div class="col-span-3">
              <div class="font-bold text-slate-800 text-sm capitalize">
                {{ invoice.customer || 'Khách vãng lai' }}
              </div>
              <div class="text-[11px] text-slate-400 font-mono mt-0.5">
                {{ invoice.phone || 'N/A' }}
              </div>
            </div>

            <div class="col-span-2 font-medium text-slate-500 font-mono">
              {{ invoice.created }}
            </div>

            <div class="col-span-2 font-extrabold text-slate-900 font-mono text-sm">
              {{ formatMoney(invoice.final) }}
            </div>

            <div class="col-span-2 flex justify-start md:justify-center">
              <span :class="statusClassModern(invoice.status)">
                <span
                  :class="[
                    'w-1.5 h-1.5 rounded-full mr-2 transition-all',
                    statusDot(invoice.status),
                    invoice.status !== 'da_huy' ? 'animate-pulse' : '',
                  ]"
                ></span>
                {{
                  invoice.status === 'da_giao'
                    ? 'Đã giao'
                    : invoice.status === 'dang_giao'
                      ? 'Đang giao'
                      : invoice.status === 'cho_xac_nhan'
                        ? 'Chờ xác nhận'
                        : invoice.status === 'da_huy'
                          ? 'Đã hủy'
                          : invoice.status
                }}
              </span>
            </div>

            <div class="col-span-1 flex justify-start md:justify-center">
              <button
                @click="toggleDetails(invoice.id)"
                :class="[
                  'p-2 rounded-xl transition-all duration-300 border',
                  expandedIds.includes(invoice.id)
                    ? 'bg-indigo-600 border-indigo-600 text-white rotate-180 shadow-md shadow-indigo-600/10'
                    : 'bg-slate-50 border-slate-200 text-indigo-600 hover:bg-indigo-50 hover:border-indigo-200',
                ]"
                title="Xem chi tiết nhanh"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  class="w-4 h-4"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2.5"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                >
                  <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" />
                  <circle cx="12" cy="12" r="3" />
                </svg>
              </button>
            </div>
          </div>

          <div
            v-if="expandedIds.includes(invoice.id)"
            class="px-6 pb-6 pt-2 border-t border-indigo-100 bg-indigo-50/10 animate-slide-down"
          >
            <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 py-4 text-xs">
              <div class="bg-white p-3.5 rounded-xl border border-indigo-100/40 shadow-sm">
                <p
                  class="font-extrabold uppercase text-[10px] mb-2 tracking-wider text-indigo-950 flex items-center gap-1.5"
                >
                  🚚 Thông tin nhận hàng
                </p>
                <p class="text-slate-600 leading-relaxed">
                  Địa chỉ thường trú:
                  <span class="font-bold text-slate-900 block mt-0.5">{{
                    invoice.address || 'Nhận tại quầy bán hàng'
                  }}</span>
                </p>
              </div>

              <div class="bg-white p-3.5 rounded-xl border border-indigo-100/40 shadow-sm">
                <p
                  class="font-extrabold uppercase text-[10px] mb-2 tracking-wider text-indigo-950 flex items-center gap-1.5"
                >
                  💳 Phương thức chi trả
                </p>
                <p class="text-slate-600">
                  Hình thức:
                  <span
                    class="font-bold text-slate-900 mt-0.5 mb-1 bg-slate-50 inline-block px-2 py-0.5 rounded-md border border-slate-200"
                    >{{ invoice.paymentMethod || 'Tiền mặt' }}</span
                  >
                </p>
                <p class="text-slate-500 mt-1">
                  Phí vận chuyển:
                  <span class="font-bold font-mono text-slate-800">{{
                    formatMoney(invoice.shippingFee)
                  }}</span>
                </p>
              </div>

              <div class="bg-white p-3.5 rounded-xl border border-indigo-100/40 shadow-sm">
                <p
                  class="font-extrabold uppercase text-[10px] mb-2 tracking-wider text-indigo-950 flex items-center gap-1.5"
                >
                  📊 Chi tiết cấu trúc giá
                </p>
                <p class="text-slate-500 flex justify-between items-center mb-1">
                  <span>Tiền hàng gốc:</span>
                  <span class="font-bold font-mono text-slate-700">{{
                    formatMoney(invoice.totalOriginal)
                  }}</span>
                </p>
                <p class="text-slate-500 flex justify-between items-center">
                  <span>Khấu trừ voucher:</span>
                  <span class="font-bold font-mono text-rose-600"
                    >-{{ formatMoney(invoice.discount) }}</span
                  >
                </p>
              </div>

              <div class="bg-white p-3.5 rounded-xl border border-indigo-100/40 shadow-sm">
                <p
                  class="font-extrabold uppercase text-[10px] mb-2 tracking-wider text-indigo-950 flex items-center gap-1.5"
                >
                  📝 Ghi chú vận hành
                </p>
                <p
                  class="text-slate-500 italic bg-slate-50 p-2 rounded-lg border border-slate-200 min-h-[50px] leading-relaxed"
                >
                  {{ invoice.note || 'Không có ghi chú lưu ý đặc biệt nào.' }}
                </p>
              </div>
            </div>

            <div class="flex justify-end pt-2">
              <button
                @click="goToDetail(invoice.id)"
                class="flex items-center gap-1.5 px-4 py-1.5 bg-indigo-600 hover:bg-indigo-500 text-white font-bold text-[11px] rounded-xl tracking-wide transition-colors shadow-sm"
              >
                QUẢN TRỊ CHI TIẾT ĐƠN ➔
              </button>
            </div>
          </div>
        </div>

        <tr v-if="invoices.length === 0">
          <div
            class="py-20 text-center flex flex-col items-center justify-center gap-2 text-slate-400 select-none"
          >
            <div
              class="w-12 h-12 rounded-2xl bg-slate-50 text-indigo-600 border border-slate-100 flex items-center justify-center text-lg font-black"
            >
              📦
            </div>
            <strong class="text-sm font-bold text-slate-800 mt-1">Hệ thống trống dữ liệu</strong>
            <span class="text-xs"
              >Không tìm thấy bất kỳ hóa đơn nào tương thích với điều kiện bộ lọc của bạn.</span
            >
          </div>
        </tr>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { getAllHoadon } from '@/service/HoaDonService'
import { useRouter } from 'vue-router'

const router = useRouter()
const rawData = ref([])
const invoices = ref([])
const expandedIds = ref([])

const filters = ref({
  keyword: '',
  fromDate: '',
  toDate: '',
  trangThai: '',
  loaiHoaDon: '',
  minPrice: '',
  maxPrice: '',
})

const goToDetail = (id) => {
  router.push({ name: 'HoaDonChiTiet', params: { id: id } })
}

const toggleDetails = (id) => {
  const index = expandedIds.value.indexOf(id)
  index > -1 ? expandedIds.value.splice(index, 1) : expandedIds.value.push(id)
}

const statusDot = (status) => {
  const dots = {
    da_giao: 'bg-emerald-500',
    dang_giao: 'bg-blue-500',
    cho_xac_nhan: 'bg-amber-500',
    da_huy: 'bg-rose-500',
  }
  return dots[status] || 'bg-slate-500'
}

const statusClassModern = (status) => {
  const base =
    'inline-flex items-center px-2.5 py-1 rounded-xl text-[10px] font-bold uppercase tracking-wider border select-none min-w-[110px] justify-center'

  switch (status) {
    case 'da_giao':
      return `${base} bg-emerald-50 text-emerald-700 border-emerald-200`
    case 'dang_giao':
      return `${base} bg-blue-50 text-blue-700 border-blue-200`
    case 'cho_xac_nhan':
      return `${base} bg-amber-50 text-amber-700 border-amber-200`
    case 'da_huy':
      return `${base} bg-rose-50 text-rose-700 border-rose-200`
    default:
      return `${base} bg-slate-50 text-slate-700 border-slate-200`
  }
}

const fetchInvoices = async () => {
  const res = await getAllHoadon()

  rawData.value = res.map((item) => ({
    id: item.id,
    code: item.maHoaDon,
    customer: item.tenNguoiNhan,
    phone: item.soDienThoaiNguoiNhan,
    createdRaw: item.ngayTao ? new Date(item.ngayTao) : null,
    created: item.ngayTao ? new Date(item.ngayTao).toLocaleDateString('vi-VN') : '',
    final: item.tongThanhToan,
    status: item.trangThai,
    type: item.loaiHoaDon,
    address: item.diaChiGiaoHang,
    paymentMethod: item.phuongThucThanhToan,
    note: item.ghiChu,
    shippingFee: item.phiVanChuyen,
    totalOriginal: item.tongTienHang,
    discount: item.tongGiamGia,
  }))

  applyFilter()
}

const applyFilter = () => {
  let data = [...rawData.value]

  if (filters.value.keyword) {
    const k = filters.value.keyword.toLowerCase()
    data = data.filter(
      (x) =>
        x.code?.toLowerCase().includes(k) ||
        x.customer?.toLowerCase().includes(k) ||
        x.phone?.includes(k),
    )
  }

  if (filters.value.trangThai) data = data.filter((x) => x.status === filters.value.trangThai)
  if (filters.value.loaiHoaDon) data = data.filter((x) => x.type === filters.value.loaiHoaDon)
  if (filters.value.minPrice) data = data.filter((x) => x.final >= Number(filters.value.minPrice))
  if (filters.value.maxPrice) data = data.filter((x) => x.final <= Number(filters.value.maxPrice))

  if (filters.value.fromDate) {
    const from = new Date(filters.value.fromDate)
    data = data.filter((x) => x.createdRaw && x.createdRaw >= from)
  }

  if (filters.value.toDate) {
    const to = new Date(filters.value.toDate)
    to.setHours(23, 59, 59, 999)
    data = data.filter((x) => x.createdRaw && x.createdRaw <= to)
  }

  invoices.value = data
}

watch(filters, applyFilter, { deep: true })

const resetFilter = () => {
  filters.value = {
    keyword: '',
    fromDate: '',
    toDate: '',
    trangThai: '',
    loaiHoaDon: '',
    minPrice: '',
    maxPrice: '',
  }
  applyFilter()
}

const formatMoney = (v) => Number(v || 0).toLocaleString('vi-VN') + ' đ'

onMounted(fetchInvoices)
</script>

<style scoped>
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(8px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-8px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animate-fade-in {
  animation: fadeIn 0.35s ease-out forwards;
}

.animate-slide-down {
  animation: slideDown 0.3s cubic-bezier(0.16, 1, 0.3, 1) forwards;
}
</style>
