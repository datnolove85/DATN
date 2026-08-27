<template>
  <div
    class="space-y-4 max-w-full mx-auto p-4 animate-fade-in bg-slate-50 text-slate-800 rounded-2xl min-h-screen selection:bg-indigo-100 selection:text-indigo-900 overflow-hidden"
  >
    <!-- Header đã được thu gọn -->
    <div
      class="relative px-4 py-3.5 bg-white rounded-xl border border-slate-200/80 flex flex-col md:flex-row justify-between items-start md:items-center gap-3 shadow-2xs"
    >
      <div>
        <span class="text-[10px] tracking-wide uppercase font-semibold text-indigo-600">
          Hồ sơ lưu trữ giao dịch
        </span>
        <h1 class="text-base font-bold tracking-tight text-slate-800">Chi Tiết Hóa Đơn</h1>
        <p class="text-[11px] text-slate-500 mt-0.5 flex items-center gap-1.5">
          <span>Mã hóa đơn:</span>
          <span
            class="font-mono font-bold text-indigo-600 bg-indigo-50 px-1.5 py-0.2 rounded border border-indigo-100"
          >
            {{ invoice.code || 'Đang tải...' }}
          </span>
        </p>
      </div>

      <div class="flex flex-wrap items-center gap-2 w-full md:w-auto justify-end">
        <div>
          <button
            @click="openPreview"
            :disabled="!rawInvoice || ['huy', 'da_huy', 'da_tra_hang'].includes(invoice.status)"
            :class="[
              'flex items-center justify-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-bold transition-all whitespace-nowrap',
              ['huy', 'da_huy', 'da_tra_hang'].includes(invoice.status)
                ? 'bg-slate-100 text-slate-500 border border-slate-200 cursor-not-allowed shadow-none'
                : 'bg-indigo-600 hover:bg-indigo-500 text-white shadow-xs active:scale-95',
            ]"
          >
            <svg
              v-if="!['huy', 'da_huy', 'da_tra_hang'].includes(invoice.status)"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="2.5"
              stroke="currentColor"
              class="w-3.5 h-3.5"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="M6.72 13.829c-.24.03-.48.062-.72.096m.72-.096a42.415 42.415 0 0 1 10.56 0m-10.56 0L6.34 18m10.94-4.171c.24.03.48.062.72.096m-.72-.096L17.66 18m0 0 .229 2.523a1.125 1.125 0 0 1-1.12 1.227H7.231c-.617 0-1.11-.51-1.07-1.122L6.34 18m11.32 0h-11.32M9 11V5.25A2.25 2.25 0 0 1 11.25 3h1.5A2.25 2.25 0 0 1 15 5.25V11m-6 0h6a2.25 2.25 0 0 1 2.25 2.25v1.875c0 .621-.504 1.125-1.125 1.125H6.875A1.125 1.125 0 0 1 5.625 16.25V13.25A2.25 2.25 0 0 1 7.875 11h.25Z"
              />
            </svg>
            <svg
              v-else
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="2.5"
              stroke="currentColor"
              class="w-3.5 h-3.5 text-slate-500"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="M16.5 10.5V6.75a4.5 4.5 0 1 0-9 0v3.75m-.75 11.25h10.5a2.25 2.25 0 0 0 2.25-2.25v-6.75a2.25 2.25 0 0 0-2.25-2.25H6.75a2.25 2.25 0 0 0-2.25 2.25v6.75a2.25 2.25 0 0 0 2.25 2.25Z"
              />
            </svg>
            {{
              ['huy', 'da_huy', 'da_tra_hang'].includes(invoice.status)
                ? 'ĐƠN ĐÃ HỦY'
                : 'XUẤT HÓA ĐƠN'
            }}
          </button>
          <InvoiceModal v-if="isPreviewOpen" :hoa-don="rawInvoice" @close="closePreview" />
        </div>

        <button
          @click="goBack"
          class="flex items-center justify-center gap-1.5 px-3 py-1.5 bg-white border border-slate-200 hover:bg-rose-50 hover:border-rose-200 hover:text-rose-600 text-slate-600 rounded-lg text-xs font-bold transition-all active:scale-95 whitespace-nowrap shadow-2xs"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke-width="2.5"
            stroke="currentColor"
            class="w-3.5 h-3.5"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              d="M9 15 3 9m0 0 6-6M3 9h12a6 6 0 0 1 0 12h-3"
            />
          </svg>
          QUAY LẠI
        </button>
      </div>
    </div>

    <!-- Main Content Container -->
    <div class="bg-white p-5 rounded-2xl shadow-xs border border-slate-200/80 space-y-6">
      <!-- Top 3 Info Cards -->
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div class="p-4 bg-slate-50/70 rounded-xl border border-slate-200/60">
          <p
            class="text-[11px] font-bold text-slate-400 uppercase tracking-wider mb-1.5 flex items-center gap-1.5"
          >
            👤 Hồ sơ đối tác
          </p>
          <p class="font-bold text-slate-800 text-sm capitalize truncate">
            {{ invoice.customer }}
          </p>
          <p class="text-xs text-slate-500 font-mono mt-0.5">
            {{ invoice.phone }}
          </p>
        </div>

        <div
          class="p-4 bg-slate-50/70 rounded-xl border border-slate-200/60 flex flex-col justify-center"
        >
          <p
            class="text-[11px] font-bold text-slate-400 uppercase tracking-wider mb-2 flex items-center gap-1.5"
          >
            ⚙️ Trạng thái vận hành
          </p>
          <span :class="statusClassModern(invoice.status)">
            <span
              class="w-1.5 h-1.5 rounded-full mr-2 transition-all bg-current animate-pulse"
            ></span>
            {{ formatStatusName(invoice.status) }}
          </span>
        </div>

        <div class="p-4 bg-slate-50/70 rounded-xl border border-slate-200/60">
          <p
            class="text-[11px] font-bold text-slate-400 uppercase tracking-wider mb-1.5 flex items-center gap-1.5"
          >
            📅 Thời gian đặt hàng
          </p>
          <p class="font-bold text-slate-800 text-sm font-mono">
            {{ invoice.createdAt }}
          </p>
          <p class="text-[11px] text-slate-400 mt-0.5">Ngày giờ khởi tạo hóa đơn</p>
        </div>
      </div>

      <!-- Timeline Section -->
      <div class="p-4 bg-slate-50/40 rounded-xl border border-slate-200/70">
        <div class="flex items-center justify-between mb-4 select-none">
          <h3
            class="text-xs font-extrabold uppercase tracking-wider text-indigo-600 flex items-center gap-2"
          >
            <span class="inline-block w-1.5 h-3 bg-indigo-600 rounded-sm"></span>
            Lịch sử tiến trình hóa đơn (Timeline)
          </h3>
          <span
            class="text-[11px] font-bold text-slate-500 font-mono bg-white px-2 py-0.5 rounded-md border border-slate-200"
          >
            Tổng bước: {{ invoice.history?.length || 0 }}
          </span>
        </div>

        <div
          v-if="invoice.history && invoice.history.length > 0"
          class="relative pl-5 space-y-4 before:absolute before:left-[9px] before:top-2 before:bottom-2 before:w-0.5 before:bg-slate-200"
        >
          <div
            v-for="hist in invoice.history"
            :key="hist.id"
            class="relative flex items-start gap-3 group"
          >
            <div
              class="absolute -left-5 top-1 w-5 h-5 rounded-full bg-white border-2 border-indigo-600 flex items-center justify-center shadow-xs"
            >
              <div class="w-1.5 h-1.5 rounded-full bg-indigo-600"></div>
            </div>

            <div class="flex-1 bg-white border border-slate-200/80 rounded-xl p-3.5 shadow-2xs">
              <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-2 mb-1.5">
                <div class="flex items-center gap-2 flex-wrap">
                  <span
                    v-if="hist.trangThaiCu"
                    class="px-2 py-0.5 bg-slate-100 text-slate-700 font-bold rounded-md text-[11px]"
                  >
                    {{ formatStatusName(hist.trangThaiCu) }}
                  </span>
                  <svg
                    v-if="hist.trangThaiCu"
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke-width="2.5"
                    stroke="currentColor"
                    class="w-3 h-3 text-slate-400"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      d="M13.5 4.5 21 12m0 0-7.5 7.5M21 12H3"
                    />
                  </svg>
                  <span
                    class="px-2 py-0.5 bg-indigo-600 text-white font-bold rounded-md text-[11px] shadow-2xs"
                  >
                    {{ hist.hienThiTrangThai || formatStatusName(hist.trangThaiMoi) }}
                  </span>
                </div>

                <span class="text-xs font-mono font-medium text-slate-500">
                  {{ formatDateTime(hist.thoiGian) }}
                </span>
              </div>

              <div
                class="flex flex-wrap items-center gap-2 text-xs text-slate-600 mt-2 pt-2 border-t border-slate-100"
              >
                <div class="flex items-center gap-1">
                  <span class="text-slate-400">Người thực hiện:</span>
                  <span class="font-bold text-slate-800">{{ hist.tenNhanVienHienThi }}</span>
                </div>
                <span class="text-slate-300">•</span>
                <div class="flex items-center gap-1">
                  <span class="text-slate-400">Nguồn:</span>
                  <span
                    :class="[
                      'px-1.5 py-0.2 rounded font-mono font-bold text-[10px] border uppercase',
                      getSourceBadgeClass(hist.nguonThaoTac),
                    ]"
                  >
                    {{ hist.nguonThaoTac }}
                  </span>
                </div>
              </div>

              <div
                v-if="hist.ghiChu"
                class="mt-2 text-xs text-slate-600 bg-slate-50 p-2 rounded-lg border border-slate-200/60 italic"
              >
                <span class="font-bold text-slate-700 not-italic mr-1">Ghi chú:</span>
                {{ hist.ghiChu }}
              </div>
            </div>
          </div>
        </div>

        <div v-else class="text-center py-4 text-slate-400 italic text-xs">
          Chưa có lịch sử thay đổi trạng thái cho hóa đơn này.
        </div>
      </div>

      <!-- Items Table -->
      <div>
        <div class="flex items-center justify-between mb-3 select-none">
          <h3
            class="text-xs font-extrabold uppercase tracking-wider text-indigo-600 flex items-center gap-2"
          >
            <span class="inline-block w-1.5 h-3 bg-indigo-600 rounded-sm"></span>
            Kiểm kê hạng mục sản phẩm mua
          </h3>
          <span
            class="text-[11px] font-bold text-slate-500 font-mono bg-slate-100 px-2 py-0.5 rounded-md border border-slate-200"
          >
            Tổng: {{ invoice.items?.length || 0 }} loại
          </span>
        </div>

        <div class="border border-slate-200/80 rounded-xl overflow-hidden shadow-2xs bg-white">
          <div class="overflow-x-auto">
            <table class="w-full text-xs text-left border-collapse min-w-[900px]">
              <thead
                class="bg-slate-50 text-slate-500 font-bold text-[11px] uppercase tracking-wider select-none border-b border-slate-200/80"
              >
                <tr>
                  <th class="px-5 py-3.5 text-left font-bold w-[16%]">Mã</th>
                  <th class="px-5 py-3.5 text-left font-bold w-[32%]">Tên sản phẩm</th>
                  <th class="px-5 py-3.5 text-left font-bold w-[22%]">Thông số phân loại</th>
                  <th class="px-5 py-3.5 text-center font-bold w-[8%]">Số lượng</th>
                  <th class="px-5 py-3.5 text-right font-bold w-[11%]">Đơn giá</th>
                  <th class="px-5 py-3.5 text-right font-bold w-[13%]">Thành tiền</th>
                </tr>
              </thead>
              <tbody class="divide-y divide-slate-100 font-medium">
                <tr
                  v-for="item in invoice.items"
                  :key="item.id"
                  class="hover:bg-indigo-50/20 transition-colors"
                >
                  <td class="px-5 py-4 font-mono font-bold text-indigo-600 break-words">
                    {{ item.sku }}
                  </td>
                  <td class="px-5 py-4">
                    <div
                      class="font-bold text-slate-800 text-sm leading-snug max-w-xs sm:max-w-md break-words"
                    >
                      {{ item.name }}
                    </div>
                  </td>
                  <td class="px-5 py-4 select-none">
                    <div class="flex flex-wrap gap-1.5">
                      <span
                        class="inline-flex items-center px-2 py-0.5 bg-slate-900 text-white font-bold rounded text-[9px] uppercase tracking-wider"
                      >
                        {{ item.brand }}
                      </span>
                      <span
                        class="inline-flex items-center px-2 py-0.5 bg-slate-100 text-slate-600 font-semibold rounded border border-slate-200 text-[10px]"
                      >
                        Màu: {{ item.color }}
                      </span>
                      <span
                        class="inline-flex items-center px-2 py-0.5 bg-indigo-50 text-indigo-600 font-semibold rounded border border-indigo-100 text-[10px]"
                      >
                        Size: {{ item.size }}
                      </span>
                    </div>
                  </td>
                  <td class="px-5 py-4 text-center text-slate-700 font-mono font-bold text-sm">
                    {{ item.quantity }}
                  </td>
                  <td class="px-5 py-4 text-right font-mono text-slate-500 font-semibold text-sm">
                    {{ formatMoney(item.price) }}
                  </td>
                  <td class="px-5 py-4 text-right font-extrabold text-slate-900 font-mono text-sm">
                    {{ formatMoney(item.total) }}
                  </td>
                </tr>
                <tr v-if="!invoice.items || invoice.items.length === 0">
                  <td colspan="6" class="text-center py-10 text-slate-400 italic">
                    Không tìm thấy dữ liệu cấu trúc sản phẩm của hóa đơn này.
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- Bottom Details & Financial Summary -->
      <div
        class="grid grid-cols-1 md:grid-cols-2 gap-6 pt-4 border-t border-slate-100 items-stretch"
      >
        <!-- Left Column: 3 Info Blocks -->
        <div class="flex flex-col justify-between space-y-3 text-xs">
          <div
            class="bg-slate-50/60 p-3.5 rounded-xl border border-slate-200/60 flex-1 flex flex-col justify-center"
          >
            <p
              class="font-extrabold uppercase text-[10px] mb-1 tracking-wider text-slate-600 flex items-center gap-1.5"
            >
              🚚 Địa điểm phân phối
            </p>
            <p class="text-slate-700 leading-relaxed font-medium mt-0.5">
              {{ invoice.address }}
            </p>
          </div>

          <div
            class="bg-slate-50/60 p-3.5 rounded-xl border border-slate-200/60 flex-1 flex flex-col justify-center"
          >
            <p
              class="font-extrabold uppercase text-[10px] mb-1 tracking-wider text-slate-600 flex items-center gap-1.5"
            >
              💳 Kênh xử lý dòng tiền
            </p>
            <p class="text-slate-600 mt-1">
              Phương thức:
              <span
                class="font-bold text-indigo-700 bg-indigo-50 border border-indigo-200 px-2 py-0.5 rounded-md inline-block ml-1"
              >
                {{ invoice.paymentMethod }}
              </span>
            </p>
          </div>

          <div
            class="bg-slate-50/60 p-3.5 rounded-xl border border-slate-200/60 flex-1 flex flex-col justify-center"
          >
            <p
              class="font-extrabold uppercase text-[10px] mb-1 tracking-wider text-slate-600 flex items-center gap-1.5"
            >
              📝 Biên bản ghi chú
            </p>
            <p
              class="text-slate-500 italic bg-white p-2.5 rounded-lg border border-slate-200/80 leading-relaxed mt-1"
            >
              {{ invoice.note || 'Không có yêu cầu hay ghi chú đặc biệt nào kèm theo đơn hàng.' }}
            </p>
          </div>
        </div>

        <!-- Right Column: Financial Summary Box -->
        <div class="flex flex-col">
          <div
            class="w-full h-full flex flex-col justify-between p-5 bg-slate-50/80 rounded-xl border border-slate-200/70 shadow-2xs text-xs"
          >
            <div>
              <p
                class="font-extrabold uppercase text-[10px] mb-3 tracking-wider text-slate-400 select-none"
              >
                📊 Tổng quyết toán dòng tiền
              </p>

              <div class="space-y-2">
                <!-- 1. Hàng gốc -->
                <div
                  class="flex justify-between items-center text-slate-600 px-3 py-1.5 rounded-lg bg-slate-100/40 border border-transparent"
                >
                  <span>Tổng giá trị hàng gốc:</span>
                  <span class="font-mono font-bold text-slate-700">{{
                    formatMoney(invoice.totalOriginal)
                  }}</span>
                </div>

                <!-- 2. Phí vận chuyển -->
                <div
                  class="flex justify-between items-center text-slate-600 px-3 py-1.5 rounded-lg bg-slate-100/40 border border-transparent"
                >
                  <span>Cước phí vận chuyển:</span>
                  <span class="font-mono font-bold text-slate-700"
                    >+ {{ formatMoney(invoice.shippingFee) }}</span
                  >
                </div>

                <!-- 3. Voucher -->
                <div
                  v-if="invoice.hasVoucher && invoice.voucherDiscount > 0"
                  class="flex justify-between items-center text-rose-600 bg-rose-50/60 px-3 py-1.5 rounded-lg border border-rose-100"
                >
                  <span class="font-medium">Ưu đãi giảm giá (Voucher):</span>
                  <span class="font-mono font-bold"
                    >- {{ formatMoney(invoice.voucherDiscount) }}</span
                  >
                </div>

                <!-- 4. Xu -->
                <div
                  v-if="invoice.usedCoins > 0 && invoice.coinDiscount > 0"
                  class="flex justify-between items-center text-amber-700 bg-amber-50/60 px-3 py-1.5 rounded-lg border border-amber-100"
                >
                  <span class="font-medium">Xu sử dụng ({{ invoice.usedCoins }} xu):</span>
                  <span class="font-mono font-bold">- {{ formatMoney(invoice.coinDiscount) }}</span>
                </div>
              </div>
            </div>

            <!-- Thực thanh toán -->
            <div
              class="flex justify-between items-center text-base font-black text-indigo-700 border-t border-slate-200 pt-3 mt-4 select-none"
            >
              <span class="text-xs uppercase tracking-wider text-slate-800">Thực thanh toán:</span>
              <span class="font-mono text-lg text-indigo-600 tracking-tight">{{
                formatMoney(invoice.final)
              }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getHoadonById } from '@/service/HoaDonService'
import InvoiceModal from './InvoiceModal.vue'

const route = useRoute()
const router = useRouter()
const rawInvoice = ref(null)
const isPreviewOpen = ref(false)

const goBack = () => {
  router.push({ name: 'hoadon' })
}

const openPreview = () => {
  if (['huy', 'da_huy', 'da_tra_hang'].includes(invoice.value.status)) return
  isPreviewOpen.value = true
}

const closePreview = () => {
  isPreviewOpen.value = false
}

const formatStatusName = (status) => {
  if (!status) return 'N/A'
  const map = {
    cho_xac_nhan: 'Chờ xác nhận',
    da_xac_nhan: 'Đã xác nhận',
    cho_van_chuyen: 'Chờ vận chuyển',
    dang_chuan_bi_hang: 'Đang chuẩn bị hàng',
    cho_giao_hang: 'Chờ giao hàng',
    dang_giao: 'Đang giao',
    da_giao: 'Đã giao',
    giao_that_bai: 'Giao thất bại',
    hoan_thanh: 'Hoàn thành',
    huy: 'Đã hủy',
    da_huy: 'Đã hủy',
    da_tra_hang: 'Đã trả hàng',
  }
  return map[status] || status.replace(/_/g, ' ').replace(/^\w/, (c) => c.toUpperCase())
}

const formatDateTime = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return isNaN(date.getTime()) ? dateString : date.toLocaleString('vi-VN')
}

const getSourceBadgeClass = (source) => {
  switch (source) {
    case 'STAFF':
      return 'bg-blue-50 text-blue-600 border-blue-200'
    case 'CUSTOMER':
      return 'bg-purple-50 text-purple-600 border-purple-200'
    case 'SYSTEM':
      return 'bg-slate-100 text-slate-600 border-slate-200'
    default:
      return 'bg-slate-50 text-slate-500 border-slate-200'
  }
}

const invoice = computed(() => {
  if (!rawInvoice.value) return { items: [], history: [] }

  const ngayTaoDon = rawInvoice.value.ngayTao
  let listHistory = rawInvoice.value.lichSuHoaDons ? [...rawInvoice.value.lichSuHoaDons] : []
  const isOnline = rawInvoice.value.loaiHoaDon === 'online'

  if (ngayTaoDon) {
    const hasCreationMilestone = listHistory.some(
      (h) => h.trangThaiCu === null || h.thoiGian === ngayTaoDon || h.ghiChu?.includes('Khởi tạo'),
    )

    if (!hasCreationMilestone) {
      const initialStatus =
        listHistory.length > 0 && listHistory[0].trangThaiCu
          ? listHistory[0].trangThaiCu
          : isOnline
            ? 'cho_xac_nhan'
            : 'da_xac_nhan'

      listHistory.unshift({
        id: 'always-created-milestone',
        trangThaiCu: null,
        trangThaiMoi: initialStatus,
        thoiGian: ngayTaoDon,
        tenNhanVien: isOnline
          ? rawInvoice.value.tenKhachHang || 'Khách hàng'
          : rawInvoice.value.tenNhanVien || 'Nhân viên bán hàng',
        nguonThaoTac: isOnline ? 'CUSTOMER' : 'STAFF',
        ghiChu: isOnline
          ? 'Khách hàng đặt hàng thành công'
          : 'Khởi tạo hóa đơn tại quầy thành công',
      })
    }
  }

  const processedHistory = listHistory.map((hist) => {
    let hienThiTrangThai = null
    let tenNhanVienHienThi = hist.tenNhanVien

    if (!hist.trangThaiCu) {
      hienThiTrangThai = isOnline ? 'Đặt hàng' : 'Tạo hóa đơn'
      tenNhanVienHienThi = isOnline
        ? rawInvoice.value.tenKhachHang || 'Khách hàng'
        : hist.tenNhanVien || 'Nhân viên bán hàng'
    } else if (hist.nguonThaoTac === 'CUSTOMER') {
      tenNhanVienHienThi = rawInvoice.value.tenKhachHang
        ? `(${rawInvoice.value.tenKhachHang})`
        : 'Khách hàng'
    } else {
      tenNhanVienHienThi = hist.tenNhanVien || 'Hệ thống tự động'
    }

    return {
      ...hist,
      hienThiTrangThai,
      tenNhanVienHienThi,
    }
  })

  const sortedHistory = processedHistory.sort((a, b) => new Date(a.thoiGian) - new Date(b.thoiGian))

  const totalDiscount = rawInvoice.value.tongGiamGia || 0
  const coinDiscount = rawInvoice.value.tienGiamDoXuSuDung || 0
  const hasVoucher = !!rawInvoice.value.voucher
  const voucherDiscount = hasVoucher ? Math.max(0, totalDiscount - coinDiscount) : 0

  return {
    code: rawInvoice.value.maHoaDon,
    customer:
      rawInvoice.value.tenKhachHang || rawInvoice.value.tenNguoiNhan || 'Khách lẻ (Tại quầy)',
    phone:
      rawInvoice.value.soDienThoaiKhachHang || rawInvoice.value.soDienThoaiNguoiNhan || 'Không có',
    status: rawInvoice.value.trangThai,
    createdAt: ngayTaoDon ? formatDateTime(ngayTaoDon) : 'N/A',
    address: rawInvoice.value.diaChiGiaoHang || 'Mua trực tiếp tại cửa hàng',
    paymentMethod: rawInvoice.value.phuongThucThanhToan || 'Chưa thanh toán',
    note: rawInvoice.value.ghiChu,
    totalOriginal: rawInvoice.value.tongTienHang,
    discount: totalDiscount,
    shippingFee: rawInvoice.value.phiVanChuyen || 0,
    final: rawInvoice.value.tongThanhToan,
    usedCoins: rawInvoice.value.soXuSuDung || 0,
    coinDiscount: coinDiscount,
    hasVoucher: hasVoucher,
    voucherDiscount: voucherDiscount,
    items:
      rawInvoice.value.sanPhams?.map((sp) => ({
        id: sp.id,
        sku: sp.maSanPhamChiTiet,
        name: sp.tenSanPham,
        brand: sp.tenThuongHieu,
        color: sp.tenMauSac,
        size: sp.tenKichThuoc,
        quantity: sp.soLuong,
        price: sp.donGia,
        total: sp.thanhTien,
      })) || [],
    history: sortedHistory,
  }
})

const formatMoney = (val) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(val || 0)
}

const statusClassModern = (status) => {
  const base =
    'px-3 py-1 rounded-xl text-[11px] font-bold uppercase tracking-wider flex items-center w-fit border select-none min-w-[120px] justify-center '
  if (['da_xac_nhan', 'hoan_thanh', 'da_giao'].includes(status)) {
    return `${base} bg-emerald-50 text-emerald-700 border-emerald-200`
  }
  if (['huy', 'da_huy', 'da_tra_hang', 'giao_that_bai'].includes(status)) {
    return `${base} bg-rose-50 text-rose-700 border-rose-200`
  }
  return `${base} bg-amber-50 text-amber-700 border-amber-200`
}

const loadHoaDon = async () => {
  try {
    const data = await getHoadonById(route.params.id)
    rawInvoice.value = data
  } catch (error) {
    console.error('Lỗi khi tải hóa đơn:', error)
  }
}

onMounted(() => {
  loadHoaDon()
})
</script>

<style scoped>
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(6px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
.animate-fade-in {
  animation: fadeIn 0.3s ease-out forwards;
}
</style>
