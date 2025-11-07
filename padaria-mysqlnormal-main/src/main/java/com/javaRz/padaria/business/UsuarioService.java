package com.javaRz.padaria.business;

import com.javaRz.padaria.infrastructure.entitys.SequenceGeneratorService;
import com.javaRz.padaria.infrastructure.entitys.Usuario;
import com.javaRz.padaria.infrastructure.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final SequenceGeneratorService sequenceGeneratorService;

    public void salvarUsuario(Usuario usuario) {
        // Se o usuário ainda não tem ID, gera um novo
        if (usuario.getId() == null) {
            usuario.setId(sequenceGeneratorService.getSequenceNumber(Usuario.SEQUENCE_NAME));
        }
        repository.save(usuario);
    }

    public Usuario buscarUsuarioPorCpf(String cpf) {
        return repository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("CPF não encontrado."));
    }

    public void deletarUsuarioPorCpf(String cpf) {
        repository.deleteByCpf(cpf);
    }

    public void atualizarUsuarioPorCpf(String cpf, Usuario usuario) {
        Usuario usuarioEntity = repository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        if (usuario.getNome() != null) usuarioEntity.setNome(usuario.getNome());
        if (usuario.getEmail() != null) usuarioEntity.setEmail(usuario.getEmail());
        if (usuario.getTelefone() != null) usuarioEntity.setTelefone(usuario.getTelefone());
        if (usuario.getDataNascimento() != null) usuarioEntity.setDataNascimento(usuario.getDataNascimento());

        repository.save(usuarioEntity);
    }

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }
}
