package com.michmzr.gimmeback.person;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.michmzr.gimmeback.core.audit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * Osoba
 */
@Data
@Entity
@AllArgsConstructor
public class Person extends Auditable<String>  implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String surname;

    @Email
    private String email;

    private String phone;

    @JsonCreator
    public Person() {
    }
}