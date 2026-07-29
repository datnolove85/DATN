const API = 'http://localhost:8080/chatlieu'

import { apiFetch } from './api'

export const getAllChatLieu = () => apiFetch('/chatlieu')
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
