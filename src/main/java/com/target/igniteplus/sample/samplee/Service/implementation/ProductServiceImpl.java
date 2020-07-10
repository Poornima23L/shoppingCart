package com.target.igniteplus.sample.samplee.Service.implementation;

import com.target.igniteplus.sample.samplee.Model.Product;
import com.target.igniteplus.sample.samplee.Repo.ProductRepo;
import com.target.igniteplus.sample.samplee.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;

    @Autowired
    public ProductServiceImpl(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }


    public Page<Product> findAllProductsPageable(Pageable pageable) {
        return productRepo.findAll(pageable);
    }


    public Optional<Product> findById(Long id) {
        return productRepo.findById(id);
    }
}
