const API = 'http://localhost:8080/thuonghieu'

import { apiFetch } from './api'

export const getAllThuongHieu = () => apiFetch('/thuonghieu')

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
