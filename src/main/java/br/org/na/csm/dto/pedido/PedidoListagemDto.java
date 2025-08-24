package br.org.na.csm.dto.pedido;

import br.org.na.csm.model.ItemPedido;
import br.org.na.csm.model.Pedido;

import java.util.List;

public record PedidoListagemDto(
        Long id,
        String data,
        String tipo,
        Boolean confirmado,
        List<ItemPedidoListagemDto> itens
) {
    public PedidoListagemDto(Pedido pedido, List<ItemPedido> itens) {
        this(
                pedido.getId(),
                pedido.getData().toString(),
                pedido.getTipo().toString(),
                pedido.getConfirmado(),
                itens.stream().map(ItemPedidoListagemDto::new).toList()
        );
    }
}
