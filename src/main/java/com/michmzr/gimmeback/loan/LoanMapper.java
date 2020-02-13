package com.michmzr.gimmeback.loan;

import com.michmzr.gimmeback.item.ItemMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ItemMapper.class})
public interface LoanMapper {
    LoanDTO toDTO(Loan loan);

    Loan fromDTO(LoanDTO loanDTO);
}
