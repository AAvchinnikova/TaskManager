package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter
public class UserDTO {

    private long id;

    private String firstName;

    private String LastName;

    private String email;

    private LocalDate createdAt;
}