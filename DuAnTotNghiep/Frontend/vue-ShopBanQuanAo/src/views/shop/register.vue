<template>
  <div class="min-h-screen bg-slate-950 flex items-center justify-center p-4 text-white">
    <form
      @submit.prevent="submit"
      class="w-full max-w-lg bg-white/10 border border-white/10 rounded-3xl p-8 space-y-4"
    >
      <h1 class="text-3xl font-bold">Đăng ký tài khoản</h1>
      <p class="text-white/60">Tạo tài khoản khách hàng để lưu địa chỉ và theo dõi đơn hàng.</p>
      <input v-model.trim="form.hoTen" placeholder="Họ và tên" class="field" required />
      <input v-model.trim="form.tenTaiKhoan" placeholder="Tên đăng nhập" class="field" required />
      <input v-model.trim="form.email" type="email" placeholder="Email" class="field" required />
      <input
        v-model.trim="form.soDienThoai"
        inputmode="numeric"
        placeholder="Số điện thoại"
        class="field"
        required
      />
      <input
        v-model="form.matKhau"
        type="password"
        placeholder="Mật khẩu (tối thiểu 6 ký tự)"
        class="field"
        required
      />
      <input
        v-model="form.xacNhanMatKhau"
        type="password"
        placeholder="Xác nhận mật khẩu"
        class="field"
        required
      />
      <p v-if="message" :class="ok ? 'text-emerald-400' : 'text-red-400'">{{ message }}</p>
      <button
        :disabled="loading"
        class="w-full py-3 rounded-xl bg-amber-300 text-black font-bold disabled:opacity-60"
      >
        {{ loading ? 'Đang xử lý...' : 'Đăng ký' }}
      </button>
      <RouterLink to="/login" class="block text-center text-amber-300"
        >Đã có tài khoản? Đăng nhập</RouterLink
      >
    </form>
  </div>
</template>
<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
const router = useRouter()
const loading = ref(false),
  message = ref(''),
  ok = ref(false)
const form = reactive({
  hoTen: '',
  tenTaiKhoan: '',
  email: '',
  soDienThoai: '',
  matKhau: '',
  xacNhanMatKhau: '',
})
const submit = async () => {
  loading.value = true
  message.value = ''
  try {
    const res = await fetch('http://localhost:8080/auth/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form),
    })
    const data = await res.json().catch(() => ({}))
    if (!res.ok) throw new Error(data.message || data.error || 'Đăng ký thất bại')
    ok.value = true
    message.value = 'Đăng ký thành công. Đang chuyển tới đăng nhập...'
    setTimeout(() => router.push('/login'), 800)
  } catch (e) {
    ok.value = false
    message.value = e.message
  } finally {
    loading.value = false
  }
}
</script>
<style scoped>
.field {
  width: 100%;
  padding: 0.8rem 1rem;
  border-radius: 0.75rem;
  background: white;
  color: #111;
  outline: none;
}
</style>
