package br.org.na.csm.dto.material;

import br.org.na.csm.model.Material;

public record MaterialCadastroResponseDto(
        Long id,
        String nome,
        Float preco,
        Integer quantidadeEmEstoque,
        String codigoDaImagem,
        Long grupoId
) {
    public MaterialCadastroResponseDto(Material material) {
        this(
                material.getId(),
                material.getNome(),
                material.getPreco(),
                material.getQuantidadeEmEstoque(),
                material.getCodigoDaImagem(),
                material.getGrupo().getId()
        );
    }
}
