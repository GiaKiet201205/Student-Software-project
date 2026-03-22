package com.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "xt_diemcongxetuyen")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiemCongXetTuyen {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "iddiemcong")
  private Long idDiemCong;

  @Column(name = "ts_cccd", nullable = false, length = 45)
  private String cccd;

  @Column(name = "manganh", length = 20)
  private String maNganh;

  @Column(name = "matohop", length = 10)
  private String maToHop;

  @Column(name = "phuongthuc", length = 45)
  private String phuongThuc;

  @Column(name = "diemCC", precision = 6, scale = 2)
  private BigDecimal diemCC;

  @Column(name = "diemUtxt", precision = 6, scale = 2)
  private BigDecimal diemUuTienXT;

  @Column(name = "diemTong", precision = 6, scale = 2)
  private BigDecimal diemTong;

  @Column(name = "ghichu", columnDefinition = "TEXT")
  private String ghiChu;

  @Column(name = "dc_keys", nullable = false, length = 45, unique = true)
  private String dcKeys;
}
