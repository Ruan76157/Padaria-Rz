package com.javaRz.padaria.business;


import com.javaRz.padaria.infrastructure.entitys.Produto;
import com.javaRz.padaria.infrastructure.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
    @RequiredArgsConstructor
    public class ProdutoService {

        private final ProdutoRepository repository;

        public Produto salvar(Produto produto) {
            return repository.save(produto);
        }

        public List<Produto> listar() {
            return repository.findAll();
        }
    }






