package com.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(
        name = "xt_diemthixettuyen",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_cccd_phuongthuc",
                columnNames = {"cccd", "d_phuongthuc"}
        )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiemThiXetTuyen {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "iddiemthi")
  private Integer idDiemThi;

  /**
   * Số CCCD/CMND của thí sinh.
   * KHÔNG unique đơn lẻ — unique kết hợp với phuongThuc.
   */
  @Column(name = "cccd", nullable = false, length = 20)
  private String cccd;

  /**
   * Số báo danh (SBD).
   * Đối với V-SAT (phuongThuc = "5"), có thể dùng để lưu Mã đợt thi (VD: CTU255)
   */
  @Column(name = "sobaodanh", length = 45)
  private String soBaoDanh;

  /**
   * Mã phương thức xét tuyển: "3" (THPT), "4" (ĐGNL), "5" (V-SAT)
   */
  @Column(name = "d_phuongthuc", length = 10)
  private String phuongThuc;

  // =========================================================
  // NHÓM 1: Điểm thi các môn văn hóa (Dùng cho THPT và V-SAT)
  // =========================================================

  @Column(name = "`TO`", precision = 8, scale = 2)
  private BigDecimal toan;

  @Column(name = "LI", precision = 8, scale = 2)
  private BigDecimal ly;

  @Column(name = "HO", precision = 8, scale = 2)
  private BigDecimal hoa;

  @Column(name = "SI", precision = 8, scale = 2)
  private BigDecimal sinh;

  @Column(name = "SU", precision = 8, scale = 2)
  private BigDecimal su;

  @Column(name = "DI", precision = 8, scale = 2)
  private BigDecimal dia;

  @Column(name = "VA", precision = 8, scale = 2)
  private BigDecimal van;

  /**
   * Điểm Ngoại ngữ thi gốc (Thang 10 hoặc thang 150 tùy phương thức).
   */
  @Column(name = "N1_THI", precision = 8, scale = 2)
  private BigDecimal n1Thi;

  /**
   * Điểm Ngoại ngữ chính thức = max(N1_THI, điểm quy đổi chứng chỉ NN).
   * Luôn dùng cột này để tính điểm xét tuyển tổ hợp có môn Tiếng Anh.
   */
  @Column(name = "N1_CC", precision = 8, scale = 2)
  private BigDecimal n1CC;

  @Column(name = "CNCN", precision = 8, scale = 2)
  private BigDecimal cncn;

  @Column(name = "CNNN", precision = 8, scale = 2)
  private BigDecimal cnnn;

  @Column(name = "TI", precision = 8, scale = 2)
  private BigDecimal tinHoc;

  @Column(name = "KTPL", precision = 8, scale = 2)
  private BigDecimal ktpl;

  // =========================================================
  // NHÓM 2: Điểm tổng ĐGNL (HSA)
  // =========================================================

  /**
   * Điểm tổng bài thi ĐGNL (HSA). Thang điểm 1200.
   */
  @Column(name = "NL1", precision = 8, scale = 2)
  private BigDecimal diemNangLuc;

  // =========================================================
  // NHÓM 3: Điểm thi Năng khiếu chuyên biệt
  // =========================================================

  /**
   * Điểm thi Năng khiếu 1 (VD: Vẽ Mỹ thuật)
   */
  @Column(name = "NK1", precision = 8, scale = 2)
  private BigDecimal diemNangKhieu1;

  /**
   * Điểm thi Năng khiếu 2 (VD: Vẽ Trang trí màu)
   */
  @Column(name = "NK2", precision = 8, scale = 2)
  private BigDecimal diemNangKhieu2;

  /**
   * Điểm thi Năng khiếu 3 (VD: Hát / Đọc diễn cảm)
   */
  @Column(name = "NK3", precision = 8, scale = 2)
  private BigDecimal diemNangKhieu3;

  /**
   * Điểm thi Năng khiếu 4 (VD: Thể dục thể thao)
   */
  @Column(name = "NK4", precision = 8, scale = 2)
  private BigDecimal diemNangKhieu4;

  // =========================================================
  // PHƯƠNG THỨC TIỆN ÍCH (BUSINESS LOGIC)
  // =========================================================

  public boolean isThpt() {
    return "3".equals(phuongThuc);
  }

  public boolean isDgnl() {
    return "4".equals(phuongThuc);
  }

  public boolean isVsat() {
    return "5".equals(phuongThuc);
  }

  /**
   * Lấy điểm môn theo mã môn (dùng trong tính điểm tổ hợp gốc).
   * Mã môn khớp với cột trong xt_nganh_tohop: TO, LI, HO, SI, SU, DI, VA,
   * N1, CNCN, CNNN, TI, KTPL, NL1, NK1, NK2, NK3, NK4.
   *
   * @param maMon mã môn viết hoa, ví dụ "TO", "VA", "N1", "NK1"
   * @return điểm tương ứng, hoặc BigDecimal.ZERO nếu không có
   */
  public BigDecimal getDiemByMaMon(String maMon) {
    if (maMon == null) return BigDecimal.ZERO;
    
    // lấy điểm cao nhất giữa N1_THI và N1_CC
    if ("N1".equalsIgnoreCase(maMon) || "NN".equalsIgnoreCase(maMon)) {
        BigDecimal n1Thi = this.n1Thi != null ? this.n1Thi : BigDecimal.ZERO;
        BigDecimal n1CC = this.n1CC != null ? this.n1CC : BigDecimal.ZERO;
        return n1Thi.compareTo(n1CC) > 0 ? n1Thi : n1CC;
    }
    
    return switch (maMon.toUpperCase()) {
        case "TO"   -> toan           != null ? toan           : BigDecimal.ZERO;
        case "LI"   -> ly             != null ? ly             : BigDecimal.ZERO;
        case "HO"   -> hoa            != null ? hoa            : BigDecimal.ZERO;
        case "SI"   -> sinh           != null ? sinh           : BigDecimal.ZERO;
        case "SU"   -> su             != null ? su             : BigDecimal.ZERO;
        case "DI"   -> dia            != null ? dia            : BigDecimal.ZERO;
        case "VA"   -> van            != null ? van            : BigDecimal.ZERO;
        case "CNCN" -> cncn           != null ? cncn           : BigDecimal.ZERO;
        case "CNNN" -> cnnn           != null ? cnnn           : BigDecimal.ZERO;
        case "TI"   -> tinHoc         != null ? tinHoc         : BigDecimal.ZERO;
        case "KTPL" -> ktpl           != null ? ktpl           : BigDecimal.ZERO;
        case "NL1"  -> diemNangLuc    != null ? diemNangLuc    : BigDecimal.ZERO;
        case "NK1"  -> diemNangKhieu1 != null ? diemNangKhieu1 : BigDecimal.ZERO;
        case "NK2"  -> diemNangKhieu2 != null ? diemNangKhieu2 : BigDecimal.ZERO;
        case "NK3"  -> diemNangKhieu3 != null ? diemNangKhieu3 : BigDecimal.ZERO;
        case "NK4"  -> diemNangKhieu4 != null ? diemNangKhieu4 : BigDecimal.ZERO;
        default     -> BigDecimal.ZERO;
    };
  }
}