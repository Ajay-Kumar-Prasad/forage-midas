package com.jpmc.midascore; 
//takes a raw transaction string, turns it into a Transaction object, 
//converts it to JSON, and throws it into Kafka.
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper; //Used to convert Java objects → JSON
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private final String topic;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper(); //Converts Transaction → JSON string

    public KafkaProducer(
            @Value("${general.kafka-topic}") String topic,
            KafkaTemplate<String, String> kafkaTemplate
    ) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String transactionLine) {
        String[] transactionData = transactionLine.split(", ");

        Transaction transaction = new Transaction(
            //index 0 → transaction ID
            //index 1 → user ID
            //index 2 → amount
                Long.parseLong(transactionData[0]),
                Long.parseLong(transactionData[1]),
                Float.parseFloat(transactionData[2])
        );

        try {
            String payload = objectMapper.writeValueAsString(transaction); //Convert Java object → JSON
            kafkaTemplate.send(topic, payload); //Send JSON to Kafka topic
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize transaction", e);
        }
    }
}
