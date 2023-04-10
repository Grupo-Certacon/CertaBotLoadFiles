package br.com.certacon.certabotloadfiles.component;


import br.com.certacon.certabotloadfiles.model.UserFilesModel;
import br.com.certacon.certabotloadfiles.repository.UserFilesRepository;
import br.com.certacon.certabotloadfiles.utils.StatusFile;
import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

@Component
public class CreateFileComponent {
    private final UserFilesRepository userFilesRepository;
    @Value("${config.destDir}")
    private String destDir;

    public CreateFileComponent(UserFilesRepository userFilesRepository) {
        this.userFilesRepository = userFilesRepository;
    }

    public static List<String> listFiles(File file, boolean extract) throws IOException {
        List<String> files = new ArrayList<>();
        if (isArchiveFile(file)) {
            if (isZipFile(file)) {
                files.addAll(listZipFiles(file, extract));
            } else if (isRarFile(file)) {
                files.addAll(listRarFiles(file, extract));
            } else {
                throw new UnsupportedOperationException("Unsupported archive format");
            }
        } else {
            files.add(file.getName());
        }
        return files;
    }

    private static boolean isArchiveFile(File file) {
        return isZipFile(file) || isRarFile(file);
    }

    private static boolean isZipFile(File file) {
        return file.getName().toLowerCase().endsWith(".zip");
    }

    private static boolean isRarFile(File file) {
        return file.getName().toLowerCase().endsWith(".rar");
    }

    private static List<String> listZipFiles(File file, boolean extract) throws IOException {
        List<String> files = new ArrayList<>();
        try (ZipFile zipFile = new ZipFile(file)) {
            zipFile.stream().forEach(entry -> {
                if (!entry.isDirectory()) {
                    String fileName = entry.getName();
                    files.add(fileName);
                    if (extract) {
                        try (FileOutputStream fos = new FileOutputStream(new File(file.getParent(), fileName))) {
                            fos.write(zipFile.getInputStream(entry).readAllBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        return files;
    }

    private static List<String> listRarFiles(File file, boolean extract) throws IOException {
        List<String> files = new ArrayList<>();
        try (Archive archive = new Archive(new FileInputStream(file))) {
            FileHeader fileHeader = archive.nextFileHeader();
            while (fileHeader != null) {
                if (!fileHeader.isDirectory()) {
                    String fileName = fileHeader.getFileNameString().trim();
                    files.add(fileName);
                    if (extract) {
                        try (FileOutputStream fos = new FileOutputStream(new File(file.getParent(), fileName))) {
                            archive.extractFile(fileHeader, fos);
                        } catch (RarException e) {
                            e.printStackTrace();
                        }
                    }
                }
                fileHeader = archive.nextFileHeader();
            }
        } catch (RarException e) {
            throw new RuntimeException(e);
        }
        return files;
    }

    public Boolean checkFile(String path, String cnpj, String ipServer) throws IOException {
        Boolean isCreated = Boolean.FALSE;
        Path fullPath = Paths.get(path);
        try {
            File[] listFile = new File(fullPath.toString()).listFiles();
            for (File value : listFile) {
                Optional<UserFilesModel> filePath = userFilesRepository.findByFileName(value.getName());
                if (!filePath.isPresent()) {
              /*  if (FilenameUtils.getExtension(value.getName()).equals("zip")) {
                    File zipFile = new File(value.getPath());
                    File tempDir = new File(destDir);
                    List<File> internalZipFiles = new ArrayList<>();
                    try (ZipFile zip = new ZipFile(zipFile)) {
                        Enumeration<? extends ZipEntry> entries = zip.entries();
                        while (entries.hasMoreElements()) {
                            ZipEntry entry = entries.nextElement();
                            String entryName = entry.getName();
                            if (entryName.endsWith(".zip")) {
                                Files.move(Path.of(value.getPath() + entryName), Path.of("D:\\loadFileData\\"));
                                File internalZipFile = new File(tempDir, entryName);
                                try (InputStream is = zip.getInputStream(entry);
                                     OutputStream os = new FileOutputStream(internalZipFile)) {
                                    IOUtils.copy(is, os);
                                }
                            }
                        }
                    }
                    // Etapa 2: extrair cada arquivo ZIP interno e coletar informações
                    List<String> infoList = new ArrayList<>();
                    for (File internalZipFile : internalZipFiles) {
                        try (ZipFile zip = new ZipFile(internalZipFile)) {
                            Enumeration<? extends ZipEntry> entries = zip.entries();
                            while (entries.hasMoreElements()) {
                                ZipEntry entry = entries.nextElement();
                                String entryName = entry.getName();
                                // Aqui você pode coletar as informações desejadas
                                infoList.add(entryName);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    // Etapa 3: excluir pastas e arquivos extraídos
                    FileUtils.deleteQuietly(tempDir);
                }
            }
                if (FilenameUtils.getExtension(value.getName()).equals("rar")) {
                    File file = new File(destDir);
                    List<String> files = listZipFiles(file, true);
                    System.out.println(files);
                }*/
                    if (FilenameUtils.getExtension(value.getName()).equals("txt")) {
                        userFilesRepository.findAllByExtension("txt");
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
        }
        return isCreated;
    }
}