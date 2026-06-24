import { Client } from '@stomp/stompjs'

const stompClient = new Client({
  brokerURL: 'ws://localhost:8080/ws',
  reconnectDelay: 5000,
})

stompClient.onConnect = () => {
  console.log('Đã kết nối WebSocket')

  stompClient.subscribe('/topic/test', (msg) => {
    console.log('NHẬN ĐƯỢC:', msg.body)
  })
}
stompClient.activate()

export default stompClient
