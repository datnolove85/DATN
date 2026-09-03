<template>
  <footer class="w-full bg-black text-white mt-auto">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
      <!-- TOP -->
      <div
        class="grid grid-cols-1 lg:grid-cols-[1.5fr_1fr_0.8fr] gap-10 border-b border-neutral-800 pb-10"
      >
        <!-- Company -->
        <div>
          <h2 class="text-2xl font-extrabold tracking-tight">
            K-<span class="text-indigo-500">Zone</span>
          </h2>

          <p class="mt-4 text-sm font-semibold uppercase tracking-wider text-gray-200">
            CỬA HÀNG THỜI TRANG K-ZONE
          </p>

          <p class="mt-3 text-sm leading-6 text-gray-400">
            <span class="font-semibold text-gray-200">Địa chỉ:</span>
            58 Nguyễn Khánh Toàn, Quận Cầu Giấy,<br />
            Thành phố Hà Nội, Việt Nam
          </p>

          <p class="mt-2 text-sm leading-6 text-gray-400">
            <span class="font-semibold text-gray-200">Điện thoại:</span>
            0377.280.805
          </p>

          <button
            class="mt-5 bg-white text-black px-6 py-2 rounded-md text-sm font-semibold hover:bg-gray-200 transition"
          >
            LIÊN HỆ VỚI CHÚNG TÔI
          </button>
        </div>

        <!-- Hotline -->
        <div class="space-y-6">
          <div class="flex items-start gap-4">
            <div class="w-8 flex justify-center pt-0.5">
              <i class="fas fa-mobile-alt text-base text-gray-400"></i>
            </div>
            <div>
              <div class="flex items-center gap-2 flex-wrap">
                <span class="text-sm font-semibold uppercase tracking-wide">ĐẶT HÀNG</span>
                <span class="text-xs text-gray-400">(08:30 - 22:00)</span>
              </div>
              <p class="mt-1 text-base font-bold tracking-wide">0377.280.805</p>
            </div>
          </div>

          <div class="flex items-start gap-4">
            <div class="w-8 flex justify-center pt-0.5">
              <i class="fas fa-phone-volume text-base text-gray-400"></i>
            </div>
            <div>
              <div class="flex items-center gap-2 flex-wrap">
                <span class="text-sm font-semibold uppercase tracking-wide">GÓP Ý, KHIẾU NẠI</span>
                <span class="text-xs text-gray-400">(08:30 - 22:00)</span>
              </div>
              <p class="mt-1 text-base font-bold tracking-wide">08382.88888</p>
            </div>
          </div>
        </div>

        <!-- Social -->
        <div>
          <h3 class="text-sm font-bold uppercase tracking-wider mb-4 text-gray-200">Kết nối</h3>
          <div class="flex gap-3">
            <a
              href="#"
              class="w-10 h-10 rounded-lg border border-neutral-600 flex items-center justify-center text-sm hover:border-white transition"
            >
              <i class="fab fa-facebook-f"></i>
            </a>
            <a
              href="#"
              class="w-10 h-10 rounded-lg border border-neutral-600 flex items-center justify-center hover:border-white transition"
            >
              <span class="text-xs font-semibold">Zalo</span>
            </a>
            <a
              href="https://tiktok.com"
              target="_blank"
              class="w-10 h-10 rounded-lg border border-neutral-600 flex items-center justify-center text-sm hover:border-white transition"
            >
              <i class="fab fa-tiktok"></i>
            </a>
            <a
              href="#"
              class="w-10 h-10 rounded-lg border border-neutral-600 flex items-center justify-center text-sm hover:border-white transition"
            >
              <i class="fas fa-shopping-bag"></i>
            </a>
          </div>
        </div>
      </div>

      <!-- MENU -->
      <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-8 py-8">
        <div>
          <h3 class="text-xs font-bold uppercase tracking-widest mb-3 text-gray-200">K-ZONE</h3>
          <ul class="space-y-2 text-sm text-gray-400">
            <li><a href="#" class="hover:text-white transition">Liên hệ</a></li>
            <li><a href="#" class="hover:text-white transition">Ưu đãi độc quyền</a></li>
          </ul>
        </div>

        <div>
          <h3 class="text-xs font-bold uppercase tracking-widest mb-3 text-gray-200">CHÍNH SÁCH</h3>
          <ul class="space-y-2 text-sm text-gray-400">
            <li><a href="#" class="hover:text-white transition">Chính sách thành viên</a></li>
            <li>
              <router-link to="/chinh-sach-giao-hang" class="hover:text-white transition"
                >Chính sách giao hàng</router-link
              >
            </li>
            <li>
              <router-link to="/quy-dinh-doi-tra" class="hover:text-white transition"
                >Quy định đổi trả</router-link
              >
            </li>
            <li><a href="#" class="hover:text-white transition">Xử lý khiếu nại</a></li>
          </ul>
        </div>

        <div>
          <h3 class="text-xs font-bold uppercase tracking-widest mb-3 text-gray-200">KIẾN THỨC</h3>
          <ul class="space-y-2 text-sm text-gray-400">
            <li><a href="#" class="hover:text-white transition">Blog thời trang</a></li>
            <li><a href="#" class="hover:text-white transition">K-Zone có gì mới?</a></li>
          </ul>
        </div>

        <div>
          <h3 class="text-xs font-bold uppercase tracking-widest mb-3 text-gray-200">VỀ K-ZONE</h3>
          <ul class="space-y-2 text-sm text-gray-400">
            <li><a href="#" class="hover:text-white transition">Hệ thống cửa hàng</a></li>
            <li><a href="#" class="hover:text-white transition">Tuyển dụng</a></li>
          </ul>
        </div>

        <!-- MAP -->
        <div>
          <h3 class="text-xs font-bold uppercase tracking-widest mb-3 text-gray-200">BẢN ĐỒ</h3>
          <iframe
            class="w-full h-32 rounded-lg"
            loading="lazy"
            src="https://maps.google.com/maps?q=280%20Nguyễn%20Trãi%20Thanh%20Xuân%20Hà%20Nội&z=15&output=embed"
          ></iframe>
        </div>
      </div>

      <!-- COPYRIGHT -->
      <div class="border-t border-neutral-800 pt-4 flex flex-col md:flex-row justify-between gap-3">
        <p class="text-xs text-gray-500">
          Mã số doanh nghiệp: 0108901419 do Sở Kế hoạch và Đầu tư Hà Nội cấp.
        </p>
        <p class="text-xs text-gray-500">© 2026 K-Zone. All rights reserved.</p>
      </div>
    </div>
  </footer>
</template>

<script setup></script>

<style scoped>
footer {
  font-family: 'Be Vietnam Pro', sans-serif;
  line-height: 1.7;
}
footer a {
  transition: all 0.25s ease;
}
button {
  letter-spacing: 0.3px;
}
</style>
