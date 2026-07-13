<template>
  <header
    class="sticky top-0 z-50 bg-white/80 backdrop-blur-xl border-b border-slate-200/70 shadow-sm"
  >
    <div class="h-16 px-6 flex items-center justify-between">
      <!-- LEFT -->
      <div class="flex items-center gap-4">
        <!-- Breadcrumb -->
      </div>

      <!-- CENTER -->
      <div class="hidden lg:block w-[400px]">
        <div class="relative"></div>
      </div>

      <!-- RIGHT -->
      <div class="flex items-center gap-3">
        <!-- Online -->
        <div
          class="hidden md:flex items-center gap-2 px-3 py-2 rounded-xl bg-emerald-50 border border-emerald-100"
        >
          <span class="w-2 h-2 rounded-full bg-emerald-500 animate-pulse"></span>

          <span class="text-xs font-semibold text-emerald-700"> Hệ thống hoạt động </span>
        </div>

        <!-- Notification -->
        <button class="relative w-11 h-11 rounded-xl bg-slate-100 hover:bg-slate-200 transition">
          🔔

          <span class="absolute top-2 right-2 w-2 h-2 bg-red-500 rounded-full"></span>
        </button>

        <!-- Message -->
        <button class="relative w-11 h-11 rounded-xl bg-slate-100 hover:bg-slate-200 transition">
          ✉️

          <span
            class="absolute -top-1 -right-1 px-1.5 py-0.5 text-[10px] bg-indigo-600 text-white rounded-full"
          >
            5
          </span>
        </button>

        <!-- User -->
        <div class="relative user-dropdown">
          <div
            @click="openMenu = !openMenu"
            class="flex items-center gap-3 px-3 py-2 rounded-2xl bg-slate-100 hover:bg-slate-200 transition cursor-pointer"
          >
            <div class="text-right">
              <p class="text-sm font-semibold text-slate-800">
                {{ user?.hoTen }}
              </p>

              <p class="text-[11px] text-slate-500">
                {{ roleName }}
              </p>
            </div>

            <div
              class="w-10 h-10 rounded-xl bg-gradient-to-r from-indigo-600 to-purple-600 text-white flex items-center justify-center font-bold uppercase"
            >
              {{ avatar }}
            </div>
          </div>

          <Transition
            enter-active-class="transition duration-200"
            enter-from-class="opacity-0 scale-95"
            enter-to-class="opacity-100 scale-100"
            leave-active-class="transition duration-150"
            leave-from-class="opacity-100 scale-100"
            leave-to-class="opacity-0 scale-95"
          >
            <div
              v-if="openMenu"
              class="absolute right-0 mt-3 w-72 rounded-3xl bg-white shadow-2xl border border-slate-200 overflow-hidden"
            >
              <div class="bg-gradient-to-r from-indigo-600 to-purple-600 text-white p-5">
                <div class="flex items-center gap-3">
                  <div
                    class="w-14 h-14 rounded-2xl bg-white/20 flex items-center justify-center text-xl font-bold uppercase"
                  >
                    {{ avatar }}
                  </div>

                  <div>
                    <p class="font-bold">
                      {{ user?.tenTaiKhoan }}
                    </p>

                    <p class="text-xs opacity-80">
                      {{ roleName }}
                    </p>

                    <p class="text-xs opacity-70 mt-1">
                      {{ user?.email }}
                    </p>
                  </div>
                </div>
              </div>

              <div class="p-2">
                <button
                  class="w-full flex items-center gap-3 px-4 py-3 rounded-xl hover:bg-slate-100 transition"
                >
                  👤
                  <span>Thông tin tài khoản</span>
                </button>

                <button
                  class="w-full flex items-center gap-3 px-4 py-3 rounded-xl hover:bg-slate-100 transition"
                >
                  ⚙️
                  <span>Cài đặt</span>
                </button>

                <div class="border-t my-2"></div>

                <button
                  @click="logout"
                  class="w-full flex items-center gap-3 px-4 py-3 rounded-xl text-red-600 hover:bg-red-50 transition"
                >
                  🚪
                  <span>Đăng xuất</span>
                </button>
              </div>
            </div>
          </Transition>
        </div>
      </div>
    </div>
  </header>
</template>
<script setup>
import { computed, ref, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const openMenu = ref(false)

const user = computed(() => {
  const data = sessionStorage.getItem('user')
  return data ? JSON.parse(data) : null
})

const avatar = computed(() => {
  if (!user.value?.tenTaiKhoan) return '?'
  return user.value.tenTaiKhoan.substring(0, 2).toUpperCase()
})

const roleName = computed(() => {
  switch (user.value?.vaiTro) {
    case 'ADMIN':
      return 'Super Admin'
    case 'STAFF':
      return 'Nhân viên'
    case 'USERS':
      return 'Khách hàng'
    default:
      return ''
  }
})

const logout = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')

  router.replace('/login')
}

const clickOutside = (e) => {
  if (!e.target.closest('.user-dropdown')) {
    openMenu.value = false
  }
}

onMounted(() => {
  window.addEventListener('click', clickOutside)
})

onBeforeUnmount(() => {
  window.removeEventListener('click', clickOutside)
})
</script>
