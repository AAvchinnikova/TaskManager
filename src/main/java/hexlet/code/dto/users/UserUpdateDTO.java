package hexlet.code.dto.users;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Setter
@Getter
public class UserUpdateDTO {

    private JsonNullable<String> firstName;

    private JsonNullable<String> lastName;

    private String email;

    private String password;
}
