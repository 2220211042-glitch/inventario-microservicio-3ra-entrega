package com.unibague.inventario.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa a un proveedor.
 *
 * <p>Se mapea a la tabla {@code proveedores}. Cada proveedor tiene un
 * identificador único (nit) y varios atributos adicionales. La relación
 * con Semilla es una relación de uno a muchos, pero no se declara aquí
 * explícitamente para cumplir con la restricción de no usar listas en
 * este proyecto; la relación se define desde Semilla con @ManyToOne.</p>
 */
@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @Column(name = "nit", nullable = false, length = 20)
    private String nit;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "ciudad", nullable = false)
    private String ciudad;

    @Column(name = "telefono", nullable = false)
    private String telefono;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "activo", nullable = false)
    private boolean activo;

    public Proveedor() {
    }

    public Proveedor(String nit, String nombre, String ciudad, String telefono,
                     LocalDateTime fechaRegistro, boolean activo) {
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

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}