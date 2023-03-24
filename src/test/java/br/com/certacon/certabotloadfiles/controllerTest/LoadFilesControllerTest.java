package br.com.certacon.certabotloadfiles.controllerTest;

import br.com.certacon.certabotloadfiles.controller.LoadFilesController;
import br.com.certacon.certabotloadfiles.dto.LoadFilesDto;
import br.com.certacon.certabotloadfiles.model.LoadFilesModel;
import br.com.certacon.certabotloadfiles.service.LoadFilesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LoadFilesController.class)
public class LoadFilesControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    LoadFilesService loadFilesService;
    @MockBean
    LoadFilesModel loadFilesModel;

    @Test
    @DisplayName("chamar o metodo create quando retornar com Sucesso")
    void shouldCallLoadFolderControllerWhenCreateReturnWithSuccess() throws Exception {
        //Given
        BDDMockito.given(loadFilesService.create(any(LoadFilesModel.class))).willReturn(loadFilesModel);
        //When/Then
        mockMvc.perform(
                post("/load/config")
                        .param("id", loadFilesModel.getId().toString())
                        .param("folder", loadFilesModel.getFolder())
                        .param("server", loadFilesModel.getServer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loadFilesModel))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(loadFilesModel.getId().toString()))
                .andExpect(jsonPath("$.folder").value(loadFilesModel.getFolder()))
                .andExpect(jsonPath("$.server").value(loadFilesModel.getServer()));
        then(loadFilesService).should().create(any(LoadFilesModel.class));
    }
    @Test
    @DisplayName("chamar o metodo getAll quando retornar com sucesso")
    void shouldCallLoadFolderControllerWhenGetAllReturnWithSuccess() throws Exception {
        //Given
        BDDMockito.given(loadFilesService.getAllFolders()).willReturn(Arrays.asList(loadFilesModel));
        //When/Then
        mockMvc.perform(
                get("/load/config")
                        .param("id", loadFilesModel.getId().toString())
                        .param("folder", loadFilesModel.getFolder())
                        .param("server", loadFilesModel.getServer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loadFilesModel))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(loadFilesModel.getId().toString()))
                .andExpect(jsonPath("$[0].folder").value(loadFilesModel.getFolder()))
                .andExpect(jsonPath("$[0].server").value(loadFilesModel.getServer()));
        then(loadFilesService).should().getAllFolders();
    }
    @Test
    @DisplayName("chamar o metodo GetById quando Retornar com Sucesso")
    void shouldCallLoadFilesServiceWhenGetByIdReturnWithSuccess() throws Exception {
        //Given
        BDDMockito.given(loadFilesService.getOneFolder(any(Long.class))).willReturn(Optional.of(loadFilesModel));
        //When/Then
        mockMvc.perform(
                get("/load/config/{id}", loadFilesModel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(loadFilesModel.getId()))
                .andExpect(jsonPath("$.folder").value(loadFilesModel.getFolder()))
                .andExpect(jsonPath("$.server").value(loadFilesModel.getServer()));
        then(loadFilesService).should().getOneFolder(any(Long.class));
    }

    @Test
    @DisplayName("chamar o metodo Update quando Retornar com Sucesso")
    void shouldCallLoadFilesServiceWhenUpdateReturnWithSuccess() throws Exception {
        //Given
        LoadFilesDto loadFilesDto = LoadFilesDto.builder()
                .id(loadFilesModel.getId().longValue())
                .build();
        BDDMockito.given(loadFilesService.updateFolder(any(LoadFilesDto.class))).willReturn(loadFilesModel);
        //When/Then
        mockMvc.perform(
                put("/load/config")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loadFilesDto))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(loadFilesModel.getId()))
                .andExpect(jsonPath("$.folder").value(loadFilesModel.getFolder()))
                .andExpect(jsonPath("$.server").value(loadFilesModel.getServer()));
        then(loadFilesService).should().updateFolder(any(LoadFilesDto.class));
    }

    @Test
    @DisplayName("chamar o metodo delete quando Retornar com Sucesso")
    void shouldCallLoadFilesServiceWhenDeleteReturnWithSuccess() throws Exception {
        //Given
        BDDMockito.given(loadFilesService.deleteFolder(any(Long.class))).willReturn(any(Boolean.class));
        //When/Then
        mockMvc.perform(
                delete("/load/config/{id}", loadFilesModel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        then(loadFilesService).should().deleteFolder(any(Long.class));
    }

    @BeforeEach
    void setUp(){
        loadFilesModel = new LoadFilesModel();
        loadFilesModel.setId(0L);
        loadFilesModel.setCreatedAt(new Date());
        loadFilesModel.setServer("http://192.168.0.62/tributario");
        loadFilesModel.setFolder("CertaconWeb");
        loadFilesModel.setUpdatedAt(new Date());
    }
}
