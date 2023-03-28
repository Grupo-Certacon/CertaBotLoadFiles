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
public class LoadFilesModel {
    @Id
    @GenericGenerator(name = "UUIDGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "UUIDGenerator")
    @Column(name = "load_file_id")
    @JsonProperty(value = "load_file_id")
    private UUID id;

    @Column(name = "pasta_servidor", nullable = false)
    @JsonProperty(required = true, value = "pasta_servidor")
    private String serverFolder;

    @Column(name = "pasta_cnpj", nullable = false)
    @JsonProperty(required = true, value = "pasta_cnpj")
    private String cnpjFolder;

    @Column(name = "pasta_ano", nullable = false)
    @JsonProperty(required = true, value = "pasta_ano")
    private String yearFolder;

    @Column(name = "caminho")
    @JsonProperty(value = "caminho")
    private String path;

    @Column(name = "status")
    @JsonProperty(value = "status")
    @Enumerated(EnumType.STRING)
    private StatusFile status;

    @Column(name = "criado_em", nullable = false)
    @JsonProperty(value = "criado_em")
    private Date createdAt;

    @Column(name = "atualizado_em")
    @JsonProperty(value = "atualizado_em")
    private Date updatedAt;

}
