package br.com.certacon.certabotloadfiles.service;

import br.com.certacon.certabotloadfiles.exception.BadRequestException;
import br.com.certacon.certabotloadfiles.vo.ArquivoEfdModelVO;
import br.com.certacon.certabotloadfiles.vo.ArquivoEfdVO;
import br.com.certacon.certabotloadfiles.vo.ProcessFileModelVO;
import br.com.certacon.certabotloadfiles.vo.ProcessFileVO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PostRestTemplateService {
    private RestTemplate restTemplate;

    public PostRestTemplateService() {
        this.restTemplate = new RestTemplateBuilder().rootUri("http://192.168.0.104:8092/certabot").build();
    }

    public ResponseEntity<ArquivoEfdModelVO> enviarArquivoEfd(ArquivoEfdVO arquivoEfdVO) {
        ResponseEntity<ArquivoEfdModelVO> resposta = null;
        try {
            // Crie um objeto HttpEntity contendo o objeto ArquivoEfdVO como corpo da solicitação POST
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ArquivoEfdVO> requestEntity = new HttpEntity<>(arquivoEfdVO, headers);
            // Faça a solicitação POST e obtenha a resposta como uma instância de ArquivoEfdVO
            resposta = restTemplate.exchange("/arquivo/efd", HttpMethod.POST, requestEntity, ArquivoEfdModelVO.class);
        } catch (RuntimeException e) {
            throw new RuntimeException("Objeto não encontrado");
        }
        return resposta;
    }

    public ResponseEntity<ProcessFileModelVO> createProcess(ProcessFileVO processFileVO) {
        ResponseEntity<ProcessFileModelVO> resposta = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ProcessFileVO> requestEntity = new HttpEntity<>(processFileVO, headers);
            // Faça a solicitação POST e obtenha a resposta como uma instância de ArquivoEfdVO
            resposta = restTemplate.exchange("/process/file", HttpMethod.POST, requestEntity, ProcessFileModelVO.class);
        } catch (BadRequestException e) {
            throw new BadRequestException();
        }
        return resposta;
    }
}
