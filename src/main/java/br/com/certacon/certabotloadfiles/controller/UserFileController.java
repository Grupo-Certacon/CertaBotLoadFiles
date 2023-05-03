package br.com.certacon.certabotloadfiles.controller;

import br.com.certacon.certabotloadfiles.exception.MessageExceptionHandler;
import br.com.certacon.certabotloadfiles.model.LoadFilesModel;
import br.com.certacon.certabotloadfiles.model.UserFilesModel;
import br.com.certacon.certabotloadfiles.service.UserFilesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(description = "Busca um UserFile pelo Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "UserFile encontrado!", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserFilesModel.class))}),
            @ApiResponse(responseCode = "400", description = "Informação inserida esta errada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageExceptionHandler.class))}),
            @ApiResponse(responseCode = "404", description = "UserFile não encontrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageExceptionHandler.class))}),
            @ApiResponse(responseCode = "500", description = "Erro no servidor", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = MessageExceptionHandler.class))})
    })
    public ResponseEntity<UserFilesModel> getById(@PathVariable UUID id) {
        UserFilesModel model = userFilesService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(model);
    }
    @PutMapping
    @Operation(description = "Atualiza um UserFile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "UserFile atualizado!", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserFilesModel.class))}),
            @ApiResponse(responseCode = "400", description = "Informação inserida esta errada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageExceptionHandler.class))}),
            @ApiResponse(responseCode = "404", description = "UserFile não encontrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageExceptionHandler.class))}),
            @ApiResponse(responseCode = "500", description = "Erro no servidor", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = MessageExceptionHandler.class))})
    })
    public ResponseEntity<UserFilesModel> update(@RequestBody UserFilesModel userFilesModel){
        UserFilesModel model = userFilesService.update(userFilesModel);
        return ResponseEntity.status(HttpStatus.OK).body(model);
    }

    @GetMapping
    @Operation(description = "Busca todos UserFiles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "UserFiles encontrados!", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserFilesModel.class))}),
            @ApiResponse(responseCode = "400", description = "Informação inserida esta errada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageExceptionHandler.class))}),
            @ApiResponse(responseCode = "404", description = "UserFiles não encontrados",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageExceptionHandler.class))}),
            @ApiResponse(responseCode = "500", description = "Erro no servidor", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = MessageExceptionHandler.class))})
    })
    public ResponseEntity<List<UserFilesModel>> getAll() {
        List<UserFilesModel> modelList = userFilesService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(modelList);
    }
}
