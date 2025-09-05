package br.org.na.csm.dto.pedido;

import br.org.na.csm.model.ItemPedido;
import br.org.na.csm.model.Pedido;

import java.util.List;

public record PedidoListagemDto(
        Long id,
        String cliente,
        String telefone,
        String data,
        Boolean confirmado,
        List<ItemPedidoListagemDto> itens
) {
    public PedidoListagemDto(Pedido pedido, List<ItemPedido> itens) {
        this(
                pedido.getId(),
                pedido.getCliente(),
                pedido.getTelefone(),
                pedido.getData().toString(),
                pedido.getConfirmado(),
                itens.stream().map(ItemPedidoListagemDto::new).toList()
        );
    }
}
