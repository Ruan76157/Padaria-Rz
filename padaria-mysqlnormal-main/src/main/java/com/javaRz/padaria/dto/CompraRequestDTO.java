package com.javaRz.padaria.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompraRequestDTO {
    private String usuarioCpf;
    private List<String> produtosIds;
    private Double valorTotal;
}
