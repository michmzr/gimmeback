package com.michmzr.gimmeback.loan;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.michmzr.gimmeback.item.Item;
import com.michmzr.gimmeback.model.audit.Auditable;
import com.michmzr.gimmeback.person.Person;
import com.michmzr.gimmeback.user.User;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@ToString
@Entity
public class Loan extends Auditable<Loan> implements Serializable {
    @Id
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

    /** Rzeczywista data oddania */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate returned;

    /** Ustalona data oddania*/
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate commisionDate;

    @NotNull
    @ManyToOne
    protected User author;

    @JsonCreator
    public Loan() {
    }

    void resolve() {
        returned =  LocalDate.now();
    }
}
