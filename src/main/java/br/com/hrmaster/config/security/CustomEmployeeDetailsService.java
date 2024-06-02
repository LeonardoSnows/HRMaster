package br.com.hrmaster.config.security;

import br.com.hrmaster.model.Employee;
import br.com.hrmaster.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CustomEmployeeDetailsService implements UserDetailsService {

    @Autowired
    EmployeeRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final List<Employee> employee = new ArrayList<>();
        employee.add(repository.findByEmailIgnoreCase(email));

//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority("USER"));

        if (employee.get(0) == null) {
            throw new UsernameNotFoundException("Could not find email");
        }

        return new User(
                employee.get(0).getEmail(),
                employee.get(0).getPassword(),
                new HashSet<GrantedAuthority>());
    }
}
