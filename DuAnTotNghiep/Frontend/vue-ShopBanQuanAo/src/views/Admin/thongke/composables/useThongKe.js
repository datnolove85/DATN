import { ref } from 'vue'
import {
  getDashboard,
  getRevenue,
  getOrderStatus,
  getTopProducts,
  getTopCustomers,
} from '@/service/ThongKeService'

export function useThongKe() {
  const dashboard = ref({})
  const revenue = ref([])
  const orderStatus = ref([])
  const topProducts = ref([])
  const topCustomers = ref([])
  const loading = ref(false)

  // Hàm tính toán khoảng thời gian (from, to) dựa theo loại bộ lọc
  const getDateRange = (filterType, customStart, customEnd) => {
    const now = new Date()
    const formatDate = (date) => {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    }
    let from,
      to,
      groupBy = 'day'

    if (filterType === 'today') {
      from = to = formatDate(now)
      groupBy = 'hour'
    } else if (filterType === 'month') {
      const firstDay = new Date(now.getFullYear(), now.getMonth(), 1)
      from = formatDate(firstDay)
      to = formatDate(now)
      groupBy = 'day'
    } else if (filterType === 'year') {
      const firstDay = new Date(now.getFullYear(), 0, 1)
      from = formatDate(firstDay)
      to = formatDate(now)
      groupBy = 'month'
    } else if (filterType === 'custom' && customStart && customEnd) {
      from = customStart
      to = customEnd
      groupBy = 'day'
    } else {
      // Mặc định năm hiện tại
      from = `${now.getFullYear()}-01-01`
      to = formatDate(now)
      groupBy = 'month'
    }

    return { from, to, groupBy }
  }

  // Thêm tham số loaiHoaDon vào loadAll (mặc định là rỗng để lấy tất cả)
  const loadAll = async (
    filterType = 'month',
    customStart = null,
    customEnd = null,
    loaiHoaDon = '',
  ) => {
    loading.value = true

    // Lấy khoảng thời gian từ bộ lọc
    const { from, to, groupBy } = getDateRange(filterType, customStart, customEnd)

    try {
      await Promise.all([
        getDashboard({ from, to, loaiHoaDon }).then((res) => (dashboard.value = res)),

        getRevenue({ from, to, groupBy, loaiHoaDon }).then((res) => (revenue.value = res)),

        getOrderStatus({ from, to, loaiHoaDon }).then((res) => (orderStatus.value = res)),

        getTopProducts({ from, to, limit: 10, loaiHoaDon }).then(
          (res) => (topProducts.value = res),
        ),

        getTopCustomers({ from, to, limit: 10, loaiHoaDon }).then(
          (res) => (topCustomers.value = res),
        ),
      ])
    } catch (error) {
      console.error('Lỗi tải dữ liệu thống kê:', error)
    } finally {
      loading.value = false
    }
  }

  return {
    dashboard,
    revenue,
    orderStatus,
    topProducts,
    topCustomers,
    loading,
    loadAll,
  }
}
