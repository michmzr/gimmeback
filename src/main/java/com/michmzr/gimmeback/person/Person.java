package com.michmzr.gimmeback.person;

import com.michmzr.gimmeback.model.audit.Auditable;
import com.michmzr.gimmeback.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Osoba
 */
@Entity
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Person extends Auditable<String> implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String surname;

    @Email
    private String email;

    private String phone;

    @NotNull
    @ManyToOne
    protected User author;

    public Person() {
    }
}

