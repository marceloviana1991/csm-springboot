package br.org.na.csm.dto.pedido;

public record ItemPedidoDto(
        Long materialId,
        Integer quantidade,
        Float valorTotal
) {
}
