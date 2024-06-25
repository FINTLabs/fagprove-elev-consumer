package no.fintlabs.action;

import no.fintlabs.kafka.entity.EntityProducer;
import no.fintlabs.kafka.entity.EntityProducerFactory;
import no.fintlabs.kafka.entity.EntityProducerRecord;
import no.fintlabs.kafka.entity.topic.EntityTopicNameParameters;
import no.fintlabs.kafka.entity.topic.EntityTopicService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ActionLogKafkaProducer {

    private final static long RETENTION_TIME_IN_MILLIS = 86400000;

    private final EntityProducer<ActionLog> actionLogEntityProducer;
    private final EntityTopicNameParameters topicNameParameters;

    public ActionLogKafkaProducer(EntityProducerFactory entityProducerFactory, EntityTopicService entityTopicService) {
        actionLogEntityProducer = entityProducerFactory.createProducer(ActionLog.class);
        topicNameParameters = EntityTopicNameParameters.builder()
                .resource("actionlog")
                .build();
        entityTopicService.ensureTopic(topicNameParameters, RETENTION_TIME_IN_MILLIS);

    }

    public void publishAction(ActionLog actionLog) {
        actionLogEntityProducer.send(
                EntityProducerRecord.<ActionLog>builder()
                        .topicNameParameters(topicNameParameters)
                        .key(UUID.randomUUID().toString())
                        .value(actionLog)
                        .build()
        );

    }

}
