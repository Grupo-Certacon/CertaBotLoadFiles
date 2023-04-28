package br.com.certacon.certabotloadfiles.dto;

import br.com.certacon.certabotloadfiles.utils.StatusFile;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class LoadFilesDto {
    @JsonProperty(value = "load_file_id")
    private UUID id;

    @JsonProperty(value = "pasta_servidor", required = true)
    private String serverFolder;

    @JsonProperty(value = "pasta_cnpj", required = true)
    private String cnpjFolder;

    @JsonProperty(value = "pasta_ano", required = true)
    private String yearFolder;

    @JsonProperty(value = "caminho")
    private String path;

    @JsonProperty(value = "status")
    private StatusFile status;

    @JsonProperty(value = "criado_em", required = true)
    private Date createdAt;

    @JsonProperty(value = "atualizado_em")
    private Date updatedAt;
}

