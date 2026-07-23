const BASE_URL = 'http://localhost:8080/thong-ke'

async function request(url) {
  const response = await fetch(BASE_URL + url)

  if (!response.ok) {
    throw new Error('Lỗi gọi API thống kê')
  }

  return await response.json()
}

// Hàm phụ trợ để nối query loaiHoaDon nếu được truyền vào
const appendKenhParam = (loaiHoaDon) => {
  return loaiHoaDon ? `&loaiHoaDon=${loaiHoaDon}` : ''
}

// dashboard
export const getDashboard = ({ from, to, loaiHoaDon }) => {
  return request(`/dashboard?from=${from}&to=${to}${appendKenhParam(loaiHoaDon)}`)
}

// doanh thu
export const getRevenue = ({ from, to, groupBy = 'day', loaiHoaDon }) => {
  return request(
    `/doanh-thu?from=${from}&to=${to}&groupBy=${groupBy}${appendKenhParam(loaiHoaDon)}`,
  )
}

// trạng thái đơn
export const getOrderStatus = ({ from, to, loaiHoaDon }) => {
  return request(`/trang-thai-don?from=${from}&to=${to}${appendKenhParam(loaiHoaDon)}`)
}

// top sản phẩm
export const getTopProducts = ({ from, to, limit = 10, loaiHoaDon }) => {
  return request(`/top-san-pham?from=${from}&to=${to}&limit=${limit}${appendKenhParam(loaiHoaDon)}`)
}

// top khách hàng
export const getTopCustomers = ({ from, to, limit = 10, loaiHoaDon }) => {
  return request(
    `/top-khach-hang?from=${from}&to=${to}&limit=${limit}${appendKenhParam(loaiHoaDon)}`,
  )
}
