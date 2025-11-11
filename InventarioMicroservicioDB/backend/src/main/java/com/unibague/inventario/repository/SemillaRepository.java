package com.unibague.inventario.repository;

import com.unibague.inventario.entity.Semilla;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio JPA para la entidad Semilla.
 */
@Repository
public interface SemillaRepository extends JpaRepository<Semilla, String> {

    /**
     * Busca semillas por tipo exacto (case sensitive).
     *
     * @param tipoSemilla tipo de semilla
     * @return lista de semillas
     */
    List<Semilla> findByTipoSemilla(String tipoSemilla);

    /**
     * Busca semillas con porcentaje de germinación mayor o igual a cierto valor.
     *
     * @param minGerminacion valor mínimo
     * @return lista de semillas
     */
    @Query("SELECT s FROM Semilla s WHERE s.porcentajeGerminacion >= :min")
    List<Semilla> findByPorcentajeGerminacionMin(@Param("min") double min);

    /**
     * Busca semillas cuya fecha de ingreso esté entre dos fechas (inclusive).
     *
     * @param desde fecha inicial
     * @param hasta fecha final
     * @return lista de semillas
     */
    @Query("SELECT s FROM Semilla s WHERE s.fechaIngreso >= :desde AND s.fechaIngreso <= :hasta")
    List<Semilla> findByFechaIngresoBetween(@Param("desde") LocalDateTime desde,
                                            @Param("hasta") LocalDateTime hasta);

    /**
     * Consulta personalizada que devuelve las dos semillas más recientes de un proveedor.
     *
     * @param nit NIT del proveedor
     * @param pageable objeto de paginación para limitar a dos resultados
     * @return lista de semillas
     */
    @Query("SELECT s FROM Semilla s WHERE s.proveedor.nit = :nit ORDER BY s.fechaIngreso DESC")
    List<Semilla> findTop2ByProveedorNitOrderByFechaIngresoDesc(@Param("nit") String nit, Pageable pageable);
}