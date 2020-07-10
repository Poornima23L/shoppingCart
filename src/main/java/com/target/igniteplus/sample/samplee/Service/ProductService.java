package com.target.igniteplus.sample.samplee.Service;

import com.target.igniteplus.sample.samplee.Model.Product;
import com.target.igniteplus.sample.samplee.Repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface ProductService {

    static Optional<Product> findById(Long id) {
        return null;
    }

    Page<Product> findAllProductsPageable(Pageable pageable);

}