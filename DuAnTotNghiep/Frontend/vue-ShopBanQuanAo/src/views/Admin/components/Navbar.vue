<template>
  <header
    class="h-16 bg-white border-b border-slate-200 flex items-center justify-between px-6 shadow-sm z-10 shrink-0"
  >
    <div class="flex items-center space-x-4">
      <div class="relative hidden sm:block">
        <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-slate-400 text-sm"
          >🔍</span
        >
        <input
          type="text"
          placeholder="Tìm nhanh hóa đơn, sản phẩm..."
          class="w-72 pl-9 pr-4 py-1.5 bg-slate-50 border border-slate-200 text-sm rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:bg-white transition-all"
        />
      </div>
    </div>

    <div class="flex items-center space-x-4">
      <button class="relative p-2 text-slate-500 hover:bg-slate-100 rounded-full transition-colors">
        <span>🔔</span>
        <span class="absolute top-1 right-1 w-2 h-2 bg-rose-500 rounded-full"></span>
      </button>

      <div class="h-6 w-[1px] bg-slate-200"></div>

      <div
        class="flex items-center space-x-2 text-xs font-medium text-emerald-600 bg-emerald-50 px-2.5 py-1 rounded-full"
      >
        <span class="w-1.5 h-1.5 bg-emerald-500 rounded-full animate-pulse"></span>
      </div>
    </div>
  </header>
</template>

<script setup></script>
