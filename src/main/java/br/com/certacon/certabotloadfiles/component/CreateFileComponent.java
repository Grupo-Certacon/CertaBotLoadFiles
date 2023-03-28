package br.com.certacon.certabotloadfiles.component;

import br.com.certacon.certabotloadfiles.model.UserFilesModel;
import br.com.certacon.certabotloadfiles.repository.UserFilesRepository;
import br.com.certacon.certabotloadfiles.utils.StatusFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

@Component
public class CreateFileComponent {
    private final UserFilesRepository userFilesRepository;
    @Value("${config.rootPath}")
    private String rootPath;


    public CreateFileComponent(@Value("${config.rootPath}") String rootPath, UserFilesRepository userFilesRepository) {
        this.userFilesRepository = userFilesRepository;
        this.rootPath = rootPath;
    }

    public Boolean checkFile(String path, String fileName) {
        Boolean isCreated = Boolean.FALSE;
        UserFilesModel result;
        try {
            Path fullPath = Paths.get(rootPath + path + "\\");
            File[] file = new File(fullPath.toUri()).listFiles();
            for (int i = 0; i < file.length; i++) {
                Optional<UserFilesModel> filePath = userFilesRepository.findByFileNameAndPath(path, fileName);
                String fullPathFile = rootPath + path + fileName;
                if (!Paths.get(fullPathFile).toFile().exists()) {
                    result = UserFilesModel.builder()
                            .fileName(file[i].getName())
                            .path(file[i].getPath())
                            .status(StatusFile.OK)
                            .createdAt(new Date())
                            .build();
                    isCreated = Boolean.TRUE;
                    userFilesRepository.save(result);
                }
            }

        } catch (RuntimeException e) {
            throw new RuntimeException("Objeto não encontrado!");
        }
        return isCreated;
    }
}
