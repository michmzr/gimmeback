package com.michmzr.gimmeback.loan;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/loan/")
public class LoanController {
    LoanRepository loanRepository;
    LoanService loanService;

    public LoanController(LoanRepository loanRepository, LoanService loanService) {
        this.loanRepository = loanRepository;
        this.loanService = loanService;
    }

    @GetMapping
    public ResponseEntity<List<Loan>> findAll() {
        return ResponseEntity.ok(loanService.findAll());
    }

    @PostMapping("/create")
    public ResponseEntity create(@Valid @RequestBody LoanDTO loanDTO) {
        return ResponseEntity.ok(loanService.save(loanDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> findById(@PathVariable Long id) {
        Optional<Loan> loan = loanService.find(id);

        if (!loan.isPresent()) {
            //todo return NO_CONTENT
            ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(loan.get());
    }

    @GetMapping("/{id}/resolve")
    public ResponseEntity<Boolean> resolve(@PathVariable Long id) {
        Optional<Loan> loanOpt = loanService.find(id);

        //todo is belong to user
        if (!loanOpt.isPresent()) {
            //todo return NO_CONTENT
            ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(loanService.resolve(loanOpt.get()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Loan> update(@PathVariable Long id, @Valid @RequestBody LoanDTO loanDTO) {
        Optional<Loan> loanOptional = loanService.find(id);

        if (!loanOptional.isPresent()) {
            //todo return NO_CONTENT
            ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(loanService.update(loanOptional.get(), loanDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        Optional<Loan> loanOpt = loanService.find(id);

        if (!loanOpt.isPresent()) {
            //todo return NO_CONTENT
            ResponseEntity.badRequest().build();
        }

        loanService.delete(loanOpt.get());

        return ResponseEntity.ok().build();
    }
}
