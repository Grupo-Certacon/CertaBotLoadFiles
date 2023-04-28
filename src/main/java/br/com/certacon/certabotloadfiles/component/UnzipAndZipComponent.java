package br.com.certacon.certabotloadfiles.component;

import br.com.certacon.certabotloadfiles.model.UserFilesModel;
import br.com.certacon.certabotloadfiles.utils.FileType;
import br.com.certacon.certabotloadfiles.utils.StatusFile;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;

@Component
public class UnzipAndZipComponent {

    @Value("${config.rootPath}")
    private String rootPath;

    public UserFilesModel UnzipAndZipFiles(UserFilesModel zipModel, File zipFile) throws IOException {
        File uuidDir = new File("D:\\" + "ZIP-" + zipModel.getId().toString().toUpperCase());
        File compactedDir = new File(uuidDir + "\\" + "Compactados");
        File descompactedDir = new File(uuidDir + "\\" + "Descompactados");

        if (!uuidDir.exists()) createDir(uuidDir);

        if (!compactedDir.exists()) createDir(compactedDir);

        if (!descompactedDir.exists()) createDir(descompactedDir);

        File zipPath = new File(zipModel.getPath());
        if (FilenameUtils.getExtension(zipFile.getName()).equals("rar") && zipModel.getStatus().equals(StatusFile.CREATED) ||
                FilenameUtils.getExtension(zipFile.getName()).equals("zip") && zipModel.getStatus().equals(StatusFile.CREATED)) {
            StatusFile movedStatus = moveFile(zipFile, Path.of(compactedDir + "\\" + zipFile.getName()));
            if (movedStatus.equals(StatusFile.MOVED)) {
                zipModel.setStatus(StatusFile.MOVED);
                File movedFile = new File(compactedDir + "\\" + zipFile.getName());
                StatusFile zipStatus = unzipFile(movedFile, Path.of(descompactedDir.getPath()));
                zipModel.setStatus(zipStatus);
            }

        }
        do {
            File[] descompactedList = readFolder(descompactedDir);
            for (int i = 0; i < descompactedList.length; i++) {
                if (descompactedList[i].isDirectory()) {
                    extractFolder(descompactedList[i], compactedDir);
                }
                if (FilenameUtils.getExtension(descompactedList[i].getName()).equals("rar") ||
                        FilenameUtils.getExtension(descompactedList[i].getName()).equals("zip")) {
                    moveFile(descompactedList[i], Path.of(compactedDir.getPath()));
                }
            }
            File[] compactedList = readFolder(compactedDir);
            for (int i = 0; i < compactedList.length; i++) {
                unzipFile(compactedList[i], descompactedDir.toPath());
            }
        } while (checkFolderExistence(descompactedDir.listFiles()).equals(Boolean.TRUE));

        compactedDir.delete();

        extractFolder(descompactedDir, descompactedDir.getParentFile());

        File[] checkXml = descompactedDir.getParentFile().listFiles();
        for (int i = 0; i < checkXml.length; i++) {
            if (FilenameUtils.getExtension(checkXml[i].getName()).equals("xml")) {
                String ipServerManipulado = zipModel.getIpServer().replaceAll("[^0-9]", "");
                String cnpjManipulado = zipModel.getCnpj().replaceAll("[^0-9]", "");
                String xmlFolder = rootPath + "\\" + ipServerManipulado + "\\" + cnpjManipulado + "\\" + zipModel.getYear() + "\\" + FileType.NFe;
                File xmlFile = new File(xmlFolder);
                if (!xmlFile.exists()) {
                    xmlFile.mkdirs();
                }
                moveFile(checkXml[i], Path.of(xmlFile + "\\" + checkXml[i].getName()));
            }
        }
        File finalZip = new File(zipPath.toString());
        if (zipFiles(uuidDir, finalZip).equals(StatusFile.ZIPPED)) {
            zipModel.setStatus(StatusFile.ZIPPED);
            zipModel.setPath(zipPath.getParentFile() + "\\" + uuidDir.getName() + ".zip");
            zipModel.setExtension("zip");
            zipModel.setFileName(uuidDir.getName() + ".zip");
            zipModel.setStatus(StatusFile.UPDATED);
            FileUtils.forceDelete(uuidDir);
        } else {
            throw new RuntimeException("Algo deu errado!");

        }
        return zipModel;
    }

    private File[] readFolder(File fileToRead) {
        File[] fileList = fileToRead.listFiles();
        return fileList;
    }

    private Path createDir(File fileToCreate) throws IOException {
        Path createdFile = Files.createDirectory(fileToCreate.toPath());
        return createdFile;
    }

    private StatusFile moveFile(File fileToMove, Path destiny) throws IOException {
        Files.move(Path.of(fileToMove.getPath()), destiny, ATOMIC_MOVE);
        return StatusFile.MOVED;
    }

    private StatusFile unzipFile(File toDescompact, Path destinyDir) {
        try (ArchiveInputStream entrada = new ZipArchiveInputStream(new FileInputStream(toDescompact))) {
            File pastaDestino = new File(destinyDir.toString());

            if (!pastaDestino.exists()) {
                pastaDestino.mkdirs();
            }

            ArchiveEntry entry = entrada.getNextEntry();

            while (entry != null) {
                String nomeArquivo = entry.getName();
                File arquivo = new File(pastaDestino, nomeArquivo);

                if (entry.isDirectory()) {
                    arquivo.mkdirs();
                } else {
                    File pastaArquivo = arquivo.getParentFile();
                    if (!pastaArquivo.exists()) {
                        pastaArquivo.mkdirs();
                    }
                    FileOutputStream fos = new FileOutputStream(arquivo);
                    IOUtils.copy(entrada, fos);
                    fos.close();
                }

                entry = entrada.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        toDescompact.delete();
        return StatusFile.UNZIPPED;
    }

    private StatusFile zipFiles(File descompactedDir, File destiny) throws IOException {
        File[] descompactedList = readFolder(descompactedDir);
        final FileOutputStream fos = new FileOutputStream(Paths.get(destiny.getPath()).toAbsolutePath() + "\\" + descompactedDir.getName() + ".zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        for (File srcFile : descompactedList) {
            File fileToZip = new File(srcFile.toURI());
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] buffer = new byte[8192];
            int length;
            while ((length = fis.read(buffer)) >= 0) {
                zipOut.write(buffer, 0, length);
            }
            fis.close();
        }
        zipOut.close();
        fos.close();
        descompactedDir.deleteOnExit();
        return StatusFile.ZIPPED;
    }

    public Boolean checkFolderExistence(File[] fileList) {
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }


    public StatusFile extractFolder(File directory, File destDir) throws IOException {
        File[] directoryList = readFolder(directory);
        for (File file : directoryList) {
            if (FilenameUtils.getExtension(file.getName()).equals("rar") ||
                    FilenameUtils.getExtension(file.getName()).equals("zip")
            ) {
                moveFile(file, Path.of(destDir + "\\" + file.getName()));
            } else {
                Path parentFolder = Path.of(directory.getParentFile().getPath());
                Path filePath = Path.of(file.getPath());
                Files.move(filePath, parentFolder.resolve(UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getName())), ATOMIC_MOVE);
            }
        }
        directory.delete();
        return StatusFile.MOVED;
    }
}
