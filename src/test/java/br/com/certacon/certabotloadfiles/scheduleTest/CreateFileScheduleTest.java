package br.com.certacon.certabotloadfiles.scheduleTest;

import br.com.certacon.certabotloadfiles.component.CreateFileComponent;
import br.com.certacon.certabotloadfiles.model.LoadFilesModel;
import br.com.certacon.certabotloadfiles.repository.LoadFilesRepository;
import br.com.certacon.certabotloadfiles.repository.UserFilesRepository;
import br.com.certacon.certabotloadfiles.schedule.CreateFileSchedule;
import br.com.certacon.certabotloadfiles.utils.StatusFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@SpringJUnitConfig
@SpringBootTest(classes = {CreateFileComponent.class})
@ComponentScan(basePackages = "com.example.demo")
@EnableScheduling
class CreateFileScheduleTest {
    @MockBean
    UserFilesRepository userFilesRepository;
    @MockBean
    LoadFilesRepository loadFilesRepository;
    @SpyBean
    private CreateFileSchedule createFileSchedule;

    @Test
    @DisplayName("chamar o metodo createFileScheduled quando retornar com Verdadeiro")
    void shouldCallCreateFileScheduleWhenCreateFileScheduledReturnWithTrue() throws IOException {
        //Given
        LoadFilesModel loadFilesModel = LoadFilesModel.builder()
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
        Boolean actual = createFileSchedule.createFileScheduled();
        //Then
        assertTrue(actual);
    }

    @Test
    @DisplayName("chamar o metodo createFileScheduled quando retornar com False")
    void shouldCallCreateFileScheduleWhenCreateFileScheduledReturnWithFalse() throws IOException {
        //Given
        LoadFilesModel loadFilesModel = LoadFilesModel.builder()
                .serverFolder("192.168.0.62")
                .cnpjFolder("06333120000197")
                .yearFolder("2023")
                .path("D:\\loadFileData\\192168062\\06333120000197\\2023")
                .build();
        List<LoadFilesModel> modelList = List.of(loadFilesModel);
        //When
        BDDMockito.when(loadFilesRepository.findAll()).thenReturn(modelList);
        BDDMockito.when(loadFilesRepository.save(any(LoadFilesModel.class))).thenReturn(loadFilesModel);
        Boolean actual = createFileSchedule.createFileScheduled();
        //Then
        assertFalse(actual);
    }

}