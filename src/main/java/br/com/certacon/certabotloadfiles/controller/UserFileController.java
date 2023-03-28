package br.com.certacon.certabotloadfiles.controller;

import br.com.certacon.certabotloadfiles.model.UserFilesModel;
import br.com.certacon.certabotloadfiles.service.UserFilesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("files")
public class UserFileController {
    private final UserFilesService userFilesService;

    public UserFileController(UserFilesService userFilesService) {
        this.userFilesService = userFilesService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserFilesModel> getById(@PathVariable UUID id) {
        UserFilesModel model = userFilesService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(model);
    }

    @GetMapping
    public ResponseEntity<List<UserFilesModel>> getAll() {
        List<UserFilesModel> modelList = userFilesService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(modelList);
    }
}
