package com.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "xt_bangquydoi")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BangQuyDoi {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "idqd")
  private Integer idqd;

  @Column(name = "d_phuongthuc", length = 45)
  private String phuongthuc;

  @Column(name = "d_tohop", length = 45)
  private String tohop;

  @Column(name = "d_mon", length = 45)
  private String mon;

  @Column(name = "d_diema", precision = 6, scale = 2)
  private BigDecimal diemA;

  @Column(name = "d_diemb", precision = 6, scale = 2)
  private BigDecimal diemB;

  @Column(name = "d_diemc", precision = 6, scale = 2)
  private BigDecimal diemC;

  @Column(name = "d_diemd", precision = 6, scale = 2)
  private BigDecimal diemD;

  @Column(name = "d_maquydoi", length = 45, unique = true)
  private String maQuydoi;

  @Column(name = "d_phanvi", length = 45)
  private String phanVi;
}
