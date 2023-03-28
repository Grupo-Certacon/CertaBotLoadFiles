package br.com.certacon.certabotloadfiles.componentTest;


import br.com.certacon.certabotloadfiles.component.CreateFileComponent;
import br.com.certacon.certabotloadfiles.model.UserFilesModel;
import br.com.certacon.certabotloadfiles.repository.UserFilesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private CreateFileComponent createFileComponent;
    @Value("${config.rootPath}")
    private String rootPath;

    @Test
    @DisplayName("chamar o componente createFile quando Retornar com Falso")
    void shouldCallCreateFileComponentWhenCreateFileReturnWithFalse() {
        //Given
        UserFilesModel userModel = UserFilesModel.builder()
                .id(UUID.randomUUID())
                .path("192168062\\1273821329\\2021\\")
                .fileName("SpedEFD-43408590000107-669068365110-Remessa de arquivo original-abr2022.txt")
                .build();
        //When
        BDDMockito.when(userFilesRepository.findById(any(UUID.class))).thenReturn(Optional.of(userModel));
        BDDMockito.when(userFilesRepository.findByFileNameAndPath(any(String.class), any(String.class))).thenReturn(Optional.of(userModel));
        BDDMockito.when(userFilesRepository.save(any(UserFilesModel.class))).thenReturn(userModel);
        Boolean actual = createFileComponent.checkFile(userModel.getPath(), userModel.getFileName());
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
                .fileName("NovoArquivoTeste.txt")
                .build();
        //When
        BDDMockito.when(userFilesRepository.findById(any(UUID.class))).thenReturn(Optional.of(userModel));
        BDDMockito.when(userFilesRepository.save(any(UserFilesModel.class))).thenReturn(userModel);
        Boolean actual = createFileComponent.checkFile(userModel.getPath(), userModel.getFileName());
        //Then
        assertTrue(actual);
    }

    @BeforeEach
    void setUp() {
        createFileComponent = new CreateFileComponent(rootPath, userFilesRepository);
    }

}
