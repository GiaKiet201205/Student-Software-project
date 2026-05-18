package com.sgu.admission.repository;

import com.sgu.admission.entity.Nganh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NganhRepository extends JpaRepository<Nganh, Integer> {
    Optional<Nganh> findByMaNganh(String maNganh);
}
