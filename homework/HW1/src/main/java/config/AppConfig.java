package config;

import services.ZooService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"services", "entities"})
public class AppConfig {

    @Bean
    public ZooService zooService() {
        return new ZooService();
    }
}