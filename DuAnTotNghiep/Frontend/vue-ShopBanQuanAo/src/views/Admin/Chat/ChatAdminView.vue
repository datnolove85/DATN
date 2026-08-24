<template>
  <div class="min-h-full space-y-5">
    <div>
      <p class="text-xs font-black uppercase tracking-[0.18em] text-[#8b2034]">CUSTOMER SUPPORT</p>
      <div class="mt-1 flex flex-wrap items-end justify-between gap-3">
        <div>
          <h1 class="text-3xl font-black tracking-tight text-slate-900">Trò chuyện</h1>
          <p class="mt-1 text-sm text-slate-500">
            Quản lý hội thoại khách hàng theo thời gian thực.
          </p>
        </div>
        <button
          class="rounded-xl border border-slate-200 bg-white px-4 py-2.5 text-sm font-bold text-slate-700 shadow-sm hover:border-slate-300"
          @click="loadInbox"
        >
          Làm mới
        </button>
      </div>
    </div>

    <section
      class="grid min-h-[650px] overflow-hidden rounded-[28px] border border-slate-200 bg-white shadow-sm lg:grid-cols-[320px_1fr]"
    >
      <aside class="border-b border-slate-200 lg:border-b-0 lg:border-r">
        <div class="border-b border-slate-100 p-4">
          <div class="flex items-center justify-between">
            <h2 class="font-black text-slate-900">Hội thoại</h2>
            <span
              class="rounded-full bg-[#f7ebe7] px-2.5 py-1 text-[11px] font-black text-[#8b2034]"
              >{{ inbox.length }}</span
            >
          </div>
          <input
            v-model="keyword"
            class="mt-3 w-full rounded-xl border border-slate-200 bg-slate-50 px-3 py-2.5 text-sm outline-none focus:border-[#9b4657]"
            placeholder="Tìm khách hàng..."
          />
        </div>
        <div class="max-h-[570px] overflow-y-auto">
          <button
            v-for="item in filteredInbox"
            :key="item.id"
            class="flex w-full gap-3 border-b border-slate-100 p-4 text-left transition hover:bg-slate-50"
            :class="selected?.id === item.id ? 'bg-[#fdf5f3]' : ''"
            @click="selectConversation(item)"
          >
            <div
              class="grid h-11 w-11 shrink-0 place-items-center rounded-2xl bg-slate-100 font-black text-slate-500"
            >
              {{ initials(item.tenKhachHang) }}
            </div>
            <div class="min-w-0 flex-1">
              <div class="flex items-center justify-between gap-2">
                <p class="truncate text-sm font-black text-slate-900">{{ item.tenKhachHang }}</p>
                <span class="text-[10px] text-slate-400">{{
                  formatTime(item.tinNhanCuoiLuc)
                }}</span>
              </div>
              <p class="mt-1 truncate text-xs text-slate-500">
                {{ item.tinNhanCuoi || 'Chưa có tin nhắn' }}
              </p>
              <span
                v-if="item.soTinChuaDoc"
                class="mt-2 inline-flex rounded-full bg-[#8b2034] px-2 py-0.5 text-[10px] font-black text-white"
                >{{ item.soTinChuaDoc }} mới</span
              >
            </div>
          </button>
          <div v-if="!filteredInbox.length" class="p-8 text-center text-sm text-slate-400">
            Chưa có cuộc trò chuyện.
          </div>
        </div>
      </aside>

      <main class="flex min-h-[650px] flex-col bg-[#fbfaf7]">
        <template v-if="conversation">
          <header
            class="flex items-center justify-between border-b border-slate-200 bg-white px-5 py-4"
          >
            <div class="flex items-center gap-3">
              <div
                class="grid h-11 w-11 place-items-center rounded-2xl bg-[#f7ebe7] font-black text-[#8b2034]"
              >
                {{ initials(conversation.tenKhachHang) }}
              </div>
              <div>
                <h2 class="font-black text-slate-900">{{ conversation.tenKhachHang }}</h2>
                <p class="text-xs text-slate-500">
                  {{ conversation.trangThai === 'OPEN' ? 'Cuộc trò chuyện đang mở' : 'Đã đóng' }}
                </p>
              </div>
            </div>
            <button
              v-if="conversation.trangThai === 'OPEN'"
              class="rounded-xl border border-slate-200 px-3 py-2 text-xs font-bold text-slate-600 hover:text-[#8b2034]"
              @click="closeConversation"
            >
              Đóng hội thoại
            </button>
          </header>

          <div ref="messagesEl" class="flex-1 space-y-3 overflow-y-auto px-5 py-5">
            <article
              v-for="message in conversation.messages"
              :key="message.id"
              class="flex"
              :class="message.cuaToi ? 'justify-end' : 'justify-start'"
            >
              <div class="max-w-[72%]">
                <div
                  class="rounded-[20px] px-4 py-3 text-sm leading-6"
                  :class="
                    message.cuaToi
                      ? 'rounded-br-md bg-[#8b2034] text-white'
                      : 'rounded-bl-md bg-white text-slate-800 shadow-sm ring-1 ring-slate-200/70'
                  "
                >
                  {{ message.noiDung }}
                </div>
                <p
                  class="mt-1 px-1 text-[10px] text-slate-400"
                  :class="message.cuaToi ? 'text-right' : ''"
                >
                  {{ message.tenNguoiGui }} · {{ formatTime(message.thoiGian) }}
                </p>
              </div>
            </article>
          </div>

          <form class="border-t border-slate-200 bg-white p-4" @submit.prevent="send">
            <div class="flex gap-2">
              <textarea
                v-model="draft"
                rows="2"
                maxlength="4000"
                class="min-h-12 flex-1 resize-none rounded-2xl border border-slate-200 bg-slate-50 px-3 py-2.5 text-sm outline-none focus:border-[#9b4657] focus:ring-4 focus:ring-[#8b2034]/10"
                placeholder="Nhập tin nhắn cho khách..."
                @keydown.enter.exact.prevent="send"
              ></textarea>
              <button
                class="self-end rounded-2xl bg-[#8b2034] px-5 py-3 text-sm font-black text-white disabled:opacity-40"
                :disabled="sending || !draft.trim()"
              >
                Gửi
              </button>
            </div>
          </form>
        </template>
        <div v-else class="flex flex-1 items-center justify-center p-10 text-center">
          <div>
            <MessageCircle :size="46" class="mx-auto mb-4 text-[#8b2034]" />
            <h2 class="text-xl font-black text-slate-900">Chọn một cuộc trò chuyện</h2>
            <p class="mt-1 text-sm text-slate-500">Tin nhắn mới sẽ xuất hiện realtime ở đây.</p>
          </div>
        </div>
      </main>
    </section>
  </div>
</template>

<script setup>
import { computed, nextTick, onBeforeUnmount, onMounted, ref } from 'vue'
import { Client } from '@stomp/stompjs'
import { MessageCircle } from 'lucide-vue-next'
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
let stompClient = null

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
}
async function selectConversation(item) {
  selected.value = item
  conversation.value = await getChatConversation(item.id)
  await markChatRead(item.id)
  item.soTinChuaDoc = 0
  scrollBottom()
}
async function send() {
  if (!draft.value.trim() || !conversation.value || sending.value) return
  const text = draft.value.trim()
  draft.value = ''
  sending.value = true
  try {
    const sent = await replyChatMessage(conversation.value.id, text)
    if (!conversation.value.messages.some((m) => m.id === sent.id))
      conversation.value.messages.push(sent)
    await loadInbox()
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
  stompClient = new Client({
    brokerURL: `${API_BASE_URL.replace(/^http:/, 'ws:').replace(/^https:/, 'wss:')}/ws`,
    reconnectDelay: 5000,
    connectHeaders: { Authorization: `Bearer ${token()}` },
    onConnect: () => {
      stompClient.subscribe('/user/queue/chat/inbox', async (frame) => {
        const message = JSON.parse(frame.body)
        const row = inbox.value.find((item) => item.id === message.idConversation)
        if (row) {
          row.tinNhanCuoi = message.noiDung
          row.tinNhanCuoiLuc = message.thoiGian
          row.soTinChuaDoc += 1
        } else await loadInbox()
        if (conversation.value?.id === message.idConversation) {
          if (!conversation.value.messages.some((item) => item.id === message.id))
            conversation.value.messages.push(message)
          await markChatRead(conversation.value.id)
          if (row) row.soTinChuaDoc = 0
          scrollBottom()
        }
      })
      // Admin cũng nhận tin do chính mình gửi qua queue /chat, không cần poll.
      stompClient.subscribe('/user/queue/chat', (frame) => {
        const message = JSON.parse(frame.body)
        if (conversation.value?.id !== message.idConversation) return
        if (!conversation.value.messages.some((item) => item.id === message.id))
          conversation.value.messages.push(message)
        scrollBottom()
      })
    },
  })
  stompClient.activate()
}

onMounted(async () => {
  await loadInbox()
  connectSocket()
})
onBeforeUnmount(() => stompClient?.deactivate())
</script>
