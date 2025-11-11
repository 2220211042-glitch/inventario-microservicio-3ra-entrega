package com.unibague.inventario.service;

import com.unibague.inventario.dto.ProveedorResponse;
import com.unibague.inventario.entity.Proveedor;
import com.unibague.inventario.mapper.ProveedorMapper;
import com.unibague.inventario.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para listar proveedores aplicando diferentes filtros de búsqueda.
 */
@Service
public class ListProveedoresService {
    private final ProveedorRepository proveedorRepository;

    public ListProveedoresService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    /**
     * Devuelve una lista de proveedores aplicando filtros opcionales.
     *
     * <p>Si se proporciona el nombre, se realiza la búsqueda por coincidencia parcial,
     * ignorando otros filtros. En caso contrario, se aplican los filtros de ciudad
     * y estado activo según correspondan.</p>
     *
     * @param nombre nombre parcial del proveedor (opcional)
     * @param ciudad ciudad del proveedor (opcional)
     * @param activo estado de actividad en formato String ("true"/"false", opcional)
     * @return lista de proveedores como DTOs
     */
    public List<ProveedorResponse> list(Optional<String> nombre,
                                        Optional<String> ciudad,
                                        Optional<String> activo) {
        List<Proveedor> proveedores;
        if (nombre.isPresent()) {
            proveedores = proveedorRepository.findByNombreContainingIgnoreCase(nombre.get());
        } else if (ciudad.isPresent() && activo.isPresent()) {
            boolean activoBool = Boolean.parseBoolean(activo.get());
            proveedores = proveedorRepository.findByCiudadAndActivo(ciudad.get(), activoBool);
        } else if (ciudad.isPresent()) {
            proveedores = proveedorRepository.findByCiudad(ciudad.get());
        } else if (activo.isPresent()) {
            boolean activoBool = Boolean.parseBoolean(activo.get());
            proveedores = proveedorRepository.findByActivo(activoBool);
        } else {
            proveedores = proveedorRepository.findAll();
        }
        return proveedores.stream().map(ProveedorMapper::toResponse).collect(Collectors.toList());
    }
}