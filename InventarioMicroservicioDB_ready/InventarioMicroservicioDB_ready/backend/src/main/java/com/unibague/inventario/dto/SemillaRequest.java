package com.unibague.inventario.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class SemillaRequest {

    @NotBlank
    private String codigo;

    @NotBlank
    private String nombre;

    @PositiveOrZero
    private double precio;

    @Min(0)
    private int stock;

    @NotBlank
    private String tipoSemilla;

    @DecimalMin("0.0") @DecimalMax("100.0")
    private double porcentajeGerminacion;

    @NotBlank
    private String proveedorNit;

    @NotNull
    @PastOrPresent
    private LocalDateTime fechaIngreso;

    // getters/setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getTipoSemilla() { return tipoSemilla; }
    public void setTipoSemilla(String tipoSemilla) { this.tipoSemilla = tipoSemilla; }
    public double getPorcentajeGerminacion() { return porcentajeGerminacion; }
    public void setPorcentajeGerminacion(double porcentajeGerminacion) { this.porcentajeGerminacion = porcentajeGerminacion; }
    public String getProveedorNit() { return proveedorNit; }
    public void setProveedorNit(String proveedorNit) { this.proveedorNit = proveedorNit; }
    public LocalDateTime getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDateTime fechaIngreso) { this.fechaIngreso = fechaIngreso; }
}
