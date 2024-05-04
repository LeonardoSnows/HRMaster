package br.com.hrmaster.repository;

import br.com.hrmaster.model.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, UUID> {
    Employee findByEmail(String email);
}
