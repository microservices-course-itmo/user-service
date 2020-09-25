package com.wine.to.up.user.service.repository;

import com.wine.to.up.user.service.domain.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends AbstractRepository<User, Long>{
}
