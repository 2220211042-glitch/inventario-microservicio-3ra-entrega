package com.unibague.inventario.controller;

import com.unibague.inventario.dto.ProveedorRequest;
import com.unibague.inventario.dto.ProveedorResponse;
import com.unibague.inventario.service.CreateProveedorService;
import com.unibague.inventario.service.DeleteProveedorService;
import com.unibague.inventario.service.GetProveedorService;
import com.unibague.inventario.service.ListProveedoresService;
import com.unibague.inventario.service.UpdateProveedorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar proveedores. Expone los casos de uso de
 * creación, consulta, actualización, eliminación y listados con filtros.
 */
@RestController
@RequestMapping("/api/v1/proveedores")
public class ProveedorController {

    private final CreateProveedorService createProveedorService;
    private final GetProveedorService getProveedorService;
    private final UpdateProveedorService updateProveedorService;
    private final DeleteProveedorService deleteProveedorService;
    private final ListProveedoresService listProveedoresService;

    public ProveedorController(CreateProveedorService createProveedorService,
                               GetProveedorService getProveedorService,
                               UpdateProveedorService updateProveedorService,
                               DeleteProveedorService deleteProveedorService,
                               ListProveedoresService listProveedoresService) {
        this.createProveedorService = createProveedorService;
        this.getProveedorService = getProveedorService;
        this.updateProveedorService = updateProveedorService;
        this.deleteProveedorService = deleteProveedorService;
        this.listProveedoresService = listProveedoresService;
    }

    /**
     * Crea un nuevo proveedor.
     *
     * @param request DTO con los datos del proveedor
     * @return respuesta con el proveedor creado
     */
    @PostMapping
    public ResponseEntity<ProveedorResponse> create(@Valid @RequestBody ProveedorRequest request) {
        ProveedorResponse response = createProveedorService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtiene un proveedor por su NIT.
     *
     * @param nit identificador del proveedor
     * @return proveedor encontrado
     */
    @GetMapping("/{nit}")
    public ResponseEntity<ProveedorResponse> getByNit(@PathVariable String nit) {
        ProveedorResponse response = getProveedorService.getByNit(nit);
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza un proveedor existente.
     *
     * @param nit     identificador del proveedor a actualizar
     * @param request DTO con los nuevos datos
     * @return proveedor actualizado
     */
    @PutMapping("/{nit}")
    public ResponseEntity<ProveedorResponse> update(@PathVariable String nit,
                                                    @Valid @RequestBody ProveedorRequest request) {
        // Validación: el NIT de la URL debe coincidir con el del cuerpo
        if (!nit.equals(request.getNit())) {
            throw new IllegalArgumentException("El NIT de la URL no coincide con el del cuerpo de la solicitud");
        }
        ProveedorResponse response = updateProveedorService.update(nit, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Elimina un proveedor.
     *
     * @param nit identificador del proveedor a eliminar
     * @return respuesta sin contenido
     */
    @DeleteMapping("/{nit}")
    public ResponseEntity<Void> delete(@PathVariable String nit) {
        deleteProveedorService.delete(nit);
        return ResponseEntity.noContent().build();
    }

    /**
     * Lista proveedores aplicando filtros opcionales. Si se proporciona el nombre,
     * se ignoran el resto de filtros.
     *
     * @param nombre nombre parcial del proveedor (opcional)
     * @param ciudad ciudad (opcional)
     * @param activo estado de actividad (opcional: "true"/"false")
     * @return lista de proveedores que cumplen con los filtros
     */
    @GetMapping
    public ResponseEntity<List<ProveedorResponse>> list(@RequestParam Optional<String> nombre,
                                                        @RequestParam Optional<String> ciudad,
                                                        @RequestParam Optional<String> activo) {
        List<ProveedorResponse> proveedores = listProveedoresService.list(nombre, ciudad, activo);
        return ResponseEntity.ok(proveedores);
    }
}