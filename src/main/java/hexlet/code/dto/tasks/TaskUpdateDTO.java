package hexlet.code.dto.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;
import java.util.Set;

@Getter
@Setter
public class TaskUpdateDTO {

    @JsonProperty("title")
    private String name;

    @JsonProperty("content")
    private JsonNullable<String> description;

    private Set<Long> taskLabelIds;
}
