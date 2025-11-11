package com.unibague.inventario.mapper;

import com.unibague.inventario.dto.ProveedorRequest;
import com.unibague.inventario.dto.ProveedorResponse;
import com.unibague.inventario.entity.Proveedor;

public final class ProveedorMapper {

    private ProveedorMapper(){}

    public static Proveedor toEntity(ProveedorRequest r){
        if (r == null) return null;
        Proveedor p = new Proveedor();
        p.setNit(r.getNit());
        p.setNombre(r.getNombre());
        p.setCiudad(r.getCiudad());
        p.setTelefono(r.getTelefono());
        p.setFechaRegistro(r.getFechaRegistro()); // LocalDateTime
        p.setActivo(Boolean.TRUE.equals(r.getActivo()));
        return p;
    }

    /** <-- NECESARIO para UpdateProveedorService */
    public static void updateEntity(Proveedor e, ProveedorRequest r){
        if (e == null || r == null) return;
        // NO cambiamos el NIT (clave)
        e.setNombre(r.getNombre());
        e.setCiudad(r.getCiudad());
        e.setTelefono(r.getTelefono());
        e.setFechaRegistro(r.getFechaRegistro());
        e.setActivo(Boolean.TRUE.equals(r.getActivo()));
    }

    public static ProveedorResponse toResponse(Proveedor e){
        if (e == null) return null;
        String fecha = (e.getFechaRegistro() != null) ? e.getFechaRegistro().toString() : null;
        return new ProveedorResponse(
                e.getNit(),
                e.getNombre(),
                e.getCiudad(),
                e.getTelefono(),
                fecha,            // String ISO-8601
                e.isActivo()
        );
    }
}
