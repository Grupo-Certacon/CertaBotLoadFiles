package br.com.certacon.certabotloadfiles.repository;

import br.com.certacon.certabotloadfiles.model.ConsumerOffsetOrganizerProcess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConsumerOffsetOrganizerProcessRepository extends JpaRepository<ConsumerOffsetOrganizerProcess, UUID> {

    ConsumerOffsetOrganizerProcess findByIdTopicoAndParticao(String idTopico, String particao);

}
