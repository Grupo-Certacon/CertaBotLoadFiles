package br.com.certacon.certabotloadfiles.component;


import br.com.certacon.certabotloadfiles.model.UserFilesModel;
import br.com.certacon.certabotloadfiles.repository.UserFilesRepository;
import br.com.certacon.certabotloadfiles.utils.StatusFile;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class CreateFileComponent {
    private final UnzipAndZipComponent unzipAndZipComponent;
    private final UserFilesRepository userFilesRepository;
    @Value("${config.destDir}")
    private String destDir;

    public CreateFileComponent(UnzipAndZipComponent unzipAndZipComponent, UserFilesRepository userFilesRepository) {
        this.unzipAndZipComponent = unzipAndZipComponent;
        this.userFilesRepository = userFilesRepository;
    }

    public Boolean checkFile(String path, String cnpj, String ipServer) {
        Boolean isCreated = Boolean.FALSE;
        Path fullPath = Paths.get(path);
        try {
            File[] listFile = new File(fullPath.toString()).listFiles();
            for (File value : listFile) {
                Optional<UserFilesModel> filePath = userFilesRepository.findByFileName(value.getName());
                if (!filePath.isPresent()) {
                    if (FilenameUtils.getExtension(value.getName()).equals("zip")) {
                        Path pathForSave = value.toPath();
                        String mimeType = Files.probeContentType(pathForSave);
                        String ext = FilenameUtils.getExtension(pathForSave.toString());
                        UserFilesModel userFilesModelSaved = UserFilesModel.builder()
                                .cnpj(cnpj)
                                .ipServer(ipServer)
                                .mimeType(mimeType)
                                .fileName(value.getName())
                                .path(value.getPath())
                                .extension(ext)
                                .status(StatusFile.CREATED)
                                .createdAt(new Date())
                                .build();
                        isCreated = Boolean.TRUE;
                        userFilesRepository.save(userFilesModelSaved);
                        UserFilesModel finalModel = unzipAndZipComponent.UnzipAndZipFiles(userFilesModelSaved);
                        userFilesRepository.delete(userFilesModelSaved);
                        userFilesRepository.save(finalModel);
                    } else if (FilenameUtils.getExtension(value.getName()).equals("txt")) {
                        List<UserFilesModel> txtList = userFilesRepository.findAllByExtension("txt");
                        try {
                            String zipPath = value.getPath().replace(".txt", ".zip");
                            Path pathForSave = Path.of(zipPath);
                            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipPath));
                            FileInputStream fileIn = new FileInputStream(value);
                            ZipEntry zipEntry = new ZipEntry(value.getName());
                            zipOut.putNextEntry(zipEntry);
                            byte[] bytes = new byte[1024];
                            int length;
                            while ((length = fileIn.read(bytes)) >= 0) {
                                zipOut.write(bytes, 0, length);
                            }
                            fileIn.close();
                            zipOut.close();
                            FileSystemUtils.deleteRecursively(value);
                            String mimeType = Files.probeContentType(pathForSave);
                            String ext = FilenameUtils.getExtension(pathForSave.toString());
                            UserFilesModel userFilesModelSaved = UserFilesModel.builder()
                                    .cnpj(cnpj)
                                    .ipServer(ipServer)
                                    .mimeType(mimeType)
                                    .fileName(pathForSave.getFileName().toString())
                                    .path(zipPath)
                                    .extension(ext)
                                    .status(StatusFile.CREATED)
                                    .createdAt(new Date())
                                    .build();
                            isCreated = Boolean.TRUE;
                            userFilesRepository.save(userFilesModelSaved);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Path pathForSave = value.toPath();
                        String mimeType = Files.probeContentType(pathForSave);
                        String ext = FilenameUtils.getExtension(pathForSave.toString());
                        UserFilesModel userFilesModelSaved = UserFilesModel.builder()
                                .cnpj(cnpj)
                                .ipServer(ipServer)
                                .mimeType(mimeType)
                                .fileName(value.getName())
                                .path(value.getPath())
                                .extension(ext)
                                .status(StatusFile.CREATED)
                                .createdAt(new Date())
                                .build();
                        isCreated = Boolean.TRUE;
                        userFilesRepository.save(userFilesModelSaved);
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return isCreated;
    }
}