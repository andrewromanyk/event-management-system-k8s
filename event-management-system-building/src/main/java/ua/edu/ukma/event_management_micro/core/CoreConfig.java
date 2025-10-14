package ua.edu.ukma.event_management_micro.core;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;


@Configuration
public class CoreConfig {

    @Primary
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
