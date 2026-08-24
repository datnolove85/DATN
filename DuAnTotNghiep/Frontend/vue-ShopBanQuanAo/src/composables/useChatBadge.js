import { ref } from 'vue'
import { getChatInbox } from '@/service/ChatService'

const totalUnread = ref(0)

export function useChatBadge() {
  const updateBadgeCount = async () => {
    try {
      const inbox = await getChatInbox()
      // Tính tổng số tin chưa đọc từ tất cả các hội thoại
      totalUnread.value = inbox.reduce((sum, item) => sum + (item.soTinChuaDoc || 0), 0)
    } catch (error) {
      console.error('Không thể tải số lượng tin nhắn chưa đọc:', error)
    }
  }

  return {
    totalUnread,
    updateBadgeCount,
  }
}
