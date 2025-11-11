package com.unibague.inventario.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa una semilla en el inventario.
 *
 * <p>Cada semilla posee un código único, un conjunto de atributos
 * primitivos y una referencia al proveedor asociado mediante una
 * relación ManyToOne. La tabla asociada es {@code semillas}.</p>
 */
@Entity
@Table(name = "semillas")
public class Semilla {

    @Id
    @Column(name = "codigo", nullable = false, length = 20)
    private String codigo;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "precio", nullable = false)
    private double precio;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "tipo_semilla", nullable = false)
    private String tipoSemilla;

    @Column(name = "porcentaje_germinacion", nullable = false)
    private double porcentajeGerminacion;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDateTime fechaIngreso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_nit", nullable = false)
    private Proveedor proveedor;

    public Semilla() {
    }

    public Semilla(String codigo, String nombre, double precio, int stock,
                   String tipoSemilla, double porcentajeGerminacion,
                   LocalDateTime fechaIngreso, Proveedor proveedor) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.tipoSemilla = tipoSemilla;
        this.porcentajeGerminacion = porcentajeGerminacion;
        this.fechaIngreso = fechaIngreso;
        this.proveedor = proveedor;
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getTipoSemilla() {
        return tipoSemilla;
    }

    public void setTipoSemilla(String tipoSemilla) {
        this.tipoSemilla = tipoSemilla;
    }

    public double getPorcentajeGerminacion() {
        return porcentajeGerminacion;
    }

    public void setPorcentajeGerminacion(double porcentajeGerminacion) {
        this.porcentajeGerminacion = porcentajeGerminacion;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }
}