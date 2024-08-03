package com.ibx.caixabank.caixabanktest.controllers;

import com.ibx.caixabank.caixabanktest.entities.Department;
import com.ibx.caixabank.caixabanktest.repositories.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentRepository departmentRepository;

    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        return Optional.of(departmentRepository.findAll())
                .filter(departments -> !departments.isEmpty())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable("id") long id) {
        return departmentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        return buildCreatedResponseEntity(departmentRepository.save(department));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Department> modifyDepartment(@PathVariable("id") long id, @RequestBody Department requestDepartment) {
        return departmentRepository.findById(id)
                .map(department -> updateDepartmentWithExistingId(department, requestDepartment))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable("id") long id) {
        return departmentRepository.findById(id)
                .map(department -> deleteDepartmentById(department.getId()))
                .orElse(ResponseEntity.notFound().build());
    }

    private ResponseEntity<Department> buildCreatedResponseEntity(Department department) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(department);
    }

    private ResponseEntity<Department> updateDepartmentWithExistingId(Department existingDepartment, Department requestDepartment) {
        requestDepartment.setId(existingDepartment.getId());
        return buildCreatedResponseEntity(departmentRepository.save(requestDepartment));
    }

    private ResponseEntity<Void> deleteDepartmentById(Long id) {
        departmentRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
