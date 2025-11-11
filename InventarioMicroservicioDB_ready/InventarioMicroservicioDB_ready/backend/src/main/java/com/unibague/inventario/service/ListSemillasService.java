package com.unibague.inventario.service;

import com.unibague.inventario.dto.SemillaResponse;
import com.unibague.inventario.entity.Semilla;
import com.unibague.inventario.mapper.SemillaMapper;
import com.unibague.inventario.repository.SemillaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ListSemillasService {

    private final SemillaRepository semillaRepository;

    public ListSemillasService(SemillaRepository semillaRepository) {
        this.semillaRepository = semillaRepository;
    }

    public List<SemillaResponse> list(Optional<String> tipo,
                                      Optional<String> germinacionMin,
                                      Optional<String> desde,
                                      Optional<String> hasta) {
        List<Semilla> semillas;

        if (germinacionMin.isPresent()) {
            double min = Double.parseDouble(germinacionMin.get());
            semillas = semillaRepository.findConGerminacionDesde(min); // @Query personalizada
        } else if (desde.isPresent() && hasta.isPresent()) {
            LocalDateTime d = LocalDateTime.parse(desde.get());
            LocalDateTime h = LocalDateTime.parse(hasta.get());
            semillas = semillaRepository.findByRangoFechas(d, h);      // @Query personalizada
        } else if (tipo.isPresent()) {
            semillas = semillaRepository.findByTipoSemilla(tipo.get());
        } else {
            semillas = semillaRepository.findAll();
        }

        return semillas.stream()
                .map(SemillaMapper::toResponse)
                .collect(Collectors.toList());
    }
}
