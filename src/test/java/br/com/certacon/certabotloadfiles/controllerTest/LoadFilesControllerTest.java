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

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

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
    @MockBean
    LoadFilesService loadFilesService;
    @MockBean
    LoadFilesModel loadFilesModel;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("chamar o metodo create quando retornar com Sucesso")
    void shouldCallLoadFolderControllerWhenCreateReturnWithSuccess() throws Exception {
        //Given
        BDDMockito.given(loadFilesService.create(any(LoadFilesModel.class))).willReturn(loadFilesModel);
        //When/Then
        mockMvc.perform(
                        post("/load/config")
                                .param("id", loadFilesModel.getId().toString())
                                .param("serverFolder", loadFilesModel.getServerFolder())
                                .param("cnpjFolder", loadFilesModel.getCnpjFolder())
                                .param("yearFolder", loadFilesModel.getYearFolder())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loadFilesModel))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.serverFolder").value(loadFilesModel.getServerFolder()))
                .andExpect(jsonPath("$.cnpjFolder").value(loadFilesModel.getCnpjFolder()))
                .andExpect(jsonPath("$.yearFolder").value(loadFilesModel.getYearFolder()));
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
                                .param("serverFolder", loadFilesModel.getServerFolder())
                                .param("cnpjFolder", loadFilesModel.getCnpjFolder())
                                .param("yearFolder", loadFilesModel.getYearFolder())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loadFilesModel))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(loadFilesModel.getId().toString()))
                .andExpect(jsonPath("$[0].serverFolder").value(loadFilesModel.getServerFolder()))
                .andExpect(jsonPath("$[0].cnpjFolder").value(loadFilesModel.getCnpjFolder()))
                .andExpect(jsonPath("$[0].yearFolder").value(loadFilesModel.getYearFolder()));
        then(loadFilesService).should().getAllFolders();
    }

    @Test
    @DisplayName("chamar o metodo GetById quando Retornar com Sucesso")
    void shouldCallLoadFilesServiceWhenGetByIdReturnWithSuccess() throws Exception {
        //Given
        BDDMockito.given(loadFilesService.getOneFolder(any(UUID.class))).willReturn(Optional.of(loadFilesModel));
        //When/Then
        mockMvc.perform(
                        get("/load/config/{id}", loadFilesModel.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.serverFolder").value(loadFilesModel.getServerFolder()))
                .andExpect(jsonPath("$.cnpjFolder").value(loadFilesModel.getCnpjFolder()))
                .andExpect(jsonPath("$.yearFolder").value(loadFilesModel.getYearFolder()));
        then(loadFilesService).should().getOneFolder(any(UUID.class));
    }

    @Test
    @DisplayName("chamar o metodo Update quando Retornar com Sucesso")
    void shouldCallLoadFilesServiceWhenUpdateReturnWithSuccess() throws Exception {
        //Given
        LoadFilesDto loadFilesDto = LoadFilesDto.builder()
                .id(loadFilesModel.getId())
                .build();
        BDDMockito.given(loadFilesService.updateFolder(any(LoadFilesDto.class))).willReturn(loadFilesModel);
        //When/Then
        mockMvc.perform(
                        put("/load/config")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loadFilesDto))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.serverFolder").value(loadFilesModel.getServerFolder()))
                .andExpect(jsonPath("$.cnpjFolder").value(loadFilesModel.getCnpjFolder()))
                .andExpect(jsonPath("$.yearFolder").value(loadFilesModel.getYearFolder()));
        then(loadFilesService).should().updateFolder(any(LoadFilesDto.class));
    }

    @Test
    @DisplayName("chamar o metodo delete quando Retornar com Sucesso")
    void shouldCallLoadFilesServiceWhenDeleteReturnWithSuccess() throws Exception {
        //Given
        BDDMockito.given(loadFilesService.deleteFolder(any(UUID.class))).willReturn(any(Boolean.class));
        //When/Then
        mockMvc.perform(
                delete("/load/config/{id}", loadFilesModel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        then(loadFilesService).should().deleteFolder(any(UUID.class));
    }

    @BeforeEach
    void setUp() {
        loadFilesModel = new LoadFilesModel();
        loadFilesModel.setId(UUID.randomUUID());
        loadFilesModel.setCreatedAt(new Date());
        loadFilesModel.setServerFolder("192.168.0.62");
        loadFilesModel.setCnpjFolder("1928321231");
        loadFilesModel.setYearFolder("2004");
        loadFilesModel.setPath("/Home/Selenium/Downloads");
        loadFilesModel.setUpdatedAt(new Date());
    }
}
