package com.StoreSecurity.StoreSecurity.web.controller;

import com.StoreSecurity.StoreSecurity.dto.TicketResponseDTO;
import com.StoreSecurity.StoreSecurity.dto.VentaRequestDTO;
import com.StoreSecurity.StoreSecurity.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @PostMapping("/realizar")
    public ResponseEntity<?> realizarVenta(@RequestBody VentaRequestDTO pedido) {
        try {
            // Llamamos al servicio para procesar la venta
            TicketResponseDTO ticket = ventaService.realizarVenta(pedido);

            // Retornamos el ticket con un estado 201 (Created)
            return new ResponseEntity<>(ticket, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            // Si el error es por falta de stock o datos inválidos (Error 400)
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            // Si es un error inesperado en el servidor (Error 500)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la venta: " + e.getMessage());
        }
    }
}
