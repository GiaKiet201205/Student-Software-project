package com.sgu.admission.repository;

import com.sgu.admission.entity.DiemCongXetTuyen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiemCongXetTuyenRepository extends JpaRepository<DiemCongXetTuyen, Long> {
    List<DiemCongXetTuyen> findByCccd(String cccd);
}
