<template>
  <div class="h-full flex flex-col space-y-3">
    <!-- Header -->
    <div class="flex items-center justify-between pb-2.5 border-b border-slate-200">
      <div>
        <div class="flex items-center gap-2">
          <span class="h-2 w-2 rounded-full bg-indigo-600 ring-4 ring-indigo-50"></span>
          <span class="text-[11px] font-bold uppercase tracking-wider text-slate-500">
            Customer Support Hub
          </span>
        </div>
        <h1 class="text-base font-extrabold tracking-tight text-slate-900 mt-0.5">
          Quản lý hội thoại trực tuyến
        </h1>
      </div>
      <button
        class="inline-flex items-center gap-1.5 rounded-lg border border-slate-300 bg-white px-3 py-1.5 text-xs font-bold text-slate-700 shadow-2xs hover:bg-slate-50 hover:border-slate-400 transition-all focus:outline-none focus:ring-2 focus:ring-indigo-500/20"
        @click="loadInbox"
      >
        <svg
          class="w-3.5 h-3.5 text-slate-500"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2.5"
            d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"
          />
        </svg>
        Làm mới
      </button>
    </div>

    <!-- Main Chat Workspace -->
    <section
      class="grid flex-1 min-h-[520px] max-h-[calc(100vh-10rem)] overflow-hidden rounded-xl border border-slate-300 bg-white shadow-sm lg:grid-cols-[280px_1fr]"
    >
      <!-- Sidebar / Inbox List -->
      <aside class="flex flex-col border-r border-slate-200 bg-slate-50/60 min-h-0">
        <div class="p-3 border-b border-slate-200 bg-white space-y-2.5">
          <div class="flex items-center justify-between">
            <span class="text-[11px] font-extrabold text-slate-700 uppercase tracking-wide"
              >Danh sách hội thoại</span
            >
            <span
              class="inline-flex items-center rounded-full bg-indigo-50 px-2 py-0.5 text-[10px] font-extrabold text-indigo-700"
            >
              {{ inbox.length }}
            </span>
          </div>
          <div class="relative">
            <span
              class="absolute inset-y-0 left-0 flex items-center pl-2.5 pointer-events-none text-slate-400"
            >
              <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2.5"
                  d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
                />
              </svg>
            </span>
            <input
              v-model="keyword"
              class="w-full rounded-lg border border-slate-300 bg-white pl-8 pr-3 py-1.5 text-xs font-medium text-slate-900 placeholder-slate-400 outline-none focus:border-indigo-600 focus:ring-2 focus:ring-indigo-500/25 transition-all shadow-2xs"
              placeholder="Tìm kiếm khách hàng..."
            />
          </div>
        </div>

        <div class="flex-1 overflow-y-auto divide-y divide-slate-100">
          <button
            v-for="item in filteredInbox"
            :key="item.id"
            class="flex w-full items-start gap-2.5 p-3 text-left transition-colors hover:bg-slate-100/80 relative"
            :class="
              selected?.id === item.id
                ? 'bg-white shadow-2xs border-l-3 border-indigo-600'
                : 'bg-transparent'
            "
            @click="selectConversation(item)"
          >
            <div
              class="grid h-8 w-8 shrink-0 place-items-center rounded-full bg-indigo-100 text-[11px] font-extrabold text-indigo-700 border border-indigo-200/60"
            >
              {{ initials(item.tenKhachHang) }}
            </div>
            <div class="min-w-0 flex-1">
              <div class="flex items-center justify-between gap-1">
                <p class="truncate text-xs font-bold text-slate-900">{{ item.tenKhachHang }}</p>
                <span class="text-[10px] font-semibold text-slate-400 shrink-0">{{
                  formatTime(item.tinNhanCuoiLuc)
                }}</span>
              </div>
              <p class="mt-0.5 truncate text-[11px] font-medium text-slate-600">
                {{ item.tinNhanCuoi || 'Chưa có nội dung tin nhắn' }}
              </p>
              <div v-if="item.soTinChuaDoc" class="mt-1">
                <span
                  class="inline-flex rounded-full bg-indigo-600 px-1.5 py-0.2 text-[9px] font-bold text-white"
                >
                  {{ item.soTinChuaDoc }} mới
                </span>
              </div>
            </div>
          </button>
          <div
            v-if="!filteredInbox.length"
            class="p-6 text-center text-xs font-medium text-slate-400"
          >
            Không tìm thấy hội thoại.
          </div>
        </div>
      </aside>

      <!-- Chat Room Panel -->
      <main class="flex flex-col bg-white min-h-0 overflow-hidden">
        <template v-if="conversation">
          <!-- Chat Header -->
          <header
            class="flex items-center justify-between border-b border-slate-200 px-4 py-2.5 bg-white shrink-0"
          >
            <div class="flex items-center gap-2.5">
              <div
                class="grid h-8 w-8 place-items-center rounded-full bg-indigo-100 text-[11px] font-extrabold text-indigo-700 border border-indigo-200/60"
              >
                {{ initials(conversation.tenKhachHang) }}
              </div>
              <div>
                <h2 class="text-xs font-bold text-slate-900">{{ conversation.tenKhachHang }}</h2>
                <div class="flex items-center gap-1.5 mt-0.2">
                  <span
                    class="h-1.5 w-1.5 rounded-full"
                    :class="
                      conversation.trangThai === 'OPEN'
                        ? 'bg-emerald-500 animate-pulse'
                        : 'bg-slate-400'
                    "
                  ></span>
                  <p class="text-[10px] font-semibold text-slate-500">
                    {{ conversation.trangThai === 'OPEN' ? 'Đang trực tuyến' : 'Đã đóng' }}
                  </p>
                </div>
              </div>
            </div>
            <button
              v-if="conversation.trangThai === 'OPEN'"
              class="rounded-lg border border-slate-200 px-3 py-1 text-[11px] font-bold text-slate-600 hover:bg-red-50 hover:text-red-700 hover:border-red-200 transition-colors shadow-2xs"
              @click="closeConversation"
            >
              Kết thúc
            </button>
          </header>

          <!-- Messages Log -->
          <div ref="messagesEl" class="flex-1 space-y-3 overflow-y-auto p-4 bg-slate-50/30 min-h-0">
            <article
              v-for="message in conversation.messages"
              :key="message.id"
              class="flex"
              :class="message.cuaToi ? 'justify-end' : 'justify-start'"
            >
              <div class="max-w-[70%]">
                <div
                  class="rounded-xl px-3.5 py-2 text-xs leading-relaxed shadow-2xs font-medium"
                  :class="
                    message.cuaToi
                      ? 'bg-indigo-600 text-white rounded-br-xs'
                      : 'bg-white text-slate-900 border border-slate-200 rounded-bl-xs'
                  "
                >
                  {{ message.noiDung }}
                </div>
                <div
                  class="mt-0.5 text-[10px] font-medium text-slate-400 px-0.5"
                  :class="message.cuaToi ? 'text-right' : 'text-left'"
                >
                  {{ formatTime(message.thoiGian) }}
                </div>
              </div>
            </article>
          </div>

          <!-- Chat Input Form -->
          <form class="border-t border-slate-200 p-3 bg-white shrink-0" @submit.prevent="send">
            <div class="flex items-center gap-2">
              <textarea
                v-model="draft"
                rows="1"
                maxlength="4000"
                class="max-h-20 min-h-[38px] flex-1 resize-none rounded-lg border border-slate-300 bg-slate-50/40 px-3 py-2 text-xs font-medium text-slate-900 placeholder-slate-400 outline-none focus:border-indigo-600 focus:bg-white focus:ring-2 focus:ring-indigo-500/20 transition-all shadow-2xs"
                placeholder="Nhập nội dung tin nhắn phản hồi..."
                @keydown.enter.exact.prevent="send"
              ></textarea>
              <button
                class="inline-flex h-[38px] items-center justify-center rounded-lg bg-indigo-600 px-4 text-xs font-bold text-white hover:bg-indigo-700 disabled:opacity-40 transition-colors shadow-2xs shrink-0"
                :disabled="sending || !draft.trim()"
              >
                Gửi
              </button>
            </div>
          </form>
        </template>

        <!-- Empty State -->
        <div
          v-else
          class="flex flex-1 flex-col items-center justify-center p-6 text-center bg-slate-50/20"
        >
          <div
            class="h-10 w-10 rounded-full bg-indigo-50 flex items-center justify-center mb-2.5 text-indigo-600 shadow-2xs border border-indigo-100"
          >
            <MessageCircle :size="20" />
          </div>
          <p class="text-xs font-bold text-slate-700">Chưa chọn phiên hội thoại</p>
          <p class="text-[10px] font-medium text-slate-400 mt-0.5">
            Chọn một khách hàng ở danh sách bên trái để bắt đầu nhắn tin.
          </p>
        </div>
      </main>
    </section>
  </div>
</template>

<script setup>
import { computed, nextTick, onBeforeUnmount, onMounted, ref } from 'vue'
import { Client } from '@stomp/stompjs'
import { MessageCircle } from 'lucide-vue-next'
import { useChatBadge } from '@/composables/useChatBadge'
const { updateBadgeCount } = useChatBadge()
import {
  closeChatConversation,
  getChatConversation,
  getChatInbox,
  markChatRead,
  replyChatMessage,
  API_BASE_URL,
} from '@/service/ChatService'

const inbox = ref([])
const keyword = ref('')
const selected = ref(null)
const conversation = ref(null)
const draft = ref('')
const sending = ref(false)
const messagesEl = ref(null)
const activeConversationId = ref(null)

let stompClient = null
let inboxSubscription = null
let currentChatSub = null

const filteredInbox = computed(() => {
  const q = keyword.value.trim().toLowerCase()
  return inbox.value.filter((item) => !q || item.tenKhachHang?.toLowerCase().includes(q))
})

const initials = (name) =>
  (name || '?')
    .trim()
    .split(/\s+/)
    .slice(-2)
    .map((x) => x[0])
    .join('')
    .toUpperCase()

const formatTime = (value) =>
  value ? new Date(value).toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' }) : ''
const token = () => sessionStorage.getItem('token')

async function loadInbox() {
  inbox.value = await getChatInbox()
  updateBadgeCount() // <--- Thêm dòng này để đồng bộ số lượng
}

function subscribeToCurrentChat() {
  if (currentChatSub) {
    currentChatSub.unsubscribe()
    currentChatSub = null
  }
  if (stompClient?.active && activeConversationId.value) {
    currentChatSub = stompClient.subscribe(`/topic/chat/${activeConversationId.value}`, (frame) => {
      const message = JSON.parse(frame.body)
      if (conversation.value && !conversation.value.messages.some((m) => m.id === message.id)) {
        conversation.value.messages.push(message)
      }
      markChatRead(activeConversationId.value).catch(() => {})
      scrollBottom()
    })
  }
}

async function selectConversation(item) {
  selected.value = item
  activeConversationId.value = item.id
  conversation.value = await getChatConversation(item.id)
  await markChatRead(item.id)
  item.soTinChuaDoc = 0

  updateBadgeCount() // <--- Thêm dòng này để badge trên Navbar trừ đi khi đã đọc

  scrollBottom()
  subscribeToCurrentChat()
}

async function send() {
  if (!draft.value.trim() || !conversation.value || sending.value) return
  const text = draft.value.trim()
  draft.value = ''
  sending.value = true
  try {
    const sent = await replyChatMessage(conversation.value.id, text)
    sent.cuaToi = true

    if (!conversation.value.messages.some((m) => m.id === sent.id))
      conversation.value.messages.push(sent)

    const currentId = conversation.value.id
    await loadInbox()

    if (currentId) {
      selected.value = inbox.value.find((item) => item.id === currentId) || selected.value
    }

    scrollBottom()
  } catch (error) {
    draft.value = text
    window.alert(error?.message || 'Không thể gửi tin nhắn.')
  } finally {
    sending.value = false
  }
}

async function closeConversation() {
  if (!conversation.value) return
  await closeChatConversation(conversation.value.id)
  conversation.value.trangThai = 'CLOSED'
  await loadInbox()
}

async function scrollBottom() {
  await nextTick()
  if (messagesEl.value) messagesEl.value.scrollTop = messagesEl.value.scrollHeight
}

function connectSocket() {
  if (stompClient?.active) return
  stompClient = new Client({
    brokerURL: `${API_BASE_URL.replace(/^http:/, 'ws:').replace(/^https:/, 'wss:')}/ws`,
    reconnectDelay: 5000,
    connectHeaders: { Authorization: `Bearer ${token()}` },
    onConnect: () => {
      inboxSubscription = stompClient.subscribe('/topic/admin/inbox', async (frame) => {
        const message = JSON.parse(frame.body)
        const row = inbox.value.find((item) => item.id === message.idConversation)

        if (row) {
          row.tinNhanCuoi = message.noiDung
          row.tinNhanCuoiLuc = message.thoiGian
          if (!selected.value || selected.value.id !== message.idConversation) {
            row.soTinChuaDoc += 1
          }
        } else {
          await loadInbox()
        }
      })
      updateBadgeCount()
      subscribeToCurrentChat()
    },
  })
  stompClient.activate()
}

onMounted(async () => {
  await loadInbox()
  connectSocket()
})

onBeforeUnmount(() => {
  if (inboxSubscription) inboxSubscription.unsubscribe()
  if (currentChatSub) currentChatSub.unsubscribe()
  stompClient?.deactivate()
})
</script>
