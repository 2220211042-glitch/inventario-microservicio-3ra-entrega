package com.unibague.inventario.dto;

/**
 * DTO de salida para retornar un Proveedor al cliente.
 */
public class ProveedorResponse {
    private String nit;
    private String nombre;
    private String ciudad;
    private String telefono;
    private String fechaRegistro;
    private boolean activo;

    public ProveedorResponse() {
    }

    public ProveedorResponse(String nit, String nombre, String ciudad, String telefono,
                             String fechaRegistro, boolean activo) {
        this.nit = nit;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.telefono = telefono;
        this.fechaRegistro = fechaRegistro;
        this.activo = activo;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}