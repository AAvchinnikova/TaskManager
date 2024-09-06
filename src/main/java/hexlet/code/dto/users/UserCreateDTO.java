package hexlet.code.dto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserCreateDTO {

    private String firstName;

    private String lastName;

    @Email(message = "Wrong email format")
    @NotBlank(message = "Email is requried")
    private String email;

    private String password;
}
