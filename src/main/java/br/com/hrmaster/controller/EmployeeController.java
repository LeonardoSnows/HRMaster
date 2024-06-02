package br.com.hrmaster.controller;

import br.com.hrmaster.DTO.CompanyDTO;
import br.com.hrmaster.DTO.EmployeeDTO;
import br.com.hrmaster.model.Company;
import br.com.hrmaster.model.Employee;
import br.com.hrmaster.model.PasswordResetToken;
import br.com.hrmaster.service.impl.CompanyServiceImpl;
import br.com.hrmaster.service.impl.EmployeeServiceImpl;
import br.com.hrmaster.service.impl.TokenServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;
import java.util.logging.Logger;

@Controller
@RequiredArgsConstructor
public class EmployeeController {

    Employee employee;

    @Autowired
    EmployeeServiceImpl employeeServiceImpl;

    @Autowired
    CompanyServiceImpl companyServiceImpl;

    @Autowired
    TokenServiceImpl tokenServiceImpl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/hr/login")
    public String pgLogin() {
        return "login/login";
    }

    @GetMapping("/hr/pgforgot_password")
    public String pgForgotPassowrd() {
        return "login/forgotPassword";
    }

    @GetMapping("/hr/register/employee")
    public String pgRegister(RedirectAttributes redirectAttributes) {
        return "register/register";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(EmployeeDTO emailDTO) {
        Employee employee = employeeServiceImpl.getEmployeeByEmail(emailDTO.email());
        String outPut = null;

        if (employee != null) {
            outPut = employeeServiceImpl.sendMailToResetPassword(employee);
        }

        if (Objects.equals(outPut, "success")) {
            return "redirect:/hr/pgforgot_password?success";
        }

        return "redirect:/hr/login?error";
    }

    @GetMapping("/hr/resetPassword/{token}")
    public String resetPassword(@PathVariable String token, Model model) {
        PasswordResetToken resetToken = tokenServiceImpl.getToken(token);
        if (resetToken != null && employeeServiceImpl.hasExpired(resetToken.getExpiryDateTime())) {
            String email = resetToken.getEmployeeToSet().getEmail();
            model.addAttribute("email", email);
            return "login/resetPassword";
        }
        return "redirect:/hr/pgforgot_password?error";
    }

    @Transactional
    @PostMapping("/hr/resetPassword/{token}")
    public String resetPasswordProcess(@ModelAttribute EmployeeDTO employeeDTO) {
        Employee employee = employeeServiceImpl.getEmployeeByEmail(employeeDTO.email());
        if (employee != null) {
            String hashPwd = passwordEncoder.encode(employeeDTO.password());
            employee.setPassword(hashPwd);
            employeeServiceImpl.registerEmployee(employee);
        }
        return "redirect:/hr/login";
    }

    @PostMapping("/register/employee")
    @Transactional
    public String registerEmployee(EmployeeDTO employeeDTO, RedirectAttributes redirectAttributes) {
        String hashPwd = passwordEncoder.encode(employeeDTO.password());
        Employee employee = new Employee(employeeDTO, hashPwd);
        employee.setRoles("USER");

        try {
            employeeServiceImpl.registerEmployee(employee);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/hr/login";
    }

    @PostMapping("/register/company")
    @Transactional
    public String registerCompany(CompanyDTO companyDTO) {
        try {
            companyServiceImpl.saveCompany(new Company(companyDTO));
            return "redirect:/hr/register/company";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "redirect:/hr/register/company";
        }
    }

    @GetMapping("/hr/dashboard")
    public String login(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        long employeeCount = employeeServiceImpl.countEmployee();
        model.addAttribute("employeesCount", employeeCount);

        String mail = SecurityContextHolder.getContext().getAuthentication().getName();
        this.employee = employeeServiceImpl.getEmployeeByEmail(mail);
        model.addAttribute("name", employee.getName());

        model.addAttribute("email", employee.getEmail());

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }

        return "home/index";
    }

    @GetMapping("/hr/profile")
    public String profilePage(Model model) {
        model.addAttribute("name", employee.getName());
        model.addAttribute("cargo", employee.getCargo());
        model.addAttribute("address", employee.getAddress());
        model.addAttribute("company", employee.getCompany().getName());
        model.addAttribute("postcode", employee.getCompany().getPostcode());
        model.addAttribute("state", employee.getCompany().getState());
        model.addAttribute("area", employee.getArea());
        model.addAttribute("email", employee.getEmail());

        return "profile/perfil";
    }

    @GetMapping("/hr/register/company")
    public String companyRegister(Model model) {

        return "register/registerCompany";
    }

}
