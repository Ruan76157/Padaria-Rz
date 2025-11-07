package com.javaRz.padaria.Controller;

import com.javaRz.padaria.infrastructure.entitys.Usuario;
import com.javaRz.padaria.business.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/cadastro")
@RequiredArgsConstructor
public class CadastroController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<String> cadastrarUsuario(@RequestBody Usuario usuario) {
        try {
            usuarioService.salvarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Usuário cadastrado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao cadastrar: " + e.getMessage());
        }
    }
}
