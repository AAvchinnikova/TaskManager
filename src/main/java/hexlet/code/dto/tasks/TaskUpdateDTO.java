package hexlet.code.dto.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;
import java.util.Set;

@Getter
@Setter
public class TaskUpdateDTO {

    @JsonProperty("assignee_id")
    private JsonNullable<Long> assigneeId;

    @NotBlank(message = "Name is required")
    @Size(min = 1, message = "Minimal name length is about 1 symbol")
    @JsonProperty("title")
    private JsonNullable<String> name;

    @JsonProperty("content")
    private JsonNullable<String> description;

    private JsonNullable<String> status;

    private JsonNullable<Set<Long>> taskLabelIds;
}
