package br.com.hrmaster.repository;

import br.com.hrmaster.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Companyrepository extends JpaRepository<Company, Long> {
    Company findByName(String name);
}
