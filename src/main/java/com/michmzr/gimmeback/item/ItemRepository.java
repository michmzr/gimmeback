package com.michmzr.gimmeback.item;

import com.michmzr.gimmeback.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository  extends JpaRepository<Item, Long> {
    abstract List<Item> findAllByAuthor(User author);

    abstract Optional<Item> findByIdAndAuthor(Long id, User user);

    abstract void deleteByIdAndAuthor(Long id, User user);
}
