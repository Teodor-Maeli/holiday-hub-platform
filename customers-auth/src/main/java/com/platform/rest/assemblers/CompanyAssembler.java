package com.platform.rest.assemblers;

import com.platform.domain.entity.Company;
import com.platform.models.CompanyRequest;
import com.platform.models.CompanyResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CompanyAssembler extends AbstractClientAssembler<CompanyResponse, CompanyRequest, Company> {

    protected CompanyAssembler(PasswordEncoder encoder) {
        super(encoder);
    }

    @Override
    protected CompanyResponse initResponse() {
        return new CompanyResponse();
    }

    @Override
    protected Company initEntity() {
        return new Company();
    }
}