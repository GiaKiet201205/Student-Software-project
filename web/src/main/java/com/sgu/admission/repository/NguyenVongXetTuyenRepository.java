package com.sgu.admission.repository;

import com.sgu.admission.entity.NguyenVongXetTuyen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NguyenVongXetTuyenRepository extends JpaRepository<NguyenVongXetTuyen, Integer> {
    List<NguyenVongXetTuyen> findByCccdOrderByThuTu(String cccd);
    List<NguyenVongXetTuyen> findByCccdAndMaNganh(String cccd, String maNganh);
}
