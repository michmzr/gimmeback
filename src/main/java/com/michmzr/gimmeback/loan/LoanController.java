package com.michmzr.gimmeback.loan;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;

@Slf4j
@RestController
@RequestMapping("/api/v1/loan/")
public class LoanController {
  private final LoanApiService loanApiService;
  private final LoanMapper loanMapper;

  public LoanController(LoanApiService loanApiService, LoanMapper loanMapper) {
    this.loanApiService = loanApiService;
    this.loanMapper = loanMapper;
  }

  @GetMapping("/")
  public ResponseEntity<List<LoanDTO>> list() {
    List<LoanDTO> dtos =
        loanApiService.findAllByAuthor().stream()
            .map(loanMapper::toDTO)
            .collect(Collectors.toList());

    return ResponseEntity.ok(dtos);
  }

  @PostMapping("/create")
  public ResponseEntity create(@Valid @RequestBody LoanDTO loanDTO) {
    log.info("Creating loan={}", loanDTO);

    LoanDTO savedItem = loanApiService.save(loanDTO);

    if (savedItem != null) {
      UriComponents uriComponents = b.path("/api/v1/loan/{id}").buildAndExpand(savedItem.getId());

      return ResponseEntity.created(uriComponents.toUri()).build();
    } else {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<LoanDTO> findById(@PathVariable Long id) {
    log.info("findById id: " + id);

    Optional<Loan> loan = loanApiService.find(id);

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

    Optional<Loan> loanOptional = loanApiService.find(id);

    if (loanOptional.isPresent()) {
      Boolean resolve = loanApiService.resolve(loanOptional.get());
      return ResponseEntity.ok(resolve);
    } else {
      return ResponseEntity.badRequest().build(); // todo potrzebne return???
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Boolean> update(
      @PathVariable Long id, @Valid @RequestBody LoanDTO loanDTO) {
    log.info("Update id: {}, loanDTO: {}", id, loanDTO);

    Optional<Loan> loanOptional = loanApiService.find(id);

    if (loanOptional.isPresent()) {
      log.info("Updating {} with  {}", loanOptional.get(), loanDTO);

      return ResponseEntity.ok(loanApiService.update(id, loanDTO));
    } else {
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity delete(@PathVariable Long id) {
    if (!loanApiService.find(id).isPresent()) {
      log.error("Id " + id + " not exists");

      ResponseEntity.badRequest().build();
    }

    loanApiService.delete(id);

    return ResponseEntity.ok().build();
  }
}
