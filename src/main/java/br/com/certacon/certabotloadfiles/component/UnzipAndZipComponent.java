package br.com.certacon.certabotloadfiles.component;

import br.com.certacon.certabotloadfiles.model.UserFilesModel;
import br.com.certacon.certabotloadfiles.utils.StatusFile;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
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

            byte[] buffer = new byte[1024];
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

    private Boolean checkFileExistence(File[] fileList) {
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    private StatusFile extractFolder(File directory, File destDir) throws IOException {
        File[] directoryList = readFolder(directory);
        for (File file : directoryList) {
            if (FilenameUtils.getExtension(file.getName()).equals("zip")) {
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

    public UserFilesModel UnzipAndZipFiles(UserFilesModel zipModel) throws IOException {
        File uuidDir = new File("D:\\" + zipModel.getId().toString());
        File compactedDir = new File(uuidDir + "\\" + "Compactados");
        File descompactedDir = new File(uuidDir + "\\" + "Descompactados");

        if (!uuidDir.exists()) createDir(uuidDir);

        if (!compactedDir.exists()) createDir(compactedDir);

        if (!descompactedDir.exists()) createDir(descompactedDir);

        File zipPath = new File(zipModel.getPath());
        File[] sourceDir = readFolder(zipPath.getParentFile());
        if (FilenameUtils.getExtension(sourceDir[0].getName()).equals("zip") && zipModel.getStatus().equals(StatusFile.CREATED)) {
            StatusFile movedStatus = moveFile(sourceDir[0], Path.of(compactedDir + "\\" + sourceDir[0].getName()));
            if (movedStatus.equals(StatusFile.MOVED)) {
                File movedFile = new File(compactedDir + "\\" + sourceDir[0].getName());
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
                if (FilenameUtils.getExtension(descompactedList[i].getName()).equals("zip")) {
                    moveFile(descompactedList[i], Path.of(compactedDir.getPath()));
                }
            }
            File[] compactedList = readFolder(compactedDir);
            for (int i = 0; i < compactedList.length; i++) {
                unzipFile(compactedList[i], descompactedDir.toPath());
            }
        } while (checkFileExistence(descompactedDir.listFiles()).equals(Boolean.TRUE));

        compactedDir.delete();

        extractFolder(descompactedDir, descompactedDir.getParentFile());

        if (zipFiles(uuidDir, new File(zipPath.getParentFile().getPath())).equals(StatusFile.ZIPPED)) {
            zipModel.setFileName(uuidDir.getName() + ".zip");
            zipModel.setStatus(StatusFile.UPDATED);
        }
        return zipModel;
    }


}
