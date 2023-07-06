package br.com.certacon.certabotloadfiles.schedule;

import br.com.certacon.certabotloadfiles.model.UserFilesModel;
import br.com.certacon.certabotloadfiles.repository.UserFilesRepository;
import br.com.certacon.certabotloadfiles.service.PostRestTemplateService;
import br.com.certacon.certabotloadfiles.utils.StatusFile;
import br.com.certacon.certabotloadfiles.vo.FileEntityVO;
import br.com.certacon.certabotloadfiles.vo.FileVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostRestTemplateSchedule {

    private final PostRestTemplateService postRestTemplateService;
    private final UserFilesRepository userFilesRepository;

    public PostRestTemplateSchedule(PostRestTemplateService postRestTemplateService, UserFilesRepository userFilesRepository) {

        this.postRestTemplateService = postRestTemplateService;
        this.userFilesRepository = userFilesRepository;
    }

    public boolean postRest() {

        List<UserFilesModel> modelList = userFilesRepository.findAll();
        boolean check = Boolean.FALSE;
        if (modelList.size() > 0) {
            for (int i = 0; i < modelList.size(); i++) {
                modelList.get(i).getId().toString();
                if (modelList.get(i).getStatus() == StatusFile.CREATED || modelList.get(i).getStatus() == StatusFile.UPDATED) {
                    FileVO fileVO = FileVO.builder()
                            .cnpj(modelList.get(i).getCnpj())
                            .companyName(modelList.get(i).getCompanyName())
                            .ipServer(modelList.get(i).getIpServer())
                            .fileName(modelList.get(i).getFileName())
                            .filePath(modelList.get(i).getPath())
                            .build();
                    FileEntityVO result = postRestTemplateService.createFile(fileVO).getBody();
                    if (result != null) {
                        modelList.get(i).setStatus(StatusFile.UPLOADED);
                        check = Boolean.TRUE;
                    } else {
                        modelList.get(i).setStatus(StatusFile.ERROR);
                    }
                } else {
                    modelList.get(i).setStatus(StatusFile.ERROR);
                }
                userFilesRepository.save(modelList.get(i));
            }
        }
        return check;
    }

}
