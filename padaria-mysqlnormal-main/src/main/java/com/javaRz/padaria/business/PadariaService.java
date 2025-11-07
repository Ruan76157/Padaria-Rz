package com.javaRz.padaria.business;


import com.javaRz.padaria.dto.ProdutoRequest;
import com.javaRz.padaria.infrastructure.entitys.Padaria;
import com.javaRz.padaria.infrastructure.repository.PadariaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PadariaService {

    private final PadariaRepository padariaRepository;
    private final String UPLOAD_DIR = "uploads/";

    // MÉTODO ANTIGO (mantido para compatibilidade)
    public Padaria salvarPadaria(Padaria padaria) {
        return padariaRepository.save(padaria);
    }

    // NOVO MÉTODO para upload de imagem
    public Padaria criarProdutoComImagem(ProdutoRequest request) throws IOException {
        String imagemUrl = null;

        // Salvar imagem se existir
        if (request.getImagem() != null && !request.getImagem().isEmpty()) {
            imagemUrl = salvarImagem(request.getImagem());
            log.info("Imagem salva: {}", imagemUrl);
        }

        // Criar produto
        Padaria produto = Padaria.builder()
                .nome(request.getNome())
                .preco(request.getPreco())
                .quantidade(request.getQuantidade())
                .descricao(request.getDescricao())
                .categoria(request.getCategoria())
                .imagemUrl(imagemUrl)
                .build();

        return padariaRepository.save(produto);
    }

    private String salvarImagem(MultipartFile imagem) throws IOException {
        // Criar diretório se não existir
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Gerar nome único
        String nomeOriginal = imagem.getOriginalFilename();
        String extensao = "";
        if (nomeOriginal != null && nomeOriginal.contains(".")) {
            extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
        }
        String nomeArquivo = UUID.randomUUID().toString() + extensao;

        // Salvar arquivo
        Path filePath = uploadPath.resolve(nomeArquivo);
        Files.copy(imagem.getInputStream(), filePath);

        return "/uploads/" + nomeArquivo;
    }

    // MÉTODOS EXISTENTES (atualizados para String id)
    public Padaria buscarPorId(String id) {
        return padariaRepository.findById(Integer.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Item de padaria não encontrado com Id: " + id));
    }

    public List<Padaria> listarTodos() {
        return padariaRepository.findAll();
    }

    public Padaria atualizarPadaria(String id, Padaria novaPadaria) {
        Padaria existente = padariaRepository.findById(Integer.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Item de padaria não encontrado com Id: " + id));

        existente.setNome(novaPadaria.getNome());
        existente.setPreco(novaPadaria.getPreco());
        existente.setQuantidade(novaPadaria.getQuantidade());
        existente.setDescricao(novaPadaria.getDescricao());
        existente.setCategoria(novaPadaria.getCategoria());

        // Atualiza imagem URL apenas se for fornecida
        if (novaPadaria.getImagemUrl() != null) {
            existente.setImagemUrl(novaPadaria.getImagemUrl());
        }

        return padariaRepository.save(existente);
    }

    public void deletarPadaria(String id) {
        if (!padariaRepository.existsById(Integer.valueOf(id))) {
            throw new RuntimeException("Item de padaria não encontrado com Id: " + id);
        }
        padariaRepository.deleteById(Integer.valueOf(id));
    }

    public Padaria ajustarEstoque(String id, int quantidade) {
        Padaria produto = padariaRepository.findById(Integer.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com id: " + id));

        int novoEstoque = produto.getQuantidade() + quantidade;
        if (novoEstoque < 0) novoEstoque = 0;

        produto.setQuantidade(novoEstoque);
        return padariaRepository.save(produto);
    }
}