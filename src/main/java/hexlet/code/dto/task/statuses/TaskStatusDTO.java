package hexlet.code.dto.task.statuses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskStatusDTO {

    private long id;

    private String name;

    private String slug;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
}