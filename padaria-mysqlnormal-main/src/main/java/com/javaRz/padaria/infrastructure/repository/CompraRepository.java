package com.javaRz.padaria.infrastructure.repository;

import com.javaRz.padaria.infrastructure.entitys.Compra;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraRepository extends MongoRepository<Compra, String> {
    List<Compra> findByUsuarioCpf(String usuarioCpf);
}
