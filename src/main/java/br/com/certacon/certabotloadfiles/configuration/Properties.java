package br.com.certacon.certabotloadfiles.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:application.yml"})
@PropertySource({"classpath:application-test.yml"})
public class Properties {

}
