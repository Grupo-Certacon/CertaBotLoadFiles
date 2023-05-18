package br.com.certacon.certabotloadfiles.helper;

import br.com.certacon.certabotloadfiles.model.LoadFilesModel;
import br.com.certacon.certabotloadfiles.utils.FileFunctions;
import br.com.certacon.certabotloadfiles.utils.FileType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;

@Component
public class PathCreationHelper {
    @Value("${config.rootDir}")
    private final String rootDir;

    public PathCreationHelper(@Value("${config.rootDir}") String rootDir) {
        this.rootDir = rootDir;
    }

    public Path directoryCreator(LoadFilesModel modelToCreate, FileFunctions function) {
        Path finalPath = Path.of(rootDir + function
                + File.separator + modelToCreate.getServerFolder().replaceAll("[^0-9]", "")
                + File.separator + modelToCreate.getCnpjFolder().replaceAll("[^0-9]", "")
                + File.separator + modelToCreate.getYearFolder());
        if (!finalPath.toFile().exists()) finalPath.toFile().mkdirs();
        return finalPath;
    }

    public Path directoryCreatorWithObrigacoesAcessorias(LoadFilesModel modelToCreate, FileFunctions function) {
        Path finalPath = Path.of(rootDir + function
                + File.separator + modelToCreate.getServerFolder().replaceAll("[^0-9]", "")
                + File.separator + modelToCreate.getCnpjFolder().replaceAll("[^0-9]", "")
                + File.separator + modelToCreate.getYearFolder());
        for (int i = 0; i < FileType.values().length; i++) {
            Path newPath = Path.of(finalPath + File.separator + FileType.values()[i]);
            newPath.toFile().mkdirs();
        }
        return finalPath;
    }
}
