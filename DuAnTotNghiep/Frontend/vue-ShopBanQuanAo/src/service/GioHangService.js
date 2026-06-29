import axios from 'axios'

const API = 'http://localhost:8080/giohang'

export const themGioHang = (body, token) => {
  return axios.post(`${API}/them`, body, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
}

export const layGioHang = (token) => {
  return axios.get(API, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
}
