package com.geico.claim;
import com.geico.claim.roadsideassistance.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(AppConfig.class)
public class RoadsideAssistanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoadsideAssistanceApplication.class, args);
    }
}