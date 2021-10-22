package com.michmzr.gimmeback.person;

import com.michmzr.gimmeback.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    abstract List<Person> findAllByAuthor(User author);

    abstract Optional<Person> findByIdAndAuthor(Long id, User author);

    abstract void deleteByIdAndAuthor(Long id, User author);
}
