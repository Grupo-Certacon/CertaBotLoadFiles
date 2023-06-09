package br.com.certacon.certabotloadfiles.service;

import br.com.certacon.certabotloadfiles.dto.LoadFilesDto;
import br.com.certacon.certabotloadfiles.model.LoadFilesModel;
import br.com.certacon.certabotloadfiles.model.UserFilesModel;
import br.com.certacon.certabotloadfiles.repository.UserFilesRepository;
import br.com.certacon.certabotloadfiles.utils.StatusFile;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserFilesService {
    private final UserFilesRepository userFilesRepository;

    public UserFilesService(UserFilesRepository userFilesRepository) {
        this.userFilesRepository = userFilesRepository;
    }

    public UserFilesModel getById(UUID id) {
        Optional<UserFilesModel> model = userFilesRepository.findById(id);
        UserFilesModel result = new UserFilesModel();
        try {
            result = model.get();
            if (model.isPresent()) {
                result.setStatus(StatusFile.OK);
            }
        } catch (RuntimeException e) {
            result.setStatus(StatusFile.ERROR);
            throw new RuntimeException("Objeto não encontrado!");
        }
        userFilesRepository.save(result);
        return result;
    }

    public List<UserFilesModel> getAll() {
        List<UserFilesModel> modelList = userFilesRepository.findAll();
        if (modelList == null) {
            throw new RuntimeException("Objeto(s) não encontrados!");
        }
        return modelList;
    }

    public UserFilesModel update(UserFilesModel userFilesModel) {
        UserFilesModel model;
        Optional<UserFilesModel> optionalModel = userFilesRepository.findById(userFilesModel.getId());
        if (!optionalModel.isEmpty()) {
            model = optionalModel.get();
            model.setPath(model.getPath());
            model.setCreatedAt(model.getCreatedAt());
            model.setFileName(model.getFileName());
            model.setId(model.getId());
            model.setStatus(StatusFile.UPDATED);
            userFilesRepository.save(model);
            return model;
        } else {
            throw new RuntimeException("Folder não encontrado");
        }
    }
}
