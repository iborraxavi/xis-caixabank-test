package com.ibx.caixabank.caixabanktest.repositories;

import com.ibx.caixabank.caixabanktest.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
