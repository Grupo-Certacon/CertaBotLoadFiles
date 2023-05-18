package br.com.certacon.certabotloadfiles.service;

import br.com.certacon.certabotloadfiles.exception.BadRequestException;
import br.com.certacon.certabotloadfiles.vo.FileEntityVO;
import br.com.certacon.certabotloadfiles.vo.FileVO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PostRestTemplateService {
    private RestTemplate restTemplate;

    public PostRestTemplateService() {
        this.restTemplate = new RestTemplateBuilder().rootUri("http://192.168.1.46:8068/CertaBotOrganize").build();
    }

    public ResponseEntity<FileEntityVO> createFile(FileVO fileVO) {
        ResponseEntity<FileEntityVO> resposta = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<FileVO> requestEntity = new HttpEntity<>(fileVO, headers);
            // Faça a solicitação POST e obtenha a resposta como uma instância de ArquivoEfdVO
            resposta = restTemplate.exchange("/files", HttpMethod.POST, requestEntity, FileEntityVO.class);
        } catch (BadRequestException e) {
            throw new BadRequestException();
        }
        return resposta;
    }
}
