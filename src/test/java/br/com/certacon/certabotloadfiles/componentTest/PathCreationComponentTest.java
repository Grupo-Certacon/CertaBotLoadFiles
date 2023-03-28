package br.com.certacon.certabotloadfiles.componentTest;

import br.com.certacon.certabotloadfiles.component.PathCreationComponent;
import br.com.certacon.certabotloadfiles.configuration.Properties;
import br.com.certacon.certabotloadfiles.model.LoadFilesModel;
import br.com.certacon.certabotloadfiles.repository.LoadFilesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = {PathCreationComponent.class, Properties.class})
public class PathCreationComponentTest {
    @Autowired
    PathCreationComponent pathCreationComponent;
    @MockBean
    LoadFilesRepository loadFilesRepository;

    @Value("${config.rootPath}")
    private String rootPath;

    @Test
    @DisplayName("chamar o componente checkPath quando retornar com Falso")
    void shouldCallPathCreationComponentWhenCreatePathReturnWithFalse() {
        //Given
        LoadFilesModel model = LoadFilesModel.builder()
                .id(UUID.randomUUID())
                .serverFolder("192.168.0.62")
                .cnpjFolder("127.382.132.9")
                .yearFolder("2021")
                .build();
        //When
        BDDMockito.when(loadFilesRepository.findById(any(UUID.class))).thenReturn(Optional.of(model));
        BDDMockito.when(loadFilesRepository.save(any(LoadFilesModel.class))).thenReturn(model);
        Boolean actual = pathCreationComponent.checkPath(model.getId());
        //Then
        assertFalse(actual);
    }

    @Test
    @DisplayName("chamar o componente checkPath quando retornar com Verdadeiro")
    void shouldCallPathCreationComponentWhenCreatePathReturnWithTrue() {
        //Given
        LoadFilesModel model = LoadFilesModel.builder()
                .id(UUID.randomUUID())
                .serverFolder("192.168.0.61")
                .cnpjFolder("127.382.132.9")
                .yearFolder("2021")
                .build();
        //When
        BDDMockito.when(loadFilesRepository.findById(any(UUID.class))).thenReturn(Optional.of(model));
        BDDMockito.when(loadFilesRepository.save(any(LoadFilesModel.class))).thenReturn(model);
        Boolean actual = pathCreationComponent.checkPath(model.getId());
        //Then
        assertTrue(actual);
    }

    @BeforeEach
    void setUp() {
        pathCreationComponent = new PathCreationComponent(rootPath, loadFilesRepository);
    }

}
