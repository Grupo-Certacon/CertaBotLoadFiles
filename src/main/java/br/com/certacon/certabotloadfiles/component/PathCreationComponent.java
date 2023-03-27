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

    public PathCreationComponent( @Value("${config.rootPath}") String rootPath,LoadFilesRepository loadFilesRepository) {
        this.loadFilesRepository = loadFilesRepository;
        this.rootPath = rootPath;
    }

    public Boolean checkPath(UUID id) {
        Boolean isCreated = Boolean.FALSE;
        Optional<LoadFilesModel> model = loadFilesRepository.findById(id);
        try {
            if (model.isPresent()) {
                String server = model.get().getServerFolder().replaceAll("[^0-9]", "");
                String cnpj = model.get().getCnpjFolder().replaceAll("[^0-9]", "");
                String year = model.get().getYearFolder();
                Path serverPath = Paths.get(rootPath + server);
                Path cnpjPath = Paths.get(serverPath + "\\" + cnpj);
                Path yearPath = Paths.get(cnpjPath + "\\" + year);
                if (!serverPath.toFile().isDirectory() || !cnpjPath.toFile().isDirectory() || !yearPath.toFile().isDirectory()) {
                    yearPath.toFile().mkdirs();
                    isCreated = Boolean.TRUE;
                    return isCreated;
                }
            }
        }catch (RuntimeException e){
            model.get().setStatus(StatusFile.ERROR);
        }finally {
            return isCreated;
        }
    }
}
