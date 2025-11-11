package com.unibague.inventario.controller;

import com.unibague.inventario.domain.exception.ProveedorNotFoundException;
import com.unibague.inventario.dto.Top2SemillasResponse;
import com.unibague.inventario.mapper.ProveedorMapper;
import com.unibague.inventario.mapper.SemillaMapper;
import com.unibague.inventario.repository.ProveedorRepository;
import com.unibague.inventario.repository.SemillaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/proveedores")
@CrossOrigin(origins = "*")
public class ProveedorExtraController {

    private final ProveedorRepository proveedorRepository;
    private final SemillaRepository semillaRepository;

    public ProveedorExtraController(ProveedorRepository proveedorRepository,
                                    SemillaRepository semillaRepository) {
        this.proveedorRepository = proveedorRepository;
        this.semillaRepository = semillaRepository;
    }

    @GetMapping("/{nit}/top2")
    public Top2SemillasResponse top2(@PathVariable String nit) {
        var prov = proveedorRepository.findById(nit)
                .orElseThrow(() -> new ProveedorNotFoundException(nit));
        var top2 = semillaRepository.findTopByProveedor(nit, PageRequest.of(0, 2));
        return new Top2SemillasResponse(
                ProveedorMapper.toResponse(prov),
                top2.stream().map(SemillaMapper::toResponse).toList()
        );
    }
}
