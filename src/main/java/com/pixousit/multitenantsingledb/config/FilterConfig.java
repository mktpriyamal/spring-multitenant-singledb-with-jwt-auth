package com.pixousit.multitenantsingledb.config;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.pixousit.multitenantsingledb.repository")
public class FilterConfig {

    private final EntityManagerFactory emf;

    public FilterConfig(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @PostConstruct
    public void enableTenantFilter() {
        EntityManager em = emf.createEntityManager();
        String tenantId = TenantContext.getTenantId();
        if (tenantId != null) {
            em.unwrap(Session.class).enableFilter("tenantFilter").setParameter("tenantId", tenantId);
        }else {
            em.unwrap(Session.class).disableFilter("tenantFilter");
        }
    }
}
