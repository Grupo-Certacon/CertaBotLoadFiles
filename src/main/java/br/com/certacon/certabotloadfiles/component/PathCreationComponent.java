package br.com.certacon.certabotloadfiles.component;

import br.com.certacon.certabotloadfiles.helper.PathCreationHelper;
import br.com.certacon.certabotloadfiles.model.LoadFilesModel;
import br.com.certacon.certabotloadfiles.repository.LoadFilesRepository;
import br.com.certacon.certabotloadfiles.utils.FileFunctions;
import br.com.certacon.certabotloadfiles.utils.StatusFile;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

@Component
public class PathCreationComponent {


    private final LoadFilesRepository loadFilesRepository;
    private final PathCreationHelper helper;

    public PathCreationComponent(LoadFilesRepository loadFilesRepository, PathCreationHelper helper) {
        this.loadFilesRepository = loadFilesRepository;
        this.helper = helper;
    }

    public LoadFilesModel createLoadPath(UUID id) {
        Optional<LoadFilesModel> model = loadFilesRepository.findById(id);
        LoadFilesModel result;
        result = model.get();
        if (model.isPresent()) {
            Path loadPath = helper.directoryCreator(result, FileFunctions.CARREGAMENTO);

            result.setPath(loadPath.toString());
            result.setStatus(StatusFile.CREATEDPATH);
        } else {
            result.setStatus(StatusFile.ERROR);
        }
        loadFilesRepository.save(result);
        return result;
    }

    public LoadFilesModel createSentPath(UUID id, FileFunctions function) {

        Optional<LoadFilesModel> model = loadFilesRepository.findById(id);
        LoadFilesModel result;
        result = model.get();
        if (model.isPresent()) {
            Path sentPath = helper.directoryCreatorWithObrigacoesAcessorias(result, function);
            result.setPath(sentPath.toString());
            result.setStatus(StatusFile.CREATEDSENT);
        } else {
            result.setStatus(StatusFile.ERROR);
        }
        loadFilesRepository.save(result);
        return result;
    }

    public LoadFilesModel createArchivedPath(UUID id, FileFunctions function) {
        Optional<LoadFilesModel> model = loadFilesRepository.findById(id);

        LoadFilesModel result;
        result = model.get();
        if (model.isPresent()) {
            Path sentPath = helper.directoryCreatorWithObrigacoesAcessorias(result, function);
            result.setPath(sentPath.toString());
            result.setStatus(StatusFile.CREATEDARCHIVED);
        } else {
            result.setStatus(StatusFile.ERROR);
        }
        loadFilesRepository.save(result);
        return result;
    }

    public LoadFilesModel createOrganizePath(UUID id, FileFunctions function) {
        Optional<LoadFilesModel> model = loadFilesRepository.findById(id);

        LoadFilesModel result;
        result = model.get();
        if (model.isPresent()) {
            Path sentPath = helper.directoryCreator(result, function);
            result.setPath(sentPath.toString());
            result.setStatus(StatusFile.CREATEDORGANIZE);
        } else {
            result.setStatus(StatusFile.ERROR);
        }
        loadFilesRepository.save(result);
        return result;
    }

    public LoadFilesModel createOrganizedPath(UUID id, FileFunctions function) {
        Optional<LoadFilesModel> model = loadFilesRepository.findById(id);

        LoadFilesModel result;
        result = model.get();
        if (model.isPresent()) {
            Path sentPath = helper.directoryCreatorWithObrigacoesAcessorias(result, function);
            result.setPath(sentPath.toString());
            result.setStatus(StatusFile.CREATEDORGANIZED);
        } else {
            result.setStatus(StatusFile.ERROR);
        }
        loadFilesRepository.save(result);
        return result;
    }
}
