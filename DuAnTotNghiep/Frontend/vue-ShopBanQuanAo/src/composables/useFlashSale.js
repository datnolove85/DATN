import { computed, ref, onMounted, onUnmounted } from 'vue'
import { getAllDotGiamGia, getSanPhamTrongDot } from '@/service/DotGiamGiaService'

const API_BASE = 'http://localhost:8080'

const toArray = (value) => {
  if (Array.isArray(value)) return value
  if (Array.isArray(value?.data)) return value.data
  if (Array.isArray(value?.content)) return value.content
  return []
}

const num = (value) => {
  if (value === null || value === undefined || value === '') return 0
  const parsed = Number(String(value).replace(/[^0-9.-]+/g, ''))
  return Number.isFinite(parsed) ? parsed : 0
}

const normalizeImage = (path) => {
  if (!path) return ''
  const text = String(path)
  if (/^(https?:|data:|blob:)/i.test(text)) return text
  return `${API_BASE}${text.startsWith('/') ? '' : '/'}${text}`
}

const campaignStatus = (campaign) => {
  const explicit = campaign?.trangThai
  if (explicit === 'tam_dung') return 'tam_dung'

  const start = campaign?.ngayBatDau ? new Date(campaign.ngayBatDau).getTime() : NaN
  const end = campaign?.ngayKetThuc ? new Date(campaign.ngayKetThuc).getTime() : NaN
  const now = Date.now()

  if (Number.isFinite(start) && now < start) return 'sap_dien_ra'
  if (Number.isFinite(end) && now > end) return 'da_ket_thuc'
  if (Number.isFinite(start) && Number.isFinite(end)) return 'dang_dien_ra'
  return explicit || 'dang_dien_ra'
}

const mapProduct = (item, campaign) => {
  const original = num(item.giaGoc)
  const sale = num(item.giaSauGiam) || original
  const discountPercent =
    num(item.phanTramGiam) ||
    (original > 0 && sale < original ? Math.round(((original - sale) / original) * 100) : 0)
  const stock = num(item.soLuongTon)
  const sold = num(item.daBan || item.soLuongDaBan)
  const total = sold + stock
  const soldPercent = total > 0 ? Math.min(Math.round((sold / total) * 100), 100) : 30

  return {
    id: item.idSanPham || item.idSanPhamChiTiet || item.id,
    productId: item.idSanPham || item.idSanPhamChiTiet || item.id,
    tenSanPham: item.tenSanPham || 'Sản phẩm',
    image: normalizeImage(item.anh),
    minPrice: sale,
    originalPrice: original > sale ? original : 0,
    discountPercent,
    statusText:
      sold > 0 ? `Đã bán: ${sold}` : stock > 0 && stock <= 10 ? 'Sắp cháy hàng' : 'Đang giảm giá',
    soldPercent,
    danhGia: num(item.danhGia) || 5,
    luotDanhGia: num(item.luotDanhGia),
    maSPCT: item.maSPCT,
    mauSac: item.mauSac,
    kichThuoc: item.kichThuoc,
    idDotGiamGia: campaign?.id,
  }
}

export function useFlashSale() {
  const campaigns = ref([])
  const products = ref([])
  const currentCampaign = ref(null)
  const loading = ref(false)
  const error = ref('')
  let timer = null

  const activeCampaigns = computed(() =>
    campaigns.value.filter((item) => campaignStatus(item) === 'dang_dien_ra'),
  )
  const upcomingCampaigns = computed(() =>
    campaigns.value.filter((item) => campaignStatus(item) === 'sap_dien_ra'),
  )

  const selectCampaign = () => {
    const active = [...activeCampaigns.value].sort(
      (a, b) => new Date(a.ngayKetThuc) - new Date(b.ngayKetThuc),
    )[0]
    const upcoming = [...upcomingCampaigns.value].sort(
      (a, b) => new Date(a.ngayBatDau) - new Date(b.ngayBatDau),
    )[0]
    currentCampaign.value = active || upcoming || null
  }

  const load = async () => {
    loading.value = true
    error.value = ''
    try {
      const rawCampaigns = toArray(await getAllDotGiamGia())
      campaigns.value = rawCampaigns.map((item) => ({
        ...item,
        trangThaiThucTe: campaignStatus(item),
      }))
      selectCampaign()

      if (!currentCampaign.value?.id) {
        products.value = []
        return
      }

      const rawProducts = toArray(await getSanPhamTrongDot(currentCampaign.value.id))
      products.value = rawProducts.map((item) => mapProduct(item, currentCampaign.value))
    } catch (err) {
      console.error('useFlashSale.load:', err)
      error.value = err?.message || 'Không thể tải dữ liệu Flash Sale'
      products.value = []
    } finally {
      loading.value = false
    }
  }

  const endTime = computed(() => {
    if (!currentCampaign.value) return null
    const status = campaignStatus(currentCampaign.value)
    return status === 'sap_dien_ra'
      ? currentCampaign.value.ngayBatDau
      : currentCampaign.value.ngayKetThuc
  })

  const isUpcoming = computed(() => campaignStatus(currentCampaign.value) === 'sap_dien_ra')

  onMounted(() => {
    load()
    timer = window.setInterval(load, 60000)
  })

  onUnmounted(() => {
    if (timer) window.clearInterval(timer)
  })

  return {
    campaigns,
    products,
    currentCampaign,
    activeCampaigns,
    upcomingCampaigns,
    loading,
    error,
    endTime,
    isUpcoming,
    reload: load,
  }
}
