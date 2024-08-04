package com.ibx.caixabank.caixabanktest.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "EMPLOYEE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SALARY")
    private Double salary;

    @ManyToOne(cascade = {PERSIST, MERGE, REFRESH, DETACH})
    @JoinColumn(name = "DEPARTMENT_ID")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Department department;

    public Employee(Long id, String name, Double salary) {
        this(id, name, salary, null);
    }

}
