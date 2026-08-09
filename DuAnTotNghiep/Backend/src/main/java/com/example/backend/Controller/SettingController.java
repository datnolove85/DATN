package com.example.backend.Controller;


import com.example.backend.Entity.Setting;
import com.example.backend.Repository.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/settings")
@CrossOrigin(origins = "*")
public class SettingController {

    @Autowired
    private SettingRepository settingRepository;

    // Lấy cấu hình theo key
    @GetMapping("/{key}")
    public ResponseEntity<?> getSetting(@PathVariable String key) {
        Setting setting = settingRepository.findById(key)
                .orElse(new Setting(key, "LAP_LAI")); // Giá trị mặc định nếu chưa có
        return ResponseEntity.ok(setting);
    }

    // Lưu hoặc cập nhật cấu hình
    @PostMapping("/{key}")
    public ResponseEntity<?> saveSetting(@PathVariable String key, @RequestBody Setting payload) {
        Setting setting = settingRepository.findById(key).orElse(new Setting());
        setting.setSettingKey(key);
        setting.setSettingValue(payload.getSettingValue());
        Setting saved = settingRepository.save(setting);
        return ResponseEntity.ok(saved);
    }
}