package hexlet.code.dto.users;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Setter
@Getter
public class UserUpdateDTO {

    private JsonNullable<String> firstName;

    private JsonNullable<String> lastName;

    @Email(message = "Wrong email format")
    @NotBlank(message = "Email is requried")
    private JsonNullable<String> email;

    @NotBlank(message = "Password id requried")
    @Size(min = 3, message = "Minimal password length is about 3 symbols")
    private JsonNullable<String> password;
}
