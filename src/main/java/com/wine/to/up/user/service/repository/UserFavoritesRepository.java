package com.wine.to.up.user.service.repository;

import com.wine.to.up.user.service.domain.entity.Item;
import com.wine.to.up.user.service.domain.entity.User;
import com.wine.to.up.user.service.domain.entity.UserFavorites;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFavoritesRepository extends CrudRepository<UserFavorites, String> {
    Iterable<UserFavorites> findAllByItemId(String itemId);
    Iterable<UserFavorites> findAllByUserId(Long userId);
    void deleteByItemAndUser(Item item, User user);
    void deleteAllByUser(User user);
}
