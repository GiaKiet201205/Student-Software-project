package com.sgu.admission.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KetQuaTinhDiemDTO {
    private String phuongThuc;
    private String maNganh;
    private String tenNganh;
    private String maToHop;
    private String tenToHop;
    
    // Điểm thi/nhập
    private BigDecimal diemToan;
    private BigDecimal diemLy;
    private BigDecimal diemHoa;
    private BigDecimal diemSinh;
    private BigDecimal diemVan;
    private BigDecimal diemSu;
    private BigDecimal diemDia;
    private BigDecimal diemN1;
    private BigDecimal diemVsat;
    
    // Điểm quy đổi (nếu có)
    private BigDecimal diemQuyDoiToan;
    private BigDecimal diemQuyDoiLy;
    private BigDecimal diemQuyDoiHoa;
    
    // Kết quả
    private BigDecimal diemTongToHop;
    private BigDecimal diemXetTuyen;
    private BigDecimal diemChuanNganh;
    private String ketQua;  // Đạt/Không
    private String ghiChu;
}
