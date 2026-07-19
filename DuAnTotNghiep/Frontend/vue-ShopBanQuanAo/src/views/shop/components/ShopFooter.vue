<template>
  <footer class="bg-black text-white pt-10 pb-4 mt-10 text-sm">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      
      <!-- Top Section -->
      <div class="flex flex-col lg:flex-row justify-between items-start border-b border-gray-800 pb-8 mb-8 gap-8">
        <!-- Left: Company Info -->
        <div class="flex-[1.5]">
          <div class="text-3xl font-black mb-3">Vel<span class="text-indigo-500">ora</span></div>
          <p class="mb-2 text-gray-200"><span class="font-semibold text-white">Địa chỉ:</span> Tòa nhà HONG HA NUTRITION, số 82 Phố Dịch Vọng Hậu, Phường Cầu Giấy,Thành phố Hà Nội, Việt Nam</p>
          <p class="mb-4 text-gray-200"><span class="font-semibold text-white">Điện thoại:</span> 0982.022.969</p>
          <button class="bg-white text-black font-semibold py-2 px-6 text-sm hover:bg-gray-200 transition-colors">
            LIÊN HỆ VỚI CHÚNG TÔI
          </button>
        </div>

        <!-- Middle: Contact -->
        <div class="flex-1 flex flex-col gap-6">
          <div class="flex items-start gap-3">
            <Smartphone class="w-6 h-6 shrink-0" />
            <div>
              <div class="font-semibold text-sm mb-1">ĐẶT HÀNG (08:30 - 22:00)</div>
              <div class="font-bold">0982.022.969</div>
            </div>
          </div>
          <div class="flex items-start gap-3">
            <Headphones class="w-6 h-6 shrink-0" />
            <div>
              <div class="font-semibold text-sm mb-1">GÓP Ý, KHIẾU NẠI (08:30 - 22:00)</div>
              <div class="font-bold">08382.88888</div>
            </div>
          </div>
        </div>

        <!-- Right: Social Links -->
        <div class="flex-1">
          <div class="font-semibold mb-3">Kết nối</div>
          <div class="flex gap-3">
            <!-- Facebook -->
            <a href="#" class="w-10 h-10 border-2 border-white flex items-center justify-center hover:bg-gray-800 transition-colors rounded-lg">
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M18 2h-3a5 5 0 0 0-5 5v3H7v4h3v8h4v-8h3l1-4h-4V7a1 1 0 0 1 1-1h3z"/></svg>
            </a>
            <!-- Zalo -->
            <a href="#" class="w-10 h-10 border-2 border-white flex items-center justify-center hover:bg-gray-800 transition-colors rounded-lg text-xs font-bold">
              Zalo
            </a>
            <!-- Tiktok -->
            <a href="#" class="w-10 h-10 border-2 border-white flex items-center justify-center hover:bg-gray-800 transition-colors rounded-lg">
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M9 12a4 4 0 1 0 4 4V4a5 5 0 0 0 5 5"/><path d="M15 8a4 4 0 1 0 0-8"/></svg>
            </a>
            <!-- Shopee -->
            <a href="#" class="w-10 h-10 border-2 border-white flex items-center justify-center hover:bg-gray-800 transition-colors rounded-lg">
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M6 2L3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"></path><line x1="3" y1="6" x2="21" y2="6"></line><path d="M16 10a4 4 0 0 1-8 0"></path></svg>
            </a>
          </div>
        </div>
      </div>

      <!-- Links Section -->
      <div class="grid grid-cols-1 md:grid-cols-5 gap-8 mb-8 text-sm">
        <!-- VELORA -->
        <div>
          <h4 class="font-bold mb-4 uppercase text-base">VELORA</h4>
          <ul class="space-y-2 text-gray-300">
            <li><a href="#" class="hover:text-white transition-colors">Liên hệ</a></li>
            <li><a href="#" class="hover:text-white transition-colors">Ưu đãi độc quyền</a></li>
          </ul>
        </div>
        
        <!-- CHÍNH SÁCH -->
        <div>
          <h4 class="font-bold mb-4 uppercase text-base">CHÍNH SÁCH</h4>
          <ul class="space-y-2 text-gray-300">
            <li><a href="#" class="hover:text-white transition-colors">Chính sách thành viên</a></li>
            <li><a href="#" class="hover:text-white transition-colors">Chính sách giao hàng</a></li>
            <li><a href="#" class="hover:text-white transition-colors">Quy định đổi trả</a></li>
            <li><a href="#" class="hover:text-white transition-colors">Chính sách xử lý khiếu nại</a></li>
          </ul>
        </div>

        <!-- KIẾN THỨC MẶC ĐẸP -->
        <div>
          <h4 class="font-bold mb-4 uppercase text-base">KIẾN THỨC MẶC ĐẸP</h4>
          <ul class="space-y-2 text-gray-300">
            <li><a href="#" class="hover:text-white transition-colors">Blog</a></li>
            <li><a href="#" class="hover:text-white transition-colors">VELORA có gì mới?</a></li>
          </ul>
        </div>

        <!-- VỀ VELORA -->
        <div>
          <h4 class="font-bold mb-4 uppercase text-base">VỀ VELORA</h4>
          <ul class="space-y-2 text-gray-300">
            <li><a href="#" class="hover:text-white transition-colors">Hệ thống cửa hàng</a></li>
            <li><a href="#" class="hover:text-white transition-colors">Tuyển dụng</a></li>
          </ul>
        </div>

        <!-- BẢN ĐỒ -->
        <div>
          <h4 class="font-bold mb-4 uppercase text-base">BẢN ĐỒ</h4>
          <div class="h-28 bg-gray-800 rounded overflow-hidden">
            <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3723.8638558813926!2d105.74312641476343!3d21.038132793563914!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x313454b991d80fd5%3A0x53cefc99d6b0bf6f!2zMTg5IFBobyBOaOG7lW4sIFh1w6JuIFBoxrDGoW5nLCBU4burIExpw6ptLCBIw6AgTuG7mWksIFZp4buHdCBOYW0!5e0!3m2!1svi!2s!4v1689694406155!5m2!1svi!2s" width="100%" height="100%" style="border:0;" allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade"></iframe>
          </div>
        </div>
      </div>

      <!-- Bottom Section -->
      <div class="flex flex-col md:flex-row justify-between items-center text-xs text-gray-200 mt-16 pb-4">
        <p>***Mã số doanh nghiệp: 0111901433 do Sở kế hoạch và đầu tư thành phố Hà Nội cấp ngày 17/09/2019</p>
        <div class="flex items-center gap-4 mt-4 md:mt-0">
            <!-- Scroll to top button -->
            <button @click="scrollToTop" class="w-10 h-10 border border-[#0d6efd] rounded-full flex items-center justify-center text-[#0d6efd] hover:bg-[#0d6efd] hover:text-white transition-colors ml-4 bg-[#0a0a0a]">
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m18 15-6-6-6 6"/></svg>
            </button>
        </div>
      </div>
      
    </div>
  </footer>
</template>

<script setup>
import { Smartphone, Headphones } from 'lucide-vue-next'

const scrollToTop = () => {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}
</script>
