package code.pod.space.workspace.platform.workflow.worker.create;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;

import code.pod.space.workspace.platform.workflow.entity.types.Workspace;
import code.pod.space.workspace.platform.workflow.util.SelfUtil;
import code.pod.space.workspace.platform.workflow.util.WorkflowUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 申请创建工人
 */
@Component
@Slf4j
public class ApplyWorker {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private WorkflowUtil workflowUtil;

    private static final String candidateGroup = "ApplyCreateWorker";

    private final static String processKey = "apple";

    private static final Object LOCKER = new Object();

    private final String workerName = SelfUtil.getName(candidateGroup);

    private final String workerId = SelfUtil.getName();

    private static final String taskKey = "ApplyWorkspace";

    public void applyCreateWorker() {

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

                taskService.complete(task.getId());

                log.info("创建工作空间>>完成>>{}>>工人:{}, 任务ID:{}, Workspace ID: {}",
                        task.getName(), workerName, task.getId(), workspaceId);

            } catch (Exception ex) {
                log.error("创建工作空间>>错误>>{}>>工人:{}, 任务ID:{}", task.getName(), task.getId(), ex);
            }
        }
    }

    public void create(Workspace workspace) {
        String businessKey = workspace.getUuid();

        synchronized (LOCKER) {

            Map<String, Object> vars = new HashMap<>();
            vars.put("workspace", JSON.toJSONString(workspace));

            // 该实例的所有任务需要在这个工人进程上执行，防止分布式情况下争抢任务
            vars.put("assignee", workerId);

            ProcessInstance inst = runtimeService.startProcessInstanceByKey(processKey, businessKey, vars);

            log.info("创建工作空间>>创建实例>>工人:{}, 实例ID:{}, 实例Key:{}, Workspace ID:{}",
                    workerName, inst.getId(), processKey, businessKey);
        }
    }

}
