package br.com.certacon.certabotloadfiles.schedule;

import br.com.certacon.certabotloadfiles.component.CreateFileComponent;
import br.com.certacon.certabotloadfiles.model.LoadFilesModel;
import br.com.certacon.certabotloadfiles.repository.LoadFilesRepository;
import br.com.certacon.certabotloadfiles.utils.StatusFile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class CreateFileSchedule {
    private final CreateFileComponent createFileComponent;
    private final LoadFilesRepository loadFilesRepository;

    public CreateFileSchedule(CreateFileComponent createFileComponent, LoadFilesRepository loadFilesRepository) {
        this.createFileComponent = createFileComponent;
        this.loadFilesRepository = loadFilesRepository;
    }

    @Scheduled(fixedRate = 30000, initialDelay = 45000)
    public boolean createFileScheduled() throws IOException {
        List<LoadFilesModel> modelList = loadFilesRepository.findAll();
        Boolean check = Boolean.FALSE;
        if (!modelList.isEmpty()) {
            for (int i = 0; i < modelList.size(); i++) {
                if (modelList.get(i).getStatus() == StatusFile.CREATED || modelList.get(i).getStatus() == StatusFile.UPDATED) {
                    check = createFileComponent.checkFile(modelList.get(i).getPath(), modelList.get(i).getCnpjFolder(), modelList.get(i).getServerFolder());
                }
            }
        }
        return check;
    }
}
