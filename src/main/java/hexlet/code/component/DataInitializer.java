package hexlet.code.component;

import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {
        var firstUser = new User();
        firstUser.setFirstName("First");
        firstUser.setLastName("User");
        firstUser.setEmail("hexlet@example.com");
        firstUser.setPasswordDigest(passwordEncoder.encode("qwerty"));
        userRepository.save(firstUser);
    }
}