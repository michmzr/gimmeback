package com.michmzr.gimmeback.person;

import com.michmzr.gimmeback.core.audit.Auditable;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * Osoba
 */
@Data
@Entity
public class Person  extends Auditable<String> {
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
}
