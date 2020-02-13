package com.michmzr.gimmeback.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    @Nullable
    private Long id;

    @NotEmpty
    private String name;

    @Nullable
    private BigDecimal value;

    @NotNull
    private ItemType type;
}
