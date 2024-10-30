package com.pixousit.multitenantsingledb.entity;

import com.pixousit.multitenantsingledb.config.TenantContext;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

@Getter
@Setter
@Entity
@Table(name = "VEHICLES")
@FilterDef(name = "tenantFilter", parameters = @ParamDef(name = "tenantId", type = String.class))
@Filters(@Filter(name = "tenantFilter", condition = "TENANT_ID = :tenantId"))
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "VEHICLE_NAME", nullable = false)
    private String vehicleName;

    @Column(name = "TENANT_ID", nullable = false)
    private String tenantId;

    @PrePersist
    public void setTenantId() {
        this.tenantId = TenantContext.getTenantId();
    }
}
