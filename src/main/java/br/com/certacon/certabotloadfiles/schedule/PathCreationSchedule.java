package br.com.certacon.certabotloadfiles.schedule;

import br.com.certacon.certabotloadfiles.component.PathCreationComponent;
import br.com.certacon.certabotloadfiles.model.LoadFilesModel;
import br.com.certacon.certabotloadfiles.repository.LoadFilesRepository;
import br.com.certacon.certabotloadfiles.repository.UserFilesRepository;
import br.com.certacon.certabotloadfiles.utils.FileFunctions;
import br.com.certacon.certabotloadfiles.utils.StatusFile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PathCreationSchedule {

    private final PathCreationComponent pathCreationComponent;
    private final LoadFilesRepository loadFilesRepository;
    private final UserFilesRepository userFilesRepository;

    public PathCreationSchedule(PathCreationComponent pathCreationComponent, LoadFilesRepository loadFilesRepository,
                                UserFilesRepository userFilesRepository) {

        this.pathCreationComponent = pathCreationComponent;
        this.loadFilesRepository = loadFilesRepository;
        this.userFilesRepository = userFilesRepository;
    }

    public boolean pathCreate() {

        List<LoadFilesModel> modelList = loadFilesRepository.findAll();
        if (!modelList.isEmpty()) {
            modelList.forEach(loadFile -> {
                if (loadFile.getStatus() == StatusFile.CREATED || loadFile.getStatus() == StatusFile.UPDATED) {
                    loadFile = pathCreationComponent.createOrganizedPath(loadFile.getId(), FileFunctions.ORGANIZADOS);
                    if (loadFile.getStatus() == StatusFile.CREATEDORGANIZED) {
                        loadFile = pathCreationComponent.createSentPath(loadFile.getId(), FileFunctions.ENVIADOS);
                    }
                    if (loadFile.getStatus() == StatusFile.CREATEDSENT) {
                        loadFile = pathCreationComponent.createArchivedPath(loadFile.getId(), FileFunctions.ARQUIVADOS);
                    }
                    if (loadFile.getStatus() == StatusFile.CREATEDARCHIVED) {
                        loadFile = pathCreationComponent.createOrganizePath(loadFile.getId(), FileFunctions.ORGANIZAR);
                    }
                    if (loadFile.getStatus() == StatusFile.CREATEDORGANIZE) {
                        loadFile = pathCreationComponent.createLoadPath(loadFile.getId());
                        loadFilesRepository.save(loadFile);
                    }
                }
            });
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
