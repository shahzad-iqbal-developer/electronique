package com.electronique.store.repository;

import com.electronique.store.document.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ProductRepository extends ReactiveMongoRepository<Product,String> {

    public Flux<Product> findByName(String name);
}
