package br.org.na.csm.dto.material;

public record MaterialCadastroRequestDto(
        String nome,
        Float preco,
        Integer quantidadeEmEstoque,
        Long grupoId
) {
}
