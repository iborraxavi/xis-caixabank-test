package com.ibx.caixabank.caixabanktest.repositories;

import com.ibx.caixabank.caixabanktest.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
