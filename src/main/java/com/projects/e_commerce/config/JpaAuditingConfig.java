package com.projects.e_commerce.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Enables JPA auditing (e.g. @CreatedDate/@LastModifiedDate) only when JPA is actually configured.
 *
 * This avoids breaking @WebMvcTest slices, which don't load JPA and therefore have an empty metamodel.
 */
@Configuration
@ConditionalOnBean(EntityManagerFactory.class)
@EnableJpaAuditing
public class JpaAuditingConfig {
}

