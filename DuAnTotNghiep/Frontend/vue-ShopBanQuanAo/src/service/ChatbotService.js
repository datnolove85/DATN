const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

export async function sendChatbotMessage(payload) {
  const response = await fetch(`${API_BASE_URL}/api/chatbot/messages`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })

  const data = await response.json().catch(() => ({}))
  if (!response.ok) {
    throw new Error(data?.message || data?.detail || 'Khong the gui tin nhan den chatbot.')
  }

  return data
}
