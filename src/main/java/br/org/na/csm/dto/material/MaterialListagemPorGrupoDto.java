package br.org.na.csm.dto.material;

import br.org.na.csm.model.Material;

public record MaterialListagemPorGrupoDto(
        Long id,
        String nome,
        Float preco,
        Integer quantidadeEmEstoque,
        Long grupoId
) {
    public MaterialListagemPorGrupoDto(Material material) {
        this(
                material.getId(),
                material.getNome(),
                material.getPreco(),
                material.getQuantidadeEmEstoque(),
                material.getGrupo().getId()
        );
    }
}
