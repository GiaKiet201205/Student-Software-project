package com.service.NVXTImport.model;
import lombok.Data;

@Data
public class NguyenVongRaw {
    private String cccd;           // CCCD thí sinh
    private String maNganh;        // Mã ngành xét tuyển
    private Integer thuTu;         // Thứ tự nguyện vọng
    private String sheetName;      // Sheet nguồn (để debug)
    private int rowIndex;          // Dòng trong file (để debug)
}