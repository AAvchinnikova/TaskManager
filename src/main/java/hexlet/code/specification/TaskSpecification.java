package hexlet.code.specification;

import hexlet.code.dto.tasks.TaskParamsDTO;
import hexlet.code.model.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSpecification {

    public Specification<Task> build(TaskParamsDTO params) {
        return withTitleCont(params.getTitleCont())
                .and(withAssigneeId(params.getAssigneeId()))
                .and(withStatus(params.getStatus()))
                .and(withLabelId(params.getLabelId()));
    }

    private Specification<Task> withTitleCont(String titleCont) {
        return ((root, qery, criteriaBulder)
        -> titleCont == null
                ? criteriaBulder.conjunction()
                : criteriaBulder.like(criteriaBulder.lower(root.get("name")),
                "%" + titleCont.toLowerCase() + "%"));
    }

    private Specification<Task> withAssigneeId(Long assigneeId) {
        return ((root, qery, criteriaBulder)
        -> assigneeId == null
                ? criteriaBulder.conjunction()
                : criteriaBulder.equal(root.get("assignee"), assigneeId));
    }

    private Specification<Task> withStatus(String status) {
        return ((root, qery, criteriaBulder)
        -> status == null
                ? criteriaBulder.conjunction()
                : criteriaBulder.equal(root.get("taskStatus").get("slug"), status));
    }

    private Specification<Task> withLabelId(Long labelId) {
        return ((root, qery, criteriaBulder)
                -> labelId == null
                ? criteriaBulder.conjunction()
                : criteriaBulder.equal(root.get("labels"), labelId));
    }
}
