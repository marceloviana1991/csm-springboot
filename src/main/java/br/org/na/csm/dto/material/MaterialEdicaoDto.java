package br.org.na.csm.dto.material;

public record MaterialEdicaoDto(
        String nome,
        Float preco,
        Integer quantidadeEmEstoque
) {
}
