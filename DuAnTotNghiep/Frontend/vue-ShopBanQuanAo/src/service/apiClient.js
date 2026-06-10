// services/apiClient.js
const handleResponse = async (response) => {
  const data = await response.json().catch(() => null)
  if (!response.ok) throw new Error(data?.message || `Error ${response.status}`)
  return data
}

export const request = async (url, options = {}) => {
  // Tự động thêm header nếu không phải FormData
  if (!(options.body instanceof FormData)) {
    options.headers = { ...options.headers, 'Content-Type': 'application/json' }
  }
  const response = await fetch(url, options)
  return handleResponse(response)
}
