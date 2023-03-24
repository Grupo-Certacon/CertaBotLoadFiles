package br.com.certacon.certabotloadfiles.controller;

import br.com.certacon.certabotloadfiles.dto.LoadFilesDto;
import br.com.certacon.certabotloadfiles.model.LoadFilesModel;
import br.com.certacon.certabotloadfiles.service.LoadFilesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("load/config")
public class LoadFilesController {

    private final LoadFilesService loadFilesService;

    public LoadFilesController(LoadFilesService loadFilesService) {
        this.loadFilesService = loadFilesService;
    }

    @PostMapping
    public ResponseEntity<LoadFilesModel> create(@RequestBody LoadFilesModel loadFilesModel) {
        LoadFilesModel model = loadFilesService.create(loadFilesModel);
        return ResponseEntity.status(HttpStatus.OK).body(model);
    }

    @GetMapping
    public ResponseEntity<List<LoadFilesModel>> getAll() {
        List<LoadFilesModel> modelList = loadFilesService.getAllFolders();
        return ResponseEntity.status(HttpStatus.OK).body(modelList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<LoadFilesModel>> getById(@PathVariable Long id) {
        Optional<LoadFilesModel> model = loadFilesService.getOneFolder(id);
        return ResponseEntity.status(HttpStatus.OK).body(model);
    }

    @PutMapping
    public ResponseEntity<LoadFilesModel> update(@RequestBody LoadFilesDto loadFilesDto) {
        LoadFilesModel model = loadFilesService.updateFolder(loadFilesDto);
        return ResponseEntity.status(HttpStatus.OK).body(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        Boolean model = loadFilesService.deleteFolder(id);
        return ResponseEntity.status(HttpStatus.OK).body(model);
    }
}
