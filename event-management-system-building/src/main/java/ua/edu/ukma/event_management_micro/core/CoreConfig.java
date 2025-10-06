package ua.edu.ukma.event_management_micro.core;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


@Configuration
public class CoreConfig {

    @Primary
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
