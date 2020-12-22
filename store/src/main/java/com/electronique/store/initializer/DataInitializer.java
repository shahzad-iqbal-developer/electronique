package com.electronique.store.initializer;

import com.electronique.store.document.Product;
import com.electronique.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    ProductRepository productRepository;


    List<Product> products = Arrays.asList(new Product(null,"Dell",2500.00,"Dell laptop"),
            new Product(null,"Hp",2300.00,"Hp laptop"),
            new Product("ABC","Lenovo",1200.25,"Lenovo laptop"));

    @Override
    public void run(String... args) throws Exception {
        productRepository.deleteAll()
                         .thenMany(Flux.fromIterable(products))
                         .flatMap(productRepository::save)
                         .thenMany(productRepository.findAll())
                         .subscribe(product -> System.out.println("inserted from command runner :"+ product));


    }
}
