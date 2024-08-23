package hexlet.code.controller;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import org.instancio.Instancio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import java.nio.charset.StandardCharsets;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
class UsersControllerTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ModelGenerator modelGenerator;

    private User testUser;

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();
        testUser = Instancio.of(modelGenerator.getUserModel()).create();
    }
    @AfterEach
    public void clear() {
        userRepository.deleteAll();
    }


    @Test
    public void testIndex() throws Exception {
        userRepository.save(testUser);

        var result = mockMvc.perform(get("/api/users").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testIndexWithoutAuth() throws Exception {
        userRepository.save(testUser);

        var result = mockMvc.perform(get("/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testShow() throws Exception {
        userRepository.save(testUser);

        var request = get("/api/users/{id}", testUser.getId()).with(jwt());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("firstName").isEqualTo(testUser.getFirstName()),
                v -> v.node("lastName").isEqualTo(testUser.getLastName()),
                v -> v.node("email").isEqualTo(testUser.getEmail())
        );
    }

    @Test
    public void testShowWithoutAuth() throws Exception {
        userRepository.save(testUser);

        var request = get("/users/{id}", testUser.getId());

        var result = mockMvc.perform(request)
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testCreate() throws Exception {

        var request = post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(testUser));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var user = userRepository.findByEmail(testUser.getEmail()).get();

        assertThat(user).isNotNull();
        assertThat(user.getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(user.getLastName()).isEqualTo(testUser.getLastName());
        assertThat(user.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(user.getPasswordDigest()).isNotEqualTo(testUser.getPasswordDigest());
    }

    @Test
    public  void testCreateUserWithNotValidEmail() throws Exception {
        testUser.setEmail("Not Email Type");
        UserCreateDTO dto = userMapper.mapToCreateDTO(testUser);

        MockHttpServletRequestBuilder request = post("/api/users").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateUserWithNotValidSizeEmail() throws Exception {
        userRepository.save(testUser);

        testUser.setEmail("a");
        var dto = userMapper.mapToCreateDTO(testUser);

        var request = MockMvcRequestBuilders.post("/api/users")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUser() throws Exception {
        userRepository.save(testUser);

        testUser.setFirstName("Sasha");
        testUser.setLastName("Blabla");
        testUser.setEmail("blabla@bla.ru");
        testUser.setPasswordDigest("password");

        var dto = userMapper.maptoUpdateDTO(testUser);

        MockHttpServletRequestBuilder request = put("/api/users/{id}", testUser.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        User user = userRepository.findById(testUser.getId()).get();

        assertThat(user.getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(user.getLastName()).isEqualTo(testUser.getLastName());
        assertThat(user.getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    public void testUpdateUserPartial() throws Exception {
        userRepository.save(testUser);

        testUser.setEmail("email@email.com");

        var dto = userMapper.maptoUpdateDTO(testUser);

        var request = MockMvcRequestBuilders.put("/api/users/{id}", testUser.getId())
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        User user = userRepository.findById(testUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + testUser.getEmail()));

        assertThat(user.getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(user.getLastName()).isEqualTo(testUser.getLastName());
        assertThat(user.getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    public void testUpdateUserWithNotValidEmail() throws Exception {
        userRepository.save(testUser);

        testUser.setEmail("Not Email Type");

        var dto = userMapper.maptoUpdateDTO(testUser);

        var request = MockMvcRequestBuilders.put("/api/users/{id}", testUser.getId())
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void delete() throws Exception {
        userRepository.save(testUser);

        var request = MockMvcRequestBuilders.delete("/api/users/{id}", testUser.getId()).with(jwt());

        mockMvc.perform(request).andExpect(status().isNoContent());

        assertThat(userRepository.existsById(testUser.getId())).isFalse();
    }
}
