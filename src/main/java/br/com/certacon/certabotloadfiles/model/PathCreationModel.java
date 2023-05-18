package br.com.certacon.certabotloadfiles.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PathCreationModel {
    @Id
    @GenericGenerator(name = "UUIDGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "UUIDGenerator")
    @Column(name = "path_id")
    @JsonProperty(value = "path_id")
    private UUID id;
    @Column(name = "raiz")
    private String root;

    @Column(name = "ip_servidor")
    private String ipServer;

    @Column(name = "cnpj")
    private String cnpj;

    @Column(name = "year")
    private String year;
}
