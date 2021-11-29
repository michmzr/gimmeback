package com.michmzr.gimmeback.item;

import com.michmzr.gimmeback.ApiService;
import com.michmzr.gimmeback.security.SpringSecurityService;
import com.michmzr.gimmeback.user.User;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ItemApiService extends ApiService {
  private final ItemRepository itemRepository;
  private final ItemMapper itemMapper;

  @Autowired
  public ItemApiService(
      SpringSecurityService springSecurityService,
      ItemRepository itemRepository,
      ItemMapper itemMapper) {
    super(springSecurityService);
    this.itemRepository = itemRepository;
    this.itemMapper = itemMapper;
  }

  public List<Item> findAllByAuthor() {
    return itemRepository.findAllByAuthor(getCurrentUser());
  }

  public Optional<Item> find(Long id) {
    return itemRepository.findByIdAndAuthor(id, getCurrentUser());
  }

  public ItemDTO save(ItemDTO itemDTO) {
    Item item = itemMapper.fromDTO(itemDTO);
    item.setAuthor(getCurrentUser());

    log.info("Saving item dto: {}, user: {}", itemDTO, item.getAuthor());

    return itemMapper.toDTO(item);
  }

  public ItemDTO update(Item item, ItemDTO itemDTO) {
    log.debug("Updating item:{} with {}, user: {}", item, itemDTO, getCurrentUser());

    item.setName(itemDTO.getName());
    item.setValue(itemDTO.getValue());
    item.setType(itemDTO.getType());

    log.info("Updating item: {} with dto: {}", item, itemDTO);

    itemRepository.save(item);

    return itemMapper.toDTO(item);
  }

  public void delete(Long id) {
    User currentUser = getCurrentUser();
    log.info("Deleting item #{}, user: {}", id, currentUser);
    itemRepository.deleteByIdAndAuthor(id, currentUser);
  }
}
