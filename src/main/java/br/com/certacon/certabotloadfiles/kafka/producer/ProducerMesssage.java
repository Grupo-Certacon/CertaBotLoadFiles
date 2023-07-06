package br.com.certacon.certabotloadfiles.kafka.producer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProducerMesssage {

    private String status;
    private String idArquivo;
    private String idLista;

}
