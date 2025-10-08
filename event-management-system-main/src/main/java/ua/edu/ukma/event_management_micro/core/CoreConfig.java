package ua.edu.ukma.event_management_micro.core;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableRetry
public class CoreConfig {

    @Primary
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapperResult = new ModelMapper();

        return mapperResult;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
