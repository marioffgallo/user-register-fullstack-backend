package com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class JmsConfig {

    String brokerUrl = "tcp://localhost:61616";
    /*
    CODIGO PARA SUBIR NO DOCKER
    String brokerUrl = "tcp://0.0.0.0:61616";
    */

    @Bean
    public BrokerService broker() throws Exception {

        BrokerService broker = new BrokerService();
        broker.setPersistent(false);
        broker.setUseJmx(true);
        broker.addConnector(brokerUrl);
        return broker;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        return new JmsTemplate(new PooledConnectionFactory(brokerUrl));
    }
}
