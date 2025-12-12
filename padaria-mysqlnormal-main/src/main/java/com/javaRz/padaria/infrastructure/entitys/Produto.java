package com.javaRz.padaria.infrastructure.entitys;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "produtos")
public class Produto {

    @Id
    private String id;

    private String nome;
    private String descricao;
    private double preco;
    private int estoque;
    private String imagem; // URL da imagem
}
