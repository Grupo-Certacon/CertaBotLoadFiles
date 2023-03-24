package br.com.certacon.certabotloadfiles.service;

import br.com.certacon.certabotloadfiles.dto.LoadFilesDto;
import br.com.certacon.certabotloadfiles.model.LoadFilesModel;
import br.com.certacon.certabotloadfiles.repository.LoadFilesRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LoadFilesService {
    private final LoadFilesRepository loadFilesRepository;

    public LoadFilesService(LoadFilesRepository folderRepository) {
        this.loadFilesRepository = folderRepository;
    }

    public LoadFilesModel create(LoadFilesModel loadFilesModel) {
        LoadFilesModel model;
        if(loadFilesModel != null){
            loadFilesModel.setCreatedAt(new Date());
            model = loadFilesRepository.save(loadFilesModel);
        }else{
            throw new RuntimeException("Objeto não pode ser nulo");
        }
        return model;
    }

    public Optional<LoadFilesModel> getOneFolder(Long id) {
        Optional<LoadFilesModel> model = loadFilesRepository.findById(id);
        if(model.isEmpty()) throw new RuntimeException("Pasta não existe");

        return model;
    }

    public List<LoadFilesModel> getAllFolders() {
        List<LoadFilesModel> modelList = loadFilesRepository.findAll();
        if(modelList == null) throw new RuntimeException("Pasta(s) não encontradas");

        return modelList;
    }

    public LoadFilesModel updateFolder(LoadFilesDto loadFilesDto) {
        LoadFilesModel model;
        Optional<LoadFilesModel> optionalModel = loadFilesRepository.findById(loadFilesDto.getId().longValue());
        if(!optionalModel.isEmpty()) {
            model = optionalModel.get();
            model.setFolder(model.getFolder());
            model.setUpdatedAt(new Date());
            model.setCreatedAt(model.getCreatedAt());
            model.setServer(model.getServer());
            model.setId(model.getId());
            loadFilesRepository.save(model);
            return model;
        }else{
            throw new RuntimeException("Folder não encontrado");
        }
    }

    public Boolean deleteFolder(Long id) {
        Boolean isDeleted = Boolean.FALSE;
        try{
            Optional<LoadFilesModel> optionalModel = loadFilesRepository.findById(id);
            if (optionalModel.isEmpty()){
                throw new RuntimeException("Folder não encontrado");
            } else{
                isDeleted = Boolean.TRUE;
                loadFilesRepository.delete(optionalModel.get());
            }

        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }finally {
            return isDeleted;
        }
    }
}
