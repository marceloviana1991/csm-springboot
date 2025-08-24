package br.org.na.csm.dto.pedido;

import br.org.na.csm.model.ItemPedido;

public record ItemPedidoListagemDto(
        Long id,
        String nome,
        Integer quantidade,
        Float valorTotal
) {
    public ItemPedidoListagemDto(ItemPedido item) {
        this(
                item.getId(),
                item.getMaterial().getNome(),
                item.getQuantidade(),
                item.getValorTotal()
        );
    }
}
