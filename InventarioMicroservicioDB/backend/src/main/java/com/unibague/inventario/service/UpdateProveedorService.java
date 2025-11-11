package com.unibague.inventario.service;

import com.unibague.inventario.domain.exception.ProveedorNotFoundException;
import com.unibague.inventario.dto.ProveedorRequest;
import com.unibague.inventario.dto.ProveedorResponse;
import com.unibague.inventario.entity.Proveedor;
import com.unibague.inventario.mapper.ProveedorMapper;
import com.unibague.inventario.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para actualizar la información de un proveedor existente.
 */
@Service
public class UpdateProveedorService {
    private final ProveedorRepository proveedorRepository;

    public UpdateProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    /**
     * Actualiza un proveedor con los datos proporcionados.
     *
     * @param nit     identificador del proveedor a actualizar
     * @param request DTO con los datos actualizados
     * @return DTO de salida
     */
    @Transactional
    public ProveedorResponse update(String nit, ProveedorRequest request) {
        Proveedor proveedor = proveedorRepository.findById(nit)
                .orElseThrow(() -> new ProveedorNotFoundException(nit));
        // Actualizar campos
        ProveedorMapper.updateEntity(proveedor, request);
        proveedorRepository.save(proveedor);
        return ProveedorMapper.toResponse(proveedor);
    }
}