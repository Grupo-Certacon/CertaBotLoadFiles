package br.com.certacon.certabotloadfiles.component;


import br.com.certacon.certabotloadfiles.model.UserFilesModel;
import br.com.certacon.certabotloadfiles.repository.UserFilesRepository;
import br.com.certacon.certabotloadfiles.utils.StatusFile;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;

@Component
public class CreateFileComponent {
    private final UnzipAndZipComponent unzipAndZipComponent;
    private final UserFilesRepository userFilesRepository;
    @Value("${config.xmlDir}")
    private String filesDir;

    public CreateFileComponent(UnzipAndZipComponent unzipAndZipComponent, UserFilesRepository userFilesRepository) {
        this.unzipAndZipComponent = unzipAndZipComponent;
        this.userFilesRepository = userFilesRepository;
    }

    private File createFolder(Path path, String folderName) throws IOException {
        File newPath = Path.of(path + "\\" + folderName).toFile();
        if (!newPath.exists()) {
            newPath.mkdirs();
        }
        return newPath;
    }

    private File moveFile(File fileToMove, Path destiny) throws IOException {
        Path destinyPath = Path.of(destiny + "\\" + fileToMove.getName());
        File movedFile = Files.move(fileToMove.toPath(), destinyPath, ATOMIC_MOVE).toFile();
        return movedFile;
    }

    public Boolean checkFile(String path, String cnpj, String ipServer) {
        Boolean isCreated = Boolean.FALSE;
        Path fullPath = Paths.get(path);
        try {
            File[] listFile = new File(fullPath.toString()).listFiles();
            for (File value : listFile) {
                Optional<UserFilesModel> filePath = userFilesRepository.findByFileName(value.getName());
                if (filePath.isPresent() && filePath.get().getStatus().equals(StatusFile.OK)) {
                    File conludedFolder = new File(filesDir + "\\" + cnpj + "\\" + "Concluídos");
                    moveFile(value, Path.of(conludedFolder + "\\" + value.getName()));
                }

                if (!filePath.isPresent()) {
                    if (FilenameUtils.getExtension(value.getName()).equals("rar") ||
                            FilenameUtils.getExtension(value.getName()).equals("zip")
                    ) {
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
                        UserFilesModel finalModel = unzipAndZipComponent.UnzipAndZipFiles(userFilesModelSaved, value);
                        userFilesRepository.delete(userFilesModelSaved);
                        userFilesRepository.save(finalModel);
                    } else if (FilenameUtils.getExtension(value.getName()).equals("txt")) {
                        File spedFolder = createFolder(value.getParentFile().toPath(), "SPED");
                        moveFile(value, spedFolder.toPath());
                    } else if (FilenameUtils.getExtension(value.getName()).equals("xml")) {
                        File xmlFolder = new File(filesDir + "\\" + cnpj + "\\" + "Xmls");
                        moveFile(value, Path.of(xmlFolder + "\\" + value.getName()));
                    } else if (value.isDirectory()) {
                        String folderPath = value.getPath();
                        // nome do arquivo zip que você deseja criar
                        String zipFilePath = value.getPath() + ".zip";
                        // cria o arquivo zip
                        File zipFile = new File(zipFilePath);
                        ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(new FileOutputStream(zipFile));
                        // pega os arquivos da pasta e adiciona ao arquivo zip
                        File folder = new File(folderPath);
                        File[] files = folder.listFiles();
                        for (File file : files) {
                            ZipArchiveEntry entry = new ZipArchiveEntry(file.getName());
                            zipOut.putArchiveEntry(entry);
                            FileInputStream in = new FileInputStream(file);
                            byte[] b = new byte[4096];
                            int count = 0;
                            while ((count = in.read(b)) > 0) {
                                zipOut.write(b, 0, count);
                            }
                            in.close();
                            zipOut.closeArchiveEntry();
                        }
                        FileUtils.cleanDirectory(folder);
                        FileUtils.deleteDirectory(folder);
                        // fecha o arquivo zip
                        zipOut.close();
                        Path pathForSave = zipFile.toPath();
                        String mimeType = Files.probeContentType(pathForSave);
                        String ext = FilenameUtils.getExtension(pathForSave.toString());
                        UserFilesModel userFilesModelSaved = UserFilesModel.builder()
                                .cnpj(cnpj)
                                .ipServer(ipServer)
                                .mimeType(mimeType)
                                .fileName(zipFile.getName())
                                .path(zipFile.toPath().toString())
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