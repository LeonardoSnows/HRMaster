package br.com.hrmaster.service.impl;

import br.com.hrmaster.model.Employee;
import br.com.hrmaster.model.PasswordResetToken;
import br.com.hrmaster.repository.EmployeeRepository;
import br.com.hrmaster.repository.TokenRepository;
import br.com.hrmaster.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.UUID;

@Component
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    JavaMailSender javaMailSender;

    @Override
    public Employee registerEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeByEmail(String email) {
        return employeeRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public String sendMailToResetPassword(Employee employee) {
        try {
            String resetLink = generateResetToken(employee);
            if (!resetLink.isEmpty()) {
                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setTo(employee.getEmail());

                msg.setSubject("Welcome to HRMaster");
                msg.setText("Hello \n\n" + "Please click on this link to Reset your Password: " + resetLink + ". \n\n" + "Regards \n" + "ABC");

                javaMailSender.send(msg);
                return "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @Override
    public long countEmployee() {
        return employeeRepository.count();
    }

    private String generateResetToken(Employee employee) {
        UUID uuid = UUID.randomUUID();
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime expiryDateTime = currentDateTime.plusMinutes(60);
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setEmployeeToSet(employee);
        resetToken.setToken(uuid.toString());
        resetToken.setExpiryDateTime(expiryDateTime);
        resetToken.setEmployeeToSet(employee);
        resetToken.setId(1L);
        PasswordResetToken token = tokenRepository.save(resetToken);
        if (token != null) {
            String endpointURL = "http://localhost:8080/hr/resetPassword";
            return endpointURL + "/" + resetToken.getToken();
        }
        return "";
    }

    public boolean hasExpired(LocalDateTime expiryDateTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return expiryDateTime.isAfter(currentDateTime);
    }
}
