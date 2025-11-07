package com.javaRz.padaria.infrastructure.entitys;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "compras") // ✅ Nome da coleção Mongo
public class Compra {

    @Id
    private String id; // Mongo usa String (ObjectId)

    private String usuarioCpf;
    private List<String> produtosIds; // Armazena apenas os IDs dos produtos
    private LocalDateTime dataCompra;
    private Double valorTotal;
}
