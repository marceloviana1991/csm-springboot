package br.org.na.csm.dto.pedido;

import java.util.List;

public record PedidoCompraDto(
        List<ItemPedidoDto> itens
) {
}
