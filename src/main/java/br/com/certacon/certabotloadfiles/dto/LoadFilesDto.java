package br.com.certacon.certabotloadfiles.dto;

import br.com.certacon.certabotloadfiles.model.LoadFilesModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class LoadFilesDto {
    @JsonProperty(value = "file_id")
    private Long id;
    @JsonProperty(value = "pasta", required = true)
    private String folder;
    @JsonProperty(value = "servidor", required = true)
    private String server;
    @JsonProperty(value = "criado_em", required = true)
    private Date createdAt;
    @JsonProperty(value = "atualizado_em")
    private Date updatedAt;
}

