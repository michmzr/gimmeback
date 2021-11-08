package com.michmzr.gimmeback.person;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.badRequest;

import com.michmzr.gimmeback.utills.BaseController;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/v1/person/")
public class PersonController extends BaseController {

  private final PersonApiService personApiService;
  private final PersonMapper personMapper;

  @Autowired
  public PersonController(PersonApiService personApiService, PersonMapper personMapper) {
    this.personApiService = personApiService;
    this.personMapper = personMapper;
  }

  @GetMapping
  public ResponseEntity<List<PersonDTO>> list() {
    log.info("Returning persons...");
    List<PersonDTO> dtoList =
        personApiService.findAllByAuthor();

    return ResponseEntity.ok(dtoList);
  }

  @ResponseBody
  @PostMapping(path = "/", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity create(@Valid @RequestBody PersonDTO personDTO, UriComponentsBuilder b) {
    log.info("Creating person: {}", personDTO);

    PersonDTO savedPerson = personApiService.save(personDTO);

    if (savedPerson != null) {
      UriComponents uriComponents = b.path("/api/v1/person/{id}")
          .buildAndExpand(savedPerson.getId());

      return ResponseEntity.created(uriComponents.toUri()).build();
    } else {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<PersonDTO> findById(@PathVariable Long id) {
    log.info("Returning person by id: {}", id);

    Optional<PersonDTO> personDtoOpt = personApiService.findAndDto(id);

    return personDtoOpt
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<PersonDTO> update(@PathVariable Long id,
      @Valid @RequestBody PersonDTO personDTO) {
    log.info("Update id={}, dto={}", id, personDTO);

    Optional<Person> personOpt = personApiService.find(id);

    return personOpt
        .map(person -> ResponseEntity.ok(personApiService.update(person, personDTO)))
        .orElseGet(() -> onItemNotFound(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity delete(@PathVariable Long id) {
    if (personApiService.findAndDto(id).isEmpty()) {
      log.error("Id {} is not existed", id);

      ResponseEntity.notFound().build();
    }

    personApiService.delete(id);

    return ResponseEntity.ok().build();
  }
}
