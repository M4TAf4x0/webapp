package com.archetype.monolithic.webapp.config.auditing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditingConfiguration {

    @Bean(name = "auditorProvider")
    public AuditorAware<String> auditorProvider() {
        return new AuditorProvider();
    }
}