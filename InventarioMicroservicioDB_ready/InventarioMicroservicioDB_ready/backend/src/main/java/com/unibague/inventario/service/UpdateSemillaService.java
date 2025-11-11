package com.unibague.inventario.service;

import com.unibague.inventario.domain.exception.ProveedorNotFoundException;
import com.unibague.inventario.domain.exception.SemillaNotFoundException;
import com.unibague.inventario.dto.SemillaRequest;
import com.unibague.inventario.dto.SemillaResponse;
import com.unibague.inventario.entity.Proveedor;
import com.unibague.inventario.entity.Semilla;
import com.unibague.inventario.mapper.SemillaMapper;
import com.unibague.inventario.repository.ProveedorRepository;
import com.unibague.inventario.repository.SemillaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateSemillaService {
    private final SemillaRepository semillaRepository;
    private final ProveedorRepository proveedorRepository;

    public UpdateSemillaService(SemillaRepository semillaRepository,
                                ProveedorRepository proveedorRepository) {
        this.semillaRepository = semillaRepository;
        this.proveedorRepository = proveedorRepository;
    }

    @Transactional
    public SemillaResponse update(String codigo, SemillaRequest request) {
        if (!codigo.equals(request.getCodigo())) {
            throw new IllegalArgumentException("El código de la URL y del cuerpo no coinciden");
        }

        Semilla semilla = semillaRepository.findById(codigo)
                .orElseThrow(() -> new SemillaNotFoundException(codigo));

        Proveedor proveedor = proveedorRepository.findById(request.getProveedorNit())
                .orElseThrow(() -> new ProveedorNotFoundException(request.getProveedorNit()));

        SemillaMapper.updateEntity(semilla, request, proveedor);
        Semilla guardada = semillaRepository.save(semilla);
        return SemillaMapper.toResponse(guardada);
    }
}
