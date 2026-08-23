<script setup>
import { MessageSquareText } from 'lucide-vue-next'

defineProps({
  note: String,
})
defineEmits(['update:note'])
</script>

<template>
  <section
    class="rounded-[28px] border border-slate-200/80 bg-white p-5 shadow-xl shadow-slate-200/40 sm:p-7"
  >
    <div class="flex items-center gap-3">
      <span class="grid h-11 w-11 place-items-center rounded-2xl bg-violet-50 text-violet-600">
        <MessageSquareText :size="22" />
      </span>
      <div>
        <p class="text-xs font-black uppercase tracking-[0.15em] text-violet-600">Ghi chú</p>
        <h2 class="font-black text-slate-900">Yêu cầu giao hàng</h2>
      </div>
    </div>

    <textarea
      :value="note"
      @input="$emit('update:note', $event.target.value)"
      rows="4"
      maxlength="500"
      class="mt-5 w-full resize-none rounded-2xl border border-slate-300 bg-slate-50 p-4 outline-none transition focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
      placeholder="Ví dụ: Giao hàng giờ hành chính, gọi trước khi giao..."
    ></textarea>
  </section>
</template>
