package br.com.hrmaster.service;

import br.com.hrmaster.model.Employee;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeService {
    Employee registerEmployee(Employee employee);

    Employee getEmployeeByEmail(String email);

    String sendMailToResetPassword(Employee employee);

    long countEmployee();
}
