package br.com.certacon.certabotloadfiles.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileTypeModel {
    @Id
    @GenericGenerator(name = "UUIDGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "UUIDGenerator")
    @Column(name = "file_type_model")
    @JsonProperty(value = "load_file_id")
    private UUID id;

    @Column(name = "file_type")
    @Enumerated(EnumType.STRING)
    private String fileType;
}
