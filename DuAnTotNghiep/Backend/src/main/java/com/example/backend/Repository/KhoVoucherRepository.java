package com.example.backend.Repository;

import com.example.backend.Entity.KhoVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhoVoucherRepository extends JpaRepository<KhoVoucher, Integer> {
}