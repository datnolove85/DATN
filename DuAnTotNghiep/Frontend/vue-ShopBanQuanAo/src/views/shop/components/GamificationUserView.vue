<template>
  <div
    class="min-h-screen bg-slate-50 text-slate-800 font-sans pb-12 selection:bg-indigo-500 selection:text-slate-800"
  >
    <!-- TOAST -->
    <transition name="toast">
      <div
        v-if="toast.show"
        class="fixed top-5 right-5 z-[80] flex items-center gap-3 px-5 py-3.5 rounded-2xl shadow-2xl border backdrop-blur-xl text-sm font-bold"
        :class="
          toast.type === 'success'
            ? 'bg-emerald-50/95 border-emerald-200 text-emerald-800'
            : 'bg-rose-50/95 border-rose-200 text-rose-800'
        "
      >
        <i
          :class="
            toast.type === 'success'
              ? 'fa-solid fa-circle-check text-emerald-500 text-lg'
              : 'fa-solid fa-circle-exclamation text-rose-500 text-lg'
          "
        ></i>
        <span>{{ toast.message }}</span>
      </div>
    </transition>

    <!-- NAVBAR -->
    <header class="sticky top-0 z-40 border-b border-slate-200/80 bg-white/90 backdrop-blur-xl">
      <div class="max-w-7xl mx-auto px-4 md:px-6 h-16 flex items-center justify-between">
        <div class="flex items-center gap-3">
          <!-- <div
            class="w-10 h-10 rounded-2xl bg-gradient-to-br from-indigo-600 to-violet-600 text-white flex items-center justify-center shadow-lg shadow-indigo-200"
          >
            <i class="fa-solid fa-gamepad"></i>
          </div> -->
          <!-- <div>
            <div class="text-lg font-black tracking-tight text-slate-900">
              K-<span class="text-indigo-600">ZONE</span>
            </div>
            <div class="text-[10px] font-bold uppercase tracking-[.22em] text-slate-600">
              Reward Center
            </div>
          </div> -->
        </div>

        <div class="flex items-center gap-2.5">
          <button
            @click="openHistoryModal"
            class="hidden sm:flex items-center gap-2 px-3.5 py-2.5 rounded-xl bg-slate-50 border border-slate-200 text-xs font-bold text-slate-600 hover:bg-indigo-50 hover:border-indigo-200 hover:text-indigo-600 transition"
          >
            <i class="fa-solid fa-clock-rotate-left"></i>
            Lịch sử xu
          </button>

          <div
            class="flex items-center gap-2 bg-amber-50 border border-amber-200 px-3.5 py-2.5 rounded-xl shadow-sm"
          >
            <div
              class="w-7 h-7 rounded-lg bg-amber-400 text-slate-800 flex items-center justify-center"
            >
              <i class="fa-solid fa-coins text-xs"></i>
            </div>
            <div class="leading-tight">
              <div class="text-[9px] uppercase tracking-wider font-bold text-amber-700">Số dư</div>
              <div class="text-sm font-black text-amber-600">{{ wallet.soXu || 0 }} Xu</div>
            </div>
          </div>

          <div
            class="w-10 h-10 rounded-xl bg-indigo-100 border border-indigo-200 flex items-center justify-center font-black text-sm text-indigo-700"
          >
            KH
          </div>
        </div>
      </div>
    </header>

    <main class="max-w-7xl mx-auto px-4 md:px-6 pt-5 space-y-5">
      <!-- HERO / CHECK-IN -->
      <section
        class="relative overflow-hidden rounded-[24px] bg-gradient-to-br from-white via-indigo-50 to-violet-50 text-slate-800 border border-indigo-100 shadow-[0_20px_60px_rgba(79,70,229,.10)]"
      >
        <div
          class="absolute -right-20 -top-24 w-80 h-80 rounded-full bg-indigo-400/20 blur-3xl"
        ></div>
        <div
          class="absolute -left-20 -bottom-28 w-80 h-80 rounded-full bg-violet-400/15 blur-3xl"
        ></div>
        <div class="absolute right-20 bottom-0 text-[150px] leading-none opacity-[.035] rotate-12">
          <i class="fa-solid fa-gift"></i>
        </div>

        <div class="relative z-10 p-5 md:p-6">
          <div class="flex flex-col xl:flex-row xl:items-center xl:justify-between gap-5">
            <div class="max-w-xl">
              <div
                class="inline-flex items-center gap-2 px-2.5 py-1 rounded-full bg-indigo-50 border border-indigo-100 text-[10px] font-bold uppercase tracking-wider text-indigo-700"
              >
                <span class="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse"></span>
                Điểm danh hằng ngày
              </div>

              <h1 class="mt-3 text-2xl md:text-3xl font-black tracking-tight text-slate-900">
                Điểm danh mỗi ngày,
                <span
                  class="text-transparent bg-clip-text bg-gradient-to-r from-amber-600 to-orange-500"
                  >nhận xu cực đã</span
                >
              </h1>

              <p class="mt-2 text-xs md:text-sm leading-relaxed text-slate-600">
                Duy trì chuỗi điểm danh để nhận xu thưởng tăng dần, dùng chơi minigame và đổi
                voucher độc quyền.
              </p>

              <div class="mt-4 flex flex-wrap items-center gap-2.5">
                <div
                  class="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-xl bg-indigo-50/80 border border-indigo-100 text-xs font-bold text-slate-700"
                >
                  <i class="fa-solid fa-fire text-orange-500"></i>
                  Chuỗi:
                  <span class="font-black text-slate-900"
                    >{{ wallet.chuoiDiemDanh || 0 }} ngày</span
                  >
                </div>
                <div
                  class="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-xl bg-amber-50/80 border border-amber-200/60 text-xs font-bold text-amber-800"
                >
                  <i class="fa-solid fa-coins text-amber-600"></i>
                  Hôm nay: <span class="font-black">+{{ nextCheckinCoins }} Xu</span>
                </div>
              </div>
            </div>

            <button
              @click="performCheckIn"
              :disabled="wallet.daDiemDanhHomNay || isCheckingIn"
              class="xl:w-auto w-full px-5 py-3.5 rounded-2xl font-black text-xs md:text-sm transition-all active:scale-95 flex items-center justify-center gap-2"
              :class="
                wallet.daDiemDanhHomNay
                  ? 'bg-emerald-500/90 text-white cursor-not-allowed shadow-none'
                  : 'bg-amber-400 hover:bg-amber-300 text-slate-950 shadow-lg shadow-amber-500/20 hover:-translate-y-0.5'
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
          <div class="mt-6 rounded-2xl bg-white/90 border border-indigo-100/80 p-4">
            <div class="flex items-center justify-between gap-3 mb-3">
              <div class="text-xs font-bold text-slate-700 flex items-center gap-1.5">
                <i class="fa-solid fa-route text-indigo-500"></i>
                Lộ trình phần thưởng
              </div>
              <div
                class="text-[10px] font-bold text-amber-600 bg-amber-50 px-2 py-0.5 rounded-lg border border-amber-200/60"
              >
                Mốc tiếp theo: Ngày {{ (wallet.chuoiDiemDanh || 0) + 1 }}
              </div>
            </div>

            <div class="grid grid-cols-3 sm:grid-cols-4 md:grid-cols-7 gap-2">
              <div
                v-for="(milestone, idx) in previewMilestones"
                :key="idx"
                class="min-h-[72px] rounded-xl border flex flex-col items-center justify-center text-center p-1.5 transition-all"
                :class="
                  milestone.ngayThu <= wallet.chuoiDiemDanh
                    ? 'bg-emerald-50 border-emerald-200 text-emerald-800'
                    : milestone.ngayThu === wallet.chuoiDiemDanh + 1 && !wallet.daDiemDanhHomNay
                      ? 'bg-amber-400 text-slate-950 border-amber-300 shadow-md shadow-amber-500/20 scale-[1.02]'
                      : 'bg-slate-50/80 border-slate-200/60 text-slate-600'
                "
              >
                <span class="text-[9px] uppercase tracking-wider font-bold opacity-75">
                  Ngày {{ milestone.ngayThu }}
                </span>
                <div class="mt-0.5 font-black text-xs flex items-center gap-1">
                  <i class="fa-solid fa-coins text-amber-600"></i>
                  {{ milestone.soXuThuong }}
                </div>
                <span class="mt-0.5 text-[8px] font-bold opacity-75">
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
            <div class="text-[10px] uppercase tracking-[.2em] font-black text-indigo-500">
              Play & Win
            </div>
            <h2 class="text-2xl font-black tracking-tight text-slate-900">Chơi game nhận quà</h2>
          </div>
          <div class="hidden sm:flex items-center gap-2 text-xs font-bold text-slate-600">
            <span class="w-2 h-2 rounded-full bg-emerald-400"></span>
            Minigame đang hoạt động
          </div>
        </div>

        <div class="grid grid-cols-1 lg:grid-cols-2 gap-4">
          <!-- WHEEL -->
          <div
            class="relative overflow-hidden rounded-[22px] bg-white border border-slate-200 shadow-sm hover:shadow-xl hover:shadow-indigo-100/60 transition-all"
          >
            <div
              class="absolute top-0 left-0 right-0 h-1 bg-gradient-to-r from-indigo-500 via-fuchsia-500 to-amber-400"
            ></div>

            <div class="p-5 md:p-6">
              <div class="flex items-start justify-between gap-4">
                <div>
                  <div
                    class="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-lg bg-indigo-50 text-indigo-600 text-[10px] font-black uppercase tracking-wider"
                  >
                    <i class="fa-solid fa-fire"></i>
                    Hot game
                  </div>
                  <h3 class="mt-2 text-xl font-black text-slate-900">Vòng quay may mắn</h3>
                  <p class="mt-1 text-xs leading-5 text-slate-600">
                    Quay 1 lần chỉ 5 Xu. Biết đâu hôm nay là ngày may mắn của bạn.
                  </p>
                </div>
                <div
                  class="shrink-0 px-2.5 py-1.5 rounded-xl bg-amber-50 border border-amber-100 text-amber-700 text-[10px] font-black"
                >
                  <i class="fa-solid fa-coins mr-1"></i>5 Xu/lượt
                </div>
              </div>

              <div class="relative w-[290px] h-[290px] md:w-[330px] md:h-[330px] mx-auto my-5">
                <!-- pointer -->
                <div class="absolute -top-1 left-1/2 -translate-x-1/2 z-30">
                  <div
                    class="w-0 h-0 border-l-[13px] border-l-transparent border-r-[13px] border-r-transparent border-t-[28px] border-t-rose-500 drop-shadow-lg"
                  ></div>
                  <div
                    class="absolute -top-2 left-1/2 -translate-x-1/2 w-5 h-5 rounded-full bg-white border-4 border-rose-500 shadow-md"
                  ></div>
                </div>

                <!-- glow -->
                <div
                  class="absolute inset-0 rounded-full bg-gradient-to-br from-indigo-500/20 via-fuchsia-500/10 to-amber-400/20 blur-xl scale-110"
                ></div>

                <!-- outer ring -->
                <div
                  class="absolute inset-0 rounded-full border-[10px] border-white shadow-[0_20px_45px_rgba(79,70,229,.16)] bg-white"
                ></div>

                <div
                  class="absolute inset-[8px] rounded-full border-4 border-indigo-200 shadow-[0_0_0_3px_rgba(255,255,255,.95)] overflow-hidden"
                  :style="{
                    transform: `rotate(${wheelAngle}deg)`,
                    transition: isSpinning
                      ? 'transform 4s cubic-bezier(0.15, 0.85, 0.15, 1)'
                      : 'none',
                  }"
                >
                  <svg viewBox="0 0 300 300" class="w-full h-full">
                    <defs>
                      <radialGradient id="wheelCenterGlow">
                        <stop offset="0%" stop-color="#ffffff" stop-opacity=".25" />
                        <stop offset="100%" stop-color="#ffffff" stop-opacity="0" />
                      </radialGradient>
                    </defs>

                    <g v-for="(slice, index) in wheelSlices" :key="index">
                      <path
                        :d="getSlicePath(index, wheelSlices.length)"
                        :fill="sliceColors[index % sliceColors.length]"
                        stroke="#ffffff"
                        stroke-width="3"
                      />
                      <g
                        :transform="`rotate(${index * (360 / wheelSlices.length) + 360 / wheelSlices.length / 2}, 150, 150)`"
                      >
                        <text
                          x="150"
                          y="78"
                          fill="#334155"
                          font-size="11"
                          font-weight="900"
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
                    <circle cx="150" cy="150" r="72" fill="url(#wheelCenterGlow)" />
                  </svg>
                </div>

                <!-- center button -->
                <button
                  @click="spinWheel"
                  :disabled="isSpinning || wallet.soXu < 5"
                  class="absolute z-20 left-1/2 top-1/2 -translate-x-1/2 -translate-y-1/2 w-[78px] h-[78px] rounded-full border-[5px] border-white shadow-[0_8px_25px_rgba(79,70,229,.28)] flex flex-col items-center justify-center transition-transform active:scale-95"
                  :class="
                    isSpinning || wallet.soXu < 5
                      ? 'bg-slate-400 cursor-not-allowed'
                      : 'bg-gradient-to-br from-indigo-500 via-violet-500 to-fuchsia-500 hover:scale-105'
                  "
                >
                  <i
                    class="fa-solid fa-play text-white text-lg"
                    :class="{ 'animate-spin': isSpinning }"
                  ></i>
                  <span class="text-[9px] font-black text-white mt-0.5">
                    {{ isSpinning ? '...' : 'QUAY' }}
                  </span>
                </button>
              </div>

              <button
                @click="spinWheel"
                :disabled="isSpinning || wallet.soXu < 5"
                class="w-full py-3.5 rounded-2xl font-black text-sm transition-all flex items-center justify-center gap-2 active:scale-[.98]"
                :class="
                  isSpinning || wallet.soXu < 5
                    ? 'bg-slate-100 text-slate-600 cursor-not-allowed'
                    : 'bg-indigo-600 hover:bg-indigo-700 text-white shadow-lg shadow-indigo-200'
                "
              >
                <i class="fa-solid fa-rotate" :class="{ 'animate-spin': isSpinning }"></i>
                {{
                  isSpinning
                    ? 'Đang quay thưởng...'
                    : wallet.soXu < 5
                      ? 'Không đủ xu — cần 5 Xu'
                      : 'Quay ngay với 5 Xu'
                }}
              </button>
            </div>
          </div>

          <!-- CARD GAME -->
          <div
            class="relative overflow-hidden rounded-[22px] bg-white text-slate-800 border border-slate-200 shadow-sm hover:shadow-xl hover:shadow-violet-100/50 transition-all"
          >
            <div
              class="absolute -right-16 -top-16 w-48 h-48 rounded-full bg-violet-500/20 blur-3xl"
            ></div>
            <div
              class="absolute -left-16 bottom-0 w-48 h-48 rounded-full bg-indigo-500/15 blur-3xl"
            ></div>

            <div class="relative z-10 p-5 md:p-6 h-full flex flex-col">
              <div class="flex items-start justify-between gap-4">
                <div>
                  <div
                    class="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-lg bg-indigo-50 border border-indigo-100 text-violet-700 text-[10px] font-black uppercase tracking-wider"
                  >
                    <i class="fa-solid fa-bolt"></i>
                    Quick game
                  </div>
                  <h3 class="mt-2 text-xl font-black">Lật thẻ bí mật</h3>
                  <p class="mt-1 text-xs leading-5 text-slate-600">
                    Chọn một thẻ. Phần thưởng bên trong đang chờ bạn.
                  </p>
                </div>

                <div class="text-right shrink-0">
                  <div class="text-[9px] uppercase tracking-wider text-slate-600 font-bold">
                    Lượt hôm nay
                  </div>
                  <div class="text-sm font-black text-slate-800">
                    {{ soLuotLatTheConLai
                    }}<span class="text-slate-600">/{{ soLuotLatTheToiDa }}</span>
                  </div>
                </div>
              </div>

              <div class="flex-1 flex items-center justify-center py-5">
                <div class="grid grid-cols-3 gap-3 w-full max-w-md">
                  <div
                    v-for="i in 3"
                    :key="i"
                    @click="flipCard(i)"
                    class="h-44 rounded-2xl cursor-pointer relative transition-all duration-300 group"
                    :class="flippedCardIndex === i ? 'scale-[1.04]' : 'hover:-translate-y-2'"
                  >
                    <div
                      class="absolute inset-0 rounded-2xl border transition-all overflow-hidden flex flex-col items-center justify-center"
                      :class="
                        flippedCardIndex === i
                          ? 'bg-gradient-to-br from-indigo-500 via-violet-600 to-fuchsia-600 border-violet-300 shadow-2xl shadow-violet-900/40'
                          : 'bg-indigo-50 border-indigo-100 group-hover:bg-indigo-50 group-hover:border-violet-400/40'
                      "
                    >
                      <div
                        class="w-14 h-14 rounded-2xl flex items-center justify-center mb-3 transition-all"
                        :class="
                          flippedCardIndex === i
                            ? 'bg-white/70 text-amber-500'
                            : 'bg-indigo-50 text-violet-500 group-hover:scale-110'
                        "
                      >
                        <i
                          class="fa-solid text-2xl"
                          :class="flippedCardIndex === i ? 'fa-gift animate-bounce' : 'fa-question'"
                        ></i>
                      </div>
                      <span
                        class="text-[10px] font-black uppercase tracking-wider text-slate-800/80"
                      >
                        {{ flippedCardIndex === i ? 'Đang mở...' : `Thẻ ${i}` }}
                      </span>
                    </div>
                  </div>
                </div>
              </div>

              <div
                class="rounded-2xl bg-indigo-50 border border-indigo-100 px-4 py-3 text-[11px] text-slate-600 flex items-center gap-2"
              >
                <i class="fa-solid fa-circle-info text-violet-300"></i>
                Mỗi ngày tối đa {{ soLuotLatTheToiDa }} lượt. Chọn thẻ bất kỳ để nhận quà.
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- REWARD STORE -->
      <section class="rounded-2xl bg-white border border-slate-200 shadow-sm overflow-hidden">
        <div class="p-4 border-b border-slate-100 flex items-center justify-between">
          <div class="flex items-center gap-2.5">
            <div
              class="w-8 h-8 rounded-xl bg-indigo-50 text-indigo-600 flex items-center justify-center text-sm font-black"
            >
              <i class="fa-solid fa-ticket-simple"></i>
            </div>
            <div>
              <h2 class="text-sm font-black text-slate-900">Kho Voucher</h2>
              <p class="text-[10px] text-slate-500">Đổi xu lấy mã ưu đãi độc quyền</p>
            </div>
          </div>
          <button
            @click="openMyVouchersModal"
            class="px-3 py-1.5 rounded-xl bg-indigo-50/80 text-indigo-600 font-bold text-xs hover:bg-indigo-100 transition flex items-center gap-1.5"
          >
            <i class="fa-solid fa-wallet text-[11px]"></i> Của tôi
            <span
              v-if="unusedVouchersCount > 0"
              class="w-4 h-4 rounded-full bg-indigo-600 text-white text-[9px] flex items-center justify-center font-black"
            >
              {{ unusedVouchersCount }}
            </span>
          </button>
        </div>

        <div class="p-3 md:p-4">
          <div
            v-if="khoVouchers.length > 0"
            class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-2.5"
          >
            <div
              v-for="voucher in sortedKhoVouchers"
              :key="voucher.id"
              class="relative group flex items-center justify-between p-2.5 rounded-xl border border-slate-200/80 hover:border-indigo-300 hover:shadow-sm transition-all bg-gradient-to-r from-slate-50/60 to-white overflow-hidden"
            >
              <!-- Left accent line / stub effect -->
              <div
                class="absolute left-0 top-0 bottom-0 w-1 bg-gradient-to-b from-indigo-500 to-violet-600"
              ></div>

              <div class="flex items-center gap-2.5 pl-2 min-w-0 flex-1">
                <!-- Value Badge -->
                <div
                  class="w-10 h-10 rounded-lg bg-indigo-50 border border-indigo-100 flex flex-col items-center justify-center shrink-0 text-indigo-700 shadow-sm"
                >
                  <span class="text-[8px] font-bold uppercase tracking-tighter opacity-70"
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

                <!-- Info -->
                <div class="min-w-0 flex-1 pr-1">
                  <h3 class="font-bold text-slate-900 text-xs truncate">
                    {{ voucher.tenVoucher }}
                  </h3>
                  <div class="text-[10px] text-slate-500 mt-0.5 truncate">
                    <span
                      >Đơn tối thiểu:
                      <strong class="text-slate-700"
                        >{{
                          Number(voucher.dieuKienToiThieu || 0).toLocaleString('vi-VN')
                        }}đ</strong
                      ></span
                    >
                  </div>
                  <div class="text-[10px] font-bold text-amber-600 mt-0.5 flex items-center gap-1">
                    <span>
                      <i class="fa-solid fa-coins text-[9px] mr-0.5"></i>{{ voucher.soXuDoi }} Xu
                    </span>
                  </div>
                </div>
              </div>

              <!-- Action button -->
              <div class="shrink-0 pl-1.5 border-l border-slate-100">
                <button
                  @click="handleDoiVoucher(voucher.id)"
                  :disabled="wallet.soXu < voucher.soXuDoi || voucher.soLuongConLai <= 0"
                  class="px-2.5 py-1.5 rounded-lg text-[11px] font-black transition-all shadow-sm flex items-center gap-1 bg-indigo-600 hover:bg-indigo-700 text-white disabled:bg-slate-200 disabled:text-slate-400 disabled:shadow-none disabled:cursor-not-allowed"
                >
                  <i class="fa-solid fa-gift text-[9px]"></i>
                  {{ voucher.soLuongConLai <= 0 ? 'Hết' : 'Đổi' }}
                </button>
              </div>
            </div>
          </div>

          <!-- Empty State -->
          <div v-else class="py-10 text-center text-xs text-slate-400">
            <i class="fa-solid fa-ticket-simple text-2xl mb-2 opacity-40"></i>
            <p>Hiện chưa có voucher nào trong kho</p>
          </div>
        </div>
      </section>
    </main>

    <!-- HISTORY MODAL -->
    <transition name="modal">
      <div
        v-if="showHistoryModal"
        class="fixed inset-0 z-[70] flex items-center justify-center bg-slate-900/45 backdrop-blur-sm p-4"
        @click.self="showHistoryModal = false"
      >
        <div class="bg-white rounded-[22px] max-w-2xl w-full shadow-2xl overflow-hidden">
          <div class="px-5 py-4 border-b border-slate-100 flex items-center justify-between">
            <div class="flex items-center gap-3">
              <div
                class="w-10 h-10 rounded-xl bg-amber-50 text-amber-600 flex items-center justify-center"
              >
                <i class="fa-solid fa-clock-rotate-left"></i>
              </div>
              <div>
                <h3 class="font-black text-base text-slate-900">Lịch sử Xu</h3>
                <p class="text-xs text-slate-600">Toàn bộ biến động cộng/trừ Xu</p>
              </div>
            </div>
            <button
              @click="showHistoryModal = false"
              class="w-8 h-8 rounded-xl bg-slate-100 hover:bg-slate-200 text-slate-600 flex items-center justify-center transition"
            >
              <i class="fa-solid fa-xmark"></i>
            </button>
          </div>

          <div class="overflow-auto px-5 py-3 max-h-[60vh]">
            <table class="w-full text-left text-sm">
              <thead>
                <tr
                  class="text-[10px] uppercase tracking-wider text-slate-600 border-b border-slate-100"
                >
                  <th class="py-2.5 px-3">Thời gian</th>
                  <th class="py-2.5 px-3">Nội dung</th>
                  <th class="py-2.5 px-3 text-right">Biến động</th>
                </tr>
              </thead>
              <tbody class="divide-y divide-slate-100">
                <tr
                  v-for="history in paginatedHistoryList"
                  :key="history.id"
                  class="hover:bg-slate-50 transition"
                >
                  <td class="py-2.5 px-3 text-xs text-slate-600 whitespace-nowrap">
                    {{ new Date(history.ngayTao).toLocaleString('vi-VN') }}
                  </td>
                  <td class="py-2.5 px-3 text-xs font-bold text-slate-700">{{ history.moTa }}</td>
                  <td
                    class="py-2.5 px-3 text-right font-black"
                    :class="history.soXuThayDoi > 0 ? 'text-emerald-600' : 'text-rose-600'"
                  >
                    {{ history.soXuThayDoi > 0 ? `+${history.soXuThayDoi}` : history.soXuThayDoi }}
                    Xu
                  </td>
                </tr>
                <tr v-if="historyList.length === 0">
                  <td colspan="3" class="py-8 text-center text-xs text-slate-600">
                    Chưa có lịch sử giao dịch Xu.
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <div
            v-if="historyTotalPages > 1"
            class="px-5 py-3 border-t border-slate-100 flex items-center justify-between bg-slate-50/50"
          >
            <span class="text-[11px] text-slate-600">
              {{ paginatedHistoryList.length }} / {{ historyList.length }} giao dịch
            </span>
            <div class="flex items-center gap-2">
              <button
                @click="historyCurrentPage > 1 && historyCurrentPage--"
                :disabled="historyCurrentPage === 1"
                class="px-2.5 py-1.5 rounded-xl border border-slate-200 text-xs font-bold disabled:opacity-40 hover:bg-white"
              >
                <i class="fa-solid fa-chevron-left text-[10px]"></i>
              </button>
              <span class="text-xs font-black text-slate-600">
                {{ historyCurrentPage }} / {{ historyTotalPages }}
              </span>
              <button
                @click="historyCurrentPage < historyTotalPages && historyCurrentPage++"
                :disabled="historyCurrentPage === historyTotalPages"
                class="px-2.5 py-1.5 rounded-xl border border-slate-200 text-xs font-bold disabled:opacity-40 hover:bg-white"
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
        class="fixed inset-0 z-[70] flex items-center justify-center bg-slate-900/50 backdrop-blur-sm p-4"
        @click.self="showRewardModal = false"
      >
        <div
          class="relative overflow-hidden bg-white border border-slate-200 rounded-[24px] max-w-lg w-full max-h-[88vh] overflow-y-auto shadow-[0_25px_80px_rgba(15,23,42,.28)]"
        >
          <div class="h-1.5 bg-gradient-to-r from-indigo-500 via-violet-500 to-fuchsia-500"></div>
          <div class="p-5 md:p-6">
            <div class="flex items-start justify-between gap-4">
              <div class="flex items-center gap-3">
                <div
                  class="w-14 h-14 rounded-2xl bg-gradient-to-br from-indigo-50 to-violet-100 border border-indigo-100 text-indigo-600 flex items-center justify-center text-2xl"
                >
                  <i class="fa-solid fa-gift"></i>
                </div>
                <div>
                  <div class="text-[10px] uppercase tracking-[.18em] font-black text-indigo-600">
                    Nhận phần thưởng
                  </div>
                  <div class="mt-1 text-sm font-bold text-slate-600">
                    Chúc mừng! Bạn vừa nhận được phần thưởng.
                  </div>
                </div>
              </div>
              <button
                @click="showRewardModal = false"
                class="w-9 h-9 rounded-xl bg-slate-100 hover:bg-slate-200 text-slate-600 flex items-center justify-center"
              >
                <i class="fa-solid fa-xmark"></i>
              </button>
            </div>
            <div
              class="mt-6 rounded-2xl bg-gradient-to-br from-indigo-50 via-white to-violet-50 border border-indigo-100 p-5"
            >
              <div class="text-[9px] uppercase tracking-[.18em] font-black text-indigo-500">
                Phần thưởng của bạn
              </div>
              <h3 class="mt-2 text-2xl font-black leading-tight text-slate-900 break-words">
                {{ rewardData.tenPhanThuong || 'Phần quà đặc biệt' }}
              </h3>
              <p
                v-if="rewardData.moTa"
                class="mt-3 text-sm leading-6 text-slate-600 whitespace-pre-line"
              >
                {{ rewardData.moTa }}
              </p>
            </div>

            <div class="mt-4 grid grid-cols-1 sm:grid-cols-2 gap-3">
              <div class="rounded-2xl bg-slate-50 border border-slate-200 p-4">
                <div class="text-[9px] uppercase tracking-wider font-bold text-slate-500">
                  Loại phần thưởng
                </div>
                <div class="mt-1 text-sm font-black text-indigo-700">
                  {{
                    rewardData.loaiPhanThuong === 'xu'
                      ? 'Xu'
                      : rewardData.loaiPhanThuong === 'voucher'
                        ? 'Voucher'
                        : rewardData.loaiPhanThuong === 'khong_trung'
                          ? 'Không trúng'
                          : rewardData.loaiPhanThuong || 'Phần thưởng'
                  }}
                </div>
              </div>
              <div
                v-if="rewardData.giaTriXu !== undefined && rewardData.giaTriXu !== null"
                class="rounded-2xl bg-amber-50 border border-amber-200 p-4"
              >
                <div class="text-[9px] uppercase tracking-wider font-bold text-amber-700">
                  Giá trị nhận
                </div>
                <div class="mt-1 text-base font-black text-amber-700">
                  {{ Number(rewardData.giaTriXu).toLocaleString('vi-VN') }} Xu
                </div>
              </div>
              <div
                v-if="rewardData.id_voucher !== undefined && rewardData.id_voucher !== null"
                class="rounded-2xl bg-violet-50 border border-violet-200 p-4"
              >
                <div class="text-[9px] uppercase tracking-wider font-bold text-violet-700">
                  Voucher
                </div>
                <div class="mt-1 text-sm font-black text-violet-700">
                  #{{ rewardData.id_voucher }}
                </div>
              </div>
            </div>

            <div
              v-if="rewardData.loaiPhanThuong === 'khong_trung'"
              class="mt-4 rounded-2xl bg-slate-50 border border-slate-200 px-4 py-3.5 text-sm leading-6 text-slate-600"
            >
              <i class="fa-solid fa-circle-info text-slate-400 mr-1.5"></i>Lần này chưa có phần
              thưởng. Chúc bạn may mắn ở lượt tiếp theo!
            </div>
            <button
              @click="showRewardModal = false"
              class="mt-5 w-full py-3.5 rounded-2xl bg-indigo-600 hover:bg-indigo-700 text-white font-black text-sm transition shadow-lg shadow-indigo-200"
            >
              Đã xem phần thưởng
            </button>
          </div>
        </div>
      </div>
    </transition>

    <!-- EXCHANGE CONFIRM MODAL -->
    <transition name="modal">
      <div
        v-if="showExchangeModal && pendingVoucher"
        class="fixed inset-0 z-[75] flex items-center justify-center bg-slate-900/50 backdrop-blur-sm p-4"
        @click.self="showExchangeModal = false"
      >
        <div
          class="bg-white rounded-[24px] max-w-sm w-full p-5 shadow-2xl overflow-hidden border border-slate-100"
        >
          <div class="flex items-center justify-between pb-3 border-b border-slate-100">
            <div class="flex items-center gap-2.5">
              <div
                class="w-9 h-9 rounded-xl bg-indigo-50 text-indigo-600 flex items-center justify-center text-sm font-black"
              >
                <i class="fa-solid fa-ticket"></i>
              </div>
              <div>
                <h3 class="font-black text-sm text-slate-900">Xác nhận đổi Voucher</h3>
                <p class="text-[10px] text-slate-500">Dùng Xu nhận mã ưu đãi</p>
              </div>
            </div>
            <button
              @click="showExchangeModal = false"
              class="w-8 h-8 rounded-xl bg-slate-100 hover:bg-slate-200 text-slate-500 flex items-center justify-center transition"
            >
              <i class="fa-solid fa-xmark text-xs"></i>
            </button>
          </div>

          <div
            class="mt-4 rounded-2xl bg-gradient-to-br from-indigo-50/80 via-white to-violet-50/80 border border-indigo-100/80 p-4 text-slate-800"
          >
            <h4 class="font-black text-slate-900 text-xs line-clamp-2">
              {{ pendingVoucher.tenVoucher }}
            </h4>
            <div class="mt-2.5 flex items-center justify-between text-xs">
              <span class="text-slate-500">Giá trị giảm:</span>
              <span class="font-black text-rose-600">
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
              <span class="text-slate-500">Đơn tối thiểu:</span>
              <span class="font-bold text-slate-700"
                >{{ Number(pendingVoucher.dieuKienToiThieu || 0).toLocaleString('vi-VN') }}đ</span
              >
            </div>
          </div>

          <div class="mt-3 grid grid-cols-2 gap-2 text-center">
            <div class="rounded-xl bg-amber-50 border border-amber-200/60 p-2.5">
              <div class="text-[9px] uppercase font-bold text-amber-700">Chi phí đổi</div>
              <div class="mt-0.5 text-xs font-black text-amber-800">
                🪙 {{ pendingVoucher.soXuDoi }} Xu
              </div>
            </div>
            <div class="rounded-xl bg-slate-50 border border-slate-200/60 p-2.5">
              <div class="text-[9px] uppercase font-bold text-slate-500">Số dư còn lại</div>
              <div class="mt-0.5 text-xs font-black text-slate-700">
                {{ Math.max(0, (wallet.soXu || 0) - (pendingVoucher.soXuDoi || 0)) }} Xu
              </div>
            </div>
          </div>

          <div class="mt-4 flex gap-2">
            <button
              @click="showExchangeModal = false"
              class="flex-1 py-2.5 rounded-xl bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold text-xs transition"
            >
              Hủy
            </button>
            <button
              @click="confirmDoiVoucher"
              class="flex-1 py-2.5 rounded-xl bg-indigo-600 hover:bg-indigo-700 text-white font-black text-xs transition shadow-md shadow-indigo-200 flex items-center justify-center gap-1.5"
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
        class="fixed inset-0 z-[70] flex items-center justify-center bg-slate-900/45 backdrop-blur-sm p-4"
        @click.self="showMyVouchersModal = false"
      >
        <div
          class="bg-white rounded-2xl max-w-2xl w-full max-h-[88vh] flex flex-col shadow-xl overflow-hidden"
        >
          <div class="px-4 py-4 border-b border-slate-100">
            <div class="flex items-start justify-between gap-4">
              <div class="flex items-center gap-3">
                <div
                  class="w-11 h-11 rounded-2xl bg-indigo-50 text-indigo-600 flex items-center justify-center"
                >
                  <i class="fa-solid fa-wallet"></i>
                </div>
                <div>
                  <h3 class="font-black text-xl text-slate-900">Voucher của tôi</h3>
                  <p class="text-xs text-slate-600 mt-0.5">Những ưu đãi bạn đã đổi từ Xu</p>
                </div>
              </div>
              <button
                @click="showMyVouchersModal = false"
                class="w-9 h-9 rounded-xl bg-slate-100 hover:bg-slate-200 text-slate-600 flex items-center justify-center"
              >
                <i class="fa-solid fa-xmark"></i>
              </button>
            </div>

            <div class="mt-5 flex gap-2 overflow-x-auto pb-1">
              <button
                @click="myVoucherFilter = 'ALL'"
                :class="
                  myVoucherFilter === 'ALL'
                    ? 'bg-indigo-600 text-white'
                    : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
                "
                class="px-4 py-2 rounded-xl text-xs font-black whitespace-nowrap transition"
              >
                Tất cả ({{ myVouchers.length }})
              </button>
              <button
                @click="myVoucherFilter = 'CHUA_DUNG'"
                :class="
                  myVoucherFilter === 'CHUA_DUNG'
                    ? 'bg-indigo-600 text-white'
                    : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
                "
                class="px-4 py-2 rounded-xl text-xs font-black whitespace-nowrap transition"
              >
                Chưa dùng ({{ unusedVouchersCount }})
              </button>
              <button
                @click="myVoucherFilter = 'DA_DUNG'"
                :class="
                  myVoucherFilter === 'DA_DUNG'
                    ? 'bg-slate-700 text-white'
                    : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
                "
                class="px-4 py-2 rounded-xl text-xs font-black whitespace-nowrap transition"
              >
                Đã dùng
              </button>
            </div>
          </div>

          <div class="overflow-y-auto p-4 space-y-2.5">
            <div
              v-for="item in filteredMyVouchers"
              :key="item.idVoucherKhachHang"
              class="relative group flex items-center justify-between p-2.5 rounded-xl border border-slate-200/80 hover:border-indigo-300 hover:shadow-sm transition-all bg-gradient-to-r from-slate-50/60 to-white overflow-hidden"
            >
              <!-- Left accent line -->
              <div
                class="absolute left-0 top-0 bottom-0 w-1 bg-gradient-to-b from-indigo-500 to-violet-600"
              ></div>

              <div class="flex items-center gap-2.5 pl-2 min-w-0 flex-1">
                <!-- Value Badge -->
                <div
                  class="w-10 h-10 rounded-lg bg-indigo-50 border border-indigo-100 flex flex-col items-center justify-center shrink-0 text-indigo-700 shadow-sm"
                >
                  <span class="text-[8px] font-bold uppercase tracking-tighter opacity-70"
                    >Ưu đãi</span
                  >
                  <span class="text-[11px] font-black text-rose-600 truncate max-w-[36px]">
                    {{ item.hienThiGiaTriGiam || 'KM' }}
                  </span>
                </div>

                <!-- Info -->
                <div class="min-w-0 flex-1 pr-1">
                  <div class="flex items-center gap-2">
                    <span
                      class="px-2 py-0.5 rounded text-[8px] font-black uppercase"
                      :class="
                        item.trangThai === 'CHUA_DUNG'
                          ? 'bg-emerald-50 text-emerald-600 border border-emerald-100'
                          : 'bg-slate-100 text-slate-600 border border-slate-200'
                      "
                    >
                      {{ item.trangThai === 'CHUA_DUNG' ? 'Chưa dùng' : item.trangThai }}
                    </span>
                    <span
                      v-if="item.sapHetHan"
                      class="px-1.5 py-0.5 rounded bg-rose-50 text-rose-600 text-[8px] font-black"
                    >
                      Sắp hết hạn
                    </span>
                  </div>
                  <h4 class="font-bold text-slate-900 text-xs truncate mt-0.5">
                    {{ item.tenVoucher }}
                  </h4>
                  <div class="text-[10px] text-slate-500 mt-0.5 flex items-center gap-2">
                    <span
                      >Đơn tối thiểu:
                      <strong class="text-slate-700"
                        >{{ Number(item.dieuKienToiThieu || 0).toLocaleString('vi-VN') }}đ</strong
                      ></span
                    >
                    <span class="text-slate-300">|</span>
                    <span>Hạn: {{ new Date(item.ngayHetHan).toLocaleDateString('vi-VN') }}</span>
                  </div>
                </div>
              </div>

              <!-- Action / Code -->
              <div class="shrink-0 pl-2 border-l border-slate-100 flex items-center gap-2">
                <div
                  class="px-2 py-1 rounded-lg bg-slate-50 border border-dashed border-slate-300 font-mono font-black text-indigo-600 text-[11px]"
                >
                  {{ item.maCode }}
                </div>
                <button
                  @click="copyCode(item.maCode)"
                  class="w-7 h-7 rounded-lg bg-indigo-50 hover:bg-indigo-100 text-indigo-600 flex items-center justify-center transition"
                  title="Sao chép mã"
                >
                  <i class="fa-regular fa-copy text-xs"></i>
                </button>
              </div>
            </div>

            <div
              v-if="filteredMyVouchers.length === 0"
              class="py-14 text-center rounded-2xl border border-dashed border-slate-200 bg-slate-50"
            >
              <div
                class="w-14 h-14 rounded-2xl bg-white border border-slate-200 mx-auto flex items-center justify-center text-xl text-slate-600"
              >
                <i class="fa-solid fa-ticket"></i>
              </div>
              <div class="mt-3 text-sm font-black text-slate-600">
                Chưa có voucher trong mục này
              </div>
              <div class="mt-1 text-xs text-slate-600">
                Hãy quay game hoặc đổi voucher để sử dụng tại đây.
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
    console.error('Lỗi đọc thông tin user từ sessionStorage:', e)
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
  '#4f46e5',
  '#f59e0b',
  '#10b981',
  '#8b5cf6',
  '#f43f5e',
  '#06b6d4',
  '#eab308',
  '#64748b',
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
  // Chỉ lấy các voucher có trạng thái hoạt động (trangThai === true)
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
.clip-triangle {
  clip-path: polygon(50% 100%, 0 0, 100% 0);
}

.toast-enter-active,
.toast-leave-active {
  transition: all 0.3s ease;
}

.toast-enter-from,
.toast-leave-to {
  opacity: 0;
  transform: translateY(-18px) scale(0.98);
}

.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.22s ease;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}

.perspective-1000 {
  perspective: 1000px;
}

.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
