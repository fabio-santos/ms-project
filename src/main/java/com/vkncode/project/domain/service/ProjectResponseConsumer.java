package com.vkncode.project.domain.service;

import com.vkncode.project.config.RabbitConfig;
import com.vkncode.project.domain.entity.Project;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProjectResponseConsumer {

    private ProjectService service;
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitConfig.PROJECT_RESPONSE_QUEUE)
    private void update(@Payload Project projectMessage) {
        Project project = service.findById(projectMessage.getId());

        project.setState(projectMessage.getState());
        service.update(project);
    }
}
