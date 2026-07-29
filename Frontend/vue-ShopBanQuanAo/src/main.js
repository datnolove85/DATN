import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue-next/dist/bootstrap-vue-next.css'
// Import thư viện và CSS

import Toast from 'vue-toastification'
import 'vue-toastification/dist/index.css'
import '@/assets/toast.css'
import './style.css'
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import './socket'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
//cấu hình
const options = {
  position: 'top-right',
  timeout: 2000,
  closeOnClick: true,
  pauseOnFocusLoss: true,
  pauseOnHover: true,
  draggable: true,
  draggablePercent: 0.6,
  hideProgressBar: true,
  showCloseButtonOnHover: true,
  icon: true,
  newestOnTop: true,
}
const app = createApp(App)
app.use(Toast, options)
app.use(router)
app.use(ElementPlus)
app.mount('#app')
