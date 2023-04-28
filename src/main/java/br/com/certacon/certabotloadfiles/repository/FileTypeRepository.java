package br.com.certacon.certabotloadfiles.repository;

import br.com.certacon.certabotloadfiles.model.FileTypeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileTypeRepository extends JpaRepository<FileTypeModel, UUID> {
}
