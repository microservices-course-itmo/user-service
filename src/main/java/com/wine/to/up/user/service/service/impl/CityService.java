package com.wine.to.up.user.service.service.impl;

import com.wine.to.up.user.service.domain.dto.CityDto;
import com.wine.to.up.user.service.domain.entity.City;
import com.wine.to.up.user.service.repository.CityRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CityService extends AbstractService<Long, CityDto, City, CityRepository> {
    @Autowired
    public CityService(CityRepository repository, ModelMapper modelMapper) {
        super(repository, modelMapper);
    }

    @Override
    public Class<City> getEntityClass() {
        return City.class;
    }

    @Override
    public Class<CityDto> getDTOClass() {
        return CityDto.class;
    }
}
