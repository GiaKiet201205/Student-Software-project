package com.sgu.admission.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NguyenVongDTO {
    private Integer idNv;
    private String maNganh;
    private String tenNganh;
    private Integer thuTu;
    private String phuongThuc;  // 3=THPT, 4=ĐGNL, 5=VSAT
    private String maToHop;
    private BigDecimal diemXetTuyen;
    private String ketQua;  // Đạt/Không
    private String ttThm;
}
