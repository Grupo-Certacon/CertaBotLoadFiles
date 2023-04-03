package br.com.certacon.certabotloadfiles.controllerTest;

import br.com.certacon.certabotloadfiles.controller.DownloadFileController;
import br.com.certacon.certabotloadfiles.model.UserFilesModel;
import br.com.certacon.certabotloadfiles.service.DownloadFileService;
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

import java.util.Date;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DownloadFileController.class)
public class DownloadFileControllerTest {

    @MockBean
    DownloadFileService downloadFileService;
    @Autowired
    ObjectMapper objectMapper;
    UserFilesModel userFileModel;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("chamar o metodo GetById quando retornar com Sucesso")
    void shouldCallDownloadFileControllerWhenGetByIdReturnWithSuccess() throws Exception {
        //Given
        BDDMockito.given(downloadFileService.getById(any(UUID.class))).willReturn(userFileModel);
        //When/Then
        mockMvc.perform(
                get("/downloads/{id}", userFileModel.getId())
                        .param("id", userFileModel.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userFileModel))
        ).andExpect(status().isOk());
    }

    @BeforeEach
    void setUp() {
        userFileModel = UserFilesModel.builder()
                .id(UUID.randomUUID())
                .fileName("fileTest.zip")
                .createdAt(new Date())
                .path("D:\\loadFileData\\192168062\\06333120000197\\2023")
                .build();
    }

}
