package com.target.igniteplus.sample.samplee.Service.implementation;


import com.target.igniteplus.sample.samplee.Exception.NotEnoughProductsInStockException;
import com.target.igniteplus.sample.samplee.Model.Product;
import com.target.igniteplus.sample.samplee.Repo.ProductRepo;
import com.target.igniteplus.sample.samplee.Service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Shopping Cart is implemented with a Map, and as a session bean
 *
 * @author Dusan
 */
@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ProductRepo productRepo;

    private Map<Product, Integer> products = new HashMap<>();

    @Autowired
    public ShoppingCartServiceImpl(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    /**
     * If product is in the map just increment quantity by 1.
     * If product is not in the map with, add it with quantity 1
     *
     * @param product
     */
    @Override
    public void addProduct(Product product) {
        if (products.containsKey(product)) {
            products.replace(product, products.get(product) + 1);
        } else {
            products.put(product, 1);
        }
    }

    /**
     * If product is in the map with quantity > 1, just decrement quantity by 1.
     * If product is in the map with quantity 1, remove it from map
     *
     * @param product
     */
    @Override
    public void removeProduct(Product product) {
        if (products.containsKey(product)) {
            if (products.get(product) > 1)
                products.replace(product, products.get(product) - 1);
            else if (products.get(product) == 1) {
                products.remove(product);
            }
        }
    }

    /**
     * @return unmodifiable copy of the map
     */
    @Override
    public Map<Product, Integer> getProductsInCart() {
        return Collections.unmodifiableMap(products);
    }

    /**
     * Checkout will rollback if there is not enough of some product in stock
     *
     * @throws NotEnoughProductsInStockException
     */
    @Override
    public void checkout() throws NotEnoughProductsInStockException {
        Optional<Product> product;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            // Refresh quantity for every product before checking
            product = productRepo.findById(entry.getKey().getId());

            if (Product.getQuantity() < entry.getValue())
                throw new NotEnoughProductsInStockException(product);
            entry.getKey().setQuantity(Product.getQuantity() - entry.getValue());
        }
        productRepo.save(products.keySet());
        productRepo.flush();
        products.clear();
    }

    @Override
    public BigDecimal getTotal() {
        return products.entrySet().stream()
                .map(entry -> entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}

/*public class ShoppingCartService {

    @Autowired
    private ProductRepo productRepo;

    //CreateEmployee
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    //getAllEmployees
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();

    }

    //getEmployeeById
    public Optional<Product> getProductById(Long id) throws ProductNotFoundException{
        Optional<Product> product=productRepo.findById(id);

        if(!product.isPresent())
            throw new ProductNotFoundException("product not found");
        return product;
    }

    //deleteEmployeeById
    public void deleteProductById(Long id) {

        Optional<Product> product=productRepo.findById(id);
        if(!product.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"product not found in CART,enter correct details");
        }

        productRepo.deleteById(id);

    }

    //updateEmpNameById
    public Employee addProductById(Long id) throws ProductNotFoundException {

        if(!productRepo.findById(id).isPresent()) {
            throw new ProductNotFoundException("product not found");
        }
        Product product=productRepo.getOne(id);
        employee.setName(newName);
        return employeeRepository.save(employee);

    }




}*/
