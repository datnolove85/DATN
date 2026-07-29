const API_URL = 'http://localhost:8080/don-hang'

// Lấy Token từ sessionStorage
const getToken = () => sessionStorage.getItem('token')

// Tạo Header dùng chung
const getHeaders = () => ({
  'Content-Type': 'application/json',
  Authorization: `Bearer ${getToken()}`,
})

const donHangService = {
  // 1. Lấy danh sách đơn hàng của khách
  async layDanhSachDonHang() {
    const response = await fetch(API_URL, {
      method: 'GET',
      headers: getHeaders(),
    })

    if (!response.ok) {
      throw new Error(await response.text())
    }

    return await response.json()
  },

  // 2. Lấy chi tiết đơn hàng (Dùng chung cho cả Khách hàng và Admin)
  async layChiTietDonHang(idHoaDon) {
    const response = await fetch(`${API_URL}/${idHoaDon}`, {
      method: 'GET',
      headers: getHeaders(),
    })

    if (!response.ok) {
      throw new Error(await response.text())
    }

    return await response.json()
  },

  // 3. Khách hàng xác nhận đã nhận hàng
  async xacNhanDaNhan(idHoaDon) {
    const response = await fetch(`${API_URL}/${idHoaDon}/xac-nhan-da-nhan`, {
      method: 'PUT',
      headers: getHeaders(),
    })

    if (!response.ok) {
      throw new Error(await response.text())
    }

    return await response.text()
  },

  // 4. Khách hàng tự hủy đơn hàng
  async huyDonHang(idHoaDon, lyDoHuy = '') {
    const response = await fetch(`${API_URL}/${idHoaDon}/huy-don`, {
      method: 'PUT',
      headers: getHeaders(),
      body: JSON.stringify({ lyDoHuy }),
    })

    if (!response.ok) {
      throw new Error(await response.text())
    }

    return await response.text()
  },
}

export default donHangService
