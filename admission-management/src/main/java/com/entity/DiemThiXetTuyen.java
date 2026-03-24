package com.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "xt_diemthixettuyen")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiemThiXetTuyen {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "iddiemthi")
  private Integer idDiemThi;

  @Column(name = "cccd", nullable = false, length = 20, unique = true)
  private String cccd;

  @Column(name = "sobaodanh", length = 45)
  private String soBaoDanh;

  @Column(name = "d_phuongthuc", length = 10)
  private String phuongThuc;

  /** Toán */
  @Column(name = "TO", precision = 8, scale = 2)
  private BigDecimal toan;

  /** Lý */
  @Column(name = "LI", precision = 8, scale = 2)
  private BigDecimal ly;

  /** Hóa */
  @Column(name = "HO", precision = 8, scale = 2)
  private BigDecimal hoa;

  /** Sinh */
  @Column(name = "SI", precision = 8, scale = 2)
  private BigDecimal sinh;

  /** Sử */
  @Column(name = "SU", precision = 8, scale = 2)
  private BigDecimal su;

  /** Địa */
  @Column(name = "DI", precision = 8, scale = 2)
  private BigDecimal dia;

  /** Văn */
  @Column(name = "VA", precision = 8, scale = 2)
  private BigDecimal van;

  /** Điểm thi gốc Ngoại ngữ 1 */
  @Column(name = "N1_THI", precision = 8, scale = 2)
  private BigDecimal n1Thi;

  /** max(N1_Thi, N1_QD) */
  @Column(name = "N1_CC", precision = 8, scale = 2)
  private BigDecimal n1CC;

  /** Công nghệ - CN Cơ Nhiệt */
  @Column(name = "CNCN", precision = 8, scale = 2)
  private BigDecimal cncn;

  /** Công nghệ - Nông Nghiệp */
  @Column(name = "CNNN", precision = 8, scale = 2)
  private BigDecimal cnnn;

  /** Tin học */
  @Column(name = "TI", precision = 8, scale = 2)
  private BigDecimal tinHoc;

  /** Kinh tế pháp luật */
  @Column(name = "KTPL", precision = 8, scale = 2)
  private BigDecimal ktpl;

  /** Ngoại ngữ 1 */
  @Column(name = "NL1", precision = 8, scale = 2)
  private BigDecimal nl1;

  /** Ngoại ngữ khác 1 */
  @Column(name = "NK1", precision = 8, scale = 2)
  private BigDecimal nk1;

  /** Ngoại ngữ khác 2 */
  @Column(name = "NK2", precision = 8, scale = 2)
  private BigDecimal nk2;
}
