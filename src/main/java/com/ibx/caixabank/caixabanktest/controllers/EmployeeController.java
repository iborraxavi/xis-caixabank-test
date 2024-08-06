package com.ibx.caixabank.caixabanktest.controllers;

import com.ibx.caixabank.caixabanktest.entities.Department;
import com.ibx.caixabank.caixabanktest.entities.Employee;
import com.ibx.caixabank.caixabanktest.repositories.DepartmentRepository;
import com.ibx.caixabank.caixabanktest.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PutMapping("/{id}")
    public ResponseEntity<Employee> modifyEmployee(@PathVariable Long id, @RequestBody Employee requestEmployee) {
        return employeeRepository.findById(id)
                .map(existingEmployee -> modifyEmployeeWithDepartmentById(requestEmployee, existingEmployee))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        return employeeRepository.findById(id)
                .map(employee -> deleteEmployeeById(employee.getId()))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllEmployee() {
        employeeRepository.deleteAll();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/salary")
    public ResponseEntity<List<Employee>> getSalary(@RequestParam(name = "initialSalary") Double initialSalary, @RequestParam(name = "finalSalary") Double finalSalary) {
        return ResponseEntity.ok(employeeRepository.findBySalaryBetween(initialSalary, finalSalary));
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

    private ResponseEntity<Employee> modifyEmployeeWithDepartmentById(Employee requestEmployee, Employee existingEmployee) {
        return departmentRepository.findById(requestEmployee.getDepartment().getId())
                .map(department -> {
                    requestEmployee.setId(existingEmployee.getId());
                    requestEmployee.setDepartment(department);
                    return department;
                })
                .map(department -> employeeRepository.save(requestEmployee))
                .map(this::buildCreatedResponseEntity)
                .orElse(ResponseEntity.notFound().build());
    }

    private ResponseEntity<Void> deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
