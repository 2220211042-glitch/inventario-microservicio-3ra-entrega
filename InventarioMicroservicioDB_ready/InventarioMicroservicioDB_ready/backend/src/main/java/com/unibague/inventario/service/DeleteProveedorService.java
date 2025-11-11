package com.unibague.inventario.service;

import com.unibague.inventario.domain.exception.ProveedorNotFoundException;
import com.unibague.inventario.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para eliminar un proveedor existente.
 */
@Service
public class DeleteProveedorService {
    private final ProveedorRepository proveedorRepository;

    public DeleteProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    /**
     * Elimina un proveedor por su NIT.
     *
     * @param nit identificador del proveedor
     */
    @Transactional
    public void delete(String nit) {
        if (!proveedorRepository.existsById(nit)) {
            throw new ProveedorNotFoundException(nit);
        }
        proveedorRepository.deleteById(nit);
    }
}