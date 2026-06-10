<template>
  <Teleport to="body">
    <div
      v-if="hoaDon"
      class="fixed inset-0 z-[100] flex items-center justify-center bg-black bg-opacity-50 p-4"
    >
      <div
        id="invoice-area"
        class="bg-white w-80 shadow-2xl p-6 font-mono text-[11px] text-slate-900 border border-slate-300"
      >
        <div class="text-center mb-4">
          <div class="text-lg font-bold uppercase tracking-widest">Velora</div>
          <div class="text-[10px]">58 Nguyễn Khánh Toàn,Quận Cầu Giấy,TP.Hà Nội</div>
          <div class="text-[10px]">Hotline: 0377 28 0805</div>
          <div class="my-2 border-t border-b border-dashed border-slate-800 py-1">
            HÓA ĐƠN BÁN HÀNG
          </div>
        </div>

        <div class="mb-4 text-[10px]">
          <p>
            Mã HĐ: <span class="font-bold">{{ hoaDon.maHoaDon }}</span>
          </p>
          <p>Ngày: {{ formatDate(hoaDon.ngayTao) }}</p>
        </div>

        <div class="border-t border-dashed border-slate-800 py-1 mb-2">
          <div class="flex font-bold pb-1">
            <span class="w-6">STT</span>
            <span class="flex-1">Tên SP</span>
            <span class="w-10 text-center">SL</span>
            <span class="w-16 text-right">Đơn giá</span>
          </div>
        </div>

        <div class="space-y-2 mb-2">
          <div v-for="(sp, index) in hoaDon.sanPhams" :key="index">
            <div class="flex items-start">
              <span class="w-6">{{ index + 1 }}</span>
              <div class="flex-1">
                <div>{{ sp.tenSanPhamChiTiet }}</div>
                <div class="text-[9px] text-slate-500 italic">
                  {{ sp.tenMauSac }} / {{ sp.tenKichThuoc }}
                </div>
              </div>
              <span class="w-10 text-center">{{ sp.soLuong }}</span>
              <span class="w-16 text-right">{{ formatPriceNoCurrency(sp.donGia) }}</span>
            </div>
          </div>
        </div>

        <div class="border-t border-dashed border-slate-800 py-2">
          <div class="flex justify-between">
            <span>Tổng hàng:</span>
            <span>{{ formatPriceNoCurrency(hoaDon.tongTienHang) }}</span>
          </div>
          <div class="flex justify-between text-red-600">
            <span>Giảm giá:</span>
            <span>-{{ formatPriceNoCurrency(hoaDon.tongGiamGia) }}</span>
          </div>
          <div
            class="flex justify-between font-bold text-[13px] mt-1 border-t border-slate-200 pt-1"
          >
            <span>TỔNG CỘNG:</span>
            <span>{{ formatPriceNoCurrency(hoaDon.tongThanhToan) }}</span>
          </div>
        </div>

        <div class="text-center mt-4 text-[10px]">
          <p>
            Thanh toán: <span class="font-bold">{{ hoaDon.phuongThucThanhToan }}</span>
          </p>
          <p class="mt-2 font-bold uppercase">Xin cảm ơn quý khách!</p>
          <p class="mt-1">Hẹn gặp lại quý khách lần sau</p>
        </div>

        <div class="flex justify-center mt-4 gap-2 print:hidden">
          <button @click="$emit('close')" class="px-4 py-1 bg-gray-200 rounded hover:bg-gray-300">
            Đóng
          </button>
          <button
            @click="printInvoice"
            class="px-4 py-1 bg-slate-900 text-white rounded hover:bg-black"
          >
            In HĐ
          </button>
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script setup>
const props = defineProps({ hoaDon: Object })
const emit = defineEmits(['close'])

// Hàm format không kèm chữ "₫" để giống hóa đơn nhiệt hơn
const formatPriceNoCurrency = (v) => new Intl.NumberFormat('vi-VN').format(v || 0)
const formatDate = (d) =>
  new Date(d).toLocaleDateString('vi-VN') +
  ' ' +
  new Date(d).toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' })

const printInvoice = () => {
  const content = document.getElementById('invoice-area').innerHTML
  const win = window.open('', '_blank', 'width=400,height=600')
  win.document.write(`
    <html>
      <head>
        <title>In hóa đơn</title>
        <style>
          body { font-family: monospace; padding: 10px; width: 300px; }
          .print\:hidden { display: none; }
        </style>
      </head>
      <body>${content}</body>
    </html>
  `)
  win.document.close()
  setTimeout(() => {
    win.print()
    win.close()
  }, 500)
}
</script>
