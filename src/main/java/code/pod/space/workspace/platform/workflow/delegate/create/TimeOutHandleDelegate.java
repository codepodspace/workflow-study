package code.pod.space.workspace.platform.workflow.delegate.create;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.runtime.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import code.pod.space.workspace.platform.workflow.util.WorkflowUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 超时处理
 */
@Component
@Slf4j
public class TimeOutHandleDelegate implements JavaDelegate {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private WorkflowUtil workflowUtil;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String businessKey = execution.getBusinessKey();
        String processInstanceId = execution.getProcessInstanceId();
        log.info("创建工作空间>>超时服务>>开始处理>>ProcessInstanceId:{}, WorkspaceId:{}",
                processInstanceId, businessKey);
                
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(execution.getId()).list();
        List<String> activiteIds = new ArrayList<>();
        executions.forEach(exe -> {
            ExecutionEntity exeEntity = (ExecutionEntity) exe;
            String activeId = exeEntity.getActivityId();
            switch (activeId) {
                case "ApplyWorkspace" -> {

                }
                case "AllocateResource" -> {

                }
                case "BuildScript" -> {

                }
                case "ExecuteCreate" -> {

                }
            }
            activiteIds.add(activeId);
        });

        log.info("创建工作空间>>超时服务>>结束处理>>ProcessInstanceId:{}, WorkspaceId:{}, activeIds:{}",
                processInstanceId, businessKey, JSON.toJSONString(activiteIds));
    }

}
