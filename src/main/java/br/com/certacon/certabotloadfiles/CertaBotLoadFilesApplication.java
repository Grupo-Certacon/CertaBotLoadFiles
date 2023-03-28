package br.com.certacon.certabotloadfiles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CertaBotLoadFilesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CertaBotLoadFilesApplication.class, args);
    }

}
