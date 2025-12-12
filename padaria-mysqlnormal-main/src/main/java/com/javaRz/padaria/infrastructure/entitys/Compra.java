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
@Document(collection = "compras")
public class Compra {

    @Id
    private String id;

    private String usuarioCpf;
    private List<String> produtosIds;
    private LocalDateTime dataCompra;
    private Double valorTotal;
}
