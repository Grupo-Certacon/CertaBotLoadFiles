package br.com.certacon.certabotloadfiles.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class LoadFilesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "load_file_id")
    private Long id;

    @Column(name = "pasta_servidor", nullable = false)
    private String serverFolder;

    @Column(name = "pasta_cnpj", nullable = false)
    private String cnpjFolder;

    @Column(name = "pasta_ano", nullable = false)
    private String yearFolder;

    @Column(name = "caminho")
    private String path;

    @Column(name = "criado_em", nullable = false)
    private Date createdAt;

    @Column(name = "atualizado_em")
    private Date updatedAt;

}
