package com.michmzr.gimmeback.loan;

import com.michmzr.gimmeback.item.Item;
import com.michmzr.gimmeback.item.ItemMapper;
import com.michmzr.gimmeback.security.SpringSecurityService;
import com.michmzr.gimmeback.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final SpringSecurityService springSecurityService;
    private final LoanMapper loanMapper;
    private final ItemMapper itemMapper;

    public LoanService(LoanRepository loanRepository, SpringSecurityService springSecurityService,
                       LoanMapper loanMapper, ItemMapper itemMapper) {
        this.loanRepository = loanRepository;
        this.springSecurityService = springSecurityService;
        this.loanMapper = loanMapper;
        this.itemMapper = itemMapper;
    }

    Optional<Loan> find(Long id) {
        return findByIdAndAuthor(id, springSecurityService.getCurrentUser());
    }

    private Optional<Loan> findByIdAndAuthor(Long id, User user) {
        return loanRepository.findByIdAndAuthor(id, user);
    }

    List<LoanDTO> findAll() {
        return loanRepository.findAll()
                .stream()
                .map(loanMapper::toDTO)
                .collect(Collectors.toList());
    }

    Loan save(LoanDTO loanDTO) {
        log.info("Saving loan dto: {}", loanDTO);

        Loan loan = loanMapper.fromDTO(loanDTO);
        loan.setAuthor(springSecurityService.getCurrentUser());

        return loanRepository.save(loan);
    }

    Boolean update(Long id, LoanDTO loanDTO) {//todo testy
        log.info("Update loan dto loan: {}", loanDTO);

        Optional<Loan> loanOpt = findByIdAndAuthor(id, springSecurityService.getCurrentUser());

        if (loanOpt.isPresent()) {
            Loan loan = loanOpt.get();

            log.info("Updating loan: {} with loan dto: {}", loan, loanDTO);

            loan.setName(loanDTO.getName());
            loan.setDescription(loanDTO.getDescription());
            loan.setPerson(loanDTO.getPerson());
            loan.setDirection(loanDTO.getDirection());

            Set<Item> items = loanDTO.getItems().stream().map(itemMapper::fromDTO).collect(Collectors.toSet());
            loan.setItems(items);

            loan.setHappended(loanDTO.getHappended());
            loan.setReturned(loanDTO.getReturned());
            loan.setCommisionDate(loanDTO.getCommisionDate());

            return loanRepository.save(loan) != null;
        } else {
            log.debug("Not found loan for id: {} and author:{}", id, springSecurityService.getCurrentUser());
            return false;
        }
    }

    void delete(long id) throws InvalidParameterException {
        log.info("Deleting loan id: " + id);

        Optional<Loan> loanOpt = findByIdAndAuthor(id, springSecurityService.getCurrentUser());

        if (loanOpt.isPresent()) {
            delete(loanOpt.get());
        } else {
            throw new InvalidParameterException("Loan with id " + id + " not found");
        }
    }

    private void delete(Loan loan) {
        log.info("Deleting {}", loan);

        loanRepository.deleteByIdAndAuthor(
                loan.getId(),
                springSecurityService.getCurrentUser()
        );
    }

    Boolean resolve(Loan loan) {
        log.info("Resolving loan {}", loan);

        loan.resolve();

        return loanRepository.save(loan) != null;
    }
}
