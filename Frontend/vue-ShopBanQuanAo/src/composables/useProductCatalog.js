import { computed, onBeforeUnmount, onMounted, reactive, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { Client } from '@stomp/stompjs'
import { getAllSanpham } from '@/service/SanphamService'
import { getAllSanPhamChiTiet } from '@/service/SanPhamChiTiet'
import { getAllDanhMuc } from '@/service/DanhMucService'
import { getAllThuongHieu } from '@/service/ThuongHieuService'
import { getAllChatLieu } from '@/service/ChatLieuService'
import stompClient from '@/socket'
const API_URL = 'http://localhost:8080'
const PLACEHOLDER =
  'data:image/svg+xml;charset=UTF-8,' +
  encodeURIComponent(`
    <svg xmlns="http://www.w3.org/2000/svg" width="720" height="900" viewBox="0 0 720 900">
      <defs>
        <linearGradient id="g" x1="0" x2="1" y1="0" y2="1">
          <stop offset="0" stop-color="#eef2ff"/>
          <stop offset="0.5" stop-color="#f8fafc"/>
          <stop offset="1" stop-color="#fce7f3"/>
        </linearGradient>
      </defs>
      <rect width="720" height="900" fill="url(#g)"/>
      <circle cx="600" cy="155" r="120" fill="#c7d2fe" opacity=".45"/>
      <circle cx="95" cy="760" r="150" fill="#fbcfe8" opacity=".38"/>
      <path d="M275 310h170l58 76-52 44-28-34v215H297V396l-28 34-52-44 58-76Z" fill="#cbd5e1"/>
      <text x="360" y="690" text-anchor="middle" font-family="Arial, sans-serif" font-size="28" fill="#64748b">Chưa có hình ảnh</text>
    </svg>
  `)

const toNumber = (value, fallback = 0) => {
  const number = Number(value)
  return Number.isFinite(number) ? number : fallback
}

const deriveDiscountPercent = (variant) => {
  const fromApi = toNumber(variant?.phanTramGiam)
  if (fromApi > 0) return Math.min(100, Math.round(fromApi))

  const original = toNumber(variant?.giaBan)
  const sale = toNumber(variant?.giaSauGiam, original)
  if (original <= 0 || sale >= original) return 0

  return Math.min(100, Math.round(((original - sale) / original) * 100))
}

const normalizeImagePath = (path) => {
  const image = String(path || '').trim()
  if (!image) return ''
  if (/^(https?:|data:|blob:)/i.test(image)) return image
  return `${API_URL}${image.startsWith('/') ? '' : '/'}${image}`
}

export function useProductCatalog(options = {}) {
  const router = useRouter()
  const pageSize = ref(options.pageSize || 12)

  const products = ref([])
  const loading = ref(true)
  const errorMessage = ref('')
  const mobileFiltersOpen = ref(false)
  const sortBy = ref('newest')
  const currentPage = ref(1)
  const gridColumns = ref(4)

  const catalogOptions = reactive({
    categories: [],
    brands: [],
    materials: [],
  })

  const filters = reactive({
    keyword: '',
    categories: [],
    brands: [],
    materials: [],
    priceRange: 'all',
    inStockOnly: false,
    onSaleOnly: false,
  })

  const dynamicFilters = computed(() => [
    { key: 'categories', label: 'Danh mục', options: catalogOptions.categories },
    { key: 'brands', label: 'Thương hiệu', options: catalogOptions.brands },
    { key: 'materials', label: 'Chất liệu', options: catalogOptions.materials },
  ])

  const hasActiveFilters = computed(
    () =>
      filters.keyword.trim() !== '' ||
      filters.categories.length > 0 ||
      filters.brands.length > 0 ||
      filters.materials.length > 0 ||
      filters.priceRange !== 'all' ||
      filters.inStockOnly ||
      filters.onSaleOnly,
  )

  const activeFilterChips = computed(() => {
    const chips = []

    if (filters.keyword.trim()) {
      chips.push({
        key: 'keyword',
        value: filters.keyword.trim(),
        label: `Từ khóa: ${filters.keyword.trim()}`,
      })
    }

    for (const key of ['categories', 'brands', 'materials']) {
      for (const value of filters[key]) {
        chips.push({ key, value, label: value })
      }
    }

    const priceLabels = {
      under300: 'Dưới 300.000đ',
      from300to500: '300.000đ – 500.000đ',
      from500to1000: '500.000đ – 1.000.000đ',
      over1000: 'Trên 1.000.000đ',
    }

    if (filters.priceRange !== 'all') {
      chips.push({
        key: 'priceRange',
        value: filters.priceRange,
        label: priceLabels[filters.priceRange] || 'Khoảng giá',
      })
    }

    if (filters.inStockOnly) {
      chips.push({ key: 'inStockOnly', value: true, label: 'Còn hàng' })
    }

    if (filters.onSaleOnly) {
      chips.push({ key: 'onSaleOnly', value: true, label: 'Đang giảm giá' })
    }

    return chips
  })

  const priceMatches = (price) => {
    if (filters.priceRange === 'under300') return price < 300000
    if (filters.priceRange === 'from300to500') return price >= 300000 && price <= 500000
    if (filters.priceRange === 'from500to1000') return price > 500000 && price <= 1000000
    if (filters.priceRange === 'over1000') return price > 1000000
    return true
  }

  const filteredProducts = computed(() => {
    const keyword = filters.keyword.trim().toLocaleLowerCase('vi')

    const result = products.value.filter((product) => {
      const name = String(product.tenSanPham || '').toLocaleLowerCase('vi')
      const code = String(product.maSanPham || '').toLocaleLowerCase('vi')
      const description = String(product.moTa || '').toLocaleLowerCase('vi')
      const brand = String(product.idThuongHieu?.tenThuongHieu || '').toLocaleLowerCase('vi')

      const matchKeyword =
        !keyword ||
        name.includes(keyword) ||
        code.includes(keyword) ||
        description.includes(keyword) ||
        brand.includes(keyword)

      const matchCategory =
        filters.categories.length === 0 ||
        filters.categories.includes(product.idDanhMuc?.tenDanhMuc)
      const matchBrand =
        filters.brands.length === 0 || filters.brands.includes(product.idThuongHieu?.tenThuongHieu)
      const matchMaterial =
        filters.materials.length === 0 ||
        filters.materials.includes(product.idChatLieu?.tenChatLieu)
      const matchPrice = priceMatches(toNumber(product.displayPrice))
      const matchStock = !filters.inStockOnly || toNumber(product.soLuong) > 0
      const matchSale = !filters.onSaleOnly || product.dangGiamGia

      return (
        matchKeyword &&
        matchCategory &&
        matchBrand &&
        matchMaterial &&
        matchPrice &&
        matchStock &&
        matchSale
      )
    })

    return [...result].sort((a, b) => {
      if (sortBy.value === 'name-asc') {
        return String(a.tenSanPham || '').localeCompare(String(b.tenSanPham || ''), 'vi')
      }

      if (sortBy.value === 'price-asc') {
        return (
          toNumber(a.displayPrice, Number.MAX_SAFE_INTEGER) -
          toNumber(b.displayPrice, Number.MAX_SAFE_INTEGER)
        )
      }

      if (sortBy.value === 'price-desc') {
        return toNumber(b.displayPrice) - toNumber(a.displayPrice)
      }

      if (sortBy.value === 'discount-desc') {
        return toNumber(b.maxDiscountPercent) - toNumber(a.maxDiscountPercent)
      }

      if (sortBy.value === 'stock-desc') {
        return toNumber(b.soLuong) - toNumber(a.soLuong)
      }

      if (sortBy.value === 'stock-asc') {
        return toNumber(a.soLuong) - toNumber(b.soLuong)
      }

      if (sortBy.value === 'oldest') {
        return new Date(a.ngayTao || 0).getTime() - new Date(b.ngayTao || 0).getTime()
      }

      return new Date(b.ngayTao || 0).getTime() - new Date(a.ngayTao || 0).getTime()
    })
  })

  const totalPages = computed(() =>
    Math.max(1, Math.ceil(filteredProducts.value.length / pageSize.value)),
  )

  const visibleProducts = computed(() => {
    const start = (currentPage.value - 1) * pageSize.value
    return filteredProducts.value.slice(start, start + pageSize.value)
  })

  const pageNumbers = computed(() => {
    const total = totalPages.value
    const current = currentPage.value
    const start = Math.max(1, Math.min(current - 2, Math.max(1, total - 4)))
    const end = Math.min(total, start + 4)

    return Array.from({ length: end - start + 1 }, (_, index) => start + index)
  })

  const stats = computed(() => ({
    products: products.value.length,
    brands: catalogOptions.brands.length,
    categories: catalogOptions.categories.length,
    onSale: products.value.filter((item) => item.dangGiamGia).length,
    inStock: products.value.filter((item) => toNumber(item.soLuong) > 0).length,
  }))

  const clearFilters = () => {
    filters.keyword = ''
    filters.categories.splice(0)
    filters.brands.splice(0)
    filters.materials.splice(0)
    filters.priceRange = 'all'
    filters.inStockOnly = false
    filters.onSaleOnly = false
    currentPage.value = 1
  }

  const removeFilter = (chip) => {
    if (chip.key === 'keyword') {
      filters.keyword = ''
      return
    }

    if (chip.key === 'priceRange') {
      filters.priceRange = 'all'
      return
    }

    if (chip.key === 'inStockOnly' || chip.key === 'onSaleOnly') {
      filters[chip.key] = false
      return
    }

    const index = filters[chip.key].indexOf(chip.value)
    if (index >= 0) filters[chip.key].splice(index, 1)
  }

  const goToDetail = (id) => {
    router.push({ name: 'confirmbuy', params: { id } })
  }

  const imageUrl = (product) => {
    const image = product?.image || product?.variantImage
    return normalizeImagePath(image) || PLACEHOLDER
  }

  const handleImageError = (event) => {
    if (event?.target && event.target.src !== PLACEHOLDER) {
      event.target.src = PLACEHOLDER
    }
  }

  const isNewProduct = (product) => {
    const createdAt = new Date(product?.ngayTao || 0).getTime()
    if (!createdAt) return false
    return Date.now() - createdAt <= 30 * 24 * 60 * 60 * 1000
  }

  const formatPrice = (value) => {
    const price = toNumber(value)
    if (price <= 0) return 'Liên hệ'
    return `${price.toLocaleString('vi-VN')}đ`
  }

  const priceLabel = (product) => {
    if (!product || toNumber(product.displayPrice) <= 0) return 'Liên hệ'
    const min = toNumber(product.minPrice)
    const max = toNumber(product.maxPrice)
    const prefix = max > min ? 'Từ ' : ''
    return `${prefix}${formatPrice(product.displayPrice)}`
  }

  const normalizeProducts = (rawProducts, rawVariants) => {
    const variantsByProduct = new Map()

    for (const variant of Array.isArray(rawVariants) ? rawVariants : []) {
      if (variant?.trangThai === false || variant?.trangThai === 0) continue
      const productId = Number(variant?.idSanPham)
      if (!Number.isFinite(productId)) continue

      const originalPrice = toNumber(variant.giaBan)
      const apiSalePrice = toNumber(variant.giaSauGiam, originalPrice)
      const salePrice =
        apiSalePrice > 0 ? Math.min(apiSalePrice, originalPrice || apiSalePrice) : originalPrice
      const percent = deriveDiscountPercent(variant)
      const normalizedVariant = {
        ...variant,
        originalPrice,
        salePrice,
        discountPercent: percent,
        isDiscounted:
          Boolean(variant.dangGiamGia) ||
          percent > 0 ||
          (originalPrice > 0 && salePrice > 0 && salePrice < originalPrice),
      }

      const list = variantsByProduct.get(productId) || []
      list.push(normalizedVariant)
      variantsByProduct.set(productId, list)
    }

    return (Array.isArray(rawProducts) ? rawProducts : []).map((product) => {
      const variants = variantsByProduct.get(Number(product.id)) || []
      const pricedVariants = variants.filter((item) => item.originalPrice > 0)
      const displayVariant = [...pricedVariants].sort((a, b) => a.salePrice - b.salePrice)[0]
      const discountVariants = pricedVariants.filter((item) => item.isDiscounted)
      const bestDiscountVariant = [...discountVariants].sort(
        (a, b) => b.discountPercent - a.discountPercent,
      )[0]

      const originalPrices = pricedVariants.map((item) => item.originalPrice)
      const salePrices = pricedVariants.map((item) => item.salePrice)
      const variantStock = variants.reduce((sum, item) => sum + toNumber(item.soLuongTon), 0)
      const variantImage = variants.find((item) => Array.isArray(item.images) && item.images[0])
        ?.images?.[0]

      return {
        ...product,
        variants,
        variantCount: variants.length,
        colorCount: new Set(variants.map((item) => item.idMauSac).filter(Boolean)).size,
        sizeCount: new Set(variants.map((item) => item.idKichThuoc).filter(Boolean)).size,
        displayPrice: displayVariant?.salePrice || 0,
        originalPrice: displayVariant?.originalPrice || 0,
        minPrice: salePrices.length ? Math.min(...salePrices) : 0,
        maxPrice: salePrices.length ? Math.max(...salePrices) : 0,
        minOriginalPrice: originalPrices.length ? Math.min(...originalPrices) : 0,
        maxOriginalPrice: originalPrices.length ? Math.max(...originalPrices) : 0,
        displayDiscountPercent: displayVariant?.discountPercent || 0,
        maxDiscountPercent: bestDiscountVariant?.discountPercent || 0,
        dangGiamGia: discountVariants.length > 0,
        variantImage,
        soLuong: toNumber(product.soLuong, variantStock) || variantStock,
      }
    })
  }

  const loadProducts = async () => {
    // /sanpham chứa thông tin sản phẩm tổng quát; /spct chứa giá và % giảm.
    // Gộp hai nguồn để trang danh sách luôn hiển thị đúng khuyến mãi.
    const rawProducts = await getAllSanpham()
    let rawVariants = []

    try {
      rawVariants = await getAllSanPhamChiTiet()
    } catch (error) {
      // Danh sách sản phẩm vẫn hiển thị nếu API biến thể tạm thời lỗi;
      // chỉ phần giá/khuyến mãi chuyển sang trạng thái "Liên hệ".
      console.warn('Không tải được giá và khuyến mãi từ /spct:', error)
    }

    products.value = normalizeProducts(rawProducts, rawVariants).filter(
      (product) => product?.trangThai === true || product?.trangThai === 1,
    )
  }

  const loadCatalogs = async () => {
    const [categories, brands, materials] = await Promise.all([
      getAllDanhMuc(),
      getAllThuongHieu(),
      getAllChatLieu(),
    ])

    catalogOptions.categories = (Array.isArray(categories) ? categories : [])
      .filter((item) => item?.trangThai !== false && item?.trangThai !== 0)
      .map((item) => ({ id: item.id, name: item.tenDanhMuc }))
    catalogOptions.brands = (Array.isArray(brands) ? brands : [])
      .filter((item) => item?.trangThai !== false && item?.trangThai !== 0)
      .map((item) => ({ id: item.id, name: item.tenThuongHieu }))
    catalogOptions.materials = (Array.isArray(materials) ? materials : [])
      .filter((item) => item?.trangThai !== false && item?.trangThai !== 0)
      .map((item) => ({ id: item.id, name: item.tenChatLieu }))
  }

  const reload = async () => {
    loading.value = true
    errorMessage.value = ''

    try {
      await Promise.all([loadProducts(), loadCatalogs()])
    } catch (error) {
      console.error('Không tải được danh sách sản phẩm:', error)
      errorMessage.value = error?.message || 'Không thể tải danh sách sản phẩm. Vui lòng thử lại.'
    } finally {
      loading.value = false
    }
  }

  watch(
    () => [
      filters.keyword,
      [...filters.categories],
      [...filters.brands],
      [...filters.materials],
      filters.priceRange,
      filters.inStockOnly,
      filters.onSaleOnly,
      sortBy.value,
    ],
    () => {
      currentPage.value = 1
    },
    { deep: true },
  )
  let subscription = null

  function subscribeProduct() {
    if (subscription) {
      subscription.unsubscribe()
    }

    subscription = stompClient.subscribe('/topic/pos', async (message) => {
      try {
        const event = JSON.parse(message.body)

        switch (event.type) {
          case 'PRODUCT_UPDATED':
          case 'DISCOUNT_UPDATED':
          case 'STOCK_UPDATED':
          case 'PAYMENT_SUCCESS':
            await loadProducts()
            break
        }
      } catch (err) {
        console.error(err)
      }
    })
  }

  function connectSocket() {
    if (stompClient.connected) {
      subscribeProduct()
    } else {
      stompClient.onConnect = () => {
        console.log('✅ Product socket connected')
        subscribeProduct()
      }
    }
  }

  watch(totalPages, (total) => {
    if (currentPage.value > total) currentPage.value = total
  })

  onMounted(async () => {
    await reload()
    connectSocket()
  })

  onBeforeUnmount(() => {
    subscription?.unsubscribe()
    subscription = null
  })

  return {
    filters,
    dynamicFilters,
    products,
    visibleProducts,
    filteredProducts,
    loading,
    errorMessage,
    hasActiveFilters,
    activeFilterChips,
    sortBy,
    currentPage,
    totalPages,
    pageNumbers,
    pageSize,
    stats,
    mobileFiltersOpen,
    gridColumns,
    clearFilters,
    removeFilter,
    goToDetail,
    imageUrl,
    handleImageError,
    isNewProduct,
    formatPrice,
    priceLabel,
    reload,
  }
}
