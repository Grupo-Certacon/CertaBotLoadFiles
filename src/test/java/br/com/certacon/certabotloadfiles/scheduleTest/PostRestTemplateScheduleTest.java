package br.com.certacon.certabotloadfiles.scheduleTest;

import br.com.certacon.certabotloadfiles.model.UserFilesModel;
import br.com.certacon.certabotloadfiles.repository.UserFilesRepository;
import br.com.certacon.certabotloadfiles.schedule.PostRestTemplateSchedule;
import br.com.certacon.certabotloadfiles.service.PostRestTemplateService;
import br.com.certacon.certabotloadfiles.utils.StatusFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = {PostRestTemplateService.class})
public class PostRestTemplateScheduleTest {
    @MockBean
    UserFilesRepository userFilesRepository;
    @SpyBean
    PostRestTemplateSchedule postRestTemplateSchedule;

    @Test
    @DisplayName("chamar o metodo PostRest quando retornar com Verdadeiro")
    void shouldCallPostRestTemplateScheduleWhenPostRestReturnWithTrue() {
        //Given
        UserFilesModel userFilesModel = UserFilesModel.builder()
                .id(UUID.randomUUID())
                .status(StatusFile.CREATED)
                .build();
        List<UserFilesModel> modelList = List.of(userFilesModel);
        //When
        BDDMockito.when(userFilesRepository.findAll()).thenReturn(modelList);
        BDDMockito.when(userFilesRepository.save(any(UserFilesModel.class))).thenReturn(userFilesModel);
        Boolean actual = postRestTemplateSchedule.postRest();
        //Then
        assertTrue(actual);
    }
}
