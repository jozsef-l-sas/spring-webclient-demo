package com.jozsef.webclient;

import com.jozsef.webclient.domain.Product;
import com.jozsef.webclient.domain.ProductEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class WebClientAPI {

    private WebClient webClient;

    public WebClientAPI() {
        this.webClient = WebClient.builder()
                                  .baseUrl("http://localhost:8080/functional/products")
                                  .build();
    }

    public Flux<Product> getAllProducts() {
        return webClient
                .get()
                .retrieve()
                .bodyToFlux(Product.class)
                .doOnNext(o -> System.out.println("********* GET: " + o));
    }

    public Mono<Product> getProduct(String id) {
        return webClient
                .get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(o -> System.out.println("********* GET: " + o));
    }

    public Mono<ResponseEntity<Product>> createNewProduct() {
        return webClient
                .post()
                .body(Mono.just(Product.builder().name("Jasmine Tea").price(1.99).build()), Product.class)
                .exchangeToMono(response -> response.toEntity(Product.class))
                .doOnSuccess(o -> System.out.println("********* POST: " + o));
    }

    public Mono<Product> updateProduct(String id, String name, double price) {
        return webClient
                .put()
                .uri("/{id}", id)
                .body(Mono.just(Product.builder().id(id).name(name).price(price).build()), Product.class)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnSuccess(o -> System.out.println("********* PUT: " + o));
    }

    public Mono<ResponseEntity<Void>> deleteProduct(String id) {
        return webClient
                .delete()
                .uri("/{id}", id)
                .exchangeToMono(response -> response.toEntity(Void.class))
                .doOnSuccess(o -> System.out.println("********* DELETE: " + o));
    }

    public Mono<Void> deleteAllProducts() {
        return webClient
                .delete()
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(o -> System.out.println("********* DELETE: " + o));
    }

    public Flux<ProductEvent> getAllEvents() {
        return webClient
                .get()
                .uri("/events")
                .retrieve()
                .bodyToFlux(ProductEvent.class);
    }

}
