package br.com.certacon.certabotloadfiles.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, ProducerMesssage> kafkaTemplate;
    //@Value("${topic.processorganizerinicio}")
    private String topicInicio;

    // @Value("${topic.processorganizertermino}")
    private String topicTermino;

    public void sendInicio(ProducerMesssage message) {

        this.kafkaTemplate.send(topicInicio, message);
        log.info("Published the message [{}] to the kafka queue: [{}]",
                message.getStatus(),
                message.getIdArquivo(),
                message.getIdLista(),
                topicInicio
        );
    }

    public void sendTermino(ProducerMesssage message) {

        this.kafkaTemplate.send(topicTermino, message);
        log.info("Published the message [{}] to the kafka queue: [{}]",
                message.getStatus(),
                message.getIdArquivo(),
                message.getIdLista(),
                topicTermino
        );
    }

}