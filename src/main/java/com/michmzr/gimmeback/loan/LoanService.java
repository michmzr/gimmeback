package com.michmzr.gimmeback.loan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    @Autowired
    LoanRepository loanRepository;

    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    public Optional<Loan> find(Long id) {
        return loanRepository.findById(id);
    }

    public Loan save(Loan item) {
        return loanRepository.save(item);
    }

    public void delete(Long id) {
        loanRepository.deleteById(id);
    }

}
