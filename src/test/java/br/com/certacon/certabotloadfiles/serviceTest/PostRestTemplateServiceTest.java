package br.com.certacon.certabotloadfiles.serviceTest;

import br.com.certacon.certabotloadfiles.service.PostRestTemplateService;
import br.com.certacon.certabotloadfiles.vo.FileEntityVO;
import br.com.certacon.certabotloadfiles.vo.FileVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {PostRestTemplateService.class})
class PostRestTemplateServiceTest {
    @Autowired
    private PostRestTemplateService postRestTemplateService;

    @Value("$.{config.downloadPath}")
    private String downloadPath;

    @Test
    @DisplayName("chamar o metodo EnviarArquivoEfd quando retornar com Sucesso")
    void shouldCallPostRestTemplateServiceWhenEnviarArquivoEfdReturnWithSuccess() {
        //Given
        FileVO fileVO = FileVO.builder()
                .fileName("batatinha")
                .cnpj("03.189.421/0001-65")
                .build();
        FileEntityVO fileEntityVO = FileEntityVO.builder()
                .build();
        ResponseEntity<FileEntityVO> expected = ResponseEntity.ok().body(fileEntityVO);
        //When
        ResponseEntity<FileEntityVO> actual = postRestTemplateService.createFile(fileVO);
        //Then
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }

    @Test
    @DisplayName("chamar o metodo EnviarArquivoEfd quando retornar com NotFound")
    void shouldCallPostRestTemplateServiceWhenEnviarArquivoEfdReturnWithNotFound() {
        //Given
        RuntimeException expected = new RuntimeException("Objeto não encontrado");
        //When
        RuntimeException actual = assertThrows(RuntimeException.class, () -> postRestTemplateService.createFile(null));
        //Then
        assertEquals(expected.getMessage(), actual.getMessage());
    }

}
