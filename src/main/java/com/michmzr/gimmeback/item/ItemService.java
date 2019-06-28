package com.michmzr.gimmeback.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    @Autowired
    ItemRepository itemRepository;

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    Item create(@NotEmpty String name, @NotNull ItemType type, BigDecimal value) {
        Item item = new Item();

        item.setName(name);
        item.setType(type);
        item.setValue(value);

        return   itemRepository.save(item);
    }

    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    public Item save(Item item) {
        return itemRepository.save(item);
    }

    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }
}
