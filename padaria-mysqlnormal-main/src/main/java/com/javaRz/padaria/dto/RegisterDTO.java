package com.javaRz.padaria.dto;

import java.time.LocalDate;

public record RegisterDTO(
        String nome,
        String cpf,
        LocalDate dataNascimento,
        String email,
        String telefone,
        String senha
) {
}