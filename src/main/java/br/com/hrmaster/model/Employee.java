package br.com.hrmaster.model;

import br.com.hrmaster.DTO.EmployeeDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.management.relation.Role;
import java.util.Collection;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_EMPLOYEE")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String dtNascimento;

    @Column(nullable = false)
    private String cargo;

    @Column(nullable = false)
    private String roles;

    private String address;

    private String area;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne(mappedBy = "employeeToSet", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PasswordResetToken passwordResetToken;

    public Employee(EmployeeDTO employeeDTO, String password) {
        this.email = employeeDTO.email();
        this.password = password;
        this.name = employeeDTO.name();
        this.dtNascimento = employeeDTO.dtNascimento();
        this.cargo = employeeDTO.cargo();
    }
}
