package com.wine.to.up.user.service.service;

import com.wine.to.up.user.service.domain.dto.AbstractDto;
import com.wine.to.up.user.service.domain.entity.AbstractEntity;
import com.wine.to.up.user.service.exception.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains common CRUD operations and mapping Entity <-> Dto
 *
 * @param <I> Type of ID field in Entity and dto
 * @param <D> Type of Dto class
 * @param <E> Type of Entity class
 * @param <R> Type of Repository class
 */
@Transactional
@Service
public abstract class AbstractService
    <I, D extends AbstractDto<I>, E extends AbstractEntity<I>, R extends CrudRepository<E, I>> {

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

    public D create(D entity) {
        return modelMapper.map(repository.save(modelMapper.map(entity, getEntityClass())), this.getDTOClass());
    }

    @Transactional(readOnly = true)
    public D getById(I id) {
        return modelMapper.map(
            repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(getEntityClass().getName(), id.toString())
            ),
            this.getDTOClass()
        );
    }

    public List<D> mapList(List<E> source) {
        return source
                .stream()
                .map(element -> modelMapper.map(element, getDTOClass()))
                .collect(Collectors.toList());
    }

    public void update(D entity) {
        repository.save(modelMapper.map(entity, this.getEntityClass()));
    }

    public void delete(D entity) {
        repository.delete(modelMapper.map(entity, this.getEntityClass()));
    }

    public void deleteById(I id) {
        repository.deleteById(id);
    }
}
