package br.com.certacon.certabotloadfiles.controller;

import br.com.certacon.certabotloadfiles.model.UserFilesModel;
import br.com.certacon.certabotloadfiles.service.DownloadFileService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("downloads")
public class DownloadFileController {
    private final DownloadFileService downloadFileService;

    public DownloadFileController(DownloadFileService downloadFileService) {
        this.downloadFileService = downloadFileService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> download(@PathVariable UUID id) throws FileNotFoundException {
        UserFilesModel model = downloadFileService.getById(id);
        if (model != null) {
            File arquivo = new File(model.getPath());
            InputStreamResource resource = new InputStreamResource(new FileInputStream(arquivo));
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + arquivo.getName());
            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
            header.add("Pragma", "no-cache");
            header.add("Expires", "0");
            return ResponseEntity.ok()
                    .headers(header)
                    .contentLength(arquivo.length())
                    .contentType(MediaType.parseMediaType(model.getMimeType()))
                    .body(resource);
        } else {
            return null;
        }
    }

}
