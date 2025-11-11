package com.unibague.inventario.service;

import com.unibague.inventario.domain.exception.ProveedorNotFoundException;
import com.unibague.inventario.domain.exception.SemillaAlreadyExistsException;
import com.unibague.inventario.dto.SemillaRequest;
import com.unibague.inventario.dto.SemillaResponse;
import com.unibague.inventario.entity.Proveedor;
import com.unibague.inventario.entity.Semilla;
import com.unibague.inventario.mapper.SemillaMapper;
import com.unibague.inventario.repository.ProveedorRepository;
import com.unibague.inventario.repository.SemillaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de caso de uso para crear una nueva semilla.
 */
@Service
public class CreateSemillaService {

    private final SemillaRepository semillaRepository;
    private final ProveedorRepository proveedorRepository;

    public CreateSemillaService(SemillaRepository semillaRepository,
                                ProveedorRepository proveedorRepository) {
        this.semillaRepository = semillaRepository;
        this.proveedorRepository = proveedorRepository;
    }

    /**
     * Crea una nueva semilla en la base de datos.
     *
     * @param request DTO con los datos de la semilla
     * @return DTO de salida
     */
    @Transactional
    public SemillaResponse create(SemillaRequest request) {
        // Validar existencia previa
        if (semillaRepository.existsById(request.getCodigo())) {
            throw new SemillaAlreadyExistsException(request.getCodigo());
        }
        // Buscar proveedor
        Proveedor proveedor = proveedorRepository.findById(request.getProveedorNit())
                .orElseThrow(() -> new ProveedorNotFoundException(request.getProveedorNit()));
        // Convertir a entidad y guardar
        Semilla semilla = SemillaMapper.toEntity(request, proveedor);
        semillaRepository.save(semilla);
        return SemillaMapper.toResponse(semilla);
    }
}