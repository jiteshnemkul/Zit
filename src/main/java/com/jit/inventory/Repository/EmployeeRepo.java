package com.jit.inventory.Repository;

import com.jit.inventory.Entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface EmployeeRepo extends JpaRepository<Employee,Long> {
    Employee findByUsername(String username);
    List<Employee> findAll();
}
