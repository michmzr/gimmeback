package com.michmzr.gimmeback.loan;

import com.michmzr.gimmeback.ApiService;
import com.michmzr.gimmeback.item.Item;
import com.michmzr.gimmeback.item.ItemMapper;
import com.michmzr.gimmeback.security.SpringSecurityService;
import com.michmzr.gimmeback.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LoanApiService extends ApiService {
    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;
    private final ItemMapper itemMapper;

    public LoanApiService(LoanRepository loanRepository, SpringSecurityService springSecurityService,
                       LoanMapper loanMapper, ItemMapper itemMapper) {
        super(springSecurityService);
        this.loanRepository = loanRepository;
        this.loanMapper = loanMapper;
        this.itemMapper = itemMapper;
    }

    Optional<Loan> find(Long id) {
        return findByIdAndAuthor(id, getCurrentUser());
    }

    private Optional<Loan> findByIdAndAuthor(Long id, User user) {
        return loanRepository.findByIdAndAuthor(id, user);
    }

    private List<Loan> findAll() {
        return loanRepository.findAll();
    }

    List<Loan> findAllByAuthor() {
        return loanRepository
            .findAllByAuthor(getCurrentUser());
    }

    Loan save(LoanDTO loanDTO) {
        log.info("Saving loan dto: {}", loanDTO);

        Loan loan = loanMapper.fromDTO(loanDTO);
        loan.setAuthor(springSecurityService.getCurrentUser());

        return loanRepository.save(loan);
    }

    Boolean update(Long id, LoanDTO loanDTO) {
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
