<template>
  <div class="max-w-[1400px] mx-auto px-4 md:px-6 lg:px-8 py-8 bg-gradient-to-b from-white to-slate-50 min-h-screen">
    
    <!-- Breadcrumb -->
    <nav class="flex mb-6 text-sm text-slate-500 font-medium">
      <ol class="inline-flex items-center space-x-1 md:space-x-2">
        <li class="inline-flex items-center">
          <router-link to="/home" class="inline-flex items-center hover:text-slate-800 transition-colors">
            Trang chủ
          </router-link>
        </li>
        <li>
          <div class="flex items-center">
            <span class="mx-2 text-slate-400">></span>
            <span class="text-slate-900 font-bold">Sản phẩm</span>
          </div>
        </li>
      </ol>
    </nav>

    <div class="flex flex-col lg:flex-row gap-8">
      <aside class="w-full lg:w-72 shrink-0">
        <div class="bg-white sticky top-24 border border-slate-200 rounded-sm">
          <!-- Header -->
          <div class="flex items-center gap-3 px-5 pt-5 pb-3">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.8" stroke="currentColor" class="w-5 h-5 text-slate-800">
              <path stroke-linecap="round" stroke-linejoin="round" d="M10.5 6h9.75M10.5 6a1.5 1.5 0 1 1-3 0m3 0a1.5 1.5 0 1 0-3 0M3.75 6H7.5m3 12h9.75m-9.75 0a1.5 1.5 0 0 1-3 0m3 0a1.5 1.5 0 0 0-3 0m-3.75 0H7.5m9-6h3.75m-3.75 0a1.5 1.5 0 0 1-3 0m3 0a1.5 1.5 0 0 0-3 0m-9.75 0h9.75" />
            </svg>
            <span class="font-bold text-base text-slate-900 uppercase tracking-wider">Bộ lọc</span>
            <button
              v-if="hasActiveFilters"
              @click="clearFilters"
              class="ml-auto text-xs font-medium text-red-500 hover:text-red-700 transition-colors"
            >
              Xóa tất cả
            </button>
          </div>

          <!-- Search -->
          <div class="px-5 pb-5">
            <input
              v-model="filters.keyword"
              placeholder="Tìm kiếm sản phẩm..."
              class="w-full px-4 py-2.5 bg-white border border-slate-200 rounded-sm focus:ring-1 focus:ring-slate-400 focus:border-slate-400 outline-none text-sm transition-all placeholder:text-slate-400"
            />
          </div>

          <!-- Filter Sections -->
          <div v-for="(cat, catIdx) in dynamicFilters" :key="cat.label">
            <!-- Top divider for every section -->
            <div class="border-t border-slate-200"></div>

            <div
              class="flex items-center justify-between px-5 py-4 cursor-pointer select-none hover:bg-slate-50/60 transition-colors"
              @click="toggleFilter(cat.key)"
            >
              <span class="font-bold text-[13px] text-slate-900 uppercase tracking-wider">{{ cat.label }}</span>
              <svg
                xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor"
                class="w-4 h-4 text-slate-400 transition-transform duration-300"
                :class="expandedFilters[cat.key] ? 'rotate-180' : ''"
              >
                <path stroke-linecap="round" stroke-linejoin="round" d="m19.5 8.25-7.5 7.5-7.5-7.5" />
              </svg>
            </div>

            <!-- Collapsible content -->
            <div
              v-show="expandedFilters[cat.key]"
              class="px-5 pb-5"
            >
              <div
                v-for="opt in cat.options"
                :key="opt.id"
                class="flex items-center gap-3 text-sm text-slate-700 py-2 cursor-pointer hover:text-slate-900 transition-colors"
              >
                <input
                  type="checkbox"
                  :value="opt.name"
                  v-model="filters[cat.key]"
                  class="w-4 h-4 rounded-none border-slate-300 text-slate-900 focus:ring-slate-500 cursor-pointer"
                />
                <span>{{ opt.name }}</span>
              </div>
            </div>
          </div>
        </div>
      </aside>

      <!-- Main Content -->
      <main class="flex-1">
        <div
          class="bg-white px-6 py-4 mb-6 rounded-[20px] border border-slate-100 flex justify-between items-center shadow-[0_2px_10px_rgb(0,0,0,0.02)]"
        >
          <span class="text-sm text-slate-600">
            Tìm thấy <strong class="text-slate-900">{{ filteredProducts.length }}</strong> sản phẩm
          </span>
        </div>

        <div class="grid grid-cols-2 md:grid-cols-3 xl:grid-cols-4 gap-5">
          <div
            v-for="p in filteredProducts"
            :key="p.id"
            @click="goToDetail(p.id)"
            class="group bg-white rounded-[24px] border border-slate-100 shadow-[0_2px_10px_rgb(0,0,0,0.02)] hover:shadow-[0_12px_40px_rgb(0,0,0,0.06)] hover:border-slate-200 hover:-translate-y-1 transition-all duration-500 flex flex-col cursor-pointer overflow-hidden"
          >
            <!-- Image -->
            <div class="relative overflow-hidden h-64 bg-slate-50/50">
              <img
                :src="getCurrentImage(p)"
                class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
              />
              <div
                v-if="p.dangGiamGia"
                class="absolute top-4 left-0 bg-red-500/90 backdrop-blur-sm text-white px-3 py-1.5 rounded-r-xl text-[11px] font-bold shadow-sm z-10"
              >
                Giảm {{ p.phanTramGiam ? p.phanTramGiam + '%' : (p.giaBan - p.giaSauGiam).toLocaleString('vi-VN') + 'đ' }}
              </div>
            </div>

            <!-- Info -->
            <div class="p-4 flex flex-col flex-1">
              <!-- Color Swatches -->
              <div v-if="p.colors && p.colors.length > 0" class="flex gap-1.5 mb-3 flex-wrap">
                <button
                  v-for="(color) in p.colors"
                  :key="color.name"
                  @click.stop="handleColorClick(p, color)"
                  class="w-5 h-5 rounded-[6px] transition-all duration-300 flex-shrink-0 p-[2px]"
                  :class="isColorSelected(p, color)
                    ? 'ring-2 ring-offset-1 ring-blue-500 shadow-sm'
                    : 'ring-1 ring-slate-200 hover:ring-blue-400'"
                  :title="color.name"
                >
                  <div
                    class="w-full h-full rounded-[4px] border border-slate-200/60"
                    :style="{ backgroundColor: color.code || getColorCode(color.name) }"
                  ></div>
                </button>
              </div>

              <!-- Product Name -->
              <h3 class="text-[15px] text-slate-700 font-semibold leading-relaxed line-clamp-2 mb-2 group-hover:text-blue-600 transition-colors">{{ p.tenSanPham }}</h3>

              <!-- Price -->
              <div class="mt-auto">
                <template v-if="p.dangGiamGia">
                  <div class="flex items-baseline gap-2">
                    <span class="text-[17px] font-bold text-red-500">{{ p.giaSauGiam.toLocaleString('vi-VN') }}đ</span>
                    <span class="text-xs text-slate-400 line-through">{{ p.giaBan.toLocaleString('vi-VN') }}đ</span>
                  </div>
                </template>
                <template v-else>
                  <span class="text-[17px] font-bold text-slate-800">{{ p.giaBan.toLocaleString('vi-VN') }}đ</span>
                </template>
              </div>

              <!-- Meta -->
              <p class="text-xs text-slate-400 mt-2 font-medium">{{ p.tenDanhMuc }} <span class="mx-1 text-slate-300">•</span> {{ p.tenThuongHieu }}</p>

              <!-- Footer -->
              <div class="mt-4 pt-4 border-t border-slate-100 flex justify-between items-center">
                <span class="text-[11px] font-semibold text-emerald-600 bg-emerald-50/80 px-2.5 py-1 rounded-md">Còn: {{ p.soLuong }}</span>
                <button
                  class="bg-slate-50 text-slate-600 border border-slate-100 px-4 py-1.5 rounded-xl text-[11px] font-semibold hover:bg-slate-800 hover:text-white transition-all duration-300 hover:shadow-md"
                >
                  Chi tiết
                </button>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>


<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { getAllSanPhamChiTiet } from '@/service/SanPhamChiTiet'
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
const shopVariants = ref([])
const filters = reactive({
  keyword: '',
  categories: [],
  brands: [],
  materials: [],
})

// Collapsible filter sections — first one open by default
const expandedFilters = reactive({
  categories: true,
  brands: false,
  materials: false,
})

const toggleFilter = (key) => {
  expandedFilters[key] = !expandedFilters[key]
}


// Store dữ liệu từ API để hiển thị bộ lọc
const dynamicFilters = ref([
  { key: 'categories', label: 'Danh mục', options: [] },
  { key: 'brands', label: 'Thương hiệu', options: [] },
  { key: 'materials', label: 'Chất liệu', options: [] },
])


const loadProducts = async () => {
  const data = await getAllSanPhamChiTiet()


  shopVariants.value = data.filter((item) => item.trangThai)
}


const loadFilters = async () => {
  const [dData, tData, cData] = await Promise.all([
    getAllDanhMuc(),
    getAllThuongHieu(),
    getAllChatLieu(),
  ])


  dynamicFilters.value[0].options = dData.map((i) => ({
    id: i.id,
    name: i.tenDanhMuc,
  }))


  dynamicFilters.value[1].options = tData.map((i) => ({
    id: i.id,
    name: i.tenThuongHieu,
  }))


  dynamicFilters.value[2].options = cData.map((i) => ({
    id: i.id,
    name: i.tenChatLieu,
  }))
}
onMounted(async () => {
  await Promise.all([loadProducts(), loadFilters()])


  client.onConnect = () => {
    client.subscribe('/topic/pos', () => {
      loadProducts()
    })
  }


  client.activate()
})


// Kiểm tra xem có bộ lọc nào đang hoạt động không để hiển thị nút xóa
const hasActiveFilters = computed(() => {
  return (
    filters.keyword !== '' ||
    filters.categories.length > 0 ||
    filters.brands.length > 0 ||
    filters.materials.length > 0
  )
})


// Hàm reset toàn bộ filter về mặc định
const clearFilters = () => {
  filters.keyword = ''
  filters.categories = []
  filters.brands = []
  filters.materials = []
}
const filteredProducts = computed(() => {
  const groups = {}


  shopVariants.value.forEach((item) => {
    if (!groups[item.idSanPham]) {
      groups[item.idSanPham] = {
        id: item.idSanPham,


        tenSanPham: item.tenSanPham,


        tenDanhMuc: item.tenDanhMuc,


        tenThuongHieu: item.tenThuongHieu,


        tenChatLieu: item.tenChatLieu,


        image: item.images?.[0] || '',


        giaBan: item.giaBan,


        giaSauGiam: item.giaSauGiam ?? item.giaBan,


        dangGiamGia: item.dangGiamGia,


        phanTramGiam: item.phanTramGiam ?? 0,


        soLuong: 0,
        
        colors: [],
      }
    }


    const p = groups[item.idSanPham]


    p.soLuong += item.soLuongTon


    if (item.giaBan < p.giaBan) {
      p.giaBan = item.giaBan
    }


    const currentPrice = item.giaSauGiam ?? item.giaBan


    if (currentPrice < p.giaSauGiam) {
      p.giaSauGiam = currentPrice
    }


    if (item.dangGiamGia && (item.phanTramGiam ?? 0) > p.phanTramGiam) {
      p.phanTramGiam = item.phanTramGiam
      p.dangGiamGia = true
    }
    
    // Add unique color
    if (item.tenMauSac) {
      const colorExists = p.colors.find(c => c.name === item.tenMauSac)
      if (!colorExists) {
        p.colors.push({
          name: item.tenMauSac,
          code: item.maMauSac || item.maMau || '',
          image: item.images?.[0] || p.image
        })
      }
    }
  })


  return Object.values(groups).filter((p) => {
    const matchKeyword = p.tenSanPham.toLowerCase().includes(filters.keyword.toLowerCase())


    const matchCat = filters.categories.length === 0 || filters.categories.includes(p.tenDanhMuc)


    const matchBrand = filters.brands.length === 0 || filters.brands.includes(p.tenThuongHieu)


    const matchMat = filters.materials.length === 0 || filters.materials.includes(p.tenChatLieu)


    return matchKeyword && matchCat && matchBrand && matchMat
  })
})

// selectedColors tracks which color NAME is selected per product
const selectedColors = ref({})

// Explicitly initialize first color for each product
watch(filteredProducts, (products) => {
  const newColors = { ...selectedColors.value }
  let changed = false
  products.forEach((p) => {
    if (!newColors[p.id] && p.colors && p.colors.length > 0) {
      newColors[p.id] = p.colors[0].name
      changed = true
    }
  })
  if (changed) selectedColors.value = newColors
}, { immediate: true })

const handleColorClick = (product, color) => {
  selectedColors.value = {
    ...selectedColors.value,
    [product.id]: color.name
  }
}

const getCurrentImage = (p) => {
  const selectedName = selectedColors.value[p.id]
  const color = selectedName 
    ? p.colors?.find(c => c.name === selectedName) 
    : p.colors?.[0]

  const img = color?.image || p.image
  if (!img) return placeholder
  if (img.startsWith('http')) return img
  return API_URL + (img.startsWith('/') ? img : '/' + img)
}

const isColorSelected = (p, color) => {
  return selectedColors.value[p.id] === color.name
}

const getColorCode = (name) => {
  if (!name) return '#ddd'
  const map = {
    'đen': '#222222',
    'trắng': '#ffffff',
    'đỏ': '#ef4444',
    'xanh dương': '#3b82f6',
    'xanh lam': '#3b82f6',
    'xanh lá': '#22c55e',
    'xanh lục': '#22c55e',
    'vàng': '#eab308',
    'xám': '#9ca3af',
    'ghi': '#9ca3af',
    'hồng': '#ec4899',
    'nâu': '#78350f',
    'tím': '#a855f7',
    'cam': '#f97316',
    'kem': '#fef3c7',
    'be': '#f5f5dc'
  }
  const n = name.toLowerCase()
  for (const [key, value] of Object.entries(map)) {
    if (n.includes(key)) return value
  }
  return '#ddd'
}
</script>



