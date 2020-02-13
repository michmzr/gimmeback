package com.michmzr.gimmeback.item;

import com.michmzr.gimmeback.security.SpringSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    private final SpringSecurityService springSecurityService;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemService(SpringSecurityService springSecurityService, ItemRepository itemRepository, ItemMapper itemMapper) {
        this.springSecurityService = springSecurityService;
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    public List<Item> findAll() {//todo test
        return itemRepository.findAllByAuthor(
                springSecurityService.getCurrentUser()
        );
    }

    public Optional<Item> find(Long id) {
        return itemRepository.findByIdAndAuthor(
                id,
                springSecurityService.getCurrentUser()
        );
    }

    public Item save(ItemDTO itemDTO) { //todo test
        Item item = itemMapper.fromDTO(itemDTO);
        item.setAuthor(
                springSecurityService.getCurrentUser()
        );

        return itemRepository.save(item);
    }

    public Item update(Item item, ItemDTO itemDTO) { //todo test
        item.setName(itemDTO.getName());
        item.setValue(itemDTO.getValue());
        item.setType(itemDTO.getType());

        return itemRepository.save(item);
    }

    public void delete(Long id) {
        itemRepository.deleteByIdAndAuthor(
                id,
                springSecurityService.getCurrentUser()
        );
    }
}
