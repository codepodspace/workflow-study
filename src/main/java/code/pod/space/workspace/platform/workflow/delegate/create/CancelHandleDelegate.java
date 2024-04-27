package code.pod.space.workspace.platform.workflow.delegate.create;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import code.pod.space.workspace.platform.workflow.util.WorkflowUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 取消处理
 */
@Slf4j
public class CancelHandleDelegate implements JavaDelegate {

    @Autowired
    private WorkflowUtil workflowUtil;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String businessKey = execution.getBusinessKey();
        String processInstanceId = execution.getProcessInstanceId();

        log.info("创建工作空间>>取消服务>>开始处理>>InstanceId: {}, WorkspaceId: {}",
                processInstanceId, businessKey);
    }

}
