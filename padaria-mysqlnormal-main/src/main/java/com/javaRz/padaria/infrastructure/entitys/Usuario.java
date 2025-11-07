package com.javaRz.padaria.infrastructure.entitys;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "usuarios")
public class Usuario {

    @Transient
    public static final String SEQUENCE_NAME = "usuario_sequence";

    @Id

    private Integer id;

    private String nome;
    private String cpf;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private String dataNascimento;

    private String email;
    private String telefone;
}
