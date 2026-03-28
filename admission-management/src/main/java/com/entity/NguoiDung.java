package com.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "xt_nguoidung")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idnguoidung")
    private Integer idNguoiDung;

    @Column(name = "username", length = 50, unique = true)
    private String username;

    @Column(name = "password", length = 50)
    private String password;

    @Column(name = "role")
    private Integer role;

    @Column(name = "active")
    private Integer active;

}
