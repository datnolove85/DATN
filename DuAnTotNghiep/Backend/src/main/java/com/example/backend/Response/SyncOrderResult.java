package com.example.backend.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SyncOrderResult {

    /**
     * true nếu backend đã tự sửa hóa đơn
     */
    private boolean changed = false;

    /**
     * danh sách thông báo gửi lên FE
     */
    private List<String> messages = new ArrayList<>();

    public void addMessage(String message) {
        this.changed = true;
        this.messages.add(message);
    }
}