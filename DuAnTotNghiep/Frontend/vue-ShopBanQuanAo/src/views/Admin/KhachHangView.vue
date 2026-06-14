<template>
  <div
    class="space-y-6 max-w-full mx-auto p-4 animate-fade-in bg-slate-50 text-slate-800 rounded-2xl selection:bg-indigo-100 selection:text-indigo-900 overflow-hidden"
  >
    <div
      class="relative p-8 bg-[#0b0f19] rounded-3xl text-white flex flex-col md:flex-row justify-between items-start md:items-center gap-6 shadow-sm overflow-hidden"
    >
      <div class="relative z-10">
        <span class="text-[10px] tracking-widest uppercase font-bold text-indigo-400"
          >Hệ thống quản trị thế hệ mới</span
        >
        <h1 class="text-2xl font-bold tracking-tight mt-1 text-white">Phân Hệ Khách Hàng</h1>
        <p class="text-xs text-slate-400 mt-1 flex items-center gap-2">
          <span class="relative flex h-2 w-2">
            <span
              class="animate-ping absolute inline-flex h-full w-full rounded-full bg-indigo-400 opacity-75"
            ></span>
            <span class="relative inline-block rounded-full h-2 w-2 bg-indigo-500"></span>
          </span>
          Hệ thống lưu trữ hồ sơ và định danh khách hàng thời gian thực.
        </p>
      </div>

      <div
        class="w-full md:w-auto flex flex-col sm:flex-row items-stretch sm:items-center gap-3 relative z-10"
      >
        <div
          class="relative flex items-center bg-white/5 border border-white/10 hover:border-indigo-500 rounded-2xl px-4 py-2 w-full sm:w-80 focus-within:bg-white/10 focus-within:border-indigo-500 transition-all group"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke-width="2.5"
            stroke="currentColor"
            class="w-4 h-4 text-slate-400 mr-2 group-focus-within:text-indigo-400"
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
            placeholder="Mã hoặc tên khách hàng..."
            class="bg-transparent text-white placeholder-slate-500 text-xs outline-none w-full font-semibold"
            @keyup.enter="searchKhachHang"
          />
          <button
            @click="searchKhachHang"
            class="ml-2 px-3 py-1 bg-indigo-600 hover:bg-indigo-500 text-white rounded-xl text-[10px] font-bold transition-colors shadow-sm"
          >
            TÌM
          </button>
        </div>

        <router-link
          to="/admin/khach-hang/add"
          class="flex items-center justify-center gap-2 px-5 py-3 bg-indigo-600 hover:bg-indigo-500 text-white rounded-2xl text-xs font-bold shadow-lg shadow-indigo-600/20 transition-all active:scale-95 whitespace-nowrap"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke-width="2.5"
            stroke="currentColor"
            class="w-4 h-4"
          >
            <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
          </svg>
          THÊM KHÁCH HÀNG
        </router-link>
      </div>
    </div>

    <div
      class="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-3 bg-white p-4 rounded-2xl border border-slate-100 shadow-sm"
    >
      <div class="flex items-center gap-2">
        <button
          @click="resetSearch"
          class="flex items-center gap-2 px-4 py-2.5 bg-slate-50 hover:bg-slate-100 border border-slate-200 text-slate-600 rounded-xl text-xs font-bold transition-all active:scale-95"
          title="Làm mới dữ liệu"
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
          class="flex items-center gap-2 px-4 py-2.5 bg-emerald-50 hover:bg-emerald-100 border border-emerald-200 text-emerald-700 rounded-xl text-xs font-bold transition-all active:scale-95"
          title="Xuất file Excel"
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
          Xuất dữ liệu Excel
        </button>
      </div>

      <span
        class="px-3 py-1 bg-indigo-50 text-indigo-600 font-extrabold rounded-xl text-[11px] border border-indigo-100"
      >
        Khối dữ liệu: {{ listKhachHang.length }} mục / trang
      </span>
    </div>

    <div class="overflow-x-auto bg-white border border-slate-100 rounded-2xl shadow-sm">
      <table class="w-full border-collapse text-left min-w-[1100px] table-auto">
        <thead>
          <tr
            class="bg-slate-50 border-b border-slate-100 text-[11px] font-bold text-slate-400 uppercase tracking-wider select-none"
          >
            <th class="py-4 px-4 text-center w-14">#</th>
            <th class="py-4 px-4 w-20 text-center">Hồ sơ ảnh</th>
            <th class="py-4 px-4">Họ và tên</th>
            <th class="py-4 px-4">Thông tin liên lạc</th>
            <th class="py-4 px-4 w-36">Tài khoản</th>
            <th class="py-4 px-4 max-w-xs">Địa chỉ thường trú</th>
            <th class="py-4 px-4 text-center w-28">Giới tính</th>
            <th class="py-4 px-4 text-center w-36">Trạng thái</th>
            <th class="py-4 px-4 text-center w-28">Thao tác</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-slate-100 text-xs">
          <tr
            v-for="(item, index) in listKhachHang"
            :key="item.id"
            class="hover:bg-slate-50/60 transition-colors group"
          >
            <td class="py-3.5 px-4 text-center font-bold font-mono text-slate-400">
              {{ pageNo * pageSize + index + 1 }}
            </td>

            <td class="py-3.5 px-4 text-center">
              <div class="inline-flex items-center justify-center">
                <div
                  class="w-11 h-11 rounded-full overflow-hidden border-2 border-slate-100 shadow-sm bg-slate-50 flex items-center justify-center"
                  v-if="item.anh"
                >
                  <img
                    :src="`http://localhost:8080/api/shop-ban-quan-ao/khach-hang/uploads/${item.anh}`"
                    class="w-full h-full object-cover"
                  />
                </div>
                <div
                  v-else
                  class="w-11 h-11 rounded-full bg-slate-50 border border-slate-200 flex items-center justify-center text-[9px] font-bold text-slate-400 text-center px-1 uppercase tracking-tight select-none"
                >
                  Empty
                </div>
              </div>
            </td>

            <td
              class="py-3.5 px-4 font-bold text-slate-800 capitalize text-nowrap group-hover:translate-x-1 transition-transform duration-300"
            >
              {{ item.hoTen }}
            </td>

            <td class="py-3.5 px-4 space-y-0.5">
              <div class="font-semibold text-slate-700 text-nowrap max-w-[160px] truncate">
                {{ item.email || item.soDienThoai }}
              </div>

              <div v-if="item.email" class="text-[10px] font-mono text-slate-400 tracking-wider">
                {{ item.soDienThoai }}
              </div>
            </td>

            <td class="py-3.5 px-4">
              <span
                class="inline-block px-2.5 py-1 bg-slate-50/80 text-slate-600 font-bold rounded-xl border border-slate-100 font-mono tracking-wide group-hover:bg-white group-hover:border-indigo-400 transition-all"
              >
                {{ item.tenTaiKhoan }}
              </span>
            </td>

            <td
              class="py-3.5 px-4 text-slate-500 max-w-[180px] truncate"
              :title="getHienThiDiaChi(item)"
            >
              {{ getHienThiDiaChi(item) }}
            </td>

            <td class="py-3.5 px-4 text-center">
              <span
                :class="[
                  'inline-flex items-center justify-center px-2.5 py-0.5 text-[10px] font-bold uppercase tracking-wider rounded-xl border min-w-[60px]',
                  item.gioiTinh
                    ? 'bg-blue-50 text-blue-600 border-blue-100'
                    : 'bg-rose-50 text-rose-600 border-rose-100',
                ]"
              >
                {{ item.gioiTinh ? 'Nam' : 'Nữ' }}
              </span>
            </td>

            <td class="py-3.5 px-4 text-center">
              <span
                :class="[
                  'inline-flex items-center gap-1.5 px-2.5 py-0.5 text-[10px] font-bold uppercase tracking-wider rounded-xl border transition-all duration-300',
                  item.trangThai
                    ? 'bg-emerald-50 text-emerald-600 border-emerald-100'
                    : 'bg-rose-50 text-rose-600 border-rose-100',
                ]"
              >
                <span
                  :class="[
                    'w-1 h-1 rounded-full',
                    item.trangThai ? 'bg-emerald-500 animate-pulse' : 'bg-rose-500',
                  ]"
                ></span>
                {{ item.trangThai ? 'Hoạt động' : 'Tạm ngừng' }}
              </span>
            </td>

            <td class="py-3.5 px-4">
              <div class="flex items-center justify-center gap-1">
                <button
                  @click="detailKhachHang(item.id)"
                  class="p-1.5 text-slate-400 hover:text-indigo-600 hover:bg-indigo-50 rounded-xl transition-all"
                  title="Sửa hồ sơ"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    class="w-4 h-4"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2.5"
                  >
                    <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" />
                    <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" />
                  </svg>
                </button>
                <button
                  @click="deleteKhachHang(item)"
                  class="p-1.5 text-slate-400 hover:text-rose-600 hover:bg-rose-50 rounded-xl transition-all"
                  title="Xóa tài khoản"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    class="w-4 h-4"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2.5"
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

          <tr v-if="listKhachHang.length === 0">
            <td colspan="9" class="py-20 text-center">
              <div class="flex flex-col items-center justify-center gap-2 text-slate-400">
                <div
                  class="w-12 h-12 rounded-2xl bg-slate-50 text-indigo-600 border border-slate-100 flex items-center justify-center text-base font-black"
                >
                  👤
                </div>
                <strong class="text-sm font-bold text-slate-800 mt-1"
                  >Không tìm thấy khách hàng</strong
                >
                <span class="text-xs"
                  >Không có dữ liệu khách hàng tương thích khớp với từ khóa tìm kiếm.</span
                >
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div
      class="flex flex-col sm:flex-row justify-between items-center gap-4 bg-white p-5 rounded-2xl border border-slate-100 shadow-sm"
    >
      <div class="text-xs font-semibold text-slate-500 flex items-center gap-2">
        <span>Hiển thị</span>
        <select
          v-model="pageSize"
          @change="handleSizeChange"
          class="px-2 py-1 bg-slate-50 border border-slate-200 rounded-lg outline-none font-bold text-slate-700 text-xs focus:border-indigo-500 cursor-pointer"
        >
          <option :value="5">5</option>
          <option :value="10">10</option>
          <option :value="20">20</option>
        </select>
        <span>khách hàng trên mỗi trang</span>
      </div>

      <div class="flex items-center gap-4 text-xs font-bold">
        <button
          class="flex items-center gap-1 px-4 py-2 bg-slate-50 hover:bg-slate-100 border border-slate-200 text-slate-600 rounded-xl disabled:opacity-40 disabled:hover:bg-slate-50 disabled:cursor-not-allowed transition-colors active:scale-95"
          :disabled="pageNo === 0"
          @click="changePage(-1)"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke-width="2.5"
            stroke="currentColor"
            class="w-3.5 h-3.5"
          >
            <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 19.5 8.25 12l7.5-7.5" />
          </svg>
          Trước
        </button>

        <div class="text-slate-400 font-medium">
          Trang
          <span
            class="text-indigo-600 font-extrabold font-mono text-sm bg-indigo-50 px-2 py-0.5 rounded-lg border border-indigo-100"
            >{{ pageNo + 1 }}</span
          >
        </div>

        <button
          class="flex items-center gap-1 px-4 py-2 bg-slate-50 hover:bg-slate-100 border border-slate-200 text-slate-600 rounded-xl disabled:opacity-40 disabled:hover:bg-slate-50 disabled:cursor-not-allowed transition-colors active:scale-95"
          :disabled="listKhachHang.length < pageSize"
          @click="changePage(1)"
        >
          Sau
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke-width="2.5"
            stroke="currentColor"
            class="w-3.5 h-3.5"
          >
            <path stroke-linecap="round" stroke-linejoin="round" d="m8.25 4.5 7.5 7.5-7.5 7.5" />
          </svg>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
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

// Tự động gộp địa chỉ linh hoạt nếu trường k.diaChi từ backend bị null
const getHienThiDiaChi = (item) => {
  if (item.diaChi && item.diaChi.trim() !== '') {
    return item.diaChi
  }

  const parts = []
  if (item.diaChiCuThe) parts.push(item.diaChiCuThe.trim())
  if (item.phuong) parts.push(item.phuong.trim())
  if (item.quan) parts.push(item.quan.trim())
  if (item.thanhPho) parts.push(item.thanhPho.trim())

  return parts.length > 0 ? parts.join(', ') : '---'
}

// Hàm tải danh sách phân trang gốc
const fetchKhachHang = async () => {
  try {
    listKhachHang.value = await phanTrangKhachHangService(pageNo.value, pageSize.value)
  } catch (error) {
    console.error('Lỗi load dữ liệu', error)
  }
}

// Hàm Tìm kiếm có bắn thông báo Toast tương tự bên Nhân viên
const searchKhachHang = async () => {
  pageNo.value = 0

  if (!keyword.value.trim()) {
    await fetchKhachHang()
    return
  }

  try {
    listKhachHang.value = await searchKhachHangService(keyword.value, pageNo.value, pageSize.value)
    toast.success('Tìm kiếm thành công!')
  } catch (error) {
    toast.error('Có lỗi xảy ra khi tìm kiếm!')
    console.error(error)
  }
}

// Hàm thay đổi số trang (giữ luồng dữ liệu tìm kiếm mượt mà, không lặp lại toast)
const changePage = async (step) => {
  pageNo.value += step

  if (keyword.value.trim()) {
    try {
      listKhachHang.value = await searchKhachHangService(
        keyword.value,
        pageNo.value,
        pageSize.value,
      )
    } catch (error) {
      console.error(error)
    }
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
    text: `Bạn có chắc chắn muốn xóa khách hàng ${item.hoTen}?`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#d33',
    cancelButtonColor: '#3085d6',
    confirmButtonText: 'Đồng ý xóa',
    cancelButtonText: 'Hủy',
  })

  if (result.isConfirmed) {
    try {
      await deleteKhachHangService(item.id)
      toast.success('Xóa khách hàng thành công!')
      if (keyword.value.trim()) {
        listKhachHang.value = await searchKhachHangService(
          keyword.value,
          pageNo.value,
          pageSize.value,
        )
      } else {
        fetchKhachHang()
      }
    } catch (error) {
      toast.error('Có lỗi xảy ra khi xóa!')
    }
  }
}

const handleExportExecl = async () => {
  const result = await Swal.fire({
    title: 'Xác nhận xuất file?',
    text: 'Hệ thống sẽ xuất danh sách khách hàng ra file Excel.',
    icon: 'question',
    showCancelButton: true,
    confirmButtonText: 'Đồng ý',
    cancelButtonText: 'Hủy',
  })

  if (result.isConfirmed) {
    try {
      await exportKhachHangExcelService(keyword.value || '', '', '')
      toast.success('Xuất file Excel thành công!')
    } catch (error) {
      toast.error('Lỗi khi xuất file!')
    }
  }
}

const detailKhachHang = (id) => {
  router.push({
    path: `/admin/khach-hang/edit/${id}`,
  })
}

onMounted(fetchKhachHang)
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
