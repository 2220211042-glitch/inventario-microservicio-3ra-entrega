package com.unibague.inventario.repository;

import com.unibague.inventario.entity.Semilla;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SemillaRepository extends JpaRepository<Semilla, String> {

    // Derived ya existente
    List<Semilla> findByTipoSemilla(String tipoSemilla);

    // -------- Consultas personalizadas --------
    @Query("select s from Semilla s " +
           "where s.porcentajeGerminacion >= :min " +
           "order by s.porcentajeGerminacion desc")
    List<Semilla> findConGerminacionDesde(@Param("min") double min);

    @Query("select s from Semilla s " +
           "where s.fechaIngreso between :desde and :hasta " +
           "order by s.fechaIngreso desc")
    List<Semilla> findByRangoFechas(@Param("desde") LocalDateTime desde,
                                    @Param("hasta") LocalDateTime hasta);

    // Para maestro + 2 de detalle
    @Query("select s from Semilla s " +
           "where s.proveedor.nit = :nit " +
           "order by s.fechaIngreso desc")
    List<Semilla> findTopByProveedor(@Param("nit") String nit, Pageable pageable);
}
