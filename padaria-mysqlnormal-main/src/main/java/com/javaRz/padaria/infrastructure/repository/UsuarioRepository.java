package com.javaRz.padaria.infrastructure.repository;

import com.javaRz.padaria.infrastructure.entitys.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UsuarioRepository extends MongoRepository<Usuario, Integer> {

    Optional<Usuario> findByCpf(String cpf);
    void deleteByCpf(String cpf);
}
