package com.michmzr.gimmeback.loan;

import com.michmzr.gimmeback.item.ItemDTO;
import com.michmzr.gimmeback.person.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanDTO {
    @Nullable
    private Long id;

    @Size(max = 50)
    private String name;

    @Size(max = 2048)
    private String description;

    @NotNull
    @ManyToOne
    private Person person;

    @NotNull
    private LoadDirection direction;

    @OneToMany
    private Set<ItemDTO> items = new HashSet<>(0);

    /**
     * Data pożyczenia
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate happended;

    /**
     * Rzeczywista data oddania
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate returned;

    /**
     * Ustalona data oddania
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate commisionDate;
}
