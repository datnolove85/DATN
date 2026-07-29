const BASE_URL = 'http://localhost:8080/api/don-hang-loi'

export const donHangLoiService = {
  /**
   * 1. Tìm danh sách đơn hàng chứa sản phẩm/từ khóa lỗi
   * @param {string} keyword Từ khóa tìm kiếm (Tên/Mã sản phẩm)
   * @param {number} page Trang hiện tại (bắt đầu từ 0)
   * @param {number} size Số lượng bản ghi mỗi trang
   */
  async timKiemDonHang(keyword = '', page = 0, size = 10) {
    try {
      const response = await fetch(
        `${BASE_URL}/tim-kiem?keyword=${encodeURIComponent(keyword)}&page=${page}&size=${size}`,
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
   * @param {number} id ID của hóa đơn (Integer)
   * @param {string} lyDoLoi Lý do chi tiết
   */
  async huyDonLoiLe(id, lyDoLoi) {
    try {
      const response = await fetch(`${BASE_URL}/${id}/huy-loi`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ lyDoLoi }),
      })

      if (!response.ok) {
        throw new Error('Hủy đơn hàng thất bại.')
      }
      return await response.json()
    } catch (error) {
      console.error(`Lỗi khi hủy đơn ID ${id}:`, error)
      throw error
    }
  },

  /**
   * 3. Hủy HÀNG LOẠT nhiều đơn hàng cùng lúc
   * @param {Array<number>} danhSachHoaDonId Mảng chứa các ID hóa đơn [1, 2, 3]
   * @param {string} lyDoLoi Lý do hủy chung
   */
  async huyHangLoatLoi(danhSachHoaDonId, lyDoLoi) {
    try {
      const response = await fetch(`${BASE_URL}/huy-hang-loat`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          danhSachHoaDonId,
          lyDoLoi,
        }),
      })

      if (!response.ok) {
        throw new Error('Hủy hàng loạt thất bại.')
      }
      return await response.json()
    } catch (error) {
      console.error('Lỗi khi hủy hàng loạt:', error)
      throw error
    }
  },
}
