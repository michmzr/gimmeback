package com.michmzr.gimmeback.item;

import com.michmzr.gimmeback.utills.BaseController;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
@RequestMapping("/api/v1/item")
public class ItemController extends BaseController {

  private final ItemApiService itemService;
  private final ItemMapper itemMapper;

  ItemController(ItemApiService itemService, ItemMapper itemMapper) {
    this.itemService = itemService;
    this.itemMapper = itemMapper;
  }

  @GetMapping("/")
  ResponseEntity<List<ItemDTO>> list() {
    List<ItemDTO> dtoList =
        itemService.findAllByAuthor().stream().map(itemMapper::toDTO).collect(Collectors.toList());

    return ResponseEntity.ok(dtoList);
  }

  @ResponseBody
  @PostMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity create(@Valid @RequestBody ItemDTO itemDto, UriComponentsBuilder b) {
    log.info("Creating item={}", itemDto);

    ItemDTO savedItem = itemService.save(itemDto);

    if (savedItem != null) {
      UriComponents uriComponents = b.path("/api/v1/item/{id}").buildAndExpand(savedItem.getId());

      return ResponseEntity.created(uriComponents.toUri()).build();
    } else {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @GetMapping("/{id}")
  ResponseEntity<ItemDTO> findById(@PathVariable Long id) {
    log.info("Returning item by id: {}", id);

    Optional<Item> itemOpt = itemService.find(id);

    return itemOpt
        .map(item -> ResponseEntity.ok(itemMapper.toDTO(item)))
        .orElse(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
  }

  @PutMapping("/{id}")
  ResponseEntity<ItemDTO> update(@PathVariable Long id, @Valid @RequestBody ItemDTO itemDTO) {
    log.info("Update id={}, dto={}", id, itemDTO);
    Optional<Item> itemOptional = itemService.find(id);

    if (itemOptional.isPresent()) {
      ItemDTO item = itemService.update(itemOptional.get(), itemDTO);
      return ResponseEntity.ok(item);
    } else {
      return onItemNotFound(id);
    }
  }

  @DeleteMapping("/{id}")
  ResponseEntity delete(@PathVariable Long id) {
    log.info("Deleting item #{}", id);
    if (!itemService.find(id).isPresent()) {
      return onItemNotFound(id);
    }

    itemService.delete(id);

    return ResponseEntity.ok().build();
  }
}
