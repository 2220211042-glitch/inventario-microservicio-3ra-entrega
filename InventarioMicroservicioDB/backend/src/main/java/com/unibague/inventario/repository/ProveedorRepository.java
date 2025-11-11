package com.unibague.inventario.repository;

import com.unibague.inventario.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad Proveedor.
 */
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, String> {

    /**
     * Busca proveedores cuyo nombre contenga una cadena (ignorando mayúsculas/minúsculas).
     *
     * @param nombre parte del nombre
     * @return lista de proveedores con nombre que contiene la cadena
     */
    List<Proveedor> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca proveedores de una ciudad y con estado activo/inactivo.
     *
     * @param ciudad ciudad
     * @param activo si están activos
     * @return lista de proveedores
     */
    List<Proveedor> findByCiudadAndActivo(String ciudad, boolean activo);

    /**
     * Busca proveedores por ciudad sin importar el estado.
     *
     * @param ciudad ciudad
     * @return lista de proveedores
     */
    List<Proveedor> findByCiudad(String ciudad);

    /**
     * Consulta personalizada para obtener proveedores activos o inactivos.
     *
     * @param activo estado
     * @return proveedores con el estado indicado
     */
    @Query("SELECT p FROM Proveedor p WHERE p.activo = :activo")
    List<Proveedor> buscarPorEstado(@Param("activo") boolean activo);
}