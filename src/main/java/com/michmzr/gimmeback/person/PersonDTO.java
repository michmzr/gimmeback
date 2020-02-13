package com.michmzr.gimmeback.person;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class PersonDTO {
    @Nullable
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String surname;

    @Email
    private String email;

    @Nullable
    private String phone;

}
