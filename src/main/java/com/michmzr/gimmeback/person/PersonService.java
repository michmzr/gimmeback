package com.michmzr.gimmeback.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    @Autowired
    PersonRepository personRepository;

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Optional<Person> find(Long id) {
        return personRepository.findById(id);
    }

    public Person save(Person item) {
        return personRepository.save(item);
    }

    public void delete(Long id) {
        personRepository.deleteById(id);
    }
}
