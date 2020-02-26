package com.example.restful_api_demo;

class EmployeeNotFoundException extends RuntimeException {

    EmployeeNotFoundException(Long id) {
        super("Could not find employee by id: " + id);
    }
}