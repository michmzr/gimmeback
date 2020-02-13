package com.michmzr.gimmeback.person;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    PersonDTO toDTO(Person person);

    Person fromDTO(PersonDTO personDTO);
}
