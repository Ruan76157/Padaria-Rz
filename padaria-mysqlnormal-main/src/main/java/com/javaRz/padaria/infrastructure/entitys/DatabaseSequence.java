package com.javaRz.padaria.infrastructure.entitys;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "database_sequences")
public class DatabaseSequence {
    @Id
    private String id;
    private Integer seq;
}
