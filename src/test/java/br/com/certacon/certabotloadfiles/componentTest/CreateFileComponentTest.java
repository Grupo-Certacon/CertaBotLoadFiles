package br.com.certacon.certabotloadfiles.componentTest;


import br.com.certacon.certabotloadfiles.component.CreateFileComponent;
import br.com.certacon.certabotloadfiles.model.UserFilesModel;
import br.com.certacon.certabotloadfiles.repository.UserFilesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = {CreateFileComponent.class, Properties.class})
public class CreateFileComponentTest {
    @MockBean
    UserFilesRepository userFilesRepository;
    @Autowired
    CreateFileComponent createFileComponent;

    @Test
    @DisplayName("chamar o componente createFile quando Retornar com Falso")
    void shouldCallCreateFileComponentWhenCreateFileReturnWithFalse() {
        //Given
        UserFilesModel userModel = UserFilesModel.builder()
                .id(UUID.randomUUID())
                .path("D:\\loadFileData\\1921680512\\1234569874323\\2020")
                .build();
        //When
        BDDMockito.when(userFilesRepository.findById(any(UUID.class))).thenReturn(Optional.of(userModel));
        BDDMockito.when(userFilesRepository.save(any(UserFilesModel.class))).thenReturn(userModel);
        BDDMockito.when(userFilesRepository.findByFileName(any(String.class))).thenReturn(Optional.of(userModel));
        Boolean actual = createFileComponent.checkFile(userModel.getPath());
        //Then
        assertFalse(actual);
    }

    @Test
    @DisplayName("chamar o componente createFile quando Retornar com True")
    void shouldCallCreateFileComponentWhenCreateFileReturnWithTrue() {
        //Given
        UserFilesModel userModel = UserFilesModel.builder()
                .id(UUID.randomUUID())
                .path("192168062\\1273821329\\2021\\")
                .build();
        //When
        BDDMockito.when(userFilesRepository.findById(any(UUID.class))).thenReturn(Optional.of(userModel));
        BDDMockito.when(userFilesRepository.save(any(UserFilesModel.class))).thenReturn(userModel);
        BDDMockito.when(userFilesRepository.findByFileName(any(String.class))).thenReturn(Optional.empty());
        Boolean actual = createFileComponent.checkFile(userModel.getPath());
        //Then
        assertTrue(actual);
    }

    @BeforeEach
    void setUp() {
        createFileComponent = new CreateFileComponent(userFilesRepository);
    }

}
