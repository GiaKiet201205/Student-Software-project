package com.sgu.admission.entity;

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

  @Column(name = "cccd", nullable = false, length = 20)
  private String cccd;

  @Column(name = "sobaodanh", length = 45)
  private String soBaoDanh;

  @Column(name = "d_phuongthuc", length = 10)
  private String phuongThuc;

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

  @Column(name = "N1_THI", precision = 8, scale = 2)
  private BigDecimal n1Thi;

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

  // Điểm ĐGNL (HSA)
  @Column(name = "hsa", precision = 10, scale = 2)
  private BigDecimal hsa;
}
