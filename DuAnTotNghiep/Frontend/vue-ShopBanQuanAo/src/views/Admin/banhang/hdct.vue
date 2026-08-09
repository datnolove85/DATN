<template>
  <div
    class="space-y-6 max-w-full mx-auto p-4 animate-fade-in bg-slate-50 text-slate-800 rounded-2xl min-h-screen selection:bg-indigo-100 selection:text-indigo-900 overflow-hidden"
  >
    <div
      class="relative p-6 bg-white rounded-2xl border border-slate-200 flex flex-col md:flex-row justify-between items-start md:items-center gap-4 shadow-sm"
    >
      <div>
        <span class="text-xs tracking-wide uppercase font-semibold text-indigo-600">
          Hồ sơ lưu trữ giao dịch
        </span>
        <h1 class="text-2xl font-bold tracking-tight mt-1 text-slate-800">Chi Tiết Hóa Đơn</h1>
        <p class="text-sm text-slate-500 mt-1 flex items-center gap-2">
          <span>Mã hóa đơn:</span>
          <span
            class="font-mono font-bold text-indigo-600 bg-indigo-50 px-2 py-0.5 rounded-md border border-indigo-100"
          >
            {{ invoice.code || 'Đang tải...' }}
          </span>
        </p>
      </div>

      <div class="flex flex-wrap items-center gap-2.5 w-full md:w-auto justify-end">
        <!-- <button
          v-if="canReturn"
          @click="openTraHang"
          class="flex items-center gap-2 px-4 py-2 bg-red-600 hover:bg-red-500 text-white rounded-xl text-xs font-bold transition-all active:scale-95 shadow-sm"
        >
          🔄 TRẢ HÀNG
        </button> -->

        <div>
          <button
            @click="openPreview"
            :disabled="!rawInvoice"
            class="flex items-center justify-center gap-2 px-4 py-2 bg-indigo-600 hover:bg-indigo-500 disabled:bg-slate-100 disabled:text-slate-400 text-white rounded-xl text-xs font-bold transition-all active:scale-95 whitespace-nowrap shadow-sm"
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
                d="M6.72 13.829c-.24.03-.48.062-.72.096m.72-.096a42.415 42.415 0 0 1 10.56 0m-10.56 0L6.34 18m10.94-4.171c.24.03.48.062.72.096m-.72-.096L17.66 18m0 0 .229 2.523a1.125 1.125 0 0 1-1.12 1.227H7.231c-.617 0-1.11-.51-1.07-1.122L6.34 18m11.32 0h-11.32M9 11V5.25A2.25 2.25 0 0 1 11.25 3h1.5A2.25 2.25 0 0 1 15 5.25V11m-6 0h6a2.25 2.25 0 0 1 2.25 2.25v1.875c0 .621-.504 1.125-1.125 1.125H6.875A1.125 1.125 0 0 1 5.625 16.25V13.25A2.25 2.25 0 0 1 7.875 11h.25Z"
              />
            </svg>
            XUẤT HÓA ĐƠN
          </button>
          <InvoiceModal v-if="isPreviewOpen" :hoa-don="rawInvoice" @close="closePreview" />
        </div>

        <button
          @click="goBack"
          class="flex items-center justify-center gap-2 px-4 py-2 bg-slate-50 border border-slate-200 hover:bg-rose-50 hover:border-rose-200 hover:text-rose-600 text-slate-600 rounded-xl text-xs font-bold transition-all active:scale-95 whitespace-nowrap shadow-sm"
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
              d="M9 15 3 9m0 0 6-6M3 9h12a6 6 0 0 1 0 12h-3"
            />
          </svg>
          QUAY LẠI
        </button>
      </div>
    </div>

    <div class="bg-white p-6 rounded-2xl shadow-sm border border-slate-100">
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <div
          class="p-5 bg-slate-50/60 rounded-xl border border-slate-200/60 transition-all hover:bg-slate-50"
        >
          <p
            class="text-[10px] font-extrabold text-slate-400 uppercase tracking-widest mb-2 flex items-center gap-1.5"
          >
            👤 Hồ sơ đối tác
          </p>
          <p class="font-bold text-slate-800 text-sm capitalize">
            {{ invoice.customer || 'Khách vãng lai' }}
          </p>
          <p class="text-xs text-slate-500 font-mono mt-0.5">
            {{ invoice.phone || 'Không để lại số' }}
          </p>
        </div>

        <div
          class="p-5 bg-slate-50/60 rounded-xl border border-slate-200/60 transition-all hover:bg-slate-50 flex flex-col justify-between"
        >
          <div>
            <p
              class="text-[10px] font-extrabold text-slate-400 uppercase tracking-widest mb-2 flex items-center gap-1.5"
            >
              ⚙️ Trạng thái vận hành
            </p>
            <span :class="statusClassModern(invoice.status)">
              <span
                :class="['w-1.5 h-1.5 rounded-full mr-2 transition-all bg-current animate-pulse']"
              ></span>
              {{
                invoice.status === 'da_xac_nhan'
                  ? 'Đã xác nhận'
                  : invoice.status === 'cho_xac_nhan'
                    ? 'Chờ xác nhận'
                    : invoice.status
              }}
            </span>
          </div>
        </div>

        <div
          class="p-5 bg-slate-50/60 rounded-xl border border-slate-200/60 transition-all hover:bg-slate-50"
        >
          <p
            class="text-[10px] font-extrabold text-slate-400 uppercase tracking-widest mb-2 flex items-center gap-1.5"
          >
            📅 Biên niên ký tạo
          </p>
          <p class="font-bold text-slate-800 text-sm font-mono flex items-center gap-2">
            {{ invoice.created }}
          </p>
          <p class="text-[11px] text-slate-400 mt-0.5">Thời gian cập nhật gần nhất</p>
        </div>
      </div>

      <div class="mb-8">
        <div class="flex items-center justify-between mb-4 select-none">
          <h3
            class="text-xs font-extrabold uppercase tracking-widest text-indigo-600 flex items-center gap-2"
          >
            <span class="inline-block w-1.5 h-3.5 bg-indigo-600 rounded-sm"></span>
            Kiểm kê hạng mục sản phẩm mua
          </h3>
          <span
            class="text-[11px] font-bold text-slate-400 font-mono bg-slate-100 px-2.5 py-0.5 rounded-md border border-slate-200"
          >
            Tổng cộng: {{ invoice.items?.length || 0 }} loại
          </span>
        </div>

        <div class="border border-slate-200/70 rounded-xl overflow-hidden shadow-sm bg-white">
          <div class="overflow-x-auto">
            <table class="w-full text-xs text-left border-collapse min-w-[900px]">
              <thead
                class="bg-slate-50 text-slate-400 font-bold text-[11px] uppercase tracking-wider select-none border-b border-slate-200/70"
              >
                <tr>
                  <th class="px-6 py-4 text-left font-bold w-[12%]">Mã</th>
                  <th class="px-6 py-4 text-left font-bold w-[35%]">Tên sản phẩm</th>
                  <th class="px-6 py-4 text-left font-bold w-[18%]">Thông số phân loại</th>
                  <th class="px-6 py-4 text-center font-bold w-[10%]">Số lượng</th>
                  <th class="px-6 py-4 text-right font-bold w-[12%]">Đơn giá</th>
                  <th class="px-6 py-4 text-right font-bold w-[13%]">Thành tiền</th>
                </tr>
              </thead>
              <tbody class="divide-y divide-slate-100 font-medium">
                <tr
                  v-for="item in invoice.items"
                  :key="item.id"
                  class="hover:bg-indigo-50/15 transition-colors group"
                >
                  <td class="px-6 py-5 font-mono font-bold text-indigo-600 tracking-wide">
                    {{ item.sku }}
                  </td>
                  <td class="px-6 py-5">
                    <div
                      class="font-bold text-slate-800 text-sm leading-snug max-w-xs sm:max-w-md break-words"
                    >
                      {{ item.name }}
                    </div>
                  </td>
                  <td class="px-6 py-5 select-none">
                    <div class="flex flex-wrap gap-2">
                      <span
                        class="inline-flex items-center px-2 py-0.5 bg-slate-900 text-white font-bold rounded text-[9px] uppercase tracking-wider shadow-xs"
                      >
                        {{ item.brand }}
                      </span>
                      <span
                        class="inline-flex items-center gap-1 px-2.5 py-0.5 bg-slate-100 text-slate-600 font-semibold rounded-md border border-slate-200 text-[10px]"
                      >
                        Màu: {{ item.color }}
                      </span>
                      <span
                        class="inline-flex items-center gap-1 px-2.5 py-0.5 bg-indigo-50 text-indigo-600 font-semibold rounded-md border border-indigo-100 text-[10px]"
                      >
                        Size: {{ item.size }}
                      </span>
                    </div>
                  </td>
                  <td class="px-6 py-5 text-center text-slate-700 font-mono font-bold text-sm">
                    {{ item.quantity }}
                  </td>
                  <td class="px-6 py-5 text-right font-mono text-slate-500 font-semibold text-sm">
                    {{ formatMoney(item.price) }}
                  </td>
                  <td
                    class="px-6 py-5 text-right font-extrabold text-slate-900 font-mono text-sm tracking-wide"
                  >
                    {{ formatMoney(item.total) }}
                  </td>
                </tr>
                <tr v-if="!invoice.items || invoice.items.length === 0">
                  <td colspan="6" class="text-center py-14 text-slate-400 italic">
                    Không tìm thấy dữ liệu cấu trúc sản phẩm của hóa đơn này.
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-8 pt-6 border-t border-slate-100">
        <div class="space-y-4 text-xs">
          <div class="bg-slate-50/50 p-4 rounded-xl border border-slate-200/60">
            <p
              class="font-extrabold uppercase text-[10px] mb-2 tracking-wider text-indigo-950 flex items-center gap-1.5"
            >
              🚚 Địa điểm phân phối
            </p>
            <p class="text-slate-600 leading-relaxed">
              <span class="font-bold text-slate-900 block mt-0.5">
                {{ invoice.address }}
              </span>
            </p>
          </div>
          <div class="bg-slate-50/50 p-4 rounded-xl border border-slate-200/60">
            <p
              class="font-extrabold uppercase text-[10px] mb-2 tracking-wider text-indigo-950 flex items-center gap-1.5"
            >
              💳 Kênh xử lý dòng tiền
            </p>
            <p class="text-slate-600">
              Phương thức thanh toán:
              <span
                class="font-bold text-indigo-700 bg-indigo-50 border border-indigo-200 px-2.5 py-0.5 rounded-md inline-block mt-1 sm:mt-0 ml-0 sm:ml-1"
                >{{ invoice.paymentMethod }}</span
              >
            </p>
          </div>
          <div class="bg-slate-50/50 p-4 rounded-xl border border-slate-200/60">
            <p
              class="font-extrabold uppercase text-[10px] mb-2 tracking-wider text-indigo-950 flex items-center gap-1.5"
            >
              📝 Biên bản ghi chú
            </p>
            <p
              class="text-slate-500 italic bg-white p-2.5 rounded-lg border border-slate-200 min-h-[50px] leading-relaxed"
            >
              {{ invoice.note || 'Không có yêu cầu hay ghi chú đặc biệt nào kèm theo đơn hàng.' }}
            </p>
          </div>
        </div>

        <div class="flex justify-end items-start">
          <div
            class="w-full md:w-85 space-y-3.5 p-6 bg-slate-50 rounded-2xl border border-slate-200/70 shadow-sm text-xs"
          >
            <p
              class="font-extrabold uppercase text-[10px] mb-3 tracking-widest text-slate-400 flex items-center gap-1.5 select-none"
            >
              📊 Tổng quyết toán dòng tiền
            </p>
            <div class="flex justify-between items-center text-slate-600">
              <span>Tổng giá trị hàng gốc:</span>
              <span class="font-mono font-bold text-slate-700">{{
                formatMoney(invoice.totalOriginal)
              }}</span>
            </div>
            <div
              class="flex justify-between items-center text-rose-600 bg-rose-50/60 px-3 py-1.5 rounded-xl border border-rose-100"
            >
              <span class="font-medium">Ưu đãi giảm giá (Voucher):</span>
              <span class="font-mono font-extrabold">- {{ formatMoney(invoice.discount) }}</span>
            </div>
            <div class="flex justify-between items-center text-slate-600">
              <span>Cước phí vận chuyển:</span>
              <span class="font-mono font-bold text-slate-700">{{
                formatMoney(invoice.shippingFee)
              }}</span>
            </div>
            <div
              class="flex justify-between items-center text-base font-black text-indigo-700 border-t border-slate-200 pt-3.5 mt-4 select-none"
            >
              <span class="text-xs uppercase tracking-wider text-slate-800">Thực thanh toán:</span>
              <span class="font-mono text-xl text-indigo-600 tracking-tight">{{
                formatMoney(invoice.final)
              }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <Teleport to="body">
      <div
        v-if="showTraHang"
        class="fixed inset-0 bg-black/60 z-[200] flex items-center justify-center"
      >
        <div class="bg-white w-[900px] rounded-2xl shadow-2xl overflow-hidden">
          <!-- HEADER -->
          <div class="bg-red-500 text-white px-5 py-3 flex justify-between items-center">
            <h2 class="font-bold">🔄 Trả hàng hóa đơn</h2>
            <button @click="showTraHang = false" class="text-white font-bold">✕</button>
          </div>

          <!-- BODY -->
          <div class="p-5 grid grid-cols-3 gap-4">
            <!-- LEFT: TABLE -->
            <div class="col-span-2">
              <table class="w-full text-xs border">
                <thead class="bg-slate-100 text-[11px]">
                  <tr>
                    <th class="p-2 text-left">Sản phẩm</th>
                    <th>Đã mua</th>
                    <th>Đã trả</th>
                    <th>Còn lại</th>
                    <th>Trả</th>
                    <th>Tiền hoàn</th>
                  </tr>
                </thead>

                <tbody>
                  <tr v-for="sp in traHangData?.sanPhams || []" :key="sp.hdctId">
                    <td class="p-2 font-semibold">{{ sp.tenSanPham }}</td>

                    <td>{{ sp.soLuongMua }}</td>
                    <td class="text-rose-500 font-bold">{{ sp.daTra }}</td>

                    <td class="text-green-600 font-bold">
                      {{ sp.conLai }}
                    </td>

                    <td>
                      <input
                        type="number"
                        v-model.number="sp.soLuongTra"
                        :max="sp.conLai"
                        min="0"
                        class="border px-2 py-1 w-16"
                        @input="calcRefund"
                      />
                    </td>

                    <td class="text-right font-bold text-indigo-600">
                      {{ formatMoney((sp.soLuongTra || 0) * sp.donGia) }}
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>

            <!-- RIGHT: SUMMARY -->
            <div class="col-span-1 border rounded-xl p-4 bg-slate-50">
              <h3 class="font-bold mb-3">📊 Tổng hoàn tiền</h3>

              <div class="space-y-2 text-xs">
                <div class="flex justify-between">
                  <span>Tổng trả:</span>
                  <span class="font-bold text-red-600">{{ totalReturnQty }}</span>
                </div>

                <div class="flex justify-between">
                  <span>Tiền hoàn:</span>
                  <span class="font-bold text-indigo-600">
                    {{ formatMoney(totalRefund) }}
                  </span>
                </div>

                <div class="border-t pt-2 mt-2 flex justify-between">
                  <span class="font-bold">Thực nhận lại:</span>
                  <span class="text-green-600 font-black">
                    {{ formatMoney(totalRefund) }}
                  </span>
                </div>
              </div>

              <button
                class="w-full mt-4 bg-red-500 text-white py-2 rounded-lg font-bold"
                @click="submitTH"
              >
                Xác nhận trả hàng
              </button>
            </div>
          </div>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getHoadonById } from '@/service/HoaDonService'
import { useToast } from 'vue-toastification'
import { getTraHangByHoaDon, submitTraHang } from '@/service/HoaDonService'
import InvoiceModal from './InvoiceModal.vue'
const route = useRoute()
const router = useRouter()
const rawInvoice = ref(null)

const toast = useToast()

const showTraHang = ref(false)
const traHangData = ref(null)

const openTraHang = async () => {
  try {
    showTraHang.value = true

    const res = await getTraHangByHoaDon(route.params.id)

    traHangData.value = {
      sanPhams:
        res?.sanPhams?.map((sp) => ({
          ...sp,
          soLuongTra: 0,
          conLai: sp.conLai || 0,
        })) || [],
    }
  } catch (e) {
    console.error(e)
    showTraHang.value = false
  }
}
const canReturn = computed(() => {
  const s = invoice.value?.status?.trim()
  return ['da_giao', 'da_xac_nhan'].includes(s)
})
const submitTH = async () => {
  try {
    const payload = {
      hoaDonId: route.params.id,
      maTraHang: 'TH' + Date.now(),
      tongTienHoan: totalRefund.value,
      lyDo: 'Khách trả hàng',
      danhSachTra: traHangData.value.sanPhams
        .filter((sp) => sp.soLuongTra > 0)
        .map((sp) => ({
          hdctId: sp.hdctId,
          soLuongTra: sp.soLuongTra,
        })),
    }
    await submitTraHang(payload)

    toast.success('Trả hàng thành công!')
    showTraHang.value = false

    loadHoaDon()
  } catch (e) {
    console.error(e)
    toast.error('Trả hàng thất bại!')
  }
}

const calcRefund = () => {
  if (!traHangData.value?.sanPhams) return

  traHangData.value.sanPhams.forEach((sp) => {
    if (!sp.soLuongTra || sp.soLuongTra < 0) sp.soLuongTra = 0
    if (sp.soLuongTra > sp.conLai) sp.soLuongTra = sp.conLai
  })
}
const totalRefund = computed(() => {
  return (
    traHangData.value?.sanPhams?.reduce((sum, sp) => {
      return sum + (sp.soLuongTra || 0) * (sp.donGia || 0)
    }, 0) || 0
  )
})

const totalReturnQty = computed(() => {
  return (
    traHangData.value?.sanPhams?.reduce((sum, sp) => {
      return sum + (sp.soLuongTra || 0)
    }, 0) || 0
  )
})

const isPreviewOpen = ref(false)

const goBack = () => {
  router.push({ name: 'hoadon' })
}

const openPreview = () => {
  isPreviewOpen.value = true
}

const closePreview = () => {
  isPreviewOpen.value = false
}

const invoice = computed(() => {
  if (!rawInvoice.value) return { items: [] }

  return {
    code: rawInvoice.value.maHoaDon,
    customer: rawInvoice.value.tenNguoiNhan,
    phone: rawInvoice.value.soDienThoaiNguoiNhan,
    status: rawInvoice.value.trangThai,
    created: rawInvoice.value.ngayCapNhat
      ? (() => {
          const [year, month, day] = rawInvoice.value.ngayCapNhat.split('T')[0].split('-')
          return `${day}/${month}/${year}`
        })()
      : 'N/A',
    address: rawInvoice.value.diaChiGiaoHang,
    paymentMethod: rawInvoice.value.phuongThucThanhToan,
    note: rawInvoice.value.ghiChu,
    totalOriginal: rawInvoice.value.tongTienHang,
    discount: rawInvoice.value.tongGiamGia,
    shippingFee: rawInvoice.value.phiVanChuyen || 0,
    final: rawInvoice.value.tongThanhToan,
    items: rawInvoice.value.sanPhams.map((sp) => ({
      id: sp.id,
      sku: sp.maSanPhamChiTiet,
      name: sp.tenSanPham,
      brand: sp.tenThuongHieu,
      color: sp.tenMauSac,
      size: sp.tenKichThuoc,
      quantity: sp.soLuong,
      price: sp.donGia,
      total: sp.thanhTien,
    })),
  }
})

const formatMoney = (val) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(val || 0)
}

const statusClassModern = (status) => {
  const base =
    'px-3 py-1 rounded-xl text-[10px] font-bold uppercase tracking-wider flex items-center w-fit border select-none min-w-[120px] justify-center '
  return status === 'da_xac_nhan'
    ? `${base} bg-emerald-50 text-emerald-700 border-emerald-200`
    : `${base} bg-amber-50 text-amber-700 border-amber-200`
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
    transform: translateY(8px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
.animate-fade-in {
  animation: fadeIn 0.35s ease-out forwards;
}
</style>
