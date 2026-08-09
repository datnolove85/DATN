import io
from fastapi import FastAPI, UploadFile, File
from fastapi.responses import Response
from rembg import remove
from PIL import Image

app = FastAPI()

@app.post("/remove-bg")
async def remove_background(file: UploadFile = File(...)):
    input_bytes = await file.read()
    
    # 1. Xóa nền bằng rembg
    output_bytes = remove(input_bytes)
    
    # 2. Mở ảnh bằng Pillow để căn chỉnh
    img = Image.open(io.BytesIO(output_bytes)).convert("RGBA")
    
    # Tự động cắt bỏ phần viền thừa trống rỗng xung quanh vật thể
    bbox = img.getbbox()
    if bbox:
        img = img.crop(bbox)
        
    # 3. Căn chỉnh: Đặt vật thể vào giữa một khung ảnh vuông (ví dụ 500x500)
    target_size = (500, 500)
    canvas = Image.new("RGBA", target_size, (0, 0, 0, 0)) # Khung nền trong suốt
    
    # Resize giữ nguyên tỷ lệ ảnh để không bị bóp méo
    img.thumbnail((450, 450), Image.Resampling.LANCZOS)
    
    # Tính toán vị trí để đặt ảnh vào chính giữa
    x = (target_size[0] - img.width) // 2
    y = (target_size[1] - img.height) // 2
    canvas.paste(img, (x, y), img)
    
    # 4. Xuất ảnh ra định dạng PNG
    img_byte_arr = io.BytesIO()
    canvas.save(img_byte_arr, format='PNG')
    
    return Response(content=img_byte_arr.getvalue(), media_type="image/png")

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="127.0.0.1", port=8000)