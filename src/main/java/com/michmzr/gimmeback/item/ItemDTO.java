package com.michmzr.gimmeback.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    private Long id;

    @NotNull
    @NotEmpty
    private String name;

    private BigDecimal value;

    @NotNull
    private ItemType type;
}
