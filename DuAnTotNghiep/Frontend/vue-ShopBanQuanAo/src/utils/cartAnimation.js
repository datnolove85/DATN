export const flyToCart = (event, imageUrl) => {
  const targetRect = event.target.getBoundingClientRect()
  const cartPosStr = sessionStorage.getItem('cart_icon_pos')
  const cartPos = cartPosStr ? JSON.parse(cartPosStr) : { x: window.innerWidth - 60, y: 40 }

  // Tạo phần tử ảnh bay với phong cách glassmorphism và bóng đổ sang trọng
  const flyer = document.createElement('div')
  flyer.classList.add(
    'fixed',
    'z-[9999]',
    'w-14',
    'h-14',
    'rounded-2xl',
    'overflow-hidden',
    'shadow-[0_10px_25px_-5px_rgba(223,52,64,0.5)]', // Hiệu ứng đổ bóng rực rỡ thu hút ánh nhìn
    'border-2',
    'border-white',
    'bg-white',
    'pointer-events-none',
  )

  // Tọa độ bắt đầu (tại tâm của nút bấm)
  const startX = targetRect.left + targetRect.width / 2 - 28
  const startY = targetRect.top + targetRect.height / 2 - 28

  // Tọa độ kết thúc (tại icon giỏ hàng)
  const endX = cartPos.x - 14
  const endY = cartPos.y - 14

  flyer.style.left = `${startX}px`
  flyer.style.top = `${startY}px`
  flyer.innerHTML = `<img src="${imageUrl}" class="w-full h-full object-cover rounded-xl" />`
  document.body.appendChild(flyer)

  // Tính toán điểm uốn để tạo đường cong Parabol (bay vồng lên trên một khoảng rồi rơi xuống)
  const controlX = startX + (endX - startX) / 2
  const controlY = Math.min(startY, endY) - 130

  const duration = 700 // Thời gian bay (ms)
  let startTime = null

  // Sử dụng requestAnimationFrame để điều khiển quỹ đạo vật lý mượt mà tuyệt đối
  const animate = (currentTime) => {
    if (!startTime) startTime = currentTime
    const elapsed = currentTime - startTime
    let progress = elapsed / duration

    if (progress > 1) progress = 1

    // Hàm gia tốc mượt (Ease-out cubic) giúp khởi động nhanh và dừng êm ái
    const easeProgress = 1 - Math.pow(1 - progress, 3)

    // Công thức toán học Bezier bậc 2 tạo đường cong mềm mại
    const currentX =
      (1 - easeProgress) * (1 - easeProgress) * startX +
      2 * (1 - easeProgress) * easeProgress * controlX +
      easeProgress * easeProgress * endX

    const currentY =
      (1 - easeProgress) * (1 - easeProgress) * startY +
      2 * (1 - easeProgress) * easeProgress * controlY +
      easeProgress * easeProgress * endY

    // Thu nhỏ dần kích thước và xoay nhẹ sản phẩm khi bay
    const currentScale = 1 - easeProgress * 0.85 // Từ 1 xuống còn 0.15
    const currentRotate = easeProgress * 180 // Xoay 180 độ dọc đường bay

    flyer.style.transform = `translate(${currentX - startX}px, ${currentY - startY}px) scale(${currentScale}) rotate(${currentRotate}deg)`
    flyer.style.opacity = `${1 - easeProgress * 0.3}`

    if (progress < 1) {
      requestAnimationFrame(animate)
    } else {
      // Khi bay đến nơi: Xóa ảnh bay và kích hoạt hiệu ứng nảy (pulse) cho icon giỏ hàng ở Navbar
      flyer.remove()

      const cartIconElement = document.getElementById('cart-icon-wrapper')
      if (cartIconElement) {
        cartIconElement.classList.add('scale-125', 'transition-transform', 'duration-200')
        setTimeout(() => {
          cartIconElement.classList.remove('scale-125')
        }, 200)
      }
    }
  }

  requestAnimationFrame(animate)
}
