package br.com.certacon.certabotloadfiles.service;

import br.com.certacon.certabotloadfiles.model.UserFilesModel;
import br.com.certacon.certabotloadfiles.repository.UserFilesRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DownloadFileService {
    private final UserFilesRepository userFilesRepository;

    public DownloadFileService(UserFilesRepository userFilesRepository) {
        this.userFilesRepository = userFilesRepository;
    }

    public UserFilesModel getById(UUID id) {
        Optional<UserFilesModel> model = userFilesRepository.findById(id);
        if (!model.isPresent()) {
            throw new RuntimeException("Objeto não foi encontrado!");
        }
        UserFilesModel result = model.get();
        return result;
    }
}
