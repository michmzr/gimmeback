package com.michmzr.gimmeback.person;

import com.michmzr.gimmeback.security.SpringSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final SpringSecurityService springSecurityService;
    private final PersonMapper personMapper;

    @Autowired
    public PersonService(PersonRepository personRepository, SpringSecurityService springSecurityService, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.springSecurityService = springSecurityService;
        this.personMapper = personMapper;
    }

    public List<Person> findAll() {
        return personRepository.findAllByAuthor(
                springSecurityService.getCurrentUser()
        );
    }

    public Optional<Person> find(Long id) {
        return personRepository.findByIdAndAuthor(
                id,
                springSecurityService.getCurrentUser()
        );
    }

    public Person save(PersonDTO personDTO) {
        Person person = personMapper.fromDTO(personDTO);
        person.setAuthor(
                springSecurityService.getCurrentUser()
        );

        return personRepository.save(person);
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
