package br.com.certacon.certabotloadfiles.scheduleTest;

import br.com.certacon.certabotloadfiles.component.PathCreationComponent;
import br.com.certacon.certabotloadfiles.model.LoadFilesModel;
import br.com.certacon.certabotloadfiles.repository.LoadFilesRepository;
import br.com.certacon.certabotloadfiles.schedule.PathCreationSchedule;
import br.com.certacon.certabotloadfiles.utils.StatusFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = {PathCreationComponent.class})
public class PathCreationScheduleTest {
    @MockBean
    LoadFilesRepository loadFilesRepository;
    @SpyBean
    PathCreationSchedule pathCreationSchedule;

    @Test
    @DisplayName("chamar o metodo PathCreate quando retornar com Falso")
    void shouldCallPathCreationScheduleWhenPathCreateReturnFalse() {
        //Given
        LoadFilesModel loadFilesModel = LoadFilesModel.builder()
                .id(UUID.randomUUID())
                .serverFolder("192.168.0.62")
                .cnpjFolder("06333120000197")
                .yearFolder("2023")
                .path("D:\\loadFileData\\192168062\\06333120000197\\2023")
                .status(StatusFile.CREATED)
                .build();
        List<LoadFilesModel> modelList = List.of(loadFilesModel);
        //When
        BDDMockito.when(loadFilesRepository.findAll()).thenReturn(modelList);
        BDDMockito.when(loadFilesRepository.save(any(LoadFilesModel.class))).thenReturn(loadFilesModel);
        Boolean actual = pathCreationSchedule.pathCreate();
        //Then
        assertFalse(actual);
    }

    @Test
    @DisplayName("chamar o metodo PathCreate quando retornar com Verdadeiro")
    void shouldCallPathCreationScheduleWhenPathCreateReturnTrue() {
        //Given
        LoadFilesModel loadFilesModel = LoadFilesModel.builder()
                .id(UUID.fromString("c73a80ac-c440-4455-ad0d-c90cb7983ba1"))
                .serverFolder("192.168.0.62")
                .cnpjFolder("06333120000197")
                .yearFolder("2023")
                .path("D:\\loadFileData\\192168062\\06333120000197\\2023")
                .status(StatusFile.CREATED)
                .build();
        List<LoadFilesModel> modelList = List.of(loadFilesModel);
        //When
        BDDMockito.when(loadFilesRepository.findAll()).thenReturn(modelList);
        BDDMockito.when(loadFilesRepository.save(any(LoadFilesModel.class))).thenReturn(loadFilesModel);
        Boolean actual = pathCreationSchedule.pathCreate();
        //Then
        assertFalse(actual);
    }
}
