package com.sgu.admission.repository;

import com.sgu.admission.entity.BangQuyDoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BangQuyDoiRepository extends JpaRepository<BangQuyDoi, Integer> {
    List<BangQuyDoi> findByPhuongthucAndTohopAndMon(String phuongthuc, String tohop, String mon);
}
