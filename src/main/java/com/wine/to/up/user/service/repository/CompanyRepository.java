package com.wine.to.up.user.service.repository;

import com.wine.to.up.user.service.domain.entity.Company;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long> {
}
