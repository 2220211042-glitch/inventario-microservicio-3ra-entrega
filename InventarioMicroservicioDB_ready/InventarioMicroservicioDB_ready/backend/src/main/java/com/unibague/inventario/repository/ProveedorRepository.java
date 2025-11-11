package com.unibague.inventario.repository;

import com.unibague.inventario.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProveedorRepository extends JpaRepository<Proveedor, String> {

    // Derived ya existentes / útiles
    List<Proveedor> findByNombreContainingIgnoreCase(String nombre);
    List<Proveedor> findByCiudad(String ciudad);
    List<Proveedor> findByCiudadAndActivo(String ciudad, boolean activo);
    List<Proveedor> findByActivo(boolean activo);

    // -------- Consulta personalizada --------
    @Query("select p from Proveedor p " +
           "where lower(p.ciudad) = lower(:ciudad) and p.activo = :activo")
    List<Proveedor> buscarPorCiudadYActivo(@Param("ciudad") String ciudad,
                                           @Param("activo") boolean activo);
}
