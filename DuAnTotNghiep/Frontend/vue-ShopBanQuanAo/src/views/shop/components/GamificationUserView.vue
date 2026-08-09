<template>
  <div
    class="min-h-screen bg-slate-100/70 font-sans text-slate-800 pb-16 relative selection:bg-indigo-500 selection:text-white"
  >
    <!-- 🔔 TOAST NOTIFICATION SYSTEM -->
    <transition name="toast">
      <div
        v-if="toast.show"
        class="fixed top-5 right-5 z-50 flex items-center gap-3 px-5 py-3.5 rounded-2xl shadow-xl border text-sm font-semibold backdrop-blur-md"
        :class="
          toast.type === 'success'
            ? 'bg-emerald-50/90 border-emerald-200 text-emerald-800'
            : 'bg-rose-50/90 border-rose-200 text-rose-800'
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

    <!-- Header / Navbar Khách Hàng -->
    <header
      class="bg-white/80 backdrop-blur-md border-b border-slate-200/80 sticky top-0 z-30 shadow-xs transition-all"
    >
      <div class="max-w-6xl mx-auto px-4 h-16 flex items-center justify-between">
        <div class="flex items-center gap-3">
          <span class="text-xl font-extrabold tracking-tight text-indigo-600">
            K-<span class="text-amber-500">Zone</span> Gaming
          </span>
        </div>
        <div class="flex items-center gap-4">
          <!-- Ví Xu Badge -->
          <div
            class="bg-amber-50 border border-amber-200/60 px-4 py-1.5 rounded-full flex items-center gap-2 shadow-xs hover:scale-105 transition transform"
          >
            <i class="fa-solid fa-coins text-amber-500 animate-pulse"></i>
            <span class="text-xs font-semibold text-amber-900 uppercase">Ví Xu:</span>
            <span class="text-sm font-bold text-amber-600">{{ wallet.soXu || 0 }} Xu</span>
          </div>
          <div
            class="w-9 h-9 rounded-full bg-indigo-100 border border-indigo-200 flex items-center justify-center font-bold text-indigo-700 text-sm shadow-xs"
          >
            KH
          </div>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="max-w-6xl mx-auto px-4 mt-8 space-y-8">
      <!-- 🌟 BANNER & LỘ TRÌNH ĐIỂM DANH HÀNG NGÀY -->
      <section
        class="bg-gradient-to-r from-indigo-600 via-indigo-700 to-violet-700 rounded-3xl p-6 md:p-8 text-white shadow-xl relative overflow-hidden space-y-6"
      >
        <div
          class="absolute -right-10 -bottom-10 w-60 h-60 bg-white/10 rounded-full blur-2xl pointer-events-none"
        ></div>

        <div class="flex flex-col md:flex-row justify-between items-center gap-6 relative z-10">
          <div class="space-y-2 text-center md:text-left">
            <span
              class="bg-white/20 backdrop-blur-sm text-white text-xs font-semibold px-3 py-1 rounded-full uppercase tracking-wider inline-block"
            >
              Streak Điểm Danh (Chuỗi: {{ wallet.chuoiDiemDanh || 0 }} ngày)
            </span>
            <h2 class="text-2xl md:text-3xl font-extrabold tracking-tight">
              Điểm Danh Mỗi Ngày - Nhận Quà Liền Tay
            </h2>
            <p class="text-indigo-100 text-sm max-w-xl leading-relaxed">
              Duy trì điểm danh hàng ngày để tích lũy xu khủng tăng dần qua từng ngày theo cấp số
              cộng tại K-Zone.
            </p>
          </div>

          <button
            @click="performCheckIn"
            :disabled="wallet.daDiemDanhHomNay || isCheckingIn"
            :class="
              wallet.daDiemDanhHomNay
                ? 'bg-emerald-600/90 cursor-not-allowed shadow-none'
                : 'bg-amber-500 hover:bg-amber-600 shadow-lg hover:shadow-amber-500/30 hover:-translate-y-0.5'
            "
            class="px-7 py-4 rounded-2xl font-bold text-white transition-all duration-200 flex items-center gap-2.5 text-sm shrink-0 relative z-10 active:scale-95"
          >
            <i
              class="fa-solid"
              :class="isCheckingIn ? 'fa-spinner fa-spin' : 'fa-calendar-check'"
            ></i>
            {{
              wallet.daDiemDanhHomNay
                ? 'Đã Điểm Danh Hôm Nay'
                : `Điểm Danh Ngay (+${nextCheckinCoins} Xu)`
            }}
          </button>
        </div>

        <!-- 📅 LỘ TRÌNH CHUỖI NGÀY ĐIỂM DANH -->
        <div
          class="bg-white/10 backdrop-blur-md rounded-2xl p-4 border border-white/15 relative z-10"
        >
          <div class="text-xs font-semibold text-indigo-100 mb-3 flex items-center justify-between">
            <span>Mốc thưởng chuỗi ngày (Xem trước phần thưởng):</span>
            <span class="text-[11px] text-amber-300 font-bold"
              >Hôm nay nhận: +{{ nextCheckinCoins }} Xu</span
            >
          </div>

          <div class="grid grid-cols-3 sm:grid-cols-4 md:grid-cols-7 gap-2.5">
            <div
              v-for="(milestone, idx) in previewMilestones"
              :key="idx"
              class="rounded-xl p-3 flex flex-col items-center justify-center text-center transition-all border"
              :class="
                milestone.ngayThu <= wallet.chuoiDiemDanh
                  ? 'bg-emerald-500/20 border-emerald-400/40 text-emerald-200'
                  : milestone.ngayThu === wallet.chuoiDiemDanh + 1 && !wallet.daDiemDanhHomNay
                    ? 'bg-amber-500 text-white border-amber-300 shadow-lg scale-105 font-bold animate-pulse'
                    : 'bg-white/5 border-white/10 text-indigo-200'
              "
            >
              <span class="text-[10px] uppercase opacity-80">Ngày {{ milestone.ngayThu }}</span>
              <div class="flex items-center gap-1 my-1 text-xs font-extrabold">
                <i class="fa-solid fa-coins text-amber-300 text-[10px]"></i>
                <span>{{ milestone.soXuThuong }} Xu</span>
              </div>
              <span
                class="text-[9px] px-1.5 py-0.5 rounded-md"
                :class="
                  milestone.ngayThu <= wallet.chuoiDiemDanh
                    ? 'bg-emerald-500/40 text-white'
                    : 'bg-black/20 text-indigo-200'
                "
              >
                {{ milestone.ngayThu <= wallet.chuoiDiemDanh ? 'Đã nhận' : 'Khóa' }}
              </span>
            </div>
          </div>
        </div>
      </section>

      <!-- Khu Vực Minigame (Vòng Quay & Lật Thẻ) -->
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <!-- Vòng Quay May Mắn -->
        <div
          class="bg-white border border-slate-200/80 rounded-3xl p-6 md:p-8 shadow-sm flex flex-col items-center text-center justify-between hover:shadow-md transition-shadow"
        >
          <div class="w-full text-left mb-4">
            <span class="bg-blue-50 text-blue-700 text-xs font-semibold px-2.5 py-1 rounded-lg"
              >Minigame Hot</span
            >
            <h3 class="text-xl font-bold text-slate-900 mt-2">Vòng Quay May Mắn</h3>
            <p class="text-sm text-slate-500">
              Thử vận may mỗi ngày với các phần thưởng hấp dẫn từ hệ thống.
            </p>
          </div>

          <!-- Vòng Quay SVG -->
          <div class="relative w-72 h-72 my-4 flex items-center justify-center">
            <div class="absolute -top-4 z-30 flex flex-col items-center filter drop-shadow-lg">
              <div
                class="w-5 h-7 bg-gradient-to-b from-amber-400 to-amber-600 clip-triangle rounded-sm border border-amber-300"
              ></div>
            </div>

            <div
              class="absolute inset-0 rounded-full border-8 border-amber-400/30 shadow-inner pointer-events-none z-10"
            ></div>

            <div
              class="w-full h-full rounded-full shadow-xl relative overflow-hidden transition-all"
              :style="{
                transform: `rotate(${wheelAngle}deg)`,
                transition: isSpinning ? 'transform 4s cubic-bezier(0.15, 0.85, 0.15, 1)' : 'none',
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
                      fill="#ffffff"
                      font-size="11"
                      font-weight="900"
                      text-anchor="middle"
                      dominant-baseline="middle"
                      class="drop-shadow-[0_1px_2px_rgba(0,0,0,0.6)] uppercase tracking-wider"
                    >
                      {{ slice.tenPhanThuong || slice.name }}
                    </text>
                  </g>
                </g>
              </svg>
            </div>

            <div
              class="absolute z-20 w-16 h-16 bg-gradient-to-tr from-amber-500 to-amber-400 rounded-full shadow-lg border-4 border-white flex items-center justify-center text-white font-extrabold text-[11px] tracking-tighter drop-shadow-md"
            >
              K-ZONE
            </div>
          </div>

          <!-- Đã FIX: Kiểm tra xu < 5 để chặn lỗi 400 từ Backend -->
          <button
            @click="spinWheel"
            :disabled="isSpinning || wallet.soXu < 5"
            :class="
              isSpinning || wallet.soXu < 5
                ? 'bg-slate-300 cursor-not-allowed text-slate-500'
                : 'bg-indigo-600 hover:bg-indigo-700 text-white shadow-md hover:shadow-indigo-500/30 hover:-translate-y-0.5'
            "
            class="w-full mt-4 font-semibold py-3.5 rounded-2xl transition-all duration-200 flex items-center justify-center gap-2 text-sm active:scale-98"
          >
            <i class="fa-solid fa-rotate" :class="{ 'animate-spin': isSpinning }"></i>
            {{
              isSpinning
                ? 'Đang quay thưởng...'
                : wallet.soXu < 5
                  ? 'Không đủ xu (Cần 5 Xu)'
                  : 'Quay Ngay (Mất 5 Xu)'
            }}
          </button>
        </div>

        <!-- Lật Thẻ Bí Mật -->
        <div
          class="bg-white border border-slate-200/80 rounded-3xl p-6 md:p-8 shadow-sm flex flex-col justify-between hover:shadow-md transition-shadow"
        >
          <div>
            <div class="flex justify-between items-center">
              <span
                class="bg-purple-50 text-purple-700 text-xs font-semibold px-2.5 py-1 rounded-lg"
                >Trò Chơi Nhanh</span
              >
              <span class="text-xs font-medium text-slate-400">
                Hôm nay còn lại: <strong>{{ soLuotLatTheConLai }}</strong> /
                {{ soLuotLatTheToiDa }} lượt
              </span>
            </div>
            <h3 class="text-xl font-bold text-slate-900 mt-2">Lật Thẻ Bí Mật</h3>
            <p class="text-sm text-slate-500">
              Chọn 1 trong các thẻ bên dưới để khám phá phần thưởng ẩn giấu.
            </p>
          </div>

          <!-- Khu vực thẻ bài 3D -->
          <div class="grid grid-cols-3 gap-3.5 my-6 perspective-1000">
            <div
              v-for="i in 3"
              :key="i"
              @click="flipCard(i)"
              class="h-36 rounded-2xl cursor-pointer relative transition-all duration-500 transform-gpu group"
              :class="flippedCardIndex === i ? 'scale-105' : 'hover:-translate-y-1'"
            >
              <div
                class="w-full h-full rounded-2xl flex flex-col items-center justify-center border-2 transition-all duration-300 shadow-sm"
                :class="
                  flippedCardIndex === i
                    ? 'bg-gradient-to-br from-indigo-600 to-violet-600 border-indigo-600 text-white shadow-lg rotate-y-180'
                    : 'bg-slate-50 hover:bg-indigo-50/60 border-dashed border-slate-300 hover:border-indigo-400 text-slate-600'
                "
              >
                <i
                  class="fa-solid text-2xl transition-transform duration-300 group-hover:scale-110 mb-2"
                  :class="
                    flippedCardIndex === i
                      ? 'fa-gift text-amber-300 animate-bounce'
                      : 'fa-box-open text-slate-400 group-hover:text-indigo-600'
                  "
                ></i>
                <span
                  class="text-xs font-bold tracking-wide"
                  :class="
                    flippedCardIndex === i
                      ? 'text-white'
                      : 'text-slate-500 group-hover:text-indigo-600'
                  "
                >
                  {{ flippedCardIndex === i ? 'Đang mở...' : `Thẻ #${i}` }}
                </span>
              </div>
            </div>
          </div>

          <p class="text-xs text-center text-slate-400 italic">
            Tối đa {{ soLuotLatTheToiDa }} lần lật mỗi ngày. Click vào thẻ bất kỳ để thử vận may!
          </p>
        </div>
      </div>

      <!-- ✨ KHU VỰC THỂ LỆ & KHO ĐỔI VOUCHER ƯU ĐÃI ✨ -->
      <div class="bg-white border border-slate-200/80 rounded-3xl p-6 md:p-8 shadow-sm space-y-6">
        <div class="flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
          <div>
            <span class="bg-amber-50 text-amber-700 text-xs font-semibold px-2.5 py-1 rounded-lg"
              >Cửa Hàng Đổi Quà</span
            >
            <h3 class="text-xl font-bold text-slate-900 mt-2">Đổi Xu Lấy Voucher Ưu Đãi</h3>
            <p class="text-sm text-slate-500">
              Sử dụng xu tích lũy từ điểm danh và minigame để đổi các voucher giảm giá độc quyền.
            </p>
          </div>

          <button
            @click="openMyVouchersModal"
            class="bg-white border border-slate-200 hover:border-indigo-500 text-slate-700 hover:text-indigo-600 px-4 py-2.5 rounded-2xl font-semibold text-sm transition shadow-xs flex items-center gap-2 shrink-0"
          >
            <i class="fa-solid fa-ticket text-indigo-500"></i> Voucher Của Tôi ({{
              myVouchers.length
            }})
          </button>
        </div>

        <!-- Khối Thể Lệ Tóm Tắt -->
        <div
          class="bg-indigo-50/60 border border-indigo-100 rounded-2xl p-5 text-indigo-900 flex items-start gap-3.5"
        >
          <div
            class="w-10 h-10 rounded-xl bg-indigo-600 text-white flex items-center justify-center shrink-0 mt-0.5 shadow-xs"
          >
            <i class="fa-solid fa-circle-info text-sm"></i>
          </div>
          <div class="space-y-1">
            <h4 class="font-bold text-sm">Thể Lệ Tích Xu & Đổi Quà</h4>
            <ul class="text-xs text-indigo-700 space-y-1.5 list-disc list-inside">
              <li>
                <strong>Tích lũy:</strong> Điểm danh hàng ngày (nhận xu tăng dần theo chuỗi ngày)
                hoặc tham gia Vòng quay / Lật thẻ may mắn (Tối đa 3 lần lật thẻ/ngày).
              </li>
              <li>
                <strong>Quy tắc đổi:</strong> Xu sau khi đổi voucher sẽ được trừ trực tiếp vào ví và
                <strong>không thể hoàn lại</strong>.
              </li>
              <li>
                <strong>Sử dụng:</strong> Mỗi voucher có mã code riêng, áp dụng khi thanh toán đơn
                hàng đạt giá trị tối thiểu tương ứng.
              </li>
            </ul>
          </div>
        </div>

        <!-- 🔍 THANH SẮP XẾP & ĐIỀU KHIỂN KHO VOUCHER -->
        <div
          class="flex items-center justify-between flex-wrap gap-3 pt-2 pb-1 border-b border-slate-100"
        >
          <div class="text-sm font-bold text-slate-800">
            Danh sách kho voucher ({{ khoVouchers.length }})
          </div>
          <div class="flex items-center gap-2">
            <span class="text-xs text-slate-500 font-medium">Sắp xếp theo xu:</span>
            <select
              v-model="sortBy"
              class="bg-slate-50 border border-slate-200 rounded-xl px-3 py-1.5 text-xs font-semibold text-slate-700 focus:outline-none focus:border-indigo-500 transition"
            >
              <option value="asc">Xu: Thấp đến Cao</option>
              <option value="desc">Xu: Cao đến Thấp</option>
            </select>
          </div>
        </div>

        <!-- Danh Sách Kho Voucher -->
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div
            v-for="voucher in paginatedKhoVouchers"
            :key="voucher.id"
            class="bg-white border border-slate-200/80 rounded-2xl p-5 shadow-xs hover:shadow-md transition flex flex-col justify-between relative overflow-hidden group"
          >
            <div
              class="absolute top-0 right-0 bg-gradient-to-l from-amber-500 to-amber-600 text-white text-[10px] font-bold px-3 py-1 rounded-bl-2xl shadow-xs"
            >
              <template
                v-if="
                  voucher.loaiGiamGia === 'PERCENT' ||
                  voucher.loaiGiamGia === 'phan_tram' ||
                  voucher.phanTramGiam
                "
              >
                Giảm {{ voucher.phanTramGiam || voucher.giaTriGiam }}%
                <span v-if="voucher.giamToiDa">
                  (Tối đa {{ Number(voucher.giamToiDa).toLocaleString('vi-VN') }}đ)</span
                >
              </template>
              <template v-else>
                Giảm {{ Number(voucher.giaTriGiam || 0).toLocaleString('vi-VN') }}đ
              </template>
            </div>

            <div class="space-y-3 pt-2">
              <div
                class="w-12 h-12 rounded-xl bg-amber-50 text-amber-500 flex items-center justify-center text-xl shadow-inner"
              >
                <i class="fa-solid fa-store"></i>
              </div>
              <div>
                <h4
                  class="font-bold text-slate-900 text-base group-hover:text-indigo-600 transition"
                >
                  {{ voucher.tenVoucher }}
                </h4>
                <p class="text-xs text-slate-400 mt-0.5">
                  Đơn tối thiểu:
                  {{ Number(voucher.dieuKienToiThieu || 0).toLocaleString('vi-VN') }}đ
                </p>
              </div>
              <div
                class="text-xs text-slate-500 bg-slate-50 p-2.5 rounded-xl border border-slate-100 flex justify-between items-center"
              >
                <span>
                  Còn lại: <strong class="text-slate-700">{{ voucher.soLuongConLai }} suất</strong>
                </span>
                <span class="text-slate-400">
                  Hạn: {{ new Date(voucher.ngayHetHan).toLocaleDateString('vi-VN') }}
                </span>
              </div>
            </div>

            <div class="mt-6 pt-4 border-t border-slate-100 flex items-center justify-between">
              <div class="flex items-center gap-1.5 text-amber-600 font-extrabold text-sm">
                <i class="fa-solid fa-coins text-amber-500"></i>
                <span>{{ voucher.soXuDoi }} Xu</span>
              </div>
              <button
                @click="handleDoiVoucher(voucher.id)"
                :disabled="wallet.soXu < voucher.soXuDoi || voucher.soLuongConLai <= 0"
                :class="
                  wallet.soXu >= voucher.soXuDoi && voucher.soLuongConLai > 0
                    ? 'bg-indigo-600 hover:bg-indigo-700 text-white shadow-sm'
                    : 'bg-slate-100 text-slate-400 cursor-not-allowed'
                "
                class="px-4 py-2 rounded-xl text-xs font-bold transition flex items-center gap-1.5"
              >
                <i class="fa-solid fa-gift"></i> Đổi Ngay
              </button>
            </div>
          </div>
        </div>

        <!-- 📑 PHÂN TRANG KHO VOUCHER -->
        <div
          v-if="totalPages > 1"
          class="flex items-center justify-center gap-3 pt-4 border-t border-slate-100"
        >
          <button
            @click="currentPage > 1 && currentPage--"
            :disabled="currentPage === 1"
            class="px-4 py-2 rounded-xl border border-slate-200 text-xs font-semibold bg-white disabled:opacity-40 disabled:cursor-not-allowed hover:bg-slate-50 transition flex items-center gap-1"
          >
            <i class="fa-solid fa-chevron-left"></i> Trước
          </button>
          <span class="text-xs font-bold text-slate-700 px-2">
            Trang {{ currentPage }} / {{ totalPages }}
          </span>
          <button
            @click="currentPage < totalPages && currentPage++"
            :disabled="currentPage === totalPages"
            class="px-4 py-2 rounded-xl border border-slate-200 text-xs font-semibold bg-white disabled:opacity-40 disabled:cursor-not-allowed hover:bg-slate-50 transition flex items-center gap-1"
          >
            Sau <i class="fa-solid fa-chevron-right"></i>
          </button>
        </div>

        <div v-if="khoVouchers.length === 0" class="text-center py-8 text-slate-400 text-xs italic">
          Hiện tại chưa có voucher nào trong kho!
        </div>
      </div>

      <!-- Lịch Sử Ví Xu Cá Nhân -->
      <div class="bg-white border border-slate-200/80 rounded-3xl p-6 md:p-8 shadow-sm space-y-4">
        <div class="flex items-center justify-between">
          <h3 class="text-lg font-bold text-slate-900">Lịch Sử Biến Động Xu</h3>
          <button
            @click="fetchClientData"
            class="text-xs font-semibold text-indigo-600 hover:text-indigo-800 flex items-center gap-1.5 transition"
          >
            <i class="fa-solid fa-arrows-rotate"></i> Làm mới
          </button>
        </div>
        <div class="overflow-x-auto">
          <table class="w-full text-left border-collapse text-sm">
            <thead>
              <tr
                class="bg-slate-50 border-b border-slate-200 text-xs font-semibold text-slate-500 uppercase tracking-wider"
              >
                <th class="py-3 px-4 rounded-l-xl">Thời Gian</th>
                <th class="py-3 px-4">Nội Dung Giao Dịch</th>
                <th class="py-3 px-4 text-right rounded-r-xl">Biến Động</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-slate-100">
              <tr
                v-for="history in paginatedHistoryList"
                :key="history.id"
                class="hover:bg-slate-50/50"
              >
                <td class="py-3.5 px-4 text-slate-500 text-xs">
                  {{ new Date(history.ngayTao).toLocaleString('vi-VN') }}
                </td>
                <td class="py-3.5 px-4 font-medium text-slate-800">{{ history.moTa }}</td>
                <td
                  class="py-3.5 px-4 text-right font-semibold"
                  :class="history.soXuThayDoi > 0 ? 'text-emerald-600' : 'text-rose-600'"
                >
                  {{ history.soXuThayDoi > 0 ? `+${history.soXuThayDoi}` : history.soXuThayDoi }} Xu
                </td>
              </tr>
              <tr v-if="historyList.length === 0">
                <td colspan="3" class="py-8 text-center text-slate-400 italic">
                  Chưa có lịch sử giao dịch xu nào
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- 📑 PHÂN TRANG LỊCH SỬ XU -->
        <div
          v-if="historyTotalPages > 1"
          class="flex items-center justify-between pt-2 border-t border-slate-100"
        >
          <span class="text-xs text-slate-500">
            Hiển thị {{ paginatedHistoryList.length }} / {{ historyList.length }} giao dịch
          </span>
          <div class="flex items-center gap-2">
            <button
              @click="historyCurrentPage > 1 && historyCurrentPage--"
              :disabled="historyCurrentPage === 1"
              class="px-3 py-1.5 rounded-xl border border-slate-200 text-xs font-semibold bg-white disabled:opacity-40 disabled:cursor-not-allowed hover:bg-slate-50 transition flex items-center gap-1"
            >
              <i class="fa-solid fa-chevron-left"></i> Trước
            </button>
            <span class="text-xs font-bold text-slate-700 px-1">
              {{ historyCurrentPage }} / {{ historyTotalPages }}
            </span>
            <button
              @click="historyCurrentPage < historyTotalPages && historyCurrentPage++"
              :disabled="historyCurrentPage === historyTotalPages"
              class="px-3 py-1.5 rounded-xl border border-slate-200 text-xs font-semibold bg-white disabled:opacity-40 disabled:cursor-not-allowed hover:bg-slate-50 transition flex items-center gap-1"
            >
              Sau <i class="fa-solid fa-chevron-right"></i>
            </button>
          </div>
        </div>
      </div>
    </main>

    <!-- ✨ MODAL KẾT QUẢ MINIGAME ✨ -->
    <transition name="modal">
      <div
        v-if="showRewardModal"
        class="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/60 backdrop-blur-xs p-4"
      >
        <div
          class="bg-white rounded-3xl max-w-sm w-full p-6 text-center shadow-2xl border border-slate-100 transform transition-all space-y-4"
        >
          <div
            class="w-16 h-16 bg-amber-100 text-amber-500 rounded-full flex items-center justify-center mx-auto text-3xl shadow-inner animate-bounce"
          >
            🎁
          </div>
          <div>
            <span
              class="bg-amber-50 text-amber-700 text-xs font-semibold px-3 py-1 rounded-full uppercase"
              >Chúc Mừng Bạn Trúng Thưởng</span
            >
            <h3 class="text-xl font-extrabold text-slate-900 mt-3">
              {{ rewardData.tenPhanThuong || 'Phần Quà Bí Mật' }}
            </h3>
            <p class="text-sm text-slate-500 mt-1 leading-relaxed">
              {{ rewardData.moTa || 'Phần thưởng đã được cộng trực tiếp vào ví xu của bạn.' }}
            </p>
          </div>

          <div class="bg-slate-50 p-3 rounded-2xl border border-slate-100 text-xs text-slate-600">
            Loại phần thưởng:
            <strong class="text-indigo-600">{{
              rewardData.loaiPhanThuong || 'Xu / Voucher'
            }}</strong>
          </div>

          <button
            @click="showRewardModal = false"
            class="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-semibold py-3.5 rounded-2xl transition shadow-md hover:shadow-indigo-500/30 text-sm active:scale-98"
          >
            Tuyệt vời, Nhận ngay!
          </button>
        </div>
      </div>
    </transition>

    <!-- ✨ MODAL VOUCHER CỦA TÔI ✨ -->
    <transition name="modal">
      <div
        v-if="showMyVouchersModal"
        class="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/60 backdrop-blur-xs p-4"
      >
        <div
          class="bg-white rounded-3xl max-w-2xl w-full p-6 md:p-8 shadow-2xl border border-slate-100 transform transition-all space-y-6 max-h-[90vh] flex flex-col"
        >
          <div class="flex justify-between items-center border-b border-slate-100 pb-4 shrink-0">
            <div class="flex items-center gap-3">
              <div
                class="w-10 h-10 rounded-2xl bg-indigo-50 text-indigo-600 flex items-center justify-center text-lg"
              >
                <i class="fa-solid fa-ticket"></i>
              </div>
              <div>
                <h3 class="font-extrabold text-lg text-slate-900">Voucher Của Tôi</h3>
                <p class="text-xs text-slate-500">
                  Quản lý kho voucher cá nhân và sao chép mã sử dụng
                </p>
              </div>
            </div>
            <button
              @click="showMyVouchersModal = false"
              class="w-9 h-9 rounded-full bg-slate-100 hover:bg-slate-200 text-slate-500 flex items-center justify-center transition"
            >
              <i class="fa-solid fa-xmark text-lg"></i>
            </button>
          </div>

          <!-- Bộ lọc trạng thái voucher -->
          <div class="flex items-center gap-2 overflow-x-auto pb-1 shrink-0">
            <button
              @click="myVoucherFilter = 'ALL'"
              :class="
                myVoucherFilter === 'ALL'
                  ? 'bg-indigo-600 text-white shadow-sm'
                  : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
              "
              class="px-4 py-2 rounded-xl text-xs font-bold transition whitespace-nowrap"
            >
              Tất cả ({{ myVouchers.length }})
            </button>
            <button
              @click="myVoucherFilter = 'CHUA_DUNG'"
              :class="
                myVoucherFilter === 'CHUA_DUNG'
                  ? 'bg-indigo-600 text-white shadow-sm'
                  : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
              "
              class="px-4 py-2 rounded-xl text-xs font-bold transition whitespace-nowrap"
            >
              Chưa dùng ({{ unusedVouchersCount }})
            </button>
            <button
              @click="myVoucherFilter = 'DA_DUNG'"
              :class="
                myVoucherFilter === 'DA_DUNG'
                  ? 'bg-indigo-600 text-white shadow-sm'
                  : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
              "
              class="px-4 py-2 rounded-xl text-xs font-bold transition whitespace-nowrap"
            >
              Đã dùng
            </button>
          </div>

          <!-- Danh sách voucher chi tiết -->
          <div class="space-y-4 overflow-y-auto pr-2 grow">
            <div
              v-for="item in filteredMyVouchers"
              :key="item.idVoucherKhachHang"
              class="bg-gradient-to-r from-slate-50 to-indigo-50/30 border border-slate-200/85 rounded-2xl p-5 flex flex-col md:flex-row justify-between items-start md:items-center gap-4 relative overflow-hidden group hover:shadow-md transition"
            >
              <div class="absolute left-0 top-0 bottom-0 w-2 bg-indigo-600"></div>

              <div class="space-y-2 pl-2">
                <div class="flex items-center gap-2 flex-wrap">
                  <span
                    class="bg-indigo-600 text-white text-xs font-extrabold px-2.5 py-0.5 rounded-md shadow-xs"
                  >
                    {{ item.hienThiGiaTriGiam || 'Voucher ưu đãi' }}
                  </span>
                  <span
                    v-if="item.sapHetHan"
                    class="bg-rose-100 text-rose-700 text-[10px] font-bold px-2.5 py-0.5 rounded-md animate-pulse"
                  >
                    Sắp hết hạn (Còn {{ item.soNgayConLai }} ngày)
                  </span>
                  <span
                    v-else
                    class="bg-emerald-100 text-emerald-700 text-[10px] font-bold px-2.5 py-0.5 rounded-md"
                  >
                    Còn {{ item.soNgayConLai }} ngày
                  </span>
                </div>

                <div>
                  <h4 class="font-bold text-slate-900 text-base">{{ item.tenVoucher }}</h4>
                  <p class="text-xs text-slate-500 mt-0.5">
                    Đơn tối thiểu:
                    <strong class="text-slate-700"
                      >{{ Number(item.dieuKienToiThieu || 0).toLocaleString('vi-VN') }}đ</strong
                    >
                  </p>
                </div>

                <div class="flex items-center gap-3 text-xs text-slate-400 pt-1">
                  <span>Đổi ngày: {{ new Date(item.ngayDoi).toLocaleString('vi-VN') }}</span>
                  <span>•</span>
                  <span>Hạn: {{ new Date(item.ngayHetHan).toLocaleDateString('vi-VN') }}</span>
                </div>
              </div>

              <div
                class="flex flex-col items-end gap-2.5 w-full md:w-auto shrink-0 border-t md:border-t-0 pt-3 md:pt-0 border-slate-200"
              >
                <div
                  class="flex items-center gap-2 bg-white border border-slate-200 px-3 py-1.5 rounded-xl shadow-xs w-full md:w-auto justify-between"
                >
                  <span class="font-mono font-bold text-indigo-600 text-xs">{{ item.maCode }}</span>
                  <button
                    @click="copyCode(item.maCode)"
                    class="text-slate-400 hover:text-indigo-600 text-xs transition p-1"
                    title="Sao chép mã"
                  >
                    <i class="fa-regular fa-copy"></i>
                  </button>
                </div>

                <span
                  class="px-3 py-1 text-xs font-bold rounded-xl text-center w-full md:w-auto"
                  :class="
                    item.trangThai === 'CHUA_DUNG'
                      ? 'bg-emerald-50 text-emerald-700 border border-emerald-200'
                      : 'bg-slate-200 text-slate-600'
                  "
                >
                  {{ item.trangThai === 'CHUA_DUNG' ? 'Chưa sử dụng' : item.trangThai }}
                </span>
              </div>
            </div>

            <div
              v-if="filteredMyVouchers.length === 0"
              class="text-center py-12 text-slate-400 text-sm italic bg-slate-50/50 rounded-2xl border border-dashed border-slate-200"
            >
              Không tìm thấy voucher nào trong danh mục này.
            </div>
          </div>

          <div class="pt-2 border-t border-slate-100 shrink-0">
            <button
              @click="showMyVouchersModal = false"
              class="w-full bg-slate-100 hover:bg-slate-200 text-slate-700 font-semibold py-3.5 rounded-2xl transition text-sm"
            >
              Đóng
            </button>
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

// Lấy ID khách hàng từ sessionStorage (hoặc fallback mặc định = 1)
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
const checkinConfigs = ref([])

// Phân trang & sắp xếp kho voucher
const sortBy = ref('asc')
const currentPage = ref(1)
const itemsPerPage = ref(6)
const myVoucherFilter = ref('CHUA_DUNG')
const unusedVouchersCount = computed(() => {
  return myVouchers.value.filter((item) => item.trangThai === 'CHUA_DUNG').length
})

// Phân trang lịch sử xu
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

// Lấy cấu hình điểm danh
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

// Tính xu thưởng ngày tiếp theo
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

// Computed kho voucher
const sortedKhoVouchers = computed(() => {
  let list = [...khoVouchers.value]
  list.sort((a, b) => {
    if (sortBy.value === 'asc') {
      return (a.soXuDoi || 0) - (b.soXuDoi || 0)
    } else {
      return (b.soXuDoi || 0) - (a.soXuDoi || 0)
    }
  })
  return list
})

const totalPages = computed(() => {
  return Math.ceil(sortedKhoVouchers.value.length / itemsPerPage.value) || 1
})

const paginatedKhoVouchers = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage.value
  const end = start + itemsPerPage.value
  return sortedKhoVouchers.value.slice(start, end)
})

// Computed lịch sử xu phân trang
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

const handleDoiVoucher = async (idKhoVoucher) => {
  if (!confirm('Bạn có chắc chắn muốn dùng xu để đổi voucher này không?')) return
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
          tenPhanThuong: `Thẻ #${cardIndex}: ` + (rewardPayload.tenPhanThuong || 'Quà đặc biệt'),
          moTa: rewardPayload.moTa,
          loaiPhanThuong: rewardPayload.loaiPhanThuong,
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
  transform: translateY(-20px);
}

.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.25s ease;
}
.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}
</style>
