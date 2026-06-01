const API = 'http://localhost:8080/sanpham'

export const getAllSanPhamChiTiet = async () => {
  const res = await fetch(API)

  if (!res.ok) {
    throw new Error('Lỗi lấy sản phẩm')
  }

  return await res.json()
}

const API_BY_ID = 'http://localhost:8080/spct'
export const getSanPhamChiTietById = async (id) => {
  const res = await fetch(`${API_BY_ID}/${id}`)

  if (!res.ok) {
    throw new Error('Lỗi lấy chi tiết sản phẩm')
  }

  return await res.json()
}
