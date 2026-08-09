import axios from 'axios'

const API_URL = 'http://localhost:8080/api/virtual-try-on' // Đổi port theo Spring Boot của bạn

export const createVirtualTryOn = async ({ spctId, personImage, category }) => {
  const formData = new FormData()
  formData.append('spctId', spctId)
  formData.append('personImage', personImage)
  formData.append('category', category)

  const response = await axios.post(API_URL, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
    responseType: 'blob', // Quan trọng: Nhận binary image (PNG/JPEG) trả về
  })

  return response.data
}
