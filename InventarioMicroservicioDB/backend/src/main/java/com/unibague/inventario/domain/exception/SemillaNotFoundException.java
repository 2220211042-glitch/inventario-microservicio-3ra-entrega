package com.unibague.inventario.domain.exception;

/**
 * Excepción lanzada cuando una semilla no se encuentra en la base de datos.
 */
public class SemillaNotFoundException extends RuntimeException {
    public SemillaNotFoundException(String codigo) {
        super("La semilla con código '" + codigo + "' no existe");
    }
}