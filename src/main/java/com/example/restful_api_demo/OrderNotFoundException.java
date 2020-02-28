package com.example.restful_api_demo;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("Could not find order by id: " + id);
    }
}