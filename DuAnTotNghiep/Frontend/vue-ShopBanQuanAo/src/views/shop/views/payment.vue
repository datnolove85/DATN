<template>
  <div class="payment-page min-h-screen bg-[#f7f4ee] text-slate-900">
    <div class="pointer-events-none fixed inset-0 overflow-hidden">
      <div class="absolute -left-24 top-20 h-80 w-80 rounded-full bg-amber-200/40 blur-3xl"></div>
      <div class="absolute -right-24 top-0 h-96 w-96 rounded-full bg-amber-200/30 blur-3xl"></div>
      <div class="absolute bottom-0 left-1/3 h-72 w-72 rounded-full bg-rose-100/30 blur-3xl"></div>
    </div>

    <div class="relative mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 lg:py-10">
      <!-- TOP BAR -->
      <div class="mb-6 flex flex-wrap items-center justify-between gap-4">
        <button
          type="button"
          class="group inline-flex items-center gap-2 text-sm font-semibold text-slate-600 transition hover:text-rose-900"
          @click="router.push('/san-pham')"
        >
          <span
            class="grid h-9 w-9 place-items-center rounded-full border border-slate-200 bg-white shadow-sm transition group-hover:border-amber-200 group-hover:bg-amber-50"
          >
            <ArrowLeft :size="17" />
          </span>
          Tiếp tục mua sắm
        </button>

        <RouterLink
          to="/tra-cuu-don-hang"
          class="inline-flex items-center gap-2 rounded-full border border-slate-200 bg-white px-4 py-2 text-sm font-semibold text-slate-700 shadow-sm transition hover:border-amber-200 hover:text-rose-900"
        >
          <Search :size="16" />
          Tra cứu đơn hàng
        </RouterLink>
      </div>

      <!-- HERO -->
      <section
        class="relative overflow-hidden rounded-[28px] border border-white/70 bg-[#201914] px-6 py-7 text-white shadow-2xl shadow-slate-300/60 sm:px-8 lg:px-10 lg:py-9"
      >
        <div
          class="absolute -right-20 -top-24 h-72 w-72 rounded-full bg-amber-500/30 blur-2xl"
        ></div>
        <div
          class="absolute -bottom-28 left-1/3 h-64 w-64 rounded-full bg-amber-400/20 blur-2xl"
        ></div>

        <div class="relative grid gap-7 lg:grid-cols-[1fr_auto] lg:items-center">
          <div>
            <div
              class="mb-4 inline-flex items-center gap-2 rounded-full border border-white/15 bg-white/10 px-3 py-1.5 text-xs font-bold uppercase tracking-[0.18em] text-amber-100"
            >
              <Lock :size="14" />
              Thanh toán an toàn
            </div>

            <h1 class="max-w-2xl text-3xl font-black tracking-tight sm:text-4xl lg:text-5xl">
              Hoàn tất đơn hàng của bạn
            </h1>

            <p class="mt-3 max-w-xl text-sm leading-6 text-slate-300 sm:text-base">
              Kiểm tra lại đơn hàng, chọn phương thức phù hợp và xác nhận thanh toán.
            </p>
          </div>

          <div
            class="min-w-[250px] rounded-2xl border border-white/15 bg-white/10 p-4 backdrop-blur"
          >
            <p class="text-xs font-bold uppercase tracking-[0.16em] text-slate-300">Mã đơn hàng</p>

            <div class="mt-2 flex items-center justify-between gap-3">
              <span class="break-all text-lg font-black text-white sm:text-xl">
                {{ orderCode || 'Đang cập nhật' }}
              </span>

              <button
                v-if="orderCode"
                type="button"
                class="grid h-10 w-10 shrink-0 place-items-center rounded-xl border border-white/15 bg-white/10 transition hover:bg-white/20"
                title="Sao chép mã đơn"
                @click="copyOrderCode"
              >
                <Check v-if="copied" :size="18" />
                <Copy v-else :size="18" />
              </button>
            </div>

            <div
              class="mt-3 flex items-center gap-2 text-xs"
              :class="orderCancelled ? 'text-red-300' : 'text-emerald-300'"
            >
              <span
                class="h-2 w-2 rounded-full"
                :class="orderCancelled ? 'bg-red-400' : 'bg-emerald-400'"
              ></span>
              {{ orderCancelled ? 'Đơn hàng đã bị hủy' : 'Đơn hàng đã được tạo thành công' }}
            </div>
          </div>
        </div>
      </section>

      <!-- CHECKOUT STEPS -->
      <div
        class="mx-auto mt-5 grid max-w-3xl grid-cols-3 rounded-2xl border border-slate-200/80 bg-white/90 p-2 shadow-sm backdrop-blur"
      >
        <div
          class="flex items-center justify-center gap-2 rounded-xl px-2 py-3 text-xs font-bold text-emerald-700 sm:text-sm"
        >
          <span class="grid h-6 w-6 place-items-center rounded-full bg-emerald-100">
            <Check :size="14" />
          </span>
          <span class="hidden sm:inline">Thông tin</span>
        </div>

        <div
          class="flex items-center justify-center gap-2 rounded-xl bg-amber-50 px-2 py-3 text-xs font-bold text-rose-900 sm:text-sm"
        >
          <span class="grid h-6 w-6 place-items-center rounded-full bg-rose-800 text-white">2</span>
          Thanh toán
        </div>

        <div
          class="flex items-center justify-center gap-2 rounded-xl px-2 py-3 text-xs font-bold text-slate-400 sm:text-sm"
        >
          <span class="grid h-6 w-6 place-items-center rounded-full bg-slate-100">3</span>
          <span class="hidden sm:inline">Hoàn tất</span>
        </div>
      </div>

      <!-- LOADING -->
      <div
        v-if="loading"
        class="mt-8 grid gap-6 lg:grid-cols-[minmax(0,1.45fr)_minmax(340px,0.75fr)]"
      >
        <div class="space-y-6">
          <div class="h-72 animate-pulse rounded-[28px] border border-slate-200 bg-white"></div>
          <div class="h-80 animate-pulse rounded-[28px] border border-slate-200 bg-white"></div>
        </div>
        <div class="h-[460px] animate-pulse rounded-[28px] border border-slate-200 bg-white"></div>
      </div>

      <!-- LOAD ERROR -->
      <div
        v-else-if="loadError"
        class="mx-auto mt-8 max-w-2xl rounded-[28px] border border-red-100 bg-white p-8 text-center shadow-xl shadow-slate-200/60"
      >
        <div class="mx-auto grid h-16 w-16 place-items-center rounded-2xl bg-red-50 text-red-600">
          <X :size="30" />
        </div>
        <h2 class="mt-5 text-2xl font-black">Không thể tải đơn hàng</h2>
        <p class="mt-2 text-slate-500">{{ loadError }}</p>
        <button
          type="button"
          class="mt-6 rounded-xl bg-slate-900 px-6 py-3 font-bold text-white transition hover:bg-rose-900"
          @click="router.push('/san-pham')"
        >
          Quay lại cửa hàng
        </button>
      </div>

      <!-- MAIN CONTENT -->
      <div
        v-else-if="orderInfo"
        class="mt-8 grid items-start gap-6 lg:grid-cols-[minmax(0,1.45fr)_minmax(340px,0.75fr)]"
      >
        <div class="space-y-6">
          <!-- PAYMENT METHOD -->
          <section
            class="rounded-[28px] border border-slate-200/80 bg-white p-5 shadow-xl shadow-slate-200/40 sm:p-7"
          >
            <div class="flex items-start justify-between gap-4">
              <div>
                <p class="text-xs font-black uppercase tracking-[0.16em] text-rose-800">Bước 1</p>
                <h2 class="mt-1 text-2xl font-black tracking-tight text-slate-900">
                  Chọn phương thức thanh toán
                </h2>
                <p class="mt-2 text-sm text-slate-500">
                  Bạn có thể thanh toán khi nhận hàng hoặc chuyển khoản qua VietQR.
                </p>
              </div>
              <div
                class="hidden h-12 w-12 place-items-center rounded-2xl bg-amber-50 text-rose-800 sm:grid"
              >
                <CreditCard :size="24" />
              </div>
            </div>

            <div class="mt-6 grid gap-4 md:grid-cols-3">
              <!-- Nút COD -->
              <button
                type="button"
                :disabled="qrStarted || orderCancelled"
                class="payment-option group relative overflow-hidden rounded-2xl border-2 p-5 text-left transition disabled:cursor-not-allowed disabled:opacity-55"
                :class="
                  paymentMethod === 'COD'
                    ? 'border-rose-800 bg-amber-50/70 shadow-lg shadow-indigo-100'
                    : 'border-slate-200 bg-white hover:border-amber-200 hover:bg-[#f7f4ee]'
                "
                @click="paymentMethod = 'COD'"
              >
                <div class="flex items-start gap-4">
                  <span
                    class="grid h-12 w-12 shrink-0 place-items-center rounded-2xl bg-amber-50 text-amber-600"
                    ><Truck :size="23"
                  /></span>
                  <span class="min-w-0 flex-1">
                    <span class="block font-black text-slate-900">COD</span>
                    <span class="mt-1 block text-sm leading-5 text-slate-500"
                      >Thanh toán khi nhận hàng.</span
                    >
                  </span>
                </div>
              </button>

              <!-- Nút BANK (VietQR) -->
              <button
                type="button"
                :disabled="orderCancelled"
                class="payment-option group relative overflow-hidden rounded-2xl border-2 p-5 text-left transition disabled:cursor-not-allowed disabled:opacity-55"
                :class="
                  paymentMethod === 'BANK'
                    ? 'border-rose-800 bg-amber-50/70 shadow-lg shadow-indigo-100'
                    : 'border-slate-200 bg-white hover:border-amber-200 hover:bg-[#f7f4ee]'
                "
                @click="paymentMethod = 'BANK'"
              >
                <div class="flex items-start gap-4">
                  <span
                    class="grid h-12 w-12 shrink-0 place-items-center rounded-2xl bg-sky-50 text-sky-600"
                    ><Landmark :size="23"
                  /></span>
                  <span class="min-w-0 flex-1">
                    <span class="block font-black text-slate-900">VietQR</span>
                    <span class="mt-1 block text-sm leading-5 text-slate-500"
                      >Quét mã chuyển khoản nhanh.</span
                    >
                  </span>
                </div>
              </button>

              <!-- VNPAY -->
              <button
                type="button"
                :disabled="orderCancelled"
                class="payment-option group relative overflow-hidden rounded-2xl border-2 p-5 text-left transition disabled:cursor-not-allowed disabled:opacity-55"
                :class="
                  paymentMethod === 'VNPAY'
                    ? 'border-rose-800 bg-amber-50/70 shadow-lg shadow-indigo-100'
                    : 'border-slate-200 bg-white hover:border-amber-200 hover:bg-[#f7f4ee]'
                "
                @click="paymentMethod = 'VNPAY'"
              >
                <div class="flex items-start gap-4">
                  <span
                    class="grid h-12 w-12 shrink-0 place-items-center rounded-2xl bg-blue-50 text-blue-600"
                    ><CreditCard :size="23"
                  /></span>
                  <span class="min-w-0 flex-1">
                    <span class="block font-black text-slate-900">VNPAY</span>
                    <span class="mt-1 block text-sm leading-5 text-slate-500"
                      >Thẻ ATM / QR Pay ngân hàng.</span
                    >
                  </span>
                </div>
              </button>
            </div>

            <div
              v-if="qrStarted && !qrExpired"
              class="mt-4 flex items-start gap-3 rounded-2xl border border-amber-100 bg-amber-50 p-4 text-sm text-indigo-800"
            >
              <Lock :size="18" class="mt-0.5 shrink-0" />
              <p>
                Mã QR đang hoạt động nên phương thức thanh toán được khóa ở chuyển khoản ngân hàng.
              </p>
            </div>
          </section>

          <!-- QR PAYMENT -->
          <Transition name="slide-fade">
            <section
              v-if="paymentMethod === 'BANK'"
              class="rounded-[28px] border border-amber-100 bg-white p-5 shadow-xl shadow-indigo-100/50 sm:p-7"
            >
              <div class="flex flex-wrap items-start justify-between gap-4">
                <div>
                  <p class="text-xs font-black uppercase tracking-[0.16em] text-rose-800">Bước 2</p>
                  <h2 class="mt-1 text-2xl font-black tracking-tight">Thanh toán bằng VietQR</h2>
                  <p class="mt-2 text-sm text-slate-500">
                    Mã QR có thời hạn theo thời gian giữ đơn của hệ thống. (Đang giả lập tự động
                    thanh toán sau 10s)
                  </p>
                </div>

                <span
                  class="inline-flex items-center gap-2 rounded-full bg-sky-50 px-3 py-1.5 text-xs font-black text-sky-700"
                >
                  <QrCode :size="15" />
                  VIETQR
                </span>
              </div>

              <!-- CREATE QR -->
              <div
                v-if="!qrStarted && !qrExpired"
                class="mt-7 overflow-hidden rounded-[26px] border border-dashed border-amber-200 bg-gradient-to-br from-indigo-50 via-white to-sky-50 p-6 sm:p-8"
              >
                <div class="mx-auto max-w-xl text-center">
                  <span
                    class="mx-auto grid h-16 w-16 place-items-center rounded-2xl bg-rose-800 text-white shadow-lg shadow-rose-200"
                  >
                    <QrCode :size="31" />
                  </span>
                  <h3 class="mt-5 text-2xl font-black text-slate-900">Tạo mã QR thanh toán</h3>
                  <p class="mt-2 text-sm leading-6 text-slate-500">
                    Sau khi tạo, đồng hồ đếm ngược sẽ hiển thị. Hệ thống sẽ tự động giả lập thanh
                    toán thành công sau 10 giây.
                  </p>

                  <div
                    class="mx-auto mt-5 flex max-w-sm items-center justify-center gap-3 rounded-2xl border border-amber-100 bg-amber-50 px-4 py-3 text-sm font-bold text-amber-800"
                  >
                    <Clock3 :size="18" />
                    Thời gian giữ đơn tối đa: {{ QR_VALIDITY_MINUTES }} phút
                  </div>

                  <button
                    type="button"
                    :disabled="!qrUrl || orderCancelled"
                    class="mt-6 inline-flex items-center justify-center gap-2 rounded-2xl bg-rose-800 px-7 py-3.5 font-black text-white shadow-lg shadow-rose-200 transition hover:-translate-y-0.5 hover:bg-rose-900 disabled:cursor-not-allowed disabled:opacity-55 disabled:hover:translate-y-0"
                    @click="createQr"
                  >
                    <QrCode :size="20" />
                    Tạo mã QR
                  </button>
                </div>
              </div>

              <!-- QR EXPIRED -->
              <div
                v-else-if="qrExpired || orderCancelled"
                class="mt-7 rounded-[26px] border border-red-100 bg-red-50 p-7 text-center"
              >
                <span
                  class="mx-auto grid h-16 w-16 place-items-center rounded-2xl bg-white text-red-600 shadow-sm"
                >
                  <TimerOff :size="31" />
                </span>
                <h3 class="mt-4 text-2xl font-black text-red-700">Mã QR đã hết hạn</h3>
                <p class="mx-auto mt-2 max-w-lg text-sm leading-6 text-red-700/75">
                  Đơn hàng đã được hủy tự động. Bạn cần tạo đơn mới nếu vẫn muốn mua sản phẩm.
                </p>
                <button
                  type="button"
                  class="mt-5 rounded-xl bg-slate-900 px-6 py-3 font-bold text-white transition hover:bg-rose-900"
                  @click="router.push('/san-pham')"
                >
                  Quay lại mua sắm
                </button>
              </div>

              <!-- ACTIVE QR -->
              <div v-else class="mt-7">
                <div
                  class="mb-6 grid gap-4 rounded-[24px] border border-red-100 bg-gradient-to-r from-red-50 to-amber-50 p-5 sm:grid-cols-[1fr_auto] sm:items-center"
                >
                  <div class="flex items-start gap-3">
                    <span
                      class="grid h-11 w-11 shrink-0 place-items-center rounded-2xl bg-white text-red-600 shadow-sm"
                    >
                      <Clock3 :size="23" />
                    </span>
                    <div>
                      <p class="text-xs font-black uppercase tracking-[0.15em] text-red-500">
                        Đang chờ thanh toán tự động (10s)
                      </p>
                      <p class="mt-1 text-sm text-slate-600">
                        Hệ thống đang giả lập quét mã thành công...
                      </p>
                    </div>
                  </div>

                  <div class="text-left sm:text-right">
                    <div class="font-mono text-4xl font-black tabular-nums text-red-600">
                      {{ countdownText }}
                    </div>
                    <p class="mt-1 text-xs font-semibold text-red-500">Đơn tự hủy khi hết giờ</p>
                  </div>
                </div>

                <div class="grid gap-7 md:grid-cols-[280px_1fr] md:items-center">
                  <div class="mx-auto w-full max-w-[280px]">
                    <div
                      class="rounded-[26px] border border-slate-200 bg-white p-4 shadow-lg shadow-slate-200/70"
                    >
                      <img
                        v-if="qrUrl"
                        :src="qrUrl"
                        alt="Mã QR thanh toán"
                        class="aspect-square w-full rounded-2xl object-contain"
                      />
                    </div>
                    <p class="mt-3 text-center text-xs text-slate-400">
                      Dùng ứng dụng ngân hàng hỗ trợ VietQR
                    </p>
                  </div>

                  <div class="space-y-4">
                    <div class="rounded-2xl border border-slate-200 bg-[#f7f4ee] p-4">
                      <p class="text-xs font-bold uppercase tracking-wider text-slate-400">
                        Nội dung chuyển khoản
                      </p>
                      <div class="mt-2 flex items-center justify-between gap-3">
                        <span class="break-all text-xl font-black text-rose-900">{{
                          orderCode
                        }}</span>
                        <button
                          type="button"
                          class="grid h-9 w-9 shrink-0 place-items-center rounded-xl border border-slate-200 bg-white text-slate-600 transition hover:border-amber-200 hover:text-rose-900"
                          title="Sao chép nội dung"
                          @click="copyOrderCode"
                        >
                          <Check v-if="copied" :size="17" />
                          <Copy v-else :size="17" />
                        </button>
                      </div>
                    </div>

                    <div class="rounded-2xl border border-red-100 bg-red-50 p-4">
                      <p class="text-xs font-bold uppercase tracking-wider text-red-400">
                        Số tiền cần chuyển
                      </p>
                      <p class="mt-1 text-3xl font-black text-red-600">
                        {{ formatMoney(orderInfo.tongThanhToan) }}
                      </p>
                    </div>

                    <div
                      class="rounded-2xl border border-amber-100 bg-amber-50 p-4 text-sm leading-6 text-amber-900"
                    >
                      <p class="font-black">Lưu ý khi chuyển khoản</p>
                      <ul class="mt-2 space-y-1.5">
                        <li class="flex gap-2">
                          <Check :size="16" class="mt-1 shrink-0" /> Chuyển đúng số tiền hiển thị.
                        </li>
                        <li class="flex gap-2">
                          <Check :size="16" class="mt-1 shrink-0" /> Giữ nguyên nội dung là mã đơn
                          hàng.
                        </li>
                        <li class="flex gap-2">
                          <Check :size="16" class="mt-1 shrink-0" /> Hệ thống sẽ tự động xác nhận
                          sau 10 giây (Giả lập).
                        </li>
                      </ul>
                    </div>
                  </div>
                </div>
              </div>
            </section>
          </Transition>

          <!-- PRODUCTS -->
          <section
            class="rounded-[28px] border border-slate-200/80 bg-white p-5 shadow-xl shadow-slate-200/40 sm:p-7"
          >
            <div class="flex items-center justify-between gap-4">
              <div>
                <p class="text-xs font-black uppercase tracking-[0.16em] text-rose-800">Đơn hàng</p>
                <h2 class="mt-1 text-2xl font-black tracking-tight">Sản phẩm đã chọn</h2>
              </div>
              <span class="rounded-full bg-amber-50 px-3 py-1.5 text-xs font-black text-rose-900">
                {{ orderItems.length }} sản phẩm
              </span>
            </div>

            <div class="mt-6 space-y-4">
              <article
                v-for="item in orderItems"
                :key="item.id"
                class="grid gap-5 rounded-2xl border border-slate-200/80 bg-white p-4 shadow-sm transition-all hover:shadow-md sm:grid-cols-[132px_1fr] sm:p-5"
              >
                <!-- Ảnh sản phẩm -->
                <div
                  class="h-32 w-32 flex-shrink-0 overflow-hidden rounded-2xl border border-slate-100 bg-[#f7f4ee] shadow-inner"
                >
                  <img
                    :src="
                      item.anh
                        ? item.anh.startsWith('http')
                          ? item.anh
                          : 'http://localhost:8080' + item.anh
                        : '/no-image.png'
                    "
                    :alt="item.tenSanPham || 'Sản phẩm'"
                    class="h-full w-full object-cover"
                  />
                </div>

                <!-- Thông tin sản phẩm -->
                <div class="flex min-w-0 flex-1 flex-col justify-between">
                  <div>
                    <!-- Tên & Mã SPCT -->
                    <div class="flex items-start justify-between gap-4">
                      <h3 class="line-clamp-2 text-lg font-bold text-slate-900">
                        {{ item.tenSanPham }}
                      </h3>
                      <span v-if="item.maSanPhamChiTiet" class="text-xs font-medium text-slate-400">
                        #{{ item.maSanPhamChiTiet }}
                      </span>
                    </div>

                    <!-- Thuộc tính (Màu sắc, Kích cỡ) -->
                    <div class="mt-2.5 flex flex-wrap gap-2">
                      <span
                        v-if="item.tenMauSac"
                        class="inline-flex items-center gap-1 rounded-full bg-slate-100 px-3 py-1 text-xs font-semibold text-slate-700"
                      >
                        🎨 {{ item.tenMauSac }}
                      </span>

                      <span
                        v-if="item.tenKichThuoc"
                        class="inline-flex items-center gap-1 rounded-full bg-slate-100 px-3 py-1 text-xs font-semibold text-slate-700"
                      >
                        📏 {{ item.tenKichThuoc }}
                      </span>
                    </div>
                  </div>

                  <!-- Giá, Số lượng & Thành tiền -->
                  <div
                    class="mt-4 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between"
                  >
                    <!-- Đơn giá & Số lượng -->
                    <div class="flex items-center gap-4">
                      <div>
                        <p class="text-xs uppercase tracking-wider text-slate-400">Đơn giá</p>
                        <span class="text-base font-bold text-slate-900">
                          {{ formatMoney(item.donGia || item.thanhTien / item.soLuong) }}
                        </span>
                      </div>

                      <div class="h-6 w-px bg-slate-200"></div>

                      <div>
                        <p class="text-xs uppercase tracking-wider text-slate-400">Số lượng</p>
                        <span class="text-base font-bold text-slate-900">
                          x{{ item.soLuong }}
                        </span>
                      </div>
                    </div>

                    <!-- Thành tiền -->
                    <div
                      class="flex items-center justify-between border-t border-dashed border-slate-200 pt-3 sm:border-t-0 sm:pt-0 sm:justify-end sm:gap-2"
                    >
                      <span class="text-sm text-slate-500">Thành tiền:</span>
                      <span class="text-xl font-extrabold text-red-600">
                        {{ formatMoney(item.thanhTien) }}
                      </span>
                    </div>
                  </div>
                </div>
              </article>
            </div>
          </section>
        </div>

        <!-- SUMMARY SIDEBAR -->
        <aside class="space-y-5 lg:sticky lg:top-6">
          <section
            class="overflow-hidden rounded-[28px] border border-slate-200/80 bg-white shadow-2xl shadow-slate-200/60"
          >
            <div class="border-b border-slate-100 px-6 py-5">
              <div class="flex items-center gap-3">
                <span class="grid h-11 w-11 place-items-center rounded-2xl bg-slate-900 text-white">
                  <ReceiptText :size="22" />
                </span>
                <div>
                  <h2 class="text-xl font-black">Tóm tắt thanh toán</h2>
                  <p class="text-xs text-slate-400">Mã đơn: {{ orderCode }}</p>
                </div>
              </div>
            </div>

            <div class="space-y-4 px-6 py-5 text-sm">
              <div class="flex items-center justify-between gap-4">
                <span class="text-slate-500">Tiền hàng</span>
                <span class="font-bold text-slate-800">{{
                  formatMoney(orderInfo.tongTienHang)
                }}</span>
              </div>

              <div class="flex items-center justify-between gap-4">
                <span class="text-slate-500">Giảm giá</span>
                <span class="font-bold text-emerald-600"
                  >-{{ formatMoney(orderInfo.tongGiamGia) }}</span
                >
              </div>

              <div class="flex items-center justify-between gap-4">
                <span class="text-slate-500">Phí vận chuyển</span>
                <span class="font-bold text-slate-800">{{
                  formatMoney(orderInfo.phiVanChuyen)
                }}</span>
              </div>

              <div class="border-t border-dashed border-slate-200 pt-4">
                <div class="flex items-end justify-between gap-4">
                  <span class="font-black text-slate-900">Tổng thanh toán</span>
                  <span class="text-right text-2xl font-black text-rose-900">
                    {{ formatMoney(orderInfo.tongThanhToan) }}
                  </span>
                </div>
                <p class="mt-2 text-right text-xs text-slate-400">Đã bao gồm phí vận chuyển</p>
              </div>
            </div>

            <div class="border-t border-slate-100 bg-[#f7f4ee]/80 p-5">
              <button
                type="button"
                :disabled="payDisabled"
                class="flex w-full items-center justify-center gap-2 rounded-2xl bg-rose-800 px-5 py-4 text-base font-black text-white shadow-lg shadow-rose-200 transition hover:-translate-y-0.5 hover:bg-rose-900 disabled:cursor-not-allowed disabled:opacity-60 disabled:hover:translate-y-0"
                @click="pay"
              >
                <LoaderCircle v-if="isPaying" :size="20" class="animate-spin" />
                <PackageCheck v-else-if="paymentMethod === 'COD'" :size="20" />
                <CreditCard v-else :size="20" />
                {{ payButtonLabel }}
              </button>

              <p
                v-if="paymentMethod === 'BANK' && !qrStarted && !qrExpired"
                class="mt-2 text-center text-xs text-amber-600"
              >
                Hãy tạo mã QR trước khi xác nhận chuyển khoản.
              </p>

              <button
                type="button"
                :disabled="isPaying || isCancelling || isExpiring || orderCancelled"
                class="mt-3 flex w-full items-center justify-center gap-2 rounded-2xl border border-slate-200 bg-white px-5 py-3.5 font-bold text-slate-600 transition hover:border-red-200 hover:bg-red-50 hover:text-red-600 disabled:cursor-not-allowed disabled:opacity-60"
                @click="handleCancel"
              >
                <LoaderCircle v-if="isCancelling" :size="18" class="animate-spin" />
                <X v-else :size="18" />
                {{ isCancelling ? 'Đang hủy đơn...' : 'Hủy đơn hàng' }}
              </button>
            </div>
          </section>

          <section class="rounded-2xl border border-emerald-100 bg-emerald-50/80 p-5">
            <div class="flex gap-3">
              <span
                class="grid h-10 w-10 shrink-0 place-items-center rounded-xl bg-white text-emerald-600 shadow-sm"
              >
                <ShieldCheck :size="21" />
              </span>
              <div>
                <h3 class="font-black text-emerald-900">Thông tin đơn hàng được bảo vệ</h3>
                <p class="mt-1 text-sm leading-5 text-emerald-800/75">
                  Hãy lưu mã đơn để theo dõi trạng thái bằng số điện thoại nhận hàng.
                </p>
              </div>
            </div>
          </section>

          <RouterLink
            to="/donhang"
            class="flex items-center justify-between rounded-2xl border border-slate-200 bg-white p-5 text-slate-700 shadow-sm transition hover:border-amber-200 hover:text-rose-900"
          >
            <span class="flex items-center gap-3 font-bold">
              <ShoppingBag :size="20" />
              Tra cứu đơn sau khi đặt
            </span>
            <ArrowLeft :size="18" class="rotate-180" />
          </RouterLink>
        </aside>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  ArrowLeft,
  Check,
  Clock3,
  Copy,
  CreditCard,
  Landmark,
  LoaderCircle,
  Lock,
  PackageCheck,
  QrCode,
  ReceiptText,
  Search,
  ShieldCheck,
  ShoppingBag,
  TimerOff,
  Truck,
  X,
} from 'lucide-vue-next'
import { huyHoaDonOnline } from '@/service/HoaDonService'
import { useToast } from 'vue-toastification'
import { thanhToan } from '@/service/PaymentService'

const QR_VALIDITY_MINUTES = 15
const QR_VALIDITY_SECONDS = QR_VALIDITY_MINUTES * 60

const toast = useToast()
const route = useRoute()
const router = useRouter()

const orderId = route.query.id
const qrUrl = route.query.qrUrl

const loading = ref(true)
const loadError = ref('')
const isPaying = ref(false)
const isCancelling = ref(false)
const isExpiring = ref(false)
const copied = ref(false)

const orderItems = ref([])
const orderInfo = ref(null)
const paymentMethod = ref('COD')

const qrStarted = ref(false)
const qrExpired = ref(false)
const remainingSeconds = ref(QR_VALIDITY_SECONDS)
let countdownTimer = null
let simulatedPaymentTimer = null // Timer giả lập thanh toán sau 10s

const qrStorageKey = computed(() => `payment-qr-started-${orderId || 'unknown'}`)
const orderCode = computed(() => orderInfo.value?.maHoaDon || route.query.maHoaDon || '')
const orderCancelled = computed(() => qrExpired.value || orderInfo.value?.trangThai === 'da_huy')

const payButtonLabel = computed(() => {
  if (isPaying.value) return 'Đang xử lý...'
  if (orderCancelled.value) return 'Đơn hàng đã bị hủy'

  switch (paymentMethod.value) {
    case 'COD':
      return 'Xác nhận đặt hàng'
    case 'VNPAY':
      return 'Thanh toán qua VNPay'
    case 'BANK':
      return 'Quét mã VietQR chuyển khoản'
    default:
      return 'Chọn phương thức thanh toán!'
  }
})

const payDisabled = computed(() => {
  if (isPaying.value || isCancelling.value || isExpiring.value || orderCancelled.value) return true
  if (paymentMethod.value === 'BANK' && !qrStarted.value) return true
  return false
})

const countdownText = computed(() => {
  const minutes = Math.floor(remainingSeconds.value / 60)
  const seconds = remainingSeconds.value % 60

  return `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`
})

const expirationTimestamp = computed(() => {
  const source = orderInfo.value?.ngayCapNhat || orderInfo.value?.ngayTao
  if (!source) return null

  const createdAt = new Date(source).getTime()
  if (Number.isNaN(createdAt)) return null

  return createdAt + QR_VALIDITY_SECONDS * 1000
})

const formatMoney = (money) => Number(money || 0).toLocaleString('vi-VN') + ' đ'

const clearSimulatedPayment = () => {
  if (simulatedPaymentTimer) {
    window.clearTimeout(simulatedPaymentTimer)
    simulatedPaymentTimer = null
  }
}

const clearCountdown = () => {
  if (countdownTimer) {
    window.clearInterval(countdownTimer)
    countdownTimer = null
  }
  clearSimulatedPayment()
}

const clearQrSession = () => {
  clearCountdown()
  sessionStorage.removeItem(qrStorageKey.value)
}

const copyOrderCode = async () => {
  if (!orderCode.value) return

  try {
    await navigator.clipboard.writeText(orderCode.value)
    copied.value = true
    toast.success('Đã sao chép mã đơn hàng')

    window.setTimeout(() => {
      copied.value = false
    }, 1600)
  } catch (error) {
    console.error(error)
    toast.error('Không thể sao chép mã đơn hàng')
  }
}

const expireOrder = async () => {
  if (isExpiring.value || orderCancelled.value) return

  isExpiring.value = true
  qrExpired.value = true
  remainingSeconds.value = 0
  clearQrSession()

  try {
    await huyHoaDonOnline(Number(orderId))
  } catch (error) {
    console.warn('Đơn có thể đã được hệ thống hủy trước:', error)
  } finally {
    if (orderInfo.value) {
      orderInfo.value.trangThai = 'da_huy'
    }

    isExpiring.value = false
    toast.warning('Mã QR đã hết hạn. Đơn hàng đã được hủy tự động.')
  }
}

const syncCountdown = () => {
  if (!expirationTimestamp.value) {
    loadError.value = 'Không xác định được thời hạn giữ đơn'
    clearCountdown()
    return
  }

  const seconds = Math.max(0, Math.ceil((expirationTimestamp.value - Date.now()) / 1000))

  remainingSeconds.value = seconds

  if (seconds <= 0) {
    clearCountdown()
    expireOrder()
  }
}

const startCountdown = () => {
  clearCountdown()
  syncCountdown()

  if (remainingSeconds.value <= 0) return

  countdownTimer = window.setInterval(syncCountdown, 1000)
}

// Bắt đầu đếm ngược 10 giây để giả lập tự động thanh toán thành công
const startSimulatedPayment = () => {
  clearSimulatedPayment()
  simulatedPaymentTimer = window.setTimeout(async () => {
    if (qrStarted.value && !orderCancelled.value && paymentMethod.value === 'BANK') {
      toast.info('Đang giả lập thanh toán tự động thành công sau 10 giây...')
      await pay()
    }
  }, 10000) // 10000ms = 10 giây
}

const createQr = () => {
  if (!qrUrl) {
    toast.error('Không tạo được mã QR cho đơn hàng này')
    return
  }

  if (orderCancelled.value) {
    toast.error('Đơn hàng đã bị hủy')
    return
  }

  paymentMethod.value = 'BANK'
  qrStarted.value = true
  sessionStorage.setItem(qrStorageKey.value, 'true')
  startCountdown()
  startSimulatedPayment() // Kích hoạt giả lập 10s khi tạo QR
}

const loadOrder = async () => {
  if (!orderId) {
    throw new Error('Thiếu mã định danh đơn hàng')
  }

  const res = await fetch(`http://localhost:8080/hoadon/${orderId}`)

  if (!res.ok) {
    throw new Error('Máy chủ không trả về thông tin đơn hàng')
  }

  const data = await res.json()

  if (!data?.id) {
    throw new Error('Đơn hàng không có dữ liệu hợp lệ')
  }

  orderInfo.value = data
  orderItems.value = Array.isArray(data.sanPhams) ? data.sanPhams : []

  if (data.trangThai === 'da_huy') {
    qrExpired.value = true
    clearQrSession()
    return
  }

  const savedQrSession = sessionStorage.getItem(qrStorageKey.value) === 'true'

  if (savedQrSession) {
    paymentMethod.value = 'BANK'
    qrStarted.value = true
    startCountdown()
    startSimulatedPayment() // Kích hoạt lại giả lập nếu refresh trang khi đang mở QR
  }
}

onMounted(async () => {
  try {
    await loadOrder()
  } catch (error) {
    console.error(error)
    loadError.value = error.message || 'Không tải được thông tin đơn hàng'
    toast.error(loadError.value)
  } finally {
    loading.value = false
  }
})

onBeforeUnmount(clearCountdown)

const pay = async () => {
  if (payDisabled.value) return

  if (paymentMethod.value === 'BANK') {
    syncCountdown()
    if (remainingSeconds.value <= 0) {
      await expireOrder()
      return
    }
  }

  try {
    isPaying.value = true

    const body = {
      idHoaDon: Number(orderId),
      method: paymentMethod.value,
      orderInfo: `Thanh toan don hang ${orderCode.value}`,
    }

    console.log('=== DỮ LIỆU GỬI LÊN BACKEND ===', body)
    const res = await thanhToan(body)

    if (res && res.paymentUrl && paymentMethod.value === 'VNPAY') {
      clearQrSession()
      sessionStorage.removeItem('checkoutData')
      toast.success('Đang chuyển hướng đến cổng thanh toán VNPay...')
      window.location.href = res.paymentUrl
      return
    }

    clearQrSession()
    sessionStorage.removeItem('checkoutData')
    toast.success(res.message || 'Đã ghi nhận phương thức thanh toán thành công!')
    router.push('/san-pham')
  } catch (error) {
    console.error(error)
    toast.error(error.message || 'Thanh toán thất bại')
  } finally {
    isPaying.value = false
  }
}

const handleCancel = async () => {
  if (isPaying.value || isCancelling.value || isExpiring.value || orderCancelled.value) return

  try {
    isCancelling.value = true

    await huyHoaDonOnline(Number(orderId))
    clearQrSession()

    if (orderInfo.value) {
      orderInfo.value.trangThai = 'da_huy'
    }

    toast.success('Đã hủy đơn hàng')
    router.push('/san-pham')
  } catch (error) {
    console.error(error)
    toast.error(error.message || 'Hủy đơn thất bại')
  } finally {
    isCancelling.value = false
  }
}
</script>

<style scoped>
.payment-page {
  background-image:
    linear-gradient(rgba(255, 255, 255, 0.72), rgba(248, 250, 252, 0.9)),
    radial-gradient(circle at 1px 1px, rgba(99, 102, 241, 0.12) 1px, transparent 0);
  background-size:
    auto,
    24px 24px;
}

.payment-option::after {
  position: absolute;
  right: -36px;
  bottom: -52px;
  width: 120px;
  height: 120px;
  content: '';
  border-radius: 9999px;
  background: rgba(99, 102, 241, 0.08);
  transition: transform 220ms ease;
}

.payment-option:hover::after {
  transform: scale(1.15);
}

.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: all 240ms ease;
}

.slide-fade-enter-from,
.slide-fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
</style>
