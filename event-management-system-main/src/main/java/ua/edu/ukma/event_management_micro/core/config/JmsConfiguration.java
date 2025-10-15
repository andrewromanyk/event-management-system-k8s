package ua.edu.ukma.event_management_micro.core.config;

import jakarta.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import ua.edu.ukma.event_management_micro.core.dto.EmailDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJms
public class JmsConfiguration {

//    @Value("${spring.activemq.broker-url}")
//    private String brokerUrl;


    @Bean
    public BrokerService broker() throws Exception {
        BrokerService broker = new BrokerService();
        broker.addConnector("tcp://0.0.0.0:61616");
        broker.setPersistent(false);
        broker.setUseJmx(false);
        broker.setBrokerName("main-service-broker");
        return broker;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory("vm://localhost");
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate template = new JmsTemplate(connectionFactory);
        template.setReceiveTimeout(3000);
        template.setMessageConverter(jacksonJmsMessageConverter());
        return template;
    }

    @Bean
    public JmsTemplate jmsTopicTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate template = new JmsTemplate(connectionFactory);
        template.setReceiveTimeout(3000);
        template.setPubSubDomain(true);
        template.setMessageConverter(jacksonJmsMessageConverter());
        return template;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");

        // Map logical type names to actual classes
        Map<String, Class<?>> typeIdMappings = new HashMap<>();
        typeIdMappings.put("EmailDto", EmailDto.class);
        converter.setTypeIdMappings(typeIdMappings);

        return converter;
    }

}
