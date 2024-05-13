package br.com.hrmaster.controller;

import br.com.hrmaster.model.Employee;
import br.com.hrmaster.repository.EmployeeRepository;
import br.com.hrmaster.service.EmployeeService;
import br.com.hrmaster.service.impl.EmployeeServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class EmployeeController {

    @Autowired
    EmployeeServiceImpl employeeServiceImpl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/hr/login")
    public String pgLogin() {
        return "login/login";
    }

    @GetMapping("/hr/forgot_password")
    public String pgForgotPassowrd() {
        return "login/forgot-password";
    }

    @GetMapping("/hr/register")
    public String pgRegister(RedirectAttributes redirectAttributes) {
        return "register/register";
    }

    @PostMapping("/register")
    @Transactional
    public String setEmployees(Employee employee, RedirectAttributes redirectAttributes) {
        String hashPwd = passwordEncoder.encode(employee.getPassword());
        employee.setPassword(hashPwd);
        employee.setRoles("USER");
        try {
            Employee employeeSaved = employeeServiceImpl.registerEmployee(employee);
            redirectAttributes.addFlashAttribute("success", "Seus dados foram salvos no banco de dados !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", "Seus dados foram salvos no banco de dados !");
        }
        return "redirect:/hr/login";
    }

    @RequestMapping("/hr/dashboard")
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        return "home/index";
    }
}
