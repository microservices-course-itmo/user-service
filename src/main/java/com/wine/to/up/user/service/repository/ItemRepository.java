package com.wine.to.up.user.service.repository;

import com.wine.to.up.user.service.domain.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {
    Item findItemById(String id);
}
