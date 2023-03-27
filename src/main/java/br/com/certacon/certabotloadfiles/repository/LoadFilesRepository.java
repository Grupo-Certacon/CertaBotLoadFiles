package br.com.certacon.certabotloadfiles.repository;

import br.com.certacon.certabotloadfiles.model.LoadFilesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LoadFilesRepository extends JpaRepository<LoadFilesModel, UUID> {
}
