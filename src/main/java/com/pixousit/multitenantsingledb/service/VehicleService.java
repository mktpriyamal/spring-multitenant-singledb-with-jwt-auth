package com.pixousit.multitenantsingledb.service;

import com.pixousit.multitenantsingledb.config.TenantContext;
import com.pixousit.multitenantsingledb.entity.Vehicle;
import com.pixousit.multitenantsingledb.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> getAllVehicles() {
        String tenantId = TenantContext.getTenantId();
        return vehicleRepository.findByTenantId(tenantId);
    }

    public Vehicle createVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }
}
