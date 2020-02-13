package com.michmzr.gimmeback.loan;

import com.michmzr.gimmeback.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface LoanRepository extends JpaRepository<Loan, Long> {
    abstract List<Loan> findAllByAuthor(User author);

    abstract Optional<Loan> findByIdAndAuthor(Long id, User user);

    abstract void deleteByIdAndAuthor(Long id, User user);
}
