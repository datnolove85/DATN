<template>
  <section class="chatbot" aria-live="polite">
    <Transition name="chatbot-panel">
      <div v-if="isOpen" class="chatbot__panel" role="dialog" aria-label="Tro ly mua sam AI">
        <header class="chatbot__header">
          <div>
            <p class="chatbot__eyebrow">STYLEHUB AI</p>
            <h2>Tro ly mua sam</h2>
            <span><i></i> San sang tu van</span>
          </div>
          <button
            type="button"
            class="chatbot__close"
            aria-label="Dong chatbot"
            @click="isOpen = false"
          >
            ×
          </button>
        </header>

        <div ref="messagesEl" class="chatbot__messages">
          <article v-for="item in messages" :key="item.id" :class="['chatbot__message', item.role]">
            <span v-if="item.role === 'model'" class="chatbot__avatar">AI</span>
            <p>{{ item.text }}</p>
          </article>
          <article v-if="isLoading" class="chatbot__message model">
            <span class="chatbot__avatar">AI</span>
            <p class="chatbot__typing"><b></b><b></b><b></b></p>
          </article>
        </div>

        <div class="chatbot__suggestions">
          <button
            v-for="suggestion in suggestions"
            :key="suggestion"
            type="button"
            @click="sendSuggestion(suggestion)"
          >
            {{ suggestion }}
          </button>
        </div>

        <form class="chatbot__form" @submit.prevent="sendMessage">
          <input
            v-model="draft"
            :disabled="isLoading"
            maxlength="2000"
            autocomplete="off"
            placeholder="Hoi ve san pham, size, dat hang..."
            aria-label="Noi dung tin nhan"
          />
          <button type="submit" :disabled="isLoading || !draft.trim()" aria-label="Gui tin nhan">
            ➤
          </button>
        </form>
      </div>
    </Transition>

    <button
      type="button"
      class="chatbot__toggle"
      :aria-expanded="isOpen"
      aria-label="Mo tro ly mua sam AI"
      @click="toggle"
    >
      <span v-if="!isOpen">✦</span>
      <span v-else>×</span>
      <em>ChatBot AI</em>
    </button>
  </section>
</template>

<script setup>
import { nextTick, ref } from 'vue'
import { sendChatbotMessage } from '@/service/ChatbotService'

const isOpen = ref(false)
const isLoading = ref(false)
const draft = ref('')
const messagesEl = ref(null)
let nextId = 1

const messages = ref([
  {
    id: nextId++,
    role: 'model',
    text: 'Chao ban! Minh co the tu van san pham, size, chat lieu va cach dat hang tai StyleHub.',
  },
])

const suggestions = ['Tu van size', 'San pham con hang?', 'Cach dat hang']

const scrollToLatest = async () => {
  await nextTick()
  if (messagesEl.value) messagesEl.value.scrollTop = messagesEl.value.scrollHeight
}

const toggle = () => {
  isOpen.value = !isOpen.value
  if (isOpen.value) scrollToLatest()
}

const sendSuggestion = (suggestion) => {
  draft.value = suggestion
  sendMessage()
}

const sendMessage = async () => {
  const text = draft.value.trim()
  if (!text || isLoading.value) return

  const history = messages.value.slice(-10).map((item) => ({ role: item.role, text: item.text }))

  messages.value.push({ id: nextId++, role: 'user', text })
  draft.value = ''
  isLoading.value = true
  await scrollToLatest()

  try {
    const response = await sendChatbotMessage({ message: text, history })
    messages.value.push({ id: nextId++, role: 'model', text: response.answer })
  } catch (error) {
    messages.value.push({
      id: nextId++,
      role: 'model',
      text: error.message || 'Chatbot tam thoi khong san sang. Vui long thu lai sau.',
    })
  } finally {
    isLoading.value = false
    await scrollToLatest()
  }
}
</script>

<style scoped>
.chatbot {
  position: fixed;
  z-index: 60;
  right: 24px;
  bottom: 24px;
  font-family: inherit;
}
.chatbot__panel {
  position: absolute;
  right: 0;
  bottom: 68px;
  width: min(390px, calc(100vw - 32px));
  overflow: hidden;
  border: 1px solid #e9d5ff;
  border-radius: 20px;
  background: #fff;
  box-shadow: 0 24px 60px rgba(76, 29, 149, 0.24);
}
.chatbot__header {
  display: flex;
  justify-content: space-between;
  padding: 18px 20px;
  color: #fff;
  background: linear-gradient(135deg, #4c1d95, #7e22ce);
}
.chatbot__header h2,
.chatbot__header p {
  margin: 0;
}
.chatbot__header h2 {
  font-size: 18px;
  font-weight: 700;
}
.chatbot__eyebrow {
  margin-bottom: 3px !important;
  font-size: 10px;
  font-weight: 800;
  letter-spacing: 0.12em;
  opacity: 0.78;
}
.chatbot__header span {
  display: block;
  margin-top: 5px;
  font-size: 12px;
  opacity: 0.9;
}
.chatbot__header i {
  display: inline-block;
  width: 7px;
  height: 7px;
  margin-right: 4px;
  border-radius: 50%;
  background: #86efac;
}
.chatbot__close {
  width: 30px;
  height: 30px;
  border: 0;
  border-radius: 50%;
  color: #fff;
  background: rgba(255, 255, 255, 0.14);
  font-size: 24px;
  line-height: 1;
  cursor: pointer;
}
.chatbot__messages {
  display: flex;
  flex-direction: column;
  gap: 11px;
  height: 300px;
  padding: 16px;
  overflow-y: auto;
  background: #fafafa;
}
.chatbot__message {
  display: flex;
  align-items: flex-end;
  gap: 8px;
  max-width: 90%;
}
.chatbot__message.user {
  align-self: flex-end;
}
.chatbot__message p {
  margin: 0;
  padding: 10px 13px;
  border-radius: 14px 14px 14px 4px;
  color: #312e81;
  background: #ede9fe;
  font-size: 14px;
  line-height: 1.45;
  white-space: pre-wrap;
}
.chatbot__message.user p {
  border-radius: 14px 14px 4px 14px;
  color: #fff;
  background: #6d28d9;
}
.chatbot__avatar {
  flex: 0 0 auto;
  display: grid;
  width: 26px;
  height: 26px;
  place-items: center;
  border-radius: 50%;
  color: #fff;
  background: #7e22ce;
  font-size: 10px;
  font-weight: 800;
}
.chatbot__typing {
  display: flex;
  gap: 4px;
  min-width: 52px;
}
.chatbot__typing b {
  width: 6px;
  height: 6px;
  border-radius: 99px;
  background: #7c3aed;
  animation: blink 1.2s infinite ease-in-out;
}
.chatbot__typing b:nth-child(2) {
  animation-delay: 0.15s;
}
.chatbot__typing b:nth-child(3) {
  animation-delay: 0.3s;
}
.chatbot__suggestions {
  display: flex;
  gap: 7px;
  padding: 10px 12px;
  overflow-x: auto;
  border-top: 1px solid #f3e8ff;
}
.chatbot__suggestions button {
  flex: 0 0 auto;
  border: 1px solid #ddd6fe;
  border-radius: 999px;
  padding: 6px 9px;
  color: #6d28d9;
  background: #faf5ff;
  font-size: 11px;
  cursor: pointer;
}
.chatbot__form {
  display: flex;
  gap: 8px;
  padding: 12px;
  border-top: 1px solid #f3f4f6;
}
.chatbot__form input {
  min-width: 0;
  flex: 1;
  border: 1px solid #d1d5db;
  border-radius: 10px;
  padding: 10px 12px;
  outline: none;
  font-size: 13px;
}
.chatbot__form input:focus {
  border-color: #8b5cf6;
  box-shadow: 0 0 0 3px #ede9fe;
}
.chatbot__form button,
.chatbot__toggle {
  border: 0;
  cursor: pointer;
}
.chatbot__form button {
  width: 38px;
  border-radius: 10px;
  color: #fff;
  background: #6d28d9;
  font-size: 18px;
}
.chatbot__form button:disabled {
  cursor: not-allowed;
  opacity: 0.45;
}
.chatbot__toggle {
  display: flex;
  align-items: center;
  gap: 7px;
  min-height: 52px;
  border-radius: 999px;
  padding: 0 17px;
  color: #fff;
  background: linear-gradient(135deg, #581c87, #7e22ce);
  box-shadow: 0 10px 24px rgba(107, 33, 168, 0.35);
  font-weight: 700;
}
.chatbot__toggle span {
  font-size: 21px;
}
.chatbot__toggle em {
  font-size: 14px;
  font-style: normal;
}
.chatbot-panel-enter-active,
.chatbot-panel-leave-active {
  transition:
    opacity 0.18s ease,
    transform 0.18s ease;
}
.chatbot-panel-enter-from,
.chatbot-panel-leave-to {
  opacity: 0;
  transform: translateY(12px) scale(0.97);
}
@keyframes blink {
  0%,
  80%,
  100% {
    opacity: 0.25;
    transform: translateY(0);
  }
  40% {
    opacity: 1;
    transform: translateY(-3px);
  }
}
@media (max-width: 520px) {
  .chatbot {
    right: 16px;
    bottom: 16px;
  }
  .chatbot__panel {
    bottom: 62px;
  }
  .chatbot__messages {
    height: min(300px, 48vh);
  }
  .chatbot__toggle em {
    display: none;
  }
  .chatbot__toggle {
    width: 52px;
    justify-content: center;
    padding: 0;
  }
}
</style>
