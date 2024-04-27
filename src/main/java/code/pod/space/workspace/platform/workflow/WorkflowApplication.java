package code.pod.space.workspace.platform.workflow;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ComponentScan({ "code.pod" })
@SpringBootApplication
@EnableScheduling
@EnableProcessApplication
public class WorkflowApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(WorkflowApplication.class, args);
        log.info("\n------------------------Workspace Job Running!-------------------------\n\t");
    }
}
