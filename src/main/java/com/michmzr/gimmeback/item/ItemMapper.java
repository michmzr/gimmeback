package com.michmzr.gimmeback.item;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemDTO toDTO(Item item);

    Item fromDTO(ItemDTO itemDTO);
}
