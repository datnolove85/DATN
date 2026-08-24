<template>
  <div class="fixed bottom-24 right-5 z-[80] font-sans">
    <Transition name="chat-pop">
      <section
        v-if="open"
        class="mb-3 flex h-[min(72vh,680px)] w-[min(390px,calc(100vw-24px))] flex-col overflow-hidden rounded-[26px] border border-slate-200 bg-white shadow-2xl shadow-slate-900/15"
      >
        <header class="flex items-center justify-between bg-[#7f1d2d] px-4 py-3.5 text-white">
          <div class="flex items-center gap-3">
            <div class="grid h-10 w-10 place-items-center rounded-2xl bg-white/15">
              <MessageCircle :size="21" />
            </div>
            <div>
              <p class="text-[11px] font-black uppercase tracking-[0.18em] text-white/65">
                K-ZONE SUPPORT
              </p>
              <h2 class="text-base font-black">Chat với Shop</h2>
            </div>
          </div>
          <div class="flex items-center gap-1">
            <button
              class="grid h-9 w-9 place-items-center rounded-xl text-white/80 hover:bg-white/10 hover:text-white"
              @click="minimized = !minimized"
            >
              <Minus :size="18" />
            </button>
            <button
              class="grid h-9 w-9 place-items-center rounded-xl text-white/80 hover:bg-white/10 hover:text-white"
              @click="open = false"
            >
              <X :size="19" />
            </button>
          </div>
        </header>

        <template v-if="!minimized">
          <div
            class="flex items-center justify-between border-b border-slate-100 bg-white px-4 py-3"
          >
            <div>
              <p class="text-[11px] font-black uppercase tracking-[0.15em] text-[#8b2034]">
                Hỗ trợ khách hàng
              </p>
              <p class="text-xs text-slate-500">Nhân viên sẽ trả lời trong hệ thống chat</p>
            </div>
            <span
              v-if="conversation?.trangThai === 'OPEN'"
              class="inline-flex items-center gap-1.5 rounded-full bg-emerald-50 px-2.5 py-1 text-[11px] font-bold text-emerald-700"
            >
              <span class="h-1.5 w-1.5 rounded-full bg-emerald-500"></span> Đang mở
            </span>
          </div>

          <div ref="messagesEl" class="flex-1 space-y-3 overflow-y-auto bg-[#fbfaf7] px-4 py-4">
            <div v-if="!loggedIn" class="flex h-full items-center justify-center text-center">
              <div class="max-w-[250px]">
                <MessageCircle :size="36" class="mx-auto mb-3 text-[#8b2034]" />
                <h3 class="font-black text-slate-900">Đăng nhập để chat với Shop</h3>
                <p class="mt-1 text-xs leading-5 text-slate-500">
                  Bạn sẽ xem được lịch sử hội thoại và nhận tin nhắn realtime từ nhân viên.
                </p>
                <RouterLink
                  to="/login"
                  class="mt-4 inline-flex rounded-xl bg-[#8b2034] px-4 py-2.5 text-sm font-bold text-white"
                  >Đăng nhập</RouterLink
                >
              </div>
            </div>

            <template v-else-if="messages.length">
              <article
                v-for="message in messages"
                :key="message.id"
                class="flex"
                :class="message.cuaToi ? 'justify-end' : 'justify-start'"
              >
                <div class="max-w-[82%]">
                  <div
                    class="rounded-[20px] px-3.5 py-2.5 text-sm leading-6"
                    :class="
                      message.cuaToi
                        ? 'rounded-br-md bg-[#8b2034] text-white'
                        : 'rounded-bl-md border border-slate-200 bg-white text-slate-800 shadow-sm'
                    "
                  >
                    {{ message.noiDung }}
                  </div>
                  <p
                    class="mt-1 px-1 text-[10px] text-slate-400"
                    :class="message.cuaToi ? 'text-right' : ''"
                  >
                    {{ formatTime(message.thoiGian) }}
                  </p>
                </div>
              </article>
            </template>

            <div v-else class="flex h-full items-center justify-center text-center">
              <div>
                <div
                  class="mx-auto mb-3 grid h-14 w-14 place-items-center rounded-2xl bg-[#f7ebe7] text-[#8b2034]"
                >
                  <MessagesSquare :size="25" />
                </div>
                <h3 class="font-black text-slate-900">Bắt đầu trò chuyện</h3>
                <p class="mt-1 text-xs text-slate-500">
                  Hỏi về sản phẩm, size, đơn hàng hoặc hỗ trợ mua hàng.
                </p>
              </div>
            </div>
          </div>

          <div class="border-t border-slate-100 bg-white p-3">
            <div class="mb-2 flex gap-2 overflow-x-auto pb-1">
              <button
                v-for="suggestion in suggestions"
                :key="suggestion"
                class="shrink-0 rounded-full border border-slate-200 bg-white px-3 py-1.5 text-[11px] font-semibold text-slate-600 hover:border-[#b87584] hover:text-[#8b2034]"
                @click="draft = suggestion"
              >
                {{ suggestion }}
              </button>
            </div>
            <form class="flex items-end gap-2" @submit.prevent="send">
              <textarea
                v-model="draft"
                rows="1"
                maxlength="4000"
                class="max-h-28 min-h-11 flex-1 resize-none rounded-2xl border border-slate-200 bg-slate-50 px-3 py-2.5 text-sm outline-none focus:border-[#9b4657] focus:ring-4 focus:ring-[#8b2034]/10"
                placeholder="Nhập tin nhắn..."
                @keydown.enter.exact.prevent="send"
              ></textarea>
              <button
                class="grid h-11 w-11 shrink-0 place-items-center rounded-2xl bg-[#8b2034] text-white disabled:cursor-not-allowed disabled:opacity-40"
                :disabled="sending || !draft.trim()"
              >
                <Send :size="18" />
              </button>
            </form>
          </div>
        </template>
      </section>
    </Transition>

    <button
      class="group flex items-center gap-2 rounded-full bg-[#8b2034] px-4 py-3 text-white shadow-xl shadow-[#8b2034]/25 transition hover:-translate-y-0.5 hover:bg-[#741a2b]"
      @click="toggle"
    >
      <MessageCircle :size="21" />
      <span class="text-sm font-black">Chat với Shop</span>
      <span
        v-if="unread > 0"
        class="grid min-w-5 h-5 place-items-center rounded-full bg-white px-1 text-[10px] font-black text-[#8b2034]"
        >{{ unread > 9 ? '9+' : unread }}</span
      >
    </button>
  </div>
</template>

<script setup>
import { nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { RouterLink } from 'vue-router'
import { Client } from '@stomp/stompjs'
import { MessageCircle, MessagesSquare, Minus, Send, X } from 'lucide-vue-next'
import { getMyChat, markChatRead, sendChatMessage, API_BASE_URL } from '@/service/ChatService'

const open = ref(false)
const minimized = ref(false)
const loggedIn = ref(Boolean(sessionStorage.getItem('token')))
const conversation = ref(null)
const messages = ref([])
const draft = ref('')
const sending = ref(false)
const unread = ref(0)
const messagesEl = ref(null)

let stompClient = null
let customerChatSub = null
const recentSentMessages = [] // Bộ nhớ tạm nhận diện tin nhắn do khách vừa gửi

const suggestions = [
  'Áo này còn size M không?',
  'Tôi muốn hỏi đơn hàng',
  'Shop tư vấn size giúp tôi',
]

const formatTime = (value) =>
  value ? new Date(value).toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' }) : ''
const token = () => sessionStorage.getItem('token')

async function load() {
  loggedIn.value = Boolean(token())
  if (!loggedIn.value) return

  const data = await getMyChat()
  conversation.value = data
  messages.value = data?.messages || []
  unread.value = data?.soTinChuaDoc || 0

  if (conversation.value?.id) {
    await markChatRead(conversation.value.id)
    unread.value = 0
    subscribeToCustomerChat()
  }

  scrollBottom()
}

function subscribeToCustomerChat() {
  if (customerChatSub) {
    customerChatSub.unsubscribe()
    customerChatSub = null
  }
  if (stompClient?.active && conversation.value?.id) {
    customerChatSub = stompClient.subscribe(`/topic/chat/${conversation.value.id}`, (frame) => {
      const message = JSON.parse(frame.body)

      // Kiểm tra xem tin nhắn trả về từ socket có phải là do khách vừa gửi đi không
      const recentIndex = recentSentMessages.findIndex(
        (m) => m.text === message.noiDung && Date.now() - m.time < 10000,
      )

      if (recentIndex !== -1) {
        message.cuaToi = true
        recentSentMessages.splice(recentIndex, 1) // Xóa khỏi danh sách tạm sau khi khớp
      } else {
        message.cuaToi = false // Nếu không phải khách gửi thì đích thị là tin của Admin
      }

      // Kiểm tra xem tin nhắn đã tồn tại trong danh sách chưa (tránh trùng lặp)
      const existing = messages.value.find(
        (item) =>
          item.id === message.id ||
          (message.cuaToi &&
            item.noiDung === message.noiDung &&
            item.id?.toString().startsWith('opt-')),
      )

      if (existing) {
        existing.id = message.id
        existing.thoiGian = message.thoiGian
        existing.cuaToi = true
      } else {
        messages.value.push(message)

        if (!message.cuaToi) {
          if (open.value && !minimized.value) {
            markChatRead(conversation.value.id).catch(() => {})
            unread.value = 0
          } else {
            unread.value += 1
          }
        }
      }
      scrollBottom()
    })
  }
}

function connectSocket() {
  if (stompClient?.active || !token()) return
  stompClient = new Client({
    brokerURL: `${API_BASE_URL.replace(/^http:/, 'ws:').replace(/^https:/, 'wss:')}/ws`,
    reconnectDelay: 5000,
    connectHeaders: { Authorization: `Bearer ${token()}` },
    onConnect: () => {
      subscribeToCustomerChat()
    },
  })
  stompClient.activate()
}

async function send() {
  if (!draft.value.trim() || sending.value || !conversation.value) return
  sending.value = true
  const text = draft.value.trim()
  draft.value = ''

  // Ghi nhớ nội dung vừa gửi để phân biệt khi websocket echo lại
  recentSentMessages.push({ text, time: Date.now() })
  while (recentSentMessages.length > 20) recentSentMessages.shift()

  // Hiển thị tạm thời tin nhắn lên giao diện ngay lập tức (Optimistic Update)
  const optimisticMsg = {
    id: 'opt-' + Date.now(),
    noiDung: text,
    thoiGian: new Date().toISOString(),
    cuaToi: true,
  }
  messages.value.push(optimisticMsg)
  scrollBottom()

  try {
    const message = await sendChatMessage(conversation.value.id, text)
    message.cuaToi = true

    const index = messages.value.findIndex((m) => m.id === optimisticMsg.id)
    if (index !== -1) {
      messages.value[index] = message
    } else if (!messages.value.some((m) => m.id === message.id)) {
      messages.value.push(message)
    }

    await markChatRead(conversation.value.id)
    scrollBottom()
  } catch (error) {
    messages.value = messages.value.filter((m) => m.id !== optimisticMsg.id)
    draft.value = text
    window.alert(error?.message || 'Không thể gửi tin nhắn.')
  } finally {
    sending.value = false
  }
}

async function scrollBottom() {
  await nextTick()
  if (messagesEl.value) messagesEl.value.scrollTop = messagesEl.value.scrollHeight
}

async function toggle() {
  open.value = !open.value
  if (open.value) {
    minimized.value = false
    try {
      await load()
    } catch (error) {
      window.alert(error?.message || 'Không thể tải cuộc trò chuyện.')
    }
  }
}

watch(open, (value) => {
  if (value) scrollBottom()
})

onMounted(async () => {
  if (token()) {
    try {
      const data = await getMyChat()
      if (data) {
        conversation.value = data
        unread.value = data?.soTinChuaDoc || 0
      }
    } catch (e) {
      // Bỏ qua nếu chưa có hội thoại
    }
    connectSocket()
  }
})

onBeforeUnmount(() => {
  if (customerChatSub) customerChatSub.unsubscribe()
  stompClient?.deactivate()
})
</script>
