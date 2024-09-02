package hexlet.code.dto.tasks;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;
import java.util.Set;


@Getter
@Setter
public class TaskDTO {

    private long id;

    private JsonNullable<Integer> index;

    @JsonProperty("title")
    private String name;

    @JsonProperty("content")
    private JsonNullable<String> description;

    private String status;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    private Set<Long> taskLabelIds;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
}
