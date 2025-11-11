package com.unibague.inventario;

import com.unibague.inventario.entity.Proveedor;
import com.unibague.inventario.entity.Semilla;
import com.unibague.inventario.repository.ProveedorRepository;
import com.unibague.inventario.repository.SemillaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

/**
 * Clase principal que arranca la aplicación Spring Boot y precarga algunos
 * datos de ejemplo en la base de datos para facilitar las pruebas. El
 * sufijo 'Db' diferencia este prototipo del anterior que almacenaba los
 * datos en memoria.
 */
@SpringBootApplication
public class InventarioDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventarioDbApplication.class, args);
    }

    /**
     * Precarga proveedores y semillas de ejemplo si no existen. Esto
     * simplifica las pruebas manuales sin necesidad de crear todos los
     * registros mediante los clientes.
     */
    @Bean
    public CommandLineRunner dataLoader(ProveedorRepository proveedorRepository,
                                        SemillaRepository semillaRepository) {
        return args -> {
            // Precargar proveedores
            if (!proveedorRepository.existsById("900123456")) {
                proveedorRepository.save(new Proveedor(
                        "900123456", "AgroUni", "Bogotá", "3001234567",
                        LocalDateTime.now().minusDays(10), true));
            }
            if (!proveedorRepository.existsById("900654321")) {
                proveedorRepository.save(new Proveedor(
                        "900654321", "Semillas del Norte", "Medellín", "3017654321",
                        LocalDateTime.now().minusDays(7), true));
            }
            // Precargar semillas
            if (!semillaRepository.existsById("S001")) {
                semillaRepository.save(new Semilla(
                        "S001", "Maíz amarillo", 1200.0, 50,
                        "Cereal", 92.5,
                        LocalDateTime.now().minusDays(5),
                        proveedorRepository.findById("900123456").get()
                ));
            }
            if (!semillaRepository.existsById("S002")) {
                semillaRepository.save(new Semilla(
                        "S002", "Frijol rojo", 1500.0, 30,
                        "Leguminosa", 85.0,
                        LocalDateTime.now().minusDays(2),
                        proveedorRepository.findById("900654321").get()
                ));
            }
        };
    }
}