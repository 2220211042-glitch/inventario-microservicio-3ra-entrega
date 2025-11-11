package com.unibague.inventario.dto;

public class SemillaResponse {
    private String codigo;
    private String nombre;
    private double precio;
    private int stock;
    private String tipoSemilla;
    private double porcentajeGerminacion;
    private String proveedorNit;
    private String fechaIngreso; // ISO-8601 como String

    public SemillaResponse() {}

    public SemillaResponse(String codigo, String nombre, double precio, int stock, String tipoSemilla,
                           double porcentajeGerminacion, String proveedorNit, String fechaIngreso) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.tipoSemilla = tipoSemilla;
        this.porcentajeGerminacion = porcentajeGerminacion;
        this.proveedorNit = proveedorNit;
        this.fechaIngreso = fechaIngreso;
    }

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
    public String getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(String fechaIngreso) { this.fechaIngreso = fechaIngreso; }
}
