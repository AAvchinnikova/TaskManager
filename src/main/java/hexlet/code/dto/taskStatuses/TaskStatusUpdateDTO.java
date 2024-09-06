package hexlet.code.dto.taskStatuses;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class TaskStatusUpdateDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 1, message = "Minimal name length is about 1 symbol")
    @Column(unique = true)
    private JsonNullable<String> name;

    @NotBlank(message = "Slug is required")
    @Size(min = 1, message = "Minimal slug length is about 1 symbol")
    @Column(unique = true)
    private JsonNullable<String> slug;

}
