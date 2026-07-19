<template>
  <div class="w-full relative font-sans select-none h-[500px] md:h-[550px] bg-[#f8f9fa] overflow-hidden group">
    
    <!-- Slides Container -->
    <div class="relative w-full h-full z-10">
      <transition-group name="banner-fade">
        <div
          v-for="(slide, index) in bannerSlides"
          :key="index"
          v-show="currentSlide === index"
          class="absolute inset-0 w-full h-full transition-colors duration-500"
          :style="{ backgroundColor: slide.bgColor || '#db2827' }"
        >
          <!-- TAG LAYOUT (Dành cho slide khuyến mãi đặc biệt) -->
          <div v-if="slide.type === 'tag'" class="w-full h-full flex items-center justify-center max-w-[1400px] mx-auto relative px-4">
            
            <div class="w-full max-w-[1000px] h-[400px] border-[5px] border-white rounded-l-[10px] rounded-r-[200px] relative mt-8">
              <!-- Banner Image -->
              <img 
                :src="slide.image" 
                :alt="slide.alt || 'Banner ' + (index + 1)"
                class="absolute inset-0 w-full h-full object-cover rounded-l-[5px] rounded-r-[195px]"
              />
              
              <!-- Overlay gradient for text readability -->
              <div class="absolute inset-0 bg-gradient-to-r from-[#db2827]/90 to-transparent rounded-l-[5px] rounded-r-[195px] z-[1]"></div>

              <!-- Huge faint SALE text watermark inside tag -->
              <div class="absolute inset-0 overflow-hidden rounded-l-[5px] rounded-r-[195px] z-[2] pointer-events-none flex items-center justify-center">
                <div class="text-[280px] font-black text-[#c01817] opacity-60 transform translate-y-16 -translate-x-12 select-none tracking-tighter">SALE</div>
              </div>

              <!-- Overlapping Title Box (Top Center-Left) -->
              <div class="absolute -top-[50px] left-[8%] bg-white rounded-[30px] px-8 py-4 flex items-center gap-8 shadow-[0_15px_30px_-5px_rgba(0,0,0,0.3)] border-b-[6px] border-[#b01c1c] z-30">
                <div class="flex flex-col items-center justify-center">
                  <div class="bg-[#db2827] text-white px-5 py-1 rounded-full text-[12px] font-black tracking-widest text-center shadow-inner flex items-center gap-2">
                    TRẠM
                  </div>
                  <div class="text-[34px] font-black text-[#db2827] leading-none mt-1 tracking-tight">Deals Hời</div>
                </div>
                <div class="pl-2">
                  <div class="text-[#2f404d] text-[20px] font-black uppercase leading-tight tracking-wide">
                    End of<br/>season sale
                  </div>
                  <div class="text-[#2f404d] font-bold text-[13px] mt-1">
                    16/07 - 26/07
                  </div>
                </div>
              </div>

              <div class="relative z-20 flex h-full items-center pl-10 pr-24">
                <!-- Left Side: 70% -->
                <div class="w-[45%] flex flex-col pt-6">
                  <div class="bg-white text-[#2f404d] text-[18px] font-black px-4 py-1.5 w-max rounded-sm shadow-md">
                    {{ slide.discountLabel || 'Ưu đãi tới' }}
                  </div>
                  <div class="text-[180px] font-black text-white leading-[0.8] tracking-tighter flex items-end -ml-2 mt-4" style="text-shadow: 0px 5px 15px rgba(0,0,0,0.2);">
                    {{ slide.discountValue || '70' }}<span class="text-[100px] mb-4">%</span>
                  </div>
                </div>

                <!-- Right Side: Vouchers & Info -->
                <div class="w-[55%] flex flex-col justify-center pt-6">
                  <div class="border-[2px] border-white rounded-[12px] p-5 text-white bg-transparent w-[100%] shadow-sm backdrop-blur-sm bg-black/10">
                    <div class="text-[16px] font-extrabold uppercase tracking-widest mb-3">{{ slide.voucherTitle || 'ĐỘC QUYỀN WEB, APP' }}</div>
                    <div class="flex items-start">
                      <div class="flex-1">
                        <div class="text-[11px] font-bold mb-0.5">Voucher</div>
                        <div class="text-[44px] font-black leading-[0.9] tracking-tighter">{{ slide.voucher1 || '200K' }}</div>
                        <div class="text-[10px] mt-1 font-medium opacity-90">{{ slide.voucher1Min || 'Đơn từ 1.299K' }}</div>
                      </div>
                      <div class="w-px h-14 bg-white/60 mx-4 mt-2"></div>
                      <div class="flex-1 pl-2">
                        <div class="text-[11px] font-bold mb-0.5">Voucher</div>
                        <div class="text-[44px] font-black leading-[0.9] tracking-tighter">{{ slide.voucher2 || '100K' }}</div>
                        <div class="text-[10px] mt-1 font-medium opacity-90">{{ slide.voucher2Min || 'Đơn từ 799K' }}</div>
                      </div>
                    </div>
                  </div>
                  <button class="mt-4 bg-white text-[#2f404d] font-extrabold text-[13px] px-8 py-3 w-max rounded-[4px] hover:bg-gray-100 transition shadow-lg hover:shadow-xl transform hover:-translate-y-0.5">
                    Mua ngay
                  </button>
                </div>
              </div>

              <!-- The Tag Hole (Far right edge) -->
              <div class="absolute right-[40px] top-1/2 -translate-y-1/2 w-10 h-10 rounded-full border-[3px] border-white bg-[#db2827] flex items-center justify-center z-20 shadow-inner">
              </div>
            </div>
          </div>

          <!-- IMAGE LAYOUT (Dành cho slide ảnh thuần túy, tràn viền) -->
          <div v-else class="w-full h-full relative cursor-pointer block">
            <img 
              :src="slide.image" 
              :alt="slide.alt || 'Banner ' + (index + 1)"
              class="absolute inset-0 w-full h-full object-cover object-center"
            />
          </div>

        </div>
      </transition-group>
    </div>

    <!-- Controls Overlay (Arrows and Dots) -->
    <div class="absolute inset-0 max-w-[1400px] mx-auto pointer-events-none z-30">
      <!-- Left Arrow -->
      <button 
        @click="prevSlide" 
        class="pointer-events-auto absolute left-4 md:left-8 top-1/2 -translate-y-1/2 w-12 h-12 bg-white/70 hover:bg-white hover:scale-110 text-gray-800 flex items-center justify-center transition-all duration-300 shadow-[0_4px_15px_rgba(0,0,0,0.15)] rounded-full z-40"
        aria-label="Previous Slide"
      >
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="3" stroke="currentColor" class="w-6 h-6 pr-0.5"><path stroke-linecap="round" stroke-linejoin="round" d="M15.75 19.5L8.25 12l7.5-7.5" /></svg>
      </button>

      <!-- Right Arrow -->
      <button 
        @click="nextSlide" 
        class="pointer-events-auto absolute right-4 md:right-8 top-1/2 -translate-y-1/2 w-12 h-12 bg-white/70 hover:bg-white hover:scale-110 text-gray-800 flex items-center justify-center transition-all duration-300 shadow-[0_4px_15px_rgba(0,0,0,0.15)] rounded-full z-40"
        aria-label="Next Slide"
      >
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="3" stroke="currentColor" class="w-6 h-6 pl-0.5"><path stroke-linecap="round" stroke-linejoin="round" d="M8.25 4.5l7.5 7.5-7.5 7.5" /></svg>
      </button>

      <!-- Dynamic Slider Dots -->
      <div class="pointer-events-auto absolute bottom-6 left-1/2 -translate-x-1/2 flex gap-2 bg-black/40 px-3 py-2 rounded-full backdrop-blur-md shadow-lg">
        <button 
          v-for="(slide, index) in bannerSlides" 
          :key="'dot-' + index"
          @click="goToSlide(index)"
          :class="[
            'rounded-full transition-all duration-300',
            currentSlide === index 
              ? 'w-6 h-2 bg-white shadow-sm' 
              : 'w-2 h-2 bg-white/50 hover:bg-white/90'
          ]"
        ></button>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const bannerSlides = ref([
  {
    type: 'tag',
    image: 'https://images.unsplash.com/photo-1607082348824-0a96f2a4b9da?w=1200&q=80',
    alt: 'End of Season Sale',
    discountLabel: 'Ưu đãi tới',
    discountValue: '70',
    voucherTitle: 'ĐỘC QUYỀN WEB, APP',
    voucher1: '200K',
    voucher1Min: 'Đơn từ 1.299K',
    voucher2: '100K',
    voucher2Min: 'Đơn từ 799K',
    bgColor: '#db2827'
  },
  {
    type: 'image',
    image: 'https://images.unsplash.com/photo-1483985988355-763728e1935b?w=1200&q=80', 
    alt: 'Trạm Hè Đa Sắc',
    bgColor: '#f8f9fa' 
  },
  {
    type: 'image',
    image: 'https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=1200&q=80',
    alt: 'New Arrivals',
    bgColor: '#db2827'
  },
  {
    type: 'image',
    image: 'https://images.unsplash.com/photo-1556905055-8f358a7a47b2?w=1200&q=80',
    alt: 'Summer Collection',
    bgColor: '#db2827'
  },
])

const currentSlide = ref(0)
let autoSlideTimer = null

const nextSlide = () => {
  currentSlide.value = (currentSlide.value + 1) % bannerSlides.value.length
  resetAutoSlide()
}

const prevSlide = () => {
  currentSlide.value = (currentSlide.value - 1 + bannerSlides.value.length) % bannerSlides.value.length
  resetAutoSlide()
}

const goToSlide = (index) => {
  currentSlide.value = index
  resetAutoSlide()
}

const startAutoSlide = () => {
  autoSlideTimer = setInterval(() => {
    currentSlide.value = (currentSlide.value + 1) % bannerSlides.value.length
  }, 5000)
}

const resetAutoSlide = () => {
  if (autoSlideTimer) clearInterval(autoSlideTimer)
  startAutoSlide()
}

onMounted(() => {
  startAutoSlide()
})

onUnmounted(() => {
  if (autoSlideTimer) clearInterval(autoSlideTimer)
})
</script>

<style scoped>
.banner-fade-enter-active,
.banner-fade-leave-active {
  transition: opacity 0.8s ease;
}
.banner-fade-enter-from,
.banner-fade-leave-to {
  opacity: 0;
}
</style>
