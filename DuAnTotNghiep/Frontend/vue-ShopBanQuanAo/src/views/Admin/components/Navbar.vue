<template>
  <header
    class="sticky top-0 z-50 bg-white/90 backdrop-blur-md border-b border-slate-200/80 shadow-xs transition-all"
  >
    <div class="h-16 px-6 flex items-center justify-end">
      <!-- RIGHT: Actions & User Profile -->
      <div class="flex items-center gap-3">
        <!-- Online System Status -->
        <div
          class="hidden md:flex items-center gap-2 px-3 py-1.5 rounded-xl bg-emerald-50/80 border border-emerald-100/80"
        >
          <span class="w-2 h-2 rounded-full bg-emerald-500 animate-pulse"></span>
          <span class="text-xs font-medium text-emerald-700">Hệ thống ổn định</span>
        </div>

        <!-- Notification Button -->
        <button
          class="relative w-10 h-10 rounded-xl bg-slate-100 hover:bg-slate-200 text-slate-600 flex items-center justify-center transition"
          title="Thông báo"
        >
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"
            />
          </svg>
          <span
            class="absolute top-2.5 right-2.5 w-2 h-2 bg-rose-500 rounded-full ring-2 ring-white"
          ></span>
        </button>

        <!-- Messages Button -->
        <router-link
          to="/admin/chat"
          class="relative w-10 h-10 rounded-xl bg-slate-100 hover:bg-slate-200 text-slate-600 flex items-center justify-center transition"
          active-class="bg-indigo-100 text-indigo-600"
          title="Tin nhắn"
        >
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"
            />
          </svg>
          <!-- Badge hiển thị số lượng tin nhắn chưa đọc động chuẩn xác -->
          <span
            v-if="totalUnread > 0"
            class="absolute -top-1 -right-1 px-1.5 py-0.2 text-[10px] font-bold bg-indigo-600 text-white rounded-full ring-2 ring-white"
          >
            {{ totalUnread > 99 ? '99+' : totalUnread }}
          </span>
        </router-link>

        <div class="h-6 w-px bg-slate-200 mx-1"></div>

        <!-- User Profile Dropdown -->
        <div class="relative user-dropdown">
          <div
            @click="openMenu = !openMenu"
            class="flex items-center gap-3 p-1.5 pr-3 rounded-2xl hover:bg-slate-100 transition cursor-pointer select-none"
          >
            <div
              class="w-9 h-9 rounded-xl bg-gradient-to-tr from-indigo-600 to-purple-600 text-white flex items-center justify-center font-bold text-xs uppercase shadow-sm"
            >
              {{ avatar }}
            </div>

            <div class="text-left hidden sm:block">
              <p class="text-xs font-semibold text-slate-800 leading-tight">
                {{ user?.hoTen || 'Tài khoản' }}
              </p>
              <p class="text-[10px] font-medium text-slate-500 mt-0.5">
                {{ roleName }}
              </p>
            </div>

            <svg
              class="w-4 h-4 text-slate-400 transition-transform duration-200"
              :class="{ 'rotate-180': openMenu }"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M19 9l-7 7-7-7"
              />
            </svg>
          </div>

          <!-- Dropdown Menu -->
          <Transition
            enter-active-class="transition duration-200 ease-out"
            enter-from-class="opacity-0 scale-95 translate-y-1"
            enter-to-class="opacity-100 scale-100 translate-y-0"
            leave-active-class="transition duration-150 ease-in"
            leave-from-class="opacity-100 scale-100 translate-y-0"
            leave-to-class="opacity-0 scale-95 translate-y-1"
          >
            <div
              v-if="openMenu"
              class="absolute right-0 mt-2 w-72 rounded-2xl bg-white shadow-xl shadow-slate-200/50 border border-slate-100 overflow-hidden py-1.5"
            >
              <!-- User Info Card Header -->
              <div class="px-4 py-3 mx-2 my-1 bg-slate-50 rounded-xl border border-slate-100">
                <p class="text-xs font-semibold text-slate-800 truncate">
                  {{ user?.tenTaiKhoan }}
                </p>
                <p class="text-[11px] text-slate-500 truncate mt-0.5">
                  {{ user?.email || 'Chưa cập nhật email' }}
                </p>
                <div
                  class="mt-2 inline-block px-2 py-0.5 bg-indigo-50 text-indigo-700 text-[10px] font-semibold rounded-md border border-indigo-100"
                >
                  {{ roleName }}
                </div>
              </div>

              <div class="h-px bg-slate-100 my-1"></div>

              <!-- Menu Items -->
              <div class="px-1.5">
                <button
                  @click="goToProfile"
                  class="w-full flex items-center gap-3 px-3 py-2.5 rounded-xl text-xs font-medium text-slate-600 hover:text-slate-900 hover:bg-slate-100 transition text-left"
                >
                  <svg
                    class="w-4 h-4 text-slate-400"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"
                    />
                  </svg>
                  <span>Thông tin tài khoản</span>
                </button>

                <button
                  class="w-full flex items-center gap-3 px-3 py-2.5 rounded-xl text-xs font-medium text-slate-600 hover:text-slate-900 hover:bg-slate-100 transition text-left"
                >
                  <svg
                    class="w-4 h-4 text-slate-400"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"
                    />
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
                    />
                  </svg>
                  <span>Cài đặt hệ thống</span>
                </button>
              </div>

              <div class="h-px bg-slate-100 my-1"></div>

              <!-- Logout Button -->
              <div class="px-1.5">
                <button
                  @click="logout"
                  class="w-full flex items-center gap-3 px-3 py-2.5 rounded-xl text-xs font-medium text-rose-600 hover:bg-rose-50 transition text-left"
                >
                  <svg
                    class="w-4 h-4 text-rose-500"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"
                    />
                  </svg>
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
import { useChatBadge } from '@/composables/useChatBadge'

const router = useRouter()
const openMenu = ref(false)

// Lấy state số lượng tin nhắn chưa đọc chung
const { totalUnread, updateBadgeCount } = useChatBadge()

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
      return 'Thành viên'
  }
})

// Hàm điều hướng tới trang chỉnh sửa thông tin tài khoản kèm ID
const goToProfile = () => {
  openMenu.value = false
  const userId = user.value?.id || user.value?.maNhanVien

  if (userId) {
    router.push(`/admin/nhan-vien/edit/${userId}`)
  } else {
    console.warn('Không tìm thấy ID người dùng trong session')
  }
}

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
  updateBadgeCount() // Load số lượng tin chưa đọc lúc vừa vào trang
})

onBeforeUnmount(() => {
  window.removeEventListener('click', clickOutside)
})
</script>
