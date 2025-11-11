package com.unibague.inventario.service;

import com.unibague.inventario.domain.exception.SemillaNotFoundException;
import com.unibague.inventario.dto.SemillaResponse;
import com.unibague.inventario.entity.Semilla;
import com.unibague.inventario.mapper.SemillaMapper;
import com.unibague.inventario.repository.SemillaRepository;
import org.springframework.stereotype.Service;

/**
 * Servicio de caso de uso para obtener una semilla por código.
 */
@Service
public class GetSemillaService {
    private final SemillaRepository semillaRepository;

    public GetSemillaService(SemillaRepository semillaRepository) {
        this.semillaRepository = semillaRepository;
    }

    /**
     * Obtiene la semilla con el código proporcionado.
     *
     * @param codigo código de la semilla
     * @return DTO de salida
     */
    public SemillaResponse getByCodigo(String codigo) {
        Semilla semilla = semillaRepository.findById(codigo)
                .orElseThrow(() -> new SemillaNotFoundException(codigo));
        return SemillaMapper.toResponse(semilla);
    }
}