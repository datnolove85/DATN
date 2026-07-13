const BASE_URL = 'http://localhost:8080/payment'

export const thanhToan = async (data) => {
  try {
    const res = await fetch(`${BASE_URL}/pay`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    })

    const result = await res.json()

    if (!res.ok) {
      throw new Error(result.message || 'Thanh toán thất bại')
    }

    return result
  } catch (error) {
    console.error('Thanh toán lỗi:', error)
    throw error
  }
}
