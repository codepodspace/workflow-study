package code.pod.space.workspace.platform.workflow.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({WorkspaceJobProperties.class})
public class WorkspaceJobConfig {

    @Autowired
    private WorkspaceJobProperties workspaceProperties;

    public WorkspaceJobProperties getWorkspaceProperties() {
        return workspaceProperties;
    }
}
