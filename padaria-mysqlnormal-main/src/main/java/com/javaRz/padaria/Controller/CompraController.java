package com.javaRz.padaria.Controller;

import com.javaRz.padaria.infrastructure.entitys.Compra;
import com.javaRz.padaria.dto.CompraRequestDTO;
import com.javaRz.padaria.business.CompraService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/compras")
@RequiredArgsConstructor
public class CompraController {

    private final CompraService compraService;

    @PostMapping
    public ResponseEntity<?> criarCompra(@RequestBody CompraRequestDTO dto) {
        try {
            log.info("📥 Requisição recebida: {}", dto);
            Compra novaCompra = compraService.criarCompraComDTO(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaCompra);
        } catch (RuntimeException e) {
            log.error("❌ Erro ao criar compra: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping
    public ResponseEntity<List<Compra>> listarTodas() {
        return ResponseEntity.ok(compraService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(compraService.buscarPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/usuario/{cpf}")
    public ResponseEntity<List<Compra>> buscarPorUsuario(@PathVariable String cpf) {
        return ResponseEntity.ok(compraService.buscarPorUsuarioCpf(cpf));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable String id) {
        try {
            compraService.deletarCompra(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
