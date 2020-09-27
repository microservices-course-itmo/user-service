package com.wine.to.up.user.service.service.impl;

import com.wine.to.up.user.service.domain.dto.RoleDto;
import com.wine.to.up.user.service.domain.entity.Role;
import com.wine.to.up.user.service.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleService extends AbstractService<Long, RoleDto, Role, RoleRepository> {
    @Autowired
    public RoleService(RoleRepository repository, ModelMapper modelMapper) {
        super(repository, modelMapper);
    }

    @Override
    public Class<Role> getEntityClass() {
        return Role.class;
    }

    @Override
    public Class<RoleDto> getDTOClass() {
        return RoleDto.class;
    }
}
