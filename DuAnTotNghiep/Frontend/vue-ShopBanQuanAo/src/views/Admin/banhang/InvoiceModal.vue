<template>
  <Teleport to="body">
    <div
      v-if="hoaDon"
      class="fixed inset-0 z-[100] flex items-center justify-center bg-black/50 p-4"
    >
      <div
        id="invoice-area"
        class="bg-white w-[300px] shadow-2xl p-3 font-mono text-[11px] text-slate-900 border border-slate-300"
      >
        <!-- Header -->
        <div class="text-center mb-2">
          <div class="text-lg font-bold uppercase tracking-widest">K-Zone</div>
          <div class="text-[10px]">58 Nguyễn Khánh Toàn, Cầu Giấy, Hà Nội</div>
          <div class="text-[10px]">Hotline: 0377 28 0805</div>

          <div class="my-1 border-y border-dashed border-slate-800 py-1">
            {{ hoaDon.loaiHoaDon === 'ONLINE' ? 'HÓA ĐƠN ONLINE' : 'HÓA ĐƠN BÁN HÀNG' }}
          </div>
        </div>

        <!-- Thông tin hóa đơn -->
        <div class="flex justify-between text-[10px] mb-2">
          <div>
            Mã HĐ:
            <span class="font-bold">{{ hoaDon.maHoaDon }}</span>
          </div>
          <div>Ngày: {{ formatDate(hoaDon.ngayTao) }}</div>
        </div>

        <!-- Thông tin khách hàng & Giao hàng -->
        <div class="border-y border-dashed border-slate-800 py-1 mb-2 text-[10px] space-y-0.5">
          <div class="flex justify-between">
            <div>
              KH:
              <span class="font-semibold">
                {{ hoaDon.tenKhachHang || 'Khách lẻ' }}
              </span>
            </div>
            <div>SĐT: {{ hoaDon.soDienThoai || '---' }}</div>
          </div>

          <div v-if="hoaDon.diaChiGiaoHang">ĐC: {{ hoaDon.diaChiGiaoHang }}</div>
        </div>

        <!-- Tiêu đề bảng -->
        <div class="border-b border-dashed border-slate-800 pb-1 mb-1">
          <div class="flex font-bold">
            <span class="w-4">#</span>
            <span class="flex-1">Sản phẩm</span>
            <span class="w-6 text-center">SL</span>
            <span class="w-14 text-right">Giá</span>
          </div>
        </div>

        <!-- Danh sách sản phẩm -->
        <div class="space-y-1.5 mb-2">
          <div v-for="(sp, index) in hoaDon.sanPhams" :key="index" class="leading-tight">
            <div class="flex items-start">
              <span class="w-4">{{ index + 1 }}</span>

              <div class="flex-1 pr-1">
                <!-- Tên sản phẩm -->
                <div class="font-medium text-slate-800">
                  {{ sp.tenSanPham }}
                </div>

                <!-- Mã chi tiết chống xuống dòng vỡ chữ -->
                <div class="mt-0.5">
                  <span
                    class="inline-block px-1 py-[1px] text-[9px] rounded border border-slate-300 text-slate-600 bg-slate-50 whitespace-nowrap"
                  >
                    #{{ sp.maSanPhamChiTiet }}
                  </span>
                </div>

                <!-- Thuộc tính -->
                <div class="text-[8px] text-slate-500 mt-0.5">
                  {{ sp.tenMauSac }}/{{ sp.tenKichThuoc }}/{{ sp.tenChatLieu }}/{{
                    sp.tenThuongHieu
                  }}
                </div>
              </div>

              <span class="w-6 text-center">
                {{ sp.soLuong }}
              </span>

              <span class="w-14 text-right whitespace-nowrap">
                {{ formatPriceNoCurrency(sp.donGia) }}
              </span>
            </div>
          </div>
        </div>

        <!-- Tổng tiền -->
        <div class="border-t border-dashed border-slate-800 pt-1 text-[10px] space-y-0.5">
          <div class="flex justify-between">
            <span>Tổng tiền hàng:</span>
            <span>
              {{ formatPriceNoCurrency(hoaDon.tongTienHang) }}
            </span>
          </div>

          <div class="flex justify-between text-red-600">
            <span>Giảm giá:</span>
            <span> -{{ formatPriceNoCurrency(hoaDon.tongGiamGia) }} </span>
          </div>

          <!-- Phí vận chuyển (chỉ hiện khi có phí ship > 0) -->
          <div v-if="hoaDon.phiVanChuyen > 0" class="flex justify-between">
            <span>Phí vận chuyển:</span>
            <span> +{{ formatPriceNoCurrency(hoaDon.phiVanChuyen) }} </span>
          </div>

          <div class="flex justify-between font-bold text-[12px] border-t mt-1 pt-1">
            <span>TỔNG THANH TOÁN</span>
            <span>
              {{ formatPriceNoCurrency(hoaDon.tongThanhToan) }}
            </span>
          </div>
        </div>

        <!-- Footer -->
        <div class="text-center mt-2 text-[10px] space-y-1">
          <div class="flex justify-between">
            <span>Thanh toán:</span>
            <span class="font-bold text-right">
              {{ hoaDon?.phuongThucThanhToan || '---' }}
            </span>
          </div>

          <p class="mt-1 font-bold uppercase">Xin cảm ơn quý khách!</p>
          <p class="text-[9px] text-slate-500">Hẹn gặp lại quý khách lần sau</p>
        </div>

        <!-- Nút thao tác -->
        <div class="flex justify-center gap-2 mt-3 print:hidden">
          <button @click="$emit('close')" class="px-3 py-1 bg-gray-200 rounded hover:bg-gray-300">
            Đóng
          </button>

          <button
            @click="printInvoice"
            class="px-3 py-1 bg-slate-900 text-white rounded hover:bg-black"
          >
            In HĐ
          </button>
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script setup lang="ts">
defineProps<{
  hoaDon: any
}>()

defineEmits(['close'])

const formatPriceNoCurrency = (v: number) => new Intl.NumberFormat('vi-VN').format(v || 0)

const formatDate = (d: string | number | Date) =>
  new Date(d).toLocaleDateString('vi-VN') +
  ' ' +
  new Date(d).toLocaleTimeString('vi-VN', {
    hour: '2-digit',
    minute: '2-digit',
  })

const printInvoice = () => {
  const invoiceArea = document.getElementById('invoice-area')
  if (!invoiceArea) return

  const content = invoiceArea.innerHTML
  const win = window.open('', '_blank', 'width=400,height=700')
  if (!win) return

  win.document.write(`
    <html>
      <head>
        <title>Hóa đơn</title>
        <style>
          *{
            box-sizing:border-box;
          }

          body{
            width:300px;
            margin:0 auto;
            padding:8px;
            font-family:monospace;
            font-size:11px;
            color:#000;
          }

          .print\\:hidden{
            display:none !important;
          }
        </style>
      </head>
      <body>
        ${content}
      </body>
    </html>
  `)

  win.document.close()

  setTimeout(() => {
    win.print()
    win.close()
  }, 300)
}
</script>
