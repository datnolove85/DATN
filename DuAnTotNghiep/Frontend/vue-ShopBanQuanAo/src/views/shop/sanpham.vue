<template>
  <div class="min-h-screen bg-[#f6f7fb] text-slate-950">
    <!-- PRODUCT DISCOVERY HERO -->
    <section class="product-hero relative overflow-hidden border-b border-[#e8e0d5] bg-[#f5f1eb]">
      <div class="product-hero-glow product-hero-glow-one"></div>
      <div class="product-hero-glow product-hero-glow-two"></div>
      <div class="relative mx-auto max-w-[1480px] px-4 py-8 md:px-8 md:py-12">
        <div class="flex flex-col gap-7 lg:flex-row lg:items-end lg:justify-between">
          <div class="max-w-3xl">
            <div
              class="mb-4 inline-flex items-center gap-2 rounded-full border border-[#d9cbbd] bg-white/75 px-3 py-1.5 text-[10px] font-black uppercase tracking-[0.18em] text-[#725c49] shadow-sm"
            >
              <Sparkles :size="14" /> Bộ sưu tập thời trang
            </div>
            <p class="mb-2 text-[11px] font-bold uppercase tracking-[0.22em] text-[#a37b50]">
              Khám phá sản phẩm
            </p>
            <h1
              class="text-4xl font-black leading-[1.02] tracking-[-0.04em] text-[#241f1a] md:text-6xl"
            >
              Chọn món đồ<br class="hidden md:block" />
              hợp với bạn.
            </h1>
            <p class="mt-4 max-w-2xl text-sm leading-6 text-[#746a61] md:text-[15px]">
              Tìm kiếm, lọc và so sánh sản phẩm theo nhu cầu. Giá, ưu đãi và tình trạng hàng được
              hiển thị ngay trên từng sản phẩm.
            </p>
          </div>
          <div class="grid grid-cols-2 gap-2 sm:grid-cols-4 lg:w-[520px]">
            <div class="hero-stat">
              <PackageOpen :size="18" /><strong>{{ stats.products }}</strong
              ><span>Sản phẩm</span>
            </div>
            <div class="hero-stat">
              <BadgeCheck :size="18" /><strong>{{ stats.brands }}</strong
              ><span>Thương hiệu</span>
            </div>
            <div class="hero-stat">
              <BadgePercent :size="18" /><strong>{{ stats.onSale }}</strong
              ><span>Đang ưu đãi</span>
            </div>
            <div class="hero-stat">
              <Boxes :size="18" /><strong>{{ stats.inStock }}</strong
              ><span>Còn hàng</span>
            </div>
          </div>
        </div>
        <div
          class="mt-7 max-w-3xl rounded-2xl border border-[#ded3c8] bg-white p-1.5 shadow-[0_18px_50px_rgba(69,52,36,.08)]"
        >
          <div class="flex items-center rounded-xl bg-[#faf8f5] px-4">
            <Search :size="19" class="shrink-0 text-[#a37b50]" />
            <input
              v-model="filters.keyword"
              type="search"
              placeholder="Tìm tên sản phẩm, thương hiệu hoặc mã..."
              class="min-w-0 flex-1 bg-transparent px-3 py-3 text-sm font-semibold text-[#241f1a] outline-none placeholder:text-[#aaa096]"
            />
            <button
              v-if="filters.keyword"
              type="button"
              class="rounded-lg p-2 text-[#938a82] hover:bg-white"
              aria-label="Xóa từ khóa"
              @click="filters.keyword = ''"
            >
              <X :size="16" />
            </button>
          </div>
        </div>
      </div>
    </section>

    <main class="mx-auto max-w-[1480px] px-4 py-8 md:px-8 md:py-12">
      <!-- DANH MỤC NHANH -->
      <section
        class="mb-5 overflow-hidden rounded-[24px] border border-slate-200 bg-white p-3 shadow-sm md:p-4"
      >
        <div class="flex items-center gap-2.5 overflow-x-auto pb-1 hide-scrollbar">
          <button
            type="button"
            class="inline-flex shrink-0 items-center gap-2 rounded-xl px-3.5 py-2.5 text-[13px] font-black transition"
            :class="
              filters.categories.length === 0
                ? 'bg-slate-950 text-white shadow-lg shadow-slate-300'
                : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
            "
            @click="filters.categories.splice(0)"
          >
            <LayoutGrid :size="17" /> Tất cả
          </button>
          <button
            v-for="(category, index) in dynamicFilters[0]?.options || []"
            :key="category.id"
            type="button"
            class="inline-flex shrink-0 items-center gap-2 rounded-xl border px-3.5 py-2.5 text-[13px] font-black transition"
            :class="
              filters.categories.includes(category.name)
                ? quickCategoryActiveClasses[index % quickCategoryActiveClasses.length]
                : 'border-slate-200 bg-white text-slate-600 hover:-translate-y-0.5 hover:border-indigo-200 hover:text-indigo-700'
            "
            @click="toggleQuickCategory(category.name)"
          >
            <span
              class="h-2.5 w-2.5 rounded-full"
              :class="quickCategoryDots[index % quickCategoryDots.length]"
            ></span>
            {{ category.name }}
          </button>
        </div>
      </section>

      <!-- TOOLBAR -->
      <section
        class="sticky top-2 z-30 mb-5 rounded-[24px] border border-slate-200/80 bg-white/90 p-2.5 shadow-lg shadow-slate-200/40 backdrop-blur-xl md:p-3"
      >
        <div class="flex flex-col gap-3 xl:flex-row xl:items-center xl:justify-between">
          <div class="flex items-center gap-3">
            <button
              type="button"
              class="inline-flex items-center gap-2 rounded-xl border border-slate-200 bg-white px-3.5 py-2.5 text-[13px] font-black transition hover:border-indigo-300 hover:text-indigo-700 lg:hidden"
              @click="mobileFiltersOpen = true"
            >
              <SlidersHorizontal :size="18" /> Bộ lọc
              <span
                v-if="activeFilterChips.length"
                class="grid h-5 min-w-5 place-items-center rounded-full bg-indigo-600 px-1 text-[10px] text-white"
              >
                {{ activeFilterChips.length }}
              </span>
            </button>

            <div>
              <p class="text-[11px] font-bold uppercase tracking-[0.12em] text-slate-400">
                Kết quả
              </p>
              <p class="mt-0.5 text-[15px] font-black text-slate-950">
                {{ filteredProducts.length }} sản phẩm phù hợp
              </p>
            </div>
          </div>

          <div class="flex flex-wrap items-center gap-2.5">
            <label
              class="inline-flex cursor-pointer items-center gap-2 rounded-xl bg-emerald-50 px-3 py-2 text-[11px] font-black text-emerald-700"
            >
              <input
                v-model="filters.inStockOnly"
                type="checkbox"
                class="h-4 w-4 rounded accent-emerald-600"
              />
              Còn hàng
            </label>
            <label
              class="inline-flex cursor-pointer items-center gap-2 rounded-xl bg-rose-50 px-3 py-2 text-[11px] font-black text-rose-700"
            >
              <input
                v-model="filters.onSaleOnly"
                type="checkbox"
                class="h-4 w-4 rounded accent-rose-600"
              />
              Có giảm giá
            </label>

            <select
              v-model="sortBy"
              class="min-w-[170px] flex-1 rounded-xl border border-slate-200 bg-slate-50 px-3.5 py-2.5 text-[13px] font-black outline-none transition focus:border-indigo-500 sm:flex-none"
            >
              <option value="newest">Mới nhất</option>
              <option value="discount-desc">Giảm giá nhiều nhất</option>
              <option value="price-asc">Giá thấp → cao</option>
              <option value="price-desc">Giá cao → thấp</option>
              <option value="name-asc">Tên A → Z</option>
              <option value="stock-desc">Tồn kho nhiều nhất</option>
            </select>

            <div class="hidden rounded-xl border border-slate-200 bg-slate-50 p-1 md:flex">
              <button
                type="button"
                class="rounded-lg p-2 transition"
                :class="
                  gridColumns === 3
                    ? 'bg-white text-indigo-600 shadow-sm'
                    : 'text-slate-400 hover:text-slate-900'
                "
                aria-label="Hiển thị 3 cột"
                @click="gridColumns = 3"
              >
                <Columns3 :size="18" />
              </button>
              <button
                type="button"
                class="rounded-lg p-2 transition"
                :class="
                  gridColumns === 4
                    ? 'bg-white text-indigo-600 shadow-sm'
                    : 'text-slate-400 hover:text-slate-900'
                "
                aria-label="Hiển thị 4 cột"
                @click="gridColumns = 4"
              >
                <Grid2X2 :size="18" />
              </button>
            </div>
          </div>
        </div>
      </section>

      <div v-if="activeFilterChips.length" class="mb-5 flex flex-wrap items-center gap-2">
        <span class="mr-1 text-[11px] font-black uppercase tracking-wide text-slate-400"
          >Đang lọc</span
        >
        <button
          v-for="chip in activeFilterChips"
          :key="`${chip.key}-${chip.value}`"
          type="button"
          class="inline-flex items-center gap-1.5 rounded-full border border-indigo-100 bg-indigo-50 px-3 py-1.5 text-[11px] font-bold text-indigo-700 transition hover:bg-indigo-100"
          @click="removeFilter(chip)"
        >
          {{ chip.label }} <X :size="13" />
        </button>
        <button
          type="button"
          class="ml-1 text-xs font-black text-rose-600 hover:underline"
          @click="clearFilters"
        >
          Xóa tất cả
        </button>
      </div>

      <div class="grid gap-5 lg:grid-cols-[248px_minmax(0,1fr)]">
        <!-- SIDEBAR -->
        <aside class="hidden lg:block">
          <div
            class="sticky top-24 overflow-hidden rounded-[24px] border border-slate-200 bg-white shadow-sm"
          >
            <div class="flex items-center justify-between border-b border-slate-100 px-5 py-4">
              <div class="flex items-center gap-2 font-black">
                <SlidersHorizontal :size="18" class="text-indigo-600" /> Bộ lọc thông minh
              </div>
              <button
                v-if="hasActiveFilters"
                type="button"
                class="text-xs font-black text-rose-600 hover:underline"
                @click="clearFilters"
              >
                Đặt lại
              </button>
            </div>

            <div class="border-b border-slate-100 p-4">
              <p class="mb-3 text-[11px] font-black uppercase tracking-[0.14em] text-slate-400">
                Khoảng giá
              </p>
              <div class="space-y-1.5">
                <label
                  v-for="price in priceRanges"
                  :key="price.value"
                  class="flex cursor-pointer items-center gap-3 rounded-lg px-2.5 py-2 text-[13px] font-semibold transition hover:bg-slate-50"
                  :class="
                    filters.priceRange === price.value
                      ? 'bg-indigo-50 text-indigo-700'
                      : 'text-slate-600'
                  "
                >
                  <input
                    v-model="filters.priceRange"
                    type="radio"
                    :value="price.value"
                    class="accent-indigo-600"
                  />
                  {{ price.label }}
                </label>
              </div>
            </div>

            <div class="divide-y divide-slate-100">
              <details v-for="group in dynamicFilters" :key="group.key" class="group" open>
                <summary
                  class="flex cursor-pointer list-none items-center justify-between px-5 py-4"
                >
                  <span class="text-[11px] font-black uppercase tracking-[0.14em] text-slate-500">{{
                    group.label
                  }}</span>
                  <ChevronDown :size="16" class="text-slate-400 transition group-open:rotate-180" />
                </summary>
                <div class="max-h-52 space-y-1 overflow-y-auto px-3 pb-4">
                  <label
                    v-for="option in group.options"
                    :key="option.id"
                    class="flex cursor-pointer items-center justify-between rounded-lg px-2.5 py-2 transition hover:bg-slate-50"
                  >
                    <span class="flex items-center gap-3 text-[13px] font-semibold text-slate-600">
                      <input
                        v-model="filters[group.key]"
                        type="checkbox"
                        :value="option.name"
                        class="h-4 w-4 rounded accent-indigo-600"
                      />
                      {{ option.name }}
                    </span>
                    <ChevronRight :size="14" class="text-slate-300" />
                  </label>
                </div>
              </details>
            </div>
          </div>
        </aside>

        <!-- PRODUCT AREA -->
        <section class="min-w-0">
          <div
            v-if="loading"
            class="grid grid-cols-2 gap-3 md:grid-cols-3"
            :class="gridColumns === 4 ? 'xl:grid-cols-4' : 'xl:grid-cols-3'"
          >
            <div
              v-for="index in 8"
              :key="index"
              class="overflow-hidden rounded-3xl border border-slate-200 bg-white p-3"
            >
              <div class="aspect-[4/5] animate-pulse rounded-2xl bg-slate-200"></div>
              <div class="mt-4 h-3 w-2/5 animate-pulse rounded bg-slate-200"></div>
              <div class="mt-3 h-5 w-4/5 animate-pulse rounded bg-slate-200"></div>
              <div class="mt-4 h-8 w-3/5 animate-pulse rounded bg-slate-200"></div>
            </div>
          </div>

          <div
            v-else-if="errorMessage"
            class="rounded-3xl border border-rose-200 bg-white px-6 py-16 text-center shadow-sm"
          >
            <CircleAlert class="mx-auto text-rose-500" :size="44" />
            <h2 class="mt-4 text-xl font-black">Không tải được sản phẩm</h2>
            <p class="mx-auto mt-2 max-w-lg text-sm leading-6 text-slate-500">{{ errorMessage }}</p>
            <button
              type="button"
              class="mt-6 inline-flex items-center gap-2 rounded-2xl bg-slate-950 px-5 py-3 text-sm font-black text-white transition hover:bg-indigo-600"
              @click="reload"
            >
              <RefreshCw :size="17" /> Thử lại
            </button>
          </div>

          <div
            v-else-if="visibleProducts.length === 0"
            class="rounded-3xl border border-dashed border-slate-300 bg-white px-6 py-20 text-center"
          >
            <SearchX class="mx-auto text-slate-300" :size="50" />
            <h2 class="mt-4 text-xl font-black">Chưa tìm thấy sản phẩm phù hợp</h2>
            <p class="mt-2 text-sm text-slate-500">
              Hãy thử bỏ bớt bộ lọc hoặc thay đổi từ khóa tìm kiếm.
            </p>
            <button
              type="button"
              class="mt-6 rounded-2xl bg-slate-950 px-5 py-3 text-sm font-black text-white"
              @click="clearFilters"
            >
              Xem toàn bộ sản phẩm
            </button>
          </div>

          <div
            v-else
            class="grid grid-cols-2 gap-3 md:grid-cols-3 md:gap-4"
            :class="gridColumns === 4 ? 'xl:grid-cols-4' : 'xl:grid-cols-3'"
          >
            <article
              v-for="product in visibleProducts"
              :key="product.id"
              class="product-card group relative flex h-full min-w-0 cursor-pointer flex-col overflow-hidden rounded-[22px] border border-slate-200 bg-white p-2 shadow-sm transition duration-300 hover:-translate-y-1 hover:border-indigo-200 hover:shadow-xl hover:shadow-indigo-100/60"
              tabindex="0"
              @click="goToDetail(product.id)"
              @keydown.enter="goToDetail(product.id)"
            >
              <div
                class="relative aspect-[4/5] overflow-hidden rounded-[18px] bg-gradient-to-br from-indigo-50 via-slate-50 to-rose-50"
              >
                <img
                  :src="imageUrl(product)"
                  :alt="product.tenSanPham"
                  loading="lazy"
                  class="h-full w-full object-cover transition duration-700 group-hover:scale-[1.06]"
                  @error="handleImageError"
                />
                <div class="absolute inset-x-0 top-0 flex items-start justify-between gap-2 p-2.5">
                  <div class="flex flex-col items-start gap-2">
                    <span
                      v-if="product.dangGiamGia && product.maxDiscountPercent > 0"
                      class="inline-flex items-center gap-1 rounded-full bg-rose-500 px-2.5 py-1 text-[10px] font-black text-white shadow-lg shadow-rose-500/30"
                    >
                      <Zap :size="12" fill="currentColor" /> GIẢM ĐẾN
                      {{ product.maxDiscountPercent }}%
                    </span>
                    <span
                      v-if="isNewProduct(product)"
                      class="rounded-full bg-amber-300 px-2.5 py-1 text-[10px] font-black text-slate-950 shadow"
                    >
                      NEW
                    </span>
                  </div>

                  <button
                    type="button"
                    class="grid h-9 w-9 place-items-center rounded-full border border-white/70 bg-white/90 text-slate-600 shadow-lg backdrop-blur transition hover:scale-105 hover:text-rose-500"
                    :class="isFavorite(product.id) ? 'text-rose-500' : ''"
                    :aria-label="isFavorite(product.id) ? 'Bỏ yêu thích' : 'Thêm yêu thích'"
                    @click.stop="toggleFavorite(product.id)"
                  >
                    <Heart :size="18" :fill="isFavorite(product.id) ? 'currentColor' : 'none'" />
                  </button>
                </div>

                <div
                  class="absolute inset-x-2.5 bottom-2.5 flex items-center justify-between gap-2"
                >
                  <span
                    class="rounded-full px-3 py-1 text-[10px] font-black shadow-lg backdrop-blur"
                    :class="
                      Number(product.soLuong || 0) > 0
                        ? 'bg-emerald-500/90 text-white'
                        : 'bg-slate-950/85 text-white'
                    "
                  >
                    {{ Number(product.soLuong || 0) > 0 ? `Còn ${product.soLuong}` : 'Hết hàng' }}
                  </span>
                  <button
                    type="button"
                    class="translate-y-3 rounded-full bg-white px-3 py-1.5 text-[10px] font-black text-slate-950 opacity-0 shadow-xl transition duration-300 group-hover:translate-y-0 group-hover:opacity-100"
                    @click.stop="goToDetail(product.id)"
                  >
                    Xem nhanh
                  </button>
                </div>
              </div>

              <div class="flex flex-1 flex-col px-1.5 pb-1.5 pt-3">
                <div class="flex min-h-[20px] items-center justify-between gap-2">
                  <span
                    class="truncate text-[11px] font-black uppercase tracking-[0.12em] text-indigo-600"
                  >
                    {{ product.idThuongHieu?.tenThuongHieu || 'HD Fashion' }}
                  </span>
                  <span class="shrink-0 text-[10px] font-bold text-slate-400">{{
                    product.maSanPham
                  }}</span>
                </div>

                <h3
                  class="mt-1.5 min-h-[40px] line-clamp-2 text-[15px] font-black leading-5 text-slate-950 transition group-hover:text-indigo-700"
                >
                  {{ product.tenSanPham }}
                </h3>

                <p class="mt-1 min-h-[40px] line-clamp-2 text-[12px] leading-5 text-slate-500">
                  {{
                    product.moTa ||
                    `${product.idDanhMuc?.tenDanhMuc || 'Thời trang'} · ${product.idChatLieu?.tenChatLieu || 'Chất liệu chọn lọc'}`
                  }}
                </p>

                <div class="mt-2 min-h-[28px] flex flex-wrap content-start gap-1.5">
                  <span
                    class="rounded-md bg-indigo-50 px-2 py-1 text-[10px] font-black text-indigo-700"
                  >
                    {{ product.idDanhMuc?.tenDanhMuc || 'Sản phẩm' }}
                  </span>
                  <span
                    v-if="product.colorCount"
                    class="rounded-md bg-fuchsia-50 px-2 py-1 text-[10px] font-black text-fuchsia-700"
                  >
                    {{ product.colorCount }} màu
                  </span>
                  <span
                    v-if="product.sizeCount"
                    class="rounded-md bg-cyan-50 px-2 py-1 text-[10px] font-black text-cyan-700"
                  >
                    {{ product.sizeCount }} size
                  </span>
                </div>

                <div class="mt-auto border-t border-slate-100 pt-2.5">
                  <div class="flex items-end justify-between gap-2">
                    <div class="min-h-[48px] min-w-0 flex-1">
                      <div class="truncate text-[18px] font-black tracking-tight text-rose-600">
                        {{ priceLabel(product) }}
                      </div>

                      <div
                        v-if="
                          product.displayDiscountPercent > 0 &&
                          product.originalPrice > product.displayPrice
                        "
                        class="mt-0.5 flex min-h-[18px] items-center gap-2"
                      >
                        <span
                          class="truncate text-[12px] font-semibold text-slate-400 line-through"
                        >
                          {{ formatPrice(product.originalPrice) }}
                        </span>
                        <span class="shrink-0 text-[11px] font-black text-emerald-600">
                          -{{ product.displayDiscountPercent }}%
                        </span>
                      </div>

                      <div
                        v-else-if="product.dangGiamGia"
                        class="mt-0.5 min-h-[18px] truncate text-[11px] font-black text-rose-500"
                        :title="`Một số lựa chọn đang giảm đến ${product.maxDiscountPercent}%`"
                      >
                        Một số lựa chọn đang giảm đến {{ product.maxDiscountPercent }}%
                      </div>

                      <div v-else class="min-h-[18px]"></div>
                    </div>

                    <div
                      class="grid h-9 w-9 shrink-0 place-items-center rounded-xl bg-slate-950 text-white transition group-hover:rotate-[-4deg] group-hover:bg-indigo-600"
                    >
                      <ArrowUpRight :size="18" />
                    </div>
                  </div>
                </div>
              </div>
            </article>
          </div>

          <!-- PAGINATION -->
          <nav
            v-if="!loading && !errorMessage && filteredProducts.length > 0 && totalPages > 1"
            class="mt-8 flex flex-wrap items-center justify-center gap-2"
          >
            <button
              type="button"
              class="grid h-10 w-10 place-items-center rounded-2xl border border-slate-200 bg-white text-slate-500 transition hover:border-indigo-300 hover:text-indigo-700 disabled:cursor-not-allowed disabled:opacity-40"
              :disabled="currentPage === 1"
              @click="currentPage--"
            >
              <ChevronLeft :size="18" />
            </button>
            <button
              v-for="page in pageNumbers"
              :key="page"
              type="button"
              class="grid h-10 min-w-10 place-items-center rounded-2xl px-3 text-sm font-black transition"
              :class="
                currentPage === page
                  ? 'bg-slate-950 text-white shadow-lg shadow-slate-300'
                  : 'border border-slate-200 bg-white text-slate-600 hover:border-indigo-300 hover:text-indigo-700'
              "
              @click="currentPage = page"
            >
              {{ page }}
            </button>
            <button
              type="button"
              class="grid h-10 w-10 place-items-center rounded-2xl border border-slate-200 bg-white text-slate-500 transition hover:border-indigo-300 hover:text-indigo-700 disabled:cursor-not-allowed disabled:opacity-40"
              :disabled="currentPage === totalPages"
              @click="currentPage++"
            >
              <ChevronRight :size="18" />
            </button>
          </nav>
        </section>
      </div>
    </main>

    <!-- MOBILE FILTER DRAWER -->
    <Teleport to="body">
      <Transition name="drawer">
        <div
          v-if="mobileFiltersOpen"
          class="fixed inset-0 z-[100] bg-slate-950/55 backdrop-blur-sm"
          @click.self="mobileFiltersOpen = false"
        >
          <aside class="ml-auto flex h-full w-[min(90vw,360px)] flex-col bg-white shadow-2xl">
            <div class="flex items-center justify-between border-b border-slate-100 px-4 py-4">
              <div>
                <p class="text-xs font-black uppercase tracking-[0.16em] text-indigo-600">
                  Tùy chỉnh kết quả
                </p>
                <h2 class="mt-1 text-lg font-black">Bộ lọc sản phẩm</h2>
              </div>
              <button
                type="button"
                class="rounded-xl bg-slate-100 p-2.5"
                @click="mobileFiltersOpen = false"
              >
                <X :size="20" />
              </button>
            </div>

            <div class="flex-1 overflow-y-auto p-4">
              <div class="mb-5">
                <p class="mb-2.5 text-[11px] font-black uppercase tracking-[0.14em] text-slate-400">
                  Khoảng giá
                </p>
                <div class="grid grid-cols-2 gap-2">
                  <label
                    v-for="price in priceRanges"
                    :key="price.value"
                    class="cursor-pointer rounded-lg border px-2.5 py-2.5 text-[11px] font-bold transition"
                    :class="
                      filters.priceRange === price.value
                        ? 'border-indigo-500 bg-indigo-50 text-indigo-700'
                        : 'border-slate-200 text-slate-600'
                    "
                  >
                    <input
                      v-model="filters.priceRange"
                      type="radio"
                      :value="price.value"
                      class="mr-2 accent-indigo-600"
                    />
                    {{ price.shortLabel || price.label }}
                  </label>
                </div>
              </div>

              <div
                v-for="group in dynamicFilters"
                :key="group.key"
                class="border-t border-slate-100 py-4"
              >
                <p class="mb-2.5 text-[11px] font-black uppercase tracking-[0.14em] text-slate-400">
                  {{ group.label }}
                </p>
                <div class="grid grid-cols-2 gap-2">
                  <label
                    v-for="option in group.options"
                    :key="option.id"
                    class="flex cursor-pointer items-center gap-2 rounded-lg border border-slate-200 px-2.5 py-2.5 text-[11px] font-bold text-slate-600"
                  >
                    <input
                      v-model="filters[group.key]"
                      type="checkbox"
                      :value="option.name"
                      class="accent-indigo-600"
                    />
                    <span class="truncate">{{ option.name }}</span>
                  </label>
                </div>
              </div>
            </div>

            <div class="grid grid-cols-2 gap-3 border-t border-slate-100 p-4">
              <button
                type="button"
                class="rounded-xl border border-slate-200 px-4 py-2.5 text-[13px] font-black"
                @click="clearFilters"
              >
                Đặt lại
              </button>
              <button
                type="button"
                class="rounded-xl bg-slate-950 px-4 py-2.5 text-[13px] font-black text-white"
                @click="mobileFiltersOpen = false"
              >
                Xem {{ filteredProducts.length }} sản phẩm
              </button>
            </div>
          </aside>
        </div>
      </Transition>
    </Teleport>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router'
import { onMounted, watch } from 'vue'
import {
  ArrowUpRight,
  BadgeCheck,
  BadgePercent,
  Boxes,
  ChevronDown,
  ChevronLeft,
  ChevronRight,
  CircleAlert,
  Columns3,
  Grid2X2,
  Heart,
  LayoutGrid,
  PackageOpen,
  RefreshCw,
  Search,
  SearchX,
  SlidersHorizontal,
  Sparkles,
  X,
  Zap,
} from 'lucide-vue-next'
import { useProductCatalog } from '@/composables/useProductCatalog'

const {
  filters,
  dynamicFilters,
  visibleProducts,
  filteredProducts,
  loading,
  errorMessage,
  hasActiveFilters,
  activeFilterChips,
  sortBy,
  currentPage,
  totalPages,
  pageNumbers,
  stats,
  mobileFiltersOpen,
  gridColumns,
  clearFilters,
  removeFilter,
  goToDetail,
  imageUrl,
  handleImageError,
  isNewProduct,
  formatPrice,
  priceLabel,
  reload,
} = useProductCatalog({ pageSize: 12 })

const route = useRoute()

// Hàm tự động đồng bộ query brand từ URL vào bộ lọc
const syncBrandFromQuery = () => {
  const brandQuery = route.query.brand
  if (brandQuery) {
    // Đảm bảo filters.brands tồn tại và chưa chứa thương hiệu này
    if (filters.brands && !filters.brands.includes(brandQuery)) {
      filters.brands.push(brandQuery)
    }
  }
}

onMounted(() => {
  syncBrandFromQuery()
})

// Theo dõi nếu người dùng tiếp tục bấm các breadcrumb thương hiệu khác mà không reload lại trang hoàn toàn
watch(
  () => route.query.brand,
  (newBrand) => {
    if (newBrand && filters.brands) {
      if (!filters.brands.includes(newBrand)) {
        filters.brands.push(newBrand)
      }
    }
  },
)
const priceRanges = [
  { value: 'all', label: 'Tất cả mức giá', shortLabel: 'Tất cả' },
  { value: 'under300', label: 'Dưới 300.000đ', shortLabel: '< 300K' },
  { value: 'from300to500', label: '300.000đ – 500.000đ', shortLabel: '300K – 500K' },
  { value: 'from500to1000', label: '500.000đ – 1.000.000đ', shortLabel: '500K – 1 triệu' },
  { value: 'over1000', label: 'Trên 1.000.000đ', shortLabel: '> 1 triệu' },
]

const quickCategoryActiveClasses = [
  'border-indigo-500 bg-indigo-600 text-white shadow-lg shadow-indigo-200',
  'border-fuchsia-500 bg-fuchsia-600 text-white shadow-lg shadow-fuchsia-200',
  'border-cyan-500 bg-cyan-600 text-white shadow-lg shadow-cyan-200',
  'border-rose-500 bg-rose-600 text-white shadow-lg shadow-rose-200',
  'border-amber-500 bg-amber-500 text-slate-950 shadow-lg shadow-amber-200',
]
const quickCategoryDots = [
  'bg-indigo-400',
  'bg-fuchsia-400',
  'bg-cyan-400',
  'bg-rose-400',
  'bg-amber-400',
]

const toggleQuickCategory = (name) => {
  const index = filters.categories.indexOf(name)
  if (index >= 0) filters.categories.splice(index, 1)
  else filters.categories.push(name)
}

const storedFavorites = (() => {
  try {
    const parsed = JSON.parse(localStorage.getItem('favorite-product-ids') || '[]')
    return Array.isArray(parsed) ? parsed.map(Number).filter(Number.isFinite) : []
  } catch {
    return []
  }
})()

const favoriteIds = ref(storedFavorites)
const isFavorite = (id) => favoriteIds.value.includes(Number(id))
const toggleFavorite = (id) => {
  const normalizedId = Number(id)
  const index = favoriteIds.value.indexOf(normalizedId)
  if (index >= 0) favoriteIds.value.splice(index, 1)
  else favoriteIds.value.push(normalizedId)
  localStorage.setItem('favorite-product-ids', JSON.stringify(favoriteIds.value))
}
</script>

<style scoped>
.product-hero {
  isolation: isolate;
}
.product-hero-glow {
  position: absolute;
  border-radius: 999px;
  filter: blur(70px);
  opacity: 0.45;
  pointer-events: none;
}
.product-hero-glow-one {
  width: 360px;
  height: 360px;
  right: -100px;
  top: -150px;
  background: #dfc2a1;
}
.product-hero-glow-two {
  width: 260px;
  height: 260px;
  left: -100px;
  bottom: -160px;
  background: #eaded0;
}
.hero-stat {
  min-height: 92px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  border: 1px solid #e1d6ca;
  border-radius: 18px;
  padding: 13px;
  background: rgba(255, 255, 255, 0.72);
  color: #725c49;
  box-shadow: 0 10px 30px rgba(69, 52, 36, 0.05);
  backdrop-filter: blur(12px);
}
.hero-stat strong {
  margin-top: 7px;
  color: #241f1a;
  font-size: 1.35rem;
  line-height: 1;
  font-weight: 900;
}
.hero-stat span {
  margin-top: 3px;
  color: #8c8177;
  font-size: 0.68rem;
  font-weight: 700;
}
.hide-scrollbar {
  scrollbar-width: none;
}
.hide-scrollbar::-webkit-scrollbar {
  display: none;
}
.product-card {
  border-color: #e8e0d5 !important;
  border-radius: 20px !important;
  box-shadow: 0 8px 30px rgba(69, 52, 36, 0.045) !important;
  transition:
    transform 0.35s cubic-bezier(0.2, 0.8, 0.2, 1),
    box-shadow 0.35s ease,
    border-color 0.35s ease !important;
}
.product-card:hover {
  transform: translateY(-5px) !important;
  border-color: #cdb79f !important;
  box-shadow: 0 22px 50px rgba(69, 52, 36, 0.12) !important;
}
.product-card > div:first-child {
  border-radius: 16px !important;
  background: #f2eee8 !important;
}
.product-card img {
  transition:
    transform 0.7s cubic-bezier(0.2, 0.8, 0.2, 1),
    filter 0.5s ease !important;
}
.product-card:hover img {
  transform: scale(1.045) !important;
}
.product-card h3 {
  color: #2b241e !important;
  letter-spacing: -0.02em;
}
.product-card:hover h3 {
  color: #8a6240 !important;
}
.product-card .text-rose-600 {
  color: #9a5b38 !important;
}
.product-card .bg-indigo-600,
.product-card .bg-indigo-50 {
}
.line-clamp-2 {
  display: -webkit-box;
  overflow: hidden;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
}
summary::-webkit-details-marker {
  display: none;
}
.drawer-enter-active,
.drawer-leave-active {
  transition: opacity 0.25s ease;
}
.drawer-enter-active aside,
.drawer-leave-active aside {
  transition: transform 0.25s ease;
}
.drawer-enter-from,
.drawer-leave-to {
  opacity: 0;
}
.drawer-enter-from aside,
.drawer-leave-to aside {
  transform: translateX(100%);
}
@media (max-width: 640px) {
  .hero-stat {
    min-height: 82px;
    padding: 11px;
  }
  .hero-stat strong {
    font-size: 1.15rem;
  }
}
</style>
