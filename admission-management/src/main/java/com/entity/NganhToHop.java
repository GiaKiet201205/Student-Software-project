package com.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "xt_nganh_tohop")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NganhToHop {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "manganh", nullable = false, length = 45)
  private String maNganh;

  @Column(name = "matohop", nullable = false, length = 45)
  private String maToHop;

  @Column(name = "th_mon1", length = 10)
  private String mon1;

  @Column(name = "hsmon1")
  private Byte heSoMon1;

  @Column(name = "th_mon2", length = 10)
  private String mon2;

  @Column(name = "hsmon2")
  private Byte heSoMon2;

  @Column(name = "th_mon3", length = 10)
  private String mon3;

  @Column(name = "hsmon3")
  private Byte heSoMon3;

  /** manganh_matohop */
  @Column(name = "tb_keys", length = 45, unique = true)
  private String tbKeys;

  @Column(name = "N1")
  private Boolean n1;

  @Column(name = "TO")
  private Boolean toan;

  @Column(name = "LI")
  private Boolean ly;

  @Column(name = "HO")
  private Boolean hoa;

  @Column(name = "SI")
  private Boolean sinh;

  @Column(name = "VA")
  private Boolean van;

  @Column(name = "SU")
  private Boolean su;

  @Column(name = "DI")
  private Boolean dia;

  @Column(name = "TI")
  private Boolean tinHoc;

  @Column(name = "KHAC")
  private Boolean khac;

  @Column(name = "KTPL")
  private Boolean ktpl;

  @Column(name = "dolech", precision = 6, scale = 2)
  private BigDecimal doLech;
}

