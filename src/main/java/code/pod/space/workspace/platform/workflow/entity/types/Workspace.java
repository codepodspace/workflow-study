package code.pod.space.workspace.platform.workflow.entity.types;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Workspace {
    private Long id;
    private String uuid;
    private String projectId;
    private String templateId;
    private String name;
    private String url;
    private String info;
    private String status;
    private String commandStatus;
    private String ownerId;
    private String category;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private Integer modifyVersion;
    private LocalDateTime updateTime;
}
