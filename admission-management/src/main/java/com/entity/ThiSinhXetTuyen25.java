package com.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "xt_thisinhxettuyen25")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThiSinhXetTuyen25 {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "idthisinh")
  private Integer idThiSinh;

  @Column(name = "cccd", length = 20, unique = true)
  private String cccd;

  @Column(name = "sobaodanh", length = 45)
  private String soBaoDanh;

  @Column(name = "ho", length = 100)
  private String ho;

  @Column(name = "ten", length = 100)
  private String ten;

  @Column(name = "ngay_sinh", length = 45)
  private String ngaySinh;

  @Column(name = "dien_thoai", length = 20)
  private String dienThoai;

  @Column(name = "password", length = 100)
  private String password;

  @Column(name = "gioi_tinh", length = 10)
  private String gioiTinh;

  @Column(name = "email", length = 100, unique = true)
  private String email;

  @Column(name = "noi_sinh", length = 45)
  private String noiSinh;

  @Column(name = "updated_at")
  private LocalDate updatedAt;

  @Column(name = "doi_tuong", length = 45)
  private String doiTuong;

  @Column(name = "khu_vuc", length = 45)
  private String khuVuc;
}