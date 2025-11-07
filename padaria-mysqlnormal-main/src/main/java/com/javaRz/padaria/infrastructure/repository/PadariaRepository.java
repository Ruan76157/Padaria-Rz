package com.javaRz.padaria.infrastructure.repository;

import com.javaRz.padaria.infrastructure.entitys.Padaria;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PadariaRepository extends MongoRepository<Padaria, Integer> {
    List<Padaria> findAllById(List<String> produtosIds);

    List<Padaria> findAllByIdIn(List<String> ids);
}
