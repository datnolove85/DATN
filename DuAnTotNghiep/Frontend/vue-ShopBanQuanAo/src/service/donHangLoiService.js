const BASE_URL = 'http://localhost:8080/api/don-hang-loi'

// Lấy token từ sessionStorage (khớp với trang đăng nhập của bạn)
const getAuthHeaders = () => {
  const token = sessionStorage.getItem('token')
  return {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
  }
}

export const donHangLoiService = {
  /**
   * 1. Tìm danh sách đơn hàng chứa sản phẩm/từ khóa lỗi
   */
  async timKiemDonHang(keyword = '', page = 0, size = 10) {
    try {
      const response = await fetch(
        `${BASE_URL}/tim-kiem?keyword=${encodeURIComponent(keyword)}&page=${page}&size=${size}`,
        {
          headers: getAuthHeaders(),
        },
      )

      if (!response.ok) {
        throw new Error('Không thể tải danh sách đơn hàng.')
      }
      return await response.json()
    } catch (error) {
      console.error('Lỗi khi gọi API tìm kiếm:', error)
      throw error
    }
  },

  /**
   * 2. Hủy 1 đơn hàng lẻ do lỗi
   */
  async huyDonLoiLe(id, lyDoLoi) {
    try {
      const response = await fetch(`${BASE_URL}/${id}/huy-loi`, {
        method: 'PUT',
        headers: getAuthHeaders(),
        body: JSON.stringify({ lyDoLoi }),
      })

      if (!response.ok) {
        const err = await response.json()
        throw new Error(err.message || 'Hủy đơn hàng thất bại.')
      }
      return await response.json()
    } catch (error) {
      console.error(`Lỗi khi hủy đơn ID ${id}:`, error)
      throw error
    }
  },

  /**
   * 3. Hủy HÀNG LOẠT nhiều đơn hàng cùng lúc
   */
  async huyHangLoatLoi(danhSachHoaDonId, lyDoLoi) {
    try {
      const response = await fetch(`${BASE_URL}/huy-hang-loat`, {
        method: 'POST',
        headers: getAuthHeaders(),
        body: JSON.stringify({
          danhSachHoaDonId,
          lyDoLoi,
        }),
      })

      if (!response.ok) {
        const err = await response.json()
        throw new Error(err.message || 'Hủy hàng loạt thất bại.')
      }
      return await response.json()
    } catch (error) {
      console.error('Lỗi khi hủy hàng loạt:', error)
      throw error
    }
  },
}
