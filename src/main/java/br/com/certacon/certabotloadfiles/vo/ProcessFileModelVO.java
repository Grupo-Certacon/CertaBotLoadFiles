package br.com.certacon.certabotloadfiles.vo;

import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProcessFileModelVO {
    private String id;
    private String process;
    private String taskName;
    private Date updatedAt;
    private Date createdAt;
    private String remoteDriverUpload;
    private String registers;
    private String remoteDriverDownload;
    private String time;
    private String percentage;
    private String downloadPath;
    private String username;
    private String password;
    private String status;
    private String fileName;
    private List<String> processFile;

}
