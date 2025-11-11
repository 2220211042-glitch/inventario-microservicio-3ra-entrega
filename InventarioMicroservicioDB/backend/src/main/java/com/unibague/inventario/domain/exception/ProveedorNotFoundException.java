package com.unibague.inventario.domain.exception;

/**
 * Excepción lanzada cuando un proveedor no se encuentra en la base de datos.
 */
public class ProveedorNotFoundException extends RuntimeException {
    public ProveedorNotFoundException(String nit) {
        super("El proveedor con NIT '" + nit + "' no existe");
    }
}