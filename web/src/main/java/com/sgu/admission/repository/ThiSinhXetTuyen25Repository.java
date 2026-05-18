package com.sgu.admission.repository;

import com.sgu.admission.entity.ThiSinhXetTuyen25;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThiSinhXetTuyen25Repository extends JpaRepository<ThiSinhXetTuyen25, Integer> {
    Optional<ThiSinhXetTuyen25> findByCccd(String cccd);
}
