package br.com.certacon.certabotloadfiles.model;

import br.com.certacon.certabotloadfiles.utils.StatusFile;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserFilesModel {
    @Id
    @GenericGenerator(name = "UUIDGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "UUIDGenerator")
    @Column(name = "user_id")
    private UUID id;

    @Column(name = "nome_arquivo", nullable = false)
    @JsonProperty(required = true)
    private String fileName;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusFile status;

    @Column(name = "criado_em")
    private Date createdAt;

    @Column(name = "caminho", nullable = false)
    private String path;

}
