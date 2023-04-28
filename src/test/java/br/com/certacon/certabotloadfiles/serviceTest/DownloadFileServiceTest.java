package br.com.certacon.certabotloadfiles.serviceTest;

import br.com.certacon.certabotloadfiles.model.UserFilesModel;
import br.com.certacon.certabotloadfiles.repository.UserFilesRepository;
import br.com.certacon.certabotloadfiles.service.DownloadFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class DownloadFileServiceTest {

    @MockBean
    DownloadFileService downloadFileService;

    @MockBean
    UserFilesRepository userFilesRepository;

    UserFilesModel userFilesModel;

    @Test
    @DisplayName("chamar o metodo getById quando retornar com Sucesso")
    void shouldCallDownloadFileServiceWhenGetByIdReturnWithSuccess() {
        //Given
        UserFilesModel expected = userFilesModel;
        //When
        BDDMockito.when(userFilesRepository.findById(any(UUID.class))).thenReturn(Optional.of(userFilesModel));
        UserFilesModel actual = downloadFileService.getById(userFilesModel.getId());
        //Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("chamar o metodo getById quando retornar com Falha")
    void shouldCallDownloadFileServiceWhenGetByIdReturnWithFail() {
        //Given
        Exception expected = new RuntimeException("Objeto não foi encontrado!");
        //When
        BDDMockito.when(userFilesRepository.findById(any(UUID.class))).thenReturn(Optional.of(userFilesModel));
        RuntimeException actual = assertThrows(RuntimeException.class, () -> downloadFileService.getById(null));
        //Then
        assertEquals(expected.getMessage(), actual.getMessage());
    }

    @BeforeEach
    void setUp() {
        downloadFileService = new DownloadFileService(userFilesRepository);
        userFilesModel = UserFilesModel.builder()
                .id(UUID.randomUUID())
                .build();
    }
}
