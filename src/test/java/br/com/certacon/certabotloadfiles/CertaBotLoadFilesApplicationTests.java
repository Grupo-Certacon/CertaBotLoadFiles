package br.com.certacon.certabotloadfiles;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CertaBotLoadFilesApplicationTests {

    @Test
    void main() {
        CertaBotLoadFilesApplication.main(new String[]{});
    }

}
