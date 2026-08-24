const API_BASE_URL = String(import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080').replace(
  /\/$/,
  '',
)

async function request(path, options = {}) {
  const token = sessionStorage.getItem('token')
  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...(options.headers || {}),
    },
  })
  const data = await response.json().catch(() => null)
  if (!response.ok) throw new Error(data?.message || `Chat error ${response.status}`)
  return data
}

export const getMyChat = () => request('/api/chat/me')
export const createChatConversation = () => request('/api/chat/conversation', { method: 'POST' })
export const getChatInbox = () => request('/api/chat/inbox')
export const getChatConversation = (id) => request(`/api/chat/conversation/${id}`)
export const sendChatMessage = (id, noiDung, loai = 'TEXT') =>
  request(`/api/chat/conversation/${id}/message`, {
    method: 'POST',
    body: JSON.stringify({ noiDung, loai }),
  })
export const replyChatMessage = (id, noiDung, loai = 'TEXT') =>
  request(`/api/chat/conversation/${id}/reply`, {
    method: 'POST',
    body: JSON.stringify({ noiDung, loai }),
  })
export const markChatRead = (id) => request(`/api/chat/conversation/${id}/read`, { method: 'POST' })
export const closeChatConversation = (id) =>
  request(`/api/chat/conversation/${id}/close`, { method: 'POST' })

export { API_BASE_URL }
