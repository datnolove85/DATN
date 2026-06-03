const API = 'http://localhost:8080/spct'

// ================= GET ALL =================
export const getAllSanPhamChiTiet = async () => {
  const res = await fetch(API)

  if (!res.ok) {
    throw new Error('Lỗi lấy danh sách SPCT')
  }

  return await res.json()
}

// ================= GET BY ID =================
export const getSanPhamChiTietById = async (id) => {
  const res = await fetch(`${API}/detail/${id}`)

  if (!res.ok) {
    throw new Error('Lỗi lấy chi tiết SPCT')
  }

  return await res.json()
}

// ================= CREATE =================
export const createSanPhamChiTiet = async (data) => {
  const res = await fetch(API, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
  })

  if (!res.ok) {
    throw new Error('Lỗi thêm SPCT')
  }

  return await res.json()
}

// ================= UPDATE =================
export const updateSanPhamChiTiet = async (id, data) => {
  const res = await fetch(`${API}/update/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
  })

  if (!res.ok) {
    throw new Error('Lỗi cập nhật SPCT')
  }

  return await res.json()
}

// ================= DELETE =================
export const deleteSanPhamChiTiet = async (id) => {
  const res = await fetch(`${API}/delete/${id}`, {
    method: 'DELETE',
  })

  if (!res.ok) {
    throw new Error('Lỗi xóa SPCT')
  }

  return await res.text()
}
