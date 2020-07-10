package com.target.igniteplus.sample.samplee.Service;

import com.target.igniteplus.sample.samplee.Exception.NotEnoughProductsInStockException;
import com.target.igniteplus.sample.samplee.Model.Product;

import java.math.BigDecimal;
import java.util.Map;

public interface ShoppingCartService {

    void addProduct(Product product);

    void removeProduct(Product product);

    Map<Product, Integer> getProductsInCart();

    void checkout() throws NotEnoughProductsInStockException;

    BigDecimal getTotal();
}