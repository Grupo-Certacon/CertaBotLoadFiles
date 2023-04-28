package br.com.certacon.certabotloadfiles.controller;

import br.com.certacon.certabotloadfiles.dto.LoadFilesDto;
import br.com.certacon.certabotloadfiles.exception.MessageExceptionHandler;
import br.com.certacon.certabotloadfiles.model.LoadFilesModel;
import br.com.certacon.certabotloadfiles.service.LoadFilesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("load/config")
public class LoadFilesController {

    private final LoadFilesService loadFilesService;

    public LoadFilesController(LoadFilesService loadFilesService) {
        this.loadFilesService = loadFilesService;
    }

    @PostMapping
    @Operation(description = "Cria um LoadFile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "LoadFile criado!", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoadFilesModel.class))}),
            @ApiResponse(responseCode = "400", description = "Informação inserida esta errada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageExceptionHandler.class))}),
            @ApiResponse(responseCode = "500", description = "Erro no servidor", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = MessageExceptionHandler.class))})
    })
    public ResponseEntity<LoadFilesModel> create(@RequestBody LoadFilesModel loadFilesModel) {
        LoadFilesModel model = loadFilesService.create(loadFilesModel);
        return ResponseEntity.status(HttpStatus.OK).body(model);
    }


    @GetMapping
    @Operation(description = "Busca todos LoadFiles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "LoadFiles encontrados!", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoadFilesModel.class))}),
            @ApiResponse(responseCode = "400", description = "Informação inserida esta errada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageExceptionHandler.class))}),
            @ApiResponse(responseCode = "404", description = "LoadFiles não encontrados",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageExceptionHandler.class))}),
            @ApiResponse(responseCode = "500", description = "Erro no servidor", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = MessageExceptionHandler.class))})
    })
    public ResponseEntity<List<LoadFilesModel>> getAll() {
        List<LoadFilesModel> modelList = loadFilesService.getAllFolders();
        return ResponseEntity.status(HttpStatus.OK).body(modelList);
    }

    @GetMapping("/{id}")
    @Operation(description = "Busca um LoadFile pelo Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "LoadFile encontrado!", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoadFilesModel.class))}),
            @ApiResponse(responseCode = "400", description = "Informação inserida esta errada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageExceptionHandler.class))}),
            @ApiResponse(responseCode = "404", description = "LoadFile não encontrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageExceptionHandler.class))}),
            @ApiResponse(responseCode = "500", description = "Erro no servidor", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = MessageExceptionHandler.class))})
    })
    public ResponseEntity<Optional<LoadFilesModel>> getById(@PathVariable UUID id) {
        Optional<LoadFilesModel> model = loadFilesService.getOneFolder(id);
        return ResponseEntity.status(HttpStatus.OK).body(model);
    }

    @PutMapping
    @Operation(description = "Atualiza um LoadFile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "LoadFile atualizado!", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoadFilesModel.class))}),
            @ApiResponse(responseCode = "400", description = "Informação inserida esta errada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageExceptionHandler.class))}),
            @ApiResponse(responseCode = "404", description = "LoadFile não encontrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageExceptionHandler.class))}),
            @ApiResponse(responseCode = "500", description = "Erro no servidor", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = MessageExceptionHandler.class))})
    })
    public ResponseEntity<LoadFilesModel> update(@RequestBody LoadFilesDto loadFilesDto) {
        LoadFilesModel model = loadFilesService.updateFolder(loadFilesDto);
        return ResponseEntity.status(HttpStatus.OK).body(model);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Deleta um LoadFile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "LoadFile deletado!"),
            @ApiResponse(responseCode = "400", description = "Informação inserida esta errada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageExceptionHandler.class))}),
            @ApiResponse(responseCode = "404", description = "LoadFile não encontrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageExceptionHandler.class))}),
            @ApiResponse(responseCode = "500", description = "Erro no servidor", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = MessageExceptionHandler.class))})
    })
    public ResponseEntity<Boolean> delete(@PathVariable UUID id) {
        Boolean model = loadFilesService.deleteFolder(id);
        return ResponseEntity.status(HttpStatus.OK).body(model);
    }
}
