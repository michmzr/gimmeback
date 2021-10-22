package com.michmzr.gimmeback.item;

import com.michmzr.gimmeback.security.SpringSecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
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

    public List<Item> findAllByAuthor() {
        return itemRepository.findAllByAuthor(springSecurityService.getCurrentUser());
    }

    public Optional<Item> find(Long id) {
        return itemRepository.findByIdAndAuthor(id, springSecurityService.getCurrentUser());
    }

    public Item save(ItemDTO itemDTO) {
        Item item = itemMapper.fromDTO(itemDTO);
        item.setAuthor(springSecurityService.getCurrentUser());

        return itemRepository.save(item);
    }

    public Item update(Item item, ItemDTO itemDTO) {
        log.debug("Updating item:{} with {}", item, itemDTO);

        item.setName(itemDTO.getName());
        item.setValue(itemDTO.getValue());
        item.setType(itemDTO.getType());

        return itemRepository.save(item);
    }

    public void delete(Long id) {
        itemRepository.deleteByIdAndAuthor(id, springSecurityService.getCurrentUser());
    }
}
