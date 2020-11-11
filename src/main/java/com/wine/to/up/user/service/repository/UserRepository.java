package com.wine.to.up.user.service.repository;

import com.wine.to.up.user.service.domain.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByPhoneNumber(String phoneNumber);
    User findUserById(Long id);
}
