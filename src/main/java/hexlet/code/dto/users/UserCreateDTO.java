package hexlet.code.dto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Password id requried")
    @Size(min = 3, message = "Minimal password length is about 3 symbols")
    private String password;
}
