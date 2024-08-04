package com.ibx.caixabank.caixabanktest.repositories;

import com.ibx.caixabank.caixabanktest.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("SELECT d FROM Department d JOIN FETCH d.employees")
    List<Department> findAllWithEmployees();

    @Query("SELECT d FROM Department d JOIN FETCH d.employees WHERE d.id = :id")
    Optional<Department> findByIdWithEmployees(@Param("id") Long id);

    @Override
    @NonNull
    @Query("SELECT d FROM Department d WHERE d.id = :id")
    Optional<Department> findById(@Param("id") @NonNull Long id);

    @Override
    @Modifying
    @Query("DELETE FROM Department d where d.id = :id")
    void deleteById(@Param("id") @NonNull Long id);

    @Override
    @Modifying
    @Query("DELETE FROM Department")
    void deleteAll();

}
