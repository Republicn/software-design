package ru.itmo.republicn.react.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import ru.itmo.republicn.react.model.Product;
import ru.itmo.republicn.react.model.UserProduct;
import ru.itmo.republicn.react.repository.ProductRepository;
import ru.itmo.republicn.react.repository.UserRepository;

import static ru.itmo.republicn.react.converter.FromRublesConverter.fromRubles;

@RestController
@RequestMapping("products")
class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public Flux<UserProduct> getProducts(String userId) {
        return userRepository.findById(userId).flatMapMany(user ->
            productRepository
                .findAll()
                .map(product ->
                    new UserProduct(
                        product.getName(),  
                        userId,
                        fromRubles(product.getPriceInRubles(), user.getCurrency()))
                )
        );
    }

    @PostMapping
    public void addProduct(@RequestBody Product product) {
        productRepository.save(product);
    }
}