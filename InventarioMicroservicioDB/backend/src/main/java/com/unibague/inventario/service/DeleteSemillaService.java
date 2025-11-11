package com.unibague.inventario.service;

import com.unibague.inventario.domain.exception.SemillaNotFoundException;
import com.unibague.inventario.repository.SemillaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para eliminar una semilla existente.
 */
@Service
public class DeleteSemillaService {
    private final SemillaRepository semillaRepository;

    public DeleteSemillaService(SemillaRepository semillaRepository) {
        this.semillaRepository = semillaRepository;
    }

    /**
     * Elimina la semilla con el código proporcionado.
     *
     * @param codigo código de la semilla
     */
    @Transactional
    public void delete(String codigo) {
        if (!semillaRepository.existsById(codigo)) {
            throw new SemillaNotFoundException(codigo);
        }
        semillaRepository.deleteById(codigo);
    }
}