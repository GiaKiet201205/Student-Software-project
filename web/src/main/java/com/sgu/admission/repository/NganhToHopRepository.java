package com.sgu.admission.repository;

import com.sgu.admission.entity.NganhToHop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NganhToHopRepository extends JpaRepository<NganhToHop, Integer> {
    List<NganhToHop> findByMaNganh(String maNganh);
    Optional<NganhToHop> findByMaNganhAndMaToHop(String maNganh, String maToHop);
}
