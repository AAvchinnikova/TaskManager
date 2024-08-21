package hexlet.code.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserUpdateDTO {

    @NotBlank
    private String firstName;

    @NotBlank
    private String LastName;

    @Email
    private String email;

    @Size(min = 3, max = 100)
    @NotNull
    private String passwordDigest;
}
