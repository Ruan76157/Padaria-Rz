package com.javaRz.padaria.infrastructure.repository;

import com.javaRz.padaria.infrastructure.entitys.Produto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends MongoRepository<Produto, String> {
}

