package com.michmzr.gimmeback.loan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    @Autowired
    LoanRepository loanRepository;

    List<Loan> findAll() {
        return loanRepository.findAll();
    }

    Optional<Loan> find(Long id) {
        return loanRepository.findById(id);
    }

    Loan save(Loan item) {
        return loanRepository.save(item);
    }

    void delete(Long id) {
        loanRepository.deleteById(id);
    }

    /**
     * Aktualizuje status pożyczenia na zwrócony
     *
     * @param id Loan id
     * @return true - operacja poszla pomyslnie, false- blad wykonania
     */
    Boolean resolve(Long id) {
        Optional<Loan> loanOpt = find(id);

        if (loanOpt.isPresent()) {
            Loan loan = loanOpt.get();
            loan.resolve();

            return save(loan) != null;
        } else {
            return false;
        }
    }
}
