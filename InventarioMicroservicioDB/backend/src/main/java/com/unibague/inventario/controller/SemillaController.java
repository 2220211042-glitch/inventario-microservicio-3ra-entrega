package com.unibague.inventario.controller;

import com.unibague.inventario.dto.SemillaRequest;
import com.unibague.inventario.dto.SemillaResponse;
import com.unibague.inventario.service.CreateSemillaService;
import com.unibague.inventario.service.DeleteSemillaService;
import com.unibague.inventario.service.GetSemillaService;
import com.unibague.inventario.service.ListSemillasService;
import com.unibague.inventario.service.UpdateSemillaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar semillas. Expone los casos de uso de
 * creación, consulta, actualización, eliminación y listados con filtros.
 */
@RestController
@RequestMapping("/api/v1/semillas")
public class SemillaController {

    private final CreateSemillaService createSemillaService;
    private final GetSemillaService getSemillaService;
    private final UpdateSemillaService updateSemillaService;
    private final DeleteSemillaService deleteSemillaService;
    private final ListSemillasService listSemillasService;

    public SemillaController(CreateSemillaService createSemillaService,
                             GetSemillaService getSemillaService,
                             UpdateSemillaService updateSemillaService,
                             DeleteSemillaService deleteSemillaService,
                             ListSemillasService listSemillasService) {
        this.createSemillaService = createSemillaService;
        this.getSemillaService = getSemillaService;
        this.updateSemillaService = updateSemillaService;
        this.deleteSemillaService = deleteSemillaService;
        this.listSemillasService = listSemillasService;
    }

    /**
     * Crea una nueva semilla.
     *
     * @param request DTO con los datos de la semilla
     * @return respuesta con la semilla creada
     */
    @PostMapping
    public ResponseEntity<SemillaResponse> create(@Valid @RequestBody SemillaRequest request) {
        SemillaResponse response = createSemillaService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtiene una semilla por su código.
     *
     * @param codigo código de la semilla
     * @return semilla encontrada
     */
    @GetMapping("/{codigo}")
    public ResponseEntity<SemillaResponse> getByCodigo(@PathVariable String codigo) {
        SemillaResponse response = getSemillaService.getByCodigo(codigo);
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza una semilla existente.
     *
     * @param codigo  identificador de la semilla a actualizar
     * @param request DTO con los nuevos datos
     * @return semilla actualizada
     */
    @PutMapping("/{codigo}")
    public ResponseEntity<SemillaResponse> update(@PathVariable String codigo,
                                                  @Valid @RequestBody SemillaRequest request) {
        // Validación: el código de la URL debe coincidir con el del body
        if (!codigo.equals(request.getCodigo())) {
            throw new IllegalArgumentException("El código de la URL no coincide con el del cuerpo de la solicitud");
        }
        SemillaResponse response = updateSemillaService.update(codigo, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Elimina una semilla existente.
     *
     * @param codigo código de la semilla a eliminar
     * @return respuesta sin contenido
     */
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> delete(@PathVariable String codigo) {
        deleteSemillaService.delete(codigo);
        return ResponseEntity.noContent().build();
    }

    /**
     * Lista semillas aplicando filtros opcionales. Los parámetros no son
     * obligatorios; si no se proporcionan, se devuelven todas las semillas.
     *
     * @param tipo           tipo de semilla
     * @param germinacionMin porcentaje mínimo de germinación
     * @param desde          fecha inicial en formato ISO-8601 (YYYY-MM-DD'T'HH:MM:SS)
     * @param hasta          fecha final en formato ISO-8601 (YYYY-MM-DD'T'HH:MM:SS)
     * @return lista de semillas que cumplen con los filtros
     */
    @GetMapping
    public ResponseEntity<List<SemillaResponse>> list(@RequestParam Optional<String> tipo,
                                                      @RequestParam(name = "germinacionMin") Optional<Double> germinacionMin,
                                                      @RequestParam Optional<String> desde,
                                                      @RequestParam Optional<String> hasta) {
        List<SemillaResponse> semillas = listSemillasService.list(tipo, germinacionMin, desde, hasta);
        return ResponseEntity.ok(semillas);
    }

    /**
     * Devuelve las dos semillas más recientes de un proveedor.
     *
     * @param nit identificador del proveedor
     * @return lista de hasta dos semillas más recientes
     */
    @GetMapping("/proveedor/{nit}/top2")
    public ResponseEntity<List<SemillaResponse>> top2ByProveedor(@PathVariable String nit) {
        List<SemillaResponse> semillas = listSemillasService.top2ByProveedor(nit);
        return ResponseEntity.ok(semillas);
    }
}