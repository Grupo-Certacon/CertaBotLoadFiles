package br.com.certacon.certabotloadfiles.component;

import br.com.certacon.certabotloadfiles.model.FileTypeModel;
import br.com.certacon.certabotloadfiles.model.LoadFilesModel;
import br.com.certacon.certabotloadfiles.repository.FileTypeRepository;
import br.com.certacon.certabotloadfiles.repository.LoadFilesRepository;
import br.com.certacon.certabotloadfiles.utils.StatusFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PathCreationComponent {

    private final FileTypeRepository fileTypeRepository;
    private final LoadFilesRepository loadFilesRepository;
    @Value("${config.rootPath}")
    private String rootPath;

    public PathCreationComponent(FileTypeRepository fileTypeRepository, @Value("${config.rootPath}") String rootPath, LoadFilesRepository loadFilesRepository) {
        this.fileTypeRepository = fileTypeRepository;
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
                if (!yearPath.toFile().exists()) {
                    yearPath.toFile().mkdirs();
                    List<FileTypeModel> files = fileTypeRepository.findAll();
                    for (int i = 0; i < files.size(); i++) {
                        Path filePath = Path.of(yearPath + "\\" + files.get(i).getFileType());
                        filePath.toFile().mkdirs();
                    }
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
