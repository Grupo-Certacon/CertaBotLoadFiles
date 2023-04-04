package br.com.certacon.certabotloadfiles.component;


import br.com.certacon.certabotloadfiles.model.UserFilesModel;
import br.com.certacon.certabotloadfiles.repository.UserFilesRepository;
import br.com.certacon.certabotloadfiles.utils.StatusFile;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

@Component
public class CreateFileComponent {
    private final UserFilesRepository userFilesRepository;

    public CreateFileComponent(UserFilesRepository userFilesRepository) {
        this.userFilesRepository = userFilesRepository;
    }

  /* public void extractAndCompress(String inputFilePath, String outputFilePath) throws IOException {
        // Cria um arquivo temporário para armazenar os arquivos extraídos
        File tempDir = new File("temp");
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }

        // Extrai todos os arquivos do arquivo ZIP e adiciona os arquivos e diretórios ao array de arquivos
        List<File> filesToZip = new ArrayList<>();
        ZipFile zipFile = new ZipFile(inputFilePath);
        List<FileHeader> fileHeaders = zipFile.getFileHeaders();
        for (FileHeader fileHeader : fileHeaders) {
            if (!fileHeader.isDirectory()) {
                String fileName = tempDir.getAbsolutePath() + "/" + fileHeader.getFileName();
                zipFile.extractFile(fileHeader, fileName);
                filesToZip.add(new File(fileName));
            }
        }

        // Adiciona os arquivos e diretórios dentro dos arquivos ZIP ao array de arquivos
        addFilesFromZips(tempDir, filesToZip);

        // Cria o novo arquivo ZIP com todos os arquivos e diretórios extraídos
        ZipFile newZipFile = new ZipFile(outputFilePath);
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(CompressionMethod.DEFLATE);
        parameters.setCompressionLevel(CompressionLevel.NORMAL);
        newZipFile.addFiles(filesToZip, parameters);

        // Deleta o arquivo temporário e seus arquivos
        deleteTempDirectory(tempDir);
    }

    private void addFilesFromZips(File dir, List<File> filesToZip) throws IOException, ZipException {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                addFilesFromZips(file, filesToZip);
            } else if (file.getName().endsWith(".zip")) {
                ZipFile zipFile = new ZipFile(file);
                List<FileHeader> fileHeaders = zipFile.getFileHeaders();
                for (FileHeader fileHeader : fileHeaders) {
                    if (!fileHeader.isDirectory()) {
                        String fileName = dir.getAbsolutePath() + "/" + fileHeader.getFileName();
                        InputStream inputStream = zipFile.getInputStream(fileHeader);
                        FileOutputStream outputStream = new FileOutputStream(fileName);
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        outputStream.close();
                        filesToZip.add(new File(fileName));
                    }
                }
                addFilesFromZips(file, filesToZip);
            } else {
                filesToZip.add(file);
            }
        }
    }

    private void deleteTempDirectory(File dir) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                deleteTempDirectory(file);
            } else {
                file.delete();
            }
        }
        dir.delete();
    }*/


    public Boolean checkFile(String path, String cnpj, String ipServer) throws IOException {
        Boolean isCreated = Boolean.FALSE;
        Path fullPath = Paths.get(path);
        File[] file = new File(fullPath.toString()).listFiles();
        for (int i = 0; i < file.length; i++) {
            Optional<UserFilesModel> filePath = userFilesRepository.findByFileName(file[i].getName());
            if (!filePath.isPresent()) {
                if (FilenameUtils.getExtension(file[i].getName()).equals("txt")) {
                    String zipPath = file[i].getPath().replace(FilenameUtils.getExtension(file[i].getName()), "zip");
                    Path pathForSave = Path.of(zipPath);
                    ZipFile zipFile = new ZipFile(zipPath);
                    try {
                        zipFile.addFile(file[i]);
                        zipFile.close();
                        FileSystemUtils.deleteRecursively(file[i]);
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
                    Path pathForSave = file[i].toPath();
                    String mimeType = Files.probeContentType(pathForSave);
                    String ext = FilenameUtils.getExtension(pathForSave.toString());
                    UserFilesModel userFilesModelSaved = UserFilesModel.builder()
                            .cnpj(cnpj)
                            .ipServer(ipServer)
                            .mimeType(mimeType)
                            .fileName(file[i].getName())
                            .path(file[i].getPath())
                            .extension(ext)
                            .status(StatusFile.CREATED)
                            .createdAt(new Date())
                            .build();
                    isCreated = Boolean.TRUE;
                    userFilesRepository.save(userFilesModelSaved);
                }
            }
        }
        return isCreated;
    }
}