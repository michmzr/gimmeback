package com.michmzr.gimmeback.person;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/person/")
public class PersonController {
    private final PersonApiService personApiService;

    @Autowired
    public PersonController(PersonApiService personApiService) {
        this.personApiService = personApiService;
    }

    @GetMapping
    ResponseEntity<List<PersonDTO>> list() {
        return ResponseEntity.ok(personApiService.findAll());
    }

    @PostMapping("/create")
    ResponseEntity<Person> create(@Valid @RequestBody PersonDTO personDTO, BindingResult bindingResult) {
        log.info("Creating person: {}", personDTO);
        personApiService.save(personDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    ResponseEntity<Person> findById(@PathVariable Long id) {
        log.info("Returning person by id: {}", id);

        Optional<Person> stock = personApiService.find(id);

        return stock.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    ResponseEntity<PersonDTO> update(@PathVariable Long id, @Valid @RequestBody PersonDTO personDTO, BindingResult bindingResult) {
        Optional<Person> person = personApiService.find(id);
        if (person.isEmpty()) {
            log.error("Not found person {}", id);

            return ResponseEntity.notFound().build();
        }

        personApiService.update(person.get(), personDTO);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        if (!personApiService.find(id).isPresent()) {
            log.error("Id {} is not existed", id);

            ResponseEntity.notFound().build();
        }

        personApiService.delete(id);

        return ResponseEntity.ok().build();
    }
}
