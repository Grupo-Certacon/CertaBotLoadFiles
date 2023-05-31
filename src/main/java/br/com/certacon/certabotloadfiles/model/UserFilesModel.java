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
    @JsonProperty(value = "user_id")
    private UUID id;

    @Column(name = "nome_arquivo")
    @JsonProperty(value = "nome_arquivo")
    private String fileName;

    @Column(name = "status")
    @JsonProperty(value = "status")
    @Enumerated(EnumType.STRING)
    private StatusFile status;
    @Column(name = "mimeType")
    @JsonProperty(value = "mimeType")
    private String mimeType;

    @Column(name = "extension")
    @JsonProperty(value = "extension")
    private String extension;

    @Column(name = "process_id")
    private String processId;

    @Column(name = "criado_em")
    @JsonProperty(value = "criado_em")
    private Date createdAt;

    @Column(name = "caminho")
    @JsonProperty(value = "caminho")
    private String path;
    @Column(name = "cnpj")
    private String cnpj;

    @Column(name = "ip_servidor")
    private String ipServer;

    @Column(name = "year")
    private String year;

    @Column(name = "nome_empresa", nullable = false)
    @JsonProperty(value = "nome_empresa")
    private String companyName;
}
