package code.pod.space.workspace.platform.workflow.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "app.workspace.workflow")
@Data
public class WorkspaceJobProperties {
    
}
