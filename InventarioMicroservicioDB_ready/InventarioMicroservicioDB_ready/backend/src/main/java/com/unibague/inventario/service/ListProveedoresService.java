package com.unibague.inventario.service;

import com.unibague.inventario.dto.ProveedorResponse;
import com.unibague.inventario.entity.Proveedor;
import com.unibague.inventario.mapper.ProveedorMapper;
import com.unibague.inventario.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ListProveedoresService {
    private final ProveedorRepository proveedorRepository;

    public ListProveedoresService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    public List<ProveedorResponse> list(Optional<String> nombre,
                                        Optional<String> ciudad,
                                        Optional<String> activo) {
        List<Proveedor> proveedores;

        if (nombre.isPresent() && !nombre.get().isBlank()) {
            proveedores = proveedorRepository.findByNombreContainingIgnoreCase(nombre.get().trim());
        } else if (ciudad.isPresent() && activo.isPresent()) {
            boolean flag = Boolean.parseBoolean(activo.get());
            proveedores = proveedorRepository.buscarPorCiudadYActivo(ciudad.get().trim(), flag); // @Query
        } else if (ciudad.isPresent()) {
            proveedores = proveedorRepository.findByCiudad(ciudad.get().trim());
        } else if (activo.isPresent()) {
            boolean flag = Boolean.parseBoolean(activo.get());
            proveedores = proveedorRepository.findByActivo(flag);
        } else {
            proveedores = proveedorRepository.findAll();
        }

        return proveedores.stream().map(ProveedorMapper::toResponse).collect(Collectors.toList());
    }
}
