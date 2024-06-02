package br.com.hrmaster.model;

import br.com.hrmaster.DTO.CompanyDTO;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Table(name = "TB_COMPANY")
@Getter
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String area;

    @Column(nullable = false, unique = true)
    private String cnpj;

    @Column(nullable = false)
    private String postcode;

    @Column(nullable = false)
    private String state;

    @Column(name = "Numero de colaboradores")
    private Long numberEmployee;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Employee> employeesList;

    public Company(CompanyDTO companyDTO) {
        this.name = companyDTO.name();
        this.area = companyDTO.area();
        this.cnpj = companyDTO.cnpj();
        this.postcode = companyDTO.postcode();
        this.state = companyDTO.state();
    }
}
