package com.javaRz.padaria.Controller;

import com.javaRz.padaria.infrastructure.entitys.Usuario;
import com.javaRz.padaria.business.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Void> salvarUsuario(@RequestBody Usuario usuario) {
        usuarioService.salvarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }


    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Usuario> buscarUsuarioPorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorCpf(cpf));
    }


    @DeleteMapping("/cpf/{cpf}")
    public ResponseEntity<Void> deletarUsuarioPorCpf(@PathVariable String cpf) {
        usuarioService.deletarUsuarioPorCpf(cpf);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/cpf/{cpf}")
    public ResponseEntity<Void> atualizarUsuarioPorCpf(@PathVariable String cpf,
                                                       @RequestBody Usuario usuario) {
        usuarioService.atualizarUsuarioPorCpf(cpf, usuario);
        return ResponseEntity.ok().build();
    }
}