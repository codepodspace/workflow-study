// package code.pod.space.workspace.platform.workflow.mutation;

// import java.io.InputStream;

// import org.camunda.bpm.engine.RepositoryService;
// import org.camunda.bpm.engine.repository.Deployment;
// import org.springframework.beans.factory.annotation.Autowired;

// import com.netflix.graphql.dgs.DgsComponent;
// import com.netflix.graphql.dgs.DgsMutation;
// import com.netflix.graphql.dgs.InputArgument;

// import cn.hutool.core.io.resource.ResourceUtil;
// import code.pod.space.workspace.platform.workflow.entity.types.WorkflowDeployment;
// import code.pod.space.workspace.platform.workflow.entity.types.WorkflowDeploymentInput;
// import graphql.schema.DataFetchingEnvironment;
// import reactor.core.publisher.Mono;
// import site.likewind.cloud.workspace.common.exception.BizCodeEnum;
// import site.likewind.cloud.workspace.common.exception.BizException;
// import site.likewind.cloud.workspace.common.graphql.utils.UserContext;

// @DgsComponent
// public class WorkflowMutation {

//     @Autowired
//     private RepositoryService repositoryService;

//     @DgsMutation(field = "deploy")
//     public Mono<WorkflowDeployment> create(@InputArgument WorkflowDeploymentInput wfdInput, DataFetchingEnvironment dfe) {
//         String userId = UserContext.getUserId(dfe);
//         String projectId = UserContext.getProjectId(dfe);
//         String accessKey = UserContext.getAccessKey(dfe);

//         if(!"Likewind@#2024".equals(accessKey)){
//             return Mono.error(new BizException(BizCodeEnum.A_07_AUTH_FAILED));
//         }
        
//         wfdInput.setUserId(userId);
//         wfdInput.setProjectId(projectId);

//         try (InputStream bpmnStream = ResourceUtil.getStream("bpmn/" + wfdInput.getName() + ".bpmn")) {
//             Deployment deploy = repositoryService.createDeployment()
//                                 .name("create_workspace")
//                                 .addInputStream("create_workspace.bpmn", bpmnStream)
//                                 .deploy();;

//             WorkflowDeployment wd = new WorkflowDeployment();
//             wd.setName(deploy.getName());
//             wd.setId(deploy.getId());
//             wd.setDeploymenTime(deploy.getDeploymentTime());

//             return Mono.just(wd);
//         } catch (Exception ex) {
//             return Mono.error(new BizException(BizCodeEnum.A_06_DEPLOY_FAILED, ex.getMessage()));
//         }
//     }
// }
