package code.pod.space.workspace.platform.workflow.entity.types;

import java.util.Date;

import lombok.Data;

@Data
public class WorkflowDeployment {

    private String id;
    private String name;
    private Date deploymenTime;
}
