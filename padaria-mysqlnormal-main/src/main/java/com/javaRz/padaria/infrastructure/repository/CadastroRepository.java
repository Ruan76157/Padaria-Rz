package com.javaRz.padaria.infrastructure.repository;

import com.javaRz.padaria.infrastructure.entitys.Cadastro;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CadastroRepository extends MongoRepository<Cadastro, String> {
}
