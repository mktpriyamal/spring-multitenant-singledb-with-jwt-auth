package com.pixousit.multitenantsingledb.repository;

import com.pixousit.multitenantsingledb.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByTenantId(String tenantId);
}
