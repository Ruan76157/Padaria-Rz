package com.javaRz.padaria.infrastructure.entitys;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document(collection = "cadastro")
public class Cadastro {

    @Id
    private Integer id;

    private String nome;
    private Integer idade;
}
