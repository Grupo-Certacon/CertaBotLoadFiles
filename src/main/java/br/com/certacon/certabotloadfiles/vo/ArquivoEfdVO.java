package br.com.certacon.certabotloadfiles.vo;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ArquivoEfdVO {
    private String name;
    private String clientCnpj;
}
