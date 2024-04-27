package code.pod.space.workspace.platform.workflow.controller;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import code.pod.space.workspace.platform.workflow.entity.types.WorkflowDeployment;
import code.pod.space.workspace.platform.workflow.entity.types.WorkflowHistoryBpmn;
import code.pod.space.workspace.platform.workflow.entity.types.Workspace;
import code.pod.space.workspace.platform.workflow.worker.create.AllocateResWorker;
import code.pod.space.workspace.platform.workflow.worker.create.ApplyWorker;
import code.pod.space.workspace.platform.workflow.worker.create.ScriptWorker;

@RestController
@RequestMapping("workflow")
public class WorkflowController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ApplyWorker applyWorker;

    @Autowired
    private AllocateResWorker allocateResWorker;

    @Autowired
    private ScriptWorker scriptWorker;

    @GetMapping("query/deploy/list")
    public List<WorkflowDeployment> queryDeployList() {

        List<Deployment> list = repositoryService.createDeploymentQuery().orderByDeploymentId().asc().list();

        List<WorkflowDeployment> rets = new ArrayList<>();
        list.forEach(e -> {

            WorkflowDeployment deploy = new WorkflowDeployment();
            deploy.setId(e.getId());
            deploy.setName(e.getName());
            deploy.setDeploymenTime(e.getDeploymentTime());

            rets.add(deploy);
        });

        return rets;
    }

    @GetMapping("query/history/bpmn")
    public WorkflowHistoryBpmn queryHistoryBpmn(
            @RequestParam("businessKey") String businessKey) {

        String processKey = "apple";

        ProcessInstance inst = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(processKey)
                .processInstanceBusinessKey(businessKey).singleResult();

        HistoricProcessInstance hisInst = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(processKey)
                .processInstanceBusinessKey(businessKey).singleResult();

        String instId = inst != null ? inst.getId() : hisInst.getId();
        String defId = inst != null ? inst.getProcessDefinitionId() : hisInst.getProcessDefinitionId();

        List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instId)
                .orderByHistoricActivityInstanceStartTime().asc().list();

        ProcessDefinition def = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(defId).singleResult();

        String deployId = def.getDeploymentId();
        String resourceName = def.getResourceName();

        String xml = "";
        try (InputStream bpmnStream = repositoryService.getResourceAsStream(deployId, resourceName)) {
            byte[] buf = IoUtil.readBytes(bpmnStream);
            xml = new String(buf, StandardCharsets.UTF_8);
        } catch (Exception ex) {
        }

        List<String> activeIds = historicActivityInstances.stream()
                .filter(e -> !e.getActivityType().equalsIgnoreCase("subProcess"))
                .map(HistoricActivityInstance::getActivityId).collect(Collectors.toList());

        WorkflowHistoryBpmn historyBpmn = new WorkflowHistoryBpmn();
        historyBpmn.setXml(xml);
        historyBpmn.setActiveIds(activeIds);

        return historyBpmn;
    }

    @GetMapping("deploy")
    public WorkflowDeployment deploy() {

        try (InputStream bpmnStream = ResourceUtil.getStream("bpmn/apple.bpmn")) {
            Deployment deploy = repositoryService.createDeployment()
                    .name("apple")
                    .addInputStream("apple.bpmn", bpmnStream)
                    .deploy();

            WorkflowDeployment wd = new WorkflowDeployment();
            wd.setName(deploy.getName());
            wd.setId(deploy.getId());
            wd.setDeploymenTime(deploy.getDeploymentTime());

            return wd;
        } catch (Exception ex) {
            throw new RuntimeException("遇到异常", ex);
        }
    }

    @GetMapping("apply/workspace")
    public String applyWorkspace() {

        Workspace ws = new Workspace();
        ws.setId(0L);
        ws.setUuid(UUID.randomUUID().toString());
        ws.setCommandStatus("INIT");

        applyWorker.create(ws);
        applyWorker.applyCreateWorker();
        return ws.getUuid();
    }

    @GetMapping("allocate/res")
    public String allocateRes() {
        allocateResWorker.allocateResWorker();
        return "allocate resource success!";
    }

    @GetMapping("cancel")
    public String cancel() {
        
        runtimeService.signalEventReceived("signal_cancle");

        return "cancel success!";
    }
    
    // @GetMapping("build/script")
    // public String buildScript() {
    //     scriptWorker;
    //     return ws.getUuid();
    // }
}
