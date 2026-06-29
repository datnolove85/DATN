<template>
  <div class="max-w-[1400px] mx-auto p-4 md:p-6 bg-gray-50 min-h-screen">
    <div class="flex flex-col lg:flex-row gap-8">
      <aside class="w-full lg:w-72 shrink-0">
        <div class="bg-white p-6 rounded-2xl border border-gray-100 shadow-sm sticky top-4">
          <h3 class="font-bold text-lg mb-6 flex items-center gap-2">
            <span class="w-1 h-5 bg-blue-600 rounded-full"></span> Bộ lọc
          </h3>

          <input
            v-model="filters.keyword"
            placeholder="Tên sản phẩm..."
            class="w-full pl-4 pr-4 py-2.5 bg-gray-50 border-none rounded-xl focus:ring-2 focus:ring-blue-500 outline-none text-sm mb-6"
          />

          <div class="space-y-6">
            <div v-for="cat in dynamicFilters" :key="cat.label">
              <p class="text-sm font-semibold mb-3 text-gray-700">{{ cat.label }}</p>
              <div
                v-for="opt in cat.options"
                :key="opt.id"
                class="flex items-center gap-3 text-sm text-gray-600 mb-2 cursor-pointer"
              >
                <input
                  type="checkbox"
                  :value="opt.name"
                  v-model="filters[cat.key]"
                  class="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                />
                {{ opt.name }}
              </div>
            </div>
          </div>
        </div>
      </aside>

      <main class="flex-1">
        <div
          class="bg-white p-4 mb-6 rounded-2xl border border-gray-100 flex justify-between items-center shadow-sm"
        >
          <span class="text-sm text-gray-600"
            >Tìm thấy <strong class="text-black">{{ filteredProducts.length }}</strong> sản
            phẩm</span
          >
        </div>

        <div class="grid grid-cols-2 md:grid-cols-3 xl:grid-cols-4 gap-6">
          <div
            v-for="p in filteredProducts"
            :key="p.id"
            @click="goToDetail(p.id)"
            class="group bg-white p-3 rounded-2xl border border-gray-100 hover:border-blue-200 hover:shadow-xl transition-all duration-300 flex flex-col"
          >
            <div class="relative overflow-hidden rounded-xl mb-3 h-56 bg-gray-100">
              <img
                :src="p.image ? API_URL + p.image : placeholder"
                class="w-full h-full object-cover"
              />
            </div>

            <h3 class="text-sm text-gray-700 font-medium truncate">{{ p.tenSanPham }}</h3>
            <p class="text-[10px] text-gray-400 mb-2">
              {{ p.idDanhMuc?.tenDanhMuc }} | {{ p.idThuongHieu?.tenThuongHieu }}
            </p>

            <div class="mt-auto pt-2 flex justify-between items-center">
              <span class="text-blue-600 font-bold text-xs">Còn lại: {{ p.soLuong }}</span>
              <button
                class="bg-gray-900 text-white px-3 py-1.5 rounded-lg text-[10px] font-semibold"
              >
                Chi tiết
              </button>
            </div>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { getAllSanpham } from '@/service/SanphamService'
import { getAllDanhMuc } from '@/service/DanhMucService'
import { getAllThuongHieu } from '@/service/ThuongHieuService'
import { getAllChatLieu } from '@/service/ChatLieuService'
import { useRouter } from 'vue-router'

const API_URL = 'http://localhost:8080'
const placeholder = 'https://via.placeholder.com/300'
const router = useRouter()

const goToDetail = (id) => {
  // Điều hướng bằng 'name' mà bạn đã định nghĩa trong router/index.js
  router.push({
    name: 'confirmbuy',
    params: { id: id },
  })
}
const products = ref([])
const filters = reactive({
  keyword: '',
  categories: [],
  brands: [],
  materials: [],
})

// Store dữ liệu từ API để hiển thị bộ lọc
const dynamicFilters = ref([
  { key: 'categories', label: 'Danh mục', options: [] },
  { key: 'brands', label: 'Thương hiệu', options: [] },
  { key: 'materials', label: 'Chất liệu', options: [] },
])

onMounted(async () => {
  const [pData, dData, tData, cData] = await Promise.all([
    getAllSanpham(),
    getAllDanhMuc(),
    getAllThuongHieu(),
    getAllChatLieu(),
  ])
  products.value = pData.filter((p) => p.trangThai === true)
  // Gán data vào bộ lọc
  dynamicFilters.value[0].options = dData.map((i) => ({ id: i.id, name: i.tenDanhMuc }))
  dynamicFilters.value[1].options = tData.map((i) => ({ id: i.id, name: i.tenThuongHieu }))
  dynamicFilters.value[2].options = cData.map((i) => ({ id: i.id, name: i.tenChatLieu }))
})

const filteredProducts = computed(() => {
  return products.value.filter((p) => {
    const matchKeyword = p.tenSanPham.toLowerCase().includes(filters.keyword.toLowerCase())
    const matchCat =
      filters.categories.length === 0 || filters.categories.includes(p.idDanhMuc?.tenDanhMuc)
    const matchBrand =
      filters.brands.length === 0 || filters.brands.includes(p.idThuongHieu?.tenThuongHieu)
    const matchMat =
      filters.materials.length === 0 || filters.materials.includes(p.idChatLieu?.tenChatLieu)

    return matchKeyword && matchCat && matchBrand && matchMat
  })
})
</script>
