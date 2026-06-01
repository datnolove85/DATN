const API = 'http://localhost:8080/kichthuoc'

// ================= GET ALL =================
export const getAllKichThuoc = async () => {
  const res = await fetch(API)
  if (!res.ok) throw new Error('Lỗi load kích thước')
  return await res.json()
}

// ================= GET BY ID =================
export const getKichThuocById = async (id) => {
  const res = await fetch(`${API}/${id}`)
  if (!res.ok) throw new Error('Không tìm thấy kích thước')
  return await res.json()
}

// ================= CREATE =================
export const createKichThuoc = async (data) => {
  const res = await fetch(API, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
  })

  if (!res.ok) throw new Error('Lỗi tạo kích thước')
  return await res.json()
}

// ================= UPDATE =================
export const updateKichThuoc = async (id, data) => {
  const res = await fetch(`${API}/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
  })

  if (!res.ok) throw new Error('Lỗi update kích thước')
  return await res.json()
}

// ================= DELETE =================
export const deleteKichThuoc = async (id) => {
  const res = await fetch(`${API}/${id}`, {
    method: 'DELETE',
  })

  if (!res.ok) throw new Error('Lỗi xóa kích thước')
  return true
}
