package com.javaRz.padaria.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("/upload")
@CrossOrigin(origins = "*")
public class ProdutoUploadController {

    private static final String UPLOAD_DIR = "C:/padaria/uploads/";

    @PostMapping("/imagem")
    public ResponseEntity<?> uploadImagem(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Arquivo vazio");
            }

            // Gerar nome único
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path caminho = Paths.get(UPLOAD_DIR + fileName);

            // Criar diretório se não existir
            File diretorio = new File(UPLOAD_DIR);
            if (!diretorio.exists()) {
                diretorio.mkdirs();
            }

            // Salvar imagem
            Files.write(caminho, file.getBytes());

            // URL final da imagem
            String url = "http://localhost:8081/uploads/" + fileName;

            return ResponseEntity.ok(url);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao enviar imagem: " + e.getMessage());
        }
    }
}
