package com.geico.claim.roadsideassistance.config;

import com.geico.claim.roadsideassistance.service.AssistantManager;
import com.geico.claim.roadsideassistance.service.RoadsideAssistanceService;
import com.geico.claim.roadsideassistance.service.RoadsideAssistanceServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public RoadsideAssistanceService roadsideAssistanceService() {
        return new RoadsideAssistanceServiceImpl(this.assistantManager());
    }

    @Bean
    public AssistantManager assistantManager() {
       return AssistantManager.getInstance();
    }
}