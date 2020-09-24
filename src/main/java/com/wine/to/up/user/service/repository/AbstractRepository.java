package com.wine.to.up.user.service.repository;

import com.wine.to.up.user.service.domain.entity.AbstractEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AbstractRepository<E extends AbstractEntity<I>, I> extends CrudRepository<E, I> {
}
