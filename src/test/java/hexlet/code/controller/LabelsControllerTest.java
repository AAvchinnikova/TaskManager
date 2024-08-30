package hexlet.code.controller;

import hexlet.code.dto.labels.LabelCreateDTO;
import hexlet.code.dto.labels.LabelUpdateDTO;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
class LabelsControllerTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private LabelMapper labelMapper;

    @Autowired
    private ModelGenerator modelGenerator;

    private User testUser;

    private Task testTask;

    private Label testLabel;

    private String url = "/api/labels";

    private String urlWitId = "/api/labels/{id}";

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();
        testUser = Instancio.of(modelGenerator.getUserModel()).create();
        testTask = Instancio.of(modelGenerator.getTaskModel()).create();
        testLabel = Instancio.of(modelGenerator.getLabelModel()).create();

    }
    @AfterEach
    public void clear() {

        userRepository.deleteAll();
        taskRepository.deleteAll();
        labelRepository.deleteAll();
    }

    @Test
    public void testIndex() throws Exception {
        labelRepository.save(testLabel);
        var result = mockMvc.perform(get(url).with(jwt()))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        labelRepository.save(testLabel);
        var request = get(urlWitId, testLabel.getId()).with(jwt());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testLabel.getName())
        );
    }

    @Test
    public void testCreate() throws Exception {
        LabelCreateDTO labelCreate = labelMapper.mapToCreateDTO(testLabel);

        var request = post(url).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(labelCreate));
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();
        var testLabel = labelRepository.findByName(labelCreate.getName()).get();

        assertThat(testLabel).isNotNull();
        assertThat(testLabel.getName()).isEqualTo(labelCreate.getName());
    }

    @Test
    public void testUpdate() throws Exception {
        labelRepository.save(testLabel);

        testLabel.setName("Hello");

        LabelUpdateDTO labelUpdate = labelMapper.maptoUpdateDTO(testLabel);

        var request = put(urlWitId, testLabel.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(labelUpdate));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var result = labelRepository.findById(testLabel.getId()).get();

        assertThat(result.getName()).isEqualTo("Hello");
    }

    @Test
    public void delete() throws Exception {
        labelRepository.save(testLabel);

        var request = MockMvcRequestBuilders.delete(urlWitId, testLabel.getId()).with(jwt());

        mockMvc.perform(request).andExpect(status().isNoContent());

        assertThat(labelRepository.existsById(testLabel.getId())).isFalse();
    }

}
