package com.unibague.inventario.dto;

import java.util.List;

public record Top2SemillasResponse(
    ProveedorResponse proveedor,
    List<SemillaResponse> semillas
) {}
