package com.unibague.inventario.dto;

import jakarta.validation.constraints.*;

/**
 * DTO de entrada para crear o actualizar Semilla.
 *
 * <p>Se usa en los controladores con {@code @Valid} para validar los datos
 * proporcionados por el cliente. La fecha se envía en formato ISO
 * (yyyy-MM-ddTHH:mm:ss).</p>
 */
public class SemillaRequest {

    @NotBlank(message = "El código es obligatorio")
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio no puede ser negativo")
    private Double precio;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @NotBlank(message = "El tipo de semilla es obligatorio")
    private String tipoSemilla;

    @NotNull(message = "El porcentaje de germinación es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "La germinación no puede ser negativa")
    @DecimalMax(value = "100.0", inclusive = true, message = "La germinación no puede superar 100")
    private Double porcentajeGerminacion;

    @NotBlank(message = "El proveedor NIT es obligatorio")
    private String proveedorNit;

    /**
     * Fecha en formato ISO-8601: yyyy-MM-ddTHH:mm:ss
     * Ejemplo: 2025-10-21T13:30:00
     */
    @NotBlank(message = "La fecha de ingreso es obligatoria (ISO-8601)")
    private String fechaIngreso;

    public SemillaRequest() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getTipoSemilla() {
        return tipoSemilla;
    }

    public void setTipoSemilla(String tipoSemilla) {
        this.tipoSemilla = tipoSemilla;
    }

    public Double getPorcentajeGerminacion() {
        return porcentajeGerminacion;
    }

    public void setPorcentajeGerminacion(Double porcentajeGerminacion) {
        this.porcentajeGerminacion = porcentajeGerminacion;
    }

    public String getProveedorNit() {
        return proveedorNit;
    }

    public void setProveedorNit(String proveedorNit) {
        this.proveedorNit = proveedorNit;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
}