package com.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "xt_nguyenvongxettuyen")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NguyenVongXetTuyen {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "idnv")
  private Integer idNv;

  @Column(name = "nn_cccd", nullable = false, length = 45)
  private String cccd;

  @Column(name = "nv_manganh", nullable = false, length = 45)
  private String maNganh;

  /** Thứ tự ưu tiên nguyện vọng */
  @Column(name = "nv_tt", nullable = false)
  private Integer thuTu;

  /** Điểm xét tuyển đã cộng điểm môn chính */
  @Column(name = "diem_thxt", precision = 10, scale = 5)
  private BigDecimal diemThXT;

  /** Điểm ưu tiên theo quy định (tổ hợp có thể khác nhau) */
  @Column(name = "diem_utqd", precision = 10, scale = 5)
  private BigDecimal diemUuTienQD;

  /** Tổng 3 môn chưa tính môn chính + điểm ưu tiên */
  @Column(name = "diem_cong", precision = 6, scale = 2)
  private BigDecimal diemCong;

  /** Điểm xét tuyển đã cộng điểm ưu tiên */
  @Column(name = "diem_xettuyen", precision = 10, scale = 5)
  private BigDecimal diemXetTuyen;

  @Column(name = "nv_ketqua", length = 45)
  private String ketQua;

  @Column(name = "nv_keys", length = 45, unique = true)
  private String nvKeys;

  @Column(name = "tt_phuongthuc", length = 45)
  private String phuongThuc;

  @Column(name = "tt_thm", length = 45)
  private String ttThm;

  @Column(name = "nv_rank")
  private Integer thuTuXetTuyen;
}