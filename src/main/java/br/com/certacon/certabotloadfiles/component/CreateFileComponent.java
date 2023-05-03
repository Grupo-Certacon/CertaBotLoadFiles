package br.com.certacon.certabotloadfiles.component;


import br.com.certacon.certabotloadfiles.model.UserFilesModel;
import br.com.certacon.certabotloadfiles.repository.FileTypeRepository;
import br.com.certacon.certabotloadfiles.repository.UserFilesRepository;
import br.com.certacon.certabotloadfiles.utils.StatusFile;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;

@Component
public class CreateFileComponent {
    private final FileTypeRepository fileTypeRepository;
    private final UnzipAndZipComponent unzipAndZipComponent;
    private final UserFilesRepository userFilesRepository;

    public CreateFileComponent(FileTypeRepository fileTypeRepository, UnzipAndZipComponent unzipAndZipComponent, UserFilesRepository userFilesRepository) {
        this.fileTypeRepository = fileTypeRepository;
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
              /*  if(filePath.isPresent() && filePath.get().getStatus().equals(StatusFile.UPLOADED)){
                    String serverManipulado = ipServer.replaceAll("[^0-9]", "");
                    String cnpjManipulado = cnpj.replaceAll("[^0-9]", "");
                    File sentFile = new File("D:\\Enviados\\" + serverManipulado + File.separator +cnpjManipulado + File.separator + year);
                    if (!sentFile.exists()) sentFile.mkdirs();
                    moveFile(value, sentFile.toPath());
                    filePath.get().setPath(sentFile.getPath());
                }*/
                if (filePath.isEmpty()) {
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
                                .year(year)
                                .extension(ext)
                                .status(StatusFile.CREATED)
                                .createdAt(new Date())
                                .build();
                        userFilesRepository.save(userFilesModelSaved);
                        UserFilesModel finalModel = unzipAndZipComponent.UnzipAndZipFiles(userFilesModelSaved, value);
                        userFilesRepository.delete(userFilesModelSaved);
                        userFilesRepository.save(finalModel);
                    } else if (FilenameUtils.getExtension(value.getName()).equals("txt")) {
                        Path pathForSave = value.toPath();
                        String mimeType = Files.probeContentType(pathForSave);
                        String ext = FilenameUtils.getExtension(pathForSave.toString());
                        UserFilesModel userTxtEfd = UserFilesModel.builder()
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
                        userFilesRepository.save(userTxtEfd);
                    } else if (value.isDirectory()) {
                        unzipAndZipComponent.extractFolder(value, null);
                        if (unzipAndZipComponent.checkFolderExistence(value.getParentFile().listFiles()).equals(Boolean.TRUE)) {
                            File[] parentList = value.getParentFile().listFiles();
                            for (int j = 0; j < parentList.length; j++) {
                                if (parentList[j].isDirectory())
                                    unzipAndZipComponent.extractFolder(parentList[j], null);
                            }
                        }
                        String folderPath = value.getPath();
                        // nome do arquivo zip que você deseja criar
                        String zipFilePath = value.getPath() + ".zip";
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
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return isCreated;
    }

    private File moveFile(File fileToMove, Path destiny) throws IOException {
        Path destinyPath = Path.of(destiny + File.separator + fileToMove.getName());
        File movedFile = Files.move(fileToMove.toPath(), destinyPath, ATOMIC_MOVE).toFile();
        return movedFile;
    }
}