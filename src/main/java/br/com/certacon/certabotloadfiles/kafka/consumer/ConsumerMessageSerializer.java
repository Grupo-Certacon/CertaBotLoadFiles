package br.com.certacon.certabotloadfiles.kafka.consumer;

import org.springframework.kafka.support.serializer.JsonSerializer;

public class ConsumerMessageSerializer extends JsonSerializer<UploadMesssage> {

}
