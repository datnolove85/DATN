<template>
  <div
    class="space-y-4 max-w-full mx-auto p-4 animate-fade-in bg-slate-50 text-slate-800 rounded-2xl selection:bg-indigo-100 selection:text-indigo-900 overflow-hidden"
  >
    <!-- Header phân hệ -->
    <div
      class="flex flex-col lg:flex-row justify-between items-start lg:items-center bg-white rounded-2xl shadow-sm border border-slate-200 px-5 py-3.5 gap-3"
    >
      <div class="flex items-center gap-3">
        <div
          class="w-9 h-9 rounded-xl bg-indigo-50 flex items-center justify-center text-lg text-indigo-600 font-bold"
        >
          👤
        </div>
        <div>
          <h1 class="text-base font-bold text-slate-800">Quản lý khách hàng</h1>
          <p class="text-slate-400 text-[11px]">
            Quản lý hồ sơ, thông tin định danh và thông tin tích lũy của khách hàng.
          </p>
        </div>
      </div>

      <div class="w-full lg:w-auto flex flex-wrap items-center gap-2">
        <!-- Tìm kiếm tự động -->
        <div
          class="relative flex items-center bg-slate-50 border border-slate-200 hover:border-indigo-400 focus-within:border-indigo-500 rounded-xl px-3 py-1.5 w-full sm:w-64 transition-all group"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke-width="2.5"
            stroke="currentColor"
            class="w-3.5 h-3.5 text-slate-400 mr-2 group-focus-within:text-indigo-500"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              d="m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.602 10.602Z"
            />
          </svg>
          <input
            type="text"
            v-model="keyword"
            placeholder="Nhập từ khóa tìm kiếm..."
            class="bg-transparent text-slate-700 placeholder-slate-400 text-xs outline-none w-full font-medium"
          />
        </div>

        <button
          @click="resetSearch"
          class="flex items-center gap-1.5 px-3 py-2 bg-slate-50 hover:bg-slate-100 border border-slate-200 text-slate-600 rounded-xl text-xs font-bold transition-all active:scale-95"
          title="Làm mới"
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
              d="M16.023 9.348h4.992v-.001M2.985 19.644v-4.992m0 0h4.992m-4.993 0 3.181 3.183a8.25 8.25 0 0 0 13.803-3.7M4.031 9.865a8.25 8.25 0 0 1 13.803-3.7l3.181 3.182m0-4.991v4.99"
            />
          </svg>
          Làm mới
        </button>

        <button
          @click="handleExportExecl"
          class="flex items-center gap-1.5 px-3 py-2 bg-emerald-50 hover:bg-emerald-100 border border-emerald-200 text-emerald-700 rounded-xl text-xs font-bold transition-all active:scale-95"
          title="Xuất file"
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
              d="M19.5 14.25v-2.625a3.375 3.375 0 0 0-3.375-3.375h-1.5A1.125 1.125 0 0 1 13.5 7.125v-1.5a3.375 3.375 0 0 0-3.375-3.375H8.25m6.75 12-3-3m0 0-3 3m3-3v6m-1.5-15H5.625c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125V11.25a9 9 0 0 0-9-9Z"
            />
          </svg>
          Excel
        </button>

        <router-link
          to="/admin/khach-hang/add"
          class="flex items-center justify-center gap-1.5 rounded-xl bg-indigo-600 px-3.5 py-2 font-medium text-white text-xs shadow-sm hover:bg-indigo-700 transition whitespace-nowrap"
        >
          <span class="text-base leading-none">+</span>
          <span>Thêm mới</span>
        </router-link>
      </div>
    </div>

    <!-- Bảng dữ liệu mở rộng -->
    <div class="overflow-x-auto bg-white border border-slate-100 rounded-2xl shadow-sm">
      <table class="w-full border-collapse text-left table-auto">
        <thead>
          <tr
            class="bg-slate-50 border-b border-slate-100 text-[10px] font-bold text-slate-400 uppercase tracking-wider select-none"
          >
            <th class="py-3 px-3 text-center w-10">#</th>
            <th class="py-3 px-3 text-center">Ảnh</th>
            <th class="py-3 px-3 min-w-[150px]">Mã & Họ tên</th>
            <th class="py-3 px-3 min-w-[180px]">Tài khoản & Liên lạc</th>
            <th class="py-3 px-3">Hạng</th>
            <th class="py-3 px-3 min-w-[100px]">Chi tiêu & Mua</th>
            <th class="py-3 px-3">Xu & Tương tác</th>
            <th class="py-3 px-3 min-w-[200px]">Địa chỉ</th>
            <th class="py-3 px-3 text-center">Giới tính</th>
            <th class="py-3 px-3 text-center">Trạng thái</th>
            <th class="py-3 px-3 text-center">Thao tác</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-slate-100 text-xs">
          <tr
            v-for="(item, index) in listKhachHang"
            :key="item.id"
            class="hover:bg-slate-50/60 transition-colors"
          >
            <td class="py-3 px-3 text-center font-bold font-mono text-slate-400">
              {{ pageNo * pageSize + index + 1 }}
            </td>
            <td class="py-3 px-3 text-center">
              <div
                class="w-8 h-8 rounded-full overflow-hidden border border-slate-200 bg-slate-50 flex items-center justify-center"
              >
                <img
                  v-if="item.anh"
                  :src="`http://localhost:8080/api/shop-ban-quan-ao/khach-hang/uploads/${item.anh}`"
                  class="w-full h-full object-cover"
                />
                <span v-else class="text-[8px] font-bold text-slate-400">Null</span>
              </div>
            </td>
            <!-- Cột Họ Tên: Bỏ truncate, cho dãn -->
            <td class="py-3 px-3">
              <div class="font-bold text-slate-800">{{ item.hoTen || '---' }}</div>
              <div class="text-[10px] font-mono text-indigo-600 font-semibold">
                {{ item.maKhachHang || '---' }}
              </div>
            </td>
            <!-- Cột Liên lạc: Bỏ truncate, cho dãn -->
            <td class="py-3 px-3">
              <div class="font-semibold text-slate-700 whitespace-normal">
                {{ item.email || item.soDienThoai || '---' }}
              </div>
              <div class="text-[10px] text-slate-400 font-mono mt-0.5">
                {{ item.tenTaiKhoan || 'TK trống' }}
              </div>
            </td>
            <td class="py-3 px-3">
              <span
                class="px-2 py-0.5 bg-amber-50 text-amber-700 font-extrabold rounded-lg border border-amber-200 text-[9px] uppercase whitespace-nowrap"
                >{{ item.hangThanhVien || 'Mới' }}</span
              >
            </td>
            <td class="py-3 px-3 whitespace-nowrap">
              <div class="font-extrabold text-emerald-600 font-mono text-[11px]">
                {{ formatCurrency(item.tongChiTieu) }}
              </div>
              <div class="text-[10px] text-slate-400">
                Mua: <b>{{ item.soLanMua ?? 0 }}</b>
              </div>
            </td>
            <td class="py-3 px-3 whitespace-nowrap">
              <div class="flex items-center gap-1 text-[10px] font-bold text-amber-600">
                🪙 {{ item.soDuXu ?? 0 }}
              </div>
              <div class="text-[9px] text-slate-400">
                ĐD: <b>{{ item.chuoiDiemDanh ?? 0 }}</b> | Lật: <b>{{ item.soLuotLatThe ?? 0 }}</b>
              </div>
            </td>
            <!-- Cột Địa chỉ: Bỏ truncate, cho dãn -->
            <td class="py-3 px-3 text-slate-500 text-[11px] leading-relaxed">
              {{ getHienThiDiaChi(item) }}
            </td>
            <td class="py-3 px-3 text-center whitespace-nowrap">
              <span
                :class="[
                  'px-2 py-0.5 text-[9px] font-bold uppercase rounded-lg border',
                  item.gioiTinh
                    ? 'bg-blue-50 text-blue-600 border-blue-100'
                    : 'bg-rose-50 text-rose-600 border-rose-100',
                ]"
              >
                {{ item.gioiTinh ? 'Nam' : 'Nữ' }}
              </span>
            </td>
            <td class="py-3 px-3 text-center">
              <span
                :class="[
                  'px-2 py-0.5 text-[9px] font-bold uppercase rounded-lg border',
                  item.trangThai
                    ? 'bg-emerald-50 text-emerald-600 border-emerald-100'
                    : 'bg-rose-50 text-rose-600 border-rose-100',
                ]"
              >
                {{ item.trangThai ? 'Hoạt động' : 'Ngừng' }}
              </span>
            </td>
            <td class="py-3 px-3 text-center">
              <div class="flex justify-center gap-1">
                <button
                  @click="detailKhachHang(item.id)"
                  class="p-1 text-slate-400 hover:text-indigo-600 transition-all"
                >
                  <svg
                    class="w-4 h-4"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2.5"
                    viewBox="0 0 24 24"
                  >
                    <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" />
                    <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" />
                  </svg>
                </button>
                <button
                  @click="deleteKhachHang(item)"
                  class="p-1 text-slate-400 hover:text-rose-600 transition-all"
                >
                  <svg
                    class="w-4 h-4"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2.5"
                    viewBox="0 0 24 24"
                  >
                    <polyline points="3 6 5 6 21 6" />
                    <path
                      d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"
                    />
                  </svg>
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Phân trang -->
    <div
      class="flex justify-between items-center bg-white px-5 py-3 rounded-2xl border border-slate-100 shadow-sm text-xs"
    >
      <div class="flex items-center gap-2 text-slate-500 font-medium">
        <span>Hiển thị</span>
        <select
          v-model="pageSize"
          @change="handleSizeChange"
          class="px-2 py-1 bg-slate-50 border border-slate-200 rounded-lg outline-none font-bold text-slate-700 text-xs cursor-pointer"
        >
          <option :value="5">5</option>
          <option :value="10">10</option>
          <option :value="20">20</option>
        </select>
        <span>dòng</span>
      </div>
      <div class="flex items-center gap-3 font-bold">
        <button
          class="px-3 py-1.5 bg-slate-50 hover:bg-slate-100 border border-slate-200 text-slate-600 rounded-xl disabled:opacity-40"
          :disabled="pageNo === 0"
          @click="changePage(-1)"
        >
          Trước
        </button>
        <span class="text-indigo-600 font-extrabold px-2">{{ pageNo + 1 }}</span>
        <button
          class="px-3 py-1.5 bg-slate-50 hover:bg-slate-100 border border-slate-200 text-slate-600 rounded-xl disabled:opacity-40"
          :disabled="listKhachHang.length < pageSize"
          @click="changePage(1)"
        >
          Sau
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue' // Import thêm watch
import { useRouter } from 'vue-router'
import Swal from 'sweetalert2'
import { useToast } from 'vue-toastification'
import {
  deleteKhachHangService,
  exportKhachHangExcelService,
  phanTrangKhachHangService,
  searchKhachHangService,
} from '@/service/KhachHangService'

const router = useRouter()
const toast = useToast()

const listKhachHang = ref([])
const pageNo = ref(0)
const pageSize = ref(5)
const keyword = ref('')
let searchTimeout = null // Biến lưu timeout cho debounce

// Logic Debounce cho tìm kiếm
watch(keyword, () => {
  clearTimeout(searchTimeout)
  searchTimeout = setTimeout(() => {
    searchKhachHang()
  }, 500) // Đợi 500ms sau khi ngừng gõ mới tìm
})

const formatCurrency = (value) => {
  return Number(value || 0).toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })
}

const getHienThiDiaChi = (item) => {
  if (item.diaChi && item.diaChi.trim() !== '') return item.diaChi
  const parts = [item.diaChiCuThe, item.phuong, item.quan, item.thanhPho].filter(Boolean)
  return parts.length > 0 ? parts.join(', ') : '---'
}

const fetchKhachHang = async () => {
  try {
    listKhachHang.value = await phanTrangKhachHangService(pageNo.value, pageSize.value)
  } catch (error) {
    console.error(error)
  }
}

const searchKhachHang = async () => {
  pageNo.value = 0
  if (!keyword.value.trim()) {
    await fetchKhachHang()
    return
  }
  try {
    listKhachHang.value = await searchKhachHangService(keyword.value, pageNo.value, pageSize.value)
  } catch (error) {
    console.error(error)
  }
}

const changePage = async (step) => {
  pageNo.value += step
  if (keyword.value.trim()) {
    listKhachHang.value = await searchKhachHangService(keyword.value, pageNo.value, pageSize.value)
  } else {
    fetchKhachHang()
  }
}

const handleSizeChange = () => {
  pageNo.value = 0
  if (keyword.value.trim()) {
    searchKhachHangService(keyword.value, pageNo.value, pageSize.value).then(
      (res) => (listKhachHang.value = res),
    )
  } else {
    fetchKhachHang()
  }
}

const resetSearch = () => {
  keyword.value = ''
  pageNo.value = 0
  fetchKhachHang()
}

const deleteKhachHang = async (item) => {
  const result = await Swal.fire({
    title: 'Xác nhận xóa?',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#d33',
    confirmButtonText: 'Đồng ý',
  })
  if (result.isConfirmed) {
    await deleteKhachHangService(item.id)
    toast.success('Thành công!')
    fetchKhachHang()
  }
}

const handleExportExecl = async () => {
  const result = await Swal.fire({
    title: 'Xuất file Excel?',
    icon: 'question',
    showCancelButton: true,
  })
  if (result.isConfirmed) {
    await exportKhachHangExcelService(keyword.value || '', '', '')
    toast.success('Đã xuất file!')
  }
}

const detailKhachHang = (id) => router.push(`/admin/khach-hang/edit/${id}`)

onMounted(fetchKhachHang)
</script>
