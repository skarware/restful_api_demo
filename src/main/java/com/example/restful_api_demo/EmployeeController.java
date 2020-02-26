package com.example.restful_api_demo;


import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
class EmployeeController {

    private final EmployeeRepository repository;
    private final EmployeeResourceAssembler assembler;

    EmployeeController(EmployeeRepository repository, EmployeeResourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    // Aggregate root

    @GetMapping("/employees")
    CollectionModel<EntityModel<Employee>> all() {

        List<EntityModel<Employee>> employees = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
//                .map(employee -> new EntityModel<>(employee,
//                        linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
//                        linkTo(methodOn(EmployeeController.class).all()).withRel("employees")))
//                .collect(Collectors.toList());

        return new CollectionModel<>(employees,
                linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }

    // RPC - restless way
    @GetMapping("/rpc/employees")
    List<Employee> allRPC() {
        return repository.findAll();
    }

    @PostMapping("/employees")
    Employee newEmployee(@RequestBody Employee newEmployee) {
        return repository.save(newEmployee);
    }

    // Single item

    @GetMapping("/employees/{id}")
    EntityModel<Employee> one(@PathVariable Long id) {

        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return assembler.toModel(employee);
//        return new EntityModel<>(employee,
//                linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
//                linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
    }

    // RPC - restless way
    @GetMapping("/rpc/employees/{id}")
    Employee oneRPC(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @PutMapping("/employees/{id}")
    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

        return repository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });
    }

    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
