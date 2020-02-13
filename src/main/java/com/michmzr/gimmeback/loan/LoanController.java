package com.michmzr.gimmeback.loan;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/loan/")
public class LoanController {
    @Autowired
    LoanRepository loanRepository;

    @Autowired
    LoanService loanService;

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
            log.error("Id " + id + " is not existed");
            ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(loan.get());
    }

    @GetMapping("/{id}/resolve")
    public ResponseEntity<Boolean> resolve(@PathVariable Long id) {
        if (!loanService.find(id).isPresent()) {
            log.error("Id " + id + " is not existed");

            ResponseEntity.badRequest().build(); //todo potrzebne return???
        }

        Boolean resolve = loanService.resolve(id);

        return ResponseEntity.ok(resolve);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Loan> update(@PathVariable Long id, @Valid @RequestBody LoanDTO loanDTO) {
        Optional<Loan> loanOptional = loanService.find(id);

        if (!loanOptional.isPresent()) {
            log.error("Id " + id + " is not existed");
            ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                loanService.update(loanOptional.get(), loanDTO)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        if (!loanService.find(id).isPresent()) {
            log.error("Id " + id + " is not existed");

            ResponseEntity.badRequest().build();
        }

        loanService.delete(id);

        return ResponseEntity.ok().build();
    }
}
