package hexlet.code.dto.taskStatuses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskStatusCreateDTO {

    @NotNull
    @Size(min = 1)
    private String name;

    @Size(min = 1)
    private String slug;
}
