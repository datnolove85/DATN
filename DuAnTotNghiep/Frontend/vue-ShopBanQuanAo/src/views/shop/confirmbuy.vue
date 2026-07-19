<template>
  <div
    class="max-w-[1400px] mx-auto px-6 lg:px-8 py-12 bg-gradient-to-b from-white to-slate-50 min-h-screen"
  >
    <div v-if="loading" class="flex justify-center items-center py-20 text-gray-400">
      <div class="animate-pulse">Đang tải sản phẩm...</div>
    </div>

    <!-- Breadcrumb -->
     <nav class="flex mb-6 text-sm text-slate-500 font-medium">
      <ol class="inline-flex items-center space-x-1 md:space-x-2">
        <li class="inline-flex items-center">
          <router-link to="/home" class="inline-flex items-center hover:text-slate-800 transition-colors">
            Trang chủ
          </router-link>
        </li>
        <li>
          <div class="flex items-center">
            <span class="mx-2 text-slate-400">></span>
            <router-link to="/san-pham" class="inline-flex items-center hover:text-slate-800 transition-colors">
              Sản phẩm
            </router-link>
          </div>
        </li>
        <li><div class="flex items-center">
          <span class="mx-2 text-slate-400">></span>
            <span class="text-slate-900 font-bold">{{ product?.productName }}</span>
          </div></li>
      </ol>
    </nav>

    <div v-if="!loading" class="grid grid-cols-1 xl:grid-cols-[620px_minmax(450px,1fr)] gap-16 items-start">
      <!-- CỘT TRÁI: HÌNH ẢNH -->
      <div class="relative group">
        <!-- Tag Giảm giá ribbon -->
        <div v-if="selectedVariant?.giaSauGiam && selectedVariant.giaBan > selectedVariant.giaSauGiam" class="absolute top-5 -left-2 z-10">
          <div class="bg-red-600 text-white px-4 py-1.5 rounded-r-md font-bold text-sm shadow-md relative z-20">
            Giảm {{ selectedVariant?.phanTramGiam ? selectedVariant.phanTramGiam + '%' : (selectedVariant.giaBan - selectedVariant.giaSauGiam).toLocaleString('vi-VN') + 'đ' }}
          </div>
          <div class="w-0 h-0 border-t-[8px] border-t-red-900 border-l-[8px] border-l-transparent relative z-10"></div>
        </div>
        <div
          class="aspect-[4/5] bg-gray-50 rounded-3xl overflow-hidden border border-gray-100 shadow-sm transition-transform duration-500 group-hover:scale-[1.01]"
        >
          <img
            v-if="mainImage"
            :src="mainImage"
            class="w-full h-full object-cover transition-opacity duration-700"
          />
          <div v-else class="w-full h-full flex items-center justify-center text-gray-400">
            Không có ảnh
          </div>
        </div>
        <div class="grid grid-cols-5 gap-3 mt-5">
          <button
            v-for="img in galleryImages"
            :key="img.image"
            @click="selectImage(img)"
            class="aspect-square rounded-2xl overflow-hidden border-2 transition hover:scale-105"
            :class="mainImage === img.url ? 'border-black' : 'border-white hover:border-gray-300'"
          >
            <img :src="img.url" class="w-full h-full object-cover" />
          </button>
        </div>
      </div>

      <!-- CỘT PHẢI: THÔNG TIN VÀ HÀNH ĐỘNG -->
      <div class="sticky top-28">
        <div class="rounded-3xl bg-white border border-slate-200 shadow-xl shadow-slate-100 p-10">
          <!-- Tên và Giá -->
          <h1 class="text-3xl font-bold text-gray-900 leading-tight">{{ product?.productName }}</h1>
          <div class="mt-4">
            <div class="flex items-center gap-3">
              <span class="text-5xl font-extrabold tracking-tight"
                >{{
                  (selectedVariant?.giaSauGiam || selectedVariant?.giaBan)?.toLocaleString('vi-VN')
                }}đ</span
              >
              <span v-if="selectedVariant?.giaSauGiam" class="line-through text-gray-400"
                >{{ selectedVariant?.giaBan?.toLocaleString('vi-VN') }}đ</span
              >
            </div>
            <p v-if="selectedVariant?.giaSauGiam" class="text-green-600 mt-2">
              Tiết kiệm
              {{ (selectedVariant.giaBan - selectedVariant.giaSauGiam).toLocaleString('vi-VN') }}đ
            </p>
          </div>

          <!-- Chọn Màu & Size -->
          <div class="space-y-6 mt-8">
            <div>
              <div class="flex items-center justify-between mb-3">
                <span class="text-sm font-bold text-slate-900 uppercase">Màu sắc</span>
                <span class="text-sm text-slate-500 bg-slate-50 px-2.5 py-0.5 rounded-full">{{
                  selectedColor?.name
                }}</span>
              </div>
              <div class="flex flex-wrap gap-2.5">
                <button
                  v-for="color in product?.colors"
                  :key="color.id"
                  @click="selectColor(color)"
                  class="px-5 py-2 rounded-xl text-sm font-medium border-2 transition-all"
                  :class="
                    selectedColor?.id === color.id
                      ? 'border-[#2f404d] bg-[#2f404d] text-white shadow-lg shadow-slate-800/30'
                      : 'border-slate-200 bg-white text-slate-700 hover:border-[#2f404d] hover:bg-slate-50'
                  "
                >
                  {{ color.name }}
                </button>
              </div>
            </div>

            <div>
              <div class="flex items-center justify-between mb-3">
                <div class="flex items-center gap-2">
                  <span class="text-sm font-bold text-slate-900 uppercase">Kích thước</span>
                  <span class="text-sm text-slate-500 bg-slate-50 px-2.5 py-0.5 rounded-full">{{ selectedVariant?.tenKichThuoc }}</span>
                </div>
                <button @click="showSizeGuide = true" class="flex items-center gap-1 text-sm font-medium text-[#2f404d] hover:text-slate-800">
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-4 h-4"><path stroke-linecap="round" stroke-linejoin="round" d="M14.25 10.5L13.5 9.75M10.5 14.25L9.75 13.5M6.75 17.25L6 16.5M17.25 6.75L16.5 6M20.25 3.75l-16.5 16.5m16.5-16.5a2.121 2.121 0 010 3l-16.5 16.5a2.121 2.121 0 01-3-3l16.5-16.5a2.121 2.121 0 013 0z" /></svg>
                  Gợi ý tìm kích cỡ
                </button>
              </div>
              <div class="flex flex-wrap gap-2.5">
                <button
                  v-for="variant in selectedColor?.variants"
                  :key="variant.id"
                  @click="variant.soLuongTon > 0 && selectVariant(variant)"
                  :disabled="variant.soLuongTon === 0"
                  class="w-12 h-12 rounded-xl text-sm font-medium border-2 flex items-center justify-center transition-all"
                  :class="[
                    variant.soLuongTon === 0
                      ? 'bg-slate-100 text-slate-300 border-slate-200 cursor-not-allowed line-through'
                      : selectedVariant?.id === variant.id
                        ? 'border-[#2f404d] bg-[#2f404d] text-white shadow-lg shadow-slate-800/30'
                        : 'border-slate-200 bg-white text-slate-700 hover:border-[#2f404d] hover:bg-slate-50',
                  ]"
                >
                  {{ variant.tenKichThuoc }}
                </button>
              </div>
            </div>
          </div>

          <!-- Tình trạng -->
          <div class="mt-6 flex justify-between items-center border-b border-slate-100 pb-4">
            <span class="text-sm font-semibold text-gray-500">Tình trạng</span>
            <span v-if="selectedVariant?.soLuongTon > 0" class="text-sm font-bold text-green-600">
              Còn hàng ({{ selectedVariant.soLuongTon }})
            </span>
            <span v-else class="text-sm font-bold text-red-600">
              Hết hàng
            </span>
          </div>

          <!-- Số lượng -->
          <div class="mt-8">
            <div class="text-sm font-bold uppercase mb-3">Số lượng</div>
            <div class="flex items-center w-fit border border-slate-200 rounded-xl overflow-hidden">
              <button
                @click="decreaseQty"
                :disabled="quantity <= 1"
                class="w-12 h-12 hover:bg-gray-100"
              >
                -
              </button>
              <div class="w-14 text-center font-semibold">{{ quantity }}</div>
              <button
                @click="increaseQty"
                :disabled="!selectedVariant || quantity >= selectedVariant.soLuongTon"
                class="w-12 h-12 hover:bg-gray-100"
              >
                +
              </button>
            </div>
          </div>

          <!-- Nút Hành Động -->
          <!-- Nút Hành Động đã đổi sang tone màu xanh dương -->
          <div class="grid grid-cols-2 gap-4 mt-8">
            <!-- Nút Thêm vào giỏ -->
            <button
              @click="addToCart"
              :disabled="!selectedVariant || selectedVariant.soLuongTon === 0"
              class="w-full py-4 flex justify-center items-center gap-2 rounded-2xl border-2 font-bold text-lg transition-all border-[#2f404d] text-[#2f404d] hover:bg-[#2f404d] hover:text-white disabled:bg-gray-100 disabled:border-gray-200 disabled:text-gray-400"
            >
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6">
                <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 3h1.386c.51 0 .955.343 1.087.835l.383 1.437M7.5 14.25a3 3 0 00-3 3h15.75m-12.75-3h11.218c1.121-2.3 2.1-4.684 2.924-7.138a60.114 60.114 0 00-16.536-1.84M7.5 14.25L5.106 5.272M6 20.25a.75.75 0 11-1.5 0 .75.75 0 011.5 0zm12.75 0a.75.75 0 11-1.5 0 .75.75 0 011.5 0z" />
              </svg>
              Thêm vào giỏ
            </button>

            <!-- Nút Mua ngay -->
            <button
              @click="buyNow"
              :disabled="!selectedVariant || selectedVariant.soLuongTon === 0"
              class="w-full py-4 flex justify-center items-center gap-2 rounded-2xl border-2 font-bold text-lg transition-all border-[#2f404d] bg-[#2f404d] text-white hover:bg-slate-800 hover:border-slate-800 disabled:bg-gray-300 disabled:border-gray-300 disabled:text-gray-500 shadow-lg shadow-slate-800/30 hover:shadow-slate-800/50"
            >
              Mua ngay
            </button>
          </div>
        </div>

        <!-- Thông tin hỗ trợ -->
        <div class="mt-8 space-y-4">
          <!-- Item 1 -->
          <div class="flex items-center gap-4">
            <div class="w-12 h-12 rounded-lg bg-slate-100 flex items-center justify-center shrink-0">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6 text-slate-700">
                <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 18.75a60.07 60.07 0 0115.797 2.101c.727.198 1.453-.342 1.453-1.096V18.75M3.75 4.5v.75A.75.75 0 013 6h-.75m0 0v-.375c0-.621.504-1.125 1.125-1.125H20.25M2.25 6v9m18-10.5v.75c0 .414.336.75.75.75h.75m-1.5-1.5h.375c.621 0 1.125.504 1.125 1.125v9.75c0 .621-.504 1.125-1.125 1.125h-.375m1.5-1.5H21a.75.75 0 00-.75.75v.75m0 0H3.75m0 0h-.375a1.125 1.125 0 01-1.125-1.125V15m1.5 1.5v-.75A.75.75 0 003 15h-.75M15 10.5a3 3 0 11-6 0 3 3 0 016 0zm3 0h.008v.008H18V10.5zm-12 0h.008v.008H6V10.5z" />
              </svg>
            </div>
            <div>
              <div class="font-bold text-slate-800 text-[15px]">Thanh toán khi nhận hàng (COD)</div>
              <div class="text-sm text-slate-500 mt-0.5">Giao hàng toàn quốc.</div>
            </div>
          </div>

          <!-- Item 2 -->
          <div class="flex items-center gap-4">
            <div class="w-12 h-12 rounded-lg bg-slate-100 flex items-center justify-center shrink-0">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6 text-slate-700">
                <path stroke-linecap="round" stroke-linejoin="round" d="M8.25 18.75a1.5 1.5 0 01-3 0m3 0a1.5 1.5 0 00-3 0m3 0h6m-9 0H3.375a1.125 1.125 0 01-1.125-1.125V14.25m17.25 4.5a1.5 1.5 0 01-3 0m3 0a1.5 1.5 0 00-3 0m3 0h1.125c.621 0 1.129-.504 1.09-1.124a17.902 17.902 0 00-3.213-9.193 2.056 2.056 0 00-1.58-.86H14.25M16.5 18.75h-2.25m0-11.177v-.958c0-.568-.422-1.048-.987-1.106a48.554 48.554 0 00-10.026 0 1.106 1.106 0 00-.987 1.106v7.635m12-6.677v6.677m0 4.5v-4.5m0 0h-12" />
              </svg>
            </div>
            <div>
              <div class="font-bold text-slate-800 text-[15px]">Miễn phí giao hàng</div>
              <div class="text-sm text-slate-500 mt-0.5">Với đơn hàng trên 1.000.000 đ.</div>
            </div>
          </div>

          <!-- Item 3 -->
          <div class="flex items-center gap-4">
            <div class="w-12 h-12 rounded-lg bg-slate-100 flex items-center justify-center shrink-0">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6 text-slate-700">
                <path stroke-linecap="round" stroke-linejoin="round" d="M21 7.5l-9-5.25L3 7.5m18 0l-9 5.25m9-5.25v9l-9 5.25M3 7.5l9 5.25M3 7.5v9l9 5.25m0-9v9" />
              </svg>
            </div>
            <div>
              <div class="font-bold text-slate-800 text-[15px]">Đổi hàng miễn phí</div>
              <div class="text-sm text-slate-500 mt-0.5">Trong 30 ngày kể từ ngày mua.</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- CHI TIẾT SẢN PHẨM -->
    <div class="mt-16 bg-slate-50/50 p-6 rounded-2xl border border-slate-100">
      <h4 class="font-bold text-slate-900 mb-4">Chi tiết sản phẩm</h4>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-x-10 gap-y-4 text-sm">
        <div class="flex justify-between py-2 border-b border-slate-100">
          <span class="text-slate-500">Mã SP</span
          ><span class="font-medium">{{ selectedVariant?.maSanPhamChiTiet }}</span>
        </div>
        <div class="flex justify-between py-2 border-b border-slate-100">
          <span class="text-slate-500">Danh mục</span><span>{{ selectedVariant?.tenDanhMuc }}</span>
        </div>
        <div class="flex justify-between py-2 border-b border-slate-100">
          <span class="text-slate-500">Thương hiệu</span
          ><span>{{ selectedVariant?.tenThuongHieu }}</span>
        </div>
        <div class="flex justify-between py-2 border-b border-slate-100">
          <span class="text-slate-500">Chất liệu</span
          ><span>{{ selectedVariant?.tenChatLieu }}</span>
        </div>
      </div>
    </div>

    <!-- Sản phẩm khác của shop -->
    <div class="mt-16">
      <div class="flex justify-between items-center mb-5">
        <h2 class="text-xl font-bold text-slate-800">Sản phẩm khác của shop</h2>

        <RouterLink to="/san-pham" class="text-sm font-semibold text-blue-600 hover:underline">
          Xem tất cả →
        </RouterLink>
      </div>

      <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-5">
        <div
          v-for="item in shopProducts"
          :key="item.id"
          @click="$router.push({ name: 'confirmbuy', params: { id: item.idSanPham } })"
          class="group bg-white rounded-2xl overflow-hidden border border-slate-200 hover:border-blue-300 hover:shadow-xl hover:shadow-blue-100/50 transition-all duration-300 cursor-pointer"
        >
          <!-- Ảnh -->
          <div class="relative overflow-hidden bg-slate-100">
            <img
              :src="item.image ? API_URL + item.image : placeholder"
              class="w-full h-64 object-cover transition-transform duration-500 group-hover:scale-105"
            />
            <div
              v-if="item.dangGiamGia"
              class="absolute top-3 left-0 bg-red-600 text-white px-3 py-1 rounded-r-md text-xs font-bold shadow-md z-10"
            >
              Giảm {{ item.phanTramGiam ? item.phanTramGiam + '%' : (item.giaBan - item.giaSauGiam).toLocaleString('vi-VN') + 'đ' }}
            </div>
          </div>

          <!-- Nội dung -->
          <div class="p-4 flex flex-col">
            <h3 class="font-semibold text-[15px] text-slate-800 line-clamp-2 leading-snug mb-2">
              {{ item.tenSanPham }}
            </h3>

            <!-- Giá -->
            <div class="mt-auto">
              <template v-if="item.dangGiamGia">
                <div class="flex items-baseline gap-2">
                  <span class="text-lg font-bold text-red-600">
                    {{ item.giaSauGiam.toLocaleString('vi-VN') }}đ
                  </span>
                  <span class="text-xs text-slate-400 line-through">
                    {{ item.giaBan.toLocaleString('vi-VN') }}đ
                  </span>
                </div>
              </template>
              <template v-else>
                <span class="text-lg font-bold text-slate-900">
                  {{ item.giaSauGiam.toLocaleString('vi-VN') }}đ
                </span>
              </template>
            </div>

            <!-- Meta -->
            <p class="text-xs text-slate-400 mt-1.5">{{ item.tenDanhMuc }} · {{ item.tenThuongHieu }}</p>
          </div>
        </div>
      </div>
    </div>
    <!-- Size Guide Modal -->
    <div v-if="showSizeGuide" class="fixed inset-0 z-50 flex items-center justify-center bg-black/50" @click.self="showSizeGuide = false">
      <div class="bg-white rounded-3xl w-full max-w-2xl max-h-[90vh] flex flex-col relative overflow-hidden shadow-2xl">
        
        <!-- Fixed Header & Form -->
        <div class="px-8 pt-8 pb-6 border-b border-gray-100 shadow-sm z-10 bg-white">
          <button @click="showSizeGuide = false" class="absolute top-6 right-6 text-gray-500 hover:text-black">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
          </button>
          
          <h2 class="text-3xl font-extrabold text-gray-900 mb-6">Gợi ý tìm kích cỡ</h2>
          
          <div class="grid grid-cols-2 gap-6 mb-6">
            <div>
              <label class="block text-sm text-gray-500 mb-2">Chiều cao</label>
              <div class="relative">
                <input v-model="userHeight" type="number" @keypress="validateNumberInput" min="1" class="w-full border border-gray-300 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-black" placeholder="165" />
                <span class="absolute right-4 top-3 text-gray-400">cm</span>
              </div>
            </div>
            <div>
              <label class="block text-sm text-gray-500 mb-2">Cân nặng</label>
              <div class="relative">
                <input v-model="userWeight" type="number" @keypress="validateNumberInput" min="1" class="w-full border border-gray-300 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-black" placeholder="55" />
                <span class="absolute right-4 top-3 text-gray-400">kg</span>
              </div>
            </div>
          </div>
          
          <div class="flex items-center gap-4">
            <button @click="calculateSize" class="bg-[#2f404d] text-white px-8 py-3 rounded-full font-bold hover:bg-slate-800 transition-colors">TÍNH TOÁN</button>
            <div v-if="suggestedSize" class="text-lg font-bold text-green-600">Size gợi ý: {{ suggestedSize }}</div>
          </div>
        </div>
        
        <!-- Scrollable Content -->
        <div class="overflow-y-auto flex-1 p-8 bg-gray-50/50">
          <h3 class="font-bold text-gray-900 uppercase border-b-2 border-black pb-2 mb-4 inline-block">THÔNG SỐ SẢN PHẨM</h3>
          <p class="text-sm text-gray-500 mb-6">*Số đo sản phẩm khi trải phẳng, có thể khác với số đo cơ thể do độ co giãn của vải</p>
          
          <div class="mb-6 rounded-xl border border-gray-300 overflow-hidden shadow-sm bg-white">
            <div class="overflow-x-auto">
              <table class="w-full text-sm text-center border-collapse">
                <thead class="bg-[#3A454F] text-white font-bold">
                  <tr>
                    <th class="py-4 px-4 uppercase border-r border-[#4A5562] whitespace-nowrap">SIZE</th>
                    <th class="py-4 px-2 border-r border-[#4A5562] whitespace-nowrap">Chiều cao (cm)</th>
                    <th class="py-4 px-2 border-r border-[#4A5562] whitespace-nowrap">Cân nặng (kg)</th>
                    <th class="py-4 px-2 border-r border-[#4A5562] whitespace-nowrap">Rộng ngực (cm)</th>
                    <th class="py-4 px-2 whitespace-nowrap">Rộng mông (cm)</th>
                  </tr>
                </thead>
                <tbody class="divide-y divide-gray-200">
                  <tr class="bg-white">
                    <td class="py-4 px-4 font-bold bg-[#424E5A] text-white border-r border-[#525E6A]">XS (28)</td>
                    <td class="py-4 px-2 font-semibold text-gray-700 border-r border-gray-100">155-165</td>
                    <td class="py-4 px-2 font-semibold text-gray-700 border-r border-gray-100">55-60</td>
                    <td class="py-4 px-2 font-semibold text-gray-700 border-r border-gray-100">76-84</td>
                    <td class="py-4 px-2 font-semibold text-gray-700">82-96</td>
                  </tr>
                  <tr class="bg-gray-50">
                    <td class="py-4 px-4 font-bold bg-[#4A5763] text-white border-r border-[#5A6875]">S (29)</td>
                    <td class="py-4 px-2 font-semibold text-gray-700 border-r border-gray-100">162-168</td>
                    <td class="py-4 px-2 font-semibold text-gray-700 border-r border-gray-100">57-62</td>
                    <td class="py-4 px-2 font-semibold text-gray-700 border-r border-gray-100">84-88</td>
                    <td class="py-4 px-2 font-semibold text-gray-700">85-89</td>
                  </tr>
                  <tr class="bg-white">
                    <td class="py-4 px-4 font-bold bg-[#424E5A] text-white border-r border-[#525E6A]">M (30)</td>
                    <td class="py-4 px-2 font-semibold text-gray-700 border-r border-gray-100">169-173</td>
                    <td class="py-4 px-2 font-semibold text-gray-700 border-r border-gray-100">63-67</td>
                    <td class="py-4 px-2 font-semibold text-gray-700 border-r border-gray-100">88-94</td>
                    <td class="py-4 px-2 font-semibold text-gray-700">90-94</td>
                  </tr>
                  <tr class="bg-gray-50">
                    <td class="py-4 px-4 font-bold bg-[#4A5763] text-white border-r border-[#5A6875]">L (31)</td>
                    <td class="py-4 px-2 font-semibold text-gray-700 border-r border-gray-100">171-175</td>
                    <td class="py-4 px-2 font-semibold text-gray-700 border-r border-gray-100">68-72</td>
                    <td class="py-4 px-2 font-semibold text-gray-700 border-r border-gray-100">94-98</td>
                    <td class="py-4 px-2 font-semibold text-gray-700">95-99</td>
                  </tr>
                  <tr class="bg-white">
                    <td class="py-4 px-4 font-bold bg-[#424E5A] text-white border-r border-[#525E6A]">XL (32)</td>
                    <td class="py-4 px-2 font-semibold text-gray-700 border-r border-gray-100">173-177</td>
                    <td class="py-4 px-2 font-semibold text-gray-700 border-r border-gray-100">73-77</td>
                    <td class="py-4 px-2 font-semibold text-gray-700 border-r border-gray-100">98-104</td>
                    <td class="py-4 px-2 font-semibold text-gray-700">100-104</td>
                  </tr>
                  <tr class="bg-gray-50">
                    <td class="py-4 px-4 font-bold bg-[#4A5763] text-white border-r border-[#5A6875]">XXL (33)</td>
                    <td class="py-4 px-2 font-semibold text-gray-700 border-r border-gray-100">175-179</td>
                    <td class="py-4 px-2 font-semibold text-gray-700 border-r border-gray-100">78-82</td>
                    <td class="py-4 px-2 font-semibold text-gray-700 border-r border-gray-100">104-107</td>
                    <td class="py-4 px-2 font-semibold text-gray-700">104-108</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
          
          <div class="bg-white border border-gray-300 p-5 rounded-xl text-sm text-gray-800 shadow-sm">
            <p class="font-bold mb-2">Nếu số đo của bạn nằm giữa 2 size:</p>
            <ul class="list-disc pl-5 space-y-1">
              <li>Đối với áo nam, ưu tiên theo cân nặng.</li>
              <li>97% khách hàng chọn đúng size theo cách này.</li>
              <li>Các số đo được tính bằng centimet (cm).</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'

import { useRoute } from 'vue-router'

import { getAllSanpham } from '@/service/SanphamService'

import { useRouter } from 'vue-router'

import { getShopVariantsByProductId } from '@/service/SanPhamChiTiet'

import { getAllSanPhamChiTiet } from '@/service/SanPhamChiTiet'

import axios from 'axios'

import stompClient from '@/socket'

import emitter from '@/utils/emitter'
const route = useRoute()

const router = useRouter()

const product = ref(null)

const selectedColor = ref(null)

const selectedVariant = ref(null)

const loading = ref(true)

const mainImage = ref('')

const quantity = ref(1)

const shopVariants = ref([])

const API_URL = 'http://localhost:8080'

const placeholder = 'https://via.placeholder.com/300'

const showSizeGuide = ref(false)
const userHeight = ref('')
const userWeight = ref('')
const suggestedSize = ref('')

const validateNumberInput = (e) => {
  if (['e', 'E', '+', '-', '.', ','].includes(e.key)) {
    e.preventDefault()
  }
}

const calculateSize = () => {
  const h = Number(userHeight.value)
  const w = Number(userWeight.value)
  if (!h || !w || h <= 0 || w <= 0) return alert('Vui lòng nhập chiều cao và cân nặng hợp lệ')
  
  let sizeByWeight = 'XS'
  if (w > 77) sizeByWeight = 'XXL'
  else if (w > 72) sizeByWeight = 'XL'
  else if (w > 67) sizeByWeight = 'L'
  else if (w > 62) sizeByWeight = 'M'
  else if (w > 60) sizeByWeight = 'S'
  
  let sizeByHeight = 'XS'
  if (h > 177) sizeByHeight = 'XXL'
  else if (h > 175) sizeByHeight = 'XL'
  else if (h > 173) sizeByHeight = 'L'
  else if (h > 168) sizeByHeight = 'M'
  else if (h > 165) sizeByHeight = 'S'
  
  const sizes = ['XS', 'S', 'M', 'L', 'XL', 'XXL']
  const weightIndex = sizes.indexOf(sizeByWeight)
  const heightIndex = sizes.indexOf(sizeByHeight)
  
  suggestedSize.value = sizes[Math.max(weightIndex, heightIndex)]
}

const loadShopProducts = async () => {
  const data = await getAllSanPhamChiTiet()

  shopVariants.value = data.filter(
    (item) => item.trangThai && item.idSanPham !== Number(route.params.id),
  )
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

    // Tổng tồn
    p.tongSoLuong += item.soLuongTon

    // Giá bán thấp nhất
    if (item.giaBan < p.giaBan) {
      p.giaBan = item.giaBan
    }

    // Giá sau giảm thấp nhất
    const currentPrice = item.giaSauGiam ?? item.giaBan

    if (currentPrice < p.giaSauGiam) {
      p.giaSauGiam = currentPrice
    }

    // Lấy mức giảm lớn nhất
    if (item.dangGiamGia && (item.phanTramGiam ?? 0) > p.phanTramGiam) {
      p.phanTramGiam = item.phanTramGiam
      p.dangGiamGia = true
    }

    // Màu
    if (!p.colors.includes(item.tenMauSac)) {
      p.colors.push(item.tenMauSac)
    }
  })

  return Object.values(groups).slice(0, 8)
})
const loadProduct = async () => {
  loading.value = true

  try {
    const data = await getShopVariantsByProductId(route.params.id)

    product.value = data

    if (data.colors.length) {
      selectedColor.value = data.colors[0]

      if (data.colors[0].variants.length) {
        selectedVariant.value = data.colors[0].variants[0]
      }
    }

    quantity.value = 1
  } catch (e) {
    console.log(e)
  } finally {
    loading.value = false
  }
}

const increaseQty = () => {
  if (!selectedVariant.value) return

  if (quantity.value < selectedVariant.value.soLuongTon) {
    quantity.value++
  }
}

const decreaseQty = () => {
  if (quantity.value > 1) {
    quantity.value--
  }
}

const galleryImages = computed(() => {
  if (!product.value) return []

  return product.value.gallery.map((item) => ({
    ...item,
    url: `http://localhost:8080${item.image}`,
  }))
})

watch(selectedVariant, (variant) => {
  if (!variant || !product.value) return

  const firstImage = product.value.gallery.find((g) => g.spctId === variant.id)

  mainImage.value = firstImage ? `http://localhost:8080${firstImage.image}` : ''
})
const selectImage = (img) => {
  mainImage.value = img.url

  for (const color of product.value.colors) {
    const variant = color.variants.find((v) => v.id === img.spctId)

    if (variant) {
      selectedColor.value = color
      selectedVariant.value = variant
      quantity.value = variant.soLuongTon > 0 ? 1 : 0
      break
    }
  }
}
watch(selectedVariant, (variant) => {
  if (!variant) return

  if (variant.soLuongTon === 0) {
    quantity.value = 0
  } else {
    quantity.value = 1
  }
})

const selectColor = (color) => {
  selectedColor.value = color

  selectedVariant.value = color.variants?.[0] ?? null

  quantity.value = 1
}

const selectVariant = (variant) => {
  selectedVariant.value = variant

  quantity.value = 1
}

onMounted(async () => {
  try {
    const data = await getShopVariantsByProductId(route.params.id)

    await loadShopProducts()
    await loadProduct()

    product.value = data

    if (data.colors.length) {
      selectedColor.value = data.colors[0]

      if (data.colors[0].variants.length) {
        selectedVariant.value = data.colors[0].variants[0]
      }
    }
  } catch (e) {
    console.log(e)
  } finally {
    loading.value = false
  }
  connectSocket()
})
function connectSocket() {
  if (stompClient.connected) {
    subscribeDetail()
  } else {
    stompClient.onConnect = () => {
      console.log('✅ Connected')

      subscribeDetail()
    }
  }
}

function subscribeDetail() {
  stompClient.subscribe('/topic/pos', async (msg) => {
    const event = JSON.parse(msg.body)

    switch (event.type) {
      case 'DISCOUNT_UPDATED':
        await loadShopProducts()
        await loadProduct()
        break

      case 'PRODUCT_UPDATED':
        await loadShopProducts()
        await loadProduct()
        break
    }
  })
}
const addToCart = async () => {
  const token = localStorage.getItem('token')

  if (!token) {
    alert('Bạn cần đăng nhập')
    router.push('/login')
    return
  }

  if (!selectedVariant.value) {
    alert('Vui lòng chọn sản phẩm')
    return
  }

  try {
    const res = await axios.post(
      'http://localhost:8080/giohang/them',
      {
        idSanPhamChiTiet: selectedVariant.value.id,
        soLuong: quantity.value,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      },
    )

    console.log('Emit cart-updated')
    // Báo Header tải lại giỏ hàng
    emitter.emit('cart-updated')

    alert('Thêm vào giỏ thành công 🎉')
  } catch (err) {
    console.log(err)
    alert(err?.response?.data || 'Lỗi thêm giỏ hàng')
  }
}

const buyNow = () => {
  const token = sessionStorage.getItem('token')

  if (!token) {
    router.push('/login')
    return
  }

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

    window.scrollTo({
      top: 0,
      behavior: 'smooth',
    })
  },
)
</script>
