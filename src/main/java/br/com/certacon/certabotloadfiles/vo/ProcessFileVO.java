package br.com.certacon.certabotloadfiles.vo;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProcessFileVO {
    private String url_de_download;
    private String senha;
    private String url_de_upload;
    private String caminho_de_arquivo;
    private String usuario;
    private String caminho_de_destino_download;
    private String nome_arquivo;
    private String id_arquivo;

}
