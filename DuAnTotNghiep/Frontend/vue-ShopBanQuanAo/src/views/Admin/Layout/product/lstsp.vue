<template>
  <div class="space-y-6 relative">
    <div
      class="p-6 bg-gradient-to-r from-purple-800 via-indigo-900 to-slate-900 rounded-xl shadow-lg flex justify-between items-center text-white"
    >
      <div>
        <h1 class="text-3xl font-bold tracking-tight">Quản lý sản phẩm</h1>
        <p class="text-sm opacity-80 mt-1">
          Danh sách sản phẩm hệ thống được cập nhật thời gian thực
        </p>
      </div>

      <button
        @click="openModal"
        class="flex items-center gap-2 px-6 py-3 bg-white text-indigo-900 rounded-full text-sm font-semibold hover:bg-gray-100 shadow-md transition duration-300 active:scale-95"
      >
        <span class="text-lg">＋</span> Thêm sản phẩm mới
      </button>
    </div>

    <div class="bg-slate-50/50 border border-slate-100 rounded-2xl p-4 shadow-sm">
      <table class="w-full border-separate" style="border-spacing: 0 12px">
        <thead>
          <tr class="text-xs uppercase text-slate-500 font-bold tracking-wider">
            <th class="px-6 py-2 text-left">Sản phẩm</th>
            <th class="px-6 py-2 text-left">Danh mục</th>
            <th class="px-6 py-2 text-left">Thương hiệu</th>
            <th class="px-6 py-2 text-left">Chất liệu</th>
            <th class="px-6 py-2 text-left">Giá</th>
            <th class="px-6 py-2 text-center">Thao tác</th>
          </tr>
        </thead>

        <tbody class="text-sm">
          <tr
            v-for="product in products"
            :key="product.id"
            class="bg-white hover:bg-slate-50/80 transition duration-150 shadow-sm border border-slate-100/60"
          >
            <td class="px-6 py-4 rounded-l-xl border-y border-l border-slate-100">
              <div class="flex items-center gap-4">
                <div
                  class="w-14 h-14 rounded-xl overflow-hidden bg-slate-50 border border-slate-100 flex items-center justify-center p-1 shrink-0 shadow-inner"
                >
                  <img
                    v-if="product.image"
                    :src="getImageUrl(product.image)"
                    class="w-full h-full object-cover rounded-lg"
                    alt="Product"
                  />
                  <span v-else class="text-3xl">👕</span>
                </div>

                <div>
                  <p class="font-bold text-slate-800 text-base line-clamp-1">
                    {{ product.tenSanPham }}
                  </p>
                  <p class="text-xs text-slate-400 mt-0.5 font-mono">ID: #{{ product.id }}</p>
                </div>
              </div>
            </td>

            <td class="px-6 py-4 border-y border-slate-100">
              <span
                class="inline-flex items-center px-3 py-1 text-xs font-semibold bg-sky-50 text-sky-600 rounded-full border border-sky-100/60"
              >
                {{ product.tenDanhMuc }}
              </span>
            </td>

            <td class="px-6 py-4 border-y border-slate-100">
              <div class="flex items-center gap-2">
                <div
                  class="w-6 h-6 rounded-full bg-violet-50 border border-violet-100 flex items-center justify-center text-xs font-bold text-violet-500 uppercase shrink-0"
                >
                  {{ product.tenThuongHieu ? product.tenThuongHieu.charAt(0) : 'B' }}
                </div>
                <span class="font-semibold text-slate-700">{{ product.tenThuongHieu }}</span>
              </div>
            </td>

            <td class="px-6 py-4 border-y border-slate-100">
              <span
                class="inline-flex items-center px-2.5 py-1 text-xs font-medium bg-amber-50 text-amber-700 rounded-lg border border-amber-100"
              >
                {{ product.tenChatLieu }}
              </span>
            </td>

            <td class="px-6 py-4 font-extrabold text-emerald-600 text-lg border-y border-slate-100">
              {{ formatPrice(product.giaBan) }}
            </td>

            <td
              class="px-6 py-4 text-center rounded-r-xl border-y border-r border-slate-100 space-x-2"
            >
              <button
                class="px-4 py-2 bg-indigo-50 hover:bg-indigo-600 text-indigo-600 hover:text-white font-medium rounded-xl text-xs transition-all duration-200 border border-indigo-100 shadow-sm"
              >
                Sửa
              </button>
              <button
                class="px-4 py-2 bg-rose-50 hover:bg-rose-600 text-rose-600 hover:text-white font-medium rounded-xl text-xs transition-all duration-200 border border-rose-100 shadow-sm"
              >
                Xóa
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="isModalOpen" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-slate-900/40 backdrop-blur-sm" @click="closeModal"></div>

      <div
        class="bg-white rounded-2xl shadow-2xl border border-slate-100 w-full max-w-2xl overflow-hidden transform transition-all z-10 animate-in fade-in zoom-in-95 duration-200"
      >
        <div
          class="px-6 py-4 bg-gradient-to-r from-purple-800 to-indigo-900 text-white flex justify-between items-center"
        >
          <div>
            <h3 class="text-lg font-bold">Thêm sản phẩm mới</h3>
            <p class="text-xs opacity-70">Nhập đầy đủ thông tin sản phẩm vào hệ thống</p>
          </div>
          <button
            @click="closeModal"
            class="text-white/70 hover:text-white bg-white/10 hover:bg-white/20 p-2 rounded-full transition"
          >
            ✕
          </button>
        </div>

        <form @submit.prevent="handleSubmit" class="p-6 space-y-5 max-h-[75vh] overflow-y-auto">
          <div class="space-y-2">
            <label class="text-xs font-bold uppercase tracking-wider text-slate-500"
              >Hình ảnh sản phẩm</label
            >
            <div class="flex items-center gap-4">
              <div
                class="w-24 h-24 rounded-xl border-2 border-dashed border-slate-200 bg-slate-50 flex items-center justify-center overflow-hidden shrink-0 relative group"
              >
                <img v-if="imagePreview" :src="imagePreview" class="w-full h-full object-cover" />
                <span v-else class="text-2xl text-slate-400">📸</span>

                <button
                  v-if="imagePreview"
                  @click.prevent="clearImage"
                  class="absolute inset-0 bg-black/40 text-white flex items-center justify-center opacity-0 group-hover:opacity-100 transition text-xs font-medium"
                >
                  Thay đổi
                </button>
              </div>

              <div class="flex-1">
                <label
                  class="inline-flex items-center px-4 py-2 bg-indigo-50 hover:bg-indigo-100 text-indigo-600 border border-indigo-100 font-medium rounded-xl text-xs cursor-pointer transition shadow-sm"
                >
                  <span>Chọn ảnh từ máy tính</span>
                  <input type="file" accept="image/*" class="hidden" @change="handleImageChange" />
                </label>
                <p class="text-xs text-slate-400 mt-1.5">
                  Hỗ trợ định dạng JPG, PNG, WEBP. Dung lượng tối đa 2MB.
                </p>
              </div>
            </div>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div class="space-y-1.5 md:col-span-2">
              <label class="text-xs font-bold text-slate-600 uppercase">Tên sản phẩm</label>
              <input
                v-model="form.tenSanPham"
                type="text"
                required
                placeholder="Ví dụ: Áo Sơ Mi Nam Tay Dài"
                class="w-full px-4 py-2.5 rounded-xl border border-slate-200 focus:outline-none focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100 transition text-sm"
              />
            </div>

            <div class="space-y-1.5">
              <label class="text-xs font-bold text-slate-600 uppercase">Giá bán (VND)</label>
              <input
                v-model.number="form.giaBan"
                type="number"
                required
                placeholder="Nhập giá tiền..."
                class="w-full px-4 py-2.5 rounded-xl border border-slate-200 focus:outline-none focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100 transition text-sm"
              />
            </div>

            <div class="space-y-1.5">
              <label class="text-xs font-bold text-slate-600 uppercase">Danh mục sản phẩm</label>
              <input
                v-model="form.tenDanhMuc"
                type="text"
                required
                placeholder="Ví dụ: Áo sơ mi"
                class="w-full px-4 py-2.5 rounded-xl border border-slate-200 focus:outline-none focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100 transition text-sm"
              />
            </div>

            <div class="space-y-1.5">
              <label class="text-xs font-bold text-slate-600 uppercase">Thương hiệu</label>
              <input
                v-model="form.tenThuongHieu"
                type="text"
                required
                placeholder="Ví dụ: Gucci, Chanel"
                class="w-full px-4 py-2.5 rounded-xl border border-slate-200 focus:outline-none focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100 transition text-sm"
              />
            </div>

            <div class="space-y-1.5">
              <label class="text-xs font-bold text-slate-600 uppercase">Chất liệu</label>
              <input
                v-model="form.tenChatLieu"
                type="text"
                required
                placeholder="Ví dụ: Cotton, lụa"
                class="w-full px-4 py-2.5 rounded-xl border border-slate-200 focus:outline-none focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100 transition text-sm"
              />
            </div>
          </div>

          <div class="flex items-center justify-end gap-3 pt-4 border-t border-slate-100">
            <button
              type="button"
              @click="closeModal"
              class="px-5 py-2.5 bg-slate-100 hover:bg-slate-200 text-slate-600 font-semibold rounded-xl text-xs transition"
            >
              Hủy bỏ
            </button>
            <button
              type="submit"
              class="px-5 py-2.5 bg-indigo-600 hover:bg-indigo-700 text-white font-semibold rounded-xl text-xs shadow-md shadow-indigo-200 transition"
            >
              Lưu sản phẩm
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAllSanpham } from '@/service/SanphamService'

// ==========================================
// QUAN LÝ CONFIG & DATA ĐANG CÓ
// ==========================================
const products = ref([])

const loadData = async () => {
  try {
    const res = await getAllSanpham()
    products.value = res
  } catch (err) {
    console.error('Lỗi khi lấy danh sách sản phẩm:', err)
  }
}

onMounted(loadData)

const formatPrice = (value) => {
  if (!value) return '0 ₫'
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
  }).format(value)
}

const getImageUrl = (path) => {
  if (!path) return ''
  if (path.startsWith('http')) return path
  return `http://localhost:8080${path}`
}

// ==========================================
// LOGIC MODAL & UPLOAD ẢNH (MỚI THÊM)
// ==========================================
const isModalOpen = ref(false)
const imagePreview = ref('')

// Tạo object trắng chứa dữ liệu form nhập vào
const form = ref({
  tenSanPham: '',
  tenDanhMuc: '',
  tenThuongHieu: '',
  tenChatLieu: '',
  giaBan: null,
  imageFile: null, // Lưu file thật để sau này gửi lên API
})

// Mở form
const openModal = () => {
  isModalOpen.value = true
}

// Đóng form + Reset toàn bộ dữ liệu đang gõ dở
const closeModal = () => {
  isModalOpen.value = false
  form.value = {
    tenSanPham: '',
    tenDanhMuc: '',
    tenThuongHieu: '',
    tenChatLieu: '',
    giaBan: null,
    imageFile: null,
  }
  imagePreview.value = ''
}

// Xử lý khi người dùng chọn ảnh từ máy tính
const handleImageChange = (event) => {
  const file = event.target.files[0]
  if (file) {
    form.value.imageFile = file
    // Tạo đường dẫn ảo để hiển thị hình ảnh xem trước ngay lập tức
    imagePreview.value = URL.createObjectURL(file)
  }
}

// Xóa ảnh đã chọn
const clearImage = () => {
  form.value.imageFile = null
  imagePreview.value = ''
}

// Xử lý khi nhấn nút "Lưu sản phẩm"
const handleSubmit = () => {
  console.log('Dữ liệu sẵn sàng gửi đi:', form.value)
  // Tại đây bạn viết hàm gọi API Thêm sản phẩm (vd: tạo service kết nối tới backend)

  // Thành công thì đóng modal
  closeModal()
}
</script>
