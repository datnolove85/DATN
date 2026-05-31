const API = 'http://localhost:8080/spct'

export const getAllSanPhamChiTiet = async () => {
  const res = await fetch(API)

  if (!res.ok) {
    throw new Error('Lỗi lấy sản phẩm')
  }

  return await res.json()
}

export const getSanPhamChiTietById = async (id) => {
  const res = await fetch(`${API}/${id}`)

  if (!res.ok) {
    throw new Error('Lỗi lấy chi tiết sản phẩm')
  }

  return await res.json()
}
