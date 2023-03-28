package br.com.certacon.certabotloadfiles.serviceTest;


import br.com.certacon.certabotloadfiles.model.UserFilesModel;
import br.com.certacon.certabotloadfiles.repository.UserFilesRepository;
import br.com.certacon.certabotloadfiles.service.UserFilesService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class UserFileServiceTest {
    @MockBean
    UserFilesService userFilesService;

    @MockBean
    UserFilesRepository userFilesRepository;

    @Test
    @DisplayName("chamar o metodo getById quando Retornar com Sucesso")
    void shoulCallUserFilesServiceWhenGetByIdReturnWithSuccess() {
        //Given
        UserFilesModel expected = UserFilesModel.builder()
                .id(UUID.randomUUID())
                .build();
        //When
        BDDMockito.when(userFilesRepository.findById(any(UUID.class))).thenReturn(Optional.of(expected));
        BDDMockito.when(userFilesRepository.save(any(UserFilesModel.class))).thenReturn(expected);
        UserFilesModel actual = userFilesService.getById(expected.getId());
        //Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("chamar o metodo getById quando Retornar com Falha")
    void shoulCallUserFilesServiceWhenGetByIdReturnWithFail() {
        RuntimeException expected = new RuntimeException("Objeto não encontrado!");
        //When
        BDDMockito.when(userFilesRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        BDDMockito.when(userFilesRepository.save(any(UserFilesModel.class))).thenReturn(null);
        RuntimeException actual = assertThrows(RuntimeException.class, () -> userFilesService.getById(null));
        //Then
        assertEquals(expected.getMessage(), actual.getMessage());
    }

    @Test
    @DisplayName("chamar o metodo GetAll quando Retornar com Sucesso")
    void shouldCallUserFilesServiceWhenGetAllReturnWithSuccess() {
        //Given
        List<UserFilesModel> expected = new ArrayList<>();
        //When
        BDDMockito.when(userFilesRepository.findAll()).thenReturn(expected);
        List<UserFilesModel> actual = userFilesService.getAll();
        //Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("chamar o metodo GetAll quando Retornar com Falha")
    void shouldCallUserFilesServiceWhenGetAllReturnWithFail() {
        //Given
        RuntimeException expected = new RuntimeException("Objeto(s) não encontrados!");
        //When
        BDDMockito.when(userFilesRepository.findAll()).thenReturn(null);
        RuntimeException actual = assertThrows(RuntimeException.class, () -> userFilesService.getAll());
        //Then
        assertEquals(expected.getMessage(), actual.getMessage());
    }


    @BeforeEach
    void setUp() {
        userFilesService = new UserFilesService(userFilesRepository);
    }

    @AfterEach
    void tearDown() {
        userFilesService = null;
    }
}
