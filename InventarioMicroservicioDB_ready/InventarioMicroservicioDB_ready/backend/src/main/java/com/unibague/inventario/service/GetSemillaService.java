package com.unibague.inventario.service;

import com.unibague.inventario.domain.exception.SemillaNotFoundException;
import com.unibague.inventario.dto.SemillaResponse;
import com.unibague.inventario.entity.Semilla;
import com.unibague.inventario.mapper.SemillaMapper;
import com.unibague.inventario.repository.SemillaRepository;
import org.springframework.stereotype.Service;

@Service
public class GetSemillaService {

    private final SemillaRepository semillaRepository;

    public GetSemillaService(SemillaRepository semillaRepository) {
        this.semillaRepository = semillaRepository;
    }

    public SemillaResponse get(String codigo) {
        Semilla s = semillaRepository.findById(codigo)
                .orElseThrow(() -> new SemillaNotFoundException(codigo));
        return SemillaMapper.toResponse(s);
    }
}
