package com.michmzr.gimmeback.person;

import com.michmzr.gimmeback.security.SpringSecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PersonApiService {
    private final PersonRepository personRepository;
    private final SpringSecurityService springSecurityService;
    private final PersonMapper personMapper;

    @Autowired
    public PersonApiService(PersonRepository personRepository, SpringSecurityService springSecurityService, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.springSecurityService = springSecurityService;
        this.personMapper = personMapper;
    }

    public List<PersonDTO> findAll() {
        List<Person> allByAuthor = personRepository.findAllByAuthor(
                springSecurityService.getCurrentUser()
        );

        return allByAuthor.stream().map(personMapper::toDTO).collect(Collectors.toUnmodifiableList());
    }

    public Optional<Person> find(Long id) {
        return personRepository.findByIdAndAuthor(
                id,
                springSecurityService.getCurrentUser()
        );
    }

    public PersonDTO save(PersonDTO personDTO) {
        log.info("Creating new Person {}", personDTO);

        Person person = personMapper.fromDTO(personDTO);
        person.setAuthor(
                springSecurityService.getCurrentUser()
        );

        person = personRepository.save(person);
        log.info("Created person: {}", person);

        return personMapper.toDTO(person);
    }

    public Person update(Person person, PersonDTO personDTO) {
        person.setName(personDTO.getName());
        person.setSurname(personDTO.getSurname());
        person.setEmail(personDTO.getEmail());
        person.setPhone(personDTO.getPhone());

        return personRepository.save(person);
    }

    public void delete(Long id) {
        personRepository.deleteByIdAndAuthor(
                id,
                springSecurityService.getCurrentUser()
        );
    }
}
