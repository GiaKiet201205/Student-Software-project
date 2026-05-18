package com.sgu.admission.repository;

import com.sgu.admission.entity.DiemThiXetTuyen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiemThiXetTuyenRepository extends JpaRepository<DiemThiXetTuyen, Integer> {
    Optional<DiemThiXetTuyen> findByCccdAndPhuongThuc(String cccd, String phuongThuc);
}
