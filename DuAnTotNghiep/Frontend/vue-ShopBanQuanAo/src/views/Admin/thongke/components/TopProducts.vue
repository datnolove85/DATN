<template>
  <div class="bg-white rounded-3xl border border-slate-200 shadow-sm overflow-hidden">
    <!-- HEADER -->
    <div class="p-6 border-b border-slate-100">
      <h2 class="text-xl font-black text-slate-800">Top sản phẩm bán chạy</h2>

      <p class="text-sm text-slate-500 mt-1">Sản phẩm có doanh số cao nhất</p>
    </div>

    <!-- LIST -->
    <div class="divide-y divide-slate-100">
      <div
        v-for="(item, index) in data"
        :key="item.productId"
        class="p-5 hover:bg-slate-50 transition flex gap-4"
      >
        <!-- RANK -->
        <div
          class="w-8 h-8 rounded-full bg-indigo-100 text-indigo-600 flex items-center justify-center font-black shrink-0"
        >
          {{ index + 1 }}
        </div>

        <!-- IMAGE -->
        <img
          :src="imageUrl(item.anh)"
          class="w-16 h-16 rounded-2xl object-cover border border-slate-200"
          @error="imageError"
        />

        <!-- INFO -->
        <div class="flex-1">
          <div class="font-bold text-slate-800">
            {{ item.tenSanPham }}
          </div>

          <div class="text-xs text-slate-500 mt-1 flex flex-wrap items-center gap-1.5">
            <span>{{ item.maSanPham }}</span>
            <span>•</span>
            <!-- Hiển thị Thương hiệu nếu có -->
            <span v-if="item.thuongHieu" class="text-indigo-600 font-semibold">{{
              item.thuongHieu
            }}</span>
            <span v-if="item.thuongHieu">•</span>
            <!-- Hiển thị Chất liệu nếu có -->
            <span v-if="item.chatLieu" class="text-emerald-600 font-semibold">{{
              item.chatLieu
            }}</span>
            <span v-if="item.chatLieu">•</span>
            <span>{{ item.soBienThe }} biến thể</span>
          </div>

          <!-- PROGRESS -->
          <div class="mt-3">
            <div class="flex justify-between text-xs mb-1">
              <span class="text-slate-500"> Đã bán {{ item.soLuongBan }} </span>

              <span class="font-bold text-indigo-600"> {{ percent(item.soLuongBan) }}% </span>
            </div>

            <div class="h-2 bg-slate-100 rounded-full overflow-hidden">
              <div
                class="h-full bg-indigo-600 rounded-full"
                :style="{
                  width: percent(item.soLuongBan) + '%',
                }"
              ></div>
            </div>
          </div>
        </div>

        <!-- MONEY -->
        <div class="text-right min-w-[130px]">
          <div class="font-black text-indigo-600">
            {{ money(item.doanhThu) }}
          </div>

          <div class="text-xs text-emerald-600 mt-1">Lãi {{ money(item.loiNhuan) }}</div>

          <div class="text-xs text-slate-400 mt-2">Tồn {{ item.tongTonKho }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  data: {
    type: Array,
    default: () => [],
  },
})

const imageUrl = (path) => {
  if (!path) {
    return 'https://placehold.co/100x100?text=No+Image'
  }

  return 'http://localhost:8080' + path
}

const imageError = (event) => {
  event.target.src = 'https://placehold.co/100x100?text=No+Image'
}

const money = (value) => {
  return Number(value || 0).toLocaleString('vi-VN') + ' ₫'
}

const maxSold = () => {
  return Math.max(...props.data.map((x) => x.soLuongBan), 1)
}

const percent = (value) => {
  return Math.round((value / maxSold()) * 100)
}
</script>
