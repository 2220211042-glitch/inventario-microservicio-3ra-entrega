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

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/semillas")
@CrossOrigin(origins = "*")
public class SemillaController {

    private final CreateSemillaService createService;
    private final GetSemillaService getService;
    private final UpdateSemillaService updateService;
    private final DeleteSemillaService deleteService;
    private final ListSemillasService listService;

    public SemillaController(CreateSemillaService createService,
                             GetSemillaService getService,
                             UpdateSemillaService updateService,
                             DeleteSemillaService deleteService,
                             ListSemillasService listService) {
        this.createService = createService;
        this.getService = getService;
        this.updateService = updateService;
        this.deleteService = deleteService;
        this.listService = listService;
    }

    @PostMapping
    public ResponseEntity<SemillaResponse> create(@Valid @RequestBody SemillaRequest req) {
        SemillaResponse saved = createService.create(req);
        return ResponseEntity.created(URI.create("/api/v1/semillas/" + saved.getCodigo())).body(saved);
    }

    @GetMapping("/{codigo}")
    public SemillaResponse get(@PathVariable String codigo) {
        return getService.get(codigo); // <-- método 'get' en el servicio
    }

    @PutMapping("/{codigo}")
    public SemillaResponse update(@PathVariable String codigo, @Valid @RequestBody SemillaRequest req) {
        return updateService.update(codigo, req);
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String codigo) {
        deleteService.delete(codigo);
    }

    // NOTA: el servicio espera Optional<String> para 'germinacionMin'
    @GetMapping
    public List<SemillaResponse> list(@RequestParam Optional<String> tipo,
                                      @RequestParam(name = "germinacionMin") Optional<String> germMin,
                                      @RequestParam Optional<String> desde,
                                      @RequestParam Optional<String> hasta) {
        return listService.list(tipo, germMin, desde, hasta);
    }
}
