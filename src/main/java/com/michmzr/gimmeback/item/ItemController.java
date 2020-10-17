package com.michmzr.gimmeback.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/item")
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    public ItemController(ItemService itemService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    //todo ItemApiService

    /*@GetMapping("/all")
    public ResponseEntity<List<ItemDTO>> findAll() {
        List<ItemDTO> dtoList = itemService
                .findAllByAuthor()
                .stream()
                .map(itemMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }*/

    @PostMapping("/create")
    public ResponseEntity create(@Valid @RequestBody ItemDTO itemDto) {
        Item item = itemService.save(itemDto);

        if (item != null) {
            return ResponseEntity.ok(item);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> findById(@PathVariable Long id) {
        Optional<Item> itemOpt = itemService.find(id);

        return itemOpt.
                map(item -> ResponseEntity.ok(itemMapper.toDTO(item)))
                .orElse(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> update(@PathVariable Long id,
                                       @Valid @RequestBody ItemDTO itemDTO) { //todo test
        Optional<Item> itemOptional = itemService.find(id);

        if (itemOptional.isPresent()) {
            return ResponseEntity.ok(itemService.update(itemOptional.get(), itemDTO));

        } else {
            onItemNotFound(id);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        if (!itemService.find(id).isPresent()) {
            onItemNotFound(id);
        }

        itemService.delete(id);

        return ResponseEntity.ok().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    void onItemNotFound(long id) {
        log.error("Not found item " + id);
    }
}
