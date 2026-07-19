<template>
  <div class="max-w-[1400px] mx-auto px-4 md:px-8 pb-20">
    <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
      
      <!-- Product Card -->
      <div 
        v-for="p in displayProducts" 
        :key="p.id" 
        class="group cursor-pointer flex flex-col"
        @click="goToDetail(p.id)"
      >
        <!-- Image Area -->
        <div class="relative w-full aspect-[3/4] bg-slate-100 mb-3 overflow-hidden">
          <img 
            :src="getCurrentImage(p)" 
            class="w-full h-full object-cover transition-transform duration-700 group-hover:scale-105" 
          />
          
          <!-- Discount badge (matches sanpham.vue) -->
          <div
            v-if="p.dangGiamGia"
            class="absolute top-3 left-0 bg-red-600 text-white px-3 py-1 rounded-r-md text-xs font-bold shadow-md z-10"
          >
            Giảm {{ p.phanTramGiam ? p.phanTramGiam + '%' : (p.giaBan - p.giaSauGiam).toLocaleString('vi-VN') + 'đ' }}
          </div>

          <!-- Quick view icon (bottom right) -->
          <div class="absolute bottom-3 right-3 bg-white/90 p-1.5 rounded text-gray-700 opacity-0 group-hover:opacity-100 transition-opacity">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-4 h-4">
              <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 10.5V6a3.75 3.75 0 10-7.5 0v4.5m11.356-1.993l1.263 12c.07.665-.45 1.243-1.119 1.243H4.25a1.125 1.125 0 01-1.12-1.243l1.264-12A1.125 1.125 0 015.513 7.5h12.974c.576 0 1.059.435 1.119 1.007zM8.625 10.5a.375.375 0 11-.75 0 .375.375 0 01.75 0zm7.5 0a.375.375 0 11-.75 0 .375.375 0 01.75 0z" />
            </svg>
          </div>
        </div>

        <!-- Info Area -->
        <div class="flex-1 flex flex-col">
          <!-- Color Swatches -->
          <div v-if="p.colors && p.colors.length > 0" class="flex gap-1.5 mb-2 flex-wrap">
            <button
              v-for="(color, index) in p.colors.slice(0, 4)"
              :key="color.name"
              @click.stop="handleColorClick(p, color)"
              class="w-6 h-6 rounded-sm transition-all duration-150 flex-shrink-0 p-[2px]"
              :class="isColorSelected(p, color)
                ? 'ring-2 ring-offset-1 ring-blue-500 shadow-sm'
                : 'ring-1 ring-slate-200 hover:ring-blue-400'"
              :title="color.name"
            >
              <div 
                class="w-full h-full rounded-[2px] border border-slate-200"
                :style="{ backgroundColor: color.code || getColorCode(color.name) }"
              ></div>
            </button>
            <span v-if="p.colors.length > 4" class="text-[10px] text-gray-400 flex items-center">
              +{{ p.colors.length - 4 }}
            </span>
          </div>

          <!-- Product Name -->
          <h3 class="text-xs text-slate-800 font-medium line-clamp-1 mb-1">{{ p.tenSanPham }}</h3>
          
          <!-- Price row -->
          <div class="flex items-baseline gap-2 mt-auto flex-wrap">
            <span class="text-sm font-bold text-[#d72027] no-underline">
              {{ p.dangGiamGia ? p.giaSauGiam.toLocaleString('vi-VN') : p.giaBan.toLocaleString('vi-VN') }} ₫
            </span>
            <span v-if="p.dangGiamGia" class="text-xs text-slate-400 line-through decoration-slate-400">
              {{ p.giaBan.toLocaleString('vi-VN') }}đ
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- Explore Button -->
    <div class="mt-12 flex justify-center">
      <button @click="$router.push('/san-pham')" class="bg-[#2f404d] hover:bg-slate-800 text-white font-bold text-xs px-8 py-3 rounded-sm transition tracking-wider flex items-center gap-2 uppercase">
        Khám phá bộ sưu tập
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2.5" stroke="currentColor" class="w-4 h-4">
          <path stroke-linecap="round" stroke-linejoin="round" d="M13.5 4.5L21 12m0 0l-7.5 7.5M21 12H3" />
        </svg>
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { getAllSanPhamChiTiet } from '@/service/SanPhamChiTiet'

const router = useRouter()
const API_URL = 'http://localhost:8080'
const placeholder = 'https://via.placeholder.com/300'

const shopVariants = ref([])
const selectedColors = ref({})

const loadProducts = async () => {
  try {
    const data = await getAllSanPhamChiTiet()
    shopVariants.value = data.filter((item) => item.trangThai)
  } catch (error) {
    console.error("Lỗi lấy sản phẩm:", error)
  }
}

onMounted(() => {
  loadProducts()
})

const displayProducts = computed(() => {
  const groups = {}

  shopVariants.value.forEach((item) => {
    if (!groups[item.idSanPham]) {
      groups[item.idSanPham] = {
        id: item.idSanPham,
        tenSanPham: item.tenSanPham,
        image: item.images?.[0] || '',
        giaBan: item.giaBan,
        giaSauGiam: item.giaSauGiam ?? item.giaBan,
        dangGiamGia: item.dangGiamGia,
        phanTramGiam: item.phanTramGiam ?? 0,
        colors: [],
      }
    }

    const p = groups[item.idSanPham]

    if (item.giaBan < p.giaBan) p.giaBan = item.giaBan
    const currentPrice = item.giaSauGiam ?? item.giaBan
    if (currentPrice < p.giaSauGiam) p.giaSauGiam = currentPrice
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

  // Return only the first 4 products to match the design grid
  return Object.values(groups).slice(0, 4)
})



const getImageUrl = (img) => {
  if (!img || img === 'null') return placeholder
  if (img.startsWith('http')) return img
  return API_URL + (img.startsWith('/') ? img : '/' + img)
}

// selectedColors tracks which color NAME is selected per product

// Explicitly initialize first color for each product
watch(displayProducts, (products) => {
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
  return getImageUrl(img)
}

const isColorSelected = (p, color) => {
  return selectedColors.value[p.id] === color.name
}

const getColorCode = (name) => {
  if (!name) return '#ddd'
  const map = {
    'đen': '#222222', 'trắng': '#ffffff', 'đỏ': '#ef4444', 
    'xanh dương': '#3b82f6', 'xanh lam': '#3b82f6', 'xanh lá': '#22c55e', 
    'xanh lục': '#22c55e', 'vàng': '#eab308', 'xám': '#9ca3af', 'ghi': '#9ca3af',
    'hồng': '#ec4899', 'nâu': '#78350f', 'tím': '#a855f7', 'cam': '#f97316',
    'kem': '#fef3c7', 'be': '#f5f5dc'
  }
  const n = name.toLowerCase()
  for (const [key, value] of Object.entries(map)) {
    if (n.includes(key)) return value
  }
  return '#ddd'
}

const goToDetail = (id) => {
  router.push({ name: 'confirmbuy', params: { id: id } })
}
</script>
