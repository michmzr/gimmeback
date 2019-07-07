package com.michmzr.gimmeback.loan;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.michmzr.gimmeback.core.audit.Auditable;
import com.michmzr.gimmeback.item.Item;
import com.michmzr.gimmeback.person.Person;
import lombok.Data;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Loan extends Auditable<Loan> implements Serializable {
    @Id
    @Getter
    @GeneratedValue
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
    private Set<Item> items = new HashSet<>(0);

    /** Data pożyczenia*/
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate happended;

    /** Data oddania*/
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate commisionDate;

    @JsonCreator
    public Loan() {
    }
}
