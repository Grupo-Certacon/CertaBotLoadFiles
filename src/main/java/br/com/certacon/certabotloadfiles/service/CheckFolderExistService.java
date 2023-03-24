package br.com.certacon.certabotloadfiles.service;

import br.com.certacon.certabotloadfiles.model.LoadFilesModel;
import br.com.certacon.certabotloadfiles.repository.LoadFilesRepository;
import org.springframework.stereotype.Service;

@Service
public class CheckFolderExistService {
    private final LoadFilesRepository loadFilesRepository;

    public CheckFolderExistService(LoadFilesRepository loadFilesRepository) {
        this.loadFilesRepository = loadFilesRepository;
    }

    public LoadFilesModel checkExistence(LoadFilesModel LoadFileModel) {

        return null;
    }
}
