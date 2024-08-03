package com.ibx.caixabank.caixabanktest.controllers;

import com.ibx.caixabank.caixabanktest.entities.Department;
import com.ibx.caixabank.caixabanktest.entities.Employee;
import com.ibx.caixabank.caixabanktest.repositories.DepartmentRepository;
import com.ibx.caixabank.caixabanktest.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final DepartmentRepository departmentRepository;

    private final EmployeeRepository employeeRepository;

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return Optional.of(employeeRepository.findAll())
                .filter(departments -> !departments.isEmpty())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return Optional.ofNullable(employee.getDepartment())
                .flatMap(department -> departmentRepository.findById(department.getId()))
                .map(department -> createEmployeeWithDepartment(employee, department))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        return employeeRepository.findById(id)
                .map(employee -> deleteEmployeeById(employee.getId()))
                .orElse(ResponseEntity.notFound().build());
    }

    private ResponseEntity<Employee> buildCreatedResponseEntity(Employee employee) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(employee);
    }

    private ResponseEntity<Employee> createEmployeeWithDepartment(Employee employee, Department department) {
        employee.setDepartment(department);
        return buildCreatedResponseEntity(employeeRepository.save(employee));
    }

    private ResponseEntity<Void> deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
