package br.com.certacon.certabotloadfiles.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice(basePackages = "br.com.certacon.CertaBot.controller")
public class ControllerAdvice {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<MessageExceptionHandler> badRequest(BadRequestException e) {
        MessageExceptionHandler message = MessageExceptionHandler.builder()
                .message("Alguma informação inserida não está correta")
                .error(e.getMessage())
                .timeStamp(new Date())
                .status(HttpStatus.BAD_REQUEST.value())
                .exception(BadRequestException.class.getName())
                .build();
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<MessageExceptionHandler> internalServerError(InternalServerErrorException e) {
        MessageExceptionHandler message = MessageExceptionHandler.builder()
                .message("Não foi possível conectar no servidor")
                .error(e.getMessage())
                .timeStamp(new Date())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .exception(BadRequestException.class.getName())
                .build();
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<MessageExceptionHandler> fileNotFound(NotFoundException e) {
        MessageExceptionHandler message = MessageExceptionHandler.builder()
                .message("Não foi possível encontrar o recurso")
                .error(e.getMessage())
                .timeStamp(new Date())
                .status(HttpStatus.NOT_FOUND.value())
                .exception(NotFoundException.class.getName())
                .build();
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }
}
