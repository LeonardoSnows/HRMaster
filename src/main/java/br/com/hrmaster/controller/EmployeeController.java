package br.com.hrmaster.controller;

import br.com.hrmaster.model.Employee;
import br.com.hrmaster.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String pgLogin() {
        return "login/loginPage";
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
        employee.setRoles("ROLE_USER");
        try {
            Employee employeeSaved = employeeRepository.save(employee);
            redirectAttributes.addFlashAttribute("success", "Seus dados foram salvos no banco de dados !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", "Seus dados foram salvos no banco de dados !");
        }
        return "redirect:/login";
    }

    @GetMapping("/home/dashboard")
    public String login() {
        return "home/index";
    }
}
