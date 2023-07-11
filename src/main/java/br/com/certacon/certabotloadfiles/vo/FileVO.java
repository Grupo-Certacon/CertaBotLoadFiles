package br.com.certacon.certabotloadfiles.vo;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FileVO {

    private String fileName;
    private String filePath;
    private String ipServer;
    private String cnpj;
    private String companyName;
    private String originalFile;

}
