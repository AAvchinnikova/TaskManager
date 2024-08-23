package hexlet.code.component;

import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Override
    public void run(ApplicationArguments args) {
        Optional<User> firstUser = userRepository.findByEmail("hexlet@example.com");
        if (firstUser.isEmpty()) {
            var email = "hexlet@example.com";
            var userData = new User();
            userData.setEmail(email);
            userData.setPasswordDigest(passwordEncoder.encode("qwerty"));
            userRepository.save(userData);
        }

        List<String> slugs = List.of("draft", "to_review", "to_be_fixed", "to_publish", "publish");
        slugs.stream().map(s -> {
            var status  = new TaskStatus();
            status.setName(s);
            status.setSlug(s);
            return status;
        }).forEach(taskStatusRepository::save);
    }
}
