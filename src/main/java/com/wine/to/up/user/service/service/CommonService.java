package com.wine.to.up.user.service.service;

import com.wine.to.up.user.service.domain.dto.AbstractDto;
import com.wine.to.up.user.service.domain.entity.AbstractEntity;

public interface CommonService<I, M extends AbstractDto<I>, E extends AbstractEntity<I>> {
    M create(M entity);

    M getById(I id);

    void update(M entity);

    void delete(M entity);

    void deleteById(I id);
}
