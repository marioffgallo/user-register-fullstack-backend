package com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.core.JmsTemplate;

/**
 * JMS configurations to run the ActiveMQ broker embedded and activate to post messages on the queue
 *
 * @author Mario F.F Gallo
 * @version 1.0
 */
@Configuration
@PropertySource(value = { "classpath:application.properties" })
public class JmsConfig {

    @Value("${spring.activemq.broker-url}")
    String brokerUrl;

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
