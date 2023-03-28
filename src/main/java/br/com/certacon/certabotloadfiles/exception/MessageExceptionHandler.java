package br.com.certacon.certabotloadfiles.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MessageExceptionHandler {

    private Date timeStamp;
    private Integer status;
    private String error;
    private String exception;
    private String path;
    private String message;
}
