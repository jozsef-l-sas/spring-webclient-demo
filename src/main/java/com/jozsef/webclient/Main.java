package com.jozsef.webclient;

import reactor.core.scheduler.Schedulers;

public class Main {

    public static void main(String[] args) {
        WebClientAPI api = new WebClientAPI();

        api.createNewProduct()
           .thenMany(api.getAllProducts())
           .take(1)
           .flatMap(p -> api.updateProduct(p.getId(), "White Tea", 0.99))
           .flatMap(p -> api.deleteProduct(p.getId()))
           .thenMany(api.getAllProducts())
           .thenMany(api.getAllEvents())
           .subscribeOn(Schedulers.newSingle("myThread"))
           .subscribe(System.out::println);
    }

}
