package br.com.certacon.certabotloadfiles.kafka.consumer;

import br.com.certacon.certabotloadfiles.model.ConsumerOffsetOrganizerProcess;
import br.com.certacon.certabotloadfiles.repository.ConsumerOffsetOrganizerProcessRepository;
import br.com.certacon.certabotloadfiles.utils.Processado;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@EnableKafka
public class KafkaConsumer {

    private final ConsumerOffsetOrganizerProcessRepository consumerOffsetOrganizerProcessRepository;

    public KafkaConsumer(ConsumerOffsetOrganizerProcessRepository consumerOffsetOrganizerProcessRepository) {

        this.consumerOffsetOrganizerProcessRepository = consumerOffsetOrganizerProcessRepository;
    }

    @KafkaListener(topics = "${topic.uploadcertabot}")

    public void receiveOrganizer(ConsumerRecord consumerRecord, Acknowledgment acknowledgment) throws IOException {

        ConsumerOffsetOrganizerProcess obj = consumerOffsetOrganizerProcessRepository
                .findByIdTopicoAndParticao(String.valueOf(consumerRecord.offset()), String.valueOf(consumerRecord.partition()));
        if (obj == null) {

            obj = new ConsumerOffsetOrganizerProcess();

            obj.setParticao(String.valueOf(consumerRecord.partition()));
            obj.setIdTopico(String.valueOf(consumerRecord.offset()));
            obj.setMensagem(consumerRecord.value().toString());
            obj.setStatus(Processado.FILA);
            consumerOffsetOrganizerProcessRepository.save(obj);

            String[] divisao = consumerRecord.value().toString().split(",");
            String idUsuario = divisao[0].replace("{\"idUsuario\":", "").replace("\"", "");
            String caminho = divisao[1].replace("\"arquivo\":\"{ \\\"caminho\\\": \\\"", "")
                    .replace("\\\"", "");
            String cnpj = divisao[2].replace("\\\"cnpj\\\": \\\"", "").replace("\\\"", "");
            String nome = divisao[3].replace("\\\"nome\\\": \\\"", "").replace("\\\"", "");
            String ano = divisao[5].replace("\\\"ano\\\": \\\"", "").replace("\"}", "");
            String server = divisao[6].replace("\\\"servidor\\\":", "").replace("\"}", "");
            log.info(consumerRecord.value().toString());
            log.info(caminho);
            log.info(idUsuario);
            log.info(cnpj);
            log.info(nome);
            log.info(ano);
            log.info(server);
            acknowledgment.acknowledge();
        } else {
            System.out.println("A Mensagem de Processamento da Lista com o ID: " +
                    consumerRecord.offset() + " já foi processado anteriormente! A mesma não será reprocessada!");
        }
    }

}

