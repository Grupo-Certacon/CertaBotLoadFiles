package br.com.certacon.certabotloadfiles.schedule;

import br.com.certacon.certabotloadfiles.component.PathCreationComponent;
import br.com.certacon.certabotloadfiles.model.LoadFilesModel;
import br.com.certacon.certabotloadfiles.repository.LoadFilesRepository;
import br.com.certacon.certabotloadfiles.utils.StatusFile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PathCreationSchedule {
    private final PathCreationComponent pathCreationComponent;
    private final LoadFilesRepository loadFilesRepository;

    public PathCreationSchedule(PathCreationComponent pathCreationComponent, LoadFilesRepository loadFilesRepository) {
        this.pathCreationComponent = pathCreationComponent;
        this.loadFilesRepository = loadFilesRepository;
    }

    @Scheduled(fixedRate = 30000)
    public boolean pathCreate() {
        List<LoadFilesModel> modelList = loadFilesRepository.findAll();
        Boolean created = Boolean.FALSE;
        if (!modelList.isEmpty()) {
            for (int i = 0; i < modelList.size(); i++) {
                if (modelList.get(i).getStatus() == StatusFile.CREATED || modelList.get(i).getStatus() == StatusFile.UPDATED) {
                    created = pathCreationComponent.checkPath(modelList.get(i).getId());
                }
            }
        }
        return created;
    }
}
