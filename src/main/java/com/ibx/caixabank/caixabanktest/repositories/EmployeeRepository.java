package com.ibx.caixabank.caixabanktest.repositories;

import com.ibx.caixabank.caixabanktest.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Override
    @NonNull
    @Query("SELECT new Employee(e.id, e.name, e.salary) FROM Employee e")
    List<Employee> findAll();

    @Override
    @NonNull
    @Query("SELECT new Employee(e.id, e.name, e.salary) FROM Employee e WHERE e.id = :id")
    Optional<Employee> findById(@Param("id") @NonNull Long id);

    @Query("SELECT new Employee(e.id, e.name, e.salary) FROM Employee e WHERE e.salary BETWEEN :initialSalary AND :finalSalary")
    List<Employee> findBySalaryBetween(@Param("initialSalary") Double initialSalary, @Param("finalSalary") Double finalSalary);

    @Override
    @Modifying
    @Query("DELETE FROM Employee e where e.id = :id")
    void deleteById(@Param("id") @NonNull Long id);

    @Modifying
    @Query("DELETE FROM Employee e where e.department.id = :departmentId")
    void deleteByDepartmentId(@Param("departmentId") Long departmentId);

    @Override
    @Modifying
    @Query("DELETE FROM Employee")
    void deleteAll();

}
