package br.com.certacon.certabotloadfiles.model;


import br.com.certacon.certabotloadfiles.utils.Processado;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "MS_CONSUMEROFFSETOGPR")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsumerOffsetOrganizerProcess implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_OFFSETOGPR", updatable = false, unique = true, nullable = false)
    private UUID id;
    @Lob
    @Column(name = "MS_ID_TOPICO")
    @JsonProperty(value = "lista", required = true)
    private String idTopico;
    @Lob
    @Column(name = "MS_MENSAGEM", columnDefinition = "TEXT")
    @JsonProperty(value = "mensagem", required = true)
    private String mensagem;
    @Lob
    @Column(name = "MS_PARTICAO")
    @JsonProperty(value = "particao", required = true)
    private String particao;

    @Column(name = "MS_STATUS")
    @JsonProperty(value = "status", required = true)
    @Enumerated(value = EnumType.STRING)
    private Processado status;

}
