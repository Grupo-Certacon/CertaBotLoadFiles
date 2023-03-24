package br.com.certacon.certabotloadfiles.serviceTest;

import br.com.certacon.certabotloadfiles.repository.LoadFilesRepository;
import br.com.certacon.certabotloadfiles.service.CheckFolderExistService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class CheckFolderExistServiceTest {
    @MockBean
    CheckFolderExistService checkFolderExistService;
    @MockBean
    LoadFilesRepository checkFolderRepository;
    @BeforeEach
    void setUp() {
        checkFolderExistService = new CheckFolderExistService(checkFolderRepository);
    }

    @AfterEach
    void tearDown() {
        checkFolderExistService = null;
    }
}
