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

      <!-- ❗ CHỈ SỬA Ở ĐÂY -->
      <button
        @click="openAddModal"
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
              <!-- ❗ CHỈ SỬA NÚT SỬA -->
              <button
                @click="openEditModal(product)"
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

    <!-- MODAL -->
    <div v-if="isModalOpen" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-slate-900/40 backdrop-blur-sm" @click="closeModal"></div>

      <div
        class="bg-white rounded-2xl shadow-2xl border border-slate-100 w-full max-w-2xl overflow-hidden z-10"
      >
        <div
          class="px-6 py-4 bg-gradient-to-r from-purple-800 to-indigo-900 text-white flex justify-between items-center"
        >
          <div>
            <!-- ❗ CHỈ SỬA TITLE -->
            <h3 class="text-lg font-bold">
              {{ isEditMode ? 'Cập nhật sản phẩm' : 'Thêm sản phẩm mới' }}
            </h3>

            <p class="text-xs opacity-70">Nhập đầy đủ thông tin sản phẩm</p>
          </div>

          <button @click="closeModal" class="text-white/70 hover:text-white">✕</button>
        </div>

        <form @submit.prevent="handleSubmit" class="p-6 space-y-5 max-h-[75vh] overflow-y-auto">
          <div>
            <label class="text-xs font-bold uppercase text-slate-500">Tên sản phẩm</label>
            <input v-model="form.tenSanPham" class="w-full border p-2 rounded" />
          </div>

          <div>
            <label class="text-xs font-bold uppercase text-slate-500">Giá</label>
            <input v-model.number="form.giaBan" type="number" class="w-full border p-2 rounded" />
          </div>

          <div>
            <label class="text-xs font-bold uppercase text-slate-500">Danh mục ID</label>
            <input v-model.number="form.idDanhMuc" class="w-full border p-2 rounded" />
          </div>

          <div>
            <label class="text-xs font-bold uppercase text-slate-500">Thương hiệu ID</label>
            <input v-model.number="form.idThuongHieu" class="w-full border p-2 rounded" />
          </div>

          <div>
            <label class="text-xs font-bold uppercase text-slate-500">Chất liệu ID</label>
            <input v-model.number="form.idChatLieu" class="w-full border p-2 rounded" />
          </div>

          <div class="flex justify-end gap-2">
            <button type="button" @click="closeModal" class="px-4 py-2 bg-gray-200 rounded">
              Hủy
            </button>

            <button type="submit" class="px-4 py-2 bg-indigo-600 text-white rounded">Lưu</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAllSanpham } from '@/service/SanphamService'

const products = ref([])
const isModalOpen = ref(false)
const isEditMode = ref(false)
const imagePreview = ref('')

const form = ref({
  id: null,
  idDanhMuc: null,
  idThuongHieu: null,
  idChatLieu: null,
  maSanPham: '',
  tenSanPham: '',
  moTa: '',
  giaBan: null,
  imageFile: null,
})

const loadData = async () => {
  products.value = await getAllSanpham()
}

onMounted(loadData)

const formatPrice = (v) =>
  new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
  }).format(v || 0)

const getImageUrl = (p) => (p ? (p.startsWith('http') ? p : `http://localhost:8080${p}`) : '')

// ❗ CHỈ THÊM 2 HÀM NÀY
const openAddModal = () => {
  isEditMode.value = false
  isModalOpen.value = true
}

const openEditModal = (product) => {
  isEditMode.value = true
  isModalOpen.value = true

  form.value = {
    id: product.id,
    idDanhMuc: product.idDanhMuc,
    idThuongHieu: product.idThuongHieu,
    idChatLieu: product.idChatLieu,
    maSanPham: product.maSanPham,
    tenSanPham: product.tenSanPham,
    moTa: product.moTa,
    giaBan: product.giaBan,
    imageFile: null,
  }

  imagePreview.value = product.image ? getImageUrl(product.image) : ''
}

const closeModal = () => {
  isModalOpen.value = false
}

const handleImageChange = (e) => {
  const file = e.target.files[0]
  if (file) {
    form.value.imageFile = file
    imagePreview.value = URL.createObjectURL(file)
  }
}

const handleSubmit = async () => {
  const fd = new FormData()

  fd.append('idDanhMuc', form.value.idDanhMuc)
  fd.append('idThuongHieu', form.value.idThuongHieu)
  fd.append('idChatLieu', form.value.idChatLieu)
  fd.append('maSanPham', form.value.maSanPham)
  fd.append('tenSanPham', form.value.tenSanPham)
  fd.append('moTa', form.value.moTa)

  if (form.value.imageFile) {
    fd.append('image', form.value.imageFile)
  }

  const url = isEditMode.value
    ? `http://localhost:8080/sanpham/${form.value.id}`
    : `http://localhost:8080/sanpham`

  await fetch(url, {
    method: isEditMode.value ? 'PUT' : 'POST',
    body: fd,
  })

  await loadData()
  closeModal()
}
</script>
