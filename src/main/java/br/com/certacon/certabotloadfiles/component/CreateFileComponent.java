package br.com.certacon.certabotloadfiles.component;


import br.com.certacon.certabotloadfiles.model.UserFilesModel;
import br.com.certacon.certabotloadfiles.repository.UserFilesRepository;
import br.com.certacon.certabotloadfiles.utils.FileType;
import br.com.certacon.certabotloadfiles.utils.StatusFile;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
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

    public CreateFileComponent(UnzipAndZipComponent unzipAndZipComponent, UserFilesRepository userFilesRepository) {
        this.unzipAndZipComponent = unzipAndZipComponent;
        this.userFilesRepository = userFilesRepository;
    }

    public Boolean checkFile(String path, String cnpj, String ipServer, String year) {
        Boolean isCreated = Boolean.FALSE;
        Path fullPath = Paths.get(path);
        try {
            File[] listFile = new File(fullPath.toString()).listFiles();

            for (File value : listFile) {
                Optional<UserFilesModel> filePath = userFilesRepository.findByFileName(value.getName());
                if (filePath.isEmpty()) {
                    Path pathForSave = value.toPath();
                    String mimeType = Files.probeContentType(pathForSave);
                    String ext = FilenameUtils.getExtension(pathForSave.toString());
                    UserFilesModel userFilesModelSaved = UserFilesModel.builder()
                            .cnpj(cnpj)
                            .ipServer(ipServer)
                            .mimeType(mimeType)
                            .fileName(value.getName())
                            .path(value.getPath())
                            .year(year)
                            .extension(ext)
                            .status(StatusFile.CREATED)
                            .createdAt(new Date())
                            .build();
                    if (value.isDirectory() && value.getName().equals(FileType.EFDPadrao.toString())) {
                        readAndExtractFromObrigationsFolder(value, FileType.EFDPadrao, "xml", userFilesModelSaved);
                    } else if (value.isDirectory() && value.getName().equals(FileType.NFe.toString())) {
                        readAndExtractFromObrigationsFolder(value, FileType.NFe, "txt", userFilesModelSaved);
                    }
                }
            }
            return isCreated;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private File moveFile(File fileToMove, Path destiny) throws IOException {
        Path destinyPath = Path.of(destiny + "\\" + fileToMove.getName());
        File movedFile = Files.move(fileToMove.toPath(), destinyPath, ATOMIC_MOVE).toFile();
        return movedFile;
    }

    private StatusFile readAndExtractFromObrigationsFolder(File folder, FileType fileType, String extension, UserFilesModel userFilesModelSaved) throws IOException {
        File[] listFiles = folder.listFiles();
        if (listFiles != null) {
            for (int i = 0; i < listFiles.length; i++) {
                Optional<UserFilesModel> filePath = userFilesRepository.findByFileName(listFiles[i].getName());
                if (filePath.isEmpty()) {
                    if (FilenameUtils.getExtension(listFiles[i].getName()).equals("rar") ||
                            FilenameUtils.getExtension(listFiles[i].getName()).equals("zip")
                    ) {
                        userFilesRepository.save(userFilesModelSaved);
                        UserFilesModel finalModel = unzipAndZipComponent.UnzipAndZipFiles(userFilesModelSaved, listFiles[i]);
                        userFilesRepository.delete(userFilesModelSaved);
                        userFilesRepository.save(finalModel);
                    } else if (FilenameUtils.getExtension(listFiles[i].getName()).equals(extension)) {
                        File xmlFile = new File(folder.getParentFile().toString() + "\\" + fileType.toString());
                        if (!xmlFile.exists()) xmlFile.mkdirs();
                        moveFile(listFiles[i], Path.of(folder + "\\" + listFiles[i].getName()));
                    } else if (listFiles[i].isDirectory()) {
                        unzipAndZipComponent.extractFolder(listFiles[i], null);
                        if (unzipAndZipComponent.checkFolderExistence(listFiles[i].getParentFile().listFiles()).equals(Boolean.TRUE)) {
                            File[] parentList = listFiles[i].getParentFile().listFiles();
                            for (int j = 0; j < parentList.length; j++) {
                                if (parentList[j].isDirectory())
                                    unzipAndZipComponent.extractFolder(parentList[j], null);
                            }
                        }
                        String folderPath = listFiles[i].getPath();
                        // nome do arquivo zip que você deseja criar
                        String zipFilePath = listFiles[i].getPath() + ".zip";
                        // cria o arquivo zip
                        File zipFile = new File(zipFilePath);
                        ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(new FileOutputStream(zipFile));
                        // pega os arquivos da pasta e adiciona ao arquivo zip
                        File folder1 = new File(folderPath);
                        File[] files = folder1.listFiles();
                        for (File file : files) {
                            ZipArchiveEntry entry = new ZipArchiveEntry(file.getName());
                            zipOut.putArchiveEntry(entry);
                            FileInputStream in = new FileInputStream(file);
                            byte[] b = new byte[8192];
                            int count = 0;
                            while ((count = in.read(b)) > 0) {
                                zipOut.write(b, 0, count);
                            }
                            in.close();
                            zipOut.closeArchiveEntry();
                        }
                        FileUtils.cleanDirectory(folder1);
                        FileUtils.deleteDirectory(folder1);
                        // fecha o arquivo zip
                        zipOut.close();
                        Path pathForSave = zipFile.toPath();
                        String mimeType = Files.probeContentType(pathForSave);
                        String ext = FilenameUtils.getExtension(pathForSave.toString());
                        UserFilesModel newUserFileForSave = UserFilesModel.builder()
                                .cnpj(userFilesModelSaved.getCnpj())
                                .ipServer(userFilesModelSaved.getIpServer())
                                .mimeType(mimeType)
                                .fileName(zipFile.getName())
                                .path(zipFile.toPath().toString())
                                .extension(ext)
                                .status(StatusFile.CREATED)
                                .createdAt(new Date())
                                .build();
                        userFilesRepository.save(newUserFileForSave);
                    }
                }
            }
        }
        return StatusFile.READY;
    }
}