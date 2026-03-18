package com.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "xt_bangquydoi")
public class BangQuyDoi {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer idqd;

  private String d_phuongthuc;
  private String d_tohop;
  private String d_mon;

  private BigDecimal d_diema;

  public Integer getIdqd() { return idqd; }

  public String getD_phuongthuc() { return d_phuongthuc; }

  public String getD_tohop() { return d_tohop; }

  public String getD_mon() { return d_mon; }

  public BigDecimal getD_diema() { return d_diema; }
}
