const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

export const createVirtualTryOn = async ({ spctId, personImage, category }) => {
  const formData = new FormData()
  formData.append('spctId', String(spctId))
  formData.append('personImage', personImage)
  formData.append('category', category || 'upper_body')

  const response = await fetch(`${API_BASE_URL}/api/virtual-try-on`, {
    method: 'POST',
    body: formData,
  })

  if (!response.ok) {
    const contentType = response.headers.get('content-type') || ''
    let message = `Thử đồ thất bại (HTTP ${response.status})`

    if (contentType.includes('application/json')) {
      const data = await response.json().catch(() => null)
      message = data?.message || message
    } else {
      const text = await response.text().catch(() => '')
      if (text.trim()) message = text.trim()
    }

    throw new Error(message)
  }

  return await response.blob()
}
