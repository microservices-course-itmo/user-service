package com.wine.to.up.user.service.service.impl;

import com.wine.to.up.user.service.domain.dto.AbstractDto;
import com.wine.to.up.user.service.domain.entity.AbstractEntity;
import com.wine.to.up.user.service.repository.AbstractRepository;
import com.wine.to.up.user.service.service.CommonService;
import javax.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public abstract class AbstractService
        <I, D extends AbstractDto<I>,
            E extends AbstractEntity<I>,
            R extends AbstractRepository<E, I>>
    implements CommonService<I, D, E> {

    protected final R repository;
    protected final ModelMapper modelMapper;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    AbstractService(R repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public abstract Class<E> getEntityClass();
    public abstract Class<D> getDTOClass();

    @Override
    public D create(D entity) {
        return modelMapper.map(repository.save(modelMapper.map(entity, getEntityClass())), this.getDTOClass());
    }

    @Override
    @Transactional(readOnly = true)
    public D getById(I id) {
        return modelMapper.map(
            repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Entity with id: %s was not found", id.toString()))
            ),
            this.getDTOClass()
        );
    }

    @Override
    public void update(D entity) {
        repository.save(modelMapper.map(entity, this.getEntityClass()));
    }

    @Override
    public void delete(D entity) {
        repository.delete(modelMapper.map(entity, this.getEntityClass()));
    }

    @Override
    public void deleteById(I id) {
        repository.deleteById(id);
    }
}
