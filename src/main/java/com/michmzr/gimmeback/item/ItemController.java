package com.michmzr.gimmeback.item;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.Instant;
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

    @GetMapping("/")
    public ResponseEntity<List<ItemDTO>> list() {
        List<ItemDTO> dtoList = itemService
                .findAllByAuthor()
                .stream()
                .map(itemMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }


    @PostMapping("/query")
    public ResponseEntity<String> queryParams(@Valid @RequestBody RequestParams params) {
        return ResponseEntity.ok("params " + params);
    }

    //    @ResponseBody
    @PostMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@Valid @RequestBody ItemDTO itemDto,/* Errors errors,*/ UriComponentsBuilder b) {
    /*    if(errors.hasErrors()){
            log.error("Validation errors :(");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).c.build();

        } else {*/
        Item item = itemService.save(itemDto);

        //todo walidacja

        if (item != null) {
            UriComponents uriComponents =
                    b.path("/api/v1/item/{id}").buildAndExpand(item.getId());

            return ResponseEntity
                    .created(uriComponents.toUri()) //todo fix
                    .build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
//        }
    }

    @Data
    public static class RequestParams {

        @NotBlank
        private String name;

        @NotNull
        private Instant timestamp;

        @Pattern(regexp = "^[0-9A-Za-z]{3}$")
        private String code1;

        @Size(min = 4, max = 5)
        private String code2;

        @AssertTrue(message = "You must provide at least code1 or code2")
        private boolean isCode1orCode2Provided() {
            return code1 != null || code2 != null;
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
                                       @Valid @RequestBody ItemDTO itemDTO) {
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

/*    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }*/
}
