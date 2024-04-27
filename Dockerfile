FROM openjdk:17

# terraform init
COPY bin/terraform /usr/bin/terraform
RUN chmod +x /usr/bin/terraform

RUN mkdir -p /opt/terraform
COPY bin/terraform_providers.tar.gz /tmp/terraform_providers.tar.gz
RUN tar -C /opt/terraform -xf /tmp/terraform_providers.tar.gz

WORKDIR /code
COPY target/workspace-workflow.jar /code/workspace-workflow.jar
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo 'Asia/Shanghai' > /etc/timezone


ENTRYPOINT ["java", "-XX:MetaspaceSize=128m", "-XX:MaxMetaspaceSize=512m", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-Dfile.encoding=utf-8", "-jar", "workspace-workflow.jar"]