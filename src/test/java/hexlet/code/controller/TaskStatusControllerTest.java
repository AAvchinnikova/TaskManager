package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.taskStatuses.TaskStatusUpdateDTO;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import java.nio.charset.StandardCharsets;


@SpringBootTest
@AutoConfigureMockMvc
public class TaskStatusControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    @Autowired
    private ModelGenerator modelGenerator;

    private TaskStatus testTaskStatus;
    private String url = "/api/task_statuses";
    private String urlWitId = "/api/task_statuses/{id}";

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();
        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
    }

    @AfterEach
    public void clear() {
        taskStatusRepository.deleteAll();
    }

    @Test
    public void testIndex() throws Exception {
        taskStatusRepository.save(testTaskStatus);
        var result = mockMvc.perform(get(url).with(jwt()))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        taskStatusRepository.save(testTaskStatus);
        var request = get(urlWitId, testTaskStatus.getId()).with(jwt());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testTaskStatus.getName()),
                v -> v.node("slug").isEqualTo(testTaskStatus.getSlug())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var testStatusCreate = taskStatusMapper.mapToCreateDTO(testTaskStatus);

        var request = post(url).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testStatusCreate));
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();
        var taskStatus = taskStatusRepository.findBySlug(testTaskStatus.getSlug());

        assertThat(taskStatus).isNotNull();
        assertThat(taskStatus.getName()).isEqualTo(testTaskStatus.getName());
        assertThat(taskStatus.getSlug()).isEqualTo(testTaskStatus.getSlug());
    }

    @Test
    public void testUpdate() throws Exception {
        taskStatusRepository.save(testTaskStatus);

        testTaskStatus.setName("Hi");

        TaskStatusUpdateDTO testStatusUpdate = taskStatusMapper.maptoUpdateDTO(testTaskStatus);

        var request = put(urlWitId, testTaskStatus.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testStatusUpdate));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var result = taskStatusRepository.findById(testTaskStatus.getId()).get();

        assertThat(result.getName()).isEqualTo("Hi");
    }

    @Test
    public void delete() throws Exception {
        taskStatusRepository.save(testTaskStatus);

        var request = MockMvcRequestBuilders.delete(urlWitId, testTaskStatus.getId()).with(jwt());

        mockMvc.perform(request).andExpect(status().isNoContent());

        assertThat(taskStatusRepository.existsById(testTaskStatus.getId())).isFalse();
    }

}
