package br.com.certacon.certabotloadfiles.componentTest;


import br.com.certacon.certabotloadfiles.component.CreateFileComponent;
import br.com.certacon.certabotloadfiles.component.UnzipAndZipComponent;
import br.com.certacon.certabotloadfiles.model.UserFilesModel;
import br.com.certacon.certabotloadfiles.repository.FileTypeRepository;
import br.com.certacon.certabotloadfiles.repository.UserFilesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = {CreateFileComponent.class, Properties.class, UnzipAndZipComponent.class})
public class CreateFileComponentTest {
    @MockBean
    FileTypeRepository fileTypeRepository;
    @Autowired
    UnzipAndZipComponent unzipAndZipComponent;
    @MockBean
    UserFilesRepository userFilesRepository;

    @Autowired
    CreateFileComponent createFileComponent;
    @Value("${config.rootPath}")
    private String rootPath;

    @Test
    @DisplayName("chamar o componente createFile quando Retornar com Falso")
    void shouldCallCreateFileComponentWhenCreateFileReturnWithFalse() throws IOException {
        //Given
        UserFilesModel userModel = UserFilesModel.builder()
                .id(UUID.randomUUID())
                .path("D:\\loadFileData\\192168062\\06333120000197\\2023")
                .build();
        //When
        BDDMockito.when(userFilesRepository.findById(any(UUID.class))).thenReturn(Optional.of(userModel));
        BDDMockito.when(userFilesRepository.save(any(UserFilesModel.class))).thenReturn(userModel);
        BDDMockito.when(userFilesRepository.findByFileName(any(String.class))).thenReturn(Optional.of(userModel));
        Boolean actual = createFileComponent.checkFile(userModel.getPath(), userModel.getCnpj(), userModel.getIpServer(), userModel.getYear());
        //Then
        assertFalse(actual);
    }

    @Test
    @DisplayName("chamar o componente createFile quando Retornar com True")
    void shouldCallCreateFileComponentWhenCreateFileReturnWithTrue() throws IOException {
        //Given
        UserFilesModel userModel = UserFilesModel.builder()
                .id(UUID.randomUUID())
                .path(rootPath + "192168062\\06333120000197\\2023\\")
                .fileName("helloTest.txt")
                .build();
        //When
        BDDMockito.when(userFilesRepository.findById(any(UUID.class))).thenReturn(Optional.of(userModel));
        BDDMockito.when(userFilesRepository.save(any(UserFilesModel.class))).thenReturn(userModel);
        BDDMockito.when(userFilesRepository.findByFileName(any(String.class))).thenReturn(Optional.empty());
        Boolean actual = createFileComponent.checkFile(userModel.getPath(), userModel.getCnpj(), userModel.getIpServer(), userModel.getYear());
        //Then
        assertTrue(actual);
    }

    @BeforeEach
    void setUp() {
        createFileComponent = new CreateFileComponent(fileTypeRepository, unzipAndZipComponent, userFilesRepository);
    }

}
