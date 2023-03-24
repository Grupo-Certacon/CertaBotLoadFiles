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

    @Column(name = "pasta", nullable = false)
    private String folder;

    @Column(name = "servidor", nullable = false)
    private String server;

    @Column(name = "criado_em", nullable = false)
    private Date createdAt;

    @Column(name = "atualizado_em")
    private Date updatedAt;

}
