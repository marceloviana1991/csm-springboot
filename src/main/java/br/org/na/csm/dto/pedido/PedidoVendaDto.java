package br.org.na.csm.dto.pedido;

import java.util.List;

public record PedidoVendaDto(
        String cliente,
        String telefone,
        List<ItemPedidoDto> itens
) {
}
