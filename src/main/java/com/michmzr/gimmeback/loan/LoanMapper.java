package com.michmzr.gimmeback.loan;

import com.michmzr.gimmeback.item.ItemMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ItemMapper.class})
public interface LoanMapper {
    LoanDTO toDTO(Loan loan);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    Loan fromDTO(LoanDTO loanDTO);
}
