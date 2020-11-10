package com.wine.to.up.user.service.service;

import com.wine.to.up.user.service.domain.dto.CompanyDto;
import com.wine.to.up.user.service.domain.dto.ItemDto;
import com.wine.to.up.user.service.domain.entity.Company;
import com.wine.to.up.user.service.domain.entity.Item;
import com.wine.to.up.user.service.repository.CompanyRepository;
import com.wine.to.up.user.service.repository.ItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.modelmapper.ModelMapper;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;
        import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItemService extends AbstractService<String, ItemDto, Item, ItemRepository> {
    @Autowired
    public ItemService(ItemRepository repository, ModelMapper modelMapper) {
        super(repository, modelMapper);
    }

    @Override
    public Class<Item> getEntityClass() {
        return Item.class;
    }

    @Override
    public Class<ItemDto> getDTOClass() {
        return ItemDto.class;
    }
}
