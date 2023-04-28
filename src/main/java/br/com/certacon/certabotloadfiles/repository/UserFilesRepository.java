package br.com.certacon.certabotloadfiles.repository;

import br.com.certacon.certabotloadfiles.model.UserFilesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserFilesRepository extends JpaRepository<UserFilesModel, UUID> {
    Optional<UserFilesModel> findByFileName(String fileName);
}
