package com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.model.LogEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Message producer for the logs send to ActiveMQ queue
 *
 * @author Mario F.F Gallo
 * @version 1.0
 */
@Service
public class MessageProducer {

    Logger log = LoggerFactory.getLogger(MessageProducer.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${activemq.queue.name}")
    String destination;

    public void send(LogEventDTO logEventDTO) {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            String json = objectMapper.writeValueAsString(logEventDTO);
            jmsTemplate.convertAndSend(destination, json);
            log.info("Sent log='{}'", json);
        } catch(IOException e) {

        }
    }
}
