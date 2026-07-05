<template>
  <div class="fixed inset-0 z-50 flex justify-end bg-black/40">
    <!-- BACKDROP -->
    <div class="flex-1" @click="$emit('close')"></div>

    <!-- DRAWER -->
    <div class="w-full max-w-md bg-white h-full shadow-xl overflow-y-auto">
      <!-- HEADER -->
      <div class="p-4 border-b flex justify-between items-center">
        <div>
          <h2 class="text-xl font-bold">Chi tiết hóa đơn</h2>
          <p class="text-sm text-gray-500">{{ order?.maHoaDon }}</p>
        </div>

        <button @click="$emit('close')" class="text-gray-500 hover:text-black">✕</button>
      </div>

      <div v-if="order" class="p-4 space-y-4">
        <!-- CUSTOMER -->
        <div class="bg-gray-50 rounded-xl p-3">
          <div class="font-semibold">Khách hàng</div>
          <div class="text-sm text-gray-700">
            {{ order.tenKhachHang || order.tenNguoiNhan }}
          </div>
          <div class="text-sm text-gray-500">
            {{ order.soDienThoaiNguoiNhan }}
          </div>
        </div>

        <!-- ADDRESS -->
        <div class="bg-gray-50 rounded-xl p-3">
          <div class="font-semibold">Địa chỉ giao hàng</div>
          <div class="text-sm text-gray-700">
            {{ order.diaChiGiaoHang }}
          </div>
        </div>

        <!-- STATUS -->
        <div class="flex justify-between">
          <span>Trạng thái:</span>
          <span class="font-medium">{{ order.trangThai }}</span>
        </div>

        <div class="flex justify-between">
          <span>Thanh toán:</span>
          <span class="font-medium">{{ order.trangThaiThanhToan }}</span>
        </div>

        <!-- PRODUCTS -->
        <div>
          <div class="font-semibold mb-2">Sản phẩm</div>

          <div v-for="(p, index) in order.sanPhams" :key="index" class="flex gap-3 border-b py-2">
            <img
              :src="p.image || 'https://via.placeholder.com/60'"
              class="w-14 h-14 rounded object-cover border"
            />

            <div class="flex-1">
              <div class="font-medium text-sm">
                {{ p.tenSanPham }}
              </div>

              <div class="text-xs text-gray-500">
                SL: {{ p.soLuong }} × {{ p.donGia?.toLocaleString() }}đ
              </div>

              <div class="text-sm font-bold text-red-500">{{ p.thanhTien?.toLocaleString() }}đ</div>
            </div>
          </div>
        </div>

        <!-- SUMMARY -->
        <div class="bg-gray-50 rounded-xl p-3 space-y-2">
          <div class="flex justify-between">
            <span>Tiền hàng</span>
            <span>{{ order.tongTienHang?.toLocaleString() }}đ</span>
          </div>

          <div class="flex justify-between">
            <span>Giảm giá</span>
            <span>-{{ order.tongGiamGia?.toLocaleString() }}đ</span>
          </div>

          <div class="flex justify-between">
            <span>Phí ship</span>
            <span>{{ order.phiVanChuyen?.toLocaleString() }}đ</span>
          </div>

          <hr />

          <div class="flex justify-between font-bold text-lg">
            <span>Tổng</span>
            <span class="text-red-500"> {{ order.tongThanhToan?.toLocaleString() }}đ </span>
          </div>
        </div>

        <!-- NOTE -->
        <div v-if="order.ghiChu">
          <div class="font-semibold">Ghi chú</div>
          <p class="text-sm text-gray-600">{{ order.ghiChu }}</p>
        </div>
      </div>

      <!-- FOOTER ACTION -->
      <div class="p-4 border-t space-y-2">
        <button
          v-if="order?.trangThai === 'cho_xac_nhan'"
          @click="$emit('xacNhan', order.id)"
          class="w-full bg-black text-white py-3 rounded-xl"
        >
          Xác nhận đơn
        </button>

        <button
          v-if="order?.trangThai === 'cho_xac_nhan'"
          @click="$emit('huy', order.id)"
          class="w-full bg-red-500 text-white py-3 rounded-xl"
        >
          Hủy đơn
        </button>

        <button
          v-if="order?.trangThai === 'da_xac_nhan'"
          @click="$emit('giao', order.id)"
          class="w-full bg-blue-500 text-white py-3 rounded-xl"
        >
          Bàn giao vận chuyển
        </button>

        <button
          v-if="order?.trangThai === 'dang_giao'"
          @click="$emit('hoanThanh', order.id)"
          class="w-full bg-green-500 text-white py-3 rounded-xl"
        >
          Hoàn thành
        </button>
      </div>
    </div>
  </div>
</template>
<script setup>
import { computed } from 'vue'

const props = defineProps({
  orderId: Number,
  order: Object,
})

defineEmits(['close', 'xacNhan', 'huy', 'giao', 'hoanThanh'])

// nếu sau này fetch detail riêng thì dùng orderId
</script>
