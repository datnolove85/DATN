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
        <div
          class="flex items-center gap-3 px-3 py-2 rounded-2xl bg-slate-100 hover:bg-slate-200 transition cursor-pointer"
        >
          <div>
            <p class="text-sm font-semibold text-slate-800">Nguyễn Đạt</p>

            <p class="text-[11px] text-slate-500">Super Admin</p>
          </div>

          <div
            class="w-10 h-10 rounded-xl bg-gradient-to-r from-indigo-600 to-purple-600 text-white flex items-center justify-center font-bold"
          >
            ND
          </div>
        </div>
      </div>
    </div>
  </header>
</template>
