package br.com.certacon.certabotloadfiles.vo;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FileEntityVO {
    private UUID id;
    private Date createdAt;
    private String name;
    private String cnpj;
    private String ipServer;
    private String filePath;
    private Date updatedAt;

}
