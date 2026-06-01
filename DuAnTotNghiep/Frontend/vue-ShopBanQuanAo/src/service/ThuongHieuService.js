const API = 'http://localhost:8080/thuonghieu'

export const getAllThuongHieu = async () => {
  const res = await fetch(API)
  return await res.json()
}

export const createThuongHieu = async (data) => {
  return await fetch(API, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  })
}

export const updateThuongHieu = async (id, data) => {
  return await fetch(`${API}/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  })
}

export const deleteThuongHieu = async (id) => {
  return await fetch(`${API}/${id}`, { method: 'DELETE' })
}
