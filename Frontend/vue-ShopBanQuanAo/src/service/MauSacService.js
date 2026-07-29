const API = 'http://localhost:8080/mausac'

// ================= GET ALL =================
export const getAllMauSac = async () => {
  const res = await fetch(API)
  if (!res.ok) throw new Error('Lỗi load màu sắc')
  return await res.json()
}

// ================= GET BY ID =================
export const getMauSacById = async (id) => {
  const res = await fetch(`${API}/${id}`)
  if (!res.ok) throw new Error('Không tìm thấy màu sắc')
  return await res.json()
}

// ================= CREATE =================
export const createMauSac = async (data) => {
  const res = await fetch(API, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
  })

  if (!res.ok) throw new Error('Lỗi tạo màu sắc')
  return await res.json()
}

// ================= UPDATE =================
export const updateMauSac = async (id, data) => {
  const res = await fetch(`${API}/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
  })

  if (!res.ok) throw new Error('Lỗi update màu sắc')
  return await res.json()
}

// ================= DELETE =================
export const deleteMauSac = async (id) => {
  const res = await fetch(`${API}/${id}`, {
    method: 'DELETE',
  })

  if (!res.ok) throw new Error('Lỗi xóa màu sắc')
  return true
}
