<template>
  <div class="checkout-page min-h-screen bg-slate-50 text-slate-900">
    <div class="pointer-events-none fixed inset-0 overflow-hidden">
      <div class="absolute -left-24 top-20 h-80 w-80 rounded-full bg-indigo-200/40 blur-3xl"></div>
      <div class="absolute -right-24 top-0 h-96 w-96 rounded-full bg-sky-200/40 blur-3xl"></div>
      <div
        class="absolute bottom-0 left-1/3 h-72 w-72 rounded-full bg-violet-100/50 blur-3xl"
      ></div>
    </div>

    <div class="relative mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 lg:py-10">
      <!-- TOP BAR -->
      <div class="mb-6 flex flex-wrap items-center justify-between gap-4">
        <button
          type="button"
          class="group inline-flex items-center gap-2 text-sm font-semibold text-slate-600 transition hover:text-indigo-700"
          @click="router.push('/san-pham')"
        >
          <span
            class="grid h-9 w-9 place-items-center rounded-full border border-slate-200 bg-white shadow-sm transition group-hover:border-indigo-200 group-hover:bg-indigo-50"
          >
            <ArrowLeft :size="17" />
          </span>
          Quay lại sản phẩm
        </button>

        <div
          class="inline-flex items-center gap-2 rounded-full border border-slate-200 bg-white px-4 py-2 text-sm font-semibold text-slate-700 shadow-sm"
        >
          <UserRound :size="16" class="text-indigo-600" />
          {{ isLoggedIn ? 'Khách hàng đã đăng nhập' : 'Đặt hàng không cần tài khoản' }}
        </div>
      </div>

      <!-- HERO -->
      <section
        class="relative overflow-hidden rounded-[28px] bg-gradient-to-r from-blue-600 via-indigo-600 to-violet-600 px-6 py-7 text-white shadow-xl shadow-indigo-500/20 sm:px-8 lg:px-10 lg:py-9"
      >
        <div class="absolute -right-20 -top-24 h-72 w-72 rounded-full bg-white/10 blur-2xl"></div>
        <div
          class="absolute -bottom-28 left-1/3 h-64 w-64 rounded-full bg-cyan-400/20 blur-2xl"
        ></div>

        <div class="relative grid gap-7 lg:grid-cols-[1fr_auto] lg:items-center">
          <div>
            <div
              class="mb-4 inline-flex items-center gap-2 rounded-full border border-white/20 bg-white/15 px-3 py-1.5 text-xs font-bold uppercase tracking-[0.18em] text-white backdrop-blur-md"
            >
              <ClipboardCheck :size="14" />
              Xác nhận đơn hàng
            </div>

            <h1
              class="max-w-2xl text-3xl font-black tracking-tight sm:text-4xl lg:text-5xl text-white"
            >
              Kiểm tra lần cuối trước khi đặt hàng
            </h1>

            <p class="mt-3 max-w-2xl text-sm leading-6 text-indigo-100 sm:text-base">
              Hoàn thiện thông tin nhận hàng, vận chuyển và ưu đãi. Bạn sẽ chọn phương thức thanh
              toán ở bước tiếp theo.
            </p>
          </div>

          <div
            class="min-w-[250px] rounded-2xl border border-white/20 bg-white/10 p-4 shadow-lg backdrop-blur-md"
          >
            <p class="text-xs font-bold uppercase tracking-[0.16em] text-indigo-100">
              Tổng thanh toán dự kiến
            </p>
            <p class="mt-2 text-3xl font-black text-white">{{ formatMoney(total) }}</p>
            <div class="mt-3 flex items-center gap-2 text-xs text-emerald-300 font-medium">
              <ShieldCheck :size="15" />
              Giá được kiểm tra lại ở máy chủ
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
          <span class="hidden sm:inline">Sản phẩm</span>
        </div>

        <div
          class="flex items-center justify-center gap-2 rounded-xl bg-indigo-50 px-2 py-3 text-xs font-bold text-indigo-700 sm:text-sm"
        >
          <span class="grid h-6 w-6 place-items-center rounded-full bg-indigo-600 text-white"
            >2</span
          >
          Xác nhận
        </div>

        <div
          class="flex items-center justify-center gap-2 rounded-xl px-2 py-3 text-xs font-bold text-slate-400 sm:text-sm"
        >
          <span class="grid h-6 w-6 place-items-center rounded-full bg-slate-100">3</span>
          <span class="hidden sm:inline">Thanh toán</span>
        </div>
      </div>

      <div class="mt-8 grid items-start gap-6 lg:grid-cols-[minmax(0,1.45fr)_minmax(340px,0.75fr)]">
        <!-- LEFT COLUMN -->
        <div class="space-y-6">
          <!-- RECIPIENT -->
          <section
            class="rounded-[28px] border border-slate-200/80 bg-white p-5 shadow-xl shadow-slate-200/40 sm:p-7"
          >
            <!-- (Phần Địa chỉ giữ nguyên như cũ của bạn) -->
            <div class="flex flex-wrap items-start justify-between gap-4">
              <div class="flex items-start gap-3">
                <span
                  class="grid h-12 w-12 shrink-0 place-items-center rounded-2xl bg-indigo-50 text-indigo-600"
                >
                  <MapPin :size="24" />
                </span>
                <div>
                  <p class="text-xs font-black uppercase tracking-[0.16em] text-indigo-600">
                    Bước 1
                  </p>
                  <h2 class="mt-1 text-2xl font-black tracking-tight">
                    {{ isLoggedIn ? 'Chọn địa chỉ giao hàng' : 'Thông tin nhận hàng' }}
                  </h2>
                  <p class="mt-2 text-sm text-slate-500">
                    {{
                      isLoggedIn
                        ? 'Chọn một địa chỉ đã lưu hoặc thêm địa chỉ mới.'
                        : 'Nhập đúng thông tin để đơn vị vận chuyển có thể liên hệ.'
                    }}
                  </p>
                </div>
              </div>

              <template v-if="isLoggedIn">
                <button
                  v-if="addresses.length < 3"
                  type="button"
                  class="inline-flex items-center gap-2 rounded-xl bg-indigo-600 px-4 py-2.5 text-sm font-black text-white shadow-lg shadow-indigo-100 transition hover:bg-indigo-700"
                  @click="openAddAddress"
                >
                  <Plus :size="17" />
                  Thêm địa chỉ
                </button>
                <span
                  v-else
                  class="rounded-xl bg-slate-100 px-4 py-2.5 text-sm font-semibold text-slate-500"
                >
                  Tối đa 3 địa chỉ
                </span>
              </template>
            </div>

            <!-- LOGGED-IN ADDRESSES -->
            <div v-if="isLoggedIn" class="mt-6 space-y-4">
              <article
                v-for="item in showAllAddresses
                  ? addresses
                  : addresses.filter((a) => a.macDinh || a.id === selectedAddressId).slice(0, 1)"
                :key="item.id"
                class="cursor-pointer rounded-2xl border-2 p-5 transition"
                :class="
                  selectedAddressId === item.id
                    ? 'border-indigo-600 bg-indigo-50/60 shadow-lg shadow-indigo-100/70'
                    : 'border-slate-200 hover:border-indigo-200 hover:bg-slate-50'
                "
                @click="selectAddress(item.id)"
              >
                <div class="flex items-start gap-4">
                  <span
                    class="mt-0.5 grid h-6 w-6 shrink-0 place-items-center rounded-full border-2"
                    :class="
                      selectedAddressId === item.id
                        ? 'border-indigo-600 bg-indigo-600 text-white'
                        : 'border-slate-300 text-transparent'
                    "
                  >
                    <Check :size="14" />
                  </span>

                  <div class="min-w-0 flex-1">
                    <div class="flex flex-wrap items-center gap-2">
                      <h3 class="font-black text-slate-900">{{ item.tenNguoiNhan }}</h3>
                      <span
                        v-if="item.macDinh"
                        class="inline-flex items-center gap-1 rounded-full bg-emerald-100 px-2.5 py-1 text-[10px] font-black uppercase tracking-wide text-emerald-700"
                      >
                        <Star :size="11" />
                        Mặc định
                      </span>
                    </div>

                    <p class="mt-2 flex items-center gap-2 text-sm font-semibold text-slate-600">
                      <Phone :size="15" class="text-indigo-500" />
                      {{ item.soDienThoai }}
                    </p>
                    <p class="mt-2 flex items-start gap-2 text-sm leading-6 text-slate-500">
                      <MapPin :size="15" class="mt-1 shrink-0 text-indigo-500" />
                      <span>
                        {{ item.diaChiCuThe }}, {{ item.phuong }}, {{ item.quan }},
                        {{ item.thanhPho }}
                      </span>
                    </p>

                    <div class="mt-4 flex flex-wrap gap-2">
                      <button
                        type="button"
                        class="inline-flex items-center gap-1.5 rounded-lg bg-white px-3 py-2 text-xs font-bold text-blue-600 shadow-sm ring-1 ring-slate-200 transition hover:ring-blue-200"
                        @click.stop="openEditAddress(item)"
                      >
                        <Pencil :size="14" />
                        Sửa
                      </button>
                      <button
                        type="button"
                        class="inline-flex items-center gap-1.5 rounded-lg bg-white px-3 py-2 text-xs font-bold text-red-600 shadow-sm ring-1 ring-slate-200 transition hover:ring-red-200"
                        @click.stop="deleteAddress(item.id)"
                      >
                        <Trash2 :size="14" />
                        Xóa
                      </button>
                      <button
                        v-if="!item.macDinh"
                        type="button"
                        class="inline-flex items-center gap-1.5 rounded-lg bg-white px-3 py-2 text-xs font-bold text-emerald-600 shadow-sm ring-1 ring-slate-200 transition hover:ring-emerald-200"
                        @click.stop="setDefault(item.id)"
                      >
                        <Star :size="14" />
                        Đặt mặc định
                      </button>
                    </div>
                  </div>
                </div>
              </article>

              <div
                v-if="addresses.length === 0"
                class="rounded-2xl border border-dashed border-slate-300 bg-slate-50 p-7 text-center"
              >
                <MapPin :size="35" class="mx-auto text-slate-300" />
                <p class="mt-3 font-bold text-slate-600">Bạn chưa có địa chỉ giao hàng</p>
                <button
                  type="button"
                  class="mt-4 rounded-xl bg-indigo-600 px-5 py-2.5 font-bold text-white"
                  @click="openAddAddress"
                >
                  Thêm địa chỉ đầu tiên
                </button>
              </div>

              <button
                v-if="addresses.length > 1"
                type="button"
                class="flex w-full items-center justify-center gap-2 rounded-xl bg-indigo-50 py-3 font-bold text-indigo-700 transition hover:bg-indigo-100"
                @click="showAllAddresses = !showAllAddresses"
              >
                <ChevronUp v-if="showAllAddresses" :size="18" />
                <ChevronDown v-else :size="18" />
                {{
                  showAllAddresses
                    ? 'Thu gọn địa chỉ'
                    : 'Xem thêm địa chỉ khác (' + (addresses.length - 1) + ')'
                }}
              </button>
            </div>

            <!-- GUEST FORM -->
            <div v-else class="mt-6 space-y-5">
              <div class="grid gap-5 md:grid-cols-2">
                <label class="block">
                  <span class="mb-2 block text-sm font-bold text-slate-700">Họ và tên</span>
                  <span class="relative block">
                    <UserRound
                      :size="18"
                      class="pointer-events-none absolute left-4 top-1/2 -translate-y-1/2 text-slate-400"
                    />
                    <input
                      v-model.trim="addressForm.tenNguoiNhan"
                      maxlength="100"
                      placeholder="Nguyễn Văn A"
                      class="w-full rounded-xl border border-slate-300 bg-white py-3 pl-11 pr-4 outline-none transition focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
                    />
                  </span>
                </label>

                <label class="block">
                  <span class="mb-2 block text-sm font-bold text-slate-700">Số điện thoại</span>
                  <span class="relative block">
                    <Phone
                      :size="18"
                      class="pointer-events-none absolute left-4 top-1/2 -translate-y-1/2 text-slate-400"
                    />
                    <input
                      v-model.trim="addressForm.soDienThoai"
                      inputmode="numeric"
                      maxlength="11"
                      placeholder="09xxxxxxxx"
                      class="w-full rounded-xl border border-slate-300 bg-white py-3 pl-11 pr-4 outline-none transition focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
                    />
                  </span>
                </label>
              </div>

              <div class="grid gap-4 md:grid-cols-3">
                <label class="block">
                  <span class="mb-2 block text-sm font-bold text-slate-700">Tỉnh / Thành phố</span>
                  <select
                    v-model="selectedProvince"
                    class="w-full rounded-xl border border-slate-300 bg-white p-3 outline-none transition focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
                    @change="onProvinceChange"
                  >
                    <option :value="null">Chọn tỉnh/thành phố</option>
                    <option v-for="p in provinces" :key="p.ProvinceID" :value="p">
                      {{ p.ProvinceName }}
                    </option>
                  </select>
                </label>

                <label class="block">
                  <span class="mb-2 block text-sm font-bold text-slate-700">Quận / Huyện</span>
                  <select
                    v-model="selectedDistrict"
                    :disabled="!selectedProvince"
                    class="w-full rounded-xl border border-slate-300 bg-white p-3 outline-none transition focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100 disabled:cursor-not-allowed disabled:bg-slate-100"
                    @change="onDistrictChange"
                  >
                    <option :value="null">Chọn quận/huyện</option>
                    <option v-for="d in districts" :key="d.DistrictID" :value="d">
                      {{ d.DistrictName }}
                    </option>
                  </select>
                </label>

                <label class="block">
                  <span class="mb-2 block text-sm font-bold text-slate-700">Phường / Xã</span>
                  <select
                    v-model="selectedWard"
                    :disabled="!selectedDistrict"
                    class="w-full rounded-xl border border-slate-300 bg-white p-3 outline-none transition focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100 disabled:cursor-not-allowed disabled:bg-slate-100"
                    @change="onWardChange"
                  >
                    <option :value="null">Chọn phường/xã</option>
                    <option v-for="w in wards" :key="w.WardCode" :value="w">
                      {{ w.WardName }}
                    </option>
                  </select>
                </label>
              </div>

              <label class="block">
                <span class="mb-2 block text-sm font-bold text-slate-700">Địa chỉ cụ thể</span>
                <textarea
                  v-model.trim="addressForm.diaChiCuThe"
                  rows="3"
                  maxlength="250"
                  placeholder="Số nhà, tên đường, tòa nhà..."
                  class="w-full resize-none rounded-xl border border-slate-300 bg-white p-4 outline-none transition focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
                ></textarea>
              </label>

              <div
                class="flex items-start gap-3 rounded-2xl border border-amber-100 bg-amber-50 p-4 text-sm leading-6 text-amber-900"
              >
                <ShieldCheck :size="19" class="mt-0.5 shrink-0" />
                <p>
                  Sau khi đặt hàng, hãy lưu mã đơn. Bạn có thể tra cứu bằng mã đơn và số điện thoại
                  nhận hàng.
                </p>
              </div>
            </div>
          </section>

          <!-- PRODUCT -->
          <section
            class="rounded-[28px] border border-slate-200/80 bg-white p-5 shadow-xl shadow-slate-200/40 sm:p-7"
          >
            <!-- (Phần sản phẩm giữ nguyên như cũ của bạn) -->
            <div class="flex items-start justify-between gap-4">
              <div class="flex items-start gap-3">
                <span
                  class="grid h-12 w-12 shrink-0 place-items-center rounded-2xl bg-indigo-50 text-indigo-600"
                >
                  <Package :size="24" />
                </span>
                <div>
                  <p class="text-xs font-black uppercase tracking-[0.16em] text-indigo-600">
                    Bước 2
                  </p>
                  <h2 class="mt-1 text-2xl font-black tracking-tight">Sản phẩm đã chọn</h2>
                </div>
              </div>
            </div>

            <!-- ==================== CHECKOUT TỪ GIỎ HÀNG ==================== -->
            <template v-if="isCartCheckout">
              <article
                v-for="item in checkoutItems"
                :key="item.productDetailId"
                class="mt-4 grid gap-4 rounded-2xl border border-slate-200/80 bg-white p-4 shadow-sm transition-all hover:shadow-md sm:grid-cols-[120px_1fr] sm:gap-5 sm:p-5"
              >
                <!-- Ảnh sản phẩm -->
                <div
                  class="h-28 w-28 flex-shrink-0 overflow-hidden rounded-xl border border-slate-100 bg-slate-50 shadow-inner sm:h-30 sm:w-30"
                >
                  <img
                    :src="
                      item.anh
                        ? item.anh.startsWith('http')
                          ? item.anh
                          : 'http://localhost:8080' + item.anh
                        : '/no-image.png'
                    "
                    :alt="item.tenSanPham"
                    class="h-full w-full object-cover transition-transform duration-300 hover:scale-105"
                  />
                </div>

                <!-- Thông tin sản phẩm -->
                <div class="flex min-w-0 flex-1 flex-col justify-between">
                  <div>
                    <!-- Tên & Mã -->
                    <div class="flex items-start justify-between gap-3">
                      <h3 class="line-clamp-2 text-base font-bold text-slate-900 sm:text-lg">
                        {{ item.tenSanPham }}
                      </h3>
                      <span
                        class="inline-flex shrink-0 items-center rounded-md bg-slate-100 px-2 py-0.5 text-xs font-semibold text-slate-500"
                      >
                        #{{ item.maSanPhamChiTiet }}
                      </span>
                    </div>

                    <!-- Thuộc tính & Khả dụng -->
                    <div class="mt-2.5 flex flex-wrap items-center gap-2">
                      <span
                        v-if="item.mauSac"
                        class="inline-flex items-center gap-1 rounded-full bg-slate-100 px-2.5 py-1 text-xs font-medium text-slate-700"
                      >
                        🎨 {{ item.mauSac }}
                      </span>

                      <span
                        v-if="item.kichCo"
                        class="inline-flex items-center gap-1 rounded-full bg-slate-100 px-2.5 py-1 text-xs font-medium text-slate-700"
                      >
                        📏 {{ item.kichCo }}
                      </span>

                      <span
                        class="inline-flex items-center gap-1 rounded-full bg-emerald-50 px-2.5 py-1 text-xs font-semibold text-emerald-700 border border-emerald-200/60"
                      >
                        📦 Khả dụng: {{ item.soLuongKhaDung ?? 0 }}
                      </span>
                    </div>
                  </div>

                  <!-- Giá, Số lượng & Thành tiền -->
                  <div
                    class="mt-4 flex flex-col gap-3 pt-2 border-t border-slate-100 sm:border-t-0 sm:pt-0 sm:flex-row sm:items-center sm:justify-between"
                  >
                    <div class="flex items-center justify-between gap-4 sm:justify-start">
                      <div>
                        <p class="text-[11px] font-medium uppercase tracking-wider text-slate-400">
                          Đơn giá
                        </p>
                        <div class="flex items-baseline gap-1.5">
                          <span class="text-base font-bold text-slate-900">
                            {{ formatMoney(item.giaSauGiam || item.giaBan) }}
                          </span>
                          <span
                            v-if="item.giaSauGiam && item.giaSauGiam < item.giaBan"
                            class="text-xs text-slate-400 line-through"
                          >
                            {{ formatMoney(item.giaBan) }}
                          </span>
                        </div>
                      </div>

                      <div class="h-7 w-px bg-slate-200 hidden sm:block"></div>

                      <div
                        class="flex items-center overflow-hidden rounded-lg border border-slate-200 bg-slate-50/50"
                      >
                        <button
                          type="button"
                          class="flex h-8 w-8 items-center justify-center text-slate-600 transition-colors hover:bg-slate-200/70 disabled:cursor-not-allowed disabled:opacity-30"
                          :disabled="
                            item.quantity <= 1 ||
                            (item.soLuongKhaDung ?? item.soLuongTon ?? 0) === 0
                          "
                          @click="decreaseCartQty(item)"
                        >
                          -
                        </button>

                        <input
                          type="number"
                          v-model.number="item.quantity"
                          @input="validateCartQty(item)"
                          @blur="onCartQtyBlur(item)"
                          min="0"
                          :max="item.soLuongKhaDung ?? item.soLuongTon ?? 0"
                          class="h-8 w-11 border-x border-slate-200 bg-white text-center text-sm font-bold text-slate-800 focus:outline-none focus:ring-1 focus:ring-indigo-500 [appearance:textfield] [&::-webkit-inner-spin-button]:appearance-none [&::-webkit-outer-spin-button]:appearance-none"
                        />

                        <button
                          type="button"
                          class="flex h-8 w-8 items-center justify-center text-slate-600 transition-colors hover:bg-slate-200/70 disabled:cursor-not-allowed disabled:opacity-30"
                          :disabled="item.quantity >= (item.soLuongKhaDung ?? item.soLuongTon ?? 0)"
                          @click="increaseCartQty(item)"
                        >
                          +
                        </button>
                      </div>
                    </div>

                    <div
                      class="flex items-center justify-between border-t border-dashed border-slate-200 pt-2.5 sm:border-none sm:pt-0 sm:justify-end sm:gap-2"
                    >
                      <span class="text-xs font-medium text-slate-500">Thành tiền:</span>
                      <span class="text-lg sm:text-xl font-extrabold text-rose-600">
                        {{ formatMoney((item.giaSauGiam || item.giaBan) * (item.quantity || 0)) }}
                      </span>
                    </div>
                  </div>
                </div>
              </article>
            </template>

            <!-- ==================== MUA NGAY ==================== -->
            <template v-else>
              <article
                class="mt-4 grid gap-4 rounded-2xl border border-slate-200/80 bg-white p-4 shadow-sm transition-all hover:shadow-md sm:grid-cols-[120px_1fr] sm:gap-5 sm:p-5"
              >
                <div
                  class="h-28 w-28 flex-shrink-0 overflow-hidden rounded-xl border border-slate-100 bg-slate-50 shadow-inner sm:h-30 sm:w-30"
                >
                  <img
                    :src="
                      product?.images?.length
                        ? 'http://localhost:8080' + product.images[0]
                        : '/no-image.png'
                    "
                    :alt="product?.tenSanPham"
                    class="h-full w-full object-cover transition-transform duration-300 hover:scale-105"
                  />
                </div>

                <div class="flex min-w-0 flex-1 flex-col justify-between">
                  <div>
                    <div class="flex items-start justify-between gap-3">
                      <h3 class="line-clamp-2 text-base font-bold text-slate-900 sm:text-lg">
                        {{ product?.tenSanPham }}
                      </h3>
                      <span
                        class="inline-flex shrink-0 items-center rounded-md bg-slate-100 px-2 py-0.5 text-xs font-semibold text-slate-500"
                      >
                        #{{ product?.maSanPhamChiTiet }}
                      </span>
                    </div>

                    <div class="mt-2.5 flex flex-wrap items-center gap-2">
                      <span
                        v-if="product?.tenMauSac"
                        class="inline-flex items-center gap-1 rounded-full bg-slate-100 px-2.5 py-1 text-xs font-medium text-slate-700"
                      >
                        🎨 {{ product?.tenMauSac }}
                      </span>

                      <span
                        v-if="product?.tenKichThuoc"
                        class="inline-flex items-center gap-1 rounded-full bg-slate-100 px-2.5 py-1 text-xs font-medium text-slate-700"
                      >
                        📏 {{ product?.tenKichThuoc }}
                      </span>

                      <span
                        class="inline-flex items-center gap-1 rounded-full bg-emerald-50 px-2.5 py-1 text-xs font-semibold text-emerald-700 border border-emerald-200/60"
                      >
                        📦 Khả dụng: {{ maxAvailable }}
                      </span>
                    </div>
                  </div>

                  <div
                    class="mt-4 flex flex-col gap-3 pt-2 border-t border-slate-100 sm:border-t-0 sm:pt-0 sm:flex-row sm:items-center sm:justify-between"
                  >
                    <div class="flex items-center justify-between gap-4 sm:justify-start">
                      <div>
                        <p class="text-[11px] font-medium uppercase tracking-wider text-slate-400">
                          Đơn giá
                        </p>
                        <div class="flex items-baseline gap-1.5">
                          <span class="text-base font-bold text-slate-900">
                            {{ formatMoney(product?.giaSauGiam || product?.giaBan) }}
                          </span>
                          <span
                            v-if="
                              product?.dangGiamGia ||
                              (product?.giaSauGiam && product?.giaSauGiam < product?.giaBan)
                            "
                            class="text-xs text-slate-400 line-through"
                          >
                            {{ formatMoney(product?.giaBan) }}
                          </span>
                        </div>
                      </div>

                      <div class="h-7 w-px bg-slate-200 hidden sm:block"></div>

                      <div
                        class="flex items-center overflow-hidden rounded-lg border border-slate-200 bg-slate-50/50"
                      >
                        <button
                          type="button"
                          class="flex h-8 w-8 items-center justify-center text-slate-600 transition-colors hover:bg-slate-200/70 disabled:cursor-not-allowed disabled:opacity-30"
                          :disabled="quantity <= 1 || maxAvailable === 0"
                          @click="decreaseQty"
                        >
                          -
                        </button>

                        <input
                          type="number"
                          v-model.number="quantity"
                          @input="validateQty"
                          @blur="onQtyBlur"
                          min="0"
                          :max="maxAvailable"
                          class="h-8 w-11 border-x border-slate-200 bg-white text-center text-sm font-bold text-slate-800 focus:outline-none focus:ring-1 focus:ring-indigo-500 [appearance:textfield] [&::-webkit-inner-spin-button]:appearance-none [&::-webkit-outer-spin-button]:appearance-none"
                        />

                        <button
                          type="button"
                          class="flex h-8 w-8 items-center justify-center text-slate-600 transition-colors hover:bg-slate-200/70 disabled:cursor-not-allowed disabled:opacity-30"
                          :disabled="quantity >= maxAvailable || maxAvailable === 0"
                          @click="increaseQty"
                        >
                          +
                        </button>
                      </div>
                    </div>

                    <div
                      class="flex items-center justify-between border-t border-dashed border-slate-200 pt-2.5 sm:border-none sm:pt-0 sm:justify-end sm:gap-2"
                    >
                      <span class="text-xs font-medium text-slate-500">Thành tiền:</span>
                      <span class="text-lg sm:text-xl font-extrabold text-rose-600">
                        {{
                          formatMoney(
                            (product?.giaSauGiam || product?.giaBan || 0) * (quantity || 0),
                          )
                        }}
                      </span>
                    </div>
                  </div>
                </div>
              </article>
            </template>
          </section>

          <!-- SHIPPING & VOUCHER -->
          <div class="grid gap-6 md:grid-cols-2">
            <section
              class="rounded-[28px] border border-slate-200/80 bg-white p-5 shadow-xl shadow-slate-200/40 sm:p-6"
            >
              <div class="flex items-center gap-3">
                <span class="grid h-11 w-11 place-items-center rounded-2xl bg-sky-50 text-sky-600">
                  <Truck :size="22" />
                </span>
                <div>
                  <p class="text-xs font-black uppercase tracking-[0.15em] text-sky-600">
                    Vận chuyển
                  </p>
                  <h2 class="font-black text-slate-900">Giao hàng tiêu chuẩn</h2>
                </div>
              </div>

              <div class="mt-5 rounded-2xl border border-sky-100 bg-sky-50/70 p-4">
                <div class="flex items-center justify-between gap-4">
                  <div>
                    <p class="font-black text-slate-900">Tiêu chuẩn</p>
                    <p class="mt-1 text-xs text-slate-500">Dự kiến 2–4 ngày làm việc</p>
                  </div>
                  <LoaderCircle
                    v-if="shippingLoading"
                    :size="20"
                    class="animate-spin text-sky-600"
                  />
                  <p v-else class="font-black text-sky-700">{{ formatMoney(shippingFee) }}</p>
                </div>
              </div>
            </section>

            <section
              class="rounded-[28px] border border-slate-200/80 bg-white p-5 shadow-xl shadow-slate-200/40 sm:p-6"
            >
              <div class="flex items-center gap-3">
                <span
                  class="grid h-11 w-11 place-items-center rounded-2xl bg-emerald-50 text-emerald-600"
                >
                  <TicketPercent :size="22" />
                </span>
                <div>
                  <p class="text-xs font-black uppercase tracking-[0.15em] text-emerald-600">
                    Ưu đãi
                  </p>
                  <h2 class="font-black text-slate-900">Voucher đơn hàng</h2>
                </div>
              </div>

              <div class="mt-5">
                <button
                  @click="showVoucherModal = true"
                  class="flex w-full items-center justify-between rounded-xl border border-slate-300 bg-white p-4 hover:border-indigo-500 transition"
                >
                  <div class="text-left">
                    <p class="font-semibold">
                      {{ selectedVoucher ? selectedVoucher.tenVoucher : 'Chọn voucher' }}
                    </p>

                    <p class="text-sm text-gray-500">
                      {{ selectedVoucher ? selectedVoucher.maVoucher : 'Nhấn để chọn voucher' }}
                    </p>
                  </div>
                </button>
              </div>
            </section>
          </div>

          <!-- ==================== TÍCH XU & ĐỔI THƯỞNG ==================== -->
          <section
            v-if="isLoggedIn"
            class="rounded-[28px] border border-slate-200/80 bg-white p-5 shadow-xl shadow-slate-200/40 sm:p-7"
          >
            <div class="flex items-center justify-between mb-4">
              <div class="flex items-center gap-3">
                <span
                  class="grid h-11 w-11 place-items-center rounded-2xl bg-amber-50 text-amber-600"
                >
                  <span class="text-xl">🪙</span>
                </span>
                <div>
                  <p class="text-xs font-black uppercase tracking-[0.15em] text-amber-600">
                    Tích điểm
                  </p>
                  <h2 class="font-black text-slate-900">Sử dụng Xu giảm giá</h2>
                </div>
              </div>
              <span class="text-sm font-medium text-slate-600">
                Số dư khả dụng:
                <strong class="text-amber-600">{{ customerInfo?.soDuXu || 0 }} xu</strong>
              </span>
            </div>

            <div v-if="customerInfo && customerInfo.soDuXu > 0" class="space-y-3">
              <div class="flex items-center gap-3">
                <input
                  type="number"
                  v-model.number="soXuCanDung"
                  min="0"
                  :max="customerInfo.soDuXu"
                  placeholder="Nhập số xu muốn dùng..."
                  class="w-full rounded-xl border border-slate-300 bg-slate-50 px-4 py-3 text-sm outline-none transition focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
                />
                <button
                  type="button"
                  @click="apDungSoXu"
                  class="shrink-0 rounded-xl bg-amber-500 px-5 py-3 text-sm font-bold text-white shadow-md shadow-amber-100 transition hover:bg-amber-600"
                >
                  Áp dụng xu
                </button>
                <button
                  v-if="soXuSuDung > 0"
                  type="button"
                  @click="huyBoSoXu"
                  class="shrink-0 rounded-xl bg-slate-100 px-4 py-3 text-sm font-bold text-slate-600 transition hover:bg-slate-200"
                >
                  Bỏ chọn
                </button>
              </div>

              <div
                v-if="tienGiamDoXu > 0"
                class="text-xs text-emerald-600 font-medium flex items-center gap-1"
              >
                <span
                  >✓ Đã dùng <strong>{{ soXuSuDung }} xu</strong> để giảm trừ:</span
                >
                <span class="font-bold">-{{ formatMoney(tienGiamDoXu) }}</span>
              </div>
            </div>
            <div v-else class="text-sm text-slate-400 italic">
              Khách hàng hiện không có xu tích lũy để sử dụng.
            </div>
          </section>

          <!-- NOTE -->
          <section
            class="rounded-[28px] border border-slate-200/80 bg-white p-5 shadow-xl shadow-slate-200/40 sm:p-7"
          >
            <div class="flex items-center gap-3">
              <span
                class="grid h-11 w-11 place-items-center rounded-2xl bg-violet-50 text-violet-600"
              >
                <MessageSquareText :size="22" />
              </span>
              <div>
                <p class="text-xs font-black uppercase tracking-[0.15em] text-violet-600">
                  Ghi chú
                </p>
                <h2 class="font-black text-slate-900">Yêu cầu giao hàng</h2>
              </div>
            </div>

            <textarea
              v-model="note"
              rows="4"
              maxlength="500"
              class="mt-5 w-full resize-none rounded-2xl border border-slate-300 bg-slate-50 p-4 outline-none transition focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
              placeholder="Ví dụ: Giao hàng giờ hành chính, gọi trước khi giao..."
            ></textarea>
          </section>
        </div>

        <!-- SUMMARY -->
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
                  <h2 class="text-xl font-black">Tóm tắt đơn hàng</h2>
                  <p class="text-xs text-slate-400">{{ quantity }} sản phẩm trong đơn</p>
                </div>
              </div>
            </div>

            <div class="space-y-4 px-6 py-5 text-sm">
              <div class="flex items-center justify-between gap-4">
                <span class="text-slate-500">Tiền hàng</span>
                <span class="font-bold text-slate-800">{{ formatMoney(subtotal) }}</span>
              </div>
              <div class="flex items-center justify-between gap-4">
                <span class="text-slate-500">Phí vận chuyển</span>
                <span class="font-bold text-slate-800">{{ formatMoney(shippingFee) }}</span>
              </div>
              <div class="flex items-center justify-between gap-4">
                <span class="text-slate-500">Voucher giảm</span>
                <span class="font-bold text-emerald-600">-{{ formatMoney(voucherDiscount) }}</span>
              </div>
              <div v-if="tienGiamDoXu > 0" class="flex items-center justify-between gap-4">
                <span class="text-slate-500">Xu giảm giá</span>
                <span class="font-bold text-amber-600">-{{ formatMoney(tienGiamDoXu) }}</span>
              </div>

              <div class="border-t border-dashed border-slate-200 pt-4">
                <div class="flex items-end justify-between gap-4">
                  <span class="font-black text-slate-900">Tổng thanh toán</span>
                  <span class="text-right text-2xl font-black text-indigo-700">
                    {{ formatMoney(total) }}
                  </span>
                </div>
              </div>
            </div>

            <div class="border-t border-slate-100 bg-slate-50/80 p-5">
              <button
                type="button"
                :disabled="
                  isPlacingOrder ||
                  shippingLoading ||
                  (isCartCheckout ? checkoutItems.length === 0 : maxAvailable === 0)
                "
                class="flex w-full items-center justify-center gap-2 rounded-2xl bg-indigo-600 px-5 py-4 text-base font-black text-white shadow-lg shadow-indigo-200 transition hover:-translate-y-0.5 hover:bg-indigo-700 disabled:cursor-not-allowed disabled:opacity-60 disabled:hover:translate-y-0"
                @click="placeOrder"
              >
                <LoaderCircle v-if="isPlacingOrder" :size="20" class="animate-spin" />
                <CreditCard v-else :size="20" />
                {{ isPlacingOrder ? 'Đang tạo đơn...' : 'Đặt hàng và thanh toán' }}
              </button>

              <p class="mt-3 text-center text-xs leading-5 text-slate-400">
                Nhấn đặt hàng đồng nghĩa bạn xác nhận thông tin phía trên là chính xác.
              </p>
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
                  Thông tin người nhận chỉ được dùng để xử lý và giao đơn hàng.
                </p>
              </div>
            </div>
          </section>
        </aside>
      </div>

      <!-- Các Modal (Địa chỉ, Voucher) giữ nguyên bên dưới -->
      <!-- ADDRESS MODAL -->
      <Teleport to="body">
        <div
          v-if="showAddressModal && isLoggedIn"
          class="fixed inset-0 z-50 flex items-center justify-center bg-slate-950/60 p-4 backdrop-blur-sm"
          @click.self="showAddressModal = false"
        >
          <!-- (Nội dung Modal địa chỉ của bạn) -->
          <div
            class="max-h-[94vh] w-full max-w-5xl overflow-y-auto rounded-[30px] border border-white/70 bg-white shadow-2xl"
          >
            <!-- Header modal -->
            <div
              class="flex items-center justify-between border-b border-slate-100 px-6 py-5 sm:px-8"
            >
              <div>
                <p class="text-xs font-black uppercase tracking-[0.15em] text-indigo-600">
                  Địa chỉ giao hàng
                </p>
                <h2 class="mt-1 text-2xl font-black text-slate-900">
                  {{ editingAddress ? 'Cập nhật địa chỉ' : 'Thêm địa chỉ mới' }}
                </h2>
              </div>
              <button
                type="button"
                class="grid h-10 w-10 place-items-center rounded-xl bg-slate-100 text-slate-500 transition hover:bg-red-50 hover:text-red-600"
                @click="showAddressModal = false"
              >
                <X :size="20" />
              </button>
            </div>

            <!-- Body modal -->
            <div class="grid gap-7 p-6 sm:p-8 lg:grid-cols-[1fr_380px]">
              <div class="space-y-5">
                <div class="grid gap-5 md:grid-cols-2">
                  <label class="block">
                    <span class="mb-2 block text-sm font-bold text-slate-700">Họ và tên</span>
                    <input
                      v-model.trim="addressForm.tenNguoiNhan"
                      placeholder="Họ và tên người nhận"
                      class="w-full rounded-xl border border-slate-300 px-4 py-3 outline-none transition focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
                    />
                  </label>
                  <label class="block">
                    <span class="mb-2 block text-sm font-bold text-slate-700">Số điện thoại</span>
                    <input
                      v-model.trim="addressForm.soDienThoai"
                      inputmode="numeric"
                      maxlength="11"
                      placeholder="09xxxxxxxx"
                      class="w-full rounded-xl border border-slate-300 px-4 py-3 outline-none transition focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
                    />
                  </label>
                </div>

                <label class="block">
                  <span class="mb-2 block text-sm font-bold text-slate-700">Tỉnh / Thành phố</span>
                  <select
                    v-model="selectedProvince"
                    class="w-full rounded-xl border border-slate-300 p-3 outline-none transition focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
                    @change="onProvinceChange"
                  >
                    <option :value="null">Chọn tỉnh/thành phố</option>
                    <option v-for="p in provinces" :key="p.ProvinceID" :value="p">
                      {{ p.ProvinceName }}
                    </option>
                  </select>
                </label>

                <div class="grid gap-5 md:grid-cols-2">
                  <label class="block">
                    <span class="mb-2 block text-sm font-bold text-slate-700">Quận / Huyện</span>
                    <select
                      v-model="selectedDistrict"
                      class="w-full rounded-xl border border-slate-300 p-3 outline-none transition focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
                      @change="onDistrictChange"
                    >
                      <option :value="null">Chọn quận/huyện</option>
                      <option v-for="d in districts" :key="d.DistrictID" :value="d">
                        {{ d.DistrictName }}
                      </option>
                    </select>
                  </label>

                  <label class="block">
                    <span class="mb-2 block text-sm font-bold text-slate-700">Phường / Xã</span>
                    <select
                      v-model="selectedWard"
                      class="w-full rounded-xl border border-slate-300 p-3 outline-none transition focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
                      @change="onWardChange"
                    >
                      <option :value="null">Chọn phường/xã</option>
                      <option v-for="w in wards" :key="w.WardCode" :value="w">
                        {{ w.WardName }}
                      </option>
                    </select>
                  </label>
                </div>

                <label class="block">
                  <span class="mb-2 block text-sm font-bold text-slate-700">Địa chỉ cụ thể</span>
                  <textarea
                    v-model.trim="addressForm.diaChiCuThe"
                    rows="3"
                    placeholder="Số nhà, tên đường..."
                    class="w-full resize-none rounded-xl border border-slate-300 p-4 outline-none transition focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
                  ></textarea>
                </label>

                <label
                  class="flex cursor-pointer items-center gap-3 rounded-xl border border-slate-200 bg-slate-50 p-4"
                >
                  <input
                    v-model="addressForm.macDinh"
                    type="checkbox"
                    class="h-5 w-5 accent-indigo-600"
                  />
                  <span class="font-semibold text-slate-600">Đặt làm địa chỉ mặc định</span>
                </label>
              </div>

              <div class="flex flex-col gap-4">
                <div
                  id="map"
                  class="h-[320px] overflow-hidden rounded-2xl border-2 border-slate-200 bg-slate-100"
                ></div>

                <button
                  type="button"
                  class="flex w-full items-center justify-center gap-2 rounded-xl bg-indigo-50 px-4 py-3 font-bold text-indigo-700 transition hover:bg-indigo-100"
                  @click="getCurrentLocation"
                >
                  <LoaderCircle v-if="loading" :size="18" class="animate-spin" />
                  <Navigation v-else :size="18" />
                  {{ loading ? 'Đang xác định...' : 'Dùng vị trí hiện tại' }}
                </button>

                <div class="mt-auto grid grid-cols-2 gap-3">
                  <button
                    type="button"
                    class="rounded-xl border border-slate-200 px-5 py-3 font-bold text-slate-600 transition hover:bg-slate-50"
                    @click="showAddressModal = false"
                  >
                    Hủy
                  </button>
                  <button
                    type="button"
                    class="rounded-xl bg-indigo-600 px-5 py-3 font-bold text-white shadow-lg shadow-indigo-100 transition hover:bg-indigo-700"
                    @click="saveAddress"
                  >
                    Lưu địa chỉ
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </Teleport>

      <!-- VOUCHER MODAL -->
      <Teleport to="body">
        <div
          v-if="showVoucherModal"
          class="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4 backdrop-blur-sm"
          @click.self="showVoucherModal = false"
        >
          <!-- (Nội dung Modal Voucher giữ nguyên của bạn) -->
          <div
            class="w-full max-w-lg rounded-2xl bg-slate-50 overflow-hidden shadow-2xl flex flex-col max-h-[85vh]"
          >
            <div
              class="flex items-center justify-between bg-white px-6 py-4 border-b border-slate-100"
            >
              <h2 class="text-lg font-bold text-slate-800 flex items-center gap-2">
                <span>🎁 Chọn Voucher Giảm Giá</span>
              </h2>
              <button
                @click="showVoucherModal = false"
                class="text-slate-400 hover:text-slate-600 text-xl font-light w-8 h-8 flex items-center justify-center rounded-full hover:bg-slate-100 transition-colors"
              >
                ✕
              </button>
            </div>

            <div
              class="bg-indigo-50/70 px-6 py-2.5 text-xs text-indigo-700 border-b border-indigo-100 flex items-center gap-1.5 font-medium shrink-0"
            >
              <span>💡 Nhấn vào voucher để chọn và áp dụng ngay cho đơn hàng</span>
            </div>

            <div
              class="p-4 space-y-3 overflow-y-auto flex-1 max-h-[420px] overscroll-contain custom-scrollbar"
            >
              <div
                @click="clearVoucher"
                :class="[
                  'flex items-center justify-between rounded-xl border p-3.5 cursor-pointer transition-all bg-white',
                  selectedVoucherId === null
                    ? 'border-emerald-500 ring-2 ring-emerald-100'
                    : 'border-slate-200 hover:border-indigo-500 hover:shadow-md',
                ]"
              >
                <div>
                  <div class="font-semibold text-slate-800 flex items-center gap-2">
                    Không sử dụng voucher
                    <span
                      v-if="selectedVoucherId === null"
                      class="px-2 py-0.5 rounded-full bg-emerald-100 text-emerald-700 text-[10px] font-bold"
                    >
                      ✓ Đang áp dụng
                    </span>
                  </div>
                  <p class="text-xs text-slate-500 mt-1">Thanh toán theo giá gốc của đơn hàng</p>
                </div>
                <input
                  type="radio"
                  name="voucher-selection"
                  :checked="selectedVoucherId === null"
                  class="w-4 h-4 accent-indigo-600"
                  @click.stop="clearVoucher"
                />
              </div>

              <div
                v-for="v in sortedVouchers"
                :key="v.uid"
                @click="
                  subtotal >= v.giaTriDonHangToiThieu &&
                  ((selectedVoucherId = v.uid), (showVoucherModal = false))
                "
                :class="[
                  'relative flex items-center bg-white rounded-xl border transition-all overflow-hidden p-3.5 gap-4',
                  selectedVoucherId === v.uid
                    ? 'border-emerald-500 ring-2 ring-emerald-100'
                    : bestVoucher && bestVoucher.uid === v.uid
                      ? 'border-amber-400 ring-2 ring-amber-100'
                      : subtotal >= v.giaTriDonHangToiThieu
                        ? 'border-slate-200 hover:border-indigo-500 hover:shadow-md'
                        : 'opacity-55 cursor-not-allowed border-slate-200 bg-slate-100/60',
                ]"
              >
                <div
                  :class="[
                    'w-28 py-3 rounded-xl text-white flex flex-col items-center justify-center text-center shrink-0 shadow-md',
                    subtotal >= v.giaTriDonHangToiThieu
                      ? 'bg-gradient-to-tr from-blue-600 via-indigo-600 to-violet-600'
                      : 'bg-gradient-to-tr from-slate-400 to-slate-500',
                  ]"
                >
                  <span class="text-[9px] uppercase tracking-wider opacity-90 font-medium">
                    {{ v.loaiVoucher === 'CA_NHAN' ? 'Voucher của bạn' : 'Voucher hệ thống' }}
                  </span>
                  <span class="text-base font-black my-0.5 tracking-tight">
                    {{
                      v.loaiGiamGia === 'tien_mat'
                        ? formatShortCurrency(v.giaTriGiam)
                        : v.giaTriGiam + '%'
                    }}
                  </span>
                </div>

                <div class="flex-1 flex flex-col justify-between">
                  <div class="flex items-start justify-between gap-2">
                    <div>
                      <div class="flex items-center gap-2 flex-wrap">
                        <h3 class="font-bold text-slate-800 text-sm line-clamp-1">
                          {{ v.tenVoucher }}
                        </h3>
                        <span
                          v-if="v.loaiVoucher === 'CA_NHAN'"
                          class="px-2 py-0.5 rounded-full bg-amber-100 text-amber-700 text-[10px] font-bold"
                        >
                          🎁 Voucher của bạn
                        </span>
                        <span
                          v-else
                          class="px-2 py-0.5 rounded-full bg-blue-100 text-blue-700 text-[10px] font-bold"
                        >
                          🏷️ Hệ thống
                        </span>
                        <span
                          v-if="selectedVoucherId === v.uid"
                          class="px-2 py-0.5 rounded-full bg-emerald-100 text-emerald-700 text-[10px] font-bold"
                        >
                          ✓ Đang áp dụng
                        </span>
                        <span
                          v-else-if="bestVoucher && bestVoucher.uid === v.uid"
                          class="px-2 py-0.5 rounded-full bg-amber-100 text-amber-700 text-[10px] font-bold"
                        >
                          ⭐ Đề xuất
                        </span>
                      </div>

                      <div class="flex items-center gap-2 mt-0.5">
                        <p class="text-xs text-slate-400 font-mono">
                          Mã: <span class="text-slate-600 font-semibold">{{ v.maVoucher }}</span>
                        </p>
                        <p class="text-xs text-emerald-600 font-semibold mt-1">
                          Tiết kiệm {{ formatCurrency(getVoucherDiscount(v)) }}
                        </p>
                        <span
                          v-if="v.loaiGiamGia === 'phan_tram' && v.giaTriGiamToiDa"
                          class="text-[10px] bg-indigo-50 text-indigo-600 px-1.5 py-0.5 rounded font-medium"
                        >
                          Tối đa {{ formatShortCurrency(v.giaTriGiamToiDa) }}
                        </span>
                      </div>
                    </div>

                    <div class="shrink-0 pt-0.5">
                      <input
                        type="radio"
                        :name="'voucher-selection'"
                        :checked="selectedVoucherId === v.uid"
                        :disabled="subtotal < v.giaTriDonHangToiThieu"
                        class="w-4 h-4 text-indigo-600 accent-indigo-600 cursor-pointer"
                        @click.stop="
                          subtotal >= v.giaTriDonHangToiThieu &&
                          ((selectedVoucherId = v.uid), (showVoucherModal = false))
                        "
                      />
                    </div>
                  </div>

                  <div
                    class="mt-3 flex items-center justify-between text-xs pt-2 border-t border-dashed border-slate-100"
                  >
                    <span class="text-slate-500">
                      Đơn tối thiểu:
                      <strong class="text-slate-700">{{
                        formatCurrency(v.giaTriDonHangToiThieu)
                      }}</strong>
                    </span>

                    <span
                      v-if="subtotal < v.giaTriDonHangToiThieu"
                      class="text-rose-500 font-semibold"
                    >
                      Thiếu {{ formatCurrency(v.giaTriDonHangToiThieu - subtotal) }}
                    </span>
                    <span v-else class="text-emerald-600 font-semibold"> ✓ Đạt điều kiện </span>
                  </div>
                </div>
              </div>
            </div>

            <div
              class="bg-white px-6 py-3 border-t border-slate-100 flex items-center justify-end shrink-0"
            >
              <button
                @click="showVoucherModal = false"
                class="px-5 py-2 rounded-lg bg-slate-200 text-slate-700 text-sm font-medium hover:bg-slate-300 transition-colors"
              >
                Đóng
              </button>
            </div>
          </div>
        </div>
      </Teleport>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  ArrowLeft,
  Check,
  ChevronDown,
  ChevronUp,
  ClipboardCheck,
  CreditCard,
  LoaderCircle,
  MapPin,
  MessageSquareText,
  Navigation,
  Package,
  Pencil,
  Phone,
  Plus,
  ReceiptText,
  ShieldCheck,
  Star,
  TicketPercent,
  Trash2,
  Truck,
  UserRound,
  X,
} from 'lucide-vue-next'
import { useToast } from 'vue-toastification'
import { getAllVoucher } from '@/service/VoucherService'
import { getSanPhamChiTietById } from '@/service/SanPhamChiTiet'
import { taoHoaDonOnline } from '@/service/HoaDonService'
import stompClient from '@/socket'
import { getProvinces, getDistricts, getWards, getShippingFee } from '@/service/GHNService'
import {
  getAllDiaChi,
  themDiaChi,
  capNhatDiaChi,
  xoaDiaChi,
  doiMacDinh,
} from '@/service/DiaChiKhachHangService'
import L from 'leaflet'

import gamificationApi from '@/service/clientGamificationService'
import 'leaflet/dist/leaflet.css'

import icon from 'leaflet/dist/images/marker-icon.png'
import shadow from 'leaflet/dist/images/marker-shadow.png'

import { detailKhachHangService } from '@/service/KhachHangService'

import { cauHinhService } from '@/service/cauHinhService'

const systemConfig = ref({})

const loadSystemConfig = async () => {
  try {
    const data = await cauHinhService.getCauHinhHeThong()

    // Chuyển mảng thành Object để dễ truy xuất
    // Từ [{ maCauHinh: "A", giaTriSo: 100 }, ...] -> { A: 100, ... }
    systemConfig.value = data.reduce((acc, item) => {
      acc[item.maCauHinh] = item.giaTriSo
      return acc
    }, {})

    console.log('Config đã load:', systemConfig.value)
  } catch (error) {
    console.error('Lỗi load config:', error)
  }
}

// State bổ sung cho tính năng Tích Xu & Giảm giá bằng xu
const customerInfo = ref(null) // Chứa thông tin khách hàng (bao gồm soDuXu)
const soXuCanDung = ref(0)
const soXuSuDung = ref(0)
const tienGiamDoXu = ref(0)

// Hàm áp dụng số xu (giả định 1 xu = 1000 VNĐ hoặc theo cấu hình hệ thống của bạn)
const apDungSoXu = () => {
  const tyLeQuyDoi = Number(systemConfig.value['TY_LE_QUY_DOI_XU'] || 1000)
  const tyLeGiamToiDa = Number(systemConfig.value['TY_LE_GIAM_TOI_DA_XU'] || 50)
  const soDuHienTai = customerInfo.value?.soDuXu || 0

  let soXuMuonDung = Number(soXuCanDung.value)

  if (!soXuMuonDung || soXuMuonDung === 0) {
    huyBoSoXu()
    return
  }

  if (soXuMuonDung < 0) {
    toast.warning('Số xu sử dụng không hợp lệ!')
    soXuCanDung.value = 0
    return
  }

  // 1. Kiểm tra đủ xu trong tài khoản khách
  if (soDuHienTai < soXuMuonDung) {
    toast.warning(`Số dư xu của quý khách không đủ (Hiện có: ${soDuHienTai} xu)`)
    soXuCanDung.value = soDuHienTai
    soXuMuonDung = soDuHienTai
  }

  // 2. Tính số tiền giảm tối đa theo % đơn hàng (subtotal tương đương hd.getTongTienHang())
  const tienGiamToiDaTheoPhanTram = (subtotal.value * tyLeGiamToiDa) / 100

  // 3. Quy đổi mức trần % ra số xu tối đa (Làm tròn xuống)
  const maxXuTheoPhanTram = Math.floor(tienGiamToiDaTheoPhanTram / tyLeQuyDoi)

  // 4. Số xu tối đa không được vượt quá tổng tiền hàng hiện tại
  const maxXuTheoTongTien = Math.floor(subtotal.value / tyLeQuyDoi)

  // 5. Kiểm tra nếu đơn hàng quá nhỏ mà mức trần không đủ 1 xu
  if (soXuMuonDung > 0 && maxXuTheoPhanTram === 0) {
    toast.error(
      'Đơn hàng quá nhỏ, không đủ điều kiện để sử dụng xu (Mức giảm tối đa không đủ 1 xu quy đổi)!',
    )
    soXuCanDung.value = 0
    soXuSuDung.value = 0
    tienGiamDoXu.value = 0
    return
  }

  // 6. Giới hạn số xu thực tế được phép sử dụng (lấy min các điều kiện giống BE)
  let xuThucTeSuDung = Math.min(soXuMuonDung, soDuHienTai)
  xuThucTeSuDung = Math.min(xuThucTeSuDung, maxXuTheoPhanTram)
  xuThucTeSuDung = Math.min(xuThucTeSuDung, maxXuTheoTongTien)

  // Gán lại giá trị chuẩn xác cho các biến hiển thị
  soXuCanDung.value = xuThucTeSuDung
  soXuSuDung.value = xuThucTeSuDung

  // 7. Tính tiền giảm chính xác dựa trên số xu nguyên vẹn thực tế
  tienGiamDoXu.value = xuThucTeSuDung * tyLeQuyDoi

  toast.success(`Đã áp dụng ${soXuSuDung.value} xu (Giảm ${formatMoney(tienGiamDoXu.value)})`)
}

const huyBoSoXu = () => {
  soXuCanDung.value = 0
  soXuSuDung.value = 0
  tienGiamDoXu.value = 0
  toast.info('Đã hủy sử dụng xu.')
}

// 1. Tính số lượng khả dụng tối đa cho "Mua Ngay"
const maxAvailable = computed(() => {
  if (!product.value) return 0
  return product.value.soLuongKhaDung ?? product.value.soLuongTon ?? 0
})

// ================= LOGIC MUA NGAY =================
const increaseQty = () => {
  if (maxAvailable.value === 0) return
  if (quantity.value < maxAvailable.value) {
    quantity.value++
  } else {
    toast.warning(`Số lượng tối đa có thể mua là ${maxAvailable.value}`)
  }
}

const decreaseQty = () => {
  if (quantity.value > 1) quantity.value--
}

const validateQty = async () => {
  if (maxAvailable.value === 0) {
    quantity.value = 0
    return
  }

  if (quantity.value > maxAvailable.value) {
    // Load lại sản phẩm trước
    try {
      product.value = await getSanPhamChiTietById(spctId.value)

      const max = product.value.soLuongKhaDung ?? product.value.soLuongTon ?? 0

      if (max === 0) {
        quantity.value = 0
        return
      }

      if (quantity.value > max) {
        quantity.value = max
      }
    } catch (error) {
      console.error('Lỗi load lại sản phẩm:', error)
    }
  }
}
const onQtyBlur = () => {
  if (maxAvailable.value === 0) {
    quantity.value = 0
    return
  }
  if (!quantity.value || quantity.value < 1) {
    quantity.value = 1
  }
}

// ================= LOGIC GIỎ HÀNG =================
const increaseCartQty = (item) => {
  const max = item.soLuongKhaDung ?? item.soLuongTon ?? 0
  if (max === 0) return
  if (item.quantity < max) {
    item.quantity++
  } else {
    toast.warning(`Sản phẩm này chỉ còn ${max} sản phẩm khả dụng`)
  }
}

const decreaseCartQty = (item) => {
  if (item.quantity > 1) {
    item.quantity--
  }
}

const validateCartQty = (item) => {
  const max = item.soLuongKhaDung ?? item.soLuongTon ?? 0
  if (max === 0) {
    item.quantity = 0
    return
  }
  if (item.quantity > max) {
    item.quantity = max
    toast.warning(`Số lượng vượt quá số lượng khả dụng (${max})`)
  }
}

const onCartQtyBlur = (item) => {
  const max = item.soLuongKhaDung ?? item.soLuongTon ?? 0
  if (max === 0) {
    item.quantity = 0
    return
  }
  if (!item.quantity || item.quantity < 1) {
    item.quantity = 1
  }
}

// ================= LOGIC VOUCHER =================

const fetchVouchers = async () => {
  const voucherHeThong = (await getAllVoucher())
    .filter((v) => v.trangThai === 1)
    .map((v) => ({
      ...v,
      uid: `sys_${v.id}`,
      loaiVoucher: 'HE_THONG',
    }))

  let voucherCaNhan = []
  const currentUser = JSON.parse(sessionStorage.getItem('user'))

  if (currentUser?.idKhachHang) {
    // ❌ ĐÃ XÓA dòng: customerInfo.value = currentUser ở đây để tránh ghi đè dữ liệu soDuXu
    try {
      const res = await gamificationApi.getVoucherCuaToi(currentUser.idKhachHang)
      voucherCaNhan = (res.data || [])
        .filter((v) => v.trangThai === 'CHUA_DUNG')
        .map((v) => ({
          id: v.idKhoVoucher,
          idVoucherKhachHang: v.idVoucherKhachHang,
          tenVoucher: v.tenVoucher,
          maVoucher: v.maCode,
          giaTriDonHangToiThieu: v.dieuKienToiThieu,
          giaTriGiam: v.giaTriGiam,
          giaTriGiamToiDa: v.giaTriGiamToiDa,
          loaiGiamGia: v.loaiGiamGia,
          ngayBatDau: v.ngayBatDau,
          ngayKetThuc: v.ngayHetHan,
          moTa: v.moTa,
          trangThai: 1,
          loaiVoucher: 'CA_NHAN',
          uid: `per_${v.idVoucherKhachHang || v.idKhoVoucher}`,
        }))
    } catch (e) {
      console.error('Lỗi tải voucher cá nhân:', e)
    }
  }

  vouchers.value = [...voucherHeThong, ...voucherCaNhan]
}

const selectedVoucherId = ref(null)

const selectedVoucher = computed(() => {
  return vouchers.value.find((v) => v.uid === selectedVoucherId.value) || null
})

const sortedVouchers = computed(() => {
  return [...vouchers.value].sort((a, b) => {
    if (a.uid === selectedVoucherId.value) return -1
    if (b.uid === selectedVoucherId.value) return 1

    const aValid = subtotal.value >= a.giaTriDonHangToiThieu
    const bValid = subtotal.value >= b.giaTriDonHangToiThieu

    if (aValid !== bValid) {
      return bValid - aValid
    }

    return getVoucherDiscount(b) - getVoucherDiscount(a)
  })
})

const clearVoucher = () => {
  selectedVoucherId.value = null
  showVoucherModal.value = false
}

const getVoucherDiscount = (voucher) => {
  if (!voucher || subtotal.value < voucher.giaTriDonHangToiThieu) return 0

  if (voucher.loaiGiamGia === 'tien_mat') {
    return Number(voucher.giaTriGiam)
  }

  let discount = (subtotal.value * Number(voucher.giaTriGiam)) / 100

  if (voucher.giaTriGiamToiDa) {
    discount = Math.min(discount, Number(voucher.giaTriGiamToiDa))
  }

  return discount
}

const bestVoucher = computed(() => {
  return vouchers.value
    .filter((v) => subtotal.value >= v.giaTriDonHangToiThieu)
    .sort((a, b) => getVoucherDiscount(b) - getVoucherDiscount(a))[0]
})

const formatShortCurrency = (value) => {
  if (!value) return '0đ'
  if (value >= 1000000) {
    return (value / 1000000).toFixed(value % 1000000 === 0 ? 0 : 1) + 'tr'
  }
  if (value >= 1000) {
    return (value / 1000).toFixed(value % 1000 === 0 ? 0 : 0) + 'k'
  }
  return value + 'đ'
}

const showVoucherModal = ref(false)
const selectAddress = (id) => {
  selectedAddressId.value = id
  showAllAddresses.value = false
}

const formatCurrency = (value) => {
  return Number(value || 0).toLocaleString('vi-VN') + 'đ'
}

const showAllAddresses = ref(false)
const provinces = ref([])
const districts = ref([])
const wards = ref([])
const wardCache = ref([])

const selectedProvince = ref(null)
const selectedDistrict = ref(null)
const selectedWard = ref(null)

const showAddressModal = ref(false)
const editingAddress = ref(null)

const calculateShipping = async (address) => {
  if (!address.thanhPho || !address.quan || !address.phuong) {
    console.warn('Thiếu thông tin địa chỉ để tính phí ship')
    shippingFee.value = 0
    return
  }

  try {
    shippingLoading.value = true
    const fee = await getShippingFee({
      thanhPho: address.thanhPho,
      quan: address.quan,
      phuong: address.phuong,
    })
    shippingFee.value = fee
  } catch (error) {
    console.error('Lỗi tính ship', error)
    toast.error('Không thể tính phí vận chuyển cho địa chỉ này')
    shippingFee.value = 0
  } finally {
    shippingLoading.value = false
  }
}

const loading = ref(false)
let map = null
let marker = null

const addresses = ref([])
const selectedAddressId = ref(null)
const toast = useToast()
const route = useRoute()
const router = useRouter()
const authToken = sessionStorage.getItem('token')
const isLoggedIn = Boolean(authToken)

const spctId = computed(() => {
  return route.query.spct ? Number(route.query.spct) : null
})

const quantity = ref(Number(route.query.qty) || 1)
const product = ref(null)
const checkoutItems = ref([])
const isCartCheckout = ref(false)
const vouchers = ref([])

async function loadData() {
  product.value = await getSanPhamChiTietById(spctId.value)

  const max = product.value.soLuongKhaDung ?? product.value.soLuongTon ?? 0

  if (max === 0) {
    quantity.value = 0
    // toast.warning('Sản phẩm này hiện đã hết hàng.')
  } else if (quantity.value > max) {
    quantity.value = max
    // toast.warning('Số lượng sản phẩm vượt quá hàng sẵn có, đã tự động điều chỉnh.')
  }

  await fetchVouchers()

  if (isLoggedIn) {
    await loadAddresses()
  }
  provinces.value = await getProvinces()
}

onMounted(async () => {
  loadSystemConfig()

  const userSession = sessionStorage.getItem('user')

  if (userSession) {
    try {
      const user = JSON.parse(userSession)

      // Gọi api detail bằng ID của khách hàng
      // Lưu ý: Nếu user object không có id trực tiếp, hãy kiểm tra xem nó là user.id hay user.idKhachHang
      const data = await detailKhachHangService(user.idKhachHang)

      // Gán dữ liệu vào biến customerInfo
      customerInfo.value = data

      console.log('Thông tin khách hàng:', data)
    } catch (error) {
      console.error('Lỗi khi tải thông tin khách hàng:', error)
    }
  }

  if (spctId.value) {
    isCartCheckout.value = false
    await loadData()
    connectSocket()
    return
  }

  const checkout = sessionStorage.getItem('checkoutData')

  if (checkout) {
    isCartCheckout.value = true
    checkoutItems.value = JSON.parse(checkout).items.map((item) => {
      const max = item.soLuongKhaDung ?? item.soLuongTon ?? 0
      return {
        ...item,
        quantity: max === 0 ? 0 : item.quantity || 1,
      }
    })

    await fetchVouchers()

    if (isLoggedIn) {
      await loadAddresses()
    }

    provinces.value = await getProvinces()
    connectSocket()
    return
  }

  router.push('/')
})

function connectSocket() {
  if (stompClient.connected) {
    subscribeOrder()
  } else {
    stompClient.onConnect = () => {
      console.log('✅ Connected')
      subscribeOrder()
    }
  }
}

function subscribeOrder() {
  stompClient.subscribe('/topic/pos', async (msg) => {
    const event = JSON.parse(msg.body)

    switch (event.type) {
      case 'DISCOUNT_UPDATED':
        product.value = await getSanPhamChiTietById(spctId.value)
        const max = product.value.soLuongKhaDung ?? product.value.soLuongTon ?? 0
        if (max === 0) {
          quantity.value = 0
          toast.warning('Sản phẩm đã hết hàng!')
        }
        break

      case 'VOUCHER_UPDATED':
        await fetchVouchers()

        if (selectedVoucherId.value) {
          const latest = vouchers.value.find((v) => v.uid === selectedVoucherId.value)

          if (!latest || latest.trangThai !== 1) {
            selectedVoucherId.value = null
            toast.warning('Voucher không còn khả dụng.')
            break
          }

          if (subtotal.value < latest.giaTriDonHangToiThieu) {
            selectedVoucherId.value = null
            toast.warning('Voucher không còn đủ điều kiện áp dụng.')
          }
        }
        break
      case 'KHO_VOUCHER_UPDATED':
        await fetchVouchers()

        if (selectedVoucherId.value) {
          const latest = vouchers.value.find((v) => v.id === selectedVoucherId.value)
          const now = new Date()

          if (
            !latest ||
            latest.trangThai === false ||
            (latest.soLuongConLai !== null && latest.soLuongConLai <= 0)
          ) {
            selectedVoucherId.value = null
            toast.warning(
              'Voucher bạn đang chọn đã bị Admin ngưng hoạt động hoặc đã hết lượt sử dụng!',
            )
            break
          }

          if (latest.ngayHetHan && new Date(latest.ngayHetHan) < now) {
            selectedVoucherId.value = null
            toast.warning('Voucher bạn đang chọn đã hết hạn sử dụng.')
            break
          }

          const dieuKienToiThieu = latest.dieuKienToiThieu ? Number(latest.dieuKienToiThieu) : 0
          if (subtotal.value < dieuKienToiThieu) {
            selectedVoucherId.value = null
            toast.warning(
              'Đơn hàng không còn đủ điều kiện tối thiểu theo mức quy định mới của voucher.',
            )
            break
          }

          toast.info('Thông tin hoặc mức giảm của voucher vừa được Admin cập nhật lại.')
        }
        break
    }
  })
}

const shippingFee = ref(0)
const shippingLoading = ref(false)
const note = ref('')

const subtotal = computed(() => {
  if (isCartCheckout.value) {
    return checkoutItems.value.reduce((sum, item) => sum + item.giaBan * item.quantity, 0)
  }

  return (product.value?.giaSauGiam || product.value?.giaBan || 0) * quantity.value
})

const voucherDiscount = computed(() => {
  const v = selectedVoucher.value
  if (!v || subtotal.value < v.giaTriDonHangToiThieu) return 0
  return v.loaiGiamGia === 'phan_tram'
    ? Math.min((subtotal.value * v.giaTriGiam) / 100, v.giaTriGiamToiDa ?? Number.POSITIVE_INFINITY)
    : v.giaTriGiam
})

// Tổng thanh toán đã bao gồm trừ tiền từ xu
const total = computed(() =>
  Math.max(subtotal.value + shippingFee.value - voucherDiscount.value - tienGiamDoXu.value, 0),
)

const formatMoney = (value) => Number(value || 0).toLocaleString('vi-VN') + ' đ'
const isPlacingOrder = ref(false)

const validateGuestCheckout = () => {
  if (!addressForm.value.tenNguoiNhan.trim()) {
    toast.warning('Vui lòng nhập họ tên người nhận')
    return false
  }

  if (!/^[0-9]{9,11}$/.test(addressForm.value.soDienThoai.trim())) {
    toast.warning('Số điện thoại phải gồm 9 đến 11 chữ số')
    return false
  }

  if (
    !addressForm.value.thanhPho ||
    !addressForm.value.quan ||
    !addressForm.value.phuong ||
    !addressForm.value.diaChiCuThe.trim()
  ) {
    toast.warning('Vui lòng nhập đầy đủ địa chỉ giao hàng')
    return false
  }

  return true
}

const placeOrder = async () => {
  if (isPlacingOrder.value) return

  if (isLoggedIn && !selectedAddressId.value) {
    toast.warning('Vui lòng chọn địa chỉ giao hàng')
    return
  }

  if (!isLoggedIn && !validateGuestCheckout()) {
    return
  }

  const selectedVoucherObj = selectedVoucher.value

  // Payload gửi lên backend (đã tích hợp thêm thông tin sử dụng xu)
  const body = {
    addressId: isLoggedIn ? selectedAddressId.value : null,
    tenNguoiNhan: isLoggedIn ? null : addressForm.value.tenNguoiNhan,
    soDienThoaiNguoiNhan: isLoggedIn ? null : addressForm.value.soDienThoai,
    thanhPho: isLoggedIn ? null : addressForm.value.thanhPho,
    quan: isLoggedIn ? null : addressForm.value.quan,
    phuong: isLoggedIn ? null : addressForm.value.phuong,
    diaChiCuThe: isLoggedIn ? null : addressForm.value.diaChiCuThe,
    districtId: isLoggedIn ? null : addressForm.value.districtId,
    wardCode: isLoggedIn ? null : addressForm.value.wardCode,
    shippingFee: shippingFee.value,
    voucherId:
      selectedVoucherObj?.loaiVoucher === 'CA_NHAN' ? null : (selectedVoucherObj?.id ?? null),
    voucherKhachHangId:
      selectedVoucherObj?.loaiVoucher === 'CA_NHAN' ? selectedVoucherObj.idVoucherKhachHang : null,
    soXuSuDung: soXuSuDung.value,
    tienGiamDoXu: tienGiamDoXu.value,
    note: note.value,
    items: isCartCheckout.value
      ? checkoutItems.value.map((item) => ({
          productDetailId: item.productDetailId,
          quantity: item.quantity,
        }))
      : [
          {
            productDetailId: spctId.value,
            quantity: quantity.value,
            price: product.value?.giaBan,
          },
        ],
  }

  try {
    isPlacingOrder.value = true
    const res = await taoHoaDonOnline(body, authToken)
    toast.success(`Đặt hàng thành công. Mã đơn: ${res.maHoaDon}`)
    if (isCartCheckout.value) {
      sessionStorage.removeItem('checkoutData')
    }
    sessionStorage.setItem('orderProduct', JSON.stringify(product.value))
    sessionStorage.setItem('lastGuestOrderCode', res.maHoaDon)

    router.push({
      path: '/payment',
      query: { id: res.id, maHoaDon: res.maHoaDon, qrUrl: res.qrUrl },
    })
  } catch (error) {
    toast.error(error.message || 'Đặt hàng thất bại ❌')
    if (spctId.value) {
      await loadData()
    } else if (isCartCheckout.value) {
      const checkout = sessionStorage.getItem('checkoutData')
      if (checkout) {
        checkoutItems.value = JSON.parse(checkout).items.map((item) => {
          const max = item.soLuongKhaDung ?? item.soLuongTon ?? 0
          return {
            ...item,
            quantity: max === 0 ? 0 : item.quantity || 1,
          }
        })
      }
    }
  } finally {
    isPlacingOrder.value = false
  }
}

const initMap = () => {
  if (map) {
    map.remove()
    map = null
  }

  map = L.map('map').setView([21.0285, 105.8542], 13)
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap',
  }).addTo(map)

  if (editingAddress.value && editingAddress.value.latitude) {
    addMarker(editingAddress.value.latitude, editingAddress.value.longitude)
  }
}

const addMarker = (lat, lng) => {
  if (marker) map.removeLayer(marker)
  marker = L.marker([lat, lng], {
    icon: L.icon({
      iconUrl: icon,
      shadowUrl: shadow,
      iconSize: [25, 41],
      iconAnchor: [12, 41],
    }),
  }).addTo(map)
  map.setView([lat, lng], 16)
}

const getWardsCached = async (districtId) => {
  if (wardCache[districtId]) {
    return wardCache[districtId]
  }
  const data = await getWards(districtId)
  wardCache[districtId] = data
  return data
}

const addressForm = ref({
  tenNguoiNhan: '',
  soDienThoai: '',
  thanhPho: '',
  quan: '',
  phuong: '',
  diaChiCuThe: '',
  districtId: null,
  wardCode: '',
  latitude: null,
  longitude: null,
  macDinh: false,
})

const getCurrentLocation = () => {
  loading.value = true

  navigator.geolocation.getCurrentPosition(
    async (pos) => {
      try {
        const { latitude, longitude } = pos.coords
        addressForm.value.latitude = latitude
        addressForm.value.longitude = longitude

        map.setView([latitude, longitude], 16)
        if (marker) {
          map.removeLayer(marker)
        }
        marker = L.marker([latitude, longitude], {
          icon: L.icon({
            iconUrl: icon,
            shadowUrl: shadow,
            iconSize: [25, 41],
            iconAnchor: [12, 41],
          }),
        }).addTo(map)

        const response = await fetch(
          `https://nominatim.openstreetmap.org/reverse?format=json&addressdetails=1&zoom=18&lat=${latitude}&lon=${longitude}`,
        )
        const data = await response.json()

        if (!data.address) return
        const a = data.address

        addressForm.value.diaChiCuThe = [
          a.house_number,
          a.road,
          a.neighbourhood,
          a.residential,
          a.quarter,
        ]
          .filter(Boolean)
          .join(', ')

        if (!addressForm.value.diaChiCuThe) {
          addressForm.value.diaChiCuThe = a.road || ''
        }

        const rawProvince = a.state || a.province || a.region || ''
        const rawDistrict =
          a.county || a.city_district || a.district || a.municipality || a.city || ''
        const rawWard = a.suburb || a.ward || a.town || a.village || a.neighbourhood || ''

        const cleanText = (str) =>
          str
            .replace(/^(thành phố|tỉnh|quận|huyện|thị xã|phường|xã|thị trấn)\s+/i, '')
            .trim()
            .toLowerCase()

        const targetProvince = cleanText(rawProvince)
        const targetDistrict = cleanText(rawDistrict)
        const targetWard = cleanText(rawWard)

        const foundProvince = provinces.value.find((p) => {
          const pName = cleanText(p.ProvinceName)
          return pName.includes(targetProvince) || targetProvince.includes(pName)
        })

        if (!foundProvince) {
          toast.warning('Không tự động xác định được Tỉnh/Thành phố. Vui lòng chọn thủ công.')
          return
        }

        selectedProvince.value = foundProvince
        addressForm.value.thanhPho = foundProvince.ProvinceName

        districts.value = await getDistricts(foundProvince.ProvinceID)

        const foundDistrict = districts.value.find((d) => {
          const dName = cleanText(d.DistrictName)
          return dName.includes(targetDistrict) || targetDistrict.includes(dName)
        })

        if (foundDistrict) {
          selectedDistrict.value = foundDistrict
          addressForm.value.quan = foundDistrict.DistrictName
          addressForm.value.districtId = foundDistrict.DistrictID

          wards.value = await getWardsCached(foundDistrict.DistrictID)

          const foundWard = wards.value.find((w) => {
            const wName = cleanText(w.WardName)
            return wName.includes(targetWard) || targetWard.includes(wName)
          })

          if (foundWard) {
            selectedWard.value = foundWard
            addressForm.value.phuong = foundWard.WardName
            addressForm.value.wardCode = foundWard.WardCode
          }
        }

        toast.success('Đã định vị và điền địa chỉ thành công!')
      } catch (error) {
        console.error('Lỗi lấy vị trí:', error)
        toast.error('Không thể tự động phân giải vị trí hiện tại.')
      } finally {
        loading.value = false
      }
    },
    () => {
      toast.error('Vui lòng cấp quyền truy cập vị trí trên trình duyệt.')
      loading.value = false
    },
    { enableHighAccuracy: true },
  )
}

const resetAddressForm = () => {
  addressForm.value = {
    tenNguoiNhan: '',
    soDienThoai: '',
    thanhPho: '',
    quan: '',
    phuong: '',
    diaChiCuThe: '',
    districtId: null,
    wardCode: '',
    latitude: null,
    longitude: null,
    macDinh: false,
  }
  editingAddress.value = null
}

const loadAddresses = async () => {
  if (!isLoggedIn) return
  try {
    addresses.value = await getAllDiaChi()
    const selected = addresses.value.find((item) => item.macDinh) || addresses.value[0]
    selectedAddressId.value = selected?.id ?? null
  } catch (e) {
    console.error(e)
    toast.error('Không tải được địa chỉ')
  }
}

const openAddAddress = () => {
  resetAddressForm()
  if (addresses.value.length >= 3) {
    toast.warning('Bạn chỉ được lưu tối đa 3 địa chỉ')
    return
  }
  editingAddress.value = null
  showAddressModal.value = true
}

const openEditAddress = (item) => {
  editingAddress.value = item
  addressForm.value = {
    tenNguoiNhan: item.tenNguoiNhan,
    soDienThoai: item.soDienThoai,
    thanhPho: item.thanhPho,
    quan: item.quan,
    phuong: item.phuong,
    districtId: item.districtId,
    wardCode: item.wardCode,
    latitude: item.latitude,
    longitude: item.longitude,
    diaChiCuThe: item.diaChiCuThe,
    macDinh: item.macDinh,
  }
  showAddressModal.value = true
}

const saveAddress = async () => {
  try {
    if (editingAddress.value) {
      await capNhatDiaChi(editingAddress.value.id, addressForm.value)
      toast.success('Cập nhật thành công')
    } else {
      await themDiaChi(addressForm.value)
      toast.success('Thêm địa chỉ thành công')
    }

    showAddressModal.value = false
    await loadAddresses()
    resetAddressForm()

    selectedProvince.value = null
    selectedDistrict.value = null
    selectedWard.value = null
    districts.value = []
    wards.value = []
  } catch (e) {
    console.error(e)
    toast.error(e.message)
  }
}

const deleteAddress = async (id) => {
  if (!confirm('Xóa địa chỉ này?')) return
  try {
    await xoaDiaChi(id)
    toast.success('Đã xóa')
    await loadAddresses()
  } catch (e) {
    toast.error(e.message)
  }
}

const setDefault = async (id) => {
  try {
    await doiMacDinh(id)
    toast.success('Đã đổi địa chỉ mặc định')
    await loadAddresses()
  } catch (e) {
    toast.error(e.message)
  }
}

const onProvinceChange = async () => {
  selectedDistrict.value = null
  selectedWard.value = null
  districts.value = []
  wards.value = []
  shippingFee.value = 0

  addressForm.value.quan = ''
  addressForm.value.phuong = ''
  addressForm.value.districtId = null
  addressForm.value.wardCode = ''

  if (!selectedProvince.value) {
    addressForm.value.thanhPho = ''
    return
  }

  addressForm.value.thanhPho = selectedProvince.value.ProvinceName
  districts.value = await getDistricts(selectedProvince.value.ProvinceID)
}

const onDistrictChange = async () => {
  selectedWard.value = null
  wards.value = []
  shippingFee.value = 0
  addressForm.value.phuong = ''
  addressForm.value.wardCode = ''

  if (!selectedDistrict.value) {
    addressForm.value.quan = ''
    addressForm.value.districtId = null
    return
  }

  addressForm.value.quan = selectedDistrict.value.DistrictName
  addressForm.value.districtId = selectedDistrict.value.DistrictID
  wards.value = await getWards(selectedDistrict.value.DistrictID)
}

const onWardChange = async () => {
  shippingFee.value = 0

  if (!selectedWard.value) {
    addressForm.value.phuong = ''
    addressForm.value.wardCode = ''
    return
  }

  addressForm.value.phuong = selectedWard.value.WardName
  addressForm.value.wardCode = selectedWard.value.WardCode

  if (!isLoggedIn) {
    await calculateShipping(addressForm.value)
  }
}

watch(showAddressModal, (val) => {
  if (val) {
    nextTick(() => {
      initMap()
      map?.invalidateSize()
    })
  } else {
    if (map) {
      map.remove()
      map = null
    }
  }
})

watch(selectedAddressId, async (id) => {
  if (!id) return
  const address = addresses.value.find((x) => x.id === id)
  if (!address) return
  await calculateShipping(address)
})
</script>

<style scoped>
.checkout-page {
  background-image:
    linear-gradient(rgba(255, 255, 255, 0.72), rgba(248, 250, 252, 0.9)),
    radial-gradient(circle at 1px 1px, rgba(99, 102, 241, 0.12) 1px, transparent 0);
  background-size:
    auto,
    24px 24px;
}
.custom-scrollbar::-webkit-scrollbar {
  width: 6px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 4px;
}
.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}
</style>
