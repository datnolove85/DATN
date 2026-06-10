const API = 'http://localhost:8080/hoadon'

// ================= HANDLE RESPONSE =================
const handleResponse = async (response) => {
  const data = await response.json().catch(() => null)

  if (!response.ok) {
    const message = data?.message || `HTTP Error: ${response.status}`
    throw new Error(message)
  }

  return data
}

// ================= GET ALL =================
export const getAllHoadon = async () => {
  try {
    const response = await fetch(API)
    return await handleResponse(response)
  } catch (error) {
    console.error('getAllHoadon error:', error)
    throw error
  }
}

// ================= GET BY ID =================
export const getHoadonById = async (id) => {
  try {
    const response = await fetch(`${API}/${id}`)
    return await handleResponse(response)
  } catch (error) {
    console.error('getHoadonById error:', error)
    throw error
  }
}

// ================= CREATE =================
export const createHoadon = async (data) => {
  try {
    const response = await fetch(API, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    })

    return await handleResponse(response)
  } catch (error) {
    console.error('createHoadon error:', error)
    throw error
  }
}

// ================= UPDATE =================
export const updateHoadon = async (id, data) => {
  try {
    const response = await fetch(`${API}/update/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    })

    return await handleResponse(response)
  } catch (error) {
    console.error('updateHoadon error:', error)
    throw error
  }
}

// ================= DELETE =================
export const deleteHoadon = async (id) => {
  try {
    const response = await fetch(`${API}/delete/${id}`, {
      method: 'DELETE',
    })

    if (!response.ok) {
      throw new Error(await response.text())
    }

    return await response.text()
  } catch (error) {
    console.error('deleteHoadon error:', error)
    throw error
  }
}
