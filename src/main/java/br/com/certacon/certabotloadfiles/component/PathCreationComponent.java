package br.com.certacon.certabotloadfiles.component;

import br.com.certacon.certabotloadfiles.model.LoadFilesModel;
import br.com.certacon.certabotloadfiles.repository.LoadFilesRepository;
import br.com.certacon.certabotloadfiles.utils.StatusFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Component
public class PathCreationComponent {
    private final LoadFilesRepository loadFilesRepository;
    @Value("${config.rootPath}")
    private String rootPath;

    public PathCreationComponent(@Value("${config.rootPath}") String rootPath, LoadFilesRepository loadFilesRepository) {
        this.loadFilesRepository = loadFilesRepository;
        this.rootPath = rootPath;
    }

    public Boolean checkPath(UUID id) {
        Boolean isCreated = Boolean.FALSE;
        Optional<LoadFilesModel> model = loadFilesRepository.findById(id);
        LoadFilesModel result = new LoadFilesModel();
        try {
            result = model.get();
            if (model.isPresent()) {
                String server = result.getServerFolder().replaceAll("[^0-9]", "");
                String cnpj = result.getCnpjFolder().replaceAll("[^0-9]", "");
                String year = result.getYearFolder();
                Path serverPath = Paths.get(rootPath + server);
                Path cnpjPath = Paths.get(serverPath + "\\" + cnpj);
                Path yearPath = Paths.get(cnpjPath + "\\" + year);
                if (!serverPath.toFile().isDirectory() || !cnpjPath.toFile().isDirectory() || !yearPath.toFile().isDirectory()) {
                    yearPath.toFile().mkdirs();
                    result.setPath(yearPath.toString());
                    result.setStatus(StatusFile.CREATED);
                    isCreated = Boolean.TRUE;
                } else {
                    result.setPath(yearPath.toString());
                }
            }
        } catch (RuntimeException e) {
            result.setStatus(StatusFile.ERROR);
            isCreated = Boolean.FALSE;
        }
        loadFilesRepository.save(result);
        return isCreated;
    }
}
