package com.service.NVXTImport.model;   
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ImportResult {
    private int totalRecords;      // Tổng số bản ghi đọc được
    private int successRecords;    // Số bản ghi lưu thành công
    private int failedRecords;     // Số bản ghi lỗi
    private int skippedNoDiem;     // Bỏ qua vì không có điểm
    private int skippedNoToHop;    // Bỏ qua vì không có tổ hợp
    private long elapsedTime;      // Thời gian xử lý (ms)
    private List<String> errors;   // Danh sách lỗi chi tiết
    
    public ImportResult() {
        this.errors = new ArrayList<>();
    }
    
    public String getSummary() {
        return String.format(
            "Đọc: %d | Thành công: %d | Lỗi: %d | Bỏ qua (không điểm): %d | Bỏ qua (không tổ hợp): %d | Thời gian: %d ms",
            totalRecords, successRecords, failedRecords, skippedNoDiem, skippedNoToHop, elapsedTime
        );
    }
}