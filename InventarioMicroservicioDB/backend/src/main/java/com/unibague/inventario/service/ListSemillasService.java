package com.unibague.inventario.service;

import com.unibague.inventario.dto.SemillaResponse;
import com.unibague.inventario.entity.Semilla;
import com.unibague.inventario.mapper.SemillaMapper;
import com.unibague.inventario.repository.SemillaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para listar semillas aplicando diferentes filtros.
 */
@Service
public class ListSemillasService {
    private final SemillaRepository semillaRepository;
    private static final DateTimeFormatter ISO_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public ListSemillasService(SemillaRepository semillaRepository) {
        this.semillaRepository = semillaRepository;
    }

    /**
     * Lista semillas aplicando filtros opcionales.
     *
     * @param tipo           tipo de semilla (opcional)
     * @param germinacionMin mínimo porcentaje de germinación (opcional)
     * @param desde          fecha inicial en ISO-8601 (opcional, requiere hasta)
     * @param hasta          fecha final en ISO-8601 (opcional, requiere desde)
     * @return lista de semillas convertidas a DTO
     */
    public List<SemillaResponse> list(Optional<String> tipo,
                                      Optional<Double> germinacionMin,
                                      Optional<String> desde,
                                      Optional<String> hasta) {
        List<Semilla> semillas = new ArrayList<>();
        // Si solo tipo está presente y no hay otros filtros, consultar por tipo
        if (tipo.isPresent() && germinacionMin.isEmpty() && desde.isEmpty()) {
            semillas = semillaRepository.findByTipoSemilla(tipo.get());
        } else if (germinacionMin.isPresent() && tipo.isEmpty() && desde.isEmpty()) {
            semillas = semillaRepository.findByPorcentajeGerminacionMin(germinacionMin.get());
        } else if (desde.isPresent() && hasta.isPresent() && tipo.isEmpty() && germinacionMin.isEmpty()) {
            LocalDateTime d = LocalDateTime.parse(desde.get(), ISO_FMT);
            LocalDateTime h = LocalDateTime.parse(hasta.get(), ISO_FMT);
            semillas = semillaRepository.findByFechaIngresoBetween(d, h);
        } else {
            // Caso general: cargar todo y filtrar manualmente
            semillas = semillaRepository.findAll();
            if (tipo.isPresent()) {
                semillas = semillas.stream()
                        .filter(s -> s.getTipoSemilla().equals(tipo.get()))
                        .collect(Collectors.toList());
            }
            if (germinacionMin.isPresent()) {
                semillas = semillas.stream()
                        .filter(s -> s.getPorcentajeGerminacion() >= germinacionMin.get())
                        .collect(Collectors.toList());
            }
            if (desde.isPresent() && hasta.isPresent()) {
                LocalDateTime d = LocalDateTime.parse(desde.get(), ISO_FMT);
                LocalDateTime h = LocalDateTime.parse(hasta.get(), ISO_FMT);
                semillas = semillas.stream()
                        .filter(s -> !s.getFechaIngreso().isBefore(d) && !s.getFechaIngreso().isAfter(h))
                        .collect(Collectors.toList());
            }
        }
        return semillas.stream().map(SemillaMapper::toResponse).collect(Collectors.toList());
    }

    /**
     * Devuelve las dos semillas más recientes de un proveedor.
     *
     * @param nit NIT del proveedor
     * @return lista de hasta dos semillas (puede estar vacía)
     */
    public List<SemillaResponse> top2ByProveedor(String nit) {
        List<Semilla> semillas = semillaRepository.findTop2ByProveedorNitOrderByFechaIngresoDesc(nit, PageRequest.of(0, 2));
        return semillas.stream().map(SemillaMapper::toResponse).collect(Collectors.toList());
    }
}