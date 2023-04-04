package br.com.certacon.certabotloadfiles.serviceTest;

import br.com.certacon.certabotloadfiles.service.PostRestTemplateService;
import br.com.certacon.certabotloadfiles.vo.ArquivoEfdModelVO;
import br.com.certacon.certabotloadfiles.vo.ArquivoEfdVO;
import br.com.certacon.certabotloadfiles.vo.ProcessFileModelVO;
import br.com.certacon.certabotloadfiles.vo.ProcessFileVO;
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
        ArquivoEfdVO arquivoEfdVO = ArquivoEfdVO.builder()
                .name("batatinha")
                .clientCnpj("03.189.421/0001-65")
                .build();
        ArquivoEfdModelVO arquivoEfdModelVO = ArquivoEfdModelVO.builder()
                .build();
        ResponseEntity<ArquivoEfdModelVO> expected = ResponseEntity.ok().body(arquivoEfdModelVO);
        //When
        ResponseEntity<ArquivoEfdModelVO> actual = postRestTemplateService.enviarArquivoEfd(arquivoEfdVO);
        //Then
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }

    @Test
    @DisplayName("chamar o metodo EnviarArquivoEfd quando retornar com NotFound")
    void shouldCallPostRestTemplateServiceWhenEnviarArquivoEfdReturnWithNotFound() {
        //Given
        RuntimeException expected = new RuntimeException("Objeto não encontrado");
        //When
        RuntimeException actual = assertThrows(RuntimeException.class, () -> postRestTemplateService.enviarArquivoEfd(null));
        //Then
        assertEquals(expected.getMessage(), actual.getMessage());
    }

    @Test
    @DisplayName("chamar o metodo CreateProcess quando Retornar com Sucesso")
    void shouldCallPostRestTemplateServiceWhenCreateProcessReturnWithSuccess() {
        //Given
        ProcessFileVO processFileVO = ProcessFileVO.builder()
                .url_de_download("http://192.168.0.62/0008.zip")
                .senha("1")
                .url_de_upload("http://192.168.0.62/tributario")
                .usuario("giovanni.andrade@certacon.com.br")
                .caminho_de_destino_download(downloadPath)
                .nome_arquivo("0008.zip")
                .id_arquivo("1cd27c3a-9a0f-49a5-bd2b-e987252b7def")
                .build();
        ProcessFileModelVO processFileModelVO = ProcessFileModelVO.builder().build();
        ResponseEntity<ProcessFileModelVO> expected = ResponseEntity.ok().body(processFileModelVO);
        //Then
        ResponseEntity<ProcessFileModelVO> actual = postRestTemplateService.createProcess(processFileVO);
        //When
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }

    @Test
    @DisplayName("chamar o metodo CreateProcess quando retornar com NotFound")
    void shouldCallPostRestTemplateServiceWhenCreateProcessReturnWithNotFound() {
        //Given
        RuntimeException expected = new RuntimeException("Objeto não encontrado");
        //When
        RuntimeException actual = assertThrows(RuntimeException.class, () -> postRestTemplateService.createProcess(null));
        //Then
        assertEquals(expected.getMessage(), actual.getMessage());
    }
}
