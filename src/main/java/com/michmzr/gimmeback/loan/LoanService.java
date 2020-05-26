package com.michmzr.gimmeback.loan;

import com.michmzr.gimmeback.item.Item;
import com.michmzr.gimmeback.item.ItemMapper;
import com.michmzr.gimmeback.security.SpringSecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    public LoanService(LoanRepository loanRepository, LoanMapper loanMapper, ItemMapper itemMapper,
                       SpringSecurityService springSecurityService) {
        this.loanRepository = loanRepository;
        this.springSecurityService = springSecurityService;
        this.loanMapper = loanMapper;
        this.itemMapper = itemMapper;
    }

    List<Loan> findAll() {
        return loanRepository.findAll();
    }

    Optional<Loan> find(Long id) {
        return loanRepository.findByIdAndAuthor(id, springSecurityService.getCurrentUser());
    }

    Loan save(LoanDTO loanDTO) {
        log.info("Saving loan dto: {}", loanDTO);

        Loan loan = loanMapper.fromDTO(loanDTO);
        loan.setAuthor(springSecurityService.getCurrentUser());

        return loanRepository.save(loan);
    }

    Loan update(Loan loan, LoanDTO loanDTO) {
        loan.setName(loanDTO.getName());
        loan.setDescription(loanDTO.getDescription());
        loan.setPerson(loanDTO.getPerson());
        loan.setDirection(loanDTO.getDirection());

        Set<Item> items = loanDTO.getItems()
                .stream()
                .map(itemMapper::fromDTO).collect(Collectors.toSet());
        loan.setItems(items);

        loan.setHappended(loanDTO.getHappended());
        loan.setReturned(loanDTO.getReturned());
        loan.setCommisionDate(loanDTO.getCommisionDate());

        return loanRepository.save(loan);
    }

    void delete(Loan loan) {
        delete(loan.getId());
    }

    void delete(Long id) {
        loanRepository.deleteByIdAndAuthor(id, springSecurityService.getCurrentUser());
    }

    /**
     * Aktualizuje status pożyczenia na zwrócony
     *
     * @param id Loan id
     * @return true - operacja poszla pomyslnie, false- blad wykonania
     */
    Boolean resolve(Loan loan) {
        loan.resolve();
        return loanRepository.save(loan) != null;
    }
}
