package com.pixousit.multitenantsingledb.entity;

import com.pixousit.multitenantsingledb.config.TenantContext;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(name = "TENANT_ID", nullable = true)
    private String tenantId;

    @PrePersist
    public void setTenantId() {
        this.tenantId = TenantContext.getTenantId();
    }
}

