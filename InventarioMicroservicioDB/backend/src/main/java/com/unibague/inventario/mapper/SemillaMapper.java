package com.unibague.inventario.mapper;

import com.unibague.inventario.dto.SemillaRequest;
import com.unibague.inventario.dto.SemillaResponse;
import com.unibague.inventario.entity.Proveedor;
import com.unibague.inventario.entity.Semilla;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Mapper para convertir entre entidades Semilla y sus DTOs de entrada/salida.
 */
public class SemillaMapper {
    private static final DateTimeFormatter ISO_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private SemillaMapper() {
        // utilidad
    }

    /**
     * Convierte un SemillaRequest y un Proveedor en una entidad Semilla.
     *
     * @param request   DTO de entrada
     * @param proveedor proveedor asociado
     * @return entidad Semilla
     */
    public static Semilla toEntity(SemillaRequest request, Proveedor proveedor) {
        LocalDateTime fecha = LocalDateTime.parse(request.getFechaIngreso(), ISO_FMT);
        return new Semilla(
                request.getCodigo(),
                request.getNombre(),
                request.getPrecio(),
                request.getStock(),
                request.getTipoSemilla(),
                request.getPorcentajeGerminacion(),
                fecha,
                proveedor
        );
    }

    /**
     * Actualiza una entidad Semilla existente con los datos del request y un proveedor.
     *
     * @param semilla   entidad a actualizar
     * @param request   datos de entrada
     * @param proveedor proveedor asociado
     */
    public static void updateEntity(Semilla semilla, SemillaRequest request, Proveedor proveedor) {
        semilla.setNombre(request.getNombre());
        semilla.setPrecio(request.getPrecio());
        semilla.setStock(request.getStock());
        semilla.setTipoSemilla(request.getTipoSemilla());
        semilla.setPorcentajeGerminacion(request.getPorcentajeGerminacion());
        semilla.setFechaIngreso(LocalDateTime.parse(request.getFechaIngreso(), ISO_FMT));
        semilla.setProveedor(proveedor);
    }

    /**
     * Convierte una entidad Semilla en un DTO de salida.
     *
     * @param semilla entidad
     * @return DTO de salida
     */
    public static SemillaResponse toResponse(Semilla semilla) {
        String fecha = semilla.getFechaIngreso().format(ISO_FMT);
        return new SemillaResponse(
                semilla.getCodigo(),
                semilla.getNombre(),
                semilla.getPrecio(),
                semilla.getStock(),
                semilla.getTipoSemilla(),
                semilla.getPorcentajeGerminacion(),
                semilla.getProveedor().getNit(),
                fecha
        );
    }
}