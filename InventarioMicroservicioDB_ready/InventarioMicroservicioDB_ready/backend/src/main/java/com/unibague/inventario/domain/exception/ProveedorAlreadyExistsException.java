package com.unibague.inventario.domain.exception;

/**
 * Excepción lanzada cuando se intenta crear un proveedor con un NIT ya existente.
 */
public class ProveedorAlreadyExistsException extends RuntimeException {
    public ProveedorAlreadyExistsException(String nit) {
        super("Ya existe un proveedor con NIT '" + nit + "'");
    }
}