package com.javaRz.padaria.dto;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProdutoRequest {
    private String nome;
    private Double preco;
    private Integer quantidade;
    private String descricao;
    private String categoria;
    private MultipartFile imagem;
}