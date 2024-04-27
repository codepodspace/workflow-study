package code.pod.space.workspace.platform.workflow.entity.types;

import java.util.List;

import lombok.Data;

@Data
public class WorkflowHistoryBpmn {
    private String xml;
    private List<String> activeIds;
}
