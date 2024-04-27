package code.pod.space.workspace.platform.workflow.entity.types;

import lombok.Data;

@Data
public class WorkflowDeploymentInput {
    private String name;
    private String userId;
    private String projectId;
}
