const API_URL = 'http://localhost:8080/don-hang'

const getToken = () => localStorage.getItem('token')

const getHeaders = () => ({
  'Content-Type': 'application/json',
  Authorization: `Bearer ${getToken()}`,
})

const donHangService = {
  // Lấy danh sách đơn hàng
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

  // Lấy chi tiết đơn hàng
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
}

export default donHangService
