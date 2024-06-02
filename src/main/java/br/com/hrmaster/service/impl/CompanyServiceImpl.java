package br.com.hrmaster.service.impl;

import br.com.hrmaster.model.Company;
import br.com.hrmaster.repository.Companyrepository;
import br.com.hrmaster.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    Companyrepository repository;

    @Override
    public Company saveCompany(Company company) {
        return repository.save(company);
    }
}
