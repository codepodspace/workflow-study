package code.pod.space.workspace.platform.workflow.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;

import cn.hutool.core.collection.CollectionUtil;
import code.pod.space.workspace.platform.workflow.entity.types.Workspace;

@Component
public class WorkflowUtil {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    public Task getTask(String workerId, String taskKey, String candidateGroup, String processKey) {
        Task task = null;
        // 先取自己的任务
        List<Task> selfList = taskService.createTaskQuery()
                .processDefinitionKey(processKey)
                .taskDefinitionKey(taskKey)
                .taskAssignee(workerId)
                .list();

        if (!CollectionUtil.isEmpty(selfList)) {
            task = selfList.get(0);
        } else {
            // 没有从候选组中取任务
            List<Task> taskList = taskService.createTaskQuery()
                    .taskCandidateGroup(candidateGroup)
                    .processDefinitionKey(processKey)
                    .list();
            if (!CollectionUtil.isEmpty(taskList)) {

                for (Task t : taskList) {
                    String assignee = (String) runtimeService.getVariable(t.getExecutionId(), "assignee");
                    // 实例变量有指定工人
                    if (StringUtils.isNotBlank(assignee) && workerId.equals(assignee)) {
                        taskService.claim(t.getId(), workerId);
                        task = t;
                        break;
                    }
                }
                if (task == null) {
                    task = taskList.get(0);
                    taskService.claim(task.getId(), workerId);
                }
            }
        }
        return task;
    }

    public Workspace getWorkspaceVars(String exeId) {
        try {
            String variable = (String) runtimeService.getVariable(exeId, "workspace");
            if (StringUtils.isNotBlank(variable)) {
                Workspace ws = JSON.parseObject(variable, Workspace.class);
                return ws;
            }
        } catch (Exception ignore) {
        }
        return null;
    }

    public Object getInstanceVars(String exeId, String varName) {
        try {
            return runtimeService.getVariable(exeId, varName);
        } catch (Exception ignore) {
        }
        return null;
    }
}
