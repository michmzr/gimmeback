package com.michmzr.gimmeback.person;

import com.michmzr.gimmeback.security.SpringSecurityService;
import com.michmzr.gimmeback.user.User;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PersonApiService {

  private final PersonRepository personRepository;
  private final SpringSecurityService springSecurityService;
  private final PersonMapper personMapper;

  @Autowired
  PersonApiService(PersonRepository personRepository,
      SpringSecurityService springSecurityService, PersonMapper personMapper) {
    this.personRepository = personRepository;
    this.springSecurityService = springSecurityService;
    this.personMapper = personMapper;
  }

  List<PersonDTO> findAllByAuthor() {
    log.info("Find all by author: {}", getCurrentUser());
    List<Person> allByAuthor = personRepository.findAllByAuthor(
        getCurrentUser()
    );

    log.debug("Returning {}...", allByAuthor);

    return allByAuthor
        .stream()
        .map(personMapper::toDTO)
        .collect(Collectors.toUnmodifiableList());
  }

  private User getCurrentUser() {
    return springSecurityService.getCurrentUser();
  }

  Optional<PersonDTO> findAndDto(Long id) {
    Optional<Person> personOption = find(id);

    return personOption.map(personMapper::toDTO);
  }

  Optional<Person> find(Long id) {
    return personRepository.findByIdAndAuthor(
        id,
        getCurrentUser()
    );
  }

  PersonDTO save(PersonDTO personDTO) {
    log.info("Creating new Person {}", personDTO);

    Person person = personMapper.fromDTO(personDTO);
    person.setAuthor(getCurrentUser());

    person = personRepository.save(person);

    log.info("Created person: {}", person);
    return personMapper.toDTO(person);
  }

  PersonDTO update(Person person, PersonDTO personDTO) {
    person.setName(personDTO.getName());
    person.setSurname(personDTO.getSurname());
    person.setEmail(personDTO.getEmail());
    person.setPhone(personDTO.getPhone());

    log.info("Updating person:{} with dto: {}", person, personDTO);

    return personMapper.toDTO(personRepository.save(person));
  }

  public void delete(Long id) {
    personRepository.deleteByIdAndAuthor(
        id,
        getCurrentUser()
    );
  }
}
