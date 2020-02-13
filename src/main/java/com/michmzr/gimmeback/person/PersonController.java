package com.michmzr.gimmeback.person;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/person/")
public class PersonController {
    @Autowired
    PersonRepository personRepository;
    @Autowired
    PersonService personService;

    @GetMapping
    ResponseEntity<List<Person>> list() {
        return ResponseEntity.ok(personService.findAll());
    }

    @PostMapping("/create")
    ResponseEntity<Person> create(@Valid @RequestBody PersonDTO personDTO, BindingResult bindingResult) {
        return ResponseEntity.ok(personService.save(personDTO));
    }

    @GetMapping("/{id}")
    ResponseEntity<Person> findById(@PathVariable Long id) {
        Optional<Person> stock = personService.find(id);

        if (!stock.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(stock.get());
    }

    @PutMapping("/{id}")
    ResponseEntity<Person> update(@PathVariable Long id, @Valid @RequestBody PersonDTO personDTO, BindingResult bindingResult) {
        Optional<Person> person = personService.find(id);
        if (!person.isPresent()) {
            log.error("Not found person " + id);

            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                personService.update(person.get(), personDTO)
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        if (!personService.find(id).isPresent()) {
            log.error("Id " + id + " is not existed");

            ResponseEntity.badRequest().build();
        }

        personService.delete(id);

        return ResponseEntity.ok().build();
    }
}
