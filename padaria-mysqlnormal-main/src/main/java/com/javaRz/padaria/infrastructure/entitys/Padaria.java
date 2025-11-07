package com.javaRz.padaria.infrastructure.entitys;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "padarias")
public class Padaria {
        @Id
        private String id; // MUDEI para String - MongoDB gera automaticamente
        private String nome;
        private Double preco;
        private Integer quantidade;
        private String descricao;
        private String categoria;
        private String imagemUrl;

        @Transient
        private transient MultipartFile imagemFile;
}