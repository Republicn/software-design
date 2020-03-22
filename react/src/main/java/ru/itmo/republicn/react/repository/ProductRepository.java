package ru.itmo.republicn.react.repository;

import ru.itmo.republicn.react.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

}