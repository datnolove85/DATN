<template>
  <div class="fixed bottom-6 right-6 z-[80] font-sans">
    <!-- Khung Chat Chính -->
    <Transition name="chat-pop">
      <section
        v-if="open"
        class="mb-3 flex h-[min(65vh,520px)] w-[min(360px,calc(100vw-24px))] flex-col overflow-hidden rounded-2xl border border-slate-800 bg-slate-900 text-slate-100 shadow-2xl shadow-slate-950/60 backdrop-blur-xl"
      >
        <!-- Header Thu Gọn -->
        <header class="flex items-center justify-between bg-[#7f1d2d] px-3.5 py-3 text-white">
          <div class="flex items-center gap-2.5">
            <div
              class="grid h-8 w-8 place-items-center rounded-xl bg-white/10 border border-white/15"
            >
              <Sparkles :size="16" class="text-amber-300" />
            </div>
            <div>
              <p class="text-[9px] font-black uppercase tracking-[0.15em] text-white/70">
                K-ZONE AI
              </p>
              <h2 class="text-xs font-black tracking-wide">Trung tâm trợ lý ảo</h2>
            </div>
          </div>
          <div class="flex items-center gap-1">
            <button
              class="grid h-7 w-7 place-items-center rounded-lg text-white/80 hover:bg-white/10 hover:text-white transition-colors"
              @click="minimized = !minimized"
            >
              <Minus :size="15" />
            </button>
            <button
              class="grid h-7 w-7 place-items-center rounded-lg text-white/80 hover:bg-white/10 hover:text-white transition-colors"
              @click="open = false"
            >
              <X :size="16" />
            </button>
          </div>
        </header>

        <template v-if="!minimized">
          <!-- Thanh Tab Chuyển Đổi -->
          <div class="flex border-b border-slate-800 bg-slate-950/60 p-1 gap-1">
            <button
              @click="activeTab = 'ai'"
              class="flex-1 flex items-center justify-center gap-1.5 py-1.5 rounded-lg text-[11px] font-bold transition-all"
              :class="
                activeTab === 'ai'
                  ? 'bg-slate-800 text-white shadow-sm border border-slate-700/60'
                  : 'text-slate-400 hover:text-slate-200'
              "
            >
              <Sparkles :size="13" class="text-amber-400" />
              Trợ lý AI (24/7)
            </button>
            <button
              @click="activeTab = 'staff'"
              class="flex-1 flex items-center justify-center gap-1.5 py-1.5 rounded-lg text-[11px] font-bold transition-all relative"
              :class="
                activeTab === 'staff'
                  ? 'bg-slate-800 text-white shadow-sm border border-slate-700/60'
                  : 'text-slate-400 hover:text-slate-200'
              "
            >
              <Headset :size="13" />
              Nhân viên shop
              <span
                v-if="unread > 0"
                class="absolute top-1 right-1.5 flex h-3.5 w-3.5 items-center justify-center rounded-full bg-rose-600 text-[8px] font-bold text-white"
              >
                {{ unread }}
              </span>
            </button>
          </div>

          <!-- TAB 1: TRỢ LÝ AI -->
          <div v-if="activeTab === 'ai'" class="flex flex-1 flex-col overflow-hidden bg-slate-950">
            <div class="border-b border-slate-800/60 bg-slate-900/40 px-3 py-1.5">
              <p class="text-[10px] font-bold text-amber-400/90 flex items-center gap-1.5">
                <span class="h-1.5 w-1.5 rounded-full bg-amber-400 animate-pulse"></span>
                Tư vấn sản phẩm, size và đặt hàng tự động
              </p>
            </div>

            <div
              ref="aiMessagesEl"
              class="flex-1 space-y-2.5 overflow-y-auto px-3 py-3 custom-scrollbar text-xs"
            >
              <article
                v-for="item in aiMessages"
                :key="item.id"
                class="flex"
                :class="item.role === 'user' ? 'justify-end' : 'justify-start'"
              >
                <div
                  class="flex items-end gap-1.5 max-w-[88%]"
                  :class="item.role === 'user' ? 'flex-row-reverse' : ''"
                >
                  <span
                    v-if="item.role === 'model'"
                    class="flex h-6 w-6 shrink-0 items-center justify-center rounded-full bg-[#8b2034] text-[9px] font-black text-white shadow-sm"
                    >AI</span
                  >
                  <div
                    class="rounded-xl px-3 py-2 leading-relaxed"
                    :class="
                      item.role === 'user'
                        ? 'rounded-br-xs bg-[#8b2034] text-white shadow-sm'
                        : 'rounded-bl-xs border border-slate-800 bg-slate-900 text-slate-200'
                    "
                  >
                    {{ item.text }}
                  </div>
                </div>
              </article>

              <article v-if="aiIsLoading" class="flex justify-start">
                <div class="flex items-end gap-1.5">
                  <span
                    class="flex h-6 w-6 shrink-0 items-center justify-center rounded-full bg-[#8b2034] text-[9px] font-black text-white shadow-sm"
                    >AI</span
                  >
                  <div
                    class="rounded-xl rounded-bl-xs border border-slate-800 bg-slate-900 px-3 py-2 text-slate-400 flex items-center gap-1"
                  >
                    <span class="h-1.5 w-1.5 rounded-full bg-slate-500 animate-bounce"></span>
                    <span
                      class="h-1.5 w-1.5 rounded-full bg-slate-500 animate-bounce [animation-delay:0.2s]"
                    ></span>
                    <span
                      class="h-1.5 w-1.5 rounded-full bg-slate-500 animate-bounce [animation-delay:0.4s]"
                    ></span>
                  </div>
                </div>
              </article>
            </div>

            <div class="border-t border-slate-800 bg-slate-900/80 p-2.5">
              <div class="mb-1.5 flex gap-1 overflow-x-auto pb-0.5 no-scrollbar">
                <button
                  v-for="suggestion in aiSuggestions"
                  :key="suggestion"
                  type="button"
                  @click="sendAiSuggestion(suggestion)"
                  class="shrink-0 rounded-full border border-slate-700 bg-slate-800/80 px-2.5 py-1 text-[10px] font-semibold text-slate-300 hover:border-[#8b2034] hover:text-white transition-colors"
                >
                  {{ suggestion }}
                </button>
              </div>
              <form class="flex items-end gap-1.5" @submit.prevent="sendAiMessage">
                <textarea
                  v-model="aiDraft"
                  :disabled="aiIsLoading"
                  rows="1"
                  maxlength="2000"
                  class="max-h-20 min-h-9 flex-1 resize-none rounded-lg border border-slate-800 bg-slate-950 px-2.5 py-1.5 text-xs text-slate-200 outline-none focus:border-[#8b2034]"
                  placeholder="Nhập câu hỏi..."
                  @keydown.enter.exact.prevent="sendAiMessage"
                ></textarea>
                <button
                  type="submit"
                  class="grid h-9 w-9 shrink-0 place-items-center rounded-lg bg-[#8b2034] text-white disabled:opacity-40 hover:bg-[#721929] transition-colors"
                  :disabled="aiIsLoading || !aiDraft.trim()"
                >
                  <Send :size="15" />
                </button>
              </form>
            </div>
          </div>

          <!-- TAB 2: NHÂN VIÊN HỖ TRỢ -->
          <div v-else class="flex flex-1 flex-col overflow-hidden bg-slate-950">
            <div
              class="flex items-center justify-between border-b border-slate-800/60 bg-slate-900/40 px-3 py-2"
            >
              <div>
                <p class="text-[9px] font-black uppercase tracking-wider text-rose-400">
                  Hỗ trợ trực tuyến
                </p>
                <p class="text-[11px] text-slate-400">Nhân viên sẽ phản hồi</p>
              </div>
              <span
                v-if="conversation?.trangThai === 'OPEN'"
                class="inline-flex items-center gap-1 rounded-full bg-emerald-500/10 px-2 py-0.5 text-[9px] font-bold text-emerald-400 border border-emerald-500/20"
              >
                <span class="h-1.5 w-1.5 rounded-full bg-emerald-400 animate-pulse"></span> Mở
              </span>
            </div>

            <div
              ref="messagesEl"
              class="flex-1 space-y-2.5 overflow-y-auto px-3 py-3 custom-scrollbar text-xs"
            >
              <div v-if="!loggedIn" class="flex h-full items-center justify-center text-center">
                <div class="max-w-[220px]">
                  <MessageCircle :size="28" class="mx-auto mb-2 text-[#8b2034]" />
                  <h3 class="font-bold text-white text-xs">Đăng nhập để chat với Shop</h3>
                  <p class="mt-1 text-[11px] leading-relaxed text-slate-400">
                    Xem lịch sử và nhận tin nhắn phản hồi từ nhân viên.
                  </p>
                  <RouterLink
                    to="/login"
                    class="mt-2.5 inline-flex rounded-lg bg-[#8b2034] px-3 py-1.5 text-[11px] font-bold text-white hover:bg-[#721929] transition-colors"
                    >Đăng nhập ngay</RouterLink
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
                  <div class="max-w-[85%]">
                    <div
                      class="rounded-xl px-3 py-2 leading-relaxed"
                      :class="
                        message.cuaToi
                          ? 'rounded-br-xs bg-[#8b2034] text-white shadow-sm'
                          : 'rounded-bl-xs border border-slate-800 bg-slate-900 text-slate-200'
                      "
                    >
                      {{ message.noiDung }}
                    </div>
                    <p
                      class="mt-0.5 px-1 text-[9px] text-slate-500"
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
                    class="mx-auto mb-2 grid h-10 w-10 place-items-center rounded-xl bg-slate-900 text-[#8b2034] border border-slate-800"
                  >
                    <MessagesSquare :size="18" />
                  </div>
                  <h3 class="font-bold text-white text-xs">Bắt đầu trò chuyện</h3>
                  <p class="mt-1 text-[11px] text-slate-400">Gửi câu hỏi để được hỗ trợ nhanh.</p>
                </div>
              </div>
            </div>

            <div class="border-t border-slate-800 bg-slate-900/80 p-2.5">
              <div class="mb-1.5 flex gap-1 overflow-x-auto pb-0.5 no-scrollbar">
                <button
                  v-for="suggestion in suggestions"
                  :key="suggestion"
                  class="shrink-0 rounded-full border border-slate-700 bg-slate-800/80 px-2.5 py-1 text-[10px] font-semibold text-slate-300 hover:border-[#8b2034] hover:text-white transition-colors"
                  @click="draft = suggestion"
                >
                  {{ suggestion }}
                </button>
              </div>
              <form class="flex items-end gap-1.5" @submit.prevent="send">
                <textarea
                  v-model="draft"
                  rows="1"
                  maxlength="4000"
                  class="max-h-20 min-h-9 flex-1 resize-none rounded-lg border border-slate-800 bg-slate-950 px-2.5 py-1.5 text-xs text-slate-200 outline-none focus:border-[#8b2034]"
                  placeholder="Nhập tin nhắn..."
                  @keydown.enter.exact.prevent="send"
                ></textarea>
                <button
                  class="grid h-9 w-9 shrink-0 place-items-center rounded-lg bg-[#8b2034] text-white disabled:opacity-40 hover:bg-[#721929] transition-colors"
                  :disabled="sending || !draft.trim()"
                >
                  <Send :size="15" />
                </button>
              </form>
            </div>
          </div>
        </template>
      </section>
    </Transition>

    <!-- Nút kích hoạt mở chat duy nhất khi thu gọn (Icon tin nhắn + Text + Hiệu ứng nhịp tim) -->
    <div v-if="!open">
      <button
        @click="toggle"
        class="group flex items-center gap-3 rounded-2xl border border-slate-700/80 bg-slate-900/95 px-4 py-3 text-xs text-slate-100 shadow-2xl shadow-slate-950/60 backdrop-blur-md hover:border-[#8b2034] transition-all cursor-pointer text-left"
      >
        <div
          class="grid h-9 w-9 shrink-0 place-items-center rounded-xl bg-gradient-to-tr from-[#7f1d2d] to-rose-600 text-white shadow-md relative"
        >
          <MessageCircle :size="18" />
          <span
            v-if="unread > 0"
            class="absolute -top-1 -right-1 grid h-4 w-4 place-items-center rounded-full bg-rose-600 text-[8px] font-black text-white shadow"
          >
            {{ unread > 9 ? '9+' : unread }}
          </span>
          <!-- Hiệu ứng chấm xanh chạy -->
          <span
            class="absolute -bottom-0.5 -right-0.5 h-2.5 w-2.5 rounded-full bg-emerald-400 border-2 border-slate-900 animate-pulse"
          ></span>
        </div>
        <div>
          <div class="flex items-center gap-1.5">
            <span class="font-black text-[11px] text-amber-300 tracking-wide"
              >K-ZONE Hỗ trợ 24/7</span
            >
          </div>
        </div>
      </button>
    </div>
  </div>
</template>

<script setup>
import { nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { RouterLink } from 'vue-router'
import { Client } from '@stomp/stompjs'
import { MessageCircle, MessagesSquare, Minus, Send, X, Sparkles, Headset } from 'lucide-vue-next'
import { getMyChat, markChatRead, sendChatMessage, API_BASE_URL } from '@/service/ChatService'
import { sendChatbotMessage } from '@/service/ChatbotService'

const open = ref(false)
const minimized = ref(false)
const activeTab = ref('ai')

// --- AI Chat Logic ---
const aiIsLoading = ref(false)
const aiDraft = ref('')
const aiMessagesEl = ref(null)
let aiNextId = 1

const aiMessages = ref([
  {
    id: aiNextId++,
    role: 'model',
    text: 'Chào bạn! Mình có thể tư vấn sản phẩm, size, chất liệu và cách đặt hàng tại K-Zone.',
  },
])

const aiSuggestions = ['Tư vấn size', 'Sản phẩm còn hàng?', 'Cách đặt hàng']

const scrollToAiLatest = async () => {
  await nextTick()
  if (aiMessagesEl.value) aiMessagesEl.value.scrollTop = aiMessagesEl.value.scrollHeight
}

const sendAiSuggestion = (suggestion) => {
  aiDraft.value = suggestion
  sendAiMessage()
}

const sendAiMessage = async () => {
  const text = aiDraft.value.trim()
  if (!text || aiIsLoading.value) return

  const history = aiMessages.value.slice(-10).map((item) => ({ role: item.role, text: item.text }))

  aiMessages.value.push({ id: aiNextId++, role: 'user', text })
  aiDraft.value = ''
  aiIsLoading.value = true
  await scrollToAiLatest()

  try {
    const response = await sendChatbotMessage({ message: text, history })
    aiMessages.value.push({ id: aiNextId++, role: 'model', text: response.answer })
  } catch (error) {
    aiMessages.value.push({
      id: aiNextId++,
      role: 'model',
      text: error.message || 'Chatbot tạm thời không sẵn sàng. Vui lòng thử lại sau.',
    })
  } finally {
    aiIsLoading.value = false
    await scrollToAiLatest()
  }
}

// --- Staff Chat Logic ---
const loggedIn = ref(Boolean(sessionStorage.getItem('token')))
const conversation = ref(null)
const messages = ref([])
const draft = ref('')
const sending = ref(false)
const unread = ref(0)
const messagesEl = ref(null)

let stompClient = null
let customerChatSub = null
const recentSentMessages = []

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
      const recentIndex = recentSentMessages.findIndex(
        (m) => m.text === message.noiDung && Date.now() - m.time < 10000,
      )

      if (recentIndex !== -1) {
        message.cuaToi = true
        recentSentMessages.splice(recentIndex, 1)
      } else {
        message.cuaToi = false
      }

      const existing = messages.value.find(
        (item) =>
          item.id === message.id ||
          (message.cuaToi &&
            item.noiDOG === message.noiDung &&
            item.id?.toString().startsWith('opt-')),
      )

      if (existing) {
        existing.id = message.id
        existing.thoiGian = message.thoiGian
        existing.cuaToi = true
      } else {
        messages.value.push(message)
        if (!message.cuaToi) {
          if (open.value && !minimized.value && activeTab.value === 'staff') {
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

  recentSentMessages.push({ text, time: Date.now() })
  while (recentSentMessages.length > 20) recentSentMessages.shift()

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
    } catch (error) {}
  }
}

// Theo dõi khi chuyển tab để tự động cuộn xuống tin nhắn cuối
watch(activeTab, async (tab) => {
  if (tab === 'ai') {
    await scrollToAiLatest()
  } else {
    await scrollBottom()
  }
})

watch(open, (value) => {
  if (value) {
    if (activeTab.value === 'ai') scrollToAiLatest()
    else scrollBottom()
  }
})

onMounted(async () => {
  if (token()) {
    try {
      const data = await getMyChat()
      if (data) {
        conversation.value = data
        unread.value = data?.soTinChuaDoc || 0
      }
    } catch (e) {}
    connectSocket()
  }
})

onBeforeUnmount(() => {
  if (customerChatSub) customerChatSub.unsubscribe()
  stompClient?.deactivate()
})
</script>

<style scoped>
.custom-scrollbar::-webkit-scrollbar {
  width: 4px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.15);
  border-radius: 4px;
}
.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0.3);
}
.no-scrollbar::-webkit-scrollbar {
  display: none;
}
.no-scrollbar {
  -ms-overflow-style: none;
  scrollbar-width: none;
}
</style>
