package com.javaRz.padaria.business;

import com.javaRz.padaria.dto.CompraRequestDTO;
import com.javaRz.padaria.infrastructure.entitys.Compra;
import com.javaRz.padaria.infrastructure.repository.CompraRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompraService {

    private final CompraRepository compraRepository;

    public Compra criarCompraComDTO(CompraRequestDTO dto) {
        log.info("📦 Criando compra (MongoDB) com DTO: {}", dto);

        // Calcula o valor total se não vier
        Double valorTotal = dto.getValorTotal();
        if (valorTotal == null || valorTotal == 0) {
            log.warn("⚠️ Valor total não informado, definindo como 0");
            valorTotal = 0.0;
        }

        Compra compra = Compra.builder()
                .usuarioCpf(dto.getUsuarioCpf())
                .produtosIds(dto.getProdutosIds())
                .dataCompra(LocalDateTime.now())
                .valorTotal(valorTotal)
                .build();

        Compra compraSalva = compraRepository.save(compra);
        log.info("✅ Compra salva no MongoDB! ID: {}", compraSalva.getId());
        return compraSalva;
    }

    public List<Compra> listarTodas() {
        return compraRepository.findAll();
    }

    public Compra buscarPorId(String id) {
        return compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra não encontrada"));
    }

    public List<Compra> buscarPorUsuarioCpf(String cpf) {
        return compraRepository.findByUsuarioCpf(cpf);
    }

    public void deletarCompra(String id) {
        if (!compraRepository.existsById(id)) {
            throw new RuntimeException("Compra não encontrada");
        }
        compraRepository.deleteById(id);
        log.info("🗑️ Compra deletada com sucesso: {}", id);
    }
}
