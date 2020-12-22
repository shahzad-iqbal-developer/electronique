package com.electronique.store.controller;

import com.electronique.store.document.Product;
import com.electronique.store.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @GetMapping("/product")
    public Flux<Product> getAllProducts(){
        return  productRepository.findAll();
    }

    @GetMapping("/product/{id}")
    public Mono<ResponseEntity<Product>> getProductById(@PathVariable String id){
        return productRepository.findById(id)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/product")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> createProduct(@RequestBody Product product){
            return productRepository.save(product);
    }

    @DeleteMapping("/product/{id}")
    public Mono<Void> deleteProduct(@PathVariable String id){
        return productRepository.deleteById(id);
    }

    @PutMapping("/product/{id}")
    public Mono<ResponseEntity<Product>> updateProduct(@PathVariable String id,
                                                       @RequestBody Product product){
       return productRepository.findById(id)
                .flatMap(product1 -> {
                    product1.setPrice(product.getPrice());
                    product1.setDescription(product.getDescription());
                    product1.setPrice(product.getPrice());
                    product1.setName(product.getName());
                    return productRepository.save(product1);
                }).map(updatedProduct -> new ResponseEntity<>(updatedProduct,HttpStatus.OK)).
                defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
