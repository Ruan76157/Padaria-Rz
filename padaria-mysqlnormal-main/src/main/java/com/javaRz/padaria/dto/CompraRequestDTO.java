package com.javaRz.padaria.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompraRequestDTO {
    private String usuarioCpf;       // CPF do usuário
    private List<String> produtosIds; // IDs dos produtos
    private Double valorTotal;        // Opcional
}
