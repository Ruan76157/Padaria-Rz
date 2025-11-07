package com.javaRz.padaria.Controller;

import com.javaRz.padaria.infrastructure.entitys.Padaria;
import com.javaRz.padaria.business.PadariaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/padaria")
@RequiredArgsConstructor
public class PadariaController {

    private final PadariaService padariaService;

    @PostMapping
    public ResponseEntity<Void> salvarPadaria(@RequestBody Padaria padaria) {
        padariaService.salvarPadaria(padaria);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Padaria> buscarPorId(@PathVariable String id) { // Mude para String
        Padaria padaria = padariaService.buscarPorId(id);
        return ResponseEntity.ok(padaria);
    }

    @GetMapping
    public ResponseEntity<List<Padaria>> listarTodos() {
        return ResponseEntity.ok(padariaService.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarPadaria(@PathVariable String id, @RequestBody Padaria padaria) { // Mude para String
        padariaService.atualizarPadaria(id, padaria);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable String id) { // Mude para String
        padariaService.deletarPadaria(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/ajustar-estoque")
    public ResponseEntity<Void> ajustarEstoque(@PathVariable String id, @RequestParam int quantidade) { // Mude para String
        padariaService.ajustarEstoque(id, quantidade);
        return ResponseEntity.noContent().build();
    }
}