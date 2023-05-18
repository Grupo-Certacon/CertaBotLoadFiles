package br.com.certacon.certabotloadfiles.componentTest;

import br.com.certacon.certabotloadfiles.component.PathCreationComponent;
import br.com.certacon.certabotloadfiles.configuration.Properties;
import br.com.certacon.certabotloadfiles.helper.PathCreationHelper;
import br.com.certacon.certabotloadfiles.model.LoadFilesModel;
import br.com.certacon.certabotloadfiles.repository.FileTypeRepository;
import br.com.certacon.certabotloadfiles.repository.LoadFilesRepository;
import br.com.certacon.certabotloadfiles.utils.FileFunctions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = {PathCreationComponent.class, Properties.class})
public class PathCreationComponentTest {
    @Autowired
    PathCreationComponent pathCreationComponent;
    @MockBean
    LoadFilesRepository loadFilesRepository;
    @MockBean
    FileTypeRepository fileTypeRepository;

    @MockBean
    PathCreationHelper helper;


    @Test
    @DisplayName("chamar o componente checkPath quando retornar com Falso")
    void shouldCallPathCreationComponentWhenCreatePathReturnWithFalse() {
        //Given
        LoadFilesModel model = LoadFilesModel.builder()
                .id(UUID.randomUUID())
                .serverFolder("192.168.0.62")
                .cnpjFolder("0633.3120.000.19-7")
                .yearFolder("2023")
                .build();
        //When
        BDDMockito.when(loadFilesRepository.findById(any(UUID.class))).thenReturn(Optional.of(model));
        BDDMockito.when(loadFilesRepository.save(any(LoadFilesModel.class))).thenReturn(model);
        LoadFilesModel actual = pathCreationComponent.createLoadPath(model.getId());
        //Then
        assertEquals(model, actual);
    }

    @Test
    @DisplayName("chamar o componente checkPath quando retornar com Verdadeiro")
    void shouldCallPathCreationComponentWhenCreatePathReturnWithException() {
        //Given
        LoadFilesModel model = LoadFilesModel.builder()
                .id(UUID.randomUUID())
                .serverFolder("192.168.0.61")
                .cnpjFolder("12.738.213/2-9")
                .yearFolder("2021")
                .build();
        //When
        BDDMockito.when(loadFilesRepository.findById(any(UUID.class))).thenReturn(Optional.of(model));
        BDDMockito.when(loadFilesRepository.save(any(LoadFilesModel.class))).thenReturn(model);
        BDDMockito.when(helper.directoryCreator(any(LoadFilesModel.class), any(FileFunctions.class))).thenReturn(Path.of("D:\\CARREGAMENTO\\192168062\\1273821329\\2021"));
        LoadFilesModel actual = pathCreationComponent.createLoadPath(model.getId());
        //Then
        assertEquals(model, actual);
    }

    @BeforeEach
    void setUp() {
        pathCreationComponent = new PathCreationComponent(loadFilesRepository, helper);
    }

}
