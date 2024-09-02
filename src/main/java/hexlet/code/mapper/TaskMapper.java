package hexlet.code.mapper;

import hexlet.code.dto.tasks.TaskCreateDTO;
import hexlet.code.dto.tasks.TaskDTO;
import hexlet.code.dto.tasks.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "statusSlugToModel")
    @Mapping(target = "labels", source = "taskLabelIds", qualifiedByName = "labelIdsToModel")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "status", source = "taskStatus.slug")
    @Mapping(target = "taskLabelIds", source = "labels", qualifiedByName = "modelToLabelIds")
    public abstract TaskDTO map(Task task);

    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "statusSlugToModel")
    @Mapping(target = "labels", source = "taskLabelIds", qualifiedByName = "labelIdsToModel")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task task);

    @Mapping(target = "status", source = "taskStatus.slug")
    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "taskLabelIds", source = "labels", qualifiedByName = "modelToLabelIds")
    public abstract TaskCreateDTO mapToCreateDTO(Task task);

    @Mapping(target = "taskLabelIds", source = "labels", qualifiedByName = "modelToLabelIds")
    public abstract TaskUpdateDTO maptoUpdateDTO(Task task);

    @Named("statusSlugToModel")
    public TaskStatus statusSlugToModel(String slug) {
        return taskStatusRepository.findBySlug(slug)
                .orElseThrow();
    }

    @Named("labelIdsToModel")
    public Set<Label> labelIdsToModel(Set<Long> labelIds) {
        if (labelIds == null || labelIds.isEmpty()) {
            throw new ResourceNotFoundException("Label's ids are null or empty");
        }
        return new HashSet<>(labelRepository.findByIdIn(labelIds));
    }

    @Named("modelToLabelIds")
    public Set<Long> modelToLabelIds(Set<Label> labels) {
        if (labels == null || labels.isEmpty()) {
            throw new ResourceNotFoundException("Labels are null or empty");
        }
        return labels.stream().map(Label::getId).collect(Collectors.toSet());
    }
}
