package hexlet.code.component;

import hexlet.code.dto.labels.LabelCreateDTO;
import hexlet.code.dto.task.statuses.TaskStatusCreateDTO;
import hexlet.code.dto.users.UserCreateDTO;
import hexlet.code.service.LabelService;
import hexlet.code.service.TaskStatusService;
import hexlet.code.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private UserService userService;

    private TaskStatusService taskStatusService;

    private LabelService labelService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var email = "hexlet@example.com";
        var userData = new UserCreateDTO();
        userData.setEmail(email);
        userData.setPassword("qwerty");
        userService.create(userData);

        var task = new TaskStatusCreateDTO();
        task.setName("Draft");
        task.setSlug("draft");
        taskStatusService.create(task);

        task.setName("ToReview");
        task.setSlug("to_review");
        taskStatusService.create(task);

        task.setName("ToBeFixed");
        task.setSlug("to_be_fixed");
        taskStatusService.create(task);

        task.setName("ToPublish");
        task.setSlug("to_publish");
        taskStatusService.create(task);

        task.setName("Published");
        task.setSlug("published");
        taskStatusService.create(task);

        var label = new LabelCreateDTO();
        label.setName("feature");
        labelService.create(label);

        label.setName("bug");
        labelService.create(label);
    }
}
