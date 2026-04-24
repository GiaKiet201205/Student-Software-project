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

  @Column(name = "sobaodanh", length = 45)
  private String soBaoDanh;

  /**
   * Mã phương thức xét tuyển: "0", "3", "4", "5"
   * Xem mô tả lớp để biết ý nghĩa từng mã.
   */
  @Column(name = "d_phuongthuc", length = 10)
  private String phuongThuc;

  // =========================================================
  // NHÓM 1: Điểm thi THPT Quốc gia (dùng khi phuongThuc = "3")
  // =========================================================

  /** Toán */
  @Column(name = "`TO`", precision = 8, scale = 2)
  private BigDecimal toan;

  /** Vật lý */
  @Column(name = "LI", precision = 8, scale = 2)
  private BigDecimal ly;

  /** Hóa học */
  @Column(name = "HO", precision = 8, scale = 2)
  private BigDecimal hoa;

  /** Sinh học */
  @Column(name = "SI", precision = 8, scale = 2)
  private BigDecimal sinh;

  /** Lịch sử */
  @Column(name = "SU", precision = 8, scale = 2)
  private BigDecimal su;

  /** Địa lý */
  @Column(name = "DI", precision = 8, scale = 2)
  private BigDecimal dia;

  /** Ngữ văn */
  @Column(name = "VA", precision = 8, scale = 2)
  private BigDecimal van;

  /**
   * Điểm Ngoại ngữ thi THPT gốc (thang 10).
   * Dùng khi phuongThuc = "3".
   */
  @Column(name = "N1_THI", precision = 8, scale = 2)
  private BigDecimal n1Thi;

  /**
   * Điểm Ngoại ngữ chính thức = max(N1_THI, điểm quy đổi chứng chỉ NN).
   * Được tính sau khi tra bảng quy đổi tiếng Anh (bangQUyDoiTA).
   * Dùng khi phuongThuc = "3".
   */
  @Column(name = "N1_CC", precision = 8, scale = 2)
  private BigDecimal n1CC;

  /**
   * Công nghệ — Cơ khí Nhiệt lạnh (THPT 2025).
   * Dùng khi phuongThuc = "3".
   */
  @Column(name = "CNCN", precision = 8, scale = 2)
  private BigDecimal cncn;

  /**
   * Công nghệ — Nông nghiệp (THPT 2025).
   * Dùng khi phuongThuc = "3".
   */
  @Column(name = "CNNN", precision = 8, scale = 2)
  private BigDecimal cnnn;

  /**
   * Tin học (THPT 2025).
   * Dùng khi phuongThuc = "3".
   */
  @Column(name = "TI", precision = 8, scale = 2)
  private BigDecimal tinHoc;

  /**
   * Kinh tế Pháp luật (THPT 2025).
   * Dùng khi phuongThuc = "3".
   */
  @Column(name = "KTPL", precision = 8, scale = 2)
  private BigDecimal ktpl;

  // =========================================================
  // NHÓM 2: Điểm ĐGNL / V-SAT (dùng khi phuongThuc = "4" hoặc "5")
  // Thang điểm: 400 – 1200 (ĐGNL HSA) hoặc tương đương (V-SAT)
  // =========================================================

  /**
   * Điểm tổng bài thi ĐGNL (HSA) hoặc V-SAT.
   * Đây KHÔNG phải điểm ngoại ngữ — là điểm đánh giá năng lực tổng thể.
   */
  @Column(name = "NL1", precision = 8, scale = 2)
  private BigDecimal diemNangLuc;

  /**
   * Điểm thành phần 1 của bài thi ĐGNL / V-SAT
   * (ví dụ: phần Toán - Khoa học tự nhiên).
   */
  @Column(name = "NK1", precision = 8, scale = 2)
  private BigDecimal diemThanhPhan1;

  /**
   * Điểm thành phần 2 của bài thi ĐGNL / V-SAT
   * (ví dụ: phần Ngữ văn - Khoa học xã hội).
   */
  @Column(name = "NK2", precision = 8, scale = 2)
  private BigDecimal diemThanhPhan2;

  // =========================================================
  // PHƯƠNG THỨC TIỆN ÍCH
  // =========================================================

  /**
   * Trả về true nếu đây là dòng xét tuyển THPT Quốc gia.
   */
  public boolean isThpt() {
    return "3".equals(phuongThuc);
  }

  /**
   * Trả về true nếu đây là dòng xét tuyển ĐGNL (HSA).
   */
  public boolean isDgnl() {
    return "4".equals(phuongThuc);
  }

  /**
   * Trả về true nếu đây là dòng xét tuyển V-SAT.
   */
  public boolean isVsat() {
    return "5".equals(phuongThuc);
  }

  /**
   * Lấy điểm môn theo mã môn (dùng trong tính điểm tổ hợp).
   * Mã môn khớp với cột trong xt_nganh_tohop: TO, LI, HO, SI, SU, DI, VA,
   * N1, CNCN, CNNN, TI, KTPL.
   *
   * @param maMon mã môn viết hoa, ví dụ "TO", "VA", "N1"
   * @return điểm tương ứng, hoặc BigDecimal.ZERO nếu không có
   */
  public BigDecimal getDiemByMaMon(String maMon) {
    if (maMon == null) return BigDecimal.ZERO;
    return switch (maMon.toUpperCase()) {
      case "TO"   -> toan      != null ? toan      : BigDecimal.ZERO;
      case "LI"   -> ly        != null ? ly        : BigDecimal.ZERO;
      case "HO"   -> hoa       != null ? hoa       : BigDecimal.ZERO;
      case "SI"   -> sinh      != null ? sinh      : BigDecimal.ZERO;
      case "SU"   -> su        != null ? su        : BigDecimal.ZERO;
      case "DI"   -> dia       != null ? dia       : BigDecimal.ZERO;
      case "VA"   -> van       != null ? van       : BigDecimal.ZERO;
      case "N1"   -> n1CC      != null ? n1CC      : BigDecimal.ZERO; // dùng N1_CC (đã quy đổi)
      case "CNCN" -> cncn      != null ? cncn      : BigDecimal.ZERO;
      case "CNNN" -> cnnn      != null ? cnnn      : BigDecimal.ZERO;
      case "TI"   -> tinHoc    != null ? tinHoc    : BigDecimal.ZERO;
      case "KTPL" -> ktpl      != null ? ktpl      : BigDecimal.ZERO;
      default     -> BigDecimal.ZERO;
    };
  }
}