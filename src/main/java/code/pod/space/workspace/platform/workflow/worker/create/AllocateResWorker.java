package code.pod.space.workspace.platform.workflow.worker.create;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import code.pod.space.workspace.platform.workflow.entity.types.Workspace;
import code.pod.space.workspace.platform.workflow.util.SelfUtil;
import code.pod.space.workspace.platform.workflow.util.WorkflowUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 分配资源工人
 */
@Component
@Slf4j
public class AllocateResWorker {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private WorkflowUtil workflowUtil;

    private static final String candidateGroup = "AllocateResWorker";

    private final static String processKey = "apple";

    private static final Object LOCKER = new Object();

    private final String workerName = SelfUtil.getName(candidateGroup);

    private final String workerId = SelfUtil.getName();

    private static final String taskKey = "AllocateResource";

    public void allocateResWorker() {

        synchronized (LOCKER) {

            Task task = workflowUtil.getTask(workerId, taskKey, candidateGroup, processKey);

            if (task == null) {
                return;
            }

            // 取到任务开始处理
            try {

                String exeId = task.getExecutionId();
                Workspace ws = workflowUtil.getWorkspaceVars(exeId);
                String workspaceId = ws != null ? ws.getUuid() : "";

                log.info("创建工作空间>>开始>>{}>>工人:{}, 任务ID:{}, Workspace ID: {}",
                        task.getName(), workerName, task.getId(), workspaceId);

                taskService.setVariable(task.getId(), "allocateSuccess", false);               

                taskService.complete(task.getId());
                log.info("创建工作空间>>完成>>{}>>工人:{}, 任务ID:{}, Workspace ID: {}",
                        task.getName(), workerName, task.getId(), workspaceId);

            } catch (Exception ex) {
                log.error("创建工作空间>>错误>>{}>>工人:{}, 任务ID:{}", task.getName(), task.getId(), ex);
            }
        }
    }
}
