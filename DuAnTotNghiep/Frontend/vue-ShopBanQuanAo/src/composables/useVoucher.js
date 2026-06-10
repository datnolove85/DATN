import { ref, computed } from 'vue'

export function useVoucher(totalCartPrice) {
  const appliedVoucher = ref(null)

  const voucherDiscount = computed(() => {
    if (!appliedVoucher.value) return 0

    if (appliedVoucher.value.loai === 'tien_mat') {
      return appliedVoucher.value.giatri
    }

    if (appliedVoucher.value.loai === 'phan_tram') {
      return (totalCartPrice.value * appliedVoucher.value.giatri) / 100
    }

    return 0
  })

  const removeVoucher = () => {
    appliedVoucher.value = null
  }

  return {
    appliedVoucher,
    voucherDiscount,
    removeVoucher,
  }
}
