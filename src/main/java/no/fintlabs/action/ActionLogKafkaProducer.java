package no.fintlabs.action;

import no.fintlabs.kafka.entity.EntityProducer;
import no.fintlabs.kafka.entity.EntityProducerFactory;
import no.fintlabs.kafka.entity.EntityProducerRecord;
import no.fintlabs.kafka.entity.topic.EntityTopicNameParameters;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ActionLogKafkaProducer {

    private final EntityProducer<ActionLog> actionLogEntityProducer;
    private final EntityTopicNameParameters topicNameParameters;

    public ActionLogKafkaProducer(EntityProducerFactory entityProducerFactory) {
        actionLogEntityProducer = entityProducerFactory.createProducer(ActionLog.class);
        topicNameParameters = EntityTopicNameParameters.builder()
                .resource("actionlog")
                .build();
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
