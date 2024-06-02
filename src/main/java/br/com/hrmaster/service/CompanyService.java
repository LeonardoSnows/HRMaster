package br.com.hrmaster.service;

import br.com.hrmaster.model.Company;
import org.springframework.stereotype.Service;

@Service
public interface CompanyService {

    Company saveCompany(Company company);
}
