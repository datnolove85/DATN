<template>
  <div
    class="min-h-screen bg-gradient-to-br from-stone-50 via-amber-50/20 to-stone-100 text-stone-800 font-sans pb-12 selection:bg-amber-200 selection:text-stone-900"
  >
    <!-- TOAST -->
    <transition name="toast">
      <div
        v-if="toast.show"
        class="fixed top-5 right-5 z-[80] flex items-center gap-3 px-5 py-3.5 rounded-2xl shadow-xl border backdrop-blur-xl text-sm font-semibold animate-slide-in"
        :class="
          toast.type === 'success'
            ? 'bg-emerald-50/95 border-emerald-200 text-emerald-800'
            : 'bg-rose-50/95 border-rose-200 text-rose-800'
        "
      >
        <i
          :class="
            toast.type === 'success'
              ? 'fa-solid fa-circle-check text-emerald-500 text-lg animate-pulse'
              : 'fa-solid fa-circle-exclamation text-rose-500 text-lg animate-bounce'
          "
        ></i>
        <span>{{ toast.message }}</span>
      </div>
    </transition>

    <!-- NAVBAR -->
    <header
      class="sticky top-0 z-40 border-b border-stone-200/60 bg-white/80 backdrop-blur-xl shadow-xs"
    >
      <div class="max-w-7xl mx-auto px-4 md:px-6 h-16 flex items-center justify-between">
        <div class="flex items-center gap-3">
          <div class="flex items-center gap-2">
            <div
              class="w-8 h-8 rounded-xl bg-amber-500 text-white flex items-center justify-center font-bold shadow-md shadow-amber-500/20"
            >
              <i class="fa-solid fa-crown text-xs"></i>
            </div>
            <span class="font-extrabold tracking-tight text-stone-900 text-base"> REWARDS</span>
          </div>
        </div>

        <div class="flex items-center gap-2.5">
          <button
            @click="openHistoryModal"
            class="flex items-center gap-1.5 px-3 py-2 rounded-xl bg-stone-50 border border-stone-200/80 text-xs font-semibold text-stone-800 hover:bg-amber-50 hover:border-amber-200 hover:text-amber-800 transition active:scale-95 shadow-2xs"
          >
            <i class="fa-solid fa-clock-rotate-left text-amber-600"></i>
            Lịch sử xu
          </button>

          <button
            @click="openMyVouchersModal"
            class="flex items-center gap-1.5 px-3 py-2 rounded-xl bg-stone-50 border border-stone-200/80 text-xs font-semibold text-stone-800 hover:bg-amber-50 hover:border-amber-200 hover:text-amber-800 transition active:scale-95 shadow-2xs relative"
          >
            <i class="fa-solid fa-ticket text-rose-500"></i>
            Kho voucher
            <span
              v-if="unusedVouchersCount > 0"
              class="absolute -top-1.5 -right-1.5 w-4 h-4 rounded-full bg-rose-500 text-white text-[9px] flex items-center justify-center font-bold animate-pulse shadow-sm"
            >
              {{ unusedVouchersCount }}
            </span>
          </button>

          <div
            class="flex items-center gap-2 bg-amber-50/80 border border-amber-200/70 px-3 py-2 rounded-xl shadow-2xs"
          >
            <div
              class="w-6 h-6 rounded-lg bg-amber-400 text-stone-900 flex items-center justify-center shadow-xs"
            >
              <i class="fa-solid fa-coins text-[10px]"></i>
            </div>
            <div class="leading-tight">
              <div class="text-[9px] uppercase tracking-wider font-extrabold text-amber-800">
                Số dư
              </div>
              <div class="text-xs font-black text-amber-900">{{ wallet.soXu || 0 }} Xu</div>
            </div>
          </div>
        </div>
      </div>
    </header>

    <main class="max-w-7xl mx-auto px-4 md:px-6 pt-5 space-y-5">
      <!-- HERO / CHECK-IN -->
      <section
        class="relative overflow-hidden rounded-[24px] bg-gradient-to-br from-white via-amber-50/40 to-orange-50/30 text-stone-900 border border-stone-200/70 shadow-lg shadow-stone-200/50"
      >
        <div
          class="absolute -right-20 -top-24 w-80 h-80 rounded-full bg-amber-300/20 blur-3xl pointer-events-none"
        ></div>
        <div
          class="absolute -left-20 -bottom-28 w-80 h-80 rounded-full bg-orange-300/15 blur-3xl pointer-events-none"
        ></div>
        <div
          class="absolute right-12 bottom-0 text-[130px] leading-none opacity-[0.03] rotate-12 pointer-events-none"
        >
          <i class="fa-solid fa-gift"></i>
        </div>

        <div class="relative z-10 p-5 md:p-6">
          <div class="flex flex-col xl:flex-row xl:items-center xl:justify-between gap-5">
            <div class="max-w-xl">
              <div
                class="inline-flex items-center gap-2 px-2.5 py-1 rounded-full bg-amber-100/70 border border-amber-200 text-[10px] font-bold uppercase tracking-wider text-amber-900"
              >
                <span class="w-1.5 h-1.5 rounded-full bg-emerald-500 animate-ping"></span>
                Điểm danh hằng ngày
              </div>

              <h1 class="mt-3 text-2xl md:text-3xl font-black tracking-tight text-stone-900">
                Điểm danh mỗi ngày,
                <span
                  class="text-transparent bg-clip-text bg-gradient-to-r from-amber-600 to-orange-600"
                  >nhận xu liền tay</span
                >
              </h1>

              <p class="mt-2 text-xs md:text-sm font-medium leading-relaxed text-stone-700">
                Duy trì chuỗi điểm danh để tích lũy xu đổi quà và voucher độc quyền.
              </p>

              <div class="mt-4 flex flex-wrap items-center gap-2.5">
                <div
                  class="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-xl bg-white/90 border border-stone-200 text-xs font-bold text-stone-800 shadow-2xs"
                >
                  <i class="fa-solid fa-fire text-orange-500 animate-bounce"></i>
                  Chuỗi:
                  <span class="font-black text-stone-900"
                    >{{ wallet.chuoiDiemDanh || 0 }} ngày</span
                  >
                </div>
                <div
                  class="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-xl bg-amber-50/90 border border-amber-200 text-xs font-bold text-amber-900 shadow-2xs"
                >
                  <i class="fa-solid fa-coins text-amber-600"></i>
                  {{ wallet.daDiemDanhHomNay ? 'Ngày tiếp theo:' : 'Hôm nay:' }}
                  <span class="font-black text-amber-950">+{{ nextCheckinCoins }} Xu</span>
                </div>
              </div>
            </div>

            <button
              @click="performCheckIn"
              :disabled="wallet.daDiemDanhHomNay || isCheckingIn"
              class="xl:w-auto w-full px-6 py-3.5 rounded-2xl font-bold text-xs md:text-sm transition-all active:scale-95 flex items-center justify-center gap-2 shadow-md"
              :class="
                wallet.daDiemDanhHomNay
                  ? 'bg-emerald-600/90 text-white cursor-not-allowed shadow-none'
                  : 'bg-gradient-to-r from-amber-500 to-orange-500 hover:from-amber-600 hover:to-orange-600 text-white shadow-amber-500/20 hover:-translate-y-0.5'
              "
            >
              <i
                class="fa-solid"
                :class="isCheckingIn ? 'fa-spinner fa-spin' : 'fa-calendar-check'"
              ></i>
              {{
                wallet.daDiemDanhHomNay
                  ? 'Đã điểm danh hôm nay'
                  : `Điểm danh +${nextCheckinCoins} Xu`
              }}
            </button>
          </div>

          <!-- STREAK -->
          <div
            class="mt-6 rounded-2xl bg-white/90 backdrop-blur-md border border-stone-200/70 p-4 shadow-2xs"
          >
            <div class="flex items-center justify-between gap-3 mb-3">
              <div class="text-xs font-bold text-stone-800 flex items-center gap-1.5">
                <i class="fa-solid fa-route text-amber-600"></i>
                Lộ trình thưởng
              </div>
              <div
                class="text-[10px] font-bold text-amber-900 bg-amber-100/70 px-2.5 py-0.5 rounded-lg border border-amber-200"
              >
                Mốc tiếp theo: Ngày {{ (wallet.chuoiDiemDanh || 0) + 1 }}
              </div>
            </div>

            <div class="grid grid-cols-3 sm:grid-cols-4 md:grid-cols-7 gap-2">
              <div
                v-for="(milestone, idx) in previewMilestones"
                :key="idx"
                class="min-h-[72px] rounded-xl border flex flex-col items-center justify-center text-center p-1.5 transition-all duration-300"
                :class="
                  milestone.ngayThu <= wallet.chuoiDiemDanh
                    ? 'bg-emerald-50/80 border-emerald-200 text-emerald-900'
                    : milestone.ngayThu === wallet.chuoiDiemDanh + 1 && !wallet.daDiemDanhHomNay
                      ? 'bg-gradient-to-b from-amber-400 to-amber-500 text-stone-950 border-amber-300 shadow-md shadow-amber-500/20 scale-[1.03]'
                      : 'bg-stone-50/70 border-stone-200/60 text-stone-600'
                "
              >
                <span class="text-[9px] uppercase tracking-wider font-extrabold opacity-80">
                  Ngày {{ milestone.ngayThu }}
                </span>
                <div class="mt-0.5 font-black text-xs flex items-center gap-1">
                  <i class="fa-solid fa-coins text-amber-600"></i>
                  {{ milestone.soXuThuong }}
                </div>
                <span class="mt-0.5 text-[8px] font-bold opacity-80">
                  {{
                    milestone.ngayThu <= wallet.chuoiDiemDanh
                      ? 'Đã nhận'
                      : milestone.ngayThu === wallet.chuoiDiemDanh + 1 && !wallet.daDiemDanhHomNay
                        ? 'Hôm nay'
                        : 'Chưa mở'
                  }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- MINI GAME -->
      <section>
        <div class="flex items-end justify-between gap-3 mb-4">
          <div>
            <div class="text-[10px] uppercase tracking-[.2em] font-extrabold text-amber-700">
              Mini Game
            </div>
            <h2 class="text-xl md:text-2xl font-black tracking-tight text-stone-900">
              Giải trí nhận quà
            </h2>
          </div>
          <div class="hidden sm:flex items-center gap-2 text-xs font-bold text-stone-600">
            <span class="w-2 h-2 rounded-full bg-emerald-500 animate-pulse"></span>
            Sẵn sàng chơi
          </div>
        </div>

        <div class="grid grid-cols-1 lg:grid-cols-2 gap-4">
          <!-- WHEEL -->
          <div
            class="relative overflow-hidden rounded-[22px] bg-white border border-stone-200/70 shadow-sm hover:shadow-md transition-all"
          >
            <div
              class="absolute top-0 left-0 right-0 h-1 bg-gradient-to-r from-amber-400 via-orange-400 to-rose-400"
            ></div>

            <div class="p-5 md:p-6">
              <div class="flex items-start justify-between gap-4">
                <div>
                  <div
                    class="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-lg bg-amber-50 text-amber-900 text-[10px] font-bold uppercase tracking-wider border border-amber-200/60"
                  >
                    <i class="fa-solid fa-fire text-orange-500"></i>
                    Hot
                  </div>
                  <h3 class="mt-2 text-lg font-black text-stone-900">Vòng quay may mắn</h3>
                  <p class="mt-1 text-xs font-medium leading-relaxed text-stone-600">
                    Thử vận may mỗi lượt chỉ với 5 Xu.
                  </p>
                </div>
                <div
                  class="shrink-0 px-2.5 py-1.5 rounded-xl bg-amber-50 border border-amber-200 text-amber-900 text-[10px] font-bold"
                >
                  <i class="fa-solid fa-coins mr-1 text-amber-600"></i>5 Xu/lượt
                </div>
              </div>

              <div class="relative w-[280px] h-[280px] md:w-[320px] md:h-[320px] mx-auto my-4">
                <!-- pointer -->
                <div class="absolute -top-1 left-1/2 -translate-x-1/2 z-30">
                  <div
                    class="w-0 h-0 border-l-[12px] border-l-transparent border-r-[12px] border-r-transparent border-t-[26px] border-t-rose-600 drop-shadow-md"
                  ></div>
                  <div
                    class="absolute -top-2 left-1/2 -translate-x-1/2 w-4 h-4 rounded-full bg-white border-3 border-rose-600 shadow-sm"
                  ></div>
                </div>

                <!-- outer ring -->
                <div
                  class="absolute inset-0 rounded-full border-[8px] border-stone-100 shadow-inner bg-white"
                ></div>

                <div
                  class="absolute inset-[6px] rounded-full border-3 border-stone-200 overflow-hidden shadow-sm"
                  :style="{
                    transform: `rotate(${wheelAngle}deg)`,
                    transition: isSpinning
                      ? 'transform 4s cubic-bezier(0.15, 0.85, 0.15, 1)'
                      : 'none',
                  }"
                >
                  <svg viewBox="0 0 300 300" class="w-full h-full">
                    <g v-for="(slice, index) in wheelSlices" :key="index">
                      <path
                        :d="getSlicePath(index, wheelSlices.length)"
                        :fill="sliceColors[index % sliceColors.length]"
                        stroke="#ffffff"
                        stroke-width="2"
                      />
                      <g
                        :transform="`rotate(${index * (360 / wheelSlices.length) + 360 / wheelSlices.length / 2}, 150, 150)`"
                      >
                        <text
                          x="150"
                          y="78"
                          fill="#292524"
                          font-size="10"
                          font-weight="bold"
                          text-anchor="middle"
                          dominant-baseline="middle"
                        >
                          <tspan
                            v-for="(line, lineIndex) in getWheelLabelLines(
                              slice.tenPhanThuong || slice.name,
                            )"
                            :key="lineIndex"
                            x="150"
                            :dy="
                              lineIndex === 0
                                ? -(
                                    (getWheelLabelLines(slice.tenPhanThuong || slice.name).length -
                                      1) *
                                    6
                                  )
                                : 12
                            "
                          >
                            {{ line }}
                          </tspan>
                        </text>
                      </g>
                    </g>
                  </svg>
                </div>

                <!-- center button -->
                <button
                  @click="spinWheel"
                  :disabled="isSpinning || wallet.soXu < 5"
                  class="absolute z-20 left-1/2 top-1/2 -translate-x-1/2 -translate-y-1/2 w-[72px] h-[72px] rounded-full border-4 border-white shadow-lg flex flex-col items-center justify-center transition-transform active:scale-95"
                  :class="
                    isSpinning || wallet.soXu < 5
                      ? 'bg-stone-300 cursor-not-allowed'
                      : 'bg-gradient-to-br from-amber-500 to-orange-600 hover:scale-105 text-white shadow-amber-500/30'
                  "
                >
                  <i
                    class="fa-solid fa-play text-white text-sm"
                    :class="{ 'animate-spin': isSpinning }"
                  ></i>
                  <span class="text-[9px] font-extrabold text-white mt-0.5">
                    {{ isSpinning ? '...' : 'QUAY' }}
                  </span>
                </button>
              </div>

              <button
                @click="spinWheel"
                :disabled="isSpinning || wallet.soXu < 5"
                class="w-full py-3 rounded-2xl font-bold text-xs md:text-sm transition-all flex items-center justify-center gap-2 active:scale-[.99] shadow-md"
                :class="
                  isSpinning || wallet.soXu < 5
                    ? 'bg-stone-100 text-stone-400 cursor-not-allowed shadow-none'
                    : 'bg-gradient-to-r from-amber-500 to-orange-500 hover:from-amber-600 hover:to-orange-600 text-white shadow-amber-500/20'
                "
              >
                <i class="fa-solid fa-rotate" :class="{ 'animate-spin': isSpinning }"></i>
                {{
                  isSpinning
                    ? 'Đang quay thưởng...'
                    : wallet.soXu < 5
                      ? 'Không đủ xu (cần 5 Xu)'
                      : 'Quay ngay (5 Xu)'
                }}
              </button>
            </div>
          </div>

          <!-- CARD GAME (ĐÃ THIẾT KẾ LẠI SINH ĐỘNG VÀ CÓ KHỐI MINH HỌA LƠ LỬNG) -->
          <div
            class="relative overflow-hidden rounded-[22px] bg-white text-stone-900 border border-stone-200/70 shadow-sm hover:shadow-md transition-all flex flex-col justify-between"
          >
            <!-- Top Accent Bar & Background Glow -->
            <div
              class="absolute top-0 left-0 right-0 h-1 bg-gradient-to-r from-amber-400 via-orange-400 to-amber-500"
            ></div>
            <div
              class="absolute -right-16 -bottom-16 w-48 h-48 rounded-full bg-amber-300/15 blur-2xl pointer-events-none"
            ></div>

            <div class="relative z-10 p-5 md:p-6 flex-1 flex flex-col justify-between">
              <div>
                <div class="flex items-start justify-between gap-4">
                  <div>
                    <div
                      class="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-lg bg-amber-50 border border-amber-200/60 text-amber-900 text-[10px] font-bold uppercase tracking-wider"
                    >
                      <i class="fa-solid fa-bolt text-amber-600"></i>
                      Quick Game
                    </div>
                    <h3 class="mt-2 text-lg font-black text-stone-900">Lật thẻ bí mật</h3>
                    <p class="mt-1 text-xs font-medium leading-relaxed text-stone-600">
                      Chọn thẻ bất kỳ để mở quà may mắn, tích lũy phần thưởng mỗi ngày.
                    </p>
                  </div>

                  <div
                    class="text-right shrink-0 bg-amber-50/80 border border-amber-200/60 px-3 py-1.5 rounded-xl"
                  >
                    <div class="text-[9px] uppercase tracking-wider text-amber-800 font-extrabold">
                      Lượt hôm nay
                    </div>
                    <div class="text-sm font-black text-amber-950">
                      {{ soLuotLatTheConLai
                      }}<span class="text-stone-400">/{{ soLuotLatTheToiDa }}</span>
                    </div>
                  </div>
                </div>

                <!-- Khu vực lật thẻ -->
                <div class="my-5">
                  <div class="grid grid-cols-3 gap-3.5 w-full max-w-md mx-auto">
                    <div
                      v-for="i in 3"
                      :key="i"
                      @click="flipCard(i)"
                      class="h-32 rounded-2xl cursor-pointer relative transition-all duration-300 group"
                      :class="flippedCardIndex === i ? 'scale-105' : 'hover:-translate-y-1.5'"
                    >
                      <div
                        class="absolute inset-0 rounded-2xl border transition-all overflow-hidden flex flex-col items-center justify-between p-3.5 shadow-sm"
                        :class="
                          flippedCardIndex === i
                            ? 'bg-gradient-to-br from-amber-500 via-orange-500 to-amber-600 text-white border-amber-300 shadow-lg shadow-amber-500/30'
                            : 'bg-gradient-to-b from-white via-stone-50 to-amber-50/30 border-stone-200/80 group-hover:border-amber-400 group-hover:shadow-md group-hover:bg-amber-50/40 text-stone-700'
                        "
                      >
                        <!-- Top tag on card -->
                        <div class="w-full flex items-center justify-between">
                          <span
                            class="text-[9px] font-extrabold uppercase tracking-widest px-2 py-0.5 rounded-full"
                            :class="
                              flippedCardIndex === i
                                ? 'bg-white/20 text-white'
                                : 'bg-amber-100/80 text-amber-900'
                            "
                          >
                            VIP #{{ i }}
                          </span>
                          <i
                            class="fa-solid fa-sparkles text-[10px]"
                            :class="
                              flippedCardIndex === i
                                ? 'text-white animate-spin'
                                : 'text-amber-500 opacity-70 group-hover:opacity-100'
                            "
                          ></i>
                        </div>

                        <!-- Center icon -->
                        <div
                          class="w-11 h-11 rounded-2xl flex items-center justify-center transition-all duration-300 shadow-xs"
                          :class="
                            flippedCardIndex === i
                              ? 'bg-white text-amber-600 shadow-md scale-110'
                              : 'bg-amber-100/90 text-amber-800 group-hover:scale-110 group-hover:bg-amber-500 group-hover:text-white'
                          "
                        >
                          <i
                            class="fa-solid text-sm"
                            :class="
                              flippedCardIndex === i ? 'fa-gift animate-bounce' : 'fa-question'
                            "
                          ></i>
                        </div>

                        <!-- Bottom label -->
                        <div class="text-center">
                          <span class="text-[10px] font-black uppercase tracking-wider block">
                            {{ flippedCardIndex === i ? 'Đang mở...' : `Thẻ Bí Mật ${i}` }}
                          </span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- Banner phần thưởng & Trạng thái -->
                <div
                  class="flex items-center justify-between px-3.5 py-2 rounded-xl bg-amber-50/60 border border-amber-200/60 text-[11px] font-bold text-amber-900 mb-3"
                >
                  <div class="flex items-center gap-2">
                    <i class="fa-solid fa-wand-magic-sparkles text-amber-600"></i>
                    <span>Phần thưởng: Xu vàng, Voucher giảm giá, Quà đặc biệt</span>
                  </div>
                  <span
                    class="text-[10px] bg-amber-200/60 px-2 py-0.5 rounded-md text-amber-950 font-extrabold"
                    >100% trúng</span
                  >
                </div>

                <!-- ================= KHU VỰC KHỐI MINH HỌA LƠ LỬNG (Ý TƯỞNG 4) ================= -->
                <div
                  class="relative bg-gradient-to-r from-amber-500/10 via-orange-400/10 to-amber-500/10 border border-amber-200/50 rounded-2xl p-4 mb-4 flex items-center justify-between overflow-hidden"
                >
                  <!-- Vầng sáng nền phía sau -->
                  <div
                    class="absolute -left-10 -top-10 w-28 h-28 bg-amber-400/20 rounded-full blur-2xl animate-glow"
                  ></div>

                  <div class="z-10 max-w-[240px]">
                    <h4 class="text-xs font-bold text-amber-900 mb-0.5">Sẵn sàng săn quà lớn?</h4>
                    <p class="text-[11px] text-stone-600 leading-relaxed">
                      Lật thẻ ngay để rinh về những phần quà giá trị mỗi ngày.
                    </p>
                  </div>

                  <!-- Khối minh họa hộp quà lơ lửng -->
                  <div class="relative z-10 flex items-center justify-center pr-2">
                    <div class="animate-float text-3xl select-none filter drop-shadow-md">🎁</div>
                    <!-- Các hạt sáng lấp lánh trang trí -->
                    <span class="absolute -top-2 -right-1 text-[10px] animate-ping">✨</span>
                    <span class="absolute bottom-0 -left-2 text-[9px] animate-pulse">⭐</span>
                  </div>
                </div>
                <!-- ================= KẾT THÚC KHU VỰC KHỐI MINH HỌA ================= -->
              </div>

              <div
                class="rounded-xl bg-stone-50 border border-stone-200/70 px-3.5 py-2.5 text-[11px] font-medium text-stone-600 flex items-center justify-between"
              >
                <div class="flex items-center gap-2">
                  <i class="fa-solid fa-circle-info text-amber-600"></i>
                  <span>Mỗi ngày tối đa {{ soLuotLatTheToiDa }} lượt lật thẻ miễn phí.</span>
                </div>
                <span class="font-bold text-stone-800">Làm mới 00:00</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- REWARD STORE -->
      <section class="rounded-2xl bg-white border border-stone-200/70 shadow-sm overflow-hidden">
        <div class="p-4 border-b border-stone-100 flex items-center justify-between">
          <div class="flex items-center gap-2.5">
            <div
              class="w-8 h-8 rounded-xl bg-amber-100/80 text-amber-900 flex items-center justify-center text-xs font-bold"
            >
              <i class="fa-solid fa-ticket-simple"></i>
            </div>
            <div>
              <h2 class="text-sm font-black text-stone-900">Kho Voucher</h2>
              <p class="text-[10px] font-semibold text-stone-600">Đổi xu lấy mã ưu đãi độc quyền</p>
            </div>
          </div>
        </div>

        <div class="p-3 md:p-4">
          <div
            v-if="khoVouchers.length > 0"
            class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3"
          >
            <div
              v-for="voucher in sortedKhoVouchers"
              :key="voucher.id"
              class="relative group flex items-center justify-between p-3 rounded-xl border border-stone-200/80 hover:border-amber-300 hover:shadow-xs transition-all bg-gradient-to-r from-stone-50/50 to-white overflow-hidden"
            >
              <div
                class="absolute left-0 top-0 bottom-0 w-1 bg-gradient-to-b from-amber-500 to-orange-500"
              ></div>

              <div class="flex items-center gap-3 pl-2 min-w-0 flex-1">
                <div
                  class="w-11 h-11 rounded-xl bg-amber-50 border border-amber-200/80 flex flex-col items-center justify-center shrink-0 text-amber-900 shadow-2xs"
                >
                  <span class="text-[8px] font-bold uppercase tracking-tighter opacity-80"
                    >Giảm</span
                  >
                  <span class="text-[11px] font-black text-rose-600">
                    <template
                      v-if="
                        voucher.loaiGiamGia === 'PERCENT' ||
                        voucher.loaiGiamGia === 'phan_tram' ||
                        voucher.phanTramGiam
                      "
                    >
                      {{ voucher.phanTramGiam || voucher.giaTriGiam }}%
                    </template>
                    <template v-else>
                      {{
                        (voucher.giaTriGiam || 0) >= 1000
                          ? voucher.giaTriGiam / 1000 + 'k'
                          : voucher.giaTriGiam
                      }}
                    </template>
                  </span>
                </div>

                <div class="min-w-0 flex-1 pr-1">
                  <div class="flex items-center gap-1.5 mb-0.5">
                    <span
                      class="px-1.5 py-0.5 rounded bg-stone-100 font-mono font-bold text-[9px] text-amber-900 border border-stone-200/80"
                    >
                      {{ voucher.maCode }}
                    </span>
                  </div>
                  <h3 class="font-bold text-stone-900 text-xs truncate">
                    {{ voucher.tenVoucher }}
                  </h3>
                  <div class="text-[10px] font-medium text-stone-600 mt-0.5 truncate">
                    <span
                      >Đơn tối thiểu:
                      <strong class="text-stone-800"
                        >{{
                          Number(voucher.dieuKienToiThieu || 0).toLocaleString('vi-VN')
                        }}đ</strong
                      ></span
                    >
                  </div>
                  <div class="text-[10px] font-bold text-amber-800 mt-0.5 flex items-center gap-1">
                    <span>
                      <i class="fa-solid fa-coins text-[9px] mr-0.5 text-amber-600"></i
                      >{{ voucher.soXuDoi }} Xu
                    </span>
                  </div>
                </div>
              </div>

              <div class="shrink-0 pl-2 border-l border-stone-100">
                <button
                  @click="handleDoiVoucher(voucher.id)"
                  :disabled="wallet.soXu < voucher.soXuDoi || voucher.soLuongConLai <= 0"
                  class="px-3 py-1.5 rounded-xl text-[11px] font-bold transition-all shadow-2xs flex items-center gap-1 bg-amber-500 hover:bg-amber-600 text-white disabled:bg-stone-200 disabled:text-stone-400 disabled:shadow-none disabled:cursor-not-allowed"
                >
                  <i class="fa-solid fa-gift text-[9px]"></i>
                  {{ voucher.soLuongConLai <= 0 ? 'Hết' : 'Đổi' }}
                </button>
              </div>
            </div>
          </div>

          <div v-else class="py-10 text-center text-xs text-stone-500 font-medium">
            <i class="fa-solid fa-ticket-simple text-2xl mb-2 opacity-50"></i>
            <p>Chưa có voucher nào trong kho</p>
          </div>
        </div>
      </section>
    </main>

    <!-- HISTORY MODAL -->
    <transition name="modal">
      <div
        v-if="showHistoryModal"
        class="fixed inset-0 z-[70] flex items-center justify-center bg-stone-900/30 backdrop-blur-xs p-4"
        @click.self="showHistoryModal = false"
      >
        <div
          class="bg-white rounded-2xl max-w-2xl w-full shadow-xl overflow-hidden border border-stone-200/60"
        >
          <div class="px-5 py-4 border-b border-stone-100 flex items-center justify-between">
            <div class="flex items-center gap-3">
              <div
                class="w-9 h-9 rounded-xl bg-amber-50 text-amber-900 flex items-center justify-center border border-amber-200/60"
              >
                <i class="fa-solid fa-clock-rotate-left text-sm"></i>
              </div>
              <div>
                <h3 class="font-bold text-sm text-stone-900">Lịch sử Xu</h3>
                <p class="text-[11px] font-medium text-stone-600">Biến động cộng/trừ xu của bạn</p>
              </div>
            </div>
            <button
              @click="showHistoryModal = false"
              class="w-7 h-7 rounded-lg bg-stone-100 hover:bg-stone-200 text-stone-600 flex items-center justify-center transition"
            >
              <i class="fa-solid fa-xmark text-xs"></i>
            </button>
          </div>

          <div class="overflow-auto px-5 py-3 max-h-[60vh]">
            <table class="w-full text-left text-xs">
              <thead>
                <tr
                  class="text-[10px] uppercase tracking-wider font-bold text-stone-500 border-b border-stone-100"
                >
                  <th class="py-2.5 px-3">Thời gian</th>
                  <th class="py-2.5 px-3">Nội dung</th>
                  <th class="py-2.5 px-3 text-right">Biến động</th>
                </tr>
              </thead>
              <tbody class="divide-y divide-stone-100">
                <tr
                  v-for="history in paginatedHistoryList"
                  :key="history.id"
                  class="hover:bg-stone-50/80 transition"
                >
                  <td class="py-2.5 px-3 font-medium text-stone-600 whitespace-nowrap">
                    {{ new Date(history.ngayTao).toLocaleString('vi-VN') }}
                  </td>
                  <td class="py-2.5 px-3 font-bold text-stone-800">{{ history.moTa }}</td>
                  <td
                    class="py-2.5 px-3 text-right font-black"
                    :class="history.soXuThayDoi > 0 ? 'text-emerald-600' : 'text-rose-600'"
                  >
                    {{ history.soXuThayDoi > 0 ? `+${history.soXuThayDoi}` : history.soXuThayDoi }}
                    Xu
                  </td>
                </tr>
                <tr v-if="historyList.length === 0">
                  <td colspan="3" class="py-8 text-center text-xs font-medium text-stone-500">
                    Chưa có lịch sử giao dịch Xu.
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <div
            v-if="historyTotalPages > 1"
            class="px-5 py-3 border-t border-stone-100 flex items-center justify-between bg-stone-50/50"
          >
            <span class="text-[11px] font-medium text-stone-600">
              {{ paginatedHistoryList.length }} / {{ historyList.length }} giao dịch
            </span>
            <div class="flex items-center gap-2">
              <button
                @click="historyCurrentPage > 1 && historyCurrentPage--"
                :disabled="historyCurrentPage === 1"
                class="px-2.5 py-1.5 rounded-lg border border-stone-200 text-xs font-bold text-stone-700 disabled:opacity-40 hover:bg-white"
              >
                <i class="fa-solid fa-chevron-left text-[10px]"></i>
              </button>
              <span class="text-xs font-bold text-stone-800">
                {{ historyCurrentPage }} / {{ historyTotalPages }}
              </span>
              <button
                @click="historyCurrentPage < historyTotalPages && historyCurrentPage++"
                :disabled="historyCurrentPage === historyTotalPages"
                class="px-2.5 py-1.5 rounded-lg border border-stone-200 text-xs font-bold text-stone-700 disabled:opacity-40 hover:bg-white"
              >
                <i class="fa-solid fa-chevron-right text-[10px]"></i>
              </button>
            </div>
          </div>
        </div>
      </div>
    </transition>

    <!-- REWARD MODAL -->
    <transition name="modal">
      <div
        v-if="showRewardModal"
        class="fixed inset-0 z-[70] flex items-center justify-center bg-stone-900/30 backdrop-blur-xs p-4"
        @click.self="showRewardModal = false"
      >
        <div
          class="relative overflow-hidden bg-white border border-stone-200/70 rounded-2xl max-w-md w-full max-h-[88vh] overflow-y-auto shadow-xl"
        >
          <div class="h-1.5 bg-gradient-to-r from-amber-500 to-orange-500"></div>
          <div class="p-5 md:p-6">
            <div class="flex items-start justify-between gap-4">
              <div class="flex items-center gap-3">
                <div
                  class="w-12 h-12 rounded-xl bg-amber-50 border border-amber-200 text-amber-900 flex items-center justify-center text-xl shadow-2xs"
                >
                  <i class="fa-solid fa-gift"></i>
                </div>
                <div>
                  <div class="text-[10px] uppercase tracking-wider font-bold text-amber-800">
                    Phần thưởng
                  </div>
                  <div class="text-xs font-medium text-stone-600">Chúc mừng bạn đã nhận quà!</div>
                </div>
              </div>
              <button
                @click="showRewardModal = false"
                class="w-7 h-7 rounded-lg bg-stone-100 hover:bg-stone-200 text-stone-600 flex items-center justify-center transition"
              >
                <i class="fa-solid fa-xmark text-xs"></i>
              </button>
            </div>
            <div
              class="mt-5 rounded-xl bg-gradient-to-br from-amber-50/50 to-orange-50/50 border border-amber-200/60 p-4 text-center"
            >
              <h3 class="text-lg font-black text-stone-900 break-words">
                {{ rewardData.tenPhanThuong || 'Phần quà đặc biệt' }}
              </h3>
              <p
                v-if="rewardData.moTa"
                class="mt-2 text-xs font-medium leading-relaxed text-stone-700"
              >
                {{ rewardData.moTa }}
              </p>
            </div>

            <div class="mt-4 grid grid-cols-2 gap-2">
              <div class="rounded-xl bg-stone-50 border border-stone-200 p-3 text-center">
                <div class="text-[9px] uppercase tracking-wider font-bold text-stone-500">
                  Loại phần thưởng
                </div>
                <div class="mt-0.5 text-xs font-bold text-stone-800">
                  {{
                    rewardData.loaiPhanThuong === 'xu'
                      ? 'Xu'
                      : rewardData.loaiPhanThuong === 'voucher'
                        ? 'Voucher'
                        : rewardData.loaiPhanThuong || 'Quà tặng'
                  }}
                </div>
              </div>
              <div
                v-if="rewardData.giaTriXu !== undefined && rewardData.giaTriXu !== null"
                class="rounded-xl bg-amber-50 border border-amber-200/70 p-3 text-center"
              >
                <div class="text-[9px] uppercase tracking-wider font-bold text-amber-900">
                  Giá trị
                </div>
                <div class="mt-0.5 text-xs font-black text-amber-900">
                  {{ Number(rewardData.giaTriXu).toLocaleString('vi-VN') }} Xu
                </div>
              </div>
            </div>

            <button
              @click="showRewardModal = false"
              class="mt-5 w-full py-3 rounded-xl bg-gradient-to-r from-amber-500 to-orange-500 hover:from-amber-600 hover:to-orange-600 text-white font-bold text-xs transition shadow-sm"
            >
              Xác nhận
            </button>
          </div>
        </div>
      </div>
    </transition>

    <!-- EXCHANGE CONFIRM MODAL -->
    <transition name="modal">
      <div
        v-if="showExchangeModal && pendingVoucher"
        class="fixed inset-0 z-[75] flex items-center justify-center bg-stone-900/30 backdrop-blur-xs p-4"
        @click.self="showExchangeModal = false"
      >
        <div class="bg-white rounded-2xl max-w-sm w-full p-5 shadow-xl border border-stone-200/70">
          <div class="flex items-center justify-between pb-3 border-b border-stone-100">
            <div class="flex items-center gap-2.5">
              <div
                class="w-8 h-8 rounded-lg bg-amber-50 text-amber-900 flex items-center justify-center text-xs font-bold border border-amber-200/60"
              >
                <i class="fa-solid fa-ticket"></i>
              </div>
              <div>
                <h3 class="font-bold text-xs text-stone-900">Xác nhận đổi Voucher</h3>
                <p class="text-[10px] font-medium text-stone-600">
                  Mã: {{ pendingVoucher.maCode }}
                </p>
              </div>
            </div>
            <button
              @click="showExchangeModal = false"
              class="w-7 h-7 rounded-lg bg-stone-100 hover:bg-stone-200 text-stone-600 flex items-center justify-center transition"
            >
              <i class="fa-solid fa-xmark text-xs"></i>
            </button>
          </div>

          <div class="mt-4 rounded-xl bg-stone-50 border border-stone-200/80 p-3.5 text-stone-900">
            <h4 class="font-bold text-stone-900 text-xs line-clamp-2">
              {{ pendingVoucher.tenVoucher }}
            </h4>
            <div class="mt-2 flex items-center justify-between text-xs">
              <span class="text-stone-600 font-medium">Giá trị giảm:</span>
              <span class="font-bold text-rose-600">
                <template
                  v-if="
                    pendingVoucher.loaiGiamGia === 'PERCENT' ||
                    pendingVoucher.loaiGiamGia === 'phan_tram' ||
                    pendingVoucher.phanTramGiam
                  "
                >
                  -{{ pendingVoucher.phanTramGiam || pendingVoucher.giaTriGiam }}%
                </template>
                <template v-else>
                  -{{ Number(pendingVoucher.giaTriGiam || 0).toLocaleString('vi-VN') }}đ
                </template>
              </span>
            </div>
            <div class="mt-1 flex items-center justify-between text-xs">
              <span class="text-stone-600 font-medium">Đơn tối thiểu:</span>
              <span class="font-bold text-stone-800"
                >{{ Number(pendingVoucher.dieuKienToiThieu || 0).toLocaleString('vi-VN') }}đ</span
              >
            </div>
          </div>

          <div class="mt-3 grid grid-cols-2 gap-2 text-center">
            <div class="rounded-xl bg-amber-50 border border-amber-200/60 p-2.5">
              <div class="text-[9px] uppercase font-bold text-amber-900">Chi phí</div>
              <div class="mt-0.5 text-xs font-black text-amber-950">
                {{ pendingVoucher.soXuDoi }} Xu
              </div>
            </div>
            <div class="rounded-xl bg-stone-50 border border-stone-200/60 p-2.5">
              <div class="text-[9px] uppercase font-bold text-stone-600">Còn lại</div>
              <div class="mt-0.5 text-xs font-black text-stone-800">
                {{ Math.max(0, (wallet.soXu || 0) - (pendingVoucher.soXuDoi || 0)) }} Xu
              </div>
            </div>
          </div>

          <div class="mt-4 flex gap-2">
            <button
              @click="showExchangeModal = false"
              class="flex-1 py-2.5 rounded-xl bg-stone-100 hover:bg-stone-200 text-stone-700 font-bold text-xs transition"
            >
              Hủy
            </button>
            <button
              @click="confirmDoiVoucher"
              class="flex-1 py-2.5 rounded-xl bg-amber-500 hover:bg-amber-600 text-white font-bold text-xs transition shadow-sm flex items-center justify-center gap-1.5"
            >
              <i class="fa-solid fa-gift"></i> Đổi ngay
            </button>
          </div>
        </div>
      </div>
    </transition>

    <!-- MY VOUCHERS MODAL -->
    <transition name="modal">
      <div
        v-if="showMyVouchersModal"
        class="fixed inset-0 z-[70] flex items-center justify-center bg-stone-900/30 backdrop-blur-xs p-4"
        @click.self="showMyVouchersModal = false"
      >
        <div
          class="bg-white rounded-2xl max-w-2xl w-full max-h-[88vh] flex flex-col shadow-xl overflow-hidden border border-stone-200/70"
        >
          <div class="px-5 py-4 border-b border-stone-100">
            <div class="flex items-start justify-between gap-4">
              <div class="flex items-center gap-3">
                <div
                  class="w-10 h-10 rounded-xl bg-amber-50 text-amber-900 flex items-center justify-center border border-amber-200/60"
                >
                  <i class="fa-solid fa-wallet text-sm"></i>
                </div>
                <div>
                  <h3 class="font-bold text-base text-stone-900">Voucher của tôi</h3>
                  <p class="text-[11px] font-medium text-stone-600">Mã ưu đãi bạn đã đổi</p>
                </div>
              </div>
              <button
                @click="showMyVouchersModal = false"
                class="w-7 h-7 rounded-lg bg-stone-100 hover:bg-stone-200 text-stone-600 flex items-center justify-center transition"
              >
                <i class="fa-solid fa-xmark text-xs"></i>
              </button>
            </div>

            <div class="mt-4 flex gap-2 overflow-x-auto pb-1">
              <button
                @click="myVoucherFilter = 'ALL'"
                :class="
                  myVoucherFilter === 'ALL'
                    ? 'bg-amber-500 text-white shadow-sm'
                    : 'bg-stone-100 text-stone-700 hover:bg-stone-200'
                "
                class="px-3 py-1.5 rounded-lg text-xs font-bold whitespace-nowrap transition"
              >
                Tất cả ({{ myVouchers.length }})
              </button>
              <button
                @click="myVoucherFilter = 'CHUA_DUNG'"
                :class="
                  myVoucherFilter === 'CHUA_DUNG'
                    ? 'bg-amber-500 text-white shadow-sm'
                    : 'bg-stone-100 text-stone-700 hover:bg-stone-200'
                "
                class="px-3 py-1.5 rounded-lg text-xs font-bold whitespace-nowrap transition"
              >
                Chưa dùng ({{ unusedVouchersCount }})
              </button>
              <button
                @click="myVoucherFilter = 'DA_DUNG'"
                :class="
                  myVoucherFilter === 'DA_DUNG'
                    ? 'bg-amber-500 text-white shadow-sm'
                    : 'bg-stone-100 text-stone-700 hover:bg-stone-200'
                "
                class="px-3 py-1.5 rounded-lg text-xs font-bold whitespace-nowrap transition"
              >
                Đã dùng
              </button>
            </div>
          </div>

          <div class="overflow-y-auto p-4 space-y-2.5">
            <div
              v-for="item in filteredMyVouchers"
              :key="item.idVoucherKhachHang"
              class="relative group flex items-center justify-between p-3 rounded-xl border border-stone-200/80 hover:border-amber-300 transition-all bg-gradient-to-r from-stone-50/50 to-white overflow-hidden shadow-2xs"
            >
              <div
                class="absolute left-0 top-0 bottom-0 w-1 bg-gradient-to-b from-amber-500 to-orange-500"
              ></div>

              <div class="flex items-center gap-3 pl-2 min-w-0 flex-1">
                <div
                  class="w-11 h-11 rounded-xl bg-amber-50 border border-amber-200/80 flex flex-col items-center justify-center shrink-0 text-amber-900 shadow-2xs"
                >
                  <span class="text-[8px] font-bold uppercase tracking-tighter opacity-80"
                    >Ưu đãi</span
                  >
                  <span class="text-[11px] font-black text-rose-600 truncate max-w-[40px]">
                    {{ item.hienThiGiaTriGiam || 'KM' }}
                  </span>
                </div>

                <div class="min-w-0 flex-1 pr-1">
                  <div class="flex items-center gap-2">
                    <span
                      class="px-1.5 py-0.5 rounded text-[9px] font-bold uppercase"
                      :class="
                        item.trangThai === 'CHUA_DUNG'
                          ? 'bg-emerald-50 text-emerald-700 border border-emerald-200/80'
                          : 'bg-stone-100 text-stone-600 border border-stone-200'
                      "
                    >
                      {{ item.trangThai === 'CHUA_DUNG' ? 'Chưa dùng' : item.trangThai }}
                    </span>
                    <span
                      v-if="item.sapHetHan"
                      class="px-1.5 py-0.5 rounded bg-rose-50 text-rose-700 text-[9px] font-bold border border-rose-200/80"
                    >
                      Sắp hết hạn
                    </span>
                  </div>
                  <h4 class="font-bold text-stone-900 text-xs truncate mt-0.5">
                    {{ item.tenVoucher }}
                  </h4>
                  <div
                    class="text-[10px] font-medium text-stone-600 mt-0.5 flex items-center gap-2"
                  >
                    <span
                      >Đơn tối thiểu:
                      <strong class="text-stone-800"
                        >{{ Number(item.dieuKienToiThieu || 0).toLocaleString('vi-VN') }}đ</strong
                      ></span
                    >
                    <span class="text-stone-300">|</span>
                    <span>Hạn: {{ new Date(item.ngayHetHan).toLocaleDateString('vi-VN') }}</span>
                  </div>
                </div>
              </div>

              <div class="shrink-0 pl-2 border-l border-stone-100 flex items-center gap-2">
                <div
                  class="px-2.5 py-1 rounded-lg bg-stone-50 border border-dashed border-stone-300 font-mono font-bold text-amber-900 text-xs"
                >
                  {{ item.maCode }}
                </div>
                <button
                  @click="copyCode(item.maCode)"
                  class="w-7 h-7 rounded-lg bg-stone-100 hover:bg-amber-100 text-stone-700 flex items-center justify-center transition"
                  title="Sao chép mã"
                >
                  <i class="fa-regular fa-copy text-xs"></i>
                </button>
              </div>
            </div>

            <div
              v-if="filteredMyVouchers.length === 0"
              class="py-12 text-center rounded-2xl border border-dashed border-stone-200 bg-stone-50/50"
            >
              <div
                class="w-12 h-12 rounded-xl bg-white border border-stone-200 mx-auto flex items-center justify-center text-lg text-stone-400 shadow-2xs"
              >
                <i class="fa-solid fa-ticket"></i>
              </div>
              <div class="mt-3 text-xs font-bold text-stone-800">Chưa có voucher trong mục này</div>
              <div class="mt-1 text-[11px] font-medium text-stone-500">
                Hãy đổi voucher từ kho xu để sử dụng.
              </div>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import clientService from '@/service/clientGamificationService.js'

const customerId = ref(null)

const getLoggedInCustomerId = () => {
  try {
    const userStr = sessionStorage.getItem('user')
    if (userStr) {
      const user = JSON.parse(userStr)
      customerId.value = user.id || user.idKhachHang || user.userId || 1
    } else {
      customerId.value = 1
    }
  } catch (e) {
    console.error('Lỗi đọc thông tin user:', e)
    customerId.value = 1
  }
}

const wallet = ref({
  soXu: 0,
  daDiemDanhHomNay: false,
  chuoiDiemDanh: 0,
})

const historyList = ref([])
const isCheckingIn = ref(false)
const flippedCardIndex = ref(null)
const soLuotLatTheConLai = ref(3)
const soLuotLatTheToiDa = ref(3)

const khoVouchers = ref([])
const myVouchers = ref([])
const showMyVouchersModal = ref(false)
const showHistoryModal = ref(false)
const showExchangeModal = ref(false)
const pendingVoucher = ref(null)
const checkinConfigs = ref([])

const sortBy = ref('asc')
const myVoucherFilter = ref('CHUA_DUNG')
const unusedVouchersCount = computed(() => {
  return myVouchers.value.filter((item) => item.trangThai === 'CHUA_DUNG').length
})

const historyCurrentPage = ref(1)
const historyItemsPerPage = ref(5)

const toast = ref({
  show: false,
  message: '',
  type: 'success',
})

const showNotification = (message, type = 'success') => {
  toast.value = { show: true, message, type }
  setTimeout(() => {
    toast.value.show = false
  }, 3500)
}

const wheelSlices = ref([
  { tenPhanThuong: '10 Xu' },
  { tenPhanThuong: '50 Xu' },
  { tenPhanThuong: 'Voucher' },
  { tenPhanThuong: '20 Xu' },
  { tenPhanThuong: '100 Xu' },
  { tenPhanThuong: 'Chúc May Mắn' },
])

const wheelAngle = ref(0)
const isSpinning = ref(false)

const showRewardModal = ref(false)
const rewardData = ref({
  tenPhanThuong: '',
  moTa: '',
  loaiPhanThuong: '',
})

const sliceColors = [
  '#fef3c7',
  '#fde68a',
  '#d9f99d',
  '#fed7aa',
  '#fbcfe8',
  '#e2e8f0',
  '#ccfbf1',
  '#fae8ff',
]

const getSlicePath = (index, total) => {
  const angle = 360 / total
  const startAngle = index * angle - 90
  const endAngle = (index + 1) * angle - 90

  const rad = 140
  const x1 = 150 + rad * Math.cos((startAngle * Math.PI) / 180)
  const y1 = 150 + rad * Math.sin((startAngle * Math.PI) / 180)
  const x2 = 150 + rad * Math.cos((endAngle * Math.PI) / 180)
  const y2 = 150 + rad * Math.sin((endAngle * Math.PI) / 180)

  const largeArcFlag = angle > 180 ? 1 : 0
  return `M 150 150 L ${x1} ${y1} A ${rad} ${rad} 0 ${largeArcFlag} 1 ${x2} ${y2} Z`
}

const fetchCheckinConfigs = async () => {
  try {
    const res = clientService.getCauHinhDiemDanh ? await clientService.getCauHinhDiemDanh() : null
    if (res) {
      checkinConfigs.value = Array.isArray(res) ? res : res?.data || []
    }
  } catch (e) {
    console.log('Sử dụng mốc điểm danh mặc định', e)
  }
}

const nextCheckinCoins = computed(() => {
  const nextDay = (wallet.value.chuoiDiemDanh || 0) + 1
  if (checkinConfigs.value.length > 0) {
    const found = checkinConfigs.value.find((c) => c.ngayThu === nextDay)
    if (found) return found.soXuThuong || found.soXu || 10
    const last = checkinConfigs.value[checkinConfigs.value.length - 1]
    return last ? last.soXuThuong || last.soXu || 10 : 10
  }
  return 10 + (nextDay - 1) * 5
})

const previewMilestones = computed(() => {
  if (checkinConfigs.value.length > 0) {
    return checkinConfigs.value.slice(0, 7)
  }
  return Array.from({ length: 7 }, (_, i) => ({
    ngayThu: i + 1,
    soXuThuong: 10 + i * 5,
  }))
})

const sortedKhoVouchers = computed(() => {
  let list = khoVouchers.value.filter((voucher) => voucher.trangThai === true)

  list.sort((a, b) => {
    if (sortBy.value === 'asc') {
      return (a.soXuDoi || 0) - (b.soXuDoi || 0)
    } else {
      return (b.soXuDoi || 0) - (a.soXuDoi || 0)
    }
  })
  return list
})

const historyTotalPages = computed(() => {
  return Math.ceil(historyList.value.length / historyItemsPerPage.value) || 1
})

const paginatedHistoryList = computed(() => {
  const start = (historyCurrentPage.value - 1) * historyItemsPerPage.value
  const end = start + historyItemsPerPage.value
  return historyList.value.slice(start, end)
})

const filteredMyVouchers = computed(() => {
  if (myVoucherFilter.value === 'ALL') return myVouchers.value
  return myVouchers.value.filter((item) => item.trangThai === myVoucherFilter.value)
})

const copyCode = (code) => {
  navigator.clipboard.writeText(code)
  showNotification(`Đã sao chép mã: ${code}`, 'success')
}

const fetchClientData = async () => {
  try {
    const res = await clientService.getTrangThai(customerId.value)
    if (res && res.data) {
      wallet.value = {
        soXu: res.data.soDuXu || 0,
        daDiemDanhHomNay: res.data.daDiemDanhHomNay,
        chuoiDiemDanh: res.data.chuoiDiemDanh || 0,
        ngayDiemDanhGanNhat: res.data.ngayDiemDanhGanNhat,
      }

      if (res.data.soLuotLatTheConLai !== undefined && res.data.soLuotLatTheConLai !== null) {
        soLuotLatTheConLai.value = res.data.soLuotLatTheConLai
      }
      if (res.data.soLuotLatTheToiDa !== undefined && res.data.soLuotLatTheToiDa !== null) {
        soLuotLatTheToiDa.value = res.data.soLuotLatTheToiDa
      }
    }

    const historyRes = await clientService.getLichSuXu(customerId.value)
    historyList.value = Array.isArray(historyRes) ? historyRes : historyRes?.data || []

    try {
      const prizesRes = await clientService.getDanhSachPhanThuong()
      const prizesData = prizesRes?.data || prizesRes

      if (Array.isArray(prizesData) && prizesData.length > 0) {
        const vongQuayItems = prizesData.filter(
          (item) =>
            (item.loaiGame === 'vong_quay' || item.loai_game === 'vong_quay') &&
            item.trangThai !== false,
        )

        if (vongQuayItems.length > 0) {
          wheelSlices.value = vongQuayItems
        }
      }
    } catch (e) {
      console.log('Lỗi tải phần thưởng vòng quay:', e)
    }
  } catch (error) {
    console.error('Lỗi tải dữ liệu khách hàng:', error)
  }
}

const fetchKhoVouchers = async () => {
  try {
    const res = await clientService.getKhoVoucher()
    khoVouchers.value = Array.isArray(res) ? res : res?.data || []
  } catch (error) {
    console.error('Lỗi tải kho voucher:', error)
  }
}

const fetchMyVouchers = async () => {
  try {
    const res = await clientService.getVoucherCuaToi(customerId.value)
    myVouchers.value = Array.isArray(res) ? res : res?.data || []
  } catch (error) {
    console.error('Lỗi tải voucher cá nhân:', error)
  }
}

const getWheelLabelLines = (value) => {
  const text = String(value || 'Phần thưởng').trim()
  const maxChars = 8
  const words = text.split(/\s+/)
  const lines = []
  let current = ''

  for (const word of words) {
    if (word.length > maxChars) {
      if (current) {
        lines.push(current)
        current = ''
      }

      for (let i = 0; i < word.length; i += maxChars) {
        lines.push(word.slice(i, i + maxChars))
      }
      continue
    }

    const next = (current + ' ' + word).trim()

    if (next.length <= maxChars) {
      current = next
    } else {
      if (current) lines.push(current)
      current = word
    }
  }

  if (current) lines.push(current)

  return lines.slice(0, 3)
}

const handleDoiVoucher = (idKhoVoucher) => {
  const voucher = khoVouchers.value.find((item) => item.id === idKhoVoucher)
  if (!voucher) return

  if (wallet.value.soXu < (voucher.soXuDoi || 0)) {
    showNotification('Bạn không đủ xu để đổi voucher này!', 'error')
    return
  }

  if ((voucher.soLuongConLai || 0) <= 0) {
    showNotification('Voucher này đã hết lượt đổi!', 'error')
    return
  }

  pendingVoucher.value = voucher
  showExchangeModal.value = true
}

const confirmDoiVoucher = async () => {
  if (!pendingVoucher.value) return

  const idKhoVoucher = pendingVoucher.value.id
  showExchangeModal.value = false

  try {
    await clientService.doiVoucher(customerId.value, idKhoVoucher)
    showNotification('Đổi voucher thành công! Kiểm tra trong "Voucher Của Tôi".', 'success')
    fetchClientData()
    fetchKhoVouchers()
    fetchMyVouchers()
  } catch (error) {
    const errMessage =
      error.response?.data?.message ||
      error.response?.data ||
      'Không đủ xu hoặc hết lượt đổi voucher!'
    showNotification(errMessage, 'error')
    fetchClientData()
    fetchKhoVouchers()
    fetchMyVouchers()
  }
}

const openMyVouchersModal = () => {
  fetchMyVouchers()
  showMyVouchersModal.value = true
}

const openHistoryModal = () => {
  showHistoryModal.value = true
}

const performCheckIn = async () => {
  if (isCheckingIn.value || wallet.value.daDiemDanhHomNay) return
  isCheckingIn.value = true
  try {
    const res = await clientService.diemDanh(customerId.value)
    const addedCoins = res?.data?.soXuNhan || res?.soXuNhan || nextCheckinCoins.value

    showNotification(`Điểm danh thành công +${addedCoins} Xu!`, 'success')
    wallet.value.daDiemDanhHomNay = true
    wallet.value.soXu += addedCoins
    fetchClientData()
  } catch (error) {
    const errMessage =
      error.response?.data?.message ||
      error.response?.data ||
      'Điểm danh thất bại hoặc bạn đã điểm danh rồi!'
    showNotification(errMessage, 'error')
  } finally {
    isCheckingIn.value = false
  }
}

const spinWheel = async () => {
  if (isSpinning.value || wallet.value.soXu < 5) return
  isSpinning.value = true

  try {
    const res = await clientService.quayThuong(customerId.value, 'vong_quay')
    const reward = res?.data || res

    let prizeIndex = wheelSlices.value.findIndex(
      (s) => s.id === reward.id || s.tenPhanThuong === reward.tenPhanThuong,
    )
    if (prizeIndex === -1) prizeIndex = 0

    const totalSlices = wheelSlices.value.length
    const degreesPerSlice = 360 / totalSlices

    const targetAngle = 360 - (prizeIndex * degreesPerSlice + degreesPerSlice / 2)
    const extraSpins = 360 * 5
    wheelAngle.value = wheelAngle.value - (wheelAngle.value % 360) + extraSpins + targetAngle

    setTimeout(() => {
      isSpinning.value = false
      rewardData.value = reward
      showRewardModal.value = true
      fetchClientData()
      fetchMyVouchers()
    }, 4000)
  } catch (error) {
    isSpinning.value = false
    const errMessage =
      error.response?.data?.message ||
      error.response?.data ||
      'Không đủ xu hoặc lỗi hệ thống vòng quay!'
    showNotification(errMessage, 'error')
    fetchClientData()
  }
}

const flipCard = async (cardIndex) => {
  if (flippedCardIndex.value !== null) return

  if (soLuotLatTheConLai.value <= 0) {
    showNotification(
      `Bạn đã hết lượt lật thẻ hôm nay (tối đa ${soLuotLatTheToiDa.value} lần)!`,
      'error',
    )
    return
  }

  flippedCardIndex.value = cardIndex

  try {
    const res = await clientService.quayThuong(customerId.value, 'lat_the')
    setTimeout(() => {
      const rewardPayload = res?.data || res
      if (rewardPayload) {
        rewardData.value = {
          ...rewardPayload,
          soThe: cardIndex,
        }
        showRewardModal.value = true
      }

      flippedCardIndex.value = null
      fetchClientData()
      fetchMyVouchers()
    }, 600)
  } catch (error) {
    flippedCardIndex.value = null
    const errMessage =
      error.response?.data?.message ||
      error.response?.data ||
      'Bạn đã hết lượt lật thẻ hôm nay hoặc không đủ xu!'
    showNotification(errMessage, 'error')
    fetchClientData()
  }
}

onMounted(async () => {
  getLoggedInCustomerId()
  if (customerId.value) {
    await fetchClientData()
    fetchKhoVouchers()
    fetchMyVouchers()
    fetchCheckinConfigs()
  }
})
</script>

<style scoped>
/* Hiệu ứng bồng bềnh cho hộp quà */
@keyframes float {
  0%,
  100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-6px);
  }
}
.animate-float {
  animation: float 3s ease-in-out infinite;
}

/* Hiệu ứng tỏa sáng nền phía sau */
@keyframes pulse-glow {
  0%,
  100% {
    opacity: 0.4;
    transform: scale(1);
  }
  50% {
    opacity: 0.8;
    transform: scale(1.08);
  }
}
.animate-glow {
  animation: pulse-glow 3s ease-in-out infinite;
}

.toast-enter-active,
.toast-leave-active {
  transition: all 0.3s ease;
}

.toast-enter-from,
.toast-leave-to {
  opacity: 0;
  transform: translateY(-16px) scale(0.98);
}

.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.2s ease;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}

.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
