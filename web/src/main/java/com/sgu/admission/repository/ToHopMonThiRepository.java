package com.sgu.admission.repository;

import com.sgu.admission.entity.ToHopMonThi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ToHopMonThiRepository extends JpaRepository<ToHopMonThi, Integer> {
    Optional<ToHopMonThi> findByMaToHop(String maToHop);
}
