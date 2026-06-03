package com.example.backend.Repository;

import com.example.backend.Entity.SanPham;
import com.example.backend.Response.SanPhamResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham,Integer> {

}
