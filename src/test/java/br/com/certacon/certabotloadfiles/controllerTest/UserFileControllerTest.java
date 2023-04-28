package br.com.certacon.certabotloadfiles.controllerTest;

import br.com.certacon.certabotloadfiles.controller.UserFileController;
import br.com.certacon.certabotloadfiles.model.UserFilesModel;
import br.com.certacon.certabotloadfiles.service.UserFilesService;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserFileController.class)
public class UserFileControllerTest {
    @MockBean
    UserFilesService userFilesService;

    UserFilesModel userFilesModel;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;


    @Test
    @DisplayName("Chamar o metodo GetById quando Retornar com Sucesso")
    void shouldCallUserFileControllerWhenGetByIdReturnWithSuccess() throws Exception {
        //Given
        BDDMockito.given(userFilesService.getById(any(UUID.class))).willReturn(userFilesModel);
        //When/Then
        mockMvc.perform(
                        get("/files/{id}", userFilesModel.getId())
                                .param("user_id", userFilesModel.getId().toString())
                                .param("caminho", userFilesModel.getPath())
                                .param("nome_arquivo", userFilesModel.getFileName())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userFilesModel))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(userFilesModel.getId().toString()))
                .andExpect(jsonPath("$.caminho").value(userFilesModel.getPath()))
                .andExpect(jsonPath("$.nome_arquivo").value(userFilesModel.getFileName()));
        then(userFilesService).should().getById(any(UUID.class));
    }

    @Test
    @DisplayName("Chamar o metodo GetAll quando Retornar com Sucesso")
    void shouldCallUserFileControllerWhenGetAllReturnWithSuccess() throws Exception {
        //Given
        BDDMockito.given(userFilesService.getAll()).willReturn(Arrays.asList(userFilesModel));
        //When/Then
        mockMvc.perform(
                        get("/files")
                                .param("user_id", userFilesModel.getId().toString())
                                .param("caminho", userFilesModel.getPath())
                                .param("nome_arquivo", userFilesModel.getFileName())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userFilesModel))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user_id").value(userFilesModel.getId().toString()))
                .andExpect(jsonPath("$[0].caminho").value(userFilesModel.getPath()))
                .andExpect(jsonPath("$[0].nome_arquivo").value(userFilesModel.getFileName()));
        then(userFilesService).should().getAll();
    }

    @BeforeEach
    void setUp() {
        userFilesModel = UserFilesModel.builder()
                .id(UUID.randomUUID())
                .path("D:\\loadFileData\\192168062\\1273821329\\2021")
                .fileName("SpedEFD-43408590000107-669068365110-Remessa de arquivo original-abr2022.txt")
                .build();
    }
}
