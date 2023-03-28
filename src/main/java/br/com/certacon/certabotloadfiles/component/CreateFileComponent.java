package br.com.certacon.certabotloadfiles.component;

import br.com.certacon.certabotloadfiles.model.UserFilesModel;
import br.com.certacon.certabotloadfiles.repository.UserFilesRepository;
import br.com.certacon.certabotloadfiles.utils.StatusFile;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Component
public class CreateFileComponent {
    private final UserFilesRepository userFilesRepository;


    public CreateFileComponent(UserFilesRepository userFilesRepository) {
        this.userFilesRepository = userFilesRepository;
    }

    public Boolean checkFile(String path) {
        Boolean isCreated = Boolean.FALSE;
        UserFilesModel result;
        try {
            Path fullPath = Paths.get(path);
            File[] file = new File(fullPath.toUri()).listFiles();
            for (int i = 0; i < file.length; i++) {
                Optional<UserFilesModel> filePath = userFilesRepository.findByFileName(file[i].getName());
                if (!filePath.isPresent()) {
                    if (file[i].getName().contains(".zip")) {
                        File zipFile = new File(file[i].getPath());
                        String tempDirectoryPath = zipFile.getParentFile().getAbsolutePath();

                        // Descompacta o arquivo ZIP para o diretório temporário
                        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile))) {
                            ZipEntry zipEntry;
                            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                                String fileName = zipEntry.getName();
                                File outputFile = new File(tempDirectoryPath + File.separator + fileName);
                                if (zipEntry.isDirectory()) {
                                    outputFile.mkdirs();
                                } else {
                                    File parent = outputFile.getParentFile();
                                    if (!parent.exists()) {
                                        parent.mkdirs();
                                    }
                                    FileOutputStream outputStream = new FileOutputStream(outputFile);
                                    IOUtils.copy(zipInputStream, outputStream);
                                    outputStream.close();
                                }
                                // Check if the extracted file is a ZIP file, and if it is, recursively extract it to the same directory
                                if (fileName.toLowerCase().endsWith(".zip")) {
                                    String newZipFilePath = tempDirectoryPath + File.separator + fileName;
                                    File zipFile1 = new File(newZipFilePath);
                                    String tempDirectoryPath1 = zipFile1.getParentFile().getAbsolutePath();

                                    try (ZipInputStream zipInputStream1 = new ZipInputStream(new FileInputStream(zipFile1))) {
                                        ZipEntry zipEntry1;
                                        while ((zipEntry1 = zipInputStream1.getNextEntry()) != null) {
                                            String fileName1 = zipEntry1.getName();
                                            File outputFile1 = new File(tempDirectoryPath1 + File.separator + fileName1);
                                            if (zipEntry.isDirectory()) {
                                                outputFile1.mkdirs();
                                            } else {
                                                File parent = outputFile1.getParentFile();
                                                if (!parent.exists()) {
                                                    parent.mkdirs();
                                                }
                                                FileOutputStream outputStream = new FileOutputStream(outputFile);
                                                IOUtils.copy(zipInputStream1, outputStream);
                                                outputStream.close();
                                            }
                                            // Compacta o conteúdo descompactado em um novo arquivo ZIP
                                            File tempDirectory = new File(tempDirectoryPath);
                                            File[] filesToZip = tempDirectory.listFiles();
                                            String newZipFilePath1 = tempDirectoryPath + File.separator + "newArchive.zip";
                                            try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(newZipFilePath1))) {
                                                byte[] buffer = new byte[1024];
                                                for (File newFile : filesToZip) {
                                                    if (!newFile.isDirectory()) {
                                                        ZipEntry zipEntry2 = new ZipEntry(newFile.getName());
                                                        zipOutputStream.putNextEntry(zipEntry2);
                                                        FileInputStream inputStream = new FileInputStream(newFile);
                                                        int len;
                                                        while ((len = inputStream.read(buffer)) > 0) {
                                                            zipOutputStream.write(buffer, 0, len);
                                                        }
                                                        inputStream.close();
                                                        zipOutputStream.closeEntry();
                                                    }
                                                }
                                            } catch (IOException e) {
                                                throw new IOException("Failed to create new zip file", e);
                                            }

                                            // Exclui o diretório temporário
                                            FileUtils.deleteDirectory(tempDirectory);
                                        }
                                    } catch (FileNotFoundException e) {
                                        throw new RuntimeException(e);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                result = UserFilesModel.builder()
                                        .fileName(file[i].getName())
                                        .path(file[i].getPath())
                                        .status(StatusFile.CREATED)
                                        .createdAt(new Date())
                                        .build();
                                isCreated = Boolean.TRUE;
                                userFilesRepository.save(result);
                            }
                        }
                    }
                }
            }
            return isCreated;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}