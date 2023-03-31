package br.com.certacon.certabotloadfiles.vo;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ArquivoEfdModelVO {
    private UUID id;
    private Date createdAt;
    private String name;
    private String clientCnpj;
    private Date updatedAt;
    private Date processedAt;
}
