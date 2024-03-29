package com.michmzr.gimmeback.loan;

import com.michmzr.gimmeback.item.ItemMapper;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/v1/loan/")
public class LoanController {
    private final LoanService loanService;
    private final LoanMapper loanMapper;

    public LoanController(LoanService loanService, LoanMapper loanMapper) {
        this.loanService = loanService;
        this.loanMapper = loanMapper;
    }


    @GetMapping
    public List<LoanDTO> findAll() {
        return loanService.findAll();
    }

    @PostMapping("/create")
    public ResponseEntity create(@Valid @RequestBody LoanDTO loanDTO) {
        return ResponseEntity.ok(loanService.save(loanDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanDTO> findById(@PathVariable Long id) {
        log.info("findById id: " + id);

        Optional<Loan> loan = loanService.find(id);

        if (loan.isPresent()) {
            return ResponseEntity.ok(loan.map(loanMapper::toDTO).get());
        } else {
            log.error("Id " + id + " is not existed");
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/resolve")
    public ResponseEntity<Boolean> resolve(@PathVariable Long id) {
        log.info("Resolve id: {}", id);

        Optional<Loan> loanOptional = loanService.find(id);

        if (loanOptional.isPresent()) {
            Boolean resolve = loanService.resolve(loanOptional.get());
            return ResponseEntity.ok(resolve);
        } else {
            return ResponseEntity.badRequest().build(); //todo potrzebne return???
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> update(@PathVariable Long id, @Valid @RequestBody LoanDTO loanDTO) {
        log.info("Update id: {}, loanDTO: {}", id, loanDTO);

        Optional<Loan> loanOptional = loanService.find(id);

        if (loanOptional.isPresent()) {
            log.info("Updating {} with  {}", loanOptional.get(), loanDTO);

            return ResponseEntity.ok(loanService.update(id, loanDTO));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        if (!loanService.find(id).isPresent()) {
            log.error("Id " + id + " not exists");

            ResponseEntity.badRequest().build();
        }

        loanService.delete(id);

        return ResponseEntity.ok().build();
    }
}
