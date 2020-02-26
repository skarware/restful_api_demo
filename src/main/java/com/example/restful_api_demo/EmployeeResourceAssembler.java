package com.example.restful_api_demo;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
class EmployeeResourceAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {

    @Override
    public EntityModel<Employee> toModel(Employee employee) {

        return new EntityModel<>(employee,
                linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
    }


//    @Override
//    public CollectionModel<EntityModel<Employee>> toCollectionModel(Iterable<? extends Employee> entities) {
//        return null;
//    }
}
