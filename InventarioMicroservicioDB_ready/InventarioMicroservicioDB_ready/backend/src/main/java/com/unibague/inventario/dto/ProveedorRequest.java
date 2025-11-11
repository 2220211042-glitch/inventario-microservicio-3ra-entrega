package com.unibague.inventario.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class ProveedorRequest {

    @NotBlank
    private String nit;

    @NotBlank
    private String nombre;

    @NotBlank
    private String ciudad;

    @NotBlank
    private String telefono;

    @NotNull
    @PastOrPresent
    private LocalDateTime fechaRegistro;

    @NotNull
    private Boolean activo;

    // getters y setters
    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
