const API = 'http://localhost:8080/dot-giam-gia'

// ================= GET ALL =================
export const getAllDotGiamGia = async () => {
  const res = await fetch(API)
  return await res.json()
}

// ================= GET BY ID =================
export const getDotGiamGiaById = async (id) => {
  const res = await fetch(`${API}/detail/${id}`)
  return await res.json()
}

// ================= CREATE =================
export const createDotGiamGia = async (data) => {
  return await fetch(API, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  })
}

// ================= UPDATE =================
export const updateDotGiamGia = async (id, data) => {
  return await fetch(`${API}/update/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  })
}

// ================= DELETE =================
export const deleteDotGiamGia = async (id) => {
  return await fetch(`${API}/delete/${id}`, {
    method: 'DELETE',
  })
}
