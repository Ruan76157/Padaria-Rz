package com.javaRz.padaria.Controller;


import com.javaRz.padaria.business.ProdutoService;
import com.javaRz.padaria.infrastructure.entitys.Produto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
    @RequestMapping("/produtos")
    @RequiredArgsConstructor

    public class ProdutoController {

        private final ProdutoService produtoService;

        @PostMapping
        public ResponseEntity<Produto> salvar(@RequestBody Produto produto) {
            Produto salvo = produtoService.salvar(produto);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        }

        @GetMapping
        public ResponseEntity<List<Produto>> listar() {
            return ResponseEntity.ok(produtoService.listar());
        }
    }



