<template>
  <div class="space-y-6">
    <!-- HEADER -->
    <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
      <div>
        <h1 class="text-2xl font-bold text-slate-800">Quản lý Sản phẩm</h1>
        <p class="text-sm text-slate-500 mt-1">Xem danh sách sản phẩm trong hệ thống</p>
      </div>

      <button class="px-4 py-2 bg-indigo-600 hover:bg-indigo-700 text-white rounded-lg text-sm">
        ➕ Thêm sản phẩm
      </button>
    </div>

    <!-- TABLE -->
    <div class="bg-white rounded-xl border shadow-sm overflow-hidden">
      <div class="overflow-x-auto">
        <table class="w-full text-left">
          <!-- HEADER -->
          <thead>
            <tr class="bg-slate-50 border-b text-xs uppercase text-slate-600">
              <th class="px-6 py-4">Sản phẩm</th>
              <th class="px-6 py-4">Danh mục</th>
              <th class="px-6 py-4">Thương hiệu / Chất liệu</th>
              <th class="px-6 py-4">Giá bán</th>
              <th class="px-6 py-4 text-right">Thao tác</th>
            </tr>
          </thead>

          <!-- BODY -->
          <tbody class="divide-y text-sm">
            <tr v-for="product in filteredProducts" :key="product.id" class="hover:bg-slate-50">
              <!-- PRODUCT -->
              <td class="px-6 py-4">
                <div class="flex items-center gap-3">
                  <div
                    class="w-12 h-12 rounded-lg bg-slate-100 overflow-hidden flex items-center justify-center"
                  >
                    <img
                      v-if="product.image"
                      :src="getImageUrl(product.image)"
                      class="w-full h-full object-cover"
                    />
                    <span v-else>👕</span>
                  </div>

                  <div>
                    <p class="font-medium text-slate-900">
                      {{ product.tenSanPham }}
                    </p>
                  </div>
                </div>
              </td>

              <!-- CATEGORY -->
              <td class="px-6 py-4 text-slate-600">
                {{ product.tenDanhMuc }}
              </td>

              <!-- BRAND / MATERIAL -->
              <td class="px-6 py-4 text-slate-600">
                {{ product.tenThuongHieu }} / {{ product.tenChatLieu }}
              </td>

              <!-- PRICE -->
              <td class="px-6 py-4 font-medium text-slate-900">
                {{ formatPrice(product.giaBan) }}
              </td>

              <!-- ACTION -->
              <td class="px-6 py-4 text-right space-x-2">
                <button class="text-indigo-600 hover:bg-slate-100 p-2 rounded">✏️</button>
                <button class="text-rose-600 hover:bg-slate-100 p-2 rounded">🗑️</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getAllSanpham } from '@/service/SanphamService'

// ================= STATE =================
const products = ref([])
const searchQuery = ref('')

// ================= API LOAD =================
const loadData = async () => {
  try {
    const res = await getAllSanpham()
    products.value = res
  } catch (err) {
    console.error('Lỗi load sản phẩm:', err)
  }
}

onMounted(loadData)

// ================= FORMAT PRICE =================
const formatPrice = (value) => {
  if (!value) return '0 ₫'
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
  }).format(value)
}

// ================= IMAGE FIX (QUAN TRỌNG) =================
const getImageUrl = (path) => {
  if (!path) return ''
  if (path.startsWith('http')) return path
  return `http://localhost:8080${path}`
}

// ================= SEARCH FILTER =================
const filteredProducts = computed(() => {
  if (!searchQuery.value) return products.value

  return products.value.filter((p) =>
    p.tenSanPham?.toLowerCase().includes(searchQuery.value.toLowerCase()),
  )
})
</script>
