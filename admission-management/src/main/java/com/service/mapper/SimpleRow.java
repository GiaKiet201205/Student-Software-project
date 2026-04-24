package com.service.mapper;

import java.util.Map;

public class SimpleRow {
    private final Map<Integer, String> data;

    public SimpleRow(Map<Integer, String> data) {
        this.data = data;
    }

    // Lấy giá trị theo index cột (0 = A, 1 = B...), trả về chuỗi rỗng nếu null
    public String get(int colIndex) {
        return data.getOrDefault(colIndex, "").trim();
    }
}
