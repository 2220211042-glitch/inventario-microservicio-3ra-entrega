package com.unibague.inventario.service;

import com.unibague.inventario.domain.exception.ProveedorNotFoundException;
import com.unibague.inventario.dto.ProveedorResponse;
import com.unibague.inventario.entity.Proveedor;
import com.unibague.inventario.mapper.ProveedorMapper;
import com.unibague.inventario.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

/**
 * Caso de uso para obtener la información de un proveedor por su NIT.
 */
@Service
public class GetProveedorService {
    private final ProveedorRepository proveedorRepository;

    public GetProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    /**
     * Obtiene un proveedor por su identificador NIT.
     *
     * @param nit identificador del proveedor
     * @return DTO de salida
     */
    public ProveedorResponse getByNit(String nit) {
        Proveedor proveedor = proveedorRepository.findById(nit)
                .orElseThrow(() -> new ProveedorNotFoundException(nit));
        return ProveedorMapper.toResponse(proveedor);
    }
}