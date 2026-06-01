const API = 'http://localhost:8080/chatlieu'

export const getAllChatLieu = async () => {
  const res = await fetch(API)
  return await res.json()
}

export const createChatLieu = async (data) => {
  return await fetch(API, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  })
}

export const updateChatLieu = async (id, data) => {
  return await fetch(`${API}/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  })
}

export const deleteChatLieu = async (id) => {
  return await fetch(`${API}/${id}`, { method: 'DELETE' })
}
