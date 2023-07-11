package br.com.certacon.certabotloadfiles.kafka.consumer;

import br.com.certacon.certabotloadfiles.model.ConsumerOffsetOrganizerProcess;
import br.com.certacon.certabotloadfiles.model.LoadFilesModel;
import br.com.certacon.certabotloadfiles.model.UserFilesModel;
import br.com.certacon.certabotloadfiles.repository.ConsumerOffsetOrganizerProcessRepository;
import br.com.certacon.certabotloadfiles.repository.LoadFilesRepository;
import br.com.certacon.certabotloadfiles.repository.UserFilesRepository;
import br.com.certacon.certabotloadfiles.schedule.PathCreationSchedule;
import br.com.certacon.certabotloadfiles.schedule.PostRestTemplateSchedule;
import br.com.certacon.certabotloadfiles.service.LoadFilesService;
import br.com.certacon.certabotloadfiles.utils.Processado;
import br.com.certacon.certabotloadfiles.utils.StatusFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

@Service
@Slf4j
@EnableKafka
public class KafkaConsumer {

    private final LoadFilesService loadFilesService;
    private final PathCreationSchedule pathCreationSchedule;
    private final PostRestTemplateSchedule postRestTemplateSchedule;
    private final UserFilesRepository userFilesRepository;
    private final LoadFilesRepository loadFilesRepository;
    private final ConsumerOffsetOrganizerProcessRepository consumerOffsetOrganizerProcessRepository;

    public KafkaConsumer(LoadFilesService loadFilesService, PathCreationSchedule pathCreationSchedule, PostRestTemplateSchedule postRestTemplateSchedule, UserFilesRepository userFilesRepository, LoadFilesRepository loadFilesRepository, ConsumerOffsetOrganizerProcessRepository consumerOffsetOrganizerProcessRepository) {

        this.loadFilesService = loadFilesService;
        this.pathCreationSchedule = pathCreationSchedule;
        this.postRestTemplateSchedule = postRestTemplateSchedule;
        this.userFilesRepository = userFilesRepository;
        this.loadFilesRepository = loadFilesRepository;

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

            String caminho = divisao[1].replace("\"arquivo\":\"{ \\\"caminho\\\": \\\"", "")
                    .replace("\\\"", "")
                    .replace(" ", "");

            String cnpj = divisao[2].replace("\\\"cnpj\\\": \\\"", "")
                    .replace("\\\"", "")
                    .replace(" ", "");

            String nome = divisao[3].replace("\\\"nome\\\": \\\"", "")
                    .replace("\\\"", "")
                    .replaceFirst(" ", "");

            String ano = divisao[5].replace("\\\"ano\\\": \\\"", "")
                    .replace("\\\"", "")
                    .replace(" ", "");

            String server = divisao[6].replace("\\\"servidor\\\": \\\"", "")
                    .replace("\\\"}\"}", "")
                    .replace(" ", "");

            LoadFilesModel model = LoadFilesModel.builder()
                    .companyName(nome)
                    .serverFolder(server)
                    .yearFolder(ano)
                    .cnpjFolder(cnpj)
                    .build();
            LoadFilesModel result = loadFilesService.create(model);
            pathCreationSchedule.pathCreate();
            Path source = Path.of(caminho);
            Path pathForCopy = Path.of(loadFilesRepository.findById(result.getId()).get().getPath() + File.separator + source.getFileName());
            Files.copy(source, pathForCopy);

            File caminhoFile = new File(caminho);
            UserFilesModel filesModel = UserFilesModel.builder()
                    .companyName(nome)
                    .path(pathForCopy.getParent().toString())
                    .ipServer(server)
                    .createdAt(new Date())
                    .year(ano)
                    .fileName(caminhoFile.getParentFile().getName())
                    .cnpj(cnpj)
                    .originalName(caminhoFile.getName())
                    .status(StatusFile.CREATED)
                    .build();
            userFilesRepository.save(filesModel);

            postRestTemplateSchedule.postRest();

            acknowledgment.acknowledge();
        } else {
            System.out.println("A Mensagem de Processamento da Lista com o ID: " +
                    consumerRecord.offset() + " já foi processado anteriormente! A mesma não será reprocessada!");
        }
    }

}

