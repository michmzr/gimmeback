package com.michmzr.gimmeback.user;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue
    private long id;

    @NotEmpty
    @Size(max = 30)
    private String name;

    @NotEmpty
    @Size(max = 30)
    @Column(unique = true)
    private String surname;


    @NotEmpty
    @Column(unique = true)
    @Size(min = 5, max = 50)
    private String username;

    @Email
    @Size(max = 100)
    private String email;

    @NotNull
    private String password;

}
