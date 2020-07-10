package com.target.igniteplus.sample.samplee.Repo;

import com.target.igniteplus.sample.samplee.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long id);
}
