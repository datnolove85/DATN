<template>
  <div class="space-y-6 p-6 bg-slate-50 min-h-screen">
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <div class="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm">
        <p class="text-xs font-bold text-slate-500 uppercase tracking-widest">Tổng doanh thu</p>
        <h2 class="text-2xl font-black text-slate-900 mt-2">582.400.000đ</h2>
      </div>
    </div>

    <div
      class="bg-white p-4 rounded-2xl border border-slate-200 shadow-sm flex flex-wrap gap-4 items-center"
    >
      <div class="flex-1 min-w-[200px]">
        <input
          type="text"
          placeholder="Tìm mã hóa đơn..."
          class="w-full px-4 py-2 bg-slate-50 border rounded-xl outline-none focus:ring-2 focus:ring-indigo-500"
        />
      </div>
      <div class="flex items-center gap-2">
        <input type="date" class="px-3 py-2 bg-slate-50 border rounded-xl text-xs" />
        <span class="text-slate-400 font-bold">đến</span>
        <input type="date" class="px-3 py-2 bg-slate-50 border rounded-xl text-xs" />
      </div>
      <select class="px-4 py-2 bg-slate-50 border rounded-xl text-xs">
        <option>Trạng thái thanh toán</option>
      </select>
      <button
        class="bg-indigo-600 text-white px-6 py-2 rounded-xl text-xs font-bold hover:bg-indigo-700 transition"
      >
        XUẤT BÁO CÁO
      </button>
    </div>

    <div class="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
      <table class="w-full text-left text-sm">
        <thead class="bg-slate-50 border-b border-slate-200">
          <tr class="text-[10px] uppercase font-black text-slate-500 tracking-widest">
            <th class="px-6 py-4">Mã HĐ</th>
            <th class="px-6 py-4">Khách hàng</th>
            <th class="px-6 py-4">Ngày tạo</th>
            <th class="px-6 py-4 text-right">Tổng tiền</th>
            <th class="px-6 py-4 text-center">Trạng thái</th>
            <th class="px-6 py-4 text-center">Action</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-slate-100">
          <tr v-for="hd in hoaDons" :key="hd.id" class="hover:bg-indigo-50/30 transition-colors">
            <td class="px-6 py-4 font-bold text-indigo-600">{{ hd.maHoaDon }}</td>
            <td class="px-6 py-4 font-medium">{{ hd.tenNguoiNhan }}</td>
            <td class="px-6 py-4 text-slate-500">{{ formatDate(hd.ngayTao) }}</td>
            <td class="px-6 py-4 text-right font-black">{{ formatPrice(hd.tongThanhToan) }}</td>
            <td class="px-6 py-4 text-center">
              <span
                class="px-3 py-1 rounded-full text-[10px] font-bold bg-green-100 text-green-700"
              >
                {{ hd.trangThai }}
              </span>
            </td>
            <td class="px-6 py-4 text-center">
              <button class="text-slate-400 hover:text-indigo-600 transition">Xem chi tiết</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
// Logic:
// 1. Sử dụng dayjs hoặc date-fns để xử lý ngày tháng.
// 2. Format giá tiền dùng: new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value)
// 3. Gọi API với query params chứa ngày bắt đầu & kết thúc để server lọc tại DB (rất quan trọng).
</script>
