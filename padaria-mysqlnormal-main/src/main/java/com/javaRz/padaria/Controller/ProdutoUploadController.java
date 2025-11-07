package com.javaRz.padaria.Controller;


import com.javaRz.padaria.dto.ProdutoRequest;
import com.javaRz.padaria.infrastructure.entitys.Padaria;
import com.javaRz.padaria.business.PadariaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/produtos") // NOVO endpoint para upload
@RequiredArgsConstructor
public class ProdutoUploadController {

    private final PadariaService padariaService;

    // NOVO: Endpoint para criar produto com imagem
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> criarProdutoComImagem(@ModelAttribute ProdutoRequest request) {
        try {
            Padaria produtoSalvo = padariaService.criarProdutoComImagem(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
        } catch (Exception e) {
            log.error("Erro ao criar produto: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar produto: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Padaria>> listarTodosProdutos() {
        return ResponseEntity.ok(padariaService.listarTodos());
    }
}