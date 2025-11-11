package com.unibague.inventario.mapper;

import com.unibague.inventario.dto.ProveedorRequest;
import com.unibague.inventario.dto.ProveedorResponse;
import com.unibague.inventario.entity.Proveedor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Mapper para convertir entre entidades Proveedor y sus DTOs de entrada/salida.
 */
public class ProveedorMapper {
    private static final DateTimeFormatter ISO_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private ProveedorMapper() {
        // utilidad
    }

    /**
     * Convierte un DTO de entrada en una entidad Proveedor.
     *
     * @param request datos de entrada
     * @return entidad Proveedor
     */
    public static Proveedor toEntity(ProveedorRequest request) {
        LocalDateTime fecha = LocalDateTime.parse(request.getFechaRegistro(), ISO_FMT);
        return new Proveedor(
                request.getNit(),
                request.getNombre(),
                request.getCiudad(),
                request.getTelefono(),
                fecha,
                request.getActivo()
        );
    }

    /**
     * Actualiza una entidad Proveedor existente con los datos del request.
     *
     * @param proveedor entidad a actualizar
     * @param request   datos de entrada
     */
    public static void updateEntity(Proveedor proveedor, ProveedorRequest request) {
        proveedor.setNombre(request.getNombre());
        proveedor.setCiudad(request.getCiudad());
        proveedor.setTelefono(request.getTelefono());
        proveedor.setFechaRegistro(LocalDateTime.parse(request.getFechaRegistro(), ISO_FMT));
        proveedor.setActivo(request.getActivo());
    }

    /**
     * Convierte una entidad Proveedor en un DTO de salida.
     *
     * @param proveedor entidad
     * @return DTO de salida
     */
    public static ProveedorResponse toResponse(Proveedor proveedor) {
        String fecha = proveedor.getFechaRegistro().format(ISO_FMT);
        return new ProveedorResponse(
                proveedor.getNit(),
                proveedor.getNombre(),
                proveedor.getCiudad(),
                proveedor.getTelefono(),
                fecha,
                proveedor.isActivo()
        );
    }
}