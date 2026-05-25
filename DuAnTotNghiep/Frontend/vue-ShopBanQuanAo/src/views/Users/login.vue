<template>
  <div class="min-h-screen flex flex-col bg-[#0d0d0f] font-sans antialiased text-white">
    <!-- MAIN WRAPPER -->
    <div
      class="flex-1 w-full bg-gradient-to-br from-[#0f0f10] via-[#161618] to-[#222226] relative flex flex-col overflow-hidden"
    >
      <!-- BACKGROUND EFFECT -->
      <div class="absolute inset-0 opacity-30 pointer-events-none">
        <div
          class="absolute -top-20 -left-20 w-[500px] h-[500px] bg-[#c6a77d]/10 rounded-full blur-3xl"
        ></div>

        <div
          class="absolute bottom-0 right-0 w-[450px] h-[450px] bg-white/5 rounded-full blur-3xl"
        ></div>
      </div>

      <!-- NOISE TEXTURE -->
      <div
        class="absolute inset-0 opacity-[0.03] mix-blend-overlay pointer-events-none"
        style="background-image: url('https://grainy-gradients.vercel.app/noise.svg')"
      ></div>

      <!-- HEADER -->
      <header
        class="w-full border-b border-white/10 sticky top-0 z-50 backdrop-blur-md bg-black/10"
      >
        <div class="max-w-7xl mx-auto px-4 md:px-8 h-20 flex items-center justify-between">
          <!-- LEFT -->
          <div class="flex items-center space-x-4">
            <a href="#" class="text-2xl font-black tracking-[2px] text-white">
              Vel<span class="text-[#c6a77d]">o</span>ra
            </a>

            <div
              class="text-xl md:text-2xl text-white/20 pl-4 border-l border-white/10 h-6 flex items-center"
            >
              <span class="text-white font-medium text-lg md:text-xl"> Đăng nhập </span>
            </div>
          </div>

          <!-- RIGHT -->
          <a href="#" class="text-sm text-[#c6a77d] hover:text-white transition-colors font-medium">
            Bạn cần trợ giúp?
          </a>
        </div>
      </header>

      <!-- MAIN -->
      <main class="flex-1 flex items-center justify-center py-12 md:py-20 relative z-10">
        <div
          class="max-w-7xl w-full mx-auto px-4 md:px-8 grid grid-cols-1 lg:grid-cols-12 gap-10 items-center"
        >
          <!-- LEFT CONTENT -->
          <div
            class="hidden lg:flex lg:col-span-7 flex-col items-center justify-center text-white text-center"
          >
            <!-- ICON -->
            <div
              class="bg-white/5 border border-white/10 p-8 rounded-[32px] mb-8 backdrop-blur-xl shadow-2xl"
            >
              <ShoppingBagIcon :size="100" class="text-[#f4dfc8] stroke-[1.2]" />
            </div>

            <!-- TITLE -->
            <h1 class="text-5xl font-black tracking-[8px] text-[#f8f5f1]">VELORA STUDIO</h1>

            <!-- LINE -->
            <div
              class="w-40 h-[2px] bg-gradient-to-r from-transparent via-[#c6a77d] to-transparent rounded-full my-6"
            ></div>

            <!-- DESC -->
            <p class="text-lg text-white/70 font-light max-w-lg leading-relaxed">
              Tinh giản trong thiết kế, khác biệt trong trải nghiệm mua sắm hiện đại.
            </p>
          </div>

          <!-- LOGIN CARD -->
          <div class="col-span-1 lg:col-span-5 flex justify-center lg:justify-end">
            <div
              class="relative w-full max-w-[430px] bg-white/8 backdrop-blur-2xl rounded-[36px] shadow-[0_20px_80px_rgba(0,0,0,0.45)] border border-white/10 p-8 text-white overflow-hidden"
            >
              <!-- GLASS EFFECT -->
              <div
                class="absolute inset-0 bg-gradient-to-br from-white/10 via-transparent to-transparent pointer-events-none"
              ></div>

              <!-- CARD CONTENT -->
              <div class="relative z-10">
                <!-- HEADER -->
                <div class="flex items-center justify-between mb-8">
                  <h2 class="text-2xl font-bold text-white tracking-wide">Chào mừng trở lại</h2>
                </div>

                <!-- FORM -->
                <form @submit.prevent="handleLogin" class="space-y-4">
                  <!-- EMAIL -->
                  <div>
                    <input
                      type="text"
                      v-model="form.identity"
                      placeholder="Email hoặc tên đăng nhập"
                      class="w-full px-4 py-3 bg-white border border-gray-300 rounded-xl text-black text-sm placeholder:text-gray-400 text-left outline-none focus:border-[#c6a77d] focus:ring-2 focus:ring-[#c6a77d]/20"
                      required
                    />
                  </div>

                  <!-- PASSWORD -->
                  <div class="relative">
                    <input
                      :type="showPassword ? 'text' : 'password'"
                      v-model="form.password"
                      placeholder="Mật khẩu"
                      class="w-full px-4 pr-12 py-3 bg-white border border-gray-300 rounded-xl text-black text-sm placeholder:text-gray-400 outline-none"
                      required
                    />

                    <button
                      type="button"
                      @click="showPassword = !showPassword"
                      class="absolute right-3 top-1/2 -translate-y-1/2 text-black z-10"
                    >
                      <EyeIcon v-if="!showPassword" :size="20" />
                      <EyeOffIcon v-else :size="20" />
                    </button>
                  </div>
                  <!-- LOGIN BUTTON -->
                  <button
                    type="submit"
                    :disabled="isLoading"
                    class="w-full bg-gradient-to-r from-[#c6a77d] to-[#b89368] text-black py-3 rounded-xl font-semibold"
                  >
                    <span v-if="!isLoading">ĐĂNG NHẬP</span>
                    <span
                      v-else
                      class="w-5 h-5 border-2 border-black border-t-transparent rounded-full animate-spin inline-block"
                    ></span>
                  </button>
                </form>

                <!-- LINKS -->
                <div
                  class="flex items-center justify-between text-xs text-[#d6b690] mt-5 px-1 font-medium"
                >
                  <a href="#" class="hover:text-white transition-colors"> Quên mật khẩu </a>
                </div>

                <!-- SEPARATOR -->
                <div class="relative flex py-6 items-center">
                  <div class="flex-grow border-t border-white/10"></div>

                  <span class="flex-shrink mx-4 text-white/30 text-xs uppercase tracking-[3px]">
                    Hoặc
                  </span>

                  <div class="flex-grow border-t border-white/10"></div>
                </div>

                <!-- SOCIAL -->
                <div class="grid grid-cols-2 gap-3">
                  <!-- FACEBOOK -->
                  <button
                    type="button"
                    class="flex items-center justify-center space-x-2 py-3.5 px-4 bg-white/5 border border-white/10 rounded-2xl text-sm font-medium text-white/80 hover:bg-white/10 hover:border-white/20 transition-all duration-300"
                  >
                    <svg class="w-5 h-5 text-[#1877F2] fill-current" viewBox="0 0 24 24">
                      <path
                        d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"
                      />
                    </svg>

                    <span>Facebook</span>
                  </button>

                  <!-- GOOGLE -->
                  <button
                    type="button"
                    class="flex items-center justify-center space-x-2 py-3.5 px-4 bg-white/5 border border-white/10 rounded-2xl text-sm font-medium text-white/80 hover:bg-white/10 hover:border-white/20 transition-all duration-300"
                  >
                    <svg class="w-5 h-5" viewBox="0 0 24 24">
                      <path
                        fill="#EA4335"
                        d="M12.24 10.285V14.4h6.887c-.275 1.565-1.88 4.604-6.887 4.604-4.33 0-7.866-3.577-7.866-8s3.536-8 7.866-8c2.46 0 4.105 1.025 5.047 1.926l3.256-3.133C18.332 2.117 15.547 1 12.24 1c-6.077 0-11 4.923-11 11s4.923 11 11 11c6.34 0 10.556-4.436 10.556-10.732 0-.726-.077-1.282-.175-1.683H12.24z"
                      />
                    </svg>

                    <span>Google</span>
                  </button>
                </div>

                <!-- REGISTER -->
                <div class="text-center text-sm text-white/40 mt-8">
                  Bạn chưa có tài khoản?

                  <a
                    href="#"
                    class="text-[#d6b690] font-semibold hover:text-white transition-colors ml-1"
                  >
                    Đăng ký ngay
                  </a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>

    <!-- FOOTER -->
    <footer class="w-full bg-[#111214] text-xs text-white/40 py-10 border-t border-white/5">
      <div
        class="max-w-7xl mx-auto px-4 md:px-8 flex flex-col md:flex-row items-center justify-between gap-4"
      >
        <div>© {{ currentYear }} Velora Studio. All rights reserved.</div>

        <div class="flex flex-wrap justify-center gap-x-6 gap-y-2 text-white/30 font-medium">
          <a href="#" class="hover:text-white transition-colors"> Chính sách bảo mật </a>

          <a href="#" class="hover:text-white transition-colors"> Điều khoản </a>

          <a href="#" class="hover:text-white transition-colors"> Hỗ trợ </a>

          <a href="#" class="hover:text-white transition-colors"> Liên hệ </a>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'

import {
  ShoppingBag as ShoppingBagIcon,
  QrCode as QrCodeIcon,
  Eye as EyeIcon,
  EyeOff as EyeOffIcon,
} from 'lucide-vue-next'

const currentYear = ref(new Date().getFullYear())

const showPassword = ref(false)

const isLoading = ref(false)

const form = reactive({
  identity: '',
  password: '',
})

const handleLogin = () => {
  isLoading.value = true

  setTimeout(() => {
    isLoading.value = false

    alert(`Đăng nhập thành công: ${form.identity}`)
  }, 1500)
}
</script>

<style scoped></style>
