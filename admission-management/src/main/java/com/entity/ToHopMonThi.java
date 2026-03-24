package com.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "xt_tohop_monthi")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToHopMonThi {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "idtohop")
  private Integer idToHop;

  @Column(name = "matohop", nullable = false, length = 45, unique = true)
  private String maToHop;

  @Column(name = "mon1", nullable = false, length = 10)
  private String mon1;

  @Column(name = "mon2", nullable = false, length = 10)
  private String mon2;

  @Column(name = "mon3", nullable = false, length = 10)
  private String mon3;

  @Column(name = "tentohop", length = 100)
  private String tenToHop;
}
