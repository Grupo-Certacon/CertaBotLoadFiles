package br.com.certacon.certabotloadfiles.service;

import br.com.certacon.certabotloadfiles.exception.BadRequestException;
import br.com.certacon.certabotloadfiles.vo.FileEntityVO;
import br.com.certacon.certabotloadfiles.vo.FileVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PostRestTemplateService {

    private final String restBuilder;
    private RestTemplate restTemplate;

    public PostRestTemplateService(@Value("${config.restBuilder}") String restBuilder) {
        this.restBuilder = restBuilder;
        this.restTemplate = new RestTemplateBuilder().rootUri(restBuilder).build();
    }

    public ResponseEntity<FileEntityVO> createFile(FileVO fileVO) {
        ResponseEntity<FileEntityVO> resposta = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<FileVO> requestEntity = new HttpEntity<>(fileVO, headers);
            resposta = restTemplate.exchange("/files", HttpMethod.POST, requestEntity, FileEntityVO.class);
        } catch (BadRequestException e) {
            throw new BadRequestException();
        }
        return resposta;
    }
}
