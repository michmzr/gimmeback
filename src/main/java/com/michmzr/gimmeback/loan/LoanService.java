package com.michmzr.gimmeback.loan;

import com.michmzr.gimmeback.item.Item;
import com.michmzr.gimmeback.item.ItemMapper;
import com.michmzr.gimmeback.security.SpringSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final SpringSecurityService springSecurityService;
    private final LoanMapper loanMapper;
    @Autowired
    ItemMapper itemMapper;

    @Autowired
    public LoanService(LoanRepository loanRepository, SpringSecurityService springSecurityService, LoanMapper loanMapper) {
        this.loanRepository = loanRepository;
        this.springSecurityService = springSecurityService;
        this.loanMapper = loanMapper;
    }

    List<Loan> findAll() {
        return loanRepository.findAll();
    }

    Optional<Loan> find(Long id) {
        return loanRepository.findByIdAndAuthor(
                id,
                springSecurityService.getCurrentUser()
        );
    }

    Loan save(LoanDTO loanDTO) {
        Loan loan = loanMapper.fromDTO(loanDTO);

        //todo itemDTO:

        loan.setAuthor(
                springSecurityService.getCurrentUser()
        );

        return loanRepository.save(loan);
    }

    Loan update(Loan loan, LoanDTO loanDTO) {//todo testy
        loan.setName(loanDTO.getName());
        loan.setDescription(loanDTO.getDescription());
        loan.setPerson(loanDTO.getPerson());
        loan.setDirection(loanDTO.getDirection());

        Set<Item> items = loanDTO.getItems().stream().map(itemMapper::fromDTO).collect(Collectors.toSet());
        loan.setItems(items);

        loan.setHappended(loanDTO.getHappended());
        loan.setReturned(loanDTO.getReturned());
        loan.setCommisionDate(loanDTO.getCommisionDate());

        return loanRepository.save(loan);
    }

    void delete(Long id) {
        loanRepository.deleteByIdAndAuthor(
                id,
                springSecurityService.getCurrentUser()
        );
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

            return loanRepository.save(loan) != null;
        } else {
            return false;
        }
    }
}
