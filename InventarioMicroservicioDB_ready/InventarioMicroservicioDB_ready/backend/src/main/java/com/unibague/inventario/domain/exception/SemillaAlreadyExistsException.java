package com.unibague.inventario.domain.exception;

/**
 * Excepción lanzada cuando se intenta crear una semilla con un código ya existente.
 */
public class SemillaAlreadyExistsException extends RuntimeException {
    public SemillaAlreadyExistsException(String codigo) {
        super("Ya existe una semilla con código '" + codigo + "'");
    }
}