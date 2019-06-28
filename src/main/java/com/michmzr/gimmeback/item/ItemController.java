package com.michmzr.gimmeback.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/item")
public class ItemController {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemService itemService;

    @GetMapping
    public ResponseEntity<List<Item>> findAll() {
        return ResponseEntity.ok(itemService.findAll());
    }

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody Item product) {
        return ResponseEntity.ok(itemService.save(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> findById(@PathVariable Long id) {
        Optional<Item> stock = itemService.findById(id);
        if (!stock.isPresent()) {
            log.error("Id " + id + " is not existed");
            ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(stock.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> update(@PathVariable Long id, @Valid @RequestBody Item product) {
        if (!itemService.findById(id).isPresent()) {
            log.error("Id " + id + " is not existed");
            ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(itemService.save(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        if (!itemService.findById(id).isPresent()) {
            log.error("Id " + id + " is not existed");

            ResponseEntity.badRequest().build();
        }

        itemService.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
