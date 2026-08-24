<template>
  <div
    class="max-w-7xl mx-auto px-4 py-8 bg-gradient-to-br from-sky-50/50 via-white to-pink-50/30 min-h-screen"
  >
    <!-- Tiêu đề trang -->
    <div class="flex items-center justify-between mb-8 border-b border-indigo-100 pb-4">
      <div>
        <h1 class="text-2xl font-extrabold text-gray-800 tracking-tight">Sản Phẩm Yêu Thích</h1>
        <p class="text-sm text-gray-500 mt-1">Danh sách các món đồ bạn đã lưu lại để xem sau</p>
      </div>
      <span
        class="bg-indigo-100 text-indigo-700 text-sm font-bold px-3.5 py-1.5 rounded-full shadow-sm"
      >
        {{ danhSachYeuThich.length }} sản phẩm
      </span>
    </div>

    <!-- Trạng thái đang tải (Loading) -->
    <div v-if="loading" class="flex justify-center items-center py-20">
      <div class="animate-spin rounded-full h-10 w-10 border-b-2 border-indigo-500"></div>
    </div>

    <!-- Trạng thái trống (Không có sản phẩm nào) -->
    <div
      v-else-if="danhSachYeuThich.length === 0"
      class="text-center py-20 bg-white/80 backdrop-blur-md rounded-3xl shadow-sm border border-indigo-50"
    >
      <svg
        class="mx-auto h-16 w-16 text-indigo-300 mb-4"
        fill="none"
        viewBox="0 0 24 24"
        stroke="currentColor"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          stroke-width="1.5"
          d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"
        />
      </svg>
      <h3 class="text-lg font-bold text-gray-800">Chưa có sản phẩm yêu thích nào</h3>
      <p class="text-sm text-gray-500 mt-1 mb-6">
        Hãy dạo một vòng cửa hàng và thả tim cho những món đồ bạn thích nhé!
      </p>
      <router-link
        to="/shop"
        class="inline-flex items-center px-5 py-2.5 bg-gradient-to-r from-indigo-500 to-violet-500 text-white text-sm font-semibold rounded-2xl hover:from-indigo-600 hover:to-violet-600 transition shadow-md shadow-indigo-200"
      >
        Khám phá ngay
      </router-link>
    </div>

    <!-- Grid danh sách sản phẩm -->
    <div v-else class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
      <div
        v-for="item in danhSachYeuThich"
        :key="item.idSanPham"
        class="bg-white/90 backdrop-blur-sm rounded-2xl border border-indigo-100 shadow-sm hover:shadow-xl hover:border-indigo-300 transition-all duration-300 flex flex-col overflow-hidden group"
      >
        <!-- Khung ảnh sản phẩm -->
        <div
          class="relative bg-sky-50 aspect-square overflow-hidden cursor-pointer"
          @click="chiTietSanPham(item.idSanPham)"
        >
          <img
            :src="item.hinhAnh ? `http://localhost:8080${item.hinhAnh}` : '/default-product.png'"
            :alt="item.tenSanPham"
            class="object-cover w-full h-full group-hover:scale-105 transition duration-500"
            @error="(e) => (e.target.src = 'https://via.placeholder.com/300')"
          />
          <!-- Badge Danh mục -->
          <span
            class="absolute top-3 left-3 bg-white/95 backdrop-blur-md text-xs font-bold text-indigo-700 px-2.5 py-1 rounded-lg shadow-sm border border-indigo-50"
          >
            {{ item.tenDanhMuc }}
          </span>

          <!-- Nút Xóa / Bỏ thích nhanh góc trên phải -->
          <button
            @click.stop="boThich(item.idSanPham)"
            class="absolute top-3 right-3 bg-white/95 hover:bg-rose-50 text-gray-400 hover:text-rose-500 p-2 rounded-full backdrop-blur-md shadow-sm border border-rose-100 transition"
            title="Bỏ thích"
          >
            <svg class="w-4 h-4 fill-current text-rose-500" viewBox="0 0 24 24">
              <path
                d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
              />
            </svg>
          </button>
        </div>

        <!-- Thông tin sản phẩm -->
        <div class="p-4 flex flex-col flex-grow justify-between">
          <div>
            <div class="text-xs font-bold text-indigo-500 uppercase tracking-wider mb-1">
              {{ item.tenThuongHieu }}
            </div>
            <h3
              class="font-semibold text-gray-800 text-sm line-clamp-2 hover:text-indigo-600 cursor-pointer transition"
              @click="chiTietSanPham(item.idSanPham)"
            >
              {{ item.tenSanPham }}
            </h3>
          </div>

          <div class="mt-4 pt-3 border-t border-sky-50 flex items-center justify-between">
            <div>
              <span class="text-xs text-gray-400 block">Giá từ</span>
              <span class="text-base font-extrabold text-indigo-600">
                {{ formatGia(item.giaBan) }}đ
              </span>
            </div>
            <button
              @click="chiTietSanPham(item.idSanPham)"
              class="px-3.5 py-1.5 bg-indigo-500 text-white text-xs font-semibold rounded-xl hover:bg-indigo-600 transition shadow-sm shadow-indigo-100"
            >
              Xem chi tiết
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import yeuThichService from '@/service/yeuThichService'

const router = useRouter()
const danhSachYeuThich = ref([])
const loading = ref(false)

// Lấy id khách hàng thực tế từ sessionStorage
const getCustomerId = () => {
  const userStr = sessionStorage.getItem('user')
  if (!userStr) return null
  const userObj = JSON.parse(userStr)
  return (
    userObj?.idKhachHang || userObj?.khachHangId || userObj?.khachHang?.id || userObj?.id || null
  )
}

const idKhachHang = ref(getCustomerId())

// Gọi API lấy danh sách khi component được mounted
const layDanhSachYeuThich = async () => {
  if (!idKhachHang.value) {
    console.warn('Chưa tìm thấy ID khách hàng trong sessionStorage!')
    loading.value = false
    return
  }

  loading.value = true
  try {
    const data = await yeuThichService.getDanhSachYeuThich(idKhachHang.value)
    danhSachYeuThich.value = data || []
  } catch (error) {
    console.error('Lỗi tải danh sách yêu thích:', error)
  } finally {
    loading.value = false
  }
}

// Xử lý bỏ thích sản phẩm ngay trên giao diện
const boThich = async (idSanPham) => {
  try {
    await yeuThichService.toggleYeuThich(idKhachHang.value, idSanPham)
    danhSachYeuThich.value = danhSachYeuThich.value.filter((item) => item.idSanPham !== idSanPham)
  } catch (error) {
    console.error('Lỗi khi bỏ thích:', error)
  }
}

// Chuyển hướng sang trang chi tiết sản phẩm
const chiTietSanPham = (idSanPham) => {
  router.push(`/spct/${idSanPham}`)
}

// Format giá tiền dạng số có dấu phẩy
const formatGia = (value) => {
  if (!value) return '0'
  return Number(value).toLocaleString('vi-VN')
}

onMounted(() => {
  layDanhSachYeuThich()
})
</script>
