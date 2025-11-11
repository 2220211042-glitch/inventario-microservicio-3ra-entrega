package com.unibague.inventario.mapper;

import com.unibague.inventario.dto.SemillaRequest;
import com.unibague.inventario.dto.SemillaResponse;
import com.unibague.inventario.entity.Proveedor;
import com.unibague.inventario.entity.Semilla;

public final class SemillaMapper {

    private SemillaMapper(){}

    public static Semilla toEntity(SemillaRequest r, Proveedor proveedor){
        if (r == null) return null;
        Semilla s = new Semilla();
        s.setCodigo(r.getCodigo());
        s.setNombre(r.getNombre());
        s.setPrecio(r.getPrecio());
        s.setStock(r.getStock());
        s.setTipoSemilla(r.getTipoSemilla());
        s.setPorcentajeGerminacion(r.getPorcentajeGerminacion());
        s.setFechaIngreso(r.getFechaIngreso()); // LocalDateTime
        s.setProveedor(proveedor);
        return s;
    }

    /** <-- NECESARIO para UpdateSemillaService */
    public static void updateEntity(Semilla e, SemillaRequest r, Proveedor proveedor){
        if (e == null || r == null) return;
        // NO cambiamos el código (clave)
        e.setNombre(r.getNombre());
        e.setPrecio(r.getPrecio());
        e.setStock(r.getStock());
        e.setTipoSemilla(r.getTipoSemilla());
        e.setPorcentajeGerminacion(r.getPorcentajeGerminacion());
        e.setFechaIngreso(r.getFechaIngreso());
        e.setProveedor(proveedor);
    }

    public static SemillaResponse toResponse(Semilla e){
        if (e == null) return null;
        String fecha = (e.getFechaIngreso() != null) ? e.getFechaIngreso().toString() : null;
        String nit = (e.getProveedor() != null) ? e.getProveedor().getNit() : null;
        return new SemillaResponse(
                e.getCodigo(),
                e.getNombre(),
                e.getPrecio(),
                e.getStock(),
                e.getTipoSemilla(),
                e.getPorcentajeGerminacion(),
                nit,
                fecha            // String ISO-8601
        );
    }
}
