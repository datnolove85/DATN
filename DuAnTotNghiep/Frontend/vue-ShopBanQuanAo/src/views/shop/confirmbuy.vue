<template>
  <!-- THÊM antialiased để toàn bộ chữ mượt và sắc nét như Figma, đổi màu chữ gốc tối hơn chút (#222) -->
  <div class="min-h-screen bg-white text-[#222] antialiased">
    <!-- =========================
         LOADING
    ========================== -->
    <div v-if="loading" class="min-h-[60vh] flex items-center justify-center">
      <div class="flex flex-col items-center gap-3 text-gray-500">
        <div
          class="w-7 h-7 border-2 border-gray-200 border-t-[#222] rounded-full animate-spin"
        ></div>
        <span class="text-sm font-medium">Đang tải sản phẩm...</span>
      </div>
    </div>

    <!-- =========================
         PRODUCT DETAIL
    ========================== -->
    <main v-else class="max-w-[1320px] mx-auto px-4 sm:px-6 lg:px-8 pt-1 pb-4 lg:pt-2 lg:pb-5">
      <!-- BREADCRUMB NAVIGATION -->
      <div
        class="relative left-1/2 right-1/2 -ml-[50vw] -mr-[50vw] w-screen border-y border-gray-300 bg-white mb-6"
      >
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-3">
          <nav class="text-[14px] sm:text-[15px] flex items-center flex-wrap gap-2 text-gray-600">
            <router-link
              to="/"
              class="text-[#00a884] hover:underline font-normal whitespace-nowrap"
            >
              Trang chủ
            </router-link>
            <span class="text-gray-400">/</span>
            <router-link
              :to="{ path: '/san-pham', query: { brand: selectedVariant?.tenThuongHieu } }"
              class="text-[#00a884] hover:underline font-normal whitespace-nowrap"
            >
              {{ selectedVariant?.tenThuongHieu || 'Wolf Calie' }}
            </router-link>
            <span class="text-gray-400">/</span>
            <span class="text-gray-800 font-normal">
              {{ product?.productName }}
            </span>
          </nav>
        </div>
      </div>

      <!-- MAIN LAYOUT -->
      <div
        class="grid grid-cols-1 lg:grid-cols-[minmax(0,1.3fr)_minmax(350px,1fr)] gap-8 xl:gap-10 items-stretch"
      >
        <!-- =====================================================
             LEFT: GALLERY (Bo góc và đường viền thanh lịch cho ảnh)
        ====================================================== -->
        <section class="min-w-0 flex flex-col justify-between h-full">
          <!-- MAIN IMAGE -->
          <div
            class="relative bg-[#f6f6f6] overflow-hidden flex-1 flex items-center justify-center min-h-[400px] rounded-[10px] border border-gray-200/80 shadow-sm"
          >
            <img
              v-if="mainImage"
              :src="mainImage"
              :alt="product?.productName || 'Sản phẩm'"
              class="w-full h-full object-cover absolute inset-0 main-product-image"
            />

            <div
              v-else
              class="w-full h-full flex items-center justify-center text-sm text-gray-500"
            >
              Không có ảnh sản phẩm
            </div>
          </div>

          <!-- THUMBNAILS -->
          <div
            v-if="galleryImages.length > 0"
            class="mt-3 flex gap-2 overflow-x-auto pb-1 shrink-0"
          >
            <button
              v-for="img in galleryImages"
              :key="img.image"
              type="button"
              @click="selectImage(img)"
              class="relative shrink-0 w-[80px] sm:w-[90px] aspect-square overflow-hidden bg-[#f6f6f6] border rounded-[6px] transition-all"
              :class="
                mainImage === img.url
                  ? 'border-[#222] ring-1 ring-[#222]'
                  : 'border-gray-200 hover:border-gray-400'
              "
            >
              <img
                :src="img.url"
                :alt="product?.productName || 'Sản phẩm'"
                class="w-full h-full object-cover"
              />
            </button>
          </div>
        </section>

        <!-- =====================================================
             RIGHT: PRODUCT INFO
        ====================================================== -->
        <section class="min-w-0 lg:pt-0 flex flex-col justify-between">
          <div>
            <!-- NAME + META -->
            <div class="pb-2">
              <h1
                class="text-[24px] sm:text-[27px] lg:text-[28px] font-semibold leading-[1.2] tracking-[-0.01em] text-[#111]"
              >
                {{ product?.productName }}
              </h1>

              <div class="mt-3 space-y-2 text-[14px] sm:text-[15px]">
                <div>
                  <span class="text-gray-600">Mã: </span>
                  <span class="italic text-gray-800 break-all">
                    {{ selectedVariant?.maSanPhamChiTiet || 'Đang cập nhật' }}
                  </span>
                </div>

                <div>
                  <span class="text-gray-600">Thương hiệu: </span>
                  <span class="font-medium text-[#00a884]">
                    {{ selectedVariant?.tenThuongHieu || 'Wolf Calie' }}
                  </span>
                </div>

                <div>
                  <span class="text-gray-600">Tình trạng: </span>
                  <span
                    class="inline-flex items-center bg-[#15945c] text-white px-2.5 py-[3px] rounded-[4px] text-[11px] font-bold ml-1"
                  >
                    Còn hàng
                  </span>
                </div>
              </div>
            </div>

            <!-- PRICE -->
            <div class="pt-2 pb-4 border-b border-[#e6e6e6]">
              <div class="flex items-end flex-wrap gap-x-3 gap-y-1">
                <span class="text-[27px] sm:text-[30px] font-bold text-[#bd2228] leading-none">
                  {{
                    (selectedVariant?.giaSauGiam || selectedVariant?.giaBan || 0).toLocaleString(
                      'vi-VN',
                    )
                  }}đ
                </span>

                <span
                  v-if="selectedVariant?.dangGiamGia"
                  class="text-[15px] text-gray-500 line-through"
                >
                  {{ (selectedVariant?.giaBan || 0).toLocaleString('vi-VN') }}đ
                </span>
              </div>

              <div v-if="selectedVariant?.dangGiamGia" class="mt-2 text-[14px] text-gray-600">
                Tiết kiệm
                <span class="text-[#00a884] font-medium">
                  {{
                    (
                      (selectedVariant?.giaBan || 0) -
                      (selectedVariant?.giaSauGiam || selectedVariant?.giaBan || 0)
                    ).toLocaleString('vi-VN')
                  }}đ
                </span>
                so với giá thị trường
              </div>
            </div>

            <!-- PROMOTION -->
            <div class="relative mt-4">
              <div
                class="border-2 border-[#ff625c] bg-[#fffafa] rounded-[8px] px-4 pt-6 pb-4 shadow-sm"
              >
                <div
                  class="absolute -top-[13px] left-4 bg-[#ff625c] text-white px-3 py-[6px] rounded-[4px] text-[12px] font-semibold flex items-center gap-1.5"
                >
                  <span>🎁</span>
                  <span>Khuyến mãi đặc biệt</span>
                </div>

                <div class="space-y-2 text-[13px] sm:text-[14px] leading-[1.55] text-gray-800">
                  <div class="flex items-start gap-2">
                    <span class="font-bold text-gray-900">✓</span>
                    <span>
                      Giảm <strong>10%</strong> cho đơn hàng từ <strong>3 sản phẩm</strong> trở lên.
                    </span>
                  </div>

                  <div class="flex items-start gap-2">
                    <span class="font-bold text-gray-900">✓</span>
                    <span>
                      <strong>Miễn phí giao hàng</strong> toàn quốc cho đơn hàng trên
                      <strong>500.000 VNĐ</strong>.
                    </span>
                  </div>

                  <div class="flex items-start gap-2">
                    <span class="font-bold text-gray-900">✓</span>
                    <span> Tặng ngay <strong>voucher 50.000 VNĐ</strong> cho khách hàng mới. </span>
                  </div>
                </div>
              </div>
            </div>

            <!-- COLOR -->
            <div class="mt-6">
              <div class="text-[14px] sm:text-[15px] mb-2.5">
                <span class="font-semibold text-[#111]">Màu sắc:</span>
                <span class="font-bold text-[#00a884] ml-1.5 uppercase tracking-wide">
                  {{ selectedColor?.name }}
                </span>
              </div>

              <div class="flex flex-wrap gap-2">
                <button
                  v-for="color in product?.colors"
                  :key="color.id"
                  type="button"
                  @click="selectColor(color)"
                  :title="color.name"
                  class="relative w-[42px] h-[42px] border rounded-[6px] transition-all"
                  :class="
                    selectedColor?.id === color.id
                      ? 'border-[#222] ring-2 ring-[#222]/10'
                      : 'border-gray-300 hover:border-gray-700'
                  "
                  :style="{ backgroundColor: getColorStyle(color.name) }"
                >
                  <span
                    v-if="selectedColor?.id === color.id"
                    class="absolute inset-0 flex items-center justify-center"
                  >
                    <span
                      class="w-[18px] h-[18px] rounded-full bg-white/90 flex items-center justify-center shadow"
                    >
                      <span class="text-[10px] font-bold text-gray-900"> ✓ </span>
                    </span>
                  </span>
                </button>
              </div>
            </div>

            <!-- SIZE (Đã Highlight text size khi chọn) -->
            <div class="mt-6">
              <div class="flex items-center justify-between gap-3 mb-2.5">
                <div class="text-[14px] sm:text-[15px]">
                  <span class="font-semibold text-[#111]">Size:</span>
                  <span class="font-bold text-[#00a884] ml-1.5 uppercase tracking-wide">
                    {{ selectedVariant?.tenKichThuoc }}
                  </span>
                </div>

                <button
                  type="button"
                  @click="showSizeModal = true"
                  class="text-[13px] sm:text-[14px] text-[#0878f2] font-medium hover:underline flex items-center gap-1"
                >
                  <span>▦</span>
                  <span>Gợi ý tìm size</span>
                </button>
              </div>

              <div class="flex flex-wrap gap-2">
                <button
                  v-for="variant in selectedColor?.variants"
                  :key="variant.id"
                  type="button"
                  :disabled="getVariantStock(variant) === 0"
                  @click="getVariantStock(variant) > 0 && selectVariant(variant)"
                  class="min-w-[46px] h-[42px] px-3.5 border rounded-[6px] text-[14px] font-medium transition-all"
                  :class="[
                    getVariantStock(variant) === 0
                      ? 'bg-gray-100 text-gray-400 border-gray-200 cursor-not-allowed line-through'
                      : selectedVariant?.id === variant.id
                        ? 'bg-[#222] text-white border-[#222]'
                        : 'bg-white text-gray-800 border-gray-300 hover:border-[#222]',
                  ]"
                >
                  {{ variant.tenKichThuoc }}
                </button>
              </div>
            </div>

            <!-- QUANTITY -->
            <div class="mt-6">
              <div class="flex items-center gap-4">
                <span class="text-[14px] sm:text-[15px] font-semibold text-[#111]">
                  Số lượng:
                </span>

                <div
                  class="flex items-center h-[40px] border border-gray-300 rounded-[6px] overflow-hidden"
                >
                  <button
                    type="button"
                    @click="decreaseQty"
                    :disabled="quantity <= 1"
                    class="w-10 h-full text-[18px] text-gray-600 hover:bg-gray-50 disabled:text-gray-300"
                  >
                    −
                  </button>

                  <input
                    type="number"
                    min="1"
                    :max="availableStock"
                    :value="quantity"
                    @keydown="restrictNumberKeys"
                    @input="quantity = Number($event.target.value.replace(/\D/g, ''))"
                    @blur="validateQuantity"
                    class="w-14 h-full text-center border-x border-gray-300 text-[14px] font-medium text-gray-800 focus:outline-none bg-transparent [appearance:textfield] [&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none"
                  />

                  <button
                    type="button"
                    @click="increaseQty"
                    :disabled="!selectedVariant || quantity >= availableStock"
                    class="w-10 h-full text-[18px] text-gray-600 hover:bg-gray-50 disabled:text-gray-300"
                  >
                    +
                  </button>
                </div>
              </div>

              <!-- Thông tin tồn kho & Cảnh báo vượt quá -->
              <div class="mt-2 flex flex-col gap-0.5">
                <div class="text-[13px] text-[#15945c] font-medium">
                  Còn {{ availableStock }} sản phẩm
                </div>
                <div
                  v-if="quantity > availableStock && availableStock > 0"
                  class="text-[13px] text-rose-500 font-medium"
                >
                  Vượt quá số lượng khả dụng!
                </div>
              </div>
            </div>
          </div>

          <!-- ACTION & AI BUTTONS GROUP -->
          <div class="mt-6 pt-2">
            <div class="grid grid-cols-2 gap-2.5">
              <button
                type="button"
                @click="buyNow"
                :disabled="!selectedVariant || availableStock === 0"
                class="min-h-[54px] rounded-[6px] bg-[#00a884] text-white hover:bg-[#009675] disabled:bg-gray-300 disabled:text-gray-500 flex flex-col items-center justify-center transition-all shadow-sm"
              >
                <span class="text-[14px] font-bold"> Mua ngay </span>
              </button>

              <button
                type="button"
                @click="addToCart"
                :disabled="!selectedVariant || availableStock === 0 || isAddingToCart"
                class="min-h-[54px] rounded-[6px] bg-[#df3440] text-white hover:bg-[#ce2d38] disabled:bg-gray-300 disabled:text-gray-500 flex items-center justify-center gap-1.5 text-[14px] font-semibold transition-all duration-300 active:scale-95 relative overflow-hidden shadow-sm"
                :class="{ 'bg-[#00a884] scale-95': isAddingToCart }"
              >
                <span
                  class="inline-flex items-center gap-1.5 transition-transform duration-300"
                  :class="{ 'scale-105': isAddingToCart }"
                >
                  <span class="text-[16px]">{{ isAddingToCart ? '✓' : '🛒' }}</span>
                  <span>{{ isAddingToCart ? 'Đã thêm vào giỏ!' : 'Thêm vào giỏ' }}</span>
                </span>
              </button>
            </div>

            <!-- AI -->
            <button
              type="button"
              :disabled="!selectedVariant"
              @click="showTryOn = true"
              class="mt-2.5 w-full min-h-[44px] rounded-[6px] bg-[#6d42d9] text-white hover:bg-[#5e35c3] disabled:bg-gray-300 disabled:text-gray-500 flex items-center justify-center gap-2 text-[14px] font-semibold transition-all shadow-sm"
            >
              <span>✨</span>
              <span>Thử đồ bằng AI</span>
            </button>
          </div>
        </section>
      </div>

      <!-- =====================================================
           COMMITMENT
      ====================================================== -->
      <section class="mt-12">
        <div class="flex items-center gap-4 mb-4">
          <h2 class="text-[14px] font-bold tracking-[0.08em] whitespace-nowrap text-gray-900">
            K-ZONE CAM KẾT
          </h2>
          <div class="h-px bg-gray-200 flex-1"></div>
        </div>

        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          <!-- Cam kết 1: Huy hiệu / Chất lượng -->
          <div
            class="p-4 flex items-center gap-3 border border-gray-200 rounded-[8px] bg-white shadow-sm"
          >
            <div
              class="w-10 h-10 shrink-0 border border-gray-200 bg-gray-50 rounded-[6px] flex items-center justify-center text-gray-800"
            >
              <svg
                class="w-5 h-5"
                fill="none"
                stroke="currentColor"
                stroke-width="1.5"
                viewBox="0 0 24 24"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="M11.48 3.499a.562.562 0 011.04 0l2.125 5.111a.563.563 0 00.475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 00-.182.557l1.285 5.385a.562.562 0 01-.84.61l-4.725-2.885a.563.563 0 00-.586 0L6.982 20.54a.562.562 0 01-.84-.61l1.285-5.386a.562.562 0 00-.182-.557l-4.204-3.602a.563.563 0 01.321-.988l5.518-.442a.563.563 0 00.475-.345L11.48 3.5z"
                />
              </svg>
            </div>
            <div class="text-[13px] sm:text-[14px] text-gray-700 leading-[1.4]">
              Cam kết sản phẩm đúng mô tả, chất liệu cao cấp.
            </div>
          </div>

          <!-- Cam kết 2: Giao hàng -->
          <div
            class="p-4 flex items-center gap-3 border border-gray-200 rounded-[8px] bg-white shadow-sm"
          >
            <div
              class="w-10 h-10 shrink-0 border border-gray-200 bg-gray-50 rounded-[6px] flex items-center justify-center text-gray-800"
            >
              <svg
                class="w-5 h-5"
                fill="none"
                stroke="currentColor"
                stroke-width="1.5"
                viewBox="0 0 24 24"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="M8.25 18.75a1.5 1.5 0 01-3 0m3 0a1.5 1.5 0 00-3 0m3 0h6m-9 0H3.375a1.125 1.125 0 01-1.125-1.125V14.25m17.25 4.5a1.5 1.5 0 01-3 0m3 0a1.5 1.5 0 00-3 0m3 0h1.125c.621 0 1.129-.504 1.09-1.124a17.902 17.902 0 00-3.213-9.193 2.056 2.056 0 00-1.58-.86H14.25M16.5 18.75h-2.25m0-11.177v-.958c0-.568-.422-1.048-.987-1.106a48.554 48.554 0 00-10.026 0 1.106 1.106 0 00-.987 1.106v7.635m12-6.677v6.677m0 4.5v-4.5m0 0h-6"
                />
              </svg>
            </div>
            <div class="text-[13px] sm:text-[14px] text-gray-700 leading-[1.4]">
              Giao trong 3-5 ngày và freeship đơn từ 498k
            </div>
          </div>

          <!-- Cam kết 3: Đổi trả -->
          <div
            class="p-4 flex items-center gap-3 border border-gray-200 rounded-[8px] bg-white shadow-sm"
          >
            <div
              class="w-10 h-10 shrink-0 border border-gray-200 bg-gray-50 rounded-[6px] flex items-center justify-center text-gray-800"
            >
              <svg
                class="w-5 h-5"
                fill="none"
                stroke="currentColor"
                stroke-width="1.5"
                viewBox="0 0 24 24"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="M16.023 9.348h4.992v-.001M2.985 19.644v-4.992m0 0h4.992m-4.993 0l3.181 3.183a8.25 8.25 0 0013.803-3.7M4.031 9.865a8.25 8.25 0 0113.803-3.7l3.181 3.182m0-4.991v4.9"
                />
              </svg>
            </div>
            <div class="text-[13px] sm:text-[14px] text-gray-700 leading-[1.4]">
              Hỗ trợ đổi trả trong 7 ngày nếu sản phẩm lỗi.
            </div>
          </div>

          <!-- Cam kết 4: Tư vấn -->
          <div
            class="p-4 flex items-center gap-3 border border-gray-200 rounded-[8px] bg-white shadow-sm"
          >
            <div
              class="w-10 h-10 shrink-0 border border-gray-200 bg-gray-50 rounded-[6px] flex items-center justify-center text-gray-800"
            >
              <svg
                class="w-5 h-5"
                fill="none"
                stroke="currentColor"
                stroke-width="1.5"
                viewBox="0 0 24 24"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="M9.879 7.519c1.171-1.025 3.071-1.025 4.242 0 1.172 1.025 1.172 2.687 0 3.712-.203.179-.43.326-.67.442-.745.361-1.45.999-1.45 1.827v.75M21 12a9 9 0 11-18 0 9 9 0 0118 0zm-9 5.25h.008v.008H12v-.008z"
                />
              </svg>
            </div>
            <div class="text-[13px] sm:text-[14px] text-gray-700 leading-[1.4]">
              Đội ngũ tư vấn tận tâm, giải đáp nhanh chóng
            </div>
          </div>
        </div>
      </section>

      <!-- =====================================================
           PRODUCT INFORMATION TABS
      ====================================================== -->
      <section class="mt-8 border border-gray-200 rounded-[8px] overflow-hidden shadow-sm">
        <div
          class="flex justify-start sm:justify-center border-b border-gray-200 overflow-x-auto bg-gray-50/50"
        >
          <button
            v-for="tab in ['THÔNG TIN SẢN PHẨM', 'BẢO QUẢN', 'GIAO HÀNG & ĐỔI TRẢ']"
            :key="tab"
            type="button"
            @click="activeTab = tab"
            class="px-5 py-3.5 text-sm font-semibold tracking-wide whitespace-nowrap border-b-2 transition-all"
            :class="
              activeTab === tab
                ? 'border-[#00a884] text-[#00a884] bg-white'
                : 'border-transparent text-gray-500 hover:text-gray-800'
            "
          >
            {{ tab }}
          </button>
        </div>

        <div class="p-5 lg:p-7 bg-white">
          <div
            v-if="activeTab === 'THÔNG TIN SẢN PHẨM'"
            class="text-[15px] text-gray-700 leading-relaxed"
          >
            <p>
              Áo thun tôn dáng dành riêng cho nàng. Thiết kế cổ tròn, dáng ôm tôn lên đường cong cơ
              thể giúp nàng trông thon gọn và năng động hơn. Sản phẩm cho cảm giác mặc siêu mềm mại,
              siêu co giãn, xứng đáng là một item không thể thiếu trong tủ đồ hàng ngày cho các chị
              em.
            </p>

            <div
              class="grid grid-cols-1 md:grid-cols-2 gap-x-10 mt-6 pt-4 border-t border-gray-100"
            >
              <div class="flex items-center justify-between py-2.5 border-b border-gray-100">
                <span class="text-gray-500 font-medium">Mã SP</span>
                <span class="font-medium text-gray-900">
                  {{ selectedVariant?.maSanPhamChiTiet || 'Đang cập nhật' }}
                </span>
              </div>

              <div class="flex items-center justify-between py-2.5 border-b border-gray-100">
                <span class="text-gray-500 font-medium">Danh mục</span>
                <span class="font-medium text-gray-900">
                  {{ selectedVariant?.tenDanhMuc || 'Đang cập nhật' }}
                </span>
              </div>

              <div class="flex items-center justify-between py-2.5 border-b border-gray-100">
                <span class="text-gray-500 font-medium">Thương hiệu</span>
                <span class="font-medium text-gray-900">
                  {{ selectedVariant?.tenThuongHieu || 'Wolf Calie' }}
                </span>
              </div>

              <div class="flex items-center justify-between py-2.5 border-b border-gray-100">
                <span class="text-gray-500 font-medium">Chất liệu</span>
                <span class="font-medium text-gray-900">
                  {{ selectedVariant?.tenChatLieu || 'Đang cập nhật' }}
                </span>
              </div>
            </div>
          </div>

          <div
            v-else-if="activeTab === 'BẢO QUẢN'"
            class="text-[15px] text-gray-700 leading-relaxed space-y-1.5"
          >
            <p>• Giặt tay hoặc giặt máy ở chế độ nhẹ với nước lạnh.</p>
            <p>• Không sử dụng chất tẩy mạnh.</p>
            <p>• Phơi ở nơi bóng râm, tránh ánh nắng trực tiếp.</p>
            <p>• Ủi ở nhiệt độ thấp nếu cần thiết.</p>
          </div>

          <div
            v-else-if="activeTab === 'GIAO HÀNG & ĐỔI TRẢ'"
            class="text-[15px] text-gray-700 leading-relaxed space-y-1.5"
          >
            <p>• Giao hàng toàn quốc từ 3 - 5 ngày làm việc.</p>
            <p>• Miễn phí vận chuyển cho đơn hàng từ 500.000 VNĐ.</p>
            <p>• Hỗ trợ đổi trả nếu sản phẩm bị lỗi.</p>
          </div>
        </div>
      </section>

      <!-- =====================================================
           SAME CATEGORY
      ====================================================== -->
      <section v-if="sameCategoryProducts.length > 0" class="mt-8 border-b border-gray-200">
        <div class="flex items-center justify-between mb-3">
          <h2 class="text-[18px] sm:text-[20px] font-bold text-[#111] uppercase tracking-wide">
            Sản phẩm liên quan
          </h2>
        </div>

        <div class="relative group/slider">
          <button
            type="button"
            @click="scrollSlider('left')"
            class="absolute -left-4 sm:-left-5 top-[38%] -translate-y-1/2 z-20 w-10 h-10 rounded-full bg-white shadow-md border border-gray-200 flex items-center justify-center text-gray-700 hover:bg-gray-50 hover:text-black transition-all"
          >
            ‹
          </button>

          <button
            type="button"
            @click="scrollSlider('right')"
            class="absolute -right-4 sm:-right-5 top-[38%] -translate-y-1/2 z-20 w-10 h-10 rounded-full bg-white shadow-md border border-gray-200 flex items-center justify-center text-gray-700 hover:bg-gray-50 hover:text-black transition-all"
          >
            ›
          </button>

          <div
            ref="sameCategorySliderRef"
            @scroll="handleScrollProgress"
            class="flex gap-3.5 overflow-x-auto scroll-smooth pb-2 no-scrollbar"
            style="scrollbar-width: none; -ms-overflow-style: none; scroll-snap-type: x mandatory"
          >
            <div
              v-for="item in sameCategoryProducts"
              :key="item.idSanPham"
              @mouseenter="hoveredCardId = item.idSanPham"
              @mouseleave="hoveredCardId = null"
              class="cursor-pointer flex flex-col shrink-0 w-[210px] sm:w-[230px]"
              style="scroll-snap-align: start"
            >
              <div
                class="relative overflow-hidden aspect-[4/5] bg-[#f6f6f6] rounded-[8px] border border-gray-200 shadow-sm"
                @click="
                  $router.push({
                    name: 'confirmbuy',
                    params: { id: item.idSanPham },
                  })
                "
              >
                <img
                  :src="
                    item.image
                      ? item.image.startsWith('http')
                        ? item.image
                        : API_URL + item.image
                      : placeholder
                  "
                  :alt="item.tenSanPham"
                  class="w-full h-full object-cover transition-transform duration-500"
                  :class="hoveredCardId === item.idSanPham ? 'scale-105' : 'scale-100'"
                />

                <div
                  v-if="item.dangGiamGia && item.phanTramGiam > 0"
                  class="absolute left-2.5 top-2.5 bg-[#df2633] text-white px-2 py-0.5 text-[11px] font-bold rounded-[4px] z-10 shadow-sm"
                >
                  -{{ item.phanTramGiam }}%
                </div>

                <div
                  class="absolute right-2.5 top-2.5 flex flex-col gap-1.5 transition-opacity duration-300 z-20"
                  :class="
                    hoveredCardId === item.idSanPham
                      ? 'opacity-100'
                      : 'opacity-0 pointer-events-none'
                  "
                >
                  <button
                    type="button"
                    @click.stop="toggleFavorite(item)"
                    class="w-8 h-8 rounded-full bg-white/95 backdrop-blur-sm shadow-lg flex items-center justify-center text-[#222] hover:text-[#df2633] hover:bg-white hover:scale-105 transition-all border border-gray-100"
                    title="Thêm vào yêu thích"
                  >
                    <span class="text-[16px] leading-none">♡</span>
                  </button>

                  <button
                    type="button"
                    @click.stop="quickView(item)"
                    class="w-8 h-8 rounded-full bg-white/95 backdrop-blur-sm shadow-lg flex items-center justify-center text-[#222] hover:text-[#00a884] hover:bg-white hover:scale-105 transition-all border border-gray-100"
                    title="Xem nhanh"
                  >
                    <span class="text-[14px] leading-none">👁</span>
                  </button>
                </div>
              </div>

              <div
                v-if="item.colors && item.colors.length > 0"
                class="flex items-center gap-1.5 mt-2"
              >
                <span
                  v-for="(colorName, idx) in item.colors"
                  :key="idx"
                  class="w-[14px] h-[14px] rounded-full border border-gray-300 shadow-sm"
                  :style="{ backgroundColor: getColorStyle(colorName) }"
                  :title="colorName"
                ></span>
              </div>

              <div
                class="pt-1.5 flex-1 flex flex-col justify-between"
                @click="
                  $router.push({
                    name: 'confirmbuy',
                    params: { id: item.idSanPham },
                  })
                "
              >
                <h3
                  class="text-[14px] text-gray-900 font-normal line-clamp-2 leading-snug transition-colors"
                  :class="hoveredCardId === item.idSanPham ? 'text-[#00a884]' : ''"
                >
                  {{ item.tenSanPham }}
                </h3>

                <div class="flex items-center gap-1.5 mt-1 flex-wrap">
                  <span class="text-[15px] font-bold text-[#bd2228]">
                    {{ (item.giaSauGiam ?? item.giaBan).toLocaleString('vi-VN') }}đ
                  </span>

                  <span
                    v-if="item.dangGiamGia && item.giaBan > (item.giaSauGiam ?? item.giaBan)"
                    class="text-[12px] text-gray-400 line-through"
                  >
                    {{ item.giaBan.toLocaleString('vi-VN') }}đ
                  </span>
                </div>
              </div>
            </div>
          </div>

          <div
            class="w-full h-2 bg-gray-200 rounded-full mt-3 relative overflow-hidden shadow-inner"
          >
            <div
              class="absolute top-0 left-0 h-full bg-[#bd2228] transition-all duration-100 rounded-full"
              :style="{ width: `${scrollProgress}%` }"
            ></div>
          </div>
        </div>

        <div class="mt-4 text-center">
          <button
            type="button"
            @click="$router.push('/san-pham')"
            class="px-7 py-2.5 bg-[#bd2228] border border-[#bd2228] text-white text-[14px] font-semibold rounded-[6px] hover:bg-[#a61c22] hover:border-[#a61c22] transition-all shadow-sm"
          >
            Xem thêm sản phẩm
          </button>
        </div>
      </section>

      <!-- =====================================================
           RECENTLY VIEWED
      ====================================================== -->
      <section v-if="recentlyViewedProducts.length > 0">
        <div class="flex items-center justify-between mb-3">
          <h2 class="text-[18px] sm:text-[20px] font-bold text-[#111] uppercase tracking-wide">
            Sản phẩm đã xem gần đây
          </h2>
        </div>

        <div class="relative group/slider">
          <button
            type="button"
            @click="scrollRecentlyViewed('left')"
            class="absolute -left-4 sm:-left-5 top-[38%] -translate-y-1/2 z-20 w-10 h-10 rounded-full bg-white shadow-md border border-gray-200 flex items-center justify-center text-gray-700 hover:bg-gray-50 hover:text-black transition-all"
          >
            ‹
          </button>

          <button
            type="button"
            @click="scrollRecentlyViewed('right')"
            class="absolute -right-4 sm:-right-5 top-[38%] -translate-y-1/2 z-20 w-10 h-10 rounded-full bg-white shadow-md border border-gray-200 flex items-center justify-center text-gray-700 hover:bg-gray-50 hover:text-black transition-all"
          >
            ›
          </button>

          <div
            ref="recentlyViewedSliderRef"
            @scroll="handleRecentlyViewedScroll"
            class="flex gap-3.5 overflow-x-auto scroll-smooth pb-2 no-scrollbar"
            style="scrollbar-width: none; -ms-overflow-style: none; scroll-snap-type: x mandatory"
          >
            <div
              v-for="item in recentlyViewedProducts"
              :key="item.idSanPham"
              @mouseenter="hoveredCardId = item.idSanPham"
              @mouseleave="hoveredCardId = null"
              class="cursor-pointer flex flex-col shrink-0 w-[210px] sm:w-[230px]"
              style="scroll-snap-align: start"
            >
              <div
                class="relative overflow-hidden aspect-[4/5] bg-[#f6f6f6] rounded-[8px] border border-gray-200 shadow-sm"
                @click="
                  $router.push({
                    name: 'confirmbuy',
                    params: { id: item.idSanPham },
                  })
                "
              >
                <img
                  :src="
                    item.image
                      ? item.image.startsWith('http')
                        ? item.image
                        : API_URL + item.image
                      : placeholder
                  "
                  :alt="item.tenSanPham"
                  class="w-full h-full object-cover transition-transform duration-500"
                  :class="hoveredCardId === item.idSanPham ? 'scale-105' : 'scale-100'"
                />

                <div
                  v-if="item.dangGiamGia && item.phanTramGiam > 0"
                  class="absolute left-2.5 top-2.5 bg-[#df2633] text-white px-2 py-0.5 text-[11px] font-bold rounded-[4px] z-10 shadow-sm"
                >
                  -{{ item.phanTramGiam }}%
                </div>

                <div
                  class="absolute right-2.5 top-2.5 flex flex-col gap-1.5 transition-opacity duration-300 z-20"
                  :class="
                    hoveredCardId === item.idSanPham
                      ? 'opacity-100'
                      : 'opacity-0 pointer-events-none'
                  "
                >
                  <button
                    type="button"
                    @click.stop="toggleFavorite(item)"
                    class="w-8 h-8 rounded-full bg-white/95 backdrop-blur-sm shadow-lg flex items-center justify-center text-[#222] hover:text-[#df2633] hover:bg-white hover:scale-105 transition-all border border-gray-100"
                    title="Thêm vào yêu thích"
                  >
                    <span class="text-[16px] leading-none">♡</span>
                  </button>

                  <button
                    type="button"
                    @click.stop="quickView(item)"
                    class="w-8 h-8 rounded-full bg-white/95 backdrop-blur-sm shadow-lg flex items-center justify-center text-[#222] hover:text-[#00a884] hover:bg-white hover:scale-105 transition-all border border-gray-100"
                    title="Xem nhanh"
                  >
                    <span class="text-[14px] leading-none">👁</span>
                  </button>
                </div>
              </div>

              <div
                v-if="item.colors && item.colors.length > 0"
                class="flex items-center gap-1.5 mt-2"
              >
                <span
                  v-for="(colorName, idx) in item.colors"
                  :key="idx"
                  class="w-[14px] h-[14px] rounded-full border border-gray-300 shadow-sm"
                  :style="{ backgroundColor: getColorStyle(colorName) }"
                  :title="colorName"
                ></span>
              </div>

              <div
                class="pt-1.5 flex-1 flex flex-col justify-between"
                @click="
                  $router.push({
                    name: 'confirmbuy',
                    params: { id: item.idSanPham },
                  })
                "
              >
                <h3
                  class="text-[14px] text-gray-900 font-normal line-clamp-2 leading-snug transition-colors"
                  :class="hoveredCardId === item.idSanPham ? 'text-[#00a884]' : ''"
                >
                  {{ item.tenSanPham }}
                </h3>

                <div class="flex items-center gap-1.5 mt-1 flex-wrap">
                  <span class="text-[15px] font-bold text-[#bd2228]">
                    {{ (item.giaSauGiam ?? item.giaBan).toLocaleString('vi-VN') }}đ
                  </span>

                  <span
                    v-if="item.dangGiamGia && item.giaBan > (item.giaSauGiam ?? item.giaBan)"
                    class="text-[12px] text-gray-400 line-through"
                  >
                    {{ item.giaBan.toLocaleString('vi-VN') }}đ
                  </span>
                </div>
              </div>
            </div>
          </div>

          <div
            class="w-full h-2 bg-gray-200 rounded-full mt-3 relative overflow-hidden shadow-inner"
          >
            <div
              class="absolute top-0 left-0 h-full bg-[#bd2228] transition-all duration-100 rounded-full"
              :style="{ width: `${recentlyViewedScrollProgress}%` }"
            ></div>
          </div>
        </div>
      </section>
    </main>

    <QuickViewModal
      v-if="showQuickViewModal && quickViewProductId"
      :product-id="quickViewProductId"
      @close="showQuickViewModal = false"
    />

    <SizeModal v-if="showSizeModal" @close="showSizeModal = false" />

    <VirtualTryOn
      v-if="showTryOn && selectedVariant"
      is-modal
      :spct-id="selectedVariant.id"
      :default-garment-url="mainImage"
      :default-category="tryOnCategory"
      @close="showTryOn = false"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getShopVariantsByProductId, getAllSanPhamChiTiet } from '@/service/SanPhamChiTiet'
import VirtualTryOn from '@/components/VirtualTryOn.vue'
import SizeModal from '@/views/shop/views/componnents/SizeModal.vue'
import axios from 'axios'
import stompClient from '@/socket'
import emitter from '@/utils/emitter'
import { flyToCart } from '@/utils/cartAnimation'
import QuickViewModal from '@/views/shop/views/componnents/QuickViewModal.vue'

const quickViewProductId = ref(null)
const hoveredCardId = ref(null)
const quickView = (item) => {
  quickViewProductId.value = item.idSanPham
  showQuickViewModal.value = true
}
const showQuickViewModal = ref(false)
const route = useRoute()
const router = useRouter()
const showTryOn = ref(false)
const showSizeModal = ref(false)
const activeTab = ref('THÔNG TIN SẢN PHẨM')

const product = ref(null)
const selectedColor = ref(null)
const selectedVariant = ref(null)
const loading = ref(true)
const mainImage = ref('')
const quantity = ref(1)
const shopVariants = ref([])
const recentlyViewedProducts = ref([])

const sameCategorySliderRef = ref(null)
const scrollProgress = ref(0)

const recentlyViewedSliderRef = ref(null)
const recentlyViewedScrollProgress = ref(0)

const isAddingToCart = ref(false)

const API_URL = 'http://localhost:8080'
const placeholder = 'https://via.placeholder.com/300'

const restrictNumberKeys = (event) => {
  if (['e', 'E', '+', '-', '.'].includes(event.key)) {
    event.preventDefault()
  }
}

const getColorStyle = (name) => {
  const lower = (name || '').toLowerCase()
  if (lower.includes('trắng') || lower.includes('white')) return '#ffffff'
  if (lower.includes('đỏ') || lower.includes('red')) return '#ef4444'
  if (lower.includes('vàng') || lower.includes('yellow')) return '#facc15'
  if (lower.includes('đen') || lower.includes('black')) return '#111827'
  if (lower.includes('xám') || lower.includes('gray')) return '#9ca3af'
  if (lower.includes('xanh') || lower.includes('blue')) return '#3b82f6'
  return '#e5e7eb'
}

const getVariantStock = (variant) => {
  if (!variant) return 0
  return variant.soLuongKhaDung !== undefined ? variant.soLuongKhaDung : (variant.soLuongTon ?? 0)
}

const availableStock = computed(() => getVariantStock(selectedVariant.value))
const tryOnCategory = computed(() => {
  const text = `${product.value?.productName || ''} ` + `${selectedVariant.value?.tenDanhMuc || ''}`
  const normalized = text.toLowerCase()

  if (
    normalized.includes('đầm') ||
    normalized.includes('váy liền') ||
    normalized.includes('dress')
  ) {
    return 'dresses'
  }
  if (
    normalized.includes('quần') ||
    normalized.includes('chân váy') ||
    normalized.includes('pants') ||
    normalized.includes('trouser')
  ) {
    return 'lower_body'
  }
  return 'upper_body'
})

const loadShopProducts = async () => {
  try {
    const data = await getAllSanPhamChiTiet()
    shopVariants.value = data.filter(
      (item) => item.trangThai && item.idSanPham !== Number(route.params.id),
    )
  } catch (e) {
    console.error('Lỗi tải danh sách sản phẩm khác:', e)
  }
}

const shopProducts = computed(() => {
  const groups = {}
  shopVariants.value.forEach((item) => {
    if (!item.trangThai) return
    if (!groups[item.idSanPham]) {
      groups[item.idSanPham] = {
        idSanPham: item.idSanPham,
        tenSanPham: item.tenSanPham,
        tenDanhMuc: item.tenDanhMuc,
        tenThuongHieu: item.tenThuongHieu,
        tenChatLieu: item.tenChatLieu,
        giaBan: item.giaBan,
        giaSauGiam: item.giaSauGiam ?? item.giaBan,
        dangGiamGia: item.dangGiamGia,
        phanTramGiam: item.phanTramGiam ?? 0,
        tongSoLuong: 0,
        image: item.images?.[0] ?? '',
        colors: [],
      }
    }
    const p = groups[item.idSanPham]
    p.tongSoLuong += getVariantStock(item)
    if (item.giaBan < p.giaBan) p.giaBan = item.giaBan
    const currentPrice = item.giaSauGiam ?? item.giaBan
    if (currentPrice < p.giaSauGiam) p.giaSauGiam = currentPrice
    if (item.dangGiamGia && (item.phanTramGiam ?? 0) > p.phanTramGiam) {
      p.phanTramGiam = item.phanTramGiam
      p.dangGiamGia = true
    }
    if (item.tenMauSac && !p.colors.includes(item.tenMauSac)) {
      p.colors.push(item.tenMauSac)
    }
  })
  return Object.values(groups)
})

const sameCategoryProducts = computed(() => {
  const currentCategory = selectedVariant.value?.tenDanhMuc
  if (!currentCategory) return []
  return shopProducts.value.filter(
    (item) => item.tenDanhMuc === currentCategory && item.idSanPham !== Number(route.params.id),
  )
})

const scrollSlider = (direction) => {
  if (!sameCategorySliderRef.value) return
  const firstCard = sameCategorySliderRef.value.querySelector('.cursor-pointer')
  const cardWidth = firstCard ? firstCard.offsetWidth + 14 : 240

  sameCategorySliderRef.value.scrollBy({
    left: direction === 'left' ? -cardWidth : cardWidth,
    behavior: 'smooth',
  })
}

const handleScrollProgress = () => {
  if (!sameCategorySliderRef.value) return
  const el = sameCategorySliderRef.value
  const maxScroll = el.scrollWidth - el.clientWidth
  if (maxScroll <= 0) {
    scrollProgress.value = 100
    return
  }
  scrollProgress.value = (el.scrollLeft / maxScroll) * 100
}

const scrollRecentlyViewed = (direction) => {
  if (!recentlyViewedSliderRef.value) return
  const firstCard = recentlyViewedSliderRef.value.querySelector('.cursor-pointer')
  const cardWidth = firstCard ? firstCard.offsetWidth + 14 : 240

  recentlyViewedSliderRef.value.scrollBy({
    left: direction === 'left' ? -cardWidth : cardWidth,
    behavior: 'smooth',
  })
}

const handleRecentlyViewedScroll = () => {
  if (!recentlyViewedSliderRef.value) return
  const el = recentlyViewedSliderRef.value
  const maxScroll = el.scrollWidth - el.clientWidth
  if (maxScroll <= 0) {
    recentlyViewedScrollProgress.value = 100
    return
  }
  recentlyViewedScrollProgress.value = (el.scrollLeft / maxScroll) * 100
}

const toggleFavorite = (item) => {
  alert(`Đã thêm sản phẩm "${item.tenSanPham}" vào danh sách yêu thích!`)
}

const saveToRecentlyViewed = (currentProd) => {
  if (!currentProd) return
  let viewed = JSON.parse(localStorage.getItem('recentlyViewed') || '[]')
  viewed = viewed.filter((item) => item.idSanPham !== currentProd.idSanPham)
  viewed.unshift(currentProd)
  if (viewed.length > 10) viewed = viewed.slice(0, 10)
  localStorage.setItem('recentlyViewed', JSON.stringify(viewed))
  recentlyViewedProducts.value = viewed.filter((item) => item.idSanPham !== Number(route.params.id))
}

const loadRecentlyViewed = () => {
  const viewed = JSON.parse(localStorage.getItem('recentlyViewed') || '[]')
  recentlyViewedProducts.value = viewed.filter((item) => item.idSanPham !== Number(route.params.id))
}

const loadProduct = async () => {
  loading.value = true
  try {
    const data = await getShopVariantsByProductId(route.params.id)
    product.value = data
    if (data.colors?.length) {
      selectedColor.value = data.colors[0]
      if (data.colors[0].variants?.length) {
        selectedVariant.value = data.colors[0].variants[0]
      }
    }
    const firstImg = data.gallery?.[0]?.image || ''
    const allColors = data.colors ? data.colors.map((c) => c.name) : []

    const currentGrouped = {
      idSanPham: Number(route.params.id),
      tenSanPham: data.productName,
      tenDanhMuc: selectedVariant.value?.tenDanhMuc || '',
      giaBan: selectedVariant.value?.giaBan || 0,
      giaSauGiam: selectedVariant.value?.giaSauGiam || selectedVariant.value?.giaBan || 0,
      dangGiamGia: selectedVariant.value?.dangGiamGia || false,
      phanTramGiam: selectedVariant.value?.phanTramGiam || 0,
      image: firstImg,
      colors: allColors,
    }
    saveToRecentlyViewed(currentGrouped)
  } catch (e) {
    console.error('Lỗi tải sản phẩm chi tiết:', e)
  } finally {
    loading.value = false
  }
}

const increaseQty = () => {
  if (!selectedVariant.value) return
  if (quantity.value < availableStock.value) {
    quantity.value++
  }
}

const decreaseQty = () => {
  if (quantity.value > 1) {
    quantity.value--
  }
}

const validateQuantity = () => {
  if (!quantity.value || quantity.value < 1) {
    quantity.value = 1
  } else if (availableStock.value > 0 && quantity.value > availableStock.value) {
    quantity.value = availableStock.value
  }
}

const galleryImages = computed(() => {
  if (!product.value?.gallery) return []
  return product.value.gallery.map((item) => ({
    ...item,
    url: item.image.startsWith('http') ? item.image : `${API_URL}${item.image}`,
  }))
})

watch(
  selectedVariant,
  (variant) => {
    if (!variant || !product.value) return
    const firstImage = product.value.gallery?.find((g) => g.spctId === variant.id)
    if (firstImage) {
      mainImage.value = firstImage.image.startsWith('http')
        ? firstImage.image
        : `${API_URL}${firstImage.image}`
    } else {
      mainImage.value = ''
    }
    const stock = getVariantStock(variant)
    quantity.value = stock > 0 ? 1 : 0
  },
  { immediate: true },
)

const selectImage = (img) => {
  mainImage.value = img.url
  for (const color of product.value?.colors || []) {
    const variant = color.variants?.find((v) => v.id === img.spctId)
    if (variant) {
      selectedColor.value = color
      selectedVariant.value = variant
      break
    }
  }
}

const selectColor = (color) => {
  selectedColor.value = color
  selectedVariant.value = color.variants?.[0] ?? null
}

const selectVariant = (variant) => {
  selectedVariant.value = variant
}

onMounted(async () => {
  await loadProduct()
  await loadShopProducts()
  loadRecentlyViewed()
  connectSocket()
})

function connectSocket() {
  if (stompClient.connected) {
    subscribeDetail()
  } else {
    stompClient.onConnect = () => {
      subscribeDetail()
    }
  }
}

function subscribeDetail() {
  stompClient.subscribe('/topic/pos', async (msg) => {
    const event = JSON.parse(msg.body)
    if (['DISCOUNT_UPDATED', 'PRODUCT_UPDATED'].includes(event.type)) {
      await loadShopProducts()
      await loadProduct()
    }
  })
}

const addToCart = async (event) => {
  const token = sessionStorage.getItem('token')
  if (!token) {
    alert('Bạn cần đăng nhập để thêm vào giỏ hàng!')
    router.push('/login')
    return
  }
  if (!selectedVariant.value) {
    alert('Vui lòng chọn phân loại sản phẩm!')
    return
  }

  flyToCart(event, mainImage.value)
  isAddingToCart.value = true

  try {
    await axios.post(
      `${API_URL}/giohang/them`,
      {
        idSanPhamChiTiet: selectedVariant.value.id,
        soLuong: quantity.value,
      },
      {
        headers: { Authorization: `Bearer ${token}` },
      },
    )

    emitter.emit('cart-updated')
  } catch (err) {
    console.error(err)
    alert(err?.response?.data || 'Lỗi thêm sản phẩm vào giỏ hàng!')
  } finally {
    setTimeout(() => {
      isAddingToCart.value = false
    }, 700)
  }
}

const buyNow = () => {
  if (!selectedVariant.value) return
  router.push({
    path: '/xacnhan',
    query: {
      spct: selectedVariant.value.id,
      qty: quantity.value,
    },
  })
}

watch(
  () => route.params.id,
  async () => {
    await loadProduct()
    await loadShopProducts()
    loadRecentlyViewed()
    window.scrollTo({ top: 0, behavior: 'smooth' })
  },
)
</script>
