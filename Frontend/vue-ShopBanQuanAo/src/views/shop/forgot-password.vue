<template>
  <div class="min-h-screen bg-slate-950 flex items-center justify-center p-4 text-white">
    <form
      @submit.prevent="submit"
      class="w-full max-w-md bg-white/10 border border-white/10 rounded-3xl p-8 space-y-4"
    >
      <h1 class="text-3xl font-bold">Quên mật khẩu</h1>
      <input
        v-model.trim="form.email"
        type="email"
        placeholder="Email đã đăng ký"
        class="field"
        required
      />
      <template v-if="codeSent">
        <input
          v-model.trim="form.code"
          inputmode="numeric"
          maxlength="6"
          placeholder="Mã xác nhận 6 số"
          class="field"
          required
        />
        <input
          v-model="form.matKhauMoi"
          type="password"
          placeholder="Mật khẩu mới"
          class="field"
          required
        />
        <input
          v-model="form.xacNhanMatKhau"
          type="password"
          placeholder="Xác nhận mật khẩu mới"
          class="field"
          required
        />
      </template>
      <p v-if="message" :class="ok ? 'text-emerald-400' : 'text-red-400'">{{ message }}</p>
      <button
        :disabled="loading"
        class="w-full py-3 rounded-xl bg-amber-300 text-black font-bold disabled:opacity-60"
      >
        {{ loading ? 'Đang xử lý...' : codeSent ? 'Đặt lại mật khẩu' : 'Gửi mã xác nhận' }}
      </button>
      <button v-if="codeSent" type="button" @click="sendCode" class="w-full text-amber-300">
        Gửi lại mã
      </button>
      <RouterLink to="/login" class="block text-center text-amber-300"
        >Quay lại đăng nhập</RouterLink
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
  ok = ref(false),
  codeSent = ref(false)
const form = reactive({ email: '', code: '', matKhauMoi: '', xacNhanMatKhau: '' })
const request = async (url, body) => {
  const r = await fetch(url, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
  })
  const d = await r.json().catch(() => ({}))
  if (!r.ok) throw new Error(d.message || d.error || 'Yêu cầu thất bại')
  return d
}
const sendCode = async () => {
  loading.value = true
  message.value = ''
  try {
    await request('http://localhost:8080/auth/forgot-password', { email: form.email })
    codeSent.value = true
    ok.value = true
    message.value = 'Đã gửi mã xác nhận. Mã có hiệu lực 10 phút.'
  } catch (e) {
    ok.value = false
    message.value = e.message
  } finally {
    loading.value = false
  }
}
const submit = async () => {
  if (!codeSent.value) return sendCode()
  loading.value = true
  message.value = ''
  try {
    await request('http://localhost:8080/auth/reset-password', form)
    ok.value = true
    message.value = 'Đổi mật khẩu thành công.'
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
