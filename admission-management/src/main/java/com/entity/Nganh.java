package com.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "xt_nganh")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Nganh {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "idnganh")
  private Integer idNganh;

  @Column(name = "manganh", nullable = false, length = 45)
  private String maNganh;

  @Column(name = "tennganh", nullable = false, length = 100)
  private String tenNganh;

  /** Tổ hợp gốc */
  @Column(name = "n_tohopgoc", length = 3)
  private String toHopGoc;

  @Column(name = "n_chitieu", nullable = false)
  private Integer chiTieu;

  @Column(name = "n_diemsan", precision = 10, scale = 2)
  private BigDecimal diemSan;

  @Column(name = "n_diemtrungtuyen", precision = 10, scale = 2)
  private BigDecimal diemTrungTuyen;

  /** Có tuyển thẳng: Y/N */
  @Column(name = "n_tuyenthang", length = 1)
  private String tuyenThang;

  /** Có xét ĐGNL: Y/N */
  @Column(name = "n_dgnl", length = 1)
  private String dgnl;

  /** Có xét THPT: Y/N */
  @Column(name = "n_thpt", length = 1)
  private String thpt;

  /** Có xét V-SAT: Y/N */
  @Column(name = "n_vsat", length = 1)
  private String vsat;

  /** Số lượng xét tuyển thường */
  @Column(name = "sl_xtt")
  private Integer slXtt;

  /** Số lượng ĐGNL */
  @Column(name = "sl_dgnl")
  private Integer slDgnl;

  /** Số lượng V-SAT */
  @Column(name = "sl_vsat")
  private Integer slVsat;

  /** Số lượng THPT */
  @Column(name = "sl_thpt", length = 45)
  private String slThpt;
}

