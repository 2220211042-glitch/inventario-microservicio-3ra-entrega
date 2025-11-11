package com.unibague.inventario.controller;

import com.unibague.inventario.domain.exception.ProveedorNotFoundException;
import com.unibague.inventario.dto.ProveedorRequest;
import com.unibague.inventario.dto.ProveedorResponse;
import com.unibague.inventario.entity.Proveedor;
import com.unibague.inventario.mapper.ProveedorMapper;
import com.unibague.inventario.repository.ProveedorRepository;
import com.unibague.inventario.service.ListProveedoresService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/proveedores")
@CrossOrigin(origins = "*")
public class ProveedorController {

    private final ProveedorRepository proveedorRepository;
    private final ListProveedoresService listService;

    public ProveedorController(ProveedorRepository proveedorRepository,
                               ListProveedoresService listService) {
        this.proveedorRepository = proveedorRepository;
        this.listService = listService;
    }

    // -------- LISTAR con filtros --------
    @GetMapping
    public List<ProveedorResponse> list(@RequestParam(required = false) String nombre,
                                        @RequestParam(required = false) String ciudad,
                                        @RequestParam(required = false) String activo) {
        return listService.list(Optional.ofNullable(nombre),
                                Optional.ofNullable(ciudad),
                                Optional.ofNullable(activo));
    }

    // -------- OBTENER por NIT --------
    @GetMapping("/{nit}")
    public ProveedorResponse getById(@PathVariable String nit) {
        Proveedor p = proveedorRepository.findById(nit)
                .orElseThrow(() -> new ProveedorNotFoundException(nit));
        return ProveedorMapper.toResponse(p);
    }

    // -------- CREAR --------
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ProveedorRequest req) {
        if (proveedorRepository.existsById(req.getNit())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Ya existe un proveedor con NIT " + req.getNit()));
        }
        Proveedor entidad = ProveedorMapper.toEntity(req);
        Proveedor guardado = proveedorRepository.save(entidad);
        URI location = URI.create("/api/v1/proveedores/" + guardado.getNit());
        return ResponseEntity.created(location).body(ProveedorMapper.toResponse(guardado));
    }

    // -------- ACTUALIZAR --------
    @PutMapping("/{nit}")
    public ProveedorResponse update(@PathVariable String nit,
                                    @Valid @RequestBody ProveedorRequest req) {
        Proveedor existente = proveedorRepository.findById(nit)
                .orElseThrow(() -> new ProveedorNotFoundException(nit));
        // Actualizar campos desde el request (o usar Mapper)
        existente.setNombre(req.getNombre());
        existente.setCiudad(req.getCiudad());
        existente.setTelefono(req.getTelefono());
        existente.setFechaRegistro(req.getFechaRegistro());
        existente.setActivo(req.getActivo());
        Proveedor guardado = proveedorRepository.save(existente);
        return ProveedorMapper.toResponse(guardado);
    }

    // -------- ELIMINAR --------
    @DeleteMapping("/{nit}")
    public ResponseEntity<Void> delete(@PathVariable String nit) {
        Proveedor existente = proveedorRepository.findById(nit)
                .orElseThrow(() -> new ProveedorNotFoundException(nit));
        proveedorRepository.delete(existente);
        return ResponseEntity.noContent().build();
    }
}
