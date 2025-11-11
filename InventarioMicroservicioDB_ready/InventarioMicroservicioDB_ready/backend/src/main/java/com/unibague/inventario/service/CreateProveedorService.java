package com.unibague.inventario.service;

import com.unibague.inventario.domain.exception.ProveedorAlreadyExistsException;
import com.unibague.inventario.dto.ProveedorRequest;
import com.unibague.inventario.dto.ProveedorResponse;
import com.unibague.inventario.entity.Proveedor;
import com.unibague.inventario.mapper.ProveedorMapper;
import com.unibague.inventario.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para crear un nuevo proveedor.
 */
@Service
public class CreateProveedorService {

    private final ProveedorRepository proveedorRepository;

    public CreateProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    /**
     * Crea un proveedor nuevo en la base de datos.
     *
     * @param request DTO con los datos del proveedor
     * @return DTO de salida
     */
    @Transactional
    public ProveedorResponse create(ProveedorRequest request) {
        // Verificar si ya existe un proveedor con el mismo NIT
        if (proveedorRepository.existsById(request.getNit())) {
            throw new ProveedorAlreadyExistsException(request.getNit());
        }
        // Convertir a entidad y guardar
        Proveedor proveedor = ProveedorMapper.toEntity(request);
        proveedorRepository.save(proveedor);
        return ProveedorMapper.toResponse(proveedor);
    }
}